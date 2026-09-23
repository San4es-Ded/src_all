package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.entity.Entity;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="attack")
public class InternalAttackEvent
extends EventCancellable {
    private final Entity entity;

    @Generated
    public Entity getEntity() {
        return this.entity;
    }

    @Generated
    public InternalAttackEvent(Entity entity) {
        this.entity = entity;
    }
}

