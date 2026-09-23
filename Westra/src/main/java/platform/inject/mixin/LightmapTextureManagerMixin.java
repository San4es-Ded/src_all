package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.GammaEvent;
import aethereal.event.RemovalsEvent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_7172;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_765.class})
public class LightmapTextureManagerMixin {
   @ModifyReturnValue(
      method = {"method_42597"},
      at = {@At("RETURN")}
   )
   private float onGetDarknessFactor(float original) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.DARKNESS);
      EventManager.a((IEvent)event);
      return event.a() ? 0.0F : original;
   }

   @WrapOperation(
      method = {"method_3313"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_7172;method_41753()Ljava/lang/Object;"
      )}
   )
   private Object onGetGamma(class_7172<?> option, Operation<Object> original) {
      GammaEvent event = new GammaEvent(((Number)original.call(new Object[]{option})).doubleValue());
      EventManager.a((IEvent)event);
      return event.b();
   }
}
