package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.utilities.Optimizations;
import haron.modules.visuals.RenderTweaks;
import haron.modules.visuals.Particles;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ParticleManager.class})
public class ParticleManagerMixin {
    @Inject(method={"addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAddParticle(ParticleEffect ParticleEffectVar, double d, double d2, double d3, double d4, double d5, double d6, CallbackInfoReturnable<Particle> callbackInfoReturnable) {
        ParticleType type = ParticleEffectVar.getType();
        if (type == ParticleTypes.FIREWORK || type == ParticleTypes.FLASH) {
            return;
        }
        Particles customParticles = ModuleManager.PARTICLES;
        if (customParticles.isEnabled() && (type == ParticleTypes.CRIT || type == ParticleTypes.ENCHANTED_HIT || type == ParticleTypes.DAMAGE_INDICATOR)) {
            return;
        }
        try {
            if (Optimizations.cullParticles()) {
                callbackInfoReturnable.setReturnValue(null);
                return;
            }
        }
        catch (NoSuchMethodError noSuchMethodError) {
            // empty catch block
        }
        if (type == ParticleTypes.RAIN || type == ParticleTypes.MYCELIUM || type == ParticleTypes.SPORE_BLOSSOM_AIR || type == ParticleTypes.CHERRY_LEAVES || type == ParticleTypes.CAMPFIRE_COSY_SMOKE || type == ParticleTypes.DRIPPING_WATER || type == ParticleTypes.DRIPPING_LAVA) {
            callbackInfoReturnable.setReturnValue(null);
            return;
        }
        if (Optimizations.hideExplosions() && (type == ParticleTypes.EXPLOSION || type == ParticleTypes.EXPLOSION_EMITTER || type == ParticleTypes.POOF)) {
            callbackInfoReturnable.setReturnValue(null);
            return;
        }
        RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
        if (!renderTweaks.k()) {
            return;
        }
        if (!renderTweaks.u()) {
            return;
        }
        if (type == ParticleTypes.BUBBLE || type == ParticleTypes.BUBBLE_COLUMN_UP || type == ParticleTypes.BUBBLE_POP || type == ParticleTypes.CURRENT_DOWN || type == ParticleTypes.UNDERWATER) {
            callbackInfoReturnable.setReturnValue(null);
        }
    }
}

