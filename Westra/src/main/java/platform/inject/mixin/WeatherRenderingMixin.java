package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.AmbienceEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_9976;
import net.minecraft.class_1959.class_1963;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_9976.class})
public class WeatherRenderingMixin {
   @ModifyExpressionValue(
      method = {"method_62319"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_638;method_8430(F)F"
      )}
   )
   private float onPrecipitationParticles(float original) {
      AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.a.PRECIPITATION_PARTICLES, original);
      EventManager.a((IEvent)event);
      return event.c();
   }

   @ModifyReturnValue(
      method = {"method_62317"},
      at = {@At(
         value = "RETURN",
         ordinal = 1
      )}
   )
   private class_1963 onGetPrecipitationAt(class_1963 original, class_1937 world, class_2338 pos) {
      AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.a.PRECIPITATION, original);
      EventManager.a((IEvent)event);
      return event.d();
   }
}
