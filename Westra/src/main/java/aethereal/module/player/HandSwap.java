package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.TickEvent;
import aethereal.notification.Notification;
import aethereal.setting.BindSetting;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import net.minecraft.class_1306;
import net.minecraft.class_3675;

@ModuleRegister(
   a = "Hand Swap",
   b = "Меняет ведущую руку одним нажатием клавиши, не заходя в настройки игры",
   c = Category.Player
)
public class HandSwap extends Module implements Interface {
   private final BindSetting b = new BindSetting("Клавиша смены", -1);
   private final BooleanSetting c = new BooleanSetting("Уведомление", true);
   private boolean d2;

   public HandSwap() {
      this.a(new Setting[]{this.b, this.c});
   }

   @Override
   public void b() {
      this.d2 = true;
      super.b();
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null && aM_.field_1755 == null) {
         int key = this.b.c();
         boolean down = key != -1 && class_3675.method_15987(aM_.method_22683().method_4490(), key);
         if (down && !this.d2) {
            this.q();
         }

         this.d2 = down;
      } else {
         this.d2 = false;
      }
   }

   private void q() {
      class_1306 arm = ((class_1306)aM_.field_1690.method_42552().method_41753()).method_5928();
      aM_.field_1690.method_42552().method_41748(arm);
      aM_.field_1690.method_1643();
      aM_.field_1690.method_1640();
      if (this.c.c()) {
         Westra.h().d().m().a(new Notification("C", "Ведущая рука: " + (arm == class_1306.field_6182 ? "левая" : "правая") + ".", 1500));
      }
   }
}
