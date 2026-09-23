package haron.animation;

import haron.animation.Easings;
import haron.animation.EasingFunction;

public class TimedFloatAnimation {
    private float value;
    private float startValue;
    private float targetValue;
    private long durationMs;
    private final EasingFunction easing = Easings.h;
    private long startTimeMs = System.currentTimeMillis();

    public TimedFloatAnimation(float f, float f2, long l) {
        this.value = f;
        this.startValue = f;
        this.targetValue = f2;
        this.durationMs = Math.max(1L, l);
    }

    public float b() {
        return this.value;
    }

    public boolean c() {
        return this.startTimeMs != 0L;
    }

    public boolean d() {
        return this.startTimeMs == 0L && this.value == this.targetValue;
    }

    public void a() {
        if (this.startTimeMs == 0L) {
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
}

