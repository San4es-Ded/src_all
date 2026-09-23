package com.wmedia;

/** Snapshot of the current Windows audio-session peak levels. */
public final class AudioLevelSnapshot {
    private final float masterPeak;
    private final float leftPeak;
    private final float rightPeak;

    public AudioLevelSnapshot(float masterPeak, float leftPeak, float rightPeak) {
        this.masterPeak = masterPeak;
        this.leftPeak = leftPeak;
        this.rightPeak = rightPeak;
    }

    public float getMasterPeak() {
        return this.masterPeak;
    }

    public float getLeftPeak() {
        return this.leftPeak;
    }

    public float getRightPeak() {
        return this.rightPeak;
    }
}
