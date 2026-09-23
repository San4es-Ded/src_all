package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.module.misc.StreamerMode;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScissorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.ServerUtil;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_3966;
import net.minecraft.class_408;
import net.minecraft.class_742;

public class TargetWidget extends Widget {
   private final BooleanSetting f = new BooleanSetting("Визуализация предметов", true);
   private final BooleanSetting g = new BooleanSetting("Отображать при наводке", false);
   private final AnimationUtil h = new AnimationUtil();
   private final AnimationUtil i = new AnimationUtil();
   private class_1309 j;
   private String k = "";

   public TargetWidget() {
      super(new DragInfo("Таргет-худ", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
      this.a(new Setting[]{this.g, this.f});
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      if (this.a() > 0.0F && this.j != null) {
         this.j().c(100.0F);
         this.j().d(24.0F);
         float x = this.j().a();
         float y = this.j().b();
         this.a(event, x, y, this.j().f(), this.j().g(), true, this.a());
         float headSize = this.j().g() / 1.35F;
         float headY = y + (this.j().g() - headSize) / 2.0F;
         if (this.j instanceof class_742 player) {
            event.d()
               .a(
                  event.h(),
                  x + 5.0F,
                  headY,
                  headSize,
                  headSize,
                  2.0F,
                  ColorUtil.a(-1, this.a()),
                  0.125F,
                  0.125F,
                  0.125F,
                  0.125F,
                  Interface.aM_.method_1531().method_4619(player.method_52814().comp_1626()).method_4624()
               );
         } else if (this.j != null) {
            Fonts.a.a(event.h(), "B", x + 6.5F + (headSize - 24.0F) / 2.0F, headY + (headSize - 24.0F) / 2.0F, 24.0F, ColorUtil.a(-1, this.a()));
         }

         float textX = x + 5.0F + headSize + 5.0F;
         StreamerMode streamerMode = Westra.h().d().t().aE();
         String string;
         if (streamerMode.m() && streamerMode.r().c()) {
            string = streamerMode.a(this.j.method_5477().getString());
         } else {
            string = this.j.method_5477().getString();
         }

         if (string.length() > 12) {
            Fonts.e.c(event.h(), string, textX, headY, 7.5F, ColorUtil.a(-1, this.a()), Fonts.e.a(string.substring(0, 12), 7.5F));
         } else {
            Fonts.e.a(event.h(), string, textX, headY, 7.5F, ColorUtil.a(-1, this.a()));
         }

         if (this.f.c()) {
            int i = 0;

            for (class_1799 stack : new class_1799[]{
               this.j.method_6118(class_1304.field_6166),
               this.j.method_6118(class_1304.field_6172),
               this.j.method_6118(class_1304.field_6174),
               this.j.method_6118(class_1304.field_6169),
               this.j.method_6079(),
               this.j.method_6047()
            }) {
               if (!stack.method_7960()) {
                  event.e().a(event.i(), InventoryUtil.a(stack), x + this.j().f() - 10.0F - i * 9, y + this.j().g(), 0, this.a(), 0.55F, true);
                  i++;
               }
            }
         }

         int primary = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
         String hpValue = String.valueOf((int)ServerUtil.a.a(this.j));
         if (this.k.isEmpty()) {
            this.k = hpValue;
         }

         float progress = hpValue.equals(this.k) ? 1.0F : this.i.a(0.0F, 1.0F, 0.75F);
         this.a(event, hpValue, this.k, x + this.j().f() - 5.0F - Fonts.e.a(hpValue, 7.0F), headY + 0.5F, 7.0F, primary, progress);
         if (progress >= 0.99F) {
            this.k = hpValue;
            this.i.c(0.0F);
         }

         float targetHP = MathUtil.b(MathUtil.b(ServerUtil.a.a(this.j), 0.0F, this.j.method_6063()) / this.j.method_6063(), 0.0F, 1.0F);
         float lineHP = this.h.a(targetHP, targetHP, 0.5F);
         float alpha = Westra.h().d().o().a(ThemeInfo.BACKGROUND_HUD).b() * this.a();
         event.d().a(event.h(), textX, headY + 12.5F, 54.0F, 3.0F, 0.5F, ColorUtil.a(ColorUtil.b(primary, 0.3F), this.a()));
         event.d().a(event.h(), textX, headY + 12.5F, 54.0F * lineHP, 3.0F, 0.5F, ColorUtil.a(primary, alpha));
      }

      super.a(event);
   }

   private void a(DrawEvent event, String current, String previous, float right, float y, float size, int color, float progress) {
      float height = Fonts.e.a(size);
      float cursor = right + Fonts.e.a(current, size);

      for (int i = 0; i < current.length(); i++) {
         char digit = current.charAt(current.length() - 1 - i);
         char old = i < previous.length() ? previous.charAt(previous.length() - 1 - i) : 32;
         String value = String.valueOf(digit);
         cursor -= Fonts.e.a(value, size);
         if (digit != old && !(progress >= 1.0F)) {
            ScissorUtil.a(event.h(), cursor - 0.5F, y, Fonts.e.a(value, size) + 1.0F, height + 1.0F);
            Fonts.e.a(event.h(), String.valueOf(old), cursor, y - height * progress, size, ColorUtil.a(color, (1.0F - progress) * this.a()));
            Fonts.e.a(event.h(), value, cursor, y + height * (1.0F - progress), size, ColorUtil.a(color, progress * this.a()));
            ScissorUtil.a(event.h());
         } else {
            Fonts.e.a(event.h(), value, cursor, y, size, ColorUtil.a(color, this.a()));
         }
      }
   }

   @Override
   public void a(GlobalEvent event) {
      class_1309 targets = Westra.h().d().t().B().s() != null ? Westra.h().d().t().B().s() : Westra.h().d().t().X().s();
      class_1309 class_1309Var;
      if (this.g.c()) {
         class_3966 class_3966Var = Interface.aM_.field_1765 instanceof class_3966 ? (class_3966)Interface.aM_.field_1765 : null;
         if (class_3966Var instanceof class_3966) {
            class_1309 class_1309VarMethod_17782 = class_3966Var.method_17782() instanceof class_1309 ? (class_1309)class_3966Var.method_17782() : null;
            if (class_1309VarMethod_17782 instanceof class_1657) {
               class_1309 class_1309Var3 = (class_1657)class_1309VarMethod_17782;
               if (class_1309Var3 != Interface.aM_.field_1724) {
                  class_1309Var = class_1309Var3;
               } else {
                  class_1309Var = null;
               }
            } else {
               class_1309Var = null;
            }
         } else {
            class_1309Var = null;
         }
      } else {
         class_1309Var = null;
      }

      class_1309 class_1309Var2;
      if (targets != null) {
         class_1309Var2 = targets;
      } else if (class_1309Var != null) {
         class_1309Var2 = class_1309Var;
      } else {
         class_1309Var2 = Interface.aM_.field_1755 instanceof class_408 ? Interface.aM_.field_1724 : null;
      }

      boolean visible = class_1309Var2 != null;
      if (class_1309Var2 != null) {
         this.j = class_1309Var2;
      }

      this.d().a(visible);
      if (!visible && this.d().a() <= 0.0F) {
         this.j = null;
      }

      super.a(event);
   }
}
