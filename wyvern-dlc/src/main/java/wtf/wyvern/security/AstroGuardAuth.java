package wtf.wyvern.security;

import wtf.astroguard.J2C.Native;
import wtf.astroguard.J2C.NotNative;
import wtf.astroguard.J2C.Shrouded;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Native
public final class AstroGuardAuth {

    private static final String URL_BASE = "https://wyvernclient.fun";

    private static final String RELAY    = "https://wyvernclient.fun";
    private static final String VERIFY   = "/backend/loader/api/verify";

    private static final byte[] OBF = "rvP3oTa!loaderXz9k2Q".getBytes(StandardCharsets.UTF_8);

    private static final byte[] SECRET_OBF = {
        (byte)0xc1,(byte)0x9f,(byte)0xc6,(byte)0x96,(byte)0xc2,(byte)0x93,(byte)0x96,(byte)0xc5,(byte)0x9b,(byte)0xcc,(byte)0x95,(byte)0xc8,(byte)0x9b,(byte)0x9f,(byte)0x9a,(byte)0x99,
        (byte)0x81,(byte)0xd5,(byte)0x82,(byte)0xd5,(byte)0x8a,(byte)0xd7,(byte)0x81,(byte)0x82,(byte)0xdb,(byte)0x8a,(byte)0xdb,(byte)0x84,(byte)0xd9,(byte)0x8b,(byte)0xda,(byte)0x8e,
        (byte)0xe6,(byte)0xb5,(byte)0xe0,(byte)0xb3,(byte)0xba,(byte)0xb2,(byte)0xb3,(byte)0xe2,(byte)0xba,(byte)0xea,(byte)0xbb,(byte)0xea,(byte)0xb3,(byte)0xbb,(byte)0xea,(byte)0xbc,
        (byte)0xf2,(byte)0xa6,(byte)0xf4,(byte)0xad,(byte)0xf1,(byte)0xa1,(byte)0xf5,(byte)0xa7,(byte)0xf9,(byte)0xac,(byte)0xa8,(byte)0xad,(byte)0xad,(byte)0xaa,(byte)0xa1,(byte)0xac,(byte)0x84
    };
    private static byte[] buildSecret() {
        byte[] out = new byte[SECRET_OBF.length];
        for (int i = 0; i < out.length; i++) out[i] = (byte)(SECRET_OBF[i] ^ ((i ^ 0xA7) & 0xFF));
        return out;
    }
    private static final byte[] SECRET = buildSecret();

    private static volatile boolean started     = false;
    private static volatile boolean ok          = false;
    private static volatile long    failSince   = 0L;
    private static volatile long    killAt      = 0L;
    private static volatile long    verifiedUid = -1L;

    private static volatile long    pinnedUid   = Long.MIN_VALUE;

    private static final SecureRandom RNG = new SecureRandom();

    private AstroGuardAuth() {}

    public static boolean devMode() {

        String user = System.getProperty("astroguard.user");
        String hwid = System.getProperty("astroguard.hwid");
        if (user != null && !user.isEmpty() && hwid != null && !hwid.isEmpty()) return false;
        if ("true".equalsIgnoreCase(System.getProperty("astroguard.dev"))) return true;
        if ("true".equalsIgnoreCase(System.getenv("ASTROGUARD_DEV"))) return true;
        return "true".equalsIgnoreCase(System.getProperty("fabric.development"));
    }

