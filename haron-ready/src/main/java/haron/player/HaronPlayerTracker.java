package haron.player;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class HaronPlayerTracker {
    private static final Logger LOGGER = LogManager.getLogger((String)"HaronPlayerTracker");
    private static final HaronPlayerTracker INSTANCE = new HaronPlayerTracker();

    private HaronPlayerTracker() {
    }

    public static HaronPlayerTracker get() {
        return INSTANCE;
    }

    public void init() {
        int n = 844;
        ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {});
        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> {});
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (minecraftClient.player == null) {
                return;
            }
        });
    }

    public boolean has(String string) {
        return false;
    }
}

