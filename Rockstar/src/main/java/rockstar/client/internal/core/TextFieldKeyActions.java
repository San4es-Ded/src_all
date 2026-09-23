package rockstar.client.internal.core;


import rockstar.client.*;
public final class TextFieldKeyActions {
   private final Runnable internalField0659;
   private final Runnable internalField0658;

   public TextFieldKeyActions(Runnable localValue1, Runnable localValue2) {
      this.internalField0659 = localValue1;
      this.internalField0658 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1030[enter=" + this.internalField0659 + ", tab=" + this.internalField0658 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0659);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0658);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      TextFieldKeyActions other = (TextFieldKeyActions) localValue1;
      return java.util.Objects.equals(this.internalField0659, other.internalField0659)
         && java.util.Objects.equals(this.internalField0658, other.internalField0658);
   }

   public Runnable internalMethod05575() {
      return this.internalField0659;
   }

   public Runnable internalMethod00875() {
      return this.internalField0658;
   }
}
