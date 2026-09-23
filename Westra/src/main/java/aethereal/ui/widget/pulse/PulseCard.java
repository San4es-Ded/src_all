package aethereal.ui.widget.pulse;

import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import lombok.Generated;

public class PulseCard {
   public static final int a = ColorUtil.a(120, 88, 255, 255);
   public static final int b = ColorUtil.a(15, 15, 26, 232);
   public static final int c = ColorUtil.a(9, 9, 18, 236);
   public static final int d = ColorUtil.a(15, 15, 18, 255);
   public static final int e = ColorUtil.a(14, 12, 22, 220);
   public static final int f = ColorUtil.a(230, 230, 238, 238);
   public static final int g = ColorUtil.a(190, 190, 205, 240);

   @Generated
   private PulseCard() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void a(DrawEvent event, float x, float y, float width, float height, float radius, float animation) {
      event.d().a(event.h(), x - 0.5F, y - 0.5F, width + 1.0F, height + 1.0F, radius + 0.5F, ColorUtil.a(ColorUtil.a(0, 0, 0, 255), 0.27F * animation));
      int top = ColorUtil.a(b, 0.91F * animation);
      int bottom = ColorUtil.a(c, 0.925F * animation);
      event.d().a(event.h(), x, y, width, height, radius, top, top, bottom, bottom);
   }

   public static void b(DrawEvent event, float x, float y, float width, float height, float radius, float animation) {
      event.d().a(event.h(), x, y, width, height, radius, ColorUtil.a(ColorUtil.a(0, 0, 0, 255), 0.27F * animation));
      event.d().a(event.h(), x, y, width, height, radius, ColorUtil.a(d, animation));
   }

   public static int a(float alpha) {
      return ColorUtil.a(a, alpha);
   }
}
