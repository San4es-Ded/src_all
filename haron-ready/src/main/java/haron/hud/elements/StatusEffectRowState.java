package haron.hud.elements;

import haron.animation.AnimatedValue;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

class StatusEffectRowState {
    final RegistryEntry<StatusEffect> a;
    String b;
    String c;
    int d;
    final AnimatedValue e = new AnimatedValue();
    final AnimatedValue f = new AnimatedValue();
    boolean g = false;

    StatusEffectRowState(RegistryEntry<StatusEffect> registryEntry, String string, String string2, int n) {
        this.a = registryEntry;
        this.b = string;
        this.c = string2;
        this.d = n;
    }
}

