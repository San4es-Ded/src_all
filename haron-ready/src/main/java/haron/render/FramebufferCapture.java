package haron.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import haron.render.ScaledGuiProjection;
import haron.render.ShapeRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL14;

public class FramebufferCapture {
    private static final MinecraftClient a = MinecraftClient.getInstance();
    private static Framebuffer b = null;
    private static int c = 0;
    private static int d = 0;
    private static boolean e = false;
    private static float f = 0.0f;
    private static float g = 0.0f;
    private static float h = 0.0f;
    private static float i = 0.0f;

    private static void drawCaptured(float f) {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor)GlStateManager.SrcFactor.ONE, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SrcFactor)GlStateManager.SrcFactor.ONE, (GlStateManager.DstFactor)GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager._bindTexture((int)b.getColorAttachment());
        RenderSystem.setShaderTexture((int)0, (int)b.getColorAttachment());
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f2 = ScaledGuiProjection.d();
        float f3 = a.getWindow().getFramebufferWidth();
        float f4 = a.getWindow().getFramebufferHeight();
        float f5 = FramebufferCapture.f * f2 / f3;
        float f6 = 1.0f - (g + i) * f2 / f4;
        float f7 = (FramebufferCapture.f + h) * f2 / f3;
        float f8 = 1.0f - g * f2 / f4;
        int n = (int)(f * 255.0f);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(FramebufferCapture.f, g, 0.0f).texture(f5, f8).color(n, n, n, n);
        bufferBuilder.vertex(FramebufferCapture.f, g + i, 0.0f).texture(f5, f6).color(n, n, n, n);
        bufferBuilder.vertex(FramebufferCapture.f + h, g + i, 0.0f).texture(f7, f6).color(n, n, n, n);
        bufferBuilder.vertex(FramebufferCapture.f + h, g, 0.0f).texture(f7, f8).color(n, n, n, n);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        GlStateManager._bindTexture((int)0);
        RenderSystem.defaultBlendFunc();
    }

    public static boolean isCapturing() {
        return e;
    }

    public static void applyBlendState() {
        if (e) {
            GL14.glBlendFuncSeparate((int)770, (int)771, (int)1, (int)771);
        } else {
            RenderSystem.defaultBlendFunc();
        }
    }

    public static void begin(float f, float f2, float f3, float f4) {
        if (e) {
            return;
        }
        int n = a.getWindow().getFramebufferWidth();
        int n2 = a.getWindow().getFramebufferHeight();
        if (c != n || d != n2) {
            b.delete();
            b = new SimpleFramebuffer(n, n2, true);
            b.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            c = n;
            d = n2;
        }
        FramebufferCapture.f = f;
        g = f2;
        h = f3;
        i = f4;
        b.clear();
        b.beginWrite(true);
        e = true;
    }

    public static void end(ShapeRenderer s7swsm2, float f, MatrixStack matrixStack) {
        if (e) {
            e = false;
            a.getFramebuffer().beginWrite(true);
            if (f > 0.001f) {
                FramebufferCapture.drawCaptured(f);
            }
        }
    }

    public static void dispose() {
        b.delete();
        b = null;
        e = false;
    }
}

