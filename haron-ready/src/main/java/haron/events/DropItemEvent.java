package haron.events;

import haron.events.CancellableEvent;

public class DropItemEvent
extends CancellableEvent {
    private final int slot;
    private final boolean dropEntireStack;

    public boolean dropEntireStack() {
        return this.dropEntireStack;
    }

    public int slot() {
        return this.slot;
    }

    public DropItemEvent(int n, boolean bl) {
        this.slot = n;
        this.dropEntireStack = bl;
    }

    public boolean e() {
        return this.dropEntireStack;
    }

    public int d() {
        return this.slot;
    }
}

