package moscow.rockstar.mixin.minecraft.world;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={AbstractBlock.AbstractBlockState.class})
public abstract class AbstractBlockStateMixin
implements MinecraftClientAccess {
    @Shadow
    public abstract Block getBlock();
}

