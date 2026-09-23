package rockstar.client.internal.game;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.core.TickTask;

public class FixedTickScheduler
implements MinecraftClientAccess {
    private long internalField0229 = System.nanoTime();
    private int internalField0227;
    private final boolean internalField0277;
    private int internalField0228 = 0;
    private long internalField0230 = 0L;

    public FixedTickScheduler(boolean bl) {
        this.internalField0277 = bl;
        this.internalField0227 = 0;
    }

    public void internalMethod06569(int n, TickTask ... iIiiIiiii_Class368Array) {
        if (this.internalField0228 != n) {
            this.internalField0230 = 1000000000L / (long)n;
            this.internalField0228 = n;
        }
        long l = System.nanoTime();
        long l2 = l - this.internalField0229;
        this.internalField0227 += (int)(l2 / this.internalField0230);
        this.internalField0229 += (long)this.internalField0227 * this.internalField0230;
        this.internalField0227 = Math.min(this.internalField0227, this.internalField0277 ? Math.min(this.internalField0228, internalField0149.getCurrentFps()) : this.internalField0228);
        while (this.internalField0227 > 0) {
            for (TickTask typedValue249 : iIiiIiiii_Class368Array) {
                typedValue249.internalMethod06201();
            }
            --this.internalField0227;
        }
    }
}

