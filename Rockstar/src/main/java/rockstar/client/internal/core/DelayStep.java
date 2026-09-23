package rockstar.client.internal.core;



import rockstar.client.util.*;
import rockstar.client.*;
import rockstar.client.internal.core.SequenceStep;
import rockstar.client.util.Stopwatch;

public class DelayStep
implements SequenceStep {
    private final Stopwatch internalField0519 = new Stopwatch();
    private final long internalField0229;

    public DelayStep(long l) {
        this.internalField0229 = l;
    }

    @Override
    public void internalMethod01925() {
        this.internalField0519.internalMethod00701();
    }

    @Override
    public boolean internalMethod01926() {
        return this.internalField0519.internalMethod02365(this.internalField0229);
    }

    @Override
    public void internalMethod01928() {
        this.internalField0519.internalMethod00701();
    }
}

