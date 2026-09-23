package haron.gui.core;

import haron.core.BooleanCoercion;

class GuiHitRegion {
    final float a;
    final float b;
    final float c;
    final float d;
    final float e;
    public static int f;
    public static boolean g;

    GuiHitRegion(float f, float f2, float f3, float f4, float f5) {
        this.a = f;
        this.b = f2;
        this.c = f3;
        this.d = f4;
        this.e = f5;
    }

    boolean a(double d, double d2) {
        return BooleanCoercion.from(d < (double)this.a || d > (double)(this.a + this.c) || d2 < (double)this.b || d2 > (double)(this.b + this.d) ? 0 : 1);
    }

}

