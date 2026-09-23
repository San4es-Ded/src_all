package haron.hud.elements;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

class StatusEffectSnapshot {
    final RegistryEntry<StatusEffect> a;
    final String b;
    final String c;
    final int d;

    StatusEffectSnapshot(RegistryEntry<StatusEffect> registryEntry, String string, String string2, int n) {
        this.a = registryEntry;
        this.b = string;
        this.c = string2;
        this.d = n;
    }
}

