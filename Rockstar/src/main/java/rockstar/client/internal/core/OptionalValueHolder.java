package rockstar.client.internal.core;


import rockstar.client.*;
public class OptionalValueHolder<T> {
    private boolean internalField0277 = false;
    private T internalField0290;

    public OptionalValueHolder() {
    }

    public OptionalValueHolder(T t) {
        this.internalMethod01273(t);
    }

    public boolean internalMethod02064() {
        return this.internalField0277;
    }

    public void internalMethod02063() {
        this.internalField0277 = false;
        this.internalField0290 = null;
    }

    public T internalMethod02038() {
        if (!this.internalField0277) {
            throw new IllegalStateException("Value is not set");
        }
        return this.internalField0290;
    }

    public void internalMethod01273(T t) {
        this.internalField0290 = t;
        this.internalField0277 = true;
    }
}

