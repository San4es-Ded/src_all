package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import java.util.List;
import net.minecraft.class_1713;
import net.minecraft.class_1714;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;

@ModuleRegister(
   a = "Crafter",
   b = "Складывает материалы в блоки у верстака",
   c = Category.Player
)
public class Crafter extends Module {
   private final ModeSetting b = new ModeSetting("Что крафтить", "Железо", "Железо", "Золото", "Алмазы", "Изумруды", "Уголь", "Редстоун", "Лазурит", "Медь");
   private final SliderSetting c = new SliderSetting("Задержка между крафтами", 4.0F, 1.0F, 20.0F, 1.0F);
   private final BooleanSetting d = new BooleanSetting("Выключаться без материалов", true);
   private final BooleanSetting e = new BooleanSetting("Сообщать в чат", true);
   private int f;
   private int g;

   public Crafter() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e});
   }

   @Override
   public void b() {
      super.b();
      this.f = 0;
      this.g = 0;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1761 != null) {
         if (aM_.field_1724.field_7512 instanceof class_1714 handler) {
            int delay = this.f + 1;
            this.f = delay;
            if (delay >= Math.round(this.c.c())) {
               this.f = 0;
               List<class_1735> grid = handler.method_61628();
               if (grid.size() >= 9) {
                  if (!handler.method_61627().method_7677().method_7960()) {
                     class_1799 result = handler.method_61627().method_7677();
                     int crafted = result.method_7947();
                     aM_.field_1761.method_2906(handler.field_7763, handler.method_61627().field_7874, 0, class_1713.field_7794, aM_.field_1724);
                     this.g += crafted;
                  } else if (a(grid)) {
                     class_1792 material = this.q();
                     int source = a(handler, material);
                     if (source == -1) {
                        if (this.d.c()) {
                           this.r();
                        }
                     } else if (handler.method_7611(source).method_7677().method_7947() < 9) {
                        if (this.d.c()) {
                           this.r();
                        }
                     } else {
                        aM_.field_1761.method_2906(handler.field_7763, source, 0, class_1713.field_7790, aM_.field_1724);

                        for (class_1735 slot : grid) {
                           aM_.field_1761.method_2906(handler.field_7763, slot.field_7874, 1, class_1713.field_7790, aM_.field_1724);
                        }

                        aM_.field_1761.method_2906(handler.field_7763, source, 0, class_1713.field_7790, aM_.field_1724);
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean a(List<class_1735> grid) {
      for (class_1735 slot : grid) {
         if (!slot.method_7677().method_7960()) {
            return false;
         }
      }

      return true;
   }

   private static int a(class_1714 handler, class_1792 material) {
      int best = -1;
      int bestCount = 0;

      for (class_1735 slot : handler.field_7761) {
         if (slot.field_7871 == aM_.field_1724.method_31548()) {
            class_1799 stack = slot.method_7677();
            if (stack.method_7909() == material && stack.method_7947() > bestCount) {
               bestCount = stack.method_7947();
               best = slot.field_7874;
            }
         }
      }

      return best;
   }

   private class_1792 q() {
      if (this.b.l("Золото")) {
         return class_1802.field_8695;
      } else if (this.b.l("Алмазы")) {
         return class_1802.field_8477;
      } else if (this.b.l("Изумруды")) {
         return class_1802.field_8687;
      } else if (this.b.l("Уголь")) {
         return class_1802.field_8713;
      } else if (this.b.l("Редстоун")) {
         return class_1802.field_8725;
      } else if (this.b.l("Лазурит")) {
         return class_1802.field_8759;
      } else {
         return this.b.l("Медь") ? class_1802.field_27022 : class_1802.field_8620;
      }
   }

   private void r() {
      if (this.e.c()) {
         ChatUtil.a("Крафт завершён, получено блоков: " + this.g + ".");
      }

      this.a(false);
   }
}
