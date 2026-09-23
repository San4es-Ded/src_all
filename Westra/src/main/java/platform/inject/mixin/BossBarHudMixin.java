package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.RemovalsEvent;
import net.minecraft.class_337;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_337.class})
public class BossBarHudMixin {
   @Inject(
      method = {"method_1796"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render(CallbackInfo ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.BOSS_BAR);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }
}
