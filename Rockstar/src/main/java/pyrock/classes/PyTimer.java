package pyrock.classes;


import rockstar.client.util.*;
import rockstar.client.util.Stopwatch;

public class PyTimer {
    private final Stopwatch timer = new Stopwatch();

    public PyTimer() {
        this.timer.internalMethod00701();
    }

    public void reset() {
        this.timer.internalMethod00701();
    }

    public boolean finished(long l) {
        return this.timer.internalMethod02365(l);
    }

    public boolean passed(long l) {
        return this.timer.internalMethod02365(l);
    }

    public long elapsed() {
        return this.timer.internalMethod00700();
    }
}

