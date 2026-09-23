package wtf.wyvern.client.modules.impl.render;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "Ambience", category = Category.RENDER, description = "Управляет атмосферой мира")
public class Ambience extends Module {
    public static final Ambience INSTANCE = new Ambience();

    private final ModeSetting timePreset = new ModeSetting("Время", "Вечер", "Утро", "День", "Ночь", "Своё");
    public final SliderSetting timeSetting = new SliderSetting("Своё время", 18.0F, 0.0F, 24.0F, 0.1F,
            () -> timePreset.is("Своё"));
    private final SliderSetting saturation = new SliderSetting("Насыщенность", 1.15F, 0.0F, 2.5F, 0.05F);

    private final ModeSetting fogMode = new ModeSetting("Туман", "Выкл", "Обычный", "Блюр");
    public final SliderSetting fogStart = new SliderSetting("Начало тумана", 8.0F, 1.0F, 80.0F, 1.0F,
            () -> !fogMode.is("Выкл"));
    public final SliderSetting fogEnd = new SliderSetting("Конец тумана", 48.0F, 5.0F, 160.0F, 1.0F,
            () -> !fogMode.is("Выкл"));
    public final SliderSetting blurStrength = new SliderSetting("Сила блюра", 22.0F, 6.0F, 60.0F, 1.0F,
            () -> fogMode.is("Блюр"));
    public final SliderSetting blurDensity = new SliderSetting("Плотность блюра", 0.90F, 0.45F, 1.0F, 0.02F,
            () -> fogMode.is("Блюр"));
    public final SliderSetting blurTint = new SliderSetting("Оттенок темы", 0.52F, 0.0F, 0.85F, 0.01F,
            () -> fogMode.is("Блюр"));

    private Ambience() {
    }

    @FastNative
    public float getSaturation() {
        return saturation.getCurrent();
    }

    @FastNative
    public boolean isTimeEnabled() {
        return isEnabled();
    }

    @FastNative
    public float getTime() {
        if (timePreset.is("Утро")) return 6.0F;
        if (timePreset.is("День")) return 12.0F;
        if (timePreset.is("Ночь")) return 0.0F;
        if (timePreset.is("Своё")) return MathHelper.clamp(timeSetting.getCurrent(), 0.0F, 24.0F);
        return 18.0F;
    }

    @FastNative
    public boolean isFogEnabled() {
        return isEnabled() && !fogMode.is("Выкл");
    }

    @FastNative
    public boolean isBlurFog() {
        return isEnabled() && fogMode.is("Блюр");
    }

    @FastNative
    public boolean isNormalFog() {
        return isEnabled() && fogMode.is("Обычный");
    }

    @FastNative
    public float getFogStart() {
        return Math.min(fogStart.getCurrent(), fogEnd.getCurrent());
    }

    @FastNative
    public float getFogEnd() {
        return Math.max(fogStart.getCurrent(), fogEnd.getCurrent());
    }

    @FastNative
    public float getBlurStrength() {
        return blurStrength.getCurrent();
    }

    @FastNative
    public float getBlurDensity() {
        return blurDensity.getCurrent();
    }

    @FastNative
    public float getBlurTint() {
        return blurTint.getCurrent();
    }

    @FastNative
    public ColorRGBA getThemeFogColor() {
        return Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
    }

    @FastNative
    public ColorRGBA getThemeFogSecondColor() {
        return Wyvern.getInstance().getThemeManager().getCurrentTheme().getSecondColor();
    }

    public boolean shouldModifyFog(Camera camera) {
        if (!isFogEnabled() || camera == null || mc.world == null || mc.player == null) {
            return false;
        }

        CameraSubmersionType submersion = camera.getSubmersionType();
        if (submersion == CameraSubmersionType.WATER
                || submersion == CameraSubmersionType.LAVA
                || submersion == CameraSubmersionType.POWDER_SNOW) {
            return false;
        }

        Entity entity = camera.getFocusedEntity();
        if (entity instanceof LivingEntity livingEntity) {
            return !livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)
                    && !livingEntity.hasStatusEffect(StatusEffects.NIGHT_VISION);
        }
        return true;
    }
}
