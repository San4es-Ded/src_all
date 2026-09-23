package ru.prism.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.prism.module.impl.utils.Optimization;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Inject(
            method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void prism$limitParticles(ParticleEffect effect, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        ParticleType<?> type = effect.getType();
        if (type == ParticleTypes.FIREWORK
                || type == ParticleTypes.FLASH
                || type == ParticleTypes.EXPLOSION
                || type == ParticleTypes.EXPLOSION_EMITTER
                || type == ParticleTypes.PORTAL
                || type == ParticleTypes.REVERSE_PORTAL
                || type == ParticleTypes.END_ROD
                || type == ParticleTypes.POOF
                || type == ParticleTypes.SMOKE
                || type == ParticleTypes.LARGE_SMOKE
                || type == ParticleTypes.WITCH
                || type == ParticleTypes.INSTANT_EFFECT
                || type == ParticleTypes.EFFECT) {
            return;
        }

        if (!Optimization.shouldSpawnParticle(x, y, z)) {
            cir.setReturnValue(null);
        }
    }
}
