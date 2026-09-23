package wtf.wyvern.client.modules.impl.combat.rotation;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.utility.component.FreeLookComponent;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.GCDFixer;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.interfaces.IMinecraft;

import java.util.concurrent.ThreadLocalRandom;


@FastNative
public class ShardRotation extends RotationBase implements IMinecraft {

    private final Aura aura;
    private final AttackTracker attackTracker = new AttackTracker();
    private final ShakeFixer shakeFixer = new ShakeFixer();

    private LivingEntity target;

    private float currentYaw;
    private float currentPitch;

    private float shakePhase;

    private int ticks;

    private boolean rising;

    private boolean initialized;

    private long lastAttackTime;
    private long attackDelay;
    private float yawOffsetStrength;

    private float pitchCurve = 1.2f;

    private float pitchOffsetStrength;

    private int attackDirection = 1;

    private int lastAttackDirection;

    private float lastYawOffset;

    private float progress;

    private Vec2f rotate = new Vec2f(0.0f, 0.0f);

    public ShardRotation() {
        this(Aura.INSTANCE);
    }

    public ShardRotation(Aura aura) {
        this.aura = aura;
    }

    public void reset() {
        target = null;
        progress = 0.0f;
        rising = false;
        shakePhase = 0.0f;
        ticks = 0;
        lastAttackTime = 0L;
        attackDelay = 0L;
        yawOffsetStrength = 0.0f;
        Aura.otvodkaYaw = 0.0f;
        Aura.otvodkaPitch = 0.0f;
        attackTracker.reset();
        initialized = mc.player != null;
        if (mc.player != null) {
            currentYaw = mc.player.getYaw();
            currentPitch = mc.player.getPitch();
        } else {
            currentYaw = 0.0f;
            currentPitch = 0.0f;
        }
        lastYaw = currentYaw;
        lastPitch = currentPitch;
    }

    public int onAttack() {
        attackDirection = ThreadLocalRandom.current().nextBoolean() ? 1 : -1;
        if (attackDirection == lastAttackDirection) {
            attackDirection = -lastAttackDirection;
        }
        lastAttackDirection = attackDirection;
        lastAttackTime = System.currentTimeMillis();

        // задержка отводки: 260-420 мс
        attackDelay = 260L + ThreadLocalRandom.current().nextLong(160L);

        // сила отводки по yaw: 7.2-15.6 градусов, не повторяем слишком близкое значение
        float min = 7.2f;
        float max = 15.6f;
        float strength = min + ThreadLocalRandom.current().nextFloat() * (max - min);
        if (Math.abs(strength - lastYawOffset) < 2.4f) {
            strength = strength + 3.6f > max ? min : strength + 3.6f;
        }
        yawOffsetStrength = strength;
        lastYawOffset = strength;

        progress = 0.0f;
        // форма кривой отводки по питчу: 1.15-1.5
        pitchCurve = 1.15f + ThreadLocalRandom.current().nextFloat() * 0.35f;
        return 0;
    }

    public float getCooldownGate() {
        return 0.88f;
    }

    public boolean isWhiffArmed() {
        return false;
    }

    public void returnToCamera() {
        if (mc.player == null) {
            return;
        }
        float freeYaw = FreeLookComponent.getFreeYaw();
        float freePitch = FreeLookComponent.getFreePitch();
        mc.player.setYaw(freeYaw);
        mc.player.setPitch(freePitch);
        rotate = new Vec2f(freeYaw, freePitch);
        currentYaw = freeYaw;
        currentPitch = freePitch;
        lastYaw = freeYaw;
        lastPitch = freePitch;
        if (RotationComponent.instance != null) {
            RotationComponent.instance.stopRotation();
        }
    }


    private float wave(float amplitude, boolean cos, float period) {
        float t = (float) (System.currentTimeMillis() % 1000L) / period;
        return (float) (cos ? Math.cos(t) : Math.sin(t)) * amplitude;
    }

