package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.HandViewEvent;
import aethereal.setting.ButtonSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_1268;
import net.minecraft.class_4587;

@ModuleRegister(
   a = "View Model",
   b = "Изменяет положение и размер предметов в руке",
   c = Category.Render
)
public class ViewModel extends Module {
   private final SliderSetting b = new SliderSetting("Основная рука X", 0.0F, -2.0F, 2.0F, 0.1F);
   private final SliderSetting c = new SliderSetting("Основная рука Y", 0.0F, -2.0F, 2.0F, 0.1F);
   private final SliderSetting d = new SliderSetting("Основная рука Z", 0.0F, -2.0F, 2.0F, 0.1F);
   private final SliderSetting e = new SliderSetting("Вторая рука X", 0.0F, -2.0F, 2.0F, 0.1F);
   private final SliderSetting f = new SliderSetting("Вторая рука Y", 0.0F, -2.0F, 2.0F, 0.1F);
   private final SliderSetting g = new SliderSetting("Вторая рука Z", 0.0F, -2.0F, 2.0F, 0.1F);
   private final ButtonSetting h = new ButtonSetting("Сбросить позиции", () -> this.e().forEach(setting -> {
      if (setting instanceof SliderSetting slider) {
         slider.a(slider.g());
      }
   }));

   public ViewModel() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f, this.g, this.h});
   }

   @EventTarget
   public void a(HandViewEvent e) {
      class_4587 matrix = e.b();
      if (e.d().equals(class_1268.field_5808)) {
         matrix.method_46416(this.b.h(), this.c.h(), this.d.h());
      } else {
         matrix.method_46416(this.e.h(), this.f.h(), this.g.h());
      }
   }
}
