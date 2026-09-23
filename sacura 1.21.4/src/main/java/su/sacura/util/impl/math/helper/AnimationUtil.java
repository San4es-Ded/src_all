 package su.sacura.util.impl.math.helper;
 
 public class AnimationUtil {
   public static float lerp(float start, float end, float delta) {
     return start + (end - start) * delta;
   }
   
   public static float animate(float target, float current, float speed) {
     if (Math.abs(target - current) < 5.0E-4F)
       return target; 
     return current + (target - current) * speed;
   }
 }


