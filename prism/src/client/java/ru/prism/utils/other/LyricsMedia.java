package ru.prism.utils.other;

import dev.redstones.mediaplayerinfo.IMediaSession;
import dev.redstones.mediaplayerinfo.MediaInfo;
import dev.redstones.mediaplayerinfo.MediaPlayerInfo;

import java.util.Comparator;
import java.util.Locale;

public final class LyricsMedia {

    public record Track(String title, String artist, String appId, long durationMillis) {
        public boolean isEmpty() {
            return title == null || title.isBlank();
        }
    }

    private static final String[] BROWSER_OWNERS = {
            "chrome", "chromium", "msedge", "microsoftedge", "firefox", "waterfox", "librewolf",
            "opera", "brave", "vivaldi", "iexplore", "thorium", "torbrowser", "safari",
            "browser", "zen.exe", "arc.exe"
    };

    private static volatile Track track = new Track("", "", "", 0L);
    private static volatile boolean playing;
    private static volatile long anchorNanos;
    private static volatile long anchorPosition;
    private static volatile long lastPollMillis;

    private LyricsMedia() {
    }

    public static Track getTrack() {
        return track;
    }

    public static boolean isPlaying() {
        return playing;
    }

    public static long positionMillis() {
        if (!playing) {
            return anchorPosition;
        }
        long elapsed = (System.nanoTime() - anchorNanos) / 1_000_000L;
        return anchorPosition + Math.max(0L, elapsed);
    }

    public static void tick() {
        long now = System.currentTimeMillis();
        if (now - lastPollMillis < 200L) {
            return;
        }
        lastPollMillis = now;
        try {
            IMediaSession session = MediaPlayerInfo.Instance.getMediaSessions().stream()
                    .filter(candidate -> !isBrowser(candidate.getOwner()))
                    .max(Comparator.comparingInt(candidate -> candidate.getMedia().getPlaying() ? 1 : 0))
                    .orElse(null);
            if (session == null) {
                clear();
                return;
            }
            MediaInfo media = session.getMedia();
            String title = media.getTitle() == null ? "" : media.getTitle();
            String artist = media.getArtist() == null ? "" : media.getArtist();
            String owner = session.getOwner() == null ? "" : session.getOwner();
            long duration = Math.max(0L, media.getDuration()) * 1000L;
            long position = Math.max(0L, media.getPosition()) * 1000L;

            if (!title.equals(track.title()) || !artist.equals(track.artist()) || duration != track.durationMillis()) {
                track = new Track(title, artist, owner, duration);
                anchorPosition = position;
                anchorNanos = System.nanoTime();
            } else {
                long drift = position - positionMillis();
                if (Math.abs(drift) > 300L) {
                    anchorPosition = position;
                    anchorNanos = System.nanoTime();
                }
            }
            playing = media.getPlaying();
        } catch (Throwable ignored) {
        }
    }

    private static void clear() {
        if (!track.isEmpty()) {
            track = new Track("", "", "", 0L);
            anchorPosition = 0L;
            anchorNanos = System.nanoTime();
            playing = false;
        }
    }

    private static boolean isBrowser(String owner) {
        if (owner == null) {
            return false;
        }
        String value = owner.toLowerCase(Locale.ROOT);
        for (String browser : BROWSER_OWNERS) {
            if (value.contains(browser)) {
                return true;
            }
        }
        return false;
    }
}
