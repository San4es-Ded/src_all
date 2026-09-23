package wtf.wyvern.client.modules.impl.combat;

import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.eventbus.types.Priority;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.player.EventUpdate;

@ModuleAnnotation(
        name = "MaceSwap",
        category = Category.COMBAT,
        description = "Автоматически свапает булаву на момент удара"
)
public final class MaceSwap extends Module {
    public static final MaceSwap INSTANCE = new MaceSwap();

    private final ModeSetting mode = new ModeSetting("Режим", "Скрытый", "Обычный");
    private final BooleanSetting fromInventory = new BooleanSetting("Из инвентаря", true);
    private final BooleanSetting onlyFalling = new BooleanSetting("Только при падении", false);
    private final SliderSetting restoreDelay = new SliderSetting("Задержка возврата", 1.0F, 1.0F, 5.0F, 1.0F);

    private int previousSlot = -1;
    private int inventoryMaceSlot = -1;
    private int restoreTicks;
    private boolean inventorySwap;
    private boolean clientSlotChanged;

    private MaceSwap() {
    }

    @EventTarget(Priority.LOWEST)
    public void onAttack(EventAttack event) {
        if (event.isCancelled() || mc.player == null || mc.world == null
                || mc.interactionManager == null || mc.getNetworkHandler() == null) {
            return;
        }
        if (onlyFalling.isEnabled() && mc.player.fallDistance <= 0.0F) {
            return;
        }
        if (isPending()) {
            restore();
        }
        if (mc.player.getMainHandStack().isOf(Items.MACE)) {
            return;
        }

        int maceSlot = findMace();
        if (maceSlot == -1) {
            return;
        }

        previousSlot = mc.player.getInventory().selectedSlot;
        inventorySwap = maceSlot > 8;
        inventoryMaceSlot = inventorySwap ? maceSlot : -1;

        if (inventorySwap) {
            mc.interactionManager.clickSlot(
                    mc.player.playerScreenHandler.syncId,
                    inventoryMaceSlot,
                    previousSlot,
                    SlotActionType.SWAP,
                    mc.player
            );
        } else {
            clientSlotChanged = mode.is("Обычный");
            if (clientSlotChanged) {
                mc.player.getInventory().selectedSlot = maceSlot;
            }
            sendSelectedSlot(maceSlot);
        }

        restoreTicks = Math.max(1, Math.round(restoreDelay.getCurrent()));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!isPending()) {
            return;
        }
        if (mc.player == null || mc.world == null || mc.interactionManager == null
                || mc.getNetworkHandler() == null) {
            reset();
            return;
        }
        if (--restoreTicks <= 0) {
            restore();
        }
    }

    private int findMace() {
        int end = fromInventory.isEnabled() ? 36 : 9;
        for (int slot = 0; slot < end; slot++) {
            if (mc.player.getInventory().getStack(slot).isOf(Items.MACE)) {
                return slot;
            }
        }
        return -1;
    }

    private void restore() {
        if (!isPending()) {
            return;
        }
        if (mc.player != null && mc.interactionManager != null && mc.getNetworkHandler() != null) {
            if (inventorySwap && inventoryMaceSlot != -1) {
                mc.interactionManager.clickSlot(
                        mc.player.playerScreenHandler.syncId,
                        inventoryMaceSlot,
                        previousSlot,
                        SlotActionType.SWAP,
                        mc.player
                );
            } else {
                if (clientSlotChanged) {
                    mc.player.getInventory().selectedSlot = previousSlot;
                }
                sendSelectedSlot(previousSlot);
            }
        }
        reset();
    }

    private void sendSelectedSlot(int slot) {
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    private boolean isPending() {
        return previousSlot != -1;
    }

    private void reset() {
        previousSlot = -1;
        inventoryMaceSlot = -1;
        restoreTicks = 0;
        inventorySwap = false;
        clientSlotChanged = false;
    }

    @Override
    public void onEnable() {
        reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        restore();
        super.onDisable();
    }
}
