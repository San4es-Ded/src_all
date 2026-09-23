package haron.inventory;

import haron.core.BooleanCoercion;

public class nzsxbq {
    public final int a;
    public final int b;
    public final int c;
    public static int d;
    public static boolean e;

    public nzsxbq(int n, int n2, int n3) {
        this.a = n;
        this.b = n2;
        this.c = n3;
    }

    public float b() {
        float f = this.a() ? (float)(this.c + ~this.a + 1) / 20.0f : 0.0f;
        return f;
    }

    public float c() {
        if (this.c <= this.a) {
            return 0.0f;
        }
        int n = this.a;
        int n2 = this.c;
        return (2 * (n2 & ~n) - (n2 ^ n)) / (this.c + ~this.b + 1);
    }

    public boolean a() {
        return BooleanCoercion.from(this.c <= this.a ? 0 : 1);
    }

}

