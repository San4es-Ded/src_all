package aethereal.render;

public final class Motion {
   public static final Motion.Curve SMOOTH = Motion.Curve.bezier(0.42, 0.0, 0.58, 1.0);
   public static final Motion.Curve OUT = Motion.Curve.bezier(0.22, 1.0, 0.36, 1.0);
   public static final Motion.Curve SPRING = Motion.Curve.bezier(0.34, 1.36, 0.64, 1.0);
   public static final Motion.Curve BOUNCE = Motion.Curve.bezier(0.45, 1.45, 0.49, 1.15);
   public static final Motion.Curve IN = Motion.Curve.bezier(0.55, 0.0, 1.0, 0.45);
   public static final Motion.Curve LINEAR = t -> t;
   private long duration;
   private Motion.Curve curve;
   private float value;
   private float from;
   private float target;
   private long start;

   public Motion(long duration, Motion.Curve curve) {
      this(duration, 0.0F, curve);
   }

   public Motion(long duration, float initial, Motion.Curve curve) {
      this.duration = Math.max(1L, duration);
      this.curve = curve;
      this.value = initial;
      this.from = initial;
      this.target = initial;
   }

   public float to(float target) {
      long now = System.currentTimeMillis();
      if (target != this.target) {
         this.from = this.value;
         this.target = target;
         this.start = now;
      }

      long elapsed = now - this.start;
      if (elapsed >= this.duration) {
         this.value = this.target;
      } else {
         float progress = (float)elapsed / (float)this.duration;
         this.value = this.from + (this.target - this.from) * this.curve.ease(progress);
      }

      return this.value;
   }

   public float to(boolean state) {
      return this.to(state ? 1.0F : 0.0F);
   }

   public float value() {
      return this.value;
   }

   public float target() {
      return this.target;
   }

   public boolean done() {
      return this.value == this.target;
   }

   public void set(float value) {
      this.value = value;
      this.from = value;
      this.target = value;
   }

   public void restart(float from) {
      this.value = from;
      this.from = from;
      this.start = System.currentTimeMillis();
   }

   public Motion duration(long duration) {
      this.duration = Math.max(1L, duration);
      return this;
   }

   public Motion curve(Motion.Curve curve) {
      this.curve = curve;
      return this;
   }

   public static float progress(long startedAt, long delay, long duration, Motion.Curve curve) {
      long elapsed = System.currentTimeMillis() - startedAt - delay;
      if (elapsed <= 0L) {
         return 0.0F;
      } else {
         return elapsed >= duration ? 1.0F : curve.ease((float)elapsed / (float)duration);
      }
   }

   @FunctionalInterface
   public interface Curve {
      float ease(float var1);

      static Motion.Curve bezier(double x1, double y1, double x2, double y2) {
         float ax1 = (float)x1;
         float ay1 = (float)y1;
         float ax2 = (float)x2;
         float ay2 = (float)y2;
         return t -> {
            if (t <= 0.0F) {
               return 0.0F;
            } else if (t >= 1.0F) {
               return 1.0F;
            } else {
               float u = t;

               for (int i = 0; i < 8; i++) {
                  float x = component(u, ax1, ax2) - t;
                  float dx = derivative(u, ax1, ax2);
                  if (Math.abs(x) < 1.0E-5F || Math.abs(dx) < 1.0E-6F) {
                     break;
                  }

                  u = Math.max(0.0F, Math.min(1.0F, u - x / dx));
               }

               return component(u, ay1, ay2);
            }
         };
      }

      private static float component(float u, float p1, float p2) {
         float inv = 1.0F - u;
         return 3.0F * inv * inv * u * p1 + 3.0F * inv * u * u * p2 + u * u * u;
      }

      private static float derivative(float u, float p1, float p2) {
         float inv = 1.0F - u;
         return 3.0F * inv * inv * p1 + 6.0F * inv * u * (p2 - p1) + 3.0F * u * u * (1.0F - p2);
      }
   }
}
