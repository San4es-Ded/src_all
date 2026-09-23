package rockstar.client.ui;



import rockstar.client.render.*;
import rockstar.client.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.util.math.MatrixStack;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.ui.UiElement;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiNode;

public class TextLabel
extends UiElement {
    private final SizedFont internalField0447;
    private final Supplier<String> internalField0017;
    private Function<TextLabel, ColorRGBA> internalField0571 = typedValue011 -> ColorRGBA.WHITE;
    private float internalField0205 = 8.0f;
    private float internalField0206 = 8.0f;
    private float internalField1048 = 35.0f;
    private float internalField1047 = 600.0f;
    private BooleanSupplier internalField0424;
    private boolean internalField0277;
    private float internalField1049;
    private boolean internalField0276 = true;
    private float internalField1046;

    public TextLabel(SizedFont typedValue020, String string) {
        this(typedValue020, () -> string);
    }

    public TextLabel(SizedFont typedValue020, Supplier<String> supplier) {
        this.internalField0447 = typedValue020;
        this.internalField0017 = supplier;
        this.internalMethod08361(typedValue020.internalMethod04890());
    }

    public TextLabel internalMethod04797(ColorRGBA colorRGBA) {
        this.internalField0571 = typedValue011 -> colorRGBA;
        return this;
    }

    public TextLabel internalMethod06955(Supplier<ColorRGBA> supplier) {
        this.internalField0571 = typedValue011 -> (ColorRGBA)supplier.get();
        return this;
    }

    public TextLabel internalMethod02959(Function<TextLabel, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public TextLabel internalMethod02300(float f, float f2) {
        this.internalField0205 = f;
        this.internalField0206 = f2;
        return this;
    }

    public TextLabel internalMethod05903(float f) {
        this.internalField0205 = f;
        return this;
    }

    public TextLabel internalMethod01671(float f) {
        this.internalField0206 = f;
        return this;
    }

    public TextLabel internalMethod07682(float f) {
        this.internalField1048 = f;
        return this;
    }

    public TextLabel internalMethod08097(float f) {
        this.internalField1047 = f;
        return this;
    }

    public TextLabel internalMethod04365(BooleanSupplier booleanSupplier) {
        this.internalField0424 = booleanSupplier;
        return this;
    }

    public TextLabel internalMethod06815() {
        this.internalField0424 = () -> true;
        return this;
    }

    public TextLabel internalMethod02902() {
        this.internalField0277 = true;
        return this;
    }

    public TextLabel internalMethod08003(float f) {
        super.width(f);
        return this;
    }

    public TextLabel internalMethod08361(float f) {
        super.height(f);
        return this;
    }

    public TextLabel internalMethod03610(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    public TextLabel internalMethod08062() {
        super.fillWidth();
        return this;
    }

    public TextLabel internalMethod08869() {
        super.fillHeight();
        return this;
    }

    @Override
    protected void onTick(float f, float f2, float f3) {
        if (!this.inFlow()) {
            return;
        }
        String string = this.internalField0017.get();
        float f4 = Math.max(0.0f, (string == null ? 0.0f : this.internalField0447.internalMethod00965(string)) - this.w());
        if (f4 <= 0.0f) {
            this.internalField1049 = 0.0f;
            this.internalField0276 = true;
            this.internalField1046 = 0.0f;
            return;
        }
        this.internalField1049 = Math.min(this.internalField1049, f4);
        float f5 = this.internalField1048 * f / 1000.0f;
        if (!this.internalMethod03306(f2, f3)) {
            this.internalField0276 = true;
            this.internalField1046 = 0.0f;
            this.internalField1049 = Math.max(0.0f, this.internalField1049 - f5);
            return;
        }
        if (this.internalField1046 > 0.0f) {
            this.internalField1046 -= f;
            return;
        }
        if (this.internalField0276) {
            this.internalField1049 = Math.min(this.internalField1049 + f5, f4);
            if (this.internalField1049 >= f4) {
                this.internalField0276 = false;
                this.internalField1046 = this.internalField1047;
            }
        } else {
            this.internalField1049 = Math.max(this.internalField1049 - f5, 0.0f);
            if (this.internalField1049 <= 0.0f) {
                this.internalField0276 = true;
                this.internalField1046 = this.internalField1047;
            }
        }
    }

    private boolean internalMethod03306(float f, float f2) {
        if (this.internalField0424 != null) {
            return this.internalField0424.getAsBoolean();
        }
        UiNode typedValue004 = this.parent();
        return (typedValue004 != null ? typedValue004 : this).contains(f, f2);
    }

    @Override
    protected void drawSelf(UiRenderContext iII, float f) {
        String string = this.internalField0017.get();
        if (string == null || string.isEmpty()) {
            return;
        }
        ColorRGBA colorRGBA = this.internalField0571.apply(this);
        float f2 = this.x();
        float f3 = this.y();
        float f4 = this.w();
        float f5 = f3 + this.h() / 2.0f - this.internalField0447.internalMethod04890() / 2.0f;
        float f6 = Math.max(0.0f, this.internalField0447.internalMethod00965(string) - f4);
        if (this.internalField0277) {
            UiNode typedValue004 = this.parent();
            float f7 = typedValue004 == null ? f3 - 3.0f : typedValue004.y() - 3.0f;
            float f8 = Math.max(typedValue004 == null ? this.h() : typedValue004.h(), this.internalField0447.internalMethod04890() + 4.0f) + 6.0f;
            ScissorStack.internalMethod06303(iII.getMatrices(), f2 - 3.0f, f7, f4 + 6.0f, f8);
            iII.pushMatrix();
            iII.getMatrices().translate(-this.internalField1049, 0.0f);
            float f9 = Math.max(1.0f, f4 + this.internalField1049);
            float f10 = (this.internalField1049 + f4 * 0.95f) / f9;
            iII.drawFadeoutText(this.internalField0447, string, f2, f5, colorRGBA, f10, 1.0f, f9);
            iII.popMatrix();
            ScissorStack.internalMethod07643();
            return;
        }
        if (f6 <= 0.0f) {
            iII.drawText(this.internalField0447, string, f2, f5, colorRGBA);
            return;
        }
        float f11 = Math.min(this.internalField0205, this.internalField1049);
        float f12 = Math.min(this.internalField0206, f6 - this.internalField1049);
        org.joml.Matrix3x2fStack matrixStack = iII.getMatrices();
        ScissorStack.internalMethod06303(matrixStack, f2, f3 - 5.0f, f4, this.h() + 10.0f);
        matrixStack.pushMatrix();
        matrixStack.translate(-this.internalField1049 + 0.5f, 0.0f);
        iII.drawFadeText(this.internalField0447, string, f2, f5, colorRGBA, f11, f12, f4);
        matrixStack.popMatrix();
        ScissorStack.internalMethod07643();
    }

}

