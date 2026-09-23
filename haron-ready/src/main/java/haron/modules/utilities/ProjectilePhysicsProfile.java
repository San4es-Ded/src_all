package haron.modules.utilities;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

enum ProjectilePhysicsProfile {
    ENDER_PEARL(1.5, 0.99, 0.03),
    TRIDENT(2.5, 0.99, 0.05),
    ARROW(3.0, 0.99, 0.05),
    POTION(0.5, 0.99, 0.05);

    final double baseSpeed;
    final double drag;
    final double gravity;

    private ProjectilePhysicsProfile(double d, double d2, double d3) {
        this.baseSpeed = d;
        this.drag = d2;
        this.gravity = d3;
    }

    static ProjectilePhysicsProfile from(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return null;
        }
        Item item = itemStack.getItem();
        if (item == Items.ENDER_PEARL) {
            return ENDER_PEARL;
        }
        if (item == Items.TRIDENT) {
            return TRIDENT;
        }
        if (item == Items.BOW || item == Items.CROSSBOW) {
            return ARROW;
        }
        if (item == Items.SPLASH_POTION || item == Items.LINGERING_POTION) {
            return POTION;
        }
        return null;
    }
}
