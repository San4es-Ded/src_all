package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Westra;
import aethereal.event.RemovalsEvent;
import net.minecraft.class_1058;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4603.class})
public class InGameOverlayRendererMixin {
   @Inject(
      method = {"method_23070"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void renderFireOverlay(class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.FIRE);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_23068"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void renderInWallOverlay(class_1058 sprite, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.CLIP);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_23069"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void renderUnderwaterOverlay(class_310 client, class_4587 matrices, class_4597 provider, CallbackInfo ci) {
      if (Westra.h() != null && Westra.h().d().t().bq() != null && Westra.h().d().t().bq().q()) {
         ci.cancel();
      }
   }
}
