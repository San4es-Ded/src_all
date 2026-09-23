package wtf.wyvern.core.events.impl.render;

import wtf.wyvern.core.eventbus.events.callables.EventCancellable;
import lombok.Generated;

public class EventFog extends EventCancellable {
   private float start;
   private float distance;
   private int color;

   public float getStart() {
      return this.start;
   }

   public void setStart(float start) {
      this.start = start;
   }

   @Generated
   public float getDistance() {
      return this.distance;
   }

   @Generated
   public int getColor() {
      return this.color;
   }

   @Generated
   public void setDistance(float distance) {
      this.distance = distance;
   }

   @Generated
   public void setColor(int color) {
      this.color = color;
   }
}
