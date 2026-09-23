package aethereal.util;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.core.Interface;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Generated;
import net.minecraft.class_10192;
import net.minecraft.class_1304;
import net.minecraft.class_1320;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1836;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2378;
import net.minecraft.class_5134;
import net.minecraft.class_5321;
import net.minecraft.class_6880;
import net.minecraft.class_7924;
import net.minecraft.class_9276;
import net.minecraft.class_9285;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_1792.class_9635;
import net.minecraft.class_6880.class_6883;
import net.minecraft.class_9285.class_9287;

public class InventoryUtil implements Interface {
   @Generated
   private InventoryUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static class_1799 a(class_1799 stack) {
      class_10192 equippable = (class_10192)stack.method_57824(class_9334.field_54196);
      if (equippable != null && equippable.comp_3176().isPresent() && stack.method_7936() > 0) {
         String percent = ((class_5321)equippable.comp_3176().get()).method_29177().method_12832();
         String percent2 = percent.substring(percent.lastIndexOf(95) + 1);
         if (percent2.matches("\\d+")) {
            class_1799 copy = stack.method_7972();
            copy.method_7974(copy.method_7936() * (100 - Integer.parseInt(percent2)) / 100);
            return copy;
         }
      }

      return stack;
   }

   public static int a(class_1792 item) {
      int count = 0;

      for (int slot = 0; slot < 36; slot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
         if (stack.method_7909() == item) {
            count += stack.method_7947();
         }
      }

      return count;
   }

   public static boolean a(class_1799 stack, class_5321<class_1887> enchantment, int minLevel) {
      return ((class_9304)stack.method_57825(class_9334.field_49633, class_9304.field_49385))
            .method_57536(aM_.field_1687.method_30349().method_30530(class_7924.field_41265).method_46747(enchantment))
         >= minLevel;
   }

   public static boolean a(class_1799 stack, String needle) {
      return stack.method_7950(class_9635.field_51353, aM_.field_1724, class_1836.field_41070).stream().anyMatch(line -> line.getString().contains(needle));
   }

   public static int a(class_1792 item, boolean hotbar) {
      int end = hotbar ? 9 : 36;

      for (int i = 0; i < end; i++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() == item) {
            return i;
         }
      }

      return -1;
   }

   public static int b(class_1792 item) {
      return a(item, false);
   }

   public static int a(class_1792 item, boolean hotbar, boolean simple) {
      int end = hotbar ? 9 : 36;

      for (int i = 0; i < end; i++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(i);
         if (!stack.method_7960() && stack.method_7909() == item && (!simple || !stack.method_7958())) {
            return i;
         }
      }

      return -1;
   }

   public static int a(class_1799 stack, boolean hotbar) {
      if (stack != null && !stack.method_7960()) {
         int slotLimit = hotbar ? 9 : 36;

         for (int slotIndex = 0; slotIndex < slotLimit; slotIndex++) {
            class_1799 candidate = aM_.field_1724.method_31548().method_5438(slotIndex);
            if (!candidate.method_7960()
               && candidate.method_7909() == stack.method_7909()
               && Objects.equals(candidate.method_57824(class_9334.field_49636), stack.method_57824(class_9334.field_49636))) {
               return slotIndex;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static int b(class_1799 targetStack, boolean hotbar) {
      AutoBuyEntry info = Arrays.stream(AutoBuyEntry.values()).filter(i -> i.a(targetStack)).findFirst().orElse(null);
      if (info == null) {
         return -1;
      } else {
         int direct = IntStream.range(0, hotbar ? 9 : 36).filter(slot -> info.a(aM_.field_1724.method_31548().method_5438(slot))).findFirst().orElse(-1);
         return direct == -1 && !hotbar ? IntStream.range(0, 36).filter(slot2 -> {
            class_9276 contents = (class_9276)aM_.field_1724.method_31548().method_5438(slot2).method_57824(class_9334.field_49650);
            if (contents != null) {
               Stream<class_1799> streamMethod_59707 = contents.method_59707();
               if (streamMethod_59707.anyMatch(info::a)) {
                  return true;
               }
            }

            return false;
         }).findFirst().orElse(-1) : direct;
      }
   }

   public static int a(class_1799 bundleStack, class_1799 targetStack) {
      AutoBuyEntry info = Arrays.stream(AutoBuyEntry.values()).filter(i -> i.a(targetStack)).findFirst().orElse(null);
      class_9276 contents = (class_9276)bundleStack.method_57824(class_9334.field_49650);
      return info != null && contents != null
         ? IntStream.range(0, contents.method_57426()).filter(index -> info.a(contents.method_57422(index))).findFirst().orElse(-1)
         : -1;
   }

   public static int c(class_1799 targetStack, boolean hotbar) {
      AutoBuyEntry info = Arrays.stream(AutoBuyEntry.values()).filter(i -> i.a(targetStack)).findFirst().orElse(null);
      return info == null ? 0 : IntStream.range(0, hotbar ? 9 : 36).mapToObj(slot -> aM_.field_1724.method_31548().method_5438(slot)).mapToInt(stack -> {
         class_9276 contents = (class_9276)stack.method_57824(class_9334.field_49650);
         if (contents != null) {
            Stream<class_1799> streamMethod_59707 = contents.method_59707();
            return streamMethod_59707.filter(info::a).mapToInt(class_1799::method_7947).sum();
         } else {
            return info.a(stack) ? stack.method_7947() : 0;
         }
      }).sum();
   }

   public static int c(class_1792 item) {
      int fallbackSlot = -1;

      for (int i = 0; i < 45; i++) {
         if (i != 40 && aM_.field_1724.method_31548().method_5438(i).method_7909() == item) {
            class_9285 modifiers;
            if (aM_.field_1724.method_31548().method_5438(i).method_57826(class_9334.field_49636)
               && (modifiers = (class_9285)aM_.field_1724.method_31548().method_5438(i).method_57824(class_9334.field_49636)) != null
               && !modifiers.comp_2393().isEmpty()) {
               return i;
            }

            if (fallbackSlot == -1) {
               fallbackSlot = i;
            }
         }
      }

      return fallbackSlot;
   }

   public static int a() {
      class_2378<class_1887> registry = aM_.field_1687.method_30349().method_30530(class_7924.field_41265);
      class_6883<class_1887> protection = registry.method_46747(class_1893.field_9111);
      int bestSlot = -1;
      double bestScore = 0.0;

      for (int slot = 0; slot < 36; slot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(slot);
         class_10192 equippable = (class_10192)stack.method_57824(class_9334.field_54196);
         if (equippable != null && equippable.comp_3174() == class_1304.field_6174 && !stack.method_7960() && !stack.method_31574(class_1802.field_8833)) {
            double score = class_1890.method_8225(protection, stack);
            class_9285 mods = (class_9285)stack.method_57824(class_9334.field_49636);
            if (mods != null) {
               for (class_9287 entry : mods.comp_2393()) {
                  class_6880<class_1320> attribute = entry.comp_2395();
                  if (attribute == class_5134.field_23724 || attribute == class_5134.field_23725) {
                     score += entry.comp_2396().comp_2449();
                  }
               }
            }

            if (score > bestScore) {
               bestScore = score;
               bestSlot = slot;
            }
         }
      }

      return bestSlot;
   }
}
