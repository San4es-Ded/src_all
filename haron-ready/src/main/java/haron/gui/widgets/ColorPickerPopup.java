package haron.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.gui.core.GuiInput;
import haron.gui.widgets.PopupSide;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.icons.IconTexture;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;

public class ColorPickerPopup {
    private static final ColorPickerPopup c = new ColorPickerPopup();
    private static final float d = 84.5f;
    private static final float e = 10.5f;
    private static final float f = 7.0f;
    private static final float g = 9.5f;
    private static final float h = 63.5f;
    private static final float i = 41.5f;
    private static final float j = 7.0f;
    private static final float k = 4.0f;
    private static final float l = 2.0f;
    private static final float m = 9.0f;
    private static final float n = 1.5f;
    private static final float o = 3.0f;
    private static final float p = 7.0f;
    private static final float q = 1.0f;
    private float t;
    private float u;
    private float y;
    private float z;
    private float A;
    private Consumer<Color> B;
    private float E;
    private float F;
    private float G;
    private float H;
    private float I;
    private float J;
    private float K;
    private float L;
    private float M;
    private float N;
    private float O;
    private float P;
    private float Q;
    private float R;
    private float S;
    public static int a;
    public static boolean b;
    private boolean r = false;
    private final AnimatedValue s = new AnimatedValue();
    private PopupSide v = PopupSide.BOTTOM;
    private String w = "";
    private float x = 1.0f;
    private boolean C = false;
    private boolean D = false;
    private String[] modes;
    private int modeIdx = -1;
    private IntConsumer modeCB;
    private float modesY;
    private float modesH;
    private static final float MODE_ROW_H = 13.0f;
    private static final float MODE_GAP = 4.0f;

    private void drawModes(MatrixStack matrixStack, ShapeRenderer s7swsm2, FontRenderer v6hnga2, float f, float f2, float f3, float f4, float f5, float f6, float f7, int n) {
        float f8 = 13.0f * this.x;
        float f9 = 84.5f * this.x - f7 * 2.0f;
        float f10 = 3.5f * this.x;
        for (int i = 0; i < this.modes.length; ++i) {
            float f11 = f3 + (float)i * f8;
            float f12 = this.a(f, f2, f5);
            float f13 = this.a(f11, f4, f5);
            float f14 = this.a(f9, f5);
            float f15 = this.a(f8 - 1.5f, f5);
            float f16 = this.a(f10, f5);
            boolean bl = i == this.modeIdx;
            Color color = pryrvd.a(bl ? pryrvd.C : pryrvd.q, n);
            Color color2 = pryrvd.a(bl ? pryrvd.D : pryrvd.r, n);
            s7swsm2.a(f12, f13, f14, f15, f16, color, color, color2, color2, matrixStack);
            Color color3 = pryrvd.a(bl ? pryrvd.aa : pryrvd.b, n);
            String string = this.modes[i];
            float f17 = v6hnga2.b(string) * this.x * f5 / 2.0f;
            float f18 = Math.round(f12 + 5.0f * f5);
            float f19 = Math.round(f13 + (f15 / 2.0f - f17));
            float f20 = this.x * f5;
            matrixStack.push();
            matrixStack.translate(f18, f19, 0.0f);
            matrixStack.scale(f20, f20, 1.0f);
            matrixStack.translate(-f18, -f19, 0.0f);
            v6hnga2.a(string, f18, (double)(f19 + 4.0f), color3, matrixStack);
            matrixStack.pop();
        }
    }

    private ColorPickerPopup() {
    }

    public Color e() {
        return Color.getHSBColor(this.y, this.z, this.A);
    }

    public void b(int n, int n2) {
        this.C = false;
        this.D = false;
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5, float f6) {
        s7swsm2.a(f, f2, f3, f4, f5, f6, matrixStack);
    }

