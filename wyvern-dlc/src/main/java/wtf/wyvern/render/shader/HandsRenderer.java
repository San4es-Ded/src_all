package wtf.wyvern.render.shader;

import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.impl.render.Hands;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.CustomRenderTarget;
import wtf.wyvern.render.display.shader.GlProgram;
import wtf.wyvern.utility.interfaces.IMinecraft;

public final class HandsRenderer implements IMinecraft {
    private static final HandsRenderer INSTANCE = new HandsRenderer();
    private static final float[] WHITE = {1f, 1f, 1f};
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
    private CustomRenderTarget before;
    private CustomRenderTarget after;
    private CustomRenderTarget mask;
    private CustomRenderTarget blur1;
    private CustomRenderTarget blur2;
    private CustomRenderTarget trailA;
    private CustomRenderTarget trailB;

    private GlProgram blitProgram;
    private GlProgram maskProgram;
    private GlProgram fillProgram;
    private GlProgram plasmaProgram;
    private GlProgram handsWaveProgram;
    private GlProgram outlineProgram;
    private GlProgram haloProgram;
    private GlProgram trailProgram;
    private GlProgram trailCompositeProgram;
    private GlProgram kawaseDownProgram;
    private GlProgram kawaseUpProgram;

    private boolean beforeCaptured;
    private boolean afterCaptured;
    private Matrix4f savedProjection;
    private long lastTrailNanos;
    private float smoothedTrailDelta = 1f / 60f;
    private boolean flameHistoryActive;
    private String lastMode = "";

    private HandsRenderer() {
    }

    public static HandsRenderer getInstance() {
        return INSTANCE;
    }

