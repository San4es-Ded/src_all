package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.SoundEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_1102;
import net.minecraft.class_1113;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_1102.class})
public abstract class AbstractSoundInstanceMixin {
   @ModifyReturnValue(
      method = {"method_4781"},
      at = {@At("RETURN")}
   )
   private float getVolume(float original) {
      SoundEvent event = new SoundEvent((class_1113)this, original);
      EventManager.a((IEvent)event);
      return event.c();
   }
}
