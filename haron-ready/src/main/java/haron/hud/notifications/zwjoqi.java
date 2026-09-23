package haron.hud.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.hud.notifications.ijsfy3;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class zwjoqi
extends ijsfy3 {
    private static final Color m = new Color(87, 215, 106);
    private static final Color n = new Color(215, 87, 87);
    private static final Color o = new Color(255, 255, 255);
    private static final float p = 6.5f;
    private static final float q = 9.5f;
    private static final float r = 13.0f;
    private static final float s = 10.0f;
    private final String t;
    private boolean u;
    private final AnimatedValue v = new AnimatedValue();
    public static int a;
    public static boolean b;

    public zwjoqi(String string, boolean bl) {
        this.t = string;
        this.u = bl;
        this.g = 8.0f;
        this.h = 4.5f;
        this.j = 1000L;
        this.v.d(!bl ? 0.0 : 1.0);
    }

    @Override
    public void e() {
        int n = 61;
        super.e();
        this.v.a();
    }

    @Override
    public float b() {
        int n = 455;
        return 13.0f;
    }

    public boolean n() {
        return this.u;
    }

    public void a(boolean bl) {
        if (this.u != bl) {
            this.u = bl;
            this.v.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.k = System.currentTimeMillis();
            this.l = false;
        }
    }

    @Override
    public float a() {
        FontRenderer v6hnga2 = ClientFonts.b[14];
        FontRenderer v6hnga3 = ClientFonts.a[12];
        float f = v6hnga2.a(this.t);
        return f + 10.0f + v6hnga3.a("crypt") + 13.0f + 60.0f;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.b[14];
        FontRenderer v6hnga3 = ClientFonts.a[12];
        int n = (int)(f5 * 255.0f);
        float f6 = f2 + f4 / 2.0f;
        Color color = new Color(o.getRed(), o.getGreen(), o.getBlue(), n);
        v6hnga2.a(this.t, f, (double)(f6 - v6hnga2.b(this.t) / 4.0f), color, matrixStack);
        float f7 = (float)this.v.j();
        String string = !this.u ? "crypt" : "crypt";
        float f8 = v6hnga3.a(string) + 13.0f;
        Color color2 = ColorUtils.a(zwjoqi.n, m, f7);
        Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), n);
        float f9 = f + f3 - f8 + 2.0f;
        s7swsm2.a(f9, f6 - 6.5f, f8, 13.0f, 9.5f, color3, matrixStack);
        v6hnga3.a(string, f9 + 6.5f, (double)(f6 - v6hnga3.b(string) / 4.0f), color, matrixStack);
    }

    public String m() {
        return this.t;
    }
}

