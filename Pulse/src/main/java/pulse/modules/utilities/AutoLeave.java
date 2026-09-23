package pulse.modules.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pulse.events.ClientTickEvent;
import pulse.events.PacketEvent;
import pulse.events.WorldChangeEvent;
import pulse.gui.friends.FriendLookup;
import pulse.media.chat.ChatMessages;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.settings.ModeSetting;
import ru.pulse.mixin.accessor.PlayerListHudAccessor;

@ModuleInfo(a = "Auto Leave", b = "РђРІС‚РѕРјР°С‚РёС‡РµСЃРєРёР№ РІС‹С…РѕРґ РїСЂРё РѕР±РЅР°СЂСѓР¶РµРЅРёРё РёРіСЂРѕРєРѕРІ", c = ModuleCategory.UTILITIES)
public class AutoLeave extends ClientModule {
   private final ModeSetting e = new ModeSetting("Р РµР¶РёРј РѕС‚РєР»СЋС‡РµРЅРёСЏ", new String[]{"Р’С‹С…РѕРґ СЃ СЃРµСЂРІРµСЂР°", "/hub"}, "Р’С‹С…РѕРґ СЃ СЃРµСЂРІРµСЂР°");
   private final Map<Integer, Long> f = new HashMap<>();
   private boolean g = false;
   private boolean h = false;
   private boolean i = false;
   private int j = -1;
   private long k = 0L;
   private long l = 0L;
   private final Pattern m = Pattern.compile("\\((.*?)\\)\\s+.*?\\s+(\\S+)\\s+[вЂ”вЂ“-]\\s+(\\d+\\.\\d+)\\s+Р±Р»РѕРєРѕРІ");
   private final Pattern n = Pattern.compile(".*РљРѕРјР°РЅРґР° Р±СѓРґРµС‚ РґРѕСЃС‚СѓРїРЅР° С‡РµСЂРµР·\\s+(\\d+)\\s+РјРёРЅ\\.?\\s*(\\d+)?\\s*СЃРµРє\\.?");
   private final Pattern o = Pattern.compile("Р’РѕРєСЂСѓРі РІР°СЃ РЅРёРєРѕРіРѕ РЅРµС‚");
   private final Pattern p = Pattern.compile("РџРѕРІС‚РѕСЂРёС‚Рµ С‚РµРєСЃС‚ РµС‰Рµ СЂР°Р·\\.");
   public static int a;
   public static boolean b;

   @Override
   public void e() {
      super.e();
      this.f.clear();
      this.g = false;
      this.h = false;
      this.i = false;
      this.j = -1;
      this.k = 0L;
      this.l = 0L;
      if (this.o()) {
         this.h = true;
         this.k = System.currentTimeMillis();
      }
   }

   @Override
   public void f() {
      super.f();
      this.f.clear();
      this.g = false;
      this.h = false;
      this.i = false;
      this.l = 0L;
   }

   @EventHandler
   private void a(WorldChangeEvent worldChangeEvent) {
      if (this.o()) {
         this.g = false;
         this.h = true;
         this.i = false;
         this.k = System.currentTimeMillis();
      }
   }

   @EventHandler
   private void a(ClientTickEvent clientTickEvent) {
      if (this.o()) {
         long jCurrentTimeMillis = System.currentTimeMillis();
         int iN = this.n();
         if (iN != -1 && iN != this.j) {
            this.j = iN;
            this.g = false;
            this.i = false;
         }

         if (jCurrentTimeMillis - this.l > 1000L) {
            this.l = jCurrentTimeMillis;
            this.p();
         }

         if (this.h) {
            if (this.j != -1) {
               this.h = false;
               Long l = this.f.get(this.j);
               if (l == null || jCurrentTimeMillis >= l) {
                  this.q();
                  this.i = true;
               }
            } else if (jCurrentTimeMillis - this.k > 5000L) {
               this.h = false;
            }
         }

         if (this.h || this.i || this.j == -1) {
            return;
         }

         Long l2 = this.f.get(this.j);
         if (l2 == null || jCurrentTimeMillis >= l2) {
            this.q();
            this.i = true;
         }
      }
   }

