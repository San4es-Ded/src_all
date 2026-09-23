package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.emotion.EmotionPlayback;
import aethereal.mixin.IEmotionState;
import net.minecraft.class_10034;
import net.minecraft.class_10055;
import net.minecraft.class_572;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_572.class})
public abstract class BipedEntityModelMixin implements Interface {
   @Inject(
      method = {"method_17087(Lnet/minecraft/class_10034;)V"},
      at = {@At("TAIL")},
      require = 0
   )
   private void applyEmotion(class_10034 state, CallbackInfo ci) {
      if (state instanceof class_10055 && aM_.field_1724 != null) {
         if (Westra.h().d().t().cu().m() && EmotionPlayback.b()) {
            if (((IEmotionState)state).getSelfEmotion()) {
               EmotionPlayback.a((class_572<?>)this);
            }
         }
      }
   }
}
