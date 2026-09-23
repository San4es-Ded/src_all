package wtf.wyvern.core.eventbus.events.callables;

import wtf.wyvern.core.eventbus.events.Cancellable;
import wtf.wyvern.core.eventbus.events.Event;

public abstract class EventCancellable implements Event, Cancellable {
   private boolean cancelled;

   protected EventCancellable() {
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void setCancelled(boolean state) {
      this.cancelled = state;
   }
}