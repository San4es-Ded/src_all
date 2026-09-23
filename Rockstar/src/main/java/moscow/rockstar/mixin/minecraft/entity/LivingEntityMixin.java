package moscow.rockstar.mixin.minecraft.entity;



import rockstar.client.rotation.*;
import rockstar.client.internal.game.*;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.game.BreakTotemEvent;
import pyrock.events.game.EntityDeathEvent;
import pyrock.events.game.EntityJumpEvent;
import pyrock.events.player.EventOnTravelPost;
import rockstar.modules.player.NoDelayModule;
import rockstar.modules.player.NoPushModule;
import rockstar.modules.visual.SwingAnimationModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.EntityPositionTracker;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationRequest;
import rockstar.modules.combat.ElytraTargetModule;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin {
   @Shadow
   private int jumpingCooldown;

   @Shadow
   public abstract void remove(RemovalReason localValue1);

   @Shadow
   public abstract ItemStack getMainHandStack();

   @ModifyReturnValue(
      method = {"getHandSwingDuration"},
      at = {@At("RETURN")}
   )
   public int replaceSwingSpeed(int localValue1) {
      SwingAnimationModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(SwingAnimationModule.class);
      return localValue2.isEnabled() && localValue2.internalMethod02588(this.getMainHandStack())
         ? (int)(localValue1 * RockstarClient.getInstance().internalMethod00061().internalMethod03324().internalMethod08576())
         : localValue1;
   }

   @Inject(
      method = {"jump"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void triggerJumpEvent(CallbackInfo localValue1) {
      LivingEntity localValue2 = (LivingEntity)(Object)this;
      EntityJumpEvent localValue3 = new EntityJumpEvent(localValue2);
      RockstarClient.getInstance().internalMethod03317().internalMethod06883(localValue3);
      if (localValue3.isCancelled()) {
         localValue1.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"jump"},
      at = {@At(
         value = "NEW",
         target = "(DDD)Lnet/minecraft/util/math/Vec3d;"
      )}
   )
   public Vec3d movementCorrection(Vec3d localValue1) {
      RotationManager localValue2 = RockstarClient.internalField0240.internalMethod02368();
      RotationRequest localValue3 = localValue2.internalMethod07551();
      if ((Object)this != MinecraftClient.getInstance().player) {
         return localValue1;
      } else if (localValue3 != null && localValue3.internalMethod01386().internalMethod03281()) {
         float localValue4 = localValue2.internalMethod09074().internalMethod00169() * (float) (Math.PI / 180.0);
         return new Vec3d(-MathHelper.sin(localValue4) * 0.2F, 0.0, MathHelper.cos(localValue4) * 0.2F);
      } else {
         return localValue1;
      }
   }

   @Inject(
      method = {"tickMovement"},
      at = {@At("HEAD")}
   )
   public void removeJumpDelay(CallbackInfo localValue1) {
      NoDelayModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(NoDelayModule.class);
      if (localValue2.isEnabled() && localValue2.internalMethod01813().internalMethod04496()) {
         this.jumpingCooldown = 0;
      }
   }

   @Inject(
      method = {"isPushable"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void removePushFromEntity(CallbackInfoReturnable<Boolean> localValue1) {
      NoPushModule localValue2 = RockstarClient.getInstance().getModuleManager().getModule(NoPushModule.class);
      LivingEntity localValue3 = (LivingEntity)(Object)this;
      if (localValue3 instanceof ClientPlayerEntity && localValue2.isEnabled() && localValue2.internalMethod02708().isSelected()) {
         localValue1.setReturnValue(false);
      }
   }

   @Inject(
      method = {"onDeath"},
      at = {@At("TAIL")}
   )
   public void triggerEntityDeathEvent(DamageSource localValue1, CallbackInfo localValue2) {
      LivingEntity localValue3 = (LivingEntity)(Object)this;
      if (EntityPositionTracker.internalMethod06807(localValue3)) {
         RockstarClient.getInstance().internalMethod03317().internalMethod06883(new EntityDeathEvent(localValue3, localValue1));
      }
   }

   @Redirect(
      method = {"calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"
      )
   )
   private float redirectGetPitch(LivingEntity localValue1) {
      return rockstar$shouldCorrectMovement(localValue1)
         ? RockstarClient.getInstance().internalMethod02368().internalMethod09074().internalMethod00171()
         : localValue1.getPitch();
   }

   @Redirect(
      method = {"calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
      )
   )
   private Vec3d redirectGetRotationVector(LivingEntity localValue1) {
      return rockstar$shouldCorrectMovement(localValue1)
         ? RockstarClient.getInstance().internalMethod02368().internalMethod09074().internalMethod06001()
         : localValue1.getRotationVector();
   }

   @Unique
   private static boolean rockstar$shouldCorrectMovement(LivingEntity localValue0) {
      if (localValue0 != MinecraftClient.getInstance().player) {
         return false;
      } else {
         RotationManager localValue1 = RockstarClient.getInstance().internalMethod02368();
         RotationRequest localValue2 = localValue1.internalMethod07551();
         return !localValue1.internalMethod01525() && localValue2 != null && localValue2.internalMethod01386().internalMethod03280();
      }
   }

   @Inject(
      method = {"calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private void rockstar$onCalcGlidingVelocity(Vec3d localValue1, CallbackInfoReturnable<Vec3d> localValue2) {
      LivingEntity localValue3 = (LivingEntity)(Object)this;
      if (localValue3 == MinecraftClient.getInstance().player) {
         EventOnTravelPost localValue4 = new EventOnTravelPost((Vec3d)localValue2.getReturnValue());
         RockstarClient.getInstance().internalMethod03317().internalMethod06883(localValue4);
         localValue2.setReturnValue(localValue4.getOldVelocity());
      }
   }

   @Inject(
      method = {"tryUseDeathProtector"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/item/ItemStack;decrement(I)V"
      )}
   )
   private void onTotemUse(DamageSource localValue1, CallbackInfoReturnable<Boolean> localValue2) {
      LivingEntity localValue3 = (LivingEntity)(Object)this;
      if (localValue3 instanceof PlayerEntity) {
         for (Hand localValue7 : Hand.values()) {
            ItemStack localValue8 = localValue3.getStackInHand(localValue7);
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(new BreakTotemEvent(localValue3, localValue8));
            if (localValue8.contains(DataComponentTypes.DEATH_PROTECTION)) {
               return;
            }
         }
      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void cancelElytraMovement(Vec3d localValue1, CallbackInfo localValue2) {
      LivingEntity localValue3 = (LivingEntity)(Object)this;
      if (localValue3 == MinecraftClient.getInstance().player) {
         ElytraTargetModule localValue4 = RockstarClient.getInstance().getModuleManager().getModule(ElytraTargetModule.class);
         boolean localValue5 = localValue4.isEnabled()
            && localValue4.internalMethod08428().internalMethod04496()
            && localValue4.internalMethod03335() != null
            && localValue3.isGliding()
            && localValue3.distanceTo(localValue4.internalMethod03335()) <= localValue4.internalMethod08963().internalMethod08576();
         if (localValue5) {
            localValue3.setVelocity(Vec3d.ZERO);
            localValue2.cancel();
         }
      }
   }
}
