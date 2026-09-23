package rockstar.client.ui;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.RangeSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.TextUtils;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;

public class RangeSettingComponent
extends SettingComponent<RangeSetting> {
    private final AnimatedValue internalField0809 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1321 = new AnimatedValue(500L, Easing.internalField1325);
    private boolean internalField0277;
    private boolean internalField0276;
    private int internalField0227;

    public RangeSettingComponent(RangeSetting typedValue172, LegacyUiElement typedValue001) {
        super(typedValue172, typedValue001);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        float f;
        float f2 = this.internalField0205 + 9.0f;
        float f3 = this.internalField0206 + 2.0f;
        float f4 = this.internalField1048 - 18.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f5 = 10.0f;
        float f6 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        float f7 = ((RangeSetting)this.internalField0644).internalMethod06919();
        if (f7 >= (f = ((RangeSetting)this.internalField0644).internalMethod07967())) {
            f7 = ((RangeSetting)this.internalField0644).internalMethod07967();
            f = ((RangeSetting)this.internalField0644).internalMethod06919();
        }
        this.internalField0809.internalMethod07059(f7);
        this.internalField1321.internalMethod07059(f);
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        iII.drawRoundedRect(f2, f3 + this.internalField1047 - 12.0f, f4, 2.0f, CornerRadii.internalMethod03908(0.25f), ThemeColors.internalMethod08573().withAlpha(178.5f));
        iII.drawRoundedRect(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()), f3 + this.internalField1047 - 12.0f, f4 * UiUtils.internalMethod07541(this.internalField1321.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) - f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()), 2.0f, CornerRadii.internalMethod03908(0.25f), ThemeColors.internalMethod02531());
        iII.drawShadow(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) - 3.0f, f3 + this.internalField1047 - 14.0f, 6.0f, 6.0f, 10.0f, CornerRadii.internalMethod03908(3.0f), ColorRGBA.BLACK.withAlpha(63.75f));
        iII.drawRoundedRect(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) - 3.0f, f3 + this.internalField1047 - 14.0f, 6.0f, 6.0f, CornerRadii.internalMethod03908(3.0f), ColorRGBA.WHITE);
        iII.drawShadow(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) + f4 * UiUtils.internalMethod07541(this.internalField1321.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) - f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) - 3.0f, f3 + this.internalField1047 - 14.0f, 6.0f, 6.0f, 10.0f, CornerRadii.internalMethod03908(3.0f), ColorRGBA.BLACK.withAlpha(63.75f));
        iII.drawRoundedRect(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) + f4 * UiUtils.internalMethod07541(this.internalField1321.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) - f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()) - 3.0f, f3 + this.internalField1047 - 14.0f, 6.0f, 6.0f, CornerRadii.internalMethod03908(3.0f), ColorRGBA.WHITE);
        String string = LanguageManager.internalMethod00160("ui.range_format", TextUtils.internalMethod07254(this.internalField0809.internalMethod02881()), TextUtils.internalMethod07254(this.internalField1321.internalMethod02881()));
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((RangeSetting)this.internalField0644).getName()), this.internalField0205 + f5, f3 + 11.0f - typedValue020.internalMethod04890(), this.internalMethod05803().internalMethod08827() - f5 - Fonts.internalField1154.internalMethod01432(7.0f).internalMethod00965(string) - 10.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.8f, 1.0f);
        if (this.internalMethod03399(iII) && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        if ((this.internalField0277 || this.internalField0276) && !UiUtils.internalMethod07370(this.internalField0227)) {
            this.internalField0277 = false;
            this.internalField0276 = false;
        }
        if (this.internalField0277) {
            float f8 = UiUtils.internalMethod04852(((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979(), f2, f4, iII.internalMethod05259());
            ((RangeSetting)this.internalField0644).internalMethod01445(f8);
            CursorManager.internalMethod06882(CursorType.internalField1208);
        } else if (this.internalField0276) {
            float f9 = UiUtils.internalMethod04852(((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979(), f2, f4, iII.internalMethod05259());
            ((RangeSetting)this.internalField0644).internalMethod01501(f9);
            CursorManager.internalMethod06882(CursorType.internalField1208);
        }
    }

    @Override
    public void internalMethod08256(UiRenderContext iII) {
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 2.0f;
        float f3 = this.internalField1048 - 18.0f;
        float f4 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        String string = LanguageManager.internalMethod00160("ui.range_format", TextUtils.internalMethod07254(this.internalField0809.internalMethod02881()), TextUtils.internalMethod07254(this.internalField1321.internalMethod02881()));
        iII.drawRightText(Fonts.internalField1154.internalMethod01432(7.0f), string, f + f3, f2 + 11.0f - f4, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881()) * RenderSystem.getShaderColor()[3]));
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField1048 - 18.0f;
        if (this.internalMethod04931(d, d2)) {
            float f3;
            float f4 = (float)Math.abs(d - (double)(f + f2 * UiUtils.internalMethod07541(((RangeSetting)this.internalField0644).internalMethod06919(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979())));
            if (f4 < (f3 = (float)Math.abs(d - (double)(f + f2 * UiUtils.internalMethod07541(((RangeSetting)this.internalField0644).internalMethod07967(), ((RangeSetting)this.internalField0644).internalMethod07968(), ((RangeSetting)this.internalField0644).internalMethod07979()))))) {
                this.internalField0277 = true;
            } else {
                this.internalField0276 = true;
            }
            this.internalField0227 = typedParameter1015.internalMethod02957();
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0277 = false;
        this.internalField0276 = false;
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 29.0f;
        return 29.0f;
    }
}

