package aethereal.ui.widget.kimiko;

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
import aethereal.ui.element.DragInfo;
import aethereal.ui.widget.Widget;
import aethereal.util.MathUtil;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_3966;
import net.minecraft.class_408;
import net.minecraft.class_742;

public class KimikoTargetWidget extends Widget implements Interface {
   private static final float g = 6.0F;
   private static final float h = 22.0F;
   private static final float i = 7.0F;
   private static final float j = 3.0F;
   private static final float k = 8.0F;
   private static final float l = 6.0F;
   private static final float m = 74.0F;
   private static final float n = 34.0F;
   private static final class_1304[] o = new class_1304[]{class_1304.field_6166, class_1304.field_6172, class_1304.field_6174, class_1304.field_6169};
   private final BooleanSetting p = new BooleanSetting("Показывать экипировку", true);
   private final AnimationUtil[] q = new AnimationUtil[4];
   private class_1309 r;
   private float s;
   private float t;

   public KimikoTargetWidget() {
      super(new DragInfo("Таргет-худ", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.p});

      for (int slot = 0; slot < 4; slot++) {
         this.q[slot] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      class_1309 target = this.r;
      if (target != null && !(animation <= 0.0F)) {
         String name = target.method_5477().getString();
         float row = 0.0F;

         for (int slot = 0; slot < 4; slot++) {
            class_1799 stack = target.method_6118(o[slot]);
            this.q[slot].a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            this.q[slot].a(!stack.method_7960() && this.p.c());
            row += 11.0F * this.q[slot].c();
         }

         float content = Math.max(74.0F, Fonts.c.a(name, 8.0F) + 24.0F);
         float width = MathUtil.c(this.j().f(), 35.0F + content + 6.0F, 0.3F);
         boolean items = row > 0.0F;
         float height = 34.0F + (items ? 12.0F : 0.0F);
         float x = this.j().a();
         float y = this.j().b();
         this.j().c(width);
         this.j().d(height);
         float health = target.method_29504() ? 0.0F : target.method_6032();
         this.s = MathUtil.c(this.s, health, 0.15F);
         this.t = MathUtil.c(this.t, this.s, 0.08F);
         this.a(event, x, y, width, height, true, animation);
         if (target instanceof class_742 player) {
            event.d()
               .a(
                  event.h(),
                  x + 6.0F,
                  y + 6.0F,
                  22.0F,
                  22.0F,
                  7.0F,
                  ColorUtil.a(-1, animation),
                  0.125F,
                  0.125F,
                  0.125F,
                  0.125F,
                  aM_.method_1531().method_4619(player.method_52814().comp_1626()).method_4624()
               );
         }

         float textX = x + 6.0F + 22.0F + 7.0F;
         Fonts.c.a(event.h(), name, textX, y + 6.0F, 8.0F, ColorUtil.a(-1, animation));
         float barY = y + 6.0F + Fonts.c.a(8.0F) + 3.0F;
         float barWidth = x + width - 6.0F - textX;
         float maxHealth = Math.max(1.0F, target.method_6063());
         int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         event.d().a(event.h(), textX, barY, barWidth, 3.0F, 1.5F, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), 0.45F * animation));
         float ghost = MathUtil.b(this.t / maxHealth, 0.0F, 1.0F);
         if (ghost > 0.0F) {
            event.d().a(event.h(), textX, barY, barWidth * ghost, 3.0F, 1.5F, ColorUtil.a(ColorUtil.a(255, 255, 255, 255), 0.22F * animation));
         }

         float ratio = MathUtil.b(this.s / maxHealth, 0.0F, 1.0F);
         if (ratio > 0.0F) {
            event.d().a(event.h(), textX, barY, barWidth * ratio, 3.0F, 1.5F, ColorUtil.a(accent, animation));
         }

         String value = String.format("%.1f hp", Math.max(0.0F, this.s));
         Fonts.c.a(event.h(), value, textX, barY + 3.0F + 2.0F, 6.0F, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), animation));
         if (items) {
            float itemX = textX;

            for (int slot = 3; slot >= 0; slot--) {
               class_1799 stack = target.method_6118(o[slot]);
               float step = this.q[slot].c();
               if (step > 0.002F && !stack.method_7960()) {
                  event.e().a(event.i(), stack, itemX, y + 34.0F - 2.0F, 0, animation * step, 0.6875F, false);
               }

               itemX += 11.0F * step;
            }
         }

         super.a(event);
      } else {
         this.j().c(Math.max(this.j().f(), 74.0F));
         this.j().d(34.0F);
         super.a(event);
      }
   }

   @Override
   public void a(GlobalEvent event) {
      class_1309 aura = Westra.h().d().t().B().s() != null ? Westra.h().d().t().B().s() : Westra.h().d().t().X().s();
      class_1309 crosshair = null;
      if (aM_.field_1765 instanceof class_3966 hit && hit.method_17782() instanceof class_1657 && hit.method_17782() != aM_.field_1724) {
         crosshair = (class_1309)hit.method_17782();
      }

      class_1309 target = (class_1309)(aura != null ? aura : (crosshair != null ? crosshair : (aM_.field_1755 instanceof class_408 ? aM_.field_1724 : null)));
      boolean visible = target != null;
      if (target != null) {
         if (this.r != target) {
            this.s = target.method_6032();
            this.t = this.s;
         }

         this.r = target;
      }

      this.d().a(visible);
      if (!visible && this.d().a() <= 0.0F) {
         this.r = null;
      }

      super.a(event);
   }
}
