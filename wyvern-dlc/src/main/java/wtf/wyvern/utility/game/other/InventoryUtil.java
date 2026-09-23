package wtf.wyvern.utility.game.other;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.PlayerInput;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@Deprecated
@FastNative
public class InventoryUtil implements IMinecraft {
   private static boolean internalClick;
   private static KeyBinding[] moveKeysCache;
   private static boolean isSwapping;
   private static int swapPhase;
   private static Slot swapTargetSlot;
   private static int swapOriginalSlot = -1;
   private static boolean swapLockMoves = true;
   private static boolean swapLightLock;
   private static SwapMode swapMode = SwapMode.USE;
   private static int swapMoveFromId = -1;
   private static int swapMoveToId = -1;
   private static int swapClickSlotId = -1;
   private static int swapClickButton = -1;
   private static int waitTicks;
   private static boolean swapUpdateInventory = true;
   private static boolean swapWasSprinting;
   private static int swapWatchdogTicks;
   private static int swapGateTick = -1;
   private static final int LEGIT_PHASE_CLICK = 2;
   private static final int LEGIT_PHASE_FINISH = 3;
   private static final int LEGIT_WATCHDOG_TICKS = 20;
   private static boolean canMove = true;
   public static final LightSwapScript lightSwapScript = new LightSwapScript();
   private static boolean initialized;

   static {
      init();
   }

   public static void init() {
      if (!initialized) {
         initialized = true;
         EventManager.register(new TickHandler());
      }
   }

   private static final class TickHandler {
      @EventTarget
      private void onTick(EventTick event) {
         tick();
      }
   }

   public static boolean isInternalClick() {
      return internalClick;
   }

   public static void tick() {
      processSwapPhase();
      lightSwapScript.update();
   }

