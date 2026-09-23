package aethereal.util;

import aethereal.core.Interface;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.class_3532;
import net.minecraft.class_638;

public class RotationAnimator implements Interface {
   private static final Map<Object, RotationAnimator> a = new ConcurrentHashMap<>();
   private static final long b = 90000000L;
   private static final float c = 25.0F;
   private static final float d = 12.0F;
   private final RotationAnimator.a e;
   private final RotationAnimator.a f;
   private boolean g;
   private long h;
   private WeakReference<class_638> i = new WeakReference<>(null);

   private RotationAnimator(float stiffness, float ratio) {
      this.e = new RotationAnimator.a(stiffness, ratio);
      this.f = new RotationAnimator.a(stiffness, ratio);
   }

   private static float a(float speed) {
      return speed * 60.0F + 100.0F;
   }

   private static RotationAnimator a(Object key, float stiffness, float ratio) {
      RotationAnimator animator = a.computeIfAbsent(key, k -> new RotationAnimator(stiffness, ratio));
      animator.e.a(stiffness, ratio);
      animator.f.a(stiffness, ratio);
      return animator;
   }

   public static RotationAnimator a(Object key, float speed) {
      return a(key, a(speed), 0.3F);
   }

   public static RotationAnimator b(Object key, float speed) {
      return a(key, a(speed), 0.6F);
   }

   public static RotationAnimator c(Object key, float speed) {
      return a(key, a(speed), 0.8F);
   }

   public static RotationAnimator d(Object key, float speed) {
      return a(key, a(speed), 0.7F);
   }

   public static RotationAnimator e(Object key, float speed) {
      return a(key, a(speed), 0.3F);
   }

   public static RotationAnimator f(Object key, float stiffness, float damping) {
      return a(key, stiffness, damping);
   }

   public Rotation a(Rotation current, float targetYaw, float targetPitch) {
      class_638 world = aM_.field_1687;
      long now = System.nanoTime();
      if (this.i.get() != world) {
         this.g = false;
         this.i = new WeakReference<>(world);
      }

      if (!this.g || this.a(now) || this.a(current)) {
         float yaw = current != null ? current.c() : targetYaw;
         float pitch = current != null ? current.d() : targetPitch;
         this.e.a(yaw);
         this.f.a(pitch);
         this.g = true;
      }

      this.h = now;
      this.e.b(this.e.a() + class_3532.method_15393(targetYaw - this.e.a()));
      this.f.b(class_3532.method_15363(targetPitch, -90.0F, 90.0F));
      this.e.b();
      this.f.b();
      return new Rotation(this.e.a(), class_3532.method_15363(this.f.a(), -90.0F, 90.0F));
   }

   public Rotation a(Rotation current, Rotation target) {
      return target == null ? current : this.a(current, target.c(), target.d());
   }

   private boolean a(long now) {
      return this.h != 0L && now - this.h > 90000000L;
   }

   private boolean a(Rotation current) {
      return current == null ? false : Math.abs(class_3532.method_15393(current.c() - this.e.a())) > 25.0F || Math.abs(current.d() - this.f.a()) > 12.0F;
   }

   private static final class a {
      private static final float a = 0.05F;
      private static final float b = 0.001F;
      private float c;
      private float d;
      private float e;
      private float f;
      private float g;
      private long h = System.nanoTime();

      a(float stiffness, float ratio) {
         this.a(stiffness, ratio);
      }

      void a(float stiffness, float ratio) {
         this.c = stiffness;
         this.d = ratio * 2.0F * (float)Math.sqrt(stiffness);
      }

      @Generated
      float a() {
         return this.e;
      }

      void b(float target) {
         this.g = target;
      }

      void a(float value) {
         this.e = value;
         this.g = value;
         this.f = 0.0F;
         this.h = System.nanoTime();
      }

      void b() {
         long now = System.nanoTime();
         float delta = Math.min((float)(now - this.h) / 1.0E9F, 0.05F);
         this.h = now;
         if (Math.abs(this.e - this.g) < 0.001F && Math.abs(this.f) < 0.001F) {
            this.e = this.g;
            this.f = 0.0F;
         } else {
            float force = -this.c * (this.e - this.g);
            float damping = -this.d * this.f;
            this.f += (force + damping) * delta;
            this.e = this.e + this.f * delta;
         }
      }
   }
}
