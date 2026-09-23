package wtf.wyvern.mixin.minecraft.entity;

import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.core.events.impl.other.EventTickMovement;

@Mixin({PlayerEntity.class})
public class PlayerEntityMixin {
   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   public void tickMovement(CallbackInfo ci) {
      EventTickMovement event = new EventTickMovement();
      EventManager.call(event);
   }
}