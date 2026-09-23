package haron.animation;

import haron.animation.Easings;
import haron.animation.EasingFunction;

public class PlayerListAnimation {
    private float value;
    private float startValue;
    private long startTimeMs;
    private boolean shown;
    private boolean renderable;
    private float targetValue = 1.0f;
    private long durationMs = 200L;
    private final EasingFunction easing = Easings.h;

    public boolean e() {
        return this.startTimeMs != 0L;
    }

    public float b() {
        return this.value;
    }

    public void b(long l) {
        this.durationMs = Math.max(1L, l);
        this.startValue = this.value;
        this.targetValue = 0.0f;
        this.startTimeMs = System.currentTimeMillis();
        this.shown = false;
        this.renderable = true;
    }

    public boolean c() {
        return this.shown;
    }

    public void f() {
        this.value = 1.0f;
        this.startTimeMs = 0L;
        this.shown = true;
        this.renderable = true;
    }

    public boolean d() {
        return this.renderable;
    }

    public void a(long l) {
        this.durationMs = Math.max(1L, l);
        this.startValue = this.value;
        this.targetValue = 1.0f;
        this.startTimeMs = System.currentTimeMillis();
        this.shown = true;
        this.renderable = true;
    }

    public void a() {
        if (this.startTimeMs == 0L) {
            return;
        }
        long l = System.currentTimeMillis() - this.startTimeMs;
        if (l < this.durationMs) {
            this.value = (float)((double)this.startValue + (double)(this.targetValue - this.startValue) * this.easing.ease((double)l / (double)this.durationMs));
        } else {
            this.value = this.targetValue;
            this.startTimeMs = 0L;
            if (this.shown) {
                return;
            }
            this.renderable = false;
        }
    }
}

