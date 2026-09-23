package wtf.wyvern.mixin.compat;

import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.client.modules.impl.render.MasEffectsModule;

/** Gates MasEffects' original event handlers without changing their implementation. */
@Pseudo
@org.spongepowered.asm.mixin.Mixin(targets = {
   "net.masuno.events.TotemEvent",
   "net.masuno.events.DeathEvent",
   "net.masuno.events.EnderPearlTickEvent",
   "net.masuno.events.PlayerAttackManager"
})
public abstract class MasEffectsGateMixin {
   @Inject(method = {"run", "runDeath", "tick", "clientAttack", "ShieldShockwave", "SlamEffect", "SpawnParticlesOnHitbox", "ArmorParticles"}, at = @At("HEAD"), cancellable = true, remap = false, require = 0)
   private static void wyvern$gateEffects(CallbackInfo ci) {
      if (!MasEffectsModule.INSTANCE.isEnabled()) ci.cancel();
   }
}
