package net.minecraft.client.render;


import rockstar.client.internal.render.*;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.Identifier;
import rockstar.client.internal.render.ShaderProgramBase;

public final class SmoothItemLayers {
    private static final Map<String, RenderLayer> CACHE = new HashMap<String, RenderLayer>();
    private static ShaderProgramBase translucentProgram;
    private static ShaderProgramBase cutoutProgram;

    private SmoothItemLayers() {
    }

    public static void initShaders(ShaderProgramBase typedValue017, ShaderProgramBase typedValue018) {
        translucentProgram = typedValue017;
        cutoutProgram = typedValue018;
        CACHE.clear();
    }

    public static boolean ready() {
        return translucentProgram != null && translucentProgram.internalMethod04677() && cutoutProgram != null && cutoutProgram.internalMethod04677();
    }

    public static RenderLayer cutout(Identifier identifier) {
        return CACHE.computeIfAbsent("cutout|" + identifier, string -> RenderLayer.of(
            "rockstar_item_cutout",
            RenderSetup.builder(cutoutProgram.worldLayerPipeline(false, false))
                .texture("Sampler0", identifier, () -> com.mojang.blaze3d.systems.RenderSystem.getSamplerCache().get(com.mojang.blaze3d.textures.FilterMode.LINEAR))
                .useLightmap()
                .useOverlay()
                .build()
        ));
    }

    public static RenderLayer translucent(Identifier identifier) {
        return CACHE.computeIfAbsent("translucent|" + identifier, string -> RenderLayer.of(
            "rockstar_item_translucent",
            RenderSetup.builder(translucentProgram.worldLayerPipeline(true, false))
                .texture("Sampler0", identifier, () -> com.mojang.blaze3d.systems.RenderSystem.getSamplerCache().get(com.mojang.blaze3d.textures.FilterMode.LINEAR))
                .outputTarget(OutputTarget.ITEM_ENTITY_TARGET)
                .useLightmap()
                .useOverlay()
                .translucent()
                .build()
        ));
    }
}
