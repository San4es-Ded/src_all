package haron.gui.settings;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.settings.SettingRow;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.settings.BooleanSetting;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class ToggleSettingRow
implements SettingRow {
    public boolean isEnabled() {
        return this.c();
    }

    public void setEnabled(boolean enabled) {
        this.a(enabled);
    }
    public static final float a = 20.0f;
    private static final float d = 8.0f;
    private static final float e = 18.0f;
    private static final float f = 10.0f;
    private static final float g = 5.0f;
    private final String j;
    private boolean k;
    private final BooleanSetting l;
    private final AnimatedValue m = new AnimatedValue();
    private final AnimatedValue n = new AnimatedValue();
    private final cqqvax o = new cqqvax();
    private boolean p = false;
    private boolean q = false;
    public static int b;
    public static boolean c;

    private ToggleSettingRow(BooleanSetting xcv91t2, String string, boolean bl) {
        this.l = xcv91t2;
        this.j = string;
        this.k = bl;
        this.q = bl;
        this.m.d(!bl ? 0.0 : 1.0);
    }

    public ToggleSettingRow(BooleanSetting xcv91t2) {
        this(xcv91t2, xcv91t2.f(), xcv91t2.a());
    }

    public ToggleSettingRow(String string, boolean bl) {
        this(null, string, bl);
    }

    @Override
    public float b() {
        return 20.0f;
    }

    public boolean c() {
        if (this.l != null && this.k != this.l.a()) {
            this.k = this.l.a();
        }
        return this.k;
    }

    @Override
    public boolean d() {
        int n = this.l == null || this.l.m() ? 1 : 0;
        return BooleanCoercion.from(n);
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        if (!GuiInput.a(f + f3 - 8.0f - 18.0f, f2 + 10.0f - 5.0f, 18.0f, 10.0f, (double)n, (double)n2)) {
            return false;
        }
        int n3 = this.k ? 0 : 1;
        this.k = BooleanCoercion.from(n3);
        if (this.l != null) {
            this.l.a(this.k);
        }
        return true;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5, float f6) {
        Color color;
        int n = (int)(255.0f * f4);
        float f7 = 18.0f * f5;
        float f8 = 10.0f * f5;
        float f9 = 5.0f * f5;
        float f10 = 9.0f * f5;
        float f11 = f + 0.5f * f5 + (f7 - f10 - f5) * f3;
        float f12 = f2 + 0.5f * f5;
        if (f3 > 0.01f) {
            color = pryrvd.a(pryrvd.ACCENT, (int)(255.0f * f3 * 0.5f * f4));
            s7swsm2.a(f, f2, f7, f8, f9, color, color, color, color, matrixStack);
        }
        color = ColorUtils.a(pryrvd.F, pryrvd.C, f3);
        Color color2 = ColorUtils.a(pryrvd.G, pryrvd.D, f3);
        if (f6 > 0.01f) {
            color = ColorUtils.a(color, pryrvd.ACCENT, f6 * 0.3f);
            color2 = ColorUtils.a(color2, pryrvd.ACCENT, f6 * 0.3f);
        }
        Color color3 = pryrvd.a(color, n);
        Color color4 = pryrvd.a(color2, n);
        s7swsm2.a(f - 0.5f * f5, f2 - 0.5f * f5, f7 + f5, f8 + f5, f9, color3, color3, color4, color4, matrixStack);
        Color color5 = ColorUtils.a(pryrvd.WIDGET_BG_BOT, pryrvd.y, f3);
        Color color6 = ColorUtils.a(pryrvd.H, pryrvd.z, f3);
        Color color7 = pryrvd.a(color5, n);
        Color color8 = new Color(color6.getRed(), color6.getGreen(), color6.getBlue(), Math.min(255, (int)((float)color6.getAlpha() + (float)(255 - color6.getAlpha()) * f3 * f4)));
        s7swsm2.a(f, f2, f7, f8, f9, color7, color7, color8, color8, matrixStack);
        s7swsm2.a(f11, f12, f10, f10, f10, pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.a, f3), n), matrixStack);
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        if (this.l != null && this.k != this.l.a()) {
            this.k = this.l.a();
        }
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f6 = 20.0f * f5;
        float f7 = 8.0f * f5;
        boolean bl = GuiInput.a(f, f2, f3, f6, (double)n, (double)n2);
        this.o.a(bl);
        if (bl != this.p) {
            this.n.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.p = bl;
        }
        if (this.k != this.q) {
            this.m.a(!this.k ? 0.0 : 1.0, 0.2, Easings.h);
            this.q = this.k;
        }
        this.n.a();
        this.m.a();
        float f8 = (float)this.m.j();
        float f9 = (float)this.n.j();
        if (f9 > 0.01f) {
            int n3 = (int)(f9 * 40.0f);
            Color color = pryrvd.a(pryrvd.ACCENT, n3);
            s7swsm2.a(f, f2, f3, f6, 9.5f * f5, color, color, color, color, matrixStack);
        }
        float f10 = 18.0f * f5;
        float f11 = f + f3 - f7 - f10;
        this.o.a(matrixStack, s7swsm2, v6hnga2, this.j, f + f7, f2 + f6 / 2.0f - v6hnga2.b(this.j) * f5 / 4.0f, f11 - (f + f7) - 4.0f * f5, f5, pryrvd.a, f4);
        float f12 = 10.0f * f5;
        float f13 = f2 + f6 / 2.0f - f12 / 2.0f;
        this.a(matrixStack, s7swsm2, f11, f13, f8, f4, f5, f9);
        if (GuiInput.a(f11, f13, f10, f12, (double)n, (double)n2)) {
            GuiInput.g();
        }
    }

    public void a(boolean bl) {
        this.k = bl;
        if (this.l != null) {
            this.l.a(bl);
        }
    }

    @Override
    public String a() {
        return this.j;
    }
}
