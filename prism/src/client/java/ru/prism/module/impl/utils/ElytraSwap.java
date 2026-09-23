package ru.prism.module.impl.utils;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import ru.prism.manager.event_impl.EventKey;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BindSetting;
import ru.prism.utils.math.ChatUtils;

@ModuleInfo(
        name = "Elytra Swap",
        desc = "Меняет нагрудник на элитры и обратно по клавише — клиент сам открывает инвентарь и перекидывает вещи между слотом брони и инвентарём.",
        category = Category.UTILITIES
)
public class ElytraSwap extends Module {

    private static final int CHEST_SCREEN_SLOT = 6;

    public final BindSetting swapKey = new BindSetting(this, "Клавиша", -1);

    private boolean swapping;
    private int state;
    private int delay;
    private boolean clicked;

    @EventHandler
    public void onKey(EventKey event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;
        if (swapKey.get() == -1 || event.getKey() != swapKey.get()) return;
        if (swapping) return;

        swapping = true;
        state = 0;
        delay = 0;
        clicked = false;
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null || !swapping) return;

        if (delay > 0) {
            delay--;
            return;
        }

        switch (state) {
            case 0 -> {
                if (!mc.player.getAbilities().creativeMode) {
                    boolean wearingElytra = wearingElytra();
                    if (wearingElytra && findChestplate() == -1) {
                        ChatUtils.addChatMessage("Elytra Swap: свап отменён — нет нагрудника");
                        abort();
                        return;
                    }
                    if (!wearingElytra && findElytra() == -1) {
                        ChatUtils.addChatMessage("Elytra Swap: свап отменён — нет элитр");
                        abort();
                        return;
                    }
                }

                if (!(mc.currentScreen instanceof InventoryScreen)) {
                    mc.setScreen(new InventoryScreen(mc.player));
                }

                delay = 2;
                state = 1;
            }
            case 1 -> {
                if (!(mc.currentScreen instanceof InventoryScreen)) {
                    abort();
                    return;
                }
                if (!clicked) {
                    performSwap();
                    clicked = true;
                    delay = 2;
                    state = 2;
                }
            }
            case 2 -> {
                if (mc.currentScreen instanceof InventoryScreen) {
                    mc.player.closeHandledScreen();
                }
                abort();
            }
            default -> abort();
        }
    }

    private void performSwap() {
        if (wearingElytra()) {
            int slot = findChestplate();
            if (slot == -1) return;
            swapSlot(slot);
            ChatUtils.addChatMessage("Elytra Swap: снял элитры");
        } else {
            int slot = findElytra();
            if (slot == -1) return;
            swapSlot(slot);
            ChatUtils.addChatMessage("Elytra Swap: надел элитры");
        }
    }

    private void swapSlot(int inventorySlot) {
        int syncId = mc.player.currentScreenHandler.syncId;
        int screenSlot = toScreenSlot(inventorySlot);
        mc.interactionManager.clickSlot(syncId, screenSlot, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(syncId, CHEST_SCREEN_SLOT, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(syncId, screenSlot, 0, SlotActionType.PICKUP, mc.player);
    }

    private boolean wearingElytra() {
        return mc.player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
    }

    private int findElytra() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).isOf(Items.ELYTRA)) {
                return i;
            }
        }
        return -1;
    }

    private int findChestplate() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty() || stack.isOf(Items.ELYTRA)) continue;
            EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);
            if (equippable != null && equippable.slot() == EquipmentSlot.CHEST) {
                return i;
            }
        }
        return -1;
    }

    private int toScreenSlot(int logical) {
        return logical < 9 ? 36 + logical : logical;
    }

    private void abort() {
        swapping = false;
        state = 0;
        delay = 0;
        clicked = false;
    }

    @Override
    protected void onDisable() {
        abort();
        super.onDisable();
    }
}
