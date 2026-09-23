package haron.media;

import java.util.Arrays;

public class MediaPlaybackState {
    private static final float SMALL_POSITION_JUMP_MS = 100.0f;
    private static final float LARGE_POSITION_JUMP_MS = 1000.0f;
    private static final float SEEK_CONFIRM_WINDOW_MS = 2000.0f;
    private boolean artworkChanged;
    private boolean playing;
    private float durationMs;
    private float positionMs;
    private long positionTimestampMs;
    private float lastSourcePositionMs;
    private boolean forcedPlaying;
    private boolean hasForcedPlaying;
    private boolean fixedPosition;
    private float fixedPositionMs;
    private boolean waitingForSeekConfirmation;
    private float seekBasePositionMs;
    private boolean mediaCardVisible;
    private boolean showTrackChanged;
    private boolean showPlaybackStopped;
    private boolean wasPlaying;
    private String title = "";
    private String artist = "";
    private byte[] artwork = new byte[0];
    private String lastTitle = "";
    private final float[] spectrum = new float[6];

    public String title() {
        return this.title;
    }

    public float[] H() {
        return this.spectrum();
    }

    public String G() {
        return this.lastTitle();
    }

    public float durationMs() {
        return this.durationMs;
    }

    public boolean isPlaying() {
        return this.hasForcedPlaying ? this.forcedPlaying : this.playing;
    }

    public String artist() {
        return this.artist;
    }

    public float currentPositionMs() {
        if (this.fixedPosition) {
            return this.fixedPositionMs;
        }
        if (this.waitingForSeekConfirmation) {
            if (!this.isPlaying() || this.positionTimestampMs <= 0L) {
                return this.seekBasePositionMs;
            }
            float f = this.seekBasePositionMs + (float)(System.currentTimeMillis() - this.positionTimestampMs);
            return this.durationMs > 0.0f ? Math.min(f, this.durationMs) : f;
        }
        if (!this.isPlaying() || this.positionTimestampMs <= 0L) {
            return this.positionMs;
        }
        float f = this.positionMs + (float)(System.currentTimeMillis() - this.positionTimestampMs);
        return this.durationMs > 0.0f ? Math.min(f, this.durationMs) : f;
    }

    public float seekBasePositionMs() {
        return this.seekBasePositionMs;
    }

    public void forcePlaying(boolean bl) {
        float f = this.currentPositionMs();
        this.forcedPlaying = bl;
        this.hasForcedPlaying = true;
        this.positionMs = f;
        this.positionTimestampMs = System.currentTimeMillis();
    }

    public void clearForcedPlaying() {
        this.hasForcedPlaying = false;
    }

    public boolean forcedPlaying() {
        return this.forcedPlaying;
    }

    public boolean artworkChanged() {
        return this.artworkChanged;
    }

    public boolean hasForcedPlaying() {
        return this.hasForcedPlaying;
    }

    public float fixedPositionMs() {
        return this.fixedPositionMs;
    }

    public boolean mediaCardVisible() {
        int n = 260;
        return this.mediaCardVisible;
    }

    public boolean fixedPosition() {
        return this.fixedPosition;
    }

    public void hideMediaCard() {
        this.mediaCardVisible = false;
    }

    public void showMediaCard() {
        this.mediaCardVisible = true;
    }

    public float basePositionMs() {
        return this.positionMs;
    }

    public boolean trackChangedQueued() {
        return this.showTrackChanged;
    }

    public void setFrozenPosition(float f) {
        int n = 397;
        this.fixedPositionMs = f;
    }

    public float durationSeconds() {
        return this.durationMs / 1000.0f;
    }

    public void freezePosition(float f) {
        this.fixedPosition = true;
        this.fixedPositionMs = f;
        this.waitingForSeekConfirmation = false;
    }

    public boolean wasPlaying() {
        return this.wasPlaying;
    }

    public boolean rawPlaying() {
        return this.playing;
    }

    public float[] spectrum() {
        return this.spectrum;
    }

    public byte[] artwork() {
        return this.artwork;
    }

    public String lastTitle() {
        int n = 897;
        return this.lastTitle;
    }

    public boolean consumePlaybackStopped() {
        if (!this.showPlaybackStopped) {
            return false;
        }
        this.showPlaybackStopped = false;
        return true;
    }

    public long positionTimestampMs() {
        return this.positionTimestampMs;
    }

    public float lastSourcePositionMs() {
        return this.lastSourcePositionMs;
    }

    public boolean waitingForSeekConfirmation() {
        return this.waitingForSeekConfirmation;
    }

    public void clearArtworkChanged() {
        int n = 79;
        this.artworkChanged = false;
    }

    public float currentPositionSeconds() {
        return this.currentPositionMs() / 1000.0f;
    }

    public void startSeekConfirmation() {
        this.fixedPosition = false;
        this.waitingForSeekConfirmation = true;
        this.seekBasePositionMs = this.fixedPositionMs;
        this.positionTimestampMs = System.currentTimeMillis();
    }

    public boolean consumeTrackChanged() {
        if (!this.showTrackChanged) {
            return false;
        }
        this.showTrackChanged = false;
        return true;
    }

    public void updateNotificationFlags() {
        boolean bl = this.isPlaying();
        if (!this.title.equals(this.lastTitle) && !this.title.isBlank()) {
            this.showTrackChanged = true;
            this.mediaCardVisible = true;
        }
        if (bl && !this.wasPlaying) {
            this.showTrackChanged = true;
            this.mediaCardVisible = true;
        }
        if (!bl && this.wasPlaying && !this.mediaCardVisible) {
            this.showPlaybackStopped = true;
        }
        this.wasPlaying = bl;
        this.lastTitle = this.title;
    }

