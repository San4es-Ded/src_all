package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.visuals.RenderTweaks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public class StatusEffectsMixin {
    @Inject(method={"hasStatusEffect"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHasStatusEffect(RegistryEntry<StatusEffect> RegistryEntryVar, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if ((Object)this instanceof ClientPlayerEntity) {
            RenderTweaks renderTweaks = ModuleManager.RENDER_TWEAKS;
            if (RegistryEntryVar == StatusEffects.WITHER && renderTweaks.t()) {
                callbackInfoReturnable.setReturnValue(false);
            }
        }
    }
}
