package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.GlProgram;

@ModuleAnnotation(name = "Sonar", category = Category.RENDER,
        description = "Показывает расходящуюся волну сканирования мира")
public final class Sonar extends Module {
    public static final Sonar INSTANCE = new Sonar();

    private static final GlProgram SCAN_PROGRAM =
            new GlProgram(Wyvern.id("sonar/scan_effect"), VertexFormats.POSITION_TEXTURE);

    private final SliderSetting duration = new SliderSetting(
            "Длительность", 5.6F, 0.8F, 10.0F, 0.1F);
    private final SliderSetting alpha = new SliderSetting(
            "Яркость", 1.0F, 0.1F, 1.0F, 0.01F);
    private final SliderSetting widthMultiplier = new SliderSetting(
            "Ширина", 1.0F, 0.35F, 2.2F, 0.05F);
    private final SliderSetting sharpness = new SliderSetting(
            "Резкость", 24.0F, 4.0F, 80.0F, 1.0F);

    private Framebuffer depthCopyBuffer;
    private int lastFramebufferWidth = -1;
    private int lastFramebufferHeight = -1;
    private long currentStart;
    private Vec3d center = Vec3d.ZERO;

    private Sonar() {
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            ping(mc.player.getPos());
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        currentStart = 0L;
        deleteDepthCopyFramebuffer();
        super.onDisable();
    }

    public void pingCurrentPosition() {
        if (isEnabled() && mc.player != null) {
            ping(mc.player.getPos());
        }
    }

    public void renderFromMixin(Matrix4f positionMatrix, Matrix4f projectionMatrix, Vec3d cameraPosition) {
        if (!isEnabled() || mc.player == null || mc.world == null || currentStart <= 0L) return;

        float durationMs = duration.getCurrent() * 1000.0F;
        float elapsed = System.currentTimeMillis() - currentStart;
        if (elapsed >= durationMs) {
            currentStart = 0L;
            return;
        }

        Framebuffer framebuffer = mc.getFramebuffer();
        ensureDepthCopyFramebuffer(framebuffer.textureWidth, framebuffer.textureHeight);
        if (depthCopyBuffer == null) return;
        depthCopyBuffer.copyDepthFrom(framebuffer);

        Matrix4f inverseView = new Matrix4f(positionMatrix).invert();
        Matrix4f inverseProjection = new Matrix4f(projectionMatrix).invert();

        float farPlane = mc.gameRenderer.getFarPlaneDistance();
        float progress = MathHelper.clamp(elapsed / durationMs, 0.0F, 1.0F);
        float radiusOut = lerp(1.0F, farPlane, quintOut(progress));
        float radiusInOut = lerp(1.0F, farPlane, quartInOut(progress));
        float radius = MathHelper.lerp(0.85F, radiusOut, radiusInOut);

        float alphaProgress = 1.0F - progress;
        float alphaWave = (alphaProgress > 0.5F ? 1.0F - alphaProgress : alphaProgress) * 2.0F;
        alphaWave = Math.min(alphaWave * 1.75F, 1.0F);
        float baseAlpha = MathHelper.clamp(alpha.getCurrent() * alphaWave, 0.0F, 1.0F);

        ColorRGBA first = Wyvern.getInstance().getThemeManager().getClientColor(0);
        ColorRGBA second = Wyvern.getInstance().getThemeManager().getClientColor(90);
        ColorRGBA third = Wyvern.getInstance().getThemeManager().getClientColor(180);
        ColorRGBA fourth = Wyvern.getInstance().getThemeManager().getClientColor(270);

        float width = MathHelper.clamp(
                6.0F + radius * (0.18F * widthMultiplier.getCurrent()),
                4.0F, Math.max(10.0F, farPlane * 0.42F));

        renderPass(inverseView, inverseProjection, cameraPosition, framebuffer,
                radius, width, sharpness.getCurrent(),
                applyAlpha(first.getRGB(), baseAlpha),
                applyAlpha(second.getRGB(), baseAlpha),
                applyAlpha(third.getRGB(), baseAlpha),
                applyAlpha(fourth.getRGB(), baseAlpha));
        RenderSystem.defaultBlendFunc();
    }

