package ru.pulse.mixin;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pulse.module.ModuleRegistry;
import pulse.modules.utilities.Optimization;
import pulse.modules.visuals.RenderTweaks;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
   @Inject(require = 0, method = "addParticle", at = @At("HEAD"), cancellable = true)
   private void onAddParticle(
      ParticleEffect ParticleEffectVar, double d, double d2, double d3, double d4, double d5, double d6, CallbackInfoReturnable<Particle> callbackInfoReturnable
   ) {
      if (ParticleEffectVar != null) {
         try {
            ParticleType<?> type = ParticleEffectVar.getType();
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
         } catch (Throwable var18) {
         }

         if (!Optimization.shouldSpawnParticle(d, d2, d3)) {
            callbackInfoReturnable.setReturnValue((Particle)null);
         } else {
            RenderTweaks renderTweaks = ModuleRegistry.RENDER_TWEAKS;
            if (renderTweaks != null && renderTweaks.u()) {
               try {
                  ParticleType<?> type = ParticleEffectVar.getType();
                  if (type == ParticleTypes.BUBBLE
                     || type == ParticleTypes.BUBBLE_COLUMN_UP
                     || type == ParticleTypes.BUBBLE_POP
                     || type == ParticleTypes.CURRENT_DOWN
                     || type == ParticleTypes.UNDERWATER) {
                     callbackInfoReturnable.setReturnValue((Particle)null);
                  }
               } catch (Throwable var17) {
               }
            }
         }
      }
   }
}
