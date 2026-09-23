package su.sacura.mixin.client.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.profiler.Profiler;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import su.sacura.Sacura;
import su.sacura.features.modules.impl.render.BlockHighLightModule;

@Mixin({WorldRenderer.class})
public abstract class WorldRendererMixin {
    @Shadow
    protected abstract void renderMain(FrameGraphBuilder paramFrameGraphBuilder, Frustum paramFrustum, Camera paramCamera, Matrix4f paramMatrix4f1, Matrix4f paramMatrix4f2, Fog paramFog, boolean paramBoolean1, boolean paramBoolean2, RenderTickCounter paramRenderTickCounter, Profiler paramProfiler);

    // Исправлено: @Redirect -> @WrapOperation (нативный API MixinExtras)
    @WrapOperation(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;renderMain(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/client/render/Frustum;Lnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/client/render/Fog;ZZLnet/minecraft/client/render/RenderTickCounter;Lnet/minecraft/util/profiler/Profiler;)V"
        )
    )
    private void onRender(WorldRenderer instance, FrameGraphBuilder frameGraphBuilder, Frustum frustum, Camera camera, Matrix4f positionMatrix, Matrix4f projectionMatrix, Fog fog, boolean renderBlockOutline, boolean hasEntitiesToRender, RenderTickCounter renderTickCounter, Profiler profiler, Operation<Void> original) {
        BlockHighLightModule module = Sacura.getInstance().getModuleManager().getModule(BlockHighLightModule.class);
        boolean newOutline = !(module != null && module.enable);
        original.call(instance, frameGraphBuilder, frustum, camera, positionMatrix, projectionMatrix, fog, newOutline, hasEntitiesToRender, renderTickCounter, profiler);
    }
}