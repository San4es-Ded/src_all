package haron.hud.core;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.config.LocalConfigManager;
import haron.gui.modules.HudElementSettingsPanel;
import haron.hud.core.NormalizedHudPosition;
import haron.hud.snap.SnapResult;
import haron.render.ShapeRenderer;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class HudElement {
    private static final Logger LOG = LogManager.getLogger((String)"haron/config");
    protected float b;
    protected float c;
    protected float d;
    protected float e;
    protected boolean f;
    protected float g;
    protected float h;
    protected boolean j;
    protected boolean k;
    protected int l;
    private SnapResult snapState;
    protected final MinecraftClient a = MinecraftClient.getInstance();
    protected boolean i = true;
    private final AnimatedValue hoverAnimation = new AnimatedValue();
    private final NormalizedHudPosition placement = new NormalizedHudPosition();
    private float lastScreenWidth = -1.0f;
    private float lastScreenHeight = -1.0f;
    private final HudElementSettingsPanel settingsPanel = new HudElementSettingsPanel();

    public HudElement(float f, float f2) {
        this.b = f;
        this.c = f2;
        this.hoverAnimation.d(0.0);
    }

    public void e() {
        this.hoverAnimation.a();
    }

    private static float clamp(float f, float f2, float f3) {
        return f3 < f2 ? f2 : Math.max(f2, Math.min(f, f3));
    }

    public boolean i() {
        int n = 167;
        return this.f;
    }

    public void b(float f, float f2) {
        this.placement.a(this.b, this.c, this.d, this.e, f, f2);
        this.lastScreenWidth = f;
        this.lastScreenHeight = f2;
    }

    public void b() {
        this.a();
    }

    public void b(float f) {
        this.c = f;
    }

    public void b(double d, double d2, int n) {
        if (n == 0) {
            if (this.f) {
                LOG.info("[Haron] HUD moved -> nx={}, ny={}", (Object)Float.valueOf(this.placement.a()), (Object)Float.valueOf(this.placement.b()));
                LocalConfigManager.get().flushSaveNow("hud-move");
            }
            this.f = false;
            this.snapState = null;
        }
        this.settingsPanel.b((int)d, (int)d2);
    }

    public float s() {
        return this.h;
    }

    public boolean c() {
        int n = 123;
        return true;
    }

    public float n() {
        return this.d;
    }

    public float h() {
        return (float)this.hoverAnimation.j();
    }

    public HudElementSettingsPanel f() {
        int n = 910;
        return this.settingsPanel;
    }

    public float l() {
        return this.b;
    }

    public void d() {
        this.j = false;
        this.k = false;
        this.f = false;
        this.settingsPanel.b();
        if (this.hoverAnimation.j() > 0.0 || this.hoverAnimation.i() > 0.0) {
            this.hoverAnimation.a(0.0, 0.15, Easings.h);
        }
    }

    public boolean a(double d, double d2) {
        return d >= (double)this.b && d <= (double)(this.b + this.d) && d2 >= (double)this.c && d2 <= (double)(this.c + this.e);
    }

    public void a(boolean bl) {
        this.i = bl;
    }

    public void a(float f) {
        this.b = f;
    }

    public void a(ShapeRenderer s7swsm2, MatrixStack matrixStack) {
        float f = this.h();
        if (f < 0.01f) {
            return;
        }
        s7swsm2.a(this.b, this.c, this.d, this.e, 13.0f * this.g(), new Color(130, 105, 255, (int)(25.0f * f)), matrixStack);
    }

    public void a(int n) {
        this.l = n;
    }

    public void a(float f, float f2) {
        if (this.lastScreenWidth <= 0.0f || this.lastScreenHeight <= 0.0f) {
            this.b = HudElement.clamp(this.b, 5.0f, f - this.d - 5.0f);
            this.c = HudElement.clamp(this.c, 5.0f, f2 - this.e - 5.0f);
        } else {
            this.b = HudElement.clamp(this.placement.a(this.d, f), 5.0f, f - this.d - 5.0f);
            this.c = HudElement.clamp(this.placement.b(this.e, f2), 5.0f, f2 - this.e - 5.0f);
        }
        this.lastScreenWidth = f;
        this.lastScreenHeight = f2;
    }

    protected abstract void a();

    public void a(double d, double d2, boolean bl) {
        boolean bl2;
        this.j = this.a(d, d2);
        this.k = bl;
        boolean bl3 = bl2 = this.j && bl || this.f;
        if (bl2 && this.hoverAnimation.i() < 1.0) {
            this.hoverAnimation.a(1.0, 0.15, Easings.h);
        } else {
            if (bl2 || this.hoverAnimation.i() <= 0.0) {
                return;
            }
            this.hoverAnimation.a(0.0, 0.2, Easings.h);
        }
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        this.settingsPanel.b(this.b, this.c, this.d, this.e);
        this.settingsPanel.a(matrixStack, s7swsm2, n, n2);
    }

    public void a(double d, double d2, float f, float f2) {
        if (this.f) {
            float f3 = (float)d - this.g;
            float f4 = (float)d2 - this.h;
            this.b = HudElement.clamp(f3, 5.0f, f - this.d - 5.0f);
            this.c = HudElement.clamp(f4, 5.0f, f2 - this.e - 5.0f);
            this.placement.a(this.b, this.c, this.d, this.e, f, f2);
            this.lastScreenWidth = f;
            this.lastScreenHeight = f2;
        }
    }

    public void a(SnapResult comxfd2, float f, float f2) {
        if (!this.f || comxfd2 == null) {
            return;
        }
        this.b = comxfd2.a();
        this.c = comxfd2.b();
        this.snapState = comxfd2;
        this.placement.a(this.b, this.c, this.d, this.e, f, f2);
        this.lastScreenWidth = f;
        this.lastScreenHeight = f2;
    }

    public boolean a(double d, double d2, int n) {
        if (this.f) {
            return true;
        }
        int n2 = (int)d;
        int n3 = (int)d2;
        if (this.settingsPanel.c()) {
            if (this.settingsPanel.a(n2, n3)) {
                return true;
            }
            if (!this.settingsPanel.c(n2, n3)) {
                this.settingsPanel.a();
                return false;
            }
        }
        if (n != 0 || !this.a(d, d2)) {
            return false;
        }
        if (this.settingsPanel.c()) {
            this.settingsPanel.a();
        }
        this.f = true;
        this.g = (float)d - this.b;
        this.h = (float)d2 - this.c;
        return true;
    }

    public boolean a(double d, double d2, double d3) {
        return this.settingsPanel.a((float)d3, (int)d, (int)d2);
    }

    public void a(double d, double d2, double d3, double d4) {
        this.settingsPanel.a((int)d, (int)d2, d3, d4);
    }

    public abstract void a(MatrixStack var1, ShapeRenderer var2, float var3, float var4);

    public float m() {
        return this.c;
    }

    public float o() {
        return this.e;
    }

    public int p() {
        return this.l;
    }

    public boolean k() {
        return this.j;
    }

    public NormalizedHudPosition t() {
        return this.placement;
    }

    public float g() {
        return this.settingsPanel.i();
    }

    public boolean j() {
        return this.i;
    }

    public SnapResult q() {
        return this.snapState;
    }

    public float r() {
        return this.g;
    }
}

