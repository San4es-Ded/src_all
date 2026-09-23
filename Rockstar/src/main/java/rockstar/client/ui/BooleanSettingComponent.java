package rockstar.client.ui;







import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.script.AnimatedColor;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;

public class BooleanSettingComponent
extends SettingComponent<BooleanSetting> {
    private AnimatedValue internalField0809;
    private AnimatedValue internalField1321;
    private AnimatedColor internalField0814;

    public BooleanSettingComponent(BooleanSetting typedValue164, LegacyUiElement typedValue001) {
        super(typedValue164, typedValue001);
    }

    @Override
    public void internalMethod02325() {
        this.internalField0809 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
        this.internalField1321 = new AnimatedValue(300L, Easing.internalField0812);
        this.internalField0814 = new AnimatedColor(300L, new ColorRGBA(24.0f, 24.0f, 27.0f), Easing.internalField1626);
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
        this.internalField0809.internalMethod07059(((BooleanSetting)this.internalField0644).internalMethod04496() ? 1.0f : 0.75f);
        this.internalField1321.internalMethod07059(((BooleanSetting)this.internalField0644).internalMethod04496() ? 1.0f : 0.0f);
        this.internalField0814.internalMethod03893(((BooleanSetting)this.internalField0644).internalMethod04496() ? ThemeColors.internalMethod02531() : ThemeColors.internalMethod08573());
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        if (this.internalMethod03399(iII) && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        float f = 13.0f;
        float f2 = 8.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f3 = 10.0f;
        float f4 = typedValue020.internalMethod04890();
        float f5 = 19.0f;
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((BooleanSetting)this.internalField0644).getName()), this.internalField0205 + f3, this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), f5) - 0.5f, this.internalField1048 - f - 20.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField1321.internalMethod02881() + 0.25f * this.internalField0808.internalMethod02881())), 0.7f, 0.99f);
        ColorRGBA colorRGBA = this.internalField0814.internalMethod04159().withAlpha(!((BooleanSetting)this.internalField0644).internalMethod04496() ? 255.0f - 100.0f * InterfaceModule.internalMethod07584() : 255.0f);
        iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - f - 9.0f, this.internalField0206 + 5.0f, f, f2, CornerRadii.internalMethod03908(3.0f), colorRGBA);
        iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - f - 8.5f + 5.0f * this.internalField1321.internalMethod02881(), this.internalField0206 + 5.5f, 7.0f, 7.0f, CornerRadii.internalMethod03908(4.0f), ThemeColors.internalMethod01303(colorRGBA).withAlpha(this.internalField0809.internalMethod02881() * 255.0f));
    }

    @Override
    public void internalMethod08256(UiRenderContext iII) {
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        if (this.internalMethod04931(d, d2) && typedParameter1015 == MouseButton.internalField0102) {
            ((BooleanSetting)this.internalField0644).toggle();
        }
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 18.0f;
        return 18.0f;
    }
}
