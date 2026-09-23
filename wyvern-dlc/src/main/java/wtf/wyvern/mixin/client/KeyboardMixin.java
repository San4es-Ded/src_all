package wtf.wyvern.mixin.client;

import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.lwjgl.glfw.GLFW;
import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.client.modules.impl.render.Sonar;

@Mixin({Keyboard.class})
public class KeyboardMixin {
   @Inject(
           method = {"onKey"},
           at = {@At("HEAD")}
   )
   public void triggerKeyEvent(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
      if (key != -1) {
         EventManager.call(new EventKey(action, key));
      }
   }

   @Inject(method = "processF3", at = @At("RETURN"))
   private void wyvern$restartSonarOnChunkReload(int key, CallbackInfoReturnable<Boolean> cir) {
      if (key == GLFW.GLFW_KEY_A && cir.getReturnValue()) {
         Sonar.INSTANCE.pingCurrentPosition();
      }
   }
}
