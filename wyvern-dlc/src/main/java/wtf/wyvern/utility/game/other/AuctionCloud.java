package wtf.wyvern.utility.game.other;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Клиент облачной базы цен аукциона.
 *
 * Протокол v1 инкрементальный: тянем только записи, изменившиеся после нашего курсора,
 * и отправляем только те ключи, которые действительно поменялись локально. Полный дамп
 * ходит по сети один раз - при первом запуске с пустым курсором.
 */
public final class AuctionCloud {

    private static final Gson GSON = new Gson();

    private static final int MAX_PULL_PAGES = 32;
    private static final int MAX_PUSH_BATCH = 2000;
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    private final String baseUrl;
    private final String apiKey;
    private final long pullIntervalSeconds;
    private final long pushDebounceSeconds;

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(CONNECT_TIMEOUT)
            .executor(Executors.newVirtualThreadPerTaskExecutor())
            .build();

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "wyvern-auction-cloud");
                thread.setDaemon(true);
                return thread;
            });

    private final AtomicBoolean pushPending = new AtomicBoolean(false);
    // Всё, что накопилось между дебаунс-окнами, уходит одним запросом.
    private final Map<String, Long> pushBuffer = new HashMap<>();

    private volatile long cursor;
    private volatile boolean online;
    private volatile String lastError = "";

    public AuctionCloud(AuctionCloudConfig config) {
        this.baseUrl = stripTrailingSlash(config.baseUrl());
        this.apiKey = config.apiKey();
        this.pullIntervalSeconds = config.pullSeconds();
        this.pushDebounceSeconds = config.pushSeconds();
    }

    public void start(Consumer<Map<String, Long>> onPull) {
        scheduler.execute(() -> pull(onPull));
        scheduler.scheduleAtFixedRate(() -> pull(onPull),
                pullIntervalSeconds, pullIntervalSeconds, TimeUnit.SECONDS);
    }

    public void schedulePush(Map<String, Long> changed) {
        if (changed.isEmpty()) {
            return;
        }
        synchronized (pushBuffer) {
            pushBuffer.putAll(changed);
        }
        if (pushPending.compareAndSet(false, true)) {
            scheduler.schedule(() -> {
                pushPending.set(false);
                flushPush();
            }, pushDebounceSeconds, TimeUnit.SECONDS);
        }
    }

    public boolean isOnline() {
        return online;
    }

    public String getLastError() {
        return lastError;
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }

    private void flushPush() {
        Map<String, Long> pending;
        synchronized (pushBuffer) {
            if (pushBuffer.isEmpty()) {
                return;
            }
            pending = new HashMap<>(pushBuffer);
            pushBuffer.clear();
        }

        // Сервер отвергает батч целиком, если он больше лимита, поэтому режем сами.
        Map<String, Long> chunk = new HashMap<>();
        for (Map.Entry<String, Long> entry : pending.entrySet()) {
            chunk.put(entry.getKey(), entry.getValue());
            if (chunk.size() >= MAX_PUSH_BATCH) {
                sendChunk(chunk);
                chunk = new HashMap<>();
            }
        }
        if (!chunk.isEmpty()) {
            sendChunk(chunk);
        }
    }

    private void sendChunk(Map<String, Long> batch) {
        JsonObject prices = new JsonObject();
        for (Map.Entry<String, Long> entry : batch.entrySet()) {
            prices.addProperty(entry.getKey(), entry.getValue());
        }
        JsonObject body = new JsonObject();
        body.add("prices", prices);

        try {
            HttpRequest request = authorized(URI.create(baseUrl + "/api/v1/prices"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(body), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<Void> response = http.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() == 200) {
                markOnline();
            } else {
                markOffline("push HTTP " + response.statusCode());
                requeue(batch);
            }
        } catch (Exception exception) {
            markOffline("push " + exception.getClass().getSimpleName());
            requeue(batch);
        }
    }

    // Неудачный пуш не должен терять цены: возвращаем их в буфер, но не затираем
    // более свежие значения, которые успели прийти со сканера, пока мы ходили в сеть.
    private void requeue(Map<String, Long> batch) {
        synchronized (pushBuffer) {
            for (Map.Entry<String, Long> entry : batch.entrySet()) {
                pushBuffer.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }

    private void pull(Consumer<Map<String, Long>> onPull) {
        // Сервер режет дельту по лимиту строк; при первом пулле с нулевым курсором
        // полная база приходит несколькими страницами подряд.
        for (int page = 0; page < MAX_PULL_PAGES; page++) {
            if (!pullOnce(onPull)) {
                return;
            }
        }
    }

    /** Возвращает true, если ответ был обрезан и надо тянуть следующую страницу. */
    private boolean pullOnce(Consumer<Map<String, Long>> onPull) {
        try {
            URI uri = URI.create(baseUrl + "/api/v1/prices?since=" + cursor);
            HttpRequest request = authorized(uri).GET().build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                markOffline("pull HTTP " + response.statusCode());
                return false;
            }
            PriceSnapshot snapshot = GSON.fromJson(response.body(), PriceSnapshot.class);
            markOnline();
            if (snapshot == null) {
                return false;
            }
            if (snapshot.prices != null && !snapshot.prices.isEmpty()) {
                onPull.accept(snapshot.prices);
            }
            // Курсор двигаем только после успешной обработки, иначе дельта потеряется навсегда.
            if (snapshot.cursor <= cursor) {
                return false;
            }
            cursor = snapshot.cursor;
            return snapshot.truncated;
        } catch (Exception exception) {
            markOffline("pull " + exception.getClass().getSimpleName());
            return false;
        }
    }

    private HttpRequest.Builder authorized(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("X-API-Key", apiKey)
                .timeout(REQUEST_TIMEOUT);
    }

    private void markOnline() {
        online = true;
        lastError = "";
    }

    private void markOffline(String reason) {
        online = false;
        lastError = reason;
    }

    private static String stripTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    private static final class PriceSnapshot {
        @SerializedName("cursor")
        private long cursor;
        @SerializedName("truncated")
        private boolean truncated;
        @SerializedName("prices")
        private Map<String, Long> prices;
    }
}
