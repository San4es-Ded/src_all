package wtf.wyvern.core.eventbus.events;

public interface Cancellable {
   boolean isCancelled();

   void setCancelled(boolean var1);
}