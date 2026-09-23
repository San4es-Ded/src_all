package haron.gui.modules;

import haron.animation.Easings;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.core.ToggleableEntry;
import haron.gui.modules.ModuleRowRenderResult;
import haron.gui.modules.ModuleRowAnimationState;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.util.math.MatrixStack;

public class ToggleEntryRenderer {
    private static final float c = 9.5f;
    private static final float d = 11.0f;
    private static final float e = 18.0f;
    private static final float f = 10.0f;
    private static final float g = 5.0f;
    private static final float h = 12.0f;
    private static final float i = 4.0f;
    private final Map<ToggleableEntry, ModuleRowAnimationState> m = new HashMap<ToggleableEntry, ModuleRowAnimationState>();
    public static int a;
    public static boolean b;

    private static Color mixColors(Color color, Color color2, int n) {
        if (n <= 0) {
            return color;
        }
        if (n >= 255) {
            return color2;
        }
        float f = (float)n / 255.0f;
        int n2 = (int)((float)color.getRed() + (float)(color2.getRed() - color.getRed()) * f);
        int n3 = (int)((float)color.getGreen() + (float)(color2.getGreen() - color.getGreen()) * f);
        int n4 = (int)((float)color.getBlue() + (float)(color2.getBlue() - color.getBlue()) * f);
        int n5 = Math.max(color.getAlpha(), color2.getAlpha());
        return new Color(n2, n3, n4, n5);
    }

    public static float b() {
        int n = 63;
        return 11.0f;
    }

    public static float c() {
        return 12.0f;
    }

    public static float d() {
        return 4.0f;
    }

    public static float a() {
        return 18.0f;
    }

    public void a(ToggleableEntry mt5wd72) {
        if (this.m.containsKey(mt5wd72)) {
            return;
        }
        ModuleRowAnimationState q03vqa2 = new ModuleRowAnimationState();
        q03vqa2.a.d(!mt5wd72.b() ? 0.0 : 1.0);
        q03vqa2.g = mt5wd72.b();
        this.m.put(mt5wd72, q03vqa2);
    }

