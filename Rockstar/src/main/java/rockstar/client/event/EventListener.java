package rockstar.client.event;


import rockstar.client.*;
import rockstar.client.event.ClientEvent;

public interface EventListener<T extends ClientEvent> {
    public void onEvent(T localValue1);

    default public int internalMethod07175() {
        return 0;
    }

    public static <T extends ClientEvent> EventListener<T> internalMethod05136(final int n, final EventListener<T> typedValue136) {
        return new EventListener<T>(){

            @Override
            public void onEvent(T t) {
                typedValue136.onEvent(t);
            }

            @Override
            public int internalMethod07175() {
                return n;
            }
        };
    }
}

