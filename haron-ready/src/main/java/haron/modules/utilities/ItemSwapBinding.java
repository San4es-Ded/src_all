package haron.modules.utilities;

import haron.settings.KeybindSetting;
import java.util.function.Predicate;
import net.minecraft.item.ItemStack;

final class ItemSwapBinding {
    final KeybindSetting c;
    final Predicate<ItemStack> d;
    public static int a;
    public static boolean b;

    public ItemSwapBinding(KeybindSetting hvn3h82, Predicate<ItemStack> predicate) {
        this.c = hvn3h82;
        this.d = predicate;
    }

    public Predicate<ItemStack> b() {
        return this.d;
    }

    public KeybindSetting a() {
        return this.c;
    }

}
