package rockstar.client.render;


import rockstar.client.*;
public final class CornerRadii {
   private final float internalField0205;
   private final float internalField0206;
   private final float internalField1048;
   private final float internalField1047;
   public static final CornerRadii internalField0098 = new CornerRadii(0.0F, 0.0F, 0.0F, 0.0F);

   public CornerRadii(float localValue1, float localValue2, float localValue3, float localValue4) {
      this.internalField0205 = localValue1;
      this.internalField0206 = localValue2;
      this.internalField1048 = localValue3;
      this.internalField1047 = localValue4;
   }

   public static CornerRadii internalMethod03908(float localValue0) {
      return new CornerRadii(localValue0, localValue0, localValue0, localValue0);
   }

   public static CornerRadii internalMethod05307(float localValue0) {
      return new CornerRadii(localValue0, 0.0F, 0.0F, 0.0F);
   }

   public static CornerRadii internalMethod08836(float localValue0) {
      return new CornerRadii(0.0F, localValue0, 0.0F, 0.0F);
   }

   public static CornerRadii internalMethod09083(float localValue0) {
      return new CornerRadii(0.0F, 0.0F, localValue0, 0.0F);
   }

   public static CornerRadii internalMethod08982(float localValue0) {
      return new CornerRadii(0.0F, 0.0F, 0.0F, localValue0);
   }

   public static CornerRadii internalMethod04522(float localValue0, float localValue1) {
      return new CornerRadii(localValue0, localValue1, 0.0F, 0.0F);
   }

   public static CornerRadii internalMethod05385(float localValue0, float localValue1) {
      return new CornerRadii(0.0F, 0.0F, localValue1, localValue0);
   }

   public static CornerRadii internalMethod07937(float localValue0, float localValue1) {
      return new CornerRadii(localValue0, 0.0F, 0.0F, localValue1);
   }

   public static CornerRadii internalMethod08088(float localValue0, float localValue1) {
      return new CornerRadii(0.0F, localValue0, localValue1, 0.0F);
   }

   @Override
   public String toString() {
      return "BorderRadius{topLeftRadius="
         + this.internalField0205
         + ", topRightRadius="
         + this.internalField0206
         + ", bottomRightRadius="
         + this.internalField1048
         + ", bottomLeftRadius="
         + this.internalField1047
         + "}";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      CornerRadii other = (CornerRadii) localValue1;
      return java.util.Objects.equals(this.internalField0205, other.internalField0205)
         && java.util.Objects.equals(this.internalField0206, other.internalField0206)
         && java.util.Objects.equals(this.internalField1048, other.internalField1048)
         && java.util.Objects.equals(this.internalField1047, other.internalField1047);
   }

   public float internalMethod05337() {
      return this.internalField0205;
   }

   public float internalMethod05340() {
      return this.internalField0206;
   }

   public float internalMethod08939() {
      return this.internalField1048;
   }

   public float internalMethod08942() {
      return this.internalField1047;
   }
}
