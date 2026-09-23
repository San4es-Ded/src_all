package pulse.modules.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Formatting;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pulse.events.ClientTickEvent;
import pulse.events.PacketEvent;
import pulse.events.PacketEvent.MessageDirection;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.settings.BooleanSetting;

@ModuleInfo(a = "RW Helper", b = "РђРІС‚РѕРјР°С‚РёС‡РµСЃРєРё Р·Р°РєСЂС‹РІР°РµС‚ РјРµРЅСЋ СЃРµСЂРІРµСЂР° РёР»Рё С„РёР»СЊС‚СЂСѓРµС‚ Р·Р°РїСЂРµС‰РµРЅРЅС‹Рµ СЃРѕРѕР±С‰РµРЅРёСЏ РІ С‡Р°С‚Рµ", c = ModuleCategory.UTILITIES)
public class RwHelper extends ClientModule {
   private final BooleanSetting a = new BooleanSetting("Р—Р°РєСЂС‹РІР°С‚СЊ РјРµРЅСЋ", true);
   private final BooleanSetting b = new BooleanSetting("Р¤РёР»СЊС‚СЂ Р·Р°РїСЂРµС‰РµРЅРЅС‹С… СЃР»РѕРІ", true);
   private Set<String> e = new HashSet<>(
      Arrays.asList(
         "Р°РєСЂРёРµРЅ(Р°|Сѓ|РѕРј|Рµ|С‡РёРє)?",
         "СЂРёС‡(Р°|Сѓ|РѕРј|РµР№|Рµ)?",
         "РЅСЊСЋРєРѕРґ(РѕРј|Р°|Сѓ|Р°РјРё|РёРє|Рµ)?",
         "СЌРєСЃРїРµРЅСЃРёРІ(РѕРј|Р°|Сѓ|Р°РјРё|Рµ)?",
         "РёРјРїР°РєС‚(РѕРј|Р°|Сѓ|Р°РјРё|РёРє|Рµ)?",
         "СЌРєСЃРµР»Р»РµРЅС‚(РѕРј|Р°|Сѓ|Р°РјРё|РёРє|Рµ)?",
         "СЌРєСЃРµР»РµРЅС‚(РѕРј|Р°|Сѓ|Р°РјРё|РёРє)?",
         "РєР°С‚Р»Р°РІР°РЅ(РѕРј|Р°|Сѓ|Р°РјРё|С‡РёРє)?",
         "РєР°С‚Р»РѕРІР°РЅ(РѕРј|Р°|Сѓ|Р°РјРё|С‡РёРє)?",
         "С†РµР»РµСЃС‚РёР°Р»(РѕРј|Р°|Сѓ|Р°РјРё|Рµ)?",
         "С†РµР»Рє(РѕР№|Р°|Сѓ|Р°РјРё|РѕС‡РєР°|Рµ)?",
         "РјР°С‚РёРєСЃ(РѕРј|Р°|Сѓ|Р°РјРё|Рµ)?",
         "РёРЅРµСЂС‚Рё(СЏ|РµР№|СЋ|СЏРјРё|Рµ)?",
         "СЌРєСЃРї(Р°|РѕР№|РѕСЋ|Сѓ|СѓР»РёС‡РєР°|Рµ)?",
         "С„Р»СЋРіРµСЂ(РѕРј|Р°|Сѓ|Р°РјРё)?",
         "СЂРёРєРµСЂ(Р°|Сѓ|РѕРј|РѕС‡РµРє)?",
         "С„Р°РЅРїРµ(Р№|СЋ|СЏ|РµРј|Рµ|Р№С‡РёРє)?",
         "РІРµРєСЃР°Р№Рґ(РѕРј|Р°|Сѓ|Р°РјРё|РёРє|Рµ)?",
         "РЅСѓСЂСЃСѓР»С‚Р°РЅ(Р°|Сѓ|Рµ|РѕРј|С‡РёРє)?",
         "РЅСѓСЂРёРє(Р°|Сѓ|РѕРј|Рµ)?",
         "РЅСѓСЂР»Р°РЅ(Р°|Сѓ|РѕРј|С‡РёРє|Рµ)?",
         "РІРµРєСЃ(РѕРј|Сѓ|Р°|Р°РјРё|РёРє|Рµ)?",
         "СЂРµР»РµР№Рє(РѕРј|Сѓ|Р°|Р°РјРё|Рµ)?",
         "Р°СЂР±СѓР·(РѕРј|Р°|Сѓ|Р°РјРё|РёРє|Рµ)?",
         "РІРёР»Рґ(РѕРј|Сѓ|Р°|Р°РјРё|РёРє|Рµ)?",
         "С„Р°РЅС‚Р°Р№Рј(Рµ|Р°|Сѓ)?",
         "С…РѕР»РёРє(Рµ|Р°|Сѓ)?",
         "С…РѕР»РёРІРѕСЂР»Рґ(Р°|Сѓ|Рµ)?",
         "СЂРѕРєСЃС‚Р°СЂ(РѕРј|Р°|Сѓ|Р°РјРё|С‡РёРє|Рµ)?",
         "СЂРѕРіР°Р»РёРє(Р°|Сѓ|РѕРј|Рµ)?",
         "С‚Р°РЅРґРµСЂС…Р°Рє(РѕРј|Сѓ|Рё|Р°РјРё|Р°|Рµ)?",
         "Р»РёРєРІРёРґР±Р°СѓРЅСЃ(Р°|Сѓ|Р°РјРё|Рµ)?",
         "expensive",
         "celestial",
         "newcode",
         "arbuz",
         "akrien",
         "nursultan",
         "relake",
         "wild",
         "wurst",
         "catlovan",
         "excellent",
         "rockstar",
         "catlavan",
         "impact",
         "matix",
         "inertia",
         "wex",
         "wexside",
         "nurik",
         "nurlan",
         "rich",
         "funpay",
         "fluger",
         "riker",
         "funtime",
         "holyworld",
         "wwe",
         "hvh",
         "rogalik",
         "thunderhack",
         "liquidbounce"
      )
   );
   private final List<Pattern> compiledPatterns = new ArrayList<>();

