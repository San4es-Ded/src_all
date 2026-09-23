package ru.prism.manager.event_impl;

import net.minecraft.client.sound.SoundInstance;
import ru.prism.manager.events.CancellableEvent;

public class SoundPlayEvent extends CancellableEvent {
    private final SoundInstance sound;
    private float volume = 1.0F;
    private float pitch = 1.0F;

    public SoundPlayEvent(SoundInstance sound) {
        this.sound = sound;

        try {
            this.pitch = sound.getPitch();
        } catch (NullPointerException ignored) {
            this.pitch = 1.0F;
        }
    }

    public SoundInstance getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
