package aethereal.ui.widget.westra;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.notification.Notification;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.recode.RecodeKit;
import aethereal.ui.widget.NotificationWidget;
import net.minecraft.class_1799;
import net.minecraft.class_2561;

public class WestraNotificationWidget extends NotificationWidget implements Interface {
   private static final float E = 12.0F;
   private static final float l = 7.0F;
   private static final float p = 6.25F;
   private static final float D = 6.5F;

   @Override
   protected void k(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float contentY = this.j().b();
      float height = 17.0F;

      for (Notification notification : Westra.h().d().m().b()) {
         float animation = notification.a().c() * this.a();
         if (!(animation <= 0.0F)) {
            Object message = notification.c();
            boolean item = notification.d() instanceof class_1799;
            float textSize = 6.5F;
            float textWidth = message instanceof class_2561 text ? Fonts.d.a(text, textSize) : Fonts.d.a(String.valueOf(message), textSize);
            float width = 20.0F + textWidth + 8.0F;
            float x = (aM_.method_22683().method_4486() - width) / 2.0F;
            float appear = EasingList.p.ease(notification.a().c());
            float y = contentY - (1.0F - appear) * 8.0F;
            float scale = 0.92F + 0.08F * appear;
            event.h().method_22903();
            event.h().method_46416(x + width / 2.0F, y + height / 2.0F, 0.0F);
            event.h().method_22905(scale, scale, 1.0F);
            event.h().method_46416(-(x + width / 2.0F), -(y + height / 2.0F), 0.0F);
            WestraStyle.d(event, x, y, width, height, animation);
            if (item) {
               event.e().a(event.i(), (class_1799)notification.d(), x + 3.0F, y + (height - 11.0F) / 2.0F, 0, animation, 0.6875F, false);
            } else {
               WestraStyle.e(event, String.valueOf(notification.d()), x + 3.0F, y + 3.0F, 11.0F, animation);
            }

            float textX = x + 3.0F + 11.0F + 6.0F;
            float textY = Fonts.d.a("A", textSize, y + height / 2.0F);
            if (message instanceof class_2561 textx) {
               Fonts.d.a(event.h(), textx, textX, textY, textSize, (double)animation);
            } else {
               Fonts.d.a(event.h(), String.valueOf(message), textX, textY, textSize, ColorUtil.a(-1, animation));
            }

            float left = 1.0F - Math.min(1.0F, (float)notification.b().e() / Math.max(1, notification.f()));
            if (left > 0.0F) {
               float barW = (width - height) * left;
               event.d().a(event.h(), x + height / 2.0F, y + height - 1.5F, barW, 1.0F, 0.5F, ColorUtil.a(RecodeKit.accent(), 0.8F * animation));
            }

            event.h().method_22909();
            this.j().a(x);
            this.j().c(width);
            this.j().d(height);
            contentY += (height + 3.0F) * animation;
         }
      }
   }
}
