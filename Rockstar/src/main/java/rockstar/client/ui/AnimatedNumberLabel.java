package rockstar.client.ui;




import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.animation.Motion;
import rockstar.client.animation.AnimatedFloat;
import rockstar.client.animation.Easing;
import rockstar.client.ui.UiNode;

public class AnimatedNumberLabel
extends UiNode {
    private final SizedFont internalField0447;
    private final IntSupplier internalField0657;
    private float internalField0205 = 5.0f;
    private boolean internalField0277;
    private Function<AnimatedNumberLabel, ColorRGBA> internalField0571 = typedParameter1008 -> ColorRGBA.WHITE;
    private final String[] internalField0359 = new String[]{"", ""};
    private final String[] internalField0358 = new String[]{"", ""};
    private final AnimatedFloat[] internalField0455 = new AnimatedFloat[]{new AnimatedFloat(1.0f, Motion.internalMethod01328(500L, Easing.internalField0812)), new AnimatedFloat(1.0f, Motion.internalMethod01328(500L, Easing.internalField0812))};
    private int internalField0227 = Integer.MIN_VALUE;

    public AnimatedNumberLabel(SizedFont typedValue020, IntSupplier intSupplier) {
        this.internalField0447 = typedValue020;
        this.internalField0657 = intSupplier;
        this.internalMethod08073(typedValue020.internalMethod04890());
    }

    public AnimatedNumberLabel internalMethod02983(float f) {
        this.internalField0205 = f;
        return this;
    }

    public AnimatedNumberLabel internalMethod00089() {
        this.internalField0277 = true;
        return this;
    }

    public AnimatedNumberLabel internalMethod00525(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    public AnimatedNumberLabel internalMethod01603(ColorRGBA colorRGBA) {
        this.internalField0571 = typedParameter1008 -> colorRGBA;
        return this;
    }

    public AnimatedNumberLabel internalMethod05034(Supplier<ColorRGBA> supplier) {
        this.internalField0571 = typedParameter1008 -> (ColorRGBA)supplier.get();
        return this;
    }

    public AnimatedNumberLabel internalMethod01032(Function<AnimatedNumberLabel, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public AnimatedNumberLabel internalMethod04098(Motion typedParameter1004) {
        if (typedParameter1004 != null) {
            this.internalField0455[0].internalMethod00216(typedParameter1004);
            this.internalField0455[1].internalMethod00216(typedParameter1004);
        }
        return this;
    }

    public AnimatedNumberLabel internalMethod04985(float f) {
        super.width(f);
        return this;
    }

    public AnimatedNumberLabel internalMethod08073(float f) {
        super.height(f);
        return this;
    }

    public AnimatedNumberLabel internalMethod04066(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    public AnimatedNumberLabel internalMethod04392() {
        super.fillWidth();
        return this;
    }

    public AnimatedNumberLabel internalMethod09067() {
        super.fillHeight();
        return this;
    }

    private void internalMethod03692() {
        int n = this.internalField0657.getAsInt();
        if (n == this.internalField0227) {
            return;
        }
        this.internalField0227 = n;
        int n2 = Math.max(0, Math.min(99, n));
        String string = String.valueOf(n2 / 10);
        String string2 = this.internalField0277 ? string : (string.equals("0") ? "" : string);
        String string3 = String.valueOf(n2 % 10);
        if (!string2.equals(this.internalField0358[0])) {
            this.internalField0359[0] = this.internalField0358[0];
            this.internalField0358[0] = string2;
            this.internalField0455[0].internalMethod03759(0.0f);
            this.internalField0455[0].internalMethod03690(1.0f);
        }
        if (!string3.equals(this.internalField0358[1])) {
            this.internalField0359[1] = this.internalField0358[1];
            this.internalField0358[1] = string3;
            this.internalField0455[1].internalMethod03759(0.0f);
            this.internalField0455[1].internalMethod03690(1.0f);
        }
    }

    @Override
    protected void measure() {
        this.internalMethod03692();
        if (!this.explicitW) {
            this.prefW = this.internalField0447.internalMethod00965(this.internalField0358[0] + this.internalField0358[1]);
        }
        if (!this.explicitH) {
            this.prefH = this.internalField0447.internalMethod04890();
        }
    }

    @Override
    protected void onTick(float f, float f2, float f3) {
        this.internalMethod03692();
        this.internalField0455[0].internalMethod08946(f);
        this.internalField0455[1].internalMethod08946(f);
    }

    @Override
    protected void drawSelf(UiRenderContext iII, float f) {
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        if (colorRGBA == null) {
            return;
        }
        float f2 = this.x();
        float f3 = this.y();
        float f4 = f3 + this.h() / 2.0f - this.internalField0447.internalMethod04890() / 2.0f;
        float f5 = this.internalField0455[0].internalMethod02046();
        float f6 = this.internalField0455[1].internalMethod02046();
        float f7 = this.internalField0447.internalMethod00965(this.internalField0358[0]);
        float f8 = this.internalField0447.internalMethod00965(this.internalField0359[0]);
        this.internalMethod01083(iII, this.internalField0359[0], f2, f4, colorRGBA, f5, true);
        this.internalMethod01083(iII, this.internalField0358[0], f2, f4, colorRGBA, f5, false);
        this.internalMethod01083(iII, this.internalField0359[1], f2 + f8, f4, colorRGBA, f6, true);
        this.internalMethod01083(iII, this.internalField0358[1], f2 + f7, f4, colorRGBA, f6, false);
    }

    private void internalMethod01083(UiRenderContext iII, String string, float f, float f2, ColorRGBA colorRGBA, float f3, boolean bl) {
        float f4;
        if (string == null || string.isEmpty()) {
            return;
        }
        float f5 = f4 = bl ? 1.0f - f3 : f3;
        if (f4 <= 0.001f) {
            return;
        }
        float f6 = bl ? this.internalField0205 * f3 : -this.internalField0205 + this.internalField0205 * f3;
        iII.drawText(this.internalField0447, string, f, f2 + f6, colorRGBA.withAlpha(colorRGBA.getAlpha() * f4));
    }

}

