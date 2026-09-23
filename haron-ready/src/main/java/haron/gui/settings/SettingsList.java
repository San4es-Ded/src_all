package haron.gui.settings;

import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.settings.ColorSettingRow;
import haron.gui.settings.ModeSettingRow;
import haron.gui.settings.SettingRow;
import haron.gui.widgets.ScrollBar;
import haron.gui.widgets.ColorPickerPopup;
import haron.render.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;

public class SettingsList {
    public void add(ToggleSettingRow row) {
        this.a(row);
    }

    public void setMaxHeight(float maxHeight) {
        this.a(maxHeight);
    }

    public float preferredHeight() {
        return this.m();
    }

    public boolean hasOpenEditor() {
        return this.d();
    }

    public void closeOpenEditors() {
        this.g();
    }

    public boolean contentOverflows(float height) {
        return this.d(height);
    }

    public boolean editorExtendsBelow(float bottom) {
        return this.e(bottom);
    }
    private static final float c = 3.0f;
    private static final float d = 5.0f;
    private static final float e = -2.0f;
    private static final float f = 3.0f;
    private static final float g = 1.0f;
    private static final float h = 10.0f;
    private float l;
    private float m;
    private float n;
    private float o;
    private float p;
    private float q;
    public static int a;
    public static boolean b;
    private final List<SettingRow> i = new ArrayList<SettingRow>();
    private float k = 0.0f;
    private final ScrollBar j = new ScrollBar(1.0f, 15.0f);

    public SettingsList() {
        this.j.b(10.0f);
    }

    public ModeSettingRow e() {
        for (SettingRow um973w2 : this.i) {
            ModeSettingRow tn8pw72;
            if (!um973w2.d() || !(um973w2 instanceof ModeSettingRow) || !(tn8pw72 = (ModeSettingRow)um973w2).m()) continue;
            return tn8pw72;
        }
        return null;
    }

    public boolean e(float f) {
        for (SettingRow um973w2 : this.i) {
            ModeSettingRow tn8pw72;
            if (!um973w2.d() || !(um973w2 instanceof ModeSettingRow) || !(tn8pw72 = (ModeSettingRow)um973w2).o() || !(tn8pw72.n() > f)) continue;
            return true;
        }
        return false;
    }

    public void i() {
        for (SettingRow um973w2 : this.i) {
            if (!(um973w2 instanceof ColorSettingRow)) continue;
            ((ColorSettingRow)um973w2).m();
        }
        ColorPickerPopup.a().b();
    }

    public List<SettingRow> b() {
        int n = 21;
        return this.i;
    }

