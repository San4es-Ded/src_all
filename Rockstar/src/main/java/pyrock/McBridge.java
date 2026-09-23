package pyrock;


import rockstar.client.internal.network.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
import jep.python.PyObject;
import net.fabricmc.loader.api.FabricLoader;
import rockstar.client.internal.network.ScriptSecurityGuard;
import rockstar.client.RockstarClient;

public final class McBridge {
   private static final String RESOURCE = "/rockstar/mc-mappings.txt.gz";
   private static volatile boolean loaded;
   private static boolean identity;
   private static final Map<String, String> classN2I = new HashMap<>();
   private static final Map<String, Map<String, String>> fieldsByClass = new HashMap<>();
   private static final Map<String, Map<String, List<McBridge.InternalType0213>>> methodsByClass = new HashMap<>();
   private static final Map<String, Class<?>> classCache = new ConcurrentHashMap<>();
   private static final Map<McBridge.InternalType0209, Object> methodCache = new ConcurrentHashMap<>();
   private static final Map<McBridge.InternalType0125, Object> fieldCache = new ConcurrentHashMap<>();
   private static final Object MISS = new Object();

   private McBridge() {
   }

   private static synchronized void ensureLoaded() {
      if (!loaded) {
         try {
            String localValue0 = FabricLoader.getInstance().getMappingResolver().getCurrentRuntimeNamespace();
            identity = "named".equals(localValue0);
            if (!identity) {
               parse();
               RockstarClient.internalField0572
                  .info(
                     "[McBridge] \u043c\u0430\u043f\u043f\u0438\u043d\u0433\u0438 \u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u044b: {} \u043a\u043b\u0430\u0441\u0441\u043e\u0432",
                     classN2I.size()
                  );
            } else {
               RockstarClient.internalField0572
                  .info(
                     "[McBridge] dev-\u0440\u0435\u0436\u0438\u043c (named) \u2014 \u043f\u0435\u0440\u0435\u0432\u043e\u0434 \u043d\u0435 \u043d\u0443\u0436\u0435\u043d"
                  );
            }
         } catch (Throwable localValue1) {
            RockstarClient.internalField0572
               .error("[McBridge] \u043e\u0448\u0438\u0431\u043a\u0430 \u0438\u043d\u0438\u0446\u0438\u0430\u043b\u0438\u0437\u0430\u0446\u0438\u0438", localValue1);
            identity = true;
         }

         loaded = true;
      }
   }

   private static void parse() throws Exception {
      try (InputStream localValue0 = McBridge.class.getResourceAsStream("/rockstar/mc-mappings.txt.gz")) {
         if (localValue0 == null) {
            throw new IllegalStateException(
               "\u0440\u0435\u0441\u0443\u0440\u0441 /rockstar/mc-mappings.txt.gz \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u0432 jar"
            );
         }

         try (BufferedReader localValue1 = new BufferedReader(new InputStreamReader(new GZIPInputStream(localValue0), StandardCharsets.UTF_8))) {
            String localValue2 = null;
            HashMap<String, String> localValue3 = null;
            HashMap<String, List<McBridge.InternalType0213>> localValue4 = null;

            String localValue5;
            while ((localValue5 = localValue1.readLine()) != null) {
               if (!localValue5.isEmpty()) {
                  char localValue6 = localValue5.charAt(0);
                  String[] localValue7 = localValue5.split("\t");
                  if (localValue6 == 'C') {
                     localValue2 = localValue7[1];
                     classN2I.put(localValue7[2], localValue7[1]);
                     localValue3 = new HashMap();
                     localValue4 = new HashMap();
                     fieldsByClass.put((String)localValue2, localValue3);
                     methodsByClass.put((String)localValue2, localValue4);
                  } else if (localValue6 == 'F' && localValue3 != null) {
                     localValue3.put(localValue7[1], localValue7[2]);
                  } else if (localValue6 == 'M' && localValue4 != null) {
                     localValue4.computeIfAbsent(localValue7[2], localValue0x -> new ArrayList<>()).add(new McBridge.InternalType0213(localValue7[3], Integer.parseInt(localValue7[1])));
                  }
               }
            }
         }
      }
   }

   public static Class<?> findClass(String localValue0) {
      ensureLoaded();
      ScriptSecurityGuard.internalMethod03423(localValue0);
      return classCache.computeIfAbsent(localValue0, localValue0x -> {
         String localValue1 = identity ? localValue0x : classN2I.getOrDefault(localValue0x, localValue0x);
         ScriptSecurityGuard.internalMethod03423(localValue1);

         try {
            return Class.forName(localValue1, false, McBridge.class.getClassLoader());
         } catch (ClassNotFoundException localValue3) {
            throw new RuntimeException("\u043a\u043b\u0430\u0441\u0441 \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: " + localValue0x + " (" + localValue1 + ")", localValue3);
         }
      });
   }

