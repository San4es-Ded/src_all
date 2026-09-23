package aethereal.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
   private static final Map<Class<? extends IEvent>, List<EventManager.a>> a = new HashMap<>();
   private static boolean b;
   private static final Set<String> c = new HashSet<>();

   private EventManager() {
   }

   public static void a(Object object) {
      for (Method method : object.getClass().getDeclaredMethods()) {
         if (!a(method)) {
            a(method, object);
         }
      }
   }

   public static void a(Object object, Class<? extends IEvent> eventClass) {
      for (Method method : object.getClass().getDeclaredMethods()) {
         if (!a(method, eventClass)) {
            a(method, object);
         }
      }
   }

   public static void b(Object object) {
      for (List<EventManager.a> dataList : a.values()) {
         for (EventManager.a data : dataList) {
            if (data.a().equals(object)) {
               dataList.remove(data);
            }
         }
      }

      a(true);
   }

   public static void b(Object object, Class<? extends IEvent> eventClass) {
      if (a.containsKey(eventClass)) {
         for (EventManager.a data : a.get(eventClass)) {
            if (data.a().equals(object)) {
               a.get(eventClass).remove(data);
            }
         }

         a(true);
      }
   }

   private static void a(Method method, Object obj) {
      if (!b) {
         Class<?> cls = method.getParameterTypes()[0];
         final EventManager.a aVar = new EventManager.a(obj, method, method.getAnnotation(EventTarget.class).a());
         if (!aVar.b().isAccessible()) {
            aVar.b().setAccessible(true);
         }

         if (!a.containsKey(cls)) {
            a.put((Class<? extends IEvent>)cls, new CopyOnWriteArrayList<EventManager.a>() {
               private static final long serialVersionUID = 666L;

               {
                  this.add(aVar);
               }
            });
         } else {
            boolean z = false;

            for (EventManager.a aVar2 : a.get(cls)) {
               if (aVar2.a() == obj && aVar2.b().equals(method)) {
                  z = true;
                  break;
               }
            }

            if (!z) {
               a.get(cls).add(aVar);
               b((Class<? extends IEvent>)cls);
            }
         }
      }
   }

   public static void a(Class<? extends IEvent> indexClass) {
      Iterator<Entry<Class<? extends IEvent>, List<EventManager.a>>> mapIterator = a.entrySet().iterator();

      while (mapIterator.hasNext()) {
         if (mapIterator.next().getKey().equals(indexClass)) {
            mapIterator.remove();
            return;
         }
      }
   }

   public static void a(boolean onlyEmptyEntries) {
      Iterator<Entry<Class<? extends IEvent>, List<EventManager.a>>> mapIterator = a.entrySet().iterator();

      while (mapIterator.hasNext()) {
         if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty()) {
            mapIterator.remove();
         }
      }
   }

   private static void b(Class<? extends IEvent> indexClass) {
      List<EventManager.a> sortedList = new CopyOnWriteArrayList<>();

      for (byte priority : Priority.f) {
         for (EventManager.a data : a.get(indexClass)) {
            if (data.c() == priority) {
               sortedList.add(data);
            }
         }
      }

      a.put(indexClass, sortedList);
   }

   private static boolean a(Method method) {
      return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
   }

   private static boolean a(Method method, Class<? extends IEvent> eventClass) {
      return a(method) || !method.getParameterTypes()[0].equals(eventClass);
   }

   public static void c() {
      b = true;
      a.clear();
   }

   public static boolean d() {
      return b;
   }

   public static IEvent a(IEvent event) {
      if (b) {
         return event;
      } else {
         List<EventManager.a> dataList = a.get(event.getClass());
         if (dataList != null) {
            for (EventManager.a data : dataList) {
               a(data, event);
            }
         }

         return event;
      }
   }

   private static void a(EventManager.a data, IEvent argument) {
      try {
         data.b().invoke(data.a(), argument);
      } catch (IllegalArgumentException | IllegalAccessException var3) {
         a(data, var3);
      } catch (InvocationTargetException var4) {
         a(data, (Throwable)(var4.getCause() != null ? var4.getCause() : var4));
      }
   }

   private static void a(EventManager.a data, Throwable error) {
      String key = data.a().getClass().getName() + "#" + data.b().getName();
      if (c.add(key)) {
         System.out.println("[Westra] Обработчик " + key + " упал: " + error);
         error.printStackTrace();
      }
   }

   static final class a {
      private final Object a;
      private final Method b;
      private final byte c;

      public a(Object source, Method target, byte priority) {
         this.a = source;
         this.b = target;
         this.c = priority;
      }

      public Object a() {
         return this.a;
      }

      public Method b() {
         return this.b;
      }

      public byte c() {
         return this.c;
      }
   }
}
