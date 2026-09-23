package rockstar.client.render;



import rockstar.client.ui.*;
import rockstar.client.*;
import java.util.ArrayDeque;
import java.util.Deque;
import lombok.Generated;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.util.math.MatrixUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import rockstar.client.ui.WindowAccess;
import rockstar.client.render.UiBatchRenderer;

public final class ScissorStack
implements WindowAccess {
    private static final Deque<InternalType0308> internalField0796 = new ArrayDeque<InternalType0308>();
    private static final Deque<Boolean> internalField0797 = new ArrayDeque<Boolean>();
    private static boolean internalField0277;

    private static void internalMethod07650(InternalType0308 nestedValue2037) {
        int n = internalField0267.getFramebufferHeight();
        double d = internalField0267.getScaleFactor();
        float f = nestedValue2037.internalField0205 * (float)d;
        float f2 = nestedValue2037.internalField0206 * (float)d;
        float f3 = (nestedValue2037.internalField0205 + nestedValue2037.internalField1048) * (float)d;
        float f4 = (nestedValue2037.internalField0206 + nestedValue2037.internalField1047) * (float)d;
        int n2 = (int)Math.floor(f);
        int n3 = (int)Math.floor((double)n - Math.ceil(f4) + 0.5);
        int n4 = (int)Math.max(0.0f, (float)((int)Math.ceil(f3) - n2));
        int n5 = (int)Math.max(0.0f, (float)((int)Math.ceil(f4) - (int)Math.floor(f2)) - 1.0f);
        GL11.glEnable((int)3089);
        GL11.glScissor((int)n2, (int)n3, (int)n4, (int)n5);
        rockstar.client.render.compat.ImmediateRenderer.setFramebufferScissor(n2, n3, n4, n5);
    }

    public static void internalMethod02389(float f, float f2, float f3, float f4) {
        InternalType0308 nestedValue2037 = new InternalType0308(f, f2, f3, f4);
        ScissorStack.internalMethod05527(nestedValue2037);
    }

    public static void internalMethod06303(MatrixStack matrixStack, float f, float f2, float f3, float f4) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        InternalType0308 nestedValue2037 = new InternalType0308(f, f2, f3, f4).internalMethod02007(matrix4f);
        ScissorStack.internalMethod05527(nestedValue2037);
    }

    public static void internalMethod06303(org.joml.Matrix3x2fStack matrixStack, float f, float f2, float f3, float f4) {
        internalMethod06303(GuiMatrixCompat.toLegacyStack(matrixStack), f, f2, f3, f4);
    }

    public static void internalMethod02155(Matrix4f matrix4f, float f, float f2, float f3, float f4) {
        InternalType0308 nestedValue2037 = new InternalType0308(f, f2, f3, f4);
        if (matrix4f != null) {
            nestedValue2037 = nestedValue2037.internalMethod02007(matrix4f);
        }
        ScissorStack.internalMethod05527(nestedValue2037);
    }

    private static void internalMethod05527(InternalType0308 nestedValue2037) {
        boolean bl = UiBatchRenderer.internalMethod02577();
        if (bl) {
            UiBatchRenderer.internalMethod02580();
        } else {
            UiBatchRenderer.internalMethod02576();
        }
        if (!internalField0796.isEmpty()) {
            nestedValue2037 = ScissorStack.internalMethod01379(internalField0796.peek(), nestedValue2037);
        }
        internalField0796.push(nestedValue2037);
        internalField0797.push(bl);
        if (bl) {
            UiBatchRenderer.internalMethod08317().internalMethod02386(nestedValue2037.internalField0205, nestedValue2037.internalField0206, nestedValue2037.internalField1048, nestedValue2037.internalField1047);
            if (internalField0277) {
                ScissorStack.internalMethod07650(nestedValue2037);
            }
        } else {
            ScissorStack.internalMethod07650(nestedValue2037);
        }
    }

    public static void internalMethod07643() {
        boolean bl;
        boolean bl2 = bl = !internalField0797.isEmpty() && internalField0797.peek() != false;
        if (bl) {
            UiBatchRenderer.internalMethod02580();
        } else {
            UiBatchRenderer.internalMethod02576();
        }
        if (!internalField0796.isEmpty()) {
            internalField0796.pop();
        }
        if (!internalField0797.isEmpty()) {
            internalField0797.pop();
        }
        if (bl && UiBatchRenderer.internalMethod08317() != null) {
            UiBatchRenderer.internalMethod08317().internalMethod08431();
        }
        if (!internalField0796.isEmpty()) {
            if (!bl || internalField0277) {
                ScissorStack.internalMethod07650(internalField0796.peek());
            }
        } else {
            GL11.glDisable((int)3089);
            rockstar.client.render.compat.ImmediateRenderer.clearScissor();
            internalField0277 = false;
        }
    }

    public static void internalMethod07646() {
        if (internalField0796.isEmpty() || internalField0797.isEmpty() || !internalField0797.peek().booleanValue()) {
            return;
        }
        ScissorStack.internalMethod07650(internalField0796.peek());
        internalField0277 = true;
    }

    public static void internalMethod09075() {
        if (!internalField0277) {
            return;
        }
        GL11.glDisable((int)3089);
        rockstar.client.render.compat.ImmediateRenderer.clearScissor();
        internalField0277 = false;
    }

    private static InternalType0308 internalMethod01379(InternalType0308 nestedValue2037, InternalType0308 nestedValue2038) {
        float f = Math.max(nestedValue2037.internalMethod00053(), nestedValue2038.internalMethod00053());
        float f2 = Math.max(nestedValue2037.internalMethod00058(), nestedValue2038.internalMethod00058());
        float f3 = Math.min(nestedValue2037.internalMethod07699(), nestedValue2038.internalMethod07699());
        float f4 = Math.min(nestedValue2037.internalMethod07700(), nestedValue2038.internalMethod07700());
        float f5 = Math.max(0.0f, f3 - f);
        float f6 = Math.max(0.0f, f4 - f2);
        return new InternalType0308(f, f2, f5, f6);
    }

    public static boolean internalMethod07644() {
        return !internalField0796.isEmpty();
    }

    /** Returns the active Rockstar clip in the GUI coordinate system. */
    public static ScreenRect currentScreenRect() {
        if (internalField0796.isEmpty()) {
            return null;
        }
        InternalType0308 clip = internalField0796.peek();
        int left = (int)Math.floor(clip.internalField0205);
        int top = (int)Math.floor(clip.internalField0206);
        int right = (int)Math.ceil(clip.internalField0205 + clip.internalField1048);
        int bottom = (int)Math.ceil(clip.internalField0206 + clip.internalField1047);
        return new ScreenRect(left, top, Math.max(0, right - left), Math.max(0, bottom - top));
    }

    public static void internalMethod09076() {
        if (UiBatchRenderer.internalMethod02577()) {
            UiBatchRenderer.internalMethod02580();
        } else {
            UiBatchRenderer.internalMethod02576();
        }
        if (UiBatchRenderer.internalMethod08317() != null) {
            for (Boolean bl : internalField0797) {
                if (!bl.booleanValue()) continue;
                UiBatchRenderer.internalMethod08317().internalMethod08431();
            }
        }
        internalField0796.clear();
        internalField0797.clear();
        internalField0277 = false;
        GL11.glDisable((int)3089);
        rockstar.client.render.compat.ImmediateRenderer.clearScissor();
    }

    public static int internalMethod07642() {
        return internalField0796.size();
    }

    /**
     * Restores the stack after an isolated UI renderer has finished. A widget
     * must never be able to leave its clipping rectangle active for the rest
     * of the HUD or the next frame.
     */
    public static void restoreDepth(int expectedDepth) {
        int targetDepth = Math.max(0, expectedDepth);
        if (internalField0796.size() < targetDepth || internalField0796.size() != internalField0797.size()) {
            ScissorStack.internalMethod09076();
            return;
        }

        while (internalField0796.size() > targetDepth) {
            ScissorStack.internalMethod07643();
        }
    }

    @Deprecated
    public static void internalMethod02264(float f, float f2, float f3, float f4) {
        ScissorStack.internalMethod02389(f, f2, f3, f4);
    }

    @Deprecated
    public static void internalMethod06983(float f, float f2, float f3, float f4, MatrixStack matrixStack) {
        if (matrixStack != null) {
            ScissorStack.internalMethod06303(matrixStack, f, f2, f3, f4);
        } else {
            ScissorStack.internalMethod02389(f, f2, f3, f4);
        }
    }

    public static void internalMethod06983(float f, float f2, float f3, float f4, org.joml.Matrix3x2fStack matrixStack) {
        internalMethod06983(f, f2, f3, f4, GuiMatrixCompat.toLegacyStack(matrixStack));
    }

    @Deprecated
    public static void internalMethod06715(float f, float f2, float f3, float f4, Matrix4f matrix4f) {
        ScissorStack.internalMethod02155(matrix4f, f, f2, f3, f4);
    }

    @Deprecated
    public static void internalMethod09084() {
        ScissorStack.internalMethod07643();
    }

    @Generated
    private ScissorStack() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static class InternalType0308 {
        final float internalField0205;
        final float internalField0206;
        final float internalField1048;
        final float internalField1047;

        InternalType0308(float f, float f2, float f3, float f4) {
            this.internalField0205 = f;
            this.internalField0206 = f2;
            this.internalField1048 = f3;
            this.internalField1047 = f4;
        }

        float internalMethod00053() {
            return this.internalField0205;
        }

        float internalMethod00058() {
            return this.internalField0206;
        }

        float internalMethod07699() {
            return this.internalField0205 + this.internalField1048;
        }

        float internalMethod07700() {
            return this.internalField0206 + this.internalField1047;
        }

        InternalType0308 internalMethod02007(Matrix4f matrix4f) {
            if (MatrixUtil.isIdentity((Matrix4f)matrix4f)) {
                return new InternalType0308(this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047);
            }
            Vector3f vector3f = new Vector3f(this.internalField0205, this.internalField0206, 0.0f);
            Vector3f vector3f2 = new Vector3f(this.internalField0205 + this.internalField1048, this.internalField0206 + this.internalField1047, 0.0f);
            matrix4f.transformPosition(vector3f);
            matrix4f.transformPosition(vector3f2);
            return new InternalType0308(vector3f.x, vector3f.y, vector3f2.x - vector3f.x, vector3f2.y - vector3f.y);
        }
    }
}