   public static boolean isClass(Object localValue0) {
      return localValue0 instanceof Class;
   }

   public static boolean isInstance(Object localValue0, String localValue1) {
      Object localValue2 = unwrap(localValue0);
      return localValue2 != null && findClass(localValue1).isInstance(localValue2);
   }

   public static Object unwrap(Object localValue0) {
      if (localValue0 instanceof PyObject localValue1) {
         try {
            Object localValue2 = localValue1.getAttr("_obj");
            if (localValue2 != null) {
               return localValue2;
            }
         } catch (Throwable localValue3) {
         }
      }

      return localValue0;
   }

   public static boolean hasField(Object localValue0, String localValue1) {
      ensureLoaded();
      Object localValue2 = unwrap(localValue0);
      Class localValue3 = localValue2 instanceof Class localValue4 ? localValue4 : localValue2.getClass();
      return lookupField(localValue3, localValue1) != null;
   }

   public static Object getField(Object localValue0, String localValue1) {
      ensureLoaded();
      Object localValue2 = unwrap(localValue0);
      Class localValue3 = localValue2 instanceof Class localValue4 ? localValue4 : localValue2.getClass();
      Field localValue7 = lookupField(localValue3, localValue1);
      if (localValue7 == null) {
         throw new RuntimeException("\u043d\u0435\u0442 \u043f\u043e\u043b\u044f '" + localValue1 + "' \u0443 " + localValue3.getName());
      } else {
         try {
            Object localValue5 = localValue7.get(localValue2 instanceof Class ? null : localValue2);
            return localValue7.getType() == Object.class ? ScriptSecurityGuard.internalMethod04158(localValue5) : localValue5;
         } catch (IllegalAccessException localValue6) {
            throw new RuntimeException(localValue6);
         }
      }
   }

   public static void setField(Object localValue0, String localValue1, Object localValue2) {
      ensureLoaded();
      Object localValue3 = unwrap(localValue0);
      Class localValue4 = localValue3 instanceof Class localValue5 ? localValue5 : localValue3.getClass();
      Field localValue8 = lookupField(localValue4, localValue1);
      if (localValue8 == null) {
         throw new RuntimeException("\u043d\u0435\u0442 \u043f\u043e\u043b\u044f '" + localValue1 + "' \u0443 " + localValue4.getName());
      } else {
         try {
            localValue8.set(localValue3 instanceof Class ? null : localValue3, coerce(localValue2, localValue8.getType()));
         } catch (IllegalAccessException localValue7) {
            throw new RuntimeException(localValue7);
         }
      }
   }

   public static Object invoke(Object localValue0, String localValue1, Object[] localValue2) {
      ensureLoaded();
      Object localValue3 = unwrap(localValue0);
      boolean localValue4 = localValue3 instanceof Class;
      Class localValue5 = localValue4 ? (Class)localValue3 : localValue3.getClass();
      int localValue6 = localValue2 == null ? 0 : localValue2.length;
      Method localValue7 = lookupMethod(localValue5, localValue1, localValue6);
      if (localValue7 == null) {
         throw new RuntimeException(
            "\u043d\u0435\u0442 \u043c\u0435\u0442\u043e\u0434\u0430 '" + localValue1 + "'(" + localValue6 + " \u0430\u0440\u0433.) \u0443 " + localValue5.getName()
         );
      } else {
         try {
            Object localValue8 = localValue7.invoke(localValue4 ? null : localValue3, coerceAll(localValue2, localValue7.getParameterTypes()));
            return localValue7.getReturnType() == Object.class ? ScriptSecurityGuard.internalMethod04158(localValue8) : localValue8;
         } catch (ScriptSecurityGuard.InternalType0290 localValue9) {
            throw localValue9;
         } catch (Exception localValue10) {
            throw new RuntimeException("\u043e\u0448\u0438\u0431\u043a\u0430 \u0432\u044b\u0437\u043e\u0432\u0430 " + localValue1 + ": " + localValue10.getMessage(), localValue10);
         }
      }
   }

   public static Object construct(String localValue0, Object[] localValue1) {
      return constructClass(findClass(localValue0), localValue1);
   }

