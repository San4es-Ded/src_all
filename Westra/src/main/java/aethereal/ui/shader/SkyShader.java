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
import net.minecraft.class_4184;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

public class SkyShader extends Shader implements Interface {
   private final Matrix4f c = new Matrix4f();
   private class_278 d;
   private class_278 e;
   private class_278 f;
   private class_278 g;
   private class_278 h;
   private class_278 i;
   private class_278 j;
   private class_278 k;
   private class_278 l;

   public SkyShader(String name) {
      super(class_2960.method_60655("westra", "core/sky/" + name), class_290.field_1576);
   }

   @Override
   protected void b() {
      this.d = this.a("uResolution");
      this.e = this.a("uCameraDir");
      this.f = this.a("uColor");
      this.g = this.a("uTime");
      this.h = this.a("uAlpha");
      this.i = this.a("uSpeed");
      this.j = this.a("uScale");
      this.k = this.a("uIntensity");
      this.l = this.a("uFov");
   }

   public void a(float[] color, float alpha, float speed, float scale, float intensity) {
      RenderSystem.backupProjectionMatrix();
      RenderSystem.setProjectionMatrix(this.c, class_10366.field_54953);
      Matrix4fStack modelView = RenderSystem.getModelViewStack();
      modelView.pushMatrix().identity();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      this.a();
      if (this.d != null) {
         this.d.method_1255(aM_.method_22683().method_4489(), aM_.method_22683().method_4506());
      }

      if (this.e != null) {
         class_4184 camera = aM_.field_1773.method_19418();
         this.e.method_1255((float)Math.toRadians(-camera.method_19330()), (float)Math.toRadians(camera.method_19329()));
      }

      if (this.f != null) {
         this.f.method_35657(color[0], color[1], color[2], 1.0F);
      }

      if (this.g != null) {
         this.g.method_1251((float)(System.currentTimeMillis() % 1000000L) / 1000.0F);
      }

      if (this.h != null) {
         this.h.method_1251(alpha);
      }

      if (this.i != null) {
         this.i.method_1251(speed);
      }

      if (this.j != null) {
         this.j.method_1251(scale);
      }

      if (this.k != null) {
         this.k.method_1251(intensity);
      }

      if (this.l != null) {
         this.l.method_1251(((Integer)aM_.field_1690.method_41808().method_41753()).floatValue());
      }

      int white = new Color(255, 255, 255, 255).getRGB();
      class_287 builder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      builder.method_22912(-1.0F, -1.0F, 0.0F).method_39415(white);
      builder.method_22912(-1.0F, 1.0F, 0.0F).method_39415(white);
      builder.method_22912(1.0F, 1.0F, 0.0F).method_39415(white);
      builder.method_22912(1.0F, -1.0F, 0.0F).method_39415(white);
      class_286.method_43433(builder.method_60800());
      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.disableBlend();
      modelView.popMatrix();
      RenderSystem.restoreProjectionMatrix();
   }
}
