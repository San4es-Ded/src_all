package su.sacura.events.packet;

import net.minecraft.network.packet.Packet;

public class EventPacket {
    public boolean isCancel;
    private Packet packet;
    private final PacketType packetType;

    public EventPacket(Packet packet, PacketType packetType) {
        this.packet = packet;
        this.packetType = packetType;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean isReceivePacket() {
        return this.packetType == PacketType.RECEIVE;
    }

    public boolean isSendPacket() {
        return this.packetType == PacketType.SEND;
    }

    public boolean isCancel() {
        return this.isCancel;
    }

    public static enum PacketType {
        SEND,
        RECEIVE;

    }
}
