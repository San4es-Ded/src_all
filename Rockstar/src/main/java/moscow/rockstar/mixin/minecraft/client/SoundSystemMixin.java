package moscow.rockstar.mixin.minecraft.client;


import rockstar.client.internal.script.*;
import net.minecraft.client.sound.ElytraSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.client.internal.script.SoundFilterHandler;
import rockstar.modules.movement.SpeedModule;

@Mixin(value={SoundSystem.class})
public class SoundSystemMixin {
    @Inject(method={"play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPlaySound(SoundInstance soundInstance, CallbackInfoReturnable<SoundSystem.PlayResult> callbackInfo) {
        if (soundInstance instanceof ElytraSoundInstance && SpeedModule.internalMethod09255()) {
            callbackInfo.setReturnValue(SoundSystem.PlayResult.NOT_STARTED);
            return;
        }
        SoundFilterHandler.internalMethod01687(soundInstance, callbackInfo);
        if (callbackInfo.isCancelled()) {
            callbackInfo.setReturnValue(SoundSystem.PlayResult.NOT_STARTED);
        }
    }
}
