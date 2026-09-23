package aethereal.ui.shader;

import net.minecraft.class_278;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import org.joml.Vector4f;

public class GradientShader extends Shader {
   public class_278 c;
   public class_278 d;
   public class_278 e;
   public class_278 f;
   public class_278 g;
   public class_278 h;
   public class_278 i;

   public GradientShader() {
      super(class_2960.method_60655("westra", "core/rect/gradient_rect"), class_290.field_1576);
   }

   @Override
   protected void b() {
      this.c = this.a("uSize");
      this.d = this.a("uRadius");
      this.e = this.a("uSmoothness");
      this.f = this.a("uTopLeftColor");
      this.g = this.a("uBottomLeftColor");
      this.h = this.a("uTopRightColor");
      this.i = this.a("uBottomRightColor");
   }

   public void a(float width, float height) {
      if (this.c != null) {
         this.c.method_1255(width, height);
      }
   }

   public void a(Vector4f radius) {
      if (this.d != null) {
         this.d.method_35657(radius.x, radius.z, radius.w, radius.y);
      }
   }

   public void a(float smoothness) {
      if (this.e != null) {
         this.e.method_1251(smoothness);
      }
   }

   public void a(float r, float g, float b, float a) {
      if (this.f != null) {
         this.f.method_35657(r, g, b, a);
      }
   }

   public void b(float r, float g, float b, float a) {
      if (this.g != null) {
         this.g.method_35657(r, g, b, a);
      }
   }

   public void c(float r, float g, float b, float a) {
      if (this.h != null) {
         this.h.method_35657(r, g, b, a);
      }
   }

   public void d(float r, float g, float b, float a) {
      if (this.i != null) {
         this.i.method_35657(r, g, b, a);
      }
   }
}
