package rockstar.client.command;


import rockstar.client.*;
import java.util.List;

public final class ParsedCommand {
   private final CommandNode internalField0418;
   private final List<Object> internalField0416;

   public ParsedCommand(CommandNode localValue1, List<Object> localValue2) {
      this.internalField0418 = localValue1;
      this.internalField0416 = localValue2;
   }

   @Override
   public final String toString() {
      return "typedParameter1025[command=" + this.internalField0418 + ", arguments=" + this.internalField0416 + "]";
   }

   @Override
   public final int hashCode() {
      int result = 0;
      result = 31 * result + java.util.Objects.hashCode(this.internalField0418);
      result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
      return result;
   }

   @Override
   public final boolean equals(Object localValue1) {
      if (this == localValue1) return true;
      if (localValue1 == null || getClass() != localValue1.getClass()) return false;
      ParsedCommand other = (ParsedCommand) localValue1;
      return java.util.Objects.equals(this.internalField0418, other.internalField0418)
         && java.util.Objects.equals(this.internalField0416, other.internalField0416);
   }

   public CommandNode internalMethod01018() {
      return this.internalField0418;
   }

   public List<Object> internalMethod02266() {
      return this.internalField0416;
   }
}
