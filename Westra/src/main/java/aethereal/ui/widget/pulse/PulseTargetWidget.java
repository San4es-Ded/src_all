package aethereal.ui.widget.pulse;

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
import java.util.Locale;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_3966;
import net.minecraft.class_408;
import net.minecraft.class_742;

public class PulseTargetWidget extends Widget implements Interface {
   private static final class_1304[] g = new class_1304[]{class_1304.field_6166, class_1304.field_6172, class_1304.field_6174, class_1304.field_6169};
   private static final float h = 67.0F;
   private static final float i = 19.0F;
   private static final float j = 11.0F;
   private static final float k = 3.5F;
   private static final float l = 2.5F;
   private static final float n = 8.0F;
   private static final float o = 6.75F;
   private static final float p = 0.65F;
   private static final float s = 7.0F;
   private static final float t = 11.0F;
   private class_1309 u;
   private float v;
   private final AnimationUtil[] w = new AnimationUtil[4];
   private final BooleanSetting x = new BooleanSetting("Показывать экипировку", true);
   private final BooleanSetting y = new BooleanSetting("Свечение полосы", true);

   public PulseTargetWidget() {
      super(new DragInfo("Таргет-худ", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.x, this.y});

      for (int slot = 0; slot < 4; slot++) {
         this.w[slot] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      if (this.u != null && !(animation <= 0.0F)) {
         class_1309 target = this.u;
         String name = target.method_5477().getString();
         float x = this.j().a();
         float y = this.j().b();
         this.j().c(67.0F);
         float health = target.method_29504() ? 0.0F : target.method_6032();
         this.v = MathUtil.c(this.v, health, 0.2F);
         PulseCard.b(event, x, y, 67.0F, 19.0F, 3.0F, animation);
         float headX = x + 3.5F;
         float headY = y + 2.5F;
         if (target instanceof class_742 player) {
            int white = ColorUtil.a(-1, animation);
            int texture = aM_.method_1531().method_4619(player.method_52814().comp_1626()).method_4624();
            event.d().a(event.h(), headX, headY, 11.0F, 11.0F, 2.0F, white, 0.125F, 0.125F, 0.125F, 0.125F, texture);
            event.d().a(event.h(), headX, headY, 11.0F, 11.0F, 2.0F, white, 0.625F, 0.125F, 0.125F, 0.125F, texture);
         }

         if (target.field_6235 > 0) {
            event.d()
               .a(event.h(), headX, headY, 11.0F, 11.0F, 1.5F, ColorUtil.a(ColorUtil.a(255, 70, 70, 255), target.field_6235 / 10.0F * 0.5F * animation * 0.61F));
         }

         float textX = headX + 11.0F + 3.5F;
         Fonts.e.a(event.h(), name, textX, headY, 8.0F, ColorUtil.a(-1, animation));
         String value = String.format(Locale.ROOT, "HP / %.1f", Math.max(0.0F, this.v)).replace('.', ',');
         Fonts.e.a(event.h(), value, textX, headY + 6.0F, 6.75F, ColorUtil.a(PulseCard.g, animation));
         float barY = y + 19.0F - 4.5F;
         float barWidth = 60.0F;
         event.d().a(event.h(), headX, barY, barWidth, 0.65F, 0.325F, ColorUtil.a(PulseCard.e, 0.86F * animation));
         float ratio = MathUtil.b(this.v / Math.max(1.0F, target.method_6063()), 0.0F, 1.0F);
         if (ratio > 0.0F) {
            float filled = Math.max(0.65F, barWidth * ratio);
            if (this.y.c()) {
               event.d().a(event.h(), headX - 0.6F, barY - 0.6F, filled + 1.2F, 1.85F, 0.925F, PulseCard.a(0.24F * animation));
            }

            event.d().a(event.h(), headX, barY, filled, 0.65F, 0.325F, PulseCard.a(animation));
         }

         float gearWidth = 3.5F;

         for (int slot = 0; slot < 4; slot++) {
            class_1799 stack = target.method_6118(g[slot]);
            this.w[slot].a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            this.w[slot].a(!stack.method_7960());
            gearWidth += 9.0F * this.w[slot].c();
         }

         if (gearWidth > 4.5F && this.x.c()) {
            float gearY = y + 19.0F + 2.0F;
            PulseCard.a(event, x, gearY, 67.0F, 11.0F, 3.0F, animation);
            float itemX = x + (67.0F - (gearWidth - 3.5F + 7.0F)) / 2.0F;

            for (int slot = 3; slot >= 0; slot--) {
               class_1799 stack = target.method_6118(g[slot]);
               float value2 = this.w[slot].c();
               if (value2 > 0.001F && !stack.method_7960()) {
                  event.e().a(event.i(), stack, itemX, gearY + 2.0F, 0, animation * value2, 0.4375F, false);
               }

               itemX += 9.0F * value2;
            }

            this.j().d(gearY + 11.0F - y);
         } else {
            this.j().d(19.0F);
         }

         super.a(event);
      } else {
         this.j().c(67.0F);
         this.j().d(19.0F);
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
         if (this.u != target) {
            this.v = target.method_6032();
         }

         this.u = target;
      }

      this.d().a(visible);
      if (!visible && this.d().a() <= 0.0F) {
         this.u = null;
      }

      super.a(event);
   }
}
