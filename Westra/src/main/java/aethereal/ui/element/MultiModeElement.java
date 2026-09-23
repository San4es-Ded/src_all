package aethereal.ui.element;

import aethereal.api.Compile;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.MathUtil;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class MultiModeElement extends Element_2<MultiModeSetting> {
   private final AnimationUtil[] d;

   @Compile
   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      Vector4f vector4f = this.a;
      MultiModeSetting setting = this.b;
      if (button != 0) {
         if (button != 2 || !MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
            return false;
         } else if (!(setting instanceof MultiModeSetting)) {
            throw new ClassCastException();
         } else {
            List<BooleanSetting> listC = setting.c();
            if (!(listC instanceof List)) {
               throw new ClassCastException();
            } else {
               listC.forEach(new Consumer() {
                  @Override
                  public void accept(Object obj) {
                     ((Setting)obj).b();
                  }
               });
               return true;
            }
         }
      } else {
         float f = vector4f.x;
         float fA = vector4f.y + Fonts.c.a(6.5F) + 5.0F;
         if (!(setting instanceof MultiModeSetting)) {
            throw new ClassCastException();
         } else {
            List<BooleanSetting> listC2 = setting.c();
            if (!(listC2 instanceof List)) {
               throw new ClassCastException();
            } else {
               for (BooleanSetting booleanSetting : listC2) {
                  if (!(booleanSetting instanceof BooleanSetting)) {
                     throw new ClassCastException();
                  }

                  float fA2 = Fonts.d.a(booleanSetting.i(), 5.9F) + 10.0F;
                  if (f + fA2 > vector4f.x + vector4f.z) {
                     f = vector4f.x;
                     fA += 13.0F;
                  }

                  if (MathUtil.a(mouseX, mouseY, f, fA, fA2, 10.0F)) {
                     Boolean boolC = booleanSetting.c();
                     if (!(boolC instanceof Boolean)) {
                        throw new ClassCastException();
                     }

                     booleanSetting.a(!boolC);
                     return true;
                  }

                  f += fA2 + 3.0F;
               }

               return false;
            }
         }
      }
   }

   public MultiModeElement(MultiModeSetting setting) {
      super(setting);
      this.d = new AnimationUtil[setting.c().size()];

      for (int i = 0; i < this.d.length; i++) {
         this.d[i] = new AnimationUtil();
      }
   }

   @Override
   public void a(class_332 context, double mouseX, double mouseY, float delta, float extend) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      long selectedCount = this.b.c().stream().filter(Setting::c).count();
      String counter = selectedCount + " из " + this.b.c().size();
      float counterWidth = Fonts.d.a(counter, 5.75F);
      boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0F;
      this.a(
         matrices,
         Fonts.c,
         this.b.i(),
         this.a.x,
         this.a.y,
         Fonts.c.a(6.5F) + 1.0F,
         6.5F,
         RecodeKit.text(),
         this.a.z - counterWidth - 4.0F,
         hovered,
         extend,
         delta
      );
      Fonts.d.a(matrices, counter, this.a.x + this.a.z - counterWidth, this.a.y + 0.75F, 5.75F, RecodeKit.alpha(RecodeKit.accent(), extend));
      float x = this.a.x;
      float y = this.a.y + Fonts.c.a(6.5F) + 5.0F;
      int accent = RecodeKit.accent();
      int i = 0;

      for (BooleanSetting mode : this.b.c()) {
         float width = Fonts.d.a(mode.i(), 5.9F) + 10.0F;
         if (x + width > this.a.x + this.a.z) {
            x = this.a.x;
            y += 13.0F;
         }

         this.d[i].a(mode.c());
         this.d[i].a(0.0F, 1.0F, 0.3F, EasingList.i, delta);
         float value = this.d[i].c();
         draw.a(matrices, x, y, width, 10.0F, 5.0F, RecodeKit.alpha(-1, 0.04F * (1.0F - value) * extend));
         if (value > 0.01F) {
            draw.a(matrices, x, y, width, 10.0F, 5.0F, RecodeKit.alpha(accent, 0.22F * value * extend));
         }

         draw.a(matrices, x, y, width, 10.0F, 5.0F, 0.5F, RecodeKit.alpha(ColorUtil.a(-1, accent, value), (0.07F + 0.4F * value) * extend));
         int color = ColorUtil.a(RecodeKit.dim(), -1, value);
         Fonts.d.b(matrices, mode.i(), x + width / 2.0F, Fonts.d.a(mode.i(), 5.9F, y + 5.0F), 5.9F, RecodeKit.alpha(color, extend));
         x += width + 3.0F;
         i++;
      }

      this.a.w = y + 10.0F - this.a.y;
   }

   static {
      NativeMethodLookup.lookup(MultiModeElement.class, 12);
   }
}
