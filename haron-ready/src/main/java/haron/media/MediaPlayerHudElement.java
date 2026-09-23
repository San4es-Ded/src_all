package haron.media;

import com.wmedia.AudioLevelSnapshot;
import com.wmedia.MediaSessionInfo;
import com.wmedia.WindowsMediaBridge;
import haron.core.BooleanCoercion;
import haron.events.ClientTickEvent;
import haron.hud.core.HudServiceInfo;
import haron.hud.core.HudService;
import haron.media.MediaSnapshot;
import haron.media.MediaPlaybackState;
import haron.modules.hud.DesktopMediaTitlePoller;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import meteordevelopment.orbit.EventHandler;

@HudServiceInfo(enabledByDefault=true)
public class MediaPlayerHudElement
extends HudService {
    private static final long c = 400L;
    private static final long d = 16L;
    private static final AtomicReference<float[]> j;
    private static volatile boolean k;
    private static volatile boolean l;
    private static final int m = 6;
    private static final long t = 50L;
    private static final long u = 16L;
    public static int a;
    public static boolean b;
    private static Thread h;
    private static final AtomicReference<MediaSnapshot> i;
    private static final AtomicBoolean f;
    private static final AtomicBoolean g;
    private final MediaPlaybackState e = new MediaPlaybackState();
    private final float[] n = new float[6];
    private final float[] o = new float[6];
    private float p = 0.0f;
    private int q = 0;
    private long r = 0L;
    private long s = 0L;

    static {
        h = null;
        i = new AtomicReference<MediaSnapshot>(null);
        f = new AtomicBoolean(false);
        g = new AtomicBoolean(false);
        float[] fArray = new float[]{0.0f, 0.0f, 0.0f};
        j = new AtomicReference<float[]>(fArray);
        k = false;
        l = false;
    }

    public void e() {
        if (MediaPlayerHudElement.g()) {
            new Thread(() -> {
                try {
                    int n = 150;
                    WindowsMediaBridge.next();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }, "Wݖedia-Next").start();
        }
    }

    public MediaPlaybackState i() {
        return this.e;
    }

    public void b(float f) {
        this.e.b(f * this.e.s());
    }

    public void c(float f) {
        float f2 = f * this.e.s();
        this.e.b(f2);
        this.e.e();
        if (!MediaPlayerHudElement.g() || this.e.s() <= 0.0f) {
            return;
        }
        long l = (long)f2;
        new Thread(() -> {
            try {
                WindowsMediaBridge.seek((long)l);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }, "WMedia-Seek").start();
    }

    private void n() {
        MediaSnapshot co7kmd2 = i.get();
        if (co7kmd2 != null) {
            this.e.a(co7kmd2.a, co7kmd2.b, co7kmd2.c, co7kmd2.d, co7kmd2.e, co7kmd2.f);
        } else {
            this.e.a("No media", "", false, 0.0f, 0.0f, null);
        }
        this.e.m();
    }

    public static synchronized void h() {
        if (g.getAndSet(true)) {
            return;
        }
        f.set(false);
        if (h != null) {
            h.interrupt();
            try {
                h.join(1000L);
            }
            catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        h = null;
        i.set(null);
        float[] fArray = new float[]{0.0f, 0.0f, 0.0f};
        j.set(fArray);
        if (k) {
            try {
                WindowsMediaBridge.shutdown();
            }
            catch (Throwable throwable) {
                System.err.println(MediaPlayerHudElement.$sf$1(throwable.getMessage()));
            }
        }
        k = false;
    }

    public void f() {
        if (MediaPlayerHudElement.g()) {
            new Thread(() -> {
                try {
                    WindowsMediaBridge.previous();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }, "WMedia-Previous").start();
        }
    }

    private static void l() {
        try {
            Object object;
            if (MediaPlayerHudElement.q() && ((Optional)(object = WindowsMediaBridge.getCurrentMedia())).isPresent()) {
                MediaSessionInfo mdnh732 = (MediaSessionInfo)((Optional)object).get();
                String string = mdnh732.getTitle().isEmpty() ? "No media" : mdnh732.getTitle();
                i.set(new MediaSnapshot(string, mdnh732.getArtist(), mdnh732.isPlaying(), mdnh732.getPositionMs(), mdnh732.getDurationMs(), mdnh732.getAlbumArt()));
                return;
            }
            object = DesktopMediaTitlePoller.getInstance();
            String string = ((DesktopMediaTitlePoller)object).getTitle();
            String string2 = ((DesktopMediaTitlePoller)object).getArtist();
            if (string != null && !string.isEmpty()) {
                i.set(new MediaSnapshot(string, string2 != null ? string2 : "", false, 0.0f, 0.0f, null));
            } else {
                i.set(new MediaSnapshot("No media", "", false, 0.0f, 0.0f, null));
            }
        }
        catch (Throwable throwable) {
            k = false;
            i.set(null);
        }
    }

    public void d() {
        int n = this.e.c() ? 0 : 1;
        this.e.a(BooleanCoercion.from(n));
        if (n != 0) {
            this.e.j();
        } else {
            this.e.i();
        }
        if (MediaPlayerHudElement.g()) {
            new Thread(() -> {
                try {
                    WindowsMediaBridge.playPause();
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }, "WMedia-PlayPause").start();
        }
    }

    @EventHandler
    private void a(ClientTickEvent q8krcw2) {
        long l = System.currentTimeMillis();
        if (l - this.r >= 50L) {
            this.r = l;
            this.n();
        }
        if (l - this.s >= 16L) {
            this.s = l;
            this.o();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        MediaPlayerHudElement.j();
    }

    public void a(float f) {
        int n = 964;
        this.e.a(f * this.e.s());
    }

    private static void m() {
        if (!MediaPlayerHudElement.q()) {
            float[] fArray = new float[]{0.0f, 0.0f, 0.0f};
            j.set(fArray);
            return;
        }
        try {
            Optional optional = WindowsMediaBridge.getAudioLevels();
            if (optional.isPresent()) {
                AudioLevelSnapshot calde02 = (AudioLevelSnapshot)optional.get();
                j.set(new float[]{calde02.getMasterPeak(), calde02.getLeftPeak(), calde02.getRightPeak()});
            } else {
                float[] fArray = new float[]{0.0f, 0.0f, 0.0f};
                j.set(fArray);
            }
        }
        catch (Throwable throwable) {
            float[] fArray = new float[]{0.0f, 0.0f, 0.0f};
            j.set(fArray);
        }
    }

    private void o() {
        this.q = this.q - -2 - 1;
        float[] fArray = j.get();
        if (fArray == null || fArray[0] == 0.0f && fArray[1] == 0.0f && fArray[2] == 0.0f) {
            this.p();
            return;
        }
        float f = fArray[0];
        float f2 = fArray[1];
        float f3 = fArray[2];
        float f4 = f > this.p + 0.15f ? (f - this.p) * 2.0f : 0.0f;
        this.p = f * 0.7f + this.p * 0.3f;
        float f5 = Math.min(f * 1.4f, 1.0f);
        float f6 = Math.min(f2 * 1.4f, 1.0f);
        float f7 = Math.min(f3 * 1.4f, 1.0f);
        float f8 = this.q;
        float f9 = (float)(Math.sin((double)f8 * 0.5) * 0.08 + 0.92);
        float f10 = (float)(Math.sin((double)f8 * 0.6 + 1.0) * 0.08 + 0.92);
        float f11 = (float)(Math.sin((double)f8 * 0.7 + 2.0) * 0.08 + 0.92);
        float[] fArray2 = new float[]{(f6 * 0.6f + f4 * 0.3f) * f9, (f6 * 0.85f + f5 * 0.3f + f4 * 0.5f) * f10, (f6 * 0.5f + f5 * 0.7f + f4 * 0.7f) * f11, (f7 * 0.5f + f5 * 0.7f + f4 * 0.7f) * f9, (f7 * 0.85f + f5 * 0.3f + f4 * 0.5f) * f11, (f7 * 0.6f + f4 * 0.3f) * f10};
        for (int i = 0; i < 6; ++i) {
            float f12 = Math.min(fArray2[i], 1.0f);
            this.n[i] = f12 <= this.n[i] ? this.n[i] * 0.6f + f12 * 0.4f : this.n[i] * 0.1f + f12 * 0.9f;
            this.o[i] = this.n[i] <= this.o[i] ? this.o[i] * 0.92f : this.n[i];
            this.e.H()[i] = Math.min(this.n[i] * 0.7f + this.o[i] * 0.3f, 1.0f);
        }
    }

    private void p() {
        for (int i = 0; i < 6; ++i) {
            this.n[i] = this.n[i] * 0.8f;
            this.o[i] = this.o[i] * 0.85f;
            this.e.H()[i] = this.n[i] * 0.7f + this.o[i] * 0.3f;
        }
    }

    private static void k() {
        long l = 0L;
        long l2 = 0L;
        while (f.get() && !g.get()) {
            try {
                long l3 = System.currentTimeMillis();
                if (l3 - l >= 400L) {
                    l = l3;
                    MediaPlayerHudElement.l();
                }
                if (l3 - l2 >= 16L) {
                    l2 = l3;
                    MediaPlayerHudElement.m();
                }
                Thread.sleep(8L);
            }
            catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
            }
            catch (Throwable throwable) {
                System.err.println(MediaPlayerHudElement.$sf$0(throwable.getMessage()));
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static boolean g() {
        return BooleanCoercion.from(!k || g.get() ? 0 : 1);
    }

    private static synchronized void j() {
        if ((h == null || !h.isAlive()) && !g.get()) {
            f.set(true);
            h = new Thread(MediaPlayerHudElement::k, "WMedia-Polling");
            h.setDaemon(true);
            h.setPriority(2);
            h.start();
        }
    }

    private static boolean q() {
        if (g.get()) {
            return false;
        }
        if (!l) {
            l = true;
            try {
                k = WindowsMediaBridge.isAvailable();
            }
            catch (Throwable throwable) {
                k = false;
            }
        }
        return k;
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "Polling error: " + string;
    }

    private static /* synthetic */ String $sf$1(String string) {
        return "Error during shutdown: " + string;
    }
}
