package wtf.wyvern.core.events.impl.player;

import lombok.Generated;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.eventbus.events.Event;

public class EventOnMovePost implements Event {
    private final float speed;
    private final Vec3d movementInput;

    @Generated
    public EventOnMovePost(float speed, Vec3d movementInput) {
        this.speed = speed;
        this.movementInput = movementInput;
    }

    @Generated
    public float getSpeed() {
        return this.speed;
    }

    @Generated
    public Vec3d getMovementInput() {
        return this.movementInput;
    }
}
