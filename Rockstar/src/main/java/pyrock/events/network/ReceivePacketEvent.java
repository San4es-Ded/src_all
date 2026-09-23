package pyrock.events.network;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.network.packet.Packet;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="receive_packet")
public class ReceivePacketEvent
extends EventCancellable {
    private final Packet<?> packet;

    @Generated
    public Packet<?> getPacket() {
        return this.packet;
    }

    @Generated
    public ReceivePacketEvent(Packet<?> packet) {
        this.packet = packet;
    }
}

