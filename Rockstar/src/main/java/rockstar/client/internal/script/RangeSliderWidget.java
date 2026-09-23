package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.Function;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.ui.UiNode;

public class RangeSliderWidget
extends UiNode {
    private final InternalType0346 internalField0884;
    private final InternalType0346 internalField0885;
    private final InternalType0345 internalField0882;
    private final InternalType0345 internalField0883;
    private final float internalField0205;
    private final float internalField0206;
    private float internalField1048;
    private Function<RangeSliderWidget, ColorRGBA> internalField0571 = typedParameter1009 -> new ColorRGBA(35.0f, 32.0f, 50.0f);
    private Function<RangeSliderWidget, ColorRGBA> internalField0570 = typedParameter1009 -> ThemeColors.internalField1310;
    private float internalField1047 = 2.5f;
    private float internalField1049 = 4.0f;
    private float internalField1046 = 1.2f;
    private final AnimatedFloat internalField0623 = new AnimatedFloat(Motion.internalField0913);
    private final AnimatedFloat internalField0624 = new AnimatedFloat(Motion.internalField0913);
    private int internalField0227;

    public RangeSliderWidget(InternalType0346 nestedValue2044, InternalType0345 nestedValue2042, InternalType0346 nestedValue2045, InternalType0345 nestedValue2043, float f, float f2) {
        this.internalField0884 = nestedValue2044;
        this.internalField0882 = nestedValue2042;
        this.internalField0885 = nestedValue2045;
        this.internalField0883 = nestedValue2043;
        this.internalField0205 = f;
        this.internalField0206 = f2;
        this.internalMethod03586(120.0f, 10.0f);
        this.cursor(CursorType.internalField0567);
        this.onClick(this::internalMethod00554);
    }

    public RangeSliderWidget internalMethod05734(float f) {
        this.internalField1048 = f;
        return this;
    }

    public RangeSliderWidget internalMethod03665(ColorRGBA colorRGBA) {
        this.internalField0571 = typedParameter1009 -> colorRGBA;
        return this;
    }

    public RangeSliderWidget internalMethod02936(Function<RangeSliderWidget, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public RangeSliderWidget internalMethod04415(ColorRGBA colorRGBA) {
        this.internalField0570 = typedParameter1009 -> colorRGBA;
        return this;
    }

    public RangeSliderWidget internalMethod04358(Function<RangeSliderWidget, ColorRGBA> function) {
        this.internalField0570 = function;
        return this;
    }

    public RangeSliderWidget internalMethod07015(float f) {
        this.internalField1047 = f;
        return this;
    }

    public RangeSliderWidget internalMethod08650(float f) {
        this.internalField1049 = f;
        return this;
    }

    public RangeSliderWidget internalMethod08906(float f) {
        this.internalField1046 = f;
        return this;
    }

    public RangeSliderWidget internalMethod05725(Motion typedParameter1004) {
        if (typedParameter1004 != null) {
            this.internalField0623.internalMethod00216(typedParameter1004);
            this.internalField0624.internalMethod00216(typedParameter1004);
        }
        return this;
    }

    public RangeSliderWidget internalMethod08824(float f) {
        super.width(f);
        return this;
    }

    public RangeSliderWidget internalMethod09073(float f) {
        super.height(f);
        return this;
    }

    public RangeSliderWidget internalMethod03586(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    public RangeSliderWidget internalMethod05567() {
        super.fillWidth();
        return this;
    }

    public RangeSliderWidget internalMethod07368() {
        super.fillHeight();
        return this;
    }

    private void internalMethod00554(MouseButton typedParameter1015, float f, float f2) {
        float f3;
        if (typedParameter1015 != MouseButton.internalField0102) {
            return;
        }
        float f4 = this.internalMethod05397(this.internalField0884.get());
        float f5 = this.internalMethod05397(this.internalField0885.get());
        float f6 = Math.abs(f - f4);
        this.internalField0227 = f6 < (f3 = Math.abs(f - f5)) ? 1 : (f3 < f6 ? 2 : (f >= f4 ? 2 : 1));
        this.internalMethod05398(f);
    }

    private float internalMethod05397(float f) {
        float f2 = this.internalField0206 - this.internalField0205;
        return this.x() + this.w() * RangeSliderWidget.internalMethod05451(f2 <= 0.0f ? 0.0f : (f - this.internalField0205) / f2);
    }

    private void internalMethod05398(float f) {
        float f2 = RangeSliderWidget.internalMethod05451((f - this.x()) / Math.max(1.0f, this.w()));
        float f3 = this.internalField0205 + (this.internalField0206 - this.internalField0205) * f2;
        if (this.internalField1048 > 0.0f) {
            f3 = (float)Math.round(f3 / this.internalField1048) * this.internalField1048;
        }
        if (this.internalField0227 == 1) {
            this.internalField0882.accept(Math.min(f3, this.internalField0885.get()));
        } else if (this.internalField0227 == 2) {
            this.internalField0883.accept(Math.max(f3, this.internalField0884.get()));
        }
    }

    @Override
    public void mouseReleased(float f, float f2, MouseButton typedParameter1015) {
        this.internalField0227 = 0;
        super.mouseReleased(f, f2, typedParameter1015);
    }

    @Override
    public void onTick(float f, float f2, float f3) {
        this.internalField0623.internalMethod03690(this.internalField0884.get());
        this.internalField0624.internalMethod03690(this.internalField0885.get());
        this.internalField0623.internalMethod08946(f);
        this.internalField0624.internalMethod08946(f);
        if (this.internalField0227 != 0 && !this.pressed()) {
            this.internalField0227 = 0;
        }
        if (this.internalField0227 != 0) {
            this.internalMethod05398(f2);
        }
    }

    private static float internalMethod05451(float f) {
        return f < 0.0f ? 0.0f : (f > 1.0f ? 1.0f : f);
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        float f2;
        float f3 = this.x();
        float f4 = this.y();
        float f5 = this.w();
        float f6 = this.h();
        float f7 = this.internalField0206 - this.internalField0205;
        float f8 = RangeSliderWidget.internalMethod05451(f7 <= 0.0f ? 0.0f : (this.internalField0623.internalMethod02046() - this.internalField0205) / f7);
        float f9 = RangeSliderWidget.internalMethod05451(f7 <= 0.0f ? 0.0f : (this.internalField0624.internalMethod02046() - this.internalField0205) / f7);
        float f10 = f3 + f5 * Math.min(f8, f9);
        float f11 = f3 + f5 * Math.max(f8, f9);
        float f12 = f4 + f6 / 2.0f - this.internalField1047 / 2.0f;
        float f13 = this.internalField1047 / 2.0f;
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        ColorRGBA colorRGBA2 = this.internalField0570.apply(this);
        if (colorRGBA != null && colorRGBA.getAlpha() > 0.0f) {
            iII.drawRoundedRect(f3, f12, f5, this.internalField1047, CornerRadii.internalMethod03908(f13), colorRGBA);
        }
        if ((f2 = Math.max(0.0f, f11 - f10 - 5.0f)) > 0.0f && colorRGBA2 != null && colorRGBA2.getAlpha() > 0.0f) {
            iII.drawRect(f10 + 2.5f, f12, f2, this.internalField1047, colorRGBA2);
        }
        float f14 = f4 + f6 / 2.0f;
        this.internalMethod02226(iII, f3 + f5 * f8, f14, colorRGBA2, colorRGBA);
        this.internalMethod02226(iII, f3 + f5 * f9, f14, colorRGBA2, colorRGBA);
    }

    private void internalMethod02226(UiRenderContext iII, float f, float f2, ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        float f3;
        if (colorRGBA != null && colorRGBA.getAlpha() > 0.0f) {
            iII.drawRoundedBorder(f - this.internalField1049, f2 - this.internalField1049, this.internalField1049 * 2.0f, this.internalField1049 * 2.0f, this.internalField1046 / 2.0f, CornerRadii.internalMethod03908(this.internalField1049), colorRGBA);
        }
        if ((f3 = this.internalField1049 - this.internalField1046) > 0.0f && colorRGBA2 != null && colorRGBA2.getAlpha() > 0.0f) {
            iII.drawRoundedRect(f - f3, f2 - f3, f3 * 2.0f, f3 * 2.0f, CornerRadii.internalMethod03908(f3), colorRGBA2);
        }
    }

    public static interface InternalType0346 {
        public float get();
    }

    public static interface InternalType0345 {
        public void accept(float localValue1);
    }
}

