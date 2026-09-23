package haron.modules.visuals.killeffect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

public class PendingKill {
    public final LivingEntity entity;
    public boolean triggered;
    private long expiresAt;

    public boolean shouldTrigger(MinecraftClient minecraftClient) {
        return !this.entity.isAlive() || this.entity.getHealth() <= 0.0f;
    }

    public PendingKill(LivingEntity livingEntity, long l) {
        this.entity = livingEntity;
        this.expiresAt = System.currentTimeMillis() + l;
    }

    public boolean matches(LivingEntity livingEntity) {
        return this.entity == livingEntity || this.entity.getId() == livingEntity.getId();
    }

    public boolean isActive() {
        return System.currentTimeMillis() < this.expiresAt;
    }

    public void refresh(long l) {
        this.expiresAt = System.currentTimeMillis() + l;
    }
}

