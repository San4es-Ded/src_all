package haron.render.shader;

import haron.render.shader.ShaderStage;
import haron.render.shader.ShaderProgramBuilder;
import haron.render.shader.ShaderRegistry;

public final class DefaultShaders {
    private static final ShaderRegistry REGISTRY = new ShaderRegistry();

    public static void loadDefaultShaders() {
        if (REGISTRY.find("round_rect").isPresent()) {
            return;
        }
        DefaultShaders.register("round_rect", "fragment/round_rect.fsh");
        DefaultShaders.register("round_rect_outline", "fragment/round_rect_outline.fsh");
        DefaultShaders.register("drop_shadow", "fragment/drop_shadow.fsh");
        DefaultShaders.register("rect_glow", "fragment/rect_glow.fsh");
        DefaultShaders.register("round_texture", "fragment/round_texture.fsh");
        DefaultShaders.register("gradient", "fragment/gradient.fsh");
        DefaultShaders.register("hue_bar", "fragment/hue_bar.fsh");
        DefaultShaders.register("hand_shader", "fragment/hand_shader.fsh");
        DefaultShaders.register("block_nebula", "fragment/block_nebula.fsh");
        DefaultShaders.register("block_starfield", "fragment/block_starfield.fsh");
        DefaultShaders.register("block_cobweb", "fragment/block_cobweb.fsh");
        DefaultShaders.register("block_plasma", "fragment/block_plasma.fsh");
    }

    public static ShaderRegistry getRegistry() {
        return REGISTRY;
    }

    private DefaultShaders() {
    }

    private static void register(String string, String string2) {
        REGISTRY.register(string, ShaderProgramBuilder.create().attach("vertex/passthrough.vsh", ShaderStage.VERTEX).attach(string2, ShaderStage.FRAGMENT).link().build());
    }
}

