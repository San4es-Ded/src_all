package rockstar.client.internal.core;


import rockstar.client.*;
public final class NewtonTaskFinishedEvent implements NewtonTaskEvent {
   private final String internalField0248;

   public NewtonTaskFinishedEvent(String localValue1) {
      this.internalField0248 = localValue1;
   }

   @Override
   public final String toString() {
      return "typedParameter1037[processName=" + this.internalField0248 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      NewtonTaskFinishedEvent other = (NewtonTaskFinishedEvent) localValue1;
      return java.util.Objects.equals(this.internalField0248, other.internalField0248);
   }

   public String internalMethod04436() {
      return this.internalField0248;
   }
}