   private void p() {
      long jCurrentTimeMillis = System.currentTimeMillis();
      Iterator<Map.Entry<Integer, Long>> it = this.f.entrySet().iterator();

      while (it.hasNext()) {
         Map.Entry<Integer, Long> next = it.next();
         int iIntValue = next.getKey();
         if (jCurrentTimeMillis >= next.getValue()) {
            it.remove();
            if (iIntValue == this.j && !this.i) {
               if (!this.g) {
                  this.q();
                  this.i = true;
               } else if (b) {
                  throw new IllegalAccessError();
               }
            }
         }
      }
   }

   @EventHandler
   public void a(PacketEvent packetEvent) {
      if (this.o() && packetEvent.d() instanceof GameMessageS2CPacket) {
         GameMessageS2CPacket GameMessageS2CPacketVarD = (GameMessageS2CPacket)packetEvent.d();
         String strStrip = Formatting.strip(GameMessageS2CPacketVarD.content().getString());
         if (strStrip.contains("[вњ‡] Р Р°РґР°СЂ вЂє")) {
            this.g = true;
         }

         Matcher matcher = this.n.matcher(strStrip);
         if (matcher.find()) {
            this.a(matcher);
            packetEvent.b();
            return;
         }

         if (this.g) {
            if (this.o.matcher(strStrip).find()) {
               this.g = false;
               this.i = false;
            } else if (this.m.matcher(strStrip).find()) {
               this.a(strStrip);
            } else if (this.p.matcher(strStrip).find()) {
               packetEvent.b();
               this.q();
            }
         }
      }
   }

   private void q() {
      if (c.player != null) {
         ChatMessages.a("near max");
         this.g = true;
      }
   }

   private void a(Matcher matcher) {
      this.g = false;
      this.i = false;
      this.f
         .put(
            this.j,
            System.currentTimeMillis()
               + (Integer.parseInt(matcher.group(1)) * 60L + (matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0)) * 1000L
               + 2000L
         );
   }

   private void a(String str) {
      Matcher matcher = this.m.matcher(str);
      if (matcher.find()) {
         String strGroup = matcher.group(2);
         if (FriendLookup.a(strGroup)) {
            this.g = false;
            this.i = false;
            return;
         }

         this.g = false;
         this.i = false;
         String str2 = "РћР±РЅР°СЂСѓР¶РµРЅ РёРіСЂРѕРє " + strGroup + " РЅР° СЂР°СЃСЃС‚РѕСЏРЅРёРё " + matcher.group(3) + " Р±Р»РѕРєРѕРІ";
         if (c.player != null) {
            if (this.e.b("Р’С‹С…РѕРґ СЃ СЃРµСЂРІРµСЂР°")) {
               if (c.getNetworkHandler() != null) {
                  c.getNetworkHandler().getConnection().disconnect(Text.of(str2));
               }
            } else if (this.e.b("/hub")) {
               ChatMessages.a("hub");
               ChatMessages.a((Object)str2);
            }
         }
      }
   }

   public int n() {
      try {
         if (c.inGameHud != null && c.inGameHud.getPlayerListHud() != null) {
            Text header = ((PlayerListHudAccessor)c.inGameHud.getPlayerListHud()).getHeader();
            if (header == null) {
               return -1;
            }

            String strStrip = Formatting.strip(header.getString());
            if (strStrip != null && strStrip.contains("РђРЅР°СЂС…РёСЏ-")) {
               String[] strArrSplit = strStrip.split("РђРЅР°СЂС…РёСЏ-");
               if (strArrSplit.length < 2) {
                  return -1;
               }

               try {
                  return Integer.parseInt(strArrSplit[1].trim());
               } catch (NumberFormatException e) {
                  return -1;
               }
            } else {
               return -1;
            }
         } else {
            return -1;
         }
      } catch (Exception e2) {
         return -1;
      }
   }

   public boolean o() {
      if (c.world != null) {
         if (c.world.getRegistryKey().getValue().toString().equals("minecraft:overworld")) {
            if (!c.isInSingleplayer()) {
               return true;
            }
         } else if (b) {
         }
      }

      return false;
   }

   public static String c(String str, String str2, int i, int i2, int i3, int i4) {
      return null;
   }
}
