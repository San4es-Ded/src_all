package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.PushEvent;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1536.class})
public class FishingBobberEntityMixin {
   @Inject(
      method = {"method_6954"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void pullHookedEntity(class_1297 entity, CallbackInfo ci) {
      if (entity instanceof class_746) {
         PushEvent event = new PushEvent(PushEvent.a.FISHING_HOOK);
         EventManager.a((IEvent)event);
         if (event.a()) {
            ci.cancel();
         }
      }
   }
}
