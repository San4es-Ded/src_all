package aethereal.ui.widget.system;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import net.minecraft.class_408;
import net.minecraft.class_4587;

public class SystemHotkeysWidget extends Widget implements Interface {
   private static final float g = 12.5F;
   private static final float h = 11.5F;
   private static final float i = 7.0F;
   private static final float j = 5.0F;
   private final BooleanSetting k = new BooleanSetting("Показывать заголовок", true);

   public SystemHotkeysWidget() {
      super(new DragInfo("Клавиши", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.k});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float x = this.j().a();
      float y = this.j().b();
      float animation = this.a();
      float target = 10.0F + Fonts.e.a("KeyBinds", 7.0F);
      boolean active = false;

      for (Module module : Westra.h().d().t().e()) {
         if (module.p() != -1 && module.f().c() > 0.0F) {
            active = true;
            target = Math.max(target, 10.0F + Fonts.e.a(module.j(), 7.0F) + 8.0F + Fonts.e.a(KeyUtil.b(module.p()), 7.0F));
         }
      }

      float width = MathUtil.c(this.j().f(), target, 0.5F);
      this.j().c(width);
      if (animation <= 0.0F) {
         this.j().d(12.5F);
         super.a(event);
      } else {
         if (this.k.c()) {
            this.a(event, x, y, width, 12.5F, false, animation);
         }

         Fonts.e.a(event.h(), "KeyBinds", x + 5.0F, y + (12.5F - Fonts.e.a(7.0F)) / 2.0F - 0.5F, 7.0F, ColorUtil.a(-1, animation));
         float rowY = y + 12.5F + 1.0F;
         class_4587 matrices = event.h();

         for (Module modulex : Westra.h().d().t().e()) {
            modulex.f().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            float row = modulex.p() != -1 ? modulex.f().c() * animation : 0.0F;
            if (!(row <= 0.0F)) {
               float height = 11.5F * row;
               float scale = 0.95F + 0.05F * EasingList.g.ease(row);
               float centerX = x + width / 2.0F;
               float centerY = rowY + height / 2.0F;
               matrices.method_22903();
               matrices.method_46416(centerX, centerY, 0.0F);
               matrices.method_22905(scale, scale, 1.0F);
               matrices.method_46416(-centerX, -centerY, 0.0F);
               this.a(event, x, rowY, width, height, false, row);
               float textY = rowY + (height - Fonts.e.a(7.0F)) / 2.0F - 0.5F;
               Fonts.e.a(matrices, modulex.j(), x + 5.0F, textY, 7.0F, ColorUtil.a(-1, row));
               String bind = KeyUtil.b(modulex.p());
               Fonts.e.a(matrices, bind, x + width - 5.0F - Fonts.e.a(bind, 7.0F), textY, 7.0F, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), row));
               matrices.method_22909();
               rowY += 12.5F * row;
            }
         }

         this.j().d(active ? rowY - y - 1.0F : 12.5F);
         super.a(event);
      }
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
