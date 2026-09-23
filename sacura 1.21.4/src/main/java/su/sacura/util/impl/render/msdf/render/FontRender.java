package su.sacura.util.impl.render.msdf.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import su.sacura.util.impl.render.msdf.api.MsdfFont;
import su.sacura.util.impl.render.providers.ColorProvider;
import su.sacura.util.impl.render.providers.ResourceProvider;

public final class FontRender {
    private static final ShaderProgramKey MSDF_FONT_SHADER_KEY = new ShaderProgramKey(ResourceProvider.getShaderIdentifier("msdf_font"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);

    private final MsdfFont font;

    public FontRender(MsdfFont font) {
        this.font = font;
    }

    public MsdfFont getFont() {
        return this.font;
    }

    public void draw(DrawContext context, String text, float x, float y, float size, int color) {
        draw(context.getMatrices().peek().getPositionMatrix(), text, x, y, size, color);
    }

    public void draw(Matrix4f matrix, String text, float x, float y, float size, int color) {
        render(matrix, text, x, y, size, 0.05F, color, 0.5F, 0.0F, 0, 0.0F);
    }

    public void draw(Matrix4f matrix, String text, float x, float y, float size, int color, float thickness) {
        render(matrix, text, x, y, size, thickness, color, 0.5F, 0.0F, 0, 0.0F);
    }

    public void drawCentered(DrawContext context, String text, float x, float y, float size, int color) {
        drawCentered(context.getMatrices().peek().getPositionMatrix(), text, x, y, size, color);
    }

    public void drawCentered(Matrix4f matrix, String text, float x, float y, float size, int color) {
        float width = this.font.getWidth(text, size);
        draw(matrix, text, x - width / 2.0F, y, size, color);
    }

    public void drawCentered(Matrix4f matrix, String text, float x, float y, float size, int color, float thickness) {
        float width = this.font.getWidth(text, size);
        draw(matrix, text, x - width / 2.0F, y, size, color, thickness);
    }

    public void drawGradient(DrawContext context, String text, float x, float y, float size, int color1, int color2) {
        drawGradient(context.getMatrices().peek().getPositionMatrix(), text, x, y, size, color1, color2);
    }

    public void drawGradient(Matrix4f matrix, String text, float x, float y, float size, int color1, int color2) {
        renderGradient(matrix, text, x, y, size, 0.05F, color1, color2, 0.5F, 0.0F, 0, 0.0F);
    }

    public void drawWave(Matrix4f matrix, String text, float x, float y, float size, int color1, int color2) {
        renderWave(matrix, text, x, y, size, 0.05F, color1, color2, 0.5F, 0.0F, 0, 0.0F);
    }

    private void render(Matrix4f matrix, String text, float x, float y, float size, float thickness, int color, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        if (text == null || text.isEmpty()) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, this.font.getTextureId());

        boolean outlineEnabled = (outlineThickness > 0.0F);
        ShaderProgram shader = RenderSystem.setShader(MSDF_FONT_SHADER_KEY);
        shader.getUniform("Range").set(this.font.getAtlas().range());
        shader.getUniform("Thickness").set(thickness);
        shader.getUniform("Smoothness").set(smoothness);
        shader.getUniform("Outline").set(outlineEnabled ? 1 : 0);

        if (outlineEnabled) {
            shader.getUniform("OutlineThickness").set(outlineThickness);
            float[] outlineComponents = ColorProvider.normalize(outlineColor);
            shader.getUniform("OutlineColor").set(outlineComponents[0], outlineComponents[1], outlineComponents[2], outlineComponents[3]);
        }

        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        this.font.applyGlyphs(matrix, (VertexConsumer)builder, text, size, (thickness + outlineThickness * 0.5F) * 0.5F * size, spacing, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, color);

        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.setShaderTexture(0, 0);

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void renderGradient(Matrix4f matrix, String text, float x, float y, float size, float thickness, int color1, int color2, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, this.font.getTextureId());

        boolean outlineEnabled = (outlineThickness > 0.0F);
        ShaderProgram shader = RenderSystem.setShader(MSDF_FONT_SHADER_KEY);
        shader.getUniform("Range").set(this.font.getAtlas().range());
        shader.getUniform("Thickness").set(thickness);
        shader.getUniform("Smoothness").set(smoothness);
        shader.getUniform("Outline").set(outlineEnabled ? 1 : 0);

        if (outlineEnabled) {
            shader.getUniform("OutlineThickness").set(outlineThickness);
            float[] outlineComponents = ColorProvider.normalize(outlineColor);
            shader.getUniform("OutlineColor").set(outlineComponents[0], outlineComponents[1], outlineComponents[2], outlineComponents[3]);
        }

        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        this.font.applyGlyphs(matrix, (VertexConsumer)builder, text, size, (thickness + outlineThickness * 0.5F) * 0.5F * size, spacing, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, color1, color2);

        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.setShaderTexture(0, 0);

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void renderWave(Matrix4f matrix, String text, float x, float y, float size, float thickness, int color1, int color2, float smoothness, float spacing, int outlineColor, float outlineThickness) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, this.font.getTextureId());

        boolean outlineEnabled = (outlineThickness > 0.0F);
        ShaderProgram shader = RenderSystem.setShader(MSDF_FONT_SHADER_KEY);
        shader.getUniform("Range").set(this.font.getAtlas().range());
        shader.getUniform("Thickness").set(thickness);
        shader.getUniform("Smoothness").set(smoothness);
        shader.getUniform("Outline").set(outlineEnabled ? 1 : 0);

        if (outlineEnabled) {
            shader.getUniform("OutlineThickness").set(outlineThickness);
            float[] outlineComponents = ColorProvider.normalize(outlineColor);
            shader.getUniform("OutlineColor").set(outlineComponents[0], outlineComponents[1], outlineComponents[2], outlineComponents[3]);
        }

        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        this.font.applyWave(matrix, (VertexConsumer)builder, text, size, (thickness + outlineThickness * 0.5F) * 0.5F * size, spacing, x, y + this.font.getMetrics().baselineHeight() * size, 0.0F, color1, color2);

        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.setShaderTexture(0, 0);

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public float getWidth(String text, float size) {
        return this.font.getWidth(text, size);
    }

    public float getHeight(float size) {
        return this.font.getMetrics().lineHeight() * size;
    }
}