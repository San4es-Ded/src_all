package haron.events;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import meteordevelopment.orbit.EventBus;

public final class EventDispatcher {
    public static final EventBus EVENT_BUS = new EventBus();

    private static MethodHandles.Lookup createLookup(Method method, Class<?> clazz) {
        try {
            return (MethodHandles.Lookup)method.invoke(null, clazz, MethodHandles.lookup());
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to create event-handler lookup for " + clazz.getName(), exception);
        }
    }

    private EventDispatcher() {
    }

    static {
        EVENT_BUS.registerLambdaFactory("haron", EventDispatcher::createLookup);
    }
}
