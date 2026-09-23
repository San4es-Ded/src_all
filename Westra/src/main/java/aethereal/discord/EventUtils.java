package aethereal.discord;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EventUtils {
   public static <L> void a(Object eventSource, Class<L> listenerType, L listener) {
      try {
         MethodUtils.b(eventSource, "add" + listenerType.getSimpleName(), listener);
      } catch (ReflectiveOperationException var4) {
         throw new IllegalArgumentException(
            "Unable to add listener for class "
               + eventSource.getClass().getName()
               + " and public add"
               + listenerType.getSimpleName()
               + " method which takes a parameter of type "
               + listenerType.getName()
               + "."
         );
      }
   }

   public static <L> void a(Object target, String methodName, Object eventSource, Class<L> listenerType, String... eventTypes) {
      L listener = listenerType.cast(
         Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]{listenerType}, new EventUtils.a(target, methodName, eventTypes))
      );
      a(eventSource, listenerType, listener);
   }

   static final class a implements InvocationHandler {
      private final Object a;
      private final String b;
      private final Set<String> c;

      a(Object target, String methodName, String[] eventTypes) {
         this.a = target;
         this.b = methodName;
         this.c = new HashSet<>(Arrays.asList(eventTypes));
      }

      private boolean a(Method method) {
         return MethodUtils.a(this.a.getClass(), this.b, method.getParameterTypes()) != null;
      }

      @Override
      public Object invoke(Object proxy, Method method, Object[] parameters) throws Throwable {
         if (!this.c.isEmpty() && !this.c.contains(method.getName())) {
            return null;
         } else {
            return this.a(method) ? MethodUtils.b(this.a, this.b, parameters) : MethodUtils.b(this.a, this.b);
         }
      }
   }
}
