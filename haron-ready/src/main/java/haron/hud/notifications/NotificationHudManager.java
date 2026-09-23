package haron.hud.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.hud.core.HudManager;
import haron.hud.core.HudServices;
import haron.hud.notifications.NotificationSettingsPopup;
import haron.hud.notifications.cmoikn;
import haron.hud.notifications.ijsfy3;
import haron.hud.notifications.tfzl8f;
import haron.hud.notifications.uqw6p7;
import haron.media.MediaPlayerHudElement;
import haron.media.MediaPlaybackState;
import haron.module.ModuleManager;
import haron.render.ShapeRenderer;
import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import ru.haron.mixin.accessor.BossBarHudAccessor;
import ru.haron.mixin.accessor.PlayerListHudAccessor;

public class NotificationHudManager {
    private static NotificationHudManager instance;
    private static final MinecraftClient b;
    private static final Color c;
    private static final Color d;
    private static final Color e;
    private static final Color f;
    private static final float g = 12.0f;
    private static final float h = 7.0f;
    private static final float i = 19.0f;
    private float s;
    private float t;
    private float u;
    private float v;
    private static final long z = 3000L;
    private static final long D = 300L;
    private final tfzl8f j = new tfzl8f();
    private final Queue<ijsfy3> k = new LinkedList<ijsfy3>();
    private ijsfy3 l = null;
    private uqw6p7 m = null;
    private final AnimatedValue n = new AnimatedValue();
    private final AnimatedValue o = new AnimatedValue();
    private final AnimatedValue p = new AnimatedValue();
    private final AnimatedValue q = new AnimatedValue();
    private final AnimatedValue r = new AnimatedValue();
    private final NotificationSettingsPopup settingsPopup = new NotificationSettingsPopup();
    private long x = 0L;
    private boolean y = false;
    private int A = 0;
    private int B = 0;
    private long C = 0L;
    private final AnimatedValue E = new AnimatedValue();
    private boolean F = false;

    public NotificationHudManager() {
        instance = this;
        float f = this.j.c();
        float f2 = this.j.d();
        this.n.d(f);
        this.o.d(f2);
        this.p.d(7.0);
        this.q.d(1.0);
        this.r.d(0.0);
        this.E.d(1.0);
        this.s = f;
        this.t = f2;
        this.u = 7.0f;
    }

    static {
        b = MinecraftClient.getInstance();
        c = new Color(19, 19, 25);
        d = new Color(19, 19, 26);
        e = new Color(13, 13, 17);
        f = new Color(17, 17, 23);
    }

    public float e() {
        return this.s;
    }

    public NotificationSettingsPopup settingsPopup() {
        return this.settingsPopup;
    }

    @Deprecated
    public static void b(String string, String string2) {
        NotificationHudManager.b();
    }

    public void b(double d, double d2, int n) {
        if (this.settingsPopup.isOpen()) {
            this.settingsPopup.mouseMoved((int)d, (int)d2);
        }
        if (this.l instanceof cmoikn) {
            ((cmoikn)this.l).b(d, d2, n);
        }
    }

    private boolean b(double d, double d2) {
        return BooleanCoercion.from(d < (double)this.v || d > (double)(this.v + this.s) || d2 < (double)this.u || d2 > (double)(this.u + this.t) ? 0 : 1);
    }

    private void b(ijsfy3 ijsfy32) {
    }

    public static void b() {
    }

    private void b(String string, boolean bl) {
    }

    public static void c() {
    }

    private float n() {
        int n = this.m();
        long l = System.currentTimeMillis();
        if (n > this.B) {
            this.B = n;
            this.C = 0L;
        } else if (n >= this.B) {
            this.C = 0L;
        } else if (this.C == 0L) {
            this.C = l;
        } else if (l - this.C >= 300L) {
            this.B = n;
            this.C = 0L;
        }
        this.A = n;
        if (this.B == 0) {
            return 7.0f;
        }
        return 7.0f + (float)((double)((float)this.B * 19.0f) * b.getWindow().getScaleFactor() / 2.0);
    }

    public ijsfy3 h() {
        return this.l;
    }

    public float f() {
        return this.t;
    }

