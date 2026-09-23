package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.ModeSetting;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class ModeElement extends Element_2<ModeSetting> {
   private final AnimationUtil[] d;

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      Vector4f vector4f = this.a;
      ModeSetting setting = this.b;
      if (button != 0) {
         if (button != 2 || !MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
            return false;
         } else if (!(setting instanceof ModeSetting)) {
            throw new ClassCastException();
         } else {
            setting.b();
            return true;
         }
      } else {
         float f = vector4f.x;
         float fA = vector4f.y + Fonts.c.a(6.5F) + 5.0F;
         if (!(setting instanceof ModeSetting)) {
            throw new ClassCastException();
         } else {
            for (String str : setting.k()) {
               if (!(str instanceof String)) {
                  throw new ClassCastException();
               }

               float fA2 = Fonts.d.a(str, 5.9F) + 10.0F;
               if (f + fA2 > vector4f.x + vector4f.z) {
                  f = vector4f.x;
                  fA += 13.0F;
               }

               if (MathUtil.a(mouseX, mouseY, f, fA, fA2, 10.0F)) {
                  setting.a(str);
                  return true;
               }

               f += fA2 + 3.0F;
            }

            return false;
         }
      }
   }

   public ModeElement(ModeSetting setting) {
      super(setting);
      this.d = new AnimationUtil[setting.k().size()];

      for (int i = 0; i < this.d.length; i++) {
         this.d[i] = new AnimationUtil();
      }
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      Fonts.c.a(matrices, this.b.i(), this.a.x, this.a.y, 6.5F, RecodeKit.alpha(RecodeKit.text(), extend));
      float x = this.a.x;
      float y = this.a.y + Fonts.c.a(6.5F) + 5.0F;
      int accent = RecodeKit.accent();
      int i = 0;

      for (String mode : this.b.k()) {
         float width = Fonts.d.a(mode, 5.9F) + 10.0F;
         if (x + width > this.a.x + this.a.z) {
            x = this.a.x;
            y += 13.0F;
         }

         this.d[i].a(this.b.l(mode));
         this.d[i].a(0.0F, 1.0F, 0.3F, EasingList.i, delta);
         float value = this.d[i].c();
         draw.a(matrices, x, y, width, 10.0F, 5.0F, RecodeKit.alpha(-1, 0.04F * (1.0F - value) * extend));
         if (value > 0.01F) {
            int left = RecodeKit.alpha(accent, 0.85F * value * extend);
            int right = RecodeKit.alpha(RecodeKit.accentShade(), 0.85F * value * extend);
            draw.a(matrices, x, y, width, 10.0F, 5.0F, left, right, left, right);
         }

         draw.a(matrices, x, y, width, 10.0F, 5.0F, 0.5F, RecodeKit.alpha(-1, (0.07F + 0.06F * value) * extend));
         int color = ColorUtil.a(RecodeKit.dim(), -1, value);
         Fonts.d.b(matrices, mode, x + width / 2.0F, Fonts.d.a(mode, 5.9F, y + 5.0F), 5.9F, RecodeKit.alpha(color, extend));
         x += width + 3.0F;
         i++;
      }

      this.a.w = y + 10.0F - this.a.y;
   }

   static {
      NativeMethodLookup.lookup(ModeElement.class, 11);
   }
}
