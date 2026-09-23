package moscow.rockstar.mixin.minecraft.entity;




import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.game.AttackEvent;
import pyrock.events.game.PostAttackEvent;
import pyrock.events.player.KeepSprintEvent;
import rockstar.modules.player.NoPushModule;
import rockstar.client.RockstarClient;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationRequest;
import rockstar.client.internal.core.NewtonGlobalState;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.modules.combat.HitboxesModule;
import rockstar.client.internal.core.PathExecutor;
import rockstar.client.internal.rotation.AbstractPathStep;

@Mixin({PlayerEntity.class})
public class PlayerEntityMixin {
   @Inject(
      method = {"attack"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void attackAHook2(Entity localValue1, CallbackInfo localValue2) {
      HitboxesModule localValue3 = RockstarClient.getInstance().getModuleManager().getModule(HitboxesModule.class);
      if (localValue1 instanceof LivingEntity && localValue3.isEnabled() && localValue3.internalMethod03348().internalMethod08576() <= 0.0F) {
         localValue2.cancel();
      } else {
         AttackEvent localValue4 = new AttackEvent(localValue1);
         RockstarClient.getInstance().internalMethod03317().internalMethod06883(localValue4);
         if (localValue4.isCancelled()) {
            localValue2.cancel();
         }
      }
   }

   @Inject(
      method = {"knockbackTarget"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
         ordinal = 0
      )},
      cancellable = true
   )
   private void onAttackSetVelocity(Entity localValue1, float localValue2, Vec3d localValue3, CallbackInfo localValue4) {
      KeepSprintEvent localValue5 = new KeepSprintEvent();
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(localValue5);
      if (localValue5.isCancelled()) {
         localValue4.cancel();
      }
   }

   @Inject(
      method = {"attack"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void attackAHook(Entity localValue1, CallbackInfo localValue2) {
      PostAttackEvent localValue3 = new PostAttackEvent(localValue1);
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(localValue3);
   }

   @Inject(
      method = {"isPushedByFluids"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void removePushFromFluids(CallbackInfoReturnable<Boolean> localValue1) {
      NoPushModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(NoPushModule.class);
      PlayerEntity localValue3 = (PlayerEntity)(Object)this;
      if (localValue3 == MinecraftClient.getInstance().player && localValue2.isEnabled() && localValue2.internalMethod02897().isSelected()) {
         localValue1.setReturnValue(false);
      }
   }

   @Inject(
      method = {"clipAtLedge"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void pathfinder$safewalkClipAtLedge(CallbackInfoReturnable<Boolean> localValue1) {
      if (!localValue1.getReturnValueZ()) {
         if (NewtonGlobalState.internalField0277) {
            if (NewtonCoreManager.internalMethod00010()) {
               if ((Object)this == MinecraftClient.getInstance().player) {
                  if (NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03477()) {
                     if (!MinecraftClient.getInstance().player.isTouchingWater()) {
                        PathExecutor localValue2 = NewtonCoreManager.internalMethod00114().internalMethod01484();
                        if (localValue2 != null) {
                           AbstractPathStep localValue3 = localValue2.internalMethod02430();
                           if (localValue3 != null) {
                              if (localValue3.internalMethod01348()) {
                                 if (localValue3.internalMethod02540().internalMethod02949() >= localValue3.internalMethod01873().internalMethod02949()) {
                                    AbstractPathStep localValue4 = localValue3.internalMethod03493();
                                    if (localValue4 == null
                                       || localValue4.internalMethod01348()
                                          && localValue4.internalMethod02540().internalMethod02949() >= localValue4.internalMethod01873().internalMethod02949()
                                       || !localValue3.internalMethod05094(0.85)) {
                                       localValue1.setReturnValue(true);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Redirect(
      method = {"travel(Lnet/minecraft/util/math/Vec3d;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/player/PlayerEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
      )
   )
   private Vec3d redirectGetRotationVectorInTravel(PlayerEntity localValue1) {
      if (localValue1 != MinecraftClient.getInstance().player) {
         return localValue1.getRotationVector();
      } else {
         RotationManager localValue2 = RockstarClient.getInstance().internalMethod02368();
         RotationRequest localValue3 = localValue2.internalMethod07551();
         return !localValue2.internalMethod01525() && localValue3 != null && localValue3.internalMethod01386().internalMethod03280()
            ? localValue2.internalMethod09074().internalMethod06001()
            : localValue1.getRotationVector();
      }
   }
}
