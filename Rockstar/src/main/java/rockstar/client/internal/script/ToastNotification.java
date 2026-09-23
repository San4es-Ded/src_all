package rockstar.client.internal.script;




import rockstar.client.util.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.util.Stopwatch;

public abstract class ToastNotification {
    private final Stopwatch internalField0519 = new Stopwatch();
    public final AnimatedValue internalField0808;
    public final AnimatedValue internalField0809;
    public final AnimatedValue internalField1321;
    public final long internalField0229;

    public ToastNotification(long l) {
        this.internalField0229 = l;
        this.internalField0808 = new AnimatedValue(300L, Easing.internalField0812);
        this.internalField1321 = new AnimatedValue(300L, Easing.internalField0811);
        this.internalField0809 = new AnimatedValue(300L, Easing.internalField1327);
    }

    public abstract void internalMethod06653(CustomDrawContext localValue1, float localValue2);

    public final void internalMethod01988() {
        this.internalField0808.internalMethod07059(this.internalField0519.internalMethod02365(this.internalField0229) ? 0.0f : 1.0f);
    }

    public final boolean internalMethod01989() {
        return this.internalField0808.internalMethod02881() == 0.0f && this.internalField0519.internalMethod02365(this.internalField0229);
    }

    @Generated
    public Stopwatch internalMethod00512() {
        return this.internalField0519;
    }

    @Generated
    public AnimatedValue internalMethod04206() {
        return this.internalField0808;
    }

    @Generated
    public AnimatedValue internalMethod04953() {
        return this.internalField0809;
    }

    @Generated
    public AnimatedValue internalMethod08559() {
        return this.internalField1321;
    }

    @Generated
    public long internalMethod01987() {
        return this.internalField0229;
    }
}

