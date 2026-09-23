package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;

public class HandEvent extends Event implements IEvent {
   private final HandEvent.a a;

   @Generated
   public HandEvent(HandEvent.a phase) {
      this.a = phase;
   }

   @Generated
   public HandEvent.a d() {
      return this.a;
   }

   public boolean b() {
      return this.a == HandEvent.a.PRE;
   }

   public boolean c() {
      return this.a == HandEvent.a.POST;
   }

   public static enum a {
      PRE,
      POST;
   }
}
