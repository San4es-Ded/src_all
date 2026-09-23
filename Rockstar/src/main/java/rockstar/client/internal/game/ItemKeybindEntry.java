package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.item.Item;

public final class ItemKeybindEntry {
   private final Item internalField0152;
   private final int internalField0227;

   public ItemKeybindEntry(Item localValue1, int localValue2) {
      this.internalField0152 = localValue1;
      this.internalField0227 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1031[item=" + this.internalField0152 + ", key=" + this.internalField0227 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0152);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      ItemKeybindEntry other = (ItemKeybindEntry) localValue1;
      return java.util.Objects.equals(this.internalField0152, other.internalField0152)
         && java.util.Objects.equals(this.internalField0227, other.internalField0227);
   }

   public Item internalMethod05091() {
      return this.internalField0152;
   }

   public int internalMethod04702() {
      return this.internalField0227;
   }
}
