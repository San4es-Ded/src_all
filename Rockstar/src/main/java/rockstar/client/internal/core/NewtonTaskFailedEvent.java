package rockstar.client.internal.core;


import rockstar.client.*;
public final class NewtonTaskFailedEvent implements NewtonTaskEvent {
   private final String internalField0248;
   private final String internalField0247;

   public NewtonTaskFailedEvent(String localValue1, String localValue2) {
      this.internalField0248 = localValue1;
      this.internalField0247 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1036[processName=" + this.internalField0248 + ", reason=" + this.internalField0247 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      NewtonTaskFailedEvent other = (NewtonTaskFailedEvent) localValue1;
      return java.util.Objects.equals(this.internalField0248, other.internalField0248)
         && java.util.Objects.equals(this.internalField0247, other.internalField0247);
   }

   public String internalMethod07366() {
      return this.internalField0248;
   }

   public String internalMethod03935() {
      return this.internalField0247;
   }
}
