package aethereal.core;

public class Processor {
   private final Processor.a a;
   private final Processor.b b;

   public Processor(Processor.a arch, Processor.b type) {
      this.a = arch;
      this.b = type;
   }

   public Processor.a a() {
      return this.a;
   }

   public Processor.b b() {
      return this.b;
   }

   public boolean c() {
      return Processor.a.BIT_32 == this.a;
   }

   public boolean d() {
      return Processor.a.BIT_64 == this.a;
   }

   public boolean e() {
      return Processor.b.AARCH_64 == this.b;
   }

   public boolean f() {
      return Processor.b.IA_64 == this.b;
   }

   public boolean g() {
      return Processor.b.PPC == this.b;
   }

   public boolean h() {
      return Processor.b.RISC_V == this.b;
   }

   public boolean i() {
      return Processor.b.X86 == this.b;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(this.b.a()).append(' ').append(this.a.a());
      return builder.toString();
   }

   public static enum a {
      BIT_32("32-bit"),
      BIT_64("64-bit"),
      UNKNOWN("Unknown");

      private final String d;

      private a(final String label) {
         this.d = label;
      }

      public String a() {
         return this.d;
      }
   }

   public static enum b {
      AARCH_64("AArch64"),
      X86("x86"),
      IA_64("IA-64"),
      PPC("PPC"),
      RISC_V("RISC-V"),
      UNKNOWN("Unknown");

      private final String g;

      private b(final String label) {
         this.g = label;
      }

      public String a() {
         return this.g;
      }
   }
}
