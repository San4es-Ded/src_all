package ru.prism.mixin;

import net.minecraft.client.render.debug.EntityHitboxDebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.Client;
import ru.prism.module.impl.render.HitboxCustomizer;

@Mixin(EntityHitboxDebugRenderer.class)
public class EntityHitboxDebugRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void prism$suppressVanillaHitboxes(CallbackInfo ci) {
        if (Client.get() == null) return;
        HitboxCustomizer module = Client.get().moduleManager().get(HitboxCustomizer.class);
        if (module != null && module.isEnabled()) {
            ci.cancel();
        }
    }
}