    private void b(float f, float f2) {
        IconTexture hclqea2 = HaronIcons.getInfo("arrow_v");
        float f3 = hclqea2 != null ? (float)hclqea2.b() / 2.0f * this.x : 0.0f;
        switch (this.v.ordinal()) {
            case 0: {
                this.E = this.t - f / 2.0f;
                this.F = this.u - f2 - f3 - 1.0f * this.x;
                break;
            }
            case 1: {
                this.E = this.t - f / 2.0f;
                this.F = this.u + f3 + 1.0f * this.x;
                break;
            }
            case 2: {
                this.E = this.t - f - f3 - 1.0f * this.x;
                this.F = this.u - f2 / 2.0f;
                break;
            }
            case 3: {
                this.E = this.t + f3 + 1.0f * this.x;
                this.F = this.u - f2 / 2.0f;
            }
        }
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        float f5 = 3.0f * this.x * f3;
        float f6 = 7.0f * this.x * f3;
        s7swsm2.a(f - f5 / 2.0f, f2 - f6 / 2.0f, f5, f6, f5, pryrvd.a(pryrvd.aa, f4), matrixStack);
    }

    public void b() {
        if (this.r) {
            this.r = false;
            this.s.a(0.0, 0.15, Easings.h);
            this.C = false;
            this.D = false;
            this.modes = null;
            this.modeIdx = -1;
            this.modeCB = null;
        }
    }

    public boolean c(int n, int n2) {
        if (!this.c()) {
            return false;
        }
        float f = (float)this.s.j();
        float f2 = this.G * f;
        float f3 = this.H * f;
        return GuiInput.a(this.I - f2 / 2.0f, this.J - f3 / 2.0f, f2, f3, (double)n, (double)n2);
    }

    public boolean c() {
        return this.r || this.s.j() > 0.01;
    }

    public float[] f() {
        if (!this.c()) {
            return null;
        }
        float f = (float)this.s.j();
        float f2 = this.G * f;
        float f3 = this.H * f;
        return new float[]{this.I - f2 / 2.0f, this.J - f3 / 2.0f, f2, f3};
    }

    private void d(int n, int n2) {
        float f = (float)n - this.K;
        float f2 = (float)n2 - this.L;
        this.z = Math.max(0.0f, Math.min(1.0f, f / this.M));
        this.A = Math.max(0.0f, Math.min(1.0f, 1.0f - f2 / this.N));
        this.g();
    }

    public boolean d() {
        return this.r && !(this.s.j() <= 0.5);
    }

    public void a(int n, int n2, double d, double d2) {
        if (this.r) {
            if (this.C) {
                this.d(n, n2);
            }
            if (this.D) {
                this.a(n);
            }
        }
    }

    public boolean a(int n, int n2) {
        if (this.r) {
            float f;
            if (this.modes != null && this.modes.length > 0) {
                f = 13.0f * this.x;
                float f2 = this.G - 4.0f * this.x * 2.0f;
                for (int i = 0; i < this.modes.length; ++i) {
                    float f3 = this.modesY + (float)i * f;
                    if (!GuiInput.a(this.E + 4.0f * this.x, f3, f2, f - 1.5f, (double)n, (double)n2)) continue;
                    this.modeIdx = i;
                    if (this.modeCB != null) {
                        this.modeCB.accept(i);
                    }
                    return true;
                }
            }
            if (GuiInput.a(this.K, this.L, this.M, this.N, (double)n, (double)n2)) {
                this.C = true;
                this.d(n, n2);
                return true;
            }
            f = 4.0f * this.x * this.S;
            if (!GuiInput.a(this.O, this.P - f, this.Q, this.R + f * 2.0f, (double)n, (double)n2)) {
                return this.c(n, n2);
            }
            this.D = true;
            this.a(n);
            return true;
        }
        return false;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5, float f6) {
        Color color = Color.getHSBColor(this.y, 1.0f, 1.0f);
        int n = (int)(255.0f * f6);
        Color color2 = pryrvd.a(pryrvd.aa, n);
        Color color3 = pryrvd.a(color, n);
        Color color4 = pryrvd.a(pryrvd.FriendCard, n);
        s7swsm2.a(f, f2, f3, f4, f5, color2, color3, color4, color4, matrixStack);
    }

    private Color a(Color color, float f) {
        return pryrvd.b(color, f);
    }

