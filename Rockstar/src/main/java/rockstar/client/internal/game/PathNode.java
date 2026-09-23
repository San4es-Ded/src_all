package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.util.math.BlockPos;

public final class PathNode {
   private final int internalField0227;
   private final int internalField0228;
   private final int internalField1053;

   public PathNode(int localValue1, int localValue2, int localValue3) {
      this.internalField0227 = localValue1;
      this.internalField0228 = localValue2;
      this.internalField1053 = localValue3;
   }

   public BlockPos internalMethod05000() {
      return new BlockPos(this.internalField0227, this.internalField0228, this.internalField1053);
   }

   public static PathNode internalMethod05159(BlockPos localValue0) {
      return new PathNode(localValue0.getX(), localValue0.getY(), localValue0.getZ());
   }

   @Override
   public final String toString() {
      return "typedParameter1040[x=" + this.internalField0227 + ", y=" + this.internalField0228 + ", z=" + this.internalField1053 + "]";
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
      PathNode other = (PathNode) localValue1;
      return java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0228, other.internalField0228)
         && java.util.Objects.equals(this.internalField1053, other.internalField1053);
   }

   public int internalMethod02945() {
      return this.internalField0227;
   }

   public int internalMethod02949() {
      return this.internalField0228;
   }

   public int internalMethod07945() {
      return this.internalField1053;
   }
}
