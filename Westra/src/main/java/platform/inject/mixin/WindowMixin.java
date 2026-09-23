package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.ResizeEvent;
import net.minecraft.class_1041;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1041.class})
public class WindowMixin {
   @Inject(
      method = {"method_4491"},
      at = {@At("TAIL")}
   )
   private void onSetIcon(CallbackInfo ci) {
      EventManager.a((IEvent)(new ResizeEvent()));
   }

   @Inject(
      method = {"method_4504"},
      at = {@At("TAIL")}
   )
   private void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
      EventManager.a((IEvent)(new ResizeEvent()));
   }
}
