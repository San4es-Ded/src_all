package wtf.wyvern.mixin.client.render;

import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.core.events.impl.other.EventWindowResize;

@Mixin({Window.class})
public class WindowMixin {
   @Inject(
      method = {"onWindowSizeChanged"},
      at = {@At("TAIL")}
   )
   private void onWindowSizeChanged(long window, int width, int height, CallbackInfo ci) {
      EventManager.call(new EventWindowResize());
   }
}