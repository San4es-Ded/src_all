package wtf.wyvern.render.level;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.impl.render.Chams;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.GlProgram;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

/**
 * Renders the player model's cuboids as translucent boxes with bright outlines
 * ("box chams"). Geometry is captured from ModelPart.renderCuboids via
 * ModelPartMixin with the final pose matrices, so every animation (limb swing,
 * head rotation, sneaking, Figura poses) is reproduced exactly.
 */
public final class ChamsRenderer implements IMinecraft {
    /** One captured cuboid: its pose matrix and model-space bounds (already /16). */
    private record CapturedBox(Matrix4f matrix,
                               float minX, float minY, float minZ,
                               float maxX, float maxY, float maxZ) {
    }

    private static final List<CapturedBox> BOXES = new ArrayList<>();
    /** Boxes captured by HitEffect-style ghosts whose matrices already include the view transform. */
    private static final List<CapturedBox> DIRECT_BOXES = new ArrayList<>();
    private static boolean worldPhase;
    private static boolean modelCapture;
    /** Armed for the whole ghost dispatcher.render call (see beginDirectCapture). */
    private static boolean directWindow;
    /** Active only while the ghost's BASE model draws, so feature cuboids (cape) are excluded. */
    private static boolean directCapture;

    private ChamsRenderer() {
    }

    /** Called at the start of GameRenderer.renderWorld (see MixinGameRenderer). */
    @FastNative
    public static void beginWorldFrame() {
        BOXES.clear();
        worldPhase = true;
        modelCapture = false;
    }

    /**
     * Scopes capture to LivingEntityRenderer's base-model draw. Keeps GUI player
     * previews and first-person hand rendering (which call ModelPart.render
     * directly) out of the capture.
     */
    @FastNative
    public static void beginModelCapture() {
        if (directWindow) {
            // Ghost path: capture only the base model, not the feature renderers
            // (cape etc.) that run later in the same dispatcher.render call.
            directCapture = true;
        } else {
            modelCapture = worldPhase && Chams.INSTANCE.isEnabled();
        }
    }

    @FastNative
    public static void endModelCapture() {
        modelCapture = false;
        directCapture = false;
    }

    @FastNative
    public static boolean isCapturing() {
        return modelCapture || directCapture;
    }

    /**
     * Direct capture for effect ghosts (HitEffect): the caller renders a player
     * model with view-rotated matrices, then renderDirect() draws the captured
     * cuboids as chams boxes in that same space - no extra view multiplication.
     */
    @FastNative
    public static void beginDirectCapture() {
        directWindow = true;
    }

    /** Draws and clears the directly captured boxes. Ends the direct capture window. */
    public static void renderDirect() {
        directWindow = false;
        directCapture = false;
        if (DIRECT_BOXES.isEmpty()) {
            return;
        }
        RenderSystem.disableCull();
        drawBoxes(DIRECT_BOXES, null);
        RenderSystem.enableCull();
        DIRECT_BOXES.clear();
    }

    /** Discarding consumer used by HitEffect's direct chams copies. */
    private static final net.minecraft.client.render.VertexConsumer NOOP_CONSUMER = new net.minecraft.client.render.VertexConsumer() {
        public net.minecraft.client.render.VertexConsumer vertex(float x, float y, float z) { return this; }
        public net.minecraft.client.render.VertexConsumer color(int red, int green, int blue, int alpha) { return this; }
        public net.minecraft.client.render.VertexConsumer texture(float u, float v) { return this; }
        public net.minecraft.client.render.VertexConsumer overlay(int u, int v) { return this; }
        public net.minecraft.client.render.VertexConsumer light(int u, int v) { return this; }
        public net.minecraft.client.render.VertexConsumer normal(float x, float y, float z) { return this; }
    };

    @FastNative
    public static boolean shouldHideModel() {
        return false;
    }

    public static net.minecraft.client.render.VertexConsumer noopConsumer() {
        return NOOP_CONSUMER;
    }

    /** The port overlays chams on the vanilla model and does not hide its features. */
    @FastNative
    public static boolean shouldHideFeatures(net.minecraft.client.render.entity.state.EntityRenderState state) {
        return false;
    }

    /** Invoked from ModelPartMixin for every rendered cuboid while capturing. */
    public static void capture(MatrixStack.Entry entry, ModelPart.Cuboid cuboid) {
        (directCapture ? DIRECT_BOXES : BOXES).add(new CapturedBox(new Matrix4f(entry.getPositionMatrix()),
                cuboid.minX / 16.0F, cuboid.minY / 16.0F, cuboid.minZ / 16.0F,
                cuboid.maxX / 16.0F, cuboid.maxY / 16.0F, cuboid.maxZ / 16.0F));
    }

