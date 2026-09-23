package ru.haron.mixin;

import haron.events.SoundPlayEvent;
import haron.events.EventDispatcher;
import haron.util.sound.k9fqgt;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={SoundSystem.class})
public class SoundSystemMixin {
    @ModifyVariable(method={"play(Lnet/minecraft/client/sound/SoundInstance;)V"}, at=@At(value="HEAD"), argsOnly=true)
    private SoundInstance modifySound(SoundInstance SoundInstanceVar) {
        if (SoundInstanceVar == null || SoundInstanceVar instanceof k9fqgt) {
            return SoundInstanceVar;
        }
        SoundPlayEvent soundPlayEvent = new SoundPlayEvent(SoundInstanceVar);
        EventDispatcher.EVENT_BUS.post((Object)soundPlayEvent);
        if (soundPlayEvent.c()) {
            return null;
        }
        return soundPlayEvent.e() != 1.0f ? new k9fqgt(SoundInstanceVar, soundPlayEvent.e()) : SoundInstanceVar;
    }
}

