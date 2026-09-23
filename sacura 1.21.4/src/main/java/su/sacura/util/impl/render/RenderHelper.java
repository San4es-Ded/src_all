 package su.sacura.util.impl.render;
 
 import com.mojang.blaze3d.platform.GlStateManager;
 import com.mojang.blaze3d.systems.RenderSystem;
 import net.minecraft.client.render.BufferBuilder;
 import net.minecraft.client.render.BufferRenderer;
 import net.minecraft.client.render.BuiltBuffer;
 import net.minecraft.client.util.math.MatrixStack;
 import org.joml.Matrix4f;
 import org.joml.Matrix4fc;
 import su.sacura.util.type.MinecraftWrapper;
 
 public class RenderHelper implements MinecraftWrapper {
   public static final Matrix4f lastProjMat = new Matrix4f();
   
   public static final Matrix4f lastModMat = new Matrix4f();
   
   public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();
   
   public static void translation(MatrixStack matrixStack) {
     lastProjMat.set((Matrix4fc)RenderSystem.getProjectionMatrix());
     lastModMat.set((Matrix4fc)RenderSystem.getModelViewMatrix());
     lastWorldSpaceMatrix.set((Matrix4fc)matrixStack.peek().getPositionMatrix());
   }
   
   public static void enable(GlStateManager.SrcFactor srcFactor, GlStateManager.DstFactor dstFactor) {
     RenderSystem.enableBlend();
     RenderSystem.blendFunc(srcFactor, dstFactor);
   }
   
   public static void disable() {
     RenderSystem.enableCull();
     RenderSystem.disableBlend();
   }
   
   public static void size(MatrixStack matrix, double width, double height, double scale) {
     matrix.translate(width, height, 0.0D);
     matrix.scale((float)scale, (float)scale, (float)scale);
     matrix.translate(-width, -height, 0.0D);
   }
   
   public static void end(BufferBuilder bb) {
     BuiltBuffer builtBuffer = bb.endNullable();
     if (builtBuffer != null)
       BufferRenderer.drawWithGlobalProgram(builtBuffer); 
   }
 }


