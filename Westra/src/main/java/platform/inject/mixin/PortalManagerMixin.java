package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.PortalEvent;
import net.minecraft.class_9787;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_9787.class})
public abstract class PortalManagerMixin {
   @Inject(
      method = {"method_60709"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onIsInPortal(CallbackInfoReturnable<Boolean> cir) {
      PortalEvent event = new PortalEvent((Boolean)cir.getReturnValue());
      EventManager.a((IEvent)event);
      cir.setReturnValue(event.b());
   }
}
