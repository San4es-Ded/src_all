package rockstar.client.internal.core;


import rockstar.client.*;
@FunctionalInterface
public interface ChangeListener {
    public <T> void onChange(T localValue1, T localValue2);
}

