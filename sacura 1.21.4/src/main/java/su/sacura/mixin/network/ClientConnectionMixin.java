 package su.sacura.mixin.network;
 
 import io.netty.channel.ChannelHandlerContext;
 import net.minecraft.network.ClientConnection;
 import net.minecraft.network.packet.Packet;
 import org.spongepowered.asm.mixin.Mixin;
 import org.spongepowered.asm.mixin.Unique;
 import org.spongepowered.asm.mixin.injection.At;
 import org.spongepowered.asm.mixin.injection.Inject;
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
 import su.sacura.Sacura;
 import su.sacura.events.packet.EventPacket;
 import su.sacura.util.type.MinecraftWrapper;
 
 @Mixin({ClientConnection.class})
 public class ClientConnectionMixin implements MinecraftWrapper {
   @Unique
   private boolean sendingSilent = false;
   
   @Inject(method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
   private void onPacketReceived(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
     EventPacket event = new EventPacket(packet, EventPacket.PacketType.RECEIVE);
     Sacura.getInstance().getEventBus().post(event);
     if (event.isCancel())
       ci.cancel(); 
   }
   
   @Inject(method = {"send(Lnet/minecraft/network/packet/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
   private void onPacketSend(Packet<?> packet, CallbackInfo ci) {
     if (isSendingSilent())
       return; 
     EventPacket event = new EventPacket(packet, EventPacket.PacketType.SEND);
     Sacura.getInstance().getEventBus().post(event);
     if (event.isCancel())
       ci.cancel(); 
   }
   
   public void sendSilentPacket(Packet<?> packet) {
     try {
       this.sendingSilent = true;
       mc.player.networkHandler.sendPacket(packet);
     } finally {
       this.sendingSilent = false;
     } 
   }
   
   public void sendPacket(Packet<?> packet) {
     mc.player.networkHandler.sendPacket(packet);
   }
   
   public boolean isSendingSilent() {
     return this.sendingSilent;
   }
 }


