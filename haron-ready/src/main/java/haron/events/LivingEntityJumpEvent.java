package haron.events;

import net.minecraft.entity.LivingEntity;

public class LivingEntityJumpEvent {
    private final LivingEntity entity;

    public LivingEntity entity() {
        return this.entity;
    }

    public LivingEntityJumpEvent(LivingEntity livingEntity) {
        this.entity = livingEntity;
    }

    public LivingEntity a() {
        return this.entity;
    }
}

