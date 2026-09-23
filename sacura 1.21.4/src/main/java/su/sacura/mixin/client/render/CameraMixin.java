package su.sacura.mixin.client.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.features.modules.impl.movement.freelook.CameraOverriddenEntity;
import su.sacura.features.modules.impl.movement.freelook.FreeLookState;
import su.sacura.features.modules.impl.render.BetterMinecraftModule;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({Camera.class})
public abstract class CameraMixin implements MinecraftWrapper {
    @Unique
    private boolean initialized = false;

    @Unique
    private Boolean lastThirdPerson;

    @Shadow
    protected abstract void setRotation(float paramFloat1, float paramFloat2);

    @Shadow
    protected abstract float clipToSpace(float paramFloat);

    // Исправлено: at = {@At(...)} -> at = @At(...) (без массива)
    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", shift = At.Shift.AFTER))
    private void onUpdate(CallbackInfo ci) {
        CameraOverriddenEntity entity;
        if (FreeLookState.active) {
            ClientPlayerEntity clientPlayerEntity = mc.player;
            if (clientPlayerEntity instanceof CameraOverriddenEntity) {
                entity = (CameraOverriddenEntity)clientPlayerEntity;
            } else {
                return;
            }
        } else {
            return;
        }
        if (!this.initialized) {
            entity.setCameraPitch(mc.player.getPitch());
            entity.setCameraYaw(mc.player.getYaw());
            this.initialized = true;
        }
        setRotation(entity.getCameraYaw(), entity.getCameraPitch());
    }

    // Исправлено: @Redirect -> @WrapOperation (нативный API MixinExtras)
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"))
    private float simplevisuals$smoothThirdPersonZoom(Camera instance, float desiredDistance, Operation<Float> original) {
        BetterMinecraftModule module = Sacura.getInstance().getModuleManager().getModule(BetterMinecraftModule.class);
        if (module == null || !module.enable || !((Boolean)module.smoothThirdPerson.get()).booleanValue())
            return original.call(instance, desiredDistance);

        Perspective perspective = MinecraftClient.getInstance().options.getPerspective();
        boolean isThirdPerson = (perspective != Perspective.FIRST_PERSON);
        if (this.lastThirdPerson == null || this.lastThirdPerson.booleanValue() != isThirdPerson) {
            module.getThirdPersonAnimation().reset();
            this.lastThirdPerson = Boolean.valueOf(isThirdPerson);
        }
        module.getThirdPersonAnimation().update(isThirdPerson);
        float factor = module.getThirdPersonAnimation().getValue();
        float baseOffset = isThirdPerson ? 0.35F : 0.0F;
        float animatedDistance = Math.max(baseOffset, desiredDistance * factor);
        return original.call(instance, animatedDistance);
    }
}