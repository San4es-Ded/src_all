package haron.model;

import haron.model.g42uq5;
import haron.model.h3ql66;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

public final class j4j4x0 {
    private static final float MODEL_SCALE = 16.0f;

    public static void translatePartPivot(h3ql66 h3ql662, MatrixStack matrixStack) {
        matrixStack.translate(h3ql662.j() / 16.0f, h3ql662.k() / 16.0f, h3ql662.l() / 16.0f);
    }

    public static void translateCubePivot(g42uq5 g42uq52, MatrixStack matrixStack) {
        matrixStack.translate(g42uq52.c.a() / 16.0f, g42uq52.c.b() / 16.0f, g42uq52.c.c() / 16.0f);
    }

    public static void rotatePart(h3ql66 h3ql662, MatrixStack matrixStack) {
        if (h3ql662.c() != 0.0f) {
            matrixStack.multiply(new Quaternionf().rotationZ(h3ql662.c()));
        }
        if (h3ql662.b() != 0.0f) {
            matrixStack.multiply(new Quaternionf().rotationY(h3ql662.b()));
        }
        if (h3ql662.a() != 0.0f) {
            matrixStack.multiply(new Quaternionf().rotationX(h3ql662.a()));
        }
    }

    public static void scalePart(h3ql66 h3ql662, MatrixStack matrixStack) {
        matrixStack.scale(h3ql662.g(), h3ql662.h(), h3ql662.i());
    }

    public static void rotateCube(g42uq5 g42uq52, MatrixStack matrixStack) {
        if (g42uq52.d.c() != 0.0f) {
            matrixStack.multiply(new Quaternionf().rotationZ(g42uq52.d.c()));
        }
        if (g42uq52.d.b() != 0.0f) {
            matrixStack.multiply(new Quaternionf().rotationY(g42uq52.d.b()));
        }
        if (g42uq52.d.a() != 0.0f) {
            matrixStack.multiply(new Quaternionf().rotationX(g42uq52.d.a()));
        }
    }

    public static void translatePartOffset(h3ql66 h3ql662, MatrixStack matrixStack) {
        matrixStack.translate(-h3ql662.d() / 16.0f, h3ql662.e() / 16.0f, h3ql662.f() / 16.0f);
    }

    public static void translatePartPivotBack(h3ql66 h3ql662, MatrixStack matrixStack) {
        matrixStack.translate(-h3ql662.j() / 16.0f, -h3ql662.k() / 16.0f, -h3ql662.l() / 16.0f);
    }

    public static void translateCubePivotBack(g42uq5 g42uq52, MatrixStack matrixStack) {
        matrixStack.translate(-g42uq52.c.a() / 16.0f, -g42uq52.c.b() / 16.0f, -g42uq52.c.c() / 16.0f);
    }

    private j4j4x0() {
    }
}

