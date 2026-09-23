package su.sacura.features.modules.impl.render;

import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.ModeSetting;
import su.sacura.features.modules.settings.impl.SliderSetting;
import su.sacura.util.type.ISetting;

@ModuleAnnotations(name = "Full Bright", category = Category.RENDER)
public class FullBrightModule extends Module {
    public final ModeSetting mode = (new ModeSetting("Режим", "Гамма", new String[] { "Гамма", "Ночное видение" })).setDescription("Позволяет видеть в темноте");
    public final SliderSetting bright = (new SliderSetting("Яркость", 1.0F, 0.0F, 1.0F, 0.1F)).setVisible(() -> Boolean.valueOf(this.mode.is("Гамма"))).setDescription("Устанавливает значение яркости");

    public FullBrightModule() {
        addSettings(new ISetting[] { (ISetting)this.mode, (ISetting)this.bright });
    }

    public void onDisable() {
        if (mc.player == null)
            return;
        // Исправлено: StatusEffects.NIGHT_VISION уже является RegistryEntry<StatusEffect>
        mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1, 0, true, false, false));
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(EventUpdate event) {
        if (mc.player == null)
            return;
        if (this.mode.is("Ночное видение")) {
            // Исправлено: StatusEffects.NIGHT_VISION уже является RegistryEntry<StatusEffect>
            StatusEffectInstance nv = mc.player.getStatusEffect(StatusEffects.NIGHT_VISION);
            if (nv == null || nv.getDuration() < 200 || nv.getAmplifier() != 1337)
                mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 16341, 1337, true, false, false));
        }
    }
}