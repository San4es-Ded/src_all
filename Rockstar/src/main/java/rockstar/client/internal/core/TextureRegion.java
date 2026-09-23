package rockstar.client.internal.core;


import rockstar.client.*;
import net.minecraft.util.Identifier;

public final class TextureRegion {
   private final Identifier internalField0354;
   private final float internalField0205;
   private final float internalField0206;
   private final float internalField1048;
   private final float internalField1047;
   private final int internalField0227;
   private final int internalField0228;

   public TextureRegion(Identifier localValue1, float localValue2, float localValue3, float localValue4, float localValue5, int localValue6, int localValue7) {
      this.internalField0354 = localValue1;
      this.internalField0205 = localValue2;
      this.internalField0206 = localValue3;
      this.internalField1048 = localValue4;
      this.internalField1047 = localValue5;
      this.internalField0227 = localValue6;
      this.internalField0228 = localValue7;
   }

   @Override
   public final String toString() {
      return "typedParameter1034[texture=" + this.internalField0354 + ", u1=" + this.internalField0205 + ", v1=" + this.internalField0206 + ", u2=" + this.internalField1048 + ", v2=" + this.internalField1047 + ", width=" + this.internalField0227 + ", height=" + this.internalField0228 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0354);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      TextureRegion other = (TextureRegion) localValue1;
      return java.util.Objects.equals(this.internalField0354, other.internalField0354)
         && java.util.Objects.equals(this.internalField0205, other.internalField0205)
         && java.util.Objects.equals(this.internalField0206, other.internalField0206)
         && java.util.Objects.equals(this.internalField1048, other.internalField1048)
         && java.util.Objects.equals(this.internalField1047, other.internalField1047)
         && java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0228, other.internalField0228);
   }

   public Identifier internalMethod05466() {
      return this.internalField0354;
   }

   public float internalMethod06427() {
      return this.internalField0205;
   }

   public float internalMethod06429() {
      return this.internalField0206;
   }

   public float internalMethod08077() {
      return this.internalField1048;
   }

   public float internalMethod08079() {
      return this.internalField1047;
   }

   public int internalMethod06428() {
      return this.internalField0227;
   }

   public int internalMethod06430() {
      return this.internalField0228;
   }
}