    public boolean b(int n, int n2) {
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d() || !(um973w2 instanceof ModeSettingRow) || !((ModeSettingRow)um973w2).b(n, n2)) continue;
            return true;
        }
        return false;
    }

    public boolean b(float f, float f2, float f3, float f4, int n, int n2) {
        if (ColorPickerPopup.a().c()) {
            if (ColorPickerPopup.a().c(n, n2)) {
                return true;
            }
            ColorPickerPopup.a().b();
            return true;
        }
        if (this.d()) {
            for (SettingRow um973w2 : this.i) {
                if (!um973w2.d() || !um973w2.l() || !um973w2.b(f + 3.0f, 0.0f, f3 - 6.0f, n, n2)) continue;
                return true;
            }
            this.g();
            return true;
        }
        float f5 = f2 + 5.0f;
        float f6 = this.f(f4);
        if (!GuiInput.a(f, f5, f3, f6, (double)n, (double)n2)) {
            return false;
        }
        float f7 = f3 - 6.0f;
        float f8 = f5 - this.j.b();
        for (SettingRow um973w3 : this.i) {
            if (!um973w3.d()) continue;
            float f9 = um973w3.b();
            if (f8 + f9 >= f5 && f8 <= f5 + f6 && (float)n2 >= f5 && (float)n2 <= f5 + f6 && um973w3.b(f + 3.0f, f8, f7, n, n2)) {
                return true;
            }
            f8 += f9 + -2.0f;
        }
        return false;
    }

    public void b(float f) {
    }

    public void b(SettingRow um973w2) {
        this.i.remove(um973w2);
    }

    public boolean c(float f, float f2, float f3, float f4, int n, int n2) {
        return GuiInput.a(f, f2, f3, f4, (double)n, (double)n2);
    }

    public void c(float f) {
    }

    public float c() {
        if (this.i.isEmpty()) {
            return 0.0f;
        }
        float f = 0.0f;
        int n = 0;
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d()) continue;
            f += um973w2.b() + -2.0f;
            ++n;
        }
        if (n > 0) {
            f -= -2.0f;
        }
        return f;
    }

    public void c(int n, int n2) {
        this.j.d();
        ColorPickerPopup.a().b(n, n2);
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d()) continue;
            um973w2.a(n, n2);
        }
    }

    public boolean h() {
        return ColorPickerPopup.a().c();
    }

    private float f(float f) {
        return f - 5.0f - 3.0f;
    }

    public ColorSettingRow f() {
        for (SettingRow um973w2 : this.i) {
            ColorSettingRow rl2qd22;
            if (!um973w2.d() || !(um973w2 instanceof ColorSettingRow) || !(rl2qd22 = (ColorSettingRow)um973w2).h()) continue;
            return rl2qd22;
        }
        return null;
    }

    public boolean l() {
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d() || !um973w2.a_()) continue;
            return true;
        }
        return false;
    }

    public boolean d() {
        if (ColorPickerPopup.a().c()) {
            return true;
        }
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d() || !um973w2.l()) continue;
            return true;
        }
        return false;
    }

    public boolean d(float f) {
        return BooleanCoercion.from(this.c() <= this.f((this.k > 0.0f ? 1 : (this.k == 0.0f ? 0 : -1)) <= 0 ? f : this.k) ? 0 : 1);
    }

    public boolean a(char c, int n) {
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d() || !um973w2.a(c, n)) continue;
            return true;
        }
        return false;
    }

    public boolean a(float f, float f2, float f3, float f4, int n, int n2) {
        if (ColorPickerPopup.a().c() && ColorPickerPopup.a().a(n, n2)) {
            return true;
        }
        if (this.d()) {
            for (SettingRow um973w2 : this.i) {
                if (!um973w2.d() || !um973w2.l() || !um973w2.a(f + 3.0f, 0.0f, f3 - 6.0f, n, n2)) continue;
                return true;
            }
            this.g();
            return true;
        }
        if (b) {
            throw new ExceptionInInitializerError();
        }
        float f5 = this.f(f4);
        float f6 = this.c();
        if (this.d(f4) && this.j.a(f - 3.0f + f3 - 0.5f, f2 + 5.0f + 4.0f, f5 - 8.0f, f6, f5, n, n2)) {
            return true;
        }
        float f7 = f2 + 5.0f;
        if (!GuiInput.a(f, f7, f3, f5, (double)n, (double)n2)) {
            return false;
        }
        float f8 = f3 - 6.0f;
        float f9 = f7 - this.j.b();
        for (SettingRow um973w3 : this.i) {
            if (!um973w3.d()) continue;
            float f10 = um973w3.b();
            if (f9 + f10 >= f7 && f9 <= f7 + f5 && (float)n2 >= f7 && (float)n2 <= f7 + f5 && um973w3.a(f + 3.0f, f9, f8, n, n2)) {
                return true;
            }
            f9 += f10 + -2.0f;
        }
        return false;
    }

    public void a(float f) {
        this.k = f;
    }

    public void a(float f, int n, int n2, double d, double d2) {
        if (this.j.c()) {
            this.j.a(n2, this.c(), this.f(f));
        } else {
            ColorPickerPopup.a().a(n, n2, d, d2);
            for (SettingRow um973w2 : this.i) {
                if (!um973w2.d()) continue;
                um973w2.a(n, n2, d, d2);
            }
        }
    }

    public boolean a(int n, int n2, int n3) {
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d() || !um973w2.a(n, n2, n3)) continue;
            return true;
        }
        return false;
    }

    public void a(float f, float f2) {
        if (this.j.c() || !this.d(f2) || this.d()) {
            return;
        }
        this.j.a(f, this.c(), this.f(f2));
    }

    public boolean a(int n, int n2) {
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d() || !(um973w2 instanceof ModeSettingRow) || !((ModeSettingRow)um973w2).c(n, n2)) continue;
            return true;
        }
        return ColorPickerPopup.a().c(n, n2);
    }

    public void a(SettingRow um973w2) {
        this.i.add(um973w2);
    }

    public void a(int n, SettingRow um973w2) {
        if (n < 0) {
            n = 0;
        }
        if (n > this.i.size()) {
            n = this.i.size();
        }
        this.i.add(n, um973w2);
    }

    public void a() {
        this.i.clear();
        this.j.e();
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2, float f, float f2) {
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d() || !um973w2.j()) continue;
            um973w2.a(matrixStack, s7swsm2, this.l, this.m, this.n, this.o, n, n2, f, f2);
        }
        ColorPickerPopup.a().a(matrixStack, s7swsm2, n, n2, f);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5, float f6) {
        if (this.i.isEmpty()) {
            return;
        }
        this.l = f;
        this.m = f2;
        this.n = f3;
        this.o = f4;
        this.p = f5;
        this.q = f6;
        this.j.a();
        this.j.b(this.c(), this.f(f4));
        float f7 = 3.0f * f6;
        float f8 = 5.0f * f6;
        float f9 = -2.0f * f6;
        float f10 = f4 - f8 - 3.0f * f6;
        float f11 = f2 + f8;
        float f12 = f11 + f10;
        float f13 = f11 - this.j.b() * f6;
        for (SettingRow um973w2 : this.i) {
            if (!um973w2.d()) continue;
            float f14 = um973w2.b() * f6;
            if (f13 + f14 >= f11 && f13 <= f12) {
                boolean bl = this.d();
                um973w2.a(matrixStack, s7swsm2, f + f7, f13, f3 - f7 * 2.0f, !bl || um973w2.l() ? n : -9999, bl && !um973w2.l() ? -9999 : n2, f5, f6);
            }
            f13 += f14 + f9;
        }
        if (this.d(f4)) {
            this.j.a(matrixStack, s7swsm2, f - 3.0f + f3 - 1.0f * f6 / 2.0f, f2 + f8 + 4.0f * f6, f10 - 8.0f * f6, this.c() * f6, f10, n, n2, this.d());
        }
    }

    public float m() {
        return this.c() + 5.0f + 3.0f;
    }

    public void k() {
        this.j.e();
    }

    public void g() {
        for (SettingRow um973w2 : this.i) {
            if (um973w2 instanceof ModeSettingRow) {
                ((ModeSettingRow)um973w2).p();
            }
            if (!(um973w2 instanceof ColorSettingRow)) continue;
            ((ColorSettingRow)um973w2).m();
        }
        ColorPickerPopup.a().b();
    }

    public float[] j() {
        return ColorPickerPopup.a().f();
    }
}
