package wtf.wyvern.core.events.impl.render;

import wtf.wyvern.core.eventbus.events.Event;
import lombok.Generated;
import wtf.wyvern.render.display.base.UIContext;

public class EventRenderScreen implements Event {
   private final UIContext context;

   @Generated
   public UIContext getContext() {
      return this.context;
   }

   @Generated
   public EventRenderScreen(UIContext context) {
      this.context = context;
   }
}