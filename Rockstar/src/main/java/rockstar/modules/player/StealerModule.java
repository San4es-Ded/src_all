package rockstar.modules.player;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Stealer",
   category = ModuleCategory.PLAYER
)
public class StealerModule extends Module {
   private BooleanSetting internalField0650;
   private SliderSetting internalField0383;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private BooleanSetting internalField1263;
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private final List<EnderChestBlockEntity> internalField0416 = new ArrayList<>();
   private EnderChestBlockEntity internalField0554;

   public StealerModule() {
      this.internalMethod09679();
   }

   private void internalMethod09679() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.stealer.instant");
      this.internalField0383 = new SliderSetting(this, "modules.settings.stealer.delay", this.internalField0650::internalMethod04496)
         .internalMethod05900(0.0F)
         .internalMethod02732(1000.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(400.0F);
      this.internalField0651 = new BooleanSetting(this, "modules.settings.stealer.close", "modules.settings.stealer.close.description");
      this.internalField1261 = new BooleanSetting(this, "modules.settings.stealer.off", "modules.settings.stealer.off.description");
      this.internalField1263 = new BooleanSetting(this, "modules.settings.stealer.open_mystic", "modules.settings.stealer.open_mystic.description");
      this.internalField0668 = new ModeSetting(this, "modules.settings.stealer.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.stealer.mode.up").select();
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.stealer.mode.down");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.stealer.mode.center");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.stealer.mode.random");
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.currentScreen instanceof GenericContainerScreen
         && internalField0149.player.currentScreenHandler instanceof GenericContainerScreenHandler localValue7) {
         int localValue9 = localValue7.getInventory().size();
         if (this.internalField0650.internalMethod04496()) {
            for (int localValue10 = 0; localValue10 < localValue9; localValue10++) {
               if (!localValue7.getSlot(localValue10).getStack().isEmpty()) {
                  internalField0149.interactionManager.clickSlot(localValue7.syncId, localValue10, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
               }
            }
         } else if (this.internalField0668.internalMethod06103(this.internalField0237)) {
            for (int localValue11 = 0;
               localValue11 < localValue9
                  && this.internalField0519
                     .internalMethod02365((long)(this.internalField0383.internalMethod08576() + MathUtils.internalMethod05368(-100.0, 100.0)));
               localValue11++
            ) {
               if (!localValue7.getSlot(localValue11).getStack().isEmpty()) {
                  internalField0149.interactionManager.clickSlot(localValue7.syncId, localValue11, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                  this.internalField0519.internalMethod00701();
               }
            }
         } else if (this.internalField0668.internalMethod06103(this.internalField0238)) {
            for (int localValue12 = localValue9 - 1;
               localValue12 >= 0
                  && this.internalField0519
                     .internalMethod02365((long)(this.internalField0383.internalMethod08576() + MathUtils.internalMethod05368(-100.0, 100.0)));
               localValue12--
            ) {
               if (!localValue7.getSlot(localValue12).getStack().isEmpty()) {
                  internalField0149.interactionManager.clickSlot(localValue7.syncId, localValue12, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                  this.internalField0519.internalMethod00701();
               }
            }
         } else if (this.internalField0668.internalMethod06103(this.internalField1066)) {
            int localValue13 = localValue9 / 2;

            for (int localValue15 = 0;
               localValue15 <= localValue13
                  && this.internalField0519
                     .internalMethod02365((long)(this.internalField0383.internalMethod08576() + MathUtils.internalMethod05368(-100.0, 100.0)));
               localValue15++
            ) {
               int localValue5 = localValue13 - localValue15;
               int localValue6 = localValue13 + localValue15;
               if (localValue5 >= 0 && !localValue7.getSlot(localValue5).getStack().isEmpty()) {
                  internalField0149.interactionManager.clickSlot(localValue7.syncId, localValue5, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                  this.internalField0519.internalMethod00701();
               } else if (localValue6 < localValue9 && !localValue7.getSlot(localValue6).getStack().isEmpty()) {
                  internalField0149.interactionManager.clickSlot(localValue7.syncId, localValue6, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
                  this.internalField0519.internalMethod00701();
               }
            }
         } else if (this.internalField0668.internalMethod06103(this.internalField1067)
            && this.internalField0519.internalMethod02365((long)(this.internalField0383.internalMethod08576() + MathUtils.internalMethod05368(-100.0, 100.0)))
            )
          {
            int localValue14 = (int)MathUtils.internalMethod05368(0.0, localValue9);
            if (!localValue7.getSlot(localValue14).getStack().isEmpty()) {
               internalField0149.interactionManager.clickSlot(localValue7.syncId, localValue14, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
               this.internalField0519.internalMethod00701();
            }
         }

         if (this.internalMethod04992(localValue7)) {
            if (this.internalField0554 != null && this.internalField1263.internalMethod04496()) {
               this.internalField0416.add(this.internalField0554);
            }

            if (this.internalField1261.internalMethod04496()) {
               this.toggle();
            }

            if (this.internalField0651.internalMethod04496()) {
               internalField0149.player.closeHandledScreen();
            }
         }
      } else if (this.internalField1263.internalMethod04496()) {
         if (this.internalField0554 == null || !this.internalMethod03882(this.internalField0554)) {
            this.internalField0554 = this.internalMethod06544();
            this.internalField0518.internalMethod00701();
         }

         if (this.internalField0554 != null && this.internalField0518.internalMethod02365(200L)) {
            BlockPos localValue1 = this.internalField0554.getPos();
            Vec3d localValue8 = Vec3d.ofCenter(localValue1);
            Rotation localValue3 = RotationUtils.internalMethod05580(localValue8);
            RockstarClient.getInstance()
               .internalMethod02368()
               .internalMethod00418(localValue3, RotationBehavior.internalField0115, 22.0F, 22.0F, 22.0F, RotationPriority.internalField1012);
            BlockHitResult localValue4 = new BlockHitResult(localValue8, Direction.UP, localValue1, false);
            internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue4);
            internalField0149.player.swingHand(Hand.MAIN_HAND);
            this.internalField0518.internalMethod00701();
         }
      }
   }

   private boolean internalMethod03882(EnderChestBlockEntity localValue1) {
      return internalField0149.player.squaredDistanceTo(localValue1.getPos().toCenterPos()) < 16.0 && !this.internalField0416.contains(localValue1);
   }

   private EnderChestBlockEntity internalMethod06544() {
      for (BlockEntity localValue2 : BlockEntityCache.internalMethod06956()) {
         if (localValue2 instanceof EnderChestBlockEntity localValue3 && this.internalMethod03882(localValue3)) {
            return localValue3;
         }
      }

      return null;
   }

   private boolean internalMethod04992(GenericContainerScreenHandler localValue1) {
      Inventory localValue2 = localValue1.getInventory();

      for (int localValue3 = 0; localValue3 < localValue2.size(); localValue3++) {
         if (!localValue2.getStack(localValue3).isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
