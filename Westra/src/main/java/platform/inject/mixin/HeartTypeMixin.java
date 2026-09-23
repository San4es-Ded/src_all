package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.RemovalsEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(
   targets = {"net/minecraft/class_329$class_6411"}
)
public class HeartTypeMixin {
   @ModifyExpressionValue(
      method = {"method_37301"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1657;method_6059(Lnet/minecraft/class_6880;)Z",
         ordinal = 1
      )}
   )
   private static boolean fromPlayerState(boolean original) {
      if (!original) {
         return false;
      } else {
         RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.BLACK_HEARTS);
         EventManager.a((IEvent)event);
         return !event.a();
      }
   }
}
