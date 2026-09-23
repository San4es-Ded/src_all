package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="firework")
public class FireworkEvent
extends ClientEvent {
    private final LivingEntity entity;
    private Vec3d velocity;
    private final FireworkRocketEntity rocketEntity;

    @Generated
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Generated
    public Vec3d getVelocity() {
        return this.velocity;
    }

    @Generated
    public FireworkRocketEntity getRocketEntity() {
        return this.rocketEntity;
    }

    @Generated
    public void setVelocity(Vec3d vec3d) {
        this.velocity = vec3d;
    }

    @Generated
    public FireworkEvent(LivingEntity livingEntity, Vec3d vec3d, FireworkRocketEntity fireworkRocketEntity) {
        this.entity = livingEntity;
        this.velocity = vec3d;
        this.rocketEntity = fireworkRocketEntity;
    }
}

