package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.ButtonSetting;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class ButtonElement extends Element_2<ButtonSetting> {
   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      if (button != 0) {
         return false;
      } else {
         Vector4f vector4f = this.a;
         ButtonSetting setting = this.b;
         if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
            return false;
         } else if (!(setting instanceof ButtonSetting)) {
            throw new ClassCastException();
         } else {
            setting.k();
            return true;
         }
      }
   }

   public ButtonElement(ButtonSetting setting) {
      super(setting);
      this.a.w = 14.0F;
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      this.b().a(MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0F);
      this.b().a(0.0F, 1.0F, 0.3F, EasingList.i, delta);
      float hover = this.b().c();
      int left = RecodeKit.alpha(RecodeKit.accent(), (0.22F + 0.2F * hover) * extend);
      int right = RecodeKit.alpha(RecodeKit.accentShade(), (0.12F + 0.2F * hover) * extend);
      draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 5.0F, left, right, left, right);
      draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 5.0F, 0.5F, RecodeKit.alpha(RecodeKit.accent(), (0.4F + 0.3F * hover) * extend));
      Fonts.d.b(matrices, this.b.i(), this.a.x + this.a.z / 2.0F, Fonts.d.a(this.b.i(), 6.5F, this.a.y + this.a.w / 2.0F), 6.5F, RecodeKit.alpha(-1, extend));
   }

   static {
      NativeMethodLookup.lookup(ButtonElement.class, 9);
   }
}
