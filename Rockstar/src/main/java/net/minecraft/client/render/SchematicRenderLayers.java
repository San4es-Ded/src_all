package net.minecraft.client.render;

import net.minecraft.client.render.RenderLayer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.texture.SpriteAtlasTexture;

public final class SchematicRenderLayers {
    private static RenderLayer visible;
    private static RenderLayer hidden;

    private SchematicRenderLayers() {
    }

    public static RenderLayer visible() {
        if (visible == null) {
            visible = SchematicRenderLayers.create("rockstar_schematic", DepthTestFunction.LEQUAL_DEPTH_TEST, true);
        }
        return visible;
    }

    public static RenderLayer hidden() {
        if (hidden == null) {
            hidden = SchematicRenderLayers.create("rockstar_schematic_hidden", DepthTestFunction.GREATER_DEPTH_TEST, false);
        }
        return hidden;
    }

    private static RenderLayer create(String name, DepthTestFunction depthTest, boolean depthWrite) {
        RenderPipeline pipeline = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
                .withLocation("rockstar:pipeline/" + name)
                .withShaderDefine("ALPHA_CUTOUT", 0.1F)
                .withShaderDefine("PER_FACE_LIGHTING")
                .withSampler("Sampler1")
                .withBlend(BlendFunction.TRANSLUCENT)
                .withDepthTestFunction(depthTest)
                .withDepthWrite(depthWrite)
                .build()
        );
        return RenderLayer.of(
            name,
            RenderSetup.builder(pipeline)
                .texture("Sampler0", SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                .useLightmap()
                .useOverlay()
                .translucent()
                .build()
        );
    }
}
