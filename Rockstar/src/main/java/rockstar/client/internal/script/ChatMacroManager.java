package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.event.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.command.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pyrock.events.window.KeyPressEvent;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.event.EventListener;
import rockstar.client.internal.core.ChatMacroEntry;
import rockstar.client.RockstarClient;
import rockstar.client.util.KeybindUtils;
import rockstar.client.MinecraftClientAccess;

public class ChatMacroManager
implements MinecraftClientAccess {
    private final List<ChatMacroEntry> internalField0416 = new ArrayList<ChatMacroEntry>();
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> {
        if (ChatMacroManager.internalField0149.player == null || internalField0149.getNetworkHandler() == null) {
            return;
        }
        if (ChatMacroManager.internalField0149.currentScreen != null) {
            return;
        }
        if (keyPressEvent.getAction() != 1) {
            return;
        }
        for (ChatMacroEntry typedValue143 : this.internalField0416) {
            if (!KeybindUtils.internalMethod04328(typedValue143.internalMethod03890(), keyPressEvent.getKey())) continue;
            this.internalMethod02788(typedValue143);
        }
    };

    public ChatMacroManager() {
        RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
    }

    public final void internalMethod01066(String string, int n) {
        this.internalField0416.add(new ChatMacroEntry(n, string));
    }

    public final boolean internalMethod03189(int n) {
        return this.internalField0416.removeIf(typedValue143 -> typedValue143.internalMethod03890() == n);
    }

    public final boolean internalMethod04540(String string) {
        return this.internalField0416.removeIf(typedValue143 -> typedValue143.internalMethod07169().equalsIgnoreCase(string));
    }

    public final boolean internalMethod01067(String string, int n) {
        return this.internalField0416.removeIf(typedValue143 -> typedValue143.internalMethod03890() == n && typedValue143.internalMethod07169().equalsIgnoreCase(string));
    }

    public final void internalMethod05229() {
        this.internalField0416.clear();
    }

    public final void internalMethod01171(List<ChatMacroEntry> list) {
        this.internalField0416.clear();
        this.internalField0416.addAll(list);
    }

    public final List<ChatMacroEntry> internalMethod06721() {
        return Collections.unmodifiableList(this.internalField0416);
    }

    private void internalMethod02788(ChatMacroEntry typedValue143) {
        String string = typedValue143.internalMethod07169();
        if (string == null || string.isBlank() || ChatMacroManager.internalField0149.player == null || internalField0149.getNetworkHandler() == null) {
            return;
        }
        CommandRegistry typedValue128 = RockstarClient.getInstance().internalMethod05348();
        if (typedValue128 != null) {
            String string2 = typedValue128.internalMethod03606();
            if (!string2.isEmpty() && string.startsWith(string2 + string2)) {
                ChatMacroManager.internalField0149.player.networkHandler.sendChatMessage(string.substring(string2.length()));
                return;
            }
            if (!string2.isEmpty() && string.startsWith(string2)) {
                typedValue128.internalMethod04610(string);
                return;
            }
        }
        if (string.startsWith("/")) {
            ChatMacroManager.internalField0149.player.networkHandler.sendChatCommand(string.substring(1));
        } else {
            ChatMacroManager.internalField0149.player.networkHandler.sendChatMessage(string);
        }
    }
}

