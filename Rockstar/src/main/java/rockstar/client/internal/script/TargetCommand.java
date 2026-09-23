package rockstar.client.internal.script;





import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.core.OperationResult;
import rockstar.client.internal.game.TargetSelectionManager;
import rockstar.client.RockstarClient;

public class TargetCommand {
    public CommandNode internalMethod05187() {
        return CommandBuilder.internalMethod07482("target", typedValue125 -> typedValue125.internalMethod05325("targets").internalMethod06148("commands.target.description").internalMethod01539("action", typedValue130 -> {
            typedValue130.internalMethod04818("add", "remove", "del", "delete", "clear", "list");
            typedValue130.internalMethod07138("add", "remove", "clear", "list");
        }).internalMethod01539("id", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116)).internalMethod00262(this::internalMethod01176)).internalMethod04146();
    }

    private void internalMethod01176(ParsedCommand typedValue127) {
        String string = (String)typedValue127.internalMethod02266().get(0);
        String string2 = (String)typedValue127.internalMethod02266().get(1);
        TargetSelectionManager typedValue182 = RockstarClient.getInstance().internalMethod04463();
        switch (string.toLowerCase()) {
            case "add": {
                typedValue182.internalMethod04581(string2);
                break;
            }
            case "remove": 
            case "del": 
            case "delete": {
                typedValue182.internalMethod02995(string2);
                break;
            }
            case "clear": {
                typedValue182.internalMethod01468();
                break;
            }
            case "list": {
                typedValue182.internalMethod01470();
            }
        }
    }
}
