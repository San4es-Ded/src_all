package rockstar.client.command;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import rockstar.profile.Role;

public class CommandBuilder {
   private final List<String> internalField0416 = new ArrayList<>();
   private String internalField0248 = "";
   private final List<CommandParameter<?>> internalField0417 = new ArrayList<>();
   private final List<CommandNode> internalField1145 = new ArrayList<>();
   private List<Role> internalField1146 = new ArrayList<>();
   private CommandHandler internalField0420;

   private CommandBuilder(String localValue1) {
      this.internalField0416.add(localValue1);
   }

   public static CommandBuilder internalMethod00593(String localValue0) {
      return new CommandBuilder(localValue0);
   }

   public static CommandBuilder internalMethod07482(String localValue0, Consumer<CommandBuilder> localValue1) {
      CommandBuilder localValue2 = new CommandBuilder(localValue0);
      localValue1.accept(localValue2);
      return localValue2;
   }

   public final CommandBuilder internalMethod05325(String... localValue1) {
      this.internalField0416.addAll(Arrays.asList(localValue1));
      return this;
   }

   public final CommandBuilder internalMethod04260(CommandNode... localValue1) {
      this.internalField1145.addAll(Arrays.asList(localValue1));
      return this;
   }

   public final CommandBuilder internalMethod06148(String localValue1) {
      this.internalField0248 = localValue1;
      return this;
   }

   public final <T> CommandBuilder internalMethod01539(String localValue1, Consumer<CommandParameterBuilder<T>> localValue2) {
      CommandParameterBuilder localValue3 = CommandParameterBuilder.internalMethod03710(localValue1);
      localValue2.accept(localValue3);
      this.internalField0417.add(localValue3.internalMethod06920());
      return this;
   }

   public final CommandBuilder internalMethod00262(CommandHandler localValue1) {
      this.internalField0420 = localValue1;
      return this;
   }

   public final CommandBuilder internalMethod03415(Role... localValue1) {
      this.internalField1146 = Arrays.asList(localValue1);
      return this;
   }

   public final CommandNode internalMethod04146() {
      boolean localValue1 = true;
      if (this.internalField0420 == null) {
         throw new IllegalStateException("Executable command requires handler");
      } else {
         return new CommandBuilder.InternalType0369(
            List.copyOf(this.internalField0416),
            this.internalField0248,
            List.copyOf(this.internalField0417),
            List.copyOf(this.internalField1145),
            List.copyOf(this.internalField1146),
            localValue1,
            this.internalField0420
         );
      }
   }

   static final class InternalType0369 implements CommandNode {
      private final List<String> internalField0416;
      private final String internalField0248;
      private final List<CommandParameter<?>> internalField0417;
      private final List<CommandNode> internalField1145;
      private final List<Role> internalField1146;
      private final boolean internalField0277;
      private final CommandHandler internalField0420;

      InternalType0369(
         List<String> localValue1, String localValue2, List<CommandParameter<?>> localValue3, List<CommandNode> localValue4, List<Role> localValue5, boolean localValue6, CommandHandler localValue7
      ) {
         this.internalField0416 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0417 = localValue3;
         this.internalField1145 = localValue4;
         this.internalField1146 = localValue5;
         this.internalField0277 = localValue6;
         this.internalField0420 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0369[names=" + this.internalField0416 + ", description=" + this.internalField0248 + ", parameters=" + this.internalField0417 + ", subcommands=" + this.internalField1145 + ", roles=" + this.internalField1146 + ", executable=" + this.internalField0277 + ", handler=" + this.internalField0420 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0417);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1145);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1146);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0420);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         CommandBuilder.InternalType0369 other = (CommandBuilder.InternalType0369) localValue1;
         return java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0417, other.internalField0417)
            && java.util.Objects.equals(this.internalField1145, other.internalField1145)
            && java.util.Objects.equals(this.internalField1146, other.internalField1146)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0420, other.internalField0420);
      }

      @Override
      public List<String> internalMethod03764() {
         return this.internalField0416;
      }

      @Override
      public String internalMethod03657() {
         return this.internalField0248;
      }

      @Override
      public List<CommandParameter<?>> internalMethod01430() {
         return this.internalField0417;
      }

      @Override
      public List<CommandNode> internalMethod08937() {
         return this.internalField1145;
      }

      @Override
      public List<Role> internalMethod08474() {
         return this.internalField1146;
      }

      @Override
      public boolean internalMethod03507() {
         return this.internalField0277;
      }

      @Override
      public CommandHandler internalMethod00835() {
         return this.internalField0420;
      }
   }
}
