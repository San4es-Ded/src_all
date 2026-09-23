package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Westra;
import aethereal.event.HandAnimationEvent;
import aethereal.event.HandViewEvent;
import aethereal.util.Look;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1799;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_759.class})
public class HeldItemRendererMixin {
   @Redirect(
      method = {"method_22976(FLnet/minecraft/class_4587;Lnet/minecraft/class_4597$class_4598;Lnet/minecraft/class_746;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_5695(F)F"
      )
   )
   private float redirectPitch(class_746 instance, float tickDelta) {
      return Look.c();
   }

   @Redirect(
      method = {"method_22976(FLnet/minecraft/class_4587;Lnet/minecraft/class_4597$class_4598;Lnet/minecraft/class_746;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_746;method_5705(F)F"
      )
   )
   private float redirectYaw(class_746 instance, float tickDelta) {
      return Look.b();
   }

   @Redirect(
      method = {"method_22976(FLnet/minecraft/class_4587;Lnet/minecraft/class_4597$class_4598;Lnet/minecraft/class_746;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_3532;method_16439(FFF)F",
         ordinal = 0
      )
   )
   private float redirectPitchLerp(float delta, float start, float end) {
      return Look.c();
   }

   @Redirect(
      method = {"method_22976(FLnet/minecraft/class_4587;Lnet/minecraft/class_4597$class_4598;Lnet/minecraft/class_746;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_3532;method_16439(FFF)F",
         ordinal = 1
      )
   )
   private float redirectYawLerp(float delta, float start, float end) {
      return Look.b();
   }

   @WrapOperation(
      method = {"method_3228"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_759;method_65816(FFLnet/minecraft/class_4587;ILnet/minecraft/class_1306;)V",
         ordinal = 2
      )}
   )
   private void wrapHandAnimation(
      class_759 instance,
      float swingProgress,
      float equipProgress,
      class_4587 matrices,
      int armX,
      class_1306 arm,
      Operation<Void> original,
      @Local(ordinal = 0,argsOnly = true) class_1268 hand
   ) {
      HandAnimationEvent event = new HandAnimationEvent(matrices, hand, swingProgress, armX);
      EventManager.a((IEvent)event);
      if (!event.a()) {
         original.call(new Object[]{instance, swingProgress, equipProgress, matrices, armX, arm});
      }
   }

   @Inject(
      method = {"method_3228"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4587;method_22903()V",
         shift = Shift.AFTER
      )}
   )
   private void onRenderFirstPersonItem(
      class_742 player,
      float tickDelta,
      float pitch,
      class_1268 hand,
      float swingProgress,
      class_1799 stack,
      float equipProgress,
      class_4587 matrices,
      class_4597 vertexConsumers,
      int light,
      CallbackInfo ci
   ) {
      EventManager.a((IEvent)(new HandViewEvent(matrices, stack, hand)));
   }

   @ModifyExpressionValue(
      method = {"method_3228"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_742;method_5767()Z"
      )}
   )
   private boolean renderFirstPersonItem(boolean original) {
      return Westra.h().d().t().T().m() ? false : original;
   }
}
