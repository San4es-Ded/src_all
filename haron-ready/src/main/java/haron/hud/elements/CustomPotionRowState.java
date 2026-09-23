package haron.hud.elements;

import haron.animation.AnimatedValue;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

class CustomPotionRowState {
    final String a;
    String b;
    int c;
    RegistryEntry<StatusEffect> d;
    Item e;
    boolean f;
    boolean g;
    final AnimatedValue h = new AnimatedValue();
    final AnimatedValue i = new AnimatedValue();
    boolean j = false;

    CustomPotionRowState(String string, String string2, int n, RegistryEntry<StatusEffect> registryEntry, Item item, boolean bl, boolean bl2) {
        this.a = string;
        this.b = string2;
        this.c = n;
        this.d = registryEntry;
        this.e = item;
        this.f = bl;
        this.g = bl2;
    }
}

