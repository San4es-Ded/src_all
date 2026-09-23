package rockstar.client.internal.auth;





import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.util.Map;
import net.minecraft.text.Text;
import rockstar.modules.other.AutoAuthModule;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;

public class AutoAuthCommand {
    public CommandNode internalMethod01341() {
        return CommandBuilder.internalMethod07482("auth", typedValue125 -> typedValue125.internalMethod05325("autoAuth", "\u043f\u0430\u0440\u043e\u043b\u0438", "passwords").internalMethod06148("commands.auth.description").internalMethod00262(this::internalMethod02445)).internalMethod04146();
    }

    private void internalMethod02445(ParsedCommand typedValue127) {
        Map<String, String> map = RockstarClient.getInstance().getModuleManager().getModule(AutoAuthModule.class).internalMethod06147();
        int n = 1;
        if (map.isEmpty()) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.auth.empty")));
            return;
        }
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.auth.passwords")));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String string = entry.getKey();
            String string2 = entry.getValue();
            ClientMessages.internalMethod01809(Text.of((String)(n++ + ") " + LanguageManager.internalMethod07214("commands.auth.nick") + " " + string + " | " + LanguageManager.internalMethod07214("commands.auth.password") + string2)));
        }
    }
}