    public static Vec2f getRotations(Vec3d point) {
        if (mc.player == null) return new Vec2f(0.0f, 0.0f);
        double deltaX = point.x - mc.player.getX();
        double deltaY = point.y - mc.player.getEyeY();
        double deltaZ = point.z - mc.player.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float yaw = (float) (MathHelper.atan2(deltaZ, deltaX) * 57.29577951308232 - 90.0);
        float pitch = (float) (-MathHelper.atan2(deltaY, distance) * 57.29577951308232);
        return new Vec2f(yaw, pitch);
    }

    public void update(LivingEntity target, Rotation targetAngle, boolean elytraVisual, int ticksToAttack) {
        updateRotations(target);
    }

    public void updateRotations(LivingEntity entity) {
        if (attackTracker.update()) {
            onAttack();
        }

        if (mc.player == null || entity == null) {
            return;
        }

        // верхом — не трогаем камеру
        if (mc.player.isRiding()) {
            rotate = new Vec2f(mc.player.getYaw(), mc.player.getPitch());
            currentYaw = rotate.x;
            currentPitch = rotate.y;
            lastYaw = currentYaw;
            lastPitch = currentPitch;
            return;
        }

        // первичная инициализация
        if (!initialized) {
            currentYaw = mc.player.getYaw();
            currentPitch = mc.player.getPitch();
            lastYaw = currentYaw;
            lastPitch = currentPitch;
            initialized = true;
        }

        // смена цели — полный сброс состояния
        if (target != entity) {
            target = entity;
            progress = 0.0f;
            rising = false;
            ticks = 0;
            lastAttackTime = 0L;
            attackDelay = 0L;
            Aura.otvodkaYaw = 0.0f;
            Aura.otvodkaPitch = 0.0f;
        }

        ++ticks;

        // фаза дрожания: медленный синус + шум
        shakePhase = (float) ((Math.sin(ticks * 0.134) * 0.42 + (Math.random() * 0.09 - 0.21)) * 0.33);

        // "дышащая" точка прицеливания по хитбоксу цели
        Box box = entity.getBoundingBox();
        double width = box.maxX - box.minX;
        double height = box.maxY - box.minY;
        double depth = box.maxZ - box.minZ;

        float t = (float) (System.currentTimeMillis() % 1000000L) / 1000.0f;
        int seed = entity.getId(); // Yarn: Entity.getId()

        double wobbleX = Math.sin(t * 1.43f + seed) * (width * 0.38);
        double wobbleY = Math.sin(t * 0.89f + seed * 2.1f) * (height * 0.22) + height * 0.55;
        double wobbleZ = Math.cos(t * 1.67f + seed * 3.7f) * (depth * 0.38);

        Vec3d point = new Vec3d(
                box.minX + width * 0.5 + wobbleX,
                box.minY + wobbleY,
                box.minZ + depth * 0.5 + wobbleZ);

        Vec2f rot = getRotations(point);

        // окно удержания отводки: 350-500 мс цикла
        float hold = System.currentTimeMillis() % 500L >= 350L ? 0.0f : 1.0f;

        float yawOffset = 0.0f;
        float pitchOffset = 0.0f;

        if (hold == 1.0f && lastAttackTime > 0L) {
            long elapsed = System.currentTimeMillis() - lastAttackTime;
            if (elapsed < attackDelay && attackDelay > 0L) {
                float t2 = MathHelper.clamp((float) elapsed / (float) attackDelay, 0.0f, 1.0f);
                // кривая отводки: sin(pi * t^curve)^2
                double phase = Math.PI * Math.pow(t2, pitchCurve);
                double power = Math.sin(phase);
                float p = (float) (power * power);
                yawOffset = attackDirection * yawOffsetStrength * p;
                pitchOffset = pitchOffsetStrength * p;
            } else {
                lastAttackTime = 0L;
            }
        }

        Aura.otvodkaYaw = yawOffset;
        Aura.otvodkaPitch = pitchOffset;

        float targetYaw = rot.x + yawOffset;
        float targetPitch = rot.y + pitchOffset;

        float deltaYaw = Math.abs(MathHelper.wrapDegrees(targetYaw - currentYaw));

        // "взрыв" атаки: спринт и осталось <=1 тик до удара
        // Yarn: method_7261 = isMoving() (1.0f - tick delta)
        boolean shouldHit = mc.player.getAttackCooldownProgress(1.0f) > 0.7f && Aura.getWhiteRiseTicksToAttack() <= 1;

        // раскачка прицела (прогресс -0.1 .. 0.07+)
        if (!rising) {
            float rate = 0.052f;
            rate += deltaYaw > 60.0f ? 0.032f : (deltaYaw > 50.0f ? 0.052f : 0.065f);
            if (shouldHit) {
                rate += 0.038f;
            }
            progress += rate * (0.0f + shakePhase);
            if (progress >= 0.07f) {
                rising = true;
            }
        } else {
            float rate = shouldHit ? 0.025f : 0.008f;
            progress -= rate * (3.0f + shakePhase);
            if (progress <= -0.1f) {
                rising = false;
            }
        }

        // скорость довода
        float speed = MathHelper.clamp(progress, 0.0f, mc.player.isSprinting() ? 6.0f : 5.2f);
        if (shouldHit) {
            speed = Math.min(speed + 0.08f, mc.player.isSprinting() ? 3.6f : 1.75f);
        }
        speed += shakePhase;
        if (ticks % 1 == 0) {
            speed += 0.2f;
        }

        float yawSpeed = speed * 0.7f * hold;
        float pitchSpeed = speed * (0.93f + shakePhase) * hold;

        float dy = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float dp = targetPitch - currentPitch;

        float yawLimit = mc.player.isSprinting() ? 95.0f : (shouldHit ? 20.0f : 18.0f);
        float pitchLimit = mc.player.isSprinting() ? 16.0f : (shouldHit ? 6.5f : 5.0f);

        dy = MathHelper.clamp(dy, -yawLimit, yawLimit);
        dp = MathHelper.clamp(dp, -pitchLimit, pitchLimit);

        float newYaw = currentYaw + dy * pitchSpeed;
        float newPitch = currentPitch + dp * yawSpeed;

        // микро-дрожь при доводе (зависит от расстояния до цели)
        float mult = shakeFixer.getMultiplier(entity);
        newYaw += wave((0.0f + (float) Math.random() * 0.1f) * mult * hold, false, 15.0f);
        newPitch += wave((0.0f + (float) Math.random() * 0.0f) * mult * hold, false, 16.0f);

        // привязка к GCD (целым градусам мыши)
        float gcd = GCDFixer.getGCDValue();
        if (gcd > 0.0f) {
            newYaw = currentYaw + (float) Math.round((newYaw - currentYaw) / gcd) * gcd;
            newPitch = currentPitch + (float) Math.round((newPitch - currentPitch) / gcd) * gcd;
        }

        newPitch = MathHelper.clamp(newPitch, -89.0f, 89.0f);

        Rotation rotation = new Rotation(newYaw, newPitch);

        float speedLimit = mc.player.isSprinting() && entity.isSprinting() ? 160.0f : 45.0f;
        RotationComponent.update(rotation, speedLimit, speedLimit, 360.0f, 360.0f, 0, 1, false);

        rotate = new Vec2f(rotation.getYaw(), rotation.getPitch());
        this.currentYaw = rotation.getYaw();
        this.currentPitch = rotation.getPitch();
        this.lastYaw = rotation.getYaw();
        this.lastPitch = rotation.getPitch();
    }

    @Override
    public void update(Rotation targetAngle, boolean elytraVisual) {
    }

    public static class AttackTracker {
        public boolean update() {
            return false;
        }

        public void reset() {
        }
    }

    public static class ShakeFixer {
        public float getMultiplier(LivingEntity entity) {
            if (mc.player == null || entity == null) return 1.0f;
            float distance = mc.player.distanceTo(entity);
            return MathHelper.clamp(distance / 3.0f, 0.5f, 1.5f);
        }
    }

    public int getPing() {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) return entry.getLatency();
        }
        return 50;
    }
}