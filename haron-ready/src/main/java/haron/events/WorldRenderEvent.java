package haron.events;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class WorldRenderEvent {
    private final MatrixStack matrices;
    private final float tickDelta;
    private final Camera camera;

    public MatrixStack matrices() {
        return this.matrices;
    }

    public float tickDelta() {
        return this.tickDelta;
    }

    public Camera camera() {
        return this.camera;
    }

    public WorldRenderEvent(MatrixStack matrixStack, float f, Camera camera) {
        this.matrices = matrixStack;
        this.tickDelta = f;
        this.camera = camera;
    }

    public float b() {
        return this.tickDelta;
    }

    public Camera c() {
        int n = 98;
        return this.camera;
    }

    public MatrixStack a() {
        return this.matrices;
    }
}

