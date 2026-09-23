package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.Rect;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.CornerRadii;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;

public class MenuTile
extends Rect {
    private final String internalField0248;
    private final float internalField0205;
    private final Runnable internalField0659;
    private final ColorRGBA internalField0777 = new ColorRGBA(58.0f, 58.0f, 58.0f);
    private final AnimatedValue internalField0808 = new AnimatedValue(400L, 0.0f, Easing.internalField0812);
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);

    public void internalMethod05203(UiRenderContext iII, float f) {
        boolean bl;
        boolean bl2 = bl = this.hovered(iII.internalMethod05259(), iII.internalMethod05261()) && this.internalField0808.internalMethod02881() == 1.0f;
        if (bl && f > 0.5f) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        this.internalField0809.internalMethod07062(bl);
        if (InterfaceModule.internalMethod09717()) {
            iII.drawLiquidGlass(this.x - 1.0f, this.y - 1.0f, this.width + 2.0f, this.height + 2.0f, 7.0f, 0.08f, CornerRadii.internalMethod03908(Math.min(this.width, this.height) / 2.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0808.internalMethod02881() * f));
            iII.drawRoundedRect(this.x, this.y, this.width, this.height, CornerRadii.internalMethod03908(Math.min(this.width, this.height) / 2.0f), this.internalField0777.withAlpha(255.0f * (0.15f * this.internalField0808.internalMethod02881() + 0.15f * this.internalField0809.internalMethod02881()) * f));
        } else if (InterfaceModule.internalMethod09917()) {
            iII.drawRoundedRect(this.x, this.y, this.width, this.height, CornerRadii.internalMethod03908(Math.min(this.width, this.height) / 2.0f), this.internalField0777.withAlpha(255.0f * (0.33f * this.internalField0808.internalMethod02881() + 0.2f * this.internalField0809.internalMethod02881()) * f));
        }
        iII.drawTexture(RockstarClient.id(this.internalField0248), this.x + (this.width - this.internalField0205) / 2.0f, this.y + (this.height - this.internalField0205) / 2.0f, this.internalField0205, this.internalField0205, ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0808.internalMethod02881() * f));
    }

    public void internalMethod07255(double d, double d2, int n) {
        if (this.hovered(d, d2) && n == 0 && this.internalField0808.internalMethod02881() == 1.0f) {
            this.internalField0659.run();
        }
    }

    @Generated
    public MenuTile(String string, float f, Runnable runnable) {
        this.internalField0248 = string;
        this.internalField0205 = f;
        this.internalField0659 = runnable;
    }

    @Generated
    public AnimatedValue internalMethod03820() {
        return this.internalField0808;
    }
}
