package haron.animation;

import haron.animation.Easings;
import haron.animation.EasingFunction;

public class PerspectiveDistanceAnimation {
    private static final float FIRST_PERSON_DISTANCE = 1.0f;
    private static final float THIRD_PERSON_DISTANCE = 4.0f;
    private long startTimeMs;
    private boolean thirdPerson;
    private float value = 4.0f;
    private float startValue = 4.0f;
    private float targetValue = 4.0f;
    private long durationMs = 300L;
    private final EasingFunction easing = Easings.C;

    public float distance() {
        return this.value;
    }

    public boolean isRunning() {
        return this.startTimeMs != 0L;
    }

    public void reset() {
        this.value = 4.0f;
        this.startValue = 4.0f;
        this.targetValue = 4.0f;
        this.startTimeMs = 0L;
        this.thirdPerson = false;
    }

    public void update() {
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

    public void setThirdPerson(boolean thirdPerson, long durationMs) {
        this.durationMs = Math.max(1L, durationMs);
        if (thirdPerson && !this.thirdPerson) {
            this.startValue = 1.0f;
            this.targetValue = 4.0f;
            this.value = 1.0f;
            this.startTimeMs = System.currentTimeMillis();
        } else if (!thirdPerson && this.thirdPerson) {
            this.startValue = this.value;
            this.targetValue = 1.0f;
            this.startTimeMs = System.currentTimeMillis();
        }
        this.thirdPerson = thirdPerson;
    }
}
