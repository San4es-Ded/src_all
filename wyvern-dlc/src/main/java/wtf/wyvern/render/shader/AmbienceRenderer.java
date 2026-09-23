package wtf.wyvern.render.shader;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.impl.render.Ambience;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.CustomRenderTarget;
import wtf.wyvern.render.display.shader.GlProgram;
import wtf.wyvern.utility.interfaces.IMinecraft;

/** Applies Ambience color grading and blur fog to the completed world before the HUD is drawn. */
public final class AmbienceRenderer implements IMinecraft {
    private static final AmbienceRenderer INSTANCE = new AmbienceRenderer();
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();

    private CustomRenderTarget saturationCopy;
    private CustomRenderTarget blurSharpCopy;
    private CustomRenderTarget blur1;
    private CustomRenderTarget blur2;
    private GlProgram saturationProgram;
    private GlProgram blitProgram;
    private GlProgram fogBlurProgram;
    private GlProgram kawaseDownProgram;
    private GlProgram kawaseUpProgram;

    private AmbienceRenderer() {
    }

    public static AmbienceRenderer getInstance() {
        return INSTANCE;
    }

    public static void initializeShaders() {
        INSTANCE.saturationProgram = new GlProgram(
                Wyvern.id("ambience/ambience_saturation"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.blitProgram = new GlProgram(
                Wyvern.id("hands/hands_blit"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.fogBlurProgram = new GlProgram(
                Wyvern.id("ambience/ambience_fog_blur"), VertexFormats.POSITION_TEXTURE);
        INSTANCE.kawaseDownProgram = new GlProgram(
                Wyvern.id("kawase_down/data"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.kawaseUpProgram = new GlProgram(
                Wyvern.id("kawase_up/data"), VertexFormats.POSITION_TEXTURE_COLOR);
    }

    public void render() {
        if (mc.world == null) {
            return;
        }

        float saturation = Ambience.INSTANCE.getSaturation();
        if (!Ambience.INSTANCE.isEnabled()
                || Math.abs(saturation - 1.0F) < 0.001F
                || saturationProgram == null
                || !saturationProgram.isLoaded()) {
            return;
        }

        int width = mc.getWindow().getFramebufferWidth();
        int height = mc.getWindow().getFramebufferHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        ensureSaturationTarget(width, height);
        if (saturationCopy == null) {
            return;
        }

        Matrix4f savedProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
        RenderSystem.getModelViewStack().pushMatrix();
        beginScreenPass(savedProjection);

        try {
            blitTexture(mc.getFramebuffer().getColorAttachment(), saturationCopy);
            mc.getFramebuffer().beginWrite(false);
            saturationProgram.use();
            RenderSystem.setShaderTexture(0, saturationCopy.getColorAttachment());
            setUniform(saturationProgram, "saturation", saturation);
            drawTexturedColorQuad();
        } finally {
            endScreenPass(savedProjection);
        }
    }

    public void renderBlurFog() {
        if (mc.world == null
                || !Ambience.INSTANCE.isBlurFog()
                || fogBlurProgram == null
                || !fogBlurProgram.isLoaded()
                || blitProgram == null
                || !blitProgram.isLoaded()
                || kawaseDownProgram == null
                || !kawaseDownProgram.isLoaded()
                || kawaseUpProgram == null
                || !kawaseUpProgram.isLoaded()) {
            return;
        }

        int width = mc.getWindow().getFramebufferWidth();
        int height = mc.getWindow().getFramebufferHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        ensureBlurTargets(width, height);
        if (blurSharpCopy == null || blur1 == null || blur2 == null) {
            return;
        }

        Matrix4f savedProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
        RenderSystem.getModelViewStack().pushMatrix();
        beginScreenPass(savedProjection);

        try {
            int sharpTexture = mc.getFramebuffer().getColorAttachment();
            blitTexture(sharpTexture, blurSharpCopy);
            int blurredTexture = buildKawaseBlur(blurSharpCopy.getColorAttachment(), width, height);

            mc.getFramebuffer().beginWrite(false);
            fogBlurProgram.use();
            RenderSystem.setShaderTexture(0, blurSharpCopy.getColorAttachment());
            RenderSystem.setShaderTexture(1, blurredTexture);

            setUniform(fogBlurProgram, "FogDensity", Ambience.INSTANCE.getBlurDensity());
            setUniform(fogBlurProgram, "HorizonCenter", resolveHorizonCenter());
            setUniform(fogBlurProgram, "HorizonSoftness", 0.30F);

            ColorRGBA fogColor = Ambience.INSTANCE.getThemeFogColor();
            ColorRGBA fogSecondColor = Ambience.INSTANCE.getThemeFogSecondColor();
            setUniform(fogBlurProgram, "FogColor",
                    fogColor.getRed() / 255.0F,
                    fogColor.getGreen() / 255.0F,
                    fogColor.getBlue() / 255.0F);
            setUniform(fogBlurProgram, "FogColorSecondary",
                    fogSecondColor.getRed() / 255.0F,
                    fogSecondColor.getGreen() / 255.0F,
                    fogSecondColor.getBlue() / 255.0F);
            setUniform(fogBlurProgram, "FogTint", Ambience.INSTANCE.getBlurTint());
            drawTexturedQuad();
        } catch (RuntimeException ignored) {
            mc.getFramebuffer().beginWrite(false);
        } finally {
            endScreenPass(savedProjection);
        }
    }

    private float resolveHorizonCenter() {
        if (mc.player == null) {
            return 0.5F;
        }

        float pitch = mc.player.getPitch();
        float fov = mc.options.getFov().getValue().floatValue();
        if (fov < 30.0F) {
            fov = 70.0F;
        }

        // Pitch follows the real horizon on screen: look down -> line moves up, look up -> down.
        return MathHelper.clamp(0.5F + (pitch / fov) * 0.62F, 0.10F, 0.90F);
    }

    private int buildKawaseBlur(int sourceTexture, int width, int height) {
        float radius = Ambience.INSTANCE.getBlurStrength();
        float offset = Math.max(0.85F, radius / 14.0F);
        float resolutionX = 1.0F / width;
        float resolutionY = 1.0F / height;

        kawasePass(kawaseDownProgram, blur1, sourceTexture, offset, resolutionX, resolutionY);
        kawasePass(kawaseDownProgram, blur2, blur1.getColorAttachment(), offset * 1.75F, resolutionX, resolutionY);
        kawasePass(kawaseDownProgram, blur1, blur2.getColorAttachment(), offset * 3.25F, resolutionX, resolutionY);
        kawasePass(kawaseUpProgram, blur2, blur1.getColorAttachment(), offset * 3.25F, resolutionX, resolutionY);
        kawasePass(kawaseUpProgram, blur1, blur2.getColorAttachment(), offset * 1.75F, resolutionX, resolutionY);
        kawasePass(kawaseUpProgram, blur2, blur1.getColorAttachment(), offset, resolutionX, resolutionY);
        return blur2.getColorAttachment();
    }

    private void kawasePass(GlProgram program, CustomRenderTarget target, int sourceTexture,
                            float offset, float resolutionX, float resolutionY) {
        target.setup(false);
        program.use();
        RenderSystem.setShaderTexture(0, sourceTexture);
        setUniform(program, "Resolution", resolutionX, resolutionY);
        setUniform(program, "Offset", offset);
        setUniform(program, "Saturation", 1.0F);
        setUniform(program, "TintIntensity", 0.0F);
        setUniform(program, "TintColor", 1.0F, 1.0F, 1.0F);
        drawTexturedColorQuad();
        target.stop();
    }

    private void blitTexture(int sourceTexture, CustomRenderTarget target) {
        target.setup(false);
        blitProgram.use();
        RenderSystem.setShaderTexture(0, sourceTexture);
        setUniform(blitProgram, "colorMul", 1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedColorQuad();
        target.stop();
    }

    private void beginScreenPass(Matrix4f savedProjection) {
        RenderSystem.getModelViewStack().identity();
        RenderSystem.setProjectionMatrix(new Matrix4f(), ProjectionType.ORTHOGRAPHIC);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.disableBlend();
    }

    private void endScreenPass(Matrix4f savedProjection) {
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.setShaderTexture(1, 0);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setProjectionMatrix(savedProjection, ProjectionType.PERSPECTIVE);
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        mc.getFramebuffer().beginWrite(false);
    }

    private void ensureSaturationTarget(int width, int height) {
        if (saturationCopy == null) {
            saturationCopy = new CustomRenderTarget(width, height, false).setLinear();
        } else if (saturationCopy.textureWidth != width || saturationCopy.textureHeight != height) {
            saturationCopy.resize(width, height);
        }
    }

    private void ensureBlurTargets(int width, int height) {
        if (blurSharpCopy == null) {
            blurSharpCopy = new CustomRenderTarget(width, height, false).setLinear();
            blur1 = new CustomRenderTarget(width, height, false).setLinear();
            blur2 = new CustomRenderTarget(width, height, false).setLinear();
        } else if (blurSharpCopy.textureWidth != width || blurSharpCopy.textureHeight != height) {
            blurSharpCopy.resize(width, height);
            blur1.resize(width, height);
            blur2.resize(width, height);
        }
    }

    private void setUniform(GlProgram program, String name, float value) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(value);
        }
    }

    private void setUniform(GlProgram program, String name, float x, float y) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(x, y);
        }
    }

    private void setUniform(GlProgram program, String name, float x, float y, float z) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z);
        }
    }

