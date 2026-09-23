package ru.prism.module.impl.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import ru.prism.manager.event_impl.EventKey;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BindSetting;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.utils.math.ChatUtils;

@ModuleInfo(
        name = "Item Swap",
        desc = "Меняет предмет в левой руке по нажатию клавиши — сферу на тотем и обратно, без ковыряния в инвентаре.",
        category = Category.UTILITIES
)
public class ItemSwap extends Module {

    private static final String SPHERE = "Сфера";
    private static final String TOTEM = "Тотем";

    public final ModeSetting swapFrom = new ModeSetting(this, "Откуда", SPHERE, TOTEM);
    public final ModeSetting swapTo = new ModeSetting(this, "Куда", SPHERE, TOTEM);
    public final BindSetting swapKey = new BindSetting(this, "Клавиша", -1);

    public ItemSwap() {
        swapTo.set(TOTEM);
    }

    @EventHandler
    public void onKey(EventKey event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null) return;
        if (swapKey.get() == -1 || event.getKey() != swapKey.get()) return;

        String wanted = isItem(mc.player.getOffHandStack(), swapFrom.getValue())
                ? swapTo.getValue()
                : swapFrom.getValue();

        int slot = findItem(wanted);
        if (slot == -1) {
            ChatUtils.addChatMessage("Предмет для свапа не найден");
            return;
        }

        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
                toScreenSlot(slot), 40, SlotActionType.SWAP, mc.player);
    }

    private int findItem(String target) {
        for (int i = 0; i < 36; i++) {
            if (isItem(mc.player.getInventory().getStack(i), target)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isItem(ItemStack stack, String target) {
        if (stack == null || stack.isEmpty()) return false;
        if (SPHERE.equals(target)) return stack.isOf(Items.PLAYER_HEAD);
        if (TOTEM.equals(target)) return stack.isOf(Items.TOTEM_OF_UNDYING);
        return false;
    }

    private int toScreenSlot(int logical) {
        return logical < 9 ? 36 + logical : logical;
    }
}
