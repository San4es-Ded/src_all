package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.setting.SliderSetting;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class SliderElement extends Element_2<SliderSetting> {
   private boolean d;

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      Vector4f vector4f = this.a;
      SliderSetting setting = this.b;
      if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y + Fonts.c.a(6.5F), vector4f.z, 11.0F)) {
         return false;
      } else if (button == 0) {
         this.d = true;
         this.a(mouseX);
         return true;
      } else if (button != 2) {
         return false;
      } else if (!(setting instanceof SliderSetting)) {
         throw new ClassCastException();
      } else {
         setting.b();
         return true;
      }
   }

   @Compile
   @Override
   public boolean b(double mouseX, double mouseY, int button) {
      this.d = false;
      return false;
   }

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, double amount) {
      SliderSetting setting = this.b;
      Vector4f vector4f = this.a;
      if (!(setting instanceof SliderSetting)) {
         throw new ClassCastException();
      } else if (setting.e && MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.y + Fonts.c.a(6.5F) + 9.5F - vector4f.y)) {
         Float fC = setting.c();
         if (!(fC instanceof Float)) {
            throw new ClassCastException();
         } else {
            setting.a(MathUtil.b(Math.round((fC + (float)Math.signum(amount) * setting.c) / setting.c) * setting.c, setting.a, setting.b));
            return true;
         }
      } else {
         return false;
      }
   }

   public SliderElement(SliderSetting setting) {
      super(setting);
      this.a.w = 22.0F;
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      this.a.w = 18.0F;
      if (this.d) {
         this.a(mouseX);
      }

      this.b().c(MathUtil.c(this.b().a(), (this.b.c() - this.b.a) / (this.b.b - this.b.a), 1.0F));
      float progress = this.b().a();
      boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0F;
      float current = this.b.a + (this.b.b - this.b.a) * progress;
      String value = this.b.c % 1.0F == 0.0F ? String.valueOf(Math.round(current)) : String.valueOf(Math.round(current * 100.0F) / 100.0F);
      float chipHeight = 9.0F;
      float chipWidth = Fonts.d.a(value, 5.75F) + 8.0F;
      float chipX = this.a.x + this.a.z - chipWidth;
      int accent = RecodeKit.accent();
      this.a(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, chipHeight, 6.5F, RecodeKit.text(), chipX - this.a.x - 4.0F, hovered, extend, delta);
      draw.a(matrices, chipX, this.a.y, chipWidth, chipHeight, chipHeight / 2.0F, RecodeKit.alpha(accent, (this.d ? 0.3F : 0.16F) * extend));
      draw.a(matrices, chipX, this.a.y, chipWidth, chipHeight, chipHeight / 2.0F, 0.5F, RecodeKit.alpha(accent, 0.4F * extend));
      Fonts.d.b(matrices, value, chipX + chipWidth / 2.0F, Fonts.d.a(value, 5.75F, this.a.y + chipHeight / 2.0F), 5.75F, RecodeKit.alpha(-1, extend));
      float trackY = this.a.y + 13.0F;
      float trackH = 3.0F;
      draw.a(matrices, this.a.x, trackY, this.a.z, trackH, trackH / 2.0F, RecodeKit.alpha(-1, 0.07F * extend));
      float fill = Math.max(trackH, this.a.z * progress);
      int left = RecodeKit.alpha(accent, extend);
      int right = RecodeKit.alpha(RecodeKit.accentShade(), extend);
      draw.a(matrices, this.a.x, trackY, fill, trackH, trackH / 2.0F, left, right, left, right);
      float knob = !hovered && !this.d ? 7.0F : 8.0F;
      float knobX = this.a.x + (this.a.z - knob) * progress;
      float knobY = trackY + trackH / 2.0F - knob / 2.0F;
      draw.a(matrices, knobX - 1.5F, knobY - 1.5F, knob + 3.0F, knob + 3.0F, (knob + 3.0F) / 2.0F, RecodeKit.alpha(accent, 0.25F * extend));
      draw.a(matrices, knobX, knobY, knob, knob, knob / 2.0F, RecodeKit.alpha(-1, extend));
   }

   private void a(double mouseX) {
      float progress = MathUtil.b((float)(mouseX - this.a.x) / this.a.z, 0.0F, 1.0F);
      float value = this.b.a + (this.b.b - this.b.a) * progress;
      this.b.a(MathUtil.b(Math.round(value / this.b.c) * this.b.c, this.b.a, this.b.b));
   }

   static {
      NativeMethodLookup.lookup(SliderElement.class, 13);
   }
}
