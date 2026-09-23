package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.util.math.MatrixStack;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiNode;

public class TwoAxisPad
extends UiNode {
    private final InternalType0340 internalField0871;
    private final InternalType0340 internalField0870;
    private final InternalType0341 internalField0872;
    private final float internalField0205;
    private final float internalField0206;
    private final float internalField1048;
    private final float internalField1047;
    private final AnimatedFloat internalField0623 = new AnimatedFloat(Motion.internalMethod01328(400L, Easing.internalField1325));
    private final AnimatedFloat internalField0624 = new AnimatedFloat(Motion.internalMethod01328(400L, Easing.internalField1325));
    private boolean internalField0277;
    private Function<TwoAxisPad, ColorRGBA> internalField0571 = iIIII -> ThemeColors.internalField1614;
    private Function<TwoAxisPad, ColorRGBA> internalField0570 = iIIII -> ThemeColors.internalField1616;
    private Function<TwoAxisPad, ColorRGBA> internalField1216 = iIIII -> ThemeColors.internalField1613.mulAlpha(0.05f + 0.03f * iIIII.hover());
    private Function<TwoAxisPad, ColorRGBA> internalField1213 = iIIII -> ThemeColors.internalField1310.mulAlpha(0.85f + 0.15f * iIIII.hover());
    private float internalField1049 = 3.0f;
    private int internalField0227 = 10;
    private float internalField1046 = 3.0f;
    private float internalField1456 = 1.5f;
    private Supplier<String> internalField0017;
    private SizedFont internalField0447;
    private Function<TwoAxisPad, ColorRGBA> internalField1215 = iIIII -> ThemeColors.internalField1310;

    public TwoAxisPad(InternalType0340 nestedValue2039, InternalType0340 nestedValue2040, InternalType0341 nestedValue2041, float f, float f2, float f3, float f4) {
        this.internalField0871 = nestedValue2039;
        this.internalField0870 = nestedValue2040;
        this.internalField0872 = nestedValue2041;
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalField1048 = f3;
        this.internalField1047 = f4;
        this.internalField0623.internalMethod03759(nestedValue2039.get());
        this.internalField0624.internalMethod03759(nestedValue2040.get());
        this.snapSize();
        this.cursor(CursorType.internalField1207);
        this.onClick(this::internalMethod00364);
    }

    public TwoAxisPad internalMethod07481(ColorRGBA colorRGBA) {
        this.internalField0571 = iIIII -> colorRGBA;
        return this;
    }

    public TwoAxisPad internalMethod06116(Function<TwoAxisPad, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public TwoAxisPad internalMethod00048(ColorRGBA colorRGBA) {
        this.internalField0570 = iIIII -> colorRGBA;
        return this;
    }

    public TwoAxisPad internalMethod07454(Function<TwoAxisPad, ColorRGBA> function) {
        this.internalField0570 = function;
        return this;
    }

    public TwoAxisPad internalMethod07685(ColorRGBA colorRGBA) {
        this.internalField1216 = iIIII -> colorRGBA;
        return this;
    }

    public TwoAxisPad internalMethod08099(Function<TwoAxisPad, ColorRGBA> function) {
        this.internalField1216 = function;
        return this;
    }

    public TwoAxisPad internalMethod07902(ColorRGBA colorRGBA) {
        this.internalField1213 = iIIII -> colorRGBA;
        return this;
    }

    public TwoAxisPad internalMethod08369(Function<TwoAxisPad, ColorRGBA> function) {
        this.internalField1213 = function;
        return this;
    }

    public TwoAxisPad internalMethod05064(float f) {
        this.internalField1049 = f;
        return this;
    }

    public TwoAxisPad internalMethod01402(int n) {
        this.internalField0227 = Math.max(1, n);
        return this;
    }

    public TwoAxisPad internalMethod06338(float f) {
        this.internalField1046 = f;
        return this;
    }

    public TwoAxisPad internalMethod09088(float f) {
        this.internalField1456 = f;
        return this;
    }

    public TwoAxisPad internalMethod03261(SizedFont typedValue020, Supplier<String> supplier) {
        this.internalField0447 = typedValue020;
        this.internalField0017 = supplier;
        return this;
    }

    public TwoAxisPad internalMethod08910(ColorRGBA colorRGBA) {
        this.internalField1215 = iIIII -> colorRGBA;
        return this;
    }

    public TwoAxisPad internalMethod08338(Function<TwoAxisPad, ColorRGBA> function) {
        this.internalField1215 = function;
        return this;
    }

    public TwoAxisPad internalMethod02105(Motion typedParameter1004) {
        if (typedParameter1004 != null) {
            this.internalField0623.internalMethod00216(typedParameter1004);
            this.internalField0624.internalMethod00216(typedParameter1004);
        }
        return this;
    }

    public TwoAxisPad internalMethod07960(float f) {
        super.width(f);
        return this;
    }

    public TwoAxisPad internalMethod07880(float f) {
        super.height(f);
        return this;
    }

    public TwoAxisPad internalMethod06761(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    public TwoAxisPad internalMethod04178() {
        super.fillWidth();
        return this;
    }

    public TwoAxisPad internalMethod06045() {
        super.fillHeight();
        return this;
    }

    private void internalMethod00364(MouseButton typedParameter1015, float f, float f2) {
        if (typedParameter1015 != MouseButton.internalField0102) {
            return;
        }
        this.internalField0277 = true;
        this.internalMethod04283(f, f2);
    }

    private void internalMethod04283(float f, float f2) {
        float f3 = TwoAxisPad.internalMethod05524((f - this.x()) / Math.max(1.0f, this.w()));
        float f4 = TwoAxisPad.internalMethod05524((f2 - this.y()) / Math.max(1.0f, this.h()));
        this.internalField0872.accept(this.internalField0205 + (this.internalField0206 - this.internalField0205) * f3, this.internalField1048 + (this.internalField1047 - this.internalField1048) * f4);
    }

    @Override
    public void mouseReleased(float f, float f2, MouseButton typedParameter1015) {
        this.internalField0277 = false;
        super.mouseReleased(f, f2, typedParameter1015);
    }

    @Override
    public void measure() {
        if (!this.explicitH) {
            this.prefH = this.w();
        }
    }

    @Override
    public void onTick(float f, float f2, float f3) {
        this.internalField0623.internalMethod03690(this.internalField0871.get());
        this.internalField0623.internalMethod08946(f);
        this.internalField0624.internalMethod03690(this.internalField0870.get());
        this.internalField0624.internalMethod08946(f);
        if (this.internalField0277) {
            this.internalMethod04283(f2, f3);
        }
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        Object object;
        float f2;
        float f3 = this.x();
        float f4 = this.y();
        float f5 = this.w();
        float f6 = this.h();
        CornerRadii typedParameter1014 = CornerRadii.internalMethod03908(this.internalField1049);
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        if (colorRGBA != null && colorRGBA.getAlpha() > 0.0f) {
            iII.drawRoundedRect(f3, f4, f5, f6, typedParameter1014, colorRGBA);
        }
        org.joml.Matrix3x2fStack matrixStack = iII.getMatrices();
        ScissorStack.internalMethod06303(matrixStack, f3, f4, f5, f6);
        ColorRGBA colorRGBA2 = this.internalField1216.apply(this);
        if (colorRGBA2 != null && colorRGBA2.getAlpha() > 0.0f) {
            for (int i = 1; i < this.internalField0227; ++i) {
                f2 = (float)i / (float)this.internalField0227;
                iII.drawRect(f3 + f5 * f2, f4, 1.0f, f6, colorRGBA2);
                iII.drawRect(f3, f4 + f6 * f2, f5, 1.0f, colorRGBA2);
            }
        }
        float f7 = TwoAxisPad.internalMethod05524(TwoAxisPad.internalMethod04606(this.internalField0623.internalMethod02046(), this.internalField0205, this.internalField0206));
        f2 = TwoAxisPad.internalMethod05524(TwoAxisPad.internalMethod04606(this.internalField0624.internalMethod02046(), this.internalField1048, this.internalField1047));
        float f8 = f3 + f5 * f7;
        float f9 = f4 + f6 * f2;
        ColorRGBA colorRGBA3 = this.internalField1213.apply(this);
        float f10 = this.internalField1046;
        TwoAxisPad.internalMethod03511(iII, f8 - 0.5f, f4, 1.0f, f9 - f10 - f4, colorRGBA3);
        TwoAxisPad.internalMethod03511(iII, f8 - 0.5f, f9 + f10, 1.0f, f4 + f6 - (f9 + f10), colorRGBA3);
        TwoAxisPad.internalMethod03511(iII, f3, f9 - 0.5f, f8 - f10 - f3, 1.0f, colorRGBA3);
        TwoAxisPad.internalMethod03511(iII, f8 + f10, f9 - 0.5f, f3 + f5 - (f8 + f10), 1.0f, colorRGBA3);
        iII.drawRoundedBorder(f8 - this.internalField1046, f9 - this.internalField1046, this.internalField1046 * 2.0f, this.internalField1046 * 2.0f, this.internalField1456 / 2.0f, CornerRadii.internalMethod03908(this.internalField1046), colorRGBA3);
        if (this.internalField0017 != null && this.internalField0447 != null && (object = this.internalField0017.get()) != null && !((String)object).isEmpty()) {
            float f11 = 3.0f;
            float f12 = this.internalField0447.internalMethod00965((String)object) + f11 * 2.0f;
            float f13 = this.internalField0447.internalMethod04890() + f11 * 1.4f;
            float f14 = f3 + f5 - f12 - 2.0f;
            float f15 = f4 + 2.0f;
            iII.drawRoundedRect(f14, f15, f12, f13, CornerRadii.internalMethod03908(2.5f), ThemeColors.internalField1612.mulAlpha(0.65f));
            iII.drawText(this.internalField0447, (String)object, f14 + f11, f15 + f13 / 2.0f - this.internalField0447.internalMethod04890() / 2.0f, this.internalField1215.apply(this));
        }
        ScissorStack.internalMethod07643();
        object = this.internalField0570.apply(this);
        if (object != null && ((ColorRGBA)object).getAlpha() > 0.0f) {
            iII.drawRoundedBorder(f3, f4, f5, f6, 0.5f, typedParameter1014, (ColorRGBA)object);
        }
    }

    private static void internalMethod03511(UiRenderContext iII, float f, float f2, float f3, float f4, ColorRGBA colorRGBA) {
        if (f3 > 0.0f && f4 > 0.0f) {
            iII.drawRect(f, f2, f3, f4, colorRGBA);
        }
    }

    private static float internalMethod04606(float f, float f2, float f3) {
        return f3 - f2 == 0.0f ? 0.0f : (f - f2) / (f3 - f2);
    }

    private static float internalMethod05524(float f) {
        return f < 0.0f ? 0.0f : (f > 1.0f ? 1.0f : f);
    }

    public static interface InternalType0340 {
        public float get();
    }

    public static interface InternalType0341 {
        public void accept(float localValue1, float localValue2);
    }
}

