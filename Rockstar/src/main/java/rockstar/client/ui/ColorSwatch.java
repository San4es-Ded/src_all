package rockstar.client.ui;



import rockstar.client.render.*;
import rockstar.client.*;
import java.util.function.Supplier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.UiNode;

public class ColorSwatch
extends UiNode {
    private final Supplier<ColorRGBA> internalField0017;
    private float internalField0205 = 2.0f;

    public ColorSwatch(Supplier<ColorRGBA> supplier) {
        this.internalField0017 = supplier;
        this.internalMethod06125(10.0f, 10.0f);
    }

    public ColorSwatch internalMethod06567(float f) {
        this.internalField0205 = f;
        return this;
    }

    public ColorSwatch internalMethod00330(float f) {
        super.width(f);
        return this;
    }

    public ColorSwatch internalMethod08460(float f) {
        super.height(f);
        return this;
    }

    public ColorSwatch internalMethod06125(float f, float f2) {
        super.size(f, f2);
        return this;
    }

    @Override
    protected void drawSelf(UiRenderContext iII, float f) {
        float f2 = this.x();
        float f3 = this.y();
        float f4 = this.w();
        float f5 = this.h();
        iII.drawRoundedRect(f2, f3, f4, f5, CornerRadii.internalMethod03908(f4 / 2.0f), ThemeColors.internalField1616);
        float f6 = f2 + this.internalField0205;
        float f7 = f3 + this.internalField0205;
        float f8 = f4 - this.internalField0205 * 2.0f;
        float f9 = f5 - this.internalField0205 * 2.0f;
        ColorRGBA colorRGBA = this.internalField0017.get();
        if (f8 > 0.0f && f9 > 0.0f && colorRGBA != null) {
            iII.drawRoundedRect(f6, f7, f8, f9, CornerRadii.internalMethod03908(f8 / 2.0f), colorRGBA);
        }
    }

}

