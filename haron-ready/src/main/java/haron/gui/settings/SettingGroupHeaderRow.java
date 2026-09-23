package haron.gui.settings;

import haron.core.BooleanCoercion;
import haron.gui.settings.SettingRow;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.settings.SettingGroup;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class SettingGroupHeaderRow
implements SettingRow {
    public static final float a = 18.0f;
    private static final float d = 8.0f;
    private static final float e = 6.0f;
    private static final float f = 1.0f;
    private final String g;
    private final SettingGroup h;
    public static int b;

    public SettingGroupHeaderRow(SettingGroup rfy6ep2) {
        this.h = rfy6ep2;
        this.g = rfy6ep2.f();
    }

    public SettingGroupHeaderRow(String string) {
        this.h = null;
        this.g = string;
    }

    @Override
    public float b() {
        return 18.0f;
    }

    @Override
    public boolean d() {
        return BooleanCoercion.from(this.h == null || this.h.m() ? 1 : 0);
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        return false;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        float f6;
        float f7;
        FontRenderer v6hnga2 = ClientFonts.a[16];
        float f8 = 18.0f * f5;
        float f9 = 8.0f * f5;
        float f10 = 6.0f * f5;
        float f11 = 1.0f * f5;
        int n3 = (int)(255.0f * f4);
        float f12 = v6hnga2.a(this.g) * f5;
        float f13 = f + f3 / 2.0f - f12 / 2.0f;
        float f14 = f2 + f8 / 2.0f - v6hnga2.b(this.g) * f5 / 4.0f;
        Color color = pryrvd.a(pryrvd.b, (float)n3 / 1.5f);
        matrixStack.push();
        matrixStack.translate(f13, f14, 0.0f);
        matrixStack.scale(f5, f5, 1.0f);
        matrixStack.translate(-f13, -f14, 0.0f);
        v6hnga2.a(this.g, f13, (double)f14, color, matrixStack);
        matrixStack.pop();
        float f15 = f2 + f8 / 2.0f - f11 / 2.0f;
        Color color2 = pryrvd.a(new Color(20, 20, 28), n3);
        float f16 = f + f9;
        float f17 = f13 - f10;
        if (f17 > f16) {
            s7swsm2.a(f16, f15, f17 - f16, f11, color2, matrixStack);
        }
        if ((f7 = f + f3 - f9) > (f6 = f13 + f12 + f10)) {
            s7swsm2.a(f6, f15, f7 - f6, f11, color2, matrixStack);
        }
    }

    @Override
    public String a() {
        return this.g;
    }
}

