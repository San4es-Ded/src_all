package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.ChatSendEvent;
import aethereal.event.CooldownEvent;
import aethereal.event.SyncEvent;
import aethereal.mixin.IStatusEffectInstance;
import aethereal.render.AnimationUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_10295;
import net.minecraft.class_1293;
import net.minecraft.class_1309;
import net.minecraft.class_1792;
import net.minecraft.class_2653;
import net.minecraft.class_2656;
import net.minecraft.class_2783;
import net.minecraft.class_368;
import net.minecraft.class_374;
import net.minecraft.class_634;
import net.minecraft.class_7923;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public abstract class ClientPlayNetworkHandlerMixin implements Interface {
   @Inject(
      method = {"method_45729"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void sendChatMessage(String content, CallbackInfo ci) {
      ChatSendEvent event = new ChatSendEvent(content);
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      } else {
         if (!Westra.h().d().t().am().m()) {
            Westra.h().d().u().a(content, ci);
         }
      }
   }

   @Redirect(
      method = {"method_11120"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_374;method_1999(Lnet/minecraft/class_368;)V"
      )
   )
   private void onGameJoin(class_374 manager, class_368 toast) {
   }

   @Redirect(
      method = {"method_64555"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_366;method_1985(Lnet/minecraft/class_374;Lnet/minecraft/class_10295;)V"
      )
   )
   private void onRecipeBookAdd(class_374 manager, class_10295 display) {
   }

   @Inject(
      method = {"method_11087"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onCooldownUpdate(class_2656 packet, CallbackInfo ci) {
      CooldownEvent event = new CooldownEvent((class_1792)class_7923.field_41178.method_63535(packet.comp_3082()), packet.comp_2199());
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"method_11109"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onScreenHandlerSlotUpdate(class_2653 packet, CallbackInfo ci) {
      SyncEvent event = new SyncEvent(packet.method_11450(), packet.method_11449());
      EventManager.a((IEvent)event);
      if (event.a()) {
         ci.cancel();
      }
   }

   @ModifyArg(
      method = {"method_11084"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1309;method_26082(Lnet/minecraft/class_1293;Lnet/minecraft/class_1297;)V"
      ),
      index = 0
   )
   private class_1293 onEntityStatusEffect(class_1293 newEffect, @Local(argsOnly = true) class_2783 packet) {
      if ((aM_.field_1687 == null ? null : aM_.field_1687.method_8469(packet.method_11943())) instanceof class_1309 living) {
         IStatusEffectInstance iStatusEffectInstanceMethod_6112 = (IStatusEffectInstance)living.method_6112(newEffect.method_5579());
         if (iStatusEffectInstanceMethod_6112 != null) {
            ((IStatusEffectInstance)newEffect).setInitialDuration(iStatusEffectInstanceMethod_6112.getInitialDuration());
            AnimationUtil from = iStatusEffectInstanceMethod_6112.getAnimation();
            AnimationUtil to = ((IStatusEffectInstance)newEffect).getAnimation();
            to.c(from.a());
            to.d(from.b());
         }
      }

      return newEffect;
   }
}
