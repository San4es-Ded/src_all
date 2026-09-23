package wtf.wyvern.mixin.minecraft.render;

import wtf.wyvern.core.eventbus.EventManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import wtf.wyvern.core.events.impl.render.EventAspectRatio;
import wtf.wyvern.core.events.impl.render.EventFov;
import wtf.wyvern.core.events.impl.render.EventHudRender;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.core.events.impl.render.EventRenderScreen;
import wtf.wyvern.client.modules.impl.render.Interface;
import wtf.wyvern.client.modules.impl.render.NoRender;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.render.display.base.CustomDrawContext;
import wtf.wyvern.render.display.base.UIContext;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.render.level.Render3DUtil;
import wtf.wyvern.core.performance.render.RenderFrameCache;
import wtf.wyvern.render.particles.ParticleEngine;

@Mixin({GameRenderer.class})
public abstract class MixinGameRenderer {
   @Shadow
   private float field_4005;
   @Shadow
   private float field_3988;
   @Shadow
   private float field_4004;

   @Shadow
   public abstract float method_32796();

   @Inject(method = {"tiltViewWhenHurt"}, at = {@At("HEAD")}, cancellable = true)
   private void removeHurtCamera(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
      if (NoRender.INSTANCE.isRemoveShake()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"getBasicProjectionMatrix"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void getBasicProjectionMatrixHook(float fovDegrees, CallbackInfoReturnable<Matrix4f> cir) {
      EventAspectRatio eventAspectRatio = new EventAspectRatio();
      EventManager.call(eventAspectRatio);
      if (eventAspectRatio.isCancelled()) {
         Matrix4f matrix4f = new Matrix4f();
         if (this.field_4005 != 1.0F) {
            matrix4f.translate(this.field_3988, -this.field_4004, 0.0F);
            matrix4f.scale(this.field_4005, this.field_4005, 1.0F);
         }

         matrix4f.perspective(fovDegrees * 0.017453292F, eventAspectRatio.getRatio(), 0.05F, this.method_32796());
         cir.setReturnValue(matrix4f);
      }

   }

   @ModifyExpressionValue(
      method = {"getFov"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/lang/Integer;intValue()I",
   remap = false
)}
   )
   private int hookGetFov(int original) {
      EventFov event = new EventFov();
      EventManager.call(event);
      return event.isCancelled() ? event.getFov() : original;
   }

   @Inject(method = {"renderWorld"}, at = {@At("HEAD")})
   private void wyvern$beginChamsFrame(RenderTickCounter tickCounter, CallbackInfo ci) {
      wtf.wyvern.render.level.ChamsRenderer.beginWorldFrame();
   }

   @Inject(
      method = {"renderWorld"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
   opcode = 180,
   ordinal = 0
)}
   )
   public void hookWorldRender(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f) {
      RenderFrameCache.beginFrame();
      wtf.wyvern.utility.math.ProjectionUtil.refreshViewport();
      MatrixStack matrixStack = new MatrixStack();
      matrixStack.multiplyPositionMatrix(matrix4f);
      Render3DUtil.setLastProjMat(RenderSystem.getProjectionMatrix());
      Render3DUtil.setLastModMat(RenderSystem.getModelViewMatrix());
      Render3DUtil.setLastWorldSpaceMatrix(matrix4f);
      // Flush chams BEFORE dispatching EventRender3D: handlers like HitEffect and
      // GhostPlayer draw player-model copies with view-rotated matrices - capturing
      // those would double-apply the camera rotation and make drifting boxes. The
      // capture window must cover only WorldRenderer's own entity pass.
      wtf.wyvern.render.level.ChamsRenderer.render();
      EventRender3D event = new EventRender3D(matrixStack, tickCounter.getTickDelta(false));
      EventManager.call(event);
      if (IMinecraft.mc.world != null) {
          ParticleEngine.render3D(event.getMatrix());
      }
      Render3DUtil.onEventRender3D(event.getMatrix());
   }

