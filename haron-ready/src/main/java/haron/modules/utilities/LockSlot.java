package haron.modules.utilities;

import haron.events.DropItemEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import java.util.Objects;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;

@ModuleInfo(a="Lock Slot", b="Prevents dropping items from selected hotbar slots.", c=ModuleCategory.UTILITIES)
public class LockSlot
extends HaronModule {
    private final BooleanSetting lockHotbarSlots = new BooleanSetting("Блокировать слоты хотбара", true);
    private final ModeSetting slots;
    private final BooleanSetting lockOnlyValuableItems;

    private static boolean isValuable(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ArmorItem || itemStack.getItem() instanceof MiningToolItem) {
            return true;
        }
        if (itemStack.getItem() == Items.TOTEM_OF_UNDYING || itemStack.getItem() == Items.ELYTRA || itemStack.getItem() == Items.SHIELD || itemStack.getItem() == Items.CROSSBOW || itemStack.getItem() == Items.BOW || itemStack.getItem() == Items.TRIDENT || itemStack.getItem() == Items.ENDER_PEARL || itemStack.getItem() == Items.CHORUS_FRUIT || itemStack.getItem() == Items.GOLDEN_APPLE || itemStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
            return true;
        }
        return itemStack.hasEnchantments();
    }

    public LockSlot() {
        ModeSetting s82syr2 = new ModeSetting("Слоты", new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}, new int[0]);
        BooleanSetting xcv91t2 = this.lockHotbarSlots;
        Objects.requireNonNull(xcv91t2);
        this.slots = s82syr2.a(xcv91t2::k);
        this.lockOnlyValuableItems = new BooleanSetting("Только ценные предметы", false);
    }

    @EventHandler
    public void a(DropItemEvent s8sa4k2) {
        if (!this.lockHotbarSlots.a()) {
            return;
        }
        int n = s8sa4k2.slot();
        if (n < 0 || n > 8) {
            return;
        }
        if (!this.slots.a(n)) {
            return;
        }
        if (this.lockOnlyValuableItems.a()) {
            if (LockSlot.c.player == null) {
                return;
            }
            ItemStack itemStack = LockSlot.c.player.getInventory().getStack(n);
            if (itemStack.isEmpty() || !LockSlot.isValuable(itemStack)) {
                return;
            }
        }
        s8sa4k2.a();
    }
}

