package haron.modules.utilities;

import haron.events.ChatSendEvent;
import haron.events.EventDispatcher;
import haron.markers.MarkerIcon;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.utilities.GpsMarkerStorage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;

@ModuleInfo(a="GPS Command", b="Local waypoints via .gps add / list / del", c=ModuleCategory.UTILITIES)
public class GpsCommand
extends HaronModule {
    private static final Color GPS_COLOR = new Color(80, 220, 100);
    public static int a;
    public static boolean b;

    private Waypoint findByName(String string) {
        for (Waypoint yo0tnu2 : this.localMarkers()) {
            if (!yo0tnu2.a().equalsIgnoreCase(string)) continue;
            return yo0tnu2;
        }
        return null;
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onChatSend(ChatSendEvent event) {
        String message = event.getMessage().trim();
        if (!message.startsWith(".gps")) {
            return;
        }

        event.a();
        String[] parts = message.split("\\s+", 3);
        String command = parts.length >= 2 ? parts[1].toLowerCase(Locale.ROOT) : "";
        switch (command) {
            case "add" -> {
                if (GpsCommand.c.player == null) {
                    this.feedback("§cНет игрока.");
                    return;
                }
                int x = (int)Math.floor(GpsCommand.c.player.getX());
                int y = (int)Math.floor(GpsCommand.c.player.getY());
                int z = (int)Math.floor(GpsCommand.c.player.getZ());
                String name = parts.length < 3 || parts[2].trim().isEmpty()
                        ? GpsCommand.$sf$0(x, z)
                        : parts[2].trim();
                Waypoint marker = this.findByName(name);
                if (marker != null) {
                    marker.a(x);
                    marker.b(y);
                    marker.c(z);
                    this.feedback(GpsCommand.$sf$1(name, x, y, z));
                } else {
                    MarkerRegistry.a(new Waypoint(name, x, y, z, GPS_COLOR, MarkerIcon.HOME));
                    this.feedback(GpsCommand.$sf$2(name, x, y, z));
                }
                GpsMarkerStorage.get().save();
            }
            case "list" -> {
                List<Waypoint> markers = this.localMarkers();
                if (markers.isEmpty()) {
                    this.feedback("§7Нет локальных меток.");
                    return;
                }
                this.feedback(GpsCommand.$sf$3(markers.size()));
                for (Waypoint marker : markers) {
                    this.feedback(GpsCommand.$sf$4(marker.a(), marker.b(), marker.c(), marker.d()));
                }
            }
            case "del", "delete", "remove" -> {
                if (parts.length < 3 || parts[2].trim().isEmpty()) {
                    this.feedback("§cИспользование: .gps del <имя>");
                    return;
                }
                String name = parts[2].trim();
                Waypoint marker = this.findByName(name);
                if (marker == null) {
                    this.feedback(GpsCommand.$sf$5(name));
                    return;
                }
                MarkerRegistry.b(marker);
                this.feedback(GpsCommand.$sf$6(name));
                GpsMarkerStorage.get().flushNow();
            }
            default -> {
                this.feedback("§7Использование:");
                this.feedback("  §e.gps add [имя]§r — добавить метку на текущей позиции");
                this.feedback("  §e.gps list§r — показать все метки");
                this.feedback("  §e.gps del <имя>§r — удалить метку");
            }
        }
    }

    private void feedback(String string) {
        if (GpsCommand.c.player != null) {
            GpsCommand.c.player.sendMessage((Text)Text.literal((String)string), false);
        }
    }

    private List<Waypoint> localMarkers() {
        ArrayList<Waypoint> arrayList = new ArrayList<Waypoint>();
        for (Waypoint yo0tnu2 : MarkerRegistry.a()) {
            if (yo0tnu2.j()) continue;
            arrayList.add(yo0tnu2);
        }
        return arrayList;
    }

    public GpsCommand() {
        EventDispatcher.EVENT_BUS.subscribe((Object)this);
        GpsMarkerStorage.get().init();
    }

    private static /* synthetic */ String $sf$0(int n, int n2) {
        return "GPS " + n + " " + n2;
    }

    private static /* synthetic */ String $sf$4(String string, int n, int n2, int n3) {
        return "  §e" + string + "§r — " + n + ", " + n2 + ", " + n3;
    }

    private static /* synthetic */ String $sf$3(int n) {
        return "§aЛокальные метки (" + n + "):";
    }

    private static /* synthetic */ String $sf$5(String string) {
        return "§cМетка §e" + string + "§c не найдена.";
    }

    private static /* synthetic */ String $sf$1(String string, int n, int n2, int n3) {
        return "§aОбновлена§r метка §e" + string + "§r → " + n + ", " + n2 + ", " + n3;
    }

    private static /* synthetic */ String $sf$6(String string) {
        return "§cУдалена§r метка §e" + string;
    }

    private static /* synthetic */ String $sf$2(String string, int n, int n2, int n3) {
        return "§aДобавлена§r метка §e" + string + "§r (" + n + ", " + n2 + ", " + n3 + ")";
    }

    @Override
    public void onEnable() {
    }
}