   @Inject(method = {"renderWorld"}, at = {@At("RETURN")})
   private void applyAmbienceColorGrading(RenderTickCounter tickCounter, CallbackInfo ci) {
      wtf.wyvern.render.shader.AmbienceRenderer.getInstance().render();
      wtf.wyvern.render.shader.MotionBlurRenderer.getInstance().render();
   }

   @Inject(
      method = {"render"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/client/MinecraftClient;world:Lnet/minecraft/client/world/ClientWorld;",
   opcode = 180,
   ordinal = 2
)},
      locals = LocalCapture.CAPTURE_FAILHARD
   )
   private void renderScreenHook(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci, Profiler profiler, boolean bl, int i, int j, Window window, Matrix4f matrix4f, Matrix4fStack matrix4fStack, DrawContext drawContext) {
      DrawUtil.beginBlurFrame();
      EventManager.call(new EventRenderScreen(UIContext.of(drawContext, i, j, IMinecraft.mc.getRenderTickCounter().getTickDelta(false))));
   }

   @Inject(
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/gui/DrawContext;draw()V",
   opcode = 180,
   shift = Shift.AFTER,
   ordinal = 0
)},
      method = {"render"}
   )
   void renderHudHook(RenderTickCounter tickCounter, boolean tick, CallbackInfo callbackInfo) {
      this.triggerHudRenderEvent(tickCounter);
   }

   @Unique
   private void triggerHudRenderEvent(RenderTickCounter tickCounter) {
      CustomDrawContext customDrawContext = new CustomDrawContext(IMinecraft.mc.getBufferBuilders().getEntityVertexConsumers());
      double saveScale = MinecraftClient.getInstance().getWindow().getScaleFactor();
      this.setScaleFactorOutAllMods((double)Interface.INSTANCE.getCustomScale());
      RenderSystem.setProjectionMatrix((new Matrix4f()).setOrtho(0.0F, (float)IMinecraft.mc.getWindow().getScaledWidth(), (float)IMinecraft.mc.getWindow().getScaledHeight(), 0.0F, 1000.0F, 21000.0F), ProjectionType.ORTHOGRAPHIC);
      // This hook runs immediately after vanilla DrawContext.draw(), but one instruction
      // before GameRenderer clears the HUD depth buffer. Scoreboard geometry therefore
      // remained in depth and could hide our later MSDF text, sprites and item icons while
      // shader-drawn backgrounds stayed visible. Start the custom HUD on a clean depth plane.
      RenderSystem.depthMask(true);
      RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT);
      RenderSystem.disableDepthTest();

      try {
         EventManager.call(new EventHudRender(customDrawContext, tickCounter.getTickDelta(false)));
      } catch (Exception var6) {

      }

      customDrawContext.draw();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      this.setScaleFactorOutAllMods(saveScale);
      RenderSystem.setProjectionMatrix((new Matrix4f()).setOrtho(0.0F, (float)IMinecraft.mc.getWindow().getScaledWidth(), (float)IMinecraft.mc.getWindow().getScaledHeight(), 0.0F, 1000.0F, 21000.0F), ProjectionType.ORTHOGRAPHIC);
   }

   @Unique
   public void setScaleFactorOutAllMods(double scaleFactor) {
      IMinecraft.mc.getWindow().scaleFactor = scaleFactor;
      int i = (int)((double)IMinecraft.mc.getWindow().framebufferWidth / scaleFactor);
      IMinecraft.mc.getWindow().scaledWidth = (double)IMinecraft.mc.getWindow().framebufferWidth / scaleFactor > (double)i ? i + 1 : i;
      int j = (int)((double)IMinecraft.mc.getWindow().framebufferHeight / scaleFactor);
      IMinecraft.mc.getWindow().scaledHeight = (double)IMinecraft.mc.getWindow().framebufferHeight / scaleFactor > (double)j ? j + 1 : j;
   }
}
