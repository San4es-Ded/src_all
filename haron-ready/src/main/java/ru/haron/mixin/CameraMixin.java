package ru.haron.mixin;

import haron.animation.PerspectiveDistanceAnimation;
import haron.module.ModuleManager;
import haron.modules.utilities.FreeLook;
import haron.modules.visuals.Animations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Camera.class})
public abstract class CameraMixin {
    @Shadow
    protected abstract void setRotation(float var1, float var2);

    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal=0))
    private void freeLookSetRotation(Camera cameraVar, float yaw, float pitch) {
        if (FreeLook.active && MinecraftClient.getInstance().getCameraEntity() instanceof ClientPlayerEntity) {
            this.setRotation(FreeLook.freeYaw, FreeLook.freePitch);
        } else {
            this.setRotation(yaw, pitch);
        }
    }

    @Unique
    private boolean isPerspectiveAnimationEnabled() {
        return ModuleManager.ANIMATIONS.k() && ModuleManager.ANIMATIONS.perspectiveEnabled.get();
    }

    @Inject(method={"update"}, at={@At(value="HEAD")})
    private void onUpdateHead(BlockView blockViewVar, Entity entityVar, boolean z, boolean z2, float f, CallbackInfo callbackInfo) {
        if (this.isPerspectiveAnimationEnabled()) {
            PerspectiveDistanceAnimation distanceAnimation = Animations.perspectiveDistanceAnimation();
            distanceAnimation.setThirdPerson(z, (long)(ModuleManager.ANIMATIONS.perspectiveDuration.get() * 1.5f));
            distanceAnimation.update();
        }
        if (ModuleManager.RENDER_TWEAKS.o()) {
            MinecraftClient.getInstance().options.getBobView().setValue(false);
        }
    }

    @ModifyArg(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/Camera;clipToSpace(F)F"), index=0)
    private float modifyCameraDistance(float f) {
        return !this.isPerspectiveAnimationEnabled() || MinecraftClient.getInstance().options.getPerspective() == Perspective.FIRST_PERSON
                ? f
                : Animations.perspectiveDistanceAnimation().distance();
    }
}
