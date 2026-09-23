package ru.prism.mixin;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ru.prism.manager.event_impl.SoundPlayEvent;
import ru.prism.utils.other.VolumeScaledSoundInstance;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @ModifyVariable(require = 0, method = "play", at = @At("HEAD"), argsOnly = true)
    private SoundInstance prism$modifySound(SoundInstance instance) {
        if (instance == null || instance instanceof VolumeScaledSoundInstance) {
            return instance;
        }

        SoundPlayEvent event = new SoundPlayEvent(instance);
        event.hook();

        if (event.isCancelled()) {
            return null;
        }

        return event.getVolume() != 1.0F ? new VolumeScaledSoundInstance(instance, event.getVolume()) : instance;
    }
}
