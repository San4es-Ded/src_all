package wtf.wyvern.mixin.client;

import wtf.wyvern.core.eventbus.EventManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.wyvern.core.events.impl.player.EventOnMovePost;
import wtf.wyvern.client.modules.impl.player.NoPush;

@Mixin({Entity.class})
public class EntityMixin {
   @ModifyExpressionValue(
      method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;isControlledByPlayer()Z"
      )
   )
   public boolean fixFalldistanceValue(boolean original) {
      return (Object)this == MinecraftClient.getInstance().player ? false : original;
   }

   @Inject(
      method = {"pushAwayFrom"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void pushAwayFrom(CallbackInfo ci) {
      if ((Object)this == MinecraftClient.getInstance().player && NoPush.INSTANCE.isEnabled() && NoPush.INSTANCE.getObjects().isEnable("Игроки")) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"isPushedByFluids"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void isPushedByFluids(CallbackInfoReturnable<Boolean> cir) {
      if ((Object)this == MinecraftClient.getInstance().player && NoPush.INSTANCE.isEnabled() && NoPush.INSTANCE.getObjects().isEnable("Вода")) {
         cir.setReturnValue(false);
      }

   }

   @Inject(
      method = {"updateVelocity"},
      at = {@At("TAIL")}
   )
   private void wyvern$updateVelocity(float speed, Vec3d movementInput, CallbackInfo ci) {
      Entity me = (Entity)(Object)this;
      MinecraftClient client = MinecraftClient.getInstance();
      if (client.player != null && me.getId() == client.player.getId()) {
         EventManager.call(new EventOnMovePost(speed, movementInput));
      }
   }

}
