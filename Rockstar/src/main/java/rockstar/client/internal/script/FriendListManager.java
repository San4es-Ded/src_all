package rockstar.client.internal.script;




import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.*;
import globals.client.Information;
import globals.shared.proto.Packets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.text.Text;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.GameUtils;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public class FriendListManager
implements MinecraftClientAccess {
    private final List<String> internalField0416 = new ArrayList<String>();
    private Set<String> internalField0546 = Set.of();
    private int internalField0227 = -1;
    private boolean internalField0277 = true;

    public final void internalMethod00379(String string) {
        if (string == null || string.isBlank()) {
            return;
        }
        if (RockstarClient.getInstance().internalMethod04463().internalMethod06187().contains(string)) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.friends.target")));
            return;
        }
        if (this.internalMethod04153().contains(string)) {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.friends.exists", string)));
            return;
        }
        if (string.equalsIgnoreCase(internalField0149.getSession().getUsername())) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.friends.self")));
            return;
        }
        this.internalField0416.add(string);
        this.internalField0277 = true;
        ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.friends.added", string)));
        if (GameUtils.internalMethod00471()) {
            RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
        }
    }

    public final void internalMethod06965(String string) {
        if (string == null) {
            return;
        }
        if (this.internalField0416.contains(string)) {
            this.internalField0416.remove(string);
            this.internalField0277 = true;
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.friends.removed", string)));
        } else {
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("commands.friends.not_exists", string)));
        }
        RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
    }

    public final void internalMethod05205() {
        if (this.internalField0416.isEmpty()) {
            ClientMessages.internalMethod09025(Text.of((String)LanguageManager.internalMethod07214("commands.friends.empty")));
        } else {
            this.internalField0416.clear();
            this.internalField0277 = true;
            ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod07214("commands.friends.cleared")));
            RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
        }
    }

    public final boolean internalMethod03473(List<String> list) {
        this.internalField0416.clear();
        this.internalField0277 = true;
        if (list == null) {
            return false;
        }
        boolean bl = false;
        for (String string : list) {
            if (string != null && !string.isBlank() && !this.internalField0416.contains(string)) {
                this.internalField0416.add(string);
                continue;
            }
            bl = true;
        }
        return bl;
    }

    private Set<String> internalMethod04517() {
        int n = Information.getFriendsVersion();
        if (!this.internalField0277 && n == this.internalField0227) {
            return this.internalField0546;
        }
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>(this.internalField0416);
        for (Packets.InternalType0018 nestedValue2002 : Information.getFriends()) {
            String string;
            if (nestedValue2002 == null || nestedValue2002.gameInfo() == null || (string = nestedValue2002.gameInfo().nickname()) == null || string.isBlank()) continue;
            linkedHashSet.add(string);
        }
        this.internalField0546 = linkedHashSet;
        this.internalField0227 = n;
        this.internalField0277 = false;
        return this.internalField0546;
    }

    private List<String> internalMethod04153() {
        return new ArrayList<String>(this.internalMethod04517());
    }

    public final List<String> internalMethod06515() {
        return List.copyOf(this.internalMethod04517());
    }

    public final boolean internalMethod00380(String string) {
        if (string == null) {
            return false;
        }
        return this.internalMethod04517().contains(string);
    }
}

