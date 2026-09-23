package moscow.rockstar.mixin.minecraft.client.render;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;

@Mixin(value={ParticleManager.class})
public abstract class ParticleManagerMixin {
    @Inject(method={"addParticle"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAddParticle(ParticleEffect particleEffect, double d, double d2, double d3, double d4, double d5, double d6, CallbackInfoReturnable<Particle> callbackInfoReturnable) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod09965().isSelected() && particleEffect.getType() == ParticleTypes.RAIN) {
            callbackInfoReturnable.cancel();
        }
    }
}
