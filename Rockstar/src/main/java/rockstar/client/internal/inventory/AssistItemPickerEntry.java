package rockstar.client.internal.inventory;


import rockstar.client.*;
import net.minecraft.item.ItemStack;

public final class AssistItemPickerEntry {
   private final AssistSwapItem internalField0347;
   private final String internalField0248;
   private final ItemStack internalField0878;
   private final int internalField0227;

   public AssistItemPickerEntry(AssistSwapItem localValue1, String localValue2, ItemStack localValue3, int localValue4) {
      this.internalField0347 = localValue1;
      this.internalField0248 = localValue2;
      this.internalField0878 = localValue3;
      this.internalField0227 = localValue4;
   }

   @Override
   public final String toString() {
      return "typedParameter1020[item=" + this.internalField0347 + ", name=" + this.internalField0248 + ", icon=" + this.internalField0878 + ", defaultKey=" + this.internalField0227 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0347);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      AssistItemPickerEntry other = (AssistItemPickerEntry) localValue1;
      return java.util.Objects.equals(this.internalField0347, other.internalField0347)
         && java.util.Objects.equals(this.internalField0248, other.internalField0248)
         && java.util.Objects.equals(this.internalField0878, other.internalField0878)
         && java.util.Objects.equals(this.internalField0227, other.internalField0227);
   }

   public AssistSwapItem internalMethod04960() {
      return this.internalField0347;
   }

   public String internalMethod06189() {
      return this.internalField0248;
   }

   public ItemStack internalMethod02770() {
      return this.internalField0878;
   }

   public int internalMethod05339() {
      return this.internalField0227;
   }
}
