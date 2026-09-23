package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.PushEvent;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2784;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_2784.class})
public class WorldBorderMixin {
   @Inject(
      method = {"method_17903"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void asVoxelShape(CallbackInfoReturnable<class_265> cir) {
      PushEvent event = new PushEvent(PushEvent.a.WORLD_BORDER);
      EventManager.a((IEvent)event);
      if (event.a()) {
         cir.setReturnValue(class_259.method_1073());
      }
   }
}
