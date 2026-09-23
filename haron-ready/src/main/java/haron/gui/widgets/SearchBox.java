package haron.gui.widgets;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.audio.SoundPlayer;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class SearchBox {
    private static final Color c = pryrvd.d;
    private static final float d = 4.0f;
    private static final float e = 12.0f;
    private static final float f = 4.0f;
    private static final float g = 4.0f;
    private static final int h = 16;
    private float i;
    private float j;
    private float k;
    private float l;
    private String m = "";
    private int n = 0;
    private int o = -1;
    private int p = -1;
    private boolean q = false;
    private final AnimatedValue r = new AnimatedValue();
    private final AnimatedValue s = new AnimatedValue();
    private final AnimatedValue t = new AnimatedValue();
    private boolean u = false;
    private boolean v = true;
    private boolean w = false;
    private Consumer<String> x;
    public static int a;
    public static boolean b;

    public SearchBox() {
        this.t.d(1.0);
    }

    public SearchBox(float f, float f2) {
        this();
        this.k = f;
        this.l = f2;
    }

    public float e() {
        return this.i;
    }

    private void i() {
        if (this.q()) {
            this.s();
        } else if (this.n > 0) {
            this.m = SearchBox.$sf$1(this.m.substring(0, this.n - 1), this.m.substring(this.n));
            int n = this.n;
            this.n = (n & 0xFFFFFFFE) - (~n & 1);
            this.u();
            SoundPlayer.play("disabled2", 0.3f);
        } else if (b) {
            // empty if block
        }
        this.t();
    }

    private void b(boolean bl, boolean bl2) {
        int n;
        if (bl2) {
            n = this.l();
        } else {
            int n2 = this.m.length();
            int n3 = this.n;
            n = Math.min(n2, 2 * (n3 | 1) - (n3 ^ 1));
        }
        if (bl) {
            if (!this.q()) {
                this.o = this.n;
            }
            this.p = n;
        } else {
            if (this.q()) {
                n = Math.max(this.o, this.p);
            }
            this.r();
        }
        this.n = n;
        this.t();
    }

    public void b() {
        this.m = "";
        this.n = 0;
        this.r();
        this.u();
    }

    public void b(boolean bl) {
        if (this.q != bl) {
            this.q = bl;
            this.r.a(!bl ? 0.0 : 1.0, 0.2, Easings.h);
            if (bl) {
                this.t.d(1.0);
                this.v = false;
                this.t.a(0.3, 0.5, Easings.i);
            }
        }
    }

    public void b(float f, float f2) {
        this.k = f;
        this.l = f2;
    }

    private void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.b[14];
        float f6 = f + 4.0f + 12.0f + 4.0f;
        float f7 = f2 + f4 / 2.0f - v6hnga2.b("A") / 4.0f - 0.5f;
        s7swsm2.b().a(f6 - 2.0f, f2, f3 - 4.0f - 12.0f - 4.0f - 10.0f + 4.0f, f4, matrixStack);
        if (this.m.isEmpty() && !this.q) {
            v6hnga2.a("Поиск", f6, (double)f7, pryrvd.b, matrixStack);
        } else if (this.m.isEmpty() && this.q) {
            v6hnga2.a("|", f6, (double)(f7 - 0.5f), pryrvd.a(pryrvd.a, (int)(255.0f * (float)this.t.j())), matrixStack);
        } else {
            if (this.q()) {
                int n = Math.min(this.o, this.p);
                int n2 = Math.max(this.o, this.p);
                float f8 = f6 + v6hnga2.a(this.m, n);
                s7swsm2.a(f8, f2 + 4.0f, f6 + v6hnga2.a(this.m, n2) - f8, f4 - 8.0f, 2.0f, pryrvd.A, matrixStack);
            }
            v6hnga2.a(this.m, f6, (double)f7, pryrvd.a, matrixStack);
            if (this.q && !this.q()) {
                float f9 = (float)this.t.j();
                if (f9 <= 0.3f) {
                    if (867395491 < a) {
                        throw new ExceptionInInitializerError();
                    }
                } else {
                    s7swsm2.a(f6 + v6hnga2.a(this.m, this.n), f2 + 5.0f, 1.0f, f4 - 10.0f, pryrvd.a(pryrvd.a, (int)(255.0f * f9)), matrixStack);
                }
            }
        }
        s7swsm2.b().a(matrixStack);
    }

    private void s() {
        if (this.q()) {
            int n = Math.min(this.o, this.p);
            this.m = SearchBox.$sf$1(this.m.substring(0, n), this.m.substring(Math.max(this.o, this.p)));
            this.n = n;
            this.r();
            this.u();
        }
    }

    private void c(boolean bl) {
        if (bl) {
            if (!this.q()) {
                this.o = this.n;
            }
            this.p = 0;
        } else {
            this.r();
        }
        this.n = 0;
        this.t();
    }

    public boolean c() {
        return this.q;
    }

    private void n() {
        if (this.q()) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.m.substring(Math.min(this.o, this.p), Math.max(this.o, this.p)));
        }
    }

    public float h() {
        return this.l;
    }

    public float f() {
        return this.j;
    }

    private int l() {
        int n;
        if (this.n >= this.m.length()) {
            return this.m.length();
        }
        for (n = this.n; n < this.m.length() && !Character.isWhitespace(this.m.charAt(n)); ++n) {
        }
        while (n < this.m.length() && Character.isWhitespace(this.m.charAt(n))) {
            ++n;
        }
        return n;
    }

    public boolean d() {
        return this.m.isEmpty();
    }

    private void d(boolean bl) {
        if (bl) {
            if (!this.q()) {
                this.o = this.n;
            }
            this.p = this.m.length();
        } else {
            this.r();
        }
        this.n = this.m.length();
        this.t();
    }

    public String a() {
        return this.m;
    }

    public void a(boolean bl) {
        this.w = bl;
    }

    public void a(Consumer<String> consumer) {
        this.x = consumer;
    }

    public boolean a(int n, int n2, int n3) {
        int n4;
        if (!this.q) {
            return false;
        }
        int n5 = (~n3 | n3 ^ 0xA5D43705) - ~n3 == 0 ? 0 : 1;
        int n6 = n4 = (~n3 | 1) - ~n3 == 0 ? 0 : 1;
        if (n5 != 0) {
            switch (n) {
                case 65: {
                    this.m();
                    break;
                }
                case 67: {
                    this.n();
                    break;
                }
                case 86: {
                    this.o();
                    break;
                }
                case 88: {
                    this.p();
                }
            }
            return true;
        }
        switch (n) {
            case 256: {
                this.b(false);
                this.r();
                break;
            }
            case 257: {
                this.b(false);
                this.r();
                break;
            }
            case 259: {
                this.i();
                break;
            }
            case 261: {
                this.j();
                break;
            }
            case 262: {
                this.b(BooleanCoercion.from(n4), BooleanCoercion.from(n5));
                break;
            }
            case 263: {
                this.a(BooleanCoercion.from(n4), BooleanCoercion.from(n5));
                break;
            }
            case 268: {
                this.c(BooleanCoercion.from(n4));
                break;
            }
            case 269: {
                this.d(BooleanCoercion.from(n4));
            }
        }
        return true;
    }

    public boolean a(int n, int n2) {
        boolean bl = InteractionOverlayController.a().b();
        if (!GuiInput.a(this.i, this.j, this.k, this.l, (double)n, (double)n2) || bl) {
            if (this.q) {
                this.b(false);
            }
            return false;
        }
        this.b(true);
        this.n = ClientFonts.b[14].a(this.m, (float)n - (this.i + 4.0f + 12.0f + 4.0f));
        this.r();
        return true;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2) {
        int n3;
        this.i = f;
        this.j = f2;
        this.k = f3;
        this.l = f4;
        this.r.a();
        this.s.a();
        if (this.q) {
            this.t.a();
            if (this.t.d()) {
                this.v = BooleanCoercion.from(this.v ? 0 : 1);
                this.t.a(!this.v ? 0.3 : 1.0, 0.5, Easings.i);
            }
        } else {
            this.t.d(1.0);
            this.v = true;
        }
        boolean bl = !this.w && InteractionOverlayController.a().b();
        int n4 = n3 = bl || !GuiInput.a(f, f2, f3, f4, (double)n, (double)n2) ? 0 : 1;
        if (bl && this.u) {
            this.s.a(0.0, 0.15, Easings.h);
            this.u = false;
        } else if (!bl && BooleanCoercion.from(n3) != this.u) {
            this.s.a(n3 == 0 ? 0.0 : 1.0, 0.15, Easings.h);
            this.u = BooleanCoercion.from(n3);
        }
        float f5 = (float)this.r.j();
        float f6 = (float)this.s.j();
        this.a(matrixStack, s7swsm2, f, f2, f3, f4, f5, f6);
        this.a(matrixStack, s7swsm2, f, f2, f4, f5, f6);
        this.b(matrixStack, s7swsm2, f, f2, f3, f4, f5);
        if ((n3 != 0 || this.q) && !bl) {
            if (this.q) {
                GuiInput.setTextCursor();
            } else {
                GuiInput.g();
            }
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.e[12];
        FontRenderer v6hnga3 = ClientFonts.b[14];
        v6hnga2.a("", f + 4.0f + 6.0f - v6hnga2.a("") / 2.0f + 2.0f, (double)(f2 + f3 / 2.0f - v6hnga3.b("A") / 4.0f + 0.5f), ColorUtils.a(ColorUtils.a(pryrvd.b, c, f5), c, f4), matrixStack);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5, float f6) {
        int n = (int)(f6 * 3.0f);
        Color color = pryrvd.CELL_BG_TOP;
        Color color2 = pryrvd.CELL_BG_BOT;
        s7swsm2.a(f, f2, f3, f4, 4.0f, color, color, color2, color2, matrixStack);
        if (n > 0) {
            s7swsm2.a(f, f2, 2.0f, f4, 0.0f, ColorUtils.a(new Color(pryrvd.ACCENT.getRed(), pryrvd.ACCENT.getGreen(), pryrvd.ACCENT.getBlue(), n), color, 1.0f), matrixStack);
        }
    }

    public void a(String string) {
        this.m = string;
        this.n = string.length();
        this.r();
        this.u();
    }

    private void a(boolean bl, boolean bl2) {
        int n;
        if (bl2) {
            n = this.k();
        } else {
            int n2 = this.n;
            n = Math.max(0, 2 * (n2 & 0xFFFFFFFE) - (n2 ^ 1));
        }
        if (bl) {
            if (!this.q()) {
                this.o = this.n;
            }
            this.p = n;
        } else {
            if (this.q()) {
                n = Math.min(this.o, this.p);
            }
            this.r();
        }
        this.n = n;
        this.t();
    }

    public void a(float f, float f2) {
        this.i = f;
        this.j = f2;
    }

    public boolean a(char c, int n) {
        if (this.q && !Character.isISOControl(c)) {
            if (this.m.length() >= 16 && !this.q()) {
                return true;
            }
            if (this.q()) {
                this.s();
            }
            if (this.m.length() < 16) {
                this.m = SearchBox.$sf$0(this.m.substring(0, this.n), c, this.m.substring(this.n));
                int n2 = this.n;
                this.n = (n2 | 1) + (n2 & 1);
                this.t();
                this.u();
                SoundPlayer.play("disabled2", 0.3f);
            }
            return true;
        }
        return false;
    }

    private void m() {
        this.o = 0;
        this.p = this.m.length();
        this.n = this.m.length();
    }

    private void o() {
        String string = MinecraftClient.getInstance().keyboard.getClipboard();
        if (string == null || string.isEmpty()) {
            return;
        }
        String string2 = string.replaceAll("[\\r\\n\\t]", "");
        if (this.q()) {
            this.s();
        }
        int n = 16 - this.m.length();
        if (string2.length() > n) {
            string2 = string2.substring(0, n);
        }
        if (!string2.isEmpty()) {
            this.m = SearchBox.$sf$2(this.m.substring(0, this.n), string2, this.m.substring(this.n));
            this.n = this.n - ~string2.length() - 1;
            this.u();
        }
        this.t();
    }

    private void p() {
        if (this.q()) {
            this.n();
            this.s();
        }
    }

    private int k() {
        int n;
        if (this.n == 0) {
            return 0;
        }
        for (n = this.n - 1; n > 0 && Character.isWhitespace(this.m.charAt(n)); --n) {
        }
        while (n > 0) {
            int n2;
            if (!Character.isWhitespace(this.m.charAt((n2 = n--) + ~(n2 & 0x9826F38E | ~n2 & 0x67D90C71) + 1))) continue;
        }
        return n;
    }

    private void t() {
        this.t.d(1.0);
        this.v = false;
        this.t.a(0.3, 0.5, Easings.i);
    }

    public float g() {
        return this.k;
    }

    private void j() {
        if (this.q()) {
            this.s();
        } else if (this.n < this.m.length()) {
            this.m = SearchBox.$sf$1(this.m.substring(0, this.n), this.m.substring(this.n - -2 - 1));
            this.u();
            SoundPlayer.play("disabled2", 0.3f);
        }
        this.t();
    }

    private boolean q() {
        return this.o != -1 && this.p != -1 && this.o != this.p;
    }

    private void u() {
        if (this.x != null) {
            this.x.accept(this.m);
        }
    }

    private void r() {
        this.o = -1;
        this.p = -1;
    }

    private static /* synthetic */ String $sf$0(String string, char c, String string2) {
        return string + c + string2;
    }

    private static /* synthetic */ String $sf$1(String string, String string2) {
        return string + string2;
    }

    private static /* synthetic */ String $sf$2(String string, String string2, String string3) {
        return string + string2 + string3;
    }
}

