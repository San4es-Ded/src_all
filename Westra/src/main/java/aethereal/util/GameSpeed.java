package aethereal.util;

public final class GameSpeed {
   private static float speed = 1.0F;

   private GameSpeed() {
   }

   public static float a() {
      return speed;
   }

   public static void a(float value) {
      speed = Math.max(0.05F, value);
   }

   public static void b() {
      speed = 1.0F;
   }
}