    private void a(int n) {
        this.y = Math.max(0.0f, Math.min(1.0f, ((float)n - this.O) / this.Q));
        this.g();
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
        IconTexture hclqea2 = HaronIcons.getInfo("arrow_v");
        if (hclqea2 != null) {
            float f3;
            float f4;
            Identifier identifier = hclqea2.a();
            RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
            float f5 = (float)hclqea2.b() / 2.0f * this.x * f;
            float f6 = (float)hclqea2.c() / 2.0f * this.x * f;
            Color color = pryrvd.a(pryrvd.aa, f2 * f);
            float f7 = this.a(this.E, this.I, f);
            float f8 = this.a(this.F, this.J, f) + 2.5f;
            float f9 = this.a(this.G, f);
            float f10 = this.a(this.H, f);
            float f11 = 1.0f * this.x * f;
            float f12 = switch (this.v.ordinal()) {
                case 0 -> {
                    f4 = f7 + f9 / 2.0f;
                    f3 = f8 + f10 + f6 / 2.0f + f11;
                    yield 90.0f;
                }
                case 1 -> {
                    f4 = f7 + f9 / 2.0f;
                    f3 = f8 - f6 / 2.0f - f11;
                    yield -90.0f;
                }
                case 2 -> {
                    f4 = f7 + f9 + f5 / 2.0f + f11;
                    f3 = f8 + f10 / 2.0f;
                    yield 0.0f;
                }
                default -> {
                    f4 = f7 - f5 / 2.0f - f11;
                    f3 = f8 + f10 / 2.0f;
                    yield 180.0f;
                }
            };
            matrixStack.push();
            matrixStack.translate(f4, f3, 0.0f);
            matrixStack.multiply(new Quaternionf().rotationZ((float)Math.toRadians(f12)));
            matrixStack.translate(-f4, -f3, 0.0f);
            s7swsm2.a(identifier, f4 - f5 / 2.0f, f3 - f6 / 2.0f, f5, f6, color, matrixStack);
            matrixStack.pop();
        }
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2, float f) {
        float f2;
        this.s.a();
        this.S = f2 = (float)this.s.j();
        if (f2 >= 0.01f) {
            int n3;
            FontRenderer v6hnga2 = ClientFonts.a[14];
            float f3 = 84.5f * this.x;
            float f4 = 10.5f * this.x;
            float f5 = 7.0f * this.x;
            float f6 = 9.5f * this.x;
            float f7 = 63.5f * this.x;
            float f8 = 41.5f * this.x;
            float f9 = 4.0f * this.x;
            float f10 = v6hnga2.b(this.w) * this.x + 2.0f * this.x;
            this.modesH = this.modes == null || this.modes.length == 0 ? 0.0f : (float)this.modes.length * 13.0f * this.x + 4.0f * this.x;
            float f11 = f10 + this.modesH + f8 + f5 + f9 + f4 * 2.0f;
            this.G = f3;
            this.H = f11;
            this.b(f3, f11);
            this.I = this.E + f3 / 2.0f;
            this.J = this.F + f11 / 2.0f;
            float f12 = this.E + f4;
            float f13 = this.F + f4;
            float f14 = f13 + f10 + this.modesH;
            float f15 = f14 + f8 + f5;
            this.K = f12;
            this.L = f14;
            this.M = f7;
            this.N = f8;
            this.O = f12;
            this.P = f15;
            this.Q = f7;
            this.R = f9;
            float f16 = this.a(this.E, this.I, f2);
            float f17 = this.a(this.F, this.J, f2);
            float f18 = this.a(f3, f2);
            float f19 = this.a(f11, f2);
            float f20 = this.a(f6, f2);
            this.a(f4, f2);
            this.a(f5, f2);
            this.a(matrixStack, s7swsm2, f2, f);
            Color color = this.a(pryrvd.e, f * f2);
            Color color2 = this.a(pryrvd.f, f * f2);
            s7swsm2.a(f16, f17, f18, f19, f20, color, color, color2, color2, matrixStack);
            if (f2 > 0.3f) {
                float f21 = Math.min(1.0f, (f2 - 0.3f) / 0.7f);
                n3 = (int)(255.0f * f * f21);
                s7swsm2.b().a(f16, f17, f18, f19, f20, matrixStack);
                float f22 = this.E + f4;
                float f23 = this.F + f4;
                float f24 = this.a(f22, this.I, f2);
                float f25 = this.a(f23, this.J, f2);
                this.a(f10, f2);
                float f26 = this.x * f2;
                Color color3 = pryrvd.a(pryrvd.a, n3);
                matrixStack.push();
                matrixStack.translate(f24, f25, 0.0f);
                matrixStack.scale(f26, f26, 1.0f);
                matrixStack.translate(-f24, -f25, 0.0f);
                v6hnga2.a(this.w, f24, (double)f25, color3, matrixStack);
                matrixStack.pop();
                float f27 = f23 + f10 + this.modesH;
                this.modesY = f23 + f10;
                if (this.modes != null && this.modes.length > 0 && f21 > 0.01f) {
                    this.drawModes(matrixStack, s7swsm2, v6hnga2, f22, this.I, this.modesY, this.J, f2, f21 * f, f4, n3);
                }
                float f28 = this.a(f22, this.I, f2);
                float f29 = this.a(f27, this.J, f2);
                float f30 = this.a(f7, f2);
                float f31 = this.a(f8, f2);
                this.a(matrixStack, s7swsm2, f28, f29, f30, f31, this.a(7.0f * this.x, f2), f21 * f);
                this.a(matrixStack, s7swsm2, this.a(f22 + this.z * f7, this.I, f2), this.a(f27 + (1.0f - this.A) * f8, this.J, f2), f2, f21 * f);
                float f32 = f27 + f8 + f5;
                float f33 = this.a(f22, this.I, f2);
                float f34 = this.a(f32, this.J, f2);
                float f35 = this.a(f7, f2);
                float f36 = this.a(f9, f2);
                this.b(matrixStack, s7swsm2, f33, f34, f35, f36, this.a(2.0f * this.x, f2), f21 * f);
                this.b(matrixStack, s7swsm2, this.a(f22 + this.y * f7, this.I, f2), this.a(f32 + f9 / 2.0f, this.J, f2), f2, f21 * f);
                s7swsm2.b().a(matrixStack);
            }
            if (!this.r) {
                if (1703664633 < a) {
                    return;
                }
                return;
            }
            if (f2 > 0.8f) {
                boolean bl = GuiInput.a(this.K, this.L, this.M, this.N, (double)n, (double)n2);
                n3 = GuiInput.a(this.O, this.P - 4.0f * this.x * f2, this.Q, this.R + 8.0f * this.x * f2, (double)n, (double)n2) ? 1 : 0;
                if (bl || n3 != 0 || this.C || this.D) {
                    GuiInput.g();
                }
            }
        }
    }

