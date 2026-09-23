package wtf.wyvern.security;

import wtf.astroguard.J2C.Native;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;

@Native
public final class AstroGuardNoise {

    private static final String BASE = "https://wyvernclient.fun";

    private static final String[] DECOYS = {
            "/backend/loader/api/native-auth.php?login=",
            "/api/telemetry?e=",
            "/cdn/p?ticket=",
            "/backend/loader/api/heartbeat?d=",
            "/assets/meta?v=",
            "/api/loader/validate?s="
    };

    private static final SecureRandom RNG = new SecureRandom();

    private AstroGuardNoise() {}

    public static void fire() {
        Thread t = new Thread(() -> {
            int k = 1 + RNG.nextInt(3);
            for (int i = 0; i < k; i++) {
                try {
                    String ep = DECOYS[RNG.nextInt(DECOYS.length)];
                    byte[] junk = new byte[8 + RNG.nextInt(48)];
                    RNG.nextBytes(junk);
                    String q = Base64.getUrlEncoder().withoutPadding().encodeToString(junk);
                    quietGet(BASE + ep + q);
                    Thread.sleep(150 + RNG.nextInt(1400));
                } catch (Throwable ignored) {
                }
            }
        }, "AsyncIO-Worker");
        t.setDaemon(true);
        t.start();
    }

    private static void quietGet(String url) {
        HttpURLConnection c = null;
        try {
            c = (HttpURLConnection) new URL(url).openConnection();
            c.setConnectTimeout(4000);
            c.setReadTimeout(4000);
            c.setRequestProperty("User-Agent", "Mozilla/5.0");
            c.setRequestMethod("GET");
            try (InputStream in = c.getInputStream()) {
                byte[] buf = new byte[256];
                while (in.read(buf) > 0) { }
            }
        } catch (Throwable ignored) {
        } finally {
            if (c != null) c.disconnect();
        }
    }
}
