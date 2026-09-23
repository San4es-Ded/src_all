package pulse.modules.utilities;

import java.util.ArrayList;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Formatting;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pulse.events.ChatSendEvent;
import pulse.events.PacketEvent;
import pulse.events.PacketEvent.MessageDirection;
import pulse.module.ClientModule;
import pulse.module.ModuleCategory;
import pulse.module.ModuleInfo;
import pulse.settings.BooleanSetting;

@ModuleInfo(a = "Chat Helper", b = "РџРѕРјРѕС‰РЅРёРє РґР»СЏ С‡Р°С‚Р° (Р°РЅС‚Рё-СЃРїР°Рј Рё С„РёРєСЃ СЂР°СЃРєР»Р°РґРєРё)", c = ModuleCategory.UTILITIES)
public class ChatHelper extends ClientModule {
   private final BooleanSetting antiSpam = new BooleanSetting("РђРЅС‚Рё СЃРїР°Рј", true);
   private final BooleanSetting saveHistory = new BooleanSetting("РЎРѕС…СЂР°РЅСЏС‚СЊ РёСЃС‚РѕСЂРёСЋ", true);
   private final BooleanSetting fixLayout = new BooleanSetting("Р¤РёРєСЃ СЂР°СЃРєР»Р°РґРєРё РєРѕРјР°РЅРґ", true);
   private final List<String> chatHistory = new ArrayList<>();
   private static final String RU = "Р№С†СѓРєРµРЅРіС€С‰Р·С…СЉС„С‹РІР°РїСЂРѕР»РґР¶СЌСЏС‡СЃРјРёС‚СЊР±СЋ.Р™Р¦РЈРљР•РќР“РЁР©Р—РҐРЄР¤Р«Р’РђРџР РћР›Р”Р–Р­РЇР§РЎРњРРўР¬Р‘Р®,С‘РЃ";
   private static final String EN = "qwertyuiop[]asdfghjkl;'zxcvbnm/.QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<?`~";

   @EventHandler
   public void onPacket(PacketEvent event) {
      if (this.k()) {
         if (event.e() == PacketEvent.MessageDirection.RECIEVE && this.antiSpam.get()) {
            String text = null;
            if (event.d() instanceof GameMessageS2CPacket) {
               text = ((GameMessageS2CPacket)event.d()).content().getString();
            } else if (event.d() instanceof ChatMessageS2CPacket) {
               text = ((ChatMessageS2CPacket)event.d()).body().content();
            }

            if (text != null && this.isSpam(text)) {
               event.b();
            }
         }
      }
   }

   @EventHandler
   public void onChatSend(ChatSendEvent event) {
      if (this.k() && this.fixLayout.get() && c.player != null) {
         String msg = event.getMessage();
         if (msg != null && !msg.isEmpty() && (msg.charAt(0) == '.' || msg.charAt(0) == '/')) {
            int space = msg.indexOf(32);
            String commandToken = space >= 0 ? msg.substring(0, space) : msg;
            if (this.hasRussian(commandToken)) {
               String fixedCommand = this.convertLayout(commandToken);
               String fixed = fixedCommand + (space >= 0 ? msg.substring(space) : "");
               if (!fixed.equals(msg)) {
                  event.cancel();
                  if (fixed.startsWith("/")) {
                     c.player.networkHandler.sendChatCommand(fixed.substring(1));
                  } else {
                     c.player.networkHandler.sendChatMessage(fixed);
                  }
               }
            }
         }
      }
   }

   private boolean hasRussian(String str) {
      for (char ch : str.toCharArray()) {
         if (ch >= 1072 && ch <= 1103 || ch >= 1040 && ch <= 1071 || ch == 1105 || ch == 1025) {
            return true;
         }
      }

      return false;
   }

   private String convertLayout(String str) {
      String ru = "Р№С†СѓРєРµРЅРіС€С‰Р·С…СЉС„С‹РІР°РїСЂРѕР»РґР¶СЌСЏС‡СЃРјРёС‚СЊР±СЋ.Р™Р¦РЈРљР•РќР“РЁР©Р—РҐРЄР¤Р«Р’РђРџР РћР›Р”Р–Р­РЇР§РЎРњРРўР¬Р‘Р®,С‘РЃ";
      String en = "qwertyuiop[]asdfghjkl;'zxcvbnm/.QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<?`~";
      StringBuilder sb = new StringBuilder(str.length());

      for (int i = 0; i < str.length(); i++) {
         char ch = str.charAt(i);
         if (i == 0 && ch == '.') {
            sb.append('/');
         } else {
            int idx = "Р№С†СѓРєРµРЅРіС€С‰Р·С…СЉС„С‹РІР°РїСЂРѕР»РґР¶СЌСЏС‡СЃРјРёС‚СЊР±СЋ.Р™Р¦РЈРљР•РќР“РЁР©Р—РҐРЄР¤Р«Р’РђРџР РћР›Р”Р–Р­РЇР§РЎРњРРўР¬Р‘Р®,С‘РЃ".indexOf(ch);
            sb.append(idx >= 0 ? "qwertyuiop[]asdfghjkl;'zxcvbnm/.QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<?`~".charAt(idx) : ch);
         }
      }

      return sb.toString();
   }

   private boolean isSpam(String text) {
      if (text != null && !text.trim().isEmpty()) {
         String clean = Formatting.strip(text).trim().toLowerCase();
         if (this.chatHistory.contains(clean)) {
            return true;
         }

         String[] words = clean.split("\\s+");

         for (int i = 0; i < words.length - 1; i++) {
            if (words[i].length() > 2 && words[i].equals(words[i + 1])) {
               return true;
            }
         }

         this.chatHistory.add(clean);
         if (this.chatHistory.size() > 100) {
            this.chatHistory.remove(0);
         }

         return false;
      } else {
         return false;
      }
   }
}
