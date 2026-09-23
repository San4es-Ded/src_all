package rockstar.client.ui;






import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.ColorSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.ColorPickerPanel;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;

public class ColorSettingComponent
extends SettingComponent<ColorSetting> {
    private ColorPickerPanel internalField0563;

    public ColorSettingComponent(ColorSetting typedValue167, LegacyUiElement typedValue001) {
        super(typedValue167, typedValue001);
    }

    @Override
    public void internalMethod02325() {
        this.internalField1048 = 13.0f;
        this.internalField1047 = 8.0f;
        super.internalMethod02325();
    }

    @Override
    public void internalMethod08744(UiRenderContext iII) {
        super.internalMethod08744(iII);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        if (this.internalMethod03399(iII) && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        float f = 13.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f2 = 10.0f;
        float f3 = 19.0f;
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((ColorSetting)this.internalField0644).getName()), this.internalField0205 + f2, this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), f3) - 0.5f, this.internalField1048 - f - 20.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.7f, 0.99f);
        iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - f2 - 9.0f, this.internalField0206 + 4.0f, 10.0f, 10.0f, CornerRadii.internalMethod03908(4.5f), ThemeColors.internalField1616);
        iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - f2 - 7.0f, this.internalField0206 + 6.0f, 6.0f, 6.0f, CornerRadii.internalMethod03908(4.5f), ((ColorSetting)this.internalField0644).internalMethod05620());
        if (this.internalField0563 != null && this.internalField0563.internalMethod00217()) {
            ((ColorSetting)this.internalField0644).internalMethod04886(this.internalField0563.internalMethod01145());
        } else if (this.internalField0563 != null && !this.internalField0563.internalMethod00217()) {
            this.internalField0563 = null;
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
            this.internalField0563 = new ColorPickerPanel((float)d, (float)d2, 6.0f, ((ColorSetting)this.internalField0644).internalMethod04496(), ((ColorSetting)this.internalField0644).internalMethod05620(), LanguageManager.internalMethod07214(((ColorSetting)this.internalField0644).getName()));
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 18.0f;
        return 18.0f;
    }
}
