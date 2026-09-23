package wtf.wyvern.mixin.compat;

import org.figuramc.figura.backend2.AuthHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AuthHandler.class, remap = false)
public abstract class FiguraAuthHandlerMixin {
    @Inject(method = "auth", at = @At("HEAD"), cancellable = true, remap = false)
    private static void wyvern$disableBackendAuth(boolean force, CallbackInfo ci) {
        ci.cancel();
    }
}
