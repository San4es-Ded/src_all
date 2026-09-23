package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.List;

public final class ValidationResult {
   private final boolean internalField0277;
   private final List<String> internalField0416;
   public static final ValidationResult internalField0369 = new ValidationResult(true, null);

   public ValidationResult(boolean localValue1, List<String> localValue2) {
      this.internalField0277 = localValue1;
      this.internalField0416 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1033[valid=" + this.internalField0277 + ", errors=" + this.internalField0416 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      ValidationResult other = (ValidationResult) localValue1;
      return java.util.Objects.equals(this.internalField0277, other.internalField0277)
         && java.util.Objects.equals(this.internalField0416, other.internalField0416);
   }

   public boolean internalMethod05079() {
      return this.internalField0277;
   }

   public List<String> internalMethod01025() {
      return this.internalField0416;
   }
}
