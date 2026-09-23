package rockstar.client.ui;






import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;

public class ButtonSettingComponent
extends SettingComponent<ButtonSetting> {
    public ButtonSettingComponent(ButtonSetting typedValue166, LegacyUiElement typedValue001) {
        super(typedValue166, typedValue001);
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
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        iII.drawRoundedRect(this.internalField0205 + 7.0f, this.internalField0206 + 4.0f, this.internalField1048 - 14.0f, this.internalField1047 - 7.0f, CornerRadii.internalMethod03908(6.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * (0.3f + 0.2f * this.internalField0808.internalMethod02881())));
    }

    @Override
    public void internalMethod08256(UiRenderContext iII) {
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        iII.drawCenteredText(typedValue020, LanguageManager.internalMethod07214(((ButtonSetting)this.internalField0644).getName()), this.internalField0205 + 8.0f + (this.internalField1048 - 14.0f) / 2.0f, this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), this.internalField1047) - 0.5f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881()) * RenderSystem.getShaderColor()[3]));
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        if (this.internalMethod04931(d, d2) && typedParameter1015 == MouseButton.internalField0102) {
            ((ButtonSetting)this.internalField0644).internalMethod03496().run();
        }
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 24.0f;
        return 24.0f;
    }
}
