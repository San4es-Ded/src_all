package haron.events;

import haron.events.CancellableEvent;

public class MouseScrollEvent
extends CancellableEvent {
    private final double horizontal;
    private final double vertical;

    public double vertical() {
        return this.vertical;
    }

    public double horizontal() {
        return this.horizontal;
    }

    public MouseScrollEvent(double d, double d2) {
        this.horizontal = d;
        this.vertical = d2;
    }

    public double e() {
        return this.vertical;
    }

    public double d() {
        return this.horizontal;
    }
}

