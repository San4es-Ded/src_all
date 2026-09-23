package haron.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

class PotionEffectRequirement {
    final RegistryEntry<StatusEffect> effect;
    final int minimumLevel;

    PotionEffectRequirement(RegistryEntry<StatusEffect> effect, int minimumLevel) {
        this.effect = effect;
        this.minimumLevel = minimumLevel;
    }

}
