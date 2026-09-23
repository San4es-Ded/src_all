package pyrock.events.player;


import rockstar.client.event.*;
import net.minecraft.util.math.Vec3d;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="move_post")
public class EventOnMovePost
extends ClientEvent {
    private final float speed;
    private final Vec3d movementInput;

    public EventOnMovePost(float f, Vec3d vec3d) {
        this.speed = f;
        this.movementInput = vec3d;
    }

    public float getSpeed() {
        return this.speed;
    }

    public Vec3d getMovementInput() {
        return this.movementInput;
    }
}

