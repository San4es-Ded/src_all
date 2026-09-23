package rockstar.client.animation;


import rockstar.client.*;
import lombok.Generated;
import rockstar.client.animation.Easing;

public class AnimatedValue {
    private long internalField0229;
    private float internalField0205;
    private Easing internalField0812;
    private long internalField0230;
    private float internalField0206;
    private float internalField1048;
    private boolean internalField0277;
    private boolean internalField0276;

    public AnimatedValue(long l, float f, Easing typedValue214) {
        this.internalField0229 = l;
        this.internalField0812 = typedValue214;
        this.internalField0205 = f;
        this.internalField0206 = f;
        this.internalField1048 = f;
        this.internalField0277 = true;
    }

    public AnimatedValue(long l, Easing typedValue214) {
        this(l, 0.0f, typedValue214);
    }

    public void internalMethod07062(boolean bl) {
        this.internalMethod07059(bl ? 1.0f : 0.0f);
    }

    public float internalMethod07059(float f) {
        long l;
        long l2 = System.currentTimeMillis();
        if (f != this.internalField1048) {
            this.internalField0206 = this.internalField0205;
            this.internalField1048 = f;
            this.internalField0230 = l2;
            this.internalField0277 = false;
        }
        if ((l = l2 - this.internalField0230) >= this.internalField0229) {
            this.internalField0205 = this.internalField1048;
            this.internalField0277 = true;
            return this.internalField0205;
        }
        float f2 = (float)l / (float)this.internalField0229;
        float f3 = this.internalField0812.ease(f2, 0.0f, 1.0f, 1.0f);
        this.internalField0205 = this.internalField0206 + (this.internalField1048 - this.internalField0206) * f3;
        return this.internalField0205;
    }

    public boolean internalMethod07063(boolean bl) {
        return bl ? this.internalField0205 == this.internalField1048 : this.internalField0205 == 0.0f;
    }

    public void internalMethod07060(float f) {
        this.internalField0205 = f;
        this.internalField0206 = f;
        this.internalField1048 = f;
        this.internalField0277 = true;
    }

    public void internalMethod07123(float f) {
        this.internalField0205 = f;
        this.internalField0206 = f;
        this.internalField1048 = f;
        this.internalField0277 = true;
    }

    public void internalMethod02883() {
        this.internalMethod07123(0.0f);
    }

    public void internalMethod02887() {
        if (this.internalField0276) {
            this.internalMethod07059(1.0f);
        } else {
            this.internalMethod07059(0.0f);
        }
        if (this.internalField0205 == 1.0f) {
            this.internalField0276 = false;
        } else if (this.internalField0205 == 0.0f) {
            this.internalField0276 = true;
        }
    }

    @Generated
    public long internalMethod02882() {
        return this.internalField0229;
    }

    @Generated
    public float internalMethod02881() {
        return this.internalField0205;
    }

    @Generated
    public Easing internalMethod00499() {
        return this.internalField0812;
    }

    @Generated
    public long internalMethod02886() {
        return this.internalField0230;
    }

    @Generated
    public float internalMethod02885() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod09054() {
        return this.internalField1048;
    }

    @Generated
    public boolean internalMethod02884() {
        return this.internalField0277;
    }

    @Generated
    public boolean internalMethod02888() {
        return this.internalField0276;
    }

    @Generated
    public void internalMethod07061(long l) {
        this.internalField0229 = l;
    }

    @Generated
    public void internalMethod06645(Easing typedValue214) {
        this.internalField0812 = typedValue214;
    }

    @Generated
    public void internalMethod07124(long l) {
        this.internalField0230 = l;
    }

    @Generated
    public void internalMethod08856(float f) {
        this.internalField0206 = f;
    }

    @Generated
    public void internalMethod08868(float f) {
        this.internalField1048 = f;
    }

    @Generated
    public void internalMethod07125(boolean bl) {
        this.internalField0277 = bl;
    }

    @Generated
    public void internalMethod08857(boolean bl) {
        this.internalField0276 = bl;
    }
}

