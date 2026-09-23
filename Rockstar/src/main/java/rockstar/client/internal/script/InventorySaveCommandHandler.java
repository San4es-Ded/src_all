package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import rockstar.client.command.CommandNode;
import rockstar.client.command.CommandBuilder;
import rockstar.client.command.ParsedCommand;
import rockstar.client.core.OperationResult;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public class InventorySaveCommandHandler
implements MinecraftClientAccess {
    private final Map<String, Map<Integer, Integer>> internalField0543 = new HashMap<String, Map<Integer, Integer>>();

    public CommandNode internalMethod04314() {
        return CommandBuilder.internalMethod00593("inv").internalMethod05325("inventory", "slot", "\u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c").internalMethod06148("commands.inventory.description").internalMethod01539("action", typedValue130 -> {
            typedValue130.internalMethod04818("save", "create", "add", "\u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c", "load", "use", "\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c");
            typedValue130.internalMethod07138("save", "load");
        }).internalMethod01539("name", typedValue130 -> typedValue130.internalMethod06921().internalMethod00776(string -> string.length() < 2 ? OperationResult.internalMethod05941("commands.prefix.invalid_length") : OperationResult.internalMethod00116(string))).internalMethod00262(this::internalMethod06454).internalMethod04146();
    }

    private void internalMethod06454(ParsedCommand typedValue127) {
        String string = (String)typedValue127.internalMethod02266().get(0);
        String string2 = (String)typedValue127.internalMethod02266().get(1);
        switch (string.toLowerCase(Locale.ROOT)) {
            case "save": 
            case "create": 
            case "add": 
            case "\u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c": {
                this.internalMethod01238(string2);
                ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.inventory.saved", string2)));
                break;
            }
            case "load": 
            case "use": 
            case "\u0437\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c": {
                this.internalMethod02171(string2);
                break;
            }
            default: {
                ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.inventory.invalid_action")));
            }
        }
    }

    private void internalMethod01238(String string) {
        if (InventorySaveCommandHandler.internalField0149.player == null) {
            return;
        }
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i = 0; i <= 45; ++i) {
            ItemStack itemStack = InventorySaveCommandHandler.internalField0149.player.currentScreenHandler.getSlot(i).getStack();
            if (itemStack.isEmpty()) continue;
            hashMap.put(i, Item.getRawId((Item)itemStack.getItem()));
        }
        this.internalField0543.put(string, hashMap);
    }

    private void internalMethod02171(String string) {
        if (!this.internalField0543.containsKey(string)) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod00160("commands.inventory.not_found", string)));
            return;
        }
        Map<Integer, Integer> map = this.internalField0543.get(string);
        boolean bl = false;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int n = entry.getKey();
            Item item = Item.byRawId((int)entry.getValue());
            ItemStack itemStack = new ItemStack((ItemConvertible)item);
            itemStack.setCount(1);
            InventorySaveCommandHandler.internalField0149.player.currentScreenHandler.getSlot(n).setStack(itemStack);
            bl = true;
        }
        if (bl) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.inventory.loaded")));
        } else {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.inventory.empty")));
        }
    }

    public JsonObject internalMethod03552() {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, Map<Integer, Integer>> entry : this.internalField0543.entrySet()) {
            JsonObject jsonObject2 = new JsonObject();
            for (Map.Entry<Integer, Integer> entry2 : entry.getValue().entrySet()) {
                jsonObject2.addProperty(entry2.getKey().toString(), (Number)entry2.getValue());
            }
            jsonObject.add(entry.getKey(), (JsonElement)jsonObject2);
        }
        return jsonObject;
    }

    public void internalMethod06993(JsonElement jsonElement) {
        this.internalField0543.clear();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Map.Entry entry : jsonObject.entrySet()) {
            JsonObject jsonObject2 = ((JsonElement)entry.getValue()).getAsJsonObject();
            HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
            for (Map.Entry entry2 : jsonObject2.entrySet()) {
                Integer n = Integer.valueOf((String)entry2.getKey());
                Integer n2 = ((JsonElement)entry2.getValue()).getAsInt();
                hashMap.put(n, n2);
            }
            this.internalField0543.put((String)entry.getKey(), hashMap);
        }
    }
}
