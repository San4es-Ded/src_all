package ru.haron.mixin;

import haron.events.PacketDirection;
import haron.events.PacketEvent;
import haron.events.EventDispatcher;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ClientConnection.class})
public class ClientConnectionMixin {
    @Inject(method={"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V")}, cancellable=true)
    private void onChannelRead(ChannelHandlerContext channelHandlerContext, Packet<?> PacketVar, CallbackInfo callbackInfo) {
        PacketEvent packetEvent = new PacketEvent(PacketVar, PacketDirection.RECIEVE);
        EventDispatcher.EVENT_BUS.post((Object)packetEvent);
        if (packetEvent.c()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendPacket(Packet<?> PacketVar, PacketCallbacks PacketCallbacksVar, CallbackInfo callbackInfo) {
        PacketEvent packetEvent = new PacketEvent(PacketVar, PacketDirection.SEND);
        EventDispatcher.EVENT_BUS.post((Object)packetEvent);
        if (packetEvent.c()) {
            callbackInfo.cancel();
        }
    }
}

