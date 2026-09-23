package rockstar.client.internal.core;


import rockstar.client.*;
public final class ChatMacroEntry {
   private final int internalField0227;
   private final String internalField0248;

   public ChatMacroEntry(int localValue1, String localValue2) {
      this.internalField0227 = localValue1;
      this.internalField0248 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1028[keyCode=" + this.internalField0227 + ", command=" + this.internalField0248 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      ChatMacroEntry other = (ChatMacroEntry) localValue1;
      return java.util.Objects.equals(this.internalField0227, other.internalField0227)
         && java.util.Objects.equals(this.internalField0248, other.internalField0248);
   }

   public int internalMethod03890() {
      return this.internalField0227;
   }

   public String internalMethod07169() {
      return this.internalField0248;
   }
}
