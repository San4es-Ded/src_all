package wtf.wyvern.utility.game.other;

import net.minecraft.network.packet.Packet;

import java.util.ArrayList;
import java.util.List;
import wtf.astroguard.J2C.FastNative;

@FastNative
public final class PacketSilencer {
    private static final List<Packet<?>> silentPackets = new ArrayList<>();

    private PacketSilencer() {
    }

    public static void add(Packet<?> packet) {
        silentPackets.add(packet);
    }

    public static boolean remove(Packet<?> packet) {
        return silentPackets.remove(packet);
    }

    public static List<Packet<?>> packets() {
        return silentPackets;
    }

    public static void clear() {
        silentPackets.clear();
    }
}
