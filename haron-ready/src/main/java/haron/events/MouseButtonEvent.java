package haron.events;

import haron.events.CancellableEvent;

public class MouseButtonEvent
extends CancellableEvent {
    private final int button;
    private final int action;
    private final int modifiers;
    private final double mouseX;
    private final double mouseY;

    public int button() {
        return this.button;
    }

    public double mouseX() {
        return this.mouseX;
    }

    public double mouseY() {
        return this.mouseY;
    }

    public int modifiers() {
        return this.modifiers;
    }

    public MouseButtonEvent(int n, int n2, int n3, double d, double d2) {
        this.button = n;
        this.action = n2;
        this.modifiers = n3;
        this.mouseX = d;
        this.mouseY = d2;
    }

    public int e() {
        return this.action;
    }

    public double h() {
        return this.mouseY;
    }

    public int f() {
        return this.modifiers;
    }

    public int d() {
        int n = 132;
        return this.button;
    }

    public double g() {
        int n = 879;
        return this.mouseX;
    }

    public int action() {
        return this.action;
    }
}

