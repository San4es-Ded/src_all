package rockstar.client.internal.command;





import rockstar.client.util.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import rockstar.modules.other.BaseFinderModule;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.core.OperationResult;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;

public class BaseFinderCommandHandler {
    public CommandNode internalMethod07005() {
        return CommandBuilder.internalMethod07482("base", typedValue125 -> typedValue125.internalMethod06148("Base Finder list").internalMethod01539("action", typedValue130 -> {
            typedValue130.internalMethod04818("list", "clear", "remove", "delete", "del");
            typedValue130.internalMethod07138("list", "clear", "remove");
        }).internalMethod01539("target", typedValue130 -> typedValue130.internalMethod06921().internalMethod00125().internalMethod00776(OperationResult::internalMethod00116)).internalMethod00262(this::internalMethod01762)).internalMethod04146();
    }

    private void internalMethod01762(ParsedCommand typedValue127) {
        String string = (String)typedValue127.internalMethod02266().get(0);
        BaseFinderModule typedValue153 = RockstarClient.getInstance().getModuleManager().getModule(BaseFinderModule.class);
        switch (string.toLowerCase()) {
            case "list": {
                this.internalMethod00567(typedValue153);
                break;
            }
            case "clear": {
                this.internalMethod05588(typedValue153);
                break;
            }
            case "remove": 
            case "delete": 
            case "del": {
                this.internalMethod07092(typedValue153, this.internalMethod04306(typedValue127));
            }
        }
    }

    private void internalMethod00567(BaseFinderModule typedValue153) {
        List<BaseFinderModule.InternalType0022> list = typedValue153.internalMethod01743();
        if (list.isEmpty()) {
            ClientMessages.internalMethod01809(Text.of((String)"\u0411\u0430\u0437\u044b \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u044b."));
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            ClientMessages.internalMethod01809(Text.of((String)BaseFinderModule.internalMethod07316(list.get(i))));
        }
    }

    private void internalMethod05588(BaseFinderModule typedValue153) {
        int n = typedValue153.internalMethod09062();
        ClientMessages.internalMethod01809(Text.of((String)("\u041e\u0447\u0438\u0449\u0435\u043d\u043e \u0431\u0430\u0437: " + n)));
    }

    private void internalMethod07092(BaseFinderModule typedValue153, List<String> list) {
        BaseFinderModule.InternalType0022 nestedValue0009;
        if (list.isEmpty()) {
            ClientMessages.internalMethod09025(Text.of((String)"\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .base remove <\u0411\u0430\u0437\u0430-1|1|x y z>"));
            return;
        }
        BaseFinderModule.InternalType0022 nestedValue0010 = nestedValue0009 = this.internalMethod06385(list) ? typedValue153.internalMethod05915(new BlockPos(Integer.parseInt(list.get(0)), Integer.parseInt(list.get(1)), Integer.parseInt(list.get(2)))) : typedValue153.internalMethod02420(String.join((CharSequence)" ", list));
        if (nestedValue0009 == null) {
            ClientMessages.internalMethod09025(Text.of((String)"\u0411\u0430\u0437\u0430 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d\u0430."));
            return;
        }
        ClientMessages.internalMethod01809(Text.of((String)("\u0423\u0434\u0430\u043b\u0435\u043d\u0430 " + BaseFinderModule.internalMethod07316(nestedValue0009))));
    }

    private boolean internalMethod06385(List<String> list) {
        if (list.size() != 3) {
            return false;
        }
        for (String string : list) {
            if (this.internalMethod03752(string)) continue;
            return false;
        }
        return true;
    }

    private boolean internalMethod03752(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    private List<String> internalMethod04306(ParsedCommand typedValue127) {
        if (typedValue127.internalMethod02266().size() <= 1 || typedValue127.internalMethod02266().get(1) == null) {
            return List.of();
        }
        return ((List)typedValue127.internalMethod02266().get(1)).stream().map(String::valueOf).toList();
    }
}
