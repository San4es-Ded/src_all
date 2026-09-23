package ru.prism.utils.lyrics;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.CRC32;

public final class LyricsService {

    private static final Path DIRECTORY = Path.of("C:/prism/client1_21_11/lyrics");
    private static final String USER_AGENT = "PrismClient/1.0";
    private static final long[] RETRY_DELAYS = {30_000L, 120_000L, 600_000L, 3_600_000L};
    private static final Map<String, Lyrics> MEMORY = new ConcurrentHashMap<>();
    private static final Map<String, Long> FAILURES = new ConcurrentHashMap<>();
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "prism-lyrics");
        thread.setDaemon(true);
        return thread;
    });
    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(4))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static volatile Lyrics current = Lyrics.EMPTY;
    private static volatile String currentKey = "";

    private LyricsService() {
    }

    public static Lyrics get() {
        return current;
    }

    public static void reset() {
        current = Lyrics.EMPTY;
        currentKey = "";
    }

    public static void request(String title, String artist, long durationMillis) {
        if (title == null || title.isBlank()) {
            reset();
            return;
        }
        String key = key(title, artist, durationMillis);
        if (key.equals(currentKey)) {
            return;
        }
        currentKey = key;
        current = Lyrics.EMPTY;

        Long failedAt = FAILURES.get(key);
        if (failedAt != null && System.currentTimeMillis() - failedAt < retryDelay(key)) {
            return;
        }

        Lyrics cached = MEMORY.get(key);
        if (cached != null) {
            current = cached;
            return;
        }

        EXECUTOR.submit(() -> {
            Lyrics lyrics = loadFromDisk(key);
            if (lyrics == null) {
                lyrics = fetch(title, artist, durationMillis);
                if (lyrics != null && lyrics.isSynced()) {
                    saveToDisk(key, lyrics);
                }
            }
            if (lyrics == null) {
                FAILURES.put(key, System.currentTimeMillis());
                return;
            }
            FAILURES.remove(key);
            MEMORY.put(key, lyrics);
            if (key.equals(currentKey)) {
                current = lyrics;
            }
        });
    }

    private static long retryDelay(String key) {
        int attempts = 0;
        for (Map.Entry<String, Long> entry : FAILURES.entrySet()) {
            if (entry.getKey().equals(key)) {
                attempts++;
            }
        }
        int index = Math.min(attempts, RETRY_DELAYS.length - 1);
        return RETRY_DELAYS[index];
    }

    private static String key(String title, String artist, long durationMillis) {
        CRC32 crc = new CRC32();
        crc.update((title + "|" + artist + "|" + (durationMillis / 1000L) + "|v1").getBytes(StandardCharsets.UTF_8));
        return Long.toHexString(crc.getValue());
    }

    private static Path file(String key) {
        return DIRECTORY.resolve(key + ".lrc");
    }

    private static Lyrics loadFromDisk(String key) {
        try {
            Path path = file(key);
            if (!Files.isRegularFile(path)) {
                return null;
            }
            Lyrics lyrics = LyricsParser.parse(Files.readString(path, StandardCharsets.UTF_8));
            return lyrics.isEmpty() ? null : lyrics;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void saveToDisk(String key, Lyrics lyrics) {
        try {
            Files.createDirectories(DIRECTORY);
            StringBuilder builder = new StringBuilder();
            for (LyricLine line : lyrics.getLines()) {
                long total = line.getStartMillis();
                builder.append('[')
                        .append(String.format("%02d", total / 60_000L))
                        .append(':')
                        .append(String.format("%02d", (total % 60_000L) / 1_000L))
                        .append('.')
                        .append(String.format("%03d", total % 1_000L))
                        .append(']')
                        .append(line.getText())
                        .append('\n');
            }
            Files.writeString(file(key), builder.toString(), StandardCharsets.UTF_8);
        } catch (Throwable ignored) {
        }
    }

    private static Lyrics fetch(String title, String artist, long durationMillis) {
        String cleanTitle = clean(title);
        String cleanArtist = clean(artist);
        long seconds = durationMillis / 1000L;

        Lyrics direct = fromGet(cleanTitle, cleanArtist, seconds);
        if (direct != null) {
            return direct;
        }
        if (cleanTitle.contains(" - ")) {
            String[] parts = cleanTitle.split(" - ", 2);
            Lyrics swapped = fromGet(parts[1].trim(), parts[0].trim(), seconds);
            if (swapped != null) {
                return swapped;
            }
        }
        return fromSearch(cleanArtist + " " + cleanTitle);
    }

    private static Lyrics fromGet(String title, String artist, long seconds) {
        try {
            StringBuilder url = new StringBuilder("https://lrclib.net/api/get?track_name=")
                    .append(encode(title))
                    .append("&artist_name=")
                    .append(encode(artist));
            if (seconds > 0L) {
                url.append("&duration=").append(seconds);
            }
            return fromJson(get(url.toString()));
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Lyrics fromSearch(String query) {
        try {
            JsonElement root = JsonParser.parseString(get("https://lrclib.net/api/search?q=" + encode(query)));
            if (!root.isJsonArray()) {
                return null;
            }
            JsonArray array = root.getAsJsonArray();
            Lyrics plain = null;
            for (JsonElement element : array) {
                if (!element.isJsonObject()) {
                    continue;
                }
                JsonObject object = element.getAsJsonObject();
                String synced = string(object, "syncedLyrics");
                if (synced != null && !synced.isBlank()) {
                    return LyricsParser.parse(synced);
                }
                if (plain == null) {
                    String text = string(object, "plainLyrics");
                    if (text != null && !text.isBlank()) {
                        plain = LyricsParser.parse(plainToLrc(text));
                    }
                }
            }
            return plain;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Lyrics fromJson(String body) {
        if (body == null) {
            return null;
        }
        JsonElement root = JsonParser.parseString(body);
        if (!root.isJsonObject()) {
            return null;
        }
        JsonObject object = root.getAsJsonObject();
        String synced = string(object, "syncedLyrics");
        if (synced != null && !synced.isBlank()) {
            Lyrics lyrics = LyricsParser.parse(synced);
            return lyrics.isEmpty() ? null : lyrics;
        }
        String plain = string(object, "plainLyrics");
        if (plain != null && !plain.isBlank()) {
            Lyrics lyrics = LyricsParser.parse(plainToLrc(plain));
            return lyrics.isEmpty() ? null : lyrics;
        }
        return null;
    }

    private static String string(JsonObject object, String name) {
        if (object.has(name) && !object.get(name).isJsonNull()) {
            return object.get(name).getAsString();
        }
        return null;
    }

    private static String get(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(6))
                    .header("User-Agent", USER_AGENT)
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 ? response.body() : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static String plainToLrc(String plain) {
        StringBuilder builder = new StringBuilder();
        long time = 0L;
        for (String line : plain.split("\\R")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            builder.append('[')
                    .append(String.format("%02d", time / 60_000L))
                    .append(':')
                    .append(String.format("%02d", (time % 60_000L) / 1_000L))
                    .append('.')
                    .append(String.format("%03d", time % 1_000L))
                    .append(']')
                    .append(trimmed)
                    .append('\n');
            time += 3500L;
        }
        return builder.toString();
    }

    private static String clean(String value) {
        if (value == null) {
            return "";
        }
        String result = value
                .replaceAll("(?i)\\((?:official|lyrics?|audio|video|hd|hq|clip|remaster)[^)]*\\)", "")
                .replaceAll("(?i)\\[(?:official|lyrics?|audio|video|hd|hq|clip|remaster)[^]]*]", "")
                .replaceAll("(?i)\\s+(?:feat|ft)\\.?\\s+.*$", "")
                .replaceAll("(?i)(official|lyrics|audio|video|clip|hd|hq)\\s*$", "")
                .trim();
        return result.replaceAll("\\s{2,}", " ");
    }

    private static String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
