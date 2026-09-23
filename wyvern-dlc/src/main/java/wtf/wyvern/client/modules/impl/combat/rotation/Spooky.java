package wtf.wyvern.client.modules.impl.combat.rotation;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.RaytracingUtil;
import wtf.wyvern.utility.game.player.rotation.GCDFixer;
import wtf.wyvern.utility.game.player.rotation.Rotation;

/**
 * Independent copy of the Sloth rotation used by Aura's Spooky mode.
 */
@FastNative
public final class Spooky extends RotationBase {
    private LivingEntity trackedTarget;

    private float phaseYaw;
    private float phasePitch;
    private float frequencyYaw;
    private float frequencyPitch;

    private float microBiasYaw;
    private float microBiasPitch;
    private float smoothedFactor;

    private float lastYawStep;
    private float lastPitchStep;
    private int phaseTicks;

    public void update(LivingEntity target, Rotation targetAngle, boolean elytraVisual) {
        if (mc.player == null || target == null) return;

        if (trackedTarget != target) {
            trackedTarget = target;
            phaseYaw = rng.nextFloat() * (float) Math.PI * 2.0F;
            phasePitch = rng.nextFloat() * (float) Math.PI * 2.0F;
            smoothedFactor = 0.1F;
            resetPhaseCharacteristics();
        }

        float yawError = MathHelper.wrapDegrees(targetAngle.getYaw() - lastYaw);
        float pitchError = targetAngle.getPitch() - lastPitch;
        float totalError = (float) Math.sqrt(yawError * yawError + pitchError * pitchError);

        boolean traced = RaytracingUtil.rayTrace(
                new Rotation(lastYaw, lastPitch).toVector(), 999.0D, target.getBoundingBox());

        if (--phaseTicks <= 0) {
            resetPhaseCharacteristics();
        }

        phaseYaw += frequencyYaw;
        phasePitch += frequencyPitch;

        float waveYaw = (float) (Math.sin(phaseYaw) * Math.cos(phaseYaw * 0.618F)) * microBiasYaw;
        float wavePitch = (float) (Math.cos(phasePitch) * Math.sin(phasePitch * 1.414F)) * microBiasPitch;

        float targetFactor = traced ? 0.22F + rng.nextFloat() * 0.15F : 0.45F + rng.nextFloat() * 0.35F;
        if (totalError > 45.0F) targetFactor *= 1.4F;

        smoothedFactor = smoothedFactor + (targetFactor - smoothedFactor) * 0.25F;

        float rawYawStep = (yawError + waveYaw) * smoothedFactor;
        float rawPitchStep = (pitchError + wavePitch) * smoothedFactor
                * (mc.player.isOnGround() ? 0.85F : 0.5F);

        float maxYawRate = 12.0F + (float) Math.sin(phaseYaw) * 4.0F;
        float maxPitchRate = 7.0F + (float) Math.cos(phasePitch) * 2.5F;

        float yawStep = MathHelper.clamp(rawYawStep, -maxYawRate, maxYawRate);
        float pitchStep = MathHelper.clamp(rawPitchStep, -maxPitchRate, maxPitchRate);

        if (Math.abs(yawStep - lastYawStep) < 0.008F) {
            yawStep += (rng.nextFloat() - 0.5F) * 0.04F;
        }
        if (Math.abs(pitchStep - lastPitchStep) < 0.008F) {
            pitchStep += (rng.nextFloat() - 0.5F) * 0.03F;
        }

        yawStep = quantise(yawStep);
        pitchStep = quantise(pitchStep);

        if (Math.abs(yawStep) > Math.abs(yawError) && Math.signum(yawStep) == Math.signum(yawError)) {
            yawStep = quantise(yawError);
        }
        if (Math.abs(pitchStep) > Math.abs(pitchError) && Math.signum(pitchStep) == Math.signum(pitchError)) {
            pitchStep = quantise(pitchError);
        }

        lastYaw = MathHelper.wrapDegrees(lastYaw + yawStep);
        lastPitch = MathHelper.clamp(lastPitch + pitchStep, -89.0F, 89.0F);
        lastYawStep = yawStep;
        lastPitchStep = pitchStep;

        RotationComponent.update(new Rotation(lastYaw, lastPitch),
                360.0F, 360.0F, 360.0F, 360.0F, 0, 1, false);
    }

    private void resetPhaseCharacteristics() {
        phaseTicks = 4 + rng.nextInt(7);
        frequencyYaw = 0.15F + rng.nextFloat() * 0.35F;
        frequencyPitch = 0.12F + rng.nextFloat() * 0.28F;
        microBiasYaw = (rng.nextFloat() - 0.5F) * 1.4F;
        microBiasPitch = (rng.nextFloat() - 0.5F) * 0.8F;
    }

    private static float quantise(float delta) {
        float gcd = GCDFixer.getGCDValue();
        return gcd > 0.0F && Float.isFinite(gcd) ? GCDFixer.getFixRotate(delta) : delta;
    }

    @Override
    public void update(Rotation targetAngle, boolean elytraVisual) {
        // Aura uses the entity-aware overload.
    }

    private int getPing() {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) return entry.getLatency();
        }
        return 50;
    }
}
