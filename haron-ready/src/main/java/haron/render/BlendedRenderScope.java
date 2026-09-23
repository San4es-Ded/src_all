package haron.render;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.render.FramebufferCapture;

public final class BlendedRenderScope {
    public static void runBlended(Runnable runnable) {
        RenderSystem.enableBlend();
        FramebufferCapture.applyBlendState();
        runnable.run();
        RenderSystem.disableBlend();
    }

    private BlendedRenderScope() {
    }
}

