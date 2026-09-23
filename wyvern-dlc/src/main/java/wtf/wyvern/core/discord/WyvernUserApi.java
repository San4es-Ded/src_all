package wtf.wyvern.core.discord;

/**
 * Site username + UID for the Discord Rich Presence. The launcher already
 * resolves both from the logged-in session and passes them into the JVM as
 * -Dastroguard.user / -Dastroguard.uid (see launcher.cpp LaunchMinecraft1214);
 * reading those directly is correct and avoids re-fetching over the network,
 * where the backend's XOR response wrapper and auth requirements made the
 * previous unauthenticated plain-JSON fetch to /api/user always fail silently.
 */
public final class WyvernUserApi {
    private WyvernUserApi() {
    }

    /** Username passed by the launcher, or the given fallback if absent/blank. */
    public static String getUsername(String fallback) {
        String value = System.getProperty("astroguard.user");
        return value == null || value.isBlank() ? fallback : value;
    }

    /** UID passed by the launcher, or the given fallback if absent/blank. */
    public static String getUid(String fallback) {
        String value = System.getProperty("astroguard.uid");
        return value == null || value.isBlank() ? fallback : value;
    }
}
