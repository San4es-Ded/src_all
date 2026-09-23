package rockstar.modules.player;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.gui.screen.ingame.BrewingStandScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;

@ModuleInfo(
   name = "Auto Brew",
   category = ModuleCategory.PLAYER,
   internalMethod09633 = "modules.descriptions.auto_brew"
)
public class AutoBrewModule extends Module {
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private BooleanSetting internalField0650;
   private SliderSetting internalField0383;
   private final Stopwatch internalField0519 = new Stopwatch();
   private AutoBrewModule.InternalType0381 internalField0514;
   private final Stopwatch internalField0518;
   private BrewingStandBlockEntity internalField0751;
   private ChestBlockEntity internalField0132;
   private final List<BlockPos> internalField0416;
   private List<BrewingStandBlockEntity> internalField0417;
   private static final Item[] internalField0838 = new Item[]{
      Items.NETHER_WART,
      Items.BLAZE_POWDER,
      Items.SUGAR,
      Items.MAGMA_CREAM,
      Items.GLOWSTONE_DUST,
      Items.REDSTONE,
      Items.GOLDEN_CARROT,
      Items.FERMENTED_SPIDER_EYE
   };
   private final EventListener<ClientPlayerTickEvent> internalField0157;

   public AutoBrewModule() {
      this.internalField0514 = AutoBrewModule.InternalType0381.internalField0514;
      this.internalField0518 = new Stopwatch();
      this.internalField0416 = new ArrayList<>();
      this.internalField0417 = new ArrayList<>();
      this.internalField0157 = localValue1 -> {
         switch (this.internalField0514) {
            case internalField0514:
               this.internalMethod09659();
               break;
            case internalField0513:
               this.internalMethod09511();
               break;
            case internalField1183:
               this.internalMethod09658();
               break;
            case internalField1184:
               this.internalMethod09668();
               break;
            case internalField1185:
               this.internalMethod09669();
         }
      };
      this.internalMethod09510();
   }

