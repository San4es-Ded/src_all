package aethereal.util;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class LazyUtil {
   private static final Object a = new Object() {
      @Override
      public String toString() {
         return "null";
      }
   };

   LazyUtil() {
   }

   static Object a(Object value) {
      return value == null ? a : value;
   }

   static <T> T b(Object obj) {
      return obj == a ? null : Cast_2.a(obj);
   }

   static class a<T> implements Lazy<T> {
      private final T a;

      a(T value) {
         this.a = value;
      }

      @Override
      public T a() {
         return this.a;
      }

      public boolean b() {
         return true;
      }

      public void a(T newValue) {
         throw new UnsupportedOperationException();
      }

      @Override
      public String toString() {
         return String.valueOf(this.a);
      }
   }

   static class b<T> implements Lazy<T> {
      private final Supplier<T> a;
      private Object b;

      public b(Supplier<T> supplier) {
         this.a = supplier;
      }

      @Override
      public T a() {
         Object obj = this.b;
         if (obj == null) {
            obj = this.a.get();
            this.b = LazyUtil.a(obj);
         }

         return LazyUtil.b(obj);
      }

      public boolean b() {
         return this.b != null;
      }

      public void a(T newValue) {
         this.b = newValue;
      }
   }

   static class c<T> implements Lazy<T> {
      private final Lock a = new ReentrantLock();
      private final Supplier<T> b;
      private volatile Object c;

      c(Supplier<T> supplier) {
         this.b = supplier;
      }

      @Override
      public T a() {
         Object obj = this.c;
         if (obj == null) {
            this.a.lock();

            try {
               obj = this.c;
               if (obj == null) {
                  obj = this.b.get();
                  this.c = LazyUtil.a(obj);
               }
            } finally {
               this.a.unlock();
            }
         }

         return LazyUtil.b(obj);
      }

      public void a(T newValue) {
         this.c = newValue;
      }

      public void c() {
         this.c = null;
      }

      public boolean b() {
         return this.c != null;
      }

      @Override
      public String toString() {
         return this.b() ? String.valueOf(this.c) : "Lazy value not initialized";
      }
   }

   static class d<T> implements Lazy<T> {
      private final WeakReference<T> a;

      d(T value) {
         this.a = new WeakReference<>(value);
      }

      @Override
      public T a() {
         return this.a.get();
      }

      public boolean b() {
         return true;
      }

      public void a(T newValue) {
         throw new UnsupportedOperationException();
      }

      @Override
      public String toString() {
         return String.valueOf(this.a());
      }
   }
}
