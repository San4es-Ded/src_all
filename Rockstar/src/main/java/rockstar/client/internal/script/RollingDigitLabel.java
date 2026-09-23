package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.LegacyUiElement;

public class RollingDigitLabel
extends LegacyUiElement {
    private boolean internalField0277;
    private ColorRGBA internalField0777 = ColorRGBA.WHITE;
    private final float internalField1049;
    private final SizedFont internalField0447;
    private final String[] internalField0359 = new String[]{"", ""};
    private final String[] internalField0358 = new String[]{"", ""};
    private final AnimatedValue[] internalField0556;
    private float internalField1046;

    public RollingDigitLabel(SizedFont typedValue020, float f, long l, Easing typedValue214) {
        this.internalField0447 = typedValue020;
        this.internalField1049 = f;
        this.internalField0556 = new AnimatedValue[2];
        for (int i = 0; i < this.internalField0556.length; ++i) {
            this.internalField0556[i] = new AnimatedValue(l, typedValue214);
        }
    }

    @Override
    public void internalMethod05619(UiRenderContext iII) {
        for (AnimatedValue typedValue210 : this.internalField0556) {
            typedValue210.internalMethod07059(1.0f);
        }
        iII.drawText(this.internalField0447, this.internalField0358[0], this.internalField0205, this.internalField0206 + this.internalField1049 * this.internalField0556[0].internalMethod02881(), this.internalField0777.withAlpha(this.internalField0777.getAlpha() * (1.0f - this.internalField0556[0].internalMethod02881())));
        iII.drawText(this.internalField0447, this.internalField0359[0], this.internalField0205, this.internalField0206 - this.internalField1049 + this.internalField1049 * this.internalField0556[0].internalMethod02881(), this.internalField0777.withAlpha(this.internalField0777.getAlpha() * this.internalField0556[0].internalMethod02881()));
        iII.drawText(this.internalField0447, this.internalField0358[1], this.internalField0205 + this.internalField1046, this.internalField0206 + this.internalField1049 * this.internalField0556[1].internalMethod02881(), this.internalField0777.withAlpha(this.internalField0777.getAlpha() * (1.0f - this.internalField0556[1].internalMethod02881())));
        iII.drawText(this.internalField0447, this.internalField0359[1], this.internalField0205 + this.internalField0447.internalMethod00965(this.internalField0359[0]), this.internalField0206 - this.internalField1049 + this.internalField1049 * this.internalField0556[1].internalMethod02881(), this.internalField0777.withAlpha(this.internalField0777.getAlpha() * this.internalField0556[1].internalMethod02881()));
    }

    @Override
    public float internalMethod08827() {
        return this.internalField0447.internalMethod00965(this.internalField0359[0] + this.internalField0359[1]);
    }

    public void internalMethod05850(int n) {
        String string = String.valueOf(n / 10);
        String string2 = String.valueOf(n % 10);
        if (!string2.equals(this.internalField0359[1])) {
            this.internalField1046 = this.internalField0447.internalMethod00965(this.internalField0359[0]);
            this.internalField0358[1] = this.internalField0359[1];
            this.internalField0359[1] = string2;
            this.internalField0556[1].internalMethod07060(0.0f);
        }
        if (!string.equals(this.internalField0359[0])) {
            this.internalField0358[0] = this.internalField0359[0];
            this.internalField0359[0] = this.internalField0277 ? string : (string.equals("0") ? "" : string);
            this.internalField0556[0].internalMethod07060(0.0f);
        }
    }

    public void internalMethod04016(boolean bl, ColorRGBA colorRGBA) {
        this.internalField0277 = bl;
        this.internalField0777 = colorRGBA;
    }
}

