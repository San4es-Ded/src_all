package com.wmedia;

import java.util.Optional;

/**
 * Source-level fallback for the native Windows media bridge that was injected
 * into the running client but was not part of the supplied JAR.
 */
public final class WindowsMediaBridge {
    private WindowsMediaBridge() {
    }

    public static boolean isAvailable() {
        return false;
    }

    public static Optional<MediaSessionInfo> getCurrentMedia() {
        return Optional.empty();
    }

    public static Optional<AudioLevelSnapshot> getAudioLevels() {
        return Optional.empty();
    }

    public static void playPause() {
    }

    public static void next() {
    }

    public static void previous() {
    }

    public static void seek(long positionMs) {
    }

    public static void shutdown() {
    }
}
