package ru.prism.mixin;

import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.prism.module.impl.render.AspectRatio;

@Mixin(GameRenderer.class)
public abstract class GameRendererAspectRatioMixin {

    @Shadow
    public abstract float getFarPlaneDistance();

    @Inject(method = "getBasicProjectionMatrix", at = @At("HEAD"), cancellable = true)
    private void prism$applyAspectRatio(float fov, CallbackInfoReturnable<Matrix4f> cir) {
        AspectRatio aspectRatio = AspectRatio.getInstance();
        if (aspectRatio == null || !aspectRatio.isEnabled()) return;

        cir.setReturnValue(new Matrix4f().perspective(
                fov * (float) (Math.PI / 180.0),
                aspectRatio.getAspectRatio(),
                0.05F,
                this.getFarPlaneDistance()
        ));
    }
}
