package aethereal.ui.shader;

import aethereal.core.EventManager;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.ResizeEvent;
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

public class NoiseShader extends Shader implements Interface {
   private static final class_2960 c = class_2960.method_60655("westra", "core/noise/noise_shader");
   private final Matrix4f d = new Matrix4f();
   private class_6367 e;
   private class_278 f;
   private class_278 g;

   public NoiseShader() {
      super(c, class_290.field_1576);
      EventManager.a(this);
   }

   @EventTarget
   public void a(ResizeEvent event) {
      this.e = new class_6367(aM_.method_22683().method_4489(), aM_.method_22683().method_4506(), true);
   }

   public void e() {
      if (this.e != null) {
         this.e.method_29329(aM_.method_1522());
      }
   }

   @Override
   protected void b() {
      this.f = this.a("TintColor");
      this.g = this.a("Time");
   }

   public void a(float[] color) {
      if (this.e != null) {
         RenderSystem.backupProjectionMatrix();
         RenderSystem.setProjectionMatrix(this.d, class_10366.field_54953);
         Matrix4fStack modelView = RenderSystem.getModelViewStack();
         modelView.pushMatrix().identity();
         RenderSystem.disableDepthTest();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         RenderSystem.setShaderTexture(0, aM_.method_1522().method_30277());
         RenderSystem.setShaderTexture(1, aM_.method_1522().method_30278());
         RenderSystem.setShaderTexture(2, this.e.method_30278());
         this.a();
         if (this.f != null) {
            this.f.method_35657(color[0], color[1], color[2], color[3]);
         }

         if (this.g != null) {
            this.g.method_1251((float)(System.currentTimeMillis() % 100000L) / 1000.0F);
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
         RenderSystem.setShaderTexture(2, 0);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         RenderSystem.enableDepthTest();
         modelView.popMatrix();
         RenderSystem.restoreProjectionMatrix();
      }
   }
}
