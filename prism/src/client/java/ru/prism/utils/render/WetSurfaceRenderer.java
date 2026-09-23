package ru.prism.utils.render;

import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import ru.prism.utils.render.shader.WetSurfaceCompositePipeline;

/**
 * Состояние и запуск пост-эффекта мокрого пола. Копирует цвет и depth
 * основного фреймбуфера во вспомогательные текстуры (сэмплить привязанные
 * к пассу аттачменты нельзя) и отдаёт всё в {@link WetSurfaceCompositePipeline}.
 */
public final class WetSurfaceRenderer {

    private static WetSurfaceRenderer instance;

    private final MinecraftClient mc = MinecraftClient.getInstance();

    private final WetSurfaceCompositePipeline pipeline = new WetSurfaceCompositePipeline();

    private GpuTexture sceneTexture;
    private GpuTextureView sceneTextureView;
    private GpuTexture depthTexture;
    private GpuTextureView depthTextureView;

    private int lastWidth;
    private int lastHeight;

    private float time;

    private WetSurfaceRenderer() {
    }

    public static WetSurfaceRenderer getInstance() {
        if (instance == null) {
            instance = new WetSurfaceRenderer();
        }
        return instance;
    }

    public static final class Parameters {
        public float wetness = 1.0f;
        public float puddleCoverage = 0.82f;
        public float reflectionStrength = 1.25f;
        public float maxDistance = 56.0f;
        public float rippleStrength = 0.8f;
        public float rainAmount = 1.0f;
        public int reflectionSteps = 10;
        public boolean ripples = true;
    }

    public void reset() {
        time = 0.0f;
    }

    public void apply(Camera camera, Matrix4f viewMatrix, Matrix4f projectionMatrix, Parameters parameters) {
        if (mc.world == null || mc.player == null || camera == null) return;

        Framebuffer fb = mc.getFramebuffer();
        if (fb == null || fb.getColorAttachment() == null
                || fb.getColorAttachmentView() == null
                || fb.getDepthAttachment() == null) {
            return;
        }

        int widthPx = fb.textureWidth;
        int heightPx = fb.textureHeight;
        if (widthPx <= 0 || heightPx <= 0) return;

        ensureTextures(widthPx, heightPx);

        time += 1.0f / 60.0f;

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        encoder.copyTextureToTexture(fb.getColorAttachment(), sceneTexture,
                0, 0, 0, 0, 0, widthPx, heightPx);
        encoder.copyTextureToTexture(fb.getDepthAttachment(), depthTexture,
                0, 0, 0, 0, 0, widthPx, heightPx);

        Matrix4f invView = new Matrix4f(viewMatrix).invert();
        Matrix4f invProj = new Matrix4f(projectionMatrix).invert();
        Vector3f cameraPos = camera.getCameraPos().toVector3f();

        pipeline.render(
                sceneTextureView,
                depthTextureView,
                fb.getColorAttachmentView(),
                widthPx,
                heightPx,
                viewMatrix,
                projectionMatrix,
                invView,
                invProj,
                cameraPos.x, cameraPos.y, cameraPos.z,
                time,
                parameters.wetness,
                parameters.puddleCoverage,
                parameters.reflectionStrength,
                parameters.maxDistance,
                parameters.rippleStrength,
                parameters.rainAmount,
                parameters.reflectionSteps,
                parameters.ripples
        );
    }

    private void ensureTextures(int width, int height) {
        if (sceneTexture != null && width == lastWidth && height == lastHeight) {
            return;
        }

        cleanupTextures();

        sceneTexture = RenderSystem.getDevice().createTexture(
                () -> "client:wet_surface_scene",
                GpuTexture.USAGE_COPY_DST | GpuTexture.USAGE_TEXTURE_BINDING,
                TextureFormat.RGBA8,
                width, height, 1, 1
        );
        sceneTextureView = RenderSystem.getDevice().createTextureView(sceneTexture);

        depthTexture = RenderSystem.getDevice().createTexture(
                () -> "client:wet_surface_depth",
                GpuTexture.USAGE_TEXTURE_BINDING | GpuTexture.USAGE_COPY_DST,
                TextureFormat.DEPTH32,
                width, height, 1, 1
        );
        depthTextureView = RenderSystem.getDevice().createTextureView(depthTexture);

        lastWidth = width;
        lastHeight = height;
    }

    private void cleanupTextures() {
        if (sceneTextureView != null) {
            sceneTextureView.close();
            sceneTextureView = null;
        }
        if (sceneTexture != null) {
            sceneTexture.close();
            sceneTexture = null;
        }
        if (depthTextureView != null) {
            depthTextureView.close();
            depthTextureView = null;
        }
        if (depthTexture != null) {
            depthTexture.close();
            depthTexture = null;
        }
    }

    public void close() {
        cleanupTextures();
        pipeline.close();
    }
}
