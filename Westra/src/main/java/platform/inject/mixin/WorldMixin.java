package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.AmbienceEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_1937;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_1937.class})
public class WorldMixin {
   @ModifyReturnValue(
      method = {"method_8430"},
      at = {@At("RETURN")}
   )
   private float onGetRainGradient(float original) {
      AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.a.RAIN_GRADIENT, original);
      EventManager.a((IEvent)event);
      return event.c();
   }

   @ModifyReturnValue(
      method = {"method_8478"},
      at = {@At("RETURN")}
   )
   private float onGetThunderGradient(float original) {
      AmbienceEvent.d event = new AmbienceEvent.d(AmbienceEvent.d.a.THUNDER_GRADIENT, original);
      EventManager.a((IEvent)event);
      return event.c();
   }
}
