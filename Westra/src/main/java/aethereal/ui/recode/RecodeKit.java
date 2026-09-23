package aethereal.ui.recode;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.ui.shader.GradientUtil;
import net.minecraft.class_4587;

public final class RecodeKit {
   public static final String BRAND = "Westra";
   public static final String EDITION = "Recode";
   private static final int WHITE = -1;
   private static final int BLACK = -16777216;

   private RecodeKit() {
   }

   public static Draw2DProcessor draw() {
      return Westra.h().d().i();
   }

   public static ThemeProcessor theme() {
      return Westra.h().d().o();
   }

   public static int accent() {
      return theme().a(ThemeInfo.PRIMARY).a();
   }

   public static int accentShade() {
      return ColorUtil.a(accent(), -9798401, 0.45F);
   }

   public static int text() {
      return theme().a(ThemeInfo.TEXT).a();
   }

   public static int dim() {
      return theme().a(ThemeInfo.TEXT_DISABLED).a();
   }

   public static int base() {
      return ColorUtil.a(theme().a(ThemeInfo.BACKGROUND_GUI).a(), accent(), 0.05F);
   }

   public static int alpha(int color, float alpha) {
      return ColorUtil.a(color, Math.max(0.0F, Math.min(1.0F, alpha)));
   }

   public static void glass(class_4587 matrices, float x, float y, float width, float height, float radius, float alpha, float opacity, float tint) {
      glass(matrices, x, y, width, height, radius, alpha, opacity, tint, 0.0F);
   }

   public static void glass(class_4587 matrices, float x, float y, float width, float height, float radius, float alpha, float opacity, float tint, float glow) {
      if (!(alpha <= 0.0F)) {
         Draw2DProcessor draw = draw();
         int base = base();
         int fill = alpha(base, opacity * alpha);
         if (glow > 0.0F) {
            draw.a(matrices, x, y, width, height, radius, fill, alpha, alpha(accent(), glow * alpha), 16.0F);
         } else {
            draw.b(matrices, x, y, width, height, radius, fill, alpha);
         }

         int top = alpha(ColorUtil.a(base, accent(), tint), opacity * alpha * 0.9F);
         int bottom = alpha(ColorUtil.a(base, -16777216, 0.3F), opacity * alpha * 0.9F);
         draw.a(matrices, x, y, width, height, radius, top, top, bottom, bottom);
         draw.a(matrices, x, y, width, height, radius, 0.5F, alpha(-1, 0.08F * alpha));
      }
   }

   public static void shimmer(class_4587 matrices, float x, float y, float width, float height, float phase, float alpha) {
      if (!(alpha <= 0.0F) && !(width <= 4.0F)) {
         Draw2DProcessor draw = draw();
         float travel = (float)((Math.sin(phase) + 1.0) * 0.5);
         float span = width * 0.45F;
         float center = x + span / 2.0F + (width - span) * travel;
         float left = Math.max(x, center - span / 2.0F);
         float right = Math.min(x + width, center + span / 2.0F);
         int clear = alpha(accent(), 0.0F);
         int bright = alpha(ColorUtil.a(accent(), -1, 0.25F), alpha);
         if (center > left) {
            draw.a(matrices, left, y, center - left, height, 0.0F, clear, bright, clear, bright);
         }

         if (right > center) {
            draw.a(matrices, center, y, right - center, height, 0.0F, bright, clear, bright, clear);
         }
      }
   }

   public static void toggle(class_4587 matrices, float x, float y, float width, float height, float on, float alpha) {
      Draw2DProcessor draw = draw();
      float radius = height / 2.0F;
      int off = alpha(-1, 0.08F * alpha);
      int onColor = alpha(accent(), (0.35F + 0.65F * on) * alpha);
      draw.a(matrices, x, y, width, height, radius, ColorUtil.a(off, onColor, on));
      if (on > 0.01F) {
         int left = alpha(accent(), on * alpha);
         int right = alpha(accentShade(), on * alpha);
         draw.a(matrices, x, y, width, height, radius, left, right, left, right);
      }

      draw.a(matrices, x, y, width, height, radius, 0.5F, alpha(-1, (0.09F - 0.05F * on) * alpha));
      float knob = height - 3.0F;
      float knobX = x + 1.5F + (width - knob - 3.0F) * on;
      if (on > 0.05F) {
         draw.a(matrices, knobX - 1.0F, y + 0.5F, knob + 2.0F, knob + 2.0F, (knob + 2.0F) / 2.0F, alpha(-1, 0.18F * on * alpha));
      }

      draw.a(matrices, knobX, y + 1.5F, knob, knob, knob / 2.0F, alpha(ColorUtil.a(-4933948, -1, on), alpha));
   }

   public static float chip(class_4587 matrices, String label, float x, float y, float size, float height, boolean filled, float alpha) {
      float width = Fonts.d.a(label, size) + height * 0.9F;
      Draw2DProcessor draw = draw();
      if (filled) {
         int left = alpha(accent(), alpha);
         int right = alpha(accentShade(), alpha);
         draw.a(matrices, x, y, width, height, height / 2.0F, left, right, left, right);
      } else {
         draw.a(matrices, x, y, width, height, height / 2.0F, alpha(accent(), 0.16F * alpha));
         draw.a(matrices, x, y, width, height, height / 2.0F, 0.5F, alpha(accent(), 0.45F * alpha));
      }

      Fonts.d.b(matrices, label, x + width / 2.0F, Fonts.d.a(label, size, y + height / 2.0F), size, alpha(filled ? -1 : accent(), alpha));
      return width;
   }

   public static float logo(class_4587 matrices, float x, float y, float size, float alpha) {
      Fonts.e.a(matrices, "Westra", x, y, size, alpha(-1, alpha));
      float brandWidth = Fonts.e.a("Westra", size);
      float gap = size * 0.3F;
      Fonts.e.a(matrices, GradientUtil.a("Recode", ColorUtil.a(accent(), -1, 0.55F), 3.0F, 1.2F), x + brandWidth + gap, y, size, 0.0F, alpha);
      return brandWidth + gap + Fonts.e.a("Recode", size);
   }

   public static float logoWidth(float size) {
      return Fonts.e.a("Westra", size) + size * 0.3F + Fonts.e.a("Recode", size);
   }

   public static float time() {
      return (float)(System.currentTimeMillis() % 1000000L) / 1000.0F;
   }
}
