package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_3965;

@ModuleRegister(
   a = "Fast Break",
   b = "Ускоряет разрушение блоков, обрабатывая добычу несколько раз за тик",
   c = Category.Movement
)
public class FastBreak extends Module {
   private final SliderSetting b = new SliderSetting("Интенсивность копания", 2.0F, 1.0F, 5.0F, 0.25F);

   public FastBreak() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1690.field_1886.method_1434() && aM_.field_1761.method_2923() && aM_.field_1765 instanceof class_3965 class_3965Var) {
         class_3965 hit = class_3965Var;

         for (int i = 0; i < this.b.c().intValue() - 1; i++) {
            aM_.field_1761.method_2902(hit.method_17777(), hit.method_17780());
         }
      }
   }
}
