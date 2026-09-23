package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

public class MoveEvent extends Event implements IEvent {
   public static Boolean groundOverride;
   private double x;
   private double y;
   private double z;
   private boolean changed;

   public MoveEvent(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public double b() {
      return this.x;
   }

   public double c() {
      return this.y;
   }

   public double d() {
      return this.z;
   }

   public void a(double x) {
      this.x = x;
      this.changed = true;
   }

   public void b(double y) {
      this.y = y;
      this.changed = true;
   }

   public void c(double z) {
      this.z = z;
      this.changed = true;
   }

   public boolean e() {
      return this.changed;
   }

   public void b(boolean onGround) {
      groundOverride = onGround;
   }
}
