package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.ui.screen.AltScreen;
import aethereal.ui.screen.MainScreen;
import net.minecraft.class_9919;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_9919.class})
public class InactivityFpsLimiterMixin {
   @Inject(
      method = {"method_61937"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onUpdate(CallbackInfoReturnable<Integer> cir) {
      int limit = Westra.h().d().t().bi().s();
      boolean menu = Interface.aM_.field_1755 instanceof MainScreen || Interface.aM_.field_1755 instanceof AltScreen;
      if (limit <= 0 || !menu && Interface.aM_.method_1569()) {
         if (menu) {
            cir.setReturnValue(200);
         }
      } else {
         cir.setReturnValue(limit);
      }
   }
}
