package haron.effects;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.effects.JumpCircles;
import net.minecraft.util.math.Vec3d;

final class JumpWave {
    final Vec3d position;
    private final AnimatedValue radiusAnimation = new AnimatedValue();
    private final AnimatedValue alphaAnimation = new AnimatedValue();

    boolean isFinished() {
        return this.radiusAnimation.d() && this.alphaAnimation.d();
    }

    float radius() {
        return (float)this.radiusAnimation.j();
    }

    JumpWave(JumpCircles uhj26b2, Vec3d vec3d, float f, float f2) {
        this.position = vec3d;
        this.radiusAnimation.a(0.0);
        this.radiusAnimation.a((double)f, (double)f2, Easings.f);
        this.alphaAnimation.a(1.0);
        this.alphaAnimation.a(0.0, (double)f2, Easings.s);
    }

    void update() {
        this.radiusAnimation.a();
        this.alphaAnimation.a();
    }

    float alpha() {
        return (float)this.alphaAnimation.j();
    }
}
