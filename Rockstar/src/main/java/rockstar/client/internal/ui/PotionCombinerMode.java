package rockstar.client.internal.ui;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.inventory.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.util.function.Predicate;
import net.minecraft.block.AnvilBlock;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PotionCombinerMode extends AutoFarmModeState {
   private final ModeSetting internalField0668;
   private final ModeSetting.InternalType0088 internalField0237;
   private final ModeSetting.InternalType0088 internalField0238;
   private final ModeSetting.InternalType0088 internalField1066;
   private final BooleanSetting internalField0650;
   private final BooleanSetting internalField0651;
   private final SliderSetting internalField0383;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private final Stopwatch internalField1189 = new Stopwatch();
   private String internalField0248 = "";
   private boolean internalField0277;
   private int internalField1053;

   public PotionCombinerMode(AutoFarmModule localValue1, ModeSetting localValue2) {
      super(localValue1, localValue2, "modules.settings.auto_farm.modes.potion_combiner");
      this.internalField0668 = new ModeSetting(localValue1, "modules.settings.potion_combiner.potions", () -> !this.isSelected());
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_combiner.potion.strength").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_combiner.potion.speed");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.potion_combiner.potion.strength_speed");
      this.internalField0650 = new BooleanSetting(localValue1, "modules.settings.potion_combiner.auto_open", () -> !this.isSelected()).internalMethod06630();
      this.internalField0651 = new BooleanSetting(localValue1, "modules.settings.potion_combiner.auto_exp", () -> !this.isSelected());
      this.internalField0383 = new SliderSetting(
            localValue1, "modules.settings.potion_combiner.refill_to", () -> !this.isSelected() || !this.internalField0651.internalMethod04496()
         )
         .internalMethod05900(5.0F)
         .internalMethod02732(100.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(40.0F);
   }

   @Override
   public void internalMethod04697() {
      this.internalField0248 = "";
      this.internalField0277 = false;
      this.internalField1053 = 0;
   }

   @Override
   public void internalMethod08382() {
      if (internalField0149.player != null && internalField0149.world != null && internalField0149.interactionManager != null) {
         this.internalMethod08252();
         this.internalMethod08265();
         this.internalMethod09792();
      }
   }

   @Override
   public AutoFarmPhase internalMethod02315() {
      if (this.internalField0277) {
         return AutoFarmPhase.internalField1637;
      } else {
         return internalField0149.currentScreen instanceof AnvilScreen ? AutoFarmPhase.internalField1635 : AutoFarmPhase.internalField0848;
      }
   }

   @Override
   public AutoFarmUnit internalMethod02317() {
      return AutoFarmUnit.internalField0852;
   }

   @Override
   public ItemStack internalMethod04126() {
      return new ItemStack(Items.ANVIL);
   }

   private void internalMethod08252() {
      if (!this.internalField0277) {
         if (internalField0149.currentScreen instanceof AnvilScreen localValue1) {
            AnvilScreenHandler localValue4 = (AnvilScreenHandler)localValue1.getScreenHandler();
            this.internalMethod04230(localValue4);
            if (this.internalMethod04231(localValue4)) {
               int localValue3 = localValue4.getLevelCost();
               this.internalField1053 = localValue3;
               this.internalMethod09793();
               if (localValue3 > 0 && internalField0149.player.experienceLevel >= localValue3 && !localValue4.getSlot(2).getStack().isEmpty()) {
                  this.internalMethod06871(localValue4);
               }
            }
         } else {
            this.internalField0248 = "";
         }
      }
   }

   private void internalMethod08265() {
      if (this.internalField0650.internalMethod04496()) {
         if (!this.internalField0277) {
            if (internalField0149.currentScreen == null) {
               if (this.internalField1189.internalMethod02365(500L)) {
                  if (this.internalMethod07232()) {
                     BlockPos localValue1 = this.internalMethod06275();
                     if (localValue1 != null) {
                        Vec3d localValue2 = new Vec3d(localValue1.getX() + 0.5, localValue1.getY() + 0.5, localValue1.getZ() + 0.5);
                        BlockHitResult localValue3 = new BlockHitResult(localValue2, Direction.UP, localValue1, false);
                        internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue3);
                        this.internalField1189.internalMethod00701();
                     }
                  }
               }
            }
         }
      }
   }

   private boolean internalMethod07232() {
      if (this.internalField1066.isSelected()) {
         boolean localValue5 = false;
         boolean localValue2 = false;

         for (int localValue3 = 0; localValue3 < internalField0149.player.getInventory().size(); localValue3++) {
            ItemStack localValue4 = internalField0149.player.getInventory().getStack(localValue3);
            if (this.internalMethod03034(localValue4, 0)) {
               localValue5 = true;
            }

            if (this.internalMethod03034(localValue4, 1)) {
               localValue2 = true;
            }

            if (localValue5 && localValue2) {
               return true;
            }
         }

         return false;
      } else {
         for (int localValue1 = 0; localValue1 < internalField0149.player.getInventory().size(); localValue1++) {
            if (this.internalMethod03034(internalField0149.player.getInventory().getStack(localValue1), 0)) {
               return true;
            }
         }

         return false;
      }
   }

   private BlockPos internalMethod06275() {
      BlockPos localValue1 = BlockPos.ofFloored(internalField0149.player.getEntityPos());
      BlockPos localValue2 = null;
      double localValue3 = Double.MAX_VALUE;
      double localValue5 = internalField0149.player.getBlockInteractionRange();

      for (int localValue7 = -5; localValue7 <= 5; localValue7++) {
         for (int localValue8 = -5; localValue8 <= 5; localValue8++) {
            for (int localValue9 = -5; localValue9 <= 5; localValue9++) {
               BlockPos localValue10 = localValue1.add(localValue7, localValue8, localValue9);
               if (internalField0149.world.getBlockState(localValue10).getBlock() instanceof AnvilBlock) {
                  double localValue11 = internalField0149.player.getEyePos().squaredDistanceTo(localValue10.getX() + 0.5, localValue10.getY() + 0.5, localValue10.getZ() + 0.5);
                  if (!(localValue11 > localValue5 * localValue5) && localValue11 < localValue3) {
                     localValue3 = localValue11;
                     localValue2 = localValue10;
                  }
               }
            }
         }
      }

      return localValue2;
   }

   private void internalMethod09792() {
      if (!this.internalField0651.internalMethod04496()) {
         this.internalField0277 = false;
      } else {
         int localValue1 = Math.max((int)this.internalField0383.internalMethod08576(), this.internalField1053);
         int localValue2 = Math.max(5, this.internalField1053);
         if (!this.internalField0277 && internalField0149.player.experienceLevel < localValue2 && this.internalMethod06719() != null) {
            this.internalField0277 = true;
         }

         if (this.internalField0277) {
            if (internalField0149.currentScreen instanceof AnvilScreen) {
               internalField0149.player.closeHandledScreen();
            } else if (internalField0149.player.getMainHandStack().getItem() != Items.EXPERIENCE_BOTTLE) {
               InventorySlot localValue3 = this.internalMethod06719();
               if (localValue3 == null) {
                  this.internalField0277 = false;
               } else {
                  int localValue4 = internalField0149.player.getInventory().getSelectedSlot();
                  if (localValue3 instanceof HotbarSlot localValue5) {
                     if (localValue5.internalMethod08745() != localValue4) {
                        InventoryUtils.internalMethod03663(localValue5.internalMethod08745());
                     }
                  } else {
                     InventoryUtils.internalMethod08821(localValue3.internalMethod06662(), localValue4);
                  }
               }
            } else {
               RockstarClient.getInstance()
                  .internalMethod02368()
                  .internalMethod00418(
                     new Rotation(internalField0149.player.getYaw(), 87.0F),
                     RotationBehavior.internalField0114,
                     180.0F,
                     180.0F,
                     180.0F,
                     RotationPriority.internalField0122
                  );
               if (this.internalField0518.internalMethod02365(100L)) {
                  internalField0149.interactionManager
                     .sendSequencedPacket(
                        internalField0149.world, localValue0 -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, localValue0, internalField0149.player.getYaw(), 87.0F)
                     );
                  internalField0149.player.swingHand(Hand.MAIN_HAND);
                  this.internalField0518.internalMethod00701();
                  if (internalField0149.player.experienceLevel >= localValue1) {
                     this.internalField0277 = false;
                  }
               }
            }
         }
      }
   }

   private boolean internalMethod04231(AnvilScreenHandler localValue1) {
      return this.internalMethod03034(localValue1.getSlot(0).getStack(), 0) && this.internalMethod03034(localValue1.getSlot(1).getStack(), 1);
   }

   private void internalMethod04230(AnvilScreenHandler localValue1) {
      if (this.internalField0519.internalMethod02365(300L)) {
         for (int localValue2 = 0; localValue2 < 2; localValue2++) {
            if (!this.internalMethod03034(localValue1.getSlot(localValue2).getStack(), localValue2)) {
               if (!localValue1.getSlot(localValue2).getStack().isEmpty()) {
                  internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                  this.internalField0519.internalMethod00701();
                  return;
               }

               int localValue3 = this.internalMethod04054(localValue1, localValue2);
               if (localValue3 == -1) {
                  return;
               }

               this.internalMethod00152(localValue1, localValue3, localValue2);
               this.internalField0519.internalMethod00701();
               return;
            }
         }
      }
   }

   private int internalMethod04054(ScreenHandler localValue1, int localValue2) {
      for (int localValue3 = 3; localValue3 < localValue1.slots.size(); localValue3++) {
         ItemStack localValue4 = localValue1.getSlot(localValue3).getStack();
         if (this.internalMethod03034(localValue4, localValue2)) {
            return localValue3;
         }
      }

      return -1;
   }

   private void internalMethod00152(ScreenHandler localValue1, int localValue2, int localValue3) {
      internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
      internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue3, 1, SlotActionType.PICKUP, internalField0149.player);
      if (!internalField0149.player.currentScreenHandler.getCursorStack().isEmpty()) {
         internalField0149.interactionManager.clickSlot(localValue1.syncId, localValue2, 0, SlotActionType.PICKUP, internalField0149.player);
      }
   }

   private void internalMethod06871(AnvilScreenHandler localValue1) {
      if (this.internalField0519.internalMethod02365(120L)) {
         if (!localValue1.getSlot(2).getStack().isEmpty()) {
            int localValue2 = Math.max(1, localValue1.getSlot(2).getStack().getCount());
            internalField0149.interactionManager.clickSlot(localValue1.syncId, 2, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
            this.internalMethod00320().internalMethod04318(localValue2);
            this.internalField0248 = "";
            this.internalField0519.internalMethod00701();
         }
      }
   }

   private void internalMethod09793() {
      if (this.internalField0248.length() >= 50) {
         this.internalField0248 = "";
      }

      this.internalField0248 = this.internalField0248 + "!";
      if (internalField0149.getNetworkHandler() != null) {
         internalField0149.getNetworkHandler().sendPacket(new RenameItemC2SPacket(this.internalField0248));
      }
   }

   private boolean internalMethod03034(ItemStack localValue1, int localValue2) {
      if (!localValue1.isEmpty() && localValue1.getItem() instanceof PotionItem) {
         for (StatusEffectInstance localValue4 : PotionItemUtils.internalMethod00984(localValue1)) {
            RegistryEntry localValue5 = localValue4.getEffectType();
            int localValue6 = localValue4.getAmplifier();
            if (this.internalField1066.isSelected()) {
               if (localValue6 == 2) {
                  if (localValue2 == 0 && localValue5 == StatusEffects.STRENGTH) {
                     return true;
                  }

                  if (localValue2 == 1 && localValue5 == StatusEffects.SPEED) {
                     return true;
                  }
               }
            } else if (localValue6 < 2) {
               if (this.internalField0237.isSelected() && localValue5 == StatusEffects.STRENGTH) {
                  return true;
               }

               if (this.internalField0238.isSelected() && localValue5 == StatusEffects.SPEED) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private InventorySlot internalMethod06719() {
      Predicate localValue1 = localValue0 -> ((net.minecraft.item.ItemStack)localValue0).getItem() == Items.EXPERIENCE_BOTTLE;
      InventorySlot localValue2 = InventorySlots.internalMethod02872().internalMethod03297(localValue1);
      return (InventorySlot)(localValue2 != null ? localValue2 : InventorySlots.internalMethod03558().internalMethod03297(localValue1));
   }
}
