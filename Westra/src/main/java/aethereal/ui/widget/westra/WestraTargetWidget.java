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
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_3966;
import net.minecraft.class_408;
import net.minecraft.class_742;

public class WestraTargetWidget extends WestraWidget implements Interface {
   private static final float E = 20.0F;
   private static final float F = 2.0F;
   private static final float G = 3.5F;
   private static final float H = 11.0F;
   private static final class_1304[] I = new class_1304[]{class_1304.field_6166, class_1304.field_6172, class_1304.field_6174, class_1304.field_6169};
   private final AnimationUtil[] J = new AnimationUtil[4];
   private final BooleanSetting K = new BooleanSetting("Показывать экипировку", true);
   private final BooleanSetting L = new BooleanSetting("Показывать дистанцию", true);
   private final BooleanSetting P = new BooleanSetting("Предмет во второй руке", true);
   private class_1309 M;
   private float N;
   private float O;

   public WestraTargetWidget() {
      super("Таргет-худ");
      this.a(new Setting[]{this.K, this.L, this.P});

      for (int slot = 0; slot < 4; slot++) {
         this.J[slot] = new AnimationUtil();
      }
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      class_1309 target = this.M;
      float avatar = 24.0F;
      float pad = 5.0F;
      if (target != null && !(animation <= 0.0F)) {
         String name = target.method_5477().getString();
         String health = String.format("%.1f", Math.max(0.0F, this.N));
         String distance = aM_.field_1724 == null ? "" : String.format("%.1f м", aM_.field_1724.method_5739(target));
         boolean showDistance = this.L.c() && !distance.isEmpty();
         float row = 0.0F;

         for (int slot = 0; slot < 4; slot++) {
            class_1799 stack = target.method_6118(I[slot]);
            this.J[slot].a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            this.J[slot].a(!stack.method_7960() && this.K.c());
            row += 13.0F * this.J[slot].c();
         }

         class_1799 hand = target.method_6079();
         boolean showHand = this.P.c() && !hand.method_7960();
         boolean items = row > 0.0F || showHand;
         float textWidth = Fonts.d.a(name, 7.0F) + 6.0F + WestraStyle.f(health);
         float target2 = pad + avatar + 6.0F + Math.max(textWidth, 70.0F) + pad;
         float width = MathUtil.c(this.j().f(), Math.max(120.0F, target2), 0.3F);
         float height = pad * 2.0F + avatar + (items ? 15.0F : 0.0F);
         float x = this.j().a();
         float y = this.j().b();
         float current = target.method_29504() ? 0.0F : target.method_6032();
         this.N = MathUtil.c(this.N, current, 0.2F);
         this.O = MathUtil.c(this.O, this.N, 0.05F);
         this.j().c(width);
         this.j().d(height);
         this.q(event, x, y, width, height, animation, false);
         int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         float avatarX = x + pad;
         float avatarY = y + pad;
         if (target instanceof class_742 player) {
            event.d()
               .a(
                  event.h(),
                  avatarX,
                  avatarY,
                  avatar,
                  avatar,
                  6.0F,
                  ColorUtil.a(-1, animation),
                  0.125F,
                  0.125F,
                  0.125F,
                  0.125F,
                  aM_.method_1531().method_4619(player.method_52814().comp_1626()).method_4624()
               );
         } else {
            event.d().a(event.h(), avatarX, avatarY, avatar, avatar, 6.0F, ColorUtil.a(accent, 0.25F * animation));
         }

         float hurt = target.field_6235 / 10.0F;
         if (hurt > 0.0F) {
            event.d().a(event.h(), avatarX, avatarY, avatar, avatar, 6.0F, ColorUtil.a(ColorUtil.a(255, 60, 60, 255), 0.45F * hurt * animation));
         }

         event.d().a(event.h(), avatarX, avatarY, avatar, avatar, 6.0F, 0.5F, ColorUtil.a(accent, 0.5F * animation));
         float textX = avatarX + avatar + 6.0F;
         float right = x + width - pad;
         float nameCenter = avatarY + 5.0F;
         Fonts.d.c(event.h(), name, textX, Fonts.d.a(name, 7.0F, nameCenter), 7.0F, ColorUtil.a(-1, animation), right - textX - WestraStyle.f(health) - 4.0F);
         WestraStyle.f(event, health, right, nameCenter, -1, animation);
         float barY = avatarY + 12.5F;
         float barW = right - textX;
         float barH = 4.0F;
         float maxHealth = Math.max(1.0F, target.method_6063());
         event.d().a(event.h(), textX, barY, barW, barH, barH / 2.0F, ColorUtil.a(-1, 0.07F * animation));
         float ghost = MathUtil.b(this.O / maxHealth, 0.0F, 1.0F);
         if (ghost > 0.0F) {
            event.d().a(event.h(), textX, barY, Math.max(barH, barW * ghost), barH, barH / 2.0F, ColorUtil.a(-1, 0.28F * animation));
         }

         float ratio = MathUtil.b(this.N / maxHealth, 0.0F, 1.0F);
         if (ratio > 0.0F) {
            int left = ColorUtil.a(accent, animation);
            int rightColor = ColorUtil.a(RecodeKit.accentShade(), animation);
            event.d().a(event.h(), textX, barY, Math.max(barH, barW * ratio), barH, barH / 2.0F, left, rightColor, left, rightColor);
         }

         if (showDistance) {
            Fonts.c.a(event.h(), distance, textX, barY + barH + 2.0F, 5.5F, ColorUtil.a(Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(), animation));
         }

         if (items) {
            float itemY = avatarY + avatar + 3.0F;
            float itemX = x + pad;
            if (showHand) {
               event.e().a(event.i(), hand, itemX, itemY, 0, animation, 0.6875F, true);
               itemX += 15.0F;
            }

            for (int slot = 3; slot >= 0; slot--) {
               class_1799 stack = target.method_6118(I[slot]);
               float value = this.J[slot].c();
               if (value > 0.002F && !stack.method_7960()) {
                  event.e().a(event.i(), stack, itemX, itemY, 0, animation * value, 0.6875F, false);
               }

               itemX += 13.0F * value;
            }
         }

         super.a(event);
      } else {
         this.j().c(Math.max(this.j().f(), 120.0F));
         this.j().d(avatar + pad * 2.0F);
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
         if (this.M != target) {
            this.N = target.method_6032();
            this.O = this.N;
         }

         this.M = target;
      }

      this.d().a(visible);
      if (!visible && this.d().a() <= 0.0F) {
         this.M = null;
      }

      super.a(event);
   }
}
