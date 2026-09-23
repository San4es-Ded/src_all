package ru.haron.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.WorldRenderPostEvent;
import haron.events.EventDispatcher;
import haron.events.RenderTickEvent;
import haron.module.ModuleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={WorldRenderer.class})
public abstract class WorldRendererMixin {
    private final MatrixStack sharedMatrixStack = new MatrixStack();

    @Inject(method={"renderWeather"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderWeather(FrameGraphBuilder FrameGraphBuilderVar, Vec3d Vec3dVar, float f, Fog FogVar, CallbackInfo callbackInfo) {
        if (ModuleManager.RENDER_TWEAKS.n()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void preRender(ObjectAllocator ObjectAllocatorVar, RenderTickCounter RenderTickCounterVar, boolean z, Camera CameraVar, GameRenderer GameRendererVar, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo callbackInfo) {
        EventDispatcher.EVENT_BUS.post((Object)new RenderTickEvent(RenderTickCounterVar.getTickDelta(true)));
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void postRenderEvent(ObjectAllocator ObjectAllocatorVar, RenderTickCounter RenderTickCounterVar, boolean z, Camera CameraVar, GameRenderer GameRendererVar, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo callbackInfo) {
        this.restoreRenderState();
        this.sharedMatrixStack.loadIdentity();
        this.sharedMatrixStack.peek().getPositionMatrix().set((Matrix4fc)matrix4f);
        EventDispatcher.EVENT_BUS.post((Object)new WorldRenderPostEvent(this.sharedMatrixStack, RenderTickCounterVar.getTickDelta(true), CameraVar));
    }

    private void restoreRenderState() {
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc((int)515);
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }
}

