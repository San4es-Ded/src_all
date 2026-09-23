package haron.gui.settings;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.settings.SettingRow;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.settings.NumberSetting;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.Locale;
import net.minecraft.client.util.math.MatrixStack;

public class NumberSettingRow
implements SettingRow {
    public static final float a = 28.0f;
    private static final float d = 8.0f;
    private static final float e = 6.0f;
    private static final float f = 20.0f;
    private static final float g = 3.0f;
    private static final float h = 7.0f;
    private static final float i = 8.0f;
    private final String m;
    private float n;
    private final float o;
    private final float p;
    private final float q;
    private final NumberSetting r;
    private final AnimatedValue s = new AnimatedValue();
    private final AnimatedValue t = new AnimatedValue();
    private final AnimatedValue u = new AnimatedValue();
    private final AnimatedValue v = new AnimatedValue();
    private final AnimatedValue w = new AnimatedValue();
    private final AnimatedValue x = new AnimatedValue();
    private final cqqvax y = new cqqvax();
    private boolean z = false;
    private boolean A = false;
    private float B;
    private float C = 0.0f;
    private float D = 0.0f;
    private boolean E = false;
    private float F;
    private final AnimatedValue G = new AnimatedValue();
    private final AnimatedValue H = new AnimatedValue();
    public static int b;
    public static boolean c;

    private NumberSettingRow(NumberSetting by6erl2, String string, float f, float f2, float f3, float f4) {
        this.r = by6erl2;
        this.m = string;
        this.n = f;
        this.o = f2;
        this.p = f3;
        this.q = f4;
        this.B = f;
        this.F = f;
        this.w.d(this.i());
        this.G.d(this.i());
        this.H.d(f);
        this.u.d(0.0);
        this.v.d(0.0);
        this.x.d(0.0);
    }

    public NumberSettingRow(NumberSetting by6erl2) {
        this(by6erl2, by6erl2.f(), by6erl2.a(), by6erl2.c(), by6erl2.d(), by6erl2.e());
    }

    public NumberSettingRow(String string, float f, float f2, float f3, float f4) {
        this(null, string, f, f2, f3, f4);
    }

    public float e() {
        float f;
        if (this.r != null && !this.A && Math.abs(this.n - (f = this.r.a())) > 0.001f) {
            this.F = this.n = f;
            if (this.E) {
                this.H.d(this.n);
            }
            this.w.d(this.i());
            this.G.d(this.i());
            this.B = this.n;
        }
        return !this.E ? this.n : (float)this.H.j();
    }

    private float i() {
        if (this.p == this.o) {
            return 0.0f;
        }
        return ((this.E && this.A ? this.F : this.n) - this.o) / (this.p - this.o);
    }

    private String b(float f) {
        return this.q < 1.0f ? (this.q < 0.1f ? String.format(Locale.US, "%.2f", Float.valueOf(f)) : String.format(Locale.US, "%.1f", Float.valueOf(f))) : String.valueOf((int)f);
    }

    @Override
    public float b() {
        return 28.0f;
    }

    public boolean c() {
        int n = 840;
        return this.E;
    }

    public float h() {
        return this.q;
    }

    public float f() {
        return this.o;
    }

    @Override
    public boolean d() {
        int n = this.r == null || this.r.m() ? 1 : 0;
        return BooleanCoercion.from(n);
    }

    @Override
    public void a(int n, int n2, double d, double d2) {
        if (this.A) {
            if (this.D > 0.0f) {
                this.a(this.C, this.D, n);
            } else if (c) {
                // empty if block
            }
        }
    }

    private void a(float f, float f2, int n) {
        float f3 = Math.max(this.o, Math.min(this.p, (float)Math.round((this.o + (this.p - this.o) * Math.max(0.0f, Math.min(1.0f, ((float)n - f) / f2))) / this.q) * this.q));
        if (this.E) {
            this.F = f3;
            return;
        }
        this.n = f3;
        if (this.r != null) {
            this.r.a(f3);
        }
    }

    public void a(boolean bl) {
        this.E = bl;
    }

    @Override
    public String a() {
        return this.m;
    }

    public void a(float f) {
        this.n = Math.max(this.o, Math.min(this.p, f));
        if (this.r != null) {
            this.r.a(this.n);
        }
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        float f6;
        float f7;
        if (this.r != null && !this.A && Math.abs(this.n - this.r.a()) > 0.001f) {
            this.F = this.n = this.r.a();
            this.w.d(this.i());
            this.G.d(this.i());
            this.H.d(this.n);
            this.B = this.n;
        }
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f8 = 8.0f * f5;
        boolean bl = GuiInput.a(f, f2, f3, 28.0f * f5, (double)n, (double)n2);
        this.y.a(bl);
        if (bl != this.z) {
            this.s.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.z = bl;
        }
        if (this.n != this.B) {
            this.w.a((double)this.i(), 0.15, Easings.C);
            this.x.d(1.0);
            this.x.a(0.0, 0.4, Easings.h);
            this.B = this.n;
        }
        this.s.a();
        this.t.a();
        this.u.a();
        this.v.a();
        this.w.a();
        this.x.a();
        this.H.a();
        this.G.a();
        float f9 = (float)this.s.j();
        float f10 = (float)this.t.j();
        float f11 = (float)this.u.j();
        if (this.E && this.A) {
            this.G.a((double)this.i(), 0.1, Easings.h);
            f7 = (float)this.G.j();
        } else {
            f7 = (float)this.w.j();
            this.G.d(f7);
        }
        int n3 = (int)(255.0f * f4);
        String string = this.b(this.m());
        float f12 = v6hnga2.a(string) * f5;
        float f13 = f12 + 5.0f * f5 * 2.0f;
        float f14 = f + f3 - f8 - f13;
        float f15 = f14 - (f + f8) - 4.0f * f5;
        float f16 = f2 + 6.0f * f5;
        this.y.a(matrixStack, s7swsm2, v6hnga2, this.m, (int)(f + f8), (int)f16 - 1, f15, f5, pryrvd.a, f4);
        float f17 = v6hnga2.b(string) * f5;
        float f18 = 14.0f * f5;
        float f19 = f16 + v6hnga2.b(this.m) * f5 / 2.0f - f18 / 2.0f - 5.0f * f5;
        float f20 = 4.0f * f5;
        Color color = pryrvd.a(pryrvd.m, n3);
        Color color2 = pryrvd.a(pryrvd.r, n3);
        s7swsm2.a(f14 - 0.5f * f5, f19 - 0.5f * f5, f13 + f5, f18 + f5, f20, color, color, color2, color2, matrixStack);
        Color color3 = pryrvd.a(pryrvd.WIDGET_BG_TOP, n3);
        Color color4 = pryrvd.a(pryrvd.WIDGET_BG_BOT, n3);
        s7swsm2.a(f14, f19, f13, f18, f20, color3, color3, color4, color4, matrixStack);
        float f21 = f14 + f13 / 2.0f;
        float f22 = f19 + f18 / 2.0f;
        float f23 = Math.round(f21 - f12 / 2.0f);
        float f24 = Math.round(f22 - f17 / 2.0f);
        Color color5 = pryrvd.a(pryrvd.a, n3);
        matrixStack.push();
        matrixStack.translate(f23, f24, 0.0f);
        matrixStack.scale(f5, f5, 1.0f);
        matrixStack.translate(-f23, -f24, 0.0f);
        v6hnga2.a(string, f23, (double)(f24 + 3.5f), color5, matrixStack);
        matrixStack.pop();
        float f25 = f2 + 20.0f * f5;
        float f26 = f3 - f8 * 2.0f;
        float f27 = 3.0f * f5;
        float f28 = f27 / 2.0f;
        this.C = f6 = f + f8;
        this.D = f26;
        s7swsm2.a(f6, f25, f26, f27, f28, pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.P, f9), n3), matrixStack);
        float f29 = f26 * f7;
        if (f29 > 0.0f) {
            Color color6 = ColorUtils.a(pryrvd.y, pryrvd.Q, f10);
            Color color7 = ColorUtils.a(pryrvd.z, pryrvd.R, f10);
            Color color8 = pryrvd.a(color6, n3);
            Color color9 = pryrvd.a(color7, n3);
            if (f29 < f28 * 2.0f) {
                s7swsm2.a(f6, f25, f29, f27, f29 / 2.0f, color8, color8, color9, color9, matrixStack);
            } else {
                s7swsm2.a(f6, f25, f29, f27, f28, color8, color8, color9, color9, matrixStack);
            }
        }
        float f30 = (7.0f + 1.0f * f11) * f5;
        s7swsm2.a(Math.max(f6 - f30 / 2.0f, Math.min(f6 + f29 - f30 / 2.0f, f6 + f26 - f30 / 2.0f)), f25 + f27 / 2.0f - f30 / 2.0f, f30, f30, f30, pryrvd.a(pryrvd.a, n3), matrixStack);
        if (GuiInput.a(f6 - f30 / 2.0f, f25 - 5.0f * f5, f26 + f30, f27 + 10.0f * f5, (double)n, (double)n2) || this.A) {
            GuiInput.g();
        }
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        float f4;
        float f5 = f3 - 16.0f;
        this.C = f4 = f + 8.0f;
        this.D = f5;
        if (!GuiInput.a(f4 - 3.5f, f2 + 20.0f - 5.0f, f5 + 7.0f, 13.0f, (double)n, (double)n2)) {
            return false;
        }
        this.A = true;
        if (this.E) {
            this.F = this.n;
        }
        this.t.a(1.0, 0.1, Easings.h);
        this.u.a(1.0, 0.15, Easings.F);
        this.v.a(1.0, 0.2, Easings.h);
        this.a(f4, f5, n);
        return true;
    }

    @Override
    public void a(int n, int n2) {
        if (this.A) {
            if (this.E) {
                this.n = this.F;
                if (this.r != null) {
                    this.r.a(this.n);
                }
                this.w.d(this.G.j());
                this.B = this.n;
                this.H.a((double)this.n, 0.2, Easings.C);
            }
            this.A = false;
            this.t.a(0.0, 0.2, Easings.h);
            this.u.a(0.0, 0.25, Easings.F);
            this.v.a(0.0, 0.3, Easings.h);
        }
    }

    private float m() {
        if (this.E && this.A) {
            return this.F;
        }
        return this.n;
    }

    public float g() {
        return this.p;
    }
}

