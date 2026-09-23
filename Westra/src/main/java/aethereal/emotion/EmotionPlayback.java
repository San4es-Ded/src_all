package aethereal.emotion;

import aethereal.core.Interface;
import net.minecraft.class_572;
import net.minecraft.class_630;

public final class EmotionPlayback {
   private static final EmotionPose a = new EmotionPose();
   private static Emotion b;
   private static long c;
   private static float d = 1.0F;
   private static boolean e;
   private static float f;

   private EmotionPlayback() {
   }

   public static void a(Emotion emotion) {
      b = emotion;
      c = System.currentTimeMillis();
      f = 0.0F;
   }

   public static void a() {
      b = null;
      f = 0.0F;
   }

   public static void a(float speed) {
      d = Math.max(0.1F, speed);
   }

   public static void a(boolean looping) {
      e = looping;
   }

   public static boolean b() {
      return b != null;
   }

   public static Emotion c() {
      return b;
   }

   public static float d() {
      return f;
   }

   public static void e() {
      if (b != null) {
         float elapsed = (float)(System.currentTimeMillis() - c) / 1000.0F * d;
         float duration = Math.max(0.1F, b.b());
         if (elapsed >= duration) {
            if (e) {
               c = System.currentTimeMillis();
               f = 0.0F;
            } else {
               a();
            }
         } else {
            f = elapsed / duration;
         }
      }
   }

   public static boolean f() {
      if (Interface.aM_.field_1724 == null) {
         return false;
      } else {
         double speed = Math.hypot(
            Interface.aM_.field_1724.method_23317() - Interface.aM_.field_1724.field_6014,
            Interface.aM_.field_1724.method_23321() - Interface.aM_.field_1724.field_5969
         );
         return speed > 0.02 || Interface.aM_.field_1724.field_6235 > 0 || !Interface.aM_.field_1724.method_24828();
      }
   }

   public static void a(class_572<?> model) {
      if (b != null) {
         a.a();
         b.a(a, f * b.b());
         a(model.field_3398, a.a, a.b, a.c);
         a(model.field_3391, a.d, a.e, a.f);
         a(model.field_27433, a.g, a.h, a.i);
         a(model.field_3401, a.j, a.k, a.l);
         a(model.field_3397, a.m, a.n, a.o);
         a(model.field_3392, a.p, a.q, a.r);
      }
   }

   private static void a(class_630 part, float pitch, float yaw, float roll) {
      part.field_3654 += pitch;
      part.field_3675 += yaw;
      part.field_3674 += roll;
   }
}
