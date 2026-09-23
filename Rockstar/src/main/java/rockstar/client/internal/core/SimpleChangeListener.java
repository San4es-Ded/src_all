package rockstar.client.internal.core;


import rockstar.client.*;
import rockstar.client.internal.core.ChangeListener;

@FunctionalInterface
public interface SimpleChangeListener
extends ChangeListener {
    public void internalMethod01701();

    @Override
    default public <T> void onChange(T t, T t2) {
        this.internalMethod01701();
    }
}

