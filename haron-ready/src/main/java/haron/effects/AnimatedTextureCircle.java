package haron.effects;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import java.awt.Color;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

final class AnimatedTextureCircle {
    final Vec3d position;
    final Identifier texture;
    final float size;
    final Color tintColor;
    private final AnimatedValue alphaAnim = new AnimatedValue();

    float getAlpha() {
        return (float)this.alphaAnim.j();
    }

    boolean isFinished() {
        return this.alphaAnim.d() && this.alphaAnim.i() < 0.01;
    }

    AnimatedTextureCircle(Vec3d vec3d, Identifier identifier, float f, float f2, Color color) {
        this.position = vec3d;
        this.texture = identifier;
        this.size = f;
        this.tintColor = color;
        this.alphaAnim.d(0.0);
        this.alphaAnim.a(1.0, 0.15, Easings.f);
    }

    void update() {
        this.alphaAnim.a();
        if (this.alphaAnim.d() && this.alphaAnim.i() >= 0.99) {
            this.alphaAnim.a(0.0, 1.0, Easings.f);
        }
    }
}
