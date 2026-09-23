package haron.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import haron.render.ScissorStack;
import haron.render.ScaledGuiProjection;
import haron.render.BlendedRenderScope;
import haron.render.FramebufferCapture;
import haron.render.ShapeRenderer;
import haron.render.shader.DefaultShaders;
import haron.render.shader.ShaderProgram;
import java.awt.Color;
import java.util.Optional;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class ShaderShapeRenderer
implements ShapeRenderer {
    private final ScissorStack a = new ScissorStack(this);

    @Override
    public void b(float f, float f2, float f3, float f4, MatrixStack matrixStack) {
        Window window = MinecraftClient.getInstance().getWindow();
        float f5 = ScaledGuiProjection.d();
        GL11.glEnable((int)3089);
        GL11.glScissor((int)((int)(f * f5)), (int)((int)((float)window.getFramebufferHeight() - (f2 + f4) * f5)), (int)((int)(f3 * f5)), (int)((int)(f4 * f5)));
    }

    @Override
    public void b(float f, float f2, float f3, float f4, float f5, float f6, Color color, MatrixStack matrixStack) {
        Window window = MinecraftClient.getInstance().getWindow();
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find("round_rect_outline");
        if (optional.isPresent()) {
            ShaderProgram s5pbng2 = optional.get();
            s5pbng2.d();
            s5pbng2.a("location", f * ScaledGuiProjection.d(), (float)window.getHeight() - f4 * ScaledGuiProjection.d() - f2 * ScaledGuiProjection.d());
            s5pbng2.a("rectSize", f3 * ScaledGuiProjection.d(), f4 * ScaledGuiProjection.d());
            s5pbng2.a("radius", f5);
            s5pbng2.a("thickness", f6);
            s5pbng2.a("color", new Color(0, 0, 0, 0));
            s5pbng2.a("outlineColor", color);
            BlendedRenderScope.runBlended(() -> this.a(f - f6, f2 - (1.0f + f6), f3 + f6 * 2.0f, f4 + 2.0f + f6 * 2.0f));
            s5pbng2.e();
        }
    }

    @Override
    public ScissorStack b() {
        return this.a;
    }

    @Override
    public void c(float f, float f2, float f3, float f4, MatrixStack matrixStack) {
        FramebufferCapture.begin(f, f2, f3, f4);
    }

    @Override
    public void a(float f, MatrixStack matrixStack) {
        FramebufferCapture.end(this, f, matrixStack);
    }

    @Override
    public void a(float f, float f2, float f3, float f4, float f5, float f6, MatrixStack matrixStack) {
        Window window = MinecraftClient.getInstance().getWindow();
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find("hue_bar");
        if (optional.isPresent()) {
            ShaderProgram s5pbng2 = optional.get();
            s5pbng2.d();
            s5pbng2.a("location", f * ScaledGuiProjection.d(), (float)window.getHeight() - f4 * ScaledGuiProjection.d() - f2 * ScaledGuiProjection.d());
            s5pbng2.a("rectSize", f3 * ScaledGuiProjection.d(), f4 * ScaledGuiProjection.d());
            s5pbng2.a("radius", f5 * ScaledGuiProjection.d());
            BlendedRenderScope.runBlended(() -> {
                int n = 752;
                this.a(f, f2, f3, f4);
            });
            s5pbng2.e();
        }
    }

    @Override
    public void a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, Color color, Color color2, Color color3, Color color4, MatrixStack matrixStack) {
        Window window = MinecraftClient.getInstance().getWindow();
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find("gradient");
        if (optional.isPresent()) {
            ShaderProgram s5pbng2 = optional.get();
            s5pbng2.d();
            float f9 = ScaledGuiProjection.d();
            s5pbng2.a("location", f * f9, (float)window.getHeight() - f4 * f9 - f2 * f9);
            s5pbng2.a("rectSize", f3 * f9, f4 * f9);
            s5pbng2.a("radii", f5 * f9, f6 * f9, f7 * f9, f8 * f9);
            s5pbng2.a("topLeftColor", color);
            s5pbng2.a("topRightColor", color2);
            s5pbng2.a("bottomLeftColor", color3);
            s5pbng2.a("bottomRightColor", color4);
            BlendedRenderScope.runBlended(() -> {
                this.a(f, f2, f3, f4);
            });
            s5pbng2.e();
        }
    }

    @Override
    public void a(float f, float f2, float f3, float f4, MatrixStack matrixStack) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(f, f2, 0.0f).texture(0.0f, 0.0f);
        bufferBuilder.vertex(f, f2 + f4, 0.0f).texture(0.0f, 1.0f);
        bufferBuilder.vertex(f + f3, f2 + f4, 0.0f).texture(1.0f, 1.0f);
        bufferBuilder.vertex(f + f3, f2, 0.0f).texture(1.0f, 0.0f);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    @Override
    public Window a() {
        return MinecraftClient.getInstance().getWindow();
    }

    private void a(double d, double d2, double d3, double d4) {
        ShaderProgram.a((float)d, (float)d2, (float)d3, (float)d4);
    }

    @Override
    public void a(float f, float f2, float f3, float f4, float f5, Color color, MatrixStack matrixStack) {
        Window window = MinecraftClient.getInstance().getWindow();
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find("round_rect");
        if (optional.isPresent()) {
            ShaderProgram s5pbng2 = optional.get();
            s5pbng2.d();
            s5pbng2.a("location", f * ScaledGuiProjection.d(), (float)window.getHeight() - f4 * ScaledGuiProjection.d() - f2 * ScaledGuiProjection.d());
            s5pbng2.a("rectSize", f3 * ScaledGuiProjection.d(), f4 * ScaledGuiProjection.d());
            s5pbng2.a("radius", f5);
            s5pbng2.a("color", color);
            BlendedRenderScope.runBlended(() -> {
                this.a(f, f2, f3, f4);
            });
            s5pbng2.e();
        }
    }

    @Override
    public void a(int n, float f, float f2, float f3, float f4, Color color, MatrixStack matrixStack) {
        GlStateManager._bindTexture((int)n);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        RenderSystem.setShaderTexture((int)0, (int)n);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        BlendedRenderScope.runBlended(() -> {
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(matrix4f, f, f2, 0.0f).texture(0.0f, 1.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            bufferBuilder.vertex(matrix4f, f, f2 + f4, 0.0f).texture(0.0f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            bufferBuilder.vertex(matrix4f, f + f3, f2 + f4, 0.0f).texture(1.0f, 0.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            bufferBuilder.vertex(matrix4f, f + f3, f2, 0.0f).texture(1.0f, 1.0f).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        });
        GlStateManager._bindTexture((int)0);
    }

    @Override
    public void a(float f, float f2, float f3, float f4, float f5, float f6, Color color, MatrixStack matrixStack) {
        Window window = MinecraftClient.getInstance().getWindow();
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find("rect_glow");
        if (optional.isPresent()) {
            ShaderProgram s5pbng2 = optional.get();
            s5pbng2.d();
            float f7 = ScaledGuiProjection.d();
            float f8 = f - f5;
            float f9 = f2 - f5;
            float f10 = f3 + f5 * 2.0f;
            float f11 = f4 + f5 * 2.0f;
            s5pbng2.a("location", f8 * f7, (float)window.getHeight() - f11 * f7 - f9 * f7);
            s5pbng2.a("rectSize", f10 * f7, f11 * f7);
            s5pbng2.a("glowRadius", f5 * f7);
            s5pbng2.a("cornerRadius", f6 * f7);
            s5pbng2.a("glowColor", color);
            BlendedRenderScope.runBlended(() -> {
                this.a(f8, f9, f10, f11);
            });
            s5pbng2.e();
        }
    }

    @Override
    public void a(float f, float f2, float f3, Color color, MatrixStack matrixStack) {
        Window window = MinecraftClient.getInstance().getWindow();
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find("drop_shadow");
        if (optional.isPresent()) {
            ShaderProgram s5pbng2 = optional.get();
            s5pbng2.d();
            float f4 = f3 * 2.0f * ScaledGuiProjection.d();
            float f5 = f - f3;
            float f6 = f2 - f3;
            s5pbng2.a("location", f5 * ScaledGuiProjection.d(), (float)window.getHeight() - f4 * ScaledGuiProjection.d() - f6 * ScaledGuiProjection.d());
            s5pbng2.a("rectSize", f4 * ScaledGuiProjection.d(), f4 * ScaledGuiProjection.d());
            s5pbng2.a("radius", f4);
            s5pbng2.a("shadowColor", color);
            BlendedRenderScope.runBlended(() -> {
                this.a(f5, f6, f4, f4);
            });
            s5pbng2.e();
        }
    }

    @Override
    public void a(Identifier identifier, float f, float f2, float f3, float f4, Color color, MatrixStack matrixStack) {
        this.a(MinecraftClient.getInstance().getTextureManager().getTexture(identifier).getGlId(), f, f2, f3, f4, color, matrixStack);
    }

    @Override
    public void a(float f, float f2, float f3, float f4, float f5, MatrixStack matrixStack) {
        this.b(f, f2, f3, f4, matrixStack);
    }

    @Override
    public void a(MatrixStack matrixStack) {
        GL11.glDisable((int)3089);
    }

    @Override
    public void a(Identifier identifier, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, Color color, MatrixStack matrixStack) {
        this.a(identifier, f, f2, f3, f4, f5, f6, f7, f8, f9, color, matrixStack, false);
    }

    @Override
    public void a(Identifier identifier, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, Color color, MatrixStack matrixStack, boolean bl) {
        Window window = MinecraftClient.getInstance().getWindow();
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find("round_texture");
        if (optional.isPresent()) {
            ShaderProgram s5pbng2 = optional.get();
            int n = MinecraftClient.getInstance().getTextureManager().getTexture(identifier).getGlId();
            GlStateManager._activeTexture((int)33984);
            GlStateManager._bindTexture((int)n);
            int n2 = bl ? 9728 : 9729;
            GL11.glTexParameteri((int)3553, (int)10241, (int)n2);
            GL11.glTexParameteri((int)3553, (int)10240, (int)n2);
            s5pbng2.d();
            s5pbng2.a("location", f * ScaledGuiProjection.d(), (float)window.getHeight() - f4 * ScaledGuiProjection.d() - f2 * ScaledGuiProjection.d());
            s5pbng2.a("rectSize", f3 * ScaledGuiProjection.d(), f4 * ScaledGuiProjection.d());
            s5pbng2.a("radius", f5);
            s5pbng2.b("tex", 0);
            s5pbng2.a("u", f6);
            s5pbng2.a("v", f7);
            s5pbng2.a("w", f8);
            s5pbng2.a("h", f9);
            s5pbng2.a("tintColor", color);
            BlendedRenderScope.runBlended(() -> {
                this.a(f, f2, f3, f4);
            });
            s5pbng2.e();
        }
    }
}

