package wtf.wyvern.core.events.callables;

import wtf.wyvern.core.eventbus.events.Event;
import wtf.wyvern.core.eventbus.events.Typed;

public abstract class EventTyped implements Event, Typed {
   private final byte type;

   protected EventTyped(byte eventType) {
      this.type = eventType;
   }

   public byte getType() {
      return this.type;
   }
}