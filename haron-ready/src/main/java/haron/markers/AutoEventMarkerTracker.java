package haron.markers;

import haron.client.MinecraftClientAccess;
import haron.events.PacketEvent;
import haron.events.WorldChangedEvent;
import haron.events.ClientTickEvent;
import haron.hud.core.HudServiceInfo;
import haron.hud.core.HudService;
import haron.hud.notifications.NotificationHudManager;
import haron.markers.EventMarkerProvider;
import haron.markers.providers.DelayedEventMarkerProvider;
import haron.markers.providers.ActiveEventMarkerProvider;
import haron.markers.providers.EventListMarkerProvider;
import haron.markers.MarkerStyle;
import haron.markers.QuickMarkerPreferences;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import ru.haron.mixin.accessor.PlayerListHudAccessor;

@HudServiceInfo(enabledByDefault=true)
public class AutoEventMarkerTracker
extends HudService
implements MinecraftClientAccess {
    private static final long DEFAULT_MARKER_LIFETIME_MS = 600000L;
    private static final int JOIN_REFRESH_DELAY_TICKS = 20;
    private static final Pattern SERVER_NUMBER_PATTERN = Pattern.compile("(?:анархия|сервер|гриф)\\D*(\\d+)", 2);
    private static final Pattern HEADER_SERVER_PATTERN = Pattern.compile("(?:анархия|сервер|гриф)\\D*(\\d+)(?:\\D+([a-z0-9_.:-]+))?", 2);
    private EventMarkerProvider activeProvider;
    private boolean needsProviderRefresh;
    private boolean waitingForCommandResponse;
    private String pendingMarkerName;
    public static int a;
    public static boolean b;
    private final List<EventMarkerProvider> providers = new ArrayList<EventMarkerProvider>();
    private int pendingServerId = -1;

    private MarkerStyle defaultStyle() {
        return new MarkerStyle(new Color(255, 165, 0));
    }

    public void clearMarkers() {
        MarkerRegistry.c();
        this.pendingMarkerName = null;
    }

    @EventHandler
    private void onClientTick(ClientTickEvent q8krcw2) {
        if (QuickMarkerPreferences.d() && AutoEventMarkerTracker.c.player != null && this.isConnectedToMarkedServer()) {
            this.updateActiveProvider();
            this.refreshProviderAfterJoin();
            MarkerRegistry.e();
            if (this.activeProvider != null) {
                this.activeProvider.tick(this);
            }
        }
    }

    public void sendCommand(String string, String string2) {
        if (string == null || !this.waitingForCommandResponse) {
            return;
        }
        NotificationHudManager.a("%s %s", string, string2 != null ? string2 : "");
        this.waitingForCommandResponse = false;
    }

    @EventHandler
    private void onWorldChanged(WorldChangedEvent m7z9q12) {
        if (QuickMarkerPreferences.d()) {
            this.needsProviderRefresh = true;
        }
    }

    @EventHandler
    private void onGameMessage(PacketEvent g07m232) {
        if (!QuickMarkerPreferences.d() || AutoEventMarkerTracker.c.player == null) {
            return;
        }
        if (!(g07m232.d() instanceof GameMessageS2CPacket)) {
            return;
        }
        GameMessageS2CPacket gameMessageS2CPacket = (GameMessageS2CPacket)g07m232.d();
        String string = gameMessageS2CPacket.content().getString();
        String string2 = Formatting.strip((String)string).toLowerCase(Locale.ROOT);
        this.updateActiveProvider();
        boolean bl = this.waitingForCommandResponse;
        String string3 = this.currentServerAddress();
        for (EventMarkerProvider gyqquz2 : this.providers) {
            if (!gyqquz2.supportsServer(string3)) continue;
            boolean bl2 = gyqquz2.handleMessage(string2, string, this);
            if (bl2 && bl) {
                g07m232.b();
            }
            if (!bl2) continue;
            return;
        }
    }

    public void setPendingMarker(String string, int n) {
        this.pendingMarkerName = string;
        this.pendingServerId = n;
    }

    public void addPendingMarker(int[] nArray) {
        if (this.pendingMarkerName == null || nArray == null) {
            return;
        }
        this.addMarker(this.pendingMarkerName, nArray, -1);
    }

    private Text playerListHeader() {
        if (AutoEventMarkerTracker.c.inGameHud == null || AutoEventMarkerTracker.c.inGameHud.getPlayerListHud() == null) {
            return null;
        }
        return ((PlayerListHudAccessor)AutoEventMarkerTracker.c.inGameHud.getPlayerListHud()).getHeader();
    }

    public void sendDelayLookup(int n) {
        if (!this.waitingForCommandResponse || this.activeProvider == null) {
            return;
        }
        NotificationHudManager.a("%s %s", this.activeProvider.command(), this.formatSeconds(n));
        this.waitingForCommandResponse = false;
    }

    private String formatSeconds(int n) {
        return AutoEventMarkerTracker.$sf$0(n);
    }

    public void sendTimedLookup(String string, int n) {
        if (this.waitingForCommandResponse) {
            NotificationHudManager.a("%s %s", this.formatSeconds(n), this.capitalizeEventName(string));
            this.waitingForCommandResponse = false;
        }
    }

    public void addMarker(String string, int[] nArray, int n) {
        if (string == null || nArray == null || nArray.length < 3) {
            return;
        }
        int n2 = this.h();
        String string2 = this.i();
        Waypoint yo0tnu2 = MarkerRegistry.a(nArray[0], nArray[1], nArray[2]);
        if (yo0tnu2 == null) {
            MarkerStyle se6o972 = this.a(string.toLowerCase(Locale.ROOT));
            MarkerRegistry.a(Waypoint.a(this.capitalizeEventName(string), nArray[0], nArray[1], nArray[2], se6o972.a, se6o972.b, n2, string2, n > 0 ? (long)n * 1000L : 600000L, n > 0));
        } else {
            yo0tnu2.a(this.capitalizeEventName(string));
            if (n > 0) {
                yo0tnu2.a(System.currentTimeMillis() + (long)n * 1000L);
                yo0tnu2.a(true);
            }
        }
    }

    public MarkerStyle findStyle(String string) {
        if (string == null) {
            return this.defaultStyle();
        }
        String string2 = string.toLowerCase(Locale.ROOT);
        if (this.activeProvider != null && this.activeProvider.styles().containsKey(string2)) {
            return this.activeProvider.styles().get(string2);
        }
        Iterator<EventMarkerProvider> providers = this.providers.iterator();
        while (providers.hasNext()) {
            Map<String, MarkerStyle> map = providers.next().styles();
            if (!map.containsKey(string2)) continue;
            return map.get(string2);
        }
        return this.defaultStyle();
    }

    public boolean canRenderServerBoundMarkers() {
        return AutoEventMarkerTracker.c.world != null && "minecraft:overworld".equals(AutoEventMarkerTracker.c.world.getRegistryKey().getValue().toString()) && !c.isInSingleplayer();
    }

    public boolean isConnectedToMarkedServer() {
        if (AutoEventMarkerTracker.c.player == null) {
            return false;
        }
        String string = AutoEventMarkerTracker.c.player.getDisplayName().getString();
        return !string.equals(Formatting.strip((String)string));
    }

    private void updateActiveProvider() {
        String string = this.currentServerAddress();
        if (string == null) {
            this.activeProvider = null;
            return;
        }
        if (this.activeProvider == null || !this.activeProvider.supportsServer(string)) {
            for (EventMarkerProvider gyqquz2 : this.providers) {
                if (!gyqquz2.supportsServer(string)) continue;
                this.activeProvider = gyqquz2;
                return;
            }
            this.activeProvider = null;
        }
    }

    private String currentServerAddress() {
        if (c.getCurrentServerEntry() != null) {
            return AutoEventMarkerTracker.c.getCurrentServerEntry().address;
        }
        return null;
    }

    private void refreshProviderAfterJoin() {
        if (!this.needsProviderRefresh || AutoEventMarkerTracker.c.player.age <= 20 || this.activeProvider == null) {
            return;
        }
        this.requestProviderCommand();
        this.needsProviderRefresh = false;
    }

    public void updatePendingMarkerTimer(int n) {
        if (this.pendingMarkerName == null) {
            return;
        }
        String string = this.capitalizeEventName(this.pendingMarkerName);
        for (Waypoint yo0tnu2 : MarkerRegistry.f()) {
            if (!yo0tnu2.a().equalsIgnoreCase(string)) continue;
            yo0tnu2.a(System.currentTimeMillis() + (long)n * 1000L);
            yo0tnu2.a(true);
            return;
        }
    }

    public void requestProviderCommand() {
        if (this.activeProvider == null) {
            return;
        }
        NotificationHudManager.a(this.activeProvider.command());
        this.waitingForCommandResponse = true;
    }

    private String capitalizeEventName(String string) {
        return string == null || string.isEmpty() ? string : AutoEventMarkerTracker.$sf$1(string.substring(0, 1).toUpperCase(Locale.ROOT), string.substring(1));
    }

    public AutoEventMarkerTracker() {
        this.providers.add(new DelayedEventMarkerProvider());
        this.providers.add(new EventListMarkerProvider());
        this.providers.add(new ActiveEventMarkerProvider());
    }

    public void e() {
        this.requestProviderCommand();
    }

    public String i() {
        Text text = this.playerListHeader();
        if (text == null) {
            return null;
        }
        Matcher matcher = HEADER_SERVER_PATTERN.matcher(Formatting.strip((String)text.getString()));
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    public void b(int n) {
        this.updatePendingMarkerTimer(n);
    }

    public void b(String string, int n) {
        int n2 = 642;
        this.setPendingMarker(string, n);
    }

    public boolean n() {
        return this.needsProviderRefresh;
    }

    public int h() {
        Text text = this.playerListHeader();
        if (text == null) {
            return -1;
        }
        Matcher matcher = SERVER_NUMBER_PATTERN.matcher(Formatting.strip((String)text.getString()));
        if (matcher.find()) {
            return AutoEventMarkerTracker.parseInt(matcher.group(1), -1);
        }
        return -1;
    }

    public boolean f() {
        return this.waitingForCommandResponse;
    }

    public List<EventMarkerProvider> l() {
        return this.providers;
    }

    public void d() {
        this.clearMarkers();
    }

    public void a(int n) {
        this.sendDelayLookup(n);
    }

    public void a(String string, int n) {
        this.sendTimedLookup(string, n);
    }

    public void a(String string, String string2) {
        this.sendCommand(string, string2);
    }

    public MarkerStyle a(String string) {
        return this.findStyle(string);
    }

    public void a(int[] nArray) {
        this.addPendingMarker(nArray);
    }

    public void a(String string, int[] nArray, int n) {
        this.addMarker(string, nArray, n);
    }

    public EventMarkerProvider m() {
        return this.activeProvider;
    }

    public String o() {
        return this.pendingMarkerName;
    }

    public int p() {
        int n = 990;
        return this.pendingServerId;
    }

    public boolean k() {
        return this.canRenderServerBoundMarkers();
    }

    public void g() {
        this.waitingForCommandResponse = true;
    }

    public boolean j() {
        int n = 488;
        return this.isConnectedToMarkedServer();
    }

    private static int parseInt(String string, int n) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            return n;
        }
    }

    private static /* synthetic */ String $sf$0(int n) {
        return n + " сек.";
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + string2;
    }
}
