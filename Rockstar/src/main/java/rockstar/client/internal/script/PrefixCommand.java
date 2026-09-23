package rockstar.client.internal.script;







import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.command.*;
import rockstar.client.*;
import net.minecraft.text.Text;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.core.OperationResult;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;

public class PrefixCommand {
    public CommandNode internalMethod05847() {
        return CommandBuilder.internalMethod07482("prefix", typedValue125 -> typedValue125.internalMethod06148("commands.prefix.description").internalMethod01539("action", typedValue130 -> {
            typedValue130.internalMethod06921().internalMethod04818("list", "clear", "default", "reset", "set", "create");
            typedValue130.internalMethod07138("list", "reset", "set");
        }).internalMethod01539("new", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(string -> string.length() > 1 ? OperationResult.internalMethod05941(LanguageManager.internalMethod07214("commands.prefix.invalid_length")) : OperationResult.internalMethod00116(string))).internalMethod00262(this::internalMethod06009)).internalMethod04146();
    }

    private void internalMethod06009(ParsedCommand typedValue127) {
        String string = (String)typedValue127.internalMethod02266().get(0);
        String string2 = (String)typedValue127.internalMethod02266().get(1);
        CommandRegistry typedValue128 = RockstarClient.getInstance().internalMethod05348();
        String string3 = typedValue128.internalMethod03606();
        if (string == null) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.prefix.current", string3)));
            return;
        }
        switch (string.toLowerCase()) {
            case "list": {
                ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.prefix.current", string3)));
                break;
            }
            case "clear": 
            case "default": 
            case "reset": {
                typedValue128.internalMethod04609(".");
                ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.prefix.reset")));
                break;
            }
            case "set": 
            case "create": {
                if (string2 == null || string2.isEmpty()) {
                    ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.prefix.empty")));
                    return;
                }
                typedValue128.internalMethod04609(string2);
                ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.prefix.set", string2)));
            }
        }
    }
}
