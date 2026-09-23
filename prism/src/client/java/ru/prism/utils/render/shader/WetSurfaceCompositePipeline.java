package ru.prism.utils.render.shader;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.util.OptionalInt;

/**
 * Фуллскрин-пасс мокрого пола: реконструирует мировые позиции из depth-буфера,
 * считает отражения в экранном пространстве и подмешивает их к цветовому
 * аттачменту. Порт fun.cataclysm wet_surface под рендер-пайплайны 1.21.11.
 */
public final class WetSurfaceCompositePipeline {

    private static final Identifier PIPELINE_ID = Identifier.of("client", "pipeline/wet_surface");
    private static final Identifier SHADER_ID = Identifier.of("client", "core/wet_surface");

    private static final RenderPipeline PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET)
                    .withLocation(PIPELINE_ID)
                    .withVertexShader(SHADER_ID)
                    .withFragmentShader(SHADER_ID)
                    .withVertexFormat(VertexFormats.EMPTY, VertexFormat.DrawMode.TRIANGLES)
                    .withUniform("WetSurfaceData", UniformType.UNIFORM_BUFFER)
                    .withSampler("SceneSampler")
                    .withSampler("DepthSampler")
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withCull(false)
                    .build()
    );

    private static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f MODEL_OFFSET = new Vector3f(0f, 0f, 0f);
    private static final Matrix4f TEXTURE_MATRIX = new Matrix4f();

    /** 4 матрицы (256) + 4 vec4 (64). */
    private static final int BUFFER_SIZE = 320;
    private static final int UNIFORM_RING = 8;

    private GpuBuffer[] uniformBuffers;
    private int uniformRingIndex;
    private GpuBuffer dummyVertexBuffer;
    private ByteBuffer dataBuffer;
    private boolean initialized;

    private void ensureInitialized() {
        if (initialized) return;

        dataBuffer = MemoryUtil.memAlloc(BUFFER_SIZE);

        ByteBuffer dummy = MemoryUtil.memAlloc(4);
        dummy.putInt(0).flip();

        dummyVertexBuffer = RenderSystem.getDevice().createBuffer(
                () -> "client:wet_surface_dummy_vertex",
                GpuBuffer.USAGE_VERTEX,
                dummy
        );

        MemoryUtil.memFree(dummy);
        uniformBuffers = new GpuBuffer[UNIFORM_RING];
        initialized = true;
    }

    private GpuBuffer nextUniformBuffer() {
        GpuBuffer buf = uniformBuffers[uniformRingIndex];
        if (buf == null) {
            final int idx = uniformRingIndex;
            buf = RenderSystem.getDevice().createBuffer(
                    () -> "client:wet_surface_uniform_" + idx,
                    GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
                    BUFFER_SIZE
            );
            uniformBuffers[uniformRingIndex] = buf;
        }
        uniformRingIndex = (uniformRingIndex + 1) % UNIFORM_RING;
        return buf;
    }

    public void render(
            GpuTextureView sceneView,
            GpuTextureView depthView,
            GpuTextureView targetView,
            int width,
            int height,
            Matrix4f viewMat,
            Matrix4f projMat,
            Matrix4f invViewMat,
            Matrix4f invProjMat,
            float cameraX, float cameraY, float cameraZ,
            float time,
            float wetness,
            float puddleCoverage,
            float reflectionStrength,
            float maxDistance,
            float rippleStrength,
            float rainAmount,
            int reflectionSteps,
            boolean ripples
    ) {
        ensureInitialized();

        prepareUniformData(width, height, viewMat, projMat, invViewMat, invProjMat,
                cameraX, cameraY, cameraZ, time,
                wetness, puddleCoverage, reflectionStrength, maxDistance,
                rippleStrength, rainAmount, reflectionSteps, ripples);

        GpuBuffer uniformBuffer = nextUniformBuffer();

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
        encoder.writeToBuffer(uniformBuffer.slice(0, dataBuffer.remaining()), dataBuffer);

        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().write(
                RenderSystem.getModelViewMatrix(),
                COLOR_MODULATOR,
                MODEL_OFFSET,
                TEXTURE_MATRIX
        );

        GpuSampler sceneSampler = RenderSystem.getSamplerCache().get(FilterMode.LINEAR);
        GpuSampler depthSampler = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);

        try (RenderPass pass = encoder.createRenderPass(
                () -> "client:wet_surface_pass",
                targetView,
                OptionalInt.empty()
        )) {
            pass.setPipeline(PIPELINE);
            pass.setVertexBuffer(0, dummyVertexBuffer);

            pass.bindTexture("SceneSampler", sceneView, sceneSampler);
            pass.bindTexture("DepthSampler", depthView, depthSampler);

            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", dynamicTransforms);
            pass.setUniform("WetSurfaceData", uniformBuffer);

            pass.draw(0, 6);
        }
    }

    private void prepareUniformData(
            int width,
            int height,
            Matrix4f viewMat,
            Matrix4f projMat,
            Matrix4f invViewMat,
            Matrix4f invProjMat,
            float cameraX, float cameraY, float cameraZ,
            float time,
            float wetness,
            float puddleCoverage,
            float reflectionStrength,
            float maxDistance,
            float rippleStrength,
            float rainAmount,
            int reflectionSteps,
            boolean ripples
    ) {
        dataBuffer.clear();

        putMatrix(dataBuffer, invViewMat);
        putMatrix(dataBuffer, invProjMat);
        putMatrix(dataBuffer, viewMat);
        putMatrix(dataBuffer, projMat);

        dataBuffer.putFloat(cameraX);
        dataBuffer.putFloat(cameraY);
        dataBuffer.putFloat(cameraZ);
        dataBuffer.putFloat(0.0f);

        dataBuffer.putFloat(width);
        dataBuffer.putFloat(height);
        dataBuffer.putFloat(time);
        dataBuffer.putFloat(0.0f);

        dataBuffer.putFloat(wetness);
        dataBuffer.putFloat(puddleCoverage);
        dataBuffer.putFloat(reflectionStrength);
        dataBuffer.putFloat(maxDistance);

        dataBuffer.putFloat(rippleStrength);
        dataBuffer.putFloat(rainAmount);
        dataBuffer.putFloat(reflectionSteps);
        dataBuffer.putFloat(ripples ? 1.0f : 0.0f);

        dataBuffer.flip();
    }

    private static void putMatrix(ByteBuffer buffer, Matrix4f matrix) {
        buffer.putFloat(matrix.m00()).putFloat(matrix.m01()).putFloat(matrix.m02()).putFloat(matrix.m03());
        buffer.putFloat(matrix.m10()).putFloat(matrix.m11()).putFloat(matrix.m12()).putFloat(matrix.m13());
        buffer.putFloat(matrix.m20()).putFloat(matrix.m21()).putFloat(matrix.m22()).putFloat(matrix.m23());
        buffer.putFloat(matrix.m30()).putFloat(matrix.m31()).putFloat(matrix.m32()).putFloat(matrix.m33());
    }

    public void close() {
        if (uniformBuffers != null) {
            for (int i = 0; i < uniformBuffers.length; i++) {
                if (uniformBuffers[i] != null) {
                    uniformBuffers[i].close();
                    uniformBuffers[i] = null;
                }
            }
            uniformBuffers = null;
        }
        uniformRingIndex = 0;
        if (dummyVertexBuffer != null) {
            dummyVertexBuffer.close();
            dummyVertexBuffer = null;
        }
        if (dataBuffer != null) {
            MemoryUtil.memFree(dataBuffer);
            dataBuffer = null;
        }
        initialized = false;
    }
}
