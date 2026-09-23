package wtf.wyvern.client.modules.impl.misc;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.Hand;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.core.events.impl.other.EventTickMovement;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.client.modules.impl.movement.AutoSprint;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.server.ServerHandler;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "RWHelper",
   category = Category.MISC,
   description = "Помощник для сервера ReallyWorld"
)
@FastNative
public final class ServerHelper extends Module {
   public static final ServerHelper INSTANCE = new ServerHelper();
   private final BindSetting antiFly = new BindSetting("Клавиша использования анти-полета", -1);
   private final BindSetting trap = new BindSetting("Клавиша использования трапки", -1);
   private final BooleanSetting fixAfterPvp = new BooleanSetting("/fix после ПВП", false);
   private boolean useAntiFly;
   private boolean wasPvp;
   private boolean useTrap;
   private int trapRestoreTicks;
   private int trapOriginalSlot = -1;
   private int trapSourceSlot = -1;
   private boolean trapInventorySwap;

   @EventTarget
   private void onKey(EventKey e) {
      if (mc.currentScreen == null) {
         if (e.getAction() == 1) {
            if (e.getKeyCode() == this.antiFly.getKeyCode()) {
               this.useAntiFly = true;
            }
            if (e.getKeyCode() == this.trap.getKeyCode()) {
               this.useTrap = true;
            }

         }
      }
   }

   @EventTarget
   private void onTick(EventTickMovement e) {
      this.handleFixAfterPvp();
      if (this.useTrap) {
         this.useTrap = false;
         useTrapItem();
      }
      if (trapRestoreTicks > 0 && --trapRestoreTicks == 0) {
         restoreTrapItem();
      }
      if (this.useAntiFly) {
         this.useAntiFly = false;
         int slot = PlayerInventoryUtil.find((Item)Items.FIREWORK_STAR, 9, 45);
         int slotHotbar = PlayerInventoryUtil.find((Item)Items.FIREWORK_STAR, 0, 8);
         if (mc.player.getOffHandStack().getItem() == Items.FIREWORK_STAR) {
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
         } else {
            boolean wasSprinting;
            if (slotHotbar != -1) {
               wasSprinting = false;
               if (mc.player.isSprinting()) {
                  mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, false, false, false)));
                  mc.player.setSprinting(false);
                  mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
                  if (!AutoSprint.INSTANCE.isEnabled()) {
                     mc.options.sprintKey.setPressed(false);
                  }

                  wasSprinting = true;
               }

               mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, slotHotbar, SlotActionType.SWAP, mc.player);
               mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(0));
               mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
               mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
               mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, slotHotbar, SlotActionType.SWAP, mc.player);
               mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(0));
               if (wasSprinting) {
                  mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(mc.player.input.playerInput));
               }
            }

            if (slotHotbar == -1 && slot != -1) {
               wasSprinting = false;
               if (mc.player.isSprinting()) {
                  mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(new PlayerInput(false, false, false, false, false, false, false)));
                  mc.player.setSprinting(false);
                  mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
                  if (!AutoSprint.INSTANCE.isEnabled()) {
                     mc.options.sprintKey.setPressed(false);
                  }

                  wasSprinting = true;
               }

               mc.interactionManager.clickSlot(0, slot, 40, SlotActionType.SWAP, mc.player);
               mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(0));
               mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
               mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
               mc.interactionManager.clickSlot(0, slot, 40, SlotActionType.SWAP, mc.player);
               mc.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(0));
               if (wasSprinting) {
                  mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(mc.player.input.playerInput));
               }
            }

         }
      }
   }

   /** Uses the HolyWorld-style Heart of the Sea trap without losing the item
    * or changing the player's selected slot after the short use window. */
   private void useTrapItem() {
      if (mc.player == null || mc.interactionManager == null || mc.currentScreen != null
              || trapRestoreTicks > 0) return;

      if (mc.player.getOffHandStack().isOf(Items.HEART_OF_THE_SEA)) {
         mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
         mc.player.swingHand(Hand.OFF_HAND);
         return;
      }

      int selected = mc.player.getInventory().selectedSlot;
      int hotbar = -1;
      for (int i = 0; i < 9; i++) {
         if (mc.player.getInventory().getStack(i).isOf(Items.HEART_OF_THE_SEA)) {
            hotbar = i;
            break;
         }
      }
      if (hotbar >= 0) {
         trapOriginalSlot = selected;
         mc.player.getInventory().selectedSlot = hotbar;
         if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket(hotbar));
         }
         mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
         mc.player.swingHand(Hand.MAIN_HAND);
         trapRestoreTicks = 2;
         return;
      }

      for (int slot = 9; slot < 36; slot++) {
         if (mc.player.getInventory().getStack(slot).isOf(Items.HEART_OF_THE_SEA)) {
            trapOriginalSlot = selected;
            trapSourceSlot = slot;
            trapInventorySwap = true;
            mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, slot,
                    selected, SlotActionType.SWAP, mc.player);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);
            trapRestoreTicks = 2;
            return;
         }
      }
   }

   private void restoreTrapItem() {
      if (mc.player == null || mc.interactionManager == null) {
         trapOriginalSlot = trapSourceSlot = -1;
         trapInventorySwap = false;
         return;
      }
      if (trapInventorySwap && trapSourceSlot >= 0) {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, trapSourceSlot,
                 trapOriginalSlot, SlotActionType.SWAP, mc.player);
      }
      if (trapOriginalSlot >= 0 && mc.player.getInventory().selectedSlot != trapOriginalSlot) {
         mc.player.getInventory().selectedSlot = trapOriginalSlot;
         if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket(trapOriginalSlot));
         }
      }
      trapOriginalSlot = trapSourceSlot = -1;
      trapInventorySwap = false;
   }

   private void handleFixAfterPvp() {
      if (!this.fixAfterPvp.isEnabled() || mc.player == null || mc.getNetworkHandler() == null) {
         this.wasPvp = false;
         return;
      }

      ServerHandler serverHandler = Wyvern.getInstance().getServerHandler();
      if (serverHandler == null) {
         this.wasPvp = false;
         return;
      }

      boolean inPvp = serverHandler.isPvp();
      if (this.wasPvp && !inPvp) {
         mc.getNetworkHandler().sendChatCommand("fix all");
      }

      this.wasPvp = inPvp;
   }
}
