package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.BoundingBoxEvent;
import aethereal.event.RemovalsEvent;
import aethereal.module.render.ShaderESP;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_1297.class})
public abstract class EntityMixin {
   @Shadow
   private class_238 field_6005;

   @Inject(
      method = {"method_5829"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public final void getBoundingBox(CallbackInfoReturnable<class_238> cir) {
      BoundingBoxEvent event = new BoundingBoxEvent(this.field_6005, (class_1297)this);
      EventManager.a((IEvent)event);
      cir.setReturnValue(event.b());
   }

   @Inject(
      method = {"method_5851"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
      if (!(Boolean)cir.getReturnValue() && Westra.h() != null && Westra.h().d().t().ad().q((class_1297)this)) {
         cir.setReturnValue(true);
      } else {
         if ((Boolean)cir.getReturnValue()) {
            RemovalsEvent event = new RemovalsEvent(RemovalsEvent.a.GLOW);
            EventManager.a((IEvent)event);
            if (event.a()) {
               cir.setReturnValue(false);
            }
         }
      }
   }

   @Inject(
      method = {"method_22861"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void onTeamColor(CallbackInfoReturnable<Integer> cir) {
      class_1297 self = (class_1297)this;
      if (Westra.h() != null && Westra.h().d().t().ad().q(self)) {
         cir.setReturnValue(Westra.h().d().t().ad().s(self) & 16777215);
      }
   }

   @ModifyReturnValue(
      method = {"method_22861"},
      at = {@At("RETURN")}
   )
   private int westraTeamColor(int original) {
      ShaderESP shader = Westra.h() == null ? null : Westra.h().d().t().ad();
      return shader != null && shader.q((class_1297)this) ? shader.s((class_1297)this) & 16777215 : original;
   }

   @Inject(
      method = {"method_5700"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onBubbleColumnSurfaceCollision(boolean drag, CallbackInfo ci) {
      class_1297 self = (class_1297)this;
      if (self instanceof class_746 && Westra.h().d().t().aq().m()) {
         self.method_18800(self.method_18798().field_1352, Math.min(1.8, self.method_18798().field_1351 + 9.99999999E8), self.method_18798().field_1350);
         ci.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"method_5784"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1297;method_65038()Z"
      )}
   )
   private boolean move(boolean original) {
      return (class_1297)this == Interface.aM_.field_1724 ? false : original;
   }
}
