package ru.haron.mixin;

import haron.events.CriticalHitEvent;
import haron.events.EventDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntity.class})
public class PlayerEntityMixin {
    @Inject(method={"attack"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V")})
    private void onCriticalHit(Entity EntityVar, CallbackInfo callbackInfo) {
        EventDispatcher.EVENT_BUS.post((Object)new CriticalHitEvent(EntityVar));
    }
}

