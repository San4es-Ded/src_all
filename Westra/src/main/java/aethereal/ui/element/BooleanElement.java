package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class BooleanElement extends Element_2<BooleanSetting> {
   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      Vector4f vector4f = this.a;
      BooleanSetting setting = this.b;
      if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
         return false;
      } else if (button != 0) {
         if (button != 2) {
            return false;
         } else if (!(setting instanceof BooleanSetting)) {
            throw new ClassCastException();
         } else {
            setting.b();
            return true;
         }
      } else if (!(setting instanceof BooleanSetting)) {
         throw new ClassCastException();
      } else {
         Boolean boolC = setting.c();
         if (!(boolC instanceof Boolean)) {
            throw new ClassCastException();
         } else {
            setting.a(!boolC);
            return true;
         }
      }
   }

   public BooleanElement(BooleanSetting setting) {
      super(setting);
      this.a.w = 11.0F;
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      class_4587 matrices = context.method_51448();
      this.b().a(this.b.c());
      this.b().a(0.0F, 1.0F, 0.5F, EasingList.i, delta);
      float on = this.b().c();
      float centerY = this.a.y + this.a.w / 2.0F;
      boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0F;
      float toggleW = 14.0F;
      float toggleH = 8.0F;
      int labelColor = ColorUtil.a(RecodeKit.dim(), RecodeKit.text(), 0.55F + 0.45F * Math.max(on, hovered ? 1.0F : 0.0F));
      this.a(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, this.a.w, 6.5F, labelColor, this.a.z - toggleW - 4.0F, hovered, extend, delta);
      RecodeKit.toggle(matrices, this.a.x + this.a.z - toggleW, centerY - toggleH / 2.0F, toggleW, toggleH, on, extend);
   }

   @Override
   public void a(DrawEvent event, float x, float y, float width, float animation) {
      this.b().a(this.b.c());
      this.b().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      ThemeProcessor theme = Westra.h().d().o();
      float textX = x + 19.5F;
      float toggleX = x + width - 11.0F - 5.0F;
      float toggleY = y + 2.25F;
      int primary = theme.a(ThemeInfo.PRIMARY).a();
      Fonts.a.a(event.h(), "g", x + 5.0F, y + (12.0F - Fonts.a.a(6.5F)) / 2.0F, 6.5F, ColorUtil.a(primary, animation));
      event.d().a(event.i().method_51448(), x + 15.5F, y + 3.0F, 0.75F, 6.0F, 0.0F, ColorUtil.a(ColorUtil.a(200, 200, 200, 255), 0.5F * animation));
      Fonts.e.a(event.h(), this.b.i(), textX, y + (12.0F - Fonts.e.a(6.5F)) / 2.0F - 0.5F, 6.5F, ColorUtil.a(-1, animation));
      float value = this.b().c();
      event.d().a(event.h(), toggleX, toggleY, 11.0F, 7.5F, 2.5F, ColorUtil.a(primary, value * animation));
      event.d()
         .a(
            event.h(),
            toggleX,
            toggleY,
            11.0F,
            7.5F,
            2.5F,
            0.3F,
            ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b() * animation)
         );
      event.d()
         .a(
            event.h(),
            toggleX + 1.5F + 3.5F * value,
            toggleY + 1.5F,
            4.5F,
            4.5F,
            1.25F,
            ColorUtil.a(ColorUtil.a(ColorUtil.a(150, 150, 155, 255), ColorUtil.a(255, 255, 255, 255), value), animation)
         );
   }

   static {
      NativeMethodLookup.lookup(BooleanElement.class, 8);
   }
}
