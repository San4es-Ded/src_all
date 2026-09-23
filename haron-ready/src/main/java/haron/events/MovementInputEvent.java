package haron.events;

import haron.events.CancellableEvent;

public class MovementInputEvent
extends CancellableEvent {
    private float forward;
    private float sideways;
    private boolean jumping;
    private boolean sneaking;
    private double sneakSlowdownMultiplier;

    public double sneakSlowdownMultiplier() {
        return this.sneakSlowdownMultiplier;
    }

    public void setSneakSlowdownMultiplier(double d) {
        this.sneakSlowdownMultiplier = d;
    }

    public float forward() {
        return this.forward;
    }

    public void setSideways(float f) {
        this.sideways = f;
    }

    public void setSneaking(boolean bl) {
        this.sneaking = bl;
    }

    public float sideways() {
        return this.sideways;
    }

    public void setForward(float f) {
        this.forward = f;
    }

    public boolean sneaking() {
        return this.sneaking;
    }

    public boolean jumping() {
        return this.jumping;
    }

    public void setJumping(boolean bl) {
        this.jumping = bl;
    }

    public MovementInputEvent(float f, float f2, boolean bl, boolean bl2, double d) {
        this.forward = f;
        this.sideways = f2;
        this.jumping = bl;
        this.sneaking = bl2;
        this.sneakSlowdownMultiplier = d;
    }

    public float e() {
        return this.sideways;
    }

    public void b(float f) {
        int n = 349;
        this.sideways = f;
    }

    public void b(boolean bl) {
        this.jumping = bl;
    }

    public void c(boolean bl) {
        this.sneaking = bl;
    }

    public double h() {
        return this.sneakSlowdownMultiplier;
    }

    public boolean f() {
        return this.jumping;
    }

    public float d() {
        return this.forward;
    }

    public void a(double d) {
        this.sneakSlowdownMultiplier = d;
    }

    public void a(float f) {
        this.forward = f;
    }

    public boolean g() {
        return this.sneaking;
    }
}

