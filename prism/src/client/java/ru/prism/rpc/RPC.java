package ru.prism.rpc;


import ru.prism.utils.annotation.IMinecraft;


import ru.prism.utils.math.ServerUtil;

public class RPC implements IMinecraft {

    public static DiscordRichPresence presence = new DiscordRichPresence();
    public static boolean started;
    private static Thread thread;

    
    public void startRpc() {
        // Проверяем, доступна ли библиотека Discord RPC
        if (!DiscordRPC.Loader.isAvailable()) {
            return;
        }

        DiscordRPC rpc = DiscordRPC.Loader.getInstance();
        if (!started) {
            started = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers();
            rpc.Discord_Initialize("1505143136215634050", handlers, true, "");
            presence.startTimestamp = (System.currentTimeMillis() / 1000L);
            presence.largeImageText = "PrismVisuals - 1.21.11";
            rpc.Discord_UpdatePresence(presence);

            thread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    rpc.Discord_RunCallbacks();
                    presence.details = "Build: ";
                    presence.state = "Plays on " + ServerUtil.server;

                    presence.button_label_1 = "Web";
                    presence.button_url_1 = "https://github.com/San4es-Ded/PrismVisuals";

                    presence.button_label_2 = "Discord";
                    presence.button_url_2 = "https://discord.gg/5jRJjDYW5T";


                    presence.largeImageKey = "https://i.ibb.co/LdjX1rYL/Tim6eline-1.gif";
                    presence.largeImageText = "Best client 1.21.11";
                    //presence.smallImageKey = Profile.getAvatarUrl();
                    //presence.smallImageText = Profile.getUsername() + " | " + Profile.getUid();

                    rpc.Discord_UpdatePresence(presence);
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "TH-RPC-Handler");
            thread.start();

        }
    }
}
