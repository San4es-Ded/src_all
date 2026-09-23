package aethereal.ui.element;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.util.MathUtil;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import org.joml.Quaternionf;

public class BottomBar {
   private final float[] a = new float[3];
   private boolean b;
   private float c;
   private float d;
   private final List<Button> e;

   public BottomBar(Button... buttons) {
      this.e = Arrays.asList(buttons);
   }

   public void a(int screenWidth, int screenHeight) {
      float buttonsRowY = screenHeight * 0.85F;
      this.c = (screenWidth - 79.0F) / 2.0F;
      this.d = buttonsRowY - 19.5F - 5.0F;
      this.a(screenWidth, buttonsRowY, 5.0F, this.e.toArray(new Button[0]));
   }

   public void a(class_332 context, int mouseX, int mouseY, float delta) {
      this.a(context, mouseX, mouseY, delta, 1.0F);
   }

   public void a(class_332 context, int mouseX, int mouseY, float delta, float alpha) {
      this.a(context, alpha);

      for (Button button : this.e) {
         button.a(context, mouseX, mouseY, delta, alpha);
      }
   }

   private void a(class_332 context, float alpha) {
      float[] fArr = this.a;
      fArr[1] += (this.a[0] - this.a[1]) * 0.25F;
      Draw2DProcessor draw = Westra.h().d().i();
      class_4587 matrices = context.method_51448();
      float knobX = this.c + 1.75F + this.a[1] * Math.max(1.0F, 59.5F);
      float knobY = this.d + 1.75F;
      float cx = knobX + 8.0F;
      float cy = knobY + 8.0F;
      draw.b(matrices, this.c, this.d, 79.0F, 19.5F, 8.0F, ColorUtil.a(11, 11, 13, (int)(150.0F * alpha)), 1.0F);
      draw.a(matrices, this.c, this.d, 79.0F, 19.5F, 8.0F, 0.5F, ColorUtil.a(255, 255, 255, (int)(15.0F * alpha)));
      float textX = this.c + 9.0F;
      float visibleWidth = knobX - 3.0F - textX;
      Fonts.e.c(matrices, "Выйти из игры", textX, this.d + 6.25F - 0.5F, 7.0F, ColorUtil.a(ColorUtil.a(220, 80, 80, 255), this.a[1] * alpha), visibleWidth);
      int knob = ColorUtil.a(ColorUtil.a(255, 255, 255, 13), ColorUtil.a(220, 80, 80, 40), this.a[1]);
      draw.a(matrices, knobX, knobY, 16.0F, 16.0F, 7.0F, ColorUtil.a(knob, ColorUtil.b(knob)[3] / 255.0F * alpha));
      matrices.method_22903();
      matrices.method_46416(cx, cy, 0.0F);
      matrices.method_22907(new Quaternionf().rotateZ((float)Math.toRadians(-90.0F + 180.0F * this.a[1])));
      matrices.method_46416(-cx, -cy, 0.0F);
      Fonts.a
         .a(
            matrices,
            "c",
            cx - Fonts.a.a("c", 8.5F) / 2.0F + 1.0F,
            cy - 4.5F,
            8.5F,
            ColorUtil.a(ColorUtil.a(-1, ColorUtil.a(220, 80, 80, 255), this.a[1]), alpha)
         );
      matrices.method_22909();
   }

   public boolean a(double mouseX, double mouseY) {
      float knobX = this.c + 1.75F + this.a[0] * Math.max(1.0F, 59.5F);
      float knobY = this.d + 1.75F;
      if (MathUtil.a(mouseX, mouseY, knobX, knobY, 16.0F, 16.0F)) {
         this.a[2] = (float)mouseX - knobX;
         this.b = true;
         return true;
      } else {
         for (Button button : this.e) {
            if (button.e() != null && MathUtil.a(mouseX, mouseY, button.f(), button.g(), button.b(), button.c())) {
               button.e().run();
               return true;
            }
         }

         return false;
      }
   }

   public boolean a(double mouseX) {
      if (this.b) {
         this.a[0] = class_3532.method_15363(((float)mouseX - this.a[2] - this.c - 1.75F) / Math.max(1.0F, 59.5F), 0.0F, 1.0F);
         return false;
      } else {
         return false;
      }
   }

   public boolean a() {
      if (this.b) {
         this.b = false;
         this.a[0] = this.a[0] >= 0.95F ? this.a[0] : 0.0F;
         if (this.a[0] > 0.0F) {
            Interface.aM_.method_1592();
            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void a(float screenWidth, float y, float gap, Button... row) {
      float totalWidth = -gap;

      for (Button button : row) {
         totalWidth += button.b() + gap;
      }

      float x = (screenWidth - totalWidth) / 2.0F;

      for (Button button2 : row) {
         button2.a(x, y);
         x += button2.b() + gap;
      }
   }
}
