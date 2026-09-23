package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import net.minecraft.class_1268;
import net.minecraft.class_7439;

@ModuleRegister(
   a = "Anti AFK",
   b = "Не даёт серверу кикнуть вас за бездействие",
   c = Category.Player
)
public class AntiAFK extends Module {
   private final ModeSetting b = new ModeSetting("Режим использования", "Обычный", "Обычный", "FunTime");
   private final MultiModeSetting c = new MultiModeSetting(
         "Выполнять действия", new BooleanSetting("Прыжок", true), new BooleanSetting("Взмах", true), new BooleanSetting("Движение", true)
      )
      .a(() -> this.b.l("Обычный"));
   private final BooleanSetting d = new BooleanSetting("Реагировать на недоступность", false).a(() -> this.b.l("FunTime"));

   public AntiAFK() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget
   public void a(InputEvent event) {
      if (aM_.field_1724.field_6012 % 600 == 0) {
         if (this.b.l("Обычный")) {
            if (this.c.a("Прыжок").c() && aM_.field_1724.method_24828()) {
               event.b(true);
            }

            if (this.c.a("Взмах").c()) {
               aM_.field_1724.method_6104(class_1268.field_5808);
            }

            if (this.c.a("Движение").c()) {
               Westra.h().d().v().g().a(7);
               return;
            }

            return;
         }

         if (this.b.l("FunTime") && !this.d.c()) {
            Westra.h().d().v().g().a(7);
         }
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (this.b.l("FunTime")
         && this.d.c()
         && event.c()
         && event.d() instanceof class_7439 class_7439VarD
         && class_7439VarD.comp_763().getString().equals("Данная команда недоступна в режиме AFK")) {
         Westra.h().d().v().g().a(7);
      }
   }
}
