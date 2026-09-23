package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_761.class})
public class CloudRenderMixin implements Interface {
   @Inject(
      method = {"method_62204"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderClouds(CallbackInfo ci) {
      if (Westra.h().d().t().aF().q()) {
         ci.cancel();
      }
   }
}
