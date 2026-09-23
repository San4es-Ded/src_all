package haron.gui.settings;

import com.mojang.blaze3d.systems.RenderSystem;
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
import haron.settings.ItemHighlightSetting;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

public class ItemHighlightSettingRow
implements SettingRow {
    public static final float a = 20.0f;
    private static final float d = 8.0f;
    private static final float e = 18.0f;
    private static final float f = 10.0f;
    private static final float g = 5.0f;
    private static final float h = 14.0f;
    private static final float i = 6.0f;
    private static final float j = 11.0f;
    private static final float k = 4.0f;
    private final String l;
    private final Item m;
    private boolean n;
    private Color o;
    private final Color p;
    private final ItemHighlightSetting q;
    private final AnimatedValue r = new AnimatedValue();
    private final AnimatedValue s = new AnimatedValue();
    private final cqqvax t = new cqqvax();
    private boolean u = false;
    private boolean v = false;
    private float w;
    private float x;
    private float y = 1.0f;
    private boolean z = false;
    public static int b;
    public static boolean c;

    public ItemHighlightSettingRow(ItemHighlightSetting v7v81t2) {
        this.q = v7v81t2;
        this.l = v7v81t2.f();
        this.m = v7v81t2.c();
        this.n = v7v81t2.a();
        this.o = v7v81t2.d();
        this.p = v7v81t2.d();
        this.v = this.n;
        this.r.d(!this.n ? 0.0 : 1.0);
    }

    public ItemHighlightSettingRow(String string, Item item, boolean bl, Color color) {
        this.q = null;
        this.l = string;
        this.m = item;
        this.n = bl;
        this.o = color;
        this.p = color;
        this.v = bl;
        this.r.d(!bl ? 0.0 : 1.0);
    }

    public boolean e() {
        return this.n;
    }

    public float i() {
        return Color.RGBtoHSB(this.o.getRed(), this.o.getGreen(), this.o.getBlue(), null)[0];
    }

    public boolean b(int n, int n2) {
        return this.z ? ColorPickerPopup.a().c(n, n2) : false;
    }

    private void b(Color color) {
        this.a(color);
    }

    @Override
    public float b() {
        return 20.0f;
    }

    public void s() {
        if (this.z) {
            this.r();
        }
    }

    public Item c() {
        return this.m;
    }

    public float n() {
        return Color.RGBtoHSB(this.o.getRed(), this.o.getGreen(), this.o.getBlue(), null)[2];
    }

    public Color h() {
        return this.p;
    }

    public void f() {
        this.n = BooleanCoercion.from(this.n ? 0 : 1);
        if (this.q != null) {
            this.q.a(this.n);
        }
    }

    @Override
    public boolean l() {
        return BooleanCoercion.from(this.z && ColorPickerPopup.a().c() ? 1 : 0);
    }

    @Override
    public boolean d() {
        return BooleanCoercion.from(this.q == null || this.q.m() ? 1 : 0);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        Color color;
        int n = (int)(255.0f * f4);
        float f6 = 18.0f * f5;
        float f7 = 10.0f * f5;
        float f8 = 5.0f * f5;
        float f9 = 9.0f * f5;
        float f10 = f + 0.5f * f5 + (f6 - f9 - f5) * f3;
        float f11 = f2 + 0.5f * f5;
        if (f3 > 0.01f) {
            color = pryrvd.a(pryrvd.ACCENT, (int)(255.0f * f3 * 0.5f * f4));
            s7swsm2.a(f + f5 - 1.0f, f2 - 4.0f * f5, 10.0f * f5, f7 + 6.0f * f5, f8, color, color, color, color, matrixStack);
        }
        color = ColorUtils.a(pryrvd.F, pryrvd.C, f3);
        Color color2 = ColorUtils.a(pryrvd.G, pryrvd.D, f3);
        Color color3 = pryrvd.a(color, n);
        Color color4 = pryrvd.a(color2, n);
        s7swsm2.a(f - 0.5f * f5, f2 - 0.5f * f5, f6 + f5, f7 + f5, f8, color3, color3, color4, color4, matrixStack);
        Color color5 = ColorUtils.a(pryrvd.WIDGET_BG_BOT, pryrvd.y, f3);
        Color color6 = ColorUtils.a(pryrvd.H, pryrvd.z, f3);
        Color color7 = pryrvd.a(color5, n);
        Color color8 = new Color(color6.getRed(), color6.getGreen(), color6.getBlue(), Math.min(255, (int)((float)color6.getAlpha() + (float)(255 - color6.getAlpha()) * f3 * f4)));
        s7swsm2.a(f, f2, f6, f7, f8, color7, color7, color8, color8, matrixStack);
        s7swsm2.a(f10, f11, f9, f9, f9, pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.a, f3), n), matrixStack);
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        float f4 = f + f3 - 8.0f - 14.0f;
        float f5 = f2 + 10.0f - 7.0f;
        if (GuiInput.a(f4 - 18.0f - 6.0f, f2 + 10.0f - 5.0f, 18.0f, 10.0f, (double)n, (double)n2)) {
            this.f();
            return true;
        }
        if (!GuiInput.a(f4, f5, 14.0f, 14.0f, (double)n, (double)n2)) {
            return false;
        }
        if (this.p()) {
            this.r();
        } else {
            this.u();
        }
        return true;
    }

    @Override
    public void a(int n, int n2) {
    }

    @Override
    public void a(int n, int n2, double d, double d2) {
    }

    private void a(MatrixStack matrixStack, ItemStack itemStack, float f, float f2, float f3, float f4, MinecraftClient minecraftClient) {
        if (minecraftClient.player == null || itemStack.isEmpty() || f4 < 0.01f) {
            return;
        }
        DrawContext drawContext = new DrawContext(minecraftClient, minecraftClient.getBufferBuilders().getEntityVertexConsumers());
        MatrixStack matrixStack2 = drawContext.getMatrices();
        float f5 = f3 / 16.0f;
        matrixStack2.push();
        matrixStack2.translate(f, f2, 0.0f);
        matrixStack2.scale(f5, f5, 1.0f);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f4);
        drawContext.drawItem(itemStack, 0, 0);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        matrixStack2.pop();
    }

    public void a(float f, float f2, float f3) {
        this.o = Color.getHSBColor(f, f2, f3);
    }

    public void a(Color color) {
        this.o = color;
        if (this.q != null) {
            this.q.a(color);
        }
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.a[14];
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        float f6 = 20.0f * f5;
        float f7 = 8.0f * f5;
        this.y = f5;
        boolean bl = GuiInput.a(f, f2, f3, f6, (double)n, (double)n2);
        this.t.a(bl);
        if (bl != this.u) {
            this.s.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.u = bl;
        }
        if (this.n != this.v) {
            this.r.a(!this.n ? 0.0 : 1.0, 0.2, Easings.h);
            this.v = this.n;
        }
        this.s.a();
        this.r.a();
        float f8 = (float)this.r.j();
        int n3 = (int)(255.0f * f4);
        float f9 = 16.0f * f5;
        float f10 = f + f7 - 3.0f;
        float f11 = f2 + f6 / 2.0f - f9 / 2.0f;
        if (f4 > 0.01f) {
            this.a(matrixStack, new ItemStack((ItemConvertible)this.m), f10, f11, f9, f4, minecraftClient);
        }
        float f12 = 14.0f * f5;
        float f13 = f + f3 - f7 - f12;
        float f14 = f2 + f6 / 2.0f - f12 / 2.0f;
        float f15 = 18.0f * f5;
        float f16 = 10.0f * f5;
        float f17 = f13 - f15 - 6.0f * f5;
        float f18 = f2 + f6 / 2.0f - f16 / 2.0f;
        float f19 = f + f7 + f9 - 1.0f * f5;
        this.t.a(matrixStack, s7swsm2, v6hnga2, this.l, f19, f2 + f6 / 2.0f - v6hnga2.b(this.l) * f5 / 4.0f, f17 - f19 - 4.0f * f5, f5, pryrvd.a, f4);
        this.w = f13;
        this.x = f14;
        this.a(matrixStack, s7swsm2, f17, f18, f8, f4, f5);
        s7swsm2.a(f13, f14, f12, f12, 6.0f * f5, pryrvd.a(pryrvd.V, n3), matrixStack);
        float f20 = 11.0f * f5;
        s7swsm2.a(f13 + (f12 - f20) / 2.0f, f14 + (f12 - f20) / 2.0f, f20, f20, 4.0f * f5, pryrvd.a(this.o, n3), matrixStack);
        boolean bl2 = GuiInput.a(f17, f18, f15, f16, (double)n, (double)n2);
        boolean bl3 = GuiInput.a(f13, f14, f12, f12, (double)n, (double)n2);
        if (bl2 || bl3) {
            GuiInput.g();
        }
    }

    @Override
    public String a() {
        return this.l;
    }

    public void a(boolean bl) {
        this.n = bl;
        if (this.q != null) {
            this.q.a(bl);
        }
    }

    public float m() {
        return Color.RGBtoHSB(this.o.getRed(), this.o.getGreen(), this.o.getBlue(), null)[1];
    }

    public void o() {
        int n = 676;
        this.o = this.p;
    }

    public boolean p() {
        return this.z && ColorPickerPopup.a().c();
    }

    public float[] t() {
        if (this.z) {
            return ColorPickerPopup.a().f();
        }
        return null;
    }

    public Color g() {
        return this.o;
    }

    public boolean q() {
        return this.z && ColorPickerPopup.a().c();
    }

    private void u() {
        float f = 14.0f * this.y;
        int n = (int)(this.w + f / 2.0f);
        int n2 = (int)(this.x + f + 5.0f * this.y);
        this.z = true;
        ColorPickerPopup.a().a(n, n2, PopupSide.BOTTOM, this.l, this.o, this.y, this::b);
    }

    public void r() {
        this.z = false;
        ColorPickerPopup.a().b();
    }
}

