package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.function.Consumer;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.ui.RoundedTextFieldWidget;
import rockstar.client.internal.script.ItemSettingsPopup;
import rockstar.client.internal.ui.ItemSettingsElement;
import rockstar.client.internal.core.EditableNumericRow;
import rockstar.client.internal.core.NumericFieldHelper;

public class EnchantLevelRow
implements ItemSettingsElement,
EditableNumericRow {
    private final String internalField0248;
    private final RoundedTextFieldWidget internalField0762;
    private final int internalField0227;
    private final Consumer<Integer> internalField0922;
    private int internalField0228;

    public EnchantLevelRow(String string, int n, int n2, Consumer<Integer> consumer) {
        this.internalField0248 = string;
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField0922 = consumer;
        this.internalField0762 = new RoundedTextFieldWidget("");
        this.internalField0762.internalMethod03483(String.valueOf(n2));
    }

    @Override
    public float internalMethod05812() {
        return 20.0f;
    }

    @Override
    public void internalMethod05576(UiRenderContext iII, float f, float f2, float f3) {
        boolean bl = this.internalField0228 > 0;
        ColorRGBA colorRGBA = bl ? ThemeColors.internalMethod02531().withAlpha(50.0f) : ThemeColors.internalMethod08573();
        iII.drawRoundedRect(f, f2, f3, 17.0f, CornerRadii.internalMethod03908(3.0f), colorRGBA);
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), this.internalField0248, f + 6.0f, f2 + 6.0f, ThemeColors.internalMethod08459());
        if (bl) {
            float f4 = 18.0f;
            this.internalField0762.internalMethod07039(iII, f + f3 - f4 - 2.0f, f2 + 2.0f, f4, 14.0f);
        }
    }

    @Override
    public boolean internalMethod06574(ItemSettingsPopup typedValue097, double d, double d2, int n) {
        boolean bl;
        boolean bl2 = bl = this.internalField0228 > 0;
        if (bl && this.internalField0762.internalMethod05028(d, d2)) {
            this.internalField0762.internalMethod06527(true);
            return true;
        }
        if (bl) {
            this.internalField0228 = 0;
            this.internalField0762.internalMethod03483("0");
            this.internalField0922.accept(0);
        } else {
            this.internalField0228 = 1;
            this.internalField0762.internalMethod03483("1");
            this.internalField0922.accept(1);
        }
        return true;
    }

    public void internalMethod00939() {
        NumericFieldHelper.internalMethod01391(this.internalField0762, this.internalField0228, NumericFieldHelper.internalMethod05399(0, this.internalField0227), n -> {
            this.internalField0228 = n;
            this.internalField0922.accept(n);
        });
    }

    @Override
    public boolean internalMethod00940() {
        return this.internalField0762.internalMethod06300();
    }

    @Override
    public void internalMethod00942() {
        if (this.internalField0762.internalMethod06300()) {
            this.internalMethod00939();
            this.internalField0762.internalMethod06527(false);
        }
    }

    @Generated
    public String internalMethod04939() {
        return this.internalField0248;
    }

    @Generated
    public RoundedTextFieldWidget internalMethod01113() {
        return this.internalField0762;
    }

    @Generated
    public int internalMethod00938() {
        return this.internalField0227;
    }

    @Generated
    public Consumer<Integer> internalMethod00506() {
        return this.internalField0922;
    }

    @Generated
    public int internalMethod00941() {
        return this.internalField0228;
    }
}

