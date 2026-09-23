package rockstar.client.internal.script;


import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import lombok.Generated;
import rockstar.client.compat.ShaderProgramKey;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import pyrock.utility.render.ColorRGBA;

public final class DiamondMarkerRenderer {
    private static final Vector3f[] internalField0656 = new Vector3f[]{new Vector3f(0.0f, 1.5f, 0.0f), new Vector3f(0.0f, -1.5f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(-1.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f)};
    private static final int[][] internalField0040 = new int[][]{{0, 2, 4}, {0, 4, 3}, {0, 3, 5}, {0, 5, 2}, {1, 4, 2}, {1, 3, 4}, {1, 5, 3}, {1, 2, 5}};
    private static final float[] internalField0615 = new float[]{1.0f, 0.8f, 0.6f, 0.9f, 0.7f, 0.5f, 0.4f, 0.6f};

    public static void internalMethod04026(MatrixStack matrixStack, BufferBuilder bufferBuilder, float f, float f2, float f3, float f4, ColorRGBA colorRGBA) {
        matrixStack.push();
        matrixStack.translate(f, f2, f3);
        matrixStack.scale(f4, f4, f4);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        for (int i = 0; i < internalField0040.length; ++i) {
            int[] nArray = internalField0040[i];
            float f5 = internalField0615[i];
            Vector3f vector3f = internalField0656[nArray[0]];
            Vector3f vector3f2 = internalField0656[nArray[1]];
            Vector3f vector3f3 = internalField0656[nArray[2]];
            int n = DiamondMarkerRenderer.internalMethod00803(colorRGBA.getRGB(), f5);
            bufferBuilder.vertex(matrix4f, vector3f.x, vector3f.y, vector3f.z).color(n);
            bufferBuilder.vertex(matrix4f, vector3f2.x, vector3f2.y, vector3f2.z).color(n);
            bufferBuilder.vertex(matrix4f, vector3f3.x, vector3f3.y, vector3f3.z).color(n);
        }
        matrixStack.pop();
    }

    public static BufferBuilder internalMethod05818() {
        DiamondMarkerRenderer.internalMethod02022();
        return Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
    }

    private static void internalMethod02022() {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static int internalMethod00803(int n, float f) {
        int n2 = n >> 24 & 0xFF;
        int n3 = (int)((float)(n >> 16 & 0xFF) * f);
        int n4 = (int)((float)(n >> 8 & 0xFF) * f);
        int n5 = (int)((float)(n & 0xFF) * f);
        n3 = Math.min(255, Math.max(0, n3));
        n4 = Math.min(255, Math.max(0, n4));
        n5 = Math.min(255, Math.max(0, n5));
        return n2 << 24 | n3 << 16 | n4 << 8 | n5;
    }

    @Generated
    private DiamondMarkerRenderer() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

