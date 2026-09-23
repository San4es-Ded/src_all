package wtf.wyvern.client.modules.impl.combat.rotation;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.GCDFixer;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.game.player.rotation.RotationUtil;
import wtf.wyvern.utility.predict.PredictUtils;
import wtf.astroguard.J2C.FastNative;

import java.util.Random;

/**
 * Organic HolyWorld-style rotation built on a saccadic aiming model, the same
 * structure real mouse aiming has:
 *
 * - PURSUIT: smooth proportional tracking with a low, noisy gain.
 * - FLICK: a fast ballistic saccade fired when the error exceeds a threshold.
 * - SETTLE: 1-3 ticks of small corrections after a flick.
 * - LOCK: on-target behaviour - tremor, slow drift and occasional micro-saccades.
 *
 * All noise is Ornstein-Uhlenbeck (mean-reverting, autocorrelated).
 *
 * One step per GAME TICK: the server samples rotations from movement packets
 * at 20 Hz, so anything faster is invisible on the wire and only aliases
 * smooth patterns into noise.
 */
@FastNative
public final class HolyWorldRotation extends RotationBase {
    private LivingEntity trackedTarget;

    private float currentYaw;
    private float currentPitch;
    private float lastSentYaw;
    private float lastSentPitch;

    private int lastTickAge = -1;
    private int emissionCallsThisTick;

    private int reactionTicks;
    private int reactionMax;
    private boolean reacted;

    // Saccade state
    private Phase phase = Phase.PURSUIT;
    private int phaseTicks;
    private int nextFlickIn;
    private float flickGain = 0.6F;
    private float flickPeak = 30.0F;
    private float flickLandYaw;
    private float flickLandPitch;

    // Continuous behaviour blend: 0 = careful/lazy, 1 = aggressive/sharp
    private double mood = 0.5D;
    private double moodTarget = 0.5D;
    private int moodShiftIn = 100;

    // Ornstein-Uhlenbeck noise states
    private double gainNoise;
    private double curveNoise;
    private double driftYawNoise;
    private double driftPitchNoise;

    private float driftYawDeg;
    private float driftPitchDeg;

    private int lastHurtTime;

    // Per-hit attack humanisation
    private float cooldownGate = 0.9F;
    private boolean whiffArmed;

    // Предполагается, что rng уже есть в RotationBase, но если нет — добавьте:
    // private final Random rng = new Random();

    public void update(LivingEntity target, Rotation targetAngle, boolean elytraVisual, int ticksToAttack) {
        if (mc.player == null || target == null || targetAngle == null) return;

        // Advance the aim model once per game tick
        if (mc.player.age != this.lastTickAge) {
            this.lastTickAge = mc.player.age;
            this.emissionCallsThisTick = 0;
            stepTick(target, targetAngle, ticksToAttack);
        }

        float remainYaw = MathHelper.wrapDegrees(this.currentYaw - this.lastSentYaw);
        float remainPitch = this.currentPitch - this.lastSentPitch;
        float remainingLength = (float) Math.hypot(remainYaw, remainPitch);
        float gcd = GCDFixer.getGCDValue();

        // 1. Имитация статического трения (Мертвая зона руки)
        // Если угол микроскопический и мы не в атаке, рука может "застрять" на кадр
        if (remainingLength > 0 && remainingLength < gcd * 3.0F && this.rng.nextFloat() < 0.15F && ticksToAttack > 2) {
            this.emissionCallsThisTick++;
            return; // Пропускаем движение в этом кадре (микро-статтер)
        }

        // 2. Дрожание скорости (Jerky Chase)
        // Вместо гладкого lerp'a добавляем нестабильность мышечного усилия
        float speedJitter = 1.0F + (float) this.rng.nextGaussian() * 0.25F;
        float baseChase = 0.20F + remainingLength * 0.012F;
        float chase = MathHelper.clamp(baseChase * speedJitter, 0.15F, 0.70F);

        boolean attackWindow = ticksToAttack > 0 && ticksToAttack <= 2;
        boolean smallRemainder = gcd > 0.0F && remainingLength <= gcd * 1.5F;

        this.emissionCallsThisTick++;

        // Позволяем "хвостам" движения завершаться чуть более непредсказуемо
        boolean completeStep = attackWindow || smallRemainder || this.emissionCallsThisTick >= (4 + this.rng.nextInt(2));
        float fraction = completeStep ? 1.0F : chase;

        // Применяем джерки-эффект на этапе квантизации
        boolean isFlicking = this.phase == Phase.FLICK;

        float outputYaw = this.lastSentYaw + quantise(remainYaw * fraction, isFlicking);
        float outputPitch = MathHelper.clamp(
                this.lastSentPitch + quantise(remainPitch * fraction, isFlicking), -89.0F, 89.0F);

        emit(outputYaw, outputPitch, elytraVisual);
    }

