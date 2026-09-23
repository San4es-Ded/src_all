package rockstar.client.internal.core;





import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.List;

public final class ComputedPath {
   private final List<PathNode> internalField0416;
   private final List<AbstractPathStep> internalField0417;

   public ComputedPath(List<PathNode> localValue1, List<AbstractPathStep> localValue2) {
      this.internalField0416 = localValue1;
      this.internalField0417 = localValue2;
   }

   public PathNode internalMethod00570() {
      return this.internalField0416.get(0);
   }

   public PathNode internalMethod01269() {
      return this.internalField0416.get(this.internalField0416.size() - 1);
   }

   public int internalMethod01819() {
      return this.internalField0417.size();
   }

   @Override
   public final String toString() {
      return "typedParameter1039[nodes=" + this.internalField0416 + ", movements=" + this.internalField0417 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0417);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      ComputedPath other = (ComputedPath) localValue1;
      return java.util.Objects.equals(this.internalField0416, other.internalField0416)
         && java.util.Objects.equals(this.internalField0417, other.internalField0417);
   }

   public List<PathNode> internalMethod05363() {
      return this.internalField0416;
   }

   public List<AbstractPathStep> internalMethod02878() {
      return this.internalField0417;
   }
}
