package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.text.Text;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.core.OperationResult;
import rockstar.client.internal.script.FriendListManager;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;

public class FriendCommandHandler {
    public CommandNode internalMethod07227() {
        return CommandBuilder.internalMethod07482("friend", typedValue125 -> typedValue125.internalMethod05325("friends").internalMethod06148("commands.friends.description").internalMethod01539("action", typedValue130 -> {
            typedValue130.internalMethod04818("add", "remove", "del", "delete", "clear", "list");
            typedValue130.internalMethod07138("add", "remove", "clear", "list");
        }).internalMethod01539("id", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116)).internalMethod00262(this::internalMethod00362)).internalMethod04146();
    }

    private void internalMethod00362(ParsedCommand typedValue127) {
        String string = (String)typedValue127.internalMethod02266().get(0);
        String string2 = (String)typedValue127.internalMethod02266().get(1);
        FriendListManager typedValue140 = RockstarClient.getInstance().internalMethod03375();
        switch (string.toLowerCase()) {
            case "add": {
                typedValue140.internalMethod00379(string2);
                break;
            }
            case "remove": 
            case "del": 
            case "delete": {
                typedValue140.internalMethod06965(string2);
                break;
            }
            case "clear": {
                typedValue140.internalMethod05205();
                break;
            }
            case "list": {
                this.internalMethod02304();
            }
        }
    }

    private void internalMethod02304() {
        List<String> list = RockstarClient.getInstance().internalMethod03375().internalMethod06515();
        if (list.isEmpty()) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.friends.empty")));
            return;
        }
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.friends.list")));
        for (int i = 0; i < list.size(); ++i) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.friends.list_item", i + 1, list.get(i))));
        }
    }
}
