package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.LegacyUiElement;

public class HudTooltip
extends LegacyUiElement {
    private final float internalField1049;
    private final SizedFont internalField0447;
    private String internalField0248 = "";
    private String internalField0247 = "";
    private final AnimatedValue internalField0808;
    private boolean internalField0277;

    public HudTooltip(SizedFont typedValue020, float f, long l, Easing typedValue214) {
        this.internalField0447 = typedValue020;
        this.internalField1049 = f;
        this.internalField0808 = new AnimatedValue(l, typedValue214);
    }

    @Override
    public void internalMethod05619(UiRenderContext iII) {
        this.internalField0808.internalMethod07059(1.0f);
        iII.drawText(this.internalField0447, this.internalField0248, this.internalField0205 - (this.internalField0277 ? this.internalField0447.internalMethod00965(this.internalField0248) / 2.0f : 0.0f), this.internalField0206 + this.internalField1049 * this.internalField0808.internalMethod02881(), ColorRGBA.WHITE.withAlpha(255.0f * (1.0f - this.internalField0808.internalMethod02881())));
        iII.drawText(this.internalField0447, this.internalField0247, this.internalField0205 - (this.internalField0277 ? this.internalField0447.internalMethod00965(this.internalField0247) / 2.0f : 0.0f), this.internalField0206 - this.internalField1049 + this.internalField1049 * this.internalField0808.internalMethod02881(), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0808.internalMethod02881()));
    }

    public HudTooltip internalMethod01888() {
        this.internalField0277 = true;
        return this;
    }

    public void internalMethod06105(String string) {
        if (this.internalField0247.equals(string)) {
            return;
        }
        this.internalField0248 = this.internalField0247;
        this.internalField0247 = string;
        this.internalField0808.internalMethod07060(0.0f);
    }

    @Generated
    public SizedFont internalMethod04347() {
        return this.internalField0447;
    }
}

