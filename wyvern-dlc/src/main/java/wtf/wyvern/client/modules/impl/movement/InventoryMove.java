package wtf.wyvern.client.modules.impl.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.gui.screens.menu.MenuScreen;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventClickSlot;
import wtf.wyvern.core.events.impl.other.EventCloseScreen;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.utility.game.other.InventoryUtil;

@ModuleAnnotation(
        name = "InventoryMove",
        category = Category.MOVEMENT,
        description = "Позволяет безопасно двигаться с открытым инвентарём"
)
@FastNative
public final class InventoryMove extends Module {
    public static final InventoryMove INSTANCE = new InventoryMove();

    private static final String MODE_NORMAL = "Обычный";
    private static final String MODE_FUNTIME = "FunTime";
    private static final String MODE_SPOOKYTIME = "SpookyTime";
    private static final String MODE_HOLYWORLD = "HolyWorld";
    private static final String MODE_RILLYWORLD = "RillyWorld";
    private static final String MODE_COPYTIME = "CopyTime";

    private final ModeSetting mode = new ModeSetting(
            "Режим",
            MODE_NORMAL,
            MODE_FUNTIME,
            MODE_SPOOKYTIME,
            MODE_HOLYWORLD,
            MODE_RILLYWORLD,
            MODE_COPYTIME
    );
    private final SliderSetting packetDelay = new SliderSetting(
            "Задержка пакетов", 1.0F, 0.0F, 1000.0F, 1.0F);
    private final SliderSetting stopDelay = new SliderSetting(
            "Стоп", 1.0F, 0.0F, 1000.0F, 1.0F);
    private final SliderSetting sendPacketDelay = new SliderSetting(
            "Отправка пакета", 1.0F, 0.0F, 1000.0F, 1.0F);

    private final List<PendingClick> pending = new ArrayList<>();
    private final List<ItemStack> handlerSnapshot = new ArrayList<>();
    private ItemStack cursorSnapshot = ItemStack.EMPTY;
    private int snapshotSyncId = -1;

    private MovePhase movePhase = MovePhase.READY;
    private long actionStartTime;
    private boolean keysOverridden;
    private boolean inventoryOpened;
    private boolean openedPlayerInventory;
    private boolean packetsHeld;
    private boolean replaying;
    private boolean closing;
    private boolean closeRequested;
    private boolean screenWasOpen;

    private InventoryMove() {
        mode.set(MODE_SPOOKYTIME);
    }

    public boolean isInputSuspended() {
        return keysOverridden;
    }

    public boolean shouldHandleInput() {
        return guiMoveAllowed() && InventoryUtil.isPlayerInventory();
    }

    private boolean isDefaultMode() {
        return mode.is(MODE_NORMAL);
    }

    private boolean isLegitMode() {
        return !isDefaultMode();
    }

    private boolean guiMoveAllowed() {
        return mc.currentScreen != null
                && !(mc.currentScreen instanceof ChatScreen)
                && !(mc.currentScreen instanceof MenuScreen);
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (!isLegitMode()) {
            return;
        }

        if (event.isSent() && !closing
                && event.getPacket() instanceof CloseHandledScreenC2SPacket packet
                && packet.getSyncId() == 0 && inventoryOpened && openedPlayerInventory) {
            event.cancel();
            return;
        }

        if (event.isReceive()
                && event.getPacket() instanceof CloseScreenS2CPacket packet
                && packet.getSyncId() == 0 && (packetsHeld || !pending.isEmpty())) {
            event.cancel();
        }
    }

