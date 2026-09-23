package rockstar.client.animation;


import rockstar.client.*;
import rockstar.client.animation.Motion;

public final class AnimatedFloat {
    float internalField0205;
    float internalField0206;
    float internalField1048;
    float internalField1047;
    float internalField1049;
    public boolean internalField0277;
    private Motion internalField0913;

    public AnimatedFloat(Motion typedParameter005) {
        this.internalField0913 = typedParameter005;
    }

    public AnimatedFloat(float f, Motion typedParameter005) {
        this.internalField0913 = typedParameter005;
        this.internalMethod03759(f);
    }

    public AnimatedFloat internalMethod00216(Motion typedParameter005) {
        this.internalField0913 = typedParameter005;
        return this;
    }

    public Motion internalMethod02948() {
        return this.internalField0913;
    }

    public void internalMethod03690(float f) {
        if (!this.internalField0277) {
            this.internalMethod03759(f);
            return;
        }
        if (f != this.internalField0206) {
            this.internalField0206 = f;
            if (this.internalField0913 != null) {
                this.internalField0913.internalMethod07028(this);
            }
        }
    }

    public void internalMethod03759(float f) {
        this.internalField0206 = this.internalField1047 = f;
        this.internalField0205 = this.internalField1047;
        this.internalField1048 = 0.0f;
        this.internalField1049 = 0.0f;
        this.internalField0277 = true;
    }

    public void internalMethod08930(float f) {
        if (!this.internalField0277 || f == 0.0f) {
            return;
        }
        this.internalField0205 += f;
        this.internalField0206 += f;
        this.internalField1047 += f;
    }

    public void internalMethod08946(float f) {
        if (this.internalField0913 != null) {
            this.internalField0913.internalMethod05226(this, f);
        }
    }

    public float internalMethod02046() {
        return this.internalField0205;
    }

    public float internalMethod02051() {
        return this.internalField0206;
    }

    public boolean internalMethod02047() {
        return this.internalField0913 == null || this.internalField0913.internalMethod00265(this);
    }
}
