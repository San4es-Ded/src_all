 package su.sacura.mixin.client.network;
 
 import net.minecraft.client.network.ClientPlayerEntity;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Unique;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import su.sacura.Sacura;
 import su.sacura.events.player.EventMotion;
 import su.sacura.events.tick.EventUpdate;
 import su.sacura.util.type.MinecraftWrapper;
 
 @Mixin({ClientPlayerEntity.class})
 public abstract class ClientPlayerEntityMixin implements MinecraftWrapper {
   @Unique
   private float preYaw;
   
   @Unique
   private float prePitch;
   
   @Unique
   private float packetYaw;
   
   @Unique
   private float packetPitch;
   
   @Inject(method = {"tick"}, at = {@At("HEAD")})
   private void onTickHead(CallbackInfo ci) {
     Sacura.getInstance().getEventBus().post(new EventUpdate());
     this.preYaw = mc.player.getYaw();
     this.prePitch = mc.player.getPitch();
   }
   
   @Inject(method = {"sendMovementPackets"}, at = {@At("HEAD")}, cancellable = true)
   private void onSendMovementPacketsHead(CallbackInfo ci) {
     EventMotion event = new EventMotion(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), mc.player.isOnGround());
     Sacura.getInstance().getEventBus().post(event);
     if (event.isCancel()) {
       ci.cancel();
       return;
     } 
     mc.player.setYaw(event.getYaw());
     mc.player.setPitch(event.getPitch());
   }
   
   @Inject(method = {"tick"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V", shift = At.Shift.AFTER)})
   private void afterSendMovementPackets(CallbackInfo ci) {
     this.packetYaw = mc.player.getYaw();
     this.packetPitch = mc.player.getPitch();
     mc.player.setYaw(this.preYaw);
     mc.player.setPitch(this.prePitch);
   }
 }


