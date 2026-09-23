package haron.gui.config;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.config.ConfigProfileEntry;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.widgets.TextInputType;
import haron.gui.widgets.TextInput;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.BiConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class ShareConfigDialog {
    private static final float a = 178.5f;
    private static final float b = 155.0f;
    private static final float c = 9.5f;
    private static final float d = 12.0f;
    private static final float e = 11.0f;
    private static final float f = 11.5f;
    private static final float g = 2.5f;
    private static final float h = 22.0f;
    private static final float i = 9.5f;
    private static final float j = 31.5f;
    private static final float k = 18.0f;
    private static final float l = 6.0f;
    private static final float m = 7.0f;
    private static final float n = 18.0f;
    private ConfigProfileEntry p;
    private final TextInput z;
    private BiConsumer<Integer, Integer> A;
    private Runnable B;
    private boolean o = false;
    private final AnimatedValue q = new AnimatedValue();
    private final AnimatedValue r = new AnimatedValue();
    private final AnimatedValue s = new AnimatedValue();
    private final AnimatedValue t = new AnimatedValue();
    private boolean u = false;
    private boolean v = false;
    private boolean w = false;
    private boolean x = false;
    private final TextInput y = new TextInput(TextInputType.INT, "", "1");

    public ShareConfigDialog() {
        this.y.a(6);
        this.y.a(9.5f, 0.0f, 0.0f, 9.5f);
        this.y.a(pryrvd.S, pryrvd.T);
        this.y.a(true);
        this.z = new TextInput(TextInputType.INT, "", "1");
        this.z.a(6);
        this.z.a(9.5f);
        this.z.a(pryrvd.S, pryrvd.T);
        this.z.a(true);
    }

    public boolean e() {
        return BooleanCoercion.from(this.o && (this.y.d() || this.z.d()) ? 1 : 0);
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n) {
        float f5 = (float)this.r.j();
        Color color = ColorUtils.a(pryrvd.y, pryrvd.B, f5);
        Color color2 = ColorUtils.a(pryrvd.z, pryrvd.y, f5);
        Color color3 = pryrvd.a(color, n);
        Color color4 = pryrvd.a(color2, n);
        s7swsm2.a(f, f2, f3, f4, 6.0f, color3, color3, color4, color4, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[12];
        v6hnga2.a("Создать", f + (f3 - v6hnga2.a("Создать")) / 2.0f, (double)(f2 + (f4 - v6hnga2.b("Создать")) / 2.0f + 3.5f), pryrvd.a(pryrvd.aa, n), matrixStack);
    }

    public boolean b() {
        return this.o;
    }

    private void c(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n) {
        float f5 = (float)this.s.j();
        Color color = pryrvd.b(pryrvd.q, (float)n / 255.0f);
        Color color2 = pryrvd.b(pryrvd.n, (float)n / 255.0f);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 6.0f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.e, pryrvd.I, f5);
        Color color4 = ColorUtils.a(pryrvd.f, pryrvd.J, f5);
        Color color5 = pryrvd.a(color3, n);
        Color color6 = pryrvd.a(color4, n);
        s7swsm2.a(f, f2, f3, f4, 6.0f, color5, color5, color6, color6, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[12];
        v6hnga2.a("Отмена", f + (f3 - v6hnga2.a("Отмена")) / 2.0f, (double)(f2 + (f4 - v6hnga2.b("Отмена")) / 2.0f + 3.5f), pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.a, f5), n), matrixStack);
    }

    public boolean c() {
        return BooleanCoercion.from(!this.o || this.q.j() < 0.01 ? 1 : 0);
    }

    public ConfigProfileEntry d() {
        return this.p;
    }

    public boolean a(int n, int n2, int n3) {
        return !this.o ? false : (this.y.d() ? this.y.b(n, n2, n3) : (this.z.d() ? this.z.b(n, n2, n3) : false));
    }

    public boolean a(float f, float f2, int n, int n2) {
        return false;
    }

    public void a(ConfigProfileEntry lhvtx72) {
    }

    public boolean a(char c, int n) {
        return !this.o ? false : (!Character.isDigit(c) ? true : (this.y.d() ? this.y.a(c, n) : (this.z.d() ? this.z.a(c, n) : false)));
    }

    public void a(BiConsumer<Integer, Integer> biConsumer) {
        this.A = biConsumer;
    }

    public void a(Runnable runnable) {
        this.B = runnable;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        int n3;
        this.q.a();
        this.r.a();
        this.s.a();
        this.t.a();
        float f3 = (float)this.q.j();
        if (f3 < 0.01f) {
            if (this.o && this.q.d()) {
                this.o = false;
                if (this.B != null) {
                    this.B.run();
                    return;
                }
                return;
            }
            return;
        }
        int n4 = (int)(255.0f * f3);
        float f4 = (f - 178.5f) / 2.0f;
        float f5 = (f2 - 155.0f) / 2.0f;
        float f6 = 0.9f + 0.1f * f3;
        float f7 = 178.5f * f6;
        float f8 = 155.0f * f6;
        float f9 = f4 + (178.5f - f7) / 2.0f;
        float f10 = f5 + (155.0f - f8) / 2.0f;
        Color color = pryrvd.a(new Color(17, 17, 23, 204), n4);
        Color color2 = pryrvd.a(new Color(13, 13, 17, 204), n4);
        s7swsm2.a(f9, f10, f7, f8, 9.5f * f6, color, color, color2, color2, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.b[15];
        FontRenderer v6hnga3 = ClientFonts.a[12];
        FontRenderer v6hnga4 = ClientFonts.a[12];
        float f11 = f9 + (f7 - v6hnga2.a("Создание ключа")) / 2.0f;
        float f12 = f10 + 11.0f * f6;
        v6hnga2.a("Создание ключа", f11, (double)f12, pryrvd.a(pryrvd.a, n4), matrixStack);
        if (this.p != null) {
            String string = ShareConfigDialog.$sf$0(this.p.a());
            v6hnga3.a(string, f9 + (f7 - v6hnga3.a(string)) / 2.0f, (double)(f12 + v6hnga2.b("Создание ключа") - 6.5f), pryrvd.a(pryrvd.b, n4), matrixStack);
        }
        float f13 = f9 + 12.0f * f6;
        float f14 = f7 - 24.0f * f6;
        float f15 = f12 + v6hnga2.b("Создание ключа") + 11.5f * f6 + 4.0f;
        v6hnga4.a("Количество использований", f13, (double)(f15 - 7.0f), pryrvd.a(pryrvd.a, n4), matrixStack);
        float f16 = f15 + v6hnga4.b("A") + 2.5f * f6;
        float f17 = f14 - 18.0f * f6 - 3.0f;
        this.y.a(matrixStack, s7swsm2, f13, f16 - 14.0f, f17 + 3.0f, 22.0f * f6, n, n2, f3);
        float f18 = f13 + f17 + 3.0f;
        int n5 = n3 = this.x || !GuiInput.a(f18, f16 - 14.0f, 18.0f * f6, 22.0f * f6, (double)n, (double)n2) ? 0 : 1;
        if (this.x && this.w) {
            this.t.a(0.0, 0.15, Easings.h);
            this.w = false;
        } else if (BooleanCoercion.from(n3) != this.w) {
            this.t.a(n3 != 0 ? 1.0 : 0.0, 0.15, Easings.h);
            this.w = BooleanCoercion.from(n3);
        }
        this.a(matrixStack, s7swsm2, f18, f16 - 14.0f, 18.0f * f6, 22.0f * f6, n4);
        float f19 = f16 + 22.0f * f6 + 31.5f * f6 - 40.0f;
        v6hnga4.a("Количество ключей", f13, (double)(f19 + 7.0f), pryrvd.a(pryrvd.a, n4), matrixStack);
        float f20 = f19 + v6hnga4.b("A") + 2.5f * f6;
        this.z.a(matrixStack, s7swsm2, f13, f20, f14, 22.0f * f6, n, n2, f3);
        float f21 = f20 + 22.0f * f6 + 11.5f * f6;
        float f22 = (f14 - 7.0f * f6) / 2.0f;
        float f23 = f13 + f22 + 7.0f * f6;
        boolean bl = GuiInput.a(f13, f21, f22, 18.0f * f6, (double)n, (double)n2);
        boolean bl2 = GuiInput.a(f23, f21, f22, 18.0f * f6, (double)n, (double)n2);
        if (bl != this.u) {
            this.r.a(bl ? 1.0 : 0.0, 0.15, Easings.h);
            this.u = bl;
        }
        if (bl2 != this.v) {
            this.s.a(bl2 ? 1.0 : 0.0, 0.15, Easings.h);
            this.v = bl2;
        }
        this.b(matrixStack, s7swsm2, f13, f21, f22, 18.0f * f6, n4);
        this.c(matrixStack, s7swsm2, f23, f21, f22, 18.0f * f6, n4);
        if (bl || bl2 || !this.x && n3 != 0) {
            GuiInput.g();
        }
    }

    private int a(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            return 1;
        }
    }

    public void a() {
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n) {
        float f5 = (float)this.t.j();
        Color color = pryrvd.b(pryrvd.q, (float)n / 255.0f);
        Color color2 = pryrvd.b(pryrvd.n, (float)n / 255.0f);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 9.5f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(new Color(12, 12, 20, 153), pryrvd.I, f5);
        Color color4 = ColorUtils.a(new Color(13, 13, 16, 153), pryrvd.J, f5);
        Color color5 = pryrvd.a(color3, n);
        Color color6 = pryrvd.a(color4, n);
        s7swsm2.a(f, f2, f3, f4, 0.0f, 9.5f, 0.0f, 9.5f, color5, color5, color6, color6, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.b[14];
        v6hnga2.a("∞", f + (f3 - v6hnga2.a("∞")) / 2.0f, (double)(f2 + (f4 - v6hnga2.b("∞")) / 2.0f + 3.5f), pryrvd.a(ColorUtils.a(pryrvd.a, pryrvd.aa, f5), n), matrixStack);
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "Конфиг: " + string;
    }
}

