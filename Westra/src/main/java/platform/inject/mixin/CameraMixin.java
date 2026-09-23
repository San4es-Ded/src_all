package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Westra;
import aethereal.event.CameraPositionEvent;
import aethereal.event.RemovalsEvent;
import aethereal.event.RotationEvent;
import aethereal.render.Animations;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_1297;
import net.minecraft.class_1922;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_5636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import platform.inject.accessors.CameraAccessor;

@Mixin({class_4184.class})
public abstract class CameraMixin {
   @ModifyReturnValue(
      method = {"method_19333"},
      at = {@At("RETURN")}
   )
   private boolean isThirdPerson(boolean original) {
      return Westra.h().d().t().h().m() ? true : original;
   }

   @Inject(
      method = {"method_19321"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4184;method_19327(DDD)V",
         shift = Shift.AFTER
      )}
   )
   private void onUpdate(class_1922 area, class_1297 focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
      class_4184 camera = (class_4184)this;
      CameraPositionEvent posEvent = new CameraPositionEvent(camera.method_19326());
      EventManager.a((IEvent)posEvent);
      if (posEvent.a() && posEvent.b() != null) {
         class_243 pos = posEvent.b();
         ((CameraAccessor)this).invokeSetPos(pos.method_10216(), pos.method_10214(), pos.method_10215());
      }

      RotationEvent event = new RotationEvent(focusedEntity.method_5705(tickDelta), focusedEntity.method_5695(tickDelta));
      EventManager.a((IEvent)event);
      ((CameraAccessor)this).invokeSetRotation(event.a, event.b);
   }

   @Inject(
      method = {"method_19334"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getSubmergedFluidState(CallbackInfoReturnable<class_5636> ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.WATER);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.setReturnValue(class_5636.field_27888);
      }
   }

   @Inject(
      method = {"method_19318"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onClipToSpace(float desiredCameraDistance, CallbackInfoReturnable<Float> info) {
      Animations animations = Westra.h().d().t().Q();
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.CLIP);
      EventManager.a((IEvent)event);
      if (animations.m()) {
         desiredCameraDistance *= animations.u().c();
      }

      if (event.a() || animations.m()) {
         info.setReturnValue(desiredCameraDistance);
      }
   }
}
