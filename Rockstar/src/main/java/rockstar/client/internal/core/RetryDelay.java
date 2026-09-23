package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.concurrent.TimeUnit;
import lombok.Generated;

public class RetryDelay {
    public static final RetryDelay internalField0873 = new RetryDelay(false, 0L);
    private final boolean internalField0277;
    private final long internalField0229;

    public static RetryDelay internalMethod05625(long l) {
        return RetryDelay.internalMethod05820(l, TimeUnit.MILLISECONDS);
    }

    public static RetryDelay internalMethod05820(long l, TimeUnit timeUnit) {
        return new RetryDelay(true, timeUnit.toMillis(l));
    }

    public void internalMethod02489() throws InterruptedException {
        Thread.sleep(this.internalField0229);
    }

    @Generated
    public boolean internalMethod02490() {
        return this.internalField0277;
    }

    @Generated
    public long internalMethod02488() {
        return this.internalField0229;
    }

    @Generated
    private RetryDelay(boolean bl, long l) {
        this.internalField0277 = bl;
        this.internalField0229 = l;
    }
}

