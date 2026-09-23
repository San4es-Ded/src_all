package rockstar.client.render;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix3x2fc;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

/** Bridges 1.21.11's 2D GUI pose into Rockstar's legacy 4x4 vertex code. */
public final class GuiMatrixCompat {
    private GuiMatrixCompat() {
    }

    public static Matrix4f toMatrix4f(Matrix3x2fc matrix) {
        return new Matrix4f()
            .m00(matrix.m00()).m01(matrix.m01())
            .m10(matrix.m10()).m11(matrix.m11())
            .m30(matrix.m20()).m31(matrix.m21());
    }

    public static MatrixStack toLegacyStack(Matrix3x2fc matrix) {
        MatrixStack stack = new MatrixStack();
        stack.peek().getPositionMatrix().set(toMatrix4f(matrix));
        return stack;
    }

    public static void multiply(Matrix3x2fStack stack, Matrix4fc matrix) {
        stack.mul(new Matrix3x2f(
            matrix.m00(), matrix.m01(),
            matrix.m10(), matrix.m11(),
            matrix.m30(), matrix.m31()
        ));
    }
}
