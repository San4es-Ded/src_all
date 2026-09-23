package haron.effects;

import haron.effects.PotionEffectRequirement;
import java.util.List;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

class SpecialPotionDefinition {
    final String name;
    final List<PotionEffectRequirement> requirements;
    final RegistryEntry<StatusEffect> displayEffect;
    final boolean harmful;

    SpecialPotionDefinition(String name, List<PotionEffectRequirement> requirements, RegistryEntry<StatusEffect> displayEffect, boolean harmful) {
        this.name = name;
        this.requirements = requirements;
        this.displayEffect = displayEffect;
        this.harmful = harmful;
    }

}
