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
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.impl.render.MotionBlur;
import wtf.wyvern.render.display.shader.CustomRenderTarget;
import wtf.wyvern.render.display.shader.GlProgram;
import wtf.wyvern.utility.interfaces.IMinecraft;

public final class MotionBlurRenderer implements IMinecraft {
    private static final MotionBlurRenderer INSTANCE = new MotionBlurRenderer();
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();

    private CustomRenderTarget currentCopy;
    private CustomRenderTarget previousFrame;
    private GlProgram motionBlurProgram;
    private GlProgram blitProgram;

    private float lastYaw;
    private float lastPitch;
    private float lastSwingProgress;
    private float smoothedWorldVelocityX;
    private float smoothedWorldVelocityY;
    private float smoothedHandVelocityX;
    private float smoothedHandVelocityY;
    private boolean hasPreviousFrame;
    private boolean hasLastCamera;
    private boolean hasLastSwing;

    private MotionBlurRenderer() {
    }

    public static MotionBlurRenderer getInstance() {
        return INSTANCE;
    }

    public static void initializeShaders() {
        INSTANCE.motionBlurProgram = new GlProgram(
                Wyvern.id("motion_blur/motion_blur"), VertexFormats.POSITION_TEXTURE);
        INSTANCE.blitProgram = new GlProgram(
                Wyvern.id("hands/hands_blit"), VertexFormats.POSITION_TEXTURE_COLOR);
    }

    public void render() {
        MotionBlur module = MotionBlur.INSTANCE;
        if (mc.world == null
                || mc.player == null
                || !module.isEnabled()
                || motionBlurProgram == null
                || !motionBlurProgram.isLoaded()
                || blitProgram == null
                || !blitProgram.isLoaded()) {
            return;
        }

        int width = mc.getWindow().getFramebufferWidth();
        int height = mc.getWindow().getFramebufferHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        ensureTargets(width, height);
        if (currentCopy == null || previousFrame == null) {
            return;
        }

        float strength = module.getStrength();
        if (strength <= 0.001F) {
            return;
        }

        float worldVelocityX = 0.0F;
        float worldVelocityY = 0.0F;
        float handVelocityX = 0.0F;
        float handVelocityY = 0.0F;

        if (module.useCamera()) {
            float yaw = mc.player.getYaw();
            float pitch = mc.player.getPitch();
            if (hasLastCamera) {
                float deltaYaw = MathHelper.wrapDegrees(yaw - lastYaw);
                float deltaPitch = pitch - lastPitch;
                float cameraX = deltaYaw * 0.00285F * strength;
                float cameraY = -deltaPitch * 0.0026F * strength;
                worldVelocityX += cameraX;
                worldVelocityY += cameraY;
                handVelocityX += cameraX * 0.32F;
                handVelocityY += cameraY * 0.32F;
            }
            lastYaw = yaw;
            lastPitch = pitch;
            hasLastCamera = true;
        }

        if (module.useMovement()) {
            Vec3d velocity = mc.player.getVelocity();
            float moveX = (float) velocity.x * 0.00135F * strength;
            float moveY = (float) velocity.y * 0.00085F * strength;
            float moveZ = (float) velocity.z * 0.00055F * strength;
            worldVelocityX += moveX + moveZ;
            worldVelocityY += moveY;
            handVelocityX += moveX + moveZ;
            handVelocityY += moveY;
        }

        float swingProgress = mc.player.getHandSwingProgress(mc.getRenderTickCounter().getTickDelta(false));
        if (hasLastSwing) {
            float deltaSwing = swingProgress - lastSwingProgress;
            handVelocityX += deltaSwing * 0.018F * strength;
            handVelocityY -= deltaSwing * 0.026F * strength;
        }
        lastSwingProgress = swingProgress;
        hasLastSwing = true;

        worldVelocityX = MathHelper.clamp(worldVelocityX, -0.075F, 0.075F);
        worldVelocityY = MathHelper.clamp(worldVelocityY, -0.075F, 0.075F);
        handVelocityX = MathHelper.clamp(handVelocityX, -0.075F, 0.075F);
        handVelocityY = MathHelper.clamp(handVelocityY, -0.075F, 0.075F);

        smoothedWorldVelocityX = MathHelper.lerp(0.42F, smoothedWorldVelocityX, worldVelocityX);
        smoothedWorldVelocityY = MathHelper.lerp(0.42F, smoothedWorldVelocityY, worldVelocityY);
        smoothedHandVelocityX = MathHelper.lerp(0.48F, smoothedHandVelocityX, handVelocityX);
        smoothedHandVelocityY = MathHelper.lerp(0.48F, smoothedHandVelocityY, handVelocityY);

        Matrix4f savedProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
        RenderSystem.getModelViewStack().pushMatrix();
        beginScreenPass();

        try {
            int sourceTexture = mc.getFramebuffer().getColorAttachment();
            blitTexture(sourceTexture, currentCopy);

            if (!hasPreviousFrame) {
                blitTexture(sourceTexture, previousFrame);
                hasPreviousFrame = true;
                return;
            }

            mc.getFramebuffer().beginWrite(false);
            motionBlurProgram.use();
            RenderSystem.setShaderTexture(0, currentCopy.getColorAttachment());
            RenderSystem.setShaderTexture(1, previousFrame.getColorAttachment());
            setUniform(motionBlurProgram, "Velocity", smoothedWorldVelocityX, smoothedWorldVelocityY);
            setUniform(motionBlurProgram, "HandVelocity", smoothedHandVelocityX, smoothedHandVelocityY);
            setUniform(motionBlurProgram, "HandRegionTop", 0.56F);
            setUniform(motionBlurProgram, "Strength", strength);
            setUniform(motionBlurProgram, "Samples", module.getSamples());
            drawTexturedQuad();

            blitTexture(mc.getFramebuffer().getColorAttachment(), previousFrame);
            hasPreviousFrame = true;
        } catch (RuntimeException ignored) {
            mc.getFramebuffer().beginWrite(false);
        } finally {
            endScreenPass(savedProjection);
        }
    }

    public void reset() {
        hasPreviousFrame = false;
        hasLastCamera = false;
        hasLastSwing = false;
        smoothedWorldVelocityX = 0.0F;
        smoothedWorldVelocityY = 0.0F;
        smoothedHandVelocityX = 0.0F;
        smoothedHandVelocityY = 0.0F;
    }

    private void ensureTargets(int width, int height) {
        if (currentCopy == null) {
            currentCopy = new CustomRenderTarget(width, height, false).setLinear();
            previousFrame = new CustomRenderTarget(width, height, false).setLinear();
            return;
        }

        if (currentCopy.textureWidth != width || currentCopy.textureHeight != height) {
            currentCopy.resize(width, height);
            previousFrame.resize(width, height);
            hasPreviousFrame = false;
        }
    }

    private void blitTexture(int sourceTexture, CustomRenderTarget target) {
        target.setup(false);
        blitProgram.use();
        RenderSystem.setShaderTexture(0, sourceTexture);
        setUniform(blitProgram, "colorMul", 1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedColorQuad();
        target.stop();
    }

    private void beginScreenPass() {
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