    private void renderPass(Matrix4f inverseView, Matrix4f inverseProjection, Vec3d cameraPosition,
                            Framebuffer framebuffer, float radius, float width, float sharp,
                            int outerColor, int middleColor, int innerColor, int scanlineColor) {
        if (radius <= 0.001F || width <= 0.001F) return;

        ShaderProgram shader = SCAN_PROGRAM.use();
        if (shader == null) return;

        setMatrix(shader, "invViewMat", inverseView);
        setMatrix(shader, "invProjMat", inverseProjection);
        setVector(shader, "pos", (float) cameraPosition.x, (float) cameraPosition.y, (float) cameraPosition.z);
        setVector(shader, "center", (float) center.x, (float) center.y, (float) center.z);
        setFloat(shader, "radius", radius);
        setFloat(shader, "width", width);
        setFloat(shader, "sharpness", sharp);
        setColor(shader, "outerColor", outerColor);
        setColor(shader, "midColor", middleColor);
        setColor(shader, "innerColor", innerColor);
        setColor(shader, "scanlineColor", scanlineColor);
        GlUniform debugMode = shader.getUniform("DebugMode");
        if (debugMode != null) debugMode.set(0);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        int depthTexture = depthCopyBuffer.getDepthAttachment();
        if (depthTexture == 0) {
            depthTexture = mc.getFramebuffer().getDepthAttachment();
        }
        RenderSystem.bindTexture(depthTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL11.GL_NONE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        framebuffer.beginWrite(false);
        RenderSystem.setShaderTexture(0, depthTexture);
        SCAN_PROGRAM.use();
        drawFullscreenQuad();

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    private void ensureDepthCopyFramebuffer(int width, int height) {
        if (depthCopyBuffer == null
                || lastFramebufferWidth != width
                || lastFramebufferHeight != height) {
            deleteDepthCopyFramebuffer();
            depthCopyBuffer = new SimpleFramebuffer(width, height, true);
            lastFramebufferWidth = width;
            lastFramebufferHeight = height;
        }
    }

    private void deleteDepthCopyFramebuffer() {
        if (depthCopyBuffer != null) {
            depthCopyBuffer.delete();
            depthCopyBuffer = null;
        }
        lastFramebufferWidth = -1;
        lastFramebufferHeight = -1;
    }

    private void ping(Vec3d position) {
        currentStart = System.currentTimeMillis();
        center = position;
    }

    private void setMatrix(ShaderProgram shader, String name, Matrix4f value) {
        GlUniform uniform = shader.getUniform(name);
        if (uniform != null) uniform.set(value);
    }

    private void setFloat(ShaderProgram shader, String name, float value) {
        GlUniform uniform = shader.getUniform(name);
        if (uniform != null) uniform.set(value);
    }

    private void setVector(ShaderProgram shader, String name, float x, float y, float z) {
        GlUniform uniform = shader.getUniform(name);
        if (uniform != null) uniform.set(x, y, z);
    }

    private void setColor(ShaderProgram shader, String name, int color) {
        GlUniform uniform = shader.getUniform(name);
        if (uniform == null) return;
        int alpha = color >>> 24 & 0xFF;
        int red = color >>> 16 & 0xFF;
        int green = color >>> 8 & 0xFF;
        int blue = color & 0xFF;
        if (alpha == 0) alpha = 255;
        uniform.set(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
    }

    private int applyAlpha(int color, float alphaMultiplier) {
        int value = color >>> 24 & 0xFF;
        if (value == 0) value = 255;
        value = (int) (value * MathHelper.clamp(alphaMultiplier, 0.0F, 1.0F));
        return color & 0x00FFFFFF | value << 24;
    }

    private float quintOut(float value) {
        return 1.0F - (float) Math.pow(1.0F - value, 5.0D);
    }

    private float quartInOut(float value) {
        if (value < 0.5F) {
            return 8.0F * value * value * value * value;
        }
        float shifted = -2.0F * value + 2.0F;
        return 1.0F - shifted * shifted * shifted * shifted / 2.0F;
    }

    private float lerp(float start, float end, float delta) {
        return start + (end - start) * delta;
    }

    private void drawFullscreenQuad() {
        BufferBuilder buffer = Tessellator.getInstance()
                .begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(-1.0F, -1.0F, 0.0F).texture(0.0F, 0.0F);
        buffer.vertex(-1.0F, 1.0F, 0.0F).texture(0.0F, 1.0F);
        buffer.vertex(1.0F, 1.0F, 0.0F).texture(1.0F, 1.0F);
        buffer.vertex(1.0F, -1.0F, 0.0F).texture(1.0F, 0.0F);
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}
