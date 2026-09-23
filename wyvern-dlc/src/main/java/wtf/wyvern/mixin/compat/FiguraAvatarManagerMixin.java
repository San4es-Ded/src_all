package wtf.wyvern.mixin.compat;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.wyvern.integration.figura.FiguraPreviewContext;

import java.util.UUID;

@Mixin(value = AvatarManager.class, remap = false)
public abstract class FiguraAvatarManagerMixin {
    @Inject(method = "getAvatarForPlayer", at = @At("HEAD"), cancellable = true, remap = false)
    private static void wyvern$previewAvatar(UUID id, CallbackInfoReturnable<Avatar> cir) {
        Avatar preview = FiguraPreviewContext.current();
        if (preview != null) cir.setReturnValue(preview);
    }
}
