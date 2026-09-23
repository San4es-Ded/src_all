package haron.player;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

public final class PlayerCapeTracker {
    private static final PlayerCapeTracker INSTANCE = new PlayerCapeTracker();
    private final Map<String, Integer> capeIdsByName = new ConcurrentHashMap<String, Integer>();
    private volatile String currentServer = "";

    private void refreshServer() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        this.currentServer = minecraftClient.getCurrentServerEntry() == null ? "" : minecraftClient.getCurrentServerEntry().address;
    }

    private PlayerCapeTracker() {
    }

    public Integer b(String string) {
        if (string == null || string.isBlank()) {
            return null;
        }
        String string2 = PlayerCapeTracker.normalize(string);
        return this.capeIdsByName.get(string2);
    }

    public void b() {
        this.currentServer = "";
        this.capeIdsByName.clear();
    }

    public static PlayerCapeTracker c() {
        return INSTANCE;
    }

    private Collection<PlayerListEntry> d() {
        ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();
        return clientPlayNetworkHandler == null ? Collections.emptyList() : clientPlayNetworkHandler.getListedPlayerListEntries();
    }

    public boolean a(String string) {
        return this.b(string) != null;
    }

    public void a(PlayerListS2CPacket playerListS2CPacket) {
        this.refreshServer();
    }

    public void a() {
        int n = 773;
        this.refreshServer();
        this.capeIdsByName.clear();
    }

    private static String normalize(String string) {
        return string.toLowerCase(Locale.ROOT);
    }
}

