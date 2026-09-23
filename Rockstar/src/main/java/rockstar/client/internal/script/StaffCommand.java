package rockstar.client.internal.script;







import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.core.OperationResult;
import rockstar.client.internal.script.StaffListStore;
import rockstar.client.internal.config.AbstractClientConfig;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;

public class StaffCommand {
    public CommandNode internalMethod00113() {
        return CommandBuilder.internalMethod07482("staff", typedValue125 -> typedValue125.internalMethod06148("commands.staff.description").internalMethod01539("action", typedValue130 -> {
            typedValue130.internalMethod04818("add", "remove", "del", "dell", "delete", "clear", "list", "dir");
            typedValue130.internalMethod07138("add", "remove", "clear", "list", "dir");
        }).internalMethod01539("first", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116)).internalMethod01539("second", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(OperationResult::internalMethod00116)).internalMethod00262(this::internalMethod07564)).internalMethod04146();
    }

    private void internalMethod07564(ParsedCommand typedValue127) {
        RockstarClient.getInstance().internalMethod03371().internalMethod06625("staff");
        String string = (String)typedValue127.internalMethod02266().get(0);
        String string2 = (String)typedValue127.internalMethod02266().get(1);
        String string3 = (String)typedValue127.internalMethod02266().get(2);
        StaffListStore typedValue181 = RockstarClient.getInstance().internalMethod04407();
        switch (string.toLowerCase()) {
            case "add": {
                this.internalMethod04315(typedValue181, string2, string3);
                break;
            }
            case "remove": 
            case "del": 
            case "dell": 
            case "delete": {
                typedValue181.internalMethod01246(string2);
                break;
            }
            case "clear": {
                typedValue181.internalMethod01959();
                break;
            }
            case "list": {
                this.internalMethod05641(typedValue181);
                break;
            }
            case "dir": {
                this.internalMethod06782();
                break;
            }
            default: {
                ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.staff.unknown_action")));
            }
        }
    }

    private void internalMethod06782() {
        try {
            Files.createDirectories(ConfigManager.internalField0148.toPath(), new FileAttribute[0]);
            AbstractClientConfig typedValue138 = RockstarClient.getInstance().internalMethod03371().internalMethod01175("staff");
            if (typedValue138 != null && !typedValue138.internalMethod05023().exists()) {
                RockstarClient.getInstance().internalMethod03371().internalMethod02765(typedValue138);
            }
            Util.getOperatingSystem().open(ConfigManager.internalField0148.toURI());
        }
        catch (Exception exception) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod00160("commands.staff.dir_error", exception.getMessage())));
        }
    }

    private void internalMethod04315(StaffListStore typedValue181, String string, String string2) {
        if (string == null || string.isBlank()) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.staff.empty_name")));
            return;
        }
        if (string2 == null || string2.isBlank()) {
            typedValue181.internalMethod03183(string, "MODER");
            return;
        }
        typedValue181.internalMethod03183(string2, string);
    }

    private void internalMethod05641(StaffListStore typedValue181) {
        List<StaffListStore.InternalType0181> list = typedValue181.internalMethod07279();
        if (list.isEmpty()) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.staff.empty")));
            return;
        }
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.staff.list")));
        int n = 1;
        for (StaffListStore.InternalType0181 nestedValue2025 : list) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.staff.list_item", n++, nestedValue2025.internalMethod04906().isBlank() ? "MODER" : nestedValue2025.internalMethod04906(), nestedValue2025.internalMethod00138())));
        }
    }
}
