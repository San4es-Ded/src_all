package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.event.PushEvent;
import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1657.class})
public class PlayerEntityMixin {
   @Inject(
      method = {"method_5675"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void removePushFromFluids(CallbackInfoReturnable<Boolean> cir) {
      if ((class_1657)this == Interface.aM_.field_1724) {
         PushEvent event = new PushEvent(PushEvent.a.FLUIDS);
         EventManager.a((IEvent)event);
         if (event.a()) {
            cir.setReturnValue(false);
         }
      }
   }
}
