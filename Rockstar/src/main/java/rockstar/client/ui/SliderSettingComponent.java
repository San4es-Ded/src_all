package rockstar.client.ui;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.util.math.Vector2f;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.SliderSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
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
import rockstar.client.render.RenderPipeline;
import rockstar.client.util.Stopwatch;

public class SliderSettingComponent
extends SettingComponent<SliderSetting> {
    private final AnimatedValue internalField0809 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1321 = new AnimatedValue(500L, Easing.internalField1626);
    private final Stopwatch internalField0519 = new Stopwatch();
    private boolean internalField0277;
    private int internalField0227;
    private boolean internalField0276;
    private CustomTextField internalField0936;
    private static SliderSettingComponent internalField0806;
    private static SliderSettingComponent internalField0807;

    public SliderSettingComponent(SliderSetting typedValue174, LegacyUiElement typedValue001) {
        super(typedValue174, typedValue001);
    }

    @Override
    public void internalMethod02325() {
        this.internalField0936 = new CustomTextField(Fonts.internalField1154.internalMethod01432(7.0f));
        this.internalField0936.internalMethod07563(true);
        this.internalMethod08135();
        this.internalField0809.internalMethod07060(((SliderSetting)this.internalField0644).internalMethod08576());
        super.internalMethod02325();
    }

    @Override
    public void internalMethod08744(UiRenderContext iII) {
        if (this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261())) {
            internalField0807 = this;
        }
        if (this.internalField0276 && this.internalField0936 != null) {
            this.internalField0936.internalMethod08744(iII);
            if (!this.internalField0936.internalMethod00342()) {
                this.internalMethod08122();
            }
        } else {
            this.internalMethod09322();
        }
        super.internalMethod08744(iII);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        float f;
        float f2 = this.internalField0205 + 9.0f;
        float f3 = this.internalField0206 + 2.0f;
        if (internalField0807 == this && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            internalField0807 = null;
        }
        float f4 = this.internalField1048 - 18.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f5 = 10.0f;
        float f6 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        this.internalField0809.internalMethod07059(((SliderSetting)this.internalField0644).internalMethod08576());
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        iII.drawRoundedRect(f2, f3 + this.internalField1047 - 12.0f, f4, 2.0f, CornerRadii.internalMethod03908(0.25f), ThemeColors.internalMethod08573().withAlpha((255.0f - 100.0f * InterfaceModule.internalMethod07584()) * 0.7f));
        iII.drawRoundedRect(f2, f3 + this.internalField1047 - 12.0f, f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291()), 2.0f, CornerRadii.internalMethod03908(0.25f), ThemeColors.internalMethod02531());
        if (this.internalField0519.internalMethod02365(50L)) {
            RenderPipeline.internalMethod09050();
            this.internalField0519.internalMethod00701();
        }
        if (InterfaceModule.internalMethod09719()) {
            iII.drawShadow(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291()) - 4.5f - 3.0f * this.internalField1321.internalMethod02881(), f3 + this.internalField1047 - 11.0f - 3.0f - 2.0f * this.internalField1321.internalMethod02881(), 9.0f + 6.0f * this.internalField1321.internalMethod02881(), 6.0f + 4.0f * this.internalField1321.internalMethod02881(), 10.0f, CornerRadii.internalMethod03908(3.0f + this.internalField1321.internalMethod02881() * 2.0f), ColorRGBA.BLACK.withAlpha(255.0f * (0.25f + 0.2f * this.internalField1321.internalMethod02881()) * InterfaceModule.internalMethod07584()));
            iII.drawSquircle(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291()) - 4.5f - 3.0f * this.internalField1321.internalMethod02881(), f3 + this.internalField1047 - 11.0f - 3.0f - 2.0f * this.internalField1321.internalMethod02881(), 9.0f + 6.0f * this.internalField1321.internalMethod02881(), 6.0f + 4.0f * this.internalField1321.internalMethod02881(), 7.0f, CornerRadii.internalMethod03908(3.0f + this.internalField1321.internalMethod02881()), ColorRGBA.WHITE.withAlpha(255.0f * (1.0f - this.internalField1321.internalMethod02881()) * InterfaceModule.internalMethod07584()));
            iII.drawLiquidGlass(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291()) - 4.5f - 3.0f * this.internalField1321.internalMethod02881(), f3 + this.internalField1047 - 11.0f - 3.0f - 2.0f * this.internalField1321.internalMethod02881(), 9.0f + 6.0f * this.internalField1321.internalMethod02881(), 6.0f + 4.0f * this.internalField1321.internalMethod02881(), 7.0f, CornerRadii.internalMethod03908(3.0f + this.internalField1321.internalMethod02881()), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField1321.internalMethod02881() * InterfaceModule.internalMethod07584()), false);
        }
        if (InterfaceModule.internalMethod09917()) {
            iII.drawShadow(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291()) - 3.0f, f3 + this.internalField1047 - 14.0f + this.internalField1321.internalMethod02881(), 6.0f, 6.0f - this.internalField1321.internalMethod02881() * 2.0f, 10.0f, CornerRadii.internalMethod03908(3.0f - this.internalField1321.internalMethod02881() * 2.0f), ColorRGBA.BLACK.withAlpha(63.75f * InterfaceModule.internalMethod07585()));
            iII.drawRoundedRect(f2 + f4 * UiUtils.internalMethod07541(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291()) - 3.0f, f3 + this.internalField1047 - 14.0f + this.internalField1321.internalMethod02881(), 6.0f, 6.0f - this.internalField1321.internalMethod02881() * 2.0f, CornerRadii.internalMethod03908(3.0f - this.internalField1321.internalMethod02881() * 2.0f), ColorRGBA.WHITE.withAlpha(255.0f * InterfaceModule.internalMethod07585()));
        }
        String string = TextUtils.internalMethod00670(Math.clamp(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291())) + ((SliderSetting)this.internalField0644).internalMethod08885();
        float f7 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod00965(string);
        float f8 = f2 + f4 - f7;
        float f9 = f3 + 11.0f - f6;
        float f10 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((SliderSetting)this.internalField0644).getName()), this.internalField0205 + f5, f3 + 11.0f - typedValue020.internalMethod04890(), this.internalMethod05803().internalMethod08827() - f5 - Fonts.internalField1154.internalMethod01432(7.0f).internalMethod00965(string) - 10.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.8f, 1.0f);
        if (this.internalField0276 && this.internalField0936 != null) {
            f = f9 - 1.0f;
            float f11 = f7 + 5.0f;
            float f12 = f10 + 2.0f;
            this.internalField0936.internalMethod05191(f8, f, f11, f12);
            this.internalField0936.internalMethod08627(1.0f);
            this.internalField0936.internalMethod00143(ThemeColors.internalMethod08459());
            this.internalField0936.internalMethod03398(iII);
        }
        if (this.internalMethod03399(iII) && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        if (this.internalField0277 && !UiUtils.internalMethod07370(this.internalField0227)) {
            this.internalField0277 = false;
        }
        this.internalField1321.internalMethod07061(200L);
        this.internalField1321.internalMethod07059(this.internalField0277 ? 1.0f : 0.0f);
        if (this.internalField0277) {
            f = UiUtils.internalMethod04852(((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291(), f2, f4, iII.internalMethod05259());
            ((SliderSetting)this.internalField0644).internalMethod04736(f);
            CursorManager.internalMethod06882(CursorType.internalField1208);
            internalField0806 = this;
        }
    }

    @Override
    public void internalMethod08256(UiRenderContext iII) {
        if (this.internalField0276 && this.internalField0936 != null) {
            return;
        }
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 2.0f;
        float f3 = this.internalField1048 - 18.0f;
        float f4 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        String string = TextUtils.internalMethod00670(Math.clamp(this.internalField0809.internalMethod02881(), ((SliderSetting)this.internalField0644).internalMethod05288(), ((SliderSetting)this.internalField0644).internalMethod05291())) + ((SliderSetting)this.internalField0644).internalMethod08885();
        iII.drawRightText(Fonts.internalField1154.internalMethod01432(7.0f), string, f + f3, f2 + 11.0f - f4, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881()) * RenderSystem.getShaderColor()[3]));
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        boolean bl = this.internalField0276;
        if (this.internalMethod04931(d, d2)) {
            String string = TextUtils.internalMethod00670(((SliderSetting)this.internalField0644).internalMethod08576()) + ((SliderSetting)this.internalField0644).internalMethod08885();
            float f = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod00965(string);
            float f2 = this.internalField0205 + 9.0f + (this.internalField1048 - 18.0f) - f;
            float f3 = this.internalField0206 + 2.0f + 11.0f - Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
            float f4 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
            if (d >= (double)f2 && d <= (double)(f2 + f) && d2 >= (double)f3 && d2 <= (double)(f3 + f4)) {
                this.internalMethod08124();
            } else {
                if (this.internalField0276) {
                    this.internalMethod08122();
                }
                this.internalField0277 = true;
                this.internalField0227 = typedParameter1015.internalMethod02957();
            }
        }
        if (this.internalField0276 && this.internalField0936 != null && bl) {
            this.internalField0936.internalMethod01643(d, d2, typedParameter1015);
            if (typedParameter1015 == MouseButton.internalField0102 && !this.internalField0936.internalMethod04931(d, d2) && !this.internalMethod04931(d, d2)) {
                this.internalMethod08122();
            }
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0277 = false;
        if (this.internalField0276 && this.internalField0936 != null) {
            this.internalField0936.internalMethod02863(d, d2, typedParameter1015);
        }
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod05727(int n, int n2, int n3) {
        if (this.internalField0276 && this.internalField0936 != null) {
            this.internalField0936.internalMethod05727(n, n2, n3);
            if (n == 257 || n == 335) {
                this.internalMethod08122();
            } else if (n == 256) {
                this.internalMethod08133();
            }
        } else if (n == 262 || n == 263) {
            Vector2f vector2f = UiUtils.internalMethod03634();
      if (internalField0807 == this && this.internalMethod04933(vector2f.x(), vector2f.y())) {
                ((SliderSetting)this.internalMethod05697()).internalMethod04736(((SliderSetting)this.internalMethod05697()).internalMethod08576() + ((SliderSetting)this.internalMethod05697()).internalMethod08575() * 0.7f * (float)(n == 262 ? 1 : -1));
            } else if (internalField0807 == null && internalField0806 == this) {
                ((SliderSetting)internalField0806.internalMethod05697()).internalMethod04736(((SliderSetting)internalField0806.internalMethod05697()).internalMethod08576() + ((SliderSetting)internalField0806.internalMethod05697()).internalMethod08575() * 0.7f * (float)(n == 262 ? 1 : -1));
            }
        }
    }

    @Override
    public boolean internalMethod05413(char c, int n) {
        if (this.internalField0276 && this.internalField0936 != null) {
            return this.internalField0936.internalMethod05413(c, n);
        }
        return false;
    }

    @Override
    public void internalMethod02890(double d, double d2, double d3, double d4) {
        if (this.internalField0276 || d4 == 0.0) {
            return;
        }
        if (this.internalMethod04931(d, d2)) {
            // empty if block
        }
    }

    private void internalMethod08122() {
        if (this.internalField0936 != null) {
            try {
                String string = this.internalField0936.internalMethod06202().replace(',', '.');
                if (!(string.isEmpty() || string.equals("-") || string.equals("."))) {
                    float f = Float.parseFloat(string);
                    f = Math.max(((SliderSetting)this.internalField0644).internalMethod05288(), Math.min(((SliderSetting)this.internalField0644).internalMethod05291(), f));
                    ((SliderSetting)this.internalField0644).internalMethod04736(f);
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            this.internalField0276 = false;
            this.internalField0936.internalMethod07508(false);
            this.internalMethod08135();
        }
    }

    private void internalMethod08124() {
        this.internalField0276 = true;
        if (this.internalField0936 != null) {
            String string = TextUtils.internalMethod00670(((SliderSetting)this.internalField0644).internalMethod08576());
            this.internalField0936.internalMethod00484(string);
            this.internalField0936.internalMethod09000(string);
            this.internalField0936.internalMethod07508(true);
            this.internalField0936.internalMethod00344();
        }
    }

    private void internalMethod08133() {
        this.internalField0276 = false;
        if (this.internalField0936 != null) {
            this.internalField0936.internalMethod07508(false);
            this.internalMethod08135();
        }
    }

    private void internalMethod08135() {
        if (this.internalField0936 == null) {
            return;
        }
        String string = TextUtils.internalMethod00670(((SliderSetting)this.internalField0644).internalMethod08576());
        this.internalField0936.internalMethod09000(string);
        if (!this.internalField0276) {
            this.internalField0936.internalMethod00484(string);
        }
    }

    private void internalMethod09322() {
        if (this.internalField0936 != null && !this.internalField0276) {
            this.internalField0936.internalMethod09000(TextUtils.internalMethod00670(((SliderSetting)this.internalField0644).internalMethod08576()));
        }
    }

    public static void internalMethod04360() {
        if (internalField0806 != null) {
            SliderSettingComponent.internalField0806.internalField0277 = false;
            internalField0806 = null;
        }
        internalField0807 = null;
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 29.0f;
        return 29.0f;
    }
}
