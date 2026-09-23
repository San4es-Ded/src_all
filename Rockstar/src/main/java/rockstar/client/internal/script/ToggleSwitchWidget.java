package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.UiElement;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;

public class ToggleSwitchWidget
extends UiElement {
    private Function<ToggleSwitchWidget, ColorRGBA> internalField0571 = typedParameter1013 -> ThemeColors.internalField1310;
    private Function<ToggleSwitchWidget, ColorRGBA> internalField0570 = typedParameter1013 -> new ColorRGBA(78.0f, 74.0f, 90.0f);
    private Function<ToggleSwitchWidget, ColorRGBA> internalField1216 = null;

    public ToggleSwitchWidget(BooleanSupplier booleanSupplier) {
        this.size(13.0f, 8.0f);
        this.cursor(CursorType.internalField0567);
        this.bind("on", booleanSupplier, Motion.internalField0913);
    }

    public ToggleSwitchWidget internalMethod07277(ColorRGBA colorRGBA) {
        this.internalField0571 = typedParameter1013 -> colorRGBA;
        return this;
    }

    public ToggleSwitchWidget internalMethod04445(Supplier<ColorRGBA> supplier) {
        this.internalField0571 = typedParameter1013 -> (ColorRGBA)supplier.get();
        return this;
    }

    public ToggleSwitchWidget internalMethod03720(Function<ToggleSwitchWidget, ColorRGBA> function) {
        this.internalField0571 = function;
        return this;
    }

    public ToggleSwitchWidget internalMethod05163(ColorRGBA colorRGBA) {
        this.internalField0570 = typedParameter1013 -> colorRGBA;
        return this;
    }

    public ToggleSwitchWidget internalMethod05792(Supplier<ColorRGBA> supplier) {
        this.internalField0570 = typedParameter1013 -> (ColorRGBA)supplier.get();
        return this;
    }

    public ToggleSwitchWidget internalMethod05119(Function<ToggleSwitchWidget, ColorRGBA> function) {
        this.internalField0570 = function;
        return this;
    }

    public ToggleSwitchWidget internalMethod08119(ColorRGBA colorRGBA) {
        this.internalField1216 = typedParameter1013 -> colorRGBA;
        return this;
    }

    public ToggleSwitchWidget internalMethod07995(Supplier<ColorRGBA> supplier) {
        this.internalField1216 = typedParameter1013 -> (ColorRGBA)supplier.get();
        return this;
    }

    public ToggleSwitchWidget internalMethod07878(Function<ToggleSwitchWidget, ColorRGBA> function) {
        this.internalField1216 = function;
        return this;
    }

    @Override
    public void drawSelf(UiRenderContext iII, float f) {
        float f2 = this.sig("on");
        float f3 = this.x();
        float f4 = this.y();
        float f5 = this.w();
        float f6 = this.h();
        float f7 = 1.5f;
        float f8 = f6 - f7 * 2.0f;
        ColorRGBA colorRGBA = this.internalField0571.apply(this).mix(this.internalField0570.apply(this), 1.0f - f2);
        ColorRGBA colorRGBA2 = this.internalField1216 == null ? ThemeColors.internalMethod01303(colorRGBA) : this.internalField1216.apply(this);
        iII.drawRoundedRect(f3, f4, f5, f6, CornerRadii.internalMethod03908(f6 / 2.0f - 0.5f), colorRGBA);
        iII.drawRoundedRect(f3 + f7 + (f5 - f8 - f7 * 2.0f) * f2, f4 + f7, f8, f8, CornerRadii.internalMethod03908(f8 / 2.0f - 0.5f), colorRGBA2);
    }
}

