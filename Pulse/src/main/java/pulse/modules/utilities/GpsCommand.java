package pulse.modules.utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;
import pulse.events.ChatSendEvent;
import pulse.events.EventBusService;
import pulse.markers.MapMarker;
import pulse.markers.MapMarker.Icon;
import pulse.markers.MarkerManager;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;

@ModuleInfo(a = "GPS Command", b = "Local waypoints via .gps add / list / del", c = ModuleCategory.UTILITIES)
public class GpsCommand extends ClientModule {
   private static final Color GPS_COLOR = new Color(80, 220, 100);
   public static int a;
   public static boolean b;

   public GpsCommand() {
      EventBusService.EVENT_BUS.subscribe(this);
      GpsConfigManager.get().init();
   }

   @EventHandler
   public void onChatSend(ChatSendEvent chatSendEvent) {
      String strTrim = chatSendEvent.getMessage().trim();
      if (strTrim.startsWith(".gps")) {
         chatSendEvent.a();
         String[] strArrSplit = strTrim.split("\\s+", 3);
         switch (strArrSplit.length >= 2 ? strArrSplit[1].toLowerCase(Locale.ROOT) : "") {
            case "add":
               if (c.player != null) {
                  int iFloor = (int)Math.floor(c.player.getX());
                  int iFloor2 = (int)Math.floor(c.player.getY());
                  int iFloor3 = (int)Math.floor(c.player.getZ());
                  String strTrim2 = strArrSplit.length >= 3 && !strArrSplit[2].trim().isEmpty() ? strArrSplit[2].trim() : "GPS " + iFloor + " " + iFloor3;
                  MapMarker mapMarkerFindByName = this.findByName(strTrim2);
                  if (mapMarkerFindByName != null) {
                     mapMarkerFindByName.a(iFloor);
                     mapMarkerFindByName.b(iFloor2);
                     mapMarkerFindByName.c(iFloor3);
                     this.feedback("В§aРћР±РЅРѕРІР»РµРЅР°В§r РјРµС‚РєР° В§e" + strTrim2 + "В§r в†’ " + iFloor + ", " + iFloor2 + ", " + iFloor3);
                  } else {
                     MarkerManager.a(new MapMarker(strTrim2, iFloor, iFloor2, iFloor3, GPS_COLOR, MapMarker.Icon.HOME));
                     this.feedback("В§aР”РѕР±Р°РІР»РµРЅР°В§r РјРµС‚РєР° В§e" + strTrim2 + "В§r (" + iFloor + ", " + iFloor2 + ", " + iFloor3 + ")");
                  }

                  GpsConfigManager.get().save();
               } else {
                  this.feedback("В§cРќРµС‚ РёРіСЂРѕРєР°.");
               }
               break;
            case "list":
               List<MapMarker> listLocalMarkers = this.localMarkers();
               if (!listLocalMarkers.isEmpty()) {
                  this.feedback("В§aР›РѕРєР°Р»СЊРЅС‹Рµ РјРµС‚РєРё (" + listLocalMarkers.size() + "):");

                  for (MapMarker mapMarker : listLocalMarkers) {
                     this.feedback("  В§e" + mapMarker.a() + "В§r вЂ” " + mapMarker.b() + ", " + mapMarker.c() + ", " + mapMarker.d());
                  }
               } else {
                  this.feedback("В§7РќРµС‚ Р»РѕРєР°Р»СЊРЅС‹С… РјРµС‚РѕРє.");
               }
               break;
            case "del":
            case "delete":
            case "remove":
               if (strArrSplit.length >= 3 && !strArrSplit[2].trim().isEmpty()) {
                  String strTrim3 = strArrSplit[2].trim();
                  MapMarker mapMarkerFindByName2 = this.findByName(strTrim3);
                  if (mapMarkerFindByName2 == null) {
                     this.feedback("В§cРњРµС‚РєР° В§e" + strTrim3 + "В§c РЅРµ РЅР°Р№РґРµРЅР°.");
                  } else {
                     MarkerManager.b(mapMarkerFindByName2);
                     this.feedback("В§cРЈРґР°Р»РµРЅР°В§r РјРµС‚РєР° В§e" + strTrim3);
                     GpsConfigManager.get().flushNow();
                  }
               } else {
                  this.feedback("В§cРСЃРїРѕР»СЊР·РѕРІР°РЅРёРµ: .gps del <РёРјСЏ>");
               }
               break;
            default:
               this.feedback("В§7РСЃРїРѕР»СЊР·РѕРІР°РЅРёРµ:");
               this.feedback("  В§e.gps add [РёРјСЏ]В§r вЂ” РґРѕР±Р°РІРёС‚СЊ РјРµС‚РєСѓ РЅР° С‚РµРєСѓС‰РµР№ РїРѕР·РёС†РёРё");
               this.feedback("  В§e.gps listВ§r вЂ” РїРѕРєР°Р·Р°С‚СЊ РІСЃРµ РјРµС‚РєРё");
               this.feedback("  В§e.gps del <РёРјСЏ>В§r вЂ” СѓРґР°Р»РёС‚СЊ РјРµС‚РєСѓ");
         }
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   private void feedback(String str) {
      if (c.player != null) {
         c.player.sendMessage(Text.literal(str), false);
      }
   }

   private List<MapMarker> localMarkers() {
      ArrayList arrayList = new ArrayList();

      for (MapMarker mapMarker : MarkerManager.a()) {
         if (!mapMarker.j()) {
            arrayList.add(mapMarker);
         }
      }

      return arrayList;
   }

   private MapMarker findByName(String str) {
      for (MapMarker mapMarker : this.localMarkers()) {
         if (mapMarker.a().equalsIgnoreCase(str)) {
            return mapMarker;
         }
      }

      return null;
   }
}