    public static synchronized void boot() {
        if (started) return;
        started = true;
        if (devMode()) {
            ok = true;
            return;
        }
        Thread t = new Thread(AstroGuardAuth::loop, "Render-Thread-7");
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    private static final long GRACE_BEFORE_FIRST_MS = 3L * 60_000L;
    private static final long GRACE_AFTER_VERIFY_MS = 90_000L;

    private static void loop() {
        sleep(1500 + RNG.nextInt(2500));
        while (true) {
            AstroGuardNoise.fire();

            int st = -1;
            for (int attempt = 0; attempt < 3; attempt++) {
                if (attempt > 0) sleep(2000 + RNG.nextInt(3000));
                int r = verifyState();
                if (r != -1) { st = r; break; }
            }
            long now = System.currentTimeMillis();
            if (st == 1) {
                ok = true; failSince = 0L; killAt = 0L;
            } else if (st == 0) {

                ok = false;
                if (killAt == 0L) killAt = now + 4000L + RNG.nextInt(12_000);
            } else {

                if (failSince == 0L) failSince = now;
                long grace = (verifiedUid != -1L) ? GRACE_AFTER_VERIFY_MS : GRACE_BEFORE_FIRST_MS;
                if (now - failSince > grace) {
                    ok = false;
                    if (killAt == 0L) killAt = now + 4000L + RNG.nextInt(12_000);
                }
            }

            sleep(55_000 + RNG.nextInt(25_000));
        }
    }

    public static void guard() {
        if (devMode()) return;
        if (!started) return;
        long k = killAt;
        if (k != 0L && System.currentTimeMillis() >= k) {
            Runtime.getRuntime().halt(0);
        }
    }

    public static boolean valid() {
        return ok;
    }

    public static String getUsername() {
        String u = System.getProperty("astroguard.user");
        return (u != null && !u.isEmpty()) ? u : "";
    }

    public static long getUid() {
        return verifiedUid;
    }

    @NotNative
    @Shrouded
    private static int verifyState() {
        try {
            String user = System.getProperty("astroguard.user");
            String hwid = System.getProperty("astroguard.hwid");
            if (user == null || hwid == null || user.isEmpty() || hwid.isEmpty()) {
                return -1;
            }
            long nonce = RNG.nextLong() & 0xFFFFFFFFFFFFL;

            String payload = user + "" + hwid + "" + nonce;
            String q = b64url(xor(payload.getBytes(StandardCharsets.UTF_8), OBF));
            String body = httpGet(URL_BASE + VERIFY + "?q=" + enc(q));
            if (body == null) body = httpGet(RELAY + VERIFY + "?q=" + enc(q));
            if (body == null) return -1;

            long v    = jnum(body, "v");
            long exp  = jnum(body, "exp");
            long n    = jnum(body, "n");
            long ruid = jnum(body, "uid");
            String s  = jstr(body, "s");
            if (n != nonce) return -1;

            String msg = v + "|" + exp + "|" + nonce + "|" + hwid + "|" + user + "|" + ruid;
            String want = hmacHex(SECRET, msg);
            if (!constEq(want, s)) return -1;

            if (pinnedUid == Long.MIN_VALUE) {
                pinnedUid = ruid;
            } else if (pinnedUid != ruid) {
                ok = false;
                killAt = System.currentTimeMillis() + 2000L;
                return 0;
            }

            if (v == 1L && exp > (System.currentTimeMillis() / 1000L)) {
                verifiedUid = ruid;
                return 1;
            }
            return 0;
        } catch (Throwable t) {
            return -1;
        }
    }

    private static byte[] xor(byte[] data, byte[] key) {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i++) out[i] = (byte) (data[i] ^ key[i % key.length]);
        return out;
    }

    private static String b64url(byte[] b) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
    }

    private static String enc(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    private static String httpGet(String url) {
        HttpURLConnection c = null;
        try {
            c = (HttpURLConnection) new URL(url).openConnection();
            c.setConnectTimeout(15000);
            c.setReadTimeout(15000);
            c.setRequestMethod("GET");
            c.setRequestProperty("User-Agent", "Mozilla/5.0");
            if (c.getResponseCode() != 200) return null;
            try (InputStream in = c.getInputStream()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int r;
                while ((r = in.read(buf)) > 0) bos.write(buf, 0, r);
                return new String(bos.toByteArray(), StandardCharsets.UTF_8);
            }
        } catch (Throwable t) {
            return null;
        } finally {
            if (c != null) c.disconnect();
        }
    }

    @NotNative
    @Shrouded
    private static String hmacHex(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        byte[] d = mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(d.length * 2);
        for (byte b : d) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit(b & 0xF, 16));
        }
        return sb.toString();
    }

    @NotNative
    @Shrouded
    private static boolean constEq(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) return false;
        int r = 0;
        for (int i = 0; i < a.length(); i++) r |= a.charAt(i) ^ b.charAt(i);
        return r == 0;
    }

    private static long jnum(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        if (i < 0) return -1;
        i = json.indexOf(':', i);
        if (i < 0) return -1;
        i++;
        while (i < json.length() && (json.charAt(i) == ' ' || json.charAt(i) == '"')) i++;
        int j = i;
        while (j < json.length() && (Character.isDigit(json.charAt(j)) || json.charAt(j) == '-')) j++;
        try {
            return Long.parseLong(json.substring(i, j));
        } catch (Exception e) {
            return -1;
        }
    }

    private static String jstr(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        if (i < 0) return null;
        i = json.indexOf(':', i);
        if (i < 0) return null;
        i = json.indexOf('"', i);
        if (i < 0) return null;
        i++;
        int j = json.indexOf('"', i);
        if (j < 0) return null;
        return json.substring(i, j);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
