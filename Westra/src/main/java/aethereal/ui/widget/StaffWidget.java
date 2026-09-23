package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.staff.StaffConstructor;
import aethereal.ui.element.DragInfo;
import aethereal.util.MathUtil;
import net.minecraft.class_408;
import net.minecraft.class_640;

public class StaffWidget extends Widget implements Interface {
   public StaffWidget() {
      super(new DragInfo("Стафф", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float x = this.j().a();
      float y = this.j().b();
      float targetWidth = 14.5F + Fonts.e.a("Staff-list", this.e) + 5.0F + 2.0F;
      float contentY = y + this.d + 3.0F;
      boolean active = false;

      for (StaffConstructor staff : Westra.h().d().f().a()) {
         if (staff.b().c() > 0.0F) {
            targetWidth = Math.max(
               targetWidth, 19.0F + Fonts.e.a(staff.a(), 6.5F) + 8.0F + Fonts.e.a(this.a(staff.a()) ? "Near" : "Online", 6.5F) + 5.0F + 2.0F
            );
            active = true;
         }
      }

      float width = MathUtil.c(this.j().f(), targetWidth, 0.5F);
      this.j().c(width);
      if (this.a() > 0.0F) {
         this.a(event, "i", "Staff-list", width, this.a());
      }

      for (StaffConstructor staff2 : Westra.h().d().f().a()) {
         AnimationUtil animationUtil = staff2.b();
         animationUtil.a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         float animation = animationUtil.c() * this.a();
         if (animation > 0.0F) {
            float offsetX = -8.0F * (1.0F - animation);
            float offsetY = -(1.0F - animation);
            float drawY = contentY + offsetY;
            float textY = drawY + (11.5F - Fonts.e.a(6.5F)) / 2.0F - 0.5F;
            this.a(event, x + offsetX, drawY, width, 11.5F, false, animation);
            this.a(event, x + offsetX + 15.0F, drawY, 11.5F, animation);
            class_640 entry = aM_.method_1562() == null
               ? null
               : aM_.method_1562().method_2880().stream().filter(e -> e.method_2966().getName().equalsIgnoreCase(staff2.a())).findFirst().orElse(null);
            if (entry != null) {
               event.d()
                  .a(
                     event.h(),
                     x + offsetX + 5.0F,
                     drawY + 2.0F,
                     7.5F,
                     7.5F,
                     2.0F,
                     ColorUtil.a(-1, animation),
                     0.125F,
                     0.125F,
                     0.125F,
                     0.125F,
                     aM_.method_1531().method_4619(entry.method_52810().comp_1626()).method_4624()
                  );
            } else {
               Fonts.a
                  .a(
                     event.h(),
                     "y",
                     x + offsetX + 5.0F,
                     drawY + (11.5F - Fonts.a.a(8.0F)) / 2.0F,
                     8.0F,
                     ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), animation)
                  );
            }

            Fonts.e.a(event.h(), staff2.a(), x + offsetX + 19.0F, textY, 6.5F, ColorUtil.a(-1, animation));
            boolean near = this.a(staff2.a());
            Fonts.e
               .a(
                  event.h(),
                  near ? "Near" : "Online",
                  x + offsetX + width - 5.0F - Fonts.e.a(near ? "Near" : "Online", 6.5F) - 1.0F,
                  textY,
                  6.5F,
                  ColorUtil.a(near ? -1529792 : -9711765, animation)
               );
            contentY += 13.5F * animation;
         }
      }

      this.j().d(active ? contentY - y - 2.0F : this.d);
      super.a(event);
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = aM_.field_1755 instanceof class_408;

      for (StaffConstructor staff : Westra.h().d().f().a()) {
         staff.b()
            .a(
               aM_.method_1562() != null && aM_.method_1562().method_2880().stream().anyMatch(e -> e.method_2966().getName().equalsIgnoreCase(staff.a()))
                  || this.a(staff.a())
            );
         if (staff.b().c() > 0.0F) {
            visible = true;
         }
      }

      this.d().a(visible);
      super.a(event);
   }

   private boolean a(String name) {
      return aM_.field_1687 != null
         && aM_.field_1687.method_18456().stream().anyMatch(playerEntity -> playerEntity.method_5477().getString().equalsIgnoreCase(name));
   }
}
