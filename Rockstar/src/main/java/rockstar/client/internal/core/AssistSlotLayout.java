package rockstar.client.internal.core;



import rockstar.client.internal.inventory.*;
import rockstar.client.*;
public final class AssistSlotLayout {
   private final AssistSwapItem internalField0347;
   private final float internalField0205;
   private final float internalField0206;
   private final float internalField1048;
   private final float internalField1047;
   private final float internalField1049;
   private final float internalField1046;
   private final float internalField1456;
   private final float internalField1457;

   public AssistSlotLayout(AssistSwapItem localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9) {
      this.internalField0347 = localValue1;
      this.internalField0205 = localValue2;
      this.internalField0206 = localValue3;
      this.internalField1048 = localValue4;
      this.internalField1047 = localValue5;
      this.internalField1049 = localValue6;
      this.internalField1046 = localValue7;
      this.internalField1456 = localValue8;
      this.internalField1457 = localValue9;
   }

   @Override
   public final String toString() {
      return "typedParameter1019[item=" + this.internalField0347 + ", cx=" + this.internalField0205 + ", cy=" + this.internalField0206 + ", cw=" + this.internalField1048 + ", ch=" + this.internalField1047 + ", bindX=" + this.internalField1049 + ", bindY=" + this.internalField1046 + ", bindW=" + this.internalField1456 + ", bindH=" + this.internalField1457 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0347);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      AssistSlotLayout other = (AssistSlotLayout) localValue1;
      return java.util.Objects.equals(this.internalField0347, other.internalField0347)
         && java.util.Objects.equals(this.internalField0205, other.internalField0205)
         && java.util.Objects.equals(this.internalField0206, other.internalField0206)
         && java.util.Objects.equals(this.internalField1048, other.internalField1048)
         && java.util.Objects.equals(this.internalField1047, other.internalField1047)
         && java.util.Objects.equals(this.internalField1049, other.internalField1049)
         && java.util.Objects.equals(this.internalField1046, other.internalField1046)
         && java.util.Objects.equals(this.internalField1456, other.internalField1456)
         && java.util.Objects.equals(this.internalField1457, other.internalField1457);
   }

   public AssistSwapItem internalMethod05477() {
      return this.internalField0347;
   }

   public float internalMethod03960() {
      return this.internalField0205;
   }

   public float internalMethod03961() {
      return this.internalField0206;
   }

   public float internalMethod08964() {
      return this.internalField1048;
   }

   public float internalMethod08965() {
      return this.internalField1047;
   }

   public float internalMethod08978() {
      return this.internalField1049;
   }

   public float internalMethod08979() {
      return this.internalField1046;
   }

   public float internalMethod09187() {
      return this.internalField1456;
   }

   public float internalMethod09188() {
      return this.internalField1457;
   }
}
