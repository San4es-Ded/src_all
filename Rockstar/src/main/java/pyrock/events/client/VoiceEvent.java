package pyrock.events.client;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;

public class VoiceEvent
extends ClientEvent {
    private final String text;

    @Generated
    public String getText() {
        return this.text;
    }

    @Generated
    public VoiceEvent(String string) {
        this.text = string;
    }
}

