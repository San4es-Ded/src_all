package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.event.CrosshairTargetEvent;
import aethereal.event.DrawEvent;
import aethereal.event.HandEvent;
import aethereal.event.RatioEvent;
import aethereal.event.RayTraceEvent;
import aethereal.event.RemovalsEvent;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.function.Predicate;
import net.minecraft.class_1297;
import net.minecraft.class_1675;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_757.class})
public class GameRendererMixin implements Interface {
   @Inject(
      method = {"method_3188"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/class_757;field_3992:Z",
         opcode = 180,
         ordinal = 0
      )}
   )
   public void renderWorld(class_9779 tickCounter, CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f) {
      class_4587 matrixStack = new class_4587();
      matrixStack.method_34425(matrix4f);
      EventManager.a((IEvent)(new DrawEvent(matrixStack, tickCounter.method_60637(false), DrawEvent.a.D3D)));
   }

   @Inject(
      method = {"method_3172"},
      at = {@At("HEAD")}
   )
   private void preRenderHand(class_4184 camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
      EventManager.a((IEvent)(new HandEvent(HandEvent.a.PRE)));
   }

   @Inject(
      method = {"method_3172"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4603;method_23067(Lnet/minecraft/class_310;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;)V"
      )}
   )
   private void postRenderHand(class_4184 camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
      EventManager.a((IEvent)(new HandEvent(HandEvent.a.POST)));
   }

   @Inject(
      method = {"method_3198"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tiltViewWhenHurt(class_4587 matrices, float tickDelta, CallbackInfo ci) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.HURT_CAM);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Redirect(
      method = {"method_3188"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_3532;method_16439(FFF)F"
      )
   )
   private float renderWorld(float delta, float first, float second) {
      RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.NAUSEA);
      EventManager.a((IEvent)event);
      return event.a() ? 0.0F : class_3532.method_16439(delta, first, second);
   }

   @Redirect(
      method = {"method_56153"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1675;method_18075(Lnet/minecraft/class_1297;Lnet/minecraft/class_243;Lnet/minecraft/class_243;Lnet/minecraft/class_238;Ljava/util/function/Predicate;D)Lnet/minecraft/class_3966;"
      )
   )
   private class_3966 findCrosshairTarget(class_1297 entity, class_243 min, class_243 max, class_238 box, Predicate<class_1297> predicate, double maxDistance) {
      RayTraceEvent event = new RayTraceEvent();
      EventManager.a((IEvent)event);
      return event.a() ? null : class_1675.method_18075(entity, min, max, box, predicate, maxDistance);
   }

   @Inject(
      method = {"method_56153"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onFindCrosshairTarget(
      class_1297 camera, double blockInteractionRange, double entityInteractionRange, float tickProgress, CallbackInfoReturnable<class_239> cir
   ) {
      CrosshairTargetEvent event = new CrosshairTargetEvent(tickProgress);
      EventManager.a((IEvent)event);
      if (event.a()) {
         cir.setReturnValue(event.c());
      }
   }

   @ModifyArg(
      method = {"method_22973"},
      at = @At(
         value = "INVOKE",
         target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;"
      ),
      index = 1
   )
   private float getBasicProjectionMatrix(float ratio) {
      RatioEvent event = new RatioEvent(ratio);
      EventManager.a((IEvent)event);
      return event.b();
   }
}
