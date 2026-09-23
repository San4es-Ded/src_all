package rockstar.client.internal.core;


import rockstar.client.*;
public final class NewtonTaskNodeEvent implements NewtonTaskEvent {
   private final String internalField0248;
   private final int internalField0227;
   private final int internalField0228;
   private final int internalField1053;
   private final int internalField1055;
   private final int internalField1056;

   public NewtonTaskNodeEvent(String localValue1, int localValue2, int localValue3, int localValue4, int localValue5, int localValue6) {
      this.internalField0248 = localValue1;
      this.internalField0227 = localValue2;
      this.internalField0228 = localValue3;
      this.internalField1053 = localValue4;
      this.internalField1055 = localValue5;
      this.internalField1056 = localValue6;
   }

   @Override
   public final String toString() {
      return "typedParameter1035[processName=" + this.internalField0248 + ", x=" + this.internalField0227 + ", y=" + this.internalField0228 + ", z=" + this.internalField1053 + ", stepIndex=" + this.internalField1055 + ", totalSteps=" + this.internalField1056 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
      result = 31 * result + java.util.Objects.hashCode(this.internalField1056);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      NewtonTaskNodeEvent other = (NewtonTaskNodeEvent) localValue1;
      return java.util.Objects.equals(this.internalField0248, other.internalField0248)
         && java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0228, other.internalField0228)
         && java.util.Objects.equals(this.internalField1053, other.internalField1053)
         && java.util.Objects.equals(this.internalField1055, other.internalField1055)
         && java.util.Objects.equals(this.internalField1056, other.internalField1056);
   }

   public String internalMethod05221() {
      return this.internalField0248;
   }

   public int internalMethod03220() {
      return this.internalField0227;
   }

   public int internalMethod03229() {
      return this.internalField0228;
   }

   public int internalMethod08034() {
      return this.internalField1053;
   }

   public int internalMethod08035() {
      return this.internalField1055;
   }

   public int internalMethod08046() {
      return this.internalField1056;
   }
}
