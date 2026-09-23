package rockstar.client.internal.auth;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import moscow.rockstar.mixin.minecraft.client.IMinecraftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;
import rockstar.client.RockstarClient;
import rockstar.client.internal.script.ConfigManager;
import rockstar.profile.Profile;

public final class AltManager {
    public static final List<String> alts = new ArrayList<String>();

    private AltManager() {
    }

    public static List<String> getAlts() {
        return alts;
    }

    public static void setAlts(List<String> list) {
        alts.clear();
        if (list == null) {
            return;
        }
        for (String string : list) {
            if (string == null) continue;
            String string2 = string.trim();
            if (string2.isEmpty() || string2.length() > 16 || alts.contains(string2)) continue;
            alts.add(string2);
        }
    }

    public static void addAlt(String string) {
        if (string == null) {
            return;
        }
        String string2 = string.trim();
        if (string2.isEmpty() || string2.length() > 16 || alts.contains(string2)) {
            return;
        }
        alts.add(string2);
        save();
    }

    public static void removeAlt(String string) {
        if (alts.remove(string)) {
            save();
        }
    }

    public static void login(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        UUID uUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + string).getBytes(StandardCharsets.UTF_8));
            Session session = new Session(string, uUID, "0", Optional.empty(), Optional.empty());
        ((IMinecraftClient)(Object)MinecraftClient.getInstance()).setSession(session);
        Profile.username = string;
    }

    public static void save() {
        ConfigManager scriptInternal070 = RockstarClient.getInstance().internalMethod03371();
        if (scriptInternal070 != null) {
            scriptInternal070.internalMethod05165("alts");
        }
    }
}
