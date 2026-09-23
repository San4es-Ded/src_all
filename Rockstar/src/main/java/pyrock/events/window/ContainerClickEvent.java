package pyrock.events.window;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="container_click")
public class ContainerClickEvent
extends ClientEvent {
    private final float x;
    private final float y;
    private final int button;

    @Generated
    public float getX() {
        return this.x;
    }

    @Generated
    public float getY() {
        return this.y;
    }

    @Generated
    public int getButton() {
        return this.button;
    }

    @Generated
    public ContainerClickEvent(float f, float f2, int n) {
        this.x = f;
        this.y = f2;
        this.button = n;
    }
}

