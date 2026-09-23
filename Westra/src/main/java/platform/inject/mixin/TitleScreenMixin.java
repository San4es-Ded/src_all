package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.Interface;
import aethereal.ui.screen.MainScreen;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_442.class})
public abstract class TitleScreenMixin {
   @Inject(
      method = {"method_25426"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void init(CallbackInfo ci) {
      if (!EventManager.d() && !(Interface.aM_.field_1755 instanceof MainScreen)) {
         Interface.aM_.method_1507(new MainScreen());
         ci.cancel();
      }
   }
}
