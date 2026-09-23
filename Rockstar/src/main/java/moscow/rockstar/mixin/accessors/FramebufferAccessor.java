package moscow.rockstar.mixin.accessors;

import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={Framebuffer.class})
public interface FramebufferAccessor {
    @Accessor(value="depthAttachment")
    void setDepthAttachment(GpuTexture depthAttachment);
}
