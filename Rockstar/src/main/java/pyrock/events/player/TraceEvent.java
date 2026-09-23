package pyrock.events.player;


import rockstar.client.event.*;
import lombok.Generated;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="trace")
public class TraceEvent
extends EventCancellable {
    private float yaw;
    private float pitch;

    @Generated
    public void setYaw(float f) {
        this.yaw = f;
    }

    @Generated
    public void setPitch(float f) {
        this.pitch = f;
    }

    @Generated
    public float getYaw() {
        return this.yaw;
    }

    @Generated
    public float getPitch() {
        return this.pitch;
    }

    @Generated
    public TraceEvent(float f, float f2) {
        this.yaw = f;
        this.pitch = f2;
    }
}

