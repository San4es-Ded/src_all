package aethereal.util;

import lombok.Generated;

public class CounterUtil {
   private long a;
   private long b;
   private long c;

   @Generated
   public void c(long millis) {
      this.a = millis;
   }

   @Generated
   public void d(long ticks) {
      this.b = ticks;
   }

   public long e() {
      return System.currentTimeMillis() - this.a;
   }

   public CounterUtil() {
      this.b();
   }

   public boolean a(long delay) {
      return System.currentTimeMillis() - delay >= this.a;
   }

   public boolean a(long delay, long jitter) {
      return System.currentTimeMillis() - (delay + this.c % (jitter + 1L)) >= this.a;
   }

   public boolean b(long delay) {
      return this.b >= delay;
   }

   public boolean b(long delay, long jitter) {
      return this.b >= delay + this.c % (jitter + 1L);
   }

   public void a() {
      this.b++;
   }

   public void b() {
      this.a = System.currentTimeMillis();
      this.c = (long)(Math.random() * 9.223372E18F);
      this.b = 0L;
   }

   public long c() {
      return System.currentTimeMillis() - this.a;
   }

   public long d() {
      return this.b;
   }
}
