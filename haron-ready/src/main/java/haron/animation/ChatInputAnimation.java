package haron.animation;

import haron.animation.Easings;
import haron.animation.EasingFunction;

public class ChatInputAnimation {
    private static final float START_OFFSET = 14.0f;
    private float startValue;
    private float targetValue;
    private long startTimeMs;
    private float value = Float.NaN;
    private long durationMs = 160L;
    private final EasingFunction easing = Easings.h;

    public float b() {
        if (Float.isNaN(this.value)) {
            return 0.0f;
        }
        return this.value;
    }

    public boolean c() {
        return this.b() > 0.0f;
    }

    public void d() {
        this.value = Float.NaN;
        this.startTimeMs = 0L;
    }

    public void a() {
        if (Float.isNaN(this.value) || this.startTimeMs == 0L) {
            return;
        }
        long l = System.currentTimeMillis() - this.startTimeMs;
        if (l >= this.durationMs) {
            this.value = this.targetValue;
            this.startTimeMs = 0L;
        } else {
            this.value = (float)((double)this.startValue + (double)(this.targetValue - this.startValue) * this.easing.ease((double)l / (double)this.durationMs));
        }
    }

    public void a(long l) {
        this.durationMs = Math.max(1L, l);
        if (Float.isNaN(this.value)) {
            this.value = 14.0f;
            this.startValue = 14.0f;
            this.targetValue = 0.0f;
            this.startTimeMs = System.currentTimeMillis();
            return;
        }
        if (this.value != this.targetValue) {
            this.startValue = this.value;
            this.targetValue = 0.0f;
            this.startTimeMs = System.currentTimeMillis();
        }
    }
}

