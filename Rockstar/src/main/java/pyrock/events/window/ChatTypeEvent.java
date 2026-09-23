package pyrock.events.window;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;

public class ChatTypeEvent
extends ClientEvent {
    private final char text;
    private final int modifiers;

    @Generated
    public char getText() {
        return this.text;
    }

    @Generated
    public int getModifiers() {
        return this.modifiers;
    }

    @Generated
    public ChatTypeEvent(char c, int n) {
        this.text = c;
        this.modifiers = n;
    }
}

