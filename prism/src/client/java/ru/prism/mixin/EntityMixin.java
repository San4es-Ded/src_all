package ru.prism.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import ru.prism.module.impl.utils.FreeLook;
import ru.prism.utils.annotation.IMinecraft;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements IMinecraft {
    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void prism$changeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if (FreeLook.active && (Object) this instanceof ClientPlayerEntity) {
            FreeLook.cameraYaw += (float) (cursorDeltaX * 0.15);
            FreeLook.cameraPitch = MathHelper.clamp(FreeLook.cameraPitch + (float) (cursorDeltaY * 0.15), -90.0F, 90.0F);
            ci.cancel();
        }
    }

    @ModifyExpressionValue(
            method = "move",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;isLogicalSideForUpdatingMovement()Z",
                    ordinal = 1
            )
    )
    public boolean fixFalldistanceValue(boolean original) {
        if ((Object) this == mc.player) {
            return true;
        }

        return original;
    }
}
