package haron.hud.elements;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

class PlaceholderEffect {
    final RegistryEntry<StatusEffect> a;
    final String b;
    final String c;

    PlaceholderEffect(RegistryEntry<StatusEffect> registryEntry, String string, String string2) {
        this.a = registryEntry;
        this.b = string;
        this.c = string2;
    }
}

