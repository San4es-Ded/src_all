package ru.pulse.mixin;

import java.util.List;
import net.minecraft.world.BlockRenderView;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import pulse.modules.utilities.Optimization;

@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin {
   @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
   private VertexConsumer pulse$leafAlpha(VertexConsumer original, BlockRenderView world, List<?> parts, BlockState state) {
      Optimization opt = Optimization.INSTANCE;
      return opt != null && opt.l() && opt.simpleLeaves.get() && state.getBlock() instanceof LeavesBlock
         ? new BlockModelRendererMixin.AlphaConsumer(original, 38)
         : original;
   }

   private record AlphaConsumer(VertexConsumer delegate, int alpha) implements VertexConsumer {
      public VertexConsumer vertex(float x, float y, float z) {
         this.delegate.vertex(x, y, z);
         return this;
      }

      public VertexConsumer color(int r, int g, int b, int a) {
         this.delegate.color(r, g, b, Math.min(a, this.alpha));
         return this;
      }

      public VertexConsumer color(int color) {
         return this.color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >>> 24 & 0xFF);
      }

      public VertexConsumer texture(float u, float v) {
         this.delegate.texture(u, v);
         return this;
      }

      public VertexConsumer overlay(int u, int v) {
         this.delegate.overlay(u, v);
         return this;
      }

      public VertexConsumer light(int u, int v) {
         this.delegate.light(u, v);
         return this;
      }

      public VertexConsumer normal(float x, float y, float z) {
         this.delegate.normal(x, y, z);
         return this;
      }

      public VertexConsumer lineWidth(float width) {
         this.delegate.lineWidth(width);
         return this;
      }
   }
}
