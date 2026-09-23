package moscow.rockstar.mixin.minecraft.render.block.entity;


import rockstar.client.internal.render.*;
import java.util.Map;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.block.entity.LoadedBlockEntityModels;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.internal.render.EspGlowCaptureRenderer;

@Mixin({LoadedBlockEntityModels.class})
public abstract class LoadedBlockEntityModelsMixin {
   @Shadow
   @Final
   private Map<Block, SpecialModelRenderer<?>> renderers;

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void rockstar$renderHeldSkullWithProfile(
      Block localValue1, ItemDisplayContext localValue2, MatrixStack localValue3, OrderedRenderCommandQueue localValue4, int localValue5, int localValue6, int localValue7, CallbackInfo callbackInfo
   ) {
      if (EspGlowCaptureRenderer.internalField0276 && localValue1 instanceof AbstractSkullBlock) {
         ProfileComponent localValue8 = rockstar$heldProfile(localValue1);
         if (localValue8 != null) {
            SpecialModelRenderer localValue9 = this.renderers.get(localValue1);
            if (localValue9 != null) {
               localValue9.render(localValue8, localValue2, localValue3, localValue4, localValue5, localValue6, false, localValue7);
               callbackInfo.cancel();
            }
         }
      }
   }

   @Unique
   private static ProfileComponent rockstar$heldProfile(Block localValue0) {
      ClientPlayerEntity localValue1 = MinecraftClient.getInstance().player;
      if (localValue1 == null) {
         return null;
      } else {
         ProfileComponent localValue2 = rockstar$profileOf(localValue1.getMainHandStack(), localValue0);
         return localValue2 != null ? localValue2 : rockstar$profileOf(localValue1.getOffHandStack(), localValue0);
      }
   }

   @Unique
   private static ProfileComponent rockstar$profileOf(ItemStack localValue0, Block localValue1) {
      return localValue0 != null && !localValue0.isEmpty() && Block.getBlockFromItem(localValue0.getItem()) == localValue1 ? (ProfileComponent)localValue0.get(DataComponentTypes.PROFILE) : null;
   }
}
