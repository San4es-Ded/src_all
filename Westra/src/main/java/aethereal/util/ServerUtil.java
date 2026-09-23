package aethereal.util;

import aethereal.core.Interface;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1836;
import net.minecraft.class_1972;
import net.minecraft.class_266;
import net.minecraft.class_268;
import net.minecraft.class_269;
import net.minecraft.class_345;
import net.minecraft.class_642;
import net.minecraft.class_9013;
import net.minecraft.class_9280;
import net.minecraft.class_9334;
import net.minecraft.class_1792.class_9635;
import platform.inject.accessors.BossBarHudAccessor;
import platform.inject.accessors.PlayerListHudAccessor;

public class ServerUtil implements Interface {
   @Generated
   private ServerUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static String a() {
      return aM_.field_1724 != null && aM_.field_1724.field_3944 != null && ((PlayerListHudAccessor)aM_.field_1705.method_1750()).getHeader() != null
         ? ((PlayerListHudAccessor)aM_.field_1705.method_1750()).getHeader().getString()
         : "";
   }

   public static String b() {
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         class_642 info = aM_.field_1724.field_3944.method_45734();
         return info == null ? "" : info.field_3761;
      } else {
         return "";
      }
   }

   public static double c() {
      return Math.hypot(aM_.field_1724.method_23317() - aM_.field_1724.field_6014, aM_.field_1724.method_23321() - aM_.field_1724.field_5969) * 20.0;
   }

   public static int d() {
      return aM_.field_1724 != null && Objects.requireNonNull(aM_.method_1562()).method_2871(aM_.field_1724.method_5667()) != null
         ? Objects.requireNonNull(aM_.method_1562().method_2871(aM_.field_1724.method_5667())).method_2959()
         : 0;
   }

   public static boolean e() {
      if (aM_.field_1724 != null && !((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().isEmpty()) {
         for (class_345 bossBar : ((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().values()) {
            if (bossBar.method_5414().getString().toLowerCase(Locale.ROOT).contains("pvp")
               || bossBar.method_5414().getString().toLowerCase(Locale.ROOT).contains("пвп")
               || bossBar.method_5414().getString().toLowerCase(Locale.ROOT).contains("дуэль")) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static int f() {
      if (aM_.field_1724 != null && !((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().isEmpty()) {
         for (class_345 bossBar : ((BossBarHudAccessor)aM_.field_1705.method_1740()).getBossBars().values()) {
            String name = bossBar.method_5414().getString().toLowerCase(Locale.ROOT);
            if (name.contains("pvp") || name.contains("пвп")) {
               Matcher matcher = Pattern.compile("(\\d+):(\\d+)").matcher(name);
               if (matcher.find()) {
                  return Integer.parseInt(matcher.group(1)) * 60 + Integer.parseInt(matcher.group(2));
               }

               Matcher matcher2 = Pattern.compile("(\\d+)").matcher(name);
               if (matcher2.find()) {
                  return Integer.parseInt(matcher2.group(1));
               }
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static final class a {
      @Generated
      private a() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }

      public static boolean a() {
         return ServerUtil.a().toLowerCase().contains("funtime") || ServerUtil.b().toLowerCase().contains("funtime");
      }

      public static boolean b() {
         return a() && Interface.aM_.field_1687 != null && Interface.aM_.field_1687.method_27983().method_29177().toString().equals("minecraft:duels");
      }

      public static boolean c() {
         return a()
            && Interface.aM_.field_1724.field_3944.method_52790() != null
            && Interface.aM_.field_1687.method_23753(Interface.aM_.field_1724.method_24515()).method_40225(class_1972.field_9471)
            && Interface.aM_.field_1724.field_3944.method_52790().contains("BotFilter (https://vk.cc/8hr1pU)");
      }

      public static int d() {
         return ServerUtil.a() != null && ServerUtil.a().contains("Анархия-") ? Integer.parseInt(ServerUtil.a().split("Анархия-")[1].trim()) : -1;
      }

      public static int a(class_1799 itemStack) {
         if (!itemStack.method_7960()) {
            List<String> tooltipLines = itemStack.method_7950(class_9635.field_51353, Interface.aM_.field_1724, class_1836.field_41070)
               .stream()
               .skip(1L)
               .map(v0 -> v0.getString())
               .toList();
            if (!itemStack.method_7909().method_63680().getString().contains("Товар не актуален") && itemStack.method_7909() != class_1802.field_8298) {
               for (String line : tooltipLines) {
                  if (line.contains("$ Ценa: ")) {
                     String price = line.substring(line.indexOf("$ Ценa: ") + "$ Ценa: ".length())
                        .trim()
                        .replace(",", "")
                        .replace("$", "")
                        .replaceAll("\\s+", "");
                     if (!price.isEmpty()) {
                        try {
                           return Integer.parseInt(price) / (itemStack.method_7947() > 0 ? itemStack.method_7947() : 1);
                        } catch (NumberFormatException var6) {
                           return -1;
                        }
                     }
                  }
               }

               return -1;
            } else {
               return -1;
            }
         } else {
            return -1;
         }
      }

      public static float a(class_1309 entity) {
         if (Interface.aM_.field_1687 != null) {
            class_269 scoreboard = Interface.aM_.field_1687.method_8428();

            for (class_266 objective : scoreboard.method_1151()) {
               class_9013 score = scoreboard.method_55430(entity, objective);
               if (score != null) {
                  return score.method_55397();
               }
            }
         }

         return entity.method_6032() + entity.method_6067();
      }

      public static long e() {
         if (Interface.aM_.field_1687 != null) {
            for (class_268 team : Interface.aM_.field_1687.method_8428().method_1159()) {
               String message = team.method_1144().getString().toLowerCase(Locale.ROOT);
               if (message.contains("монет")) {
                  return Long.parseLong(message.substring(message.lastIndexOf("") + 1).replaceAll("[^0-9]", ""));
               }
            }

            return -1L;
         } else {
            return -1L;
         }
      }

      public static String b(class_1799 stack) {
         int id = Optional.ofNullable((class_9280)stack.method_57824(class_9334.field_49637))
            .filter(data -> !data.comp_3354().isEmpty())
            .map(data2 -> ((Float)data2.comp_3354().getFirst()).intValue())
            .orElse(0);
         switch (id) {
            case 1:
               return "Талисман Мрака";
            case 2:
               return "Талисман Вихря";
            case 3:
               return "Талисман Демона";
            case 4:
               return "Талисман Раздора";
            case 5:
               return "Талисман Ярости";
            case 6:
               return "Талисман Крушителя";
            case 7:
               return "Талисман Карателя";
            case 8:
               return "Талисман Тирана";
            default:
               return "Тотем бессмертия";
         }
      }
   }

   public static final class b {
      @Generated
      private b() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }

      public static boolean a() {
         return ServerUtil.a().toLowerCase().contains("holyworld") || ServerUtil.b().toLowerCase().contains("holyworld");
      }

      public static int b() {
         String last = ServerUtil.a().trim().replaceAll("(?s).*\\n", "");
         return last.contains("Лайт") && last.contains("#") ? Integer.parseInt(last.replaceAll(".*#(\\d+).*", "$1")) : -1;
      }
   }

   public static final class c {
      @Generated
      private c() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }

      public static boolean a() {
         return ServerUtil.a().toLowerCase().contains("reallyworld") || ServerUtil.b().toLowerCase().contains("reallyworld");
      }
   }

   public static final class d {
      @Generated
      private d() {
         throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
      }

      public static boolean a() {
         return ServerUtil.a().toLowerCase().contains("spookytime") || ServerUtil.b().toLowerCase().contains("spookytime");
      }

      public static int b() {
         return ServerUtil.a() != null && ServerUtil.a().contains("Анархия-") ? Integer.parseInt(ServerUtil.a().split("Анархия-")[1].trim()) : -1;
      }

      public static int a(class_1799 itemStack) {
         if (!itemStack.method_7960()) {
            List<String> tooltipLines = itemStack.method_7950(class_9635.field_51353, Interface.aM_.field_1724, class_1836.field_41070)
               .stream()
               .skip(1L)
               .map(v0 -> v0.getString())
               .toList();
            if (!itemStack.method_7909().method_63680().getString().contains("Товар не актуален") && itemStack.method_7909() != class_1802.field_8298) {
               for (String line : tooltipLines) {
                  if (line.contains("$ Цена: ")) {
                     String price = line.substring(line.indexOf("$ Цена: ") + "$ Цена: ".length())
                        .trim()
                        .replace(",", "")
                        .replace("$", "")
                        .replaceAll("\\s+", "");
                     if (!price.isEmpty()) {
                        try {
                           return Integer.parseInt(price) / (itemStack.method_7947() > 0 ? itemStack.method_7947() : 1);
                        } catch (NumberFormatException var6) {
                           return -1;
                        }
                     }
                  }
               }

               return -1;
            } else {
               return -1;
            }
         } else {
            return -1;
         }
      }
   }
}