    private void l() {
        float f;
        float f2;
        this.k();
        if (this.m != null) {
            this.m.e();
            if (this.m.g() || this.m.h()) {
                this.m = null;
            }
        }
        if (this.l != null) {
            this.l.e();
            if (this.l instanceof cmoikn) {
                f2 = this.l.c();
                f = this.l.d();
                if (this.r.j() >= 0.99) {
                    this.n.d(f2);
                    this.o.d(f);
                }
            } else {
                f2 = this.l.c();
                f = this.l.d();
                if (Math.abs(this.n.i() - (double)f2) > (double)0.1f) {
                    this.n.a((double)f2, 0.25, Easings.k);
                }
                if (Math.abs(this.o.i() - (double)f) > (double)0.1f) {
                    this.o.a((double)f, 0.25, Easings.k);
                }
            }
            if (this.l.g()) {
                this.l = null;
                if (this.m != null && !this.m.h()) {
                    this.l = this.m;
                    this.m = null;
                    f2 = this.l.c();
                    f = this.l.d();
                    this.n.a((double)f2, 0.3, Easings.k);
                    this.o.a((double)f, 0.3, Easings.k);
                    this.r.a(1.0, 0.2, Easings.k);
                    this.q.a(0.0, 0.15, Easings.k);
                } else if (this.j()) {
                    this.l = new cmoikn();
                    f2 = this.l.c();
                    f = this.l.d();
                    this.n.a((double)f2, 0.3, Easings.k);
                    this.o.a((double)f, 0.3, Easings.k);
                    this.r.d(0.0);
                    this.r.a(1.0, 0.2, Easings.k);
                    this.q.a(0.0, 0.15, Easings.k);
                } else {
                    this.r.a(0.0, 0.2, Easings.k);
                    this.q.a(1.0, 0.2, Easings.k);
                    f2 = this.j.c();
                    f = this.j.d();
                    this.n.a((double)f2, 0.3, Easings.k);
                    this.o.a((double)f, 0.3, Easings.k);
                }
            }
        }
        if (this.l == null && !this.k.isEmpty()) {
            this.l = this.k.poll();
            f2 = this.l.c();
            f = this.l.d();
            this.n.a((double)f2, 0.3, Easings.k);
            this.o.a((double)f, 0.3, Easings.k);
            this.r.a(1.0, 0.2, Easings.k);
            this.q.a(0.0, 0.15, Easings.k);
        }
        this.n.a();
        this.o.a();
        this.q.a();
        this.r.a();
        f2 = this.n();
        if (Math.abs(this.u - f2) > 0.5f) {
            this.p.a(f2, 0.3, Easings.k, true);
        }
        this.p.a();
        this.s = (float)this.n.j();
        this.t = (float)this.o.j();
        this.u = (float)this.p.j();
        if (this.l == null && Math.abs(this.s - (f = this.j.c())) > 0.5f) {
            this.n.a(f, 0.3, Easings.k, true);
        }
    }

    public void d() {
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2) {
        if (NotificationHudManager.b.world == null || NotificationHudManager.b.player == null) {
            return;
        }
        this.l();
        boolean bl = this.o();
        if (bl != this.F) {
            if (bl) {
                this.E.a(0.0, 0.15, Easings.k);
                if (this.settingsPopup.isOpen()) {
                    this.settingsPopup.close();
                }
            } else {
                this.E.a(1.0, 0.2, Easings.k);
            }
            this.F = bl;
        }
        this.E.a();
        float f = (float)this.E.j();
        if (f >= 0.01f) {
            float f2 = (float)((double)Math.round((double)((float)b.getWindow().getFramebufferWidth() / 2.0f / 2.0f - this.s / 2.0f) * 2.0) / 2.0);
            float f3 = this.u;
            this.v = f2;
            float f4 = (float)this.q.j() * f;
            float f5 = (float)this.r.j() * f;
            tfzl8f tfzl8f2 = this.j;
            if (tfzl8f2 instanceof tfzl8f) {
                tfzl8f2.renderStandalone(matrixStack, s7swsm2, f2, f3, f);
            } else {
                Color color = this.a(c, f);
                Color color2 = this.a(d, f);
                Color color3 = this.a(e, f);
                Color color4 = this.a(NotificationHudManager.f, f);
                s7swsm2.a(f2 - 1.0f, f3 - 1.0f, this.s + 2.0f, this.t + 2.0f, 12.0f, color, color, color2, color2, matrixStack);
                s7swsm2.a(f2, f3, this.s, this.t, 12.0f, color3, color3, color4, color4, matrixStack);
                s7swsm2.b().a(f2, f3, this.s, this.t, 12.0f, matrixStack);
                if (f4 > 0.01f) {
                    this.j.a(matrixStack, s7swsm2, f2 + this.j.j(), f3 + this.j.k(), this.s - this.j.j() * 2.0f, this.t - this.j.k() * 2.0f, f4);
                }
            }
            if (this.l != null && f5 > 0.01f) {
                this.l.a(matrixStack, s7swsm2, f2 + this.l.j(), f3 + this.l.k(), this.s - this.l.j() * 2.0f, this.t - this.l.k() * 2.0f, f5);
            }
            s7swsm2.b().a(matrixStack);
            if (this.settingsPopup.isOpen()) {
                this.settingsPopup.updateAnchorBounds(f2, f3, this.s, this.t);
                double d = b.getWindow().getScaleFactor();
                this.settingsPopup.render(matrixStack, s7swsm2, (int)(NotificationHudManager.b.mouse.getX() * d / 2.0), (int)(NotificationHudManager.b.mouse.getY() * d / 2.0));
            }
        }
    }

