package haron.gui.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.settings.ColorSettingRow;
import haron.gui.settings.ModeSettingRow;
import haron.gui.settings.NumberSettingRow;
import haron.gui.settings.SettingRow;
import haron.gui.settings.SettingsList;
import haron.gui.settings.ToggleSettingRow;
import haron.gui.settings.KeybindSettingRow;
import haron.module.HaronModule;
import haron.render.icons.IconTexture;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.KeybindSetting;
import haron.settings.Setting;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class HudElementSettingsPanel {
    public static final float a = 140.0f;
    public static final float b = 38.0f;
    public static final float c = 108.0f;
    public static final float d = 9.5f;
    public static final float e = 7.0f;
    private float l;
    private float m;
    private float n;
    private float o;
    private float p;
    private float q;
    private static final float s = 8.0f;
    private NumberSettingRow u;
    public static int f;
    public static boolean g;
    private final AnimatedValue h = new AnimatedValue();
    private boolean i = false;
    private boolean j = false;
    private float k = 140.0f;
    private boolean r = true;
    private boolean v = false;
    private final SettingsList t = new SettingsList();

    public HudElementSettingsPanel() {
        this.t.b(7.0f);
        this.t.a(108.0f);
        this.u = new NumberSettingRow("Масштаб", 1.0f, 1.0f, 2.0f, 0.1f);
        this.u.a(true);
        this.t.a(this.u);
        this.h.d(0.0);
    }

    public boolean e() {
        return this.i && !this.j && !(this.h.j() <= 0.5);
    }

    public float i() {
        int n = 669;
        return this.u.e();
    }

    public void b(int n, int n2) {
        if (this.i) {
            this.t.c(n, n2);
        }
    }

    public void b() {
        this.i = false;
        this.j = false;
        this.h.d(0.0);
        this.t.g();
    }

    public void b(float f, float f2, float f3, float f4) {
        this.n = f;
        this.o = f2;
        this.p = f3;
        this.q = f4;
    }

    public boolean c(int n, int n2) {
        return !this.i || this.h.j() < 0.5 ? false : GuiInput.a(this.l, this.m, this.k, this.h(), (double)n, (double)n2);
    }

    public boolean c() {
        return this.i;
    }

    public float h() {
        return Math.max(38.0f, Math.min(this.t.m(), 108.0f));
    }

    public boolean f() {
        return this.j && this.h.d() && this.h.j() < 0.01;
    }

    public boolean d() {
        return this.j;
    }

    public void a(SettingRow um973w2) {
        int n = 686;
        this.t.a(um973w2);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        if (this.i) {
            this.g();
            float f = (float)this.h.j();
            if (f >= 0.01f) {
                float f2;
                float f3 = this.h();
                this.l = Math.round(this.n + this.p / 2.0f - this.k / 2.0f);
                this.m = this.r ? (float)Math.round(this.o - f3 - 8.0f) : (float)Math.round(this.o + this.q + 8.0f);
                float f4 = Math.max(0.0f, Math.min(1.0f, f));
                int n3 = (int)(255.0f * f4);
                float f5 = this.l + this.k / 2.0f;
                float f6 = this.m + f3 / 2.0f;
                float f7 = this.k * f4;
                float f8 = f3 * f4;
                if (f7 < 1.0f || f8 < 1.0f) {
                    return;
                }
                float f9 = f5 - f7 / 2.0f;
                float f10 = f6 - f8 / 2.0f;
                Color color = pryrvd.a(pryrvd.PANEL_BORDER_TOP, n3);
                Color color2 = pryrvd.a(pryrvd.PANEL_BORDER_BOT, n3);
                s7swsm2.a(f9 - 0.5f * f4, f10 - 0.5f * f4, f7 + f4, f8 + f4, 9.5f * f4, color, color, color2, color2, matrixStack);
                Color color3 = pryrvd.a(pryrvd.CELL_BG_TOP, n3);
                Color color4 = pryrvd.a(pryrvd.CELL_BG_BOT, n3);
                s7swsm2.a(f9, f10, f7, f8, 9.5f * f4, color3, color3, color4, color4, matrixStack);
                IconTexture hclqea2 = HaronIcons.getInfo("arrow_v");
                if (hclqea2 != null) {
                    float f11;
                    float f12;
                    Identifier identifier = hclqea2.a();
                    RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
                    f2 = (float)hclqea2.b() / 2.0f * f4;
                    float f13 = (float)hclqea2.c() / 2.0f * f4;
                    float f14 = f9 + f7 / 2.0f;
                    if (this.r) {
                        f12 = f10 + f8 + f2 / 2.0f + 1.0f;
                        f11 = 90.0f;
                    } else {
                        f12 = f10 - f2 / 2.0f - 1.0f;
                        f11 = -90.0f;
                    }
                    Color color5 = pryrvd.a(pryrvd.aa, n3);
                    matrixStack.push();
                    matrixStack.translate(f14, f12, 0.0f);
                    matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f11));
                    matrixStack.translate(-f14, -f12, 0.0f);
                    s7swsm2.a(identifier, f14 - f2 / 2.0f - 0.5f, f12 - f13 / 2.0f, f2, f13, color5, matrixStack);
                    matrixStack.pop();
                }
                if (f4 <= 0.1f) {
                    return;
                }
                float f15 = f10 + 7.0f * f4;
                f2 = f8 - 7.0f * f4 - 6.0f * f4;
                if (f2 < 1.0f || f7 < 1.0f) {
                    return;
                }
                s7swsm2.b().a(f9, f15, f7, f2, 9.5f * f4, matrixStack);
                this.t.a(matrixStack, s7swsm2, f9, f10, f7, f8, !this.j ? n : -1, !this.j ? n2 : -1, f4, f4);
                s7swsm2.b().a(matrixStack);
            }
        }
    }

    public void a(HaronModule jxs16t2) {
        if (this.v || jxs16t2 == null) {
            return;
        }
        this.t.a();
        this.u = null;
        for (Setting<?> mmdapc2 : jxs16t2.m()) {
            SettingRow um973w2;
            if (!mmdapc2.m() || (um973w2 = this.a(mmdapc2)) == null) continue;
            this.t.a(um973w2);
            if (this.u != null || !(um973w2 instanceof NumberSettingRow) || !mmdapc2.f().equals("Масштаб")) continue;
            this.u = (NumberSettingRow)um973w2;
            this.u.a(true);
        }
        if (this.u == null) {
            this.u = new NumberSettingRow("Масштаб", 1.0f, 1.0f, 2.0f, 0.1f);
            this.u.a(true);
            this.t.a(0, this.u);
        }
        this.v = true;
    }

    @Deprecated
    public void a(float f) {
        this.u.a(Math.max(1.0f, Math.min(2.0f, f)));
    }

    public boolean a(int n, int n2) {
        return !this.i || this.j || this.h.j() < 0.5 ? false : (this.c(n, n2) ? this.t.a(this.l, this.m, this.k, this.h(), n, n2) : false);
    }

    private SettingRow a(Setting<?> mmdapc2) {
        if (mmdapc2 instanceof BooleanSetting) {
            return new ToggleSettingRow((BooleanSetting)mmdapc2);
        }
        if (mmdapc2 instanceof NumberSetting) {
            return new NumberSettingRow((NumberSetting)mmdapc2);
        }
        if (mmdapc2 instanceof ModeSetting) {
            return new ModeSettingRow((ModeSetting)mmdapc2);
        }
        if (mmdapc2 instanceof ColorSetting) {
            return new ColorSettingRow((ColorSetting)mmdapc2);
        }
        if (mmdapc2 instanceof KeybindSetting) {
            return new KeybindSettingRow((KeybindSetting)mmdapc2);
        }
        return null;
    }

    public void a() {
        if (this.i) {
            this.j = true;
            this.t.g();
            this.h.a(0.0, 0.09, Easings.g);
        }
    }

    public void a(float f, float f2, float f3, float f4) {
        if (this.i && !this.j) {
            this.a();
            return;
        }
        this.n = f;
        this.o = f2;
        this.p = f3;
        this.q = f4;
        float f5 = this.h();
        this.r = BooleanCoercion.from(f2 - 8.0f < f5 ? 0 : 1);
        this.l = Math.round(f + f3 / 2.0f - this.k / 2.0f);
        this.m = this.r ? (float)Math.round(f2 - f5 - 8.0f) : (float)Math.round(f2 + f4 + 8.0f);
        this.i = true;
        this.j = false;
        this.h.a(1.0, 0.15, Easings.F);
    }

    public void a(int n, int n2, double d, double d2) {
        if (!this.i || this.j) {
            return;
        }
        this.t.a(this.h(), n, n2, d, d2);
    }

    public boolean a(float f, int n, int n2) {
        if (!this.i || this.j) {
            return false;
        }
        if (this.c(n, n2)) {
            this.t.a(f, this.h());
            return true;
        }
        return false;
    }

    @Deprecated
    public void a(NumberSetting by6erl2) {
        if (this.v) {
            return;
        }
        this.t.b(this.u);
        this.u = new NumberSettingRow(by6erl2);
        this.u.a(true);
        this.t.a(0, this.u);
    }

    public void g() {
        this.h.a();
        if (this.f()) {
            this.i = false;
            this.j = false;
        }
    }

    public SettingsList j() {
        return this.t;
    }
}

