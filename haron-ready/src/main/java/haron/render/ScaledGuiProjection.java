package haron.render;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import haron.client.MinecraftClientAccess;
import haron.render.ScreenPoint;
import net.minecraft.client.util.Window;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public final class ScaledGuiProjection
implements MinecraftClientAccess {
    private static float e = 1.0f;
    private static Matrix4f f;
    private static Matrix4f g;
    public static int a;
    public static boolean b;

    public static void b(float f, float f2, float f3) {
        RenderSystem.getModelViewStack().translate(f, f2, f3);
    }

    public static void b() {
        RenderSystem.getModelViewStack().pushMatrix();
    }

    public static void c() {
        RenderSystem.getModelViewStack().popMatrix();
    }

    public static float d() {
        return e;
    }

    public static void a() {
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.setProjectionMatrix((Matrix4f)f, (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        e = (float)c.getWindow().getScaleFactor();
    }

    public static ScreenPoint a(int n, int n2) {
        double d = c.getWindow().getScaleFactor();
        return new ScreenPoint((int)((double)n * d / 2.0), (int)((double)n2 * d / 2.0));
    }

    public static ScreenPoint a(double d, double d2) {
        double d3 = c.getWindow().getScaleFactor();
        return new ScreenPoint((int)(d * d3 / 2.0), (int)(d2 * d3 / 2.0));
    }

    public static void a(double d) {
        e = (float)d;
        Window window = c.getWindow();
        f = new Matrix4f((Matrix4fc)RenderSystem.getProjectionMatrix());
        g = new Matrix4f((Matrix4fc)RenderSystem.getModelViewMatrix());
        RenderSystem.setProjectionMatrix((Matrix4f)new Matrix4f().setOrtho(0.0f, (float)((double)window.getFramebufferWidth() / d), (float)((double)window.getFramebufferHeight() / d), 0.0f, 1000.0f, 21000.0f), (ProjectionType)ProjectionType.ORTHOGRAPHIC);
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().identity();
        RenderSystem.getModelViewStack().translate(0.0f, 0.0f, -11000.0f);
    }

    public static void a(float f, float f2, float f3) {
        RenderSystem.getModelViewStack().scale(f, f2, f3);
    }
}