    public ModuleRowRenderResult a(MatrixStack matrixStack, ShapeRenderer s7swsm2, ToggleableEntry mt5wd72, float f, float f2, float f3, float f4, int n, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        boolean bl5;
        boolean bl6;
        this.a(mt5wd72);
        ModuleRowAnimationState q03vqa2 = this.m.get(mt5wd72);
        FontRenderer v6hnga2 = ClientFonts.b[14];
        float f5 = f + f3 - 11.0f - 18.0f;
        float f6 = f2 + f4 / 2.0f - 5.0f;
        float f7 = f5 - 4.0f - 12.0f;
        float f8 = f2 + f4 / 2.0f - 6.0f - 0.5f;
        boolean bl7 = mt5wd72.g() || mt5wd72.h();
        int n3 = bl || bl2 || !GuiInput.a(f, f2, f3, f4, (double)n, (double)n2) ? 0 : 1;
        int n4 = !bl7 || bl || !GuiInput.a(f7, f8, 12.0f, 12.0f, (double)n, (double)n2) || n3 == 0 ? 0 : 1;
        int n5 = n3 == 0 || n4 != 0 ? 0 : 1;
        int n6 = n5;
        int n7 = bl7 && n3 != 0 && mt5wd72.d() ? 1 : 0;
        boolean bl8 = bl4 && !bl3;
        boolean bl9 = bl6 = !bl4 && bl3;
        if (bl7) {
            if (bl4) {
                if (bl2) {
                    if (bl8) {
                        q03vqa2.b.a(0.0, 0.15, Easings.h);
                        q03vqa2.d.a(1.0, 0.15, Easings.h);
                    }
                    q03vqa2.c.d(1.0);
                } else if (bl8 && q03vqa2.e) {
                    q03vqa2.b.a(0.0, 0.15, Easings.h);
                    q03vqa2.c.a(0.0, 0.2, Easings.h);
                    q03vqa2.e = false;
                }
            } else if (bl6) {
                if (n3 != 0) {
                    q03vqa2.b.a(1.0, 0.15, Easings.h);
                    q03vqa2.c.a(1.0, 0.2, Easings.h);
                    q03vqa2.e = true;
                }
                if (n4 == 0) {
                    q03vqa2.d.a(0.0, 0.15, Easings.h);
                    q03vqa2.f = false;
                }
            } else if (BooleanCoercion.from(n7) != q03vqa2.e) {
                q03vqa2.b.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
                q03vqa2.c.a(n3 == 0 ? 0.0 : 1.0, 0.2, Easings.h);
                q03vqa2.e = BooleanCoercion.from(n7);
            }
        } else if (BooleanCoercion.from(n3) != q03vqa2.e) {
            q03vqa2.b.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            q03vqa2.e = BooleanCoercion.from(n3);
        }
        if (bl7 && !bl2 && BooleanCoercion.from(n4) != q03vqa2.f) {
            q03vqa2.d.a(n4 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            q03vqa2.f = BooleanCoercion.from(n4);
        }
        if ((bl5 = mt5wd72.b()) != q03vqa2.g) {
            q03vqa2.a.a(!bl5 ? 0.0 : 1.0, 0.2, Easings.h);
            q03vqa2.g = bl5;
        }
        q03vqa2.b.a();
        q03vqa2.a.a();
        q03vqa2.c.a();
        q03vqa2.d.a();
        float f9 = (float)q03vqa2.b.j();
        float f10 = (float)q03vqa2.a.j();
        float f11 = (float)q03vqa2.c.j();
        float f12 = (float)q03vqa2.d.j();
        this.a(matrixStack, s7swsm2, f, f2, f3, f4, f9);
        v6hnga2.a(mt5wd72.a(), (int)(f + 11.0f), (double)((int)(f2 + f4 / 2.0f - v6hnga2.b(mt5wd72.a()) / 4.0f)), ColorUtils.a(pryrvd.b, pryrvd.ROW_TEXT_INACTIVE, f10), matrixStack);
        if (bl7 && f11 > 0.01f) {
            this.a(matrixStack, s7swsm2, f7, f8, f11, f12, mt5wd72);
        }
        this.a(matrixStack, s7swsm2, f5, f6, f10);
        return new ModuleRowRenderResult(BooleanCoercion.from(n6), BooleanCoercion.from(n4), f7, f8);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        Color color = pryrvd.a(pryrvd.ACCENT, (int)(f5 * 55.0f));
        s7swsm2.a(f - 2.0f, f2 - 2.0f, f3 + 4.0f, f4 + 4.0f, 11.0f, color, color, color, color, matrixStack);
        Color color2 = pryrvd.a(pryrvd.ACCENT, (int)(f5 * 30.0f));
        s7swsm2.a(f - 1.0f, f2 - 1.0f, f3 + 2.0f, f4 + 2.0f, 10.0f, color2, color2, color2, color2, matrixStack);
        s7swsm2.a(f, f2, f3, f4, 9.5f, pryrvd.ROW_BG_TOP, pryrvd.ROW_BG_TOP, pryrvd.ROW_BG_BOT, pryrvd.ROW_BG_BOT, matrixStack);
        int n = (int)(f5 * 40.0f);
        Color color3 = pryrvd.a(pryrvd.ACCENT, n);
        s7swsm2.a(f, f2, f3, f4, 9.5f, color3, color3, color3, color3, matrixStack);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, ToggleableEntry mt5wd72) {
        ClientFonts.e[24].a("", f, (double)f2, pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.N, f4), (int)(255.0f * f3)), matrixStack);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3) {
        float f4 = f + 0.5f + 8.0f * f3;
        float f5 = f2 + 0.5f;
        if (f3 > 0.01f) {
            s7swsm2.a(f + 1.0f, f2 - 3.0f, 8.0f, pryrvd.a(pryrvd.ROW_ACCENT, (int)(255.0f * f3 * 0.5f)), matrixStack);
        }
        Color color = ColorUtils.a(pryrvd.q, pryrvd.C, f3);
        Color color2 = ColorUtils.a(pryrvd.r, pryrvd.D, f3);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 19.0f, 11.0f, 5.0f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.f, pryrvd.ROW_ACCENT, f3);
        Color color4 = ColorUtils.a(pryrvd.H, pryrvd.ROW_ACCENT, f3);
        s7swsm2.a(f, f2, 18.0f, 10.0f, 5.0f, color3, color3, color4, color4, matrixStack);
        s7swsm2.a(f4, f5, 9.0f, 9.0f, 9.0f, ColorUtils.a(pryrvd.b, pryrvd.a, f3), matrixStack);
    }
}

