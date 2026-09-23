package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.mixin.IEmotionState;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_2561;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1007.class})
public abstract class PlayerEntityRendererMixin {
   @Inject(
      method = {"method_62604(Lnet/minecraft/class_742;Lnet/minecraft/class_10055;F)V"},
      at = {@At("TAIL")}
   )
   private void markSelfEmotion(class_742 player, class_10055 state, float tickDelta, CallbackInfo ci) {
      ((IEmotionState)state).setSelfEmotion(player == Interface.aM_.field_1724);
   }

   @Inject(
      method = {"method_4213(Lnet/minecraft/class_10055;Lnet/minecraft/class_2561;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderLabelIfPresent(class_10055 state, class_2561 text, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      if (Westra.h().d().t().aa().m()) {
         ci.cancel();
      }
   }
}