    private void setUniform(GlProgram program, String name, float x, float y, float z, float w) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z, w);
        }
    }

    private void drawTexturedQuad() {
        BufferBuilder builder = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        builder.vertex(IDENTITY_MATRIX, -1.0F, -1.0F, 0.0F).texture(0.0F, 0.0F);
        builder.vertex(IDENTITY_MATRIX, -1.0F, 1.0F, 0.0F).texture(0.0F, 1.0F);
        builder.vertex(IDENTITY_MATRIX, 1.0F, 1.0F, 0.0F).texture(1.0F, 1.0F);
        builder.vertex(IDENTITY_MATRIX, 1.0F, -1.0F, 0.0F).texture(1.0F, 0.0F);
        BufferRenderer.drawWithGlobalProgram(builder.end());
    }

    private void drawTexturedColorQuad() {
        BufferBuilder builder = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        builder.vertex(IDENTITY_MATRIX, -1.0F, -1.0F, 0.0F).texture(0.0F, 0.0F).color(-1);
        builder.vertex(IDENTITY_MATRIX, -1.0F, 1.0F, 0.0F).texture(0.0F, 1.0F).color(-1);
        builder.vertex(IDENTITY_MATRIX, 1.0F, 1.0F, 0.0F).texture(1.0F, 1.0F).color(-1);
        builder.vertex(IDENTITY_MATRIX, 1.0F, -1.0F, 0.0F).texture(1.0F, 0.0F).color(-1);
        BufferRenderer.drawWithGlobalProgram(builder.end());
    }
}
