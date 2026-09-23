package haron.gui.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.hud.notifications.oo89jz;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class o1leqh {
    private static final float c = 43.25f;
    private static final float d = 23.0f;
    private static final float e = 12.0f;
    private static final float f = 5.0f;
    private static final float g = 9.5f;
    private static final int h = 5;
    private static final Color i = pryrvd.I;
    private static final Color j = pryrvd.J;
    private static final Color k = pryrvd.N;
    private Consumer<oo89jz> q;
    public static int a;
    public static boolean b;
    private final Map<oo89jz, AnimatedValue> n = new HashMap<oo89jz, AnimatedValue>();
    private final Map<oo89jz, AnimatedValue> o = new HashMap<oo89jz, AnimatedValue>();
    private final Map<oo89jz, Boolean> p = new HashMap<oo89jz, Boolean>();
    private Color r = pryrvd.y;
    private final oo89jz[] l = oo89jz.values();
    private oo89jz m = oo89jz.DEATH;

    public o1leqh() {
        for (oo89jz oo89jz2 : this.l) {
            this.n.put(oo89jz2, new AnimatedValue());
            this.o.put(oo89jz2, new AnimatedValue());
            this.p.put(oo89jz2, false);
            if (oo89jz2 != this.m) continue;
            this.o.get((Object)oo89jz2).d(1.0);
        }
    }

    public static float e() {
        return 5.0f;
    }

    public float b() {
        int n = (int)Math.ceil((double)this.l.length / 5.0);
        return (float)n * 23.0f + (float)(n - 1) * 5.0f;
    }

    public static float c() {
        return 43.25f;
    }

    public static float d() {
        return 23.0f;
    }

    public boolean a(float f, float f2, int n, int n2) {
        for (int i = 0; i < this.l.length; ++i) {
            oo89jz oo89jz2 = this.l[i];
            if (!GuiInput.a(f + (float)(i % 5) * 48.25f, f2 + (float)(i / 5) * 28.0f, 43.25f, 23.0f, (double)n, (double)n2)) continue;
            this.a(oo89jz2);
            if (this.q != null) {
                this.q.accept(oo89jz2);
            }
            return true;
        }
        return false;
    }

    public void a(Color color) {
        this.r = color;
    }

    public void a(Consumer<oo89jz> consumer) {
        this.q = consumer;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        int n3 = (int)(255.0f * f4);
        boolean bl = InteractionOverlayController.a().b();
        for (int i = 0; i < this.l.length; ++i) {
            Color color;
            Color color2;
            Color color3;
            Color color4;
            Color color5;
            Color color6;
            oo89jz oo89jz2 = this.l[i];
            float f5 = f + (float)(i % 5) * 48.25f;
            float f6 = f2 + (float)(i / 5) * 28.0f;
            int n4 = bl ? 0 : (GuiInput.a(f5, f6, 43.25f, 23.0f, (double)n, (double)n2) ? 1 : 0);
            int n5 = n4;
            if (bl && this.p.get((Object)oo89jz2).booleanValue()) {
                this.n.get((Object)oo89jz2).a(0.0, 0.15, Easings.h);
                this.p.put(oo89jz2, false);
            } else if (!bl && BooleanCoercion.from(n5) != this.p.get((Object)oo89jz2)) {
                this.n.get((Object)oo89jz2).a(n5 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
                this.p.put(oo89jz2, BooleanCoercion.from(n5));
            }
            this.n.get((Object)oo89jz2).a();
            this.o.get((Object)oo89jz2).a();
            float f7 = (float)this.n.get((Object)oo89jz2).j();
            float f8 = (float)this.o.get((Object)oo89jz2).j();
            Color color7 = pryrvd.q;
            Color color8 = pryrvd.n;
            Color color9 = pryrvd.f;
            Color color10 = pryrvd.e;
            if (f8 <= 0.01f) {
                color6 = ColorUtils.a(color9, o1leqh.i, f7);
                color5 = ColorUtils.a(color10, j, f7);
                color4 = color7;
                color3 = color8;
            } else {
                color2 = this.r;
                color = pryrvd.c(color2, 150);
                color6 = ColorUtils.a(ColorUtils.a(color9, o1leqh.i, f7), color2, f8);
                color5 = ColorUtils.a(ColorUtils.a(color10, j, f7), color, f8);
                color4 = ColorUtils.a(color7, color2, f8);
                color3 = ColorUtils.a(color8, color, f8);
            }
            color2 = pryrvd.b(color4, f4);
            color = pryrvd.b(color3, f4);
            Color color11 = pryrvd.a(color6, n3);
            Color color12 = pryrvd.a(color5, n3);
            s7swsm2.a(f5 - 0.5f, f6 - 0.5f, 44.25f, 24.0f, 9.5f, color2, color2, color, color, matrixStack);
            s7swsm2.a(f5, f6, 43.25f, 23.0f, 9.5f, color11, color11, color12, color12, matrixStack);
            ClientFonts.e[22].a(oo89jz2.b(), f5 + 15.625f, (double)(f6 + 5.5f), pryrvd.a(f8 <= 0.01f ? ColorUtils.a(pryrvd.b, k, f7) : ColorUtils.a(ColorUtils.a(pryrvd.b, k, f7), pryrvd.aa, f8), n3), matrixStack);
            if (n5 == 0 || bl) continue;
            GuiInput.g();
        }
    }

    public void a(oo89jz oo89jz2) {
        if (this.m != oo89jz2) {
            if (this.m != null) {
                this.o.get((Object)this.m).a(0.0, 0.2, Easings.h);
            }
            this.m = oo89jz2;
            this.o.get((Object)oo89jz2).a(1.0, 0.2, Easings.h);
        }
    }

    public oo89jz a() {
        return this.m;
    }
}

