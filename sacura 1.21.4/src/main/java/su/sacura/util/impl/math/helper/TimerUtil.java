 package su.sacura.util.impl.math.helper;
 
 public class TimerUtil {
   public long lastMS = System.currentTimeMillis();
   
   public void reset() {
     this.lastMS = System.currentTimeMillis();
   }
   
   public boolean hasTimeElapsed(long time, boolean reset) {
     if (System.currentTimeMillis() - this.lastMS > time) {
       if (reset)
         reset(); 
       return true;
     } 
     return false;
   }
   
   public long getLastMS() {
     return this.lastMS;
   }
   
   public void setLastMC() {
     this.lastMS = System.currentTimeMillis();
   }
   
   public boolean hasTimeElapsed(long time) {
     return (System.currentTimeMillis() - this.lastMS > time);
   }
   
   public long getTime() {
     return System.currentTimeMillis() - this.lastMS;
   }
   
   public void setTime(long time) {
     this.lastMS = time;
   }
 }


