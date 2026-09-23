package haron.gui.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.gui.notifications.o1leqh;
import haron.gui.widgets.TextInputType;
import haron.gui.widgets.CoordinateInput;
import haron.gui.widgets.TextInput;
import haron.hud.notifications.eq8z6w;
import haron.hud.notifications.oo89jz;
import haron.markers.MarkerIcon;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class f6g0cs {
    private static final float c = 11.5f;
    private static final float d = 0.5f;
    private static final float e = 8.0f;
    private static final float f = 8.0f;
    private static final float g = 8.5f;
    private static final float h = 5.0f;
    private static final float i = 10.0f;
    private static final float j = 13.5f;
    private static final float k = 4.0f;
    private static final float l = 22.0f;
    private static final float m = 9.5f;
    private static final float n = 19.5f;
    private static final float o = 6.5f;
    private static final Color p = pryrvd.L;
    private static final Color q = pryrvd.M;
    private eq8z6w r;
    private final CoordinateInput t;
    private final o1leqh u;
    private Consumer<eq8z6w> A;
    private Consumer<eq8z6w> B;
    private final AnimatedValue v = new AnimatedValue();
    private boolean w = false;
    private final TextInput s = new TextInput(TextInputType.TEXT, "", "Название");

    private static MarkerIcon mapMarkerIconOf(oo89jz oo89jz2) {
        if (oo89jz2 == null) {
            return MarkerIcon.EVENT;
        }
        try {
            return MarkerIcon.valueOf(oo89jz2.name());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return MarkerIcon.EVENT;
        }
    }

    public f6g0cs() {
        this.s.a(24);
        this.s.a(this::a);
        this.s.a(7.5f);
        this.s.e(false);
        this.t = new CoordinateInput();
        this.t.a(this::a);
        this.u = new o1leqh();
        this.u.a(this::a);
    }

    public boolean e() {
        return false;
    }

    public boolean b() {
        int n = 562;
        return this.r != null && this.r.j();
    }

    public void b(Consumer<eq8z6w> consumer) {
        this.B = consumer;
    }

    public void b(int n, int n2) {
    }

    public void c() {
    }

    private void f() {
        if (this.B == null || this.r == null) {
            return;
        }
        this.B.accept(this.r);
    }

    public boolean d() {
        return BooleanCoercion.from(this.s.d() || this.t.d() ? 1 : 0);
    }

    public boolean a(int n, int n2) {
        return false;
    }

    public void a(int n, int n2, double d, double d2) {
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        float f5 = f3 - 8.0f;
        this.s.a(matrixStack, s7swsm2, f, f2, f5, 22.0f, n, n2, f4);
    }

    public boolean a(char c, int n) {
        return !this.s.d() ? (!this.t.d() ? false : this.t.a(c, n)) : this.s.a(c, n);
    }

    public boolean a(int n, int n2, int n3) {
        return !this.s.d() ? (!this.t.d() ? false : this.t.b(n, n2, n3)) : this.s.b(n, n2, n3);
    }

    public eq8z6w a() {
        return this.r;
    }

    private void a(oo89jz oo89jz2) {
        if (this.r == null || this.r.j()) {
            return;
        }
        this.r.a(oo89jz2);
        Waypoint yo0tnu2 = MarkerRegistry.a(this.r.b(), this.r.c(), this.r.d());
        if (yo0tnu2 != null) {
            yo0tnu2.a(f6g0cs.mapMarkerIconOf(oo89jz2));
        }
        this.f();
    }

    private void a(int[] nArray) {
        if (this.r == null || this.r.j()) {
            return;
        }
        Waypoint yo0tnu2 = MarkerRegistry.a(this.r.b(), this.r.c(), this.r.d());
        this.r.a(nArray[0]);
        this.r.b(nArray[1]);
        this.r.c(nArray[2]);
        if (yo0tnu2 != null) {
            yo0tnu2.a(nArray[0]);
            yo0tnu2.b(nArray[1]);
            yo0tnu2.c(nArray[2]);
        }
        this.f();
    }

    private void a(String string) {
        if (this.r == null || string.isEmpty() || this.r.j()) {
            return;
        }
        String string2 = this.r.a();
        Waypoint yo0tnu2 = MarkerRegistry.a(this.r.b(), this.r.c(), this.r.d());
        this.r.a(string);
        if (yo0tnu2 != null && string2 != null && string2.equals(yo0tnu2.a())) {
            yo0tnu2.a(string);
        }
        this.f();
    }

    public void a(Consumer<eq8z6w> consumer) {
        this.A = consumer;
    }

    public boolean a(float f, float f2, float f3, float f4, int n, int n2) {
        if (this.r == null) {
            return false;
        }
        if (GuiInput.a(f + f3 - 8.0f - 13.5f, f2 + 8.0f - 2.0f, 13.5f, 13.5f, (double)n, (double)n2)) {
            if (this.A != null) {
                this.A.accept(this.r);
            }
            return true;
        }
        if (this.b()) {
            return false;
        }
        float f5 = f + 8.0f;
        float f6 = 12.0f;
        float f7 = 14.0f;
        float f8 = 24.0f;
        float f9 = 22.0f;
        float f10 = CoordinateInput.e();
        float f11 = f2 + 8.0f + 24.0f + 12.0f;
        float f12 = f11 + 22.0f + 14.0f + f10 + 14.0f;
        boolean bl = this.s.a(n, n2, 0);
        boolean bl2 = this.t.c(n, n2, 0);
        boolean bl3 = this.u.a(f5, f12, n, n2);
        if (bl || bl2 || bl3) {
            return true;
        }
        this.s.a(false);
        return false;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2, float f) {
    }

    public void a(eq8z6w eq8z6w2) {
        boolean bl = this.r == eq8z6w2;
        this.r = eq8z6w2;
        if (eq8z6w2 != null) {
            this.s.b(eq8z6w2.a());
            if (!bl) {
                this.t.a(eq8z6w2.b(), eq8z6w2.c(), eq8z6w2.d());
            }
            this.u.a(eq8z6w2.f());
            this.u.a(eq8z6w2.e());
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3) {
        int n3;
        int n4 = (int)(255.0f * f3);
        boolean bl = InteractionOverlayController.a().b();
        int n5 = n3 = bl || !GuiInput.a(f, f2, 13.5f, 13.5f, (double)n, (double)n2) ? 0 : 1;
        if (bl && this.w) {
            this.v.a(0.0, 0.15, Easings.h);
            this.w = false;
        } else if (!bl && BooleanCoercion.from(n3) != this.w) {
            this.v.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.w = BooleanCoercion.from(n3);
        }
        float f4 = (float)this.v.j();
        Color color = pryrvd.b(pryrvd.q, f3);
        Color color2 = pryrvd.b(pryrvd.n, f3);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 14.5f, 14.5f, 4.0f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.f, p, f4);
        Color color4 = ColorUtils.a(pryrvd.e, q, f4);
        Color color5 = pryrvd.a(color3, n4);
        Color color6 = pryrvd.a(color4, n4);
        s7swsm2.a(f, f2, 13.5f, 13.5f, 4.0f, color5, color5, color6, color6, matrixStack);
        ClientFonts.e[15].a("", f + 2.75f, (double)(f2 + 2.75f - 0.5f), pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.Z, f4), n4), matrixStack);
        if (n3 == 0 || bl) {
            return;
        }
        GuiInput.g();
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5) {
        if (this.r != null) {
            this.v.a();
            int n3 = (int)(255.0f * f5);
            Color color = pryrvd.a(pryrvd.g, n3);
            Color color2 = pryrvd.a(pryrvd.n, n3);
            s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 11.5f, color, color, color2, color2, matrixStack);
            Color color3 = pryrvd.a(pryrvd.CELL_BG_TOP, n3);
            Color color4 = pryrvd.a(pryrvd.CELL_BG_BOT, n3);
            s7swsm2.a(f, f2, f3, f4, 11.5f, color3, color3, color4, color4, matrixStack);
            FontRenderer v6hnga2 = ClientFonts.a[15];
            FontRenderer v6hnga3 = ClientFonts.a[14];
            float f6 = f + 8.0f;
            float f7 = f3 - 16.0f;
            float f8 = f2 + 8.0f;
            v6hnga2.a("Настройки меток", f6, (double)f8, pryrvd.a(pryrvd.a, n3), matrixStack);
            this.a(matrixStack, s7swsm2, f + f3 - 8.0f - 13.5f, f8 - 2.0f, n, n2, f5);
            float f9 = f8 + 24.0f;
            Color color5 = pryrvd.a(pryrvd.b, n3);
            float f10 = f9 + 12.0f;
            v6hnga3.a("Название метки", f6, (double)(f10 - 11.0f), color5, matrixStack);
            this.a(matrixStack, s7swsm2, f6, f10, f7, n, n2, f5);
            float f11 = f10 + 22.0f + 14.0f;
            v6hnga3.a("Координаты", f6, (double)(f11 - 11.0f), color5, matrixStack);
            this.t.a(matrixStack, s7swsm2, f6, f11, f7, n, n2, f5);
            float f12 = f11 + CoordinateInput.e() + 14.0f;
            v6hnga3.a("Иконка", f6, (double)(f12 - 11.0f), color5, matrixStack);
            this.u.a(matrixStack, s7swsm2, f6, f12, f7, n, n2, f5);
        }
    }
}

