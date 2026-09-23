package rockstar.client.internal.core;



import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.List;

public final class AssistCategoryGroup {
   private final String internalField0248;
   private final List<AssistItemPickerEntry> internalField0416;

   public AssistCategoryGroup(String localValue1, List<AssistItemPickerEntry> localValue2) {
      this.internalField0248 = localValue1;
      this.internalField0416 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1021[title=" + this.internalField0248 + ", items=" + this.internalField0416 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      AssistCategoryGroup other = (AssistCategoryGroup) localValue1;
      return java.util.Objects.equals(this.internalField0248, other.internalField0248)
         && java.util.Objects.equals(this.internalField0416, other.internalField0416);
   }

   public String internalMethod00302() {
      return this.internalField0248;
   }

   public List<AssistItemPickerEntry> internalMethod03025() {
      return this.internalField0416;
   }
}
