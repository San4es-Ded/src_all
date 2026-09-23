package haron.events;

import haron.events.CancellableEvent;
import haron.events.PacketDirection;
import net.minecraft.network.packet.Packet;

public class PacketEvent
extends CancellableEvent {
    private final Packet<?> packet;
    private final PacketDirection direction;

    public Packet<?> packet() {
        return this.packet;
    }

    public PacketEvent(Packet<?> packet, PacketDirection dm6s682) {
        this.packet = packet;
        this.direction = dm6s682;
    }

    public PacketDirection e() {
        return this.direction;
    }

    public Packet<?> d() {
        return this.packet;
    }

    public PacketDirection direction() {
        int n = 957;
        return this.direction;
    }
}

