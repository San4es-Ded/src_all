package haron.events;

import net.minecraft.entity.Entity;

public class AttackTargetEvent {
    private final Entity entity;

    public AttackTargetEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity a() {
        return this.entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

