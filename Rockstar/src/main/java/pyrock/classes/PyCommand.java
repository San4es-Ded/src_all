package pyrock.classes;








import rockstar.client.util.*;
import rockstar.client.module.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.command.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import jep.python.PyCallable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.command.CommandValidator;
import rockstar.client.core.OperationResult;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.script.EmbeddedPythonRuntime;
import rockstar.client.module.ModuleEntry;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.game.BlockSuggestionProvider;

public class PyCommand {
   private final PyCommand root;
   private final PythonScript owner;
   private final String name;
   private final List<String> aliases = new ArrayList<>();
   private final List<PyCommand.InternalType0429> args = new ArrayList<>();
   private final List<PyCommand> subs = new ArrayList<>();
   private String desc = "";
   @Nullable
   private PyCallable handler;
   @Nullable
   private CommandNode registered;

   public PyCommand(String localValue1) {
      this(localValue1, null);
   }

   private PyCommand(String localValue1, @Nullable PyCommand localValue2) {
      this.name = localValue1.toLowerCase(Locale.ROOT);
      this.root = localValue2 == null ? this : localValue2;
      this.owner = localValue2 == null ? PythonScript.internalMethod00581() : localValue2.owner;
   }

   public PyCommand alias(String localValue1) {
      if (localValue1 != null && !localValue1.isBlank()) {
         this.aliases.add(localValue1.toLowerCase(Locale.ROOT));
      }

      return this;
   }

   public PyCommand desc(String localValue1) {
      this.desc = localValue1 == null ? "" : localValue1;
      return this;
   }

   public PyCommand handler(PyCallable localValue1) {
      this.handler = localValue1;
      return this;
   }

   public PyCommand arg(String localValue1, String localValue2, boolean localValue3, boolean localValue4, Object localValue5) {
      ArrayList localValue6 = null;
      PyCallable localValue7 = null;
      if (localValue5 instanceof PyCallable localValue8) {
         localValue7 = localValue8;
      } else if (localValue5 instanceof List localValue9) {
         localValue6 = new ArrayList();

         for (Object localValue11 : localValue9) {
            if (localValue11 != null) {
               localValue6.add(String.valueOf(localValue11));
            }
         }
      }

      this.args.add(new PyCommand.InternalType0429(localValue1, localValue2 == null ? "str" : localValue2.toLowerCase(Locale.ROOT), localValue3, localValue4, localValue7, localValue6));
      return this;
   }

   public PyCommand sub(String localValue1) {
      PyCommand localValue2 = new PyCommand(localValue1, this.root);
      this.subs.add(localValue2);
      return localValue2;
   }

   public String name() {
      return this.name;
   }

   public PyCommand install() {
      if (this.root != this) {
         return this.root.install();
      } else {
         CommandRegistry localValue1 = RockstarClient.getInstance().internalMethod05348();
         if (this.registered == null) {
            String localValue2 = this.firstTakenName(localValue1);
            if (localValue2 != null) {
               throw new IllegalStateException(
                  "\u043a\u043e\u043c\u0430\u043d\u0434\u0430 '"
                     + localValue2
                     + "' \u0443\u0436\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442 \u0432 \u043a\u043b\u0438\u0435\u043d\u0442\u0435"
               );
            }
         } else {
            localValue1.internalMethod01108(this.registered);
         }

         this.registered = this.build();
         localValue1.internalMethod01107(this.registered);
         PythonScript.internalMethod02562(this);
         return this;
      }
   }

   public boolean remove() {
      if (this.root != this) {
         return this.root.remove();
      } else if (this.registered == null) {
         return false;
      } else {
         RockstarClient.getInstance().internalMethod05348().internalMethod01108(this.registered);
         this.registered = null;
         return true;
      }
   }

   public boolean installed() {
      return this.root.registered != null;
   }

