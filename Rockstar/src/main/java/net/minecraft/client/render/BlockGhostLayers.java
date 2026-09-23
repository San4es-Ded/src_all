package net.minecraft.client.render;


import rockstar.client.internal.render.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import rockstar.client.internal.render.ShaderProgramBase;

public final class BlockGhostLayers {
    private static ShaderProgramBase program;
    private static RenderLayer layer;

    private BlockGhostLayers() {
    }

    public static void initShader(ShaderProgramBase typedValue017) {
        program = typedValue017;
        layer = null;
    }

    public static boolean ready() {
        return program != null && program.internalMethod04677();
    }

    public static RenderLayer ghost() {
        if (layer == null) {
            layer = RenderLayer.of(
                "rockstar_block_ghost",
                RenderSetup.builder(program.worldLayerPipeline(true, true))
                    .texture("Sampler0", SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
                    .useLightmap()
                    .translucent()
                    .build()
            );
        }
        return layer;
    }
}
