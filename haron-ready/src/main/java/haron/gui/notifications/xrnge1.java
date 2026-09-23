package haron.gui.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.hud.notifications.eq8z6w;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class xrnge1 {
    public static final float a = 27.5f;
    private static final float d = 7.5f;
    private static final float e = 4.0f;
    private static final float f = 20.0f;
    private static final float g = 6.5f;
    private static final float h = 12.0f;
    private static final float i = 5.0f;
    private static final float j = -6.0f;
    private final eq8z6w k;
    private final AnimatedValue l = new AnimatedValue();
    private final AnimatedValue m = new AnimatedValue();
    private final cqqvax n = new cqqvax();
    private boolean o = false;
    private boolean p = false;
    public static int b;
    public static boolean c;

    public xrnge1(eq8z6w eq8z6w2) {
        this.k = eq8z6w2;
    }

    public boolean b() {
        return this.p;
    }

    public boolean c() {
        int n2 = this.p ? 0 : 1;
        return BooleanCoercion.from(n2);
    }

    public boolean a(float f, float f2, float f3, int n, int n2) {
        return GuiInput.a(f, f2, f3, 27.5f, (double)n, (double)n2);
    }

    public eq8z6w a() {
        return this.k;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        long l;
        int n3;
        this.l.a();
        this.m.a();
        boolean bl = InteractionOverlayController.a().b();
        boolean bl2 = this.k.j();
        int n4 = n3 = bl || !GuiInput.a(f, f2, f3, 27.5f, (double)n, (double)n2) ? 0 : 1;
        if (bl && this.o) {
            this.l.a(0.0, 0.15, Easings.h);
            this.o = false;
        } else if (!bl && BooleanCoercion.from(n3) != this.o) {
            this.l.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.o = BooleanCoercion.from(n3);
        }
        float f5 = (float)this.l.j();
        float f6 = (float)this.m.j();
        int n5 = (int)(255.0f * f4);
        float f7 = Math.max(f5 * 0.6f, f6);
        if (bl2) {
            Color color = pryrvd.a(this.k.e(), (int)(Math.max(0.3f, f7 * 0.4f + 0.3f) * 255.0f * f4));
            s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, 28.5f, 7.5f, color, color, color, color, matrixStack);
            s7swsm2.a(f, f2, f3, 27.5f, 7.5f, pryrvd.a(pryrvd.e, n5), pryrvd.a(pryrvd.e, n5), pryrvd.a(pryrvd.f, n5), pryrvd.a(pryrvd.f, n5), matrixStack);
        } else if (f7 > 0.01f) {
            Color color;
            Color color2;
            Color color3;
            Color color4;
            if (f6 <= f5 * 0.6f) {
                float f8 = f5 * 0.6f;
                color4 = pryrvd.a(pryrvd.u, f8 * f4);
                color3 = pryrvd.a(pryrvd.v, f8 * f4);
                color2 = pryrvd.a(pryrvd.k, f8 * f4);
                color = pryrvd.a(pryrvd.l, f8 * f4);
            } else {
                color4 = pryrvd.a(pryrvd.s, f6 * f4);
                color3 = pryrvd.a(pryrvd.t, f6 * f4);
                color2 = pryrvd.a(pryrvd.i, f6 * f4);
                color = pryrvd.a(pryrvd.j, f6 * f4);
            }
            s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, 28.5f, 7.5f, color4, color4, color3, color3, matrixStack);
            s7swsm2.a(f, f2, f3, 27.5f, 7.5f, color2, color2, color, color, matrixStack);
        }
        float f9 = f + 4.0f;
        this.a(matrixStack, s7swsm2, f9, f2 + 4.0f, n5);
        FontRenderer v6hnga2 = ClientFonts.a[12];
        FontRenderer v6hnga3 = ClientFonts.a[9];
        float f10 = f9 + 20.0f + 5.0f;
        String string = this.k.a();
        String string2 = this.k.g();
        if (this.k.j() && (l = this.k.p()) > 0L) {
            int n6 = (int)(l / 1000L);
            int n7 = n6 / 60;
            int n8 = n6 % 60;
            string = n7 <= 0 ? xrnge1.$sf$0(string, n8) : xrnge1.$sf$1(string, n7, n8);
        }
        float f11 = v6hnga2.b(string);
        float f12 = f2 + (27.5f - (f11 + -6.0f + v6hnga3.b(string2) - 4.0f)) / 2.0f;
        float f13 = f12 + f11 + -6.0f;
        pryrvd.a(pryrvd.a, n5);
        Color color = pryrvd.a(pryrvd.b, n5);
        float f14 = f + f3 - f10 - 4.0f;
        this.n.a(BooleanCoercion.from(n3 != 0 || this.p ? 1 : 0));
        this.n.a(matrixStack, s7swsm2, v6hnga2, string, f10, f12, f14, 1.0f, pryrvd.a, f4);
        v6hnga3.a(string2, f10, (double)f13, color, matrixStack);
        if (n3 == 0 || this.p || bl) {
            return;
        }
        GuiInput.g();
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n) {
        Color color = this.k.e();
        Color color2 = pryrvd.a(color, n);
        Color color3 = pryrvd.a(pryrvd.c(color, 150), n);
        Color color4 = pryrvd.b(color, 50);
        Color color5 = pryrvd.a(color4, (int)((float)(50 * n) / 255.0f));
        Color color6 = pryrvd.a(color4, (int)((float)(10 * n) / 255.0f));
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 21.0f, 21.0f, 6.5f, color5, color5, color6, color6, matrixStack);
        s7swsm2.a(f, f2, 20.0f, 20.0f, 6.5f, color2, color2, color3, color3, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.e[22];
        String string = this.k.f().b();
        float f3 = f + 4.0f;
        v6hnga2.a(string, f3 + 0.5f, (double)(f2 + 4.0f), pryrvd.a(pryrvd.aa, n), matrixStack);
    }

    public void a(boolean bl) {
        if (this.p != bl) {
            this.p = bl;
            this.m.a(!bl ? 0.0 : 1.0, 0.2, Easings.h);
        }
    }

    private static /* synthetic */ String $sf$0(String string, int n) {
        return string + " (" + n + " сек)";
    }

    private static /* synthetic */ String $sf$1(String string, int n, int n2) {
        return string + " (" + n + " мин " + n2 + " сек)";
    }
}

