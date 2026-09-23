package haron.gui.widgets;

import haron.core.BooleanCoercion;
import haron.gui.widgets.TextInputType;
import haron.gui.widgets.TextInput;
import haron.render.ShapeRenderer;
import java.util.function.Consumer;
import net.minecraft.client.util.math.MatrixStack;

public class CoordinateInput {
    private static final float c = 23.0f;
    private static final float d = 5.0f;
    private static final float e = 7.5f;
    private final TextInput f = new TextInput(TextInputType.COORDINATE, "0", "X");
    private final TextInput g = new TextInput(TextInputType.COORDINATE, "0", "Y");
    private final TextInput h = new TextInput(TextInputType.COORDINATE, "0", "Z");
    private Consumer<int[]> i;
    public static int a;
    public static boolean b;

    private static int parseCoord(String string) {
        if (string == null) {
            return 0;
        }
        String string2 = string.trim();
        if (string2.isEmpty() || string2.equals("-") || string2.equals("+")) {
            return 0;
        }
        try {
            return Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public CoordinateInput() {
        this.f.b(true);
        this.g.b(true);
        this.h.b(true);
        this.f.a(7.5f);
        this.g.a(7.5f);
        this.h.a(7.5f);
        this.f.a(10);
        this.g.a(10);
        this.h.a(10);
        this.f.e(true);
        this.g.e(true);
        this.h.e(true);
        this.f.setClearOnFocus(true);
        this.g.setClearOnFocus(true);
        this.h.setClearOnFocus(true);
        this.f.a((String string) -> {
            this.f();
        });
        this.g.a((String string) -> {
            int n = 723;
            this.f();
        });
        this.h.a((String string) -> {
            this.f();
        });
    }

    public static float e() {
        return 23.0f;
    }

    public boolean b(int n, int n2, int n3) {
        return !this.f.d() ? (!this.g.d() ? (!this.h.d() ? false : this.h.b(n, n2, n3)) : this.g.b(n, n2, n3)) : this.f.b(n, n2, n3);
    }

    public int b() {
        return CoordinateInput.parseCoord(this.g.a());
    }

    public boolean c(int n, int n2, int n3) {
        boolean bl = this.f.a(n, n2, n3);
        boolean bl2 = this.g.a(n, n2, n3);
        boolean bl3 = this.h.a(n, n2, n3);
        return bl || bl2 || bl3;
    }

    public int c() {
        return CoordinateInput.parseCoord(this.h.a());
    }

    private void f() {
        if (this.i != null) {
            this.i.accept(new int[]{this.a(), this.b(), this.c()});
        }
    }

    public boolean d() {
        int n = this.f.d() || this.g.d() || this.h.d() ? 1 : 0;
        return BooleanCoercion.from(n);
    }

    public boolean a(char c, int n) {
        return !this.f.d() ? (!this.g.d() ? (!this.h.d() ? false : this.h.a(c, n)) : this.g.a(c, n)) : this.f.a(c, n);
    }

    public void a(int n, int n2, int n3) {
        this.f.setRawValue(String.valueOf(n));
        this.g.setRawValue(String.valueOf(n2));
        this.h.setRawValue(String.valueOf(n3));
    }

    public int a() {
        return CoordinateInput.parseCoord(this.f.a());
    }

    public void a(Consumer<int[]> consumer) {
        this.i = consumer;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        float f5 = (f3 - 10.0f) / 3.0f;
        float f6 = f + f5 + 5.0f;
        float f7 = f + (f5 + 5.0f) * 2.0f;
        this.f.a(matrixStack, s7swsm2, f, f2, f5, 23.0f, n, n2, f4);
        this.g.a(matrixStack, s7swsm2, f6, f2, f5, 23.0f, n, n2, f4);
        this.h.a(matrixStack, s7swsm2, f7, f2, f5, 23.0f, n, n2, f4);
    }

    public boolean a(int n, int n2) {
        return this.f.a(n, n2) || this.g.a(n, n2) || this.h.a(n, n2);
    }
}

