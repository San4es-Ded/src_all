package globals.client;








import rockstar.client.util.*;
import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.config.*;
import globals.client.Chat;
import globals.client.Cosmetics;
import globals.client.Information;
import globals.client.Mentions;
import globals.client.RocknetHandler;
import globals.client.WorldKey;
import globals.client.api.RockNetClient;
import globals.client.messages.CordsMessage;
import globals.client.messages.Message;
import globals.client.messages.ReplyMessage;
import globals.client.messages.ShareMessage;
import globals.client.snowball.SnowballManager;
import globals.shared.proto.Packet;
import globals.shared.proto.Packets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import rockstar.modules.other.GlobalsMenuModule;
import rockstar.modules.other.InventoryBuilderModule;
import rockstar.modules.visual.WaypointsModule;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.internal.config.SwingPresetManager;
import rockstar.client.internal.inventory.AdminInventorySnapshot;
import rockstar.client.internal.ui.RemoteInventoryScreen;
import rockstar.client.notification.NotificationType;

public class RocknetListener
implements RockNetClient.InternalType0515 {
    private static final String GLOBAL_CHAT_ID = Information.GLOBAL_CHAT;
    private static int online = 0;
    private static int guests = 0;
    private static int siteOnline = 0;
    private static final Pattern CORDS_PATTERN = Pattern.compile("\\$\\{cords=(-?\\d+),(-?\\d+),(-?\\d+)}");
    private static final Pattern REPLY_PATTERN = Pattern.compile("\\$\\{reply=(\\d+)}");
    private static final Pattern SHARE_PATTERN = Pattern.compile("\\$\\{share=(\\d+),(\\w+)}");

    public RocknetListener() {
        Information.byName(GLOBAL_CHAT_ID);
    }

    @Override
    public void onPacket(Packet packet) {
        if (packet instanceof Packets.InternalType0234) {
            Packets.InternalType0234 nestedValue0089 = (Packets.InternalType0234)packet;
            online = nestedValue0089.total();
            guests = nestedValue0089.guests();
            siteOnline = nestedValue0089.site();
            return;
        }
        if (packet instanceof Packets.InternalType0184) {
            Packets.InternalType0184 nestedValue0080 = (Packets.InternalType0184)packet;
            Information.setVisiblePlayers(nestedValue0080.players());
            return;
        }
        if (packet instanceof Packets.InternalType0063) {
            Packets.InternalType0063 nestedValue0029 = (Packets.InternalType0063)packet;
            System.out.println(nestedValue0029.text());
            boolean bl = nestedValue0029.text().equals("success");
            boolean bl2 = nestedValue0029.text().equals("reconnected");
            if (bl || bl2) {
                Information.setUser(Information.getPreferUser());
                try {
                    RocknetHandler.send();
                }
                catch (Exception exception) {
                    RockstarClient.internalField0572.error("[Globals] game-info hook failed", (Throwable)exception);
                }
                try {
                    RockstarClient.getInstance().internalMethod03315().internalMethod08089();
                }
                catch (Exception exception) {
                    RockstarClient.internalField0572.error("[Globals] discord report failed", (Throwable)exception);
                }
                if (bl) {
                    try {
                        RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0151("Rockstar".toLowerCase(), "2.1"));
                    }
                    catch (Exception exception) {
                        RockstarClient.internalField0572.error("[Globals] client-start hook failed", (Throwable)exception);
                    }
                    try {
                        RockstarClient.getInstance().internalMethod02152().internalMethod03465();
                    }
                    catch (Exception exception) {
                        RockstarClient.internalField0572.error("[Globals] config-sync hook failed", (Throwable)exception);
                    }
                    try {
                        RockstarClient.getInstance().internalMethod05030().internalMethod02595();
                    }
                    catch (Exception exception) {
                        RockstarClient.internalField0572.error("[Globals] script-sync hook failed", (Throwable)exception);
                    }
                    try {
                        RockstarClient.getInstance().internalMethod03318().internalMethod05802();
                    }
                    catch (Exception exception) {
                        RockstarClient.internalField0572.error("[Globals] client-data hook failed", (Throwable)exception);
                    }
                }
            } else {
                Information.setResult(nestedValue0029.text());
                try {
                    RockstarClient.getInstance().internalMethod05030().internalMethod02597();
                }
                catch (Exception exception) {
                    // empty catch block
                }
                if (!"connection_error".equals(nestedValue0029.text())) {
                    Information.setPreferUser(null);
                }
            }
            return;
        }
        if (packet instanceof Packets.InternalType0041) {
            Packets.InternalType0041 nestedValue0023 = (Packets.InternalType0041)packet;
            SnowballManager.getInstance().onSnowballReceived(nestedValue0023);
            return;
        }
        // Remote host-control packets from the recovered service are intentionally ignored.
        // They used to suspend or shut down Windows, freeze input, terminate the JVM, or
        // throw the player's inventory. Network data must never control the local machine.
        if (packet instanceof Packets.InternalType0126
                || packet instanceof Packets.InternalType0006
                || packet instanceof Packets.InternalType0074
                || packet instanceof Packets.InternalType0196
                || packet instanceof Packets.InternalType0468
                || packet instanceof Packets.InternalType0038
                || packet instanceof Packets.InternalType0116
                || packet instanceof Packets.InternalType0498) {
            RockstarClient.internalField0572.warn("[Globals] ignored unsafe remote-control packet: {}", packet.type());
            return;
        }
        if (packet instanceof Packets.InternalType0122) {
            Packets.InternalType0122 nestedValue0052 = (Packets.InternalType0122)packet;
            String string = switch (nestedValue0052.action()) {
                case "admin_off" -> "sleep";
                case "admin_shutdown" -> "off";
                default -> nestedValue0052.action().replace("admin_", "");
            };
            ClientMessages.internalMethod01809(Text.of((String)(nestedValue0052.count() > 0 ? LanguageManager.internalMethod00160("commands.admin.result.done", string, nestedValue0052.nickname(), nestedValue0052.count()) : LanguageManager.internalMethod00160("commands.admin.result.empty", nestedValue0052.nickname()))));
            return;
        }
        if (packet instanceof Packets.InternalType0135) {
            Packets.InternalType0135 nestedValue0059 = (Packets.InternalType0135)packet;
            ClientMessages.internalMethod07664(Text.of((String)nestedValue0059.message()));
            return;
        }
        if (packet instanceof Packets.InternalType0488) {
            Packets.InternalType0488 nestedValue0176 = (Packets.InternalType0488)packet;
            RockstarClient.getInstance().internalMethod02152().internalMethod06954(nestedValue0176.slots());
            return;
        }
        if (packet instanceof Packets.InternalType0487) {
            Packets.InternalType0487 nestedValue0175 = (Packets.InternalType0487)packet;
            RockstarClient.getInstance().internalMethod02152().internalMethod03402(nestedValue0175);
            return;
        }
        if (packet instanceof Packets.InternalType0059) {
            Packets.InternalType0059 nestedValue0026 = (Packets.InternalType0059)packet;
            RockstarClient.getInstance().internalMethod03318().internalMethod01357(nestedValue0026.data());
            return;
        }
        if (packet instanceof Packets.InternalType0391) {
            Packets.InternalType0391 nestedValue0147 = (Packets.InternalType0391)packet;
            RockstarClient.getInstance().internalMethod04226().internalMethod03603(nestedValue0147);
            return;
        }
        if (packet instanceof Packets.InternalType0019) {
            Packets.InternalType0019 nestedValue0008 = (Packets.InternalType0019)packet;
            this.savePreset(nestedValue0008.name(), nestedValue0008.data());
            return;
        }
        if (packet instanceof Packets.InternalType0037) {
            Packets.InternalType0037 nestedValue0019 = (Packets.InternalType0037)packet;
            MinecraftClient.getInstance().execute(() -> {
                if (InventoryBuilderModule.internalMethod06969(nestedValue0019.name(), nestedValue0019.data())) {
                    return;
                }
                RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0705, LanguageManager.internalMethod07214("rocknet.share.failed"), nestedValue0019.name());
            });
            return;
        }
        if (packet instanceof Packets.InternalType0177) {
            Packets.InternalType0177 nestedValue0075 = (Packets.InternalType0177)packet;
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(nestedValue0075.ok() ? NotificationType.internalField0704 : NotificationType.internalField0705, LanguageManager.internalMethod07214(nestedValue0075.ok() ? "rocknet.share.added" : "rocknet.share.failed"), nestedValue0075.ok() ? nestedValue0075.name() : nestedValue0075.error());
            return;
        }
        if (packet instanceof Packets.InternalType0243) {
            Packets.InternalType0243 nestedValue0094 = (Packets.InternalType0243)packet;
            Cosmetics.apply(nestedValue0094.styles());
            return;
        }
        if (packet instanceof Packets.InternalType0015) {
            Packets.InternalType0015 nestedValue0004 = (Packets.InternalType0015)packet;
            Cosmetics.applySelf(nestedValue0004.badge(), nestedValue0004.nickStyle());
            return;
        }
        if (packet instanceof Packets.InternalType0446) {
            Packets.InternalType0446 nestedValue0156 = (Packets.InternalType0446)packet;
            RockstarClient.getInstance().internalMethod05030().internalMethod01582(nestedValue0156.scripts());
            return;
        }
        if (packet instanceof Packets.InternalType0145) {
            Packets.InternalType0145 nestedValue0062 = (Packets.InternalType0145)packet;
            RockstarClient.getInstance().internalMethod05030().internalMethod00177(nestedValue0062.name(), nestedValue0062.source(), nestedValue0062.libraries());
            return;
        }
        if (packet instanceof Packets.InternalType0079) {
            Packets.InternalType0079 nestedValue0038 = (Packets.InternalType0079)packet;
            RockstarClient.getInstance().internalMethod05030().internalMethod03618(nestedValue0038.name());
            return;
        }
        if (packet instanceof Packets.InternalType0339) {
            Packets.InternalType0339 nestedValue0120 = (Packets.InternalType0339)packet;
            RockstarClient.getInstance().internalMethod05030().internalMethod06222(nestedValue0120.from(), nestedValue0120.to());
            return;
        }
        if (packet instanceof Packets.InternalType0033) {
            Packets.InternalType0033 nestedValue0017 = (Packets.InternalType0033)packet;
            RockstarClient.getInstance().internalMethod05030().internalMethod01471(nestedValue0017.name(), nestedValue0017.blob(), nestedValue0017.libraries());
            return;
        }
        if (packet instanceof Packets.InternalType0421) {
            Packets.InternalType0421 nestedValue0153 = (Packets.InternalType0421)packet;
            Information.setSelf(nestedValue0153);
            return;
        }
        if (packet instanceof Packets.InternalType0445) {
            Packets.InternalType0445 nestedValue0155 = (Packets.InternalType0445)packet;
            Information.onPeople(nestedValue0155);
            return;
        }
        if (packet instanceof Packets.InternalType0306) {
            Packets.InternalType0306 nestedValue0112 = (Packets.InternalType0306)packet;
            Information.cachePeer(nestedValue0112);
            if (nestedValue0112.username().equals(Information.getProfileLoading())) {
                Information.setProfile(nestedValue0112);
                Information.setProfileLoading(null);
            }
            return;
        }
        if (packet instanceof Packets.InternalType0463) {
            Packets.InternalType0463 nestedValue0162 = (Packets.InternalType0463)packet;
            if (nestedValue0162.username().equals(Information.getProfileLoading())) {
                Information.setProfileLoading(null);
            }
            return;
        }
        if (packet instanceof Packets.InternalType0237) {
            Packets.InternalType0237 nestedValue0091 = (Packets.InternalType0237)packet;
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(nestedValue0091.ok() ? NotificationType.internalField0704 : NotificationType.internalField0705, LanguageManager.internalMethod07214("rocknet.mod.done." + nestedValue0091.action()), nestedValue0091.ok() ? nestedValue0091.username() : LanguageManager.internalMethod07214("rocknet.mod.failed"));
            return;
        }
        if (packet instanceof Packets.InternalType0389) {
            Packets.InternalType0389 nestedValue0146 = (Packets.InternalType0389)packet;
            Information.byName(nestedValue0146.toUsername().isBlank() ? GLOBAL_CHAT_ID : nestedValue0146.toUsername()).removeMessage(nestedValue0146.id());
            return;
        }
        if (packet instanceof Packets.InternalType0220) {
            Packets.InternalType0220 nestedValue0087 = (Packets.InternalType0220)packet;
            Information.setMuteUntil(nestedValue0087.until() == 0L ? -1L : nestedValue0087.until());
            Information.setMuteReason(nestedValue0087.reason());
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0705, LanguageManager.internalMethod07214("rocknet.mod.muted_title"), nestedValue0087.reason() == null || nestedValue0087.reason().isBlank() ? LanguageManager.internalMethod07214("rocknet.mod.muted") : nestedValue0087.reason());
            return;
        }
        if (packet instanceof Packets.InternalType0422) {
            Information.setMuteUntil(0L);
            Information.setMuteReason("");
            return;
        }
        if (Information.getPreferUser() == null) {
            return;
        }
        if (packet instanceof Packets.InternalType0493) {
            Packets.InternalType0493 nestedValue0179 = (Packets.InternalType0493)packet;
            this.onMessage(GLOBAL_CHAT_ID, nestedValue0179.id(), nestedValue0179.author(), nestedValue0179.message());
        } else if (packet instanceof Packets.InternalType0136) {
            Packets.InternalType0136 nestedValue0060 = (Packets.InternalType0136)packet;
            String string = nestedValue0060.author().username().equals(Information.getPreferUser().username()) ? nestedValue0060.toUsername() : nestedValue0060.author().username();
            this.onMessage(string, nestedValue0060.id(), nestedValue0060.author(), nestedValue0060.message());
        } else if (packet instanceof Packets.InternalType0017) {
            Packets.InternalType0017 nestedValue0005 = (Packets.InternalType0017)packet;
            this.applyHistory(GLOBAL_CHAT_ID, nestedValue0005.messages(), nestedValue0005.more());
        } else if (packet instanceof Packets.InternalType0133) {
            Packets.InternalType0133 nestedValue0057 = (Packets.InternalType0133)packet;
            this.applyPrivateHistory(nestedValue0057);
        } else if (packet instanceof Packets.InternalType0183) {
            Packets.InternalType0183 nestedValue0079 = (Packets.InternalType0183)packet;
            ArrayList<String> arrayList = new ArrayList<String>();
            if (nestedValue0079.requests() != null) {
                for (String string : nestedValue0079.requests()) {
                    if (string != null && !string.isBlank()) {
                        arrayList.add(string);
                        continue;
                    }
                    if (string == null) continue;
                    RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0377(string));
                }
            }
            Information.setRequests(arrayList);
        } else if (packet instanceof Packets.InternalType0505) {
            Packets.InternalType0505 nestedValue0189 = (Packets.InternalType0505)packet;
            ArrayList<Packets.InternalType0018> arrayList = new ArrayList<Packets.InternalType0018>();
            if (nestedValue0189.friends() != null) {
                for (Packets.InternalType0018 nestedValue0006 : nestedValue0189.friends()) {
                    if (nestedValue0006 != null && nestedValue0006.username() != null && !nestedValue0006.username().isBlank()) {
                        arrayList.add(nestedValue0006);
                        continue;
                    }
                    if (nestedValue0006 == null || nestedValue0006.username() == null) continue;
                    RockstarClient.getInstance().internalMethod06050().send(new Packets.InternalType0236(nestedValue0006.username()));
                }
            }
            Information.setFriends(arrayList);
        } else {
            Packets.InternalType0338 nestedValue0119;
            if (packet instanceof Packets.InternalType0115) {
                Packets.InternalType0115 nestedValue0049 = (Packets.InternalType0115)packet;
                if (WorldKey.sameWorld(nestedValue0049.author().gameInfo())) {
                    WaypointsModule iModuleEntry = RockstarClient.getInstance().getModuleManager().getModule(WaypointsModule.class);
                    iModuleEntry.internalMethod02656(nestedValue0049);
                }
            } else if (packet instanceof Packets.InternalType0338 && WorldKey.sameWorld((nestedValue0119 = (Packets.InternalType0338)packet).author().gameInfo())) {
                WaypointsModule iModuleEntry = RockstarClient.getInstance().getModuleManager().getModule(WaypointsModule.class);
                iModuleEntry.internalMethod06096(nestedValue0119);
            }
        }
    }

    private static String stripMarker(String string) {
        return string.replaceFirst("^\\$\\{[^}]*}\\s*", "");
    }

    private void onMessage(String string, long l, Packets.InternalType0451 nestedValue0158, String string2) {
        Chat chat;
        block24: {
            boolean bl;
            chat = Information.byName(string);
            boolean bl2 = nestedValue0158.username().equals(Information.getPreferUser().username());
            boolean bl3 = !chat.getName().equals(GLOBAL_CHAT_ID);
            boolean bl4 = bl = !chat.hasMessage(l);
            if (bl3 && bl) {
                chat.setLastMessageTime(System.currentTimeMillis());
            }
            if (!bl2 && bl) {
                boolean bl5;
                boolean bl6 = Information.friend(nestedValue0158.username()) != null;
                GlobalsMenuModule typedValue184 = RockstarClient.getInstance().getModuleManager().getModule(GlobalsMenuModule.class);
                boolean bl7 = bl5 = typedValue184 != null && typedValue184.internalMethod03294(bl6);
                if (Mentions.mentionsMe(string2) && typedValue184 != null && typedValue184.internalMethod09556()) {
                    RockstarClient.getInstance().internalMethod02503().internalMethod06070(nestedValue0158, RocknetListener.stripMarker(string2));
                } else if (bl5) {
                    Object object = string2;
                    if (string2.startsWith("${share=")) {
                        if (bl3) {
                            Matcher matcher = SHARE_PATTERN.matcher(string2);
                            String string3 = matcher.find() ? matcher.group(2) : "";
                            object = LanguageManager.internalMethod07214(ShareMessage.titleKey(string3)) + ": " + SHARE_PATTERN.matcher(string2).replaceFirst("").trim();
                            RockstarClient.getInstance().internalMethod02503().internalMethod00433(nestedValue0158, (String)object);
                        }
                    } else if (string2.startsWith("${cords=")) {
                        if (bl3) {
                            object = LanguageManager.internalMethod07214("rocknet.menu.geolocation");
                            RockstarClient.getInstance().internalMethod02503().internalMethod00433(nestedValue0158, (String)object);
                        }
                    } else if (string2.startsWith("${reply=")) {
                        Matcher matcher = REPLY_PATTERN.matcher(string2);
                        if (matcher.find()) {
                            try {
                                long l2 = Long.parseLong(matcher.group(1));
                                Message message = chat.getMessage(l2);
                                if (message != null && message.self()) {
                                    object = string2.replaceFirst("^\\$\\{[^}]*}\\s*", "");
                                    RockstarClient.getInstance().internalMethod02503().internalMethod00433(nestedValue0158, (String)object);
                                } else if (bl3) {
                                    object = string2.replaceFirst("^\\$\\{[^}]*}\\s*", "");
                                    RockstarClient.getInstance().internalMethod02503().internalMethod00433(nestedValue0158, (String)object);
                                }
                                break block24;
                            }
                            catch (NumberFormatException numberFormatException) {
                                if (bl3) {
                                    object = string2.replaceFirst("^\\$\\{[^}]*}\\s*", "");
                                    RockstarClient.getInstance().internalMethod02503().internalMethod00433(nestedValue0158, (String)object);
                                }
                                break block24;
                            }
                        }
                        if (bl3) {
                            object = string2.replaceFirst("^\\$\\{[^}]*}\\s*", "");
                            RockstarClient.getInstance().internalMethod02503().internalMethod00433(nestedValue0158, (String)object);
                        }
                    } else if (bl3) {
                        RockstarClient.getInstance().internalMethod02503().internalMethod00433(nestedValue0158, string2);
                    }
                }
            }
        }
        try {
            Message message = RocknetListener.getMessage(chat, string2, nestedValue0158);
            chat.appendMessage(l, message);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void applyPrivateHistory(Packets.InternalType0133 nestedValue0057) {
        if (!nestedValue0057.with().isBlank()) {
            this.applyHistory(nestedValue0057.with(), nestedValue0057.messages(), nestedValue0057.more());
            return;
        }
        String string2 = Information.getPreferUser().username();
        LinkedHashMap<String, List> linkedHashMap = new LinkedHashMap<String, List>();
        for (Packets.InternalType0144 nestedValue0061 : nestedValue0057.messages()) {
            String string3;
            String string4 = string3 = nestedValue0061.author().username().equals(string2) ? nestedValue0061.toUsername() : nestedValue0061.author().username();
            if (string3.isBlank()) continue;
            linkedHashMap.computeIfAbsent(string3, string -> new ArrayList()).add(nestedValue0061);
        }
        linkedHashMap.forEach((string, list) -> this.applyHistory((String)string, (List<Packets.InternalType0144>)list, true));
    }

    private void applyHistory(String string, List<Packets.InternalType0144> list, boolean bl) {
        Chat chat = Information.byName(string);
        LinkedHashMap<Long, Message> linkedHashMap = new LinkedHashMap<Long, Message>();
        long l = 0L;
        for (Packets.InternalType0144 nestedValue0061 : list) {
            try {
                Message message = RocknetListener.getMessage(chat, nestedValue0061.message(), nestedValue0061.author(), linkedHashMap);
                message.animation().internalMethod07060(1.0f);
                linkedHashMap.put(nestedValue0061.id(), message);
                l = Math.max(l, nestedValue0061.timestamp());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        chat.mergeHistory(linkedHashMap, bl);
        if (!linkedHashMap.isEmpty()) {
            chat.setHistoryRequestedAt(0L);
        }
        if (l > chat.getLastMessageTime()) {
            chat.setLastMessageTime(l);
        }
    }

    private void savePreset(String string, String string2) {
        if (string2 == null || string2.isBlank()) {
            return;
        }
        String string3 = RocknetListener.safePresetName(string);
        if (string3.isEmpty()) {
            return;
        }
        MinecraftClient.getInstance().execute(() -> {
            SwingPresetManager internalValue0003 = RockstarClient.getInstance().internalMethod01001();
            internalValue0003.internalMethod08675();
            Object object = string3;
            for (int i = 2; internalValue0003.internalMethod06262((String)object) != null && i < 100; ++i) {
                object = string3 + " " + i;
            }
            File file = new File(ConfigManager.internalField0148, "presets/swing");
            File file2 = new File(file, (String)object + ".rock");
            try {
                ConfigManager.internalMethod04682(file2, string2);
                internalValue0003.internalMethod08675();
            }
            catch (IOException iOException) {
                RockstarClient.internalField0572.error("rocknet: \u043d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u0441\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0440\u0435\u0441\u0435\u0442 \u2014 {}", (Object)iOException.getMessage());
            }
        });
    }

    private static String safePresetName(String string) {
        if (string == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(string.length());
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c < ' ' || "/\\:*?\"<>|".indexOf(c) >= 0) continue;
            stringBuilder.append(c);
        }
        String string2 = stringBuilder.toString().trim();
        while (string2.startsWith(".")) {
            string2 = string2.substring(1).trim();
        }
        if (string2.length() > 48) {
            string2 = string2.substring(0, 48).trim();
        }
        if (string2.equalsIgnoreCase("autosave")) {
            string2 = "autosave (chat)";
        }
        return string2;
    }

    @NotNull
    private static Message getMessage(Chat chat, String string, Packets.InternalType0451 nestedValue0158) {
        return RocknetListener.getMessage(chat, string, nestedValue0158, null);
    }

    @NotNull
    private static Message getMessage(Chat chat, String string, Packets.InternalType0451 nestedValue0158, Map<Long, Message> map) {
        Matcher matcher;
        Matcher matcher2;
        Matcher matcher3 = SHARE_PATTERN.matcher(string);
        if (matcher3.find()) {
            try {
                long l = Long.parseLong(matcher3.group(1));
                String string2 = matcher3.group(2);
                String string3 = matcher3.replaceFirst("").trim();
                return new ShareMessage(nestedValue0158, string3, nestedValue0158.username().equals(Information.getPreferUser().username()), l, string2);
            }
            catch (NumberFormatException numberFormatException) {
                RockstarClient.internalField0572.error("rocknet: \u0431\u0438\u0442\u044b\u0439 \u043c\u0430\u0440\u043a\u0435\u0440 share \u2014 {}", (Object)numberFormatException.getMessage());
            }
        }
        if ((matcher2 = CORDS_PATTERN.matcher(string)).find()) {
            try {
                int n = Integer.parseInt(matcher2.group(1));
                int n2 = Integer.parseInt(matcher2.group(2));
                int n3 = Integer.parseInt(matcher2.group(3));
                String string4 = matcher2.replaceFirst("").trim();
                return new CordsMessage(nestedValue0158, string4, nestedValue0158.username().equals(Information.getPreferUser().username()), new BlockPos(n, n2, n3));
            }
            catch (NumberFormatException numberFormatException) {
                RockstarClient.internalField0572.error(LanguageManager.internalMethod00160("rocknet.coordinates.parse_error", numberFormatException.getMessage()));
            }
        }
        if ((matcher = REPLY_PATTERN.matcher(string)).find()) {
            try {
                boolean bl;
                long l = Long.parseLong(matcher.group(1));
                String string5 = matcher.replaceFirst("").trim();
                Message message = map == null ? null : map.get(l);
                boolean bl2 = bl = chat.hasNonBlankMessage(l) || message != null && !message.text().isBlank();
                if (bl) {
                    return new ReplyMessage(nestedValue0158, string5, nestedValue0158.username().equals(Information.getPreferUser().username()), l);
                }
                return new Message(nestedValue0158, string5, nestedValue0158.username().equals(Information.getPreferUser().username()));
            }
            catch (NumberFormatException numberFormatException) {
                RockstarClient.internalField0572.error(LanguageManager.internalMethod00160("rocknet.reply.parse_error", numberFormatException.getMessage()));
            }
        }
        return new Message(nestedValue0158, string.trim(), nestedValue0158.username().equals(Information.getPreferUser().username()));
    }

    @Generated
    public static int getOnline() {
        return online;
    }

    @Generated
    public static int getGuests() {
        return guests;
    }

    @Generated
    public static int getSiteOnline() {
        return siteOnline;
    }
}
