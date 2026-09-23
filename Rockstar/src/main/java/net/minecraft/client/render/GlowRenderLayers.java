package net.minecraft.client.render;


import rockstar.client.internal.render.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.render.RenderLayer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.util.Identifier;
import rockstar.client.internal.render.GlowEntitySolidShader;

public final class GlowRenderLayers {
    private static final Map<Identifier, RenderLayer> CACHE = new HashMap<Identifier, RenderLayer>();
    private static RenderPipeline glowProgram;

    private GlowRenderLayers() {
    }

    public static void initShader(GlowEntitySolidShader internalValue0011) {
        // Baseline layer uses depth test/write but explicitly disables culling
        // and blending so the glow shell remains visible from both sides.
        glowProgram = internalValue0011.worldLayerPipeline(false, false, false);
        CACHE.clear();
    }

    public static RenderLayer get(Identifier identifier) {
        if (glowProgram == null) {
            return RenderLayers.entityCutoutNoCull(identifier);
        }
        return CACHE.computeIfAbsent(identifier, GlowRenderLayers::create);
    }

    private static RenderLayer create(Identifier identifier) {
        return RenderLayer.of("rockstar_glow_entity", RenderSetup.builder(glowProgram).texture("Sampler0", identifier).build());
    }
}
