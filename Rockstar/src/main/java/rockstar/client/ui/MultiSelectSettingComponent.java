package rockstar.client.ui;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.render.VertexFormats;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.RollingDigitLabel;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.ui.SettingComponent;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.render.RenderPipeline;
import rockstar.client.render.ScissorStack;
import rockstar.client.internal.render.UiVertexBatch;
import rockstar.client.internal.render.TexturedQuadBatch;
import rockstar.client.internal.core.SpriteAnimationPlayer;
import rockstar.client.util.Stopwatch;

public class MultiSelectSettingComponent
extends SettingComponent<MultiSelectSetting> {
    private RollingDigitLabel internalField0564;
    private MultiSelectSetting.InternalType0091 internalField0245;
    private final Map<MultiSelectSetting.InternalType0091, InternalType0504> internalField0543 = new HashMap<MultiSelectSetting.InternalType0091, InternalType0504>();
    private final Stopwatch internalField0519 = new Stopwatch();
    private boolean internalField0277;

    public MultiSelectSettingComponent(MultiSelectSetting typedValue173, LegacyUiElement typedValue001) {
        super(typedValue173, typedValue001);
        ArrayList arrayList = new ArrayList();
        typedValue173.internalMethod01792().forEach(nestedValue2013 -> {
            if (nestedValue2013.isSelected()) {
                arrayList.add(nestedValue2013);
            }
        });
        typedValue173.internalMethod07492().clear();
        typedValue173.internalMethod07492().addAll(arrayList);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        if (!this.internalField0277) {
            for (MultiSelectSetting.InternalType0091 nestedValue2014 : ((MultiSelectSetting)this.internalField0644).internalMethod01792()) {
                nestedValue2014.setEnableAnimation(new SpriteAnimationPlayer(RockstarClient.id("animations/check_enable.zip")));
                nestedValue2014.setDisableAnimation(new SpriteAnimationPlayer(RockstarClient.id("animations/check_disable.zip")));
                nestedValue2014.setLastState(nestedValue2014.isSelected());
                nestedValue2014.setCurrentAnimation(nestedValue2014.isLastState() ? nestedValue2014.getEnableAnimation() : nestedValue2014.getDisableAnimation());
                if (nestedValue2014.isLastState()) {
                    nestedValue2014.getEnableAnimation().internalMethod03142();
                    continue;
                }
                nestedValue2014.getDisableAnimation().internalMethod07104(0);
                nestedValue2014.getDisableAnimation().internalMethod09078();
            }
            this.internalField0277 = true;
        }
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 1.0f;
        float f3 = this.internalField1048 - 18.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f4 = 10.0f;
        float f5 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        float f6 = 19.0f;
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        long l = ((MultiSelectSetting)this.internalField0644).internalMethod01792().stream().filter(nestedValue2013 -> !nestedValue2013.isHidden()).count();
        int n = Math.toIntExact(((MultiSelectSetting)this.internalField0644).internalMethod07492().stream().filter(nestedValue2013 -> !nestedValue2013.isHidden()).count());
        String string = String.format(" %s", LanguageManager.internalMethod07214("setting_of") + " " + l);
        if (this.internalField0564 == null) {
            this.internalField0564 = new RollingDigitLabel(Fonts.internalField0449.internalMethod01432(7.0f), 5.0f, 500L, Easing.internalField0812);
        }
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((MultiSelectSetting)this.internalField0644).getName()), this.internalField0205 + f4, f2 - 1.0f + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), f6), this.internalMethod05803().internalMethod08827() - f4 - Fonts.internalField1154.internalMethod01432(7.0f).internalMethod00965(string) - this.internalField0564.internalMethod08827() - 10.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.8f, 1.0f);
        this.internalField0564.internalMethod04016(false, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())));
        this.internalField0564.internalMethod05850(n);
        this.internalField0564.internalMethod04932(f + f3 - Fonts.internalField1154.internalMethod01432(7.0f).internalMethod00965(string) - this.internalField0564.internalMethod08827(), f2 - 1.0f + UiUtils.internalMethod07116(f5, f6));
        this.internalField0564.internalMethod03398(iII);
        iII.drawRoundedRect(f - 1.0f, f2 + 17.0f, f3 + 2.0f, 8.0f + this.internalMethod09692(), CornerRadii.internalMethod03908(6.0f), ThemeColors.internalMethod07738().withAlpha(76.5f));
        float f7 = 0.0f;
        for (MultiSelectSetting.InternalType0091 object : ((MultiSelectSetting)this.internalField0644).internalMethod01792()) {
            if (object.isHidden()) continue;
            boolean nestedValue2015 = object.isSelected();
            if (nestedValue2015 != object.isLastState()) {
                if (nestedValue2015) {
                    object.setCurrentAnimation(object.getEnableAnimation());
                } else {
                    object.setCurrentAnimation(object.getDisableAnimation());
                }
                object.getCurrentAnimation().internalMethod03142();
                object.setLastState(nestedValue2015);
            }
            object.getCurrentAnimation().internalMethod03146();
            float f8 = this.internalField0245 == object ? Math.clamp((float)(iII.internalMethod05261() - 2), f2 + 18.0f, f2 + 20.0f + this.internalMethod09692()) : f2 + 24.0f + f7;
            boolean bl = UiUtils.internalMethod05786(f - 1.0f, f8 - 4.0f, f3 + 2.0f, 12.0, iII.internalMethod05259(), iII.internalMethod05261());
            object.getYAnim().internalMethod06645(Easing.internalField0811);
            object.getYAnim().internalMethod07059(f8 - f2);
            object.setYFactor(f8);
            if (bl && this.internalField0245 != object && !object.isAlwaysEnabled() && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
            object.getHoverAnimation().internalMethod07062(bl);
            object.getActiveAnimation().internalMethod07062(object.isSelected());
            if ((UiUtils.internalMethod06450(f, f8 - 2.0f, 17.0, 10.0, iII) || object == this.internalField0245) && ((MultiSelectSetting)this.internalField0644).internalMethod04496()) {
                CursorManager.internalMethod06882(CursorType.internalField1205);
            }
            this.internalMethod01383(iII, object, Fonts.internalField0449.internalMethod01432(7.0f), LanguageManager.internalMethod07214(object.getName()), f + (float)(((MultiSelectSetting)this.internalField0644).internalMethod04496() ? 18 : 7), f2 + object.getYAnim().internalMethod02881() + 0.5f, f3 - (float)(((MultiSelectSetting)this.internalField0644).internalMethod04496() ? 30 : 19) - object.getActiveAnimation().internalMethod02881() * 9.0f, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * object.getHoverAnimation().internalMethod02881() + 0.25f * object.getActiveAnimation().internalMethod02881())), bl);
            if (object.getActiveAnimation().internalMethod02881() > 0.0f || object.getCurrentAnimation().internalMethod03147()) {
                RenderPipeline.internalMethod06903(iII.getMatrices(), object.getCurrentAnimation().internalMethod00560(), f + f3 - 11.0f - object.getActiveAnimation().internalMethod02881() * 2.0f, f2 + object.getYAnim().internalMethod02881(), 6.0f, 6.0f, ThemeColors.internalField1613.mulAlpha(0.1f + 0.9f * object.getActiveAnimation().internalMethod02881()));
            }
            f7 += 12.0f;
        }
        if (((MultiSelectSetting)this.internalField0644).internalMethod04496()) {
            TexturedQuadBatch typedValue253 = new TexturedQuadBatch(VertexFormats.POSITION_TEXTURE_COLOR, iII.getMatrices());
            for (MultiSelectSetting.InternalType0091 nestedValue2014 : ((MultiSelectSetting)this.internalField0644).internalMethod01792()) {
                if (nestedValue2014.isHidden()) continue;
                iII.drawIcon("hud/drag", f + 7.0f, f2 + nestedValue2014.getYAnim().internalMethod02881(), 6.0f, ThemeColors.internalMethod08459());
            }
            ((UiVertexBatch)typedValue253).internalMethod09053();
        }
        if (this.internalField0245 != null && this.internalField0519.internalMethod02365(100L) && ((MultiSelectSetting)this.internalField0644).internalMethod04496()) {
            ((MultiSelectSetting)this.internalField0644).notifyChanged();
            ((MultiSelectSetting)this.internalField0644).internalMethod01792().sort(Comparator.comparingDouble(MultiSelectSetting.InternalType0091::getYFactor));
            this.internalField0519.internalMethod00701();
        }
    }

    @Override
    public void internalMethod08256(UiRenderContext iII) {
        if (this.internalField0564 == null) {
            return;
        }
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 1.0f;
        float f3 = this.internalField1048 - 18.0f;
        float f4 = Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890();
        float f5 = 19.0f;
        long l = ((MultiSelectSetting)this.internalField0644).internalMethod01792().stream().filter(nestedValue2013 -> !nestedValue2013.isHidden()).count();
        String string = String.format(" %s", LanguageManager.internalMethod07214("setting_of") + " " + l);
        iII.drawRightText(Fonts.internalField1154.internalMethod01432(7.0f), string, f + f3, f2 - 1.0f + UiUtils.internalMethod07116(f4, f5), ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881()) * RenderSystem.getShaderColor()[3]));
    }

    private void internalMethod01383(UiRenderContext iII, MultiSelectSetting.InternalType0091 nestedValue2014, SizedFont typedValue020, String string, float f, float f2, float f3, ColorRGBA colorRGBA, boolean bl) {
        float f4 = Math.max(1.0f, f3);
        float f5 = typedValue020.internalMethod00965(string);
        long l = System.currentTimeMillis();
        InternalType0504 nestedValue2067 = this.internalField0543.computeIfAbsent(nestedValue2014, nestedValue2013 -> new InternalType0504());
        float f6 = Math.max(0.0f, f5 - f4);
        float f7 = (float)(l - nestedValue2067.internalField0229) / 1000.0f;
        nestedValue2067.internalField0229 = l;
        if (f6 <= 0.0f) {
            nestedValue2067.internalMethod03214(l);
        } else if (bl) {
            nestedValue2067.internalField0205 = Math.min(nestedValue2067.internalField0205, f6);
            if (l >= nestedValue2067.internalField0230) {
                float f8 = f7 * 35.0f;
                if (nestedValue2067.internalField0277) {
                    nestedValue2067.internalField0205 = Math.min(nestedValue2067.internalField0205 + f8, f6);
                    if (nestedValue2067.internalField0205 >= f6) {
                        nestedValue2067.internalField0277 = false;
                        nestedValue2067.internalField0230 = l + 600L;
                    }
                } else {
                    nestedValue2067.internalField0205 = Math.max(nestedValue2067.internalField0205 - f8, 0.0f);
                    if (nestedValue2067.internalField0205 <= 0.0f) {
                        nestedValue2067.internalField0277 = true;
                        nestedValue2067.internalField0230 = l + 600L;
                    }
                }
            }
        } else if (nestedValue2067.internalField0205 > 0.0f) {
            nestedValue2067.internalField0205 = Math.max(0.0f, nestedValue2067.internalField0205 - f7 * 35.0f);
            if (nestedValue2067.internalField0205 == 0.0f) {
                nestedValue2067.internalField0277 = true;
                nestedValue2067.internalField0230 = l;
            }
        }
        ScissorStack.internalMethod06303(iII.getMatrices(), f - 2.0f, f2 - 2.0f, f4 + 4.0f, typedValue020.internalMethod04890() + 4.0f);
        iII.pushMatrix();
        iII.getMatrices().translate(-nestedValue2067.internalField0205, 0.0f);
        iII.drawFadeoutText(typedValue020, string, f, f2, colorRGBA, bl && f6 > 0.0f ? 0.98f : 0.8f, 1.0f, f4 + nestedValue2067.internalField0205);
        iII.popMatrix();
        ScissorStack.internalMethod07643();
    }

    @Override
    public void internalMethod07807(UiRenderContext iII) {
        float f = 0.5f;
        iII.drawRect(this.internalField0205, this.internalField0206 + this.internalField1047, this.internalField1048, f, ThemeColors.internalMethod08459().withAlpha(5.1f));
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        if (typedParameter1015 != MouseButton.internalField0102) {
            return;
        }
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 1.0f;
        float f3 = 0.0f;
        for (MultiSelectSetting.InternalType0091 nestedValue2013 : ((MultiSelectSetting)this.internalField0644).internalMethod01792()) {
            if (nestedValue2013.isHidden()) continue;
            boolean bl = UiUtils.internalMethod05785(f - 1.0f, f2 + 20.0f + f3, this.internalField1048 - 2.0f, 12.0, d, d2);
            if (UiUtils.internalMethod05785(f, f2 + 22.0f + f3, 17.0, 10.0, d, d2) && ((MultiSelectSetting)this.internalField0644).internalMethod04496()) {
                this.internalField0245 = nestedValue2013;
            } else if (bl) {
                nestedValue2013.toggle();
            }
            f3 += 12.0f;
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0245 = null;
        super.internalMethod02863(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 31.0f + this.internalMethod09692();
        return this.internalField1047;
    }

    private float internalMethod09692() {
        float f = 0.0f;
        for (MultiSelectSetting.InternalType0091 nestedValue2013 : ((MultiSelectSetting)this.internalField0644).internalMethod01792()) {
            if (nestedValue2013.isHidden()) continue;
            f += 12.0f;
        }
        return f;
    }

    static class InternalType0504 {
        float internalField0205;
        boolean internalField0277 = true;
        long internalField0229 = System.currentTimeMillis();
        long internalField0230;

        InternalType0504() {
        }

        void internalMethod03214(long l) {
            this.internalField0205 = 0.0f;
            this.internalField0277 = true;
            this.internalField0230 = l;
        }
    }
}
