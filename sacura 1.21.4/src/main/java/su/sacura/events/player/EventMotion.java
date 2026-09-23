 package su.sacura.events.player;
 
 public class EventMotion {
   private double x;
   
   private double y;
   
   private double z;
   
   private float yaw;
   
   private float pitch;
   
   private boolean onGround;
   
   public boolean isCancel;
   
   public EventMotion(float yaw, float pitch) {
     this.yaw = yaw;
     this.pitch = pitch;
   }
   
   public EventMotion(double x, double y, double z, float yaw, float pitch, boolean onGround) {
     this.x = x;
     this.y = y;
     this.z = z;
     this.yaw = yaw;
     this.pitch = pitch;
     this.onGround = onGround;
   }
   
   public double getX() {
     return this.x;
   }
   
   public void setX(double x) {
     this.x = x;
   }
   
   public double getY() {
     return this.y;
   }
   
   public void setY(double y) {
     this.y = y;
   }
   
   public double getZ() {
     return this.z;
   }
   
   public void setZ(double z) {
     this.z = z;
   }
   
   public float getYaw() {
     return this.yaw;
   }
   
   public void setYaw(float yaw) {
     this.yaw = yaw;
   }
   
   public float getPitch() {
     return this.pitch;
   }
   
   public void setPitch(float pitch) {
                this.pitch = pitch;
            }
   
   public boolean isOnGround() {
     return this.onGround;
   }
   
   public void setOnGround(boolean onGround) {
     this.onGround = onGround;
   }
   
   public boolean isCancel() {
     return this.isCancel;
   }
 }


