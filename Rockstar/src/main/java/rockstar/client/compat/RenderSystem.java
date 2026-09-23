package rockstar.client.compat;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.GpuTexture;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import rockstar.client.render.compat.ImmediateRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

/** Compatibility state used by Rockstar's legacy immediate draw call sites. */
public final class RenderSystem {
    private static final Matrix4fStack MODEL_VIEW = new Matrix4fStack(32);
    private static final Matrix4f PROJECTION = new Matrix4f();
    private static final java.util.List<GpuBuffer> FRAME_PROJECTIONS = new java.util.ArrayList<>();
    private static ShaderProgram shader;
    private record SavedProjection(Matrix4f matrix, GpuBufferSlice buffer, ProjectionType type) { }
    private static final Deque<SavedProjection> PROJECTION_STACK = new ArrayDeque<>();
    private static boolean guiPhase;
    private static int clearColor;
    private static final Queue<Runnable> EARLY_RENDER_CALLS = new ConcurrentLinkedQueue<>();

    static {
        MODEL_VIEW.identity();
    }

    private RenderSystem() {
    }

    public static ShaderProgram setShader(ShaderProgramKey key) {
        replayEarlyRenderCalls();
        ShaderProgram program = Objects.requireNonNull(key).program();
        program.bind();
        shader = program;
        return program;
    }

    public static void setShaderTexture(int slot, Identifier texture) {
        if (slot == 0) {
            ImmediateRenderer.setTexture(texture);
        }
    }

    public static void setShaderTexture(int slot, int texture) {
        setShaderTexture(slot, texture, com.mojang.blaze3d.textures.FilterMode.LINEAR);
    }

    public static void setShaderTexture(int slot, int texture, com.mojang.blaze3d.textures.FilterMode filter) {
        if (texture == 0) {
            if (slot == 0) ImmediateRenderer.clearTexture();
            else ImmediateRenderer.clearTexture("Sampler" + slot);
            return;
        }
        GpuTextureView view = rockstar.client.render.TextureCompat.viewOfGlId(texture);
        if (slot == 0) ImmediateRenderer.setTexture(view, filter);
        else ImmediateRenderer.setTexture("Sampler" + slot, view, filter);
    }

    public static void setShaderTexture(int slot, GpuTextureView texture) {
        if (slot == 0) ImmediateRenderer.setTexture(texture);
        else ImmediateRenderer.setTexture("Sampler" + slot, texture, com.mojang.blaze3d.textures.FilterMode.LINEAR);
    }

    public static void setShaderTexture(int slot, GpuTexture texture) {
        if (texture == null) {
            if (slot == 0) ImmediateRenderer.clearTexture();
            return;
        }
        throw new IllegalArgumentException("Bind framebuffer attachments through getColorAttachmentView()/getDepthAttachmentView()");
    }

    public static void setShaderColor(float red, float green, float blue, float alpha) {
        ImmediateRenderer.setShaderColor(red, green, blue, alpha);
    }

    public static void enableBlend() { ImmediateRenderer.enableBlend(true); }
    public static void disableBlend() { ImmediateRenderer.enableBlend(false); }
    public static boolean isBlendEnabled() { return ImmediateRenderer.isBlendEnabled(); }
    public static boolean isCullEnabled() { return ImmediateRenderer.isCullEnabled(); }
    public static boolean isDepthTestEnabled() { return ImmediateRenderer.isDepthTestEnabled(); }
    public static void defaultBlendFunc() { ImmediateRenderer.defaultBlend(); }
    public static void blendFunc(int source, int destination) {
        ImmediateRenderer.setBlend(new BlendFunction(sourceFactor(source), destinationFactor(destination)));
    }
    public static void blendFunc(SourceFactor source, DestFactor destination) {
        ImmediateRenderer.setBlend(new BlendFunction(source, destination));
    }
    public static void blendFuncSeparate(int sourceRgb, int destinationRgb, int sourceAlpha, int destinationAlpha) {
        ImmediateRenderer.setBlend(new BlendFunction(
            sourceFactor(sourceRgb), destinationFactor(destinationRgb), sourceFactor(sourceAlpha), destinationFactor(destinationAlpha)
        ));
    }
    public static void blendFuncSeparate(SourceFactor sourceRgb, DestFactor destinationRgb,
                                         SourceFactor sourceAlpha, DestFactor destinationAlpha) {
        ImmediateRenderer.setBlend(new BlendFunction(sourceRgb, destinationRgb, sourceAlpha, destinationAlpha));
    }

