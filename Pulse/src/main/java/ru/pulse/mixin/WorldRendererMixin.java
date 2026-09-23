package ru.pulse.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.FramePass;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pulse.events.EventBusService;
import pulse.events.WorldRenderEvent;
import pulse.module.ModuleRegistry;
import pulse.modules.visuals.WorldCustomizer;
import pulse.render.RenderSystemHelper;
import pulse.render.shader.SkyShaderRenderer;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
   @Shadow
   @Final
   private BufferBuilderStorage bufferBuilders;
   @Shadow
   @Final
   private DefaultFramebufferSet framebufferSet;
   @Shadow
   private WorldRenderState worldRenderState;

   /**
    * @author Pulse
    * @reason Prevent client crashes when a render hook leaves MatrixStack dirty.
    */
   @Overwrite
   private void checkEmpty(MatrixStack matrices) {
      this.pulse$clearPoseStack(matrices);
   }

   @Inject(method = "renderBlockDamage", at = @At("TAIL"), require = 0)
   private void renderBlockDamage(MatrixStack matrices, Immediate immediate, WorldRenderState renderStates, CallbackInfo ci) {
      MinecraftClient mc = MinecraftClient.getInstance();
      if (mc.player != null && mc.world != null) {
         Immediate imm = this.bufferBuilders.getEntityVertexConsumers();
         this.restoreRenderState();
         this.postWorldRenderEvent(new MatrixStack(), imm, mc.getRenderTickCounter().getTickProgress(true));
         this.restoreRenderState();
      }
   }

   @Inject(method = {"checkEmpty", "method_22979", "a(Lnet/minecraft/client/util/math/MatrixStack;)V"}, at = @At("HEAD"), cancellable = true, require = 0)
   private void pulse$repairNonEmptyPoseStack(MatrixStack matrices, CallbackInfo ci) {
      if (this.pulse$clearPoseStack(matrices)) {
         ci.cancel();
      }
   }

   @ModifyArg(
      method = {"render", "method_62214", "a(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lnet/minecraft/client/render/state/WorldRenderState;Lnet/minecraft/util/profiler/Profiler;Lorg/joml/Matrix4f;Lnet/minecraft/client/util/Handle;Lnet/minecraft/client/util/Handle;ZLnet/minecraft/client/util/Handle;Lnet/minecraft/client/util/Handle;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V"
      ),
      index = 0,
      require = 0
   )
   private MatrixStack pulse$sanitizeCheckEmptyArgument(MatrixStack matrices) {
      this.pulse$clearPoseStack(matrices);
      return matrices;
   }

   @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true, require = 0)
   private void onRenderSky(FrameGraphBuilder frameGraphBuilder, Camera camera, GpuBufferSlice gpuBufferSlice, CallbackInfo ci) {
      WorldCustomizer wc = ModuleRegistry.WORLD_CUSTOMIZER;
      if (wc != null && wc.k() && wc.enableShader.a()) {
         FramePass framePass = frameGraphBuilder.createPass("pulse_sky_shader");
         this.framebufferSet.mainFramebuffer = framePass.transfer(this.framebufferSet.mainFramebuffer);
         framePass.setRenderer(() -> {
            RenderSystem.setShaderFog(gpuBufferSlice);
            SkyShaderRenderer.INSTANCE.renderShader();
         });
         ci.cancel();
      } else {
         if (this.worldRenderState != null && this.worldRenderState.skyRenderState != null && wc != null && wc.r()) {
            this.worldRenderState.skyRenderState.skyColor = wc.t();
         }
      }
   }

   @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true, require = 0)
   private void onRenderClouds(CallbackInfo ci) {
      WorldCustomizer wc = ModuleRegistry.WORLD_CUSTOMIZER;
      if (wc != null && wc.k() && wc.enableShader.a()) {
         ci.cancel();
      }
   }

   private void restoreRenderState() {
      RenderSystemHelper.enableDepthTest();
      RenderSystemHelper.depthFunc(515);
      RenderSystemHelper.depthMask(true);
      RenderSystemHelper.enableCull();
      RenderSystemHelper.disableBlend();
      RenderSystemHelper.defaultBlendFunc();
      RenderSystemHelper.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private boolean pulse$clearPoseStack(MatrixStack matrices) {
      if (matrices == null || matrices.isEmpty()) {
         return false;
      }

      while (!matrices.isEmpty()) {
         matrices.pop();
      }

      return true;
   }

   private void postWorldRenderEvent(MatrixStack matrices, Immediate immediate, float tickDelta) {
      try {
         EventBusService.EVENT_BUS.post(new WorldRenderEvent(matrices, immediate, tickDelta));
      } finally {
         while (!matrices.isEmpty()) {
            matrices.pop();
         }
      }
   }
}
