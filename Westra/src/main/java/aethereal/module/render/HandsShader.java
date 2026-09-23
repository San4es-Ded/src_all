package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.HandEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.shader.HandShader;
import aethereal.ui.shader.NoiseShader;
import net.minecraft.class_5498;

@ModuleRegister(
   a = "Hands Shader",
   b = "Накладывает шейдер на руку от первого лица",
   c = Category.Render
)
public class HandsShader extends Module {
   private final HandShader e = new HandShader();
   private final ModeSetting b = new ModeSetting("Эффект", "Свечение", "Свечение", "Туманность", "Звёздное поле", "Паутина", "Плазма");
   private final SliderSetting c = new SliderSetting("Непрозрачность", 0.6F, 0.0F, 1.0F, 0.05F);
   private final BooleanSetting d = new BooleanSetting("Скрыть саму руку", false).a(() -> !this.b.l("Свечение"));
   private final BooleanSetting f = new BooleanSetting("Цвет из темы", true);
   private final ColorSetting g = new ColorSetting("Цвет эффекта", ColorUtil.a(255, 111, 181, 255)).a(() -> !this.f.c());

   public HandsShader() {
      this.a(new Setting[]{this.b, this.c, this.d, this.f, this.g});
   }

   @EventTarget
   public void a(HandEvent event) {
      if (aM_.field_1690.method_31044() == class_5498.field_26664) {
         if (this.b.l("Свечение")) {
            NoiseShader shader = Westra.h().d().i().f();
            if (event.b()) {
               shader.e();
            }

            if (event.c()) {
               shader.a(this.q());
            }
         } else {
            if (event.b()) {
               this.e.e();
            }

            if (event.c()) {
               this.e.a(this.q(), this.r(), this.d.c());
            }
         }
      }
   }

   private float[] q() {
      float[] color = ColorUtil.a(this.f.c() ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : this.g.c());
      color[3] = this.c.c();
      return color;
   }

   private int r() {
      if (this.b.l("Звёздное поле")) {
         return 1;
      } else if (this.b.l("Паутина")) {
         return 2;
      } else {
         return this.b.l("Плазма") ? 3 : 0;
      }
   }
}
