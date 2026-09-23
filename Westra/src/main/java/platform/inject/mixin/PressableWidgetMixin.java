package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.module.render.Buttons;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_4264;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4264.class})
public class PressableWidgetMixin implements Interface {
   @Inject(
      method = {"method_48579"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderWidget(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (Westra.h() != null) {
         Buttons buttons = Westra.h().d().t().bo();
         if (buttons != null && buttons.m()) {
            buttons.a(context, (class_339)this, mouseX, mouseY, delta);
            ci.cancel();
         }
      }
   }
}
