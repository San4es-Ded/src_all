package wtf.wyvern.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.core.events.impl.render.EventRenderSky;
import wtf.wyvern.client.modules.impl.render.BlockOverlay;
import wtf.wyvern.client.modules.impl.render.CustomSky;
import wtf.wyvern.client.modules.impl.render.Sonar;
import wtf.wyvern.render.shader.AmbienceRenderer;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Unique
    private static final Runnable wyvern$customSkyRenderer = CustomSky.INSTANCE::renderSkyShader;

    @WrapOperation(
        method = "renderSky",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/RenderPass;setRenderer(Ljava/lang/Runnable;)V"
        )
    )
    private void wyvern$wrapSkyRenderer(RenderPass renderPass, Runnable originalRenderer, Operation<Void> original) {
        original.call(renderPass, CustomSky.INSTANCE.isEnabled() ? wyvern$customSkyRenderer : originalRenderer);
    }

    @WrapMethod(method = "renderTargetBlockOutline")
    private void wyvern$wrapTargetBlockOutline(
            Camera camera,
            VertexConsumerProvider.Immediate vertexConsumers,
            MatrixStack matrices,
            boolean translucent,
            Operation<Void> original
    ) {
        if (!BlockOverlay.INSTANCE.replacesVanillaOutline()) {
            original.call(camera, vertexConsumers, matrices, translucent);
        }
    }

    @Inject(method = "renderSky", at = @At("RETURN"))
    private void onRenderSky(FrameGraphBuilder frameGraphBuilder, Camera camera, float tickDelta, Fog fog, CallbackInfo ci) {
        EventManager.call(new EventRenderSky(new MatrixStack(), new Matrix4f(), tickDelta));
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void onRenderClouds(FrameGraphBuilder frameGraphBuilder, Matrix4f matrix4f, Matrix4f matrix4f2, CloudRenderMode cloudRenderMode, Vec3d vec3d, float f, int i, float f2, CallbackInfo ci) {
        if (CustomSky.INSTANCE.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void wyvern$renderSonar(ObjectAllocator allocator, RenderTickCounter tickCounter,
                                    boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                                    Matrix4f positionMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        AmbienceRenderer.getInstance().renderBlurFog();
        if (Sonar.INSTANCE.isEnabled()) {
            Sonar.INSTANCE.renderFromMixin(positionMatrix, projectionMatrix, camera.getPos());
        }
    }

}
