package haron.events;

import net.minecraft.client.util.math.MatrixStack;

public class HudRenderPostEvent {
    private final MatrixStack matrices;
    private final int width;
    private final int height;
    private final float tickDelta;

    public int height() {
        return this.height;
    }

    public MatrixStack matrices() {
        return this.matrices;
    }

    public float tickDelta() {
        return this.tickDelta;
    }

    public HudRenderPostEvent(MatrixStack matrixStack, int n, int n2, float f) {
        this.matrices = matrixStack;
        this.width = n;
        this.height = n2;
        this.tickDelta = f;
    }

    public int b() {
        return this.width;
    }

    public int c() {
        return this.height;
    }

    public float d() {
        return this.tickDelta;
    }

    public MatrixStack a() {
        int n = 322;
        return this.matrices;
    }

    public int width() {
        return this.width;
    }
}

