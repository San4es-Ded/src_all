package wtf.wyvern.core.neuro;

import java.util.Arrays;

final class NeuroSample {
    private final double[] inputs;
    private final float yawStep;
    private final float pitchStep;
    private final int tick;
    private boolean attack;

    NeuroSample(double[] inputs, float yawStep, float pitchStep, int tick, boolean attack) {
        this.inputs = Arrays.copyOf(inputs, inputs.length);
        this.yawStep = yawStep;
        this.pitchStep = pitchStep;
        this.tick = tick;
        this.attack = attack;
    }

    NeuroSample(NeuroSample other) {
        this(other.inputs, other.yawStep, other.pitchStep, other.tick, other.attack);
    }

    double[] inputs() {
        return inputs;
    }

    float yawStep() {
        return yawStep;
    }

    float pitchStep() {
        return pitchStep;
    }

    int tick() {
        return tick;
    }

    boolean attack() {
        return attack;
    }

    void markAttack() {
        this.attack = true;
    }
}
