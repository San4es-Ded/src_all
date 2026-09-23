package rockstar.client.internal.core;


import rockstar.client.*;
public final class SwingTransform {
   private final float internalField0205;
   private final float internalField0206;
   private final float internalField1048;
   private final float internalField1047;
   private final float internalField1049;
   private final float internalField1046;
   private final float internalField1456;
   private final float internalField1457;
   private final float internalField1458;

   public SwingTransform(float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9) {
      this.internalField0205 = localValue1;
      this.internalField0206 = localValue2;
      this.internalField1048 = localValue3;
      this.internalField1047 = localValue4;
      this.internalField1049 = localValue5;
      this.internalField1046 = localValue6;
      this.internalField1456 = localValue7;
      this.internalField1457 = localValue8;
      this.internalField1458 = localValue9;
   }

   @Override
   public final String toString() {
      return "typedParameter1023[anchorX=" + this.internalField0205 + ", anchorY=" + this.internalField0206 + ", anchorZ=" + this.internalField1048 + ", moveX=" + this.internalField1047 + ", moveY=" + this.internalField1049 + ", moveZ=" + this.internalField1046 + ", rotateX=" + this.internalField1456 + ", rotateY=" + this.internalField1457 + ", rotateZ=" + this.internalField1458 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1458);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      SwingTransform other = (SwingTransform) localValue1;
      return java.util.Objects.equals(this.internalField0205, other.internalField0205)
         && java.util.Objects.equals(this.internalField0206, other.internalField0206)
         && java.util.Objects.equals(this.internalField1048, other.internalField1048)
         && java.util.Objects.equals(this.internalField1047, other.internalField1047)
         && java.util.Objects.equals(this.internalField1049, other.internalField1049)
         && java.util.Objects.equals(this.internalField1046, other.internalField1046)
         && java.util.Objects.equals(this.internalField1456, other.internalField1456)
         && java.util.Objects.equals(this.internalField1457, other.internalField1457)
         && java.util.Objects.equals(this.internalField1458, other.internalField1458);
   }

   public float internalMethod01052() {
      return this.internalField0205;
   }

   public float internalMethod01057() {
      return this.internalField0206;
   }

   public float internalMethod08670() {
      return this.internalField1048;
   }

   public float internalMethod08672() {
      return this.internalField1047;
   }

   public float internalMethod08693() {
      return this.internalField1049;
   }

   public float internalMethod08695() {
      return this.internalField1046;
   }

   public float internalMethod09416() {
      return this.internalField1456;
   }

   public float internalMethod09417() {
      return this.internalField1457;
   }

   public float internalMethod09426() {
      return this.internalField1458;
   }
}
