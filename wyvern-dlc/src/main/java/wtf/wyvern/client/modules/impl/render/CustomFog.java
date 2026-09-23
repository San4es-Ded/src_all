package wtf.wyvern.client.modules.impl.render;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ColorSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;

@ModuleAnnotation(name = "CustomFog", category = Category.RENDER, description = "Настраиваемый туман")
public final class CustomFog extends Module {
    public static final CustomFog INSTANCE = new CustomFog();

    public final SliderSetting startDistance = new SliderSetting("Начало", 10.0F, 1.0F, 100.0F, 1.0F);
    public final SliderSetting endDistance = new SliderSetting("Конец", 50.0F, 1.0F, 100.0F, 1.0F);
    public final ColorSetting fogColor = new ColorSetting("Цвет",
            () -> Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor());

    private CustomFog() {
    }

    public boolean shouldModifyFog(Camera camera) {
        if (!isEnabled() || camera == null || mc.world == null || mc.player == null) {
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
