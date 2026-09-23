package haron.gui.settings;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.settings.SettingRow;
import haron.gui.widgets.PopupSide;
import haron.gui.widgets.ColorPickerPopup;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.settings.ColorSetting;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class ColorSettingRow
implements SettingRow {
    public static final float a = 20.0f;
    private static final float d = 8.0f;
    private static final float e = 14.0f;
    private static final float f = 6.0f;
    private static final float g = 11.0f;
    private static final float h = 4.0f;
    private final String j;
    private float k;
    private float l;
    private float m;
    private final ColorSetting n;
    private final AnimatedValue o = new AnimatedValue();
    private final cqqvax p = new cqqvax();
    private boolean q = false;
    private float r;
    private float s;
    private float t = 1.0f;
    private boolean u = false;
    public static int b;
    public static boolean c;

    public ColorSettingRow(String string, float f, float f2, float f3) {
        this.n = null;
        this.j = string;
        this.k = f;
        this.l = f2;
        this.m = f3;
    }

    public ColorSettingRow(String string, Color color) {
        this.n = null;
        this.j = string;
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.k = fArray[0];
        this.l = fArray[1];
        this.m = fArray[2];
    }

    public ColorSettingRow(ColorSetting f40tf12) {
        this.n = f40tf12;
        this.j = f40tf12.f();
        this.k = f40tf12.c();
        this.l = f40tf12.d();
        this.m = f40tf12.e();
    }

    public float e() {
        return this.k;
    }

    public boolean i() {
        return BooleanCoercion.from(this.u && ColorPickerPopup.a().c() ? 1 : 0);
    }

    private void b(Color color) {
        this.a(color);
    }

    public boolean b(int n, int n2) {
        return this.u ? ColorPickerPopup.a().c(n, n2) : false;
    }

    @Override
    public float b() {
        return 20.0f;
    }

    public Color c() {
        return Color.getHSBColor(this.k, this.l, this.m);
    }

    public float[] n() {
        if (this.u) {
            return ColorPickerPopup.a().f();
        }
        return null;
    }

    public boolean h() {
        int n = 149;
        return this.u && ColorPickerPopup.a().c();
    }

    public float f() {
        return this.l;
    }

    @Override
    public boolean l() {
        return BooleanCoercion.from(this.u && ColorPickerPopup.a().c() ? 1 : 0);
    }

    @Override
    public boolean d() {
        return BooleanCoercion.from(this.n == null || this.n.m() ? 1 : 0);
    }

    @Override
    public void a(int n, int n2) {
    }

    @Override
    public void a(int n, int n2, double d, double d2) {
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5, float f6) {
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f6 = 20.0f * f5;
        float f7 = 8.0f * f5;
        this.t = f5;
        this.p.a(GuiInput.a(f, f2, f3, f6, (double)n, (double)n2));
        this.o.a();
        int n3 = (int)(255.0f * f4);
        float f8 = 14.0f * f5;
        float f9 = f + f3 - f7 - f8;
        this.p.a(matrixStack, s7swsm2, v6hnga2, this.j, f + f7, f2 + f6 / 2.0f - v6hnga2.b(this.j) * f5 / 4.0f, f9 - (f + f7) - 4.0f * f5, f5, pryrvd.a, f4);
        float f10 = f2 + f6 / 2.0f - f8 / 2.0f;
        this.r = f9;
        this.s = f10;
        boolean bl = GuiInput.a(f9, f10, f8, f8, (double)n, (double)n2);
        if (bl != this.q) {
            this.o.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.q = bl;
        }
        Color color = this.c();
        s7swsm2.a(f9, f10, f8, f8, 6.0f * f5, pryrvd.a(pryrvd.V, n3), matrixStack);
        float f11 = 11.0f * f5;
        s7swsm2.a(f9 + (f8 - f11) / 2.0f, f10 + (f8 - f11) / 2.0f, f11, f11, 4.0f * f5, pryrvd.a(color, n3), matrixStack);
        if (bl) {
            GuiInput.g();
        }
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        float f4 = 14.0f * this.t;
        if (!GuiInput.a(this.r, this.s, f4, f4, (double)n, (double)n2)) {
            return false;
        }
        if (this.u) {
            this.k();
        } else {
            this.o();
        }
        return true;
    }

    public void a(Color color) {
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.k = fArray[0];
        this.l = fArray[1];
        this.m = fArray[2];
        if (this.n != null) {
            this.n.a(color);
        }
    }

    @Override
    public String a() {
        return this.j;
    }

    public void m() {
        if (this.u) {
            this.k();
        }
    }

    private void o() {
        float f = 14.0f * this.t;
        int n = (int)(this.r + f / 2.0f);
        int n2 = (int)(this.s + f + 5.0f * this.t);
        this.u = true;
        ColorPickerPopup.a().a(n, n2, PopupSide.BOTTOM, this.j, this.c(), this.t, this::b);
    }

    public void k() {
        this.u = false;
        ColorPickerPopup.a().b();
    }

    public float g() {
        return this.m;
    }

    @Override
    public boolean j() {
        return false;
    }
}

