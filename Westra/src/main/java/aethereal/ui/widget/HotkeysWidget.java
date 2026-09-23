package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.element.DragInfo;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import net.minecraft.class_408;

public class HotkeysWidget extends Widget implements Interface {
   public HotkeysWidget() {
      super(new DragInfo("Клавиши", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float x = this.j().a();
      float y = this.j().b();
      float targetWidth = 14.5F + Fonts.e.a("Hot-keys", this.e) + 5.0F + 2.0F;
      float contentY = y + this.d + 3.0F;
      float rightWidth = Fonts.a.a("Q", 6.5F);
      boolean active = false;

      for (Module module : Westra.h().d().t().e()) {
         if (module.p() != -1 && module.f().c() > 0.0F) {
            active = true;
            targetWidth = Math.max(
               targetWidth, 19.0F + Fonts.e.a(module.j(), 6.5F) + 8.0F + Fonts.e.a(KeyUtil.b(module.p()), 6.5F) + 4.0F + rightWidth + 5.0F + 2.0F
            );
         }
      }

      float width = MathUtil.c(this.j().f(), targetWidth, 0.5F);
      this.j().c(width);
      this.a(event, "Q", "Hot-keys", width, this.a());

      for (Module module2 : Westra.h().d().t().e()) {
         module2.f().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         float animation = module2.p() != -1 ? module2.f().c() * this.a() : 0.0F;
         if (animation > 0.0F) {
            float offsetX = -8.0F * (1.0F - animation);
            float offsetY = -(1.0F - animation);
            float drawY = contentY + offsetY;
            float bindWidth = Fonts.e.a(KeyUtil.b(module2.p()), 6.5F);
            float rightIconX = x + offsetX + width - 5.0F - rightWidth - 1.0F;
            float textY = drawY + (11.5F - Fonts.e.a(6.5F)) / 2.0F - 0.5F;
            this.a(event, x + offsetX, drawY, width, 11.5F, false, animation);
            this.a(event, x + offsetX + 15.0F, drawY, 11.5F, animation);
            Fonts.a
               .a(
                  event.h(),
                  module2.l().a(),
                  x + offsetX + 5.0F,
                  drawY + (11.5F - Fonts.a.a(6.5F)) / 2.0F - 0.15F,
                  6.5F,
                  ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), animation)
               );
            Fonts.e.a(event.h(), module2.j(), x + offsetX + 19.0F, textY, 6.5F, ColorUtil.a(-1, animation));
            Fonts.e.a(event.h(), KeyUtil.b(module2.p()), rightIconX - 4.0F - bindWidth, textY, 6.5F, ColorUtil.a(-1, 0.55F * animation));
            Fonts.a
               .a(
                  event.h(),
                  "C",
                  rightIconX,
                  drawY + (11.5F - Fonts.a.a(6.5F)) / 2.0F + 0.15F,
                  6.5F,
                  ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), animation)
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

      for (Module module : Westra.h().d().t().e()) {
         if (module.p() != -1 && module.f().c() > 0.0F) {
            visible = true;
            break;
         }
      }

      this.d().a(visible);
      super.a(event);
   }
}
