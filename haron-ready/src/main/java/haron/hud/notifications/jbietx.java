package haron.hud.notifications;

import haron.hud.notifications.ijsfy3;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class jbietx
extends ijsfy3 {
    private static final Color m = new Color(87, 215, 106);
    private static final Color n = new Color(255, 255, 255);
    private static final Color o = new Color(223, 223, 243);
    private static final Color p = new Color(255, 255, 255, 77);
    private static final float q = 12.0f;
    private static final float r = 3.0f;
    private static final float s = 3.0f;
    private final String t;
    private final String u;
    private final String v;
    public static int a;
    public static boolean b;

    public jbietx(String string, String string2, String string3) {
        this.t = string;
        this.u = string2;
        this.v = string3;
        this.g = 8.0f;
        this.h = 4.5f;
        this.j = 3000L;
    }

    @Override
    public float b() {
        return 13.0f;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.b[15];
        FontRenderer v6hnga3 = ClientFonts.a[15];
        float f6 = f2 + f4 / 2.0f;
        int n = (int)(f5 * 255.0f);
        Color color = new Color(223, 223, 255, n);
        Color color2 = new Color(255, 255, 255, n);
        Color color3 = new Color(p.getRed(), p.getGreen(), p.getBlue(), (int)((float)p.getAlpha() * f5));
        float f7 = f6 - v6hnga3.b(this.t) / 4.0f;
        float f8 = f6 - 6.0f;
        ClientFonts.e[22].a("crypt", f, (double)(f8 + 0.5f), new Color(m.getRed(), m.getGreen(), m.getBlue(), n), matrixStack);
        v6hnga3.a(this.t, f + 15.0f, (double)f7, color, matrixStack);
        float f9 = v6hnga2.a("crypt");
        float f10 = v6hnga2.a(this.u);
        float f11 = f + f3 - (f10 + 3.0f + f9 + 3.0f + v6hnga2.a(this.v));
        v6hnga2.a(this.u, f11, (double)f7, color2, matrixStack);
        float f12 = f11 + f10 + 3.0f;
        v6hnga2.a("crypt", f12, (double)f7, color3, matrixStack);
        v6hnga2.a(this.v, f12 + f9 + 3.0f, (double)f7, color2, matrixStack);
    }

    @Override
    public float a() {
        FontRenderer v6hnga2 = ClientFonts.b[15];
        float f = 15.0f + ClientFonts.a[15].a(this.t);
        return f + 15.0f + v6hnga2.a(this.u) + 3.0f + v6hnga2.a("ᗴ∪ᗎ") + 3.0f + v6hnga2.a(this.v) + 20.0f;
    }
}

