package pyrock.events.game;


import rockstar.client.event.*;
import java.util.Collections;
import java.util.List;
import lombok.Generated;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import rockstar.client.event.ClientEvent;

public class AncientDebrisEvent
extends ClientEvent {
    private final List<BlockPos> positions;
    private final Vec3d explosionCenter;

    public AncientDebrisEvent(List<BlockPos> list, Vec3d vec3d) {
        this.positions = Collections.unmodifiableList(list);
        this.explosionCenter = vec3d;
    }

    @Generated
    public List<BlockPos> getPositions() {
        return this.positions;
    }

    @Generated
    public Vec3d getExplosionCenter() {
        return this.explosionCenter;
    }
}

