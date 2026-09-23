package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;
import net.minecraft.class_1109;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_3414;
import net.minecraft.class_3417;

@ModuleRegister(
   a = "Hit Sounds",
   b = "Проигрывает выбранный звук при попадании по цели",
   c = Category.Combat
)
public class HitSounds extends Module implements Interface {
   private final ModeSetting b = new ModeSetting("Звук", "Опыт", "Опыт", "Колокол", "Пинг", "Стрела", "Аметист", "Наковальня");
   private final SliderSetting c = new SliderSetting("Громкость", 1.0F, 0.1F, 3.0F, 0.1F);
   private final SliderSetting d = new SliderSetting("Высота тона", 1.0F, 0.5F, 2.0F, 0.05F);
   private final BooleanSetting e = new BooleanSetting("Только по игрокам", true);
   private final BooleanSetting f = new BooleanSetting("Повышать тон в серии", true);
   private final CounterUtil g = new CounterUtil();
   private int h;

   public HitSounds() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f});
   }

   @EventTarget
   public void a(AttackEvent event) {
      class_1297 target = event.b();
      if (target != null && target != aM_.field_1724) {
         if (!this.e.c() || target instanceof class_1657) {
            if (this.g.a(2000L)) {
               this.h = 0;
            }

            this.g.b();
            float pitch = this.d.c();
            if (this.f.c()) {
               pitch += Math.min(this.h, 8) * 0.06F;
               this.h++;
            }

            aM_.method_1483().method_4873(class_1109.method_4757(this.q(), Math.min(pitch, 2.0F), this.c.c()));
         }
      }
   }

   private class_3414 q() {
      if (this.b.l("Колокол")) {
         return (class_3414)class_3417.field_14793.comp_349();
      } else if (this.b.l("Пинг")) {
         return (class_3414)class_3417.field_14622.comp_349();
      } else if (this.b.l("Стрела")) {
         return class_3417.field_15224;
      } else if (this.b.l("Аметист")) {
         return class_3417.field_26982;
      } else {
         return this.b.l("Наковальня") ? class_3417.field_14833 : class_3417.field_14627;
      }
   }

   @Override
   public void b() {
      this.h = 0;
      this.g.b();
      super.b();
   }
}
