package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import net.minecraft.class_10192;
import net.minecraft.class_1304;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_5134;
import net.minecraft.class_7924;
import net.minecraft.class_9276;
import net.minecraft.class_9285;
import net.minecraft.class_9334;
import net.minecraft.class_9837;
import net.minecraft.class_9285.class_9287;

@ModuleRegister(
   a = "Auto Armor",
   b = "Автоматически надевает лучшую броню из инвентаря и мешков",
   c = Category.Combat
)
public class AutoArmor extends Module implements Interface {
   private final BooleanSetting b = new BooleanSetting("Не в движении", true);
   private final class_1304[] c = new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166};
   private int d;

   public AutoArmor() {
      this.a(new Setting[]{this.b});
   }

   @Override
   public void b() {
      super.b();
      this.d = 0;
   }

   @Override
   public void c() {
      super.c();
      this.d = 0;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (Westra.h().d().v().a().a().isEmpty()) {
         this.d++;
         boolean urgent = this.a(aM_.field_1724.method_6118(class_1304.field_6169))
            || this.a(aM_.field_1724.method_6118(class_1304.field_6174))
            || this.a(aM_.field_1724.method_6118(class_1304.field_6172))
            || this.a(aM_.field_1724.method_6118(class_1304.field_6166));
         if (!urgent) {
            if (this.d % 2 != 0) {
               return;
            }

            if (this.b.c() && aM_.field_1724.method_18798().method_37268() > 9.99999713651348E-5) {
               return;
            }
         }

         int armorIndex = 0;

         while (armorIndex < this.c.length && !this.a(this.c[armorIndex], armorIndex)) {
            armorIndex++;
         }
      }
   }

   private boolean a(class_1304 slot, int armorIndex) {
      class_1799 current = aM_.field_1724.method_6118(slot);
      boolean low = this.a(current);
      double best = low ? this.b(current) + 20 : this.c(current);
      int bestSlot = -1;
      int bestBundle = -1;

      for (int inventorySlot = 0; inventorySlot < 36; inventorySlot++) {
         class_1799 stack = aM_.field_1724.method_31548().method_5438(inventorySlot);
         if (this.a(stack, slot)) {
            double value = low ? this.b(stack) : this.c(stack);
            if (value > best) {
               best = value;
               bestSlot = inventorySlot;
               bestBundle = -1;
            }
         }

         class_9276 contents = (class_9276)stack.method_57824(class_9334.field_49650);
         if (contents != null) {
            for (int bundleIndex = 0; bundleIndex < contents.method_57426(); bundleIndex++) {
               class_1799 bundled = contents.method_57422(bundleIndex);
               if (this.a(bundled, slot)) {
                  double value2 = low ? this.b(bundled) : this.c(bundled);
                  if (value2 > best) {
                     best = value2;
                     bestSlot = inventorySlot;
                     bestBundle = bundleIndex;
                  }
               }
            }
         }
      }

      if (bestSlot == -1) {
         return false;
      } else {
         if (bestBundle != -1) {
            aM_.field_1724.field_3944.method_52787(new class_9837(bestSlot < 9 ? 36 + bestSlot : bestSlot, bestBundle));
         }

         Westra.h().d().v().a().b(bestSlot, armorIndex, 1);
         return true;
      }
   }

   private boolean a(class_1799 stack) {
      return !stack.method_7960() && stack.method_7936() > 0 && this.b(stack) < 41;
   }

   private boolean a(class_1799 stack, class_1304 slot) {
      class_10192 equippable;
      return !stack.method_7960()
         && (equippable = (class_10192)stack.method_57824(class_9334.field_54196)) != null
         && equippable.comp_3174() == slot
         && (slot != class_1304.field_6174 || !stack.method_31574(class_1802.field_8833));
   }

   private int b(class_1799 stack) {
      return stack.method_7936() - stack.method_7919();
   }

   private double c(class_1799 stack) {
      if (stack.method_7960()) {
         return 0.0;
      } else {
         double score = class_1890.method_8225(aM_.field_1687.method_30349().method_30530(class_7924.field_41265).method_46747(class_1893.field_9111), stack);
         class_9285 modifiers = (class_9285)stack.method_57824(class_9334.field_49636);
         if (modifiers != null) {
            for (class_9287 entry : modifiers.comp_2393()) {
               if (entry.comp_2395() == class_5134.field_23724 || entry.comp_2395() == class_5134.field_23725) {
                  score += entry.comp_2396().comp_2449();
               }
            }
         }

         return score;
      }
   }
}
