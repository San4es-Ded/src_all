package rockstar.client.internal.core;


import rockstar.client.*;
import java.io.IOException;

@FunctionalInterface
public interface IoSupplier<T> {
    public T get() throws IOException;
}

