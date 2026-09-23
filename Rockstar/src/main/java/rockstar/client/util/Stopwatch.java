package rockstar.client.util;


import rockstar.client.*;
import lombok.Generated;

public class Stopwatch {
    private long internalField0229;

    public Stopwatch() {
        this.internalMethod00701();
    }

    public boolean internalMethod02365(long l) {
        return System.currentTimeMillis() - this.internalField0229 >= l;
    }

    public void internalMethod00701() {
        this.internalField0229 = System.currentTimeMillis();
    }

    public long internalMethod00700() {
        return System.currentTimeMillis() - this.internalField0229;
    }

    @Generated
    public long internalMethod00702() {
        return this.internalField0229;
    }

    @Generated
    public void internalMethod02364(long l) {
        this.internalField0229 = l;
    }
}
