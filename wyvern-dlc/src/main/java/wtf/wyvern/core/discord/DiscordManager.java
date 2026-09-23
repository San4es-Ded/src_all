package wtf.wyvern.core.discord;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import wtf.wyvern.utility.interfaces.IMinecraft;

import java.io.IOException;

public class DiscordManager implements IMinecraft {
    private static final String APPLICATION_ID = "1528113777164288181";
    private static final String DISCORD_URL = "https://discord.gg/v5GFP9aAcQ";
    private static final String TELEGRAM_URL = "https://t.me/wyverndlc";
    // This is the actual GIF file, not Tenor's HTML page/redirect.
    private static final String GIF_URL = "https://media.tenor.com/klwvNXJR-cAAAAAC/wyvern.gif";

    private boolean running = false;
    private DiscordInfo info = new DiscordInfo("Unknown", "", "");
    private Identifier avatarId;
    private final DiscordIpcClient ipc = new DiscordIpcClient();
    private final long startedAt = System.currentTimeMillis();

    public DiscordManager() {
        this.initRPC();
    }

    private void initRPC() {
        try {
            if (!ipc.connect(APPLICATION_ID)) return;
            this.running = true;
            this.updatePresence();

            Thread daemon = new Thread(() -> {
                while (running) {
                    try {
                        // Discord accepts at most five activity updates per 20 seconds.
                        Thread.sleep(4_000L);
                        if (running) updatePresence();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }, "Discord-RPC-Daemon");
            daemon.setDaemon(true);
            daemon.start();

        } catch (Throwable e) {
            this.running = false;
        }
    }

    public void updatePresence() {
        if (!running) return;
        try {
            JsonObject activity = new JsonObject();
            // Username and UID come from wyvernclient.fun (cached, background-fetched);
            // until the site answers, the Minecraft session name is shown.
            String fallbackName = mc.getSession() != null ? mc.getSession().getUsername() : "Unknown";
            activity.addProperty("details", "Name: " + WyvernUserApi.getUsername(fallbackName));
            activity.addProperty("state", "UID: " + WyvernUserApi.getUid("—"));
            activity.addProperty("type", 0);

            JsonObject timestamps = new JsonObject();
            timestamps.addProperty("start", startedAt);
            activity.add("timestamps", timestamps);

            JsonObject assets = new JsonObject();
            assets.addProperty("large_image", GIF_URL);
            assets.addProperty("large_text", "Wyvern Recode");
            activity.add("assets", assets);

            JsonArray buttons = new JsonArray();
            buttons.add(button("Discord", DISCORD_URL));
            buttons.add(button("Telegram", TELEGRAM_URL));
            activity.add("buttons", buttons);
            ipc.setActivity(activity);
        } catch (Exception exception) {
            running = false;
            ipc.close();
        }
    }

    public void stopRPC() {
        this.running = false;
        ipc.close();
    }

    private JsonObject button(String label, String url) {
        JsonObject button = new JsonObject();
        button.addProperty("label", label);
        button.addProperty("url", url);
        return button;
    }

    private String getServerState() {
        if (mc.getCurrentServerEntry() == null || mc.getCurrentServerEntry().address == null || mc.getCurrentServerEntry().address.isBlank()) {
            return "Play on: Singleplayer";
        }
        return "Play on: " + mc.getCurrentServerEntry().address;
    }

    public void load() throws IOException {
    }

    public void setInfo(DiscordInfo info) {
        this.info = info;
    }

    public boolean isRunning() {
        return this.running;
    }

    public DiscordInfo getInfo() {
        return this.info;
    }

    public Identifier getAvatarId() {
        return this.avatarId;
    }

    public static record DiscordInfo(String userName, String avatarUrl, String userId) {}
}