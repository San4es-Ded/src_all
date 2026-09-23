package wtf.wyvern.core.events.impl.render;

import wtf.wyvern.core.eventbus.events.callables.EventCancellable;
import lombok.Generated;

public class EventAspectRatio extends EventCancellable {
   private float ratio;

   @Generated
   public float getRatio() {
      return this.ratio;
   }

   @Generated
   public void setRatio(float ratio) {
      this.ratio = ratio;
   }
}