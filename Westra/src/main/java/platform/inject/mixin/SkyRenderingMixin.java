package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.module.render.Atmosphere;
import net.minecraft.class_9975;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_9975.class})
public class SkyRenderingMixin implements Interface {
   @Inject(
      method = {"method_62302"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderSky(float red, float green, float blue, CallbackInfo ci) {
      Atmosphere atmosphere = Westra.h().d().t().bh();
      if (atmosphere.q()) {
         ci.cancel();
      }
   }
}