   public static Object constructClass(Class<?> localValue0, Object[] localValue1) {
      ensureLoaded();
      ScriptSecurityGuard.internalMethod02572(localValue0);
      int localValue2 = localValue1 == null ? 0 : localValue1.length;

      for (Constructor localValue6 : localValue0.getDeclaredConstructors()) {
         if (localValue6.getParameterCount() == localValue2) {
            try {
               localValue6.setAccessible(true);
               return localValue6.newInstance(coerceAll(localValue1, localValue6.getParameterTypes()));
            } catch (Exception localValue8) {
            }
         }
      }

      throw new RuntimeException(
         "\u043d\u0435\u0442 \u043a\u043e\u043d\u0441\u0442\u0440\u0443\u043a\u0442\u043e\u0440\u0430 "
            + localValue0.getName()
            + " \u0441 "
            + localValue2
            + " \u0430\u0440\u0433."
      );
   }

   public static Iterator<?> iterator(Object localValue0) {
      return unwrap(localValue0) instanceof Iterable localValue1 ? localValue1.iterator() : null;
   }

   private static Field lookupField(Class<?> localValue0, String localValue1) {
      McBridge.InternalType0125 localValue2 = new McBridge.InternalType0125(localValue0, localValue1);
      Object localValue3 = fieldCache.get(localValue2);
      if (localValue3 != null) {
         return localValue3 == MISS ? null : (Field)localValue3;
      } else {
         Field localValue4 = resolveField(localValue0, localValue1);
         ScriptSecurityGuard.internalMethod01473(localValue4);
         if (localValue4 != null) {
            localValue4.setAccessible(true);
         }

         fieldCache.put(localValue2, localValue4 == null ? MISS : localValue4);
         return localValue4;
      }
   }

   private static Method lookupMethod(Class<?> localValue0, String localValue1, int localValue2) {
      McBridge.InternalType0209 localValue3 = new McBridge.InternalType0209(localValue0, localValue1, localValue2);
      Object localValue4 = methodCache.get(localValue3);
      if (localValue4 != null) {
         return localValue4 == MISS ? null : (Method)localValue4;
      } else {
         Method localValue5 = resolveMethod(localValue0, localValue1, localValue2);
         ScriptSecurityGuard.internalMethod05218(localValue5);
         if (localValue5 != null) {
            localValue5.setAccessible(true);
         }

         methodCache.put(localValue3, localValue5 == null ? MISS : localValue5);
         return localValue5;
      }
   }

   private static Field resolveField(Class<?> localValue0, String localValue1) {
      for (Class localValue2 = localValue0; localValue2 != null; localValue2 = localValue2.getSuperclass()) {
         String localValue3 = mapFieldName(localValue2, localValue1);
         Field localValue4 = declaredField(localValue2, localValue3);
         if (localValue4 == null && !localValue1.equals(localValue3)) {
            localValue4 = declaredField(localValue2, localValue1);
         }

         if (localValue4 != null) {
            return localValue4;
         }
      }

      return null;
   }

   private static Field declaredField(Class<?> localValue0, String localValue1) {
      if (localValue1 == null) {
         return null;
      } else {
         try {
            return localValue0.getDeclaredField(localValue1);
         } catch (NoSuchFieldException localValue3) {
            return null;
         }
      }
   }

   private static String mapFieldName(Class<?> localValue0, String localValue1) {
      if (identity) {
         return localValue1;
      } else {
         Map localValue2 = fieldsByClass.get(localValue0.getName());
         return localValue2 == null ? null : (String)localValue2.get(localValue1);
      }
   }

   private static Method resolveMethod(Class<?> localValue0, String localValue1, int localValue2) {
      for (Class localValue3 = localValue0; localValue3 != null; localValue3 = localValue3.getSuperclass()) {
         Method localValue4 = findOnClass(localValue3, localValue1, localValue2);
         if (localValue4 != null) {
            return localValue4;
         }
      }

      for (Class localValue7 : allInterfaces(localValue0)) {
         Method localValue5 = findOnClass(localValue7, localValue1, localValue2);
         if (localValue5 != null) {
            return localValue5;
         }
      }

      return null;
   }

   private static Method findOnClass(Class<?> localValue0, String localValue1, int localValue2) {
      List localValue3 = mapMethodNames(localValue0, localValue1, localValue2);
      Method localValue4 = null;

      for (Method localValue8 : localValue0.getDeclaredMethods()) {
         if (localValue8.getParameterCount() == localValue2) {
            if (localValue3.contains(localValue8.getName())) {
               return localValue8;
            }

            if (localValue4 == null && localValue8.getName().equals(localValue1)) {
               localValue4 = localValue8;
            }
         }
      }

      return localValue4;
   }

