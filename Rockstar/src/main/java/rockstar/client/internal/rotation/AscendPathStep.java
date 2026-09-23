package rockstar.client.internal.rotation;






import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AscendPathStep extends AbstractPathStep {
   private double internalField1044 = Double.POSITIVE_INFINITY;
   private int internalField0227;
   private boolean internalField0277;
   private final BlockFaceRotationHelper internalField0904 = new BlockFaceRotationHelper();
   private BlockPos internalField0352;

   public AscendPathStep(PathNode localValue1, PathNode localValue2) {
      super(localValue1, localValue2);
   }

   @Override
   public double internalMethod01349() {
      return this.internalField1044;
   }

   @Override
   public int internalMethod01346() {
      return Math.min(600, this.internalField0227 * 2 + 120);
   }

   @Override
   public boolean internalMethod04946(PathfindBlockView localValue1) {
      if (Math.abs(this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945())
            + Math.abs(this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945())
         != 1) {
         return false;
      } else if (this.internalField0924.internalMethod02949() != this.internalField0923.internalMethod02949() + 1) {
         return false;
      } else if (!localValue1.internalMethod02776(
         this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() - 1, this.internalField0924.internalMethod07945()
      )) {
         return false;
      } else if (localValue1.internalMethod08739(
         this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() - 1, this.internalField0924.internalMethod07945()
      )) {
         return false;
      } else {
         int[][] localValue2 = new int[][]{
            {this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() + 2, this.internalField0923.internalMethod07945()},
            {this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945()},
            {this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()}
         };
         int localValue3 = 0;
         int localValue4 = 0;

         for (int[] localValue8 : localValue2) {
            int localValue9 = localValue1.internalMethod05957(localValue8[0], localValue8[1], localValue8[2]);
            if (localValue9 == Integer.MAX_VALUE) {
               return false;
            }

            if (localValue9 > 0) {
               localValue3 += localValue9;
               localValue4++;
            }
         }

         this.internalField0227 = localValue3;
         if (localValue4 == 0) {
            return false;
         } else {
            double localValue10 = localValue3 / 20.0;
            this.internalField1044 = 1.5 + localValue10 * 1.5 + localValue4 * 0.4;
            return true;
         }
      }
   }

   @Override
   public AbstractPathStep.InternalType0058 internalMethod04014() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      if (localValue2 != null && localValue1.world != null && localValue1.interactionManager != null) {
         BlockPos[] localValue3 = new BlockPos[]{
            new BlockPos(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() + 2, this.internalField0923.internalMethod07945()),
            new BlockPos(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945()),
            new BlockPos(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945())
         };
         BlockPos localValue4 = null;
         BlockState localValue5 = null;

         for (BlockPos localValue9 : localValue3) {
            BlockState localValue10 = localValue1.world.getBlockState(localValue9);
            if (!BlockBreakPathStep.internalMethod04846(localValue10)) {
               localValue4 = localValue9;
               localValue5 = localValue10;
               break;
            }
         }

         MovementInputController localValue11 = NewtonCoreManager.internalMethod00114().internalMethod00183();
         localValue11.internalMethod01281();
         if (localValue4 == null) {
            if (this.internalField0277) {
               localValue1.interactionManager.cancelBlockBreaking();
               this.internalField0277 = false;
               this.internalField0352 = null;
               this.internalField0904.internalMethod06793();
            }

            return this.internalMethod00074();
         } else {
            localValue11.internalMethod03508(false);
            localValue11.internalMethod03557(false);
            localValue11.internalMethod08033(false);
            localValue11.internalMethod08045(false);
            localValue11.internalMethod08359(false);
            localValue11.internalMethod08371(false);
            localValue11.internalMethod09358(false);
            double localValue12 = Math.hypot(
               localValue2.getX() - (this.internalField0923.internalMethod02945() + 0.5), localValue2.getZ() - (this.internalField0923.internalMethod07945() + 0.5)
            );
            if (localValue12 > 1.7) {
               return AbstractPathStep.InternalType0058.internalField1160;
            } else {
               BestToolSelector.internalMethod04395(localValue5);
               if (this.internalField0352 != null && !this.internalField0352.equals(localValue4)) {
                  localValue1.interactionManager.cancelBlockBreaking();
                  this.internalField0904.internalMethod06793();
               }

               this.internalField0352 = localValue4;
               this.internalField0277 = true;
               Direction localValue13 = internalMethod03611(localValue2, localValue4);
               if (!this.internalField0904.internalMethod05481(localValue4, localValue13)) {
                  return AbstractPathStep.InternalType0058.internalField0453;
               } else {
                  localValue1.interactionManager.updateBlockBreakingProgress(localValue4, localValue13);
                  localValue2.swingHand(localValue2.getActiveHand());
                  return AbstractPathStep.InternalType0058.internalField0453;
               }
            }
         }
      } else {
         return AbstractPathStep.InternalType0058.internalField1160;
      }
   }

   private AbstractPathStep.InternalType0058 internalMethod00074() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      if (localValue2 == null) {
         return AbstractPathStep.InternalType0058.internalField1160;
      } else if (this.internalMethod05161(0.4) && localValue2.getY() >= this.internalField0924.internalMethod02949() - 0.05) {
         return AbstractPathStep.InternalType0058.internalField0454;
      } else {
         Vec3d localValue3 = new Vec3d(
            this.internalField0924.internalMethod02945() + 0.5, this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945() + 0.5
         );
         double localValue4 = localValue3.x - localValue2.getX();
         double localValue6 = localValue3.z - localValue2.getZ();
         float localValue8 = (float)Math.toDegrees(Math.atan2(localValue6, localValue4)) - 90.0F;
         SilentRotationHelper.internalMethod05978(new Rotation(localValue8, 0.0F));
         MovementInputController localValue9 = NewtonCoreManager.internalMethod00114().internalMethod00183();
         localValue9.internalMethod01281();
         localValue9.internalMethod03508(true);
         localValue9.internalMethod03557(false);
         localValue9.internalMethod08033(false);
         localValue9.internalMethod08045(false);
         localValue9.internalMethod08371(false);
         localValue9.internalMethod09358(false);
         boolean localValue10 = localValue2.getY() < this.internalField0924.internalMethod02949() - 0.05;
         boolean localValue11 = localValue2.horizontalCollision || Math.hypot(localValue4, localValue6) < 0.6;
         localValue9.internalMethod08359(localValue10 && localValue2.isOnGround() && localValue11);
         return AbstractPathStep.InternalType0058.internalField0453;
      }
   }

   private static Direction internalMethod03611(ClientPlayerEntity localValue0, BlockPos localValue1) {
      Vec3d localValue2 = localValue0.getEyePos();
      Direction localValue3 = Direction.UP;
      double localValue4 = -Double.MAX_VALUE;

      for (Direction localValue9 : Direction.values()) {
         Vec3d localValue10 = Vec3d.ofCenter(localValue1).add(localValue9.getOffsetX() * 0.5, localValue9.getOffsetY() * 0.5, localValue9.getOffsetZ() * 0.5);
         Vec3d localValue11 = localValue10.subtract(localValue2).normalize();
         double localValue12 = localValue11.x * localValue9.getOffsetX() + localValue11.y * localValue9.getOffsetY() + localValue11.z * localValue9.getOffsetZ();
         double localValue14 = -localValue12;
         if (localValue14 > localValue4) {
            localValue4 = localValue14;
            localValue3 = localValue9;
         }
      }

      return localValue3;
   }

   @Override
   public void internalMethod01347() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.interactionManager != null && this.internalField0277) {
         localValue1.interactionManager.cancelBlockBreaking();
      }

      this.internalField0277 = false;
      this.internalField0352 = null;
      this.internalField0904.internalMethod06793();
      MovementInputController localValue2 = NewtonCoreManager.internalMethod00114().internalMethod00183();
      localValue2.internalMethod03508(false);
      localValue2.internalMethod08359(false);
      localValue2.internalMethod09358(false);
   }
}
