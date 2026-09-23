package rockstar.client.internal.script;








import rockstar.client.util.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.ui.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.command.CommandParameterBuilder;
import rockstar.client.core.OperationResult;
import rockstar.modules.visual.MenuModule;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleEntry;
import rockstar.client.RockstarClient;
import rockstar.client.internal.ui.ModuleSettingsScreen;
import rockstar.client.internal.ui.KeybindParser;
import rockstar.client.util.ClientMessages;
import rockstar.client.util.TextUtils;

public class BindCommandHandler {
    public CommandNode internalMethod02253() {
        List<String> list = RockstarClient.getInstance().getModuleManager().getModules().stream().map(typedValue145 -> typedValue145.getName().replace(" ", "")).toList();
        List<String> list2 = KeybindParser.internalMethod07034();
        return CommandBuilder.internalMethod07482("bind", typedValue125 -> typedValue125.internalMethod05325("binds", "\u0431\u0438\u043d\u0434").internalMethod06148("commands.bind.description")).internalMethod01539("action", typedValue130 -> {
            typedValue130.internalMethod04818("add", "create", "remove", "delete", "list", "clear");
            typedValue130.internalMethod07138("add", "remove", "list", "clear");
        }).internalMethod01539("module", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(CommandParameterBuilder.internalField0829).internalMethod05362(list)).internalMethod01539("key", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(string -> string.isBlank() ? OperationResult.internalMethod05941("key is empty") : OperationResult.internalMethod00116(string)).internalMethod05362(list2)).internalMethod00262(this::internalMethod07436).internalMethod04146();
    }

    private void internalMethod07436(ParsedCommand typedValue127) {
        String string = (String)typedValue127.internalMethod02266().getFirst();
        ModuleEntry typedValue145 = (ModuleEntry)typedValue127.internalMethod02266().get(1);
        String string2 = (String)typedValue127.internalMethod02266().get(2);
        if (string.equalsIgnoreCase("clear")) {
            for (ModuleEntry typedValue146 : RockstarClient.getInstance().getModuleManager().getModules()) {
                if (typedValue146 instanceof MenuModule || typedValue146.getKeybind() == -1) continue;
                typedValue146.setKeybind(-1);
            }
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.bind.clear")));
        }
        if (string.equalsIgnoreCase("list")) {
            this.internalMethod02896();
            return;
        }
        if (string.equalsIgnoreCase("add") || string.equalsIgnoreCase("create")) {
            if (string2 == null) {
                ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.bind.create.key_not")));
                return;
            }
            int n = KeybindParser.internalMethod06543(string2);
            if (n == -1) {
                ClientMessages.internalMethod09025(Text.of((String)(LanguageManager.internalMethod07214("commands.bind.create.key_unknow") + string2)));
                return;
            }
            typedValue145.setKeybind(n);
            ClientMessages.internalMethod01809(Text.of((String)(LanguageManager.internalMethod07214("commands.bind.create.download") + " " + TextUtils.internalMethod04982(n))));
        } else if (string.equalsIgnoreCase("remove") || string.equalsIgnoreCase("delete")) {
            if (typedValue145 == null) {
                ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.bind.module_required")));
                return;
            }
            typedValue145.setKeybind(-1);
            ClientMessages.internalMethod01809(Text.of((String)(LanguageManager.internalMethod07214("commands.bind.delete") + " " + typedValue145.getName())));
        }
    }

    private void internalMethod02896() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.send(() -> minecraftClient.setScreen((Screen)new ModuleSettingsScreen()));
    }
}
