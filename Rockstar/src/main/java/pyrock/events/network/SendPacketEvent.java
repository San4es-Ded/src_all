package pyrock.events.network;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.network.packet.Packet;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="send_packet")
public class SendPacketEvent
extends EventCancellable {
    private Packet<?> packet;

    @Generated
    public Packet<?> getPacket() {
        return this.packet;
    }

    @Generated
    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    @Generated
    public SendPacketEvent(Packet<?> packet) {
        this.packet = packet;
    }
}

