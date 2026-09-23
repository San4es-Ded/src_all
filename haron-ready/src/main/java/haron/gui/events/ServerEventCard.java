package haron.gui.events;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.gui.events.EventListCategory;
import haron.gui.events.ServerEventEntry;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class ServerEventCard {
    public static final float a = 36.5f;
    private static final float d = 6.5f;
    private static final float e = 1.5f;
    private static final float f = 19.0f;
    private static final float g = 2.0f;
    private static final float h = 6.5f;
    private static final float i = 9.0f;
    private static final float j = -6.0f;
    private static final float k = 19.0f;
    private static final float l = 5.0f;
    private final ServerEventEntry m;
    private final AnimatedValue n = new AnimatedValue();
    private final AnimatedValue o = new AnimatedValue();
    private boolean p = false;
    private boolean q = false;
    private Consumer<ServerEventEntry> r;
    public static int b;
    public static boolean c;

    public ServerEventCard(ServerEventEntry tywt852) {
        this.m = tywt852;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3) {
        int n = (int)(255.0f * f3);
        float f4 = (float)this.o.j();
        Color color = ColorUtils.a(pryrvd.f, pryrvd.I, f4);
        Color color2 = ColorUtils.a(pryrvd.e, pryrvd.K, f4);
        Color color3 = pryrvd.a(color, n);
        Color color4 = pryrvd.a(color2, n);
        Color color5 = new Color(19, 19, 25);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 20.0f, 20.0f, 6.0f, color5, color5, color5, color5, matrixStack);
        s7swsm2.a(f, f2, 19.0f, 19.0f, 6.0f, color3, color3, color4, color4, matrixStack);
        Color color6 = pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.O, f4), n);
        FontRenderer v6hnga2 = ClientFonts.e[12];
        float f5 = v6hnga2.a("");
        float f6 = v6hnga2.b("");
        v6hnga2.a("", f + (19.0f - f5) / 2.0f, (double)(f2 + (19.0f - f6) / 2.0f + 2.5f), color6, matrixStack);
    }

    public boolean a(float f, float f2, float f3, int n, int n2) {
        if (!GuiInput.a(f + f3 - 9.0f - 19.0f, f2 + 8.75f, 19.0f, 19.0f, (double)n, (double)n2)) {
            return false;
        }
        if (this.r != null) {
            this.r.accept(this.m);
        }
        return true;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        String string;
        String string2;
        int n3;
        this.n.a();
        this.o.a();
        int n4 = (int)(255.0f * f4);
        boolean bl = InteractionOverlayController.a().b();
        int n5 = !bl && GuiInput.a(f, f2, f3, 36.5f, (double)n, (double)n2) ? 1 : 0;
        float f5 = f + f3 - 9.0f - 19.0f;
        float f6 = f2 + 8.75f + 0.7f;
        int n6 = n3 = !bl && GuiInput.a(f5, f6, 19.0f, 19.0f, (double)n, (double)n2) ? 1 : 0;
        if (bl && this.p) {
            this.n.a(0.0, 0.15, Easings.h);
            this.p = false;
        } else if (!bl && BooleanCoercion.from(n5) != this.p) {
            this.n.a(n5 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.p = BooleanCoercion.from(n5);
        }
        if (bl && this.q) {
            this.o.a(0.0, 0.15, Easings.h);
            this.q = false;
        } else if (!bl && BooleanCoercion.from(n3) != this.q) {
            this.o.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.q = BooleanCoercion.from(n3);
        }
        float f7 = (float)this.n.j();
        Color color = new Color(17, 17, 22);
        Color color2 = new Color(14, 14, 19);
        Color color3 = new Color(22, 22, 29);
        Color color4 = ColorUtils.a(color, pryrvd.g, f7);
        Color color5 = ColorUtils.a(color2, pryrvd.h, f7);
        Color color6 = pryrvd.a(color4, n4);
        Color color7 = pryrvd.a(color5, n4);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, 37.5f, 6.5f, color3, color3, color3, color3, matrixStack);
        s7swsm2.a(f, f2, f3, 36.5f, 6.5f, color6, color6, color7, color7, matrixStack);
        FontRenderer v6hnga2 = ClientFonts.a[11];
        FontRenderer v6hnga3 = ClientFonts.a[15];
        float f8 = f + 6.5f + 3.0f;
        if (this.m.c() == EventListCategory.UPCOMING) {
            string2 = this.m.h();
            string = this.m.j();
        } else {
            string2 = String.valueOf(this.m.b());
            string = this.m.a();
        }
        float f9 = v6hnga2.b(string2);
        float f10 = f2 + (36.5f - (f9 + -6.0f + v6hnga3.b(string) - 8.0f)) / 2.0f;
        float f11 = f10 + f9 + -6.0f;
        Color color8 = pryrvd.a(pryrvd.b, n4);
        Color color9 = pryrvd.a(pryrvd.a, n4);
        v6hnga2.a(string2, f8, (double)f10, color8, matrixStack);
        v6hnga3.a(string, f8, (double)f11, color9, matrixStack);
        String string3 = this.m.g();
        v6hnga2.a(string3, f5 - 5.0f - v6hnga2.a(string3), (double)(f2 + 18.25f - v6hnga2.b(string3) / 4.0f), color8, matrixStack);
        this.a(matrixStack, s7swsm2, (float)((int)f5), (float)((int)f6), f4);
        if (n3 != 0 && !bl) {
            GuiInput.g();
        }
    }

    public void a(Consumer<ServerEventEntry> consumer) {
        this.r = consumer;
    }

    public ServerEventEntry a() {
        return this.m;
    }
}

