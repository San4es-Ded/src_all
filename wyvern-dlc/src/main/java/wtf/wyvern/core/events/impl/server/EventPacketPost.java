package wtf.wyvern.core.events.impl.server;

import lombok.Generated;
import net.minecraft.network.packet.Packet;
import wtf.wyvern.core.events.callables.EventCancellable;

public class EventPacketPost extends EventCancellable {
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
   public EventPacketPost(Packet<?> packet) {
      this.packet = packet;
   }
}