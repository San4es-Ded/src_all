package haron.inventory;

import haron.client.MinecraftClientAccess;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public final class rk1ts2
implements MinecraftClientAccess {
    public static int a;
    public static boolean b;

    private rk1ts2() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Optional<Integer> a(Predicate<ItemStack> predicate, boolean bl, boolean bl2) {
        PlayerInventory playerInventory = rk1ts2.c.player.getInventory();
        int n = !bl ? 9 : 36;
        for (int i = 0; i < n; ++i) {
            int n2;
            if (!predicate.test(playerInventory.getStack(i))) continue;
            if (!bl || bl2) {
                return Optional.of(i);
            }
            if (i >= 9) {
                n2 = i;
            } else {
                int n3 = i;
                n2 = (n3 ^ 0x24) + 2 * (n3 & 0x24);
            }
            return Optional.of(n2);
        }
        return Optional.empty();
    }
}

