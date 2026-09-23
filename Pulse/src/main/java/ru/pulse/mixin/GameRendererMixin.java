package ru.pulse.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.memory.ObjectAllocator;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pulse.events.EventBusService;
import pulse.events.WorldRenderStartEvent;
import pulse.module.ModuleRegistry;
import pulse.modules.utilities.Zoom;
import pulse.modules.visuals.AspectRatio;
import pulse.render.CustomHandShaderCapture;
import pulse.render.motionblur.MotionBlurManager;
import pulse.render.world.WorldToScreen;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
   @Unique
   private Matrix4f prevModelView = new Matrix4f();
   @Unique
   private Matrix4f prevProjection = new Matrix4f();
   @Unique
   private Vector3f prevCameraPos = new Vector3f();

   @Shadow
   public abstract float getFarPlaneDistance();

   @Redirect(
      method = {"renderWorld", "method_3188"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/FrameGraphBuilder;run(Lnet/minecraft/client/util/memory/ObjectAllocator;Lnet/minecraft/client/render/FrameGraphBuilder$Profiler;)V"
      ),
      require = 0
   )
   private void pulse$runFrameGraphSafely(FrameGraphBuilder frameGraphBuilder, ObjectAllocator objectAllocator, FrameGraphBuilder.Profiler profiler) {
      try {
         frameGraphBuilder.run(objectAllocator, profiler);
      } catch (IllegalStateException e) {
         if (!"Pose stack not empty".equals(e.getMessage())) {
            throw e;
         }
      }
   }

   @Redirect(
      method = {"render", "method_3192"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/GameRenderer;renderWorld(Lnet/minecraft/client/render/RenderTickCounter;)V"
      ),
      require = 0
   )
   private void pulse$renderWorldSafely(GameRenderer gameRenderer, RenderTickCounter tickCounter) {
      try {
         gameRenderer.renderWorld(tickCounter);
      } catch (IllegalStateException e) {
         if (!"Pose stack not empty".equals(e.getMessage())) {
            throw e;
         }
      }
   }

   @Inject(require = 0, method = "renderWorld", at = @At("HEAD"))
   private void onRenderWorldStart(RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
      EventBusService.EVENT_BUS.post(new WorldRenderStartEvent(RenderTickCounterVar.getTickProgress(true)));
      MinecraftClient mc = MinecraftClient.getInstance();
      if (mc.player != null && mc.world != null && mc.gameRenderer != null && mc.gameRenderer.getCamera() != null) {
         Camera camera = mc.gameRenderer.getCamera();
         float tickDelta = RenderTickCounterVar.getTickProgress(true);
         float fov = ((GameRendererAccessor)this).invokeGetFov(camera, tickDelta, true);
         Matrix4f projection = this.calcProjectionMatrix(fov);
         WorldToScreen.a.set(projection);
         Matrix4f modelView = new Matrix4f().rotation(camera.getRotation().conjugate(new Quaternionf()));
         MotionBlurManager.INSTANCE
            .setFrameMotionBlur(
               modelView,
               this.prevModelView,
               projection,
               this.prevProjection,
               new Vector3f(
                  (float)(camera.getCameraPos().x % 30000.0),
                  (float)(camera.getCameraPos().y % 30000.0),
                  (float)(camera.getCameraPos().z % 30000.0)
               ),
               this.prevCameraPos
            );
         this.prevModelView.set(modelView);
         this.prevProjection.set(projection);
         this.prevCameraPos
            .set(
               (float)(camera.getCameraPos().x % 30000.0),
               (float)(camera.getCameraPos().y % 30000.0),
               (float)(camera.getCameraPos().z % 30000.0)
            );
      }
   }

   @Inject(method = "tiltViewWhenHurt", at = @At("HEAD"), cancellable = true, require = 0)
   private void pulse$cancelHurtCamera(MatrixStack matrices, float tickProgress, CallbackInfo ci) {
      if (ModuleRegistry.RENDER_TWEAKS != null && ModuleRegistry.RENDER_TWEAKS.o()) {
         ci.cancel();
      }
   }

   private Matrix4f calcProjectionMatrix(float fov) {
      float aspect = (float)MinecraftClient.getInstance().getWindow().getFramebufferWidth() / MinecraftClient.getInstance().getWindow().getFramebufferHeight();
      return new Matrix4f().perspective(fov * (float) (Math.PI / 180.0), aspect, 0.05F, this.getFarPlaneDistance());
   }

   @Inject(require = 0, method = "renderHand", at = @At("HEAD"))
   private void onRenderHandStart(float f, boolean z, Matrix4f matrix4f, CallbackInfo callbackInfo) {
      MotionBlurManager.INSTANCE.applyMotionBlurBeforeHands();
      if (CustomHandShaderCapture.guiOpen()) {
         CustomHandShaderCapture.resetForGui();
      } else {
         CustomHandShaderCapture.beginCapture();
      }
   }

   @Inject(require = 0, method = "renderHand", at = @At("RETURN"))
   private void onRenderHandEnd(float f, boolean z, Matrix4f matrix4f, CallbackInfo callbackInfo) {
      CustomHandShaderCapture.endCapture();
   }

   @Inject(require = 0, method = "renderWorld", at = @At("TAIL"))
   public void renderWorld(RenderTickCounter RenderTickCounterVar, CallbackInfo callbackInfo) {
      Camera CameraVarGetCamera = MinecraftClient.getInstance().gameRenderer.getCamera();
      if (CameraVarGetCamera != null) {
         WorldToScreen.b.identity();
         WorldToScreen.e.rotation(CameraVarGetCamera.getRotation().conjugate(new Quaternionf()));
      }
   }

   @Inject(require = 0, method = "getBasicProjectionMatrix", at = @At("HEAD"), cancellable = true)
   public void getBasicProjectionMatrix(float f, CallbackInfoReturnable<Matrix4f> callbackInfoReturnable) {
      if (Zoom.a) {
         f = (float)Zoom.b;
      }

      MatrixStack MatrixStackVar = new MatrixStack();
      MatrixStackVar.peek().getPositionMatrix().identity();
      float fGetFramebufferWidth = (float)MinecraftClient.getInstance().getWindow().getFramebufferWidth() / MinecraftClient.getInstance().getWindow().getFramebufferHeight();
      AspectRatio aspectRatio = ModuleRegistry.ASPECT_RATIO;
      if (aspectRatio.k()) {
         fGetFramebufferWidth = aspectRatio.n();
      }

      callbackInfoReturnable.setReturnValue(
         MatrixStackVar.peek().getPositionMatrix().perspective(f * (float) (Math.PI / 180.0), fGetFramebufferWidth, 0.05F, this.getFarPlaneDistance())
      );
   }
}
