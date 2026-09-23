package aethereal.util;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Timer implements Serializable {
   private static final long serialVersionUID = 9175191792439630013L;
   private final String a;
   private Timer.State b;
   private long c;
   private final int d;
   private static long e = 1000000000L;
   private static long f = e * 60L;
   private static long g = f * 60L;
   private ThreadLocal<Long> h = new ThreadLocal<Long>() {
      public Long initialValue() {
         return 0L;
      }
   };

   public Timer(String name) {
      this(name, 0);
   }

   public Timer(String name, int iterations) {
      this.a = name;
      this.b = Timer.State.Stopped;
      this.d = iterations > 0 ? iterations : 0;
   }

   public synchronized void a() {
      this.h.set(System.nanoTime());
      this.c = 0L;
      this.b = Timer.State.Started;
   }

   public synchronized void b() {
      if (this.b == Timer.State.Stopped) {
         this.a();
      } else {
         this.e();
      }
   }

   public synchronized String c() {
      this.c = this.c + (System.nanoTime() - this.h.get());
      this.h.set(0L);
      this.b = Timer.State.Stopped;
      return this.toString();
   }

   public synchronized void d() {
      this.c = this.c + (System.nanoTime() - this.h.get());
      this.h.set(0L);
      this.b = Timer.State.Paused;
   }

   public synchronized void e() {
      this.h.set(System.nanoTime());
      this.b = Timer.State.Started;
   }

   public String f() {
      return this.a;
   }

   public long g() {
      return this.c / 1000000L;
   }

   public long h() {
      return this.c;
   }

   public Timer.State i() {
      return this.b;
   }

   @Override
   public String toString() {
      StringBuilder result = new StringBuilder();
      this.a(result);
      return result.toString();
   }

   public void a(StringBuilder buffer) {
      buffer.append("Timer ").append(this.a);
      switch (this.b) {
         case Started:
            buffer.append(" started");
            break;
         case Stopped:
            long nanoseconds = this.c;
            long hours = nanoseconds / g;
            long nanoseconds2 = nanoseconds % g;
            long minutes = nanoseconds2 / f;
            long nanoseconds3 = nanoseconds2 % f;
            long seconds = nanoseconds3 / e;
            long nanoseconds4 = nanoseconds3 % e;
            String elapsed = "";
            if (hours > 0L) {
               elapsed = elapsed + hours + " hours ";
            }

            if (minutes > 0L || hours > 0L) {
               elapsed = elapsed + minutes + " minutes ";
            }

            DecimalFormat numFormat = new DecimalFormat("#0");
            String elapsed2 = elapsed + numFormat.format(seconds) + ".";
            DecimalFormat numFormat2 = new DecimalFormat("000000000");
            buffer.append(" stopped. Elapsed time: ").append(elapsed2 + numFormat2.format(nanoseconds4) + " seconds");
            if (this.d > 0) {
               long nanoseconds5 = this.c / this.d;
               long hours2 = nanoseconds5 / g;
               long nanoseconds6 = nanoseconds5 % g;
               long minutes2 = nanoseconds6 / f;
               long nanoseconds7 = nanoseconds6 % f;
               long seconds2 = nanoseconds7 / e;
               long nanoseconds8 = nanoseconds7 % e;
               String elapsed3 = "";
               if (hours2 > 0L) {
                  elapsed3 = elapsed3 + hours2 + " hours ";
               }

               if (minutes2 > 0L || hours2 > 0L) {
                  elapsed3 = elapsed3 + minutes2 + " minutes ";
               }

               DecimalFormat numFormat3 = new DecimalFormat("#0");
               String elapsed4 = elapsed3 + numFormat3.format(seconds2) + ".";
               DecimalFormat numFormat4 = new DecimalFormat("000000000");
               buffer.append(" Average per iteration: ").append(elapsed4 + numFormat4.format(nanoseconds8) + " seconds");
            }
            break;
         case Paused:
            buffer.append(" paused");
            break;
         default:
            buffer.append(' ').append(this.b);
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Timer timer)) {
         return false;
      } else if (this.c == timer.c && this.h == timer.h) {
         if (this.a != null) {
            if (!this.a.equals(timer.a)) {
               return false;
            }
         } else if (timer.a != null) {
            return false;
         }

         return this.b != null ? this.b.equals(timer.b) : timer.b == null;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.a != null ? this.a.hashCode() : 0;
      int result2 = 29 * result + (this.b != null ? this.b.hashCode() : 0);
      long time = this.h.get();
      return 29 * (29 * result2 + (int)(time ^ time >>> 32)) + (int)(this.c ^ this.c >>> 32);
   }

   public static enum State {
      Started,
      Stopped,
      Paused;
   }
}
