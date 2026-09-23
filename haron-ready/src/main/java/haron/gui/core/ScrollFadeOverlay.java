package haron.gui.core;

import haron.gui.core.ClickGuiOverlay;
import haron.gui.core.ClickGuiScreen;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class ScrollFadeOverlay
implements ClickGuiOverlay {
    public void render(MatrixStack matrices, ShapeRenderer renderer, float x, float y, float width, float height, float opacity) {
        this.a(matrices, renderer, x, y, width, height, opacity);
    }
    private static final int c = 0;
    private static final float d = 5.0f;
    private static final float e = 25.0f;
    private int f;
    private float g;
    private float h;
    private Color i;
    private boolean j;
    private float k;
    private float l;
    private float m;
    private float n;
    public static int a;
    public static boolean b;

    public ScrollFadeOverlay(int n, float f, float f2, Color color) {
        this.g = 5.0f;
        this.h = 25.0f;
        this.j = false;
        this.f = 0;
        this.g = f;
        this.h = f2;
        this.i = color;
    }

    public ScrollFadeOverlay(int n, float f, float f2) {
        this.g = 5.0f;
        this.h = 25.0f;
        this.i = pryrvd.f;
        this.j = false;
        this.f = 0;
        this.g = f;
        this.h = f2;
    }

    public ScrollFadeOverlay() {
        this.f = 0;
        this.g = 5.0f;
        this.h = 25.0f;
        this.i = pryrvd.f;
        this.j = false;
    }

    public void b(float f) {
        this.h = f;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        float f3;
        float f4;
        float f5;
        float f6;
        if (this.f <= 0) {
            return;
        }
        if (this.j) {
            f6 = this.k;
            f5 = this.l;
            f4 = this.m;
            f3 = this.n;
        } else {
            f6 = f;
            f5 = f2;
            f4 = ClickGuiScreen.d();
            f3 = ClickGuiScreen.e();
        }
        Color color = pryrvd.a(this.i, 0);
        s7swsm2.a(f6, f5 + f3 - (float)this.f - this.g, f4, this.f, this.h, color, color, this.i, this.i, matrixStack);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        if (this.f <= 0) {
            return;
        }
        Color color = pryrvd.a(this.i, f5);
        Color color2 = pryrvd.a(this.i, 0);
        s7swsm2.a(f, f2 + f4 - (float)this.f - this.g, f3, this.f, 0.0f, color2, color2, color, color, matrixStack);
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
    }

    public void a(Color color) {
        this.i = color;
    }

    public void a(float f) {
        this.g = f;
    }

    public void a(int n) {
        this.f = 0;
    }

    public void a(float f, float f2, float f3, float f4) {
        this.j = true;
        this.k = f;
        this.l = f2;
        this.m = f3;
        this.n = f4;
    }

    public void a() {
        this.j = false;
    }
}
