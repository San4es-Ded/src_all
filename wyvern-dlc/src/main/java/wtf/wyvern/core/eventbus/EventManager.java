package wtf.wyvern.core.eventbus;

import wtf.wyvern.core.eventbus.events.Event;
import wtf.wyvern.core.eventbus.events.EventStoppable;
import wtf.wyvern.core.eventbus.types.Priority;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventManager {

   private static final Map<Class<? extends Event>, List<MethodData>> REGISTRY_MAP = new HashMap<>();

   private EventManager() {
   }

   public static void register(Object object) {
      for (Method method : object.getClass().getDeclaredMethods()) {
         if (!isMethodBad(method)) {
            register(method, object);
         }
      }
   }

   public static void register(Object object, Class<? extends Event> eventClass) {
      for (Method method : object.getClass().getDeclaredMethods()) {
         if (!isMethodBad(method, eventClass)) {
            register(method, object);
         }
      }
   }

   public static void unregister(Object object) {
      for (List<MethodData> dataList : REGISTRY_MAP.values()) {
         dataList.removeIf(data -> data.getSource().equals(object));
      }
      cleanMap(true);
   }

   public static void unregister(Object object, Class<? extends Event> eventClass) {
      List<MethodData> dataList = REGISTRY_MAP.get(eventClass);
      if (dataList != null) {
         dataList.removeIf(data -> data.getSource().equals(object));
         cleanMap(true);
      }
   }

   private static void register(Method method, Object object) {
      if (method.getParameterCount() != 1) {
         return;
      }

      Class<?> paramType = method.getParameterTypes()[0];
      if (!Event.class.isAssignableFrom(paramType)) {
         return;
      }

      @SuppressWarnings("unchecked")
      Class<? extends Event> indexClass = (Class<? extends Event>) paramType;

      EventTarget annotation = method.getAnnotation(EventTarget.class);
      if (annotation == null) {
         return;
      }

      if (!method.canAccess(object)) {
         method.setAccessible(true);
      }

      MethodHandle invoker;
      try {
         invoker = MethodHandles.lookup().unreflect(method).bindTo(object)
                 .asType(MethodType.methodType(void.class, Event.class));
      } catch (IllegalAccessException exception) {
         throw new IllegalStateException("Cannot register event handler " + method, exception);
      }

      MethodData data = new MethodData(object, invoker, annotation.value());

      REGISTRY_MAP.computeIfAbsent(indexClass, k -> new CopyOnWriteArrayList<>()).add(data);
      sortListValue(indexClass);
   }

   public static void removeEntry(Class<? extends Event> indexClass) {
      REGISTRY_MAP.remove(indexClass);
   }

   public static void cleanMap(boolean onlyEmptyEntries) {
      if (onlyEmptyEntries) {
         REGISTRY_MAP.entrySet().removeIf(entry -> entry.getValue().isEmpty());
      } else {
         REGISTRY_MAP.clear();
      }
   }

   private static void sortListValue(Class<? extends Event> indexClass) {
      List<MethodData> list = REGISTRY_MAP.get(indexClass);
      if (list == null || list.isEmpty()) {
         return;
      }

      List<MethodData> sortedList = new CopyOnWriteArrayList<>();
      for (byte priority : Priority.VALUE_ARRAY) {
         for (MethodData data : list) {
            if (data.getPriority() == priority) {
               sortedList.add(data);
            }
         }
      }

      REGISTRY_MAP.put(indexClass, sortedList);
   }

   private static boolean isMethodBad(Method method) {
      return method.getParameterCount() != 1
              || !Event.class.isAssignableFrom(method.getParameterTypes()[0])
              || !method.isAnnotationPresent(EventTarget.class);
   }

   private static boolean isMethodBad(Method method, Class<? extends Event> eventClass) {
      return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
   }

   public static Event call(Event event) {
      List<MethodData> dataList = REGISTRY_MAP.get(event.getClass());
      if (dataList == null) {
         return event;
      }

      if (event instanceof EventStoppable stoppable) {
         for (MethodData data : dataList) {
            invoke(data, event);
            if (stoppable.isStopped()) {
               break;
            }
         }
      } else {
         for (MethodData data : dataList) {
            invoke(data, event);
         }
      }

      return event;
   }

   private static void invoke(MethodData data, Event argument) {
      try {
         data.getInvoker().invokeExact(argument);
      } catch (Throwable ignored) {
      }
   }

   private static final class MethodData {
      private final Object source;
      private final MethodHandle invoker;
      private final byte priority;

      public MethodData(Object source, MethodHandle invoker, byte priority) {
         this.source = source;
         this.invoker = invoker;
         this.priority = priority;
      }

      public Object getSource() {
         return source;
      }

      public MethodHandle getInvoker() {
         return invoker;
      }

      public byte getPriority() {
         return priority;
      }
   }
}
