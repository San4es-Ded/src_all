package ru.prism.module.impl.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import ru.prism.manager.event_impl.DropItemEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.MultiBooleanSetting;

@ModuleInfo(
        name = "Lock Slot",
        desc = "Не даёт выкинуть вещи из залоченных слотов хотбара, даже если руки чешутся.",
        category = Category.UTILITIES
)
public class LockSlot extends Module {

    public final BooleanSetting lockHotbar = new BooleanSetting(this, "Блокировать хотбар", true);
    public final MultiBooleanSetting slots = new MultiBooleanSetting(this, "Слоты",
            new BooleanSetting("1", true),
            new BooleanSetting("2", true),
            new BooleanSetting("3", true),
            new BooleanSetting("4", true),
            new BooleanSetting("5", true),
            new BooleanSetting("6", true),
            new BooleanSetting("7", true),
            new BooleanSetting("8", true),
            new BooleanSetting("9", true));
    public final BooleanSetting valuableOnly = new BooleanSetting(this, "Только ценные", false);

    @EventHandler
    public void onDropItem(DropItemEvent event) {
        if (!lockHotbar.getValue()) return;

        int slot = event.getSlot();
        if (slot < 0 || slot > 8) return;

        if (!slots.getValue(String.valueOf(slot + 1)) && slots.isAnyTrue()) return;

        if (valuableOnly.getValue()) {
            if (mc.player == null) return;

            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.isEmpty() || !isValuable(stack)) return;
        }

        event.cancel();
    }

    private static boolean isValuable(ItemStack stack) {
        if (stack.get(DataComponentTypes.EQUIPPABLE) == null && stack.get(DataComponentTypes.TOOL) == null) {
            Text name = stack.getName();
            if (name == null || !name.getString().contains("★") && !name.getStyle().isObfuscated()) {
                return stack.getItem() == Items.TOTEM_OF_UNDYING
                        || stack.getItem() == Items.ELYTRA
                        || stack.getItem() == Items.SHIELD
                        || stack.getItem() == Items.CROSSBOW
                        || stack.getItem() == Items.BOW
                        || stack.getItem() == Items.TRIDENT
                        || stack.getItem() == Items.ENDER_PEARL
                        || stack.getItem() == Items.CHORUS_FRUIT
                        || stack.getItem() == Items.GOLDEN_APPLE
                        || stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE
                        || stack.hasEnchantments();
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
