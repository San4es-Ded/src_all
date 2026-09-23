package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.event.KeyEvent;
import aethereal.ui.screen.AssistantScreen;
import aethereal.ui.screen.StationScreen;
import aethereal.ui.screen.SwapScreen;
import net.minecraft.class_309;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_309.class})
public class KeyboardMixin {
   @Inject(
      method = {"method_1466"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onKey(long window, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
      if (Interface.aM_.field_1755 == null
         || Interface.aM_.field_1755 instanceof SwapScreen
         || Interface.aM_.field_1755 instanceof AssistantScreen
         || Interface.aM_.field_1755 instanceof StationScreen
         || Interface.aM_.field_1755 instanceof class_465) {
         KeyEvent event = new KeyEvent(key, scanCode, action, modifiers);
         EventManager.a((IEvent)event);
         if (event.a()) {
            ci.cancel();
         }
      }
   }
}
