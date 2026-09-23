 package su.sacura.mixin.client.network;
 
 import java.util.HashMap;
 import java.util.Map;
 import java.util.UUID;
 import net.minecraft.client.network.ClientPlayerInteractionManager;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.PlayerEntity;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Unique;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import su.sacura.Sacura;
 import su.sacura.events.player.EventAttack;
 
 @Mixin({ClientPlayerInteractionManager.class})
 public class ClientPlayerInteractionManagerMixin {
   @Unique
   private static final Map<UUID, Long> lastHitTimes = new HashMap<>();
   
   @Inject(method = {"attackEntity"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", shift = At.Shift.AFTER, ordinal = 0)})
   private void afterSendPacket(PlayerEntity player, Entity target, CallbackInfo ci) {
     Sacura.getInstance().getEventBus().post(new EventAttack(player, target));
     markHit(target);
   }
   
   public void markHit(Entity entity) {
     lastHitTimes.put(entity.getUuid(), Long.valueOf(System.currentTimeMillis()));
   }
 }


