package rockstar.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.util.math.ColorHelper;
import rockstar.client.render.compat.ImmediateRenderer;

/** Explicit framebuffer routing for the 1.21.11 render-pass API. */
public final class FramebufferCompat {
    private FramebufferCompat() {
    }

    public static void beginWrite(Framebuffer framebuffer, boolean setViewport) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (framebuffer == client.getFramebuffer()) {
            ImmediateRenderer.clearTarget();
            RenderSystem.outputColorTextureOverride = null;
            RenderSystem.outputDepthTextureOverride = null;
        } else {
            ImmediateRenderer.setTarget(framebuffer);
            RenderSystem.outputColorTextureOverride = framebuffer.getColorAttachmentView();
            RenderSystem.outputDepthTextureOverride = framebuffer.useDepthAttachment ? framebuffer.getDepthAttachmentView() : null;
        }
    }

    public static void clear(Framebuffer framebuffer, int clearColor) {
        if (framebuffer.getColorAttachment() == null) {
            return;
        }
        if (framebuffer.useDepthAttachment && framebuffer.getDepthAttachment() != null) {
            RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(
                framebuffer.getColorAttachment(), clearColor, framebuffer.getDepthAttachment(), 1.0
            );
        } else {
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(framebuffer.getColorAttachment(), clearColor);
        }
    }

    public static void endWrite(Framebuffer framebuffer) {
        if (framebuffer == MinecraftClient.getInstance().getFramebuffer()) {
            ImmediateRenderer.clearTarget();
            RenderSystem.outputColorTextureOverride = null;
            RenderSystem.outputDepthTextureOverride = null;
        }
    }

    public static int color(float red, float green, float blue, float alpha) {
        return ColorHelper.fromFloats(alpha, red, green, blue);
    }

    public static int glId(GpuTexture texture) {
        return texture instanceof GlTexture glTexture ? glTexture.getGlId() : 0;
    }
}
