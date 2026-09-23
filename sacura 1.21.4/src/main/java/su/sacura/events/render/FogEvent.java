 package su.sacura.events.render;
 
 public class FogEvent {
   private boolean cancelled;
   
   private float distance;
   
   private int color;
   
   public void setCancelled(boolean cancelled) {
     this.cancelled = cancelled;
   }
   
   public void setDistance(float distance) {
     this.distance = distance;
   }
   
   public void setColor(int color) {
     this.color = color;
   }
   
   public float getDistance() {
     return this.distance;
   }
   
   public int getColor() {
     return this.color;
   }
   
   public boolean isCancelled() {
     return this.cancelled;
   }
   
   public void cancel() {
     this.cancelled = true;
   }
 }


