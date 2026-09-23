package rockstar.modules.other;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.module.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(
   name = "Name Protect",
   category = ModuleCategory.OTHER,
   internalMethod09633 = "modules.descriptions.name_protect"
)
public class NameProtectModule extends Module {
   private TextSetting internalField0384;
   private BooleanSetting internalField0650;
   private TextSetting internalField0385;
   private BooleanSetting internalField0651;
   private BooleanSetting internalField1261;
   private static final Pattern internalField0293 = Pattern.compile(
      "(\u0410\u043d\u0430\u0440\u0445\u0438[\u044f\u0438]\\s*(?:PvP\\s*)?(?:[#\u2116:\\-]|\\s)+)(\\d{1,4})", 66
   );
   private static final Pattern internalField0294 = Pattern.compile(
      "((?:\u041b\u0430\u0439\u0442|Lite|\u041a\u043b\u0430\u0441\u0441\u0438\u043a|Classic|Classik)\\s+\u0410\u043d\u0430\u0440\u0445\u0438[\u044f\u0438]\\s*[#\u2116:\\-]?\\s*|(?:\u041b\u0430\u0439\u0442|Lite|\u041a\u043b\u0430\u0441\u0441\u0438\u043a|Classic|Classik)\\s*[#\u2116]\\s*)(\\d{1,4})",
      66
   );
   private static final Pattern internalField1112 = Pattern.compile("(?:play\\.)?holyworld(?:\\.[a-z0-9_.-]+)*", 66);
   private static final Pattern internalField1111 = Pattern.compile("(?:play\\.)?hollyworld(?:\\.[a-z0-9_.-]+)*", 66);
   private static final Pattern internalField1113 = Pattern.compile("playhw(?:\\.[a-z0-9_.-]+)*", 66);
   private static final Pattern internalField1110 = Pattern.compile("Holy\\s*World|HolyWorld|HollyWorld", 66);
   private static final Pattern internalField1525 = Pattern.compile("/(?:anarchy|an|lite|classik|classic)\\s*[#\u2116\\-]?\\s*\\d{1,4}", 66);
   private static final Pattern internalField1524 = Pattern.compile("\\b(?:anarchy|an|lite|classik|classic)\\s*[#\u2116\\-]?\\s*\\d{1,4}\\b", 66);
   private static final Pattern internalField1523 = Pattern.compile("\\b(?:Lite|Classic|Classik)\\b", 66);
   private static final Pattern internalField1522 = Pattern.compile(
      "(?<![\u0410-\u042f\u0430-\u044f\u0401\u0451])(?:\u041b\u0430\u0439\u0442|\u041a\u043b\u0430\u0441\u0441\u0438\u043a)(?![\u0410-\u042f\u0430-\u044f\u0401\u0451])",
      66
   );
   private static final Pattern internalField1521 = Pattern.compile("\u0410\u043d\u0430\u0440\u0445\u0438[\u044f\u0438]", 66);
   private static final Pattern internalField1520 = Pattern.compile("\\bAnarchy\\b", 66);
   private static final Pattern internalField1519 = Pattern.compile(
      "(?<![A-Za-z0-9_-])(?:(?:[a-z0-9-]+\\.)+[a-z]{2,63}|(?:\\d{1,3}\\.){3}\\d{1,3})(?::\\d{1,5})?(?![A-Za-z0-9_-])", 66
   );
   private static final Pattern internalField1518 = Pattern.compile(
      "(?<![\\d.-])-?\\d{1,8}(?:\\.\\d+)?(?:\\s*(?:,|/|;|\\s)\\s*)-?\\d{1,8}(?:\\.\\d+)?(?:\\s*(?:,|/|;|\\s)\\s*)-?\\d{1,8}(?:\\.\\d+)?(?![\\d.])"
   );
   private static final Pattern internalField1793 = Pattern.compile("(?:(?:[xXyYzZ])\\s*[:=]\\s*-?\\d{1,8}(?:\\.\\d+)?\\s*[,;/]?\\s*){3}");
   private static final Pattern internalField1794 = Pattern.compile(
      "((?:\u0410\u043d\u0430\u0440\u0445\u0438[\u044f\u0438]|Anarchy|\u041b\u0430\u0439\u0442|Lite|\u041a\u043b\u0430\u0441\u0441\u0438\u043a|Classic|Classik|\u0413\u0440\u0438\u0444(?:\u0435\u0440\u0441\u043a\u0438\u0439)?)\\s*(?:PvP\\s*)?(?:[#\u2116:\\-]|\\s)+)(\\d{1,4})",
      66
   );
   private final Set<String> internalField0546 = new HashSet<>();
   private final Map<String, String> internalField0543 = new HashMap<>();
   private String internalField0248 = "";
   private int internalField0228 = -1;
   private Pattern internalField1795;
   private boolean internalField0277 = true;

