 package su.sacura.util.impl.math.helper;
 
 import java.math.BigDecimal;
 import java.math.RoundingMode;
 import net.minecraft.util.math.MathHelper;
 import net.minecraft.util.math.Vec3d;
 import su.sacura.util.type.MinecraftWrapper;
 
 public class MathUtil implements MinecraftWrapper {
   public static double round(double value, int places) {
     if (places < 0)
       throw new IllegalArgumentException(); 
     BigDecimal bd = BigDecimal.valueOf(value);
     bd = bd.setScale(places, RoundingMode.HALF_UP);
     return bd.doubleValue();
   }
   
   public static double fpsTime() {
     int fps = mc.getCurrentFps();
     return (fps > 0) ? (1.0D / fps) : 0.016666666666666666D;
   }
   
   public static float lerp(float end, float start, float multiple) {
     return (float)(end + (start - end) * MathHelper.clamp(fpsTime() * multiple, 0.0D, 1.0D));
   }
   
   public static float random(float min, float max) {
     return (float)(Math.random() * (max - min) + min);
   }
   
   public static float clamp(float value, float min, float max) {
     return Math.max(min, Math.min(max, value));
   }
   
   public static Vec3d interpolatePos(float prevX, float prevY, float prevZ, float x, float y, float z) {
     Vec3d camPos = (mc.getEntityRenderDispatcher()).camera.getPos();
     double delta = MinecraftWrapper.tickCounter.getTickDelta(true);
     return new Vec3d(interpolate(prevX, x, delta).doubleValue() - camPos.x, interpolate(prevY, y, delta).doubleValue() - camPos.y, interpolate(prevZ, z, delta).doubleValue() - camPos.z);
   }
   
   public static Double interpolate(double old, double value, double interpolation) {
     return Double.valueOf(old + (value - old) * interpolation);
   }
 }


