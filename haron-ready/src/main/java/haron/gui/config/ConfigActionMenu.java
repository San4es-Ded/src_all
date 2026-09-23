package haron.gui.config;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.config.ConfigProfileEntry;
import haron.core.BooleanCoercion;
import haron.gui.config.ConfigAction;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class ConfigActionMenu {
    private static final float c = 79.0f;
    private static final float d = 12.0f;
    private static final float e = 11.0f;
    private static final float f = 9.5f;
    private static final float g = 7.0f;
    private static final float h = 6.5f;
    private static final float i = 8.0f;
    private static final float j = 4.0f;
    private static final float k = 3.0f;
    private float m;
    private float n;
    private float o;
    private ConfigProfileEntry p;
    private Consumer<ConfigAction> t;
    private Runnable u;
    public static int a;
    public static boolean b;
    private boolean l = false;
    private final AnimatedValue q = new AnimatedValue();
    private final AnimatedValue[] r = new AnimatedValue[4];
    private final boolean[] s = new boolean[4];

    public ConfigActionMenu() {
        for (int i = 0; i < 4; ++i) {
            this.r[i] = new AnimatedValue();
            this.s[i] = false;
        }
    }

    private String b(ConfigAction gpg0qq2) {
        return this.p != null && this.p.f() && gpg0qq2 == ConfigAction.SAVE_TO ? "Применить" : gpg0qq2.a();
    }

    public boolean b(int n, int n2) {
        return this.l ? GuiInput.a(this.m, this.n, 79.0f, 69.5f, (double)n, (double)n2) : false;
    }

    public boolean b() {
        return this.l;
    }

    public boolean c() {
        return !this.l || this.q.j() < 0.01;
    }

    public ConfigProfileEntry d() {
        return this.p;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, ConfigAction gpg0qq2, Color color) {
        String string;
        switch (gpg0qq2.ordinal()) {
            case 0: {
                string = "";
                break;
            }
            case 1: {
                string = "";
                break;
            }
            case 2: {
                string = "";
                break;
            }
            case 3: {
                string = "";
                break;
            }
            default: {
                return;
            }
        }
        ClientFonts.e[14].a(string, f, (double)f2, color, matrixStack);
    }

    public boolean a(int n, int n2) {
        if (!this.l || this.q.j() < 0.5) {
            return false;
        }
        if (!GuiInput.a(this.m, this.n, 79.0f, 69.5f, (double)n, (double)n2)) {
            this.a();
            return true;
        }
        float f = this.m + 12.0f;
        float f2 = this.n + 11.0f;
        ConfigAction[] gpg0qqArray = ConfigAction.values();
        for (int i = 0; i < gpg0qqArray.length; ++i) {
            if (!GuiInput.a(f - 5.0f, f2 + (float)i * 13.5f - 2.0f, 65.0f, 11.0f, (double)n, (double)n2)) continue;
            if (this.a(gpg0qqArray[i])) {
                return true;
            }
            if (this.t != null) {
                this.t.accept(gpg0qqArray[i]);
            }
            this.a();
            return true;
        }
        return true;
    }

    public void a(Consumer<ConfigAction> consumer) {
        this.t = consumer;
    }

    public void a(float f, float f2, float f3, ConfigProfileEntry lhvtx72) {
        this.o = f + f3 / 2.0f;
        this.m = this.o - 39.5f;
        this.n = f2 + 3.0f;
        this.p = lhvtx72;
        this.l = true;
        this.q.d(0.0);
        this.q.a(1.0, 0.25, Easings.F);
        for (int i = 0; i < 4; ++i) {
            this.r[i].d(0.0);
            this.s[i] = false;
        }
        InteractionOverlayController.a().d(true);
        GuiInput.a(this.m, this.n, 79.0f, 69.5f);
    }

    public void a(float f, float f2, ConfigProfileEntry lhvtx72) {
        this.a(f, f2, 0.0f, lhvtx72);
    }

    public void a() {
        if (this.l) {
            this.q.a(0.0, 0.15, Easings.g);
            InteractionOverlayController.a().d(false);
            GuiInput.a();
        }
    }

    private boolean a(ConfigAction gpg0qq2) {
        if (this.p == null) {
            return false;
        }
        if (this.p.f()) {
            return BooleanCoercion.from(gpg0qq2 == ConfigAction.SAVE_TO ? 0 : 1);
        }
        return this.p.e() && (gpg0qq2 == ConfigAction.DELETE || gpg0qq2 == ConfigAction.RENAME);
    }

    public void a(Runnable runnable) {
        this.u = runnable;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        this.q.a();
        float f = (float)this.q.j();
        if (f < 0.01f) {
            if (this.l && this.q.d()) {
                this.l = false;
                if (this.u != null) {
                    this.u.run();
                    return;
                }
                return;
            }
            return;
        }
        for (int i = 0; i < 4; ++i) {
            this.r[i].a();
        }
        float f2 = Math.max(0.0f, Math.min(1.0f, f));
        int n3 = (int)(255.0f * f2);
        float f3 = 79.0f * f2;
        float f4 = 69.5f * f2;
        float f5 = this.m + 39.5f;
        float f6 = this.n + 34.75f;
        float f7 = f5 - f3 / 2.0f;
        float f8 = f6 - f4 / 2.0f;
        Color color = pryrvd.a(pryrvd.q, n3);
        Color color2 = pryrvd.a(pryrvd.r, n3);
        s7swsm2.a(f7 - 0.5f * f2, f8 - 0.5f * f2, f3 + f2, f4 + f2, 9.5f * f2, color, color, color2, color2, matrixStack);
        Color color3 = pryrvd.a(pryrvd.e, n3);
        Color color4 = pryrvd.a(pryrvd.f, n3);
        s7swsm2.a(f7, f8, f3, f4, 9.5f * f2, color3, color3, color4, color4, matrixStack);
        if (f2 > 0.1f) {
            matrixStack.push();
            matrixStack.translate(f5, f6, 0.0f);
            matrixStack.scale(f2, f2, 1.0f);
            matrixStack.translate(-f5, -f6, 0.0f);
            int n4 = (int)(this.m + 12.0f);
            int n5 = (int)(this.n + 11.0f);
            FontRenderer v6hnga2 = ClientFonts.a[12];
            ConfigAction[] gpg0qqArray = ConfigAction.values();
            for (int i = 0; i < gpg0qqArray.length; ++i) {
                int n6;
                float f9 = (float)n5 + (float)i * 13.5f;
                boolean bl = this.a(gpg0qqArray[i]);
                int n7 = n6 = bl || f2 <= 0.5f || !GuiInput.a(f7 + 12.0f * f2 - 5.0f, f8 + 11.0f * f2 + (float)i * 13.5f * f2 - 2.0f, 55.0f * f2 + 10.0f, 7.0f * f2 + 4.0f, (double)n, (double)n2) ? 0 : 1;
                if (BooleanCoercion.from(n6) != this.s[i]) {
                    this.r[i].a(n6 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
                    this.s[i] = BooleanCoercion.from(n6);
                }
                float f10 = (float)this.r[i].j();
                this.a(matrixStack, s7swsm2, v6hnga2, n4, f9, 1.0f, gpg0qqArray[i], this.b(gpg0qqArray[i]), pryrvd.a(!bl ? (gpg0qqArray[i] != ConfigAction.DELETE ? ColorUtils.a(pryrvd.a, pryrvd.aa, f10) : ColorUtils.a(pryrvd.Z, pryrvd.b(pryrvd.Z, 30), f10)) : pryrvd.b, n3), n3);
                if (n6 == 0) continue;
                GuiInput.g();
            }
            matrixStack.pop();
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, FontRenderer v6hnga2, float f, float f2, float f3, ConfigAction gpg0qq2, String string, Color color, int n) {
        float f4 = 8.0f * f3;
        float f5 = f + f4 + 4.0f * f3;
        this.a(matrixStack, s7swsm2, f, f2, f4, gpg0qq2, color);
        v6hnga2.a(string, f5, (double)f2, color, matrixStack);
    }
}

