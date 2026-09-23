package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.AmbienceEvent;
import aethereal.event.RemovalsEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_4184;
import net.minecraft.class_758;
import net.minecraft.class_9958;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_758.class})
public class BackgroundRendererMixin {
   @Inject(
      method = {"method_42588(Lnet/minecraft/class_1297;F)Lnet/minecraft/class_758$class_7286;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(class_1297 entity, float tickDelta, CallbackInfoReturnable<Object> info) {
      if (entity instanceof class_1309 living) {
         if (living.method_6059(class_1294.field_5919)) {
            RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.BLINDNESS);
            EventManager.a((IEvent)event);
            if (event.a()) {
               info.setReturnValue(null);
               return;
            }
         }

         if (living.method_6059(class_1294.field_38092)) {
            RemovalsEvent event2 = new RemovalsEvent(RemovalsEvent.a.DARKNESS);
            EventManager.a((IEvent)event2);
            if (event2.a()) {
               info.setReturnValue(null);
            }
         }
      }
   }

   @ModifyReturnValue(
      method = {"method_3211"},
      at = {@At("RETURN")}
   )
   private static class_9958 onApplyFog(class_9958 original, @Local(argsOnly = true) class_4184 camera, @Local(argsOnly = true,ordinal = 0) float viewDistance) {
      AmbienceEvent.b event = new AmbienceEvent.b(camera, viewDistance, original);
      EventManager.a((IEvent)event);
      return event.d();
   }
}
