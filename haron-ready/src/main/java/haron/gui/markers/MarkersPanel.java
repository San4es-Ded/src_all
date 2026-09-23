package haron.gui.markers;

import haron.core.BooleanCoercion;
import haron.gui.core.CategorySelectionModel;
import haron.gui.core.CategorySelectorOverlay;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiScreen;
import haron.gui.core.ClickGuiTabType;
import haron.gui.markers.MarkerSettingsPanel;
import haron.gui.notifications.f6g0cs;
import haron.gui.notifications.u5dyg4;
import haron.hud.notifications.eq8z6w;
import haron.render.ShapeRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class MarkersPanel
implements CategorySelectionModel,
ClickGuiTab {
    private static final String[] c = new String[]{"Метки", "Настройки"};
    private static final float d = 20.0f;
    private static final float e = 19.0f;
    private static final float f = 5.0f;
    private int m;
    private int n;
    public static int a;
    public static boolean b;
    private int g = 0;
    private boolean o = false;
    private final CategorySelectorOverlay h = new CategorySelectorOverlay(this);
    private final MarkerSettingsPanel i = new MarkerSettingsPanel();
    private final ScrollFadeOverlay j = new ScrollFadeOverlay(25, 10.0f, 7.5f);
    private final u5dyg4 k = new u5dyg4();
    private final f6g0cs l = new f6g0cs();

    public MarkersPanel() {
        this.k.a(eq8z6w2 -> {
            int n = 806;
            this.l.a((eq8z6w)eq8z6w2);
        });
        this.k.b(eq8z6w2 -> {
            this.l.a((eq8z6w)eq8z6w2);
        });
        this.l.a(eq8z6w2 -> {
            eq8z6w eq8z6w3 = this.k.b((eq8z6w)eq8z6w2);
            if (eq8z6w3 == null) {
                this.l.a((eq8z6w)null);
            } else {
                this.k.c(eq8z6w3);
                this.l.a(eq8z6w3);
            }
        });
    }

    public void e() {
        this.l.c();
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        float f3 = ClickGuiScreen.d();
        float f4 = ClickGuiScreen.e();
        float f5 = f2 + 20.0f;
        float f6 = f4 - 20.0f - 9.5f + 50.0f;
        float f7 = f + 19.0f;
        float f8 = f2 + f4 - 20.0f;
        float f9 = f8 - f5;
        float f10 = f6 - 5.0f - 7.0f - 20.0f;
        float f11 = f8 - f10;
        this.k.a(matrixStack, s7swsm2, f7, f11, f10, n, n2, 1.0f);
        float f12 = f7 + 115.5f + 5.0f;
        float f13 = f3 - 38.0f - 115.5f - 5.0f;
        if (this.k.g()) {
            this.a(matrixStack, s7swsm2, f12, f5, f13, f9);
        } else if (this.l.a() == null) {
            this.b(matrixStack, s7swsm2, f12, f5, f13, f9);
        } else {
            this.l.a(matrixStack, s7swsm2, f12, f5, f13, f9, n, n2, 1.0f);
        }
        this.l.a(matrixStack, s7swsm2, n, n2, 1.0f);
    }

    @Override
    public boolean b() {
        if (this.g == 0) {
            return BooleanCoercion.from(this.k.f() || this.l.d() ? 1 : 0);
        }
        return this.i.b();
    }

    @Override
    public void b(float f, float f2, int n, int n2) {
        if (this.g != 0) {
            this.i.b(f, f2, n, n2);
        } else if (this.l.a(n, n2)) {
            // empty if block
        }
    }

    @Override
    public String[] c() {
        return c;
    }

    @Override
    public void c(float f, float f2, int n, int n2) {
        if (this.g != 0) {
            this.i.c(f, f2, n, n2);
            return;
        }
        this.k.a(n, n2);
        this.l.b(n, n2);
    }

    public void f() {
        this.i.c();
    }

    @Override
    public int d() {
        return this.g;
    }

    public boolean a(char c, int n) {
        return this.g == 0 && (this.k.a(c, n) || this.l.a(c, n));
    }

    @Override
    public void a(int n) {
        if (n < 0 || n >= c.length || this.g == n) {
            return;
        }
        this.g = n;
        if (n != 1) {
            this.i.c();
        } else {
            this.i.a();
        }
        this.l.c();
    }

    @Override
    public ClickGuiTabType a() {
        return ClickGuiTabType.MARKERS;
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
        this.h.a(f, f2, n, n2);
        if (this.g != 0) {
            this.i.a(f, f2, n, n2);
            return;
        }
        float f3 = ClickGuiScreen.d();
        float f4 = ClickGuiScreen.e();
        float f5 = f2 + 20.0f;
        float f6 = f2 + f4 - 20.0f;
        float f7 = f4 - 20.0f - 9.5f - 5.0f + 50.0f;
        float f8 = f + 19.0f;
        float f9 = f8 + 115.5f + 5.0f;
        float f10 = f3 - 38.0f - 115.5f - 5.0f;
        float f11 = f7 - 7.0f - 20.0f;
        float f12 = f6 - f11;
        float f13 = f6 - f5;
        if (this.l.a() != null && this.l.a(f9, f5, f10, f13, n, n2) || this.k.a(f8, f12, f11, n, n2)) {
            // empty if block
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4) {
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.m = n;
        this.n = n2;
        this.g();
        this.h.a(matrixStack, s7swsm2, f, f2, n, n2);
        if (this.g == 0) {
            this.b(matrixStack, s7swsm2, f, f2, n, n2);
        } else {
            this.i.a(matrixStack, s7swsm2, f, f2, n, n2);
            this.j.a(matrixStack, s7swsm2, f, f2, n, n2);
        }
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        if (this.g == 1) {
            this.i.a(matrixStack, s7swsm2, n, n2);
        }
    }

    @Override
    public void a(float f) {
        if (this.g != 0) {
            this.i.a(f);
        } else {
            this.k.a(f, this.m, this.n);
        }
    }

    @Override
    public boolean a(int n, int n2, int n3) {
        if (this.g != 0) {
            if (this.i.a(n, n2, n3)) {
                return true;
            }
        } else {
            if (this.k.a(n, n2, n3)) {
                return true;
            }
            if (this.l.a(n, n2, n3)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void a(float f, float f2, int n, int n2, double d, double d2) {
        if (this.g != 0) {
            this.i.a(f, f2, n, n2, d, d2);
        } else {
            this.k.a(n, n2, d, d2);
            this.l.a(n, n2, d, d2);
        }
    }

    private void g() {
        if (this.o || this.k.g()) {
            return;
        }
        eq8z6w eq8z6w2 = this.k.d();
        if (eq8z6w2 != null) {
            this.k.c(eq8z6w2);
            this.l.a(eq8z6w2);
        }
        this.o = true;
    }
}

