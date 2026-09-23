package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_339;

@ModuleRegister(
   a = "Buttons",
   b = "Заменяет ванильные кнопки на кнопки в стиле клиента",
   c = Category.Render
)
public class Buttons extends Module {
   private final SliderSetting b = new SliderSetting("Скругление", 4.0F, 0.0F, 10.0F, 0.5F);
   private final SliderSetting c = new SliderSetting("Кегль текста", 8.0F, 6.0F, 12.0F, 0.5F);
   private final BooleanSetting d = new BooleanSetting("Обводка", true);
   private final BooleanSetting e = new BooleanSetting("Подсветка акцентом", true);
   private final BooleanSetting f = new BooleanSetting("Размытие под кнопкой", true);
   private final Map<class_339, AnimationUtil> g = new WeakHashMap<>();

   public Buttons() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f});
   }

   public void a(class_332 context, class_339 widget, int mouseX, int mouseY, float delta) {
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float x = widget.method_46426();
      float y = widget.method_46427();
      float width = widget.method_25368();
      float height = widget.method_25364();
      float radius = Math.min(this.b.c(), height / 2.0F);
      boolean enabled = widget.field_22763;
      AnimationUtil hover = this.g.computeIfAbsent(widget, key -> new AnimationUtil());
      hover.a(0.0F, 1.0F, 0.25F, EasingList.i, delta);
      hover.a(enabled && widget.method_49606());
      float value = hover.c();
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      int fill = ColorUtil.a(enabled ? accent : ColorUtil.a(120, 120, 130, 255), (0.05F + 0.14F * value) * (enabled ? 1.0F : 0.6F));
      if (this.f.c()) {
         draw.b(context.method_51448(), x, y, width, height, radius, ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), 0.55F));
      }

      draw.a(context.method_51448(), x, y, width, height, radius, fill);
      if (this.d.c()) {
         int outline = this.e.c() && enabled
            ? ColorUtil.a(accent, 0.25F + 0.55F * value)
            : ColorUtil.a(theme.a(ThemeInfo.OUTLINE_MEDIUM).a(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b());
         draw.a(context.method_51448(), x, y, width, height, radius, 0.5F, outline);
      }

      if (this.e.c() && enabled && value > 0.0F) {
         float barWidth = (width - 8.0F) * value;
         draw.a(context.method_51448(), x + (width - barWidth) / 2.0F, y + height - 1.5F, barWidth, 1.0F, 0.5F, ColorUtil.a(accent, value));
      }

      class_2561 message = widget.method_25369();
      float size = this.c.c();
      float textWidth = Fonts.e.a(message.getString(), size);
      int color = enabled
         ? ColorUtil.a(ColorUtil.a(theme.a(ThemeInfo.TEXT).a(), accent, value * 0.35F), 1.0F)
         : ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), 1.0F);
      Fonts.e.a(context.method_51448(), message.getString(), x + (width - textWidth) / 2.0F, y + (height - Fonts.e.a(size)) / 2.0F - 0.5F, size, color);
   }

   public void a(class_332 context, class_339 widget, double value, float delta) {
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float x = widget.method_46426();
      float y = widget.method_46427();
      float width = widget.method_25368();
      float height = widget.method_25364();
      float radius = Math.min(this.b.c(), height / 2.0F);
      AnimationUtil hover = this.g.computeIfAbsent(widget, key -> new AnimationUtil());
      hover.a(0.0F, 1.0F, 0.25F, EasingList.i, delta);
      hover.a(widget.method_49606());
      float animation = hover.c();
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      if (this.f.c()) {
         draw.b(context.method_51448(), x, y, width, height, radius, ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), 0.55F));
      }

      draw.a(context.method_51448(), x, y, width, height, radius, ColorUtil.a(accent, 0.05F));
      float filled = (float)(width * Math.max(0.0, Math.min(1.0, value)));
      if (filled > 0.0F) {
         draw.a(context.method_51448(), x, y, filled, height, radius, ColorUtil.a(accent, 0.22F + 0.18F * animation));
      }

      if (this.d.c()) {
         draw.a(context.method_51448(), x, y, width, height, radius, 0.5F, ColorUtil.a(accent, 0.25F + 0.55F * animation));
      }

      float knob = Math.max(3.0F, height * 0.55F);
      float knobX = x + (width - knob) * (float)Math.max(0.0, Math.min(1.0, value));
      draw.a(context.method_51448(), knobX, y + (height - knob) / 2.0F, knob, knob, knob / 2.0F, ColorUtil.a(accent, 1.0F));
      String message = widget.method_25369().getString();
      float size = this.c.c();
      float textWidth = Fonts.e.a(message, size);
      Fonts.e
         .a(
            context.method_51448(),
            message,
            x + (width - textWidth) / 2.0F,
            y + (height - Fonts.e.a(size)) / 2.0F - 0.5F,
            size,
            ColorUtil.a(theme.a(ThemeInfo.TEXT).a(), 1.0F)
         );
   }

   public void b(class_332 context, class_339 widget, float delta) {
      Draw2DProcessor draw = Westra.h().d().i();
      ThemeProcessor theme = Westra.h().d().o();
      float x = widget.method_46426();
      float y = widget.method_46427();
      float width = widget.method_25368();
      float height = widget.method_25364();
      float radius = Math.min(this.b.c(), Math.min(width, height) / 2.0F);
      AnimationUtil hover = this.g.computeIfAbsent(widget, key -> new AnimationUtil());
      hover.a(0.0F, 1.0F, 0.25F, EasingList.i, delta);
      hover.a(widget.field_22763 && widget.method_49606());
      float value = hover.c();
      int accent = theme.a(ThemeInfo.PRIMARY).a();
      if (this.f.c()) {
         draw.b(context.method_51448(), x, y, width, height, radius, ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_GUI).a(), 0.55F));
      }

      draw.a(context.method_51448(), x, y, width, height, radius, ColorUtil.a(accent, 0.05F + 0.14F * value));
      if (this.d.c()) {
         draw.a(context.method_51448(), x, y, width, height, radius, 0.5F, ColorUtil.a(accent, 0.25F + 0.55F * value));
      }
   }
}
