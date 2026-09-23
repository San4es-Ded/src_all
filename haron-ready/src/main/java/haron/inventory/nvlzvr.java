package haron.inventory;

import haron.inventory.nzsxbq;

class nvlzvr
extends nzsxbq {
    private final float progress;
    private final float remainingSeconds;

    nvlzvr(float f, float f2) {
        super(0, 0, 1);
        this.progress = f;
        this.remainingSeconds = f2;
    }

    @Override
    public float b() {
        return this.remainingSeconds;
    }

    @Override
    public float c() {
        return this.progress;
    }

    @Override
    public boolean a() {
        return this.progress > 0.0f;
    }
}

