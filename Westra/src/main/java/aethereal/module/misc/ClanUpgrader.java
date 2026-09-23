package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import aethereal.util.InventoryUtil;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_3965;

@ModuleRegister(
   a = "Clan Upgrader",
   b = "Быстро прокачивает клан постановкой и ломкой редстоуна",
   c = Category.Misc
)
public class ClanUpgrader extends Module {
   private final SliderSetting b = new SliderSetting("Задержка постановки, мс", 60.0F, 20.0F, 400.0F, 10.0F);
   private final BooleanSetting c = new BooleanSetting("Использовать факел, если нет редстоуна", true);
   private class_2338 d;
   private boolean e;
   private long f;
   private long g;
   private int h = -1;

   public ClanUpgrader() {
      this.a(new Setting[]{this.b, this.c});
   }

   @Override
   public void b() {
      super.b();
      this.d = null;
      this.e = false;
      this.f = 0L;
      this.g = 0L;
      this.h = -1;
   }

   @Override
   public void c() {
      super.c();
      if (this.h != -1 && aM_.field_1724 != null) {
         aM_.field_1724.method_31548().field_7545 = this.h;
         this.h = -1;
      }

      this.d = null;
      this.e = false;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null && aM_.field_1761 != null) {
         class_2338 nearby = r();
         if (nearby != null) {
            this.d = nearby;
            this.e = true;
         }

         if (this.e && this.d != null) {
            q();
            if (aM_.field_1687.method_8320(this.d).method_26215()) {
               this.e = false;
               this.d = null;
            } else {
               aM_.field_1761.method_2910(this.d, class_2350.field_11036);
               aM_.field_1761.method_2902(this.d, class_2350.field_11036);
               aM_.field_1724.method_6104(class_1268.field_5808);
            }
         } else if (System.currentTimeMillis() - this.f >= (long)this.b.c().floatValue()) {
            int slot = InventoryUtil.a(class_1802.field_8725, true);
            if (slot == -1 && this.c.c()) {
               slot = InventoryUtil.a(class_1802.field_8810, true);
            }

            if (slot == -1) {
               if (System.currentTimeMillis() - this.g > 1000L) {
                  this.g = System.currentTimeMillis();
                  ChatUtil.a("Нужен редстоун или факел в хотбаре.");
               }
            } else {
               class_2338 placed = this.s(slot);
               if (placed != null) {
                  this.d = placed;
                  this.e = true;
               }

               this.f = System.currentTimeMillis();
            }
         }
      }
   }

   private class_2338 s(int slot) {
      class_2338 position = aM_.field_1724.method_24515();
      if (!aM_.field_1687.method_8320(position).method_45474()) {
         return null;
      } else {
         if (this.h == -1) {
            this.h = aM_.field_1724.method_31548().field_7545;
         }

         int previous = aM_.field_1724.method_31548().field_7545;
         aM_.field_1724.method_31548().field_7545 = slot;
         q();
         boolean placed = false;

         for (class_2350 direction : class_2350.values()) {
            class_2338 neighbour = position.method_10093(direction);
            if (!aM_.field_1687.method_8320(neighbour).method_45474()) {
               class_2350 side = direction.method_10153();
               class_243 hit = class_243.method_24953(neighbour).method_1031(side.method_10148() * 0.5, side.method_10164() * 0.5, side.method_10165() * 0.5);
               class_1269 result = aM_.field_1761.method_2896(aM_.field_1724, class_1268.field_5808, new class_3965(hit, side, neighbour, false));
               if (result.method_23665()) {
                  aM_.field_1724.method_6104(class_1268.field_5808);
                  placed = true;
                  break;
               }
            }
         }

         aM_.field_1724.method_31548().field_7545 = previous;
         return placed ? position : null;
      }
   }

   private static void q() {
      aM_.field_1724.method_36457(90.0F);
   }

   private static class_2338 r() {
      class_2338 base = aM_.field_1724.method_24515();
      class_2338 best = null;
      double bestDistance = Double.MAX_VALUE;

      for (int y = -1; y <= 0; y++) {
         for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
               class_2338 position = base.method_10069(x, y, z);
               if (aM_.field_1687.method_8320(position).method_27852(class_2246.field_10091)
                  || aM_.field_1687.method_8320(position).method_27852(class_2246.field_10336)) {
                  double distance = position.method_10262(base);
                  if (distance < bestDistance) {
                     bestDistance = distance;
                     best = position;
                  }
               }
            }
         }
      }

      return best;
   }
}
