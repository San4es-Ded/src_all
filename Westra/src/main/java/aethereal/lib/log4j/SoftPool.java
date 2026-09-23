package aethereal.lib.log4j;

import java.util.function.Supplier;

public class SoftPool<T> {
   private final ThreadLocal<T> local;

   public SoftPool(Supplier<T> supplier) {
      this.local = ThreadLocal.withInitial(supplier);
   }

   public T a() {
      return this.local.get();
   }

   public void a(T value) {
      this.local.set(value);
   }
}