    /**
     * One 20 Hz model step. Everything in here is calibrated in game ticks;
     * the visual smoothness lives in {@link #update}, not here.
     */
    private void stepTick(LivingEntity target, Rotation targetAngle, int ticksToAttack) {
        if (this.trackedTarget != target) {
            initializeTarget(target);
        } else {
            resyncIfOverridden();
        }

        detectFlinch();
        updateMood();
        updateDrift(target);

        if (this.nextFlickIn > 0) {
            this.nextFlickIn--;
        }

        Rotation led = applyVelocityLead(target, targetAngle);
        float wantedYaw = MathHelper.wrapDegrees(led.getYaw() + this.driftYawDeg);
        float wantedPitch = MathHelper.clamp(led.getPitch() + this.driftPitchDeg, -89.0F, 89.0F);

        if (!this.reacted) {
            if (++this.reactionTicks >= this.reactionMax) {
                this.reacted = true;
            }
            return;
        }

        float diffYaw = MathHelper.wrapDegrees(wantedYaw - this.currentYaw);
        float diffPitch = wantedPitch - this.currentPitch;
        float error = (float) Math.hypot(diffYaw, diffPitch);

        this.gainNoise += -this.gainNoise * 0.12D + this.rng.nextGaussian() * 0.10D;
        this.curveNoise += -this.curveNoise * 0.07D + this.rng.nextGaussian() * 0.045D;
        float gainScale = 1.0F + MathHelper.clamp((float) this.gainNoise, -0.6F, 0.6F);

        // Fire a saccade when pursuit cannot keep up with the error.
        if (this.phase == Phase.PURSUIT
                && error > lerpMood(13.0F, 7.0F)
                && this.nextFlickIn <= 0) {
            enterFlick(diffYaw, diffPitch);
        }

        float stepYaw;
        float stepPitch;
        switch (this.phase) {
            case FLICK -> {
                float fdy = MathHelper.wrapDegrees(this.flickLandYaw - this.currentYaw);
                float fdp = this.flickLandPitch - this.currentPitch;
                float gain = this.flickGain * gainScale;
                float[] step = capStep(fdy * gain, fdp * gain, this.flickPeak);
                stepYaw = step[0];
                stepPitch = step[1];
                if (--this.phaseTicks <= 0 || Math.hypot(fdy, fdp) < 1.0D) {
                    enterSettle();
                }
            }
            case SETTLE -> {
                float gain = (0.18F + this.rng.nextFloat() * 0.08F) * gainScale;
                float[] step = capStep(diffYaw * gain, diffPitch * gain, 2.5F);
                stepYaw = step[0];
                stepPitch = step[1];
                if (--this.phaseTicks <= 0) {
                    this.phase = Phase.PURSUIT;
                }
            }
            default -> {
                float lockRadius = lerpMood(1.6F, 0.9F);
                if (error < lockRadius) {
                    if (error > lockRadius * 0.6F && this.rng.nextFloat() < 0.3F) {
                        float[] step = capStep(diffYaw * 0.4F, diffPitch * 0.4F, 1.1F);
                        stepYaw = step[0];
                        stepPitch = step[1];
                    } else {
                        stepYaw = 0.0F;
                        stepPitch = 0.0F;
                    }
                } else {
                    float gain = lerpMood(0.20F, 0.42F) * gainScale;
                    float[] step = capStep(diffYaw * gain, diffPitch * gain, lerpMood(3.5F, 8.5F));
                    stepYaw = step[0];
                    stepPitch = step[1];
                }
            }
        }

        float tremor = lerpMood(0.05F, 0.11F) * switch (this.phase) {
            case FLICK -> 0.25F;
            case SETTLE -> 0.7F;
            default -> 1.0F;
        };
        stepYaw += (float) this.rng.nextGaussian() * tremor;
        stepPitch += (float) this.rng.nextGaussian() * tremor * 0.8F;

        if (ticksToAttack == 2) {
            float ease = 0.6F * (0.85F + this.rng.nextFloat() * 0.3F);
            stepYaw *= ease;
            stepPitch *= ease;
        } else if (ticksToAttack == 1) {
            float ease = 0.35F * (0.8F + this.rng.nextFloat() * 0.4F);
            stepYaw *= ease;
            stepPitch *= ease;
        }

        float curveScale = this.phase == Phase.FLICK ? 0.3F : 1.0F;
        float curveAngle = MathHelper.clamp((float) this.curveNoise, -0.28F, 0.28F) * curveScale;
        float cos = MathHelper.cos(curveAngle);
        float sin = MathHelper.sin(curveAngle);
        float rotatedYaw = stepYaw * cos - stepPitch * sin;
        float rotatedPitch = stepYaw * sin + stepPitch * cos;

        this.currentYaw += rotatedYaw;
        this.currentPitch = MathHelper.clamp(this.currentPitch + rotatedPitch, -89.0F, 89.0F);
    }