    public static void enableDepthTest() { ImmediateRenderer.enableDepthTest(); }
    public static void disableDepthTest() { ImmediateRenderer.disableDepthTest(); }
    public static void depthMask(boolean value) { ImmediateRenderer.depthMask(value); }
    public static void enableCull() { ImmediateRenderer.enableCull(true); }
    public static void disableCull() { ImmediateRenderer.enableCull(false); }
    public static void lineWidth(float width) { ImmediateRenderer.setLineWidth(width); }
    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        if (red != green || red != blue) throw new IllegalArgumentException("Independent RGB masks are not supported by RenderPipeline");
        ImmediateRenderer.colorMask(red, alpha);
    }
    public static void viewport(int x, int y, int width, int height) {
        // Framebuffer capture/post-process code temporarily renders into
        // attachments with dimensions different from the main window.  The
        // 1.21.11 pipeline no longer exposes the old facade method, but the
        // viewport is still dynamic OpenGL state and must be restored around
        // those passes or their output is stretched/cropped.
        com.mojang.blaze3d.opengl.GlStateManager._viewport(x, y, width, height);
    }
    public static void depthFunc(int function) {
        ImmediateRenderer.setDepthFunction(switch (function) {
            case 513 -> DepthTestFunction.LESS_DEPTH_TEST;
            case 514 -> DepthTestFunction.EQUAL_DEPTH_TEST;
            case 515 -> DepthTestFunction.LEQUAL_DEPTH_TEST;
            case 516 -> DepthTestFunction.GREATER_DEPTH_TEST;
            case 519 -> DepthTestFunction.NO_DEPTH_TEST;
            default -> throw new IllegalArgumentException("Unsupported depth function: " + function);
        });
    }
    public static void enableScissor(int x, int y, int width, int height) {
        ImmediateRenderer.setFramebufferScissor(x, y, width, height);
        com.mojang.blaze3d.systems.RenderSystem.enableScissorForRenderTypeDraws(x, y, width, height);
    }
    public static void disableScissor() {
        ImmediateRenderer.clearScissor();
        com.mojang.blaze3d.systems.RenderSystem.disableScissorForRenderTypeDraws();
    }
    public static void activeTexture(int texture) { }
    public static void bindTexture(int texture) { }

    public static Matrix4fStack getModelViewStack() { return MODEL_VIEW; }
    public static Matrix4f getModelViewMatrix() { return new Matrix4f(MODEL_VIEW); }
    public static Matrix4f getProjectionMatrix() { return new Matrix4f(PROJECTION); }
    public static ProjectionType getProjectionType() { return com.mojang.blaze3d.systems.RenderSystem.getProjectionType(); }
    public static void setProjectionMatrix(Matrix4f matrix, ProjectionType type) {
        PROJECTION.set(matrix);
        ByteBuffer bytes = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());
        Std140Builder.intoBuffer(bytes).putMat4f(matrix);
        // createBuffer reads the remaining range. Std140Builder leaves the
        // position at the end, so without flip() 1.21.11 sees an empty source.
        bytes.flip();
        GpuBuffer next = com.mojang.blaze3d.systems.RenderSystem.getDevice().createBuffer(
            () -> "Rockstar projection", GpuBuffer.USAGE_UNIFORM, bytes
        );
        com.mojang.blaze3d.systems.RenderSystem.setProjectionMatrix(next.slice(), type);
        // Nested post-process passes may restore an earlier projection later
        // in this frame. Retire its storage only after all draws have finished.
        FRAME_PROJECTIONS.add(next);
    }

    public static void backupProjectionMatrix() {
        PROJECTION_STACK.push(new SavedProjection(new Matrix4f(PROJECTION),
            com.mojang.blaze3d.systems.RenderSystem.getProjectionMatrixBuffer(), getProjectionType()));
    }

    public static void restoreProjectionMatrix() {
        if (!PROJECTION_STACK.isEmpty()) {
            SavedProjection saved = PROJECTION_STACK.pop();
            PROJECTION.set(saved.matrix());
            com.mojang.blaze3d.systems.RenderSystem.setProjectionMatrix(saved.buffer(), saved.type());
        }
    }

    /** Mirrors the CPU world projection used by projection/ESP helpers. */
    public static void syncWorldProjection(Matrix4f matrix) {
        PROJECTION.set(matrix);
        MODEL_VIEW.identity();
        enableDepthTest();
        depthMask(true);
        depthFunc(515);
        enableCull();
        disableBlend();
        colorMask(true, true, true, true);
    }

    public static void beginGuiFrame() {
        backupProjectionMatrix();
        var window = net.minecraft.client.MinecraftClient.getInstance().getWindow();
        setProjectionMatrix(new Matrix4f().setOrtho(0.0F, window.getScaledWidth(),
            window.getScaledHeight(), 0.0F, 1000.0F, 21000.0F), ProjectionType.ORTHOGRAPHIC);
        MODEL_VIEW.pushMatrix().identity().translate(0.0F, 0.0F, -11000.0F);
        disableDepthTest();
        depthMask(false);
        disableCull();
        enableBlend();
        defaultBlendFunc();
        setShaderColor(1, 1, 1, 1);
        guiPhase = true;
        ImmediateRenderer.setGuiPhase(true);
    }

    public static void endGuiFrame() {
        if (!guiPhase) return;
        guiPhase = false;
        ImmediateRenderer.setGuiPhase(false);
        MODEL_VIEW.popMatrix();
        restoreProjectionMatrix();
    }

    public static void finishFrame() {
        endGuiFrame();
        for (GpuBuffer buffer : FRAME_PROJECTIONS) {
            com.mojang.blaze3d.systems.RenderSystem.queueFencedTask(buffer::close);
        }
        FRAME_PROJECTIONS.clear();
    }

    public static ShaderProgram getShader() { return shader; }
    public static float[] getShaderColor() { return ImmediateRenderer.getShaderColor(); }
    public static boolean isOnRenderThread() { return com.mojang.blaze3d.systems.RenderSystem.isOnRenderThread(); }
    public static void clearColor(float red, float green, float blue, float alpha) {
        clearColor = rockstar.client.render.FramebufferCompat.color(red, green, blue, alpha);
    }
    public static void clear(int mask) {
        if ((mask & 0x4000) != 0) {
            ImmediateRenderer.clearColor(clearColor);
        }
        if ((mask & 0x0100) != 0) {
            ImmediateRenderer.clearDepth();
        }
    }

    public static Tessellator renderThreadTesselator() { return Tessellator.getInstance(); }
    public static void assertOnRenderThread() { com.mojang.blaze3d.systems.RenderSystem.assertOnRenderThread(); }
    public static void recordRenderCall(Runnable action) {
        Objects.requireNonNull(action);
        if (com.mojang.blaze3d.systems.RenderSystem.tryGetDevice() == null) {
            EARLY_RENDER_CALLS.add(action);
        } else if (com.mojang.blaze3d.systems.RenderSystem.isOnRenderThread()) {
            action.run();
        } else {
            com.mojang.blaze3d.systems.RenderSystem.queueFencedTask(action);
        }
    }

    private static void replayEarlyRenderCalls() {
        if (com.mojang.blaze3d.systems.RenderSystem.tryGetDevice() == null
                || !com.mojang.blaze3d.systems.RenderSystem.isOnRenderThread()) {
            return;
        }
        Runnable action;
        while ((action = EARLY_RENDER_CALLS.poll()) != null) {
            action.run();
        }
    }

    private static SourceFactor sourceFactor(int value) {
        return switch (value) {
            case 0 -> SourceFactor.ZERO;
            case 1 -> SourceFactor.ONE;
            case 768 -> SourceFactor.SRC_COLOR;
            case 769 -> SourceFactor.ONE_MINUS_SRC_COLOR;
            case 770 -> SourceFactor.SRC_ALPHA;
            case 771 -> SourceFactor.ONE_MINUS_SRC_ALPHA;
            case 772 -> SourceFactor.DST_ALPHA;
            case 773 -> SourceFactor.ONE_MINUS_DST_ALPHA;
            case 774 -> SourceFactor.DST_COLOR;
            case 775 -> SourceFactor.ONE_MINUS_DST_COLOR;
            case 776 -> SourceFactor.SRC_ALPHA_SATURATE;
            case 32769 -> SourceFactor.CONSTANT_COLOR;
            case 32770 -> SourceFactor.ONE_MINUS_CONSTANT_COLOR;
            case 32771 -> SourceFactor.CONSTANT_ALPHA;
            case 32772 -> SourceFactor.ONE_MINUS_CONSTANT_ALPHA;
            default -> throw new IllegalArgumentException("Unsupported OpenGL source blend factor: " + value);
        };
    }

    private static DestFactor destinationFactor(int value) {
        return switch (value) {
            case 0 -> DestFactor.ZERO;
            case 1 -> DestFactor.ONE;
            case 768 -> DestFactor.SRC_COLOR;
            case 769 -> DestFactor.ONE_MINUS_SRC_COLOR;
            case 770 -> DestFactor.SRC_ALPHA;
            case 771 -> DestFactor.ONE_MINUS_SRC_ALPHA;
            case 772 -> DestFactor.DST_ALPHA;
            case 773 -> DestFactor.ONE_MINUS_DST_ALPHA;
            case 774 -> DestFactor.DST_COLOR;
            case 775 -> DestFactor.ONE_MINUS_DST_COLOR;
            case 32769 -> DestFactor.CONSTANT_COLOR;
            case 32770 -> DestFactor.ONE_MINUS_CONSTANT_COLOR;
            case 32771 -> DestFactor.CONSTANT_ALPHA;
            case 32772 -> DestFactor.ONE_MINUS_CONSTANT_ALPHA;
            default -> throw new IllegalArgumentException("Unsupported OpenGL destination blend factor: " + value);
        };
    }
    public static float getShaderGameTime() { return (System.currentTimeMillis() % 1_200_000L) / 1_200_000.0f; }
}
