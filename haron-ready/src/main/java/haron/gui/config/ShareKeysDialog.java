package haron.gui.config;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.config.ConfigProfileEntry;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.widgets.ScrollBar;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class ShareKeysDialog {
    private static final float a = 178.5f;
    private static final float b = 9.5f;
    private static final float c = 12.0f;
    private static final float d = 11.0f;
    private static final float e = 10.0f;
    private static final float f = 22.0f;
    private static final float g = 3.0f;
    private static final float h = 9.5f;
    private static final float i = 18.0f;
    private static final float j = 6.0f;
    private static final float k = 7.0f;
    private static final float l = 2.0f;
    private static final int m = 5;
    private ConfigProfileEntry q;
    private Runnable z;
    private boolean n = false;
    private String o = "";
    private String[] p = new String[0];
    private boolean r = false;
    private final ScrollBar s = new ScrollBar(2.0f, 20.0f);
    private final AnimatedValue t = new AnimatedValue();
    private final AnimatedValue u = new AnimatedValue();
    private final AnimatedValue v = new AnimatedValue();
    private final AnimatedValue w = new AnimatedValue();
    private boolean x = false;
    private boolean y = false;

    private float e() {
        return 41.0f + ((float)Math.min(this.p.length, 5) * 25.0f - 3.0f) + 10.0f + 18.0f + 11.0f;
    }

    public boolean b() {
        return this.n;
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n) {
        float f5 = (float)this.v.j();
        Color color = pryrvd.b(pryrvd.q, (float)n / 255.0f);
        Color color2 = pryrvd.b(pryrvd.n, (float)n / 255.0f);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 6.0f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.e, pryrvd.I, f5);
        Color color4 = ColorUtils.a(pryrvd.f, pryrvd.J, f5);
        Color color5 = pryrvd.a(color3, n);
        Color color6 = pryrvd.a(color4, n);
        s7swsm2.a(f, f2, f3, f4, 6.0f, color5, color5, color6, color6, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[12];
        v6hnga2.a("Закрыть", f + (f3 - v6hnga2.a("Закрыть")) / 2.0f, (double)(f2 + (f4 - v6hnga2.b("Закрыть")) / 2.0f + 3.5f), pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.a, f5), n), matrixStack);
    }

    public boolean c() {
        return BooleanCoercion.from(!this.n || this.t.j() < 0.01 ? 1 : 0);
    }

    private boolean h() {
        int n = 336;
        return BooleanCoercion.from(this.p.length > 5 ? 1 : 0);
    }

    private float f() {
        return (float)Math.min(this.p.length, 5) * 25.0f - 3.0f;
    }

    public boolean d() {
        return false;
    }

    public void a(String string, ConfigProfileEntry lhvtx72) {
        String string2;
        this.q = lhvtx72;
        if (lhvtx72 != null) {
            string2 = lhvtx72.a();
            if (string2 == null) {
                string2 = string;
            }
        } else {
            string2 = string;
        }
        if (string2 == null) {
            string2 = "";
        }
        this.o = string2;
        String[] stringArray = new String[]{string == null ? "" : string};
        this.p = stringArray;
        this.n = true;
        this.s.e();
        this.t.a(true, 1.0);
        this.u.a(true, 1.0);
        this.v.a(true, 1.0);
        this.w.a(true, 1.0);
    }

    public boolean a(float f, float f2, int n, int n2) {
        return this.n;
    }

    public void a(int n, int n2, double d, double d2) {
        if (this.s.c()) {
            float f = this.f();
            this.s.a(n2, this.g(), f);
        }
    }

    public void a(int n, int n2) {
        this.s.d();
    }

    public boolean a(int n, int n2, int n3) {
        if (!this.n) {
            return false;
        }
        if (n == 259) {
            if (this.o == null) {
                this.o = "";
                return true;
            }
            if (this.o.isEmpty()) {
                return true;
            }
            this.o = this.o.substring(0, this.o.length() - 1);
            if (this.q == null) {
                return true;
            }
            this.q.a(this.o);
            return true;
        }
        if (n != 257 && n != 335) {
            if (n != 256) {
                return true;
            }
            this.n = false;
            return true;
        }
        if (this.q != null) {
            this.q.a(this.o);
        }
        this.n = false;
        if (this.z == null) {
            return true;
        }
        this.z.run();
        return true;
    }

    public boolean a(char c, int n) {
        if (!this.n) {
            return false;
        }
        if (c < ' ') {
            return true;
        }
        this.o = ShareKeysDialog.$sf$3(this.o == null ? "" : this.o, c);
        if (this.q == null) {
            return true;
        }
        this.q.a(this.o);
        return true;
    }

    public void a() {
    }

    public void a(Runnable runnable) {
        this.z = runnable;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        float f3;
        this.t.a();
        this.u.a();
        this.v.a();
        this.w.a();
        this.s.a();
        float f4 = (float)this.t.j();
        if (f4 < 0.01f) {
            if (this.n && this.t.d()) {
                this.n = false;
                if (this.z != null) {
                    this.z.run();
                    return;
                }
                return;
            }
            return;
        }
        int n3 = (int)(255.0f * f4);
        float f5 = this.e();
        float f6 = (f - 178.5f) / 2.0f;
        float f7 = (f2 - f5) / 2.0f;
        float f8 = 0.9f + 0.1f * f4;
        float f9 = 178.5f * f8;
        float f10 = f5 * f8;
        float f11 = f6 + (178.5f - f9) / 2.0f;
        float f12 = f7 + (f5 - f10) / 2.0f;
        Color color = pryrvd.a(new Color(17, 17, 23, 204), n3);
        Color color2 = pryrvd.a(new Color(13, 13, 17, 204), n3);
        s7swsm2.a(f11, f12, f9, f10, 9.5f * f8, color, color, color2, color2, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.b[15];
        FontRenderer v6hnga3 = ClientFonts.a[12];
        String string = this.p.length > 1 ? "Ключи успешно созданы" : "Ключ усބешно создан";
        float f13 = f11 + (f9 - v6hnga2.a(string)) / 2.0f;
        float f14 = f12 + 11.0f * f8;
        v6hnga2.a(string, f13, (double)f14, pryrvd.a(pryrvd.a, n3), matrixStack);
        float f15 = f14 + v6hnga2.b(string) / 2.0f - 6.0f;
        if (this.q != null) {
            String string2 = ShareKeysDialog.$sf$1(this.q.a(), this.p.length > 1 ? ShareKeysDialog.$sf$0(this.p.length) : "");
            v6hnga3.a(string2, f11 + (f9 - v6hnga3.a(string2)) / 2.0f, (double)(f15 + 8.0f), pryrvd.a(pryrvd.b, n3), matrixStack);
        }
        float f16 = f11 + 12.0f * f8;
        float f17 = f9 - 24.0f * f8;
        float f18 = f15 + v6hnga3.b("A") + 10.0f * f8;
        float f19 = this.f() * f8;
        float f20 = this.g() * f8;
        if (this.h()) {
            s7swsm2.b().a(f16, f18, f17 + 10.0f, f19, matrixStack);
        }
        float f21 = this.s.b();
        for (int i = 0; i < this.p.length; ++i) {
            f3 = f18 + (float)i * 25.0f * f8 - f21;
            if (!(f3 + 22.0f * f8 >= f18) || !(f3 <= f18 + f19)) continue;
            this.a(matrixStack, s7swsm2, f16, f3, f17, 22.0f * f8, n3, this.p[i]);
        }
        if (this.h()) {
            s7swsm2.b().a(matrixStack);
            this.s.a(matrixStack, s7swsm2, f11 + f9 - 12.0f * f8 / 2.0f - 2.0f, f18, f19, f20, f19, n, n2, false);
        }
        float f22 = f18 + f19 + 10.0f * f8;
        f3 = (f17 - 7.0f * f8) / 2.0f;
        float f23 = f16 + f3 + 7.0f * f8;
        boolean bl = GuiInput.a(f16, f22, f3, 18.0f * f8, (double)n, (double)n2);
        boolean bl2 = GuiInput.a(f23, f22, f3, 18.0f * f8, (double)n, (double)n2);
        if (bl != this.x) {
            this.u.a(bl ? 1.0 : 0.0, 0.15, Easings.h);
            this.x = bl;
        }
        if (bl2 != this.y) {
            this.v.a(bl2 ? 1.0 : 0.0, 0.15, Easings.h);
            this.y = bl2;
        }
        this.a(matrixStack, s7swsm2, f16, f22, f3, 18.0f * f8, n3);
        this.b(matrixStack, s7swsm2, f23, f22, f3, 18.0f * f8, n3);
        if (bl || bl2) {
            GuiInput.g();
        }
    }

    public void a(float f, int n, int n2) {
        if (this.n && this.h()) {
            float f2 = this.f();
            this.s.a(f, this.g(), f2);
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n) {
        float f5 = (float)this.u.j();
        float f6 = (float)this.w.j();
        Color color = ColorUtils.a(pryrvd.y, new Color(34, 197, 94), f6);
        Color color2 = ColorUtils.a(pryrvd.z, new Color(22, 163, 74), f6);
        Color color3 = ColorUtils.a(pryrvd.B, new Color(74, 222, 128), f6);
        Color color4 = ColorUtils.a(pryrvd.y, new Color(34, 197, 94), f6);
        Color color5 = ColorUtils.a(color, color3, f5);
        Color color6 = ColorUtils.a(color2, color4, f5);
        Color color7 = pryrvd.a(color5, n);
        Color color8 = pryrvd.a(color6, n);
        s7swsm2.a(f, f2, f3, f4, 6.0f, color7, color7, color8, color8, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[12];
        String string = this.r ? "Скопировано" : "Копировать";
        v6hnga2.a(string, f + (f3 - v6hnga2.a(string)) / 2.0f, (double)(f2 + (f4 - v6hnga2.b(string)) / 2.0f + 3.5f), pryrvd.a(pryrvd.aa, n), matrixStack);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, String string) {
        Color color = pryrvd.b(pryrvd.q, (float)n / 255.0f);
        Color color2 = pryrvd.b(pryrvd.n, (float)n / 255.0f);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 9.5f, color, color, color2, color2, matrixStack);
        Color color3 = pryrvd.a(new Color(12, 12, 20, 153), n);
        Color color4 = pryrvd.a(new Color(13, 13, 16, 153), n);
        s7swsm2.a(f, f2, f3, f4, 9.5f, color3, color3, color4, color4, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[12];
        float f5 = f2 + (f4 - v6hnga2.b(string)) / 2.0f + 3.5f;
        String string2 = string;
        if (v6hnga2.a(string2) > f3 - 16.0f) {
            while (v6hnga2.a(ShareKeysDialog.$sf$2(string2)) > f3 - 16.0f && string2.length() > 0) {
                int n2 = string2.length();
                string2 = string2.substring(0, 2 * (n2 & 0xFFFFFFFE) - (n2 ^ 1));
            }
            string2 = ShareKeysDialog.$sf$2(string2);
        }
        v6hnga2.a(string2, f + (f3 - v6hnga2.a(string2)) / 2.0f, (double)f5, pryrvd.a(pryrvd.a, n), matrixStack);
    }

    private float g() {
        return (float)this.p.length * 25.0f - 3.0f;
    }

    private static /* synthetic */ String $sf$0(int n) {
        return " (" + n + " шт.)";
    }

    private static /* synthetic */ String $sf$3(String string, char c) {
        return string + c;
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return "Конфиг: " + string + string2;
    }

    private static /* synthetic */ String $sf$2(String string) {
        return string + "...";
    }
}

