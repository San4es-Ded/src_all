package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class RadiusGoal implements PathGoal {
   private final int internalField0227;
   private final int internalField0228;
   private final int internalField1053;
   private final int internalField1055;

   public RadiusGoal(BlockPos localValue1, int localValue2) {
      this(localValue1.getX(), localValue1.getY(), localValue1.getZ(), localValue2);
   }

   public RadiusGoal(int localValue1, int localValue2, int localValue3, int localValue4) {
      this.internalField0227 = localValue1;
      this.internalField0228 = localValue2;
      this.internalField1053 = localValue3;
      this.internalField1055 = localValue4;
   }

   @Override
   public boolean internalMethod05088(int localValue1, int localValue2, int localValue3) {
      long localValue4 = this.internalField0227 - localValue1;
      long localValue6 = this.internalField0228 - localValue2;
      long localValue8 = this.internalField1053 - localValue3;
      return localValue4 * localValue4 + localValue6 * localValue6 + localValue8 * localValue8 <= (long)this.internalField1055 * this.internalField1055;
   }

   @Override
   public double internalMethod05087(int localValue1, int localValue2, int localValue3) {
      double localValue4 = this.internalField0227 - localValue1;
      double localValue6 = this.internalField0228 - localValue2;
      double localValue8 = this.internalField1053 - localValue3;
      double localValue10 = Math.sqrt(localValue4 * localValue4 + localValue6 * localValue6 + localValue8 * localValue8);
      return Math.max(0.0, localValue10 - this.internalField1055);
   }

   @Override
   public Vec3d internalMethod07298() {
      return new Vec3d(this.internalField0227 + 0.5, this.internalField0228, this.internalField1053 + 0.5);
   }

   @Override
   public final String toString() {
      return "typedParameter1042[x=" + this.internalField0227 + ", y=" + this.internalField0228 + ", z=" + this.internalField1053 + ", radius=" + this.internalField1055 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      RadiusGoal other = (RadiusGoal) localValue1;
      return java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0228, other.internalField0228)
         && java.util.Objects.equals(this.internalField1053, other.internalField1053)
         && java.util.Objects.equals(this.internalField1055, other.internalField1055);
   }

   public int internalMethod01182() {
      return this.internalField0227;
   }

   public int internalMethod01184() {
      return this.internalField0228;
   }

   public int internalMethod07833() {
      return this.internalField1053;
   }

   public int internalMethod07834() {
      return this.internalField1055;
   }
}
