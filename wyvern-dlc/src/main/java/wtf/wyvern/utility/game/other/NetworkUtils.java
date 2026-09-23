package wtf.wyvern.utility.game.other;

import java.util.List;
import net.minecraft.network.packet.Packet;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@FastNative
public class NetworkUtils implements IMinecraft {
   public static void sendSilentPacket(Packet<?> packet) {
      PacketSilencer.add(packet);
      mc.getNetworkHandler().sendPacket(packet);
   }

   public static void sendPacket(Packet<?> packet) {
      mc.getNetworkHandler().sendPacket(packet);
   }

   public static List<Packet<?>> getSilentPackets() {
      return PacketSilencer.packets();
   }

   public static void clearSilentPackets() {
      PacketSilencer.clear();
   }
}
