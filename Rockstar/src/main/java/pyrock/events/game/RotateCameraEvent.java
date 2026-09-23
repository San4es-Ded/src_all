package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="rotate_camera")
public class RotateCameraEvent
extends EventCancellable {
    private float deltaYaw;
    private float deltaPitch;

    @Generated
    public void setDeltaYaw(float f) {
        this.deltaYaw = f;
    }

    @Generated
    public void setDeltaPitch(float f) {
        this.deltaPitch = f;
    }

    @Generated
    public float getDeltaYaw() {
        return this.deltaYaw;
    }

    @Generated
    public float getDeltaPitch() {
        return this.deltaPitch;
    }

    @Generated
    public RotateCameraEvent(float f, float f2) {
        this.deltaYaw = f;
        this.deltaPitch = f2;
    }
}

