package wtf.wyvern.core.events.impl.player;

import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.callables.EventCancellable;

public final class EventOnTravelPost extends EventCancellable {
    private Vec3d oldVelocity;

    public EventOnTravelPost(Vec3d oldVelocity) {
        this.oldVelocity = oldVelocity;
    }

    public Vec3d getOldVelocity() {
        return oldVelocity;
    }

    public void setOldVelocity(Vec3d oldVelocity) {
        this.oldVelocity = oldVelocity;
    }
}
