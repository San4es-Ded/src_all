package rockstar.client.ui;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.script.AnimatedColor;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.TextUtils;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.render.ScissorStack;

public class KeybindSettingComponent
extends SettingComponent<KeybindSetting> {
    private final AnimatedColor internalField0814 = new AnimatedColor(300L, new ColorRGBA(24.0f, 24.0f, 27.0f), Easing.internalField1626);
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, Easing.internalField1626);
    private AnimatedValue internalField1321 = new AnimatedValue(300L, 1.0f, Easing.internalField1626);
    private int internalField0227;
    private boolean internalField0277;

    public boolean internalMethod05402() {
        return this.internalField0277;
    }

    public KeybindSettingComponent(KeybindSetting typedValue161, LegacyUiElement typedValue001) {
        super(typedValue161, typedValue001);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        SizedFont typedValue021 = Fonts.internalField1154.internalMethod01432(7.0f);
        float f = 10.0f;
        float f2 = 19.0f;
        this.internalField0814.internalMethod03893(this.internalField0277 ? ThemeColors.internalMethod02531() : ThemeColors.internalMethod08459());
        this.internalField1321.internalMethod07061(500L);
        this.internalField1321.internalMethod07059(1.0f);
        String string = TextUtils.internalMethod04982(((KeybindSetting)this.internalField0644).internalMethod07477());
        String string2 = TextUtils.internalMethod04982(this.internalField0227);
        float f3 = typedValue021.internalMethod00965(string) + 7.0f;
        this.internalField0809.internalMethod07059(f3);
        iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - 9.0f - this.internalField0809.internalMethod02881(), this.internalField0206 + 4.0f, this.internalField0809.internalMethod02881(), 11.0f, CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod08573());
        ScissorStack.internalMethod06303(iII.getMatrices(), this.internalField0205 + this.internalField1048 - 9.0f - this.internalField0809.internalMethod02881(), this.internalField0206 + 4.0f, this.internalField0809.internalMethod02881(), 11.0f);
        iII.drawText(typedValue021, string2, this.internalField0205 + this.internalField1048 - 9.0f - this.internalField0809.internalMethod02881() + 4.0f + 4.0f * this.internalField1321.internalMethod02881(), this.internalField0206 + 7.0f, this.internalField0814.internalMethod04159().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881()) * (1.0f - this.internalField1321.internalMethod02881())));
        iII.drawText(typedValue021, string, this.internalField0205 + this.internalField1048 - 9.0f - this.internalField0809.internalMethod02881() + 4.0f - 4.0f + 4.0f * this.internalField1321.internalMethod02881(), this.internalField0206 + 7.0f, this.internalField0814.internalMethod04159().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881()) * this.internalField1321.internalMethod02881()));
        ScissorStack.internalMethod07643();
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((KeybindSetting)this.internalField0644).getName()), this.internalField0205 + f, this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), f2), this.internalField1048 - this.internalField0809.internalMethod02881() - 20.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.7f, 0.99f);
        if (this.internalMethod03399(iII) && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        if (this.internalMethod04931(d, d2) && typedParameter1015 == MouseButton.internalField0102) {
            boolean bl = this.internalField0277 = !this.internalField0277;
        }
        if (this.internalField0277 && typedParameter1015 != MouseButton.internalField0102) {
            int n = typedParameter1015.internalMethod02957();
            ((KeybindSetting)this.internalField0644).internalMethod02164(n);
            this.internalField0277 = false;
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod05727(int n, int n2, int n3) {
        if (this.internalField0277) {
            this.internalField0227 = ((KeybindSetting)this.internalField0644).internalMethod07477();
            if (n == 256 || n == 261) {
                ((KeybindSetting)this.internalField0644).internalMethod02164(-1);
            } else {
                ((KeybindSetting)this.internalField0644).internalMethod02164(n);
            }
            this.internalField1321 = new AnimatedValue(500L, 0.0f, Easing.internalField1626);
            this.internalField0277 = false;
            return;
        }
        super.internalMethod05727(n, n2, n3);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 19.0f;
        return 19.0f;
    }

    @Generated
    public void internalMethod00594(boolean bl) {
        this.internalField0277 = bl;
    }
}
