package rockstar.client.internal.rotation;



import rockstar.client.rotation.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import rockstar.client.MinecraftClientAccess;

public final class ScreenProjectionUtils
implements MinecraftClientAccess {
    private static Matrix4f internalField0788;
    private static Matrix4f internalField0787;

    public static void internalMethod00887(Matrix4f matrix4f, Matrix4f matrix4f2) {
        internalField0788 = new Matrix4f((Matrix4fc)matrix4f);
        internalField0787 = new Matrix4f((Matrix4fc)matrix4f2);
    }

    public static Vec2f internalMethod00612(Vec3d vec3d) {
        Camera camera = ScreenProjectionUtils.internalField0149.gameRenderer.getCamera();
        Vec3d vec3d2 = vec3d.subtract(camera.getCameraPos());
        Vector4f vector4f = new Vector4f((float)vec3d2.x, (float)vec3d2.y, (float)vec3d2.z, 1.0f);
        vector4f.mul((Matrix4fc)internalField0788).mul((Matrix4fc)internalField0787);
        if (vector4f.w <= 0.0f) {
            return null;
        }
        Vector4f vector4f2 = vector4f.div(vector4f.w);
        float f = (vector4f2.x + 1.0f) / 2.0f * (float)internalField0149.getWindow().getScaledWidth();
        float f2 = (1.0f - vector4f2.y) / 2.0f * (float)internalField0149.getWindow().getScaledHeight();
        return new Vec2f(f, f2);
    }

    public static Vec2f internalMethod06420(Vec3d vec3d) {
        if (internalField0787 == null) {
            return null;
        }
        Camera camera = ScreenProjectionUtils.internalField0149.gameRenderer.getCamera();
        float f = camera.getYaw() * ((float)Math.PI / 180);
        float f2 = camera.getPitch() * ((float)Math.PI / 180);
        double d = MathHelper.cos((float)f);
        double d2 = MathHelper.sin((float)f);
        double d3 = MathHelper.cos((float)f2);
        double d4 = MathHelper.sin((float)f2);
        Vec3d vec3d2 = new Vec3d(-d2 * d3, -d4, d * d3);
        Vec3d vec3d3 = new Vec3d(-d, 0.0, -d2);
        Vec3d vec3d4 = vec3d3.crossProduct(vec3d2);
        double d5 = vec3d.dotProduct(vec3d2);
        if (d5 <= 1.0E-4) {
            return null;
        }
        double d6 = (double)internalField0787.m00() * vec3d.dotProduct(vec3d3) / d5;
        double d7 = (double)internalField0787.m11() * vec3d.dotProduct(vec3d4) / d5;
        return new Vec2f((float)((d6 + 1.0) / 2.0 * (double)internalField0149.getWindow().getScaledWidth()), (float)((1.0 - d7) / 2.0 * (double)internalField0149.getWindow().getScaledHeight()));
    }

    public static Vec3d internalMethod02822(Entity entity, float f) {
        return new Vec3d(MathHelper.lerp((double)f, (double)entity.lastX, (double)entity.getX()), MathHelper.lerp((double)f, (double)entity.lastY, (double)entity.getY()), MathHelper.lerp((double)f, (double)entity.lastZ, (double)entity.getZ()));
    }

    public static Vec3d internalMethod00202(Vec3d vec3d, Vec3d vec3d2, float f) {
        return new Vec3d(MathHelper.lerp((double)f, (double)vec3d.x, (double)vec3d2.getX()), MathHelper.lerp((double)f, (double)vec3d.y, (double)vec3d2.getY()), MathHelper.lerp((double)f, (double)vec3d.z, (double)vec3d2.getZ()));
    }

    @Generated
    private ScreenProjectionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