    public int onAttack() {
        this.moodTarget = MathHelper.clamp(
                this.moodTarget + this.rng.nextGaussian() * 0.12D + 0.02D, 0.05D, 0.95D);

        this.cooldownGate = 0.80F + 0.20F * (float) Math.sqrt(this.rng.nextDouble());
        this.whiffArmed = this.rng.nextFloat() < lerpMood(0.04F, 0.09F);

        float mean = lerpMood(10.0F, 5.5F);
        double delay = mean + this.rng.nextGaussian() * (1.2D + (1.0D - this.mood) * 0.8D);
        if (this.rng.nextFloat() < 0.18F) {
            delay += 1.0D + Math.abs(this.rng.nextGaussian()) * 2.5D;
        }
        if (this.rng.nextFloat() < 0.07F) {
            delay += 6.0D + this.rng.nextInt(9);
        }
        return MathHelper.clamp((int) Math.round(delay), 2, 26);
    }

    public float getCooldownGate() {
        return this.cooldownGate;
    }

    public boolean isWhiffArmed() {
        return this.whiffArmed;
    }

    public void reset() {
        this.trackedTarget = null;
        this.lastTickAge = -1;
        this.emissionCallsThisTick = 0;
        this.phase = Phase.PURSUIT;
        this.phaseTicks = 0;
        this.nextFlickIn = 0;
        if (mc.player != null) {
            this.currentYaw = this.lastSentYaw = this.lastYaw = mc.player.getYaw();
            this.currentPitch = this.lastSentPitch = this.lastPitch = mc.player.getPitch();
            this.lastHurtTime = mc.player.hurtTime;
        }
    }

