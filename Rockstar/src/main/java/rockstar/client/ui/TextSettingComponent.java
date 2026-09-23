package rockstar.client.ui;







import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.TextSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;

public class TextSettingComponent
extends SettingComponent<TextSetting> {
    private CustomTextField internalField0936;
    private final AnimatedValue internalField0809 = new AnimatedValue(150L, Easing.internalField1626);

    public TextSettingComponent(TextSetting typedValue179, LegacyUiElement typedValue001) {
        super(typedValue179, typedValue001);
    }

    @Override
    public void internalMethod02325() {
        this.internalField1048 = 13.0f;
        this.internalField1047 = 8.0f;
        this.internalField0936 = new CustomTextField(Fonts.internalField1154.internalMethod01432(8.0f));
        this.internalField0936.internalMethod07506(((TextSetting)this.internalField0644).internalMethod00346());
        this.internalField0936.internalMethod07070(((TextSetting)this.internalField0644).internalMethod08926());
        this.internalField0936.internalMethod07563(((TextSetting)this.internalField0644).internalMethod04496());
        this.internalField0936.internalMethod09000(LanguageManager.internalMethod07214("type_text"));
        this.internalField0809.internalMethod07060(((TextSetting)this.internalField0644).isVisible() ? 1.0f : 0.0f);
        super.internalMethod02325();
    }

    @Override
    public void internalMethod08744(UiRenderContext iII) {
        this.internalField0809.internalMethod07062(((TextSetting)this.internalField0644).isVisible());
        super.internalMethod08744(iII);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        float f = this.internalField0205 + 8.0f;
        float f2 = this.internalField0206 + 15.0f;
        float f3 = this.internalField1048 - 16.0f;
        float f4 = this.internalField1047 - 20.0f;
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        if (this.internalMethod03399(iII) && ((TextSetting)this.internalField0644).isVisible() && this.internalMethod05803().internalMethod03399(iII)) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        float f5 = 13.0f;
        float f6 = 8.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f7 = 10.0f;
        float f8 = typedValue020.internalMethod04890();
        float f9 = 19.0f;
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((TextSetting)this.internalField0644).getName()), this.internalField0205 + f7, this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), f9) - 0.5f, f3 - f5 - 20.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.7f, 0.99f);
        iII.drawRoundedRect(f, f2, f3, f4, CornerRadii.internalMethod03908(4.0f), ThemeColors.internalMethod07738().withAlpha(76.5f));
        this.internalField0936.internalMethod05191(f, f2, f3, f4);
        this.internalField0936.internalMethod08627(this.internalField0809.internalMethod02881());
        this.internalField0936.internalMethod00143(ThemeColors.internalMethod08459());
        this.internalField0936.internalMethod03398(iII);
        ((TextSetting)this.internalField0644).internalMethod00011(this.internalField0936.internalMethod06202());
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod05727(int n, int n2, int n3) {
        this.internalField0936.internalMethod05727(n, n2, n3);
    }

    @Override
    public boolean internalMethod05413(char c, int n) {
        return this.internalField0936.internalMethod05413(c, n);
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0936.internalMethod01643(d, d2, typedParameter1015);
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0936.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 35.0f;
        return 35.0f;
    }
}

