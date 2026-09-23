package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;

@ModuleRegister(
   a = "Hit Color",
   b = "Меняет цвет сущности при получении урона",
   c = Category.Render
)
public class HitColor extends Module {
   private final BooleanSetting b = new BooleanSetting("Цвет из темы", true);
   private final ColorSetting c = new ColorSetting("Свой цвет", ColorUtil.a(255, 60, 60, 255)).a(() -> !this.b.c());
   private final SliderSetting d = new SliderSetting("Насыщенность", 0.6F, 0.1F, 1.0F, 0.05F);

   public HitColor() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   public int q() {
      int color = this.b.c() ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : this.c.c();
      return ColorUtil.a(color, this.d.c());
   }
}
