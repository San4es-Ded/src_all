package rockstar.client.ui;







import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import net.minecraft.util.math.Vec2f;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.setting.Vector2Setting;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.SettingComponent;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.render.UiBatchRenderer;

public class Vector2SettingComponent
extends SettingComponent<Vector2Setting> {
    private final AnimatedValue internalField0809 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1321 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1322 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1323 = new AnimatedValue(500L, Easing.internalField1325);
    private boolean internalField0277;
    private boolean internalField0276;

    public Vector2SettingComponent(Vector2Setting typedValue160, LegacyUiElement typedValue001) {
        super(typedValue160, typedValue001);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 2.0f;
        float f3 = this.internalField1048 - 18.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f4 = 10.0f;
        float f5 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        float f6 = 3.0f;
        float f7 = f - 1.0f + f6;
        float f8 = f2 + 17.0f + f6;
        float f9 = f3 + 2.0f - f6 * 2.0f;
        float f10 = this.internalField1047 - 10.0f - 17.0f - f6 * 2.0f;
        iII.drawRoundedRect(f7 - f6, f8 - f6, f9 + f6 * 2.0f, f10 + f6 * 2.0f, CornerRadii.internalMethod03908(6.0f), ThemeColors.internalMethod07738().withAlpha(76.5f));
        iII.drawRoundedRect(f7 + this.internalField0809.internalMethod02881() * f9 - 3.0f, f8 + this.internalField1321.internalMethod02881() * f10 - 3.0f, 6.0f, 6.0f, CornerRadii.internalMethod03908(6.0f), ThemeColors.internalField1312.withAlpha(255.0f));
        iII.drawRoundedRect(f7 + this.internalField1322.internalMethod02881() * f9 - 3.0f, f8 + this.internalField1323.internalMethod02881() * f10 - 3.0f, 6.0f, 6.0f, CornerRadii.internalMethod03908(6.0f), ThemeColors.internalField1312.withAlpha(255.0f));
        UiBatchRenderer.internalMethod02576();
        Vec2f vec2f = new Vec2f(f7, f8 + f10);
        Vec2f vec2f2 = new Vec2f(f7 + this.internalField0809.internalMethod02881() * f9, f8 + this.internalField1321.internalMethod02881() * f10);
        Vec2f vec2f3 = new Vec2f(f7 + this.internalField1322.internalMethod02881() * f9, f8 + this.internalField1323.internalMethod02881() * f10);
        Vec2f vec2f4 = new Vec2f(f7 + f9, f8);
        iII.drawBezier(vec2f, vec2f2, vec2f3, vec2f4, ColorRGBA.WHITE, 50);
        iII.drawLine(vec2f, vec2f2, ThemeColors.internalField1312.mulAlpha(0.5f));
        iII.drawLine(vec2f4, vec2f3, ThemeColors.internalField1312.mulAlpha(0.5f));
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((Vector2Setting)this.internalField0644).getName()), this.internalField0205 + f4, f2 + 11.0f - typedValue020.internalMethod04890(), this.internalMethod05803().internalMethod08827() - f4 - 10.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.8f, 1.0f);
        if (this.internalMethod03399(iII) && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        if (this.internalField0277) {
            float f11 = UiUtils.internalMethod04852(0.0f, 1.0f, f7, f9, iII.internalMethod05259());
            float f12 = UiUtils.internalMethod01005(0.0f, 1.0f, f8, f10, iII.internalMethod05261());
            ((Vector2Setting)this.internalField0644).internalMethod07286(new Vec2f(f11, Math.clamp(f12, -0.5f, 1.5f)));
            CursorManager.internalMethod06882(CursorType.internalField1207);
        } else if (this.internalField0276) {
            float f13 = UiUtils.internalMethod04852(0.0f, 1.0f, f7, f9, iII.internalMethod05259());
            float f14 = UiUtils.internalMethod01005(0.0f, 1.0f, f8, f10, iII.internalMethod05261());
            ((Vector2Setting)this.internalField0644).internalMethod01741(new Vec2f(f13, Math.clamp(f14, -0.5f, 1.5f)));
            CursorManager.internalMethod06882(CursorType.internalField1207);
        }
        this.internalField0809.internalMethod07060(((Vector2Setting)this.internalField0644).internalMethod03179().x);
        this.internalField1321.internalMethod07060(((Vector2Setting)this.internalField0644).internalMethod03179().y);
        this.internalField1322.internalMethod07060(((Vector2Setting)this.internalField0644).internalMethod00322().x);
        this.internalField1323.internalMethod07060(((Vector2Setting)this.internalField0644).internalMethod00322().y);
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 2.0f;
        float f3 = this.internalField1048 - 18.0f;
        if (this.internalMethod04931(d, d2)) {
            float f4;
            float f5 = f - 1.0f;
            float f6 = f2 + 17.0f;
            float f7 = f3 + 2.0f;
            float f8 = this.internalField1047 - 10.0f - 17.0f;
            Vec2f vec2f = new Vec2f(UiUtils.internalMethod07541((float)d, f5, f5 + f7), UiUtils.internalMethod07541((float)d2, f6, f6 + f8));
            float f9 = this.internalMethod06516(((Vector2Setting)this.internalField0644).internalMethod03179(), vec2f);
            if (f9 < (f4 = this.internalMethod06516(((Vector2Setting)this.internalField0644).internalMethod00322(), vec2f))) {
                this.internalField0277 = true;
            } else {
                this.internalField0276 = true;
            }
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    public float internalMethod06516(Vec2f vec2f, Vec2f vec2f2) {
        float f = vec2f.x - vec2f2.x;
        float f2 = vec2f.y - vec2f2.y;
        return (float)Math.sqrt(f * f + f2 * f2);
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0277 = false;
        this.internalField0276 = false;
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = this.internalField1048 - 14.0f;
        return this.internalField1047;
    }
}