   private void internalMethod09510() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.auto_brew.brew");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_brew.potion.strength").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_brew.potion.speed");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_brew.potion.fire_resistance");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.auto_brew.potion.invisibility");
      this.internalField0650 = new BooleanSetting(this, "modules.settings.auto_brew.enhance", () -> !this.internalField1067.isSelected());
      this.internalField0383 = new SliderSetting(this, "modules.settings.auto_brew.delay", "modules.settings.auto_brew.delay.description")
         .internalMethod08673(10.0F)
         .internalMethod05900(100.0F)
         .internalMethod02732(1000.0F)
         .internalMethod08074(100.0F);
   }

   private void internalMethod09511() {
      if (internalField0149.currentScreen instanceof BrewingStandScreen) {
         this.internalField0514 = AutoBrewModule.InternalType0381.internalField1183;
      } else {
         if (this.internalField0518.internalMethod02365(500L)) {
            BlockPos localValue1 = this.internalField0751.getPos();
            Vec3d localValue2 = new Vec3d(localValue1.getX() + 0.5, localValue1.getY() + 0.5, localValue1.getZ() + 0.5);
            BlockHitResult localValue3 = new BlockHitResult(localValue2, Direction.UP, localValue1, false);
            internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue3);
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod09658() {
      if (!(internalField0149.player.currentScreenHandler instanceof BrewingStandScreenHandler localValue1)) {
         this.internalField0514 = AutoBrewModule.InternalType0381.internalField0514;
      } else if (localValue1.getFuel() <= 0 || localValue1.getSlot(3).getStack().getItem() == Items.AIR) {
         if (localValue1.getSlot(4).getStack().getItem() == Items.AIR && localValue1.getFuel() == 0) {
            if (this.internalMethod05100(Items.BLAZE_POWDER) == -1) {
               return;
            }

            this.internalMethod00412(Items.BLAZE_POWDER, 4);
         }

         for (int localValue3 = 0; localValue3 < 3; localValue3++) {
            if (localValue1.getSlot(localValue3).getStack().getItem() == Items.AIR) {
               if (this.internalMethod05509(localValue1) == -1) {
                  return;
               }

               InventoryUtils.internalMethod03592(this.internalMethod05509(localValue1));
            }
         }

         if (localValue1.getSlot(3).getStack().getItem() == Items.AIR) {
            if (this.internalMethod06254(localValue1, (Potion)Potions.WATER.value())) {
               if (this.internalMethod05100(Items.NETHER_WART) == -1) {
                  RockstarClient.getInstance()
                     .internalMethod02503()
                     .internalMethod00599(
                        NotificationType.internalField0705,
                        LanguageManager.internalMethod07214("autobrew.item_not_found"),
                        LanguageManager.internalMethod00160("autobrew.need_item", Items.NETHER_WART.getName().getString())
                     );
               }

               this.internalMethod02167(Items.NETHER_WART, 3);
            }

            if (this.internalField0237.isSelected() && this.internalMethod06254(localValue1, (Potion)Potions.AWKWARD.value())) {
               this.internalMethod02167(Items.BLAZE_POWDER, 3);
            } else if (this.internalField0238.isSelected() && this.internalMethod06254(localValue1, (Potion)Potions.AWKWARD.value())) {
               this.internalMethod02167(Items.SUGAR, 3);
            } else if (this.internalField1066.isSelected() && this.internalMethod06254(localValue1, (Potion)Potions.AWKWARD.value())) {
               this.internalMethod02167(Items.MAGMA_CREAM, 3);
            } else if (this.internalField1067.isSelected() && this.internalMethod06254(localValue1, (Potion)Potions.AWKWARD.value())) {
               this.internalMethod02167(Items.GOLDEN_CARROT, 3);
            }

            if (this.internalMethod06254(localValue1, (Potion)Potions.STRENGTH.value()) || this.internalMethod06254(localValue1, (Potion)Potions.SWIFTNESS.value())) {
               this.internalMethod02167(Items.GLOWSTONE_DUST, 3);
            }

            if (this.internalMethod06254(localValue1, (Potion)Potions.FIRE_RESISTANCE.value())) {
               this.internalMethod02167(Items.REDSTONE, 3);
            }

            if (this.internalField1067.isSelected() && this.internalMethod06254(localValue1, (Potion)Potions.NIGHT_VISION.value())) {
               this.internalMethod02167(Items.FERMENTED_SPIDER_EYE, 3);
            }

            if (this.internalField1067.isSelected()
               && this.internalMethod06254(localValue1, (Potion)Potions.INVISIBILITY.value())
               && this.internalField0650.internalMethod04496()) {
               this.internalMethod02167(Items.REDSTONE, 3);
            }

            if (this.internalMethod06254(localValue1, (Potion)Potions.STRONG_STRENGTH.value())
               || this.internalMethod06254(localValue1, (Potion)Potions.STRONG_SWIFTNESS.value())
               || this.internalMethod06254(localValue1, (Potion)Potions.LONG_FIRE_RESISTANCE.value())
               || this.internalField1067.isSelected() && this.internalMethod06254(localValue1, (Potion)Potions.INVISIBILITY.value())
               || this.internalField1067.isSelected()
                  && this.internalMethod06254(localValue1, (Potion)Potions.LONG_INVISIBILITY.value())
                  && this.internalField0650.internalMethod04496()) {
               this.internalMethod05510(localValue1);
               this.internalField0514 = AutoBrewModule.InternalType0381.internalField1184;
               this.internalField0518.internalMethod00701();
            }
         }
      }
   }

   private void internalMethod09659() {
      if (this.internalField0518.internalMethod02365(1000L)) {
         if (this.internalField0417.isEmpty()) {
            this.internalField0417 = this.internalMethod04457();
         }

         if (!this.internalField0417.isEmpty()) {
            this.internalField0751 = this.internalField0417.removeFirst();
            this.internalField0514 = AutoBrewModule.InternalType0381.internalField0513;
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod02167(Item localValue1, int localValue2) {
      if (this.internalMethod05100(localValue1) == -1) {
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod00599(
               NotificationType.internalField0705,
               LanguageManager.internalMethod07214("autobrew.item_not_found"),
               LanguageManager.internalMethod00160("autobrew.need_item", localValue1.getName().getString())
            );
         this.toggle();
      } else {
         this.internalMethod00412(localValue1, localValue2);
         internalField0149.player.closeHandledScreen();
      }
   }

   private void internalMethod09668() {
      if (internalField0149.player.currentScreenHandler instanceof BrewingStandScreenHandler) {
         if (this.internalField0518.internalMethod02365(200L)) {
            internalField0149.player.closeHandledScreen();
            this.internalField0518.internalMethod00701();
         }
      } else {
         if (this.internalField0132 == null) {
            List localValue1 = this.internalMethod02084();
            if (localValue1.isEmpty()) {
               this.internalField0514 = AutoBrewModule.InternalType0381.internalField1185;
               this.internalField0518.internalMethod00701();
               return;
            }

            this.internalField0132 = (ChestBlockEntity)localValue1.getFirst();
         }

         if (!(internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler)) {
            if (this.internalField0518.internalMethod02365(500L)) {
               this.internalMethod00678(this.internalField0132);
               this.internalField0518.internalMethod00701();
            }
         } else if (this.internalField0518.internalMethod02365(200L)) {
            this.internalMethod09964();
            this.internalField0514 = AutoBrewModule.InternalType0381.internalField1185;
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private void internalMethod09669() {
      if (this.internalField0518.internalMethod02365(500L)) {
         internalField0149.player.closeHandledScreen();
         if (this.internalField0751 != null) {
            this.internalField0416.add(this.internalField0751.getPos());
         }

         this.internalField0514 = AutoBrewModule.InternalType0381.internalField0514;
         this.internalField0751 = null;
         this.internalField0132 = null;
         this.internalField0518.internalMethod00701();
      }
   }

   private List<BrewingStandBlockEntity> internalMethod04457() {
      ArrayList<BrewingStandBlockEntity> localValue1 = new ArrayList<>();
      byte localValue2 = 10;
      BlockPos localValue3 = BlockPos.ofFloored(internalField0149.player.getEntityPos());

      for (int localValue4 = -localValue2; localValue4 <= localValue2; localValue4++) {
         for (int localValue5 = -localValue2; localValue5 <= localValue2; localValue5++) {
            for (int localValue6 = -localValue2; localValue6 <= localValue2; localValue6++) {
               BlockPos localValue7 = localValue3.add(localValue4, localValue5, localValue6);
               if (internalField0149.world.getBlockEntity(localValue7) instanceof BrewingStandBlockEntity localValue8) {
                  localValue1.add(localValue8);
               }
            }
         }
      }

      return localValue1;
   }

   private List<ChestBlockEntity> internalMethod02084() {
      ArrayList<ChestBlockEntity> localValue1 = new ArrayList<>();
      byte localValue2 = 10;
      BlockPos localValue3 = BlockPos.ofFloored(internalField0149.player.getEntityPos());

      for (int localValue4 = -localValue2; localValue4 <= localValue2; localValue4++) {
         for (int localValue5 = -localValue2; localValue5 <= localValue2; localValue5++) {
            for (int localValue6 = -localValue2; localValue6 <= localValue2; localValue6++) {
               BlockPos localValue7 = localValue3.add(localValue4, localValue5, localValue6);
               if (internalField0149.world.getBlockEntity(localValue7) instanceof ChestBlockEntity localValue8) {
                  localValue1.add(localValue8);
               }
            }
         }
      }

      localValue1.sort(Comparator.comparingDouble(localValue1x -> Vec3d.ofCenter(localValue1x.getPos()).squaredDistanceTo(Vec3d.ofCenter(localValue3))));
      return localValue1;
   }

   private void internalMethod09964() {
      if (internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue1) {
         DefaultedList localValue6 = localValue1.slots;

         for (int localValue3 = 0; localValue3 < localValue6.size(); localValue3++) {
            Slot localValue4 = (Slot)localValue6.get(localValue3);
            if (localValue4.inventory == internalField0149.player.getInventory()) {
               ItemStack localValue5 = localValue4.getStack();
               if (!localValue5.isEmpty() && (this.internalMethod06589(localValue5) || this.internalMethod03647(localValue5))) {
                  InventoryUtils.internalMethod03592(localValue3);
               }
            }
         }
      }
   }

   private boolean internalMethod06589(ItemStack localValue1) {
      return localValue1.getItem() == Items.POTION || localValue1.getItem() == Items.SPLASH_POTION || localValue1.getItem() == Items.LINGERING_POTION;
   }

   private boolean internalMethod03647(ItemStack localValue1) {
      Item localValue2 = localValue1.getItem();

      for (Item localValue6 : internalField0838) {
         if (localValue2 == localValue6) {
            return true;
         }
      }

      return false;
   }

   private void internalMethod05510(BrewingStandScreenHandler localValue1) {
      for (int localValue2 = 0; localValue2 < 3; localValue2++) {
         if (!localValue1.getSlot(localValue2).getStack().isEmpty()) {
            InventoryUtils.internalMethod03592(localValue2);
         }
      }
   }

   private void internalMethod00678(ChestBlockEntity localValue1) {
      BlockPos localValue2 = localValue1.getPos();
      Vec3d localValue3 = new Vec3d(localValue2.getX() + 0.5, localValue2.getY() + 0.5, localValue2.getZ() + 0.5);
      BlockHitResult localValue4 = new BlockHitResult(localValue3, Direction.UP, localValue2, false);
      internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue4);
   }

   private void internalMethod00412(Item localValue1, int localValue2) {
      int localValue3;
      if (this.internalField0519.internalMethod02365((long)(this.internalField0383.internalMethod08576() * 2.0F)) && (localValue3 = this.internalMethod05100(localValue1)) != -1) {
         InventoryUtils.internalMethod08185(localValue3, localValue2);
         this.internalField0519.internalMethod00701();
      }
   }

   private int internalMethod05100(Item localValue1) {
      for (int localValue2 = 5; localValue2 < 41; localValue2++) {
         if (((Slot)internalField0149.player.currentScreenHandler.slots.get(localValue2)).getStack().getItem() == localValue1) {
            return localValue2;
         }
      }

      return -1;
   }

   private boolean internalMethod06254(BrewingStandScreenHandler localValue1, Potion localValue2) {
      boolean localValue3 = true;

      for (int localValue4 = 0; localValue4 < 3; localValue4++) {
         ItemStack localValue5 = ((Slot)localValue1.slots.get(localValue4)).getStack();
         if (localValue5.getItem() == Items.POTION
            && ((RegistryEntry)((PotionContentsComponent)localValue5.get(DataComponentTypes.POTION_CONTENTS)).potion().get()).value() != localValue2) {
            localValue3 = false;
         }
      }

      return localValue3;
   }

   private int internalMethod05509(BrewingStandScreenHandler localValue1) {
      for (int localValue2 = 5; localValue2 < 41; localValue2++) {
         ItemStack localValue3 = ((Slot)localValue1.slots.get(localValue2)).getStack();
         if (localValue3.getItem() == Items.POTION
            && ((PotionContentsComponent)localValue3.get(DataComponentTypes.POTION_CONTENTS)).potion().isPresent()
            && ((RegistryEntry)((PotionContentsComponent)localValue3.get(DataComponentTypes.POTION_CONTENTS)).potion().get()).value() == Potions.WATER.value()) {
            return localValue2;
         }
      }

      return -1;
   }

   static enum InternalType0381 {
      internalField0514,
      internalField0513,
      internalField1183,
      internalField1184,
      internalField1185;
   }
}
