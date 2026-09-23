package haron.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

public final class CameraMatrixScope {
    private static final Matrix4f CAMERA_MATRIX = new Matrix4f();
    private static int depth = 0;

    public static void forceReset() {
        while (depth > 0) {
            try {
                RenderSystem.getModelViewStack().popMatrix();
            }
            catch (Throwable throwable) {
                break;
            }
            --depth;
        }
        depth = 0;
    }

    public static Matrix4f cameraMatrix() {
        return CameraMatrixScope.buildCameraMatrix();
    }

    private static Matrix4f buildCameraMatrix() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.gameRenderer == null) {
            return CAMERA_MATRIX.identity();
        }
        Camera camera = minecraftClient.gameRenderer.getCamera();
        return CAMERA_MATRIX.identity().rotateX((float)Math.toRadians(camera.getPitch())).rotateY((float)Math.toRadians(camera.getYaw() + 180.0f));
    }

    private CameraMatrixScope() {
    }

    public static Matrix4f begin() {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.identity();
        ++depth;
        return CameraMatrixScope.buildCameraMatrix();
    }

    public static void end() {
        if (depth <= 0) {
            return;
        }
        try {
            RenderSystem.getModelViewStack().popMatrix();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        --depth;
    }
}

