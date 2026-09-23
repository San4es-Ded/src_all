package haron.events;

import net.minecraft.entity.Entity;

public class CriticalHitEvent {
    private final Entity entity;

    public CriticalHitEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity a() {
        return this.entity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