    /** Called at the end of GameRenderer.renderWorld, after all entities drew. */
    public static void render() {
        worldPhase = false;
        modelCapture = false;
        if (BOXES.isEmpty()) {
            return;
        }
        Chams chams = Chams.INSTANCE;
        if (!chams.isEnabled()) {
            BOXES.clear();
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        if (chams.throughWalls.isEnabled()) {
            RenderSystem.disableDepthTest();
        } else {
            RenderSystem.enableDepthTest();
        }

        // Entity model matrices are camera-relative but do NOT include the camera
        // rotation: during entity rendering the rotation sits in the global
        // model-view stack, which is already popped by the time we flush here.
        // Pre-multiply by the frame's view matrix (the same one Render3DUtil uses
        // for its world overlays) so the boxes land exactly on the models.
        drawBoxes(BOXES, Render3DUtil.getLastWorldSpaceMatrix());

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        BOXES.clear();
    }

    /**
     * Draws a box set in Chams style. When {@code view} is non-null the boxes are
     * pre-multiplied by it (world entity pass); ghosts pass null because their
     * matrices already include the view transform. Alpha fading of ghosts comes
     * for free through RenderSystem.setShaderColor set by the caller.
     */
    private static void drawBoxes(List<CapturedBox> boxes, Matrix4f view) {
        Chams chams = Chams.INSTANCE;
        ColorRGBA base = chams.getChamsColor();
        int fillAlpha = Math.round(chams.alpha.getCurrent() * 255.0F);
        int lineAlpha = Math.min(255, fillAlpha + 51);
        ColorRGBA lineBase = new ColorRGBA(
                Math.min(255, base.getRed() + 38),
                Math.min(255, base.getGreen() + 38),
                Math.min(255, base.getBlue() + 38),
                255
        );
        int fillColor = base.withAlpha(fillAlpha).getRGB();
        int lineColor = lineBase.withAlpha(lineAlpha).getRGB();

        Matrix4f combined = new Matrix4f();
        Vector3f[] corners = new Vector3f[8];
        for (int i = 0; i < 8; i++) {
            corners[i] = new Vector3f();
        }

        GlProgram plasmaShader = chams.getPlasmaShader();
        if (chams.isPlasma() && plasmaShader.isLoaded()) {
            renderPlasmaFill(boxes, view, combined, corners, chams, plasmaShader);
        } else {
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder fill = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            for (CapturedBox box : boxes) {
                combine(view, box, combined);
                transformCorners(box, combined, FILL_EXPAND, corners);
                emitFaces(fill, corners, fillColor);
            }
            draw(fill);
        }

        // RENDERTYPE_LINES uses per-vertex normals, so the width slider remains
        // effective on core-profile drivers where plain GL_LINES is capped at 1px.
        RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
        float width = chams.lineWidth.getCurrent();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        if (chams.glow.isEnabled()) {
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SrcFactor.SRC_ALPHA,
                    GlStateManager.DstFactor.ONE,
                    GlStateManager.SrcFactor.ONE,
                    GlStateManager.DstFactor.ZERO
            );
            int layers = Math.max(1, Math.round(chams.glowLayers.getCurrent()));
            float intensity = chams.glowIntensity.getCurrent();
            for (int layer = layers; layer >= 1; layer--) {
                int glowAlpha = Math.max(1, Math.round(lineAlpha * (1.0F / (layer + 1.0F)) * 0.7F));
                int glowColor = lineBase.withAlpha(glowAlpha).getRGB();
                RenderSystem.lineWidth(width + layer * 0.75F * intensity);
                BufferBuilder glow = Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.LINES);
                for (CapturedBox box : boxes) {
                    combine(view, box, combined);
                    transformCorners(box, combined, LINE_EXPAND, corners);
                    emitEdges(glow, corners, glowColor);
                }
                draw(glow);
            }
            RenderSystem.defaultBlendFunc();
        }

