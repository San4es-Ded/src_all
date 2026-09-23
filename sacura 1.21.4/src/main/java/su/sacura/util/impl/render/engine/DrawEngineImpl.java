package su.sacura.util.impl.render.engine;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector4i;
import su.sacura.util.type.MinecraftWrapper;

public class DrawEngineImpl implements DrawEngine, MinecraftWrapper {
    public void quad(Matrix4f matrix4f, BufferBuilder buffer, float x, float y, float width, float height) {
        buffer.vertex(matrix4f, x, y, 0.0F);
        buffer.vertex(matrix4f, x, y + height, 0.0F);
        buffer.vertex(matrix4f, x + width, y + height, 0.0F);
        buffer.vertex(matrix4f, x + width, y, 0.0F);
    }

    public void quad(Matrix4f matrix4f, BufferBuilder buffer, float x, float y, float width, float height, int color) {
        buffer.vertex(matrix4f, x, y, 0.0F).color(color);
        buffer.vertex(matrix4f, x, y + height, 0.0F).color(color);
        buffer.vertex(matrix4f, x + width, y + height, 0.0F).color(color);
        buffer.vertex(matrix4f, x + width, y, 0.0F).color(color);
    }

    public void quad(Matrix4f matrix4f, float x, float y, float width, float height, int color) {
        // Исправлено: field_53880 -> ShaderProgramKeys.POSITION_TEX_COLOR
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(matrix4f, x, y + height, 0.0F).texture(0.0F, 0.0F).color(color);
        buffer.vertex(matrix4f, x + width, y + height, 0.0F).texture(0.0F, 1.0F).color(color);
        buffer.vertex(matrix4f, x + width, y, 0.0F).texture(1.0F, 1.0F).color(color);
        buffer.vertex(matrix4f, x, y, 0.0F).texture(1.0F, 0.0F).color(color);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    public void quadTexture(MatrixStack.Entry entry, BufferBuilder buffer, float x, float y, float width, float height, Vector4i color) {
        // Исправлено: метод vertex для Entry в 1.21.4 отсутствует. Используем Matrix4f из entry.
        Matrix4f m = entry.getPositionMatrix();
        buffer.vertex(m, x, y + height, 0.0F).texture(0.0F, 0.0F).color(color.x);
        buffer.vertex(m, x + width, y + height, 0.0F).texture(0.0F, 1.0F).color(color.y);
        buffer.vertex(m, x + width, y, 0.0F).texture(1.0F, 1.0F).color(color.w);
        buffer.vertex(m, x, y, 0.0F).texture(1.0F, 0.0F).color(color.z);
    }
}