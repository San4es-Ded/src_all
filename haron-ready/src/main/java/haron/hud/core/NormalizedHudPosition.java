package haron.hud.core;

public class NormalizedHudPosition {
    private float normalizedCenterX;
    private float normalizedCenterY;

    public float normalizedCenterX() {
        return this.normalizedCenterX;
    }

    public float normalizedCenterY() {
        return this.normalizedCenterY;
    }

    private static float clamp01(float f) {
        return Math.max(0.0f, Math.min(1.0f, f));
    }

    public float resolveX(float f, float f2) {
        return this.normalizedCenterX * f2 - f / 2.0f;
    }

    public float resolveY(float f, float f2) {
        return this.normalizedCenterY * f2 - f / 2.0f;
    }

    public void setNormalizedCenterY(float f) {
        this.normalizedCenterY = NormalizedHudPosition.clamp01(f);
    }

    public void setNormalizedCenterX(float f) {
        this.normalizedCenterX = NormalizedHudPosition.clamp01(f);
    }

    public void update(float f, float f2, float f3, float f4, float f5, float f6) {
        this.normalizedCenterX = NormalizedHudPosition.clamp01((f + f3 / 2.0f) / f5);
        this.normalizedCenterY = NormalizedHudPosition.clamp01((f2 + f4 / 2.0f) / f6);
    }

    public void b(float f) {
        int n = 271;
        this.setNormalizedCenterY(f);
    }

    public float b() {
        return this.normalizedCenterY();
    }

    public float b(float f, float f2) {
        return this.resolveY(f, f2);
    }

    public void a(float f, float f2, float f3, float f4, float f5, float f6) {
        this.update(f, f2, f3, f4, f5, f6);
    }

    public float a(float f, float f2) {
        return this.resolveX(f, f2);
    }

    public void a(float f) {
        this.setNormalizedCenterX(f);
    }

    public float a() {
        int n = 360;
        return this.normalizedCenterX();
    }
}

