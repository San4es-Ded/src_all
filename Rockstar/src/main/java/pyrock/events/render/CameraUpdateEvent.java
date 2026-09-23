package pyrock.events.render;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="camera_update")
public class CameraUpdateEvent
extends ClientEvent {
    private final Camera camera;
    private final Entity focusedEntity;
    private final boolean thirdPerson;
    private final boolean inverseView;
    private final float tickDelta;

    @Generated
    public Camera getCamera() {
        return this.camera;
    }

    @Generated
    public Entity getFocusedEntity() {
        return this.focusedEntity;
    }

    @Generated
    public boolean isThirdPerson() {
        return this.thirdPerson;
    }

    @Generated
    public boolean isInverseView() {
        return this.inverseView;
    }

    @Generated
    public float getTickDelta() {
        return this.tickDelta;
    }

    @Generated
    public CameraUpdateEvent(Camera camera, Entity entity, boolean bl, boolean bl2, float f) {
        this.camera = camera;
        this.focusedEntity = entity;
        this.thirdPerson = bl;
        this.inverseView = bl2;
        this.tickDelta = f;
    }
}

