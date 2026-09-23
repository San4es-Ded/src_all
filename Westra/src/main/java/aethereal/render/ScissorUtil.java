package aethereal.render;

import aethereal.core.Interface;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Generated;
import net.minecraft.class_4587;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ScissorUtil implements Interface {
   private static final Deque<ScissorUtil.a> b = new ArrayDeque<>();
   private static final Matrix4f c = new Matrix4f();

   @Generated
   private ScissorUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void a(class_4587 matrixStack, float x, float y, float width, float height) {
      Matrix4f transform = new Matrix4f(RenderSystem.getProjectionMatrix())
         .mul(RenderSystem.getModelViewMatrix())
         .mul(matrixStack.method_23760().method_23761());
      Vector4f first = transform.transform(new Vector4f(x, y, 0.0F, 1.0F));
      Vector4f second = transform.transform(new Vector4f(x + width, y + height, 0.0F, 1.0F));
      int framebufferWidth = aM_.method_22683().method_4489();
      int framebufferHeight = aM_.method_22683().method_4506();
      float left = (Math.min(first.x, second.x) + 1.0F) * 0.5F * framebufferWidth;
      float right = (Math.max(first.x, second.x) + 1.0F) * 0.5F * framebufferWidth;
      float bottom = (Math.min(first.y, second.y) + 1.0F) * 0.5F * framebufferHeight;
      float top = (Math.max(first.y, second.y) + 1.0F) * 0.5F * framebufferHeight;
      ScissorUtil.a scissorBox = new ScissorUtil.a((int)left, (int)bottom, Math.max(0, (int)(right - left)), Math.max(0, (int)(top - bottom)));
      if (!b.isEmpty()) {
         scissorBox = scissorBox.a(b.peek());
      }

      b.push(scissorBox);
      matrixStack.method_22903();
      a(scissorBox);
   }

   public static void a(class_4587 matrixStack) {
      b.pop();
      if (b.isEmpty()) {
         RenderSystem.disableScissor();
      } else {
         a(b.peek());
      }

      matrixStack.method_22909();
   }

   private static void a(ScissorUtil.a box) {
      RenderSystem.enableScissor(box.a, box.b, box.c, box.d);
   }

   static final class a {
      final int a;
      final int b;
      final int c;
      final int d;

      a(int x, int y, int w, int h) {
         this.a = x;
         this.b = y;
         this.c = w;
         this.d = h;
      }

      public int a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }

      public int c() {
         return this.c;
      }

      public int d() {
         return this.d;
      }

      ScissorUtil.a a(ScissorUtil.a p) {
         int nx = Math.max(this.a, p.a);
         int ny = Math.max(this.b, p.b);
         return new ScissorUtil.a(nx, ny, Math.max(0, Math.min(this.a + this.c, p.a + p.c) - nx), Math.max(0, Math.min(this.b + this.d, p.b + p.d) - ny));
      }
   }
}
