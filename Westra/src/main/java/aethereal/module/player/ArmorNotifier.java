package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.notification.Notification;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;
import net.minecraft.class_1799;

@ModuleRegister(
   a = "Armor Notifier",
   b = "Предупреждает, когда прочность надетой брони подходит к концу",
   c = Category.Player
)
public class ArmorNotifier extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Порог прочности", 15.0F, 1.0F, 50.0F, 1.0F);
   private final SliderSetting c = new SliderSetting("Пауза между напоминаниями", 10.0F, 3.0F, 60.0F, 1.0F);
   private final BooleanSetting d = new BooleanSetting("Учитывать предмет в руке", false);
   private final CounterUtil e = new CounterUtil();

   public ArmorNotifier() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null) {
         if (this.e.a((long)(this.c.c() * 1000.0F))) {
            for (int slot = 36; slot <= 39; slot++) {
               if (this.a(aM_.field_1724.method_31548().method_5438(slot))) {
                  return;
               }
            }

            if (this.d.c()) {
               this.a(aM_.field_1724.method_6047());
            }
         }
      }
   }

   private boolean a(class_1799 stack) {
      if (!stack.method_7960() && stack.method_7963() && stack.method_7936() > 0) {
         float left = (float)(stack.method_7936() - stack.method_7919()) / stack.method_7936() * 100.0F;
         if (left > this.b.c()) {
            return false;
         } else {
            this.e.b();
            Westra.h()
               .d()
               .m()
               .a(new Notification(stack.method_7972(), stack.method_7964().getString() + " — осталось " + Math.round(left) + "% прочности", 2500));
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void b() {
      this.e.b();
      super.b();
   }
}
