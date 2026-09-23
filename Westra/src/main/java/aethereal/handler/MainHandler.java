package aethereal.handler;

import aethereal.core.Client;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ConsumeEvent;
import aethereal.event.CooldownEvent;
import aethereal.event.PacketEvent;
import aethereal.mixin.IItemCooldownManager;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1802;
import net.minecraft.class_1844;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_2797;
import net.minecraft.class_500;
import net.minecraft.class_634;
import net.minecraft.class_642;
import net.minecraft.class_7439;
import net.minecraft.class_7472;
import net.minecraft.class_9334;
import net.minecraft.class_9449;
import net.minecraft.class_2568.class_5247;

@Handler_2
public class MainHandler extends BaseHandler implements Interface {
   private class_642 b;
   private String c;

   @Generated
   public void a(class_642 serverInfo) {
      this.b = serverInfo;
   }

   @Generated
   public class_642 a() {
      return this.b;
   }

   @Generated
   public void a(String confirm) {
      this.c = confirm;
   }

   @Generated
   public String b() {
      return this.c;
   }

   @EventTarget
   public void a(PacketEvent event) throws MatchException {
      if (event.c() && event.d() instanceof class_7439 class_7439VarD) {
         for (class_2561 component : this.a(class_7439VarD.comp_763())) {
            class_2568 hover;
            class_2561 hoverText;
            if (component.getString().contains("Подробнее")
               && (hover = component.method_10866().method_10969()) != null
               && hover.method_10892() == class_5247.field_24342
               && (hoverText = (class_2561)hover.method_10891(class_5247.field_24342)) != null) {
               List<String> lines = Arrays.stream(hoverText.getString().split("\n")).map(v0 -> v0.trim()).toList();
               String userLine = lines.stream().filter(line -> line.startsWith("[") && line.contains("]")).findFirst().orElse(null);
               String reason = lines.stream()
                  .filter(line2 -> line2.startsWith("Причина:"))
                  .map(line3 -> line3.substring("Причина:".length()).trim())
                  .findFirst()
                  .orElse(null);
               String expiration = lines.stream()
                  .filter(line4 -> line4.startsWith("Окончание:"))
                  .map(line5 -> line5.substring("Окончание:".length()).trim())
                  .findFirst()
                  .orElse(null);
               if (userLine != null && reason != null && expiration != null) {
                  aM_.field_1724
                     .method_7353(
                        class_2561.method_43470(
                              "§c[♨] §6"
                                 + userLine.substring(userLine.indexOf(93) + 1).trim()
                                 + "§e забанен с причиной: §c\""
                                 + reason
                                 + "\"§e на §c\""
                                 + expiration
                                 + "\" "
                           )
                           .method_10852(ChatUtil.a("&c[Подробнее]", hoverText)),
                        false
                     );
                  event.a(true);
                  break;
               }
            }
         }
      }

      if (event.b()) {
         String strComp_2532;
         if (event.d() instanceof class_2797 class_2797VarD) {
            strComp_2532 = class_2797VarD.comp_945();
         } else if (event.d() instanceof class_7472 class_7472VarD) {
            try {
               String command = class_7472VarD.comp_808();
               strComp_2532 = command;
            } catch (Throwable var14) {
               throw new MatchException(var14.toString(), var14);
            }
         } else if (event.d() instanceof class_9449 class_9449VarD) {
            strComp_2532 = class_9449VarD.comp_2532();
         } else {
            strComp_2532 = null;
         }

         if (strComp_2532 != null && aM_.field_1724 != null) {
            int sell = strComp_2532.trim().toLowerCase(Locale.ROOT).startsWith("/ah sell")
               ? 8
               : (strComp_2532.trim().toLowerCase(Locale.ROOT).startsWith("ah sell") ? 7 : -1);
            if (sell >= 0 && strComp_2532.trim().endsWith("!")) {
               String body = strComp_2532.trim().substring(sell, strComp_2532.trim().length() - 1).trim();
               int numEnd = 0;

               while (numEnd < body.length() && (Character.isDigit(body.charAt(numEnd)) || body.charAt(numEnd) == '.')) {
                  numEnd++;
               }

               if (numEnd > 0) {
                  class_634 class_634Var = aM_.field_1724.field_3944;
                  long jRound = Math.round(Double.parseDouble(body.substring(0, numEnd)) * Math.max(1, aM_.field_1724.method_6047().method_7947()));
                  class_634Var.method_45729("/ah sell " + jRound);
                  event.a(true);
                  return;
               }
            }
         }

         if (strComp_2532 != null && aM_.field_1724 != null) {
            int pay = strComp_2532.trim().toLowerCase(Locale.ROOT).startsWith("/pay ")
               ? 5
               : (strComp_2532.trim().toLowerCase(Locale.ROOT).startsWith("pay ") ? 4 : -1);
            if (pay >= 0) {
               String[] parts = strComp_2532.trim().substring(pay).trim().split("\\s+");
               long balance = ServerUtil.a.e();
               if (parts.length == 2 && parts[1].equalsIgnoreCase("all") && balance > 0L) {
                  aM_.field_1724.field_3944.method_45729("/pay " + parts[0] + " " + balance);
                  event.a(true);
                  return;
               }
            }
         }

         if (strComp_2532 != null) {
            String trimmed = strComp_2532.trim();
            boolean dangerous = trimmed.toLowerCase().startsWith("hub") || trimmed.toLowerCase().startsWith("an");
            if (!ServerUtil.e() || !dangerous) {
               this.c = null;
               return;
            }

            if (trimmed.equals(this.c)) {
               this.c = null;
               return;
            }

            this.c = trimmed;
            ChatUtil.a("&cВы находитесь в PvP режиме! &7Чтобы отправить эту команду, повторите её");
            Westra.h().d().u().f().d();
            event.a(true);
         }
      }
   }

