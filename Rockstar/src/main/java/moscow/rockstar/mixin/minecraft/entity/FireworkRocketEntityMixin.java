package moscow.rockstar.mixin.minecraft.entity;


import rockstar.client.rotation.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pyrock.events.game.FireworkEvent;
import rockstar.client.RockstarClient;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationState;

@Mixin(value={FireworkRocketEntity.class})
public abstract class FireworkRocketEntityMixin
implements MinecraftClientAccess {
    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void redirectSetVelocity(LivingEntity livingEntity, Vec3d vec3d) {
        FireworkRocketEntity fireworkRocketEntity = (FireworkRocketEntity)(Object)this;
        FireworkEvent fireworkEvent = new FireworkEvent(livingEntity, vec3d, fireworkRocketEntity);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(fireworkEvent);
        livingEntity.setVelocity(fireworkEvent.getVelocity());
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d redirectGetRotationVector(LivingEntity livingEntity) {
        RotationManager typedValue269;
        if (livingEntity == FireworkRocketEntityMixin.internalField0149.player && (typedValue269 = RockstarClient.getInstance().internalMethod02368()) != null && typedValue269.internalMethod07550() != RotationState.internalField0126) {
            Rotation typedValue266 = typedValue269.internalMethod09074();
            return Vec3d.fromPolar((float)typedValue266.internalMethod00171(), (float)typedValue266.internalMethod00169());
        }
        return livingEntity.getRotationVector();
    }
}

