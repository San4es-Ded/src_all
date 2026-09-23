package pyrock.events.player;


import rockstar.client.event.*;
import net.minecraft.util.math.Vec3d;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="travel_post")
public class EventOnTravelPost
extends ClientEvent {
    private Vec3d oldVelocity;

    public EventOnTravelPost(Vec3d vec3d) {
        this.oldVelocity = vec3d;
    }

    public Vec3d getOldVelocity() {
        return this.oldVelocity;
    }

    public void setOldVelocity(Vec3d vec3d) {
        this.oldVelocity = vec3d;
    }
}

