package ru.haron.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.module.ModuleManager;
import haron.modules.utilities.Zoom;
import haron.modules.utilities.FreeLook;
import haron.modules.visuals.AspectRatio;
import haron.render.world.trp5t7;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GameRenderer.class})
public abstract class GameRendererMixin {
    @Shadow
    private float zoom;
    @Shadow
    private float zoomX;
    @Shadow
    private float zoomY;
    private final MatrixStack tempMatrixStack = new MatrixStack();
    private final Matrix4f tempProjectionMatrix = new Matrix4f();
    private final Matrix4f tempModelViewMatrix = new Matrix4f();
    private final Matrix4f tempPositionMatrix = new Matrix4f();

    @Shadow
    public abstract float getFarPlaneDistance();

    @Inject(method={"renderWorld"}, at={@At(value="HEAD")})
    private void onRenderWorldStart(RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        Zoom.tickZoom();
    }

    @Inject(method={"renderWorld"}, at={@At(value="FIELD", target="Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode=180, ordinal=0)})
    private void renderWorld(RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        MatrixStack matrixStack = this.tempMatrixStack;
        matrixStack.loadIdentity();
        float yaw = FreeLook.active ? FreeLook.freeYaw : camera.getYaw();
        float pitch = FreeLook.active ? FreeLook.freePitch : camera.getPitch();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw + 180.0f));
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.mul((Matrix4fc)matrixStack.peek().getPositionMatrix());
        trp5t7.a.set((Matrix4fc)RenderSystem.getProjectionMatrix());
        trp5t7.b.set((Matrix4fc)RenderSystem.getModelViewMatrix());
        trp5t7.e.set((Matrix4fc)matrixStack.peek().getPositionMatrix());
        modelViewStack.popMatrix();
    }

    @Inject(method={"getBasicProjectionMatrix"}, at={@At(value="HEAD")}, cancellable=true)
    private void getBasicProjectionMatrix(float fov, CallbackInfoReturnable<Matrix4f> callbackInfoReturnable) {
        float finalFov = Zoom.a ? (float)Zoom.b : fov;
        MatrixStack matrixStack = this.tempMatrixStack;
        matrixStack.loadIdentity();
        if (this.zoom != 1.0f) {
            matrixStack.translate((double)this.zoomX, (double)(-this.zoomY), 0.0);
            matrixStack.scale(this.zoom, this.zoom, 1.0f);
        }
        MinecraftClient client = MinecraftClient.getInstance();
        float aspect = (float)client.getWindow().getFramebufferWidth() / (float)client.getWindow().getFramebufferHeight();
        AspectRatio aspectRatio = ModuleManager.ASPECT_RATIO;
        if (aspectRatio.k()) {
            aspect = aspectRatio.n();
        }
        Matrix4f proj = this.tempProjectionMatrix;
        proj.identity();
        proj.perspective(finalFov * ((float)Math.PI / 180), aspect, 0.05f, this.getFarPlaneDistance());
        Matrix4f result = matrixStack.peek().getPositionMatrix().mul((Matrix4fc)proj, this.tempModelViewMatrix);
        callbackInfoReturnable.setReturnValue(result);
    }
}