   @Nullable
   private String firstTakenName(CommandRegistry localValue1) {
      ArrayList localValue2 = new ArrayList();
      localValue2.add(this.name);
      localValue2.addAll(this.aliases);

      for (CommandNode localValue4 : localValue1.internalMethod05967()) {
         if (localValue4 != this.registered) {
            for (String localValue6 : localValue4.internalMethod03764()) {
               for (String localValue8 : (Iterable<String>)(Iterable<?>)localValue2) {
                  if (localValue6.equalsIgnoreCase(localValue8)) {
                     return localValue8;
                  }
               }
            }
         }
      }

      return null;
   }

   private CommandNode build() {
      CommandBuilder localValue1 = CommandBuilder.internalMethod00593(this.name).internalMethod06148(this.desc);
      if (!this.aliases.isEmpty()) {
         localValue1.internalMethod05325(this.aliases.toArray(new String[0]));
      }

      for (PyCommand.InternalType0429 localValue3 : this.args) {
         localValue1.internalMethod01539(localValue3.name(), localValue2 -> {
            if (!localValue3.required()) {
               localValue2.internalMethod06921();
            }

            if (localValue3.vararg()) {
               localValue2.internalMethod00125();
            }

            localValue2.internalMethod00776(this.validator(localValue3));
         });
      }

      if (!this.subs.isEmpty()) {
         ArrayList<CommandNode> localValue5 = new ArrayList<>();

         for (PyCommand localValue4 : this.subs) {
            localValue5.add(localValue4.build());
         }

         localValue1.internalMethod04260(localValue5.toArray(new CommandNode[0]));
      }

      localValue1.internalMethod00262(this::execute);
      return localValue1.internalMethod04146();
   }

   private void execute(ParsedCommand localValue1) {
      if (this.handler == null) {
         ClientMessages.internalMethod01809(Text.of(this.usage()));
      } else {
         ArrayList localValue2 = new ArrayList<>(localValue1.internalMethod02266());
         if (EmbeddedPythonRuntime.internalMethod08724()) {
            this.call(localValue2);
         } else {
            MinecraftClientAccess.internalField0149.execute(() -> this.call(localValue2));
         }
      }
   }

   private void call(List<Object> localValue1) {
      if (this.handler != null && (this.owner == null || this.owner.internalMethod08681())) {
         try (AutoCloseable localValue2 = PythonScript.internalMethod02561(this.owner)) {
            this.handler.call(new Object[]{this.name, localValue1});
         } catch (Exception localValue7) {
            String localValue3 = localValue7.getMessage();
            if (localValue3 != null && localValue3.contains(":")) {
               localValue3 = localValue3.substring(localValue3.indexOf(":") + 1).trim();
            }

            ClientMessages.internalMethod09025(Text.of("[Python Error] " + localValue3));
            RockstarClient.internalField0572.error("Python error in command '" + this.name + "':", localValue7);
         }
      }
   }

   private String usage() {
      StringBuilder localValue1 = new StringBuilder(RockstarClient.getInstance().internalMethod05348().internalMethod03606()).append(this.name);

      for (PyCommand localValue3 : this.subs) {
         localValue1.append(' ').append(localValue3.name);
      }

      for (PyCommand.InternalType0429 localValue5 : this.args) {
         localValue1.append(localValue5.required() ? " <" : " [").append(localValue5.name()).append((char)(localValue5.required() ? '>' : ']'));
      }

      return localValue1.toString();
   }

