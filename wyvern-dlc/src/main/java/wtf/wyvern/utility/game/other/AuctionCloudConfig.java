package wtf.wyvern.utility.game.other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Адрес и ключ облака цен лежат в конфиге, а не в константах: сервер переезжает
 * и ключ ротируется без пересборки клиента.
 */
public record AuctionCloudConfig(boolean enabled, String baseUrl, String apiKey,
                                 int pullSeconds, int pushSeconds) {

    private static final String DEFAULT_BASE_URL = "http://51.38.153.138:7777";
    private static final String DEFAULT_API_KEY = "32d1bfc4fc3813a56a1336d7883d151135eb0c88d8307e8a";

    // Пуш почти сразу - чужие клиенты должны видеть отсканированную страницу быстро.
    // Пулл реже: дельта обычно пустая, а каждый запрос это лишний коннект.
    private static final int DEFAULT_PULL_SECONDS = 30;
    private static final int DEFAULT_PUSH_SECONDS = 2;

    private static final int MIN_PULL_SECONDS = 5;
    private static final int MIN_PUSH_SECONDS = 1;

    /** Значения из конфига могут быть мусорными или нулями - подставляем дефолты. */
    public AuctionCloudConfig {
        pullSeconds = pullSeconds < MIN_PULL_SECONDS ? DEFAULT_PULL_SECONDS : pullSeconds;
        pushSeconds = pushSeconds < MIN_PUSH_SECONDS ? DEFAULT_PUSH_SECONDS : pushSeconds;
    }

    public static AuctionCloudConfig load() {
        Path path = configPath();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                AuctionCloudConfig config = gson().fromJson(reader, AuctionCloudConfig.class);
                if (config != null && config.baseUrl != null && !config.baseUrl.isBlank()) {
                    return config;
                }
            } catch (Exception ignored) {
            }
        }
        // Первый запуск: кладём рабочий конфиг рядом с остальными, чтобы адрес и ключ
        // можно было поменять после переезда сервера без пересборки.
        AuctionCloudConfig fallback = new AuctionCloudConfig(true, DEFAULT_BASE_URL, DEFAULT_API_KEY,
                DEFAULT_PULL_SECONDS, DEFAULT_PUSH_SECONDS);
        fallback.save();
        return fallback;
    }

    public void save() {
        try {
            Path path = configPath();
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson().toJson(this, writer);
            }
        } catch (Exception ignored) {
        }
    }

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("wyvern").resolve("auction_cloud.json");
    }

    private static Gson gson() {
        return GsonHolder.GSON;
    }

    private static final class GsonHolder {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
