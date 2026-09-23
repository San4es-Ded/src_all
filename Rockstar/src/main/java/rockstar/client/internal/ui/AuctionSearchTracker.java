package rockstar.client.internal.ui;


import rockstar.client.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import rockstar.client.internal.ui.AuctionTooltipParser;
import rockstar.client.MinecraftClientAccess;

public final class AuctionSearchTracker
implements MinecraftClientAccess {
    private static final Pattern internalField0293 = Pattern.compile("^(?:ah|auc|auction|\u0430\u0443\u043a\u0446\u0438\u043e\u043d)(?:\\s+(.*))?$", 66);
    private static final Pattern internalField0294 = Pattern.compile("^(?:donmarket|dmarket|\u0434\u043e\u043d\u043c\u0430\u0440\u043a\u0435\u0442)(?:\\s+.*)?$", 66);
    private static final String[] internalField0359 = new String[]{"search", "find", "s", "\u043f\u043e\u0438\u0441\u043a"};
    private static final Map<String, Long> internalField0543 = new LinkedHashMap<String, Long>();
    private static volatile String internalField0248 = "";
    private static volatile String internalField0247 = "";
    private static volatile long internalField1058 = -4611686018427387904L;
    private static volatile boolean internalField0277;
    private static volatile int internalField1055;
    private static String internalField1077;
    private static boolean internalField0276;
    private static long internalField1060;

    public static void internalMethod05770(Packet<?> packet) {
        ChatMessageC2SPacket chatMessageC2SPacket;
        String string;
        if (packet instanceof CommandExecutionC2SPacket) {
            CommandExecutionC2SPacket commandExecutionC2SPacket = (CommandExecutionC2SPacket)packet;
            AuctionSearchTracker.internalMethod06216(commandExecutionC2SPacket.command());
            return;
        }
        if (packet instanceof ChatMessageC2SPacket && (string = (chatMessageC2SPacket = (ChatMessageC2SPacket)packet).chatMessage()) != null && string.startsWith("/")) {
            AuctionSearchTracker.internalMethod06216(string.substring(1));
        }
    }

    private static void internalMethod06216(String string) {
        if (string == null) {
            return;
        }
        String string2 = string.trim();
        if (string2.startsWith("/")) {
            string2 = string2.substring(1).trim();
        }
        if (string2.isEmpty()) {
            return;
        }
        Matcher matcher = internalField0293.matcher(string2);
        if (matcher.matches()) {
            internalField0248 = string2;
            internalField0247 = AuctionSearchTracker.internalMethod06783(matcher.group(1));
            internalField0277 = false;
            AuctionSearchTracker.internalMethod01247();
            return;
        }
        if (internalField0294.matcher(string2).matches()) {
            internalField0248 = string2;
            internalField0247 = "";
            internalField0277 = true;
            AuctionSearchTracker.internalMethod01247();
        }
    }

    private static void internalMethod01247() {
        internalField1058 = System.currentTimeMillis();
        internalField1055 = Integer.MIN_VALUE;
    }

    private static String internalMethod06783(String string) {
        if (string == null) {
            return "";
        }
        String string2 = string.trim();
        String string3 = string2.toLowerCase(Locale.ROOT);
        for (String string4 : internalField0359) {
            if (string3.equals(string4)) {
                return "";
            }
            if (!string3.startsWith(string4 + " ")) continue;
            return string2.substring(string4.length()).trim();
        }
        return "";
    }

    public static String internalMethod04907() {
        return internalField0247;
    }

    private static boolean internalMethod01251() {
        return System.currentTimeMillis() - internalField1058 <= 20000L;
    }

    public static boolean internalMethod06217(String string) {
        Screen screen = AuctionSearchTracker.internalField0149.currentScreen;
        if (!(screen instanceof HandledScreen)) {
            return false;
        }
        HandledScreen handledScreen = (HandledScreen)screen;
        if (!AuctionTooltipParser.internalMethod03300(handledScreen.getTitle().getString()).equals(string)) {
            return false;
        }
        return AuctionSearchTracker.internalMethod01007(handledScreen);
    }

    public static boolean internalMethod01007(HandledScreen<?> handledScreen) {
        if (handledScreen == null || handledScreen.getScreenHandler() == null) {
            return false;
        }
        String string = AuctionTooltipParser.internalMethod03300(handledScreen.getTitle().getString());
        int n = handledScreen.getScreenHandler().syncId;
        String string2 = n + " " + string;
        long l = System.currentTimeMillis();
        if (string2.equals(internalField1077) && (internalField0276 || l - internalField1060 < 250L)) {
            return internalField0276;
        }
        boolean bl = AuctionSearchTracker.internalMethod02751(handledScreen, string, n, l);
        internalField1077 = string2;
        internalField0276 = bl;
        internalField1060 = l;
        if (bl) {
            AuctionSearchTracker.internalMethod04122(string, l);
        }
        return bl;
    }

    public static boolean internalMethod01248() {
        Object object = AuctionSearchTracker.internalField0149.currentScreen;
        if (!(object instanceof HandledScreen)) {
            return false;
        }
        HandledScreen handledScreen = (HandledScreen)object;
        object = AuctionTooltipParser.internalMethod03300(handledScreen.getTitle().getString());
        if (AuctionTooltipParser.internalMethod00707((String)object)) {
            return true;
        }
        if (!internalField0277 || !AuctionSearchTracker.internalMethod01251()) {
            return false;
        }
        return AuctionSearchTracker.internalMethod01007(handledScreen) && internalField1055 == handledScreen.getScreenHandler().syncId;
    }

    private static boolean internalMethod02751(HandledScreen<?> handledScreen, String string, int n, long l) {
        if (AuctionTooltipParser.internalMethod02206(string)) {
            return true;
        }
        if (AuctionSearchTracker.internalMethod04123(string, l)) {
            return true;
        }
        if (!AuctionSearchTracker.internalMethod05980(handledScreen)) {
            return false;
        }
        if (AuctionSearchTracker.internalMethod00908(n, l)) {
            return true;
        }
        int n2 = AuctionSearchTracker.internalMethod01006(handledScreen);
        if (n2 <= 0) {
            return false;
        }
        return AuctionSearchTracker.internalMethod04810(string) || n2 >= 3;
    }

    private static boolean internalMethod04810(String string) {
        return string.startsWith(":");
    }

    public static boolean internalMethod05980(HandledScreen<?> handledScreen) {
        GenericContainerScreenHandler genericContainerScreenHandler;
        ScreenHandler screenHandler = handledScreen.getScreenHandler();
        return screenHandler instanceof GenericContainerScreenHandler && (genericContainerScreenHandler = (GenericContainerScreenHandler)screenHandler).getRows() >= 5;
    }

    private static boolean internalMethod00908(int n, long l) {
        if (l - internalField1058 > 20000L) {
            return false;
        }
        if (internalField1055 == Integer.MIN_VALUE) {
            internalField1055 = n;
        }
        return internalField1055 == n;
    }

    private static int internalMethod01006(HandledScreen<?> handledScreen) {
        ScreenHandler screenHandler = handledScreen.getScreenHandler();
        int n = screenHandler.slots.size() - 36;
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            LoreComponent loreComponent;
            Slot slot = screenHandler.getSlot(i);
            if (slot == null || !slot.hasStack() || (loreComponent = (LoreComponent)slot.getStack().get(DataComponentTypes.LORE)) == null || loreComponent.lines().isEmpty() || AuctionTooltipParser.internalMethod02982(loreComponent.lines()) <= 0L || ++n2 < 3) continue;
            return n2;
        }
        return n2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void internalMethod04122(String string, long l) {
        if (string.isEmpty()) {
            return;
        }
        Map<String, Long> map = internalField0543;
        synchronized (map) {
            internalField0543.remove(string);
            internalField0543.put(string, l);
            while (internalField0543.size() > 16) {
                Iterator<String> iterator = internalField0543.keySet().iterator();
                iterator.next();
                iterator.remove();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean internalMethod04123(String string, long l) {
        if (string.isEmpty()) {
            return false;
        }
        Map<String, Long> map = internalField0543;
        synchronized (map) {
            Long l2 = internalField0543.get(string);
            if (l2 == null) {
                return false;
            }
            if (l - l2 > 600000L) {
                internalField0543.remove(string);
                return false;
            }
            return true;
        }
    }

    @Generated
    private AuctionSearchTracker() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        internalField1055 = Integer.MIN_VALUE;
        internalField1077 = "";
    }
}

