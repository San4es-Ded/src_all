package rockstar.client.internal.core;



import rockstar.client.internal.inventory.*;
import rockstar.client.*;
public final class AssistItemTemplate {
   private final AssistItemPickerEntry internalField0075;

   public AssistItemTemplate(AssistItemPickerEntry localValue1) {
      this.internalField0075 = localValue1;
   }

   @Override
   public final String toString() {
      return "typedParameter1022[template=" + this.internalField0075 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0075);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      AssistItemTemplate other = (AssistItemTemplate) localValue1;
      return java.util.Objects.equals(this.internalField0075, other.internalField0075);
   }

   public AssistItemPickerEntry internalMethod07428() {
      return this.internalField0075;
   }
}
