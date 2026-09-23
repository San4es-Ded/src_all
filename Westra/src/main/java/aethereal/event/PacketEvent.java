package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.class_2596;

public class PacketEvent extends Event implements IEvent {
   private final class_2596<?> packet;
   private final PacketEvent.Type type;

   @Generated
   public class_2596<?> d() {
      return this.packet;
   }

   @Generated
   public PacketEvent.Type e() {
      return this.type;
   }

   public PacketEvent(class_2596<?> packet, PacketEvent.Type type) {
      this.packet = packet;
      this.type = type;
   }

   public boolean b() {
      return this.type == PacketEvent.Type.SEND;
   }

   public boolean c() {
      return this.type == PacketEvent.Type.RECEIVE;
   }

   public static enum Type {
      SEND,
      RECEIVE;
   }
}