   public RwHelper() {
      for (String s : this.e) {
         try {
            this.compiledPatterns.add(Pattern.compile(s, 66));
         } catch (Exception ex) {
            this.compiledPatterns.add(Pattern.compile(Pattern.quote(s), 2));
         }
      }
   }

   @EventHandler
   public void onTick(ClientTickEvent event) {
      if (this.a.get() && c.currentScreen instanceof BookScreen) {
         c.setScreen(null);
      }
   }

   @EventHandler
   private void a(PacketEvent packetEvent) {
      if (c.player != null) {
         if (packetEvent.e() == PacketEvent.MessageDirection.RECIEVE) {
            if (this.a.get()) {
               if (packetEvent.d() instanceof OpenScreenS2CPacket) {
                  OpenScreenS2CPacket openScreen = (OpenScreenS2CPacket)packetEvent.d();
                  String title = Formatting.strip(openScreen.getName().getString());
                  if (title != null
                     && (
                        title.contains("РњРµРЅСЋ")
                           || title.contains("ReallyWorld")
                           || title.contains("РРЅС„РѕСЂРјР°С†РёСЏ")
                           || title.contains("РќРѕРІРѕСЃС‚Рё")
                           || title.contains("РџРѕРјРѕС‰СЊ")
                     )) {
                     packetEvent.b();
                     if (c.getNetworkHandler() != null) {
                        c.getNetworkHandler().sendPacket(new CloseHandledScreenC2SPacket(openScreen.getSyncId()));
                     }

                     return;
                  }
               }

               if (packetEvent.d() instanceof OpenWrittenBookS2CPacket) {
                  packetEvent.b();
                  return;
               }
            }

            if (this.b.get()) {
               String text = null;
               if (packetEvent.d() instanceof GameMessageS2CPacket) {
                  text = ((GameMessageS2CPacket)packetEvent.d()).content().getString();
               } else if (packetEvent.d() instanceof ChatMessageS2CPacket) {
                  text = ((ChatMessageS2CPacket)packetEvent.d()).body().content();
               }

               if (text != null && this.containsBannedWord(text)) {
                  packetEvent.b();
                  return;
               }
            }
         }
      }
   }

   private boolean containsBannedWord(String text) {
      if (text != null && !text.isEmpty()) {
         for (Pattern p : this.compiledPatterns) {
            if (p.matcher(text).find()) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static String c(String str, String str2, int i, int i2, int i3, int i4) {
      return null;
   }
}
