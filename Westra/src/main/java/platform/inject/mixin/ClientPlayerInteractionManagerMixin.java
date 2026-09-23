package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.AttackEvent;
import aethereal.module.misc.NoInteract;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1695;
import net.minecraft.class_1696;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2349;
import net.minecraft.class_2354;
import net.minecraft.class_2480;
import net.minecraft.class_2533;
import net.minecraft.class_3965;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_636.class})
public class ClientPlayerInteractionManagerMixin implements Interface {
   @Inject(
      method = {"method_2896"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void interactBlock(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
      NoInteract noInteract = Westra.h().d().t().s();
      class_2248 block = aM_.field_1687.method_8320(hitResult.method_17777()).method_26204();
      if (noInteract.m()) {
         if (noInteract.q().c() && !Westra.h().d().t().B().m()) {
            return;
         }

         if (block == class_2246.field_10034
            || block == class_2246.field_10380
            || block == class_2246.field_10181
            || block == class_2246.field_10535
            || block == class_2246.field_9980
            || block == class_2246.field_10312
            || block == class_2246.field_10223
            || block == class_2246.field_10179
            || block == class_2246.field_10443
            || block == class_2246.field_10200
            || block == class_2246.field_10228
            || block instanceof class_2480
            || block instanceof class_2354
            || block instanceof class_2349
            || block instanceof class_2533) {
            cir.setReturnValue(class_1269.field_5811);
         }
      }
   }

   @Inject(
      method = {"method_2905"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void interactEntity(class_1657 player, class_1297 entity, class_1268 hand, CallbackInfoReturnable<class_1269> cir) {
      NoInteract noInteract = Westra.h().d().t().s();
      if (noInteract.m()) {
         if (noInteract.q().c() && !Westra.h().d().t().B().m()) {
            return;
         }

         if (entity instanceof class_1695 || entity instanceof class_1696) {
            cir.setReturnValue(class_1269.field_5811);
         }
      }
   }

   @Inject(
      method = {"method_2918"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAttackEntity(class_1657 player, class_1297 target, CallbackInfo ci) {
      AttackEvent event = new AttackEvent(target);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }
}
