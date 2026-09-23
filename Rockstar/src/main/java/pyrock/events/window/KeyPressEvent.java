package pyrock.events.window;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;

public class KeyPressEvent
extends ClientEvent {
    private final int action;
    private final int key;

    @Generated
    public int getAction() {
        return this.action;
    }

    @Generated
    public int getKey() {
        return this.key;
    }

    @Generated
    public KeyPressEvent(int n, int n2) {
        this.action = n;
        this.key = n2;
    }
}

