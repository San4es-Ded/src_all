package wtf.wyvern.client.modules.impl.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;

@ModuleAnnotation(
        name = "SP Joiner",
        category = Category.PLAYER,
        description = "Автоматический джойнер для SP"
)
@Environment(EnvType.CLIENT)
@FastNative
public final class SPJoiner extends Module {
    public static final SPJoiner INSTANCE = new SPJoiner();

    private SPJoiner() {
    }

    @EventTarget
    public void onEvent(EventUpdate event) {
        if (mc.player == null || mc.interactionManager == null) {
            return;
        }

        if (mc.currentScreen instanceof HandledScreen<?> handledScreen) {
            ScreenHandler handler = handledScreen.getScreenHandler();

            for (int i = 0; i < handler.slots.size(); i++) {
                Slot slot = handler.slots.get(i);
                ItemStack stack = slot.getStack();
                if (stack.isOf(Items.STICKY_PISTON)) {
                    mc.interactionManager.clickSlot(
                            handler.syncId,
                            i,
                            0,
                            SlotActionType.PICKUP,
                            mc.player
                    );
                    return;
                }
            }
        } else {
            selectCompass();
        }
    }

    public static void selectCompass() {
        if (mc.player == null || mc.interactionManager == null || mc.getNetworkHandler() == null) {
            return;
        }

        PlayerInventory inventory = mc.player.getInventory();
        int slot = -1;

        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).isOf(Items.COMPASS)) {
                slot = i;
                break;
            }
        }

        if (slot != -1) {
            inventory.setSelectedSlot(slot);
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        }
    }
}
