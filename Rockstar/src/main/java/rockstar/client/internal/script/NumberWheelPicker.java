package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.minecraft.client.util.math.MatrixStack;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiNode;

public class NumberWheelPicker
extends UiNode {
    static final Motion internalField0913 = Motion.internalMethod01328(420L, Easing.internalField1325);
    private static final float internalField0205 = 170.0f;
    private static final float internalField0206 = 14.0f;
    private final List<InternalType0349> internalField0416 = new ArrayList<InternalType0349>();
    private SizedFont internalField0447 = Fonts.internalField0449.internalMethod01432(9.0f);
    private SizedFont internalField0448 = Fonts.internalField1154.internalMethod01432(6.5f);
    float internalField1048 = 12.5f;
    private int internalField0227 = 5;
    private float internalField1047 = 22.0f;
    private float internalField1049 = 2.0f;
    private boolean internalField0277;
    private Function<NumberWheelPicker, ColorRGBA> internalField0571 = typedParameter1012 -> ThemeColors.internalField1614.mulAlpha(0.75f + 0.25f * typedParameter1012.hover());
    private Function<NumberWheelPicker, ColorRGBA> internalField0570 = typedParameter1012 -> ThemeColors.internalField1613;
    private Function<NumberWheelPicker, ColorRGBA> internalField1216 = typedParameter1012 -> ThemeColors.internalField1613.mulAlpha(0.38f + 0.12f * typedParameter1012.hover());
    private Function<NumberWheelPicker, ColorRGBA> internalField1213 = typedParameter1012 -> ThemeColors.internalField1613.mulAlpha(0.7f);

    public NumberWheelPicker() {
        this.cursor(CursorType.internalField0567);
        this.snapSize();
    }

    public NumberWheelPicker internalMethod05960(SizedFont typedValue020) {
        if (typedValue020 != null) {
            this.internalField0447 = typedValue020;
        }
        return this;
    }

    public NumberWheelPicker internalMethod07655(SizedFont typedValue020) {
        if (typedValue020 != null) {
            this.internalField0448 = typedValue020;
        }
        return this;
    }

    public NumberWheelPicker internalMethod03157(float f) {
        this.internalField1048 = Math.max(4.0f, f);
        return this;
    }

    public NumberWheelPicker internalMethod01310(int n) {
        this.internalField0227 = Math.max(3, n);
        return this;
    }

    public NumberWheelPicker internalMethod04583(float f) {
        this.internalField1047 = Math.max(4.0f, Math.min(35.0f, f));
        return this;
    }

    public NumberWheelPicker internalMethod07884(float f) {
        this.internalField1049 = f;
        return this;
    }

    public NumberWheelPicker internalMethod05729() {
        this.internalField0277 = true;
        return this;
    }

    public NumberWheelPicker internalMethod05859(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    public NumberWheelPicker internalMethod05538(ColorRGBA colorRGBA) {
        this.internalField0571 = typedParameter1012 -> colorRGBA;
        return this;
    }

    public NumberWheelPicker internalMethod04403(Function<NumberWheelPicker, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public NumberWheelPicker internalMethod06195(ColorRGBA colorRGBA) {
        this.internalField0570 = typedParameter1012 -> colorRGBA;
        return this;
    }

    public NumberWheelPicker internalMethod05758(Function<NumberWheelPicker, ColorRGBA> function) {
        this.internalField0570 = function;
        return this;
    }

    public NumberWheelPicker internalMethod08253(ColorRGBA colorRGBA) {
        this.internalField1216 = typedParameter1012 -> colorRGBA;
        return this;
    }

    public NumberWheelPicker internalMethod08667(Function<NumberWheelPicker, ColorRGBA> function) {
        this.internalField1216 = function;
        return this;
    }

    public NumberWheelPicker internalMethod08391(ColorRGBA colorRGBA) {
        this.internalField1213 = typedParameter1012 -> colorRGBA;
        return this;
    }

    public NumberWheelPicker internalMethod08929(Function<NumberWheelPicker, ColorRGBA> function) {
        this.internalField1213 = function;
        return this;
    }

    public NumberWheelPicker internalMethod08123(float f) {
        super.width(f);
        return this;
    }

    public NumberWheelPicker internalMethod08037(float f) {
        super.height(f);
        return this;
    }

    public NumberWheelPicker internalMethod07620(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    public NumberWheelPicker internalMethod07555() {
        super.fillWidth();
        return this;
    }

    public NumberWheelPicker internalMethod07917() {
        super.fillHeight();
        return this;
    }

    public NumberWheelPicker internalMethod05476(int n, IntSupplier intSupplier, IntConsumer intConsumer, Supplier<String> supplier) {
        if (n > 0) {
            this.internalField0416.add(new InternalType0349(n, intSupplier, intConsumer, supplier));
        }
        return this;
    }

    public NumberWheelPicker internalMethod08280() {
        this.internalField0416.clear();
        return this;
    }

    public int internalMethod00294() {
        return this.internalField0416.size();
    }

    @Override
    public void measure() {
        if (!this.explicitH) {
            this.prefH = this.internalField1048 * (float)this.internalField0227;
        }
    }

    @Override
    public void onTick(float f, float f2, float f3) {
        for (InternalType0349 nestedValue2048 : this.internalField0416) {
            nestedValue2048.internalMethod00847(f, f3);
        }
    }

    @Override
    public boolean mouseClicked(float f, float f2, MouseButton typedParameter1015) {
        if (!(this.interactive && this.inFlow() && this.contains(f, f2))) {
            return false;
        }
        if (typedParameter1015 != MouseButton.internalField0102) {
            return true;
        }
        InternalType0349 nestedValue2048 = this.internalMethod00548(f);
        if (nestedValue2048 != null) {
            nestedValue2048.internalMethod00910(f2);
        }
        return true;
    }

    @Override
    public void mouseReleased(float f, float f2, MouseButton typedParameter1015) {
        for (InternalType0349 nestedValue2048 : this.internalField0416) {
            if (!nestedValue2048.internalField0277) continue;
            nestedValue2048.internalMethod00969(nestedValue2048.internalField0276 ? 0.0f : this.internalMethod06659(f2));
        }
        super.mouseReleased(f, f2, typedParameter1015);
    }

    @Override
    public boolean mouseScrolled(float f, float f2, float f3, float f4) {
        if (!(this.interactive && this.inFlow() && this.contains(f, f2) && f4 != 0.0f)) {
            return false;
        }
        InternalType0349 nestedValue2048 = this.internalMethod00548(f);
        if (nestedValue2048 == null) {
            return false;
        }
        nestedValue2048.internalMethod00911(f4 > 0.0f ? -1 : 1);
        return true;
    }

    private InternalType0349 internalMethod00548(float f) {
        if (this.internalField0416.isEmpty() || this.w() <= 0.0f) {
            return null;
        }
        float f2 = this.w() / (float)this.internalField0416.size();
        int n = (int)((f - this.x()) / f2);
        return this.internalField0416.get(Math.max(0, Math.min(this.internalField0416.size() - 1, n)));
    }

    private float internalMethod06659(float f) {
        float f2 = (f - (this.y() + this.h() / 2.0f)) / this.internalMethod00293();
        f2 = Math.max(-0.999f, Math.min(0.999f, f2));
        return (float)(Math.asin(f2) / Math.toRadians(this.internalField1047));
    }

    private float internalMethod00293() {
        return this.internalField1048 / (float)Math.sin(Math.toRadians(this.internalField1047));
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        if (this.internalField0416.isEmpty() || this.w() <= 0.0f || this.h() <= 0.0f) {
            return;
        }
        float f2 = this.internalField1048 * 1.5f;
        float f3 = this.y() + this.h() / 2.0f - f2 / 2.0f;
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        if (colorRGBA != null && colorRGBA.getAlpha() > 0.0f) {
            iII.drawRoundedRect(this.x(), f3, this.w(), f2, CornerRadii.internalMethod03908(Math.min(f2 / 2.0f, 8.0f)), colorRGBA);
        }
        org.joml.Matrix3x2fStack matrixStack = iII.getMatrices();
        ScissorStack.internalMethod06303(matrixStack, this.x(), this.y(), this.w(), this.h());
        this.internalMethod03309(iII, this.internalField1216.apply(this), this.y(), this.y() + this.h());
        ScissorStack.internalMethod07643();
        ScissorStack.internalMethod06303(matrixStack, this.x(), f3, this.w(), f2);
        this.internalMethod03309(iII, this.internalField0570.apply(this), f3, f3 + f2);
        ScissorStack.internalMethod07643();
        this.internalMethod00243(iII);
    }

    private void internalMethod03309(UiRenderContext iII, ColorRGBA colorRGBA, float f, float f2) {
        if (colorRGBA == null || colorRGBA.getAlpha() <= 0.0f) {
            return;
        }
        float f3 = this.w() / (float)this.internalField0416.size();
        float f4 = this.y() + this.h() / 2.0f;
        float f5 = this.internalMethod00293();
        float f6 = (float)Math.toRadians(this.internalField1047);
        int n = Math.min(7, (int)Math.ceil(Math.toRadians(85.0) / (double)f6));
        float f7 = this.internalMethod06717(f3);
        org.joml.Matrix3x2fStack matrixStack = iII.getMatrices();
        for (int i = 0; i < this.internalField0416.size(); ++i) {
            InternalType0349 nestedValue2048 = this.internalField0416.get(i);
            float f8 = this.x() + (float)i * f3 + f7;
            float f9 = nestedValue2048.internalField0623.internalMethod02046();
            int n2 = Math.round(f9);
            for (int j = -n; j <= n; ++j) {
                float f10;
                float f11;
                float f12;
                int n3 = n2 + j;
                float f13 = ((float)n3 - f9) * f6;
                float f14 = (float)Math.cos(f13);
                if (f14 <= 0.02f || (f12 = (float)Math.pow(f14, 2.2)) <= 0.02f || (f11 = f4 + f5 * (float)Math.sin(f13)) + (f10 = this.internalField0447.internalMethod07850() * 0.5f * f14) < f || f11 - f10 > f2) continue;
                SizedFont typedValue020 = this.internalField0447.internalMethod01335().internalMethod01432(this.internalField0447.internalMethod07850() * (0.74f + 0.26f * f14));
                String string = nestedValue2048.internalMethod01241(Math.floorMod(n3, nestedValue2048.internalField0227), this.internalField0277);
                float f15 = this.internalField0447.internalMethod00965(string);
                float f16 = f8 - f15 + (f15 - typedValue020.internalMethod00965(string)) / 2.0f;
                float f17 = f11 - typedValue020.internalMethod04890() / 2.0f;
                matrixStack.pushMatrix();
                matrixStack.translate(0.0f, f11);
                matrixStack.scale(1.0f, f14);
                matrixStack.translate(0.0f, -f11);
                iII.drawText(typedValue020, string, f16, f17, colorRGBA.mulAlpha(f12));
                matrixStack.popMatrix();
            }
        }
    }

    private void internalMethod00243(UiRenderContext iII) {
        ColorRGBA colorRGBA = this.internalField1213.apply(this);
        if (colorRGBA == null || colorRGBA.getAlpha() <= 0.0f) {
            return;
        }
        float f = this.w() / (float)this.internalField0416.size();
        float f2 = this.y() + this.h() / 2.0f;
        float f3 = this.internalMethod06717(f);
        for (int i = 0; i < this.internalField0416.size(); ++i) {
            String string;
            InternalType0349 nestedValue2048 = this.internalField0416.get(i);
            String string2 = string = nestedValue2048.internalField0017 == null ? null : nestedValue2048.internalField0017.get();
            if (string == null || string.isEmpty()) continue;
            float f4 = this.x() + (float)i * f + f3 + this.internalField1049;
            iII.drawText(this.internalField0448, string, f4, f2 - this.internalField0448.internalMethod04890() / 2.0f, colorRGBA);
        }
    }

    private float internalMethod06717(float f) {
        float f2 = 0.0f;
        float f3 = 0.0f;
        for (InternalType0349 nestedValue2048 : this.internalField0416) {
            f2 = Math.max(f2, this.internalField0447.internalMethod00965(nestedValue2048.internalMethod05484(this.internalField0277)));
            String string = nestedValue2048.internalField0017 == null ? null : nestedValue2048.internalField0017.get();
            if (string == null || string.isEmpty()) continue;
            f3 = Math.max(f3, this.internalField0448.internalMethod00965(string));
        }
        float f4 = f3 > 0.0f ? this.internalField1049 + f3 : 0.0f;
        return (f - (f2 + f4)) / 2.0f + f2;
    }

    final class InternalType0349 {
        final int internalField0227;
        private final IntSupplier internalField0657;
        private final IntConsumer internalField0540;
        final Supplier<String> internalField0017;
        final AnimatedFloat internalField0623 = new AnimatedFloat(internalField0913);
        private int internalField0228;
        boolean internalField0277;
        boolean internalField0276;
        private float internalField0205;
        private float internalField0206;
        private float internalField1048;

        InternalType0349(int n, IntSupplier intSupplier, IntConsumer intConsumer, Supplier<String> supplier) {
            this.internalField0227 = n;
            this.internalField0657 = intSupplier;
            this.internalField0540 = intConsumer;
            this.internalField0017 = supplier;
            this.internalField0228 = this.internalMethod04452();
            this.internalField0623.internalMethod03759(this.internalField0228);
        }

        private int internalMethod04452() {
            return Math.floorMod(this.internalField0657.getAsInt(), this.internalField0227);
        }

        String internalMethod01241(int n, boolean bl) {
            return bl && n < 10 ? "0" + n : String.valueOf(n);
        }

        String internalMethod05484(boolean bl) {
            return this.internalMethod01241(this.internalField0227 - 1, bl);
        }

        void internalMethod00910(float f) {
            this.internalField0277 = true;
            this.internalField0276 = false;
            this.internalField0205 = f;
            this.internalField0206 = this.internalField0623.internalMethod02046();
            this.internalField1048 = 0.0f;
        }

        void internalMethod00969(float f) {
            this.internalField0277 = false;
            float f2 = this.internalField0623.internalMethod02046();
            float f3 = this.internalField0276 ? Math.max(-14.0f, Math.min(14.0f, this.internalField1048 * 170.0f)) : f;
            this.internalField0623.internalMethod03690(Math.round(f2 + f3));
        }

        void internalMethod00911(int n) {
            this.internalField0277 = false;
            this.internalField1048 = 0.0f;
            this.internalField0623.internalMethod03690(Math.round(this.internalField0623.internalMethod02051()) + n);
        }

        void internalMethod00847(float f, float f2) {
            this.internalMethod04453();
            if (this.internalField0277) {
                float f3 = this.internalField0206 + (this.internalField0205 - f2) / NumberWheelPicker.this.internalField1048;
                if (Math.abs(f3 - this.internalField0206) > 0.12f) {
                    this.internalField0276 = true;
                }
                float f4 = (f3 - this.internalField0623.internalMethod02046()) / Math.max(1.0f, f);
                this.internalField1048 = this.internalField1048 * 0.65f + f4 * 0.35f;
                this.internalField0623.internalMethod03759(f3);
            } else {
                int n;
                int n2;
                this.internalField0623.internalMethod08946(f);
                if (this.internalField0623.internalMethod02047() && (n2 = Math.floorMod(n = Math.round(this.internalField0623.internalMethod02046()), this.internalField0227)) != n) {
                    this.internalField0623.internalMethod03759(n2);
                }
            }
            this.internalMethod04492();
        }

        private void internalMethod04453() {
            int n = this.internalMethod04452();
            if (n == this.internalField0228 || this.internalField0277) {
                return;
            }
            this.internalField0228 = n;
            int n2 = Math.round(this.internalField0623.internalMethod02051());
            int n3 = Math.floorMod(n - Math.floorMod(n2, this.internalField0227), this.internalField0227);
            if (n3 > this.internalField0227 / 2) {
                n3 -= this.internalField0227;
            }
            this.internalField0623.internalMethod03690(n2 + n3);
        }

        private void internalMethod04492() {
            int n = Math.floorMod(Math.round(this.internalField0623.internalMethod02046()), this.internalField0227);
            if (n == this.internalField0228) {
                return;
            }
            this.internalField0228 = n;
            this.internalField0540.accept(n);
        }
    }
}

