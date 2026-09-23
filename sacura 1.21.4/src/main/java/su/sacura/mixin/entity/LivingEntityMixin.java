package su.sacura.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.sacura.Sacura;
import su.sacura.events.player.EntityDeathEvent;
import su.sacura.features.modules.impl.render.SwingAnimationsModule;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({LivingEntity.class})
public class LivingEntityMixin implements MinecraftWrapper {
    @Inject(method = {"getHandSwingDuration"}, at = {@At("HEAD")}, cancellable = true)
    private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> info) {
        SwingAnimationsModule swingAnimations = (SwingAnimationsModule)Sacura.getInstance().getModuleManager().getModule(SwingAnimationsModule.class);
        if (swingAnimations.enable)
            info.setReturnValue(Integer.valueOf(((Float)swingAnimations.slowAnimationSpeed.get()).intValue()));
    }

    @Inject(method = {"onDeath"}, at = {@At("HEAD")})
    private void onDeath(DamageSource source, CallbackInfo ci) {
        // Исправлено: (LivingEntity)this -> (LivingEntity)(Object)this
        LivingEntity entity = (LivingEntity)(Object)this;
        EntityDeathEvent event = new EntityDeathEvent((Entity)entity, source);
        Sacura.getInstance().getEventBus().post(event);
    }

    @Inject(method = {"handleStatus"}, at = {@At("HEAD")})
    private void handleStatus(byte status, CallbackInfo ci) {
        if (status == 3) {
            // Исправлено: (LivingEntity)this -> (LivingEntity)(Object)this
            LivingEntity entity = (LivingEntity)(Object)this;
            EntityDeathEvent event = new EntityDeathEvent((Entity)entity, null);
            Sacura.getInstance().getEventBus().post(event);
        }
    }
}