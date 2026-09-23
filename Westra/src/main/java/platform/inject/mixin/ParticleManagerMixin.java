package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Westra;
import aethereal.event.RemovalsEvent;
import java.util.Queue;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_2680;
import net.minecraft.class_702;
import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import platform.inject.accessors.ParticleManagerAccessor;

@Mixin({class_702.class})
public abstract class ParticleManagerMixin {
   @Inject(
      method = {"method_3046"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddBlockBreakParticles(class_2338 blockPos, class_2680 state, CallbackInfo info) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.BREAK_PARTICLES);
      EventManager.a((IEvent)event);
      if (event.a()) {
         info.cancel();
      }
   }

   @Inject(
      method = {"method_3054"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddBlockBreakingParticles(class_2338 blockPos, class_2350 direction, CallbackInfo info) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.BREAK_PARTICLES);
      EventManager.a((IEvent)event);
      if (event.a()) {
         info.cancel();
      }
   }

   @Inject(
      method = {"method_3056*", "method_3058*"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddParticle(
      class_2394 parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<class_703> cir
   ) {
      int limit = Westra.h().d().t().bi().t();
      if (limit > 0) {
         int total = 0;

         for (Queue<class_703> queue : ((ParticleManagerAccessor)this).getParticles().values()) {
            total += queue.size();
         }

         if (total >= limit) {
            cir.cancel();
            return;
         }
      }

      if (parameters.method_10295() == class_2398.field_11242) {
         RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.WEATHER);
         EventManager.a((IEvent)event);
         if (event.a()) {
            cir.cancel();
         }
      }
   }
}
