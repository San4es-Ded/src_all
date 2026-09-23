package rockstar.client.internal.core;


import rockstar.client.*;
public final class NewtonTaskStartedEvent implements NewtonTaskEvent {
   private final String internalField0248;
   private final int internalField0227;

   public NewtonTaskStartedEvent(String localValue1, int localValue2) {
      this.internalField0248 = localValue1;
      this.internalField0227 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1038[processName=" + this.internalField0248 + ", totalSteps=" + this.internalField0227 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      NewtonTaskStartedEvent other = (NewtonTaskStartedEvent) localValue1;
      return java.util.Objects.equals(this.internalField0248, other.internalField0248)
         && java.util.Objects.equals(this.internalField0227, other.internalField0227);
   }

   public String internalMethod06658() {
      return this.internalField0248;
   }

   public int internalMethod00163() {
      return this.internalField0227;
   }
}
