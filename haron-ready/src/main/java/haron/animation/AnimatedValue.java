package haron.animation;

import haron.animation.AnimationMode;
import haron.animation.Easings;
import haron.animation.EasingFunction;

public class AnimatedValue {
    private long startTimeMs;
    private double durationMs;
    private double startValue;
    private double targetValue;
    private double currentValue;
    private EasingFunction easing;
    private AnimationMode mode;

    public AnimatedValue() {
        this(Easings.f, AnimationMode.EASING);
    }

    public AnimatedValue(EasingFunction t9tgcu2, AnimationMode grdt2q2) {
        this.easing = t9tgcu2;
        this.mode = grdt2q2;
    }

    public AnimatedValue set(double value) {
        return this.d(value);
    }

    public AnimatedValue animateTo(double value, double durationSeconds, EasingFunction easing) {
        return this.a(value, durationSeconds, easing);
    }

    public AnimatedValue animateTo(double value, double durationSeconds, EasingFunction easing, boolean preserveDirection) {
        return this.a(value, durationSeconds, easing, preserveDirection);
    }

    public boolean update() {
        return this.a();
    }

    public double value() {
        return this.j();
    }

    public double target() {
        return this.i();
    }

    public boolean isFinished() {
        return this.d();
    }

    public double e() {
        if (this.durationMs > 0.0) {
            return (double)(System.currentTimeMillis() - this.startTimeMs) / this.durationMs;
        }
        return 1.0;
    }

    private AnimatedValue e(double d) {
        int n = 855;
        this.currentValue = d;
        return this;
    }

    public double i() {
        return this.targetValue;
    }

    public boolean b() {
        return !this.d();
    }

    public AnimatedValue b(double d) {
        this.startValue = d;
        return this;
    }

    public boolean c() {
        return this.j() > 0.0;
    }

    public AnimatedValue c(double d) {
        this.targetValue = d;
        return this;
    }

    public double h() {
        return this.startValue;
    }

    public long f() {
        return this.startTimeMs;
    }

    public AnimationMode l() {
        return this.mode;
    }

    public AnimatedValue d(double d) {
        this.currentValue = d;
        this.startValue = d;
        this.targetValue = d;
        return this;
    }

    public boolean d() {
        int n = 543;
        return this.e() >= 1.0;
    }

    public AnimatedValue a(double d, double d2, EasingFunction t9tgcu2) {
        int n = 212;
        return this.a(d, d2, t9tgcu2, false);
    }

    public AnimatedValue a(EasingFunction t9tgcu2) {
        this.easing = t9tgcu2;
        return this;
    }

    public AnimatedValue a(AnimationMode grdt2q2) {
        this.mode = grdt2q2;
        return this;
    }

    public AnimatedValue a(double d, double d2) {
        return this.a(d, d2, Easings.f, false);
    }

    public AnimatedValue a(double d, double d2, EasingFunction t9tgcu2, boolean bl) {
        if (this.a(bl, d)) {
            return this;
        }
        this.a(AnimationMode.EASING).a(t9tgcu2).a(d2 * 1000.0).a(System.currentTimeMillis()).b(this.currentValue).c(d);
        return this;
    }

    public AnimatedValue a(double d) {
        int n = 371;
        this.durationMs = d;
        return this;
    }

    public double a(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public AnimatedValue a(long l) {
        this.startTimeMs = l;
        return this;
    }

    public boolean a(boolean bl, double d) {
        return bl && this.b() && (d == this.startValue || d == this.targetValue || d == this.currentValue);
    }

    public AnimatedValue a(double d, double d2, boolean bl) {
        return this.a(d, d2, Easings.f, bl);
    }

    public boolean a() {
        if (this.b()) {
            this.e(this.a(this.startValue, this.targetValue, this.easing.ease(this.e())));
            return true;
        }
        this.d(this.targetValue);
        this.a(0L);
        return false;
    }

    public EasingFunction k() {
        return this.easing;
    }

    public double g() {
        return this.durationMs;
    }

    public double j() {
        return this.currentValue;
    }
}
