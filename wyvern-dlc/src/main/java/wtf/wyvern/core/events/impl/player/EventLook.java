package wtf.wyvern.core.events.impl.player;

import lombok.Generated;
import wtf.wyvern.core.events.callables.EventCancellable;

public class EventLook extends EventCancellable {
   private double yaw;
   private double pitch;

   @Generated
   public double getYaw() {
      return this.yaw;
   }

   @Generated
   public double getPitch() {
      return this.pitch;
   }

   @Generated
   public void setYaw(double yaw) {
      this.yaw = yaw;
   }

   @Generated
   public void setPitch(double pitch) {
      this.pitch = pitch;
   }

   @Generated
   public EventLook(double yaw, double pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
   }
}