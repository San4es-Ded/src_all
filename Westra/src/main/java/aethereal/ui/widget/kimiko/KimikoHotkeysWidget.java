package aethereal.ui.widget.kimiko;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.class_408;
import net.minecraft.class_4587;

public class KimikoHotkeysWidget extends Widget implements Interface {
   private static final float g = 7.2F;
   private static final float h = 8.7F;
   private static final float i = 1.5F;
   private static final float j = 3.0F;
   private final BooleanSetting k = new BooleanSetting("Радужная волна", true);
   private final BooleanSetting l = new BooleanSetting("Показывать клавишу", true);

   public KimikoHotkeysWidget() {
      super(new DragInfo("Клавиши", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.k, this.l});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      List<Module> shown = new ArrayList<>();

      for (Module module : Westra.h().d().t().e()) {
         module.f().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         if (module.f().c() > 0.0F) {
            shown.add(module);
         }
      }

      shown.sort(Comparator.comparingDouble(modulex -> -Fonts.c.a(this.q(modulex), 7.2F)));
      float width = 0.0F;
      float height = 0.0F;

      for (Module modulex : shown) {
         width = Math.max(width, 3.0F + Fonts.c.a(this.q(modulex), 7.2F));
         height += 8.7F * modulex.f().c();
      }

      float target = MathUtil.c(this.j().f(), Math.max(width, 1.0F), 0.4F);
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(target);
      this.j().d(Math.max(height, 8.7F));
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         class_4587 matrices = event.h();
         int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         float rowY = y;

         for (int index = 0; index < shown.size(); index++) {
            Module modulex = shown.get(index);
            float value = modulex.f().c() * animation;
            if (!(value <= 0.002F)) {
               String text = this.q(modulex);
               float rowWidth = 3.0F + Fonts.c.a(text, 7.2F);
               float rowHeight = 8.7F * modulex.f().c();
               float rowX = x + target - rowWidth;
               this.a(event, rowX, rowY, rowWidth, rowHeight, false, value);
               int color = this.k.c()
                  ? ColorUtil.a(ColorUtil.a(255, 255, 255, 255), accent, (float)(Math.sin(System.currentTimeMillis() / 400.0 + index * 0.35) * 0.5 + 0.5))
                  : ColorUtil.a(255, 255, 255, 255);
               Fonts.c.a(matrices, text, rowX + 1.5F, rowY + (rowHeight - Fonts.c.a(7.2F)) / 2.0F - 0.25F, 7.2F, ColorUtil.a(color, value));
               rowY += rowHeight;
            }
         }

         super.a(event);
      }
   }

   private String q(Module module) {
      return this.l.c() && module.p() != -1 ? module.j() + " [" + KeyUtil.b(module.p()) + "]" : module.j();
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = aM_.field_1755 instanceof class_408;

      for (Module module : Westra.h().d().t().e()) {
         module.f().a(module.m());
         if (module.f().c() > 0.0F) {
            visible = true;
         }
      }

      this.d().a(visible);
      super.a(event);
   }
}
