package wtf.wyvern.core.events.impl.player;

import lombok.Generated;
import wtf.wyvern.core.events.callables.EventCancellable;

public class EventMotion extends EventCancellable {
   private float yaw;
   private float pitch;
   private boolean onGround;
   private boolean onGroundSpoofed;
   private double x, y, z;
   private boolean positionSpoofed;
   public static float lastYaw;
   public static float lastPitch;

   public void setYaw(float yaw) {
      this.yaw = yaw;
      lastYaw = yaw;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
      lastPitch = pitch;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
      this.onGroundSpoofed = true;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public boolean isOnGroundSpoofed() {
      return this.onGroundSpoofed;
   }

   public void setPosition(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.positionSpoofed = true;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public boolean isPositionSpoofed() {
      return this.positionSpoofed;
   }

   @Generated
   public EventMotion(float yaw, float pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
      this.onGroundSpoofed = false;
      this.positionSpoofed = false;
   }

   @Generated
   public float getYaw() {
      return this.yaw;
   }

   @Generated
   public float getPitch() {
      return this.pitch;
   }
}