package rockstar.client.internal.rotation;





import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPathStep {
   public final PathNode internalField0923;
   public final PathNode internalField0924;
   @Nullable
   public AbstractPathStep internalField0100;
   public double internalField0194;
   public double internalField0193;
   public double internalField1045;

   public AbstractPathStep(PathNode localValue1, PathNode localValue2) {
      this.internalField0923 = localValue1;
      this.internalField0924 = localValue2;
      this.internalField0193 = localValue1.internalMethod02949();
      this.internalField1045 = localValue2.internalMethod02949();
   }

   public PathNode internalMethod01873() {
      return this.internalField0923;
   }

   public PathNode internalMethod02540() {
      return this.internalField0924;
   }

   public void internalMethod02953(@Nullable AbstractPathStep localValue1) {
      this.internalField0100 = localValue1;
   }

   @Nullable
   public AbstractPathStep internalMethod03493() {
      return this.internalField0100;
   }

   public boolean internalMethod05094(double localValue1) {
      ClientPlayerEntity localValue3 = internalMethod03778();
      if (localValue3 == null) {
         return false;
      } else {
         double localValue4 = localValue3.getX() - (this.internalField0924.internalMethod02945() + 0.5);
         double localValue6 = localValue3.getZ() - (this.internalField0924.internalMethod07945() + 0.5);
         return localValue4 * localValue4 + localValue6 * localValue6 < localValue1 * localValue1;
      }
   }

   public void internalMethod05093(double localValue1) {
      this.internalField0194 += localValue1;
   }

   public void internalMethod01515(double localValue1, double localValue3) {
      this.internalField0193 = localValue1;
      this.internalField1045 = localValue3;
   }

   public boolean internalMethod01348() {
      return true;
   }

   public boolean internalMethod01351() {
      return true;
   }

   public final double internalMethod01344() {
      return this.internalMethod01349() + this.internalField0194;
   }

   public abstract double internalMethod01349();

   public int internalMethod01346() {
      return 60;
   }

   public abstract boolean internalMethod04946(PathfindBlockView localValue1);

   public abstract AbstractPathStep.InternalType0058 internalMethod04014();

   public void internalMethod01347() {
   }

   @Nullable
   public static ClientPlayerEntity internalMethod03778() {
      return MinecraftClient.getInstance().player;
   }

   public static MovementInputController internalMethod00577() {
      MovementInputController localValue0 = NewtonCoreManager.internalMethod00114().internalMethod00183();
      localValue0.internalMethod01281();
      return localValue0;
   }

   public static void internalMethod01516(float localValue0, float localValue1) {
      SilentRotationHelper.internalMethod05978(new Rotation(localValue0, localValue1));
   }

   public static float internalMethod01345() {
      RotationManager localValue0 = NewtonCoreManager.internalMethod00114().internalMethod03241();
      ClientPlayerEntity localValue1 = internalMethod03778();
      if (!localValue0.internalMethod01525() && localValue1 != null) {
         return localValue0.internalMethod09074().internalMethod00169();
      } else {
         return localValue1 != null ? localValue1.getYaw() : 0.0F;
      }
   }

   public static float internalMethod01514(double localValue0, double localValue2) {
      return (float)Math.toDegrees(Math.atan2(localValue2, localValue0)) - 90.0F;
   }

   public void internalMethod01443(double localValue1, double localValue3) {
      ClientPlayerEntity localValue5 = internalMethod03778();
      if (localValue5 != null) {
         double localValue6 = localValue1 - localValue5.getX();
         double localValue8 = localValue3 - localValue5.getZ();
         if (Math.hypot(localValue6, localValue8) < 0.3) {
            float localValue10 = this.internalMethod01350();
            if (!Float.isNaN(localValue10)) {
               internalMethod01516(localValue10, 0.0F);
            }
         } else {
            internalMethod01516(internalMethod01514(localValue6, localValue8), 0.0F);
         }
      }
   }

   public void internalMethod05160(double localValue1) {
      ClientPlayerEntity localValue3 = internalMethod03778();
      if (localValue3 != null) {
         double localValue4 = this.internalField0923.internalMethod02945() + 0.5;
         double localValue6 = this.internalField0923.internalMethod07945() + 0.5;
         double localValue8 = this.internalField0924.internalMethod02945() + 0.5 - localValue4;
         double localValue10 = this.internalField0924.internalMethod07945() + 0.5 - localValue6;
         double localValue12 = Math.sqrt(localValue8 * localValue8 + localValue10 * localValue10);
         if (localValue12 < 1.0E-6) {
            this.internalMethod01443(this.internalField0924.internalMethod02945() + 0.5, this.internalField0924.internalMethod07945() + 0.5);
         } else {
            double localValue14 = localValue8 / localValue12;
            double localValue16 = localValue10 / localValue12;
            double localValue18 = Math.min(localValue12, Math.max(0.0, this.internalMethod01442(localValue3.getX(), localValue3.getZ())) + localValue1);
            this.internalMethod01443(localValue4 + localValue14 * localValue18, localValue6 + localValue16 * localValue18);
         }
      }
   }

   public Vec3d internalMethod05550() {
      return new Vec3d(this.internalField0924.internalMethod02945() + 0.5, this.internalField1045, this.internalField0924.internalMethod07945() + 0.5);
   }

   public boolean internalMethod05161(double localValue1) {
      ClientPlayerEntity localValue3 = internalMethod03778();
      if (localValue3 == null) {
         return false;
      } else {
         double localValue4 = this.internalField0923.internalMethod02945() + 0.5;
         double localValue6 = this.internalField0923.internalMethod07945() + 0.5;
         double localValue8 = this.internalField0924.internalMethod02945() + 0.5 - localValue4;
         double localValue10 = this.internalField0924.internalMethod07945() + 0.5 - localValue6;
         double localValue12 = Math.sqrt(localValue8 * localValue8 + localValue10 * localValue10);
         if (localValue12 < 1.0E-6) {
            double localValue24 = localValue3.getX() - (this.internalField0924.internalMethod02945() + 0.5);
            double localValue25 = localValue3.getZ() - (this.internalField0924.internalMethod07945() + 0.5);
            return localValue24 * localValue24 + localValue25 * localValue25 < localValue1 * localValue1;
         } else {
            double localValue14 = localValue8 / localValue12;
            double localValue16 = localValue10 / localValue12;
            double localValue18 = localValue3.getX() - localValue4;
            double localValue20 = localValue3.getZ() - localValue6;
            double localValue22 = localValue18 * localValue14 + localValue20 * localValue16;
            return localValue22 >= localValue12 - localValue1;
         }
      }
   }

   public float internalMethod01350() {
      double localValue1 = this.internalField0924.internalMethod02945() + 0.5 - (this.internalField0923.internalMethod02945() + 0.5);
      double localValue3 = this.internalField0924.internalMethod07945() + 0.5 - (this.internalField0923.internalMethod07945() + 0.5);
      return localValue1 == 0.0 && localValue3 == 0.0 ? Float.NaN : internalMethod01514(localValue1, localValue3);
   }

   public boolean internalMethod08154(double localValue1) {
      if (this.internalField0100 == null) {
         return false;
      } else {
         float localValue3 = this.internalMethod01350();
         float localValue4 = this.internalField0100.internalMethod01350();
         if (!Float.isNaN(localValue3) && !Float.isNaN(localValue4)) {
            float localValue5 = localValue4 - localValue3;

            while (localValue5 > 180.0F) {
               localValue5 -= 360.0F;
            }

            while (localValue5 < -180.0F) {
               localValue5 += 360.0F;
            }

            return Math.abs(localValue5) >= localValue1;
         } else {
            return false;
         }
      }
   }

   public double[] internalMethod01518(int localValue1) {
      ClientPlayerEntity localValue2 = internalMethod03778();
      if (localValue2 == null) {
         return new double[]{0.0, 0.0};
      } else {
         Vec3d localValue3 = localValue2.getVelocity();
         double localValue4 = localValue2.getX();
         double localValue6 = localValue2.getZ();
         double localValue8 = localValue3.x;
         double localValue10 = localValue3.z;

         for (int localValue12 = 0; localValue12 < localValue1; localValue12++) {
            localValue4 += localValue8;
            localValue6 += localValue10;
            localValue8 *= 0.91;
            localValue10 *= 0.91;
         }

         return new double[]{localValue4, localValue6};
      }
   }

   public double internalMethod01513(double localValue1, double localValue3) {
      double localValue5 = this.internalField0923.internalMethod02945() + 0.5;
      double localValue7 = this.internalField0923.internalMethod07945() + 0.5;
      double localValue9 = this.internalField0924.internalMethod02945() + 0.5 - localValue5;
      double localValue11 = this.internalField0924.internalMethod07945() + 0.5 - localValue7;
      double localValue13 = Math.sqrt(localValue9 * localValue9 + localValue11 * localValue11);
      double localValue15 = localValue1 - localValue5;
      double localValue17 = localValue3 - localValue7;
      if (localValue13 < 1.0E-6) {
         return Math.sqrt(localValue15 * localValue15 + localValue17 * localValue17);
      } else {
         double localValue19 = (localValue15 * localValue9 + localValue17 * localValue11) / localValue13;
         double localValue21 = localValue15 * localValue15 + localValue17 * localValue17;
         return Math.sqrt(Math.max(0.0, localValue21 - localValue19 * localValue19));
      }
   }

   public double internalMethod01442(double localValue1, double localValue3) {
      double localValue5 = this.internalField0923.internalMethod02945() + 0.5;
      double localValue7 = this.internalField0923.internalMethod07945() + 0.5;
      double localValue9 = this.internalField0924.internalMethod02945() + 0.5 - localValue5;
      double localValue11 = this.internalField0924.internalMethod07945() + 0.5 - localValue7;
      double localValue13 = Math.sqrt(localValue9 * localValue9 + localValue11 * localValue11);
      if (localValue13 < 1.0E-6) {
         return 0.0;
      } else {
         double localValue15 = localValue9 / localValue13;
         double localValue17 = localValue11 / localValue13;
         return (localValue1 - localValue5) * localValue15 + (localValue3 - localValue7) * localValue17;
      }
   }

   public double internalMethod08658() {
      ClientPlayerEntity localValue1 = internalMethod03778();
      if (localValue1 == null) {
         return 0.0;
      } else {
         double localValue2 = this.internalField0924.internalMethod02945() + 0.5 - (this.internalField0923.internalMethod02945() + 0.5);
         double localValue4 = this.internalField0924.internalMethod07945() + 0.5 - (this.internalField0923.internalMethod07945() + 0.5);
         double localValue6 = Math.sqrt(localValue2 * localValue2 + localValue4 * localValue4);
         if (localValue6 < 1.0E-6) {
            return 0.0;
         } else {
            Vec3d localValue8 = localValue1.getVelocity();
            return (localValue8.x * localValue2 + localValue8.z * localValue4) / localValue6;
         }
      }
   }

   public double internalMethod08660() {
      double localValue1 = this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945();
      double localValue3 = this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945();
      return Math.sqrt(localValue1 * localValue1 + localValue3 * localValue3);
   }

   public void internalMethod01706(MovementInputController localValue1) {
      ClientPlayerEntity localValue2 = internalMethod03778();
      if (localValue2 != null && this.internalField0100 != null) {
         if (this.internalMethod08154(60.0)) {
            double[] localValue3 = this.internalMethod01518(6);
            double localValue4 = this.internalMethod01442(localValue3[0], localValue3[1]);
            double localValue6 = this.internalMethod08660();
            if (localValue4 > localValue6 + 0.05) {
               localValue1.internalMethod09358(false);
               localValue1.internalMethod03508(false);
               localValue1.internalMethod03557(true);
            } else if (localValue4 > localValue6 - 0.25) {
               localValue1.internalMethod09358(false);
               if (localValue2.isSprinting()) {
                  localValue1.internalMethod03508(false);
               }
            }
         }
      }
   }

   public void internalMethod06359(MovementInputController localValue1, AbstractPathStep.InternalType0057 localValue2) {
      localValue1.internalMethod08033(false);
      localValue1.internalMethod08045(false);
      localValue1.internalMethod08371(false);
      switch (localValue2) {
         case internalField0451:
            localValue1.internalMethod03508(true);
            localValue1.internalMethod03557(false);
            localValue1.internalMethod09358(true);
            break;
         case internalField0452:
            localValue1.internalMethod03508(true);
            localValue1.internalMethod03557(false);
            localValue1.internalMethod09358(false);
            break;
         case internalField1158:
            localValue1.internalMethod03508(false);
            localValue1.internalMethod03557(false);
            localValue1.internalMethod09358(false);
            break;
         case internalField1159:
            localValue1.internalMethod03508(false);
            localValue1.internalMethod03557(true);
            localValue1.internalMethod09358(false);
      }
   }

   public void internalMethod06640(MovementInputController localValue1) {
      ClientPlayerEntity localValue2 = internalMethod03778();
      if (localValue2 != null) {
         double localValue3 = this.internalField0923.internalMethod02945() + 0.5;
         double localValue5 = this.internalField0923.internalMethod07945() + 0.5;
         double localValue7 = this.internalField0924.internalMethod02945() + 0.5 - localValue3;
         double localValue9 = this.internalField0924.internalMethod07945() + 0.5 - localValue5;
         double localValue11 = Math.sqrt(localValue7 * localValue7 + localValue9 * localValue9);
         if (!(localValue11 < 1.0E-6)) {
            double localValue13 = localValue7 / localValue11;
            double localValue15 = localValue9 / localValue11;
            double localValue17 = localValue2.getX() - localValue3;
            double localValue19 = localValue2.getZ() - localValue5;
            double localValue21 = localValue17 * localValue13 + localValue19 * localValue15;
            double localValue23 = localValue17 - localValue21 * localValue13;
            double localValue25 = localValue19 - localValue21 * localValue15;
            if (!(localValue23 * localValue23 + localValue25 * localValue25 < 0.0025000000000000005)) {
               double localValue27 = Math.toRadians(internalMethod01345());
               double localValue29 = Math.cos(localValue27);
               double localValue31 = Math.sin(localValue27);
               boolean localValue33 = -localValue23 * localValue29 + -localValue25 * localValue31 > 0.0;
               localValue1.internalMethod08033(localValue33);
               localValue1.internalMethod08045(!localValue33);
            }
         }
      }
   }

   public boolean internalMethod08659() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      ClientWorld localValue3 = localValue1.world;
      if (localValue2 == null || localValue3 == null) {
         return false;
      } else if (!localValue2.isTouchingWater() && !localValue2.isClimbing()) {
         BlockPos localValue4 = BlockPos.ofFloored(localValue2.getX(), localValue2.getBoundingBox().minY + 1.0E-4, localValue2.getZ());
         return internalMethod00650(localValue3, localValue4.getX(), localValue4.getY(), localValue4.getZ())
            || internalMethod00650(
               localValue3, this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949(), this.internalField0923.internalMethod07945()
            )
            || internalMethod00650(
               localValue3, this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945()
            );
      } else {
         return false;
      }
   }

   private static boolean internalMethod00650(World localValue0, int localValue1, int localValue2, int localValue3) {
      return internalMethod03892(localValue0, new BlockPos(localValue1, localValue2 + 1, localValue3), localValue1, localValue2, localValue3)
         || internalMethod03892(localValue0, new BlockPos(localValue1, localValue2 + 2, localValue3), localValue1, localValue2, localValue3);
   }

   private static boolean internalMethod03892(World localValue0, BlockPos localValue1, int localValue2, int localValue3, int localValue4) {
      BlockState localValue5 = localValue0.getBlockState(localValue1);
      return localValue5.getBlock() instanceof TrapdoorBlock && PathfindBlockView.internalMethod05828(localValue0, localValue5, localValue1, localValue2, localValue3, localValue4);
   }

   public static enum InternalType0057 {
      internalField0451,
      internalField0452,
      internalField1158,
      internalField1159;

      public static AbstractPathStep.InternalType0057[] internalMethod04650() {
         return values();
      }

      public static AbstractPathStep.InternalType0057 internalMethod05043(String localValue0) {
         return Enum.valueOf(AbstractPathStep.InternalType0057.class, localValue0);
      }
   }

   public static enum InternalType0058 {
      internalField0453,
      internalField0454,
      internalField1160;

      public static AbstractPathStep.InternalType0058[] internalMethod03389() {
         return values();
      }

      public static AbstractPathStep.InternalType0058 internalMethod02752(String localValue0) {
         return Enum.valueOf(AbstractPathStep.InternalType0058.class, localValue0);
      }
   }
}
