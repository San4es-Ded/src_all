package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class BlockPositionGoal implements PathGoal {
   private final int internalField0227;
   private final int internalField0228;
   private final int internalField1053;

   public BlockPositionGoal(BlockPos localValue1) {
      this(localValue1.getX(), localValue1.getY(), localValue1.getZ());
   }

   public BlockPositionGoal(int localValue1, int localValue2, int localValue3) {
      this.internalField0227 = localValue1;
      this.internalField0228 = localValue2;
      this.internalField1053 = localValue3;
   }

   @Override
   public boolean internalMethod05088(int localValue1, int localValue2, int localValue3) {
      return this.internalField0227 == localValue1 && this.internalField0228 == localValue2 && this.internalField1053 == localValue3;
   }

   @Override
   public double internalMethod05087(int localValue1, int localValue2, int localValue3) {
      double localValue4 = this.internalField0227 - localValue1;
      double localValue6 = this.internalField0228 - localValue2;
      double localValue8 = this.internalField1053 - localValue3;
      double localValue10 = Math.abs(localValue4);
      double localValue12 = Math.abs(localValue8);
      double localValue14 = Math.min(localValue10, localValue12);
      double localValue16 = Math.abs(localValue10 - localValue12);
      return localValue14 * 1.41421356 + localValue16 + Math.abs(localValue6);
   }

   @Override
   public Vec3d internalMethod07298() {
      return new Vec3d(this.internalField0227 + 0.5, this.internalField0228, this.internalField1053 + 0.5);
   }

   @Override
   public final String toString() {
      return "typedParameter1041[x=" + this.internalField0227 + ", y=" + this.internalField0228 + ", z=" + this.internalField1053 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      BlockPositionGoal other = (BlockPositionGoal) localValue1;
      return java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0228, other.internalField0228)
         && java.util.Objects.equals(this.internalField1053, other.internalField1053);
   }

   public int internalMethod04608() {
      return this.internalField0227;
   }

   public int internalMethod04643() {
      return this.internalField0228;
   }

   public int internalMethod07920() {
      return this.internalField1053;
   }
}
