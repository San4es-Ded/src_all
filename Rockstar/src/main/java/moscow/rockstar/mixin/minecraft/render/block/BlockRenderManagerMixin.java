package moscow.rockstar.mixin.minecraft.render.block;



import rockstar.client.esp.*;
import rockstar.client.internal.render.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.modules.visual.ViewModelModule;
import rockstar.client.esp.ItemTargetType;
import rockstar.client.internal.render.EspGlowCaptureRenderer;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.FillEspFeature;
import rockstar.client.esp.FlameEspFeature;
import rockstar.client.esp.GlowEspFeature;

@Mixin({BlockRenderManager.class})
public abstract class BlockRenderManagerMixin {
   @Unique
   private static boolean rockstar$capturingHeldBlock;

   @Inject(
      method = {"renderBlockAsEntity"},
      at = {@At("HEAD")}
   )
   private void rockstar$decorateHeldBlock(BlockState localValue1, MatrixStack localValue2, VertexConsumerProvider localValue3, int localValue4, int localValue5, CallbackInfo localValue6) {
      if (!rockstar$capturingHeldBlock && EspGlowCaptureRenderer.internalField0276 && !EspGlowCaptureRenderer.internalField0277 && !ViewModelModule.internalField0277) {
         if (localValue1 != null && rockstar$isHeld(localValue1.getBlock())) {
            EspGlowCaptureRenderer.internalMethod02731(localValue1, localValue5, localValue2);
            GlowEspFeature localValue7 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
            FlameEspFeature localValue8 = EspManager.internalMethod06726().internalMethod05464(FlameEspFeature.class);
            FillEspFeature localValue9 = EspManager.internalMethod06726().internalMethod05464(FillEspFeature.class);
            boolean localValue10 = localValue7 != null && localValue7.internalMethod02927(ItemTargetType.internalField0012);
            boolean localValue11 = localValue8 != null && localValue8.internalMethod02927(ItemTargetType.internalField0012);
            boolean localValue12 = localValue9 != null && localValue9.internalMethod02927(ItemTargetType.internalField0012);
            if (localValue10 || localValue11 || localValue12) {
               if (localValue3 instanceof Immediate localValue13) {
                  localValue13.draw();
               }

               BlockRenderManager localValue17 = (BlockRenderManager)(Object)this;
               rockstar$capturingHeldBlock = true;

               try {
                  if (localValue10) {
                     localValue7.internalMethod05860(localValue17, localValue1, localValue2, localValue5);
                  }

                  if (localValue11) {
                     localValue8.internalMethod00240(localValue17, localValue1, localValue2, localValue5);
                  }

                  if (localValue12) {
                     localValue9.internalMethod06931(localValue17, localValue1, localValue2, localValue5);
                  }
               } finally {
                  rockstar$capturingHeldBlock = false;
               }
            }
         }
      }
   }

   @Unique
   private static boolean rockstar$isHeld(Block localValue0) {
      ClientPlayerEntity localValue1 = MinecraftClient.getInstance().player;
      return localValue1 == null
         ? false
         : Block.getBlockFromItem(localValue1.getMainHandStack().getItem()) == localValue0 || Block.getBlockFromItem(localValue1.getOffHandStack().getItem()) == localValue0;
   }
}
