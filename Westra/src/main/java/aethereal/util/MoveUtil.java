package aethereal.util;

import aethereal.core.Interface;
import aethereal.event.InputEvent;
import lombok.Generated;
import net.minecraft.class_3532;

public class MoveUtil implements Interface {
   private static int b = Integer.MAX_VALUE;
   private static float c;

   @Generated
   private MoveUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean a() {
      return aM_.field_1724.field_3913.field_3905 != 0.0F || aM_.field_1724.field_3913.field_3907 != 0.0F;
   }

   public static void a(InputEvent event, float yaw, int priority) {
      if (priority < b) {
         b = priority;
         c = yaw;
      }
   }

   public static void a(InputEvent event) {
      if (b != Integer.MAX_VALUE) {
         float forward = event.b();
         float strafe = event.c();
         if (forward != 0.0F || strafe != 0.0F) {
            float yaw = c;
            double angle = class_3532.method_15338(Math.toDegrees(a(yaw, forward, strafe)));
            float bestF = 0.0F;
            float bestS = 0.0F;
            float bestDiff = Float.MAX_VALUE;

            for (float pf = -1.0F; pf <= 1.0F; pf++) {
               for (float ps = -1.0F; ps <= 1.0F; ps++) {
                  if (pf != 0.0F || ps != 0.0F) {
                     double predicted = class_3532.method_15338(Math.toDegrees(a(aM_.field_1724.method_36454(), pf, ps)));
                     float diff = (float)Math.abs(angle - predicted);
                     if (diff < bestDiff) {
                        bestDiff = diff;
                        bestF = pf;
                        bestS = ps;
                     }
                  }
               }
            }

            b = Integer.MAX_VALUE;
            event.a(bestF);
            event.b(bestS);
         }
      }
   }

   private static double a(float rotationYaw, double moveForward, double moveStrafing) {
      float f4 = moveForward < 0.0 ? rotationYaw + 180.0F : rotationYaw;
      float f;
      if (moveStrafing > 0.0) {
         float f3;
         if (moveForward < 0.0) {
            f3 = -0.5F;
         } else {
            f3 = moveForward > 0.0 ? 0.5F : 1.0F;
         }

         f = -90.0F * f3;
      } else if (moveStrafing < 0.0) {
         float f2;
         if (moveForward < 0.0) {
            f2 = -0.5F;
         } else {
            f2 = moveForward > 0.0 ? 0.5F : 1.0F;
         }

         f = 90.0F * f2;
      } else {
         f = 0.0F;
      }

      return Math.toRadians(f4 + f);
   }

   public static void b(InputEvent event) {
      event.a(0.0F);
      event.b(0.0F);
   }

   public static boolean a(float under) {
      return aM_.field_1724.method_23318() < 0.0
         ? false
         : aM_.field_1687.method_8600(aM_.field_1724, aM_.field_1724.method_5829().method_989(0.0, -under, 0.0)).iterator().hasNext();
   }

   public static double b() {
      double dx = aM_.field_1724.method_23317() - aM_.field_1724.field_6014;
      double dz = aM_.field_1724.method_23321() - aM_.field_1724.field_5969;
      return Math.sqrt(dx * dx + dz * dz);
   }

   public static double[] c(double speed) {
      float forward = aM_.field_1724.field_3913.field_3905;
      float strafe = aM_.field_1724.field_3913.field_3907;
      float yaw = aM_.field_1724.method_36454();
      if (forward != 0.0F) {
         if (strafe > 0.0F) {
            yaw += forward > 0.0F ? -45.0F : 45.0F;
         } else if (strafe < 0.0F) {
            yaw += forward > 0.0F ? 45.0F : -45.0F;
         }

         strafe = 0.0F;
         forward = forward > 0.0F ? 1.0F : -1.0F;
      } else {
         strafe = strafe > 0.0F ? 1.0F : (strafe < 0.0F ? -1.0F : 0.0F);
      }

      double sin = Math.sin(Math.toRadians(yaw + 90.0F));
      double cos = Math.cos(Math.toRadians(yaw + 90.0F));
      return new double[]{forward * speed * cos + strafe * speed * sin, forward * speed * sin - strafe * speed * cos};
   }
}
