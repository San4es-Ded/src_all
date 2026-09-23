package pyrock.events.window;


import rockstar.client.event.*;
import lombok.Generated;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;
import rockstar.client.MinecraftClientAccess;

@EventName(internalMethod03601="scroll")
public class ScrollEvent
extends EventCancellable
implements MinecraftClientAccess {
    private final double horizontal;
    private final double vertical;
    private final float x;
    private final float y;

    public ScrollEvent(double d, double d2) {
        this.horizontal = d;
        this.vertical = d2;
        double d3 = internalField0149.getWindow().getScaleFactor();
        this.x = (float)(ScrollEvent.internalField0149.mouse.getX() / d3);
        this.y = (float)(ScrollEvent.internalField0149.mouse.getY() / d3);
    }

    public boolean isUp() {
        return this.vertical > 0.0;
    }

    public boolean isDown() {
        return this.vertical < 0.0;
    }

    public boolean isScreenOpen() {
        return ScrollEvent.internalField0149.currentScreen != null;
    }

    @Generated
    public double getHorizontal() {
        return this.horizontal;
    }

    @Generated
    public double getVertical() {
        return this.vertical;
    }

    @Generated
    public float getX() {
        return this.x;
    }

    @Generated
    public float getY() {
        return this.y;
    }
}

