package rockstar.client.ui;


import rockstar.client.*;
public final class Insets {
    public static final Insets internalField0910 = new Insets(0.0f, 0.0f, 0.0f, 0.0f);
    public final float internalField0205;
    public final float internalField0206;
    public final float internalField1048;
    public final float internalField1047;

    private Insets(float f, float f2, float f3, float f4) {
        this.internalField0205 = Math.max(0.0f, f);
        this.internalField0206 = Math.max(0.0f, f2);
        this.internalField1048 = Math.max(0.0f, f3);
        this.internalField1047 = Math.max(0.0f, f4);
    }

    public static Insets internalMethod00172(float f) {
        return f <= 0.0f ? internalField0910 : new Insets(f, f, f, f);
    }

    public static Insets internalMethod00105(float f, float f2, float f3, float f4) {
        return new Insets(f, f2, f3, f4);
    }

    public static Insets internalMethod05266(float f, float f2) {
        return new Insets(f, f2, f, f2);
    }

    public static Insets internalMethod02086(float f) {
        return new Insets(0.0f, f, 0.0f, f);
    }

    public static Insets internalMethod08300(float f) {
        return new Insets(f, 0.0f, f, 0.0f);
    }

    public float internalMethod05308() {
        return this.internalField1047 + this.internalField0206;
    }

    public float internalMethod05311() {
        return this.internalField0205 + this.internalField1048;
    }
}

