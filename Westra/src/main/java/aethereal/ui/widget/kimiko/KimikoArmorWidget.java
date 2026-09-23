package aethereal.ui.widget.kimiko;

import aethereal.config.ThemeInfo;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import net.minecraft.class_1799;

public class KimikoArmorWidget extends Widget implements Interface {
   private static final float g = 16.0F;
   private static final float h = 20.0F;
   private static final float i = 6.0F;
   private static final float j = 7.0F;
   private static final float k = 2.0F;
   private static final float l = 5.5F;
   private final BooleanSetting m = new BooleanSetting("Полоска прочности", true);
   private final BooleanSetting n = new BooleanSetting("Процент прочности", true);
   private final BooleanSetting o = new BooleanSetting("Предмет в руке", true);
   private final AnimationUtil[] p = new AnimationUtil[5];

   public KimikoArmorWidget() {
      super(new DragInfo("Броня", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.m, this.n, this.o});

      for (int slot = 0; slot < this.p.length; slot++) {
         this.p[slot] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      int count = this.o.c() ? 5 : 4;
      float row = 0.0F;

      for (int slot = 0; slot < this.p.length; slot++) {
         class_1799 stack = this.q(slot);
         this.p[slot].a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         this.p[slot].a(slot < count && !stack.method_7960());
         row += 20.0F * this.p[slot].c();
      }

      boolean percent = this.n.c();
      float height = 22.0F + (this.m.c() ? 3.5F : 0.0F) + (percent ? Fonts.c.a(5.5F) + 1.0F : 0.0F) + 6.0F;
      float width = Math.max(row + 12.0F - 4.0F, 40.0F);
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(width);
      this.j().d(height);
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         this.a(event, x, y, width, height, true, animation);
         float itemX = x + 6.0F;

         for (int slot = this.p.length - 1; slot >= 0; slot--) {
            class_1799 stack = this.q(slot);
            float value = this.p[slot].c();
            if (value > 0.002F && !stack.method_7960()) {
               event.e().a(event.i(), stack, itemX, y + 6.0F, 0, animation * value, 1.0F, false);
               if (stack.method_7963() && stack.method_7936() > 0) {
                  float ratio = 1.0F - (float)stack.method_7919() / stack.method_7936();
                  float barY = y + 6.0F + 16.0F + 2.0F;
                  int color = this.r(ratio);
                  if (this.m.c()) {
                     event.d()
                        .a(event.h(), itemX, barY, 16.0F, 2.0F, 1.0F, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), 0.4F * animation * value));
                     event.d().a(event.h(), itemX, barY, 16.0F * ratio, 2.0F, 1.0F, ColorUtil.a(color, animation * value));
                  }

                  if (percent) {
                     String text = Math.round(ratio * 100.0F) + "%";
                     Fonts.c
                        .a(
                           event.h(),
                           text,
                           itemX + (16.0F - Fonts.c.a(text, 5.5F)) / 2.0F,
                           barY + 2.0F + 1.5F,
                           5.5F,
                           ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), animation * value)
                        );
                  }
               }
            }

            itemX += 20.0F * value;
         }

         super.a(event);
      }
   }

   private class_1799 q(int slot) {
      if (aM_.field_1724 == null) {
         return class_1799.field_8037;
      } else {
         return slot == 4 ? aM_.field_1724.method_6047() : aM_.field_1724.method_31548().method_5438(36 + slot);
      }
   }

   private int r(float ratio) {
      int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
      return ratio > 0.5F ? accent : ColorUtil.a(ColorUtil.a(235, 64, 64, 255), accent, ratio / 0.5F);
   }
}