    private void initializeTarget(LivingEntity target) {
        this.trackedTarget = target;
        this.emissionCallsThisTick = 0;
        this.currentYaw = mc.player.getYaw();
        this.currentPitch = mc.player.getPitch();
        this.lastSentYaw = this.currentYaw;
        this.lastSentPitch = this.currentPitch;
        this.lastYaw = this.currentYaw;
        this.lastPitch = this.currentPitch;
        this.lastHurtTime = mc.player.hurtTime;
        this.reactionMax = 2 + this.rng.nextInt(4);
        this.reactionTicks = 0;
        this.reacted = false;
        this.phase = Phase.PURSUIT;
        this.phaseTicks = 0;
        this.nextFlickIn = 0;
        this.moodShiftIn = 60 + this.rng.nextInt(120);
        this.gainNoise = 0.0D;
        this.curveNoise = 0.0D;
        this.driftYawNoise = 0.0D;
        this.driftPitchNoise = 0.0D;
        this.driftYawDeg = 0.0F;
        this.driftPitchDeg = 0.0F;
        this.cooldownGate = 0.85F + 0.15F * this.rng.nextFloat();
        this.whiffArmed = false;
    }

    private void detectFlinch() {
        int hurt = mc.player.hurtTime;
        if (hurt > this.lastHurtTime) {
            this.currentYaw += (this.rng.nextFloat() - 0.5F) * 2.0F * (1.0F + this.rng.nextFloat() * 2.5F);
            this.currentPitch = MathHelper.clamp(this.currentPitch
                    + (this.rng.nextFloat() - 0.5F) * 2.0F * (0.8F + this.rng.nextFloat() * 1.8F), -89.0F, 89.0F);
            if (this.phase == Phase.FLICK) {
                enterSettle();
            }
        }
        this.lastHurtTime = hurt;
    }

    private void updateMood() {
        if (--this.moodShiftIn <= 0) {
            this.moodTarget = MathHelper.clamp(
                    this.moodTarget + this.rng.nextGaussian() * 0.45D, 0.05D, 0.95D);
            this.moodShiftIn = 80 + this.rng.nextInt(160);
        }
        this.mood += (this.moodTarget - this.mood) * 0.02D + this.rng.nextGaussian() * 0.003D;
        this.mood = MathHelper.clamp(this.mood, 0.0D, 1.0D);
    }

    private void enterFlick(float diffYaw, float diffPitch) {
        this.phase = Phase.FLICK;
        this.phaseTicks = 1 + this.rng.nextInt(2 + (int) Math.round(this.mood));
        this.flickGain = 0.50F + this.rng.nextFloat() * 0.35F;
        this.flickPeak = 24.0F + this.rng.nextFloat() * 16.0F + (float) this.mood * 6.0F;
        float land = this.rng.nextFloat() < 0.22F
                ? 1.01F + this.rng.nextFloat() * 0.04F
                : 0.90F + this.rng.nextFloat() * 0.09F;
        this.flickLandYaw = this.currentYaw + diffYaw * land;
        this.flickLandPitch = this.currentPitch + diffPitch * land;
    }

    private void enterSettle() {
        this.phase = Phase.SETTLE;
        this.phaseTicks = 1 + this.rng.nextInt(3);
        this.nextFlickIn = Math.round(lerpMood(9.0F, 4.0F)) + this.rng.nextInt(4);
    }

    private void resyncIfOverridden() {
        float dy = Math.abs(MathHelper.wrapDegrees(mc.player.getYaw() - this.lastSentYaw));
        float dp = Math.abs(mc.player.getPitch() - this.lastSentPitch);
        if (dy > 2.0F || dp > 2.0F) {
            this.currentYaw = this.lastSentYaw = mc.player.getYaw();
            this.currentPitch = this.lastSentPitch = mc.player.getPitch();
        }
    }

