package haron.hud.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.render.ShapeRenderer;
import java.awt.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ijsfy3 {
    protected static final MinecraftClient c = MinecraftClient.getInstance();
    protected final AnimatedValue d = new AnimatedValue();
    protected final AnimatedValue e = new AnimatedValue();
    protected final AnimatedValue f = new AnimatedValue();
    protected float g = 8.0f;
    protected float h = 4.0f;
    protected float i = 12.0f;
    protected long j = 1000L;
    protected boolean l = false;
    protected long k = System.currentTimeMillis();

    public ijsfy3() {
        this.f.d(0.0);
        this.f.a(1.0, 0.2, Easings.k);
    }

    public void e() {
        this.d.a();
        this.e.a();
        this.f.a();
        if (this.l || System.currentTimeMillis() - this.k <= this.j) {
            return;
        }
        this.f();
    }

    public float i() {
        return (float)this.f.j();
    }

    public abstract float b();

    public float c() {
        return this.g + this.a() + this.g;
    }

    public boolean h() {
        return this.l;
    }

    public void f() {
        if (this.l) {
            return;
        }
        this.l = true;
        this.f.a(0.0, 0.2, Easings.j);
    }

    public float l() {
        return this.i;
    }

    public float d() {
        return this.h + this.b() + this.h;
    }

    public abstract float a();

    protected Color a(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * f));
    }

    public abstract void a(MatrixStack var1, ShapeRenderer var2, float var3, float var4, float var5, float var6, float var7);

    public float k() {
        return this.h;
    }

    public boolean g() {
        return this.l && !(this.f.j() > 0.01);
    }

    public float j() {
        return this.g;
    }
}

