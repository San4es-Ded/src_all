package moscow.rockstar.mixin.minecraft.world.chunk;


import rockstar.client.internal.game.*;
import java.util.Map;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.internal.game.BlockEntityCache;

@Mixin(value={WorldChunk.class})
public abstract class WorldChunkMixin {
    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract Map<BlockPos, BlockEntity> getBlockEntities();

    @Inject(method={"setBlockEntity"}, at={@At(value="INVOKE", target="Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")})
    private void onLoadBlockEntity(BlockEntity blockEntity, CallbackInfo callbackInfo) {
        if (this.getWorld().isClient()) {
            BlockEntityCache.internalMethod04676(blockEntity);
        }
    }

    @Inject(method={"removeBlockEntity"}, at={@At(value="INVOKE", target="Lnet/minecraft/block/entity/BlockEntity;markRemoved()V")})
    private void onRemoveBlockEntity(BlockPos blockPos, CallbackInfo callbackInfo) {
        BlockEntityCache.internalMethod07047(blockPos);
    }

    @Inject(method={"clear"}, at={@At(value="HEAD")})
    private void onClearBlockEntities(CallbackInfo callbackInfo) {
        if (!this.getWorld().isClient()) {
            return;
        }
        for (BlockPos blockPos : this.getBlockEntities().keySet()) {
            BlockEntityCache.internalMethod07047(blockPos);
        }
    }
}

