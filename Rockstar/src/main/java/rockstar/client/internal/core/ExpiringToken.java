package rockstar.client.internal.core;


import rockstar.client.*;
public interface ExpiringToken {
    public long internalMethod03800();

    default public boolean internalMethod01680() {
        return this.internalMethod03800() <= System.currentTimeMillis();
    }
}

