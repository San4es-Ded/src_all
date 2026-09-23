package aethereal.ui.widget.system;

import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import net.minecraft.class_1799;
import net.minecraft.class_408;

public class SystemArmorWidget extends Widget implements Interface {
   private static final float g = 14.0F;
   private static final float h = 16.5F;
   private static final float i = 2.5F;
   private static final float j = 21.0F;
   private final AnimationUtil[] k = new AnimationUtil[4];
   private final BooleanSetting l = new BooleanSetting("Полоска прочности", true);

   public SystemArmorWidget() {
      super(new DragInfo("Броня", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.l});

      for (int slot = 0; slot < 4; slot++) {
         this.k[slot] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float x = this.j().a();
      float y = this.j().b();
      float animation = this.a();
      float width = 2.5F;

      for (int slot = 0; slot < 4; slot++) {
         class_1799 stack = aM_.field_1724 == null ? class_1799.field_8037 : aM_.field_1724.method_31548().method_5438(36 + slot);
         this.k[slot].a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         this.k[slot].a(!stack.method_7960());
         width += 16.5F * this.k[slot].c();
      }

      width += 0.0F;
      this.j().c(Math.max(width, 5.0F));
      this.j().d(21.0F);
      if (animation <= 0.0F) {
         super.a(event);
      } else {
         this.a(event, x, y, this.j().f(), 21.0F, false, animation);
         float itemX = x + 2.5F;

         for (int slot = 3; slot >= 0; slot--) {
            class_1799 stack = aM_.field_1724 == null ? class_1799.field_8037 : aM_.field_1724.method_31548().method_5438(36 + slot);
            float value = this.k[slot].c();
            if (!(value <= 0.001F)) {
               if (!stack.method_7960()) {
                  event.e().a(event.i(), stack, itemX, y + 2.5F, 0, animation * value, 0.875F, false);
                  if (stack.method_7963() && this.l.c()) {
                     float ratio = 1.0F - (float)stack.method_7919() / stack.method_7936();
                     float barY = y + 2.5F + 14.0F + 1.0F;
                     event.d().a(event.h(), itemX, barY, 14.0F, 1.0F, 0.5F, ColorUtil.a(ColorUtil.a(255, 255, 255, 255), 0.2F * animation * value));
                     event.d().a(event.h(), itemX, barY, 14.0F * ratio, 1.0F, 0.5F, ColorUtil.a(a(ratio), animation * value));
                  }
               }

               itemX += 16.5F * value;
            }
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
