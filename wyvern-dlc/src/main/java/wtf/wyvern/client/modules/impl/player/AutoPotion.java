package wtf.wyvern.client.modules.impl.player;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.astroguard.J2C.FastNative;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@ModuleAnnotation(name = "AutoPotion", category = Category.PLAYER, description = "Автоматически кидает зелья под себя")
public final class AutoPotion extends Module {
   private static final float THROW_PITCH = 90.0F;
   private static final int POST_BATCH_COOLDOWN = 10;
   private static final List<PotionTarget> TARGETS = List.of(
      new PotionTarget("Скорость", StatusEffects.SPEED),
      new PotionTarget("Сила", StatusEffects.STRENGTH),
      new PotionTarget("Огнестойкость", StatusEffects.FIRE_RESISTANCE)
   );

   public static final AutoPotion INSTANCE = new AutoPotion();
   private final MultiBooleanSetting potions = new MultiBooleanSetting("Зелья",
      new MultiBooleanSetting.Value("Скорость", true), new MultiBooleanSetting.Value("Сила", true), new MultiBooleanSetting.Value("Огнестойкость", true));
   private final Set<RegistryEntry<StatusEffect>> attemptedEffects = new HashSet<>();
   private boolean batchActive;
   private float restorePitch;
   private int cooldown;

   @EventTarget
   public void onTick(EventTick event) {
      if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.getNetworkHandler() == null) {
         resetState(false);
         return;
      }

      if (mc.currentScreen != null || mc.player.isUsingItem()) {
         if (batchActive) {
            finishBatch();
         }
         return;
      }

      if (cooldown > 0) {
         cooldown--;
         return;
      }

      if (!batchActive) {
         if (!hasPotionToThrow()) {
            return;
         }
         batchActive = true;
         restorePitch = mc.player.getPitch();
         attemptedEffects.clear();
      }

      if (!throwNextPotion()) {
         finishBatch();
      }
   }

   private boolean hasPotionToThrow() {
      for (PotionTarget target : TARGETS) {
         if (shouldThrow(target) && findPotionSlot(target.effect()) != -1) {
            return true;
         }
      }
      return false;
   }

   private boolean throwNextPotion() {
      for (PotionTarget target : TARGETS) {
         if (!shouldThrow(target) || attemptedEffects.contains(target.effect())) {
            continue;
         }

         int inventorySlot = findPotionSlot(target.effect());
         if (inventorySlot == -1) {
            continue;
         }

         attemptedEffects.add(target.effect());
         if (throwPotion(inventorySlot)) {
            return true;
         }
      }
      return false;
   }

   private boolean shouldThrow(PotionTarget target) {
      return potions.isEnable(target.setting()) && !mc.player.hasStatusEffect(target.effect());
   }

   private int findPotionSlot(RegistryEntry<StatusEffect> effect) {
      for (int slot = 0; slot < 36; slot++) {
         ItemStack stack = mc.player.getInventory().getStack(slot);
         if (!stack.isOf(Items.SPLASH_POTION)) {
            continue;
         }

         PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
         if (contents != null && StreamSupport.stream(contents.getEffects().spliterator(), false)
            .anyMatch(instance -> instance.getEffectType().equals(effect))) {
            return slot;
         }
      }
      return -1;
   }

   private boolean throwPotion(int inventorySlot) {
      int previousHotbarSlot = mc.player.getInventory().selectedSlot;
      boolean inventorySwap = inventorySlot >= 9;

      if (inventorySwap) {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, inventorySlot,
            previousHotbarSlot, SlotActionType.SWAP, mc.player);
      } else {
         mc.player.getInventory().selectedSlot = inventorySlot;
         mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(inventorySlot));
      }

      if (!mc.player.getMainHandStack().isOf(Items.SPLASH_POTION)) {
         restoreSlot(inventorySlot, previousHotbarSlot, inventorySwap);
         return false;
      }

      // The look packet is deliberately sent immediately before the use packet:
      // the server then always sees the splash potion aimed at the player's feet.
      pointCameraDown();
      mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
      restoreSlot(inventorySlot, previousHotbarSlot, inventorySwap);
      return true;
   }

   private void restoreSlot(int inventorySlot, int previousHotbarSlot, boolean inventorySwap) {
      if (inventorySwap) {
         mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, inventorySlot,
            previousHotbarSlot, SlotActionType.SWAP, mc.player);
      } else {
         mc.player.getInventory().selectedSlot = previousHotbarSlot;
         mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(previousHotbarSlot));
      }
   }

   private void pointCameraDown() {
      mc.player.setPitch(THROW_PITCH);
      mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
         mc.player.getYaw(), THROW_PITCH, mc.player.isOnGround(), mc.player.horizontalCollision));
   }

   private void finishBatch() {
      if (mc.player != null && mc.getNetworkHandler() != null) {
         mc.player.setPitch(restorePitch);
         mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
            mc.player.getYaw(), restorePitch, mc.player.isOnGround(), mc.player.horizontalCollision));
      }
      batchActive = false;
      attemptedEffects.clear();
      cooldown = POST_BATCH_COOLDOWN;
   }

   private void resetState(boolean restoreCamera) {
      if (restoreCamera && batchActive) {
         finishBatch();
      } else {
         batchActive = false;
         attemptedEffects.clear();
         cooldown = 0;
      }
   }

   @FastNative
   @Override
   public void onEnable() {
      resetState(false);
      super.onEnable();
   }

   @FastNative
   @Override
   public void onDisable() {
      resetState(true);
      super.onDisable();
   }

   private record PotionTarget(String setting, RegistryEntry<StatusEffect> effect) {
   }
}
