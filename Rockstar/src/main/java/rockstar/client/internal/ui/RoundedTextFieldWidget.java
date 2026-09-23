package rockstar.client.internal.ui;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.ui.ThemeColors;

public class RoundedTextFieldWidget {
    private final CustomTextField internalField0936 = new CustomTextField(Fonts.internalField1154.internalMethod01432(7.0f));

    public RoundedTextFieldWidget(String string) {
        this.internalField0936.internalMethod09000(string);
    }

    public void internalMethod07039(UiRenderContext iII, float f, float f2, float f3, float f4) {
        this.internalField0936.internalMethod03623(f);
        this.internalField0936.internalMethod03701(f2);
        this.internalField0936.internalMethod08630(f3);
        this.internalField0936.internalMethod08642(f4);
        iII.drawRoundedRect(f, f2, f3, f4, CornerRadii.internalMethod03908(4.0f), this.internalMethod05028(iII.internalMethod05259(), iII.internalMethod05261()) ? ThemeColors.internalMethod08573().withAlpha(170.0f) : ThemeColors.internalMethod08573().withAlpha(150.0f));
        this.internalField0936.internalMethod03398(iII);
    }

    public boolean internalMethod05028(double d, double d2) {
        return this.internalField0936.internalMethod04931(d, d2);
    }

    public String internalMethod01396() {
        return this.internalField0936.internalMethod06202();
    }

    public void internalMethod03483(String string) {
        this.internalField0936.internalMethod00484(string);
    }

    public void internalMethod06527(boolean bl) {
        this.internalField0936.internalMethod07508(bl);
    }

    public boolean internalMethod06300() {
        return this.internalField0936.internalMethod00342();
    }

    @Generated
    public CustomTextField internalMethod01918() {
        return this.internalField0936;
    }
}

