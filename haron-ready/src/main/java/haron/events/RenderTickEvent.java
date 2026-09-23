package haron.events;

public class RenderTickEvent {
    private final float tickDelta;

    public float tickDelta() {
        return this.tickDelta;
    }

    public RenderTickEvent(float f) {
        this.tickDelta = f;
    }

    public float a() {
        return this.tickDelta;
    }
}

