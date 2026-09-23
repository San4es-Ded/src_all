package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.HotbarEvent;
import aethereal.event.SyncEvent;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1661.class})
public abstract class PlayerInventoryMixin {
   @Inject(
      method = {"method_61496"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSetSelectedSlot(int slot, CallbackInfo ci) {
      HotbarEvent event = new HotbarEvent(slot);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_5447"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSetStack(int slot, class_1799 stack, CallbackInfo ci) {
      SyncEvent event = new SyncEvent(slot, stack);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }
}
