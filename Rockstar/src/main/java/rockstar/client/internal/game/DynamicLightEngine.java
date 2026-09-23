package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.BlockRenderView;

public final class DynamicLightEngine {
   private static final Direction[] internalField0470 = Direction.values();
   private static final DynamicLightEngine.InternalType0082 internalField0030 = new DynamicLightEngine.InternalType0082(0, 0, 0, 0, new byte[0]);
   private static volatile DynamicLightEngine.InternalType0082 internalField0031 = internalField0030;

   private DynamicLightEngine() {
   }

   public static void internalMethod03622(BlockRenderView localValue0, BlockPos localValue1, float localValue2, int localValue3) {
      int localValue4 = MathHelper.ceil(localValue2);
      if (localValue4 > 0 && localValue3 > 0) {
         int localValue5 = localValue4 * 2 + 1;
         int localValue6 = localValue5 * localValue5 * localValue5;
         int localValue7 = localValue1.getX() - localValue4;
         int localValue8 = localValue1.getY() - localValue4;
         int localValue9 = localValue1.getZ() - localValue4;
         byte[] localValue10 = new byte[localValue6];
         byte[] localValue11 = new byte[localValue6];
         boolean[] localValue12 = new boolean[localValue6];
         int[] localValue13 = new int[localValue6];
         int localValue14 = internalMethod07560(localValue4, localValue4, localValue4, localValue5);
         int localValue15 = 0;
         int localValue16 = 1;
         Mutable localValue17 = new Mutable();
         localValue13[0] = localValue14;
         localValue12[localValue14] = true;

         while (localValue15 < localValue16) {
            int localValue18 = localValue13[localValue15++];
            int localValue19 = localValue18 % localValue5;
            int localValue20 = localValue18 / localValue5 % localValue5;
            int localValue21 = localValue18 / (localValue5 * localValue5);
            int localValue22 = Byte.toUnsignedInt(localValue11[localValue18]);
            localValue10[localValue18] = (byte)MathHelper.clamp(Math.round(localValue3 * Math.max(0.0F, localValue2 + 1.0F - localValue22) / (localValue2 + 1.0F)), 0, 15);
            if (localValue22 < localValue4) {
               for (Direction localValue26 : internalField0470) {
                  int localValue27 = localValue19 + localValue26.getOffsetX();
                  int localValue28 = localValue20 + localValue26.getOffsetY();
                  int localValue29 = localValue21 + localValue26.getOffsetZ();
                  if (localValue27 >= 0 && localValue28 >= 0 && localValue29 >= 0 && localValue27 < localValue5 && localValue28 < localValue5 && localValue29 < localValue5) {
                     int localValue30 = internalMethod07560(localValue27, localValue28, localValue29, localValue5);
                     if (!localValue12[localValue30]) {
                        int localValue31 = localValue22 + 1;
                        if (!(localValue31 > localValue2)) {
                           localValue12[localValue30] = true;
                           localValue11[localValue30] = (byte)localValue31;
                           localValue10[localValue30] = (byte)MathHelper.clamp(Math.round(localValue3 * (localValue2 + 1.0F - localValue31) / (localValue2 + 1.0F)), 0, 15);
                           localValue17.set(localValue7 + localValue27, localValue8 + localValue28, localValue9 + localValue29);
                           if (!localValue0.getBlockState(localValue17).isOpaqueFullCube()) {
                              localValue13[localValue16++] = localValue30;
                           }
                        }
                     }
                  }
               }
            }
         }

         internalField0031 = new DynamicLightEngine.InternalType0082(localValue7, localValue8, localValue9, localValue5, localValue10);
      } else {
         internalMethod00949();
      }
   }

   public static void internalMethod00949() {
      internalField0031 = internalField0030;
   }

   public static int internalMethod07614(BlockPos localValue0, int localValue1) {
      int localValue2 = localValue1 >> 4 & 15;
      int localValue3 = internalField0031.internalMethod01712(localValue0);
      return localValue3 <= localValue2 ? localValue1 : localValue1 & -241 | localValue3 << 4;
   }

   public static int internalMethod01092(BlockPos localValue0, int localValue1) {
      return Math.max(localValue1, internalField0031.internalMethod01712(localValue0));
   }

   public static int internalMethod07560(int localValue0, int localValue1, int localValue2, int localValue3) {
      return (localValue2 * localValue3 + localValue1) * localValue3 + localValue0;
   }

   static final class InternalType0082 {
      private final int internalField0227;
      private final int internalField0228;
      private final int internalField1053;
      private final int internalField1055;
      private final byte[] internalField0609;

      InternalType0082(int localValue1, int localValue2, int localValue3, int localValue4, byte[] localValue5) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
         this.internalField1053 = localValue3;
         this.internalField1055 = localValue4;
         this.internalField0609 = localValue5;
      }

      int internalMethod01712(BlockPos localValue1) {
         int localValue2 = localValue1.getX() - this.internalField0227;
         int localValue3 = localValue1.getY() - this.internalField0228;
         int localValue4 = localValue1.getZ() - this.internalField1053;
         return localValue2 >= 0 && localValue3 >= 0 && localValue4 >= 0 && localValue2 < this.internalField1055 && localValue3 < this.internalField1055 && localValue4 < this.internalField1055
            ? Byte.toUnsignedInt(this.internalField0609[DynamicLightEngine.internalMethod07560(localValue2, localValue3, localValue4, this.internalField1055)])
            : 0;
      }

      @Override
      public final String toString() {
         return "InternalType0082[minX=" + this.internalField0227 + ", minY=" + this.internalField0228 + ", minZ=" + this.internalField1053 + ", size=" + this.internalField1055 + ", levels=" + this.internalField0609 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0609);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         DynamicLightEngine.InternalType0082 other = (DynamicLightEngine.InternalType0082) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053)
            && java.util.Objects.equals(this.internalField1055, other.internalField1055)
            && java.util.Objects.equals(this.internalField0609, other.internalField0609);
      }

      public int internalMethod01033() {
         return this.internalField0227;
      }

      public int internalMethod01036() {
         return this.internalField0228;
      }

      public int internalMethod08569() {
         return this.internalField1053;
      }

      public int internalMethod08570() {
         return this.internalField1055;
      }

      public byte[] internalMethod04683() {
         return this.internalField0609;
      }
   }
}
