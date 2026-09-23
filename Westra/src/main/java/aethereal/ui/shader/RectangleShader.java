package aethereal.ui.shader;

import net.minecraft.class_278;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import org.joml.Vector4f;

public class RectangleShader extends Shader {
   public class_278 c;
   public class_278 d;
   public class_278 e;
   public class_278 f;

   public RectangleShader() {
      super(class_2960.method_60655("westra", "core/rect/rect"), class_290.field_1576);
   }

   @Override
   protected void b() {
      this.c = this.a("uSize");
      this.d = this.a("uRadius");
      this.e = this.a("uSmoothness");
      this.f = this.a("uOutlineWidth");
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

   public void b(float width) {
      if (this.f != null) {
         this.f.method_1251(width);
      }
   }
}
