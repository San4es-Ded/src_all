package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.entity.LivingEntity;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="entity_jump")
public class EntityJumpEvent
extends EventCancellable {
    private final LivingEntity entity;

    @Generated
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Generated
    public EntityJumpEvent(LivingEntity livingEntity) {
        this.entity = livingEntity;
    }
}

