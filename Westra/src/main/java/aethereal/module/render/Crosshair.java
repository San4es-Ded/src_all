package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.CrosshairEvent;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import net.minecraft.class_332;
import net.minecraft.class_3966;

@ModuleRegister(
   a = "Crosshair",
   b = "Отображает настраиваемый прицел на экране",
   c = Category.Render
)
public class Crosshair extends Module {
   private final SliderSetting b = new SliderSetting("Расстояние от центра", 0.0F, 0.0F, 6.0F, 0.5F);
   private final SliderSetting c = new SliderSetting("Длина сегментов", 2.5F, 2.0F, 5.0F, 0.5F);
   private final MultiModeSetting d = new MultiModeSetting(
      "Параметры прицела", new BooleanSetting("Адаптивность", false), new BooleanSetting("Контур", true), new BooleanSetting("Центральная метка", false)
   );

   public Crosshair() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget
   public void a(CrosshairEvent e) {
      if (aM_.field_1690.method_31044().method_31034() && !aM_.field_1690.field_1842) {
         e.a(true);
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.b() && aM_.field_1690.method_31044().method_31034() && !aM_.field_1690.field_1842) {
         this.a(event, aM_.method_22683().method_4486() / 2.0F, aM_.method_22683().method_4502() / 2.0F, 1.0F - aM_.field_1724.method_7261(event.g()));
      }
   }

   private void a(DrawEvent drawEvent, float centerX, float centerY, float cooldown) {
      Draw2DProcessor draw2D = drawEvent.d();
      class_332 context = drawEvent.i();
      float actualGap = this.d.a("Адаптивность").c() ? this.b.c() + 8.0F * cooldown : this.b.c();
      int color = aM_.field_1765 instanceof class_3966 ? ColorUtil.a(255, 64, 64) : -1;
      if (this.d.a("Контур").c()) {
         draw2D.a(context, centerX + actualGap - 0.5F, centerY - 0.5F - 0.5F, this.c.c() + 1.0F, 2.0F, ColorUtil.a(0, 0, 0));
         draw2D.a(context, centerX - actualGap - this.c.c() - 0.5F, centerY - 0.5F - 0.5F, this.c.c() + 1.0F, 2.0F, ColorUtil.a(0, 0, 0));
         draw2D.a(context, centerX - 0.5F - 0.5F, centerY - actualGap - this.c.c() - 0.5F, 2.0F, this.c.c() + 1.0F, ColorUtil.a(0, 0, 0));
         draw2D.a(context, centerX - 0.5F - 0.5F, centerY + actualGap - 0.5F, 2.0F, this.c.c() + 1.0F, ColorUtil.a(0, 0, 0));
         draw2D.a(context, centerX + actualGap, centerY - 0.5F, this.c.c(), 1.0F, color);
         draw2D.a(context, centerX - actualGap - this.c.c(), centerY - 0.5F, this.c.c(), 1.0F, color);
         draw2D.a(context, centerX - 0.5F, centerY - actualGap - this.c.c(), 1.0F, this.c.c(), color);
         draw2D.a(context, centerX - 0.5F, centerY + actualGap, 1.0F, this.c.c(), color);
      } else {
         draw2D.a(context, centerX + actualGap, centerY - 0.5F, this.c.c(), 1.0F, color);
         draw2D.a(context, centerX - actualGap - this.c.c(), centerY - 0.5F, this.c.c(), 1.0F, color);
         draw2D.a(context, centerX - 0.5F, centerY - actualGap - this.c.c(), 1.0F, this.c.c(), color);
         draw2D.a(context, centerX - 0.5F, centerY + actualGap, 1.0F, this.c.c(), color);
      }

      if (this.d.a("Центральная метка").c() && actualGap > 0.0F) {
         float x = centerX - 0.5F;
         float y = centerY - 0.5F;
         if (this.d.a("Контур").c()) {
            draw2D.a(context, x - 0.5F, y - 0.5F, 2.0F, 2.0F, ColorUtil.a(0, 0, 0));
            draw2D.a(context, x, y, 1.0F, 1.0F, color);
         } else {
            draw2D.a(context, x, y, 1.0F, 1.0F, color);
         }
      }
   }
}
