package aethereal.ui.widget.system;

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

public class SystemTargetWidget extends Widget implements Interface {
   private static final float g = 30.0F;
   private static final float h = 2.5F;
   private static final float i = 6.5F;
   private static final float j = 5.0F;
   private static final float k = 17.0F;
   private static final float l = 10.0F;
   private static final class_1304[] q = new class_1304[]{class_1304.field_6166, class_1304.field_6172, class_1304.field_6174, class_1304.field_6169};
   private class_1309 m;
   private float n;
   private float o;
   private final AnimationUtil[] p = new AnimationUtil[4];
   private final BooleanSetting x = new BooleanSetting("Показывать экипировку", true);
   private final BooleanSetting y = new BooleanSetting("Полоса здоровья", true);

   public SystemTargetWidget() {
      super(new DragInfo("Таргет-худ", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.x, this.y});

      for (int slot = 0; slot < 4; slot++) {
         this.p[slot] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      if (this.m != null && !(animation <= 0.0F)) {
         class_1309 target = this.m;
         String name = target.method_5477().getString();
         float width = Math.max(99.5F, 32.5F + Fonts.e.a(name, 6.5F) + 2.5F + 25.0F);
         float x = this.j().a();
         float y = this.j().b();
         this.j().c(width);
         float health = target.method_29504() ? 0.0F : target.method_6032();
         this.n = MathUtil.c(this.n, health, 0.15F);
         this.o = MathUtil.c(this.o, this.n, 0.15F);
         this.a(event, x, y, width, 30.0F, false, animation);
         this.a(event, x, y, 30.0F, 30.0F, false, animation);
         if (target instanceof class_742 player) {
            event.d()
               .a(
                  event.h(),
                  x + 4.0F,
                  y + 4.0F,
                  22.0F,
                  22.0F,
                  4.0F,
                  ColorUtil.a(-1, animation),
                  0.125F,
                  0.125F,
                  0.125F,
                  0.125F,
                  aM_.method_1531().method_4619(player.method_52814().comp_1626()).method_4624()
               );
         }

         float textX = x + 30.0F + 2.5F;
         Fonts.e.a(event.h(), name, textX, y + 2.5F + 1.0F, 6.5F, ColorUtil.a(-1, animation));
         float barWidth = width - 30.0F - 5.0F - 22.0F;
         float maxHealth = Math.max(1.0F, target.method_6063());
         float barY = y + 17.0F;
         if (this.y.c()) {
            event.d().a(event.h(), textX, barY, barWidth, 5.0F, 2.0F, ColorUtil.a(ColorUtil.a(255, 255, 255, 255), 0.12F * animation));
            float ghost = MathUtil.b(this.o / maxHealth, 0.0F, 1.0F);
            if (ghost > 0.0F) {
               event.d().a(event.h(), textX, barY, barWidth * ghost, 5.0F, 2.0F, ColorUtil.a(ColorUtil.a(255, 255, 255, 255), 0.25F * animation));
            }

            float ratio = MathUtil.b(this.n / maxHealth, 0.0F, 1.0F);
            if (ratio > 0.0F) {
               event.d().a(event.h(), textX, barY, barWidth * ratio, 5.0F, 2.0F, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), animation));
            }
         }

         String value = String.format("%.1f", this.n);
         Fonts.e
            .a(event.h(), value, x + width - 2.5F - Fonts.e.a(value, 6.5F), barY + (5.0F - Fonts.e.a(6.5F)) / 2.0F - 0.5F, 6.5F, ColorUtil.a(-1, animation));
         float itemsY = y + 30.0F + 1.0F;
         float itemsWidth = 2.5F;

         for (int slot = 0; slot < 4; slot++) {
            class_1799 stack = target.method_6118(q[slot]);
            this.p[slot].a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            this.p[slot].a(!stack.method_7960());
            itemsWidth += 11.5F * this.p[slot].c();
         }

         if (++itemsWidth > 5.0F && this.x.c()) {
            float itemsX = x + (width - itemsWidth) / 2.0F;
            this.a(event, itemsX, itemsY, itemsWidth, 15.0F, false, animation);
            float itemX = itemsX + 2.5F;

            for (int slot = 3; slot >= 0; slot--) {
               class_1799 stack = target.method_6118(q[slot]);
               float value2 = this.p[slot].c();
               if (value2 > 0.001F && !stack.method_7960()) {
                  event.e().a(event.i(), stack, itemX, itemsY + 2.5F, 0, animation * value2, 0.625F, false);
               }

               itemX += 11.5F * value2;
            }

            this.j().d(itemsY + 10.0F + 5.0F - y);
         } else {
            this.j().d(30.0F);
         }

         super.a(event);
      } else {
         this.j().c(99.5F);
         this.j().d(30.0F);
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
         if (this.m != target) {
            this.n = target.method_6032();
            this.o = this.n;
         }

         this.m = target;
      }

      this.d().a(visible);
      if (!visible && this.d().a() <= 0.0F) {
         this.m = null;
      }

      super.a(event);
   }
}
