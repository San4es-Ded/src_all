package rockstar.client.internal.game;





import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TraversalTransition extends AbstractPathStep {
   private final TraversalTransition.InternalType0062 internalField0690;

   public TraversalTransition(PathNode localValue1, PathNode localValue2, TraversalTransition.InternalType0062 localValue3) {
      super(localValue1, localValue2);
      this.internalField0690 = localValue3;
   }

   public TraversalTransition.InternalType0062 internalMethod00838() {
      return this.internalField0690;
   }

   @Override
   public double internalMethod01349() {
      return switch (this.internalField0690) {
         case internalField0690 -> 2.0;
         case internalField0689 -> 1.3;
         case internalField1279 -> 2.6;
      };
   }

   @Override
   public int internalMethod01346() {
      return 100;
   }

   @Override
   public boolean internalMethod04946(PathfindBlockView localValue1) {
      int localValue2 = this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945();
      int localValue3 = this.internalField0924.internalMethod02949() - this.internalField0923.internalMethod02949();
      int localValue4 = this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945();
      switch (this.internalField0690) {
         case internalField0690:
            if (localValue2 == 0 && localValue4 == 0 && localValue3 == 1) {
               return localValue1.internalMethod09918(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               );
            }

            return false;
         case internalField0689:
            if (localValue2 == 0 && localValue4 == 0 && localValue3 == -1) {
               if (localValue1.internalMethod09918(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               )) {
                  return true;
               }

               if (localValue1.internalMethod09396(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               )) {
                  this.internalMethod01515(
                     this.internalField0923.internalMethod02949(),
                     localValue1.internalMethod05956(
                        this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
                     )
                  );
                  return true;
               }

               return localValue1.internalMethod08763(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               );
            }

            return false;
         case internalField1279:
            if (Math.abs(localValue2) + Math.abs(localValue4) == 1 && localValue3 == 1) {
               double localValue5 = localValue1.internalMethod05956(
                  this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
               );
               if (!Double.isNaN(localValue5) && !(localValue5 - this.internalField0924.internalMethod02949() > 0.4)) {
                  if (!localValue1.internalMethod05958(
                        this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() + 1, this.internalField0923.internalMethod07945()
                     )
                     && !localValue1.internalMethod07759(
                        this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() + 1, this.internalField0923.internalMethod07945()
                     )) {
                     return false;
                  }

                  this.internalMethod01515(this.internalField0923.internalMethod02949(), localValue5);
                  return localValue1.internalMethod03056(
                     this.internalField0923.internalMethod02945(),
                     this.internalField0923.internalMethod07945(),
                     this.internalField0924.internalMethod02945(),
                     this.internalField0924.internalMethod07945(),
                     localValue5 + 0.05,
                     localValue5 + 1.8
                  );
               }

               return false;
            }

            return false;
         default:
            return false;
      }
   }

   @Override
   public AbstractPathStep.InternalType0058 internalMethod04014() {
      ClientPlayerEntity localValue1 = internalMethod03778();
      if (localValue1 == null) {
         return AbstractPathStep.InternalType0058.internalField1160;
      } else {
         double localValue2 = localValue1.getX();
         double localValue4 = localValue1.getY();
         double localValue6 = localValue1.getZ();
         MovementInputController localValue8 = internalMethod00577();
         localValue8.internalMethod03557(false);
         localValue8.internalMethod08033(false);
         localValue8.internalMethod08045(false);
         localValue8.internalMethod08371(false);
         localValue8.internalMethod09358(false);
         switch (this.internalField0690) {
            case internalField0690:
               if (localValue4 >= this.internalField0924.internalMethod02949() - 0.05) {
                  return AbstractPathStep.InternalType0058.internalField0454;
               } else if (localValue4 < this.internalField0923.internalMethod02949() - 1.3 && !localValue1.isClimbing() && !localValue1.isOnGround()) {
                  return AbstractPathStep.InternalType0058.internalField1160;
               } else {
                  double localValue17 = this.internalField0923.internalMethod02945() + 0.5;
                  double localValue18 = this.internalField0923.internalMethod07945() + 0.5;
                  double localValue13 = Math.hypot(localValue2 - localValue17, localValue6 - localValue18);
                  if (localValue13 > 0.3) {
                     internalMethod01516(internalMethod01514(localValue17 - localValue2, localValue18 - localValue6), 0.0F);
                     localValue8.internalMethod03508(true);
                     localValue8.internalMethod08359(true);
                     return AbstractPathStep.InternalType0058.internalField0453;
                  }

                  Direction localValue15 = this.internalMethod04817();
                  if (localValue15 != null) {
                     internalMethod01516(internalMethod01514(localValue15.getOffsetX(), localValue15.getOffsetZ()), 0.0F);
                     localValue8.internalMethod03508(true);
                  } else {
                     localValue8.internalMethod03508(false);
                  }

                  localValue8.internalMethod08359(true);
                  return AbstractPathStep.InternalType0058.internalField0453;
               }
            case internalField0689:
               boolean localValue16 = localValue4 <= this.internalField0924.internalMethod02949() + 0.35
                  || localValue1.isOnGround() && localValue4 <= this.internalField0923.internalMethod02949() + 0.05
                  || localValue1.isTouchingWater() && localValue4 <= this.internalField0924.internalMethod02949() + 1.0;
               if (localValue16) {
                  return AbstractPathStep.InternalType0058.internalField0454;
               } else {
                  localValue8.internalMethod03508(false);
                  localValue8.internalMethod08359(false);
                  return AbstractPathStep.InternalType0058.internalField0453;
               }
            case internalField1279:
               double localValue9 = localValue2 - (this.internalField0924.internalMethod02945() + 0.5);
               double localValue11 = localValue6 - (this.internalField0924.internalMethod07945() + 0.5);
               if (localValue1.isOnGround() && Math.abs(localValue4 - this.internalField1045) < 0.6 && Math.hypot(localValue9, localValue11) < 0.5) {
                  return AbstractPathStep.InternalType0058.internalField0454;
               } else {
                  if (localValue4 < this.internalField0923.internalMethod02949() - 1.5 && !localValue1.isClimbing()) {
                     return AbstractPathStep.InternalType0058.internalField1160;
                  }

                  this.internalMethod01443(this.internalField0924.internalMethod02945() + 0.5, this.internalField0924.internalMethod07945() + 0.5);
                  localValue8.internalMethod03508(true);
                  localValue8.internalMethod08359(true);
                  return AbstractPathStep.InternalType0058.internalField0453;
               }
            default:
               return AbstractPathStep.InternalType0058.internalField1160;
         }
      }
   }

   private Direction internalMethod04817() {
      ClientWorld localValue1 = MinecraftClient.getInstance().world;
      if (localValue1 == null) {
         return null;
      } else {
         BlockState localValue2 = localValue1.getBlockState(
            new BlockPos(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949(), this.internalField0923.internalMethod07945())
         );
         return localValue2.isOf(Blocks.LADDER) ? ((Direction)localValue2.get(LadderBlock.FACING)).getOpposite() : null;
      }
   }

   @Override
   public void internalMethod01347() {
      MovementInputController localValue1 = internalMethod00577();
      localValue1.internalMethod03508(false);
      localValue1.internalMethod08359(false);
   }

   public static enum InternalType0062 {
      internalField0690,
      internalField0689,
      internalField1279;

      public static TraversalTransition.InternalType0062[] internalMethod00649() {
         return values();
      }

      public static TraversalTransition.InternalType0062 internalMethod01256(String localValue0) {
         return Enum.valueOf(TraversalTransition.InternalType0062.class, localValue0);
      }
   }
}
