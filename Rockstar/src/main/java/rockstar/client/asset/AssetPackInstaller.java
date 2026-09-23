package rockstar.client.asset;


import rockstar.client.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import rockstar.client.RockstarClient;
import rockstar.client.asset.AssetTextureManager;

public final class AssetPackInstaller {
    private static final String internalField0248 = ".rockstar-assets";
    private static final Duration internalField0590 = Duration.ofSeconds(15L);
    private static final Duration internalField0589 = Duration.ofSeconds(20L);
    private static final Duration internalField1226 = Duration.ofMinutes(10L);
    private static volatile boolean internalField0277;

    private AssetPackInstaller() {
    }

    public static void internalMethod02290(String string) {
        if (string == null || string.isBlank() || internalField0277) {
            return;
        }
        internalField0277 = true;
        Thread thread = new Thread(() -> {
            try {
                AssetPackInstaller.internalMethod00807(string);
            }
            catch (Throwable throwable) {
                RockstarClient.internalField0572.error("[Assets] \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0430 \u043f\u0430\u043a\u0430 \u0441\u043e\u0440\u0432\u0430\u043b\u0430\u0441\u044c", throwable);
            }
            finally {
                internalField0277 = false;
            }
        }, "Rockstar-AssetPack-Installer");
        thread.setDaemon(true);
        thread.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void internalMethod00807(String string) throws Exception {
        HttpClient httpClient = HttpClient.newBuilder().connectTimeout(internalField0590).followRedirects(HttpClient.Redirect.NORMAL).build();
        JsonObject jsonObject = AssetPackInstaller.internalMethod06936(httpClient, string);
        if (jsonObject == null) {
            return;
        }
        String string2 = jsonObject.get("sha256").getAsString().toLowerCase(Locale.ROOT);
        long l = jsonObject.has("size") ? jsonObject.get("size").getAsLong() : -1L;
        Path path = AssetTextureManager.internalMethod00045().toPath();
        if (Files.isDirectory(path, new LinkOption[0]) && string2.equals(AssetPackInstaller.internalMethod01055())) {
            RockstarClient.internalField0572.info("[Assets] \u043f\u0430\u043a \u043d\u0430 \u043c\u0435\u0441\u0442\u0435 \u0438 \u0430\u043a\u0442\u0443\u0430\u043b\u0435\u043d");
            return;
        }
        RockstarClient.internalField0572.info("[Assets] {} \u043f\u0430\u043a \u043a\u0430\u0440\u0442\u0438\u043d\u043e\u043a ({} \u041c\u0411)...", (Object)(Files.isDirectory(path, new LinkOption[0]) ? "\u043e\u0431\u043d\u043e\u0432\u043b\u044f\u044e" : "\u043a\u0430\u0447\u0430\u044e"), (Object)(Math.max(l, 0L) / 0x100000L));
        Path path2 = path.resolveSibling("assets.download");
        Path path3 = path.resolveSibling("assets.zip.part");
        AssetPackInstaller.internalMethod07592(path2);
        Files.createDirectories(path2, new FileAttribute[0]);
        try {
            String string3 = AssetPackInstaller.internalMethod01724(httpClient, string + "/assets/download", path3, l);
            if (!string2.equals(string3)) {
                RockstarClient.internalField0572.error("[Assets] \u0430\u0440\u0445\u0438\u0432 \u043f\u043e\u0431\u0438\u043b\u0441\u044f: \u043e\u0436\u0438\u0434\u0430\u043b\u0438 sha256={}, \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u0438 {}", (Object)string2, (Object)string3);
                return;
            }
            AssetPackInstaller.internalMethod01808(path3, path2);
            AssetPackInstaller.internalMethod04814(path2, path);
            Files.writeString(path.resolve(internalField0248), (CharSequence)string2, StandardCharsets.UTF_8, new OpenOption[0]);
            AssetTextureManager.internalMethod00192();
            RockstarClient.internalField0572.info("[Assets] \u043f\u0430\u043a \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d: {}", (Object)path);
        }
        finally {
            Files.deleteIfExists(path3);
            AssetPackInstaller.internalMethod07592(path2);
        }
    }

    private static JsonObject internalMethod06936(HttpClient httpClient, String string) {
        String string2 = string + "/assets/manifest";
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(string2)).timeout(internalField0589).GET().build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() != 200) {
                RockstarClient.internalField0572.warn("[Assets] \u043c\u0430\u043d\u0438\u0444\u0435\u0441\u0442 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u0435\u043d (HTTP {}) \u2014 \u043e\u0441\u0442\u0430\u0451\u043c\u0441\u044f \u043d\u0430 \u0432\u0441\u0442\u0440\u043e\u0435\u043d\u043d\u043e\u0439 \u043e\u0431\u043e\u0435", (Object)httpResponse.statusCode());
                return null;
            }
            JsonObject jsonObject = JsonParser.parseString((String)httpResponse.body()).getAsJsonObject();
            if (!jsonObject.has("sha256")) {
                RockstarClient.internalField0572.warn("[Assets] \u0432 \u043c\u0430\u043d\u0438\u0444\u0435\u0441\u0442\u0435 \u043d\u0435\u0442 sha256 \u2014 \u043f\u0440\u043e\u043f\u0443\u0441\u043a\u0430\u0435\u043c \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0443");
                return null;
            }
            return jsonObject;
        }
        catch (Exception exception) {
            RockstarClient.internalField0572.warn("[Assets] \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u043b\u0443\u0447\u0438\u0442\u044c \u043c\u0430\u043d\u0438\u0444\u0435\u0441\u0442 ({}): {}", (Object)string2, (Object)exception.getMessage());
            return null;
        }
    }

    private static String internalMethod01724(HttpClient httpClient, String string, Path path, long l) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(string)).timeout(internalField1226).GET().build();
        HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
        if (httpResponse.statusCode() != 200) {
            throw new IOException("\u0441\u0435\u0440\u0432\u0435\u0440 \u043e\u0442\u0432\u0435\u0442\u0438\u043b HTTP " + httpResponse.statusCode());
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] byArray = new byte[65536];
        long l2 = 0L;
        int n = 0;
        Files.createDirectories(path.getParent(), new FileAttribute[0]);
        try (InputStream inputStream = httpResponse.body();
             OutputStream outputStream = Files.newOutputStream(path, new OpenOption[0]);){
            int n2;
            while ((n2 = inputStream.read(byArray)) != -1) {
                int n3;
                outputStream.write(byArray, 0, n2);
                messageDigest.update(byArray, 0, n2);
                if (l <= 0L || (n3 = (int)((l2 += (long)n2) * 100L / l)) < n + 25 || n3 >= 100) continue;
                n = n3;
                RockstarClient.internalField0572.info("[Assets] \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430: {}%", (Object)n3);
            }
        }
        return AssetPackInstaller.internalMethod01827(messageDigest.digest());
    }

    private static void internalMethod01808(Path path, Path path2) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(path, new OpenOption[0]));){
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                Path path3 = path2.resolve(zipEntry.getName().replace('\\', '/')).normalize();
                if (!path3.startsWith(path2)) {
                    throw new IOException("\u0430\u0440\u0445\u0438\u0432 \u043f\u044b\u0442\u0430\u0435\u0442\u0441\u044f \u043f\u0438\u0441\u0430\u0442\u044c \u0437\u0430 \u043f\u0440\u0435\u0434\u0435\u043b\u044b \u043f\u0430\u043f\u043a\u0438: " + zipEntry.getName());
                }
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(path3, new FileAttribute[0]);
                    continue;
                }
                Files.createDirectories(path3.getParent(), new FileAttribute[0]);
                Files.copy(zipInputStream, path3, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private static void internalMethod04814(Path path, Path path2) throws IOException {
        Path path3 = path2.resolveSibling("assets.old");
        AssetPackInstaller.internalMethod07592(path3);
        if (Files.exists(path2, new LinkOption[0])) {
            Files.move(path2, path3, StandardCopyOption.REPLACE_EXISTING);
        }
        try {
            Files.move(path, path2, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException iOException) {
            if (Files.exists(path3, new LinkOption[0])) {
                Files.move(path3, path2, StandardCopyOption.REPLACE_EXISTING);
            }
            throw iOException;
        }
        AssetPackInstaller.internalMethod07592(path3);
    }

    private static String internalMethod01055() {
        Path path = AssetTextureManager.internalMethod00045().toPath().resolve(internalField0248);
        try {
            return Files.exists(path, new LinkOption[0]) ? Files.readString(path, StandardCharsets.UTF_8).trim().toLowerCase(Locale.ROOT) : "";
        }
        catch (IOException iOException) {
            return "";
        }
    }

    private static void internalMethod07592(Path path2) throws IOException {
        if (!Files.exists(path2, new LinkOption[0])) {
            return;
        }
        try (Stream<Path> stream = Files.walk(path2, new FileVisitOption[0]);){
            stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            });
        }
    }

    private static String internalMethod01827(byte[] byArray) {
        StringBuilder stringBuilder = new StringBuilder(byArray.length * 2);
        for (byte by : byArray) {
            stringBuilder.append(Character.forDigit(by >> 4 & 0xF, 16));
            stringBuilder.append(Character.forDigit(by & 0xF, 16));
        }
        return stringBuilder.toString();
    }
}

