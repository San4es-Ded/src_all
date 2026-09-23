package wtf.wyvern.core.events.impl.render;

import wtf.wyvern.core.eventbus.events.Event;
import lombok.Generated;
import wtf.wyvern.render.display.base.CustomDrawContext;

public class EventRender2D implements Event {
   private final CustomDrawContext context;
   private final float tickDelta;

   @Generated
   public CustomDrawContext getContext() {
      return this.context;
   }

   @Generated
   public float getTickDelta() {
      return this.tickDelta;
   }

   @Generated
   public EventRender2D(CustomDrawContext context, float tickDelta) {
      this.context = context;
      this.tickDelta = tickDelta;
   }
}