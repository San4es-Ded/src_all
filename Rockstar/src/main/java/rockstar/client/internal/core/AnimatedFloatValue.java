package rockstar.client.internal.core;



import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;

public class AnimatedFloatValue {
    private float internalField0205;
    private Easing internalField0812 = Easing.internalField1624;
    private AnimatedValue internalField0808 = new AnimatedValue(0L, Easing.internalField1624);

    public float internalMethod02407(float f, int n) {
        n = Math.max(1, n);
        if (this.internalField0808.internalMethod09054() != f) {
            float f2 = this.internalField0808.internalMethod07059(this.internalField0808.internalMethod09054());
            this.internalField0808 = new AnimatedValue(n, f2, this.internalField0812);
            this.internalField0808.internalMethod07059(f);
        }
        this.internalField0205 = this.internalField0808.internalMethod07059(this.internalField0808.internalMethod09054());
        return this.internalField0205;
    }

    public boolean internalMethod01102() {
        return this.internalField0808.internalMethod02884();
    }

    public float internalMethod01101() {
        this.internalField0205 = this.internalField0808.internalMethod07059(this.internalField0808.internalMethod09054());
        return this.internalField0205;
    }

    public AnimatedFloatValue internalMethod04783(Easing typedValue214) {
        this.internalField0812 = typedValue214;
        return this;
    }

    @Generated
    public float internalMethod01106() {
        return this.internalField0205;
    }

    @Generated
    public Easing internalMethod04659() {
        return this.internalField0812;
    }

    @Generated
    public AnimatedValue internalMethod04655() {
        return this.internalField0808;
    }

    @Generated
    public void internalMethod07637(Easing typedValue214) {
        this.internalField0812 = typedValue214;
    }
}

