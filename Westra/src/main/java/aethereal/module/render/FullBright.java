package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.GammaEvent;
import aethereal.event.TickEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_1293;
import net.minecraft.class_1294;

@ModuleRegister(
   a = "Full Bright",
   b = "Полностью освещает мир через гамму или ночное зрение",
   c = Category.Render
)
public class FullBright extends Module {
   private final ModeSetting b = new ModeSetting("Режим видения", "Гамма", "Гамма", "Ночное зрение");
   private final SliderSetting c = new SliderSetting("Уровень гаммы", 4.0F, 1.0F, 8.0F, 0.5F).a(() -> this.b.l("Гамма"));

   public FullBright() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.b.l("Ночное зрение")) {
         aM_.field_1724.method_6092(new class_1293(class_1294.field_5925, 240, 1, false, false, false));
      } else {
         aM_.field_1724.method_6016(class_1294.field_5925);
      }
   }

   @EventTarget
   public void a(GammaEvent event) {
      if (this.b.l("Гамма")) {
         event.a(this.c.c().floatValue());
      }
   }
}
