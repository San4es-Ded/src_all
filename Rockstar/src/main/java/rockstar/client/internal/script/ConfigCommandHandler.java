package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.text.Text;

public final class ConfigCommandHandler {
   private static final CommandValidator internalField0829 = new CommandValidator() {
      @Override
      public OperationResult validate(String localValue1) {
         return OperationResult.internalMethod00116(localValue1);
      }

      @Override
      public List<String> suggestions(String localValue1) {
         String localValue2 = localValue1.toLowerCase();
         return RockstarClient.getInstance().internalMethod02152().internalMethod07433().stream().filter(localValue1x -> localValue1x.toLowerCase().startsWith(localValue2)).toList();
      }
   };

   public CommandNode internalMethod06280() {
      return CommandBuilder.internalMethod07482(
            "config",
            localValue1 -> localValue1.internalMethod05325("cfg", "\u043a\u0444\u0433", "\u043a\u043e\u043d\u0444\u0438\u0433")
               .internalMethod06148("commands.config.description")
               .internalMethod01539(
                  "action",
                  localValue0 -> localValue0.internalMethod00776(
                        localValue0x -> ConfigCommandHandler.InternalType0011.internalMethod04263(localValue0x)
                            .map(localValue1x -> (OperationResult)OperationResult.internalMethod00116(localValue1x))
                           .orElseGet(() -> OperationResult.internalMethod05941(LanguageManager.internalMethod07214("commands.config.invalid_action")))
                     )
                     .internalMethod05362(ConfigCommandHandler.InternalType0011.internalMethod05036())
               )
               .internalMethod01539("id", localValue0 -> localValue0.internalMethod06921().internalMethod00776(internalField0829))
               .internalMethod01539("arg", localValue0 -> localValue0.internalMethod06921().internalMethod00776(internalField0829))
               .internalMethod00262(this::internalMethod03419)
         )
         .internalMethod04146();
   }

   public void internalMethod03419(ParsedCommand localValue1) {
      ConfigCommandHandler.InternalType0011 localValue2 = (ConfigCommandHandler.InternalType0011)localValue1.internalMethod02266().get(0);
      String localValue3 = (String)localValue1.internalMethod02266().get(1);
      String localValue4 = (String)localValue1.internalMethod02266().get(2);
      if (localValue2 != ConfigCommandHandler.InternalType0011.internalField1165 && localValue4 != null) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.too_many_args")));
      } else {
         LocalConfigStore localValue5 = RockstarClient.getInstance().internalMethod02152();
         switch (localValue2) {
            case internalField0465:
               localValue5.internalMethod06089(localValue3);
               break;
            case internalField0466:
               localValue5.internalMethod07852(localValue3);
               break;
            case internalField1166:
               localValue5.internalMethod07826();
               break;
            case internalField1164:
               localValue5.internalMethod04632(localValue3);
               break;
            case internalField1163:
               localValue5.internalMethod07825();
               break;
            case internalField1165:
               localValue5.internalMethod06062(localValue3, localValue4);
               break;
            case internalField1539:
               localValue5.internalMethod08544(localValue3);
               break;
            case internalField1538:
               localValue5.internalMethod08118(localValue3);
         }
      }
   }

   static enum InternalType0011 {
      internalField0465("save", "create", "add", "\u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c", "\u044b\u0444\u043c\u0443"),
      internalField0466("remove", "delete", "del", "\u0443\u0434\u0430\u043b\u0438\u0442\u044c", "\u0432\u0443\u0434\u0443\u0435\u0443"),
      internalField1166("list", "\u0434\u0448\u044b\u0435"),
      internalField1164("load", "use", "enable", "\u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c", "true", "\u0434\u0449\u0444\u0432"),
      internalField1163("reset", "restore", "\u0441\u0431\u0440\u043e\u0441", "\u0441\u0431\u0440\u043e\u0441\u0438\u0442\u044c"),
      internalField1165("rename", "\u043f\u0435\u0440\u0435\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u0442\u044c"),
      internalField1539(
         "duplicate",
         "copy",
         "\u0434\u0443\u0431\u043b\u0438\u0440\u043e\u0432\u0430\u0442\u044c",
         "\u043a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c"
      ),
      internalField1538(
         "undo", "\u043e\u0442\u043a\u0430\u0442", "\u043e\u0442\u043a\u0430\u0442\u0438\u0442\u044c", "\u0432\u0435\u0440\u043d\u0443\u0442\u044c"
      );

      private final List<String> internalField0416;

      private InternalType0011(String... localValue3) {
         this.internalField0416 = Arrays.stream(localValue3).map(String::toLowerCase).toList();
      }

      public static Optional<ConfigCommandHandler.InternalType0011> internalMethod04263(String localValue0) {
         String localValue1 = localValue0.toLowerCase();
         return Arrays.stream(values()).filter(localValue1x -> localValue1x.internalField0416.contains(localValue1)).findFirst();
      }

      public static List<String> internalMethod05036() {
         return Arrays.stream(values()).map(localValue0 -> localValue0.internalField0416.getFirst()).toList();
      }
   }
}
