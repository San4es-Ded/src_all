package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.command.*;
import rockstar.client.internal.command.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.text.Text;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;

public class HelpCommandHandler {
    public CommandNode internalMethod04111() {
        return CommandBuilder.internalMethod07482("help", typedValue125 -> typedValue125.internalMethod05325("\u043f\u043e\u043c\u043e\u0449\u044c", "\u043a\u043e\u043c\u0430\u043d\u0434\u044b", "commands", "helpme").internalMethod06148("commands.help.description").internalMethod00262(this::internalMethod00546)).internalMethod04146();
    }

    private void internalMethod00546(ParsedCommand typedValue127) {
        ArrayList<CommandNode> arrayList = new ArrayList<CommandNode>(RockstarClient.getInstance().internalMethod05348().internalMethod05967());
        CommandRegistry typedValue128 = RockstarClient.getInstance().internalMethod05348();
        arrayList.sort(Comparator.comparing(internalValue0007 -> internalValue0007.internalMethod03764().getFirst(), String.CASE_INSENSITIVE_ORDER));
        ArrayList<String> arrayList2 = new ArrayList<String>();
        int n = 1;
        for (CommandNode internalValue0008 : arrayList) {
            if (!typedValue128.internalMethod01800(internalValue0008)) continue;
            arrayList2.add(String.format("%d) %s%s - %s", n++, typedValue128.internalMethod03606(), internalValue0008.internalMethod03764().getFirst(), LanguageManager.internalMethod07214(internalValue0008.internalMethod03657())));
        }
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.help.list", String.join((CharSequence)"\n", arrayList2))));
    }
}
