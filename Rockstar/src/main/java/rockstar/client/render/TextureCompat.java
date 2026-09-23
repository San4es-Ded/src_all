package rockstar.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import java.util.HashMap;
import java.util.Map;
import moscow.rockstar.mixin.accessors.AbstractTextureAccessor;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.GlTexture;

/** Maps the pre-1.21.5 blur/mipmap flags to immutable GPU samplers. */
public final class TextureCompat {
    private static final Map<Integer, GpuTextureView> EXTERNAL_GL_VIEWS = new HashMap<>();

    private TextureCompat() {
    }

    public static void setFilter(AbstractTexture texture, boolean blur, boolean mipmap) {
        FilterMode filter = blur ? FilterMode.LINEAR : FilterMode.NEAREST;
        ((AbstractTextureAccessor)(Object)texture).rockstar$setSampler(
            RenderSystem.getSamplerCache().getRepeated(filter, mipmap)
        );
    }

    /**
     * Exposes Rockstar's still manually-uploaded OpenGL textures to the new
     * explicit render-pass API without taking ownership of their GL names.
     */
    public static GpuTextureView viewOfGlId(int glId) {
        if (glId == 0) {
            return null;
        }
        return EXTERNAL_GL_VIEWS.computeIfAbsent(glId, id -> RenderSystem.getDevice().createTextureView(new ExternalGlTexture(id)));
    }

    private static final class ExternalGlTexture extends GlTexture {
        private ExternalGlTexture(int glId) {
            super(GpuTexture.USAGE_TEXTURE_BINDING, "Rockstar external GL texture " + glId, TextureFormat.RGBA8, 1, 1, 1, 1, glId);
        }

        @Override
        public void close() {
            // Ownership stays with the legacy allocator that created the GL id.
        }

        @Override
        public boolean isClosed() {
            return false;
        }
    }
}