   private CommandValidator validator(final PyCommand.InternalType0429 localValue1) {
      return new CommandValidator() {
         @Override
         public OperationResult validate(String localValue1x) {
            String localValue2 = localValue1.type();

            return (OperationResult)(switch (localValue2) {
               case "int" -> {
                  OperationResult.InternalType0448 localValue15;
                  try {
                     localValue15 = OperationResult.internalMethod00116(Integer.parseInt(localValue1x));
                  } catch (NumberFormatException localValue7) {
                     OperationResult.InternalType0447 localValue14 = OperationResult.internalMethod05941(
                        "'"
                           + localValue1x
                           + "' \u043d\u0435 \u0446\u0435\u043b\u043e\u0435 \u0447\u0438\u0441\u043b\u043e (\u0430\u0440\u0433\u0443\u043c\u0435\u043d\u0442 "
                           + localValue1.name()
                           + ")"
                     );
                     yield localValue14;
                  }

                  yield localValue15;
               }
               case "float", "number" -> {
                  OperationResult.InternalType0448 localValue13;
                  try {
                     localValue13 = OperationResult.internalMethod00116(Double.parseDouble(localValue1x));
                  } catch (NumberFormatException localValue6) {
                     OperationResult.InternalType0447 localValue12 = OperationResult.internalMethod05941(
                        "'" + localValue1x + "' \u043d\u0435 \u0447\u0438\u0441\u043b\u043e (\u0430\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + localValue1.name() + ")"
                     );
                     yield localValue12;
                  }

                  yield localValue13;
               }
               case "bool" -> {
                  Boolean localValue5 = PyCommand.parseBool(localValue1x);
                  Object localValue11 = localValue5 == null
                     ? OperationResult.internalMethod05941(
                        "'" + localValue1x + "' \u043d\u0435 \u0434\u0430/\u043d\u0435\u0442 (\u0430\u0440\u0433\u0443\u043c\u0435\u043d\u0442 " + localValue1.name() + ")"
                     )
                     : OperationResult.internalMethod00116(localValue5);
                  yield localValue11;
               }
               case "block" -> {
                  Object localValue10 = BlockSuggestionProvider.internalMethod05431(localValue1x) == null
                     ? OperationResult.internalMethod05941(
                        "\u0431\u043b\u043e\u043a\u0430 '" + localValue1x + "' \u043d\u0435 \u0441\u0443\u0449\u0435\u0441\u0442\u0432\u0443\u0435\u0442"
                     )
                     : OperationResult.internalMethod00116(localValue1x);
                  yield localValue10;
               }
               case "module" -> {
                  if (PyCommand.findModule(localValue1x) == null) {
                     OperationResult.InternalType0447 localValue8 = OperationResult.internalMethod05941(
                        "\u043c\u043e\u0434\u0443\u043b\u044f '" + localValue1x + "' \u043d\u0435\u0442 \u0432 \u043a\u043b\u0438\u0435\u043d\u0442\u0435"
                     );
                     yield localValue8;
                  } else {
                     OperationResult.InternalType0448 localValue9 = OperationResult.internalMethod00116(localValue1x);
                     yield localValue9;
                  }
               }
               default -> {
                  OperationResult.InternalType0448 localValue4 = OperationResult.internalMethod00116(localValue1x);
                  yield localValue4;
               }
            });
         }

         @Override
         public List<String> suggestions(String localValue1x) {
            String localValue2 = localValue1x == null ? "" : localValue1x.toLowerCase(Locale.ROOT);
            if (localValue1.suggest() != null) {
               return PyCommand.this.fromScript(localValue1.suggest(), localValue1x);
            } else if (localValue1.suggestions() != null) {
               return PyCommand.filter(localValue1.suggestions(), localValue2);
            } else {
               String localValue3 = localValue1.type();

               return switch (localValue3) {
                  case "player" -> PyCommand.filter(PyCommand.playerNames(), localValue2);
                  case "module" -> PyCommand.filter(PyCommand.moduleNames(), localValue2);
                  case "block" -> BlockSuggestionProvider.internalMethod06324(localValue2);
                  case "bool" -> PyCommand.filter(List.of("true", "false"), localValue2);
                  default -> List.of();
               };
            }
         }
      };
   }

   List<String> fromScript(PyCallable localValue1, String localValue2) {
      if (this.owner != null && !this.owner.internalMethod08681()) {
         return List.of();
      } else if (!EmbeddedPythonRuntime.internalMethod08724()) {
         return List.of();
      } else {
         try {
            ArrayList localValue12;
            try (AutoCloseable localValue3 = PythonScript.internalMethod02561(this.owner)) {
               Object localValue4 = localValue1.call(new Object[]{localValue2 == null ? "" : localValue2});
               ArrayList localValue5 = new ArrayList();
               if (localValue4 instanceof List) {
                  for (Object localValue8 : (List)localValue4) {
                     if (localValue8 != null) {
                        localValue5.add(String.valueOf(localValue8));
                     }
                  }
               }

               localValue12 = localValue5;
            }

            return localValue12;
         } catch (Exception localValue11) {
            return List.of();
         }
      }
   }

   static List<String> filter(List<String> localValue0, String localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (String localValue4 : localValue0) {
         if (localValue4.toLowerCase(Locale.ROOT).startsWith(localValue1)) {
            localValue2.add(localValue4);
         }
      }

      return localValue2;
   }

   static List<String> playerNames() {
      ArrayList localValue0 = new ArrayList();
      if (MinecraftClientAccess.internalField0149.world == null) {
         return localValue0;
      } else {
         for (PlayerEntity localValue2 : MinecraftClientAccess.internalField0149.world.getPlayers()) {
            localValue0.add(localValue2.getNameForScoreboard());
         }

         return localValue0;
      }
   }

   static List<String> moduleNames() {
      ArrayList localValue0 = new ArrayList();

      for (ModuleEntry localValue2 : RockstarClient.getInstance().getModuleManager().getModules()) {
         localValue0.add(localValue2.getName());
      }

      return localValue0;
   }

   @Nullable
   static ModuleEntry findModule(String localValue0) {
      for (ModuleEntry localValue2 : RockstarClient.getInstance().getModuleManager().getModules()) {
         if (localValue2.getName().equalsIgnoreCase(localValue0)) {
            return localValue2;
         }
      }

      return null;
   }

   @Nullable
   static Boolean parseBool(String localValue0) {
      String localValue1 = localValue0.toLowerCase(Locale.ROOT);

      return switch (localValue1) {
         case "true", "1", "on", "yes", "\u0434\u0430", "\u0432\u043a\u043b" -> Boolean.TRUE;
         case "false", "0", "off", "no", "\u043d\u0435\u0442", "\u0432\u044b\u043a\u043b" -> Boolean.FALSE;
         default -> null;
      };
   }

   static final class InternalType0429 {
      private final String name;
      private final String type;
      private final boolean required;
      private final boolean vararg;
      @Nullable
      private final PyCallable suggest;
      @Nullable
      private final List<String> suggestions;

      InternalType0429(String localValue1, String localValue2, boolean localValue3, boolean localValue4, @Nullable PyCallable localValue5, @Nullable List<String> localValue6) {
         this.name = localValue1;
         this.type = localValue2;
         this.required = localValue3;
         this.vararg = localValue4;
         this.suggest = localValue5;
         this.suggestions = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0429[name=" + this.name() + ", type=" + this.type() + ", required=" + this.required() + ", vararg=" + this.vararg() + ", suggest=" + this.suggest() + ", suggestions=" + this.suggestions() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.name());
         result = 31 * result + java.util.Objects.hashCode(this.type());
         result = 31 * result + java.util.Objects.hashCode(this.required());
         result = 31 * result + java.util.Objects.hashCode(this.vararg());
         result = 31 * result + java.util.Objects.hashCode(this.suggest());
         result = 31 * result + java.util.Objects.hashCode(this.suggestions());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PyCommand.InternalType0429 other = (PyCommand.InternalType0429) localValue1;
         return java.util.Objects.equals(this.name(), other.name())
            && java.util.Objects.equals(this.type(), other.type())
            && java.util.Objects.equals(this.required(), other.required())
            && java.util.Objects.equals(this.vararg(), other.vararg())
            && java.util.Objects.equals(this.suggest(), other.suggest())
            && java.util.Objects.equals(this.suggestions(), other.suggestions());
      }

      public String name() {
         return this.name;
      }

      public String type() {
         return this.type;
      }

      public boolean required() {
         return this.required;
      }

      public boolean vararg() {
         return this.vararg;
      }

      @Nullable
      public PyCallable suggest() {
         return this.suggest;
      }

      @Nullable
      public List<String> suggestions() {
         return this.suggestions;
      }
   }
}
