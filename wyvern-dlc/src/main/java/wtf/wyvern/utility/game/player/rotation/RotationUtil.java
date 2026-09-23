package wtf.wyvern.utility.game.player.rotation;

import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@FastNative
public final class RotationUtil implements IMinecraft {
    public static Vec2f calculate(Vec3d fromVec, Vec3d toVec) {
        Vec3d diff = toVec.subtract(fromVec);
        double distance = Math.hypot(diff.x, diff.z);
        float yaw = (float) Math.toDegrees(MathHelper.atan2(diff.z, diff.x)) - 90.0F;
        float pitch = (float) -Math.toDegrees(MathHelper.atan2(diff.y, distance));
        return new Vec2f(yaw, pitch);
    }

    public static Vec2f calculate(Entity entity) {
        return calculate(getEyesPos(entity));
    }

    public static Vec2f calculate(Vec3d toVec) {
        return calculate(getEyesPos(mc.player), toVec);
    }

    public static float getAngleDifference(float direction, float yaw) {
        float difference = Math.abs(yaw - direction) % 360.0F;
        return difference > 180.0F ? 360.0F - difference : difference;
    }

    public static Vec3d getEyesPos(Entity entity) {
        return entity.getPos().add(0.0D, entity.getEyeHeight(entity.getPose()), 0.0D);
    }

    public static float[] calculateAngle(Vec3d from, Vec3d to) {
        Vec2f result = calculate(from, to);
        return new float[]{
                MathHelper.wrapDegrees(result.x),
                MathHelper.clamp(MathHelper.wrapDegrees(result.y), -90.0F, 90.0F)
        };
    }

    public static Vector3f getDirectionVector(float yaw, float pitch) {
        float yawRadians = (float) Math.toRadians(yaw);
        float pitchRadians = (float) Math.toRadians(pitch);
        float x = -MathHelper.cos(pitchRadians) * MathHelper.sin(yawRadians);
        float y = -MathHelper.sin(pitchRadians);
        float z = MathHelper.cos(pitchRadians) * MathHelper.cos(yawRadians);
        return new Vector3f(x, y, z);
    }

    public static float calculateFov(float cameraYaw, float cameraPitch, float targetYaw, float targetPitch) {
        Vector3f cameraDirection = getDirectionVector(cameraYaw, cameraPitch);
        Vector3f targetDirection = getDirectionVector(targetYaw, targetPitch);
        float dot = MathHelper.clamp(cameraDirection.dot(targetDirection), -1.0F, 1.0F);
        return (float) Math.toDegrees(Math.acos(dot));
    }

    public static Rotation getClientRotation() {
        return new Rotation(mc.player.getYaw(), mc.player.getPitch());
    }

    public static Rotation fromVec3d(Vec3d vector) {
        return new Rotation((float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(vector.z, vector.x)) - 90.0D), (float)MathHelper.wrapDegrees(Math.toDegrees(-Math.atan2(vector.y, Math.hypot(vector.x, vector.z)))));
    }

    public static Rotation calculateAngle(Vec3d to) {
        return fromVec3d(to.subtract(mc.player.getEyePos()));
    }

    @Generated
    private RotationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
