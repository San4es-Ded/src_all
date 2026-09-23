package haron.animation;

import haron.animation.Easings;
import haron.animation.EasingFunction;

public class HotbarSelectionAnimation {
    private static final int UNSET_SLOT = -1;
    private static final int HOTBAR_LEFT_OFFSET = 91;
    private static final int HOTBAR_SLOT_WIDTH = 20;
    private static final int SELECTION_TEXTURE_OFFSET = 1;
    private float startValue;
    private float targetValue;
    private long startTimeMs;
    private float value = Float.NaN;
    private long durationMs = 160L;
    private int selectedSlot = -1;
    private final EasingFunction easing = Easings.h;

    public float b() {
        if (Float.isNaN(this.value)) {
            return 0.0f;
        }
        return this.value;
    }

    public boolean c() {
        return this.startTimeMs != 0L;
    }

    public void d() {
        this.value = Float.NaN;
        this.startTimeMs = 0L;
        this.selectedSlot = -1;
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

    public void a(int n, int n2, long l) {
        this.durationMs = Math.max(1L, l);
        float f = n2 - 91 - 1 + n * 20;
        if (this.selectedSlot == -1 || Float.isNaN(this.value)) {
            this.selectedSlot = n;
            this.value = f;
            this.startValue = f;
            this.targetValue = f;
            this.startTimeMs = 0L;
            return;
        }
        if (n == this.selectedSlot) {
            this.targetValue = f;
            if (this.startTimeMs == 0L) {
                this.value = f;
                return;
            }
            return;
        }
        this.startValue = this.value;
        this.targetValue = f;
        this.startTimeMs = System.currentTimeMillis();
        this.selectedSlot = n;
    }
}

