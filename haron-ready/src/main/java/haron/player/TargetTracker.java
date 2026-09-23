package haron.player;

import haron.client.MinecraftClientAccess;
import haron.entity.EntityInterpolation;
import haron.events.ClientTickEvent;
import haron.hud.core.HudServiceInfo;
import haron.hud.core.HudService;
import haron.util.jeooat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

@HudServiceInfo
public class TargetTracker
extends HudService
implements MinecraftClientAccess {
    private static final long TARGET_MEMORY_MS = 1000L;
    private LivingEntity currentTarget;
    private final Map<LivingEntity, jeooat> recentlySeenTargets = new HashMap<LivingEntity, jeooat>();
    private final jeooat clearTimer = new jeooat();
    private boolean waitingToClearTarget;

    public void clearAll() {
        this.clearTarget();
        this.recentlySeenTargets.clear();
    }

    @EventHandler
    public void onClientTick(ClientTickEvent q8krcw2) {
        if (TargetTracker.c.player == null || TargetTracker.c.world == null) {
            return;
        }
        HitResult hitResult = TargetTracker.c.crosshairTarget;
        if (hitResult instanceof EntityHitResult && hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult)hitResult).getEntity();
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                if (entity != TargetTracker.c.player && !livingEntity.isInvisible() && !EntityInterpolation.a(livingEntity)) {
                    this.currentTarget = livingEntity;
                    this.recentlySeenTargets.put(livingEntity, new jeooat());
                    this.clearTimer.reset();
                    this.waitingToClearTarget = true;
                }
            }
        } else if (this.waitingToClearTarget && this.clearTimer.hasElapsed(1000L)) {
            this.currentTarget = null;
            this.waitingToClearTarget = false;
        }
        this.recentlySeenTargets.entrySet().removeIf(entry -> {
            LivingEntity livingEntity = (LivingEntity)entry.getKey();
            if (!((jeooat)entry.getValue()).hasElapsed(1000L)) {
                return false;
            }
            if (!(livingEntity instanceof PlayerEntity) || !livingEntity.isInvisible() || this.currentTarget != livingEntity) {
                return true;
            }
            this.currentTarget = null;
            this.waitingToClearTarget = false;
            return true;
        });
    }

    public boolean hasTarget() {
        return this.currentTarget != null;
    }

    public List<LivingEntity> recentTargets() {
        return new ArrayList<LivingEntity>(this.recentlySeenTargets.keySet());
    }

    public void clearTarget() {
        this.currentTarget = null;
        this.waitingToClearTarget = false;
        this.clearTimer.reset();
    }

    public List<LivingEntity> e() {
        return this.recentTargets();
    }

    public LivingEntity h() {
        return this.currentTarget();
    }

    public void f() {
        this.clearTarget();
    }

    public boolean d() {
        return this.hasTarget();
    }

    public void a(ClientTickEvent q8krcw2) {
        int n = 761;
        this.onClientTick(q8krcw2);
    }

    public void g() {
        int n = 191;
        this.clearAll();
    }

    public LivingEntity currentTarget() {
        return this.currentTarget;
    }
}

