package wtf.wyvern.core.events.impl.player;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.callables.EventCancellable;

public final class EventGlidingVelocity extends EventCancellable {
    private final LivingEntity entity;
    private Vec3d velocity;

    public EventGlidingVelocity(LivingEntity entity, Vec3d velocity) {
        this.entity = entity;
        this.velocity = velocity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }
}
