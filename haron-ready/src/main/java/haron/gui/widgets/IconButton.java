package haron.gui.widgets;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class IconButton {
    private static final float c = 16.0f;
    private float d;
    private float e;
    private float f;
    private final AnimatedValue g = new AnimatedValue();
    private boolean h = false;
    private Consumer<Void> i;
    public static int a;
    public static boolean b;

    public float b() {
        int n = 455;
        return this.e;
    }

    public float c() {
        return this.f;
    }

    public float a() {
        return this.d;
    }

    public void a(Consumer<Void> consumer) {
        this.i = consumer;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2) {
        int n3;
        this.d = f;
        this.e = f2;
        this.f = f3;
        this.g.a();
        boolean bl = InteractionOverlayController.a().b();
        int n4 = n3 = bl || !GuiInput.a(f, f2, f3, f3, (double)n, (double)n2) ? 0 : 1;
        if (bl && this.h) {
            this.g.a(0.0, 0.15, Easings.h);
            this.h = false;
        } else if (!bl && BooleanCoercion.from(n3) != this.h) {
            this.g.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.h = BooleanCoercion.from(n3);
        }
        this.a(matrixStack, s7swsm2, f, f2, f3, (float)this.g.j());
        if (n3 == 0 || bl) {
            return;
        }
        GuiInput.g();
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
        FontRenderer v6hnga2 = ClientFonts.e[16];
        float f5 = v6hnga2.a("");
        float f6 = v6hnga2.b("");
        float f7 = f + (f3 - f5) / 2.0f;
        float f8 = f2 + (f3 - f6) / 2.0f + 4.0f;
        int n = (int)(f4 * 40.0f);
        v6hnga2.a("", f7, (double)f8, new Color(Math.min(255, (0x31 | n) + (0x31 & n)), Math.min(255, (0x31 ^ n) + 2 * (0x31 & n)), Math.min(255, 2 * (0x45 | n) - (0x45 ^ n))), matrixStack);
    }

    public boolean a(int n, int n2) {
        boolean bl = InteractionOverlayController.a().b();
        if (!GuiInput.a(this.d, this.e, this.f, this.f, (double)n, (double)n2) || bl) {
            return false;
        }
        if (this.i != null) {
            this.i.accept(null);
        }
        return true;
    }
}

