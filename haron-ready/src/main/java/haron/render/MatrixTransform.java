package haron.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class MatrixTransform {
    private MatrixStack matrices;

    public void e(float f) {
        this.b(f);
    }

    public void b(float f) {
        this.matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f));
    }

    public void b(float f, float f2, float f3) {
        this.matrices.scale(f, f2, f3);
    }

    public void b() {
        this.matrices.pop();
    }

    public MatrixStack c() {
        return this.matrices;
    }

    public void c(float f, float f2, float f3) {
        if (f3 != 0.0f) {
            this.c(f3);
        }
        if (f2 != 0.0f) {
            this.b(f2);
        }
        if (f != 0.0f) {
            this.a(f);
        }
    }

    public void c(float f) {
        this.matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f));
    }

    public void f(float f) {
        this.c(f);
    }

    public void d(float f) {
        this.a(f);
    }

    public void d(float f, float f2, float f3) {
        this.c(f, f2, f3);
    }

    public void a(float f) {
        this.matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(f));
    }

    public void a(MatrixStack matrixStack) {
        this.matrices = matrixStack;
    }

    public void a(float f, float f2, float f3) {
        this.matrices.translate(f, f2, f3);
    }

    public void a() {
        this.matrices.push();
    }
}

