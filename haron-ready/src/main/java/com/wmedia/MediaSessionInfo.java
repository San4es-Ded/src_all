package com.wmedia;

/** Immutable media-session information returned by the optional native bridge. */
public final class MediaSessionInfo {
    private final String title;
    private final String artist;
    private final boolean playing;
    private final float positionMs;
    private final float durationMs;
    private final byte[] albumArt;

    public MediaSessionInfo(String title, String artist, boolean playing, float positionMs, float durationMs, byte[] albumArt) {
        this.title = title == null ? "" : title;
        this.artist = artist == null ? "" : artist;
        this.playing = playing;
        this.positionMs = positionMs;
        this.durationMs = durationMs;
        this.albumArt = albumArt == null ? null : albumArt.clone();
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public float getPositionMs() {
        return this.positionMs;
    }

    public float getDurationMs() {
        return this.durationMs;
    }

    public byte[] getAlbumArt() {
        return this.albumArt == null ? null : this.albumArt.clone();
    }
}