    @EventTarget
    private void onClickSlot(EventClickSlot event) {
        if (!isLegitMode() || replaying || InventoryUtil.isInternalClick()
                || !guiMoveAllowed() || mc.player == null
                || mc.player.currentScreenHandler == null) {
            return;
        }
        if (!packetsHeld && !InventoryUtil.hasPlayerMovement()) {
            return;
        }
        if (mc.player.currentScreenHandler.syncId != event.getWindowId()) {
            return;
        }

        if (pending.isEmpty()) {
            captureHandlerSnapshot(event.getWindowId());
        }

        pending.add(new PendingClick(
                event.getWindowId(),
                event.getSlotId(),
                event.getButton(),
                event.getActionType()
        ));
        packetsHeld = true;

        try {
            mc.player.currentScreenHandler.onSlotClick(
                    event.getSlotId(),
                    event.getButton(),
                    event.getActionType(),
                    mc.player
            );
        } catch (RuntimeException ignored) {
        }
        event.setCancelled(true);
    }

    private void captureHandlerSnapshot(int syncId) {
        handlerSnapshot.clear();
        if (mc.player == null || mc.player.currentScreenHandler == null) {
            snapshotSyncId = -1;
            cursorSnapshot = ItemStack.EMPTY;
            return;
        }

        snapshotSyncId = syncId;
        for (Slot slot : mc.player.currentScreenHandler.slots) {
            handlerSnapshot.add(slot.getStack().copy());
        }
        cursorSnapshot = mc.player.currentScreenHandler.getCursorStack().copy();
    }

    private void restoreHandlerSnapshot() {
        if (mc.player == null || mc.player.currentScreenHandler == null
                || snapshotSyncId != mc.player.currentScreenHandler.syncId
                || handlerSnapshot.size() != mc.player.currentScreenHandler.slots.size()) {
            return;
        }

        for (int i = 0; i < handlerSnapshot.size(); i++) {
            mc.player.currentScreenHandler.slots.get(i).setStack(handlerSnapshot.get(i).copy());
        }
        mc.player.currentScreenHandler.setCursorStack(cursorSnapshot.copy());
    }

    @EventTarget
    private void onTick(EventTick event) {
        if (mc.player == null || mc.world == null) {
            resetState();
            return;
        }

        if (!isLegitMode()) {
            if (shouldHandleInput()) {
                InventoryUtil.updateMoveKeys();
            }
            return;
        }

        processLegitMovement();
    }

    private void processLegitMovement() {
        boolean hasOpenScreen = guiMoveAllowed();

        if (hasOpenScreen && !inventoryOpened && movePhase == MovePhase.READY) {
            startLegitMovement();
            inventoryOpened = true;
            openedPlayerInventory = InventoryUtil.isPlayerInventory();
        }

        if (!hasOpenScreen && screenWasOpen && inventoryOpened) {
            boolean wasPlayerInventory = openedPlayerInventory;
            inventoryOpened = false;
            openedPlayerInventory = false;
            screenWasOpen = false;

            if (!wasPlayerInventory) {
                resetState();
                return;
            }

            InventoryUtil.unPressMoveKeys();
            InventoryUtil.stopSprint();
            keysOverridden = true;

            if (movePhase == MovePhase.ALLOW_MOVEMENT) {
                movePhase = MovePhase.SLOWING_DOWN;
                actionStartTime = System.currentTimeMillis();
            } else {
                resetState();
            }
            return;
        }

        screenWasOpen = hasOpenScreen;
        if (movePhase != MovePhase.READY) {
            handleMovementStates();
        }
    }

    private void startLegitMovement() {
        movePhase = MovePhase.ALLOW_MOVEMENT;
        keysOverridden = false;
        packetsHeld = false;
    }

