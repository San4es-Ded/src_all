package rockstar.client.internal.core;


import rockstar.client.*;
public final class ServerSlotReference {
   private final int internalField0227;
   private final ServerModeCategory internalField0584;
   private final int internalField0228;
   private final int internalField1053;

   public ServerSlotReference(int localValue1, ServerModeCategory localValue2, int localValue3, int localValue4) {
      this.internalField0227 = localValue1;
      this.internalField0584 = localValue2;
      this.internalField0228 = localValue3;
      this.internalField1053 = localValue4;
   }

   @Override
   public final String toString() {
      return "typedParameter1032[number=" + this.internalField0227 + ", category=" + this.internalField0584 + ", categorySlot=" + this.internalField0228 + ", serverSlot=" + this.internalField1053 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0584);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      ServerSlotReference other = (ServerSlotReference) localValue1;
      return java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0584, other.internalField0584)
         && java.util.Objects.equals(this.internalField0228, other.internalField0228)
         && java.util.Objects.equals(this.internalField1053, other.internalField1053);
   }

   public int internalMethod05526() {
      return this.internalField0227;
   }

   public ServerModeCategory internalMethod04794() {
      return this.internalField0584;
   }

   public int internalMethod05528() {
      return this.internalField0228;
   }

   public int internalMethod08512() {
      return this.internalField1053;
   }
}
