package ru.prism.mixin;


import ru.prism.manager.event_impl.EventJump;
import ru.prism.manager.event_impl.SwingDurationEvent;
import ru.prism.utils.annotation.IMinecraft;
import ru.prism.utils.math.MathUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements IMinecraft {

    @Shadow
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow @Nullable
    public abstract StatusEffectInstance getStatusEffect(RegistryEntry<StatusEffect> effect);



    @Inject(method = "getHandSwingDuration", at = @At("HEAD"), cancellable = true)
    private void swingProgressHook(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this != mc.player) {
            return;
        }

        SwingDurationEvent event = new SwingDurationEvent();
        event.hook();

        if (event.isCancelled()) {
            float animation = event.getAnimation();
            if (StatusEffectUtil.hasHaste(mc.player)) animation *= (6 - (1 + StatusEffectUtil.getHasteAmplifier(mc.player)));
            else animation *= (hasStatusEffect(StatusEffects.MINING_FATIGUE) ? 6 + (1 + getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6);
            cir.setReturnValue((int) animation);
        }
    }


    @Inject(method = "jump", at = @At("HEAD"))
    public void jumpYo(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (self instanceof ClientPlayerEntity) {
            new EventJump().hook();
        }
    }



}