   public static void clickSlot(int slotId, int buttonId, SlotActionType clickType) {
      if (mc.player == null || mc.interactionManager == null || mc.player.currentScreenHandler == null) {
         return;
      }
      if (slotId < 0 || slotId >= mc.player.currentScreenHandler.slots.size()) {
         return;
      }
      if (clickType == SlotActionType.SWAP && buttonId != 40 && (buttonId < 0 || buttonId > 8)) {
         return;
      }
      internalClick = true;
      try {
         mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, slotId, buttonId, clickType, mc.player);
      } finally {
         internalClick = false;
      }
   }

   public static void grimSwap(int slotId, int button) {
      if (mc.player == null || mc.interactionManager == null || mc.getNetworkHandler() == null) {
         return;
      }
      mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(
              new PlayerInput(false, false, false, false, false, false, false)));
      if (mc.player.isSprinting()) {
         mc.player.setSprinting(false);
         mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(
                 mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
      }
      clickSlot(slotId, button, SlotActionType.SWAP);
      mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(0));
      mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(mc.player.input.playerInput));
   }

   public static void replayClick(int syncId, int slotId, int button, SlotActionType actionType) {
      if (mc.player == null || mc.interactionManager == null) {
         return;
      }
      internalClick = true;
      try {
         mc.interactionManager.clickSlot(syncId, slotId, button, actionType, mc.player);
      } finally {
         internalClick = false;
      }
   }

   public static void swapHand(Slot slot, Hand hand, boolean updateInventory) {
      if (slot == null || mc.player == null) {
         return;
      }
      int button = hand == Hand.MAIN_HAND ? mc.player.getInventory().selectedSlot : 40;
      clickSlot(slot.id, button, SlotActionType.SWAP);
      if (updateInventory) {
         updateSlots();
      }
   }

   public static boolean isSwapping() {
      return isSwapping;
   }

   public static void swapAndUseFromHotbar(Item item) {
      swapAndUseFromHotbar(getSlot(item));
   }

   public static void swapAndUseFromHotbar(Slot slot) {
      if (slot == null || isSwapping || mc.player == null) {
         return;
      }
      isSwapping = true;
      swapPhase = 1;
      swapTargetSlot = slot;
      swapOriginalSlot = mc.player.getInventory().selectedSlot;
      swapLockMoves = !isHotbarSlotId(slot.id);
      swapMode = SwapMode.USE;
      swapWasSprinting = mc.player.isSprinting();
      processSwapPhase();
   }

   public static void swapItemsPhased(int from, int to, boolean updateInventory) {
      swapItemsPhased(from, to, updateInventory, false);
   }

   public static void swapItemsPhasedWithMoveLock(int from, int to, boolean updateInventory) {
      swapItemsPhased(from, to, updateInventory, true);
   }

   private static void swapItemsPhased(int from, int to, boolean updateInventory, boolean moveLock) {
      if (isSwapping || from == to || from == -1 || mc.player == null) {
         return;
      }
      isSwapping = true;
      swapPhase = 1;
      swapTargetSlot = null;
      swapOriginalSlot = mc.player.getInventory().selectedSlot;
      swapMode = SwapMode.MOVE;
      swapMoveFromId = from;
      swapMoveToId = to;
      swapUpdateInventory = updateInventory;
      swapLockMoves = moveLock;
      swapWasSprinting = mc.player.isSprinting();
      processSwapPhase();
   }

   public static void swapToOffhandPhased(Slot slot) {
      if (slot != null) {
         legitSwap(slot.id, 40);
      }
   }

   public static boolean legitSwap(int slotId, int button) {
      return legitSwap(slotId, button, mc.player != null && mc.player.isSprinting());
   }

   public static boolean legitSwap(int slotId, int button, boolean restoreSprint) {
      if (mc.player == null || mc.player.currentScreenHandler == null || isSwapping) {
         return false;
      }
      if (slotId < 0 || slotId >= mc.player.currentScreenHandler.slots.size()) {
         return false;
      }
      if (button != 40 && (button < 0 || button > 8)) {
         return false;
      }
      isSwapping = true;
      swapPhase = LEGIT_PHASE_CLICK;
      swapTargetSlot = mc.player.currentScreenHandler.slots.get(slotId);
      swapOriginalSlot = mc.player.getInventory().selectedSlot;
      swapMode = SwapMode.LEGIT;
      swapClickSlotId = slotId;
      swapClickButton = button;
      swapLockMoves = true;
      swapUpdateInventory = true;
      swapWasSprinting = restoreSprint;
      swapWatchdogTicks = 0;
      swapGateTick = mc.player.age;
      processSwapPhase();
      return true;
   }

   public static void processSwapPhase() {
      if (!isSwapping || mc.player == null) {
         return;
      }
      if (swapMode == SwapMode.LEGIT) {
         if (swapPhase == LEGIT_PHASE_FINISH || ++swapWatchdogTicks > LEGIT_WATCHDOG_TICKS) {
            finishLegitSwap();
            return;
         }
         applySwapMoveLock();
         if (mc.player.age <= swapGateTick) {
            return;
         }
         clickSlot(swapClickSlotId, swapClickButton, SlotActionType.SWAP);
         if (swapUpdateInventory) {
            updateSlots();
         }
         swapPhase = LEGIT_PHASE_FINISH;
         return;
      }
      applySwapMoveLock();
      if (waitTicks > 0) {
         waitTicks--;
         return;
      }
      switch (swapPhase) {
         case 1 -> { waitTicks = 1; swapPhase = 2; }
         case 2 -> {
            if (swapMode == SwapMode.MOVE) {
               performMoveSwapStep(1);
            } else if (swapTargetSlot.id < 36 || swapTargetSlot.id > 44) {
               clickSlot(swapTargetSlot.id, swapOriginalSlot, SlotActionType.SWAP);
            } else {
               setSelectedSlotSynced(swapTargetSlot.id - 36);
            }
            swapPhase = 3;
         }
         case 3 -> {
            if (swapMode == SwapMode.MOVE) {
               performMoveSwapStep(2);
            } else if (swapMode == SwapMode.USE) {
               useMainHandItem();
            }
            swapPhase = 4;
         }
         case 4 -> {
            if (swapMode == SwapMode.MOVE) {
               performMoveSwapStep(3);
            } else if (swapMode == SwapMode.USE) {
               setSelectedSlotSynced(swapOriginalSlot);
               if (swapTargetSlot.id < 36 || swapTargetSlot.id > 44) {
                  clickSlot(swapTargetSlot.id, swapOriginalSlot, SlotActionType.SWAP);
               }
            }
            swapPhase = 5;
         }
         case 5 -> { if (swapUpdateInventory) updateSlots(); waitTicks = 1; swapPhase = 6; }
         case 6 -> {
            if (swapUpdateInventory) updateSlots();
            if (swapLockMoves) {
               if (swapLightLock) {
                  closeScreen(true);
                  updateMoveKeys();
               } else {
                  enableMoveKeys();
               }
               resumeSprintAfterSwap(swapWasSprinting);
            }
            resetSwapState();
         }
         default -> { }
      }
   }

   private static void applySwapMoveLock() {
      if (swapPhase >= 6 || !swapLockMoves) {
         return;
      }
      if (swapLightLock) {
         unPressMoveKeys();
         if (mc.player != null) {
            mc.player.setSprinting(false);
         }
         return;
      }
      disableMoveKeys();
      stopSprint();
   }

   private static void finishLegitSwap() {
      if (swapUpdateInventory) {
         updateSlots();
      }
      if (swapLockMoves) {
         enableMoveKeys();
         resumeSprintAfterSwap(swapWasSprinting);
      }
      resetSwapState();
   }

   private static void resetSwapState() {
      isSwapping = false;
      swapPhase = 0;
      swapTargetSlot = null;
      swapOriginalSlot = -1;
      swapLockMoves = true;
      swapLightLock = false;
      swapMode = SwapMode.USE;
      swapMoveFromId = -1;
      swapMoveToId = -1;
      swapClickSlotId = -1;
      swapClickButton = -1;
      swapUpdateInventory = true;
      swapWasSprinting = false;
      swapWatchdogTicks = 0;
      swapGateTick = -1;
      waitTicks = 0;
   }

   private static void performMoveSwapStep(int step) {
      boolean fromHotbar = isHotbarSlotId(swapMoveFromId);
      boolean toHotbar = isHotbarSlotId(swapMoveToId);
      int tempHotbarIndex = swapOriginalSlot;
      if (fromHotbar && toHotbar) {
         if (step == 1) clickSlot(swapMoveFromId, hotbarIndexFromSlotId(swapMoveToId), SlotActionType.SWAP);
      } else if (fromHotbar) {
         if (step == 1) clickSlot(swapMoveToId, hotbarIndexFromSlotId(swapMoveFromId), SlotActionType.SWAP);
      } else if (toHotbar) {
         if (step == 1) clickSlot(swapMoveFromId, hotbarIndexFromSlotId(swapMoveToId), SlotActionType.SWAP);
      } else if (step == 1) {
         clickSlot(swapMoveFromId, tempHotbarIndex, SlotActionType.SWAP);
      } else if (step == 2) {
         clickSlot(swapMoveToId, tempHotbarIndex, SlotActionType.SWAP);
      } else if (step == 3) {
         clickSlot(swapMoveFromId, tempHotbarIndex, SlotActionType.SWAP);
      }
   }

   private static boolean isHotbarSlotId(int slotId) {
      return slotId >= 36 && slotId <= 44;
   }

   private static int hotbarIndexFromSlotId(int slotId) {
      return slotId - 36;
   }

   private static void useMainHandItem() {
      if (mc.player == null || mc.interactionManager == null) {
         return;
      }
      setSelectedSlotSynced(mc.player.getInventory().selectedSlot);
      mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
      mc.player.currentScreenHandler.sendContentUpdates();
   }

   private static void setSelectedSlotSynced(int slot) {
      if (mc.player == null || slot < 0 || slot > 8) {
         return;
      }
      if (mc.player.getInventory().selectedSlot != slot) {
         mc.player.getInventory().selectedSlot = slot;
      }
      if (mc.getNetworkHandler() != null) {
         mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
      }
   }

   public static Slot getSlot(Item item) {
      return slots().filter(s -> s.getStack().getItem().equals(item)).findFirst().orElse(null);
   }

   public static Slot getSlot(Item item, java.util.Comparator<Slot> comparator,
                              java.util.function.Predicate<Slot> filter) {
      return slots().filter(s -> s.getStack().getItem().equals(item)).filter(filter).max(comparator).orElse(null);
   }

   public static java.util.stream.Stream<Slot> slots() {
      if (mc.player == null || mc.player.currentScreenHandler == null) {
         return java.util.stream.Stream.empty();
      }
      return mc.player.currentScreenHandler.slots.stream();
   }

   public static void closeScreen(boolean packet) {
      if (mc.player == null || mc.player.currentScreenHandler == null) {
         return;
      }
      mc.player.currentScreenHandler.sendContentUpdates();
      if (packet && mc.getNetworkHandler() != null) {
         mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(
                 mc.player.currentScreenHandler.syncId));
      } else if (!packet) {
         mc.player.closeHandledScreen();
      }
   }

   public static boolean isMovementBlocked() {
      return !canMove;
   }

   public static void disableMoveKeys() {
      canMove = false;
      unPressMoveKeys();
   }

   public static void enableMoveKeys() {
      closeScreen(true);
      canMove = true;
      updateMoveKeys();
   }

   public static void lightStopSwapOffhand(Slot slot) {
      if (slot == null || mc.player == null || !lightSwapScript.isFinished()) {
         return;
      }
      lightSwapScript.cleanup()
              .addTickStep(0, () -> {
                 unPressMoveKeys();
                 if (mc.player != null) mc.player.setSprinting(false);
              })
              .addTickStep(2, () -> {
                 stopSprint();
                 performSafeHandSwap(slot, Hand.OFF_HAND);
              })
              .addTickStep(2, InventoryUtil::updateMoveKeys);
   }

   public static void defaultSwapOffhand(Slot slot) {
      if (slot == null || mc.player == null || !lightSwapScript.isFinished()) {
         return;
      }
      if (!hasPlayerMovement()) {
         stopSprint();
         performSafeHandSwap(slot, Hand.OFF_HAND);
         return;
      }
      boolean wasSprinting = mc.player.isSprinting();
      lightSwapScript.cleanup()
              .addTickStep(0, () -> {
                 unPressMoveKeys();
                 stopSprint();
              })
              .addTickStep(1, () -> {
                 performSafeHandSwap(slot, Hand.OFF_HAND);
                 updateMoveKeys();
                 resumeSprintAfterSwap(wasSprinting);
              });
   }

   public static void stopSprint() {
      if (mc.player == null || !mc.player.isSprinting()) {
         return;
      }
      mc.player.setSprinting(false);
      if (mc.options != null) {
         mc.options.sprintKey.setPressed(false);
      }
   }

   public static void resumeSprintFromPhysical() {
      if (mc.options == null || mc.getWindow() == null) {
         return;
      }
      KeyBinding sprint = mc.options.sprintKey;
      int code = InputUtil.fromTranslationKey(sprint.getBoundKeyTranslationKey()).getCode();
      sprint.setPressed(code >= 0 && InputUtil.isKeyPressed(mc.getWindow().getHandle(), code));
   }

   public static void resumeSprintAfterSwap(boolean wasSprinting) {
      resumeSprintFromPhysical();
      if (!wasSprinting || mc.player == null || mc.options == null || mc.player.isSprinting()) {
         return;
      }
      if (canResumeSprint()) {
         mc.player.setSprinting(true);
      }
   }

   private static boolean canResumeSprint() {
      if (mc.player.isSpectator() || mc.player.isSneaking() || mc.player.isUsingItem()) {
         return false;
      }
      if (!mc.player.input.playerInput.forward() && !mc.options.forwardKey.isPressed()) {
         return false;
      }
      return mc.player.getHungerManager().getFoodLevel() > 6 || mc.player.getAbilities().flying;
   }

   private static void performSafeHandSwap(Slot slot, Hand hand) {
      if (mc.player == null || mc.getNetworkHandler() == null || mc.player.currentScreenHandler == null
              || mc.player.currentScreenHandler.syncId != 0) {
         return;
      }
      if (mc.currentScreen == null) {
         mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(
                 mc.player, ClientCommandC2SPacket.Mode.OPEN_INVENTORY));
         swapHand(slot, hand, false);
         closeScreen(true);
      } else {
         swapHand(slot, hand, false);
      }
   }

   public static boolean hasPlayerMovementInput() {
      return hasPlayerMovement();
   }

   public static PlayerEntity player() {
      return mc.player;
   }

   public static final class LightSwapScript {
      private final java.util.List<ScheduledStep> steps = new java.util.ArrayList<>();

      public LightSwapScript cleanup() {
         steps.clear();
         return this;
      }

      public LightSwapScript addTickStep(int ticks, Runnable action) {
         steps.add(new ScheduledStep(Math.max(0, ticks), action));
         return this;
      }

      public boolean isFinished() {
         return steps.isEmpty();
      }

      public void update() {
         for (int i = 0; i < steps.size(); i++) {
            ScheduledStep step = steps.get(i);
            if (step.ticks > 0) {
               step.ticks--;
               continue;
            }
            steps.remove(i--);
            try {
               step.action.run();
            } catch (RuntimeException ignored) {
            }
         }
      }

      private static final class ScheduledStep {
         private int ticks;
         private final Runnable action;

         private ScheduledStep(int ticks, Runnable action) {
            this.ticks = ticks;
            this.action = action;
         }
      }
   }

   private enum SwapMode {
      USE, MOVE, LEGIT
   }

   public static boolean isServerScreen() {
      return mc.player != null
              && mc.player.currentScreenHandler != mc.player.playerScreenHandler;
   }

   public static boolean isPlayerInventory() {
      return mc.player != null
              && mc.player.currentScreenHandler == mc.player.playerScreenHandler;
   }

   public static boolean hasPlayerMovement() {
      if (mc.player == null || mc.options == null) {
         return false;
      }
      if (mc.player.isSprinting()) {
         return true;
      }
      for (KeyBinding key : moveKeys()) {
         int code = InputUtil.fromTranslationKey(key.getBoundKeyTranslationKey()).getCode();
         if (code >= 0 && InputUtil.isKeyPressed(mc.getWindow().getHandle(), code)) {
            return true;
         }
      }
      return false;
   }

   public static void unPressMoveKeys() {
      for (KeyBinding key : moveKeys()) {
         key.setPressed(false);
      }
   }

   public static void updateMoveKeys() {
      if (mc.getWindow() == null) {
         return;
      }
      for (KeyBinding key : moveKeys()) {
         int code = InputUtil.fromTranslationKey(key.getBoundKeyTranslationKey()).getCode();
         key.setPressed(code >= 0 && InputUtil.isKeyPressed(mc.getWindow().getHandle(), code));
      }
   }

   public static void updateSlots() {
      if (mc.player != null && mc.player.currentScreenHandler != null) {
         mc.player.currentScreenHandler.sendContentUpdates();
      }
   }

   private static KeyBinding[] moveKeys() {
      if (moveKeysCache == null) {
         moveKeysCache = new KeyBinding[]{
                 mc.options.forwardKey,
                 mc.options.backKey,
                 mc.options.leftKey,
                 mc.options.rightKey,
                 mc.options.jumpKey
         };
      }
      return moveKeysCache;
   }

   public static int findItem(Item item) {
      return findItem(item, 0, 35);
   }

   public static int findHotbar(Item item) {
      return findItem(item, 0, 8);
   }

   public static int findInventory(Item item) {
      return findItem(item, 9, 35);
   }

   public static int findItem(Item item, int start, int end) {
      for(int i = end; i >= start; --i) {
         if (mc.player.getInventory().getStack(i).getItem() == item) {
            return i;
         }
      }

      return -1;
   }

   public static int findEmptySlot(int start, int end) {
      for(int i = end; i >= start; --i) {
         if (mc.player.getInventory().getStack(i).isEmpty()) {
            return i;
         }
      }

      return -1;
   }

   public static void switchSlot(Switch mode, int slot, int previousSlot) {
      if (slot != -1 && previousSlot != -1) {
         switch(mode.ordinal()) {
         case 0:
            mc.player.getInventory().selectedSlot = slot;
            break;
         case 1:
            mc.player.getInventory().selectedSlot = slot;
            NetworkUtils.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
            break;
         case 2:
            swapItems(slot, previousSlot);
         }

      }
   }

   public static void switchBack(Switch mode, int slot, int previousSlot) {
      if (slot != -1 && previousSlot != -1) {
         switch(mode.ordinal()) {
         case 0:
            mc.player.getInventory().selectedSlot = previousSlot;
            break;
         case 1:
            mc.player.getInventory().selectedSlot = previousSlot;
            NetworkUtils.sendPacket(new UpdateSelectedSlotC2SPacket(previousSlot));
            break;
         case 2:
            swapItems(slot, previousSlot);
         }

      }
   }

   public static void swapItems(int slot, int targetSlot) {
      if (slot != -1 && targetSlot != -1) {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, targetSlot, 0, SlotActionType.PICKUP, mc.player);
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, mc.player);
      }
   }

   public static void swap(int slot, int targetSlot) {
      if (slot != -1 && targetSlot != -1) {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, indexToSlot(slot), 0, SlotActionType.PICKUP, mc.player);
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, indexToSlot(targetSlot), 0, SlotActionType.PICKUP, mc.player);
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, indexToSlot(slot), 0, SlotActionType.PICKUP, mc.player);
      }
   }

   public static void bypassSwap(int slot, int targetSlot) {
      if (slot != -1 && targetSlot != -1) {
         swap(slot, targetSlot);
      }
   }

   public static int indexToSlot(int index) {
      return index >= 0 && index <= 8 ? 36 + index : index;
   }

   public static void swing(Swing mode) {
      switch(mode.ordinal()) {
      case 0:
         mc.player.swingHand(Hand.MAIN_HAND);
         break;
      case 1:
         mc.player.swingHand(Hand.OFF_HAND);
         break;
      case 2:
         NetworkUtils.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
      }

   }

   public static enum Switch {
      Normal,
      Silent,
      Alternative,
      None;

      private static Switch[] $values() {
         return new Switch[]{Normal, Silent, Alternative, None};
      }
   }

   public static enum Swing {
      MainHand,
      OffHand,
      Packet,
      None;

      private static Swing[] $values() {
         return new Swing[]{MainHand, OffHand, Packet, None};
      }
   }
}