    private float a(float f, float f2) {
        return f * f2;
    }

    private float a(float f, float f2, float f3) {
        return f2 + (f - f2) * f3;
    }

    public void a(float f, float f2, String string, Color color, float f3, Consumer<Color> consumer) {
        this.a(f, f2, PopupSide.BOTTOM, string, color, f3, consumer);
    }

    public void a(float f, float f2, PopupSide rrs4ry2, String string, Color color, float f3, Consumer<Color> consumer, String[] stringArray, int n, IntConsumer intConsumer) {
        if (this.r) {
            this.b();
        }
        this.t = f;
        this.u = f2;
        this.v = rrs4ry2 != null ? rrs4ry2 : PopupSide.BOTTOM;
        this.w = string != null ? string : "";
        this.x = f3;
        this.B = consumer;
        this.modes = stringArray;
        this.modeIdx = n;
        this.modeCB = intConsumer;
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.y = fArray[0];
        this.z = fArray[1];
        this.A = fArray[2];
        this.r = true;
        this.s.d(0.0);
        this.s.a(1.0, 0.2, Easings.h);
    }

    public static ColorPickerPopup a() {
        int n = 278;
        return c;
    }

    public void a(float f, float f2, PopupSide rrs4ry2, String string, Color color, float f3, Consumer<Color> consumer) {
        this.a(f, f2, rrs4ry2, string, color, f3, consumer, null, -1, null);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        float f5 = 9.0f * this.x * f3;
        float f6 = f5 - 1.5f * this.x * f3 * 2.0f;
        int n = (int)(255.0f * f4);
        s7swsm2.a(f - f6 / 2.0f - 2.0f, f2 - f6 / 2.0f - 2.0f, 5.0f, pryrvd.ad, matrixStack);
        s7swsm2.a(f - f5 / 2.0f, f2 - f5 / 2.0f, f5, f5, f5, pryrvd.a(pryrvd.aa, n), matrixStack);
        s7swsm2.a(f - f6 / 2.0f, f2 - f6 / 2.0f, f6, f6, f6, pryrvd.a(this.e(), n), matrixStack);
    }

    private void g() {
        if (this.B != null) {
            this.B.accept(this.e());
        }
    }
}