        RenderSystem.lineWidth(width);
        BufferBuilder lines = Tessellator.getInstance().begin(DrawMode.LINES, VertexFormats.LINES);
        for (CapturedBox box : boxes) {
            combine(view, box, combined);
            transformCorners(box, combined, LINE_EXPAND, corners);
            emitEdges(lines, corners, lineColor);
        }
        draw(lines);
        RenderSystem.lineWidth(1.0F);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    private static void renderPlasmaFill(List<CapturedBox> boxes, Matrix4f view,
                                         Matrix4f combined, Vector3f[] corners,
                                         Chams chams, GlProgram shader) {
        ColorRGBA first = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
        ColorRGBA second = Wyvern.getInstance().getThemeManager().getCurrentTheme().getSecondColor();
        float alpha = chams.alpha.getCurrent();

        shader.use();
        setUniform(shader, "u_Color",
                first.getRed() / 255.0F,
                first.getGreen() / 255.0F,
                first.getBlue() / 255.0F,
                alpha);
        setUniform(shader, "u_Color2",
                second.getRed() / 255.0F,
                second.getGreen() / 255.0F,
                second.getBlue() / 255.0F,
                alpha);
        setUniform(shader, "u_Resolution",
                mc.getWindow().getFramebufferWidth(),
                mc.getWindow().getFramebufferHeight());
        setUniform(shader, "u_Scale", chams.plasmaScale.getCurrent());
        setUniform(shader, "u_Time", chams.getPlasmaTime());
        setUniform(shader, "u_Fov", (float) mc.options.getFov().getValue().intValue());
        setUniform(shader, "u_CameraDir",
                (float) Math.toRadians(-mc.gameRenderer.getCamera().getYaw()),
                (float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()));

        BufferBuilder fill = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION);
        for (CapturedBox box : boxes) {
            combine(view, box, combined);
            transformCorners(box, combined, FILL_EXPAND, corners);
            emitFaces(fill, corners);
        }
        draw(fill);
    }

    private static void combine(Matrix4f view, CapturedBox box, Matrix4f dest) {
        if (view != null) {
            view.mul(box.matrix(), dest);
        } else {
            dest.set(box.matrix());
        }
    }

    // Fill sits just off the skin; lines sit just off the fill - each gap kills a
    // z-fighting pair that showed up as pixel shimmer on the box edges.
    private static final float FILL_EXPAND = 0.004F;
    private static final float LINE_EXPAND = 0.011F;

    private static void transformCorners(CapturedBox box, Matrix4f m, float e, Vector3f[] out) {
        float x0 = box.minX() - e, y0 = box.minY() - e, z0 = box.minZ() - e;
        float x1 = box.maxX() + e, y1 = box.maxY() + e, z1 = box.maxZ() + e;
        m.transformPosition(x0, y0, z0, out[0]);
        m.transformPosition(x1, y0, z0, out[1]);
        m.transformPosition(x1, y1, z0, out[2]);
        m.transformPosition(x0, y1, z0, out[3]);
        m.transformPosition(x0, y0, z1, out[4]);
        m.transformPosition(x1, y0, z1, out[5]);
        m.transformPosition(x1, y1, z1, out[6]);
        m.transformPosition(x0, y1, z1, out[7]);
    }

    private static final int[][] FACES = {
            {0, 1, 2, 3}, {5, 4, 7, 6}, {4, 0, 3, 7}, {1, 5, 6, 2}, {3, 2, 6, 7}, {4, 5, 1, 0}
    };
    private static final int[][] EDGES = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };

    private static void emitFaces(BufferBuilder buffer, Vector3f[] c, int color) {
        for (int[] face : FACES) {
            for (int idx : face) {
                buffer.vertex(c[idx].x, c[idx].y, c[idx].z).color(color);
            }
        }
    }

    private static void emitFaces(BufferBuilder buffer, Vector3f[] c) {
        for (int[] face : FACES) {
            for (int idx : face) {
                buffer.vertex(c[idx].x, c[idx].y, c[idx].z);
            }
        }
    }

    private static void emitEdges(BufferBuilder buffer, Vector3f[] c, int color) {
        for (int[] edge : EDGES) {
            Vector3f a = c[edge[0]];
            Vector3f b = c[edge[1]];
            float nx = b.x - a.x;
            float ny = b.y - a.y;
            float nz = b.z - a.z;
            float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
            if (len < 1.0E-5F) {
                len = 1.0F;
            }
            nx /= len;
            ny /= len;
            nz /= len;
            buffer.vertex(a.x, a.y, a.z).color(color).normal(nx, ny, nz);
            buffer.vertex(b.x, b.y, b.z).color(color).normal(nx, ny, nz);
        }
    }

    private static void draw(BufferBuilder buffer) {
        BuiltBuffer built = buffer.endNullable();
        if (built != null) {
            BufferRenderer.drawWithGlobalProgram(built);
        }
    }

    private static void setUniform(GlProgram program, String name, float value) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(value);
        }
    }

    private static void setUniform(GlProgram program, String name, float x, float y) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(x, y);
        }
    }

    private static void setUniform(GlProgram program, String name, float x, float y, float z, float w) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z, w);
        }
    }
}
