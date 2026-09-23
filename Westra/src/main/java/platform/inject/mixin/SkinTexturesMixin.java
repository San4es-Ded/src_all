package platform.inject.mixin;

import aethereal.core.Westra;
import aethereal.module.misc.StreamerMode;
import net.minecraft.class_2960;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_8685.class})
public class SkinTexturesMixin {
   @Inject(
      method = {"comp_1626"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void texture(CallbackInfoReturnable<class_2960> cir) {
      StreamerMode streamerMode = Westra.h().d().t().aE();
      if (streamerMode.m() && streamerMode.q().c()) {
         cir.setReturnValue(class_2960.method_60655("westra", "pictures/skin.png"));
      }
   }
}
