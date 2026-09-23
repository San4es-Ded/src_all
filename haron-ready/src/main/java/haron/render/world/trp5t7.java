package haron.render.world;

import haron.client.MinecraftClientAccess;
import haron.render.ScaledGuiProjection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class trp5t7
implements MinecraftClientAccess {
    public static final Matrix4f a = new Matrix4f();
    public static final Matrix4f b = new Matrix4f();
    public static final Matrix4f e = new Matrix4f();
    private static final ThreadLocal<Matrix4f> cleanRotationMatrixTL = ThreadLocal.withInitial(Matrix4f::new);
    private static final ThreadLocal<Matrix4f> combinedMatrixTL = ThreadLocal.withInitial(Matrix4f::new);
    private static final ThreadLocal<Vector3f> screenPosTL = ThreadLocal.withInitial(Vector3f::new);
    private static final int[] viewport = new int[4];
    private static double cachedScale = -1.0;
    private static int cachedWidth = -1;
    private static int cachedHeight = -1;

    private trp5t7() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean b(Vec3d vec3d) {
        if (vec3d == null) {
            return false;
        }
        float f = (float)c.getWindow().getFramebufferWidth() / ScaledGuiProjection.d();
        float f2 = (float)c.getWindow().getFramebufferHeight() / ScaledGuiProjection.d();
        if (vec3d.x >= -100.0 && vec3d.x <= (double)(f + 100.0f) && vec3d.y >= -100.0) {
            return vec3d.y <= (double)(f2 + 100.0f);
        }
        return false;
    }

    public static boolean c(Vec3d vec3d) {
        if (vec3d == null || trp5t7.c.player == null) {
            return false;
        }
        double[] dArray = trp5t7.a(vec3d.x, vec3d.y, vec3d.z);
        return dArray[2] >= 0.0 && dArray[2] <= 1.0;
    }

    public static Vec3d a(Vec3d vec3d) {
        if (trp5t7.c.gameRenderer == null || c.getWindow() == null || trp5t7.c.player == null) {
            return null;
        }
        double[] dArray = trp5t7.a(vec3d.x, vec3d.y, vec3d.z);
        if (dArray[2] < 0.0 || dArray[2] > 1.0) {
            return null;
        }
        return new Vec3d(dArray[0], dArray[1], dArray[2]);
    }

    private static double[] a(double d, double d2, double d3) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Camera camera = minecraftClient.getEntityRenderDispatcher().camera;
        int n = minecraftClient.getWindow().getHeight();
        int n2 = minecraftClient.getWindow().getWidth();
        if (n2 != cachedWidth || n != cachedHeight) {
            cachedWidth = n2;
            cachedHeight = n;
            trp5t7.viewport[0] = 0;
            trp5t7.viewport[1] = 0;
            trp5t7.viewport[2] = n2;
            trp5t7.viewport[3] = n;
        }
        float f = (float)(d - camera.getPos().x);
        float f2 = (float)(d2 - camera.getPos().y);
        float f3 = (float)(d3 - camera.getPos().z);
        Matrix4f matrix4f = cleanRotationMatrixTL.get();
        matrix4f.set((Matrix4fc)b);
        matrix4f.m30(0.0f);
        matrix4f.m31(0.0f);
        matrix4f.m32(0.0f);
        Matrix4f matrix4f2 = combinedMatrixTL.get();
        matrix4f2.set((Matrix4fc)a).mul((Matrix4fc)matrix4f);
        Vector3f vector3f = screenPosTL.get();
        matrix4f2.project(f, f2, f3, viewport, vector3f);
        double d4 = ScaledGuiProjection.d();
        if (d4 != cachedScale) {
            cachedScale = d4;
        }
        double[] dArray = new double[]{(double)vector3f.x / cachedScale, (double)((float)n - vector3f.y) / cachedScale, vector3f.z};
        return dArray;
    }
}

