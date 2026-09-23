package rockstar.client.internal.rotation;




import rockstar.client.rotation.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public final class EntityMotionSnapshot {
   private final Vec3d internalField0283;
   private final Vec3d internalField0282;
   private final Rotation internalField0118;
   private final boolean internalField0277;
   private final Box internalField0681;

   public EntityMotionSnapshot(Vec3d localValue1, Vec3d localValue2, Rotation localValue3, boolean localValue4, Box localValue5) {
      this.internalField0283 = localValue1;
      this.internalField0282 = localValue2;
      this.internalField0118 = localValue3;
      this.internalField0277 = localValue4;
      this.internalField0681 = localValue5;
   }

   @Override
   public final String toString() {
      return "typedParameter1024[pos=" + this.internalField0283 + ", motion=" + this.internalField0282 + ", rotation=" + this.internalField0118 + ", onGround=" + this.internalField0277 + ", box=" + this.internalField0681 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0282);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0118);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0681);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      EntityMotionSnapshot other = (EntityMotionSnapshot) localValue1;
      return java.util.Objects.equals(this.internalField0283, other.internalField0283)
         && java.util.Objects.equals(this.internalField0282, other.internalField0282)
         && java.util.Objects.equals(this.internalField0118, other.internalField0118)
         && java.util.Objects.equals(this.internalField0277, other.internalField0277)
         && java.util.Objects.equals(this.internalField0681, other.internalField0681);
   }

   public Vec3d internalMethod00677() {
      return this.internalField0283;
   }

   public Vec3d internalMethod05854() {
      return this.internalField0282;
   }

   public Rotation internalMethod02774() {
      return this.internalField0118;
   }

   public boolean internalMethod01146() {
      return this.internalField0277;
   }

   public Box internalMethod07420() {
      return this.internalField0681;
   }
}
