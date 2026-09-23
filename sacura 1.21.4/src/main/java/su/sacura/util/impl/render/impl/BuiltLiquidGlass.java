package su.sacura.util.impl.render.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import su.sacura.util.impl.render.builders.states.QuadColorState;
import su.sacura.util.impl.render.builders.states.QuadRadiusState;
import su.sacura.util.impl.render.builders.states.SizeState;
import su.sacura.util.impl.render.providers.ResourceProvider;
import su.sacura.util.type.IRenderer;
import su.sacura.util.type.MinecraftWrapper;

public record BuiltLiquidGlass(SizeState size, QuadRadiusState radius, QuadColorState color, float smoothness, float fresnelPower, float fresnelAlpha, float baseAlpha, boolean fresnelInvert, float fresnelMix, float distortStrength) implements IRenderer,
        MinecraftWrapper
{
    private static final ShaderProgramKey LIQUID_GLASS_SHADER_KEY = new ShaderProgramKey(ResourceProvider.getShaderIdentifier("liquid"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);

    @Override
    public void render(Matrix4f matrix, float x, float y, float z) {
        float width = this.size.width();
        float height = this.size.height();
        Framebuffer screenFBO = mc.getFramebuffer();
        int screenTexture = screenFBO.getColorAttachment();
        ShaderProgram shader = RenderSystem.setShader((ShaderProgramKey)LIQUID_GLASS_SHADER_KEY);
        // Исправлено: method_1250 -> set(Matrix4f)
        shader.getUniform("ModelViewMat").set(matrix);
        shader.getUniform("ProjMat").set(RenderSystem.getProjectionMatrix());
        // Исправлено: method_1255 -> set(float, float)
        shader.getUniform("Size").set(width, height);
        // Исправлено: method_35657 -> set(float, float, float, float)
        shader.getUniform("Radius").set(this.radius.radius1(), this.radius.radius2(), this.radius.radius3(), this.radius.radius4());
        // Исправлено: method_1251 -> set(float)
        shader.getUniform("Smoothness").set(this.smoothness);
        shader.getUniform("CornerSmoothness").set(1.0f);
        shader.getUniform("GlobalAlpha").set((float)(this.color.color1() >> 24 & 0xFF) / 255.0f);
        shader.getUniform("FresnelPower").set(this.fresnelPower);
        // Исправлено: method_1249 -> set(float, float, float)
        shader.getUniform("FresnelColor").set(1.0f, 1.0f, 1.0f);
        shader.getUniform("FresnelAlpha").set(this.fresnelAlpha);
        shader.getUniform("BaseAlpha").set(this.baseAlpha);
        // Исправлено: method_35649 -> set(int)
        shader.getUniform("FresnelInvert").set(this.fresnelInvert ? 1 : 0);
        shader.getUniform("FresnelMix").set(this.fresnelMix);
        shader.getUniform("DistortStrength").set(this.distortStrength);
        RenderSystem.setShaderTexture(0, screenTexture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        float scaleX = (float)screenFBO.textureWidth / (float)mc.getWindow().getScaledWidth();
        float scaleY = (float)screenFBO.textureHeight / (float)mc.getWindow().getScaledHeight();
        float fx = x * scaleX;
        float fy = y * scaleY;
        float fwidth = width * scaleX;
        float fheight = height * scaleY;
        fy = (float)screenFBO.textureHeight - fy - fheight;
        float u0 = fx / (float)screenFBO.textureWidth;
        float v0 = fy / (float)screenFBO.textureHeight;
        float u1 = (fx + fwidth) / (float)screenFBO.textureWidth;
        float v1 = (fy + fheight) / (float)screenFBO.textureHeight;
        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        // Исправлено: method_22918 -> vertex(Matrix4f, float, float, float)
        builder.vertex(matrix, x, y, z).texture(u0, v1).color(this.color.color1());
        builder.vertex(matrix, x, y + height, z).texture(u0, v0).color(this.color.color2());
        builder.vertex(matrix, x + width, y + height, z).texture(u1, v0).color(this.color.color3());
        builder.vertex(matrix, x + width, y, z).texture(u1, v1).color(this.color.color4());
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builder.end());
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}