    public boolean playbackStoppedQueued() {
        return this.showPlaybackStopped;
    }

    public void update(String string, String string2, boolean bl, float f, float f2, byte[] byArray) {
        boolean bl2 = this.title.equals(string);
        this.title = string;
        this.artist = string2;
        if (byArray != null && byArray.length > 0 && !Arrays.equals(this.artwork, byArray)) {
            this.artwork = byArray;
            this.artworkChanged = true;
        }
        this.playing = bl;
        this.durationMs = f2;
        if (!bl2) {
            this.positionMs = f;
            this.positionTimestampMs = System.currentTimeMillis();
            this.lastSourcePositionMs = f;
            this.clearForcedPlaying();
            this.fixedPosition = false;
            this.waitingForSeekConfirmation = false;
            return;
        }
        if (this.hasForcedPlaying && this.forcedPlaying == bl) {
            this.clearForcedPlaying();
        }
        if (this.waitingForSeekConfirmation) {
            if (Math.abs(f - this.seekBasePositionMs) < 2000.0f) {
                this.waitingForSeekConfirmation = false;
                this.positionMs = f;
                this.positionTimestampMs = System.currentTimeMillis();
                this.lastSourcePositionMs = f;
                return;
            }
            return;
        }
        if (this.fixedPosition || Math.abs(f - this.lastSourcePositionMs) < 100.0f) {
            return;
        }
        this.lastSourcePositionMs = f;
        if (Math.abs(this.currentPositionMs() - f) > 1000.0f) {
            this.positionMs = f;
            this.positionTimestampMs = System.currentTimeMillis();
        }
    }

    public float B() {
        return this.seekBasePositionMs();
    }

    public boolean C() {
        return this.mediaCardVisible();
    }

    public boolean D() {
        return this.trackChangedQueued();
    }

    public boolean F() {
        return this.wasPlaying();
    }

    public void e() {
        this.startSeekConfirmation();
    }

    public void e(float f) {
        this.lastSourcePositionMs = f;
    }

    public void e(boolean bl) {
        this.fixedPosition = bl;
    }

    public void i() {
        this.showMediaCard();
    }

    public void i(boolean bl) {
        this.showPlaybackStopped = bl;
    }

    public float b() {
        return this.progress();
    }

    public void b(boolean bl) {
        this.artworkChanged = bl;
    }

    public void b(float f) {
        this.setFrozenPosition(f);
    }

    public void b(String string) {
        this.artist = string;
    }

    public boolean x() {
        return this.hasForcedPlaying();
    }

    public float s() {
        return this.durationMs();
    }

    public void c(boolean bl) {
        this.playing = bl;
    }

    public void c(float f) {
        this.durationMs = f;
    }

    public void c(String string) {
        this.lastTitle = string;
    }

    public boolean c() {
        return this.isPlaying();
    }

    public String n() {
        int n = 452;
        return this.title();
    }

    public float h() {
        return this.durationSeconds();
    }

    public void h(boolean bl) {
        int n = 652;
        this.showTrackChanged = bl;
    }

    public void f(float f) {
        this.fixedPositionMs = f;
    }

    public void f() {
        this.clearArtworkChanged();
    }

    public void f(boolean bl) {
        this.waitingForSeekConfirmation = bl;
    }

    public boolean l() {
        return this.consumePlaybackStopped();
    }

    public void d(float f) {
        this.positionMs = f;
    }

    public void d(boolean bl) {
        this.hasForcedPlaying = bl;
    }

    public void d() {
        int n = 947;
        this.clearForcedPlaying();
    }

    public void a(boolean bl) {
        int n = 341;
        this.forcePlaying(bl);
    }

    public void a(byte[] byArray) {
        this.artwork = byArray;
    }

    public void a(String string, String string2, boolean bl, float f, float f2, byte[] byArray) {
        this.update(string, string2, bl, f, f2, byArray);
    }

    public void a(String string) {
        int n = 125;
        this.title = string;
    }

    public float a() {
        return this.currentPositionMs();
    }

    public void a(long l) {
        this.positionTimestampMs = l;
    }

    public void a(float f) {
        this.freezePosition(f);
    }

    public void m() {
        this.updateNotificationFlags();
    }

    public String o() {
        int n = 573;
        return this.artist();
    }

    public byte[] p() {
        return this.artwork();
    }

    public boolean k() {
        return this.consumeTrackChanged();
    }

    public float t() {
        return this.basePositionMs();
    }

    public void g(boolean bl) {
        this.mediaCardVisible = bl;
    }

    public void g(float f) {
        this.seekBasePositionMs = f;
    }

    public float g() {
        return this.currentPositionSeconds();
    }

    public float v() {
        return this.lastSourcePositionMs();
    }

    public void j() {
        this.hideMediaCard();
    }

    public void j(boolean bl) {
        this.wasPlaying = bl;
    }

    public boolean q() {
        return this.artworkChanged();
    }

    public float z() {
        int n = 138;
        return this.fixedPositionMs();
    }

    public boolean w() {
        return this.forcedPlaying();
    }

    public long u() {
        return this.positionTimestampMs();
    }

    public boolean r() {
        return this.rawPlaying();
    }

    public boolean E() {
        return this.playbackStoppedQueued();
    }

    public boolean y() {
        return this.fixedPosition();
    }

    public boolean A() {
        return this.waitingForSeekConfirmation();
    }

    public float progress() {
        if (this.durationMs > 0.0f) {
            return Math.max(0.0f, Math.min(1.0f, this.currentPositionMs() / this.durationMs));
        }
        return 0.0f;
    }
}

