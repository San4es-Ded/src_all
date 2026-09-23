package aethereal.ui.widget;

import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.widget.westra.WestraStyle;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class WestraEnvironmentWidget extends EnvironmentWidget implements Interface {
   private static final float k = 12.0F;
   private static final float l = 7.0F;
   private static final float m = 13.0F;

   @Override
   protected void k(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      float x = this.j().a();
      float y = this.j().b();
      float header = 15.0F;
      Stream<UUID> stream = this.h.stream();
      Map<UUID, EnvironmentWidget.b> map = this.g;
      List<EnvironmentWidget.b> shown = stream.map(map::get).filter(Objects::nonNull).toList();
      String count = String.valueOf(shown.size());
      float target = 20.0F + Fonts.d.a("Окружение", 6.5F) + 6.0F + WestraStyle.f(count) + 5.0F;

      for (EnvironmentWidget.b data : shown) {
         target = Math.max(target, 21.0F + Fonts.d.a(data.d, 6.25F) + 6.0F + 40.0F + WestraStyle.f("20") + 5.0F);
      }

      float width = MathUtil.c(this.j().f(), Math.max(90.0F, target), 0.35F);
      float height = header + shown.size() * 13.0F + 3.0F;
      this.j().c(width);
      this.j().d(height);
      if (!(animation <= 0.0F)) {
         WestraStyle.a(event, x, y, width, height, animation, false);
         WestraStyle.e(event, "L", x + 4.0F, y + (header - 9.0F) / 2.0F, 9.0F, animation);
         Fonts.d.a(event.h(), "Окружение", x + 17.0F, Fonts.d.a("Окружение", 6.5F, y + header / 2.0F), 6.5F, ColorUtil.a(-1, animation));
         WestraStyle.f(event, count, x + width - 4.5F, y + header / 2.0F, -1, animation);
         if (!shown.isEmpty()) {
            event.d().a(event.h(), x + 5.0F, y + header - 0.25F, width - 10.0F, 0.5F, 0.0F, ColorUtil.a(-1, 0.07F * animation));
         }

         float rowY = y + header;

         for (EnvironmentWidget.b data : shown) {
            this.a(event, data, x, rowY, width, animation);
            rowY += 13.0F;
         }
      }
   }

   @Override
   protected float a(DrawEvent event, EnvironmentWidget.b data, float x, float y, float width, float animation) {
      String health = String.valueOf((int)data.f);
      float head = 10.0F;
      float center = y + 6.5F;
      event.d()
         .a(
            event.h(),
            x + 7.0F,
            center - head / 2.0F,
            head,
            head,
            3.0F,
            ColorUtil.a(-1, animation),
            0.125F,
            0.125F,
            0.125F,
            0.125F,
            aM_.method_1531().method_4619(data.e).method_4624()
         );
      float right = x + width - 4.5F;
      right -= WestraStyle.f(event, health, right, center, a(data.f), animation) + 3.0F;
      if (this.f.c()) {
         for (int slot = 3; slot >= 0; slot--) {
            if (!data.c[slot].method_7960()) {
               right -= 9.5F;
               event.e().a(event.i(), InventoryUtil.a(data.c[slot]), right, center - 4.5F, 0, animation, 0.5625F, false);
            }
         }
      }

      float nameX = x + 7.0F + head + 4.0F;
      Fonts.d.c(event.h(), data.d, nameX, Fonts.d.a("A", 6.25F, center), 6.25F, ColorUtil.a(-1, animation), right - 3.0F - nameX);
      return 13.0F;
   }

   private static int a(float health) {
      if (health > 14.0F) {
         return ColorUtil.a(110, 220, 130, 255);
      } else {
         return health > 7.0F ? ColorUtil.a(240, 200, 90, 255) : ColorUtil.a(235, 90, 90, 255);
      }
   }
}
