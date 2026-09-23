package wtf.wyvern.mixin.minecraft.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.wyvern.client.modules.impl.movement.GrimGlide;
import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.events.impl.player.EventOnTravelPost;

@Mixin({LivingEntity.class})
public class LivingEntityMixin {
   @Inject(method = "calcGlidingVelocity", at = @At(value = "RETURN"), cancellable = true)
   private void onsetvelocity(Vec3d oldVelocity, CallbackInfoReturnable<Vec3d> cir) {
      if (!GrimGlide.INSTANCE.isEnabled()) {
         return;
      }
      EventOnTravelPost event = new EventOnTravelPost(
              oldVelocity.multiply(0.9900000095367432, 0.9800000190734863, 0.9900000095367432));
      EventManager.call(event);
      cir.setReturnValue(event.getOldVelocity());
   }
}
