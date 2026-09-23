package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;

public class MotionEvent extends Event implements IEvent {
   private double a;
   private double b;
   private double c;
   private float d;
   private float e;
   private boolean f;
   private boolean g;
   private boolean h;

   @Generated
   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else {
         return !(o instanceof MotionEvent other)
            ? false
            : other.a(this)
               && super.equals(o)
               && Double.compare(this.b(), other.b()) == 0
               && Double.compare(this.c(), other.c()) == 0
               && Double.compare(this.d(), other.d()) == 0
               && Float.compare(this.e(), other.e()) == 0
               && Float.compare(this.f(), other.f()) == 0
               && this.g() == other.g()
               && this.h() == other.h()
               && this.i() == other.i();
      }
   }

   @Generated
   protected boolean a(Object other) {
      return other instanceof MotionEvent;
   }

   @Generated
   @Override
   public int hashCode() {
      int result = super.hashCode();
      long $x = Double.doubleToLongBits(this.b());
      int result2 = result * 59 + (int)($x >>> 32 ^ $x);
      long $y = Double.doubleToLongBits(this.c());
      int result3 = result2 * 59 + (int)($y >>> 32 ^ $y);
      long $z = Double.doubleToLongBits(this.d());
      return (
               (
                        (((result3 * 59 + (int)($z >>> 32 ^ $z)) * 59 + Float.floatToIntBits(this.e())) * 59 + Float.floatToIntBits(this.f())) * 59
                           + (this.g() ? 79 : 97)
                     )
                     * 59
                  + (this.h() ? 79 : 97)
            )
            * 59
         + (this.i() ? 79 : 97);
   }

   @Generated
   public void a(double x) {
      this.a = x;
   }

   @Generated
   public void b(double y) {
      this.b = y;
   }

   @Generated
   public void c(double z) {
      this.c = z;
   }

   @Generated
   public void a(float yaw) {
      this.d = yaw;
   }

   @Generated
   public void b(float pitch) {
      this.e = pitch;
   }

   @Generated
   public void b(boolean onGround) {
      this.f = onGround;
   }

   @Generated
   public void c(boolean isCrouching) {
      this.g = isCrouching;
   }

   @Generated
   public void d(boolean isSprinting) {
      this.h = isSprinting;
   }

   @Generated
   @Override
   public String toString() {
      double dB = this.b();
      double dC = this.c();
      double d = this.d();
      float fE = this.e();
      float f = this.f();
      this.g();
      this.h();
      this.i();
      return "MotionEvent(x="
         + dB
         + ", y="
         + dB
         + ", z="
         + dC
         + ", yaw="
         + dB
         + ", pitch="
         + d
         + ", onGround="
         + dB
         + ", isCrouching="
         + fE
         + ", isSprinting="
         + f
         + ")";
   }

   @Generated
   public double b() {
      return this.a;
   }

   @Generated
   public double c() {
      return this.b;
   }

   @Generated
   public double d() {
      return this.c;
   }

   @Generated
   public float e() {
      return this.d;
   }

   @Generated
   public float f() {
      return this.e;
   }

   @Generated
   public boolean g() {
      return this.f;
   }

   @Generated
   public boolean h() {
      return this.g;
   }

   @Generated
   public boolean i() {
      return this.h;
   }

   public MotionEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean isCrouching, boolean isSprinting) {
      this.a = x;
      this.b = y;
      this.c = z;
      this.d = yaw;
      this.e = pitch;
      this.f = onGround;
      this.g = isCrouching;
      this.h = isSprinting;
   }
}
