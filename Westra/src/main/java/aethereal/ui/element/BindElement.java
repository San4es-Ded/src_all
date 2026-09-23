package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScissorUtil;
import aethereal.setting.BindSetting;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class BindElement extends Element_2<BindSetting> {
   private boolean d;

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      Vector4f vector4f = this.a;
      BindSetting setting = this.b;
      if (this.d) {
         if (!(setting instanceof BindSetting)) {
            throw new ClassCastException();
         } else {
            int code = button >= 0 && button <= 7 ? -100 + button : -100;
            setting.a(code);
            this.d = false;
            return true;
         }
      } else if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
         return false;
      } else if (button == 0) {
         this.d = true;
         return true;
      } else if (button != 2) {
         return false;
      } else if (!(setting instanceof BindSetting)) {
         throw new ClassCastException();
      } else {
         setting.b();
         return true;
      }
   }

   @Compile
   @Override
   public boolean a(int keyCode, int scanCode, int modifiers) {
      if (!this.d) {
         return false;
      } else {
         BindSetting setting = this.b;
         if (!(setting instanceof BindSetting)) {
            throw new ClassCastException();
         } else {
            int code = keyCode != KeyUtil.ESC.a() && keyCode != KeyUtil.BACKSPACE.a() ? keyCode : -1;
            setting.a(code);
            this.d = false;
            return true;
         }
      }
   }

   public BindElement(BindSetting setting) {
      super(setting);
      this.a.w = 11.0F;
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      this.b().a(this.d);
      this.b().a(0.0F, 1.0F, 0.4F, EasingList.p, delta);
      float centerY = this.a.y + this.a.w / 2.0F;
      boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0F;
      float anim = this.b().c();
      float reverse = 1.0F - anim;
      String value = this.b.c() == -1 ? "Нет" : KeyUtil.b(this.b.c());
      float total = Fonts.d.a(value, 5.9F) * reverse + Fonts.d.a("...", 5.9F) * anim;
      float boxWidth = total + 10.0F;
      float boxHeight = 10.0F;
      float boxX = this.a.x + this.a.z - boxWidth;
      float boxY = centerY - boxHeight / 2.0F;
      int accent = RecodeKit.accent();
      this.a(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, this.a.w, 6.5F, RecodeKit.text(), boxX - this.a.x - 4.0F, hovered, extend, delta);
      draw.a(matrices, boxX, boxY, boxWidth, boxHeight, 5.0F, RecodeKit.alpha(accent, (0.14F + 0.2F * anim) * extend));
      draw.a(matrices, boxX, boxY, boxWidth, boxHeight, 5.0F, 0.5F, RecodeKit.alpha(accent, (0.35F + 0.4F * anim) * extend));
      ScissorUtil.a(matrices, boxX, boxY, boxWidth, boxHeight);
      if (reverse > 0.0F) {
         Fonts.d.a(matrices, value, boxX + 5.0F, Fonts.d.a(value, 5.9F, centerY), 5.9F, RecodeKit.alpha(-1, extend * reverse));
      }

      if (anim > 0.0F) {
         Fonts.d.a(matrices, "...", boxX + 5.0F, Fonts.d.a("...", 5.9F, centerY), 5.9F, RecodeKit.alpha(-1, extend * anim));
      }

      ScissorUtil.a(matrices);
   }

   static {
      NativeMethodLookup.lookup(BindElement.class, 7);
   }
}