    public static void initializeShaders() {
        INSTANCE.blitProgram = new GlProgram(Wyvern.id("hands/hands_blit"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.maskProgram = new GlProgram(Wyvern.id("hands/hands_mask_diff"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.fillProgram = new GlProgram(Wyvern.id("hands/hands_fill"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.plasmaProgram = new GlProgram(Wyvern.id("hands/hands_plasma"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.handsWaveProgram = new GlProgram(Wyvern.id("hands/hands_wave"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.outlineProgram = new GlProgram(Wyvern.id("hands/hands_outline"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.haloProgram = new GlProgram(Wyvern.id("hands/hands_halo"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.trailProgram = new GlProgram(Wyvern.id("hands/hands_trail"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.trailCompositeProgram = new GlProgram(Wyvern.id("hands/hands_trail_composite"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.kawaseDownProgram = new GlProgram(Wyvern.id("hands/hands_kawase_down"), VertexFormats.POSITION_TEXTURE_COLOR);
        INSTANCE.kawaseUpProgram = new GlProgram(Wyvern.id("hands/hands_kawase_up"), VertexFormats.POSITION_TEXTURE_COLOR);
    }

    public void captureBeforeHands() {
        if (!isReady() || blitProgram == null || !blitProgram.isLoaded()) return;
        int w = mc.getWindow().getFramebufferWidth();
        int h = mc.getWindow().getFramebufferHeight();
        ensureTargets(w, h);
        begin2D();
        copyTexture(mc.getFramebuffer().getColorAttachment(), before);
        before.copyDepthFrom(mc.getFramebuffer());
        mc.getFramebuffer().beginWrite(false);
        end2D();
        beforeCaptured = true;
        afterCaptured = false;
    }

    public void captureAfterHands() {
        if (!isReady() || !beforeCaptured || blitProgram == null || !blitProgram.isLoaded()) return;
        int w = mc.getWindow().getFramebufferWidth();
        int h = mc.getWindow().getFramebufferHeight();
        ensureTargets(w, h);
        begin2D();
        copyTexture(mc.getFramebuffer().getColorAttachment(), after);
        after.copyDepthFrom(mc.getFramebuffer());
        mc.getFramebuffer().beginWrite(false);
        end2D();
        afterCaptured = true;
    }

    public void renderOverlay() {
        if (!Hands.INSTANCE.isEnabled() || !afterCaptured || !programsReady()) {
            beforeCaptured = false;
            afterCaptured = false;
            return;
        }

        Hands module = Hands.INSTANCE;
        int w = mc.getWindow().getFramebufferWidth();
        int h = mc.getWindow().getFramebufferHeight();
        float time = (System.currentTimeMillis() % 100000L) / 1000f;
        float[] color = themeColor();

        begin2D();
        if (!module.mode.get().equals(lastMode)) {
            clearTrailHistory(w, h);
            lastMode = module.mode.get();
        }

        buildMask();

        if (module.hasFill()) {
            if (module.hasGlass()) {
                int scene = before.getColorAttachment();
                float blurRadius = module.glassBlur.getCurrent();
                if (blurRadius > 0.001f) {
                    scene = buildBlur(w, h, scene, blurRadius, false);
                }
                mc.getFramebuffer().beginWrite(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                handsWaveProgram.use();
                RenderSystem.setShaderTexture(0, scene);
                RenderSystem.setShaderTexture(1, mask.getColorAttachment());
                setUniform(handsWaveProgram, "glassColor", color[0], color[1], color[2], module.fillOpacity.getCurrent());
                setUniform(handsWaveProgram, "waveTime", time);
                setUniform(handsWaveProgram, "waveSpeed", 1.2f);
                setUniform(handsWaveProgram, "waveScale", 1.0f);
                setUniform(handsWaveProgram, "glowStrength", 1.4f);
                drawFullScreenQuad();
            } else if (module.hasPlasma()) {
                mc.getFramebuffer().beginWrite(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                plasmaProgram.use();
                RenderSystem.setShaderTexture(0, mask.getColorAttachment());
                setUniform(plasmaProgram, "fillColor", color[0], color[1], color[2], module.fillOpacity.getCurrent());
                setUniform(plasmaProgram, "plasmaTime", time * module.plasmaSpeed.getCurrent());
                drawFullScreenQuad();
            } else {
                mc.getFramebuffer().beginWrite(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                fillProgram.use();
                RenderSystem.setShaderTexture(0, mask.getColorAttachment());
                setUniform(fillProgram, "fillColor", color[0], color[1], color[2], module.fillOpacity.getCurrent());
                drawFullScreenQuad();
            }
        }

        if (module.hasOutline()) {
            mc.getFramebuffer().beginWrite(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            outlineProgram.use();
            RenderSystem.setShaderTexture(0, mask.getColorAttachment());
            setUniform(outlineProgram, "texelSize", 1f / Math.max(w, 1), 1f / Math.max(h, 1));
            setUniform(outlineProgram, "width", module.outlineThickness.getCurrent());
            setUniform(outlineProgram, "outlineColor", color[0], color[1], color[2], 1f);
            drawFullScreenQuad();
        }

        if (module.hasGlow()) {
            int blurredMask = buildBlur(w, h, mask.getColorAttachment(), module.glowRadius.getCurrent(), true);
            if (module.hasFlame()) {
                float trailTime = time * module.flameSpeed.getCurrent();
                int flame = renderTrail(w, h, blurredMask, trailTime, trailDelta());
                mc.getFramebuffer().beginWrite(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 1, 0, 1);
                trailCompositeProgram.use();
                RenderSystem.setShaderTexture(0, flame);
                RenderSystem.setShaderTexture(1, mask.getColorAttachment());
                setUniform(trailCompositeProgram, "flameColor",
                        color[0], color[1], color[2], module.glowStrength.getCurrent());
                drawFullScreenQuad();
                flameHistoryActive = true;
            } else {
                clearFlameHistoryIfNeeded();
                mc.getFramebuffer().beginWrite(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(770, 1, 0, 1);
                haloProgram.use();
                RenderSystem.setShaderTexture(0, blurredMask);
                RenderSystem.setShaderTexture(1, mask.getColorAttachment());
                setUniform(haloProgram, "haloColor",
                        color[0], color[1], color[2], module.glowStrength.getCurrent());
                setUniform(haloProgram, "tapScale", module.glowRadius.getCurrent() / 12f);
                drawFullScreenQuad();
            }
        } else {
            clearFlameHistoryIfNeeded();
        }

        end2D();
        beforeCaptured = false;
        afterCaptured = false;
    }

    private void buildMask() {
        mask.setup(false);
        RenderSystem.disableBlend();
        maskProgram.use();
        RenderSystem.setShaderTexture(0, after.getColorAttachment());
        RenderSystem.setShaderTexture(1, before.getColorAttachment());
        RenderSystem.setShaderTexture(2, before.getDepthAttachment());
        RenderSystem.setShaderTexture(3, after.getDepthAttachment());
        setUniform(maskProgram, "texelSize",
                1f / Math.max(mask.textureWidth, 1),
                1f / Math.max(mask.textureHeight, 1));
        drawFullScreenQuad();
        mask.stop();
    }

    private int renderTrail(int w, int h, int injectTex, float time, float deltaTime) {
        ensureTrailTargets(w, h);
        Hands module = Hands.INSTANCE;

        trailB.setup(false);
        RenderSystem.disableBlend();
        trailProgram.use();
        RenderSystem.setShaderTexture(0, trailA.getColorAttachment());
        RenderSystem.setShaderTexture(1, injectTex);
        setUniform(trailProgram, "texelSize", 1f / Math.max(w, 1), 1f / Math.max(h, 1));
        setUniform(trailProgram, "trailLength", module.flameTrail.getCurrent());
        setUniform(trailProgram, "trailSpeed", module.flameSpeed.getCurrent());
        setUniform(trailProgram, "time", time);
        setUniform(trailProgram, "deltaTime", deltaTime);
        drawFullScreenQuad();
        trailB.stop();

        CustomRenderTarget tmp = trailA;
        trailA = trailB;
        trailB = tmp;
        return trailA.getColorAttachment();
    }

    private float trailDelta() {
        long now = System.nanoTime();
        float raw = lastTrailNanos == 0L
                ? 1f / 60f
                : (now - lastTrailNanos) / 1.0E9f;
        lastTrailNanos = now;
        raw = Math.max(1f / 2000f, Math.min(1f / 20f, raw));
        smoothedTrailDelta += (raw - smoothedTrailDelta) * 0.18f;
        return smoothedTrailDelta;
    }

    private int buildBlur(int w, int h, int srcTex, float radius, boolean boostAlpha) {
        ensureBlurTargets(w, h);
        float offset = Math.max(0.5f, radius / 18.0f);
        float[] upColor = boostAlpha
                ? new float[]{WHITE[0] * 1.04f, WHITE[1] * 1.04f, WHITE[2] * 1.04f}
                : WHITE;
        kawasePass(kawaseDownProgram, blur1, srcTex, offset, w, h, null);
        kawasePass(kawaseDownProgram, blur2, blur1.getColorAttachment(), offset * 2f, w, h, null);
        kawasePass(kawaseUpProgram, blur1, blur2.getColorAttachment(), offset * 2f, w, h, upColor);
        kawasePass(kawaseUpProgram, blur2, blur1.getColorAttachment(), offset, w, h, WHITE);
        return blur2.getColorAttachment();
    }

    private void kawasePass(GlProgram program, CustomRenderTarget dst, int srcTex, float offset, int w, int h, float[] color) {
        dst.setup(false);
        RenderSystem.disableBlend();
        program.use();
        RenderSystem.setShaderTexture(0, srcTex);
        setUniform(program, "uOffset", offset, offset);
        setUniform(program, "uHalfPixel", 0.5f / w, 0.5f / h);
        setUniform(program, "uSize", (float) w, (float) h);
        if (color != null) {
            setUniform(program, "color", color[0], color[1], color[2]);
        }
        drawFullScreenQuad();
        dst.stop();
    }

    private boolean isReady() {
        Hands module = Hands.INSTANCE;
        return module.isEnabled()
                && mc.isWindowFocused()
                && mc.getWindow().getFramebufferWidth() > 0
                && mc.getWindow().getFramebufferHeight() > 0;
    }

    private boolean programsReady() {
        return maskProgram != null && maskProgram.isLoaded()
                && fillProgram.isLoaded() && plasmaProgram.isLoaded() && handsWaveProgram.isLoaded()
                && outlineProgram.isLoaded() && haloProgram.isLoaded()
                && trailProgram.isLoaded() && trailCompositeProgram.isLoaded()
                && kawaseDownProgram.isLoaded() && kawaseUpProgram.isLoaded()
                && blitProgram.isLoaded();
    }

    private float[] themeColor() {
        ColorRGBA color = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
        return new float[]{color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f};
    }

    private void copyTexture(int srcTex, CustomRenderTarget dst) {
        dst.setup(false);
        RenderSystem.disableBlend();
        blitProgram.use();
        RenderSystem.setShaderTexture(0, srcTex);
        setUniform(blitProgram, "colorMul", 1f, 1f, 1f, 1f);
        drawFullScreenQuad();
        dst.stop();
    }

    private void ensureTargets(int w, int h) {
        if (before == null) {
            before = new CustomRenderTarget(w, h, true);
            after = new CustomRenderTarget(w, h, true).setLinear();
            mask = new CustomRenderTarget(w, h, false);
        } else if (before.textureWidth != w || before.textureHeight != h) {
            before.resize(w, h);
            after.resize(w, h);
            mask.resize(w, h);
        }
    }

    private void ensureBlurTargets(int w, int h) {
        if (blur1 == null) {
            blur1 = new CustomRenderTarget(w, h, false).setLinear();
            blur2 = new CustomRenderTarget(w, h, false).setLinear();
        } else if (blur1.textureWidth != w || blur1.textureHeight != h) {
            blur1.resize(w, h);
            blur2.resize(w, h);
        }
    }

    private void ensureTrailTargets(int w, int h) {
        if (trailA == null) {
            trailA = new CustomRenderTarget(w, h, false).setLinear();
            trailB = new CustomRenderTarget(w, h, false).setLinear();
            clearTrailBuffers();
            return;
        }
        if (trailA.textureWidth != w || trailA.textureHeight != h) {
            trailA.resize(w, h);
            trailB.resize(w, h);
            clearTrailBuffers();
        }
    }

    private void clearTrailHistory(int w, int h) {
        ensureTrailTargets(w, h);
        clearTrailBuffers();
    }

    private void clearTrailBuffers() {
        if (trailA == null || trailB == null) return;
        trailA.setup(true);
        trailA.stop();
        trailB.setup(true);
        trailB.stop();
        lastTrailNanos = 0L;
        smoothedTrailDelta = 1f / 60f;
        flameHistoryActive = false;
    }

    private void clearFlameHistoryIfNeeded() {
        if (flameHistoryActive) {
            int w = mc.getWindow().getFramebufferWidth();
            int h = mc.getWindow().getFramebufferHeight();
            clearTrailHistory(w, h);
        }
    }

    private void begin2D() {
        savedProjection = RenderSystem.getProjectionMatrix();
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().identity();
        RenderSystem.setProjectionMatrix(new Matrix4f(), ProjectionType.ORTHOGRAPHIC);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
    }

    private void end2D() {
        RenderSystem.setProjectionMatrix(savedProjection, ProjectionType.PERSPECTIVE);
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private void drawFullScreenQuad() {
        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        builder.vertex(IDENTITY_MATRIX, -1.0F, -1.0F, 0.0F).texture(0.0F, 0.0F).color(-1);
        builder.vertex(IDENTITY_MATRIX, -1.0F, 1.0F, 0.0F).texture(0.0F, 1.0F).color(-1);
        builder.vertex(IDENTITY_MATRIX, 1.0F, 1.0F, 0.0F).texture(1.0F, 1.0F).color(-1);
        builder.vertex(IDENTITY_MATRIX, 1.0F, -1.0F, 0.0F).texture(1.0F, 0.0F).color(-1);
        BufferRenderer.drawWithGlobalProgram(builder.end());
    }

    private void setUniform(GlProgram program, String name, float value) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) uniform.set(value);
    }

    private void setUniform(GlProgram program, String name, float x, float y) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) uniform.set(x, y);
    }

    private void setUniform(GlProgram program, String name, float x, float y, float z) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) uniform.set(x, y, z);
    }

    private void setUniform(GlProgram program, String name, float x, float y, float z, float w) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) uniform.set(x, y, z, w);
    }

    public void invalidateState() {
        if (before != null) { before.delete(); before = null; }
        if (after != null) { after.delete(); after = null; }
        if (mask != null) { mask.delete(); mask = null; }
        if (blur1 != null) { blur1.delete(); blur1 = null; }
        if (blur2 != null) { blur2.delete(); blur2 = null; }
        if (trailA != null) { trailA.delete(); trailA = null; }
        if (trailB != null) { trailB.delete(); trailB = null; }
        beforeCaptured = false;
        afterCaptured = false;
        lastTrailNanos = 0L;
        smoothedTrailDelta = 1f / 60f;
        flameHistoryActive = false;
        lastMode = "";
    }
}
