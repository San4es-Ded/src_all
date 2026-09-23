package aethereal.ui.shader;

import aethereal.core.Interface;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.class_10366;
import net.minecraft.class_278;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_6367;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL30;

public class MotionBlurShader extends Shader implements Interface {
   private static final class_2960 c = class_2960.method_60655("westra", "core/motion/motion_blur");
   private final Matrix4f d = new Matrix4f();
   private final Matrix4f e = new Matrix4f();
   private final Matrix4f f = new Matrix4f();
   private final Matrix4f g = new Matrix4f();
   private final float[] h = new float[3];
   private final float[] i = new float[3];
   private class_6367 j;
   private int k;
   private int l;
   private boolean m;
   private class_278 n;
   private class_278 o;
   private class_278 p;
   private class_278 q;
   private class_278 r;
   private class_278 s;
   private class_278 t;
   private class_278 u;
   private class_278 v;
   private class_278 w;
   private class_278 x;
   private class_278 y;
   private class_278 z;

   public MotionBlurShader() {
      super(c, class_290.field_1576);
   }

   @Override
   protected void b() {
      this.n = this.a("MvInverse");
      this.o = this.a("ProjInverse");
      this.p = this.a("PrevModelView");
      this.q = this.a("PrevProjection");
      this.r = this.a("CameraPos");
      this.s = this.a("PrevCameraPos");
      this.t = this.a("ViewRes");
      this.u = this.a("BlendFactor");
      this.v = this.a("InverseSamples");
      this.w = this.a("HandDepthThreshold");
      this.x = this.a("SampleCount");
      this.y = this.a("HalfSamples");
      this.z = this.a("BlurAlgorithm");
   }

   public void a(Matrix4f modelView, Matrix4f projection, double cameraX, double cameraY, double cameraZ) {
      if (this.m) {
         this.f.set(this.d);
         this.g.set(this.e);
         this.i[0] = this.h[0];
         this.i[1] = this.h[1];
         this.i[2] = this.h[2];
      }

      this.d.set(modelView);
      this.e.set(projection);
      this.h[0] = (float)cameraX;
      this.h[1] = (float)cameraY;
      this.h[2] = (float)cameraZ;
      if (!this.m) {
         this.f.set(this.d);
         this.g.set(this.e);
         this.i[0] = this.h[0];
         this.i[1] = this.h[1];
         this.i[2] = this.h[2];
         this.m = true;
      }
   }

   private void e() {
      int width = aM_.method_22683().method_4489();
      int height = aM_.method_22683().method_4506();
      if (this.j == null || this.k != width || this.l != height) {
         if (width <= 0 || height <= 0) {
            return;
         }

         this.j = new class_6367(width, height, false);
         this.k = width;
         this.l = height;
      }
   }

   public void a(int samples, float strength, boolean centered) {
      if (this.m) {
         this.e();
         if (this.j != null) {
            int main = aM_.method_1522().field_1476;
            GL30.glBindFramebuffer(36008, main);
            GL30.glBindFramebuffer(36009, this.j.field_1476);
            GL30.glBlitFramebuffer(0, 0, this.k, this.l, 0, 0, this.k, this.l, 16384, 9728);
            GL30.glBindFramebuffer(36160, main);
            RenderSystem.backupProjectionMatrix();
            RenderSystem.setProjectionMatrix(new Matrix4f(), class_10366.field_54953);
            Matrix4fStack modelView = RenderSystem.getModelViewStack();
            modelView.pushMatrix().identity();
            RenderSystem.disableDepthTest();
            RenderSystem.disableCull();
            RenderSystem.disableBlend();
            RenderSystem.setShaderTexture(0, this.j.method_30277());
            RenderSystem.setShaderTexture(1, aM_.method_1522().method_30278());
            this.a();
            if (this.n != null) {
               this.n.method_1250(new Matrix4f(this.d).invert());
            }

            if (this.o != null) {
               this.o.method_1250(new Matrix4f(this.e).invert());
            }

            if (this.p != null) {
               this.p.method_1250(this.f);
            }

            if (this.q != null) {
               this.q.method_1250(this.g);
            }

            if (this.r != null) {
               this.r.method_35657(this.h[0], this.h[1], this.h[2], 0.0F);
            }

            if (this.s != null) {
               this.s.method_35657(this.i[0], this.i[1], this.i[2], 0.0F);
            }

            if (this.t != null) {
               this.t.method_1255(this.k, this.l);
            }

            if (this.u != null) {
               this.u.method_1251(strength);
            }

            if (this.v != null) {
               this.v.method_1251(1.0F / samples);
            }

            if (this.w != null) {
               this.w.method_1251(0.05F);
            }

            if (this.x != null) {
               this.x.method_1251(samples);
            }

            if (this.y != null) {
               this.y.method_1251(samples / 2.0F);
            }

            if (this.z != null) {
               this.z.method_1251(centered ? 1.0F : 0.0F);
            }

            int white = new Color(255, 255, 255, 255).getRGB();
            class_287 builder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
            builder.method_22912(-1.0F, -1.0F, 0.0F).method_39415(white);
            builder.method_22912(-1.0F, 1.0F, 0.0F).method_39415(white);
            builder.method_22912(1.0F, 1.0F, 0.0F).method_39415(white);
            builder.method_22912(1.0F, -1.0F, 0.0F).method_39415(white);
            class_286.method_43433(builder.method_60800());
            RenderSystem.setShaderTexture(0, 0);
            RenderSystem.setShaderTexture(1, 0);
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            modelView.popMatrix();
            RenderSystem.restoreProjectionMatrix();
         }
      }
   }
}
