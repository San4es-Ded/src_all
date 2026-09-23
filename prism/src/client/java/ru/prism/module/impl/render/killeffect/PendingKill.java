package ru.prism.module.impl.render.killeffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

public class PendingKill {

    public final LivingEntity entity;
    public boolean triggered;

    private Vec3d lastPos;
    private long expiresAt;

    public PendingKill(LivingEntity entity, long memoryMs) {
        this.entity = entity;
        this.lastPos = entity.getEntityPos();
        this.expiresAt = System.currentTimeMillis() + memoryMs;
    }

    public boolean shouldTrigger() {
        return entity == null || !entity.isAlive() || entity.isRemoved() || entity.getHealth() <= 0.0F;
    }

    public boolean matches(LivingEntity other) {
        return entity == other || (entity != null && other != null && entity.getId() == other.getId());
    }

    public boolean isActive() {
        return System.currentTimeMillis() < expiresAt;
    }

    public void refresh(long memoryMs) {
        this.expiresAt = System.currentTimeMillis() + memoryMs;
    }

    public void update() {
        if (entity != null && entity.isAlive()) {
            this.lastPos = entity.getEntityPos();
        }
    }

    public Vec3d position() {
        if (entity != null && !entity.isRemoved()) {
            return entity.getEntityPos();
        }
        return lastPos != null ? lastPos : Vec3d.ZERO;
    }

    public float radius() {
        return entity != null ? Math.max(0.65F, entity.getWidth()) : 0.65F;
    }
}
