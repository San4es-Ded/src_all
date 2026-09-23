package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.util.math.Vec3d;

public final class ColumnGoal implements PathGoal {
   private final int internalField0227;
   private final int internalField0228;

   public ColumnGoal(int localValue1, int localValue2) {
      this.internalField0227 = localValue1;
      this.internalField0228 = localValue2;
   }

   @Override
   public boolean internalMethod05088(int localValue1, int localValue2, int localValue3) {
      return this.internalField0227 == localValue1 && this.internalField0228 == localValue3;
   }

   @Override
   public double internalMethod05087(int localValue1, int localValue2, int localValue3) {
      double localValue4 = this.internalField0227 - localValue1;
      double localValue6 = this.internalField0228 - localValue3;
      double localValue8 = Math.abs(localValue4);
      double localValue10 = Math.abs(localValue6);
      double localValue12 = Math.min(localValue8, localValue10);
      double localValue14 = Math.abs(localValue8 - localValue10);
      return localValue12 * 1.41421356 + localValue14;
   }

   @Override
   public Vec3d internalMethod07298() {
      return new Vec3d(this.internalField0227 + 0.5, 64.0, this.internalField0228 + 0.5);
   }

   @Override
   public final String toString() {
      return "typedParameter1043[x=" + this.internalField0227 + ", z=" + this.internalField0228 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      ColumnGoal other = (ColumnGoal) localValue1;
      return java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0228, other.internalField0228);
   }

   public int internalMethod02844() {
      return this.internalField0227;
   }

   public int internalMethod02848() {
      return this.internalField0228;
   }
}
