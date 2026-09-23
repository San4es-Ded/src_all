package wtf.wyvern.mixin.minecraft.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.client.modules.impl.movement.ElytraBooster;
import wtf.wyvern.utility.math.BoostUtils;

@Mixin({FireworkRocketEntity.class})
public abstract class FireworkRocketEntityMixin extends ProjectileEntity {
   @Shadow
   private LivingEntity shooter;

   public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
      super(entityType, world);
   }

   @WrapOperation(
           method = {"tick"},
           at = @At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;",
                   ordinal = 0
           )
   )
   private Vec3d wyvern$modifyBoost(Vec3d velocity, double x, double y, double z, Operation<Vec3d> original) {
      MinecraftClient mc = MinecraftClient.getInstance();
      ElytraBooster elytraBooster = ElytraBooster.INSTANCE;

      if (mc.player == null || !mc.player.isGliding() || this.shooter != mc.player) {
         return original.call(velocity, x, y, z);
      }

      if (elytraBooster == null || !elytraBooster.isEnabled()) {
         return Aura.INSTANCE.applyMovementOvertake(original.call(velocity, x, y, z));
      }

      return wyvern$handleElytraBoost(mc, elytraBooster, velocity, original);
   }

   @Unique
   private Vec3d wyvern$handleElytraBoost(
           MinecraftClient mc,
           ElytraBooster elytraBooster,
           Vec3d velocity,
           Operation<Vec3d> original
   ) {
      String modeName = elytraBooster.getMode().getValue().getName();
      float effectivePitch = elytraBooster.getEffectivePitch();
      Vec3d boost;

      switch (modeName) {
         case "LonyGrief":
            boost = BoostUtils.getBoost(mc.player);
            break;
         case "BravoHVHFFA":
            Vec2f bravoBoost = elytraBooster.getBravoBoostV2();
            boost = BoostUtils.fromSliderSettings(mc.player, bravoBoost.x, bravoBoost.y, effectivePitch);
            break;
         case "ReallyWorld":
            boost = BoostUtils.getBoostrw(mc.player);
            break;
         case "Custom":
         default:
            Vec2f customBoost = elytraBooster.getBoostV2();
            boost = BoostUtils.fromSliderSettings(mc.player, customBoost.x, customBoost.y, effectivePitch);
            break;
      }

      Vec3d rotation = wyvern$getBoostRotation(elytraBooster);
      Vec3d target = new Vec3d(
              rotation.x * boost.x,
              rotation.y * boost.y,
              rotation.z * boost.z
      );
      Vec3d boostedVelocity = original.call(
              velocity,
              rotation.x * 0.1D + (target.x - velocity.x) * 0.5D,
              rotation.y * 0.1D + (target.y - velocity.y) * 0.5D,
              rotation.z * 0.1D + (target.z - velocity.z) * 0.5D
      );
      return Aura.INSTANCE.applyMovementOvertake(boostedVelocity);
   }

   @Unique
   private Vec3d wyvern$getBoostRotation(ElytraBooster elytraBooster) {
      Aura aura = Aura.INSTANCE;
      if (aura != null && aura.isEnabled() && aura.getTarget() != null) {
         return Vec3d.fromPolar(elytraBooster.getEffectivePitch(), elytraBooster.getEffectiveYaw());
      }
      return this.shooter.getRotationVector();
   }
}
