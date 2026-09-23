package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.shader.MotionBlurShader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_243;

@ModuleRegister(
   a = "Motion Blur",
   b = "Размывает картинку при движении камеры",
   c = Category.Render
)
public class MotionBlur extends Module {
   private final SliderSetting b = new SliderSetting("Сила размытия", 1.0F, 0.1F, 3.0F, 0.1F);
   private final SliderSetting c = new SliderSetting("Количество проб", 8.0F, 2.0F, 24.0F, 1.0F);
   private final BooleanSetting d = new BooleanSetting("Симметричное размытие", true);
   private final MotionBlurShader e = new MotionBlurShader();

   public MotionBlur() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget(
      a = 0
   )
   public void a(DrawEvent event) {
      if (event.c()) {
         class_243 camera = aM_.method_1561().field_4686.method_19326();
         this.e.a(event.h().method_23760().method_23761(), RenderSystem.getProjectionMatrix(), camera.field_1352, camera.field_1351, camera.field_1350);
      }
   }

   @EventTarget(
      a = 0
   )
   public void b(DrawEvent event) {
      if (event.b() && aM_.field_1687 != null && aM_.field_1755 == null) {
         this.e.a(Math.round(this.c.c()), this.b.c(), this.d.c());
      }
   }
}
