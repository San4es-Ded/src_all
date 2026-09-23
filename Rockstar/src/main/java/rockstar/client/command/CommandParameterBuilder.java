package rockstar.client.command;




import rockstar.client.module.*;
import rockstar.client.core.*;
import rockstar.client.*;
import rockstar.client.internal.core.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandParameterBuilder<T> {
   private final String internalField0248;
   private boolean internalField0277 = true;
   private boolean internalField0276 = false;
   private CommandValidator internalField0828 = OperationResult::internalMethod00116;
   private List<String> internalField0416;
   public static final CommandValidator internalField0829 = localValue0 -> {
      try {
         ModuleEntry localValue1 = RockstarClient.getInstance().getModuleManager().getModuleByName(localValue0);
         return (OperationResult)(!localValue1.isAvailable()
            ? OperationResult.internalMethod05941("Module with name '%s' was not found".formatted(localValue0))
            : OperationResult.internalMethod00116(localValue1));
      } catch (ElementLookupException localValue2) {
         return OperationResult.internalMethod05941("Module with name '%s' was not found".formatted(localValue0));
      }
   };

   private CommandParameterBuilder(String localValue1) {
      this.internalField0248 = localValue1;
   }

   public static <T> CommandParameterBuilder<T> internalMethod03710(String localValue0) {
      return new CommandParameterBuilder<>(localValue0);
   }

   public final CommandParameterBuilder<T> internalMethod06921() {
      this.internalField0277 = false;
      return this;
   }

   public final CommandParameterBuilder<T> internalMethod00125() {
      this.internalField0276 = true;
      return this;
   }

   public final CommandParameterBuilder<T> internalMethod00776(CommandValidator localValue1) {
      this.internalField0828 = localValue1;
      return this;
   }

   public final void internalMethod07138(String... localValue1) {
      if (localValue1 != null && localValue1.length != 0) {
         this.internalField0416 = List.of(localValue1);
      } else {
         this.internalField0416 = List.of();
      }
   }

   public final void internalMethod05362(List<String> localValue1) {
      this.internalField0416 = localValue1 == null ? List.of() : List.copyOf(localValue1);
   }

   public CommandParameterBuilder<T> internalMethod04818(final String... localValue1) {
      final String localValue2 = Arrays.stream(localValue1).map(localValue0 -> "'" + localValue0 + "'").collect(Collectors.joining(", "));
      this.internalField0828 = new CommandValidator() {
         @Override
         public OperationResult validate(String localValue1x) {
            for (String localValue5 : localValue1) {
               if (localValue5.equalsIgnoreCase(localValue1x)) {
                  return OperationResult.internalMethod00116(localValue1x);
               }
            }

            return OperationResult.internalMethod05941("Expected one of: " + localValue2);
         }

         @Override
         public List<String> suggestions(String localValue1x) {
            return Arrays.stream(localValue1).filter(localValue1xxx -> localValue1xxx.toLowerCase().startsWith(localValue1x.toLowerCase())).toList();
         }
      };
      this.internalField0416 = List.of(localValue1);
      return this;
   }

   public final CommandParameter<T> internalMethod06920() {
      CommandValidator localValue1 = this.internalField0828 != null ? this.internalField0828 : OperationResult::internalMethod00116;
      if (this.internalField0416 != null && !this.internalField0416.isEmpty()) {
         localValue1 = new CommandParameterBuilder.InternalType0375(localValue1, this.internalField0416);
      }

      return new CommandParameter<>(this.internalField0248, this.internalField0277, this.internalField0276, localValue1);
   }

   static final class InternalType0375 implements CommandValidator {
      private final CommandValidator internalField0829;
      private final List<String> internalField0416;

      InternalType0375(CommandValidator localValue1, List<String> localValue2) {
         this.internalField0829 = localValue1;
         this.internalField0416 = localValue2;
      }

      @Override
      public final OperationResult validate(String localValue1) {
         return this.internalField0829.validate(localValue1);
      }

      @Override
      public final List<String> suggestions(String localValue1) {
         String localValue2 = localValue1.toLowerCase();
         return this.internalField0416.stream().filter(localValue1x -> localValue1x.toLowerCase().startsWith(localValue2)).toList();
      }
   }
}