   @EventTarget
   public void a(CooldownEvent event) {
      if (ServerUtil.a.b() && (event.b() == class_1802.field_49098 && event.c() <= 10 || event.b() == class_1802.field_8233 && event.c() <= 20)) {
         event.a(true);
      }
   }

   private List<class_2561> a(class_2561 text) {
      List<class_2561> components = new ArrayList<>();
      components.add(text);

      for (class_2561 sibling : text.method_10855()) {
         components.addAll(this.a(sibling));
      }

      return components;
   }

   @EventTarget
   public void a(ConsumeEvent event) {
      class_1844 contents;
      if (ServerUtil.e()
         && (ServerUtil.a.a() || ServerUtil.d.a())
         && event.b().method_7909() == class_1802.field_8574
         && (contents = (class_1844)event.b().method_57824(class_9334.field_49651)) != null) {
         boolean heal = StreamSupport.<class_1293>stream(contents.method_57397().spliterator(), false)
            .anyMatch(effect -> effect.method_5579() == class_1294.field_5915);
         if (heal) {
            ((IItemCooldownManager)aM_.field_1724.method_7357()).setHealCooldown(300);
         }
      }
   }

   @EventTarget
   public void a(GlobalEvent globalEvent) {
      if (aM_.field_1724 != null || aM_.field_1755 instanceof class_500) {
         Client clientF = Westra.h().f();
         Object[] objArr = new Object[]{
            "uuid", aM_.field_1724 != null ? aM_.field_1724.method_5667() : null, "minecraft", aM_.method_1548().method_1676(), "server", null
         };
         String str;
         if (ServerUtil.a.a()) {
            str = "funtime";
         } else if (ServerUtil.b.a()) {
            str = "holyworld";
         } else {
            str = ServerUtil.d.a() ? "spookytime" : null;
         }

         objArr[5] = str;
         clientF.a(true, "minecraft", objArr);
      }
   }
}
