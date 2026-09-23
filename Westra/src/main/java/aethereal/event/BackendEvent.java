package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import aethereal.core.Packet;
import lombok.Generated;

public class BackendEvent extends Event implements IEvent {
   private final Packet a;
   private final BackendEvent.Phase b;

   @Generated
   public Packet d() {
      return this.a;
   }

   @Generated
   public BackendEvent.Phase e() {
      return this.b;
   }

   public BackendEvent(Packet packet, BackendEvent.Phase type) {
      this.a = packet;
      this.b = type;
   }

   public BackendEvent(BackendEvent.Phase type) {
      this.b = type;
      this.a = null;
   }

   public boolean b() {
      return this.b == BackendEvent.Phase.RECEIVE;
   }

   public boolean c() {
      return this.b == BackendEvent.Phase.CLOSE;
   }

   public static enum Phase {
      RECEIVE,
      CLOSE;
   }
}
