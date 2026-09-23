package rockstar.client.internal.game;


import rockstar.client.*;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import globals.shared.proto.Packets;
import java.time.OffsetDateTime;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.profile.Profile;

public class DiscordRichPresenceService
implements MinecraftClientAccess {
    private final IPCClient internalField0353 = new IPCClient(1517438618715820073L);
    private volatile String internalField0248;
    private volatile String internalField0247;
    volatile boolean internalField0277;

    RichPresence.Builder internalMethod02512() {
        return new RichPresence.Builder().setDetails("UID: " + Profile.getUid()).setState(String.format("Role: %s", DiscordRichPresenceService.internalMethod04600(Profile.getRole().name()))).setStartTimestamp(OffsetDateTime.now()).setLargeImage("animlogo", "t.me/rockclient").setSmallImage("animlogo", Profile.getUsername()).setButton1Text("Telegram").setButton1Url("https://t.me/rockclient").setButton2Text("Discord").setButton2Url("https://dsc.gg/rockclient");
    }

    public final void internalMethod00955() {
        if (!this.internalField0277) {
            return;
        }
        try {
            this.internalField0353.sendRichPresence(this.internalMethod02512().build());
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public final void internalMethod00959() {
        try {
            this.internalField0353.setListener(new IPCListener(){

                public void onReady(IPCClient iPCClient) {
                    DiscordRichPresenceService.this.internalField0277 = true;
                    iPCClient.sendRichPresence(DiscordRichPresenceService.this.internalMethod02512().build());
                    DiscordRichPresenceService.this.internalMethod02797(iPCClient.getDiscordUser());
                }

                public void onCurrentUserUpdate(IPCClient iPCClient, User user) {
                    DiscordRichPresenceService.this.internalMethod02797(user);
                }
            });
            this.internalField0353.connect(new DiscordBuild[0]);
        }
        catch (Exception | LinkageError throwable) {
            RockstarClient.internalField0572.warn("Discord RPC connection failed", throwable);
        }
    }

    public void internalMethod02797(User user) {
        if (user == null) {
            return;
        }
        try {
            this.internalField0248 = user.getUsername();
            String string = user.getGlobalName();
            this.internalField0247 = string == null || string.isBlank() ? user.getName() : string;
            this.internalMethod08089();
        }
        catch (Exception | LinkageError throwable) {
            RockstarClient.internalField0572.warn("Discord user capture failed", throwable);
        }
    }

    public void internalMethod08089() {
        String string = this.internalField0248;
        if (string == null || string.isBlank()) {
            return;
        }
        try {
            RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0114(string, this.internalField0247));
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static String internalMethod04600(String string) {
        String[] stringArray = string.split("\\s+");
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : stringArray) {
            if (string2.isEmpty()) continue;
            stringBuilder.append(Character.toUpperCase(string2.charAt(0))).append(string2.substring(1).toLowerCase()).append(" ");
        }
        return stringBuilder.toString().trim();
    }
}
