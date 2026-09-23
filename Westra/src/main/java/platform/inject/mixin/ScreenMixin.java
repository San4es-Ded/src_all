package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.TooltipEvent;
import java.util.List;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_434;
import net.minecraft.class_437;
import net.minecraft.class_8671;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_437.class})
public class ScreenMixin {
   @Inject(
      method = {"method_25394"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      class_437 self = (class_437)this;
      if ((self instanceof class_8671 || self instanceof class_434) && Westra.h().d().t().aN().m()) {
         if (self instanceof class_434) {
            Interface.aM_.method_1507((class_437)null);
         }

         ci.cancel();
      }
   }

   @Inject(
      method = {"method_25408"},
      at = {@At("RETURN")}
   )
   private static void getTooltipFromItem(class_310 client, class_1799 stack, CallbackInfoReturnable<List<class_2561>> cir) {
      EventManager.a((IEvent)(new TooltipEvent(stack, (List<class_2561>)cir.getReturnValue())));
   }
}
