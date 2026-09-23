package wtf.wyvern.render.level;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.astroguard.J2C.FastNative;

/** Small shaded crystal mesh used by TargetESP's Crystall2 mode. */
@FastNative
public final class CrystalRenderer {
    private static final Vector3f[] VERTICES = {
            new Vector3f(0.0F, 1.5F, 0.0F),
            new Vector3f(0.0F, -1.5F, 0.0F),
            new Vector3f(1.0F, 0.0F, 0.0F),
            new Vector3f(-1.0F, 0.0F, 0.0F),
            new Vector3f(0.0F, 0.0F, 1.0F),
            new Vector3f(0.0F, 0.0F, -1.0F)
    };
    private static final int[][] FACES = {
            {0, 2, 4}, {0, 4, 3}, {0, 3, 5}, {0, 5, 2},
            {1, 4, 2}, {1, 3, 4}, {1, 5, 3}, {1, 2, 5}
    };
    private static final float[] FACE_BRIGHTNESS = {
            1.0F, 0.8F, 0.6F, 0.9F, 0.7F, 0.5F, 0.4F, 0.6F
    };

    public static void render(MatrixStack matrices, BufferBuilder buffer, float x, float y, float z,
                              float size, ColorRGBA color) {
        matrices.push();
        matrices.translate(x, y, z);
        matrices.scale(size, size, size);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        for (int i = 0; i < FACES.length; i++) {
            int[] face = FACES[i];
            int shadedColor = applyBrightness(color.getRGB(), FACE_BRIGHTNESS[i]);
            Vector3f v1 = VERTICES[face[0]];
            Vector3f v2 = VERTICES[face[1]];
            Vector3f v3 = VERTICES[face[2]];
            buffer.vertex(matrix, v1.x, v1.y, v1.z).color(shadedColor);
            buffer.vertex(matrix, v2.x, v2.y, v2.z).color(shadedColor);
            buffer.vertex(matrix, v3.x, v3.y, v3.z).color(shadedColor);
        }
        matrices.pop();
    }

    public static BufferBuilder createBuffer() {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        return Tessellator.getInstance().begin(VertexFormat.DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
    }

    private static int applyBrightness(int color, float brightness) {
        int alpha = color >>> 24 & 255;
        int red = Math.min(255, Math.max(0, (int) ((color >>> 16 & 255) * brightness)));
        int green = Math.min(255, Math.max(0, (int) ((color >>> 8 & 255) * brightness)));
        int blue = Math.min(255, Math.max(0, (int) ((color & 255) * brightness)));
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    private CrystalRenderer() {
    }
}