   public NameProtectModule() {
      this.internalMethod09834();
   }

   private void internalMethod09834() {
      this.internalField0384 = new TextSetting(this, "modules.settings.name_protect.fake_name").internalMethod00011("Player");
      this.internalField0650 = new BooleanSetting(this, "modules.settings.name_protect.hide_friends").internalMethod06630();
      this.internalField0385 = new TextSetting(this, "modules.settings.name_protect.friend_fake_name", () -> !this.internalField0650.internalMethod04496())
         .internalMethod00011("Friend");
      this.internalField0651 = new BooleanSetting(this, "modules.settings.name_protect.streamer_mode");
      this.internalField1261 = new BooleanSetting(this, "modules.settings.name_protect.capture_bypass");
   }

   public boolean internalMethod01816(CharSequence localValue1) {
      if (localValue1 == null || localValue1.isEmpty()) {
         return false;
      } else if (this.internalField0651.internalMethod04496()) {
         return true;
      } else if (this.internalMethod02730(localValue1, internalField0149.getSession().getUsername())) {
         return true;
      } else if (GameUtils.internalMethod00471() && this.internalMethod02730(localValue1, internalField0149.player.getDisplayName().getString())) {
         return true;
      } else {
         if (this.internalField0650.internalMethod04496()) {
            for (String localValue3 : RockstarClient.getInstance().internalMethod03375().internalMethod06515()) {
               if (localValue3 != null && !localValue3.isEmpty() && this.internalMethod02730(localValue1, localValue3)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean internalMethod09833() {
      return this.isEnabled() && this.internalField0651.internalMethod04496();
   }

   public boolean internalMethod02621(CharSequence localValue1) {
      if (!this.internalField0546.isEmpty() && this.internalMethod01816(localValue1)) {
         for (String localValue3 : this.internalField0546) {
            if (this.internalMethod02730(localValue1, localValue3)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public void internalMethod09832() {
      this.internalField0546.clear();
   }

   private boolean internalMethod02730(CharSequence localValue1, String localValue2) {
      if (localValue2 != null && !localValue2.isEmpty() && localValue2.length() <= localValue1.length()) {
         if (localValue1 instanceof String localValue5) {
            return localValue5.contains(localValue2);
         } else {
            label32:
            for (int localValue3 = 0; localValue3 + localValue2.length() <= localValue1.length(); localValue3++) {
               for (int localValue4 = 0; localValue4 < localValue2.length(); localValue4++) {
                  if (localValue1.charAt(localValue3 + localValue4) != localValue2.charAt(localValue4)) {
                     continue label32;
                  }
               }

               return true;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public String internalMethod04954(String localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         String localValue2 = internalField0149.getSession().getUsername();
         String localValue3 = this.internalMethod02855(this.internalField0384.internalMethod08926(), "Player");
         if (GameUtils.internalMethod00471()) {
            localValue1 = localValue1.replace(internalField0149.player.getDisplayName().getString(), localValue3);
         }

         localValue1 = localValue1.replace(localValue2, localValue3);
         if (this.internalField0650.internalMethod04496()) {
            FriendListManager localValue4 = RockstarClient.getInstance().internalMethod03375();
            String localValue5 = this.internalMethod02855(this.internalField0385.internalMethod08926(), "Friend");

            for (String localValue7 : localValue4.internalMethod06515()) {
               if (localValue7 != null && !localValue7.isEmpty()) {
                  localValue1 = localValue1.replace(localValue7, localValue5);
               }
            }
         }

         if (this.internalField0651.internalMethod04496()) {
            localValue1 = this.internalMethod08462(localValue1);
         }

         return localValue1;
      } else {
         return localValue1;
      }
   }

   public String internalMethod08287(String localValue1) {
      if (localValue1 == null || localValue1.isEmpty()) {
         return localValue1;
      } else if (PostProcessRenderer.internalMethod07188()) {
         this.internalField0546.add(localValue1);
         return localValue1;
      } else {
         String localValue2 = this.internalMethod10046(localValue1);
         if (!Objects.equals(localValue2, localValue1)) {
            return localValue2;
         } else if (this.internalField0651.internalMethod04496()) {
            this.internalMethod09183();
            return this.internalMethod09442(localValue1);
         } else {
            return localValue1;
         }
      }
   }

   public String internalMethod05421(String localValue1, String localValue2) {
      if (localValue1 == null || localValue1.isEmpty()) {
         return localValue1;
      } else if (PostProcessRenderer.internalMethod07188()) {
         return localValue1;
      } else if (localValue2 != null && !localValue2.isEmpty()) {
         String localValue3 = this.internalMethod08287(localValue2);
         return !Objects.equals(localValue3, localValue2) ? this.internalMethod03101(localValue1, localValue2, localValue3) : this.internalMethod04954(localValue1);
      } else {
         return this.internalMethod04954(localValue1);
      }
   }

   private String internalMethod08462(String localValue1) {
      this.internalMethod09183();
      localValue1 = this.internalMethod09531(localValue1);
      localValue1 = this.internalMethod08104(localValue1);
      localValue1 = this.internalMethod01534(localValue1, internalField1793, "");
      localValue1 = this.internalMethod01534(localValue1, internalField1518, "\u2014");
      localValue1 = this.internalMethod08308(localValue1);
      return this.internalMethod09908(localValue1);
   }

   private String internalMethod08104(String localValue1) {
      String localValue2 = ServerUtils.internalMethod00929();
      if (localValue2 != null && !localValue2.isBlank() && !localValue2.equalsIgnoreCase("single")) {
         localValue1 = localValue1.replace(localValue2, "server.local");
      }

      String localValue3 = ServerUtils.internalMethod06458(false);
      if (localValue3 != null && localValue3.length() > 2 && !localValue3.equalsIgnoreCase("single")) {
         localValue1 = this.internalMethod03101(localValue1, localValue3, "Server");
      }

      return this.internalMethod01534(localValue1, internalField1519, "server.local");
   }

   private String internalMethod08308(String localValue1) {
      Matcher localValue2 = internalField1794.matcher(localValue1);
      StringBuffer localValue3 = new StringBuffer();

      while (localValue2.find()) {
         localValue2.appendReplacement(localValue3, Matcher.quoteReplacement(localValue2.group(1) + "\u2014"));
      }

      localValue2.appendTail(localValue3);
      return this.internalMethod09431(localValue3.toString());
   }

   private String internalMethod09431(String localValue1) {
      int localValue2 = ServerUtils.internalMethod07319();
      if (localValue2 <= 0) {
         return localValue1;
      } else {
         localValue1 = this.internalMethod02669(localValue1, "#", localValue2, "\u2014");
         return this.internalMethod02669(localValue1, "\u2116", localValue2, "\u2014");
      }
   }

   private void internalMethod09183() {
      if (internalField0149.player != null && internalField0149.player.networkHandler != null) {
         String localValue1 = ServerUtils.internalMethod00929();
         if (!Objects.equals(this.internalField0248, localValue1)) {
            this.internalField0543.clear();
            this.internalField0248 = localValue1;
            this.internalField0228 = -1;
            this.internalField0277 = true;
         }

         if (this.internalField0228 != internalField0149.player.age) {
            this.internalField0228 = internalField0149.player.age;

            for (PlayerListEntry localValue3 : internalField0149.player.networkHandler.getPlayerList()) {
               String localValue4 = localValue3.getProfile().name();
               String localValue5 = localValue3.getProfile().id() != null ? localValue3.getProfile().id().toString() : localValue4.toLowerCase(Locale.ROOT);
               this.internalMethod00483(localValue4, localValue5);
            }

            if (internalField0149.world != null) {
               for (PlayerEntity localValue7 : internalField0149.world.getPlayers()) {
                  this.internalMethod00483(localValue7.getName().getString(), localValue7.getUuidAsString());
               }
            }
         }
      } else {
         this.internalField0543.clear();
         this.internalField0248 = "";
         this.internalField0228 = -1;
         this.internalField0277 = true;
      }
   }

   private void internalMethod00483(String localValue1, String localValue2) {
      if (this.internalMethod05048(localValue1) && !localValue1.equalsIgnoreCase(internalField0149.getSession().getUsername())) {
         this.internalField0543.computeIfAbsent(localValue1, localValue2x -> {
            this.internalField0277 = true;
            return this.internalMethod09315(localValue2);
         });
      }
   }

   private String internalMethod09531(String localValue1) {
      Pattern localValue2 = this.internalMethod03944();
      if (localValue2 == null) {
         return localValue1;
      } else {
         Matcher localValue3 = localValue2.matcher(localValue1);
         if (!localValue3.find()) {
            return localValue1;
         } else {
            StringBuffer localValue4 = new StringBuffer();

            do {
               String localValue5 = localValue3.group();
               String localValue6 = this.internalField0543.get(localValue5);
               localValue3.appendReplacement(localValue4, Matcher.quoteReplacement(localValue6 != null ? localValue6 : localValue5));
            } while (localValue3.find());

            localValue3.appendTail(localValue4);
            return localValue4.toString();
         }
      }
   }

   private Pattern internalMethod03944() {
      if (this.internalField0277) {
         this.internalField1795 = this.internalMethod08360();
         this.internalField0277 = false;
      }

      return this.internalField1795;
   }

   private Pattern internalMethod08360() {
      if (this.internalField0543.isEmpty()) {
         return null;
      } else {
         ArrayList localValue1 = new ArrayList<>(this.internalField0543.keySet());
         localValue1.sort(Comparator.comparingInt(String::length).reversed());
         StringBuilder localValue2 = new StringBuilder("(?<![A-Za-z0-9_])(?:");

         for (int localValue3 = 0; localValue3 < localValue1.size(); localValue3++) {
            if (localValue3 > 0) {
               localValue2.append('|');
            }

            localValue2.append(Pattern.quote((String)localValue1.get(localValue3)));
         }

         localValue2.append(")(?![A-Za-z0-9_])");
         return Pattern.compile(localValue2.toString());
      }
   }

   private String internalMethod09315(String localValue1) {
      int localValue2 = Math.floorMod(localValue1.hashCode(), 900) + 100;
      HashSet localValue3 = new HashSet<>(this.internalField0543.values());

      for (int localValue4 = 0; localValue4 < 900; localValue4++) {
         String localValue5 = "P" + localValue2;
         if (!localValue3.contains(localValue5)) {
            return localValue5;
         }

         if (++localValue2 > 999) {
            localValue2 = 100;
         }
      }

      return "P" + (this.internalField0543.size() + 1000);
   }

   private String internalMethod09442(String localValue1) {
      return this.internalMethod05048(localValue1) && !localValue1.equalsIgnoreCase(internalField0149.getSession().getUsername())
         ? this.internalField0543.computeIfAbsent(localValue1, localValue1x -> {
            this.internalField0277 = true;
            return this.internalMethod09315(localValue1x.toLowerCase(Locale.ROOT));
         })
         : localValue1;
   }

   private String internalMethod09908(String localValue1) {
      localValue1 = this.internalMethod09172(localValue1);
      localValue1 = this.internalMethod09790(localValue1);
      localValue1 = this.internalMethod09920(localValue1);
      return this.internalMethod10121(localValue1);
   }

   private String internalMethod09172(String localValue1) {
      localValue1 = this.internalMethod03599(localValue1, internalField0293);
      localValue1 = this.internalMethod03599(localValue1, internalField0294);
      int localValue2 = ServerUtils.internalMethod07319();
      if (localValue2 > 0) {
         int localValue3 = this.internalMethod07088(localValue2);
         localValue1 = this.internalMethod02669(localValue1, "#", localValue2, Integer.toString(localValue3));
         localValue1 = this.internalMethod02669(localValue1, "\u2116", localValue2, Integer.toString(localValue3));
      }

      return localValue1;
   }

   private String internalMethod09790(String localValue1) {
      localValue1 = this.internalMethod01534(localValue1, internalField1112, "server.local");
      localValue1 = this.internalMethod01534(localValue1, internalField1111, "server.local");
      localValue1 = this.internalMethod01534(localValue1, internalField1113, "server.local");
      return this.internalMethod01534(localValue1, internalField1110, "Server");
   }

   private String internalMethod09920(String localValue1) {
      localValue1 = this.internalMethod01534(localValue1, internalField1525, "/server");
      return this.internalMethod01534(localValue1, internalField1524, "server");
   }

   private String internalMethod10121(String localValue1) {
      localValue1 = this.internalMethod01534(localValue1, internalField1523, "PvP");
      localValue1 = this.internalMethod01534(localValue1, internalField1522, "PvP");
      localValue1 = this.internalMethod01534(localValue1, internalField1521, "\u0420\u0435\u0436\u0438\u043c");
      return this.internalMethod01534(localValue1, internalField1520, "Mode");
   }

   private String internalMethod03599(String localValue1, Pattern localValue2) {
      Matcher localValue3 = localValue2.matcher(localValue1);
      StringBuffer localValue4 = new StringBuffer();

      while (localValue3.find()) {
         int localValue5 = this.internalMethod05047(localValue3.group(2));
         String localValue6 = localValue5 > 0 ? localValue3.group(1) + this.internalMethod07088(localValue5) : localValue3.group(0);
         localValue3.appendReplacement(localValue4, Matcher.quoteReplacement(localValue6));
      }

      localValue3.appendTail(localValue4);
      return localValue4.toString();
   }

   private String internalMethod02669(String localValue1, String localValue2, int localValue3, String localValue4) {
      return !localValue1.contains(localValue2 + localValue3)
         ? localValue1
         : Pattern.compile(Pattern.quote(localValue2) + localValue3 + "(?!\\d)").matcher(localValue1).replaceAll(Matcher.quoteReplacement(localValue2 + localValue4));
   }

   private int internalMethod07088(int localValue1) {
      return localValue1 + 5;
   }

   private String internalMethod10046(String localValue1) {
      String localValue2 = internalField0149.getSession().getUsername();
      if (localValue1.equals(localValue2)) {
         return this.internalMethod02855(this.internalField0384.internalMethod08926(), "Player");
      } else if (GameUtils.internalMethod00471() && localValue1.equals(internalField0149.player.getDisplayName().getString())) {
         return this.internalMethod02855(this.internalField0384.internalMethod08926(), "Player");
      } else {
         if (this.internalField0650.internalMethod04496()) {
            FriendListManager localValue3 = RockstarClient.getInstance().internalMethod03375();

            for (String localValue5 : localValue3.internalMethod06515()) {
               if (localValue5 != null && !localValue5.isEmpty() && localValue5.equals(localValue1)) {
                  return this.internalMethod02855(this.internalField0385.internalMethod08926(), "Friend");
               }
            }
         }

         return localValue1;
      }
   }

   private String internalMethod01534(String localValue1, Pattern localValue2, String localValue3) {
      return localValue2.matcher(localValue1).replaceAll(Matcher.quoteReplacement(localValue3));
   }

   private String internalMethod03101(String localValue1, String localValue2, String localValue3) {
      return localValue2 != null && !localValue2.isEmpty() && localValue3 != null && !localValue3.isEmpty() && localValue1.contains(localValue2)
         ? Pattern.compile("(?<![A-Za-z0-9_])" + Pattern.quote(localValue2) + "(?![A-Za-z0-9_])").matcher(localValue1).replaceAll(Matcher.quoteReplacement(localValue3))
         : localValue1;
   }

   private int internalMethod05047(String localValue1) {
      try {
         return Integer.parseInt(localValue1);
      } catch (NumberFormatException localValue3) {
         return -1;
      }
   }

   private boolean internalMethod05048(String localValue1) {
      if (localValue1 != null && localValue1.length() >= 3 && localValue1.length() <= 16) {
         for (int localValue2 = 0; localValue2 < localValue1.length(); localValue2++) {
            char localValue3 = localValue1.charAt(localValue2);
            if ((localValue3 < 'A' || localValue3 > 'Z') && (localValue3 < 'a' || localValue3 > 'z') && (localValue3 < '0' || localValue3 > '9') && localValue3 != '_') {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private String internalMethod02855(String localValue1, String localValue2) {
      return localValue1 != null && !localValue1.isEmpty() ? localValue1 : localValue2;
   }

   @Generated
   public TextSetting internalMethod01362() {
      return this.internalField0384;
   }

   @Generated
   public BooleanSetting internalMethod03520() {
      return this.internalField0650;
   }

   @Generated
   public TextSetting internalMethod02003() {
      return this.internalField0385;
   }

   @Generated
   public BooleanSetting internalMethod04251() {
      return this.internalField0651;
   }

   @Generated
   public BooleanSetting internalMethod07675() {
      return this.internalField1261;
   }

   @Generated
   public Set<String> internalMethod05618() {
      return this.internalField0546;
   }

   @Generated
   public Map<String, String> internalMethod02097() {
      return this.internalField0543;
   }

   @Generated
   public String internalMethod08799() {
      return this.internalField0248;
   }

   @Generated
   public int internalMethod09056() {
      return this.internalField0228;
   }

   @Generated
   public Pattern internalMethod03040() {
      return this.internalField1795;
   }

   @Generated
   public boolean internalMethod09835() {
      return this.internalField0277;
   }
}
