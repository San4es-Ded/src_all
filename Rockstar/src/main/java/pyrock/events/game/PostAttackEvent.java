package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.entity.Entity;
import rockstar.client.event.ClientEvent;

public class PostAttackEvent
extends ClientEvent {
    private final Entity entity;

    public PostAttackEvent(Entity entity) {
        this.entity = entity;
    }

    @Generated
    public Entity getEntity() {
        return this.entity;
    }
}

