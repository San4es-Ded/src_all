package rockstar.client.internal.script;




import rockstar.client.event.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import pyrock.events.render.ChatRenderEvent;
import pyrock.events.render.GameRendererEvent;
import pyrock.events.render.HandRenderEvent;
import pyrock.events.render.HudLayerRenderEvent;
import pyrock.events.render.HudRenderEvent;
import pyrock.events.render.MenuRenderEvent;
import pyrock.events.render.PostHudLayerRenderEvent;
import pyrock.events.render.PostHudRenderEvent;
import pyrock.events.render.PostMenuRenderEvent;
import pyrock.events.render.PreHudRenderEvent;
import pyrock.events.render.Render3DEvent;
import pyrock.events.render.ScreenRenderEvent;

public class EventBus {
   private final ConcurrentHashMap<Type, CopyOnWriteArrayList<EventListener<?>>> internalField0155 = new ConcurrentHashMap<>();
   private final Map<Class<?>, Field[]> internalField0543 = new HashMap<>();
   private final Comparator<EventListener<?>> internalField0757 = Comparator.<EventListener<?>>comparingInt(localValue0 -> localValue0.internalMethod07175()).reversed();
   private final BiConsumer<List<EventListener<?>>, Comparator<EventListener<?>>> internalField0048 = List::sort;
   private final Consumer<Throwable> internalField0922 = Throwable::printStackTrace;

   public void internalMethod00647(Object localValue1) {
      this.internalMethod05220(localValue1, (localValue1x, localValue2) -> {
         this.internalField0155.computeIfAbsent(localValue1x, localValue0 -> new CopyOnWriteArrayList<>()).add(localValue2);
         this.internalField0048.accept(this.internalField0155.get(localValue1x), this.internalField0757);
      });
   }

   public void internalMethod07237(Object localValue1) {
      this.internalMethod05220(localValue1, (localValue1x, localValue2) -> {
         CopyOnWriteArrayList localValue3 = this.internalField0155.get(localValue1x);
         if (localValue3 != null) {
            localValue3.remove(localValue2);
            if (localValue3.isEmpty()) {
               this.internalField0155.remove(localValue1x);
            }
         }
      });
   }

   public <T extends ClientEvent> void internalMethod06883(T localValue1) {
      Class localValue2 = localValue1.getClass();
      List localValue3 = this.internalField0155.get(localValue2);
      boolean localValue4 = this.internalMethod06884(localValue1);
      if (localValue4) {
         DrawCallCounter.internalMethod08091();
      }

      try {
         RockstarClient.getInstance().internalMethod04979().internalMethod07315(localValue1);
         if (localValue3 != null && !RockstarClient.internalField0240.internalMethod06896()) {
            for (EventListener localValue6 : (Iterable<EventListener>)(Iterable<?>)localValue3) {
               try {
                  localValue6.onEvent(localValue1);
               } catch (Throwable localValue11) {
                  this.internalField0922.accept(localValue11);
               }
            }
         }
      } finally {
         if (localValue4) {
            DrawCallCounter.internalMethod08092();
         }
      }
   }

   private boolean internalMethod06884(ClientEvent localValue1) {
      return localValue1 instanceof PreHudRenderEvent
         || localValue1 instanceof HudRenderEvent
         || localValue1 instanceof PostHudRenderEvent
         || localValue1 instanceof ScreenRenderEvent
         || localValue1 instanceof MenuRenderEvent
         || localValue1 instanceof PostMenuRenderEvent
         || localValue1 instanceof HudLayerRenderEvent
         || localValue1 instanceof PostHudLayerRenderEvent
         || localValue1 instanceof ChatRenderEvent
         || localValue1 instanceof Render3DEvent
         || localValue1 instanceof HandRenderEvent
         || localValue1 instanceof GameRendererEvent;
   }

   private void internalMethod05220(Object localValue1, BiConsumer<Type, EventListener<?>> localValue2) {
      for (Field localValue6 : this.internalMethod03459(localValue1.getClass())) {
         if (localValue6.getType() == EventListener.class) {
            EventListener localValue7 = this.internalMethod01434(localValue1, localValue6);
            if (localValue7 != null) {
               Type localValue8 = ((ParameterizedType)localValue6.getGenericType()).getActualTypeArguments()[0];
               localValue2.accept(localValue8, localValue7);
            }
         }
      }
   }

   private Field[] internalMethod03459(Class<?> localValue1) {
      return this.internalField0543.computeIfAbsent(localValue1, Class::getDeclaredFields);
   }

   private EventListener<?> internalMethod01434(Object localValue1, Field localValue2) {
      boolean localValue3 = localValue2.canAccess(localValue1);
      localValue2.setAccessible(true);

      Object localValue5;
      try {
         return (EventListener<?>)localValue2.get(localValue1);
      } catch (IllegalAccessException localValue9) {
         this.internalField0922.accept(localValue9);
         localValue5 = null;
      } finally {
         localValue2.setAccessible(localValue3);
      }

      return (EventListener<?>)localValue5;
   }
}
