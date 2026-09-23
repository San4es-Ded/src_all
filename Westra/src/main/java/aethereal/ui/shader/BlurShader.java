package aethereal.ui.shader;

import aethereal.core.EventManager;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ResizeEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_276;
import net.minecraft.class_278;
import net.minecraft.class_284;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_5944;
import net.minecraft.class_6367;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class BlurShader extends Shader implements Interface {
   private final List<class_6367> n = new ArrayList<>();
   public class_278 c;
   public class_278 d;
   public class_278 e;
   public class_278 f;
   public class_278 g;
   public class_278 h;
   public class_278 i;
   public class_278 j;
   public class_278 k;
   public class_278 l;
   public class_278 m;
   private final class_10156 o = new class_10156(class_2960.method_60655("westra", "core/blur/upscale"), class_290.field_1592, class_10149.field_53930);
   private final class_10156 p = new class_10156(class_2960.method_60655("westra", "core/blur/downscale"), class_290.field_1592, class_10149.field_53930);

   @Generated
   public List<class_6367> e() {
      return this.n;
   }

   public BlurShader() {
      super(class_2960.method_60655("westra", "core/rect/blurred_rect"), class_290.field_1575);
      EventManager.a(this);
   }

   @EventTarget
   public void a(ResizeEvent event) {
      this.g();
   }

   public void g() {
      int steps = 4;

      try {
         steps = Westra.h().d().t().bi().r();
      } catch (Throwable var3) {
      }

      this.n.clear();

      for (int i = 0; i < Math.max(2, steps); i++) {
         this.n.add(this.f());
      }
   }

   @Override
   protected void b() {
      this.c = this.a("uSize");
      this.d = this.a("uRadius");
      this.e = this.a("uSmoothness");
      this.f = this.a("uMix");
      this.g = this.a("uAlpha");
      this.h = this.a("uTopLeftColor");
      this.i = this.a("uBottomLeftColor");
      this.j = this.a("uTopRightColor");
      this.k = this.a("uBottomRightColor");
      this.l = this.a("uGlowColor");
      this.m = this.a("uGlowRadius");
   }

   public void a(class_4587 matrixStack) {
      if (!this.n.isEmpty()) {
         int actualPasses = Math.max(this.n.size() - 1, 1);

         try {
            this.a(matrixStack, this.p, aM_.method_1522(), (class_276)this.n.getFirst(), 0, 24);

            for (int i = 0; i < actualPasses; i++) {
               this.a(matrixStack, this.p, (class_276)this.n.get(i), (class_276)this.n.get(i + 1), i + 1, 24);
            }

            for (int i2 = actualPasses; i2 > 0; i2--) {
               this.a(matrixStack, this.o, (class_276)this.n.get(i2), (class_276)this.n.get(i2 - 1), i2, 24);
            }
         } finally {
            aM_.method_1522().method_1235(false);
         }
      }
   }

   private void a(class_4587 matrixStack, class_10156 shaderKey, class_276 source, class_276 destination, int pass, int offset) {
      destination.method_1235(false);
      RenderSystem.setShaderTexture(0, source.method_30277());
      class_5944 shader = RenderSystem.setShader(shaderKey);
      class_284 class_284VarMethod_34582 = shader.method_34582("uHalfTexelSize");
      class_284 class_284VarMethod_34583 = shader.method_34582("uOffset");
      if (class_284VarMethod_34582 != null) {
         class_284VarMethod_34582.method_1255(0.5F / source.field_1482, 0.5F / source.field_1481);
      }

      if (class_284VarMethod_34583 != null) {
         class_284VarMethod_34583.method_1251(offset * (pass / 3.0F));
      }

      this.a(matrixStack.method_23760().method_23761());
      destination.method_1240();
   }

   private void a(Matrix4f matrix4f) {
      class_287 builder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1592);
      builder.method_22918(matrix4f, 0.0F, 0.0F, 0.0F);
      builder.method_22918(matrix4f, 0.0F, aM_.method_22683().method_4502(), 0.0F);
      builder.method_22918(matrix4f, aM_.method_22683().method_4486(), aM_.method_22683().method_4502(), 0.0F);
      builder.method_22918(matrix4f, aM_.method_22683().method_4486(), 0.0F, 0.0F);
      class_286.method_43433(builder.method_60800());
   }

   private class_6367 f() {
      return new class_6367(aM_.method_22683().method_4489(), aM_.method_22683().method_4506(), false);
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

   public void b(float mix) {
      if (this.f != null) {
         this.f.method_1251(mix);
      }
   }

   public void c(float alpha) {
      if (this.g != null) {
         this.g.method_1251(alpha);
      }
   }

   public void a(float r, float g, float b, float a) {
      if (this.h != null) {
         this.h.method_35657(r, g, b, a);
      }
   }

   public void b(float r, float g, float b, float a) {
      if (this.i != null) {
         this.i.method_35657(r, g, b, a);
      }
   }

   public void c(float r, float g, float b, float a) {
      if (this.j != null) {
         this.j.method_35657(r, g, b, a);
      }
   }

   public void d(float r, float g, float b, float a) {
      if (this.k != null) {
         this.k.method_35657(r, g, b, a);
      }
   }

   public void e(float r, float g, float b, float a) {
      if (this.l != null) {
         this.l.method_35657(r, g, b, a);
      }
   }

   public void d(float r) {
      if (this.m != null) {
         this.m.method_1251(r);
      }
   }
}
