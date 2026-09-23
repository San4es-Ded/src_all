package pyrock.events.window;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;

public class MouseEvent
extends ClientEvent {
    private final int button;
    private final int action;

    @Generated
    public int getButton() {
        return this.button;
    }

    @Generated
    public int getAction() {
        return this.action;
    }

    @Generated
    public MouseEvent(int n, int n2) {
        this.button = n;
        this.action = n2;
    }
}

