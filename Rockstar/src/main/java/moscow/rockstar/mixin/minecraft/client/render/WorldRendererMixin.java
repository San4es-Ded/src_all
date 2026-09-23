package moscow.rockstar.mixin.minecraft.client.render;




import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.memory.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.BlockRenderView;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.render.Render3DEvent;
import rockstar.modules.visual.AmbienceModule;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.script.GhostBlockRenderer;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.game.DynamicLightEngine;
import rockstar.client.ui.ThemeColors;

@Mixin(value={WorldRenderer.class})
public abstract class WorldRendererMixin
implements MinecraftClientAccess {
    private MatrixStack renderEventStack;

    @Inject(method = "render", at = @At("HEAD"))
    private void rockstar$syncWorldProjection(ObjectAllocator allocator, RenderTickCounter ticks,
        boolean outline, Camera camera, Matrix4f view, Matrix4f projection, Matrix4f culling,
        GpuBufferSlice fog, Vector4f fogColor, boolean sky, CallbackInfo ci) {
        rockstar.client.compat.RenderSystem.syncWorldProjection(projection);
    }

    private MatrixStack rockstar$renderEventStack() {
        if (this.renderEventStack == null) {
            this.renderEventStack = new MatrixStack();
        }
        return this.renderEventStack;
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void render(
        ObjectAllocator objectAllocator,
        RenderTickCounter renderTickCounter,
        boolean renderBlockOutline,
        Camera camera,
        Matrix4f positionMatrix,
        Matrix4f basicProjectionMatrix,
        Matrix4f cullingProjectionMatrix,
        GpuBufferSlice fogBuffer,
        Vector4f fogColor,
        boolean renderSky,
        CallbackInfo callbackInfo
    ) {
        Profilers.get().swap(RockstarClient.internalField1077 + "_renderWorld");
        this.applyWetWorld(camera, positionMatrix, basicProjectionMatrix, renderTickCounter.getTickProgress(false));
        this.applySaturation();
        MatrixStack matrixStack = this.rockstar$renderEventStack();
        matrixStack.push();
        matrixStack.multiplyPositionMatrix(positionMatrix);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new Render3DEvent(matrixStack, positionMatrix, basicProjectionMatrix, camera, renderTickCounter.getTickProgress(false)));
        matrixStack.pop();
    }

    @Inject(method={"getLightmapCoordinates(Lnet/minecraft/client/render/WorldRenderer$BrightnessGetter;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)I"}, at={@At(value="RETURN")}, cancellable=true)
    private static void applyDynamicLight(WorldRenderer.BrightnessGetter brightnessGetter, BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(GhostBlockRenderer.internalMethod01806(blockPos, DynamicLightEngine.internalMethod07614(blockPos, callbackInfoReturnable.getReturnValueI())));
    }

    @ModifyVariable(method={"renderClouds"}, at=@At("HEAD"), argsOnly=true, ordinal=0)
    private int rockstar$replaceCloudColor(int vanillaColor) {
        AmbienceModule ambience = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (ambience != null && ambience.isEnabled() && ambience.internalMethod06628().isSelected()) {
            return ambience.internalMethod00796().internalMethod04496()
                ? ThemeColors.internalMethod02531().getRGB()
                : ambience.internalMethod01575().internalMethod05620().getRGB();
        }
        return vanillaColor;
    }

    private void applyWetWorld(Camera camera, Matrix4f matrix4f, Matrix4f matrix4f2, float f) {
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (typedValue317 == null || !typedValue317.internalMethod10068()) {
            return;
        }
        if (RenderPipeline.internalField0446 == null || WorldRendererMixin.internalField0149.world == null || camera == null || !camera.isReady()) {
            return;
        }
        RenderPipeline.internalField0446.internalMethod01883(typedValue317.internalMethod03372(matrix4f, matrix4f2, camera, f));
    }

    private void applySaturation() {
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (typedValue317 == null || !typedValue317.internalMethod09415()) {
            return;
        }
        if (RenderPipeline.internalField0437 == null) {
            return;
        }
        Vector3f vector3f = typedValue317.internalMethod05730();
        RenderPipeline.internalField0437.internalMethod02418(typedValue317.internalMethod06261(), vector3f.x, vector3f.y, vector3f.z, typedValue317.internalMethod08781(), typedValue317.internalMethod08783(), typedValue317.internalMethod08789(), typedValue317.internalMethod08790());
    }
}