    private void handleMovementStates() {
        long elapsed = System.currentTimeMillis() - actionStartTime;

        switch (movePhase) {
            case SLOWING_DOWN -> {
                if (!keysOverridden) {
                    InventoryUtil.unPressMoveKeys();
                    keysOverridden = true;
                }
                if (elapsed >= Math.round(stopDelay.getCurrent())) {
                    movePhase = MovePhase.SEND_PACKETS;
                    actionStartTime = System.currentTimeMillis();
                }
            }
            case ALLOW_MOVEMENT -> {
                if (shouldHandleInput()) {
                    InventoryUtil.updateMoveKeys();
                }
            }
            case SEND_PACKETS -> {
                if (elapsed < Math.round(packetDelay.getCurrent())) {
                    return;
                }
                if (!pending.isEmpty() && mc.interactionManager != null) {
                    restoreHandlerSnapshot();
                    replaying = true;
                    try {
                        for (PendingClick click : pending) {
                            InventoryUtil.replayClick(
                                    click.syncId(),
                                    click.slotId(),
                                    click.button(),
                                    click.actionType()
                            );
                        }
                    } catch (RuntimeException ignored) {
                    } finally {
                        replaying = false;
                    }
                    pending.clear();
                    InventoryUtil.updateSlots();
                }

                sendInventoryClosePacket();
                if (closeRequested) {
                    closeRequested = false;
                    inventoryOpened = false;
                    openedPlayerInventory = false;
                    screenWasOpen = false;
                    mc.setScreen(null);
                }
                packetsHeld = false;
                movePhase = MovePhase.SPEEDING_UP;
                actionStartTime = System.currentTimeMillis();
            }
            case SPEEDING_UP -> {
                if (elapsed < Math.round(sendPacketDelay.getCurrent())) {
                    return;
                }
                if (keysOverridden) {
                    InventoryUtil.updateMoveKeys();
                    InventoryUtil.resumeSprintFromPhysical();
                    keysOverridden = false;
                }
                movePhase = MovePhase.FINISHED;
            }
            case FINISHED -> resetState();
            case READY -> {
            }
        }
    }

    private void sendInventoryClosePacket() {
        if (mc.player == null || mc.getNetworkHandler() == null
                || mc.player.currentScreenHandler == null) {
            return;
        }

        closing = true;
        try {
            mc.getNetworkHandler().sendPacket(
                    new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId)
            );
        } finally {
            closing = false;
        }
    }

    @EventTarget
    private void onCloseScreen(EventCloseScreen event) {
        if (!isLegitMode() || closing || pending.isEmpty()
                || !inventoryOpened || !openedPlayerInventory) {
            return;
        }
        event.cancel();
        closeRequested = true;
        InventoryUtil.unPressMoveKeys();
        InventoryUtil.stopSprint();
        keysOverridden = true;
        if (movePhase == MovePhase.ALLOW_MOVEMENT) {
            movePhase = MovePhase.SLOWING_DOWN;
            actionStartTime = System.currentTimeMillis();
        }
    }

    private boolean isPhysicallyPressed(KeyBinding key) {
        int code = InputUtil.fromTranslationKey(key.getBoundKeyTranslationKey()).getCode();
        return code >= 0 && InputUtil.isKeyPressed(mc.getWindow().getHandle(), code);
    }

    private boolean hasPhysicalMovementIntent() {
        return isPhysicallyPressed(mc.options.forwardKey)
                || isPhysicallyPressed(mc.options.backKey)
                || isPhysicallyPressed(mc.options.leftKey)
                || isPhysicallyPressed(mc.options.rightKey);
    }

    private void resetState() {
        if (!pending.isEmpty()) {
            restoreHandlerSnapshot();
        }
        if (keysOverridden && hasPhysicalMovementIntent()) {
            InventoryUtil.updateMoveKeys();
        }
        keysOverridden = false;
        movePhase = MovePhase.READY;
        inventoryOpened = false;
        openedPlayerInventory = false;
        packetsHeld = false;
        replaying = false;
        closing = false;
        closeRequested = false;
        screenWasOpen = false;
        pending.clear();
        handlerSnapshot.clear();
        cursorSnapshot = ItemStack.EMPTY;
        snapshotSyncId = -1;
    }

    @Override
    public void onDisable() {
        resetState();
        super.onDisable();
    }

    private enum MovePhase {
        READY,
        SLOWING_DOWN,
        ALLOW_MOVEMENT,
        SPEEDING_UP,
        SEND_PACKETS,
        FINISHED
    }

    private record PendingClick(
            int syncId,
            int slotId,
            int button,
            SlotActionType actionType
    ) {
    }
}
