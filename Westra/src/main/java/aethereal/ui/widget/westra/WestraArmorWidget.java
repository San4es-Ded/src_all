package aethereal.ui.widget.westra;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import net.minecraft.class_1799;
import net.minecraft.class_408;

public class WestraArmorWidget extends WestraWidget implements Interface {
   private static final float E = 14.0F;
   private static final float F = 18.0F;
   private static final float G = 3.0F;
   private final AnimationUtil[] H = new AnimationUtil[4];
   private final BooleanSetting I = new BooleanSetting("Полоска прочности", true);
   private final BooleanSetting J = new BooleanSetting("Процент прочности", true);

   public WestraArmorWidget() {
      super("Броня");
      this.a(new Setting[]{this.I, this.J});

      for (int slot = 0; slot < 4; slot++) {
         this.H[slot] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      float row = 0.0F;

      for (int slot = 0; slot < 4; slot++) {
         class_1799 stack = aM_.field_1724 == null ? class_1799.field_8037 : aM_.field_1724.method_31548().method_5438(36 + slot);
         this.H[slot].a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         this.H[slot].a(!stack.method_7960());
         row += 18.0F * this.H[slot].c();
      }

      boolean percent = this.J.c();
      float height = 17.0F + (this.I.c() ? 2.5F : 0.0F) + (percent ? Fonts.c.a(5.5F) + 1.0F : 0.0F) + 3.0F;
      float width = Math.max(7.0F + row + 2.5F, 13.5F);
      float x = this.j().a();
      float y = this.j().b();
      this.j().c(width);
      this.j().d(height);
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         this.q(event, x, y, width, height, animation, true);
         float itemX = x + 7.0F;

         for (int slot = 3; slot >= 0; slot--) {
            class_1799 stack = aM_.field_1724 == null ? class_1799.field_8037 : aM_.field_1724.method_31548().method_5438(36 + slot);
            float value = this.H[slot].c();
            if (value > 0.002F && !stack.method_7960()) {
               event.e().a(event.i(), stack, itemX, y + 3.0F, 0, animation * value, 0.875F, false);
               if (stack.method_7963() && stack.method_7936() > 0) {
                  float ratio = 1.0F - (float)stack.method_7919() / stack.method_7936();
                  float barY = y + 3.0F + 14.0F + 1.5F;
                  if (this.I.c()) {
                     event.d()
                        .a(event.h(), itemX, barY, 14.0F, 1.0F, 0.5F, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), 0.5F * animation * value));
                     event.d().a(event.h(), itemX, barY, 14.0F * ratio, 1.0F, 0.5F, ColorUtil.a(a(ratio), animation * value));
                  }

                  if (percent) {
                     String text = Math.round(ratio * 100.0F) + "%";
                     Fonts.c
                        .a(
                           event.h(),
                           text,
                           itemX + (14.0F - Fonts.c.a(text, 5.5F)) / 2.0F,
                           barY + 2.5F,
                           5.5F,
                           ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), animation * value)
                        );
                  }
               }
            }

            itemX += 18.0F * value;
         }

         super.a(event);
      }
   }

   private static int a(float ratio) {
      if (ratio > 0.5F) {
         return ColorUtil.a(110, 220, 130, 255);
      } else {
         return ratio > 0.25F ? ColorUtil.a(240, 200, 90, 255) : ColorUtil.a(235, 90, 90, 255);
      }
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = aM_.field_1755 instanceof class_408;
      if (aM_.field_1724 != null) {
         for (int slot = 0; slot < 4; slot++) {
            if (!aM_.field_1724.method_31548().method_5438(36 + slot).method_7960()) {
               visible = true;
               break;
            }
         }
      }

      this.d().a(visible);
      super.a(event);
   }
}
