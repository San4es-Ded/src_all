package wtf.wyvern.client.modules.impl.misc;

import java.util.Collection;
import java.util.Iterator;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.StringSetting;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "NameProtect",
   category = Category.MISC,
   description = "Защищает имена игроков"
)
@FastNative
public final class NameProtect extends Module {
   public static final NameProtect INSTANCE = new NameProtect();
   private final StringSetting customName = new StringSetting("Имя", "Wyvern");
   private final BooleanSetting hideInWatermark = new BooleanSetting("Скрыть в ватермарке", true);
   private final BooleanSetting hideFriends = new BooleanSetting("Скрыть друзей", false);

   private NameProtect() {
   }

   public static String getCustomName() {
      Module module = INSTANCE;
      if (module != null && module.isEnabled()) {
         String name = INSTANCE.customName.getValue().trim();
         return name.isEmpty() ? "Wyvern" : name;
      }
      return mc.player == null ? "Wyvern" : mc.player.getNameForScoreboard();
   }

   public static String getTitlePrefix() {
      return "";
   }

   public static String getProtectedDisplayName() {
      return getCustomName();
   }

   public static String getWatermarkName() {
      if (INSTANCE.isEnabled() && INSTANCE.hideInWatermark.isEnabled()) {
         return getCustomName();
      }
      return mc.player == null ? "" : mc.player.getName().getString();
   }

   public static String getCustomName(String originalName) {
      Module module = INSTANCE;
      if (module != null && module.isEnabled() && mc.player != null) {
         String me = mc.player.getNameForScoreboard();
         String replacement = getProtectedDisplayName();
         if (originalName.contains(me)) {
            return originalName.replace(me, replacement);
         } else {
            if (module instanceof NameProtect) {
               NameProtect nameProtect = (NameProtect)module;
               if (nameProtect.hideFriends.isEnabled()) {
                  Collection<String> friends = Wyvern.getInstance().getFriendManager().getItems();
                  Iterator var5 = friends.iterator();

                  while(var5.hasNext()) {
                     String friend = (String)var5.next();
                     if (originalName.contains(friend)) {
                        return originalName.replace(friend, getCustomName());
                     }
                  }
               }
            }

            return originalName;
         }
      } else {
         return originalName;
      }
   }
}