   private static List<String> mapMethodNames(Class<?> localValue0, String localValue1, int localValue2) {
      if (identity) {
         return List.of(localValue1);
      } else {
         Map localValue3 = methodsByClass.get(localValue0.getName());
         if (localValue3 == null) {
            return List.of();
         } else {
            List localValue4 = (List)localValue3.get(localValue1);
            if (localValue4 == null) {
               return List.of();
            } else {
               ArrayList localValue5 = new ArrayList(2);

               for (McBridge.InternalType0213 localValue7 : (Iterable<McBridge.InternalType0213>)(Iterable<?>)localValue4) {
                  if (localValue7.argc == localValue2) {
                     localValue5.add(localValue7.inter);
                  }
               }

               return localValue5;
            }
         }
      }
   }

   private static List<Class<?>> allInterfaces(Class<?> localValue0) {
      ArrayList localValue1 = new ArrayList();

      for (Class localValue2 = localValue0; localValue2 != null; localValue2 = localValue2.getSuperclass()) {
         collectInterfaces(localValue2, localValue1);
      }

      return localValue1;
   }

   private static void collectInterfaces(Class<?> localValue0, List<Class<?>> localValue1) {
      for (Class localValue5 : localValue0.getInterfaces()) {
         if (!localValue1.contains(localValue5)) {
            localValue1.add(localValue5);
            collectInterfaces(localValue5, localValue1);
         }
      }
   }

   private static Object[] coerceAll(Object[] localValue0, Class<?>[] localValue1) {
      if (localValue0 == null) {
         return new Object[0];
      } else {
         Object[] localValue2 = new Object[localValue0.length];

         for (int localValue3 = 0; localValue3 < localValue0.length; localValue3++) {
            localValue2[localValue3] = coerce(localValue0[localValue3], localValue3 < localValue1.length ? localValue1[localValue3] : Object.class);
         }

         return localValue2;
      }
   }

   private static Object coerce(Object localValue0, Class<?> localValue1) {
      Object localValue2 = unwrap(localValue0);
      if (localValue2 == null) {
         return null;
      } else {
         if (localValue2 instanceof Number localValue3) {
            if (localValue1 == int.class || localValue1 == Integer.class) {
               return localValue3.intValue();
            }

            if (localValue1 == long.class || localValue1 == Long.class) {
               return localValue3.longValue();
            }

            if (localValue1 == float.class || localValue1 == Float.class) {
               return localValue3.floatValue();
            }

            if (localValue1 == double.class || localValue1 == Double.class) {
               return localValue3.doubleValue();
            }

            if (localValue1 == short.class || localValue1 == Short.class) {
               return localValue3.shortValue();
            }

            if (localValue1 == byte.class || localValue1 == Byte.class) {
               return localValue3.byteValue();
            }
         }

         return localValue2;
      }
   }

   static final class InternalType0125 {
      private final Class<?> owner;
      private final String name;

      InternalType0125(Class<?> localValue1, String localValue2) {
         this.owner = localValue1;
         this.name = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0125[owner=" + this.owner() + ", name=" + this.name() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.owner());
         result = 31 * result + java.util.Objects.hashCode(this.name());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         McBridge.InternalType0125 other = (McBridge.InternalType0125) localValue1;
         return java.util.Objects.equals(this.owner(), other.owner())
            && java.util.Objects.equals(this.name(), other.name());
      }

      public Class<?> owner() {
         return this.owner;
      }

      public String name() {
         return this.name;
      }
   }

   static final class InternalType0209 {
      private final Class<?> owner;
      private final String name;
      private final int argc;

      InternalType0209(Class<?> localValue1, String localValue2, int localValue3) {
         this.owner = localValue1;
         this.name = localValue2;
         this.argc = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0209[owner=" + this.owner() + ", name=" + this.name() + ", argc=" + this.argc() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.owner());
         result = 31 * result + java.util.Objects.hashCode(this.name());
         result = 31 * result + java.util.Objects.hashCode(this.argc());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         McBridge.InternalType0209 other = (McBridge.InternalType0209) localValue1;
         return java.util.Objects.equals(this.owner(), other.owner())
            && java.util.Objects.equals(this.name(), other.name())
            && java.util.Objects.equals(this.argc(), other.argc());
      }

      public Class<?> owner() {
         return this.owner;
      }

      public String name() {
         return this.name;
      }

      public int argc() {
         return this.argc;
      }
   }

   static final class InternalType0213 {
      final String inter;
      final int argc;

      InternalType0213(String localValue1, int localValue2) {
         this.inter = localValue1;
         this.argc = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0213[inter=" + this.inter() + ", argc=" + this.argc() + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.inter());
         result = 31 * result + java.util.Objects.hashCode(this.argc());
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         McBridge.InternalType0213 other = (McBridge.InternalType0213) localValue1;
         return java.util.Objects.equals(this.inter(), other.inter())
            && java.util.Objects.equals(this.argc(), other.argc());
      }

      public String inter() {
         return this.inter;
      }

      public int argc() {
         return this.argc;
      }
   }
}
