package haron.hud.elements;

import haron.animation.AnimatedValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class CooldownRowState {
    final Item a;
    ItemStack b;
    String c;
    String d;
    int e;
    final AnimatedValue f = new AnimatedValue();
    final AnimatedValue g = new AnimatedValue();
    boolean h = false;

    CooldownRowState(Item item, ItemStack itemStack, String string, String string2, int n) {
        this.a = item;
        this.b = itemStack;
        this.c = string;
        this.d = string2;
        this.e = n;
    }

}

