package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.MotionEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;

@ModuleRegister(
   a = "Wall Climb",
   b = "Позволяет взбираться по стенам",
   c = Category.Movement
)
public class WallClimb extends Module {
   private final ModeSetting b = new ModeSetting("Выберите тип обхода", "Матрикс", "Матрикс");
   private final SliderSetting c = new SliderSetting("Скорость режима", 20.0F, 1.0F, 100.0F, 1.0F);
   private final CounterUtil d = new CounterUtil();

   public WallClimb() {
      this.a(new Setting[]{this.b, this.c});
   }

   @Override
   public void b() {
      super.b();
      this.d.b();
   }

   @EventTarget
   public void a(MotionEvent event) {
      if (this.b.l("Матрикс")) {
         this.a(event, this.c.c().longValue());
      }
   }

   private void a(MotionEvent event, long value) {
      if (this.d.a(value * 5L) && aM_.field_1724.field_5976) {
         event.b(true);
         aM_.field_1724.method_24830(true);
         aM_.field_1724.field_5992 = true;
         aM_.field_1724.field_5976 = true;
         aM_.field_1724.method_6043();
         this.d.b();
      }
   }
}
