package haron.events;

import haron.events.CancellableEvent;
import net.minecraft.client.sound.SoundInstance;

public class SoundPlayEvent
extends CancellableEvent {
    private final SoundInstance sound;
    private float volume;
    private float pitch;

    public float volume() {
        return this.volume;
    }

    public void setPitch(float f) {
        this.pitch = f;
    }

    public SoundInstance sound() {
        return this.sound;
    }

    public void setVolume(float f) {
        this.volume = f;
    }

    public float pitch() {
        return this.pitch;
    }

    public SoundPlayEvent(SoundInstance soundInstance) {
        this.sound = soundInstance;
        try {
            this.volume = soundInstance.getVolume();
            this.pitch = soundInstance.getPitch();
        }
        catch (NullPointerException nullPointerException) {
            this.volume = 1.0f;
            this.pitch = 1.0f;
        }
    }

    public float e() {
        return this.volume;
    }

    public void b(float f) {
        this.pitch = f;
    }

    public float f() {
        return this.pitch;
    }

    public SoundInstance d() {
        return this.sound;
    }

    public void a(float f) {
        this.volume = f;
    }
}