    private Rotation applyVelocityLead(LivingEntity target, Rotation base) {
        if (mc.player.isGliding()) {
            return base;
        }
        Vec3d movement = PredictUtils.getMovement(target);
        if (movement.lengthSquared() < 1.0E-6D) {
            return base;
        }
        double leadTicks = MathHelper.clamp(getPing() / 50.0D * 0.5D + 0.5D, 0.5D, 2.5D);
        Vec3d offset = movement.multiply(leadTicks);
        double length = offset.length();
        if (length > 0.45D) {
            offset = offset.multiply(0.45D / length);
        }
        Vec3d from = target.getBoundingBox().getCenter().subtract(mc.player.getEyePos());
        Rotation raw = RotationUtil.fromVec3d(from);
        Rotation ledRaw = RotationUtil.fromVec3d(from.add(offset));
        float dYaw = MathHelper.wrapDegrees(ledRaw.getYaw() - raw.getYaw());
        float dPitch = ledRaw.getPitch() - raw.getPitch();
        return new Rotation(base.getYaw() + dYaw, base.getPitch() + dPitch);
    }

    private void updateDrift(LivingEntity target) {
        this.driftYawNoise += -this.driftYawNoise * 0.05D + this.rng.nextGaussian() * 0.10D;
        this.driftPitchNoise += -this.driftPitchNoise * 0.05D + this.rng.nextGaussian() * 0.10D;
        double distance = Math.max(mc.player.distanceTo(target), 1.0F);
        float yawRange = (float) Math.toDegrees(Math.atan(0.14D / distance));
        float pitchRange = (float) Math.toDegrees(Math.atan(0.18D / distance));
        this.driftYawDeg = MathHelper.clamp((float) this.driftYawNoise, -1.0F, 1.0F) * yawRange;
        this.driftPitchDeg = MathHelper.clamp((float) this.driftPitchNoise, -1.0F, 1.0F) * pitchRange;
    }

    private float lerpMood(float careful, float aggressive) {
        return (float) (careful + (aggressive - careful) * this.mood);
    }

    private float[] capStep(float stepYaw, float stepPitch, float cap) {
        float length = (float) Math.hypot(stepYaw, stepPitch);
        if (length > cap && length > 1.0E-4F) {
            float scale = cap / length;
            return new float[]{stepYaw * scale, stepPitch * scale};
        }
        return new float[]{stepYaw, stepPitch};
    }

    private void emit(float yaw, float pitch, boolean elytraVisual) {
        this.lastSentYaw = yaw;
        this.lastSentPitch = pitch;
        this.lastYaw = yaw;
        this.lastPitch = pitch;
        RotationComponent.update(new Rotation(yaw, pitch),
                360.0F, 360.0F, 45.0F, 45.0F, 20, 1, elytraVisual);
    }

    /**
     * Человечная квантизация (Humanized GCD)
     * Добавляет шанс на срыв сенсора или пропуск пикселя при резких движениях.
     */
    private float quantise(float delta, boolean applySensorNoise) {
        float gcd = GCDFixer.getGCDValue();
        if (!(gcd > 0.0F) || !Float.isFinite(gcd)) return delta;

        // Если мы делаем быстрый рывок (флик), сенсор мыши иногда теряет трекинг
        // или перескакивает лишний шаг сетки чувствительности.
        if (applySensorNoise && Math.abs(delta) > gcd * 4.0F) {
            float noiseChance = this.rng.nextFloat();
            if (noiseChance < 0.07F) {
                // Проглатываем один микро-шаг мыши (недовод)
                delta -= Math.signum(delta) * gcd;
            } else if (noiseChance < 0.10F) {
                // Двойной клик сенсора (микро-перелет)
                delta += Math.signum(delta) * (gcd * (1.0F + this.rng.nextFloat()));
            }
        }

        return GCDFixer.getFixRotate(delta);
    }

    private int getPing() {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) return entry.getLatency();
        }
        return 50;
    }

    @Override
    public void update(Rotation targetAngle, boolean elytraVisual) {
        // Aura uses the entity-aware overload.
    }

    private enum Phase {
        PURSUIT,
        FLICK,
        SETTLE
    }
}