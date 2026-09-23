package aethereal.ui.element;

import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.util.MathUtil;
import lombok.Generated;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5250;

public class Button {
   private final AnimationUtil a = new AnimationUtil();
   private final float b;
   private final float c;
   private final String d;
   private final Runnable e;
   private float f;
   private float g;

   @Generated
   public AnimationUtil a() {
      return this.a;
   }

   @Generated
   public float b() {
      return this.b;
   }

   @Generated
   public float c() {
      return this.c;
   }

   @Generated
   public String d() {
      return this.d;
   }

   @Generated
   public Runnable e() {
      return this.e;
   }

   @Generated
   public float f() {
      return this.f;
   }

   @Generated
   public float g() {
      return this.g;
   }

   public Button(float width, float height, String label, Runnable action) {
      this.b = width;
      this.c = height;
      this.d = label;
      this.e = action;
   }

   public void a(float x, float y) {
      this.f = x;
      this.g = y;
   }

   public void a(class_332 context, int mouseX, int mouseY, float delta, float open) {
      this.a.a(this.e != null && MathUtil.a(mouseX, mouseY, this.f, this.g, this.b, this.c));
      this.a.a(0.0F, 1.0F, 0.35F, EasingList.i, delta);
      float hover = Math.min(1.0F, this.a.c() / 0.9F);
      float scale = (0.85F + 0.15F * EasingList.s.ease(open)) * (1.0F + 0.03F * hover);
      class_4587 matrices = context.method_51448();
      float cx = this.f + this.b / 2.0F;
      float cy = this.g + this.c / 2.0F;
      matrices.method_22903();
      matrices.method_46416(cx, cy, 0.0F);
      matrices.method_22905(scale, scale, 1.0F);
      matrices.method_46416(-cx, -cy, 0.0F);
      Draw2DProcessor draw = Westra.h().d().i();
      draw.b(matrices, this.f, this.g, this.b, this.c, 8.0F, ColorUtil.a(11, 11, 13, 120), open);
      draw.a(matrices, this.f, this.g, this.b, this.c, 8.0F, 0.5F, ColorUtil.a(255, 255, 255, (int)(hover * 20.0F * open)));
      if (this.d != null) {
         float time = (float)(System.currentTimeMillis() % 3000L) / 3000.0F;
         class_5250 class_2561VarMethod_43470 = class_2561.method_43470("");

         for (int i = 0; i < this.d.length(); i++) {
            float wave = (float)(Math.sin((time + i * 0.5F / this.d.length()) * 3.141592654293742 * 2.0) * 0.5 + 0.5);
            int c = (int)(180.0F + 65.0F * wave * hover);
            class_2561VarMethod_43470.method_10852(
               class_2561.method_43470(String.valueOf(this.d.charAt(i))).method_10862(class_2583.field_24360.method_36139(c << 16 | c << 8 | c))
            );
         }

         float labelW = Fonts.e.a(this.d, 8.0F);
         Fonts.e.a(matrices, class_2561VarMethod_43470, this.f + (this.b - labelW) / 2.0F, this.g + (this.c - 9.0F) / 2.0F, 8.0F, 0.0F, open);
      }

      matrices.method_22909();
   }
}
