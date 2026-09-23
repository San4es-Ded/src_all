package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.util.MathUtil;
import aethereal.util.ProjectUtil;
import aethereal.util.ServerUtil;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_238;

@ModuleRegister(
   a = "Entity Box",
   b = "Отображает боксы вокруг сущностей",
   c = Category.Render
)
public class EntityBox extends Module {
   private final ModeSetting b = new ModeSetting("Тип визуализации", "Квадрат", "Квадрат", "Углы", "Заливка", "Отключен");
   private final ModeSetting c = new ModeSetting("Источник цвета", "Клиентский", "Клиентский", "Статичный");
   private final ModeSetting d = new ModeSetting("Бар здоровья", "Отключен", "Отключен", "Стандартный").a(() -> this.b.l("Квадрат") || this.b.l("Углы"));
   private final ColorSetting e = new ColorSetting("Цвет визуализации", ColorUtil.a(255, 255, 255, 255)).a(() -> this.c.l("Статичный"));

   public EntityBox() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e});
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (this.b.l("Заливка")) {
         if (event.c()) {
            for (class_1297 entity : aM_.field_1687.method_18112()) {
               if (this.a(entity)) {
                  event.e()
                     .a(
                        event.h(),
                        entity.method_5829().method_997(MathUtil.a(entity, event.g()).method_1020(entity.method_19538())),
                        this.c.l("Статичный") ? this.e.c() : Westra.h().d().o().a(ThemeInfo.PRIMARY).a(),
                        0.75F
                     );
               }
            }
         }
      } else {
         if (event.b() && (this.b.l("Квадрат") || this.b.l("Углы"))) {
            Draw2DProcessor draw = event.d();

            for (class_1297 entityx : aM_.field_1687.method_18112()) {
               class_238 box = this.a(entityx) ? entityx.method_5829().method_997(MathUtil.a(entityx, event.g()).method_1020(entityx.method_19538())) : null;
               float[] bounds = box == null ? null : ProjectUtil.a(box);
               if (bounds != null) {
                  class_1309 living = entityx instanceof class_1309 ? (class_1309)entityx : null;
                  boolean healthBar = !this.d.l("Отключен") && living != null;
                  float percent = healthBar ? Math.min(Math.max(0.0F, ServerUtil.a.a(living)) / Math.max(1.0F, living.method_6063()), 1.0F) : 0.0F;
                  int healthColor = ColorUtil.b(ColorUtil.a(255, 0, 0, 255), ColorUtil.a(0, 255, 0, 255), percent);
                  int color = this.c.l("Статичный") ? this.e.c() : ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), 255);
                  this.a(draw, event, bounds[0], bounds[1], bounds[2], bounds[3], color, this.b.l("Углы"), healthBar, percent, healthColor);
               }
            }
         }
      }
   }

   private void a(
      Draw2DProcessor draw,
      DrawEvent event,
      float minX,
      float minY,
      float maxX,
      float maxY,
      int color,
      boolean corners,
      boolean healthBar,
      float healthPercent,
      int healthColor
   ) {
      float width = maxX - minX;
      float height = maxY - minY;
      if (!(width <= 0.0F) && !(height <= 0.0F)) {
         float line = 0.75F;
         float outline = 1.75F;
         int outlineColor = ColorUtil.a(0, 0, 0, 255);
         if (corners) {
            float length = Math.min(width, height) * 0.25F;
            this.a(draw, event, minX, minY, length, 0.0F, line, outline, color, outlineColor);
            this.a(draw, event, minX, minY, 0.0F, length, line, outline, color, outlineColor);
            this.a(draw, event, maxX, minY, -length, 0.0F, line, outline, color, outlineColor);
            this.a(draw, event, maxX, minY, 0.0F, length, line, outline, color, outlineColor);
            this.a(draw, event, minX, maxY, length, 0.0F, line, outline, color, outlineColor);
            this.a(draw, event, minX, maxY, 0.0F, -length, line, outline, color, outlineColor);
            this.a(draw, event, maxX, maxY, -length, 0.0F, line, outline, color, outlineColor);
            this.a(draw, event, maxX, maxY, 0.0F, -length, line, outline, color, outlineColor);
         } else {
            this.a(draw, event, minX, minY, width, 0.0F, line, outline, color, outlineColor);
            this.a(draw, event, minX, maxY, width, 0.0F, line, outline, color, outlineColor);
            this.a(draw, event, minX, minY, 0.0F, height, line, outline, color, outlineColor);
            this.a(draw, event, maxX, minY, 0.0F, height, line, outline, color, outlineColor);
         }

         if (healthBar) {
            float barX = minX - 3.0F;
            draw.a(event.h(), barX - 0.5F, minY - 0.5F, 1.75F, height + 1.0F, 0.0F, outlineColor);
            draw.a(event.h(), barX, minY + height * (1.0F - healthPercent), 0.75F, height * healthPercent, 0.0F, healthColor);
         }
      }
   }

   private void a(Draw2DProcessor draw, DrawEvent event, float x, float y, float lengthX, float lengthY, float line, float outline, int color, int outlineColor) {
      float left = Math.min(x, x + lengthX);
      float top = Math.min(y, y + lengthY);
      float width = lengthX == 0.0F ? line : Math.abs(lengthX);
      float height = lengthY == 0.0F ? line : Math.abs(lengthY);
      if (lengthX == 0.0F) {
         left -= line * 0.5F;
      }

      if (lengthY == 0.0F) {
         top -= line * 0.5F;
      }

      float outlineOffset = (outline - line) * 0.5F;
      draw.a(event.h(), left - outlineOffset, top - outlineOffset, width + outlineOffset * 2.0F, height + outlineOffset * 2.0F, 0.0F, outlineColor);
      draw.a(event.h(), left, top, width, height, 0.0F, color);
   }

   private boolean a(class_1297 entity) {
      return !(entity instanceof class_1657) && !(entity instanceof class_1542)
         ? false
         : entity != aM_.field_1724 || !aM_.field_1690.method_31044().method_31034();
   }
}
