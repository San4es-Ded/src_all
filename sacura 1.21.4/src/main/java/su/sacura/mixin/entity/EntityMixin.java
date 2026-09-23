package su.sacura.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.features.modules.impl.movement.freelook.CameraOverriddenEntity;
import su.sacura.features.modules.impl.movement.freelook.FreeLookState;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({Entity.class})
public abstract class EntityMixin implements CameraOverriddenEntity, MinecraftWrapper {
    @Unique private float cameraYaw;
    @Unique private float cameraPitch;

    @Inject(method = {"changeLookDirection"}, at = {@At("HEAD")}, cancellable = true)
    private void onChangeLookDirection(double deltaX, double deltaY, CallbackInfo ci) {
        // Исправлено: (Entity)this -> (Entity)(Object)this
        Entity self = (Entity)(Object)this;
        if (FreeLookState.active && self instanceof net.minecraft.client.network.ClientPlayerEntity) {
            this.cameraYaw += (float)deltaX * 0.15F;
            this.cameraPitch = MathHelper.clamp(this.cameraPitch + (float)deltaY * 0.15F, -90.0F, 90.0F);
            ci.cancel();
        }
    }

    public float getCameraPitch() { return this.cameraPitch; }
    public float getCameraYaw() { return this.cameraYaw; }
    public void setCameraPitch(float pitch) { this.cameraPitch = pitch; }
    public void setCameraYaw(float yaw) { this.cameraYaw = yaw; }
}