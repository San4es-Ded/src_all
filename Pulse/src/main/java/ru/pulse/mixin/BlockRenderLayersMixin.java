package ru.pulse.mixin;

import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BlockRenderLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pulse.modules.utilities.Optimization;

@Mixin(BlockRenderLayers.class)
public abstract class BlockRenderLayersMixin {
   @Inject(method = "getBlockLayer", at = @At("HEAD"), cancellable = true)
   private static void pulse$transparentLeaves(BlockState state, CallbackInfoReturnable<BlockRenderLayer> cir) {
      Optimization opt = Optimization.INSTANCE;
      if (opt != null && opt.l() && opt.simpleLeaves.get() && state.getBlock() instanceof LeavesBlock) {
         cir.setReturnValue(BlockRenderLayer.TRANSLUCENT);
      }
   }
}
