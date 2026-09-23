package rockstar.client.internal.core;


import rockstar.client.*;
public interface ValueTransform<T> {
    public T changed(T localValue1);
}

