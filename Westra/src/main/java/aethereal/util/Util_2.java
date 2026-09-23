package aethereal.util;

public class Util_2 {
   private static Util_2.a a;
   private static boolean b = false;

   private Util_2() {
   }

   public static String a(String key) {
      if (key == null) {
         throw new IllegalArgumentException("null input");
      } else {
         String result = null;

         try {
            result = System.getProperty(key);
         } catch (SecurityException var3) {
         }

         return result;
      }
   }

   public static boolean b(String key) {
      String value = a(key);
      return value == null ? false : value.equalsIgnoreCase("true");
   }

   private static Util_2.a b() {
      if (a != null) {
         return a;
      } else if (b) {
         return null;
      } else {
         a = c();
         b = true;
         return a;
      }
   }

   private static Util_2.a c() {
      try {
         return new Util_2.a();
      } catch (SecurityException var1) {
         return null;
      }
   }

   public static Class<?> a() {
      Util_2.a securityManager = b();
      if (securityManager == null) {
         return null;
      } else {
         Class<?>[] trace = securityManager.getClassContext();
         String thisClassName = Util_2.class.getName();
         int i = 0;

         while (i < trace.length && !thisClassName.equals(trace[i].getName())) {
            i++;
         }

         if (i < trace.length && i + 2 < trace.length) {
            return trace[i + 2];
         } else {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; this should not happen");
         }
      }
   }

   public static final void a(String msg, Throwable t) {
      System.err.println(msg);
      System.err.println("Reported exception:");
      t.printStackTrace();
   }

   public static final void c(String msg) {
      System.err.println("SLF4J: " + msg);
   }

   static final class a extends SecurityManager {
      private a() {
      }

      @Override
      protected Class<?>[] getClassContext() {
         return super.getClassContext();
      }
   }
}
