package aethereal.ui.widget.westra;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Fonts;
import aethereal.ui.recode.RecodeKit;
import aethereal.ui.shader.GradientUtil;
import net.minecraft.class_2960;

public final class WestraStyle {
   public static final float a = 12.0F;
   public static final float b = 7.0F;
   public static final float c = 5.0F;
   public static final float d = 1.75F;
   public static final float e = 5.5F;
   public static final float f = 6.25F;
   public static final float g = 1.75F;
   public static final float h = 3.0F;
   public static final float i = 2.5F;
   public static final float j = 6.5F;

   private WestraStyle() {
   }

   public static void a(DrawEvent event, float x, float y, float width, float height, float animation, boolean rail) {
      if (!(animation <= 0.0F)) {
         ThemeProcessor theme = Westra.h().d().o();
         int accent = theme.a(ThemeInfo.PRIMARY).a();
         float opacity = theme.a(ThemeInfo.BACKGROUND_HUD).b();
         int base = ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_HUD).a(), accent, 0.05F);
         event.d().b(event.h(), x, y, width, height, 5.0F, ColorUtil.a(base, opacity * animation), animation);
         int top = ColorUtil.a(ColorUtil.a(base, accent, 0.12F), opacity * 0.85F * animation);
         int bottom = ColorUtil.a(ColorUtil.a(base, -16777216, 0.25F), opacity * 0.85F * animation);
         event.d().a(event.h(), x, y, width, height, 5.0F, top, top, bottom, bottom);
         event.d().a(event.h(), x, y, width, height, 5.0F, 0.5F, ColorUtil.a(ColorUtil.a(-1, accent, 0.4F), 0.12F * animation));
         RecodeKit.shimmer(event.h(), x + 5.0F, y + 0.25F, width - 10.0F, 0.6F, RecodeKit.time() * 0.9F + x * 0.01F, 0.5F * animation);
         if (rail) {
            float railHeight = Math.max(2.0F, height - 5.0F);
            float railY = y + (height - railHeight) / 2.0F;
            int railTop = ColorUtil.a(accent, animation);
            int railBottom = ColorUtil.a(RecodeKit.accentShade(), animation);
            event.d().a(event.h(), x + 2.5F - 1.25F, railY - 1.0F, 4.25F, railHeight + 2.0F, 2.125F, ColorUtil.a(accent, 0.2F * animation));
            event.d().a(event.h(), x + 2.5F, railY, 1.75F, railHeight, 0.875F, railTop, railTop, railBottom, railBottom);
         }
      }
   }

   public static float a(DrawEvent event, float x, float y, float height, String label, String value, int valueColor, float animation) {
      float cursor = x;
      if (label != null && !label.isEmpty()) {
         Fonts.c
            .a(
               event.h(),
               label,
               x,
               y + (height - Fonts.c.a(5.5F)) / 2.0F - 0.25F,
               5.5F,
               ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), animation)
            );
         cursor = x + (Fonts.c.a(label, 5.5F) + 1.75F);
      }

      Fonts.d.a(event.h(), value, cursor, y + (height - Fonts.d.a(6.25F)) / 2.0F - 0.25F, 6.25F, ColorUtil.a(valueColor, animation));
      return cursor + Fonts.d.a(value, 6.25F) - x;
   }

   public static float a(String label, String value) {
      float width = Fonts.d.a(value, 6.25F);
      if (label != null && !label.isEmpty()) {
         width += Fonts.c.a(label, 5.5F) + 1.75F;
      }

      return width;
   }

   public static void b(DrawEvent event, float x, float y, float height, float animation) {
      float size = 2.0F;
      int color = ColorUtil.a(RecodeKit.accent(), -1, 0.45F);
      event.d().a(event.h(), x - size / 2.0F + 0.25F, y + (height - size) / 2.0F, size, size, size / 2.0F, ColorUtil.a(color, 0.55F * animation));
   }

   public static void a(DrawEvent event, String text, float x, float y, float size, float animation) {
      Fonts.d.a(event.h(), GradientUtil.a(text, Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), 2.2F, 1.6F), x, y, size, (double)animation);
   }

   public static void c(DrawEvent event, float x, float y, float size, float animation) {
      Fonts.d.a(event.h(), "Westra", x, y, size, ColorUtil.a(-1, animation));
      float offset = Fonts.d.a("Westra", size) + size * 0.3F;
      Fonts.d
         .a(
            event.h(),
            GradientUtil.a("Recode", ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), -1, 0.55F), 2.2F, 1.6F),
            x + offset,
            y,
            size,
            (double)animation
         );
   }

   public static void d(DrawEvent event, float x, float y, float width, float height, float animation) {
      if (!(animation <= 0.0F)) {
         ThemeProcessor theme = Westra.h().d().o();
         int accent = theme.a(ThemeInfo.PRIMARY).a();
         float opacity = theme.a(ThemeInfo.BACKGROUND_HUD).b();
         float radius = height / 2.0F;
         int base = ColorUtil.a(theme.a(ThemeInfo.BACKGROUND_HUD).a(), accent, 0.05F);
         event.d().a(event.h(), x, y, width, height, radius, ColorUtil.a(base, opacity * animation), animation, ColorUtil.a(accent, 0.16F * animation), 10.0F);
         int left = ColorUtil.a(ColorUtil.a(base, accent, 0.14F), opacity * 0.8F * animation);
         int right = ColorUtil.a(ColorUtil.a(base, -16777216, 0.2F), opacity * 0.8F * animation);
         event.d().a(event.h(), x, y, width, height, radius, left, right, left, right);
         event.d().a(event.h(), x, y, width, height, radius, 0.5F, ColorUtil.a(ColorUtil.a(-1, accent, 0.4F), 0.14F * animation));
      }
   }

   public static void e(DrawEvent event, String glyph, float x, float y, float size, float animation) {
      int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      int top = ColorUtil.a(accent, animation);
      int bottom = ColorUtil.a(RecodeKit.accentShade(), animation);
      event.d().a(event.h(), x, y, size, size, size / 2.0F, top, top, bottom, bottom);
      float glyphSize = size * 0.55F;
      Fonts.a
         .a(
            event.h(),
            glyph,
            x + (size - Fonts.a.b(glyph, glyphSize)) / 2.0F,
            Fonts.a.a(glyph, glyphSize, y + size / 2.0F),
            glyphSize,
            ColorUtil.a(-1, animation)
         );
   }

   public static void g(DrawEvent event, float x, float y, float size, float animation) {
      int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      event.d().a(event.h(), class_2960.method_60655("westra", "pictures/logo_avatar.png"), x, y, size, size, size / 2.0F, ColorUtil.a(-1, animation));
      event.d().a(event.h(), x, y, size, size, size / 2.0F, 0.5F, ColorUtil.a(ColorUtil.a(accent, -1, 0.4F), 0.5F * animation));
   }

   public static float f(DrawEvent event, String value, float right, float centerY, int color, float animation) {
      float height = 8.5F;
      float width = Fonts.d.a(value, 5.6F) + 7.0F;
      float x = right - width;
      int tone = color == -1 ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : color;
      event.d().a(event.h(), x, centerY - height / 2.0F, width, height, height / 2.0F, ColorUtil.a(tone, 0.3F * animation));
      int textColor = color == -1 ? ColorUtil.a(tone, -1, 0.8F) : color;
      Fonts.d.b(event.h(), value, x + width / 2.0F, Fonts.d.a(value, 5.6F, centerY), 5.6F, ColorUtil.a(textColor, animation));
      return width;
   }

   public static float f(String value) {
      return Fonts.d.a(value, 5.6F) + 7.0F;
   }

   public static float c(float size) {
      return Fonts.d.a("Westra", size) + size * 0.3F + Fonts.d.a("Recode", size);
   }
}
