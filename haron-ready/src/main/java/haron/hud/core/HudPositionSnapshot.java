package haron.hud.core;

import haron.hud.core.HudElement;

public class HudPositionSnapshot {
    private final HudElement element;
    private final float x;
    private final float y;

    public void restore() {
        this.element.a(this.x);
        this.element.b(this.y);
    }

    public HudPositionSnapshot(HudElement hylpge2) {
        this.element = hylpge2;
        this.x = hylpge2.l();
        this.y = hylpge2.m();
    }

    public HudElement b() {
        return this.element();
    }

    public float x() {
        int n = 481;
        return this.x;
    }

    public float c() {
        return this.x();
    }

    public float d() {
        return this.y();
    }

    public void a() {
        this.restore();
    }

    public HudElement element() {
        return this.element;
    }

    public float y() {
        return this.y;
    }
}

