package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.client.sound.SoundInstance;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="sound")
public class SoundEvent
extends ClientEvent {
    public SoundInstance sound;

    public SoundEvent(SoundInstance soundInstance) {
        this.sound = soundInstance;
    }

    @Generated
    public SoundInstance getSound() {
        return this.sound;
    }
}

