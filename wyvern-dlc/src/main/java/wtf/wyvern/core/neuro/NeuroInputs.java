package wtf.wyvern.core.neuro;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Shared feature layout used by both recording and inference.
 *
 * Target velocity is deliberately absent: training targets are static
 * (FakePlayer), so those features would carry no signal during training and
 * only inject out-of-distribution noise at inference. The player's own
 * velocity IS kept - strafing around the dummy produces it during recording.
 */
public final class NeuroInputs {
    public static final int SIZE = 12;

    private NeuroInputs() {
    }

    public static double[] create(float targetYaw, float targetPitch,
                                  float currentYaw, float currentPitch,
                                  float previousYawStep, float previousPitchStep,
                                  float olderYawStep, float olderPitchStep,
                                  double distance, float cooldown, int ticksSinceAttack,
                                  Vec3d playerVelocity) {
        float yawError = MathHelper.wrapDegrees(targetYaw - currentYaw);
        float pitchError = targetPitch - currentPitch;
        double angularError = Math.hypot(yawError, pitchError);

        // Screen-right axis: lateral velocity here is what the hand answers
        // with a yaw step, so the net can learn strafe-aim coupling.
        double yawRadians = Math.toRadians(currentYaw);
        double rightX = -Math.cos(yawRadians);
        double rightZ = -Math.sin(yawRadians);
        Vec3d velocity = playerVelocity == null ? Vec3d.ZERO : playerVelocity;

        return new double[]{
                clamp(yawError / 90.0D, -2.0D, 2.0D),
                clamp(pitchError / 45.0D, -2.0D, 2.0D),
                clamp(previousYawStep / 30.0D, -2.0D, 2.0D),
                clamp(previousPitchStep / 20.0D, -2.0D, 2.0D),
                clamp(olderYawStep / 30.0D, -2.0D, 2.0D),
                clamp(olderPitchStep / 20.0D, -2.0D, 2.0D),
                clamp(distance / 6.0D, 0.0D, 2.0D),
                clamp(cooldown, 0.0D, 1.0D),
                clamp(ticksSinceAttack / 20.0D, 0.0D, 2.0D),
                clamp((velocity.x * rightX + velocity.z * rightZ) / 0.3D, -2.0D, 2.0D),
                clamp(velocity.y / 0.25D, -2.0D, 2.0D),
                clamp(1.0D - angularError / 12.0D, 0.0D, 1.0D)
        };
    }

    private static double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