    public static void a(String string, boolean bl) {
    }

    private Color a(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * f));
    }

    public void a(double d, double d2) {
        ijsfy3 ijsfy32;
        ijsfy3 ijsfy33 = this.l;
        if (ijsfy33 instanceof cmoikn) {
            ijsfy32 = (cmoikn)ijsfy33;
            if (HudManager.a().p()) {
                ((cmoikn)ijsfy32).n();
            } else {
                ((cmoikn)ijsfy32).a(d, d2);
                ((cmoikn)ijsfy32).m();
            }
        }
        if ((ijsfy32 = this.l) instanceof uqw6p7) {
            ((uqw6p7)ijsfy32).n();
        }
    }

    public void a(double d, double d2, double d3, double d4) {
        if (this.settingsPopup.isOpen()) {
            this.settingsPopup.mouseDragged((int)d, (int)d2, d3, d4);
        }
        if (this.l instanceof cmoikn) {
            ((cmoikn)this.l).b(d, d2);
        }
    }

    public boolean a(double d, double d2, double d3) {
        return this.settingsPopup.isOpen() ? this.settingsPopup.mouseScrolled((float)d3, (int)d, (int)d2) : false;
    }

    public static void a(String string) {
    }

    public boolean a(double d, double d2, int n) {
        int n2 = 531;
        return false;
    }

    public static void a(String string, String string2) {
    }

    public static void a(String string, String string2, String string3) {
    }

    public void a(ijsfy3 ijsfy32) {
        this.b(ijsfy32);
    }

    public static NotificationHudManager getInstance() {
        return instance;
    }

    public static void a(uqw6p7 uqw6p72) {
    }

    private int m() {
        BossBarHudAccessor bossBarHudAccessor;
        if (NotificationHudManager.b.inGameHud == null) {
            return 0;
        }
        if (!ModuleManager.RENDER_TWEAKS.s() && (bossBarHudAccessor = (BossBarHudAccessor)NotificationHudManager.b.inGameHud.getBossBarHud()) != null) {
            return bossBarHudAccessor.getBossBars().size();
        }
        return 0;
    }

    private boolean o() {
        PlayerListHudAccessor playerListHudAccessor;
        if (NotificationHudManager.b.inGameHud != null && (playerListHudAccessor = (PlayerListHudAccessor)NotificationHudManager.b.inGameHud.getPlayerListHud()) != null) {
            return playerListHudAccessor.isVisible();
        }
        return false;
    }

    private void k() {
        MediaPlaybackState xukx3d2;
        block7: {
            block6: {
                if (!this.settingsPopup.areMusicNotificationsEnabled()) break block6;
                MediaPlayerHudElement dvkv1a2 = HudServices.MEDIA;
                xukx3d2 = dvkv1a2.i();
                if (xukx3d2 != null) break block7;
            }
            return;
        }
        if (xukx3d2.k()) {
            this.y = false;
            NotificationHudManager.b();
        }
        if (xukx3d2.l()) {
            this.y = false;
            NotificationHudManager.c();
        }
        if (!this.y || !xukx3d2.c() || this.settingsPopup.isOpen() || System.currentTimeMillis() - this.x < 3000L) {
            return;
        }
        this.y = false;
        NotificationHudManager.b();
    }

    public void g() {
        if (this.l instanceof cmoikn) {
            ((cmoikn)this.l).n();
        }
    }

    private boolean j() {
        if (this.settingsPopup.areMusicNotificationsEnabled() && !this.y) {
            MediaPlayerHudElement dvkv1a2 = HudServices.MEDIA;
            MediaPlaybackState xukx3d2 = dvkv1a2.i();
            if (xukx3d2 != null) {
                return xukx3d2.c();
            }
        }
        return false;
    }
}
