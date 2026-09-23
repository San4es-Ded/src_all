package rockstar.client.notification;




import rockstar.client.util.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.util.Stopwatch;

public abstract class NotificationView {
    private final Stopwatch internalField0519 = new Stopwatch();
    public final AnimatedValue internalField0808;
    public final AnimatedValue internalField0809;
    public final AnimatedValue internalField1321;
    public final long internalField0229;

    public NotificationView(long l) {
        this.internalField0229 = l;
        this.internalField0808 = new AnimatedValue(300L, Easing.internalField0812);
        this.internalField1321 = new AnimatedValue(300L, Easing.internalField0811);
        this.internalField0809 = new AnimatedValue(300L, Easing.internalField1327);
    }

    public abstract void internalMethod04213(CustomDrawContext localValue1, float localValue2);

    public float internalMethod06598() {
        return 30.0f;
    }

    public final void internalMethod06600() {
        this.internalField0808.internalMethod07059(this.internalField0519.internalMethod02365(this.internalField0229) ? 0.0f : 1.0f);
    }

    public final boolean internalMethod06601() {
        return this.internalField0808.internalMethod02881() == 0.0f && this.internalField0519.internalMethod02365(this.internalField0229);
    }

    @Generated
    public Stopwatch internalMethod02160() {
        return this.internalField0519;
    }

    @Generated
    public AnimatedValue internalMethod05837() {
        return this.internalField0808;
    }

    @Generated
    public AnimatedValue internalMethod06523() {
        return this.internalField0809;
    }

    @Generated
    public AnimatedValue internalMethod09044() {
        return this.internalField1321;
    }

    @Generated
    public long internalMethod06599() {
        return this.internalField0229;
    }
}

