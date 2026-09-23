package rockstar.modules.movement;




import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Water Speed",
   category = ModuleCategory.MOVEMENT
)
public class WaterSpeedModule extends Module {
   private int internalField0227 = -1;
   private int internalField0228 = -1;
   private int internalField1053 = -1;
   private int internalField1055;

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null
         && internalField0149.world != null
         && internalField0149.interactionManager != null
         && internalField0149.player.isTouchingWater()) {
         BlockPos localValue1 = internalField0149.player.getBlockPos().up();
         if (this.internalMethod05535(localValue1)) {
            internalField0149.player
               .setVelocity(
                  internalField0149.player.getVelocity().x * 1.05, internalField0149.player.getVelocity().y, internalField0149.player.getVelocity().z * 1.05
               );
            this.internalMethod09294();
         } else {
            this.internalMethod05534(localValue1);
         }
      } else {
         this.internalMethod09294();
      }

      super.internalMethod08229();
   }

   @Override
   public void onDisable() {
      this.internalMethod09294();
   }

   private void internalMethod05534(BlockPos localValue1) {
      if (!internalField0149.world.getBlockState(localValue1).isReplaceable()) {
         this.internalMethod09294();
      } else {
         WaterSpeedModule.InternalType0396 localValue2 = this.internalMethod00381(localValue1);
         if (localValue2 == null) {
            this.internalMethod09294();
         } else if (this.internalMethod03155(internalField0149.player.getOffHandStack())) {
            this.internalMethod03201(localValue2, Hand.OFF_HAND);
         } else {
            HotbarSlot localValue3 = InventorySlots.internalMethod02872().internalMethod03297(this::internalMethod03155);
            if (localValue3 == null) {
               this.internalMethod09294();
            } else if (this.internalMethod05733(localValue3)) {
               this.internalMethod03201(localValue2, Hand.MAIN_HAND);
            }
         }
      }
   }

   private WaterSpeedModule.InternalType0396 internalMethod00381(BlockPos localValue1) {
      Vec3d localValue2 = internalField0149.player.getEyePos();
      double localValue3 = internalField0149.player.getBlockInteractionRange();
      WaterSpeedModule.InternalType0396 localValue5 = null;
      double localValue6 = Double.MAX_VALUE;

      for (Direction localValue11 : Direction.values()) {
         BlockPos localValue12 = localValue1.offset(localValue11);
         Direction localValue13 = localValue11.getOpposite();
         BlockState localValue14 = internalField0149.world.getBlockState(localValue12);
         if (localValue14.isSideSolidFullSquare(internalField0149.world, localValue12, localValue13)) {
            Vec3d localValue15 = localValue12.toCenterPos().add(localValue13.getOffsetX() * 0.5, localValue13.getOffsetY() * 0.5, localValue13.getOffsetZ() * 0.5);
            double localValue16 = localValue2.squaredDistanceTo(localValue15);
            if (!(localValue16 > localValue3 * localValue3) && !(localValue16 >= localValue6)) {
               localValue6 = localValue16;
               localValue5 = new WaterSpeedModule.InternalType0396(localValue12, localValue13, localValue15);
            }
         }
      }

      return localValue5;
   }

   private boolean internalMethod05733(HotbarSlot localValue1) {
      int localValue2 = internalField0149.player.getInventory().getSelectedSlot();
      if (this.internalField0228 == localValue1.internalMethod08745() && localValue2 == localValue1.internalMethod08745()) {
         return internalField0149.player.age > this.internalField1053;
      } else {
         if (this.internalField0227 == -1) {
            this.internalField0227 = localValue2;
         }

         this.internalField0228 = localValue1.internalMethod08745();
         this.internalField1053 = internalField0149.player.age;
         this.internalField1055 = 0;
         InventoryUtils.internalMethod03663(this.internalField0228);
         return false;
      }
   }

   private void internalMethod03201(WaterSpeedModule.InternalType0396 localValue1, Hand localValue2) {
      Rotation localValue3 = this.internalMethod05916(internalField0149.player.getEyePos(), localValue1.internalMethod06228());
      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(localValue3, RotationBehavior.internalField1003, 100.0F, 100.0F, 100.0F, RotationPriority.internalField1012);
      Rotation localValue4 = RockstarClient.getInstance().internalMethod02368().internalMethod09074();
      if (Math.abs(MathHelper.wrapDegrees(localValue4.internalMethod00169() - localValue3.internalMethod00169())) > 3.0F
         || Math.abs(localValue4.internalMethod00171() - localValue3.internalMethod00171()) > 3.0F) {
         this.internalField1055 = 0;
      } else if (++this.internalField1055 >= 2) {
         internalField0149.interactionManager
            .interactBlock(
               internalField0149.player, localValue2, new BlockHitResult(localValue1.internalMethod06228(), localValue1.internalMethod01695(), localValue1.internalMethod00476(), false)
            );
         internalField0149.player.swingHand(localValue2);
         this.internalField1055 = 0;
      }
   }

   private boolean internalMethod05535(BlockPos localValue1) {
      return internalField0149.world.getBlockState(localValue1).isFullCube(internalField0149.world, localValue1);
   }

   private boolean internalMethod03155(ItemStack localValue1) {
      if (!localValue1.isEmpty() && localValue1.getItem() instanceof BlockItem localValue2) {
         BlockState localValue4 = localValue2.getBlock().getDefaultState();
         return localValue4.isFullCube(internalField0149.world, BlockPos.ORIGIN);
      } else {
         return false;
      }
   }

   private Rotation internalMethod05916(Vec3d localValue1, Vec3d localValue2) {
      double localValue3 = localValue2.x - localValue1.x;
      double localValue5 = localValue2.y - localValue1.y;
      double localValue7 = localValue2.z - localValue1.z;
      double localValue9 = Math.sqrt(localValue3 * localValue3 + localValue7 * localValue7);
      return new Rotation((float)Math.toDegrees(Math.atan2(localValue7, localValue3)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(localValue5, localValue9))));
   }

   private void internalMethod09294() {
      if (internalField0149.player != null && this.internalField0227 != -1 && internalField0149.player.getInventory().getSelectedSlot() != this.internalField0227) {
         InventoryUtils.internalMethod03663(this.internalField0227);
      }

      this.internalField0227 = -1;
      this.internalField0228 = -1;
      this.internalField1053 = -1;
      this.internalField1055 = 0;
   }

   static final class InternalType0396 {
      private final BlockPos internalField0352;
      private final Direction internalField0150;
      private final Vec3d internalField0283;

      InternalType0396(BlockPos localValue1, Direction localValue2, Vec3d localValue3) {
         this.internalField0352 = localValue1;
         this.internalField0150 = localValue2;
         this.internalField0283 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0396[support=" + this.internalField0352 + ", side=" + this.internalField0150 + ", hitPos=" + this.internalField0283 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0352);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0150);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         WaterSpeedModule.InternalType0396 other = (WaterSpeedModule.InternalType0396) localValue1;
         return java.util.Objects.equals(this.internalField0352, other.internalField0352)
            && java.util.Objects.equals(this.internalField0150, other.internalField0150)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283);
      }

      public BlockPos internalMethod00476() {
         return this.internalField0352;
      }

      public Direction internalMethod01695() {
         return this.internalField0150;
      }

      public Vec3d internalMethod06228() {
         return this.internalField0283;
      }
   }
}
