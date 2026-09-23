package ru.prism.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.manager.event_impl.EventDisconnect;

@Mixin(MinecraftClient.class)
public class MinecraftClientDisconnectEventMixin {

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void prism$onDisconnected(CallbackInfo ci) {
        new EventDisconnect().hook();
    }
}
