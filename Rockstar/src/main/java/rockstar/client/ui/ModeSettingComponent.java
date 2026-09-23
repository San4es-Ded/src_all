package rockstar.client.ui;






import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.ModeSetting;
import rockstar.client.ui.MouseButton;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.core.SpriteAnimationPlayer;

public class ModeSettingComponent
extends SettingComponent<ModeSetting> {
    private boolean internalField0277;

    public ModeSettingComponent(ModeSetting typedValue170, LegacyUiElement typedValue001) {
        super(typedValue170, typedValue001);
    }

    @Override
    protected void internalMethod05619(UiRenderContext iII) {
        if (!this.internalField0277) {
            for (ModeSetting.InternalType0088 nestedValue2011 : ((ModeSetting)this.internalField0644).internalMethod06723()) {
                this.internalMethod03074(nestedValue2011);
            }
            this.internalField0277 = true;
        }
        float f = this.internalField0205 + 9.0f;
        float f2 = this.internalField0206 + 1.0f;
        float f3 = this.internalField1048 - 18.0f;
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(8.0f);
        float f4 = 10.0f;
        float f5 = 19.0f;
        this.internalField0808.internalMethod07062(this.internalMethod04933(iII.internalMethod05259(), iII.internalMethod05261()));
        this.internalMethod00985(iII, typedValue020, LanguageManager.internalMethod07214(((ModeSetting)this.internalMethod05697()).getName()), this.internalField0205 + f4, f2 - 1.0f + UiUtils.internalMethod07116(typedValue020.internalMethod04890(), f5), this.internalMethod05803().internalMethod08827() - f4, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * this.internalField0808.internalMethod02881())), 0.8f, 1.0f);
        iII.drawRoundedRect(f - 1.0f, f2 + 17.0f, f3 + 2.0f, 8.0f + this.internalMethod09291(), CornerRadii.internalMethod03908(6.0f), ThemeColors.internalMethod07738().withAlpha(76.5f));
        float f6 = 0.0f;
        for (ModeSetting.InternalType0088 nestedValue2011 : ((ModeSetting)this.internalField0644).internalMethod06723()) {
            if (nestedValue2011.isHidden()) continue;
            this.internalMethod03074(nestedValue2011);
            boolean bl = nestedValue2011.isSelected();
            if (bl != nestedValue2011.isLastState()) {
                if (bl) {
                    nestedValue2011.setCurrentAnimation(nestedValue2011.getEnableAnimation());
                } else {
                    nestedValue2011.setCurrentAnimation(nestedValue2011.getDisableAnimation());
                }
                nestedValue2011.getCurrentAnimation().internalMethod03142();
                nestedValue2011.setLastState(bl);
            }
            nestedValue2011.getCurrentAnimation().internalMethod03146();
            boolean bl2 = UiUtils.internalMethod05786(f - 1.0f, f2 + 20.0f + f6, f3 + 2.0f, 12.0, iII.internalMethod05259(), iII.internalMethod05261());
            if (bl2 && (float)iII.internalMethod05261() > this.internalMethod09904() && (float)iII.internalMethod05261() < this.internalMethod09904() + this.internalMethod09905()) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
            nestedValue2011.getHoverAnimation().internalMethod07062(bl2);
            nestedValue2011.getActiveAnimation().internalMethod07062(nestedValue2011.isSelected());
            iII.drawFadeoutText(Fonts.internalField0449.internalMethod01432(7.0f), LanguageManager.internalMethod07214(nestedValue2011.getName()), f + 7.0f, f2 + 24.5f + f6, ThemeColors.internalMethod08459().withAlpha(255.0f * (0.75f + 0.25f * nestedValue2011.getHoverAnimation().internalMethod02881() + 0.25f * nestedValue2011.getActiveAnimation().internalMethod02881())), 0.8f, 1.0f, f3 - 12.0f - nestedValue2011.getActiveAnimation().internalMethod02881() * 10.0f);
            if (nestedValue2011.getActiveAnimation().internalMethod02881() > 0.0f || nestedValue2011.getCurrentAnimation().internalMethod03147()) {
                RenderPipeline.internalMethod06903(iII.getMatrices(), nestedValue2011.getCurrentAnimation().internalMethod00560(), f + f3 - 11.0f - nestedValue2011.getActiveAnimation().internalMethod02881() * 2.0f, f2 + 24.0f + f6, 6.0f, 6.0f, ThemeColors.internalField1613.mulAlpha(0.1f + 0.9f * nestedValue2011.getActiveAnimation().internalMethod02881()));
            }
            f6 += 12.0f;
        }
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
        float f = 0.0f;
        for (ModeSetting.InternalType0088 nestedValue2011 : ((ModeSetting)this.internalField0644).internalMethod06723()) {
            if (nestedValue2011.isHidden()) continue;
            boolean bl = UiUtils.internalMethod05785(this.internalField0205 - 1.0f, this.internalField0206 + 20.0f + f, this.internalField1048 - 2.0f, 12.0, d, d2);
            if (bl) {
                nestedValue2011.select();
            }
            f += 12.0f;
        }
        super.internalMethod01643(d, d2, typedParameter1015);
    }

    @Override
    public float internalMethod07809() {
        this.internalField1047 = 31.0f + this.internalMethod09291();
        return this.internalField1047;
    }

    private float internalMethod09291() {
        float f = 0.0f;
        for (ModeSetting.InternalType0088 nestedValue2011 : ((ModeSetting)this.internalField0644).internalMethod06723()) {
            if (nestedValue2011.isHidden()) continue;
            f += 12.0f;
        }
        return f;
    }

    private void internalMethod03074(ModeSetting.InternalType0088 nestedValue2011) {
        if (nestedValue2011.getEnableAnimation() != null && nestedValue2011.getDisableAnimation() != null && nestedValue2011.getCurrentAnimation() != null) {
            return;
        }
        nestedValue2011.setEnableAnimation(new SpriteAnimationPlayer(RockstarClient.id("animations/check_enable.zip")));
        nestedValue2011.setDisableAnimation(new SpriteAnimationPlayer(RockstarClient.id("animations/check_disable.zip")));
        nestedValue2011.setLastState(nestedValue2011.isSelected());
        nestedValue2011.setCurrentAnimation(nestedValue2011.isLastState() ? nestedValue2011.getEnableAnimation() : nestedValue2011.getDisableAnimation());
        if (nestedValue2011.isLastState()) {
            nestedValue2011.getEnableAnimation().internalMethod03142();
        } else {
            nestedValue2011.getDisableAnimation().internalMethod07104(0);
            nestedValue2011.getDisableAnimation().internalMethod09078();
        }
    }
}
