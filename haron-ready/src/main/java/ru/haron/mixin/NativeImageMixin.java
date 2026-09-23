package ru.haron.mixin;

import haron.render.NativePointer;
import net.minecraft.client.texture.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={NativeImage.class})
public class NativeImageMixin
implements NativePointer {
    @Shadow
    private long pointer;

    @Override
    public long pointer() {
        return this.pointer;
    }
}

