package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.mixin.ITransparentState;
import aethereal.module.render.HitColor;
import aethereal.render.ColorUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_10042;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_922.class})
public abstract class LivingEntityRendererMixin<T extends class_1309, S extends class_10042, M extends class_583<? super S>> {
   @Unique
   private T currentEntity;
   @Unique
   private float pitch;
   @Unique
   private class_10042 westraRenderState;

   @Inject(
      method = {"method_62354*", "method_62355*"},
      at = {@At("HEAD")}
   )
   private void onUpdateRenderState(T entity, S state, float f, CallbackInfo ci) {
      this.currentEntity = entity;
   }

   @Inject(
      method = {"method_62354*", "method_62355*"},
      at = {@At("TAIL")}
   )
   private void updateRenderState(T entity, S state, float f, CallbackInfo ci) {
      if (entity == Interface.aM_.field_1724) {
         this.pitch = Westra.h().d().k().a().a() ? class_3532.method_16439(0.5F, this.pitch, state.field_53448) : state.field_53448;
         state.field_53448 = this.pitch;
      }
   }

   @Inject(
      method = {"method_62354*", "method_62355*"},
      at = {@At("TAIL")}
   )
   private void westraTransparentState(T entity, S state, float f, CallbackInfo ci) {
      ((ITransparentState)state).setWestraAlpha(Westra.h().d().t().cC().a(entity, f));
   }

   @Inject(
      method = {"method_4054(Lnet/minecraft/class_10042;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = {@At("HEAD")}
   )
   private void westraCaptureState(S state, class_4587 matrices, class_4597 consumers, int light, CallbackInfo ci) {
      this.westraRenderState = state;
   }

   @ModifyArg(
      method = {"method_4054(Lnet/minecraft/class_10042;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_922;method_24302(Lnet/minecraft/class_10042;ZZZ)Lnet/minecraft/class_1921;"
      ),
      index = 2
   )
   private boolean westraTranslucentLayer(boolean translucent) {
      return translucent || this.westraRenderState != null && ((ITransparentState)this.westraRenderState).getWestraAlpha() < 1.0F;
   }

   @ModifyArg(
      method = {"method_4054(Lnet/minecraft/class_10042;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_583;method_62100(Lnet/minecraft/class_4587;Lnet/minecraft/class_4588;III)V"
      ),
      index = 4
   )
   private int westraTranslucentColor(int color) {
      if (this.westraRenderState == null) {
         return color;
      } else {
         float alpha = ((ITransparentState)this.westraRenderState).getWestraAlpha();
         if (alpha >= 1.0F) {
            return color;
         } else {
            int originalAlpha = color >>> 24 & 0xFF;
            return (int)(originalAlpha * alpha) << 24 | color & 16777215;
         }
      }
   }

   @Inject(
      method = {"method_4056"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onIsVisible(S state, CallbackInfoReturnable<Boolean> cir) {
      if (Westra.h().d().t().T().m() && state.field_53333 && this.currentEntity instanceof class_1657) {
         cir.setReturnValue(true);
      }
   }

   @ModifyReturnValue(
      method = {"method_62484"},
      at = {@At("RETURN")}
   )
   private int modifyMixColor(int original, S state) {
      HitColor hitColor = Westra.h().d().t().br();
      if (hitColor != null && hitColor.m() && original != 0 && state.field_53460) {
         return hitColor.q();
      } else {
         return Westra.h().d().t().T().m() && state.field_53333 && this.currentEntity instanceof class_1657
            ? ColorUtil.a(original, Westra.h().d().t().T().q())
            : original;
      }
   }
}
