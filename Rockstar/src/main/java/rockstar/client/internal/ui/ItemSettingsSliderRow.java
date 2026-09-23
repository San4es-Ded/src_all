package rockstar.client.internal.ui;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import java.util.function.Consumer;
import lombok.Generated;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.script.ItemSettingsPopup;
import rockstar.client.internal.ui.ItemSettingsElement;
import rockstar.client.ui.UiUtils;

public class ItemSettingsSliderRow
implements ItemSettingsElement {
    private final String internalField0248;
    private final int internalField0227;
    private final int internalField0228;
    private int internalField1053;
    private final Consumer<Integer> internalField0922;
    private boolean internalField0277;

    public ItemSettingsSliderRow(String string, int n, int n2, int n3, Consumer<Integer> consumer) {
        this.internalField0248 = string;
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField1053 = n3;
        this.internalField0922 = consumer;
    }

    @Override
    public float internalMethod05812() {
        return 20.0f;
    }

    @Override
    public void internalMethod05576(UiRenderContext iII, float f, float f2, float f3) {
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), this.internalField0248, f, f2 + 2.0f, ThemeColors.internalMethod08459());
        iII.drawRightText(Fonts.internalField1154.internalMethod01432(7.0f), String.valueOf(this.internalField1053), f + f3, f2 + 2.0f, ThemeColors.internalMethod08459().withAlpha(180.0f));
        float f4 = f2 + 14.0f;
        float f5 = (float)(this.internalField1053 - this.internalField0227) / (float)(this.internalField0228 - this.internalField0227);
        iII.drawRoundedRect(f, f4, f3, 2.0f, CornerRadii.internalMethod03908(0.25f), ThemeColors.internalMethod08573());
        iII.drawRoundedRect(f, f4, f3 * f5, 2.0f, CornerRadii.internalMethod03908(0.25f), ThemeColors.internalMethod02531());
        iII.drawRoundedRect(f + f3 * f5 - 3.0f, f4 - 2.0f, 6.0f, 6.0f, CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod02531());
        if (this.internalField0277) {
            float f6 = UiUtils.internalMethod04852(this.internalField0227, this.internalField0228, f, f3, iII.internalMethod05259());
            this.internalField1053 = Math.round(f6);
            this.internalField0922.accept(this.internalField1053);
        }
    }

    @Override
    public boolean internalMethod06574(ItemSettingsPopup typedValue097, double d, double d2, int n) {
        this.internalField0277 = true;
        return true;
    }

    public void internalMethod00813() {
        this.internalField0277 = false;
    }

    @Generated
    public boolean internalMethod00814() {
        return this.internalField0277;
    }
}

