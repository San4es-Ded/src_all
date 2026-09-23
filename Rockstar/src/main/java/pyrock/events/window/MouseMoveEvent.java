package pyrock.events.window;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;
import rockstar.client.MinecraftClientAccess;

@EventName(internalMethod03601="mouse_move")
public class MouseMoveEvent
extends ClientEvent
implements MinecraftClientAccess {
    private final float x;
    private final float y;
    private final float dx;
    private final float dy;

    public MouseMoveEvent(double d, double d2, double d3, double d4) {
        double d5 = internalField0149.getWindow().getScaleFactor();
        this.x = (float)(d / d5);
        this.y = (float)(d2 / d5);
        this.dx = (float)(d3 / d5);
        this.dy = (float)(d4 / d5);
    }

    public boolean isScreenOpen() {
        return MouseMoveEvent.internalField0149.currentScreen != null;
    }

    @Generated
    public float getX() {
        return this.x;
    }

    @Generated
    public float getY() {
        return this.y;
    }

    @Generated
    public float getDx() {
        return this.dx;
    }

    @Generated
    public float getDy() {
        return this.dy;
    }
}

