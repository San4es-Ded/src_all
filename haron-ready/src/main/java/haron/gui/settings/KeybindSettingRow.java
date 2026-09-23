package haron.gui.settings;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.settings.SettingRow;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.settings.KeybindSetting;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;

public class KeybindSettingRow
implements SettingRow {
    public static final float a = 20.0f;
    private static final float d = 8.0f;
    private final String g;
    private int h;
    private boolean i;
    private final KeybindSetting j;
    private final AnimatedValue k = new AnimatedValue();
    private final AnimatedValue l = new AnimatedValue();
    private final AnimatedValue m = new AnimatedValue();
    private final cqqvax n = new cqqvax();
    private boolean o = false;
    private boolean p = true;
    private float q;
    private float r;
    private float s;
    private float t;
    public static int b;
    public static boolean c;

    @Override
    public boolean a_() {
        return this.i;
    }

    public KeybindSettingRow(String string, int n) {
        this.j = null;
        this.g = string;
        this.h = n;
        this.i = false;
    }

    public KeybindSettingRow(KeybindSetting hvn3h82) {
        this.j = hvn3h82;
        this.g = hvn3h82.f();
        this.h = hvn3h82.a();
        this.i = false;
    }

    public void e() {
        this.i = false;
        this.l.d(0.0);
        this.m.d(1.0);
        this.p = true;
        this.k.d(0.0);
        this.o = false;
    }

    @Override
    public float b() {
        return 20.0f;
    }

    private String b(int n) {
        switch (n) {
            case 32: {
                return "SPACE";
            }
            default: {
                return KeybindSettingRow.$sf$0(n);
            }
            case 39: {
                return "'";
            }
            case 44: {
                return ",";
            }
            case 45: {
                return "-";
            }
            case 46: {
                return ".";
            }
            case 47: {
                return "/";
            }
            case 48: {
                return "0";
            }
            case 49: {
                return "1";
            }
            case 50: {
                return "2";
            }
            case 51: {
                return "3";
            }
            case 52: {
                return "4";
            }
            case 53: {
                return "5";
            }
            case 54: {
                return "6";
            }
            case 55: {
                return "7";
            }
            case 56: {
                return "8";
            }
            case 57: {
                return "9";
            }
            case 59: {
                return ";";
            }
            case 61: {
                return "=";
            }
            case 65: {
                return "A";
            }
            case 66: {
                return "B";
            }
            case 67: {
                return "C";
            }
            case 68: {
                return "D";
            }
            case 69: {
                return "E";
            }
            case 70: {
                return "F";
            }
            case 71: {
                return "G";
            }
            case 72: {
                return "H";
            }
            case 73: {
                return "I";
            }
            case 74: {
                return "J";
            }
            case 75: {
                return "K";
            }
            case 76: {
                return "L";
            }
            case 77: {
                return "M";
            }
            case 78: {
                return "N";
            }
            case 79: {
                return "O";
            }
            case 80: {
                return "P";
            }
            case 81: {
                return "Q";
            }
            case 82: {
                return "R";
            }
            case 83: {
                return "S";
            }
            case 84: {
                return "T";
            }
            case 85: {
                return "U";
            }
            case 86: {
                return "V";
            }
            case 87: {
                return "W";
            }
            case 88: {
                return "X";
            }
            case 89: {
                return "Y";
            }
            case 90: {
                return "Z";
            }
            case 91: {
                return "[";
            }
            case 92: {
                return "\\";
            }
            case 93: {
                return "]";
            }
            case 96: {
                return "`";
            }
            case 256: {
                return "ESC";
            }
            case 257: {
                return "ENTER";
            }
            case 258: {
                return "TAB";
            }
            case 259: {
                return "BACK";
            }
            case 260: {
                return "INS";
            }
            case 261: {
                return "DEL";
            }
            case 262: {
                return "RIGHT";
            }
            case 263: {
                return "LEFT";
            }
            case 264: {
                return "DOWN";
            }
            case 265: {
                return "UP";
            }
            case 266: {
                return "PGUP";
            }
            case 267: {
                return "PGDN";
            }
            case 268: {
                return "HOME";
            }
            case 269: {
                return "END";
            }
            case 280: {
                return "CAPS";
            }
            case 281: {
                return "SCROLL";
            }
            case 282: {
                return "NUM";
            }
            case 283: {
                return "PRINT";
            }
            case 284: {
                return "PAUSE";
            }
            case 290: {
                return "F1";
            }
            case 291: {
                return "F2";
            }
            case 292: {
                return "F3";
            }
            case 293: {
                return "F4";
            }
            case 294: {
                return "F5";
            }
            case 295: {
                return "F6";
            }
            case 296: {
                return "F7";
            }
            case 297: {
                return "F8";
            }
            case 298: {
                return "F9";
            }
            case 299: {
                return "F10";
            }
            case 300: {
                return "F11";
            }
            case 301: {
                return "F12";
            }
            case 320: {
                return "NUM0";
            }
            case 321: {
                return "NUM1";
            }
            case 322: {
                return "NUM2";
            }
            case 323: {
                return "NUM3";
            }
            case 324: {
                return "NUM4";
            }
            case 325: {
                return "NUM5";
            }
            case 326: {
                return "NUM6";
            }
            case 327: {
                return "NUM7";
            }
            case 328: {
                return "NUM8";
            }
            case 329: {
                return "NUM9";
            }
            case 330: {
                return "NUM.";
            }
            case 331: {
                return "NUM/";
            }
            case 332: {
                return "NUM*";
            }
            case 333: {
                return "NUM-";
            }
            case 334: {
                return "NUM+";
            }
            case 335: {
                return "NUMENTER";
            }
            case 340: {
                return "LSHIFT";
            }
            case 341: {
                return "LCTRL";
            }
            case 342: {
                return "LALT";
            }
            case 343: {
                return "LWIN";
            }
            case 344: {
                return "RSHIFT";
            }
            case 345: {
                return "RCTRL";
            }
            case 346: {
                return "RALT";
            }
            case 347: {
                return "RWIN";
            }
            case 348: 
        }
        return "MENU";
    }

    public int c() {
        return this.h;
    }

    private String f() {
        if (this.i) {
            return "|";
        }
        return this.h > 0 ? this.b(this.h) : "-";
    }

    @Override
    public boolean d() {
        int n = this.j == null || this.j.m() ? 1 : 0;
        return BooleanCoercion.from(n);
    }

    @Override
    public String a() {
        return this.g;
    }

    public void a(int n) {
        this.h = n;
        if (this.j != null) {
            this.j.a(n);
        }
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f6 = 20.0f * f5;
        float f7 = 8.0f * f5;
        this.n.a(GuiInput.a(f, f2, f3, f6, (double)n, (double)n2));
        this.l.a();
        this.k.a();
        if (this.i) {
            this.m.a();
            if (this.m.d()) {
                this.p = BooleanCoercion.from(this.p ? 0 : 1);
                this.m.a(!this.p ? 0.3 : 1.0, 0.5, Easings.i);
            }
        } else {
            this.m.d(1.0);
            this.p = true;
        }
        float f8 = (float)this.l.j();
        float f9 = (float)this.m.j();
        int n3 = (int)(255.0f * f4);
        String string = this.f();
        float f10 = v6hnga2.a(string) * f5;
        float f11 = v6hnga2.b(string) * f5;
        float f12 = Math.max(20.0f * f5, f10 + 5.0f * f5 * 2.0f) - 2.5f;
        float f13 = 14.0f * f5;
        float f14 = f + f3 - f7 - f12;
        float f15 = f14 - (f + f7) - 4.0f * f5;
        float f16 = f2 + f6 / 2.0f - v6hnga2.b(this.g) * f5 / 4.0f;
        this.n.a(matrixStack, s7swsm2, v6hnga2, this.g, f + f7, f16, f15, f5, pryrvd.a, f4);
        float f17 = f16 + v6hnga2.b(this.g) * f5 / 2.0f - f13 / 2.0f - 5.0f * f5;
        this.q = f14;
        this.r = f17;
        this.s = f12;
        this.t = f13;
        float f18 = 4.0f * f5;
        Color color = ColorUtils.a(pryrvd.m, pryrvd.C, f8);
        Color color2 = ColorUtils.a(pryrvd.r, pryrvd.D, f8);
        Color color3 = pryrvd.a(color, n3);
        Color color4 = pryrvd.a(color2, n3);
        s7swsm2.a(f14 - 0.5f * f5, f17 - 0.5f * f5, f12 + f5, f13 + f5, f18, color3, color3, color4, color4, matrixStack);
        Color color5 = ColorUtils.a(pryrvd.WIDGET_BG_TOP, pryrvd.y, f8);
        Color color6 = ColorUtils.a(pryrvd.WIDGET_BG_BOT, pryrvd.z, f8);
        Color color7 = pryrvd.a(color5, n3);
        Color color8 = pryrvd.a(color6, n3);
        s7swsm2.a(f14, f17, f12, f13, f18, color7, color7, color8, color8, matrixStack);
        float f19 = f14 + f12 / 2.0f;
        float f20 = f17 + f13 / 2.0f;
        float f21 = Math.round(f19 - f10 / 2.0f);
        float f22 = Math.round(f20 - f11 / 2.0f);
        int n4 = this.i ? (int)(255.0f * f4 * f9) : n3;
        Color color9 = pryrvd.a(pryrvd.a, n4);
        matrixStack.push();
        matrixStack.translate(f21, f22, 0.0f);
        matrixStack.scale(f5, f5, 1.0f);
        matrixStack.translate(-f21, -f22, 0.0f);
        v6hnga2.a(string, f21, (double)(f22 + 4.0f), color9, matrixStack);
        matrixStack.pop();
        boolean bl = GuiInput.a(f14, f17, f12, f13, (double)n, (double)n2);
        if (bl != this.o) {
            this.k.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.o = bl;
        }
        if (bl || this.i) {
            GuiInput.g();
        }
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        float f4;
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f5 = Math.max(20.0f, v6hnga2.a(this.f()) + 10.0f);
        float f6 = f + f3 - 8.0f - f5;
        if (!GuiInput.a(f6, f2 + 10.0f - (f4 = v6hnga2.b(this.g)) / 4.0f + f4 / 2.0f - 7.0f - 5.0f, f5, 14.0f, (double)n, (double)n2)) {
            if (this.i) {
                this.i = false;
                this.l.a(0.0, 0.2, Easings.h);
            }
            return false;
        }
        this.i = BooleanCoercion.from(this.i ? 0 : 1);
        this.l.a(!this.i ? 0.0 : 1.0, 0.2, Easings.h);
        if (this.i) {
            this.m.d(1.0);
            this.p = false;
            this.m.a(0.3, 0.5, Easings.i);
        }
        return true;
    }

    @Override
    public boolean a(int n, int n2, int n3) {
        if (!this.i) {
            return false;
        }
        this.i = false;
        this.l.a(0.0, 0.2, Easings.h);
        this.h = n == 256 || n == 261 || n == 259 ? -1 : n;
        if (this.j != null) {
            this.j.a(this.h);
        }
        return true;
    }

    private static /* synthetic */ String $sf$0(int n) {
        return "KEY" + n;
    }
}

