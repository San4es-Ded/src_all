package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.AmbienceEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_638.class_5271;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_5271.class})
public class ClientWorldPropertiesMixin {
   @ModifyReturnValue(
      method = {"method_217"},
      at = {@At("RETURN")}
   )
   private long getTimeOfDay(long original) {
      AmbienceEvent.c event = new AmbienceEvent.c(original);
      EventManager.a((IEvent)event);
      return event.b();
   }
}
