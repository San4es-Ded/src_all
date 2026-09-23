package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.RatioEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;

@ModuleRegister(
   a = "Aspect Ratio",
   b = "Изменяет соотношение сторон экрана",
   c = Category.Render
)
public class AspectRatio extends Module {
   public final ModeSetting b = new ModeSetting("Соотношение сторон", "Пользовательский", "4:3", "16:9", "1:1", "16:10", "Пользовательский");
   public final SliderSetting c = new SliderSetting("Соотношение", 1.9F, 0.1F, 5.0F, 0.1F).a(() -> this.b.l("Пользовательский"));

   public AspectRatio() {
      this.a(new Setting[]{this.b, this.c});
   }

   @EventTarget
   public void a(RatioEvent event) {
      event.a(this.q());
   }

   public float q() {
      String var1 = this.b.c();
      switch (var1) {
         case "4:3":
            return 1.3333334F;
         case "16:9":
            return 1.7777778F;
         case "1:1":
            return 1.0F;
         case "16:10":
            return 1.6F;
         default:
            return this.c.c();
      }
   }
}
