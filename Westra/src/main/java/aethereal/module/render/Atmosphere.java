package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.shader.SkyShader;
import java.util.LinkedHashMap;
import java.util.Map;

@ModuleRegister(
   a = "Atmosphere",
   b = "Заменяет небо шейдерным эффектом",
   c = Category.Render
)
public class Atmosphere extends Module {
   private final ModeSetting b = new ModeSetting("Эффект", "Туманность", "Туманность", "Плазма", "Каустика", "Воронка", "Свечение");
   private final SliderSetting c = new SliderSetting("Скорость", 1.0F, 0.1F, 3.0F, 0.1F);
   private final SliderSetting d = new SliderSetting("Масштаб", 1.0F, 0.2F, 3.0F, 0.1F);
   private final SliderSetting e = new SliderSetting("Насыщенность", 1.0F, 0.1F, 2.0F, 0.05F);
   private final SliderSetting f = new SliderSetting("Непрозрачность", 1.0F, 0.1F, 1.0F, 0.05F);
   private final BooleanSetting g = new BooleanSetting("Цвет из темы", true);
   private final ColorSetting h = new ColorSetting("Свой цвет", ColorUtil.a(255, 111, 181, 255)).a(() -> !this.g.c());
   private final Map<String, SkyShader> i = new LinkedHashMap<>();

   public Atmosphere() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f, this.g, this.h});
      this.i.put("Туманность", new SkyShader("nebula"));
      this.i.put("Плазма", new SkyShader("plasma"));
      this.i.put("Каустика", new SkyShader("caustic"));
      this.i.put("Воронка", new SkyShader("drain"));
      this.i.put("Свечение", new SkyShader("bloom"));
   }

   public boolean q() {
      if (this.m() && aM_.field_1687 != null) {
         SkyShader shader = this.i.get(this.b.c());
         if (shader == null) {
            return false;
         } else {
            float[] color = ColorUtil.a(this.g.c() ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : this.h.c());
            shader.a(color, this.f.c(), this.c.c(), this.d.c(), this.e.c());
            return true;
         }
      } else {
         return false;
      }
   }
}
