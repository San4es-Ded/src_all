package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.ConsumeEvent;
import aethereal.event.JumpEvent;
import aethereal.event.PushEvent;
import aethereal.event.WillLandEvent;
import aethereal.mixin.ILivingEntity;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import platform.inject.accessors.LivingEntityGravityInvoker;
import platform.inject.invokers.EntityMovementInvoker;

@Mixin({class_1309.class})
public abstract class LivingEntityMixin implements ILivingEntity {
   @Inject(
      method = {"method_6040"},
      at = {@At("HEAD")}
   )
   private void consumeItem(CallbackInfo ci) {
      class_1309 livingEntity = (class_1309)this;
      if (livingEntity instanceof class_746 && livingEntity.method_6115()) {
         EventManager.a((IEvent)(new ConsumeEvent(livingEntity.method_6030().method_7972())));
      }
   }

   @Inject(
      method = {"method_6043"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void jump(CallbackInfo ci) {
      class_1309 livingEntity = (class_1309)this;
      if (livingEntity instanceof class_746) {
         JumpEvent event = new JumpEvent(livingEntity);
         EventManager.a((IEvent)event);
         if (event.a()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"method_5810"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void removePushFromEntity(CallbackInfoReturnable<Boolean> cir) {
      class_1309 entity = (class_1309)this;
      if (entity instanceof class_746) {
         PushEvent event = new PushEvent(PushEvent.a.ENTITIES);
         EventManager.a((IEvent)event);
         if (event.a()) {
            cir.setReturnValue(false);
         }
      }
   }

   @Inject(
      method = {"method_61428"},
      at = {@At("TAIL")}
   )
   private void travelMidAir(class_243 movementInput, CallbackInfo ci) {
      EntityMovementInvoker entityMovementInvoker = (EntityMovementInvoker)this;
      if (entityMovementInvoker instanceof class_746) {
         double gravity = ((LivingEntityGravityInvoker)this).getGravityInvoker();
         double predictedNextYDelta = (((class_1297)this).method_18798().field_1351 - gravity) * 0.98;
         class_243 verticalAttempt = new class_243(0.0, predictedNextYDelta, 0.0);
         class_243 allowedVertical = entityMovementInvoker.getAdjustMovementForCollisions(verticalAttempt);
         boolean willLand = predictedNextYDelta < 0.0 && allowedVertical.field_1351 != predictedNextYDelta;
         EventManager.a((IEvent)(new WillLandEvent(willLand)));
      }
   }
}
