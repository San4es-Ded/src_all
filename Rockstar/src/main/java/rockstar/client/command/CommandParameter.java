package rockstar.client.command;


import rockstar.client.*;
public final class CommandParameter<T> {
   private final String internalField0248;
   private final boolean internalField0277;
   private final boolean internalField0276;
   private final CommandValidator internalField0829;

   public CommandParameter(String localValue1, boolean localValue2, boolean localValue3, CommandValidator localValue4) {
      this.internalField0248 = localValue1;
      this.internalField0277 = localValue2;
      this.internalField0276 = localValue3;
      this.internalField0829 = localValue4;
   }

   @Override
   public final String toString() {
      return "typedParameter1026[name=" + this.internalField0248 + ", required=" + this.internalField0277 + ", vararg=" + this.internalField0276 + ", validator=" + this.internalField0829 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0276);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0829);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      CommandParameter other = (CommandParameter) localValue1;
      return java.util.Objects.equals(this.internalField0248, other.internalField0248)
         && java.util.Objects.equals(this.internalField0277, other.internalField0277)
         && java.util.Objects.equals(this.internalField0276, other.internalField0276)
         && java.util.Objects.equals(this.internalField0829, other.internalField0829);
   }

   public String internalMethod01708() {
      return this.internalField0248;
   }

   public boolean internalMethod06679() {
      return this.internalField0277;
   }

   public boolean internalMethod06683() {
      return this.internalField0276;
   }

   public CommandValidator internalMethod05200() {
      return this.internalField0829;
   }
}
