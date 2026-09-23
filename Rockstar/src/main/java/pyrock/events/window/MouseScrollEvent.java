package pyrock.events.window;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;

public class MouseScrollEvent
extends ClientEvent {
    private final double verticalAmount;

    @Generated
    public double getVerticalAmount() {
        return this.verticalAmount;
    }

    @Generated
    public MouseScrollEvent(double d) {
        this.verticalAmount = d;
    }
}

