package haron.hud.notifications;

import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.hud.notifications.ijsfy3;
import haron.hud.notifications.wo1nxr;
import haron.hud.notifications.x0i21u;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

public class uqw6p7
extends ijsfy3 {
    private static final float a = 16.0f;
    private static final float b = 5.0f;
    private static final float m = -9.0f;
    private static final float n = 3.0f;
    private static final float o = 8.0f;
    private static final float p = 15.0f;
    private static final float q = 13.0f;
    private static final float r = 5.0f;
    private final wo1nxr s;
    private final String t;
    private final String u;
    private final String v;
    private final Runnable w;
    private final Color x;
    private final Color y;
    private final Supplier<Boolean> z;
    private final boolean A;
    private List<String> B;
    private boolean C = false;
    private float D;
    private float E;
    private float F;
    private float G;
    private float H;
    private float I;

    private uqw6p7(x0i21u x0i21u2) {
        this.s = x0i21u2.a;
        this.t = x0i21u2.b;
        this.u = x0i21u2.c;
        this.v = x0i21u2.d;
        this.w = x0i21u2.e;
        this.x = x0i21u2.f != null ? x0i21u2.f : this.s.b();
        this.y = x0i21u2.g != null ? x0i21u2.g : this.s.c();
        this.z = x0i21u2.i;
        this.A = x0i21u2.j;
        this.g = 9.5f;
        this.h = 7.0f;
        this.j = x0i21u2.h;
        this.r();
    }

    @Override
    public void e() {
        this.d.a();
        this.e.a();
        this.f.a();
        if (!this.A && !this.l && System.currentTimeMillis() - this.k > this.j) {
            this.f();
        }
        if (this.z == null || this.l) {
            return;
        }
        if (this.z.get().booleanValue()) {
            this.k = System.currentTimeMillis();
        } else {
            this.f();
        }
    }

    @Override
    public float b() {
        float f = 9.0f;
        if (!this.B.isEmpty()) {
            f = 12.0f + (float)this.B.size() * 8.0f;
        }
        if (this.v != null) {
            f += 20.0f;
        }
        return f;
    }

    private void s() {
        if (this.v == null || uqw6p7.c.currentScreen == null || !(uqw6p7.c.currentScreen instanceof ChatScreen)) {
            this.C = false;
            return;
        }
        double d = this.t();
        double d2 = this.u();
        this.C = BooleanCoercion.from(d < (double)this.D || d > (double)(this.D + this.F) || d2 < (double)this.E || d2 > (double)(this.E + this.G) ? 0 : 1);
    }

    public void n() {
        if (this.v == null || uqw6p7.c.currentScreen == null || !(uqw6p7.c.currentScreen instanceof ChatScreen)) {
            return;
        }
        double d = this.t();
        double d2 = this.u();
        if (d >= (double)this.D && d <= (double)(this.D + this.F) && d2 >= (double)this.E && d2 <= (double)(this.E + this.G)) {
            GuiInput.g();
        }
    }

    public static x0i21u a(String string) {
        return null;
    }

    @Override
    public float a() {
        if ((this.u == null || this.u.isEmpty()) && this.v == null) {
            return 13.0f + ClientFonts.b[15].a(this.t);
        }
        return 147.5f;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        this.H = f - this.g;
        this.I = f2 - this.h;
        FontRenderer v6hnga2 = ClientFonts.b[15];
        FontRenderer v6hnga3 = ClientFonts.a[13];
        FontRenderer v6hnga4 = ClientFonts.b[12];
        int n = (int)(f5 * 255.0f);
        float f6 = f + -9.0f;
        float f7 = f2 + 4.5f - 8.0f;
        ClientFonts.e[15].a(this.s.a(), f6 + 8.0f, (double)(f7 + 4.0f), new Color(this.x.getRed(), this.x.getGreen(), this.x.getBlue(), n), matrixStack);
        Color color = new Color(255, 255, 255, n);
        float f8 = f6 + 16.0f + 5.0f;
        v6hnga2.a(this.t, f8, (double)(f7 + 8.0f - 4.5f), color, matrixStack);
        float f9 = f2 + 9.0f;
        if (!this.B.isEmpty()) {
            f9 += 3.0f;
            Color color2 = new Color(223, 223, 243, n);
            Iterator<String> iterator = this.B.iterator();
            while (iterator.hasNext()) {
                v6hnga3.a(iterator.next(), f8, (double)f9, color2, matrixStack);
                f9 += 8.0f;
            }
        }
        if (this.v != null) {
            Color color3;
            this.D = f;
            this.E = f9 + 5.0f;
            this.F = f3;
            this.G = 15.0f;
            this.s();
            if (this.C) {
                int n2 = this.y.getRed();
                int n3 = Math.min(255, (n2 | 0x1E) + (n2 & 0x1E));
                int n4 = this.y.getGreen();
                color3 = new Color(n3, Math.min(255, (n4 | 0x1E) + (n4 & 0x1E)), Math.min(255, this.y.getBlue() - -31 - 1), n);
            } else {
                color3 = new Color(this.y.getRed(), this.y.getGreen(), this.y.getBlue(), n);
            }
            s7swsm2.a(this.D, this.E, this.F, this.G, 13.0f, color3, matrixStack);
            v6hnga4.a(this.v, this.D + (this.F - v6hnga4.a(this.v)) / 2.0f, (double)(this.E + this.G / 2.0f - 3.5f), new Color(255, 255, 255, n), matrixStack);
        }
    }

    public boolean a(double d, double d2, int n) {
        return false;
    }

    public boolean m() {
        return BooleanCoercion.from(this.z != null ? 1 : 0);
    }

    public boolean o() {
        return BooleanCoercion.from(this.v != null ? 1 : 0);
    }

    public String p() {
        return this.t;
    }

    private double t() {
        double[] dArray = new double[1];
        GLFW.glfwGetCursorPos((long)c.getWindow().getHandle(), (double[])dArray, (double[])new double[1]);
        return dArray[0] / 2.0;
    }

    public boolean q() {
        return this.A;
    }

    private double u() {
        double[] dArray = new double[1];
        GLFW.glfwGetCursorPos((long)c.getWindow().getHandle(), (double[])new double[1], (double[])dArray);
        return dArray[0] / 2.0;
    }

    private void r() {
        this.B = new ArrayList<String>();
        if (this.u == null || this.u.isEmpty()) {
            return;
        }
        FontRenderer v6hnga2 = ClientFonts.a[13];
        float f = this.a() - -9.0f - 16.0f - 5.0f;
        for (String string : this.u.split("crypt")) {
            if (string.isEmpty()) {
                this.B.add("");
                continue;
            }
            String[] stringArray = string.split("crypt");
            StringBuilder stringBuilder = new StringBuilder();
            for (String string2 : stringArray) {
                String string3 = stringBuilder.length() == 0 ? string2 : uqw6p7.$sf$0(String.valueOf(stringBuilder), string2);
                if (v6hnga2.a(string3) <= f || stringBuilder.length() <= 0) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("crypt");
                    }
                    stringBuilder.append(string2);
                    continue;
                }
                this.B.add(stringBuilder.toString());
                stringBuilder = new StringBuilder(string2);
            }
            if (stringBuilder.length() <= 0) continue;
            this.B.add(stringBuilder.toString());
        }
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return string + "Ẓ?Ẁ?Ữ?Ằﲠ" + string2;
    }
}

