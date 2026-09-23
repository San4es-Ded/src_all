package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;

public class PushEvent extends Event implements IEvent {
   private final PushEvent.a a;

   @Generated
   public PushEvent.a b() {
      return this.a;
   }

   public PushEvent(PushEvent.a type) {
      this.a = type;
   }

   public static enum a {
      BLOCKS,
      FLUIDS,
      ENTITIES,
      WORLD_BORDER,
      FISHING_HOOK;
   }
}
