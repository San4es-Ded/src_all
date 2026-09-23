package haron.util;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.render.font.FontRenderer;
import haron.render.ShapeRenderer;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class cqqvax {
    public static final long a = 750L;
    public static final long b = 500L;
    public static final long c = 2500L;
    public static final float d = 0.2f;
    private long g = 0L;
    private boolean h = false;
    private boolean i = false;
    private float j = 0.0f;
    private final AnimatedValue k = new AnimatedValue();
    public static int e;

    public AnimatedValue e() {
        return this.k;
    }

    public boolean b() {
        return this.h;
    }

    public void b(boolean bl) {
        this.h = bl;
    }

    public void c(boolean bl) {
        this.i = bl;
    }

    public boolean c() {
        return this.i;
    }

    public float d() {
        return this.j;
    }

    public float a(String string, FontRenderer v6hnga2, float f, float f2) {
        float f3;
        float f4 = v6hnga2.a(string) * f2;
        if (!this.h || f4 <= f) {
            return 0.0f;
        }
        float f5 = 0.0f;
        if (f4 > f) {
            long l = (System.currentTimeMillis() - this.g) % 2500L;
            f5 = -(f4 - f) * (l >= 750L ? (l >= 1250L ? (l >= 2000L ? 0.0f : 1.0f - (float)(l - 1250L) / 750.0f) : 1.0f) : (float)l / 750.0f);
        }
        if (this.i) {
            this.k.a();
            f3 = (float)this.k.j();
            if (this.k.d()) {
                this.h = false;
                this.i = false;
                f3 = 0.0f;
            }
        } else {
            this.j = f5;
            f3 = f5;
        }
        return f3;
    }

    public void a(float f) {
        this.j = f;
    }

    public void a(boolean bl) {
        if (bl && !this.h) {
            this.h = true;
            this.g = System.currentTimeMillis();
            this.i = false;
        }
        if (bl || !this.h || this.i) {
            return;
        }
        this.i = true;
        this.k.d(this.j);
        this.k.a(0.0, (double)0.2f, Easings.h);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, FontRenderer v6hnga2, String string, float f, float f2, float f3, float f4, Color color, float f5) {
        float f6 = v6hnga2.a(string) * f4;
        float f7 = this.a(string, v6hnga2, f3, f4);
        Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(255.0f * f5));
        if (f6 > f3) {
            s7swsm2.b().a(f, f2 - 5.0f, f3, 20.0f, matrixStack);
        }
        matrixStack.push();
        matrixStack.translate(f + f7, f2, 0.0f);
        matrixStack.scale(f4, f4, 1.0f);
        matrixStack.translate(-(f + f7), -f2, 0.0f);
        v6hnga2.a(string, f + f7, (double)f2, color2, matrixStack);
        matrixStack.pop();
        if (f6 > f3) {
            s7swsm2.b().a(matrixStack);
        }
    }

    public long a() {
        return this.g;
    }
}

