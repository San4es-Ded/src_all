package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DropItemEvent;
import aethereal.event.InputEvent;
import aethereal.event.MotionEvent;
import aethereal.event.MoveEvent;
import aethereal.event.PushEvent;
import aethereal.event.SlowEvent;
import aethereal.event.TickEvent;
import aethereal.module.movement.NoCrouch;
import aethereal.util.Look;
import aethereal.util.MoveUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_10185;
import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_746.class})
public abstract class ClientPlayerEntityMixin {
   @Inject(
      method = {"method_5773"},
      at = {@At("HEAD")}
   )
   private void tick(CallbackInfo ci) {
      EventManager.a((IEvent)(new TickEvent()));
   }

   @Redirect(
      method = {"method_6007"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_744;method_3129()V"
      )
   )
   private void tickMovement(class_744 input) {
      input.method_3129();
      InputEvent event = new InputEvent(input.field_3905, input.field_3907, input.field_54155.comp_3163(), input.field_54155.comp_3164());
      EventManager.a((IEvent)event);
      MoveUtil.a(event);
      input.field_3905 = event.b();
      input.field_3907 = event.c();
      input.field_54155 = new class_10185(
         event.b() > 0.0F, event.b() < 0.0F, event.c() > 0.0F, event.c() < 0.0F, event.d(), event.e(), input.field_54155.comp_3165()
      );
   }

   @ModifyReturnValue(
      method = {"method_20303"},
      at = {@At("RETURN")}
   )
   private boolean westraShouldSlowDown(boolean original) {
      if (original && Westra.h() != null) {
         NoCrouch module = Westra.h().d().t().n();
         return module != null && module.m() && module.q() ? false : original;
      } else {
         return original;
      }
   }

   @WrapOperation(
      method = {"method_5784"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_742;method_5784(Lnet/minecraft/class_1313;Lnet/minecraft/class_243;)V"
      )}
   )
   private void westraMove(class_746 player, class_1313 type, class_243 movement, Operation<Void> original) {
      if (type == class_1313.field_6308) {
         MoveEvent event = new MoveEvent(movement.field_1352, movement.field_1351, movement.field_1350);
         EventManager.a((IEvent)event);
         if (event.e()) {
            movement = new class_243(event.b(), event.c(), event.d());
         }
      }

      original.call(new Object[]{player, type, movement});
   }

   @ModifyExpressionValue(
      method = {"method_3136"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_24828()Z"
      )},
      require = 0
   )
   private boolean westraGroundSpoof(boolean original) {
      return MoveEvent.groundOverride != null ? MoveEvent.groundOverride : original;
   }

   @Inject(
      method = {"method_3136"},
      at = {@At("TAIL")}
   )
   private void westraResetGroundSpoof(CallbackInfo ci) {
      MoveEvent.groundOverride = null;
   }

   @Inject(
      method = {"method_3136"},
      at = {@At("HEAD")}
   )
   private void onSendMovementPackets(CallbackInfo ci) {
      class_746 player = (class_746)this;
      EventManager.a(
         (IEvent)(new MotionEvent(
            player.method_23317(),
            player.method_23318(),
            player.method_23321(),
            player.method_36454(),
            player.method_36455(),
            player.method_24828(),
            player.method_5715(),
            player.method_5624()
         ))
      );
   }

   @Redirect(
      method = {"method_6023"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_36455()F"
      )
   )
   private float pitchAi(class_746 instance) {
      return Look.c();
   }

   @Redirect(
      method = {"method_6023"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_36454()F"
      )
   )
   private float yawAi(class_746 instance) {
      return Look.b();
   }

   @Inject(
      method = {"method_7290"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onDropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
      DropItemEvent dropItemEvent = new DropItemEvent(Interface.aM_.field_1724.method_31548().field_7545);
      EventManager.a((IEvent)dropItemEvent);
      if (dropItemEvent.a()) {
         cir.cancel();
      }
   }

   @Redirect(
      method = {"method_6007"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_6115()Z"
      ),
      require = 0
   )
   private boolean onTickMovement(class_746 player) {
      if (!player.method_6115()) {
         return player.method_6115() && player.method_5854() == null;
      } else {
         SlowEvent slowEvent = new SlowEvent();
         EventManager.a((IEvent)slowEvent);
         return player.method_6115() && player.method_5854() == null && !slowEvent.a();
      }
   }

   @Inject(
      method = {"method_30673"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void removePushOutFromBlocks(double x, double z, CallbackInfo ci) {
      PushEvent event = new PushEvent(PushEvent.a.BLOCKS);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }
}
