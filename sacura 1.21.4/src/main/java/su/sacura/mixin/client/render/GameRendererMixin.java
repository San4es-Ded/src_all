package su.sacura.mixin.client.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.sacura.Sacura;
import su.sacura.events.render.EventRender3D;
import su.sacura.events.render.WorldRenderEvent;
import su.sacura.features.modules.impl.render.AspectRatioModule;
import su.sacura.features.modules.impl.render.NoOverlayModule;
import su.sacura.util.impl.render.RenderHelper;
import su.sacura.util.impl.render.RenderWorld;
import su.sacura.util.type.MinecraftWrapper;

@Mixin(value = {GameRenderer.class})
public abstract class GameRendererMixin implements MinecraftWrapper {

    // Исправлено: getFov() без аргументов не существует в 1.21.4.
    // Для дальней плоскости используется getFarPlaneDistance().
    @Shadow
    public abstract float getFarPlaneDistance();

    @Inject(at = {@At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = 180, ordinal = 0)}, method = {"renderWorld"})
    private void render3dHook(RenderTickCounter tickCounter, CallbackInfo ci) {
        MatrixStack matrixStack = new MatrixStack();
        Camera camera = mc.gameRenderer.getCamera();
        RenderSystem.getModelViewStack().pushMatrix().mul((Matrix4fc) matrixStack.peek().getPositionMatrix());
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));
        RenderSystem.setShaderFog((Fog) Fog.DUMMY);
        RenderHelper.translation(matrixStack);
        Sacura.getInstance().getEventBus().post(new EventRender3D(matrixStack, tickCounter));
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.getModelViewMatrix();
    }

    @Inject(method = {"renderWorld"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = 180, ordinal = 0)})
    public void hookWorldRender(RenderTickCounter tickCounter, CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(matrix4f);
        matrixStack.translate(mc.getEntityRenderDispatcher().camera.getPos().negate());
        RenderWorld.setLastProjMat(RenderSystem.getProjectionMatrix());
        RenderWorld.setLastWorldSpaceMatrix(matrixStack.peek());
        WorldRenderEvent event = new WorldRenderEvent(matrixStack, tickCounter.getTickDelta(false));
        Sacura.getInstance().getEventBus().post(event);
        RenderWorld.onWorldRender(event);
    }

    @Inject(method = {"getBasicProjectionMatrix"}, at = {@At(value = "TAIL")}, cancellable = true)
    private void getBasicProjectionMatrixHook(float fov, CallbackInfoReturnable<Matrix4f> cir) {
        AspectRatioModule aspectRatio = Sacura.getInstance().getModuleManager().getModule(AspectRatioModule.class);
        if (aspectRatio != null && aspectRatio.enable) {
            String mode;
            float aspect = 1.0f;
            switch (mode = (String) aspectRatio.mods.get()) {
                case "4:3": {
                    aspect = 1.3333334f;
                    break;
                }
                case "16:9": {
                    aspect = 1.7777778f;
                    break;
                }
                case "1:1": {
                    aspect = 1.0f;
                    break;
                }
                case "16:10": {
                    aspect = 1.6f;
                    break;
                }
                case "Кастомный": {
                    aspect = ((Float) aspectRatio.slider.get()).floatValue();
                }
            }
            MatrixStack matrixStack = new MatrixStack();
            matrixStack.peek().getPositionMatrix().identity();
            // Исправлено: this.getFov() -> this.getFarPlaneDistance()
            matrixStack.peek().getPositionMatrix().mul((Matrix4fc) new Matrix4f().setPerspective((float) ((double) fov * (Math.PI / 180)), aspect, 0.05f, this.getFarPlaneDistance()));

            cir.setReturnValue(matrixStack.peek().getPositionMatrix());
        }
    }

    @Inject(method = {"tiltViewWhenHurt"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void tiltViewWhenHurtHook(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        NoOverlayModule module = Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class);
        if (module != null && module.enable && ((Boolean) module.delete.getValueByName("Тряску камеры").get()).booleanValue()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = {"renderWorld"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float badEffects(float original) {
        NoOverlayModule module = Sacura.getInstance().getModuleManager().getModule(NoOverlayModule.class);
        if (module != null && module.enable && ((Boolean) module.delete.getValueByName("Плохие эффекты").get()).booleanValue()) {
            return 0.0f;
        }
        return original;
    }
}