package haron.gui.config;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.client.MinecraftClientAccess;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.gui.widgets.TextInputType;
import haron.gui.widgets.TextInput;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class ImportConfigDialog {
    private static final float c = 178.5f;
    private static final float d = 94.0f;
    private static final float e = 9.5f;
    private static final float f = 12.0f;
    private static final float g = 3.0f;
    private static final float h = 11.5f;
    private static final float i = 22.0f;
    private static final float j = 9.5f;
    private static final float k = 18.0f;
    private static final float l = 6.0f;
    private static final float m = 7.0f;
    private boolean n = false;
    private final AnimatedValue o = new AnimatedValue();
    private final AnimatedValue p = new AnimatedValue();
    private final AnimatedValue q = new AnimatedValue();
    private boolean r = false;
    private boolean s = false;
    private final TextInput t = new TextInput(TextInputType.KEY, "", "XXXX-XXXX-XXXX");
    private Consumer<String> u;
    private Runnable v;
    public static int a;
    public static boolean b;

    public ImportConfigDialog() {
        this.t.a(9.5f);
        this.t.a(pryrvd.S, pryrvd.T);
        this.t.a(true);
        this.t.b(true);
    }

    public boolean e() {
        return BooleanCoercion.from(this.n && this.t.d() ? 1 : 0);
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n) {
        float f5 = (float)this.q.j();
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

    public void b() {
        if (this.n) {
            this.o.a(0.0, 0.15, Easings.h);
            this.t.c(false);
            InteractionOverlayController.a().e(false);
            GuiInput.a();
        }
    }

    public boolean c() {
        return this.n;
    }

    public boolean d() {
        return !this.n || this.o.j() < 0.01;
    }

    public boolean a(char c, int n) {
        return this.n ? this.t.a(c, n) : false;
    }

    public void a() {
        this.n = true;
        this.o.d(0.0);
        this.o.a(1.0, 0.25, Easings.F);
        this.t.b("");
        InteractionOverlayController.a().e(true);
        GuiInput.a(0.0f, 0.0f, 10000.0f, 10000.0f);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n) {
        float f5 = (float)this.p.j();
        Color color = ColorUtils.a(pryrvd.y, pryrvd.B, f5);
        Color color2 = ColorUtils.a(pryrvd.z, pryrvd.y, f5);
        Color color3 = pryrvd.a(color, n);
        Color color4 = pryrvd.a(color2, n);
        s7swsm2.a(f, f2, f3, f4, 6.0f, color3, color3, color4, color4, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[12];
        v6hnga2.a("Активировать", f + (f3 - v6hnga2.a("Активировать")) / 2.0f, (double)(f2 + (f4 - v6hnga2.b("Активировать")) / 2.0f + 3.5f), pryrvd.a(pryrvd.aa, n), matrixStack);
    }

    public void a(Consumer<String> consumer) {
        this.u = consumer;
    }

    public void a(Runnable runnable) {
        this.v = runnable;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.o.a();
        this.p.a();
        this.q.a();
        float f3 = (float)this.o.j();
        if (f3 < 0.01f) {
            if (this.n && this.o.d()) {
                this.n = false;
                if (this.v != null) {
                    this.v.run();
                    return;
                }
                return;
            }
            return;
        }
        int n3 = (int)(255.0f * f3);
        float f4 = (f - 178.5f) / 2.0f;
        float f5 = (f2 - 94.0f) / 2.0f;
        float f6 = 0.9f + 0.1f * f3;
        float f7 = 178.5f * f6;
        float f8 = 94.0f * f6;
        float f9 = f4 + (178.5f - f7) / 2.0f;
        float f10 = f5 + (94.0f - f8) / 2.0f;
        Color color = pryrvd.a(new Color(17, 17, 23, 204), n3);
        Color color2 = pryrvd.a(new Color(13, 13, 17, 204), n3);
        s7swsm2.a(f9, f10, f7, f8, 9.5f * f6, color, color, color2, color2, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.b[15];
        float f11 = f9 + (f7 - v6hnga2.a("Активация конфига")) / 2.0f;
        float f12 = f10 + 3.0f * f6;
        v6hnga2.a("Активация конфига", f11, (double)(f12 + 5.0f), pryrvd.a(pryrvd.a, n3), matrixStack);
        float f13 = f9 + 12.0f * f6;
        float f14 = f12 + v6hnga2.b("Активация конфига") + 11.5f * f6;
        float f15 = f7 - 24.0f * f6;
        float f16 = 22.0f * f6;
        this.t.a(matrixStack, s7swsm2, f13, f14, f15, f16, n, n2, f3);
        float f17 = f14 + f16 + 11.5f * f6;
        float f18 = (f15 - 7.0f * f6) / 2.0f;
        float f19 = f13 + f18 + 7.0f * f6;
        boolean bl = GuiInput.a(f13, f17, f18, 18.0f * f6, (double)n, (double)n2);
        boolean bl2 = GuiInput.a(f19, f17, f18, 18.0f * f6, (double)n, (double)n2);
        if (bl != this.r) {
            this.p.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.r = bl;
        }
        if (bl2 != this.s) {
            this.q.a(!bl2 ? 0.0 : 1.0, 0.15, Easings.h);
            this.s = bl2;
        }
        this.a(matrixStack, s7swsm2, f13, f17, f18, 18.0f * f6, n3);
        this.b(matrixStack, s7swsm2, f19, f17, f18, 18.0f * f6, n3);
        if (bl || bl2) {
            GuiInput.g();
        }
    }

    public boolean a(float f, float f2, int n, int n2) {
        float f3;
        if (!this.n || this.o.j() < 0.5) {
            return false;
        }
        float f4 = ((float)MinecraftClientAccess.d.getWidth() / 2.0f - 178.5f) / 2.0f;
        if (!GuiInput.a(f4, f3 = ((float)MinecraftClientAccess.d.getHeight() / 2.0f - 94.0f) / 2.0f, 178.5f, 94.0f, (double)n, (double)n2)) {
            this.b();
            return true;
        }
        if (this.t.a(n, n2)) {
            return true;
        }
        FontRenderer v6hnga2 = ClientFonts.b[15];
        float f5 = f4 + 12.0f;
        float f6 = f3 + 3.0f + v6hnga2.b("Активация конфига") + 11.5f + 22.0f + 11.5f;
        float f7 = 73.75f;
        float f8 = f5 + 73.75f + 7.0f;
        if (GuiInput.a(f5, f6, 73.75f, 18.0f, (double)n, (double)n2)) {
            if (this.u != null) {
                this.u.accept(this.t.a());
            }
            this.b();
            return true;
        }
        if (!GuiInput.a(f8, f6, 73.75f, 18.0f, (double)n, (double)n2)) {
            return true;
        }
        this.b();
        return true;
    }

    public boolean a(int n, int n2, int n3) {
        return this.n ? this.t.b(n, n2, n3) : false;
    }
}

