package haron.gui.widgets;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class ScrollBar {
    private static final float c = 2.0f;
    private static final float d = 10.0f;
    private static final float e = 15.0f;
    private static final float f = 25.0f;
    private float g = 0.0f;
    private float h = 0.0f;
    private final AnimatedValue i = new AnimatedValue();
    private boolean j = false;
    private float k = 0.0f;
    private float l = 0.0f;
    private final AnimatedValue m = new AnimatedValue();
    private boolean n = false;
    private float o = 0.8f;
    private float p = 10.0f;
    private float q = 15.0f;
    private float r = 25.0f;
    private Color s = pryrvd.b;
    private Color t = pryrvd.d;
    private float u;
    private float v;
    private float w;
    private float x;
    private float y;
    public static int a;
    public static boolean b;

    public ScrollBar() {
    }

    public ScrollBar(float f, float f2) {
        this.o = f / 2.5f;
        this.r = f2;
    }

    public void e() {
        this.g = 0.0f;
        this.h = 0.0f;
        this.i.d(0.0);
        this.j = false;
    }

    public void e(float f) {
        this.g = f;
        this.h = f;
        this.i.d(f);
    }

    public void b(float f, float f2) {
        if (f <= f2) {
            if (this.g > 0.0f || this.h > 0.0f) {
                this.h = 0.0f;
                this.i.a(0.0, 0.15, Easings.h);
                return;
            }
            return;
        }
        float f3 = f - f2;
        if (this.g > f3 || this.h > f3) {
            this.h = f3;
            this.i.a((double)f3, 0.15, Easings.h);
        }
    }

    public float b() {
        return this.g;
    }

    public void b(Color color) {
        this.t = color;
    }

    public void b(float f) {
        this.p = f;
    }

    public void c(float f) {
        this.q = f;
    }

    public boolean c() {
        return this.j;
    }

    public void d() {
        if (this.j) {
            this.j = false;
            this.h = this.g;
            this.i.d(this.g);
        }
    }

    public void d(float f) {
        this.r = f;
    }

    public void a(int n, float f, float f2) {
        float f3;
        if (!this.j || f <= f2) {
            return;
        }
        float f4 = f - f2;
        float f5 = this.w;
        this.g = this.l + ((float)n - this.k) / (f5 - Math.max(this.q, f5 * (f2 / f))) * f4;
        this.g = f3 = Math.max(0.0f, Math.min(this.g, f4));
        this.h = f3;
        this.i.d(this.g);
    }

    public void a(float f, float f2, float f3) {
        if (this.j || f2 <= f3) {
            return;
        }
        float f4 = Math.max(0.0f, f2 - f3);
        this.h -= f * this.r;
        this.h = Math.max(0.0f, Math.min(this.h, f4));
        this.i.a((double)this.h, 0.15, Easings.h);
    }

    public void a(float f) {
        this.o = f;
    }

    public void a(Color color) {
        this.s = color;
    }

    public void a() {
        if (this.j) {
            return;
        }
        this.i.a();
        this.g = (float)this.i.j();
    }

    public boolean a(float f, float f2) {
        return BooleanCoercion.from(f <= f2 ? 0 : 1);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5, int n, int n2, boolean bl) {
        if (f4 > f5) {
            this.u = f;
            this.v = f2;
            this.w = f3;
            this.x = f4;
            this.y = f5;
            float f6 = this.g / (f4 - f5);
            float f7 = Math.max(this.q, f3 * (f5 / f4));
            float f8 = f2 + (f3 - f7) * f6;
            float f9 = f + (2.0f - this.o);
            boolean bl2 = InteractionOverlayController.a().b();
            boolean bl3 = !bl && !bl2 && GuiInput.a(f9 - this.p / 2.0f, f2, this.o + this.p, f3, (double)n, (double)n2);
            int n3 = this.j || bl3 ? 1 : 0;
            int n4 = n3;
            if (bl2 && this.n) {
                this.m.a(0.0, 0.15, Easings.h);
                this.n = false;
            } else if (!bl2 && BooleanCoercion.from(n4) != this.n) {
                this.m.a(n4 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
                this.n = BooleanCoercion.from(n4);
            }
            this.m.a();
            s7swsm2.a(f9, f8, this.o, f7, this.o / 2.0f, pryrvd.SCROLLBAR_THUMB, matrixStack);
        }
    }

    public boolean a(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        float f6 = f + (2.0f - this.o);
        if (f4 > f5 && GuiInput.a(f6 - this.p / 2.0f, f2, this.o + this.p, f3, (double)n, (double)n2)) {
            this.j = true;
            this.k = n2;
            this.l = this.g;
            return true;
        }
        return false;
    }
}

