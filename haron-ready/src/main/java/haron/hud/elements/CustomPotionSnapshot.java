package haron.hud.elements;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;

class CustomPotionSnapshot {
    final String a;
    final String b;
    final int c;
    final RegistryEntry<StatusEffect> d;
    final Item e;
    final boolean f;
    final boolean g;

    CustomPotionSnapshot(String string, String string2, int n, RegistryEntry<StatusEffect> registryEntry, Item item, boolean bl, boolean bl2) {
        this.a = string;
        this.b = string2;
        this.c = n;
        this.d = registryEntry;
        this.e = item;
        this.f = bl;
        this.g = bl2;
    }
}

