package moscow.rockstar.mixin.sodium;



import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.client.internal.script.GhostBlockRenderer;
import rockstar.client.internal.game.DynamicLightEngine;

@Pseudo
@Mixin(targets={"net.caffeinemc.mods.sodium.client.world.LevelSlice"}, remap=false)
public class LevelSliceMixin {
    @Inject(method={"getBlockState(III)Lnet/minecraft/block/BlockState;"}, at={@At(value="HEAD")}, cancellable=true, require=0)
    private void hideCameraClipBlocks(int n, int n2, int n3, CallbackInfoReturnable<BlockState> callbackInfoReturnable) {
        if (GhostBlockRenderer.internalMethod01361(n, n2, n3)) {
            callbackInfoReturnable.setReturnValue(Blocks.AIR.getDefaultState());
        }
    }

    @Inject(method={"getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"}, at={@At(value="HEAD")}, cancellable=true, require=0)
    private void hideCameraClipBlocks(BlockPos blockPos, CallbackInfoReturnable<BlockState> callbackInfoReturnable) {
        if (GhostBlockRenderer.internalMethod00945(blockPos)) {
            callbackInfoReturnable.setReturnValue(Blocks.AIR.getDefaultState());
        }
    }

    @Inject(method={"getLightLevel(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"}, at={@At(value="RETURN")}, cancellable=true, require=0)
    private void lightCameraClipBlocks(LightType lightType, BlockPos blockPos, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        int n = callbackInfoReturnable.getReturnValueI();
        if (lightType == LightType.BLOCK) {
            n = DynamicLightEngine.internalMethod01092(blockPos, n);
        }
        if (GhostBlockRenderer.internalMethod00945(blockPos)) {
            n = GhostBlockRenderer.internalMethod05834(lightType, n);
        }
        callbackInfoReturnable.setReturnValue(n);
    }
}

