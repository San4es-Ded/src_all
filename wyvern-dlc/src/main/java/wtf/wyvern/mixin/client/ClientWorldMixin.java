package wtf.wyvern.mixin.client;

import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.core.events.impl.other.EventSpawnEntity;

@Mixin({ClientWorld.class})
public class ClientWorldMixin {
   @Inject(
      method = {"addEntity"},
      at = {@At("RETURN")}
   )
   public void injectAddEntity(Entity entity, CallbackInfo ci) {
      EventSpawnEntity eventSpawnEntity = new EventSpawnEntity(entity);
      EventManager.call(eventSpawnEntity);
   }
}