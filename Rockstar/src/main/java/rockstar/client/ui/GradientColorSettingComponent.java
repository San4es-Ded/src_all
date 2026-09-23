package rockstar.client.ui;






import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.GradientColorSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.ColorPickerPanel;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;

public class GradientColorSettingComponent
extends SettingComponent<GradientColorSetting> {
    private ColorPickerPanel internalField0563;
    private int internalField1055 = -1;

    public GradientColorSettingComponent(GradientColorSetting typedValue168, LegacyUiElement typedValue001) {
        super(typedValue168, typedValue001);
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
        float f = 26.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f2 = 19.0f;
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((GradientColorSetting)this.internalField0644).getName()), this.internalField0205 + 10.0f, this.internalField0206 + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), f2) - 0.5f, this.internalField1048 - f - 20.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.7f, 0.99f);
        this.internalMethod06091(iII, this.internalMethod09286());
        this.internalMethod06091(iII, this.internalMethod09285());
        this.internalMethod07363(iII, this.internalMethod09286(), ((GradientColorSetting)this.internalField0644).internalMethod05319());
        this.internalMethod07363(iII, this.internalMethod09285(), ((GradientColorSetting)this.internalField0644).internalMethod01482());
        if (this.internalField0563 != null && this.internalField0563.internalMethod00217()) {
            if (this.internalField1055 == 0) {
                ((GradientColorSetting)this.internalField0644).internalMethod01227(this.internalField0563.internalMethod01145());
            } else if (this.internalField1055 == 1) {
                ((GradientColorSetting)this.internalField0644).internalMethod04884(this.internalField0563.internalMethod01145());
            }
        } else if (this.internalField0563 != null && !this.internalField0563.internalMethod00217()) {
            this.internalField0563 = null;
            this.internalField1055 = -1;
        }
    }

    private void internalMethod06091(UiRenderContext iII, float f) {
        iII.drawRoundedRect(f, this.internalField0206 + 4.0f, 10.0f, 10.0f, CornerRadii.internalMethod03908(4.5f), ThemeColors.internalField1616);
    }

    private void internalMethod07363(UiRenderContext iII, float f, ColorRGBA colorRGBA) {
        iII.drawRoundedRect(f + 2.0f, this.internalField0206 + 6.0f, 6.0f, 6.0f, CornerRadii.internalMethod03908(4.5f), colorRGBA);
    }

    private float internalMethod09285() {
        return this.internalField0205 + this.internalField1048 - 10.0f - 9.0f;
    }

    private float internalMethod09286() {
        return this.internalMethod09285() - 13.0f;
    }

    private boolean internalMethod02249(double d, double d2, float f) {
        return d >= (double)f && d <= (double)(f + 10.0f) && d2 >= (double)(this.internalField0206 + 4.0f) && d2 <= (double)(this.internalField0206 + 4.0f + 10.0f);
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        if (typedParameter1015 == MouseButton.internalField0102) {
            if (this.internalMethod02249(d, d2, this.internalMethod09286())) {
                this.internalMethod05836(d, d2, 0, ((GradientColorSetting)this.internalField0644).internalMethod05319());
            } else if (this.internalMethod02249(d, d2, this.internalMethod09285())) {
                this.internalMethod05836(d, d2, 1, ((GradientColorSetting)this.internalField0644).internalMethod01482());
            }
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    private void internalMethod05836(double d, double d2, int n, ColorRGBA colorRGBA) {
        this.internalField1055 = n;
        this.internalField0563 = new ColorPickerPanel((float)d, (float)d2, 6.0f, ((GradientColorSetting)this.internalField0644).internalMethod04496(), colorRGBA, LanguageManager.internalMethod07214(((GradientColorSetting)this.internalField0644).getName()));
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 18.0f;
        return 18.0f;
    }
}
