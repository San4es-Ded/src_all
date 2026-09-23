package wtf.wyvern.client.modules.impl.combat.rotation;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.math.MathUtil;
import wtf.wyvern.utility.math.MultipointUtils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@FastNative
public final class FunTimeRotation extends RotationBase {
    private float basePitch = 0.0f;
    private final Random rand = new Random();
    private float currentYawOffset = 0f;
    private float currentPitchOffset = 0f;
    private float targetYawOffset = 0f;
    private float targetPitchOffset = 0f;
    private int lastTargetId = -1;
    private boolean wasAttacking = false;
    private long nextOffsetTime = 0;
    private static final long OFFSET_INTERVAL_MIN = 7000L;
    private static final long OFFSET_INTERVAL_MAX = 10000L;
    private static final float OFFSET_DAMP = 0.8f;
    private static final float OFFSET_DAMP_INSTANT = 1.0f;
    private static final float MAX_STEP = 180f;
    private static final float STEP_INTERPOLATION = 0.55f;

    public void update(LivingEntity target, boolean attack, float distance) {
        if (mc.player == null || target == null) return;

        if (target.getId() != lastTargetId) {
            resetState(target);
        }

        Vec3d point = MultipointUtils.getNearestPoint(target, distance).subtract(mc.player.getEyePos());

        float baseYaw = (float) Math.toDegrees(Math.atan2(-point.x, point.z));
        basePitch = (float) MathHelper.clamp(
                -Math.toDegrees(Math.atan2(point.y, Math.hypot(point.x, point.z))),
                -90.0, 90.0
        );

        long now = System.currentTimeMillis();

        if (attack) {
            targetYawOffset = 0f;
            targetPitchOffset = 0f;
            currentYawOffset = 0f;
            currentPitchOffset = 0f;
            nextOffsetTime = 0;
        } else {
            if (wasAttacking) {
                generateOffset(now, true);
            } else if (now >= nextOffsetTime) {
                generateOffset(now, false);
            }

            float damp = wasAttacking ? OFFSET_DAMP_INSTANT : OFFSET_DAMP;
            currentYawOffset += (targetYawOffset - currentYawOffset) * damp;
            currentPitchOffset += (targetPitchOffset - currentPitchOffset) * damp;
        }
        wasAttacking = attack;

        float targetYaw = baseYaw + currentYawOffset;
        float targetPitch = MathHelper.clamp(basePitch + currentPitchOffset, -90f, 90f);

        float currentYaw = mc.player.getYaw();
        float currentPitch = mc.player.getPitch();
        float deltaYaw = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float deltaPitch = targetPitch - currentPitch;

        float hypot = (float) Math.hypot(Math.abs(deltaYaw), Math.abs(deltaPitch));
        if (hypot < 1e-4f) hypot = 1e-4f;

        boolean isInOffset = Math.abs(currentYawOffset) > 0.5f || Math.abs(currentPitchOffset) > 0.5f;
        float speedBase = (attack || isInOffset)
                ? MathUtil.random(100, 130)
                : MathUtil.random(40, 60);

        float maxYawSpeed = Math.abs(deltaYaw / hypot) * speedBase;
        float maxPitchSpeed = Math.abs(deltaPitch / hypot) * speedBase;

        float shakeYaw = (float) (randomRange(4, 7) * Math.sin(System.currentTimeMillis() / 60.0));
        float shakePitch = (float) (randomRange(3, 7) * Math.cos(System.currentTimeMillis() / 60.0));

        float stepYaw = MathHelper.lerp(STEP_INTERPOLATION, 0f, deltaYaw);
        float stepPitch = MathHelper.lerp(STEP_INTERPOLATION, 0f, deltaPitch);
        stepYaw = Math.signum(stepYaw) * Math.min(Math.abs(stepYaw), MAX_STEP);
        stepPitch = Math.signum(stepPitch) * Math.min(Math.abs(stepPitch), MAX_STEP);

        float newYaw = currentYaw + stepYaw;
        basePitch = currentPitch + stepPitch;

        int age = mc.player.age;
        if (age % 15 == 0 && age > 0) {
            basePitch += -4.0f;
        }

        lastYaw = newYaw + shakeYaw;
        lastPitch = MathHelper.clamp(basePitch + shakePitch, -90f, 90f);
        float yRet = MathUtil.random(22.0f, 32.0f);
        float pRet = yRet * 0.85f;
        RotationComponent.update(
                new Rotation(lastYaw, lastPitch),
                maxYawSpeed,
                maxPitchSpeed,
                yRet,
                pRet,
                0,
                15,
                false
        );
    }

    private void generateOffset(long now, boolean postAttack) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();

        if (postAttack) {
            targetYawOffset = rng.nextFloat(-70f, 70f);
            targetPitchOffset = rng.nextFloat(-60f, 60f);
        } else if (rng.nextBoolean()) {
            targetYawOffset = rng.nextFloat(-180f, 180f);
            targetPitchOffset = rng.nextFloat(-90f, 90f);
        } else {
            targetYawOffset = rng.nextFloat(-20f, 20f);
            targetPitchOffset = rng.nextFloat(-15f, 15f);
        }

        nextOffsetTime = now + OFFSET_INTERVAL_MIN
                + rng.nextInt((int) (OFFSET_INTERVAL_MAX - OFFSET_INTERVAL_MIN));
    }

    private void resetState(LivingEntity target) {
        currentYawOffset = 0f;
        currentPitchOffset = 0f;
        targetYawOffset = 0f;
        targetPitchOffset = 0f;
        wasAttacking = false;
        nextOffsetTime = System.currentTimeMillis() + OFFSET_INTERVAL_MIN
                + ThreadLocalRandom.current().nextInt((int) (OFFSET_INTERVAL_MAX - OFFSET_INTERVAL_MIN));
        lastTargetId = target != null ? target.getId() : -1;
    }

    private float randomRange(int min, int max) {
        return min + rand.nextInt(max - min + 1);
    }

    public void reset() {
        currentYawOffset = 0f;
        currentPitchOffset = 0f;
        targetYawOffset = 0f;
        targetPitchOffset = 0f;
        wasAttacking = false;
        nextOffsetTime = 0;
        lastTargetId = -1;
        if (mc.player != null) {
            basePitch = mc.player.getPitch();
            lastYaw = mc.player.getYaw();
            lastPitch = mc.player.getPitch();
        }
    }

    @Override
    public void update(Rotation targetAngle, boolean elytraVisual) {
        // FunTime uses entity + attack state from Aura.
    }

    private int getPing() {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) return entry.getLatency();
        }
        return 50;
    }
}
