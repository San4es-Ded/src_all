package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.command.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.text.Text;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.CommandParameterBuilder;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleEntry;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;

public class ToggleCommand {
    public CommandNode internalMethod00353() {
        List<String> list = RockstarClient.getInstance().getModuleManager().getModules().stream().map(typedValue145 -> typedValue145.getName().replace(" ", "")).toList();
        return CommandBuilder.internalMethod00593("toggle").internalMethod05325("t").internalMethod06148("commands.toggle.description").internalMethod01539("module", typedValue130 -> typedValue130.internalMethod00776(CommandParameterBuilder.internalField0829).internalMethod05362(list)).internalMethod00262(typedValue127 -> {
            ModuleEntry typedValue145 = (ModuleEntry)typedValue127.internalMethod02266().getFirst();
            typedValue145.toggle();
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.toggle." + (typedValue145.isEnabled() ? "enabled" : "disabled"), typedValue145.getName())));
        }).internalMethod04146();
    }
}
