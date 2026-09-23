package haron.gui.settings;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.settings.TextInputMode;
import haron.gui.settings.SettingRow;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.settings.ValidatedTextSetting;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;

public class TextSettingRow
implements SettingRow {
    private static final Set<TextSettingRow> d = Collections.newSetFromMap(new WeakHashMap());
    public static final float a = 20.0f;
    private static final float e = 8.0f;
    private static final float f = 40.0f;
    private static final float g = 100.0f;
    private static final float h = 14.0f;
    private static final float i = 4.0f;
    private static final float j = 5.0f;
    private static final int k = 16;
    private final String l;
    private final TextInputMode m;
    private String n = "";
    private String o = "";
    private final ValidatedTextSetting p;
    private boolean q = false;
    private int r = 0;
    private int s = -1;
    private int t = -1;
    private final AnimatedValue u = new AnimatedValue();
    private final AnimatedValue v = new AnimatedValue();
    private final AnimatedValue w = new AnimatedValue();
    private final AnimatedValue x = new AnimatedValue();
    private final cqqvax y = new cqqvax();
    private boolean z = false;
    private boolean A = true;
    private float B;
    private float C;
    private float D;
    private float E = 1.0f;
    private float F;
    private String G = "";
    public static int b;
    public static boolean c;

    private void H() {
        this.s = -1;
        this.t = -1;
    }

    private boolean G() {
        return this.s != -1 && this.t != -1 && this.s != this.t;
    }

    @Override
    public boolean a_() {
        return this.q;
    }

    public TextSettingRow(ValidatedTextSetting trepl22, TextInputMode o7nyq62) {
        this.p = trepl22;
        this.l = trepl22.f();
        this.m = o7nyq62;
        this.o = trepl22.c();
        this.w.d(1.0);
        this.F = 40.0f;
        this.x.d(40.0);
        d.add(this);
        this.a(trepl22.a());
    }

    public TextSettingRow(String string, TextInputMode o7nyq62, String string2) {
        this(string, o7nyq62);
        this.o = string2;
    }

    public TextSettingRow(String string, TextInputMode o7nyq62) {
        this.p = null;
        this.l = string;
        this.m = o7nyq62;
        this.w.d(1.0);
        this.F = 40.0f;
        this.x.d(40.0);
        d.add(this);
    }

    public TextSettingRow(String string, TextInputMode o7nyq62, String string2, String string3) {
        this(string, o7nyq62, string3);
        this.a(string2);
    }

    private int B() {
        int n;
        if (this.r >= this.n.length()) {
            return this.n.length();
        }
        for (n = this.r; n < this.n.length() && !Character.isWhitespace(this.n.charAt(n)); ++n) {
        }
        while (n < this.n.length() && Character.isWhitespace(this.n.charAt(n))) {
            ++n;
        }
        return n;
    }

    private void C() {
        this.s = 0;
        this.t = this.n.length();
        this.r = this.n.length();
    }

    private void D() {
        if (this.G()) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.n.substring(Math.min(this.s, this.t), Math.max(this.s, this.t)));
        }
    }

    private void F() {
        if (this.G()) {
            this.D();
            this.I();
        }
    }

    private void I() {
        if (this.G()) {
            int n = Math.min(this.s, this.t);
            this.n = TextSettingRow.$sf$2(this.n.substring(0, n), this.n.substring(Math.max(this.s, this.t)));
            this.r = n;
            this.H();
            if (this.m == TextInputMode.PRICE) {
                int n2 = this.x();
                this.n = this.b(this.n.replace(",", ""));
                this.r = this.a(n2);
            } else if (c) {
                // empty if block
            }
            this.u();
            this.t();
            this.q();
        }
    }

    private void J() {
        this.w.d(1.0);
        this.A = false;
        this.w.a(0.3, 0.5, Easings.i);
    }

    public String e() {
        return this.n;
    }

    public boolean i() {
        return this.q;
    }

    private String b(String string) {
        if (string.isEmpty()) {
            return "";
        }
        try {
            long l = Long.parseLong(string);
            StringBuilder stringBuilder = new StringBuilder();
            String string2 = String.valueOf(l);
            int n = 0;
            for (int i = string2.length() - 1; i >= 0; --i) {
                if (n > 0) {
                    if (n % 3 == 0) {
                        stringBuilder.insert(0, ",");
                    } else if (c) {
                        // empty if block
                    }
                }
                stringBuilder.insert(0, string2.charAt(i));
                ++n;
            }
            return stringBuilder.toString();
        }
        catch (NumberFormatException numberFormatException) {
            return string;
        }
    }

    private void b(boolean bl, boolean bl2) {
        int n;
        if (bl2) {
            n = this.B();
        } else {
            int n2 = this.n.length();
            int n3 = this.r;
            n = Math.min(n2, 2 * (n3 | 1) - (n3 ^ 1));
        }
        if (bl) {
            if (!this.G()) {
                this.s = this.r;
            }
            this.t = n;
        } else {
            if (this.G()) {
                n = Math.max(this.s, this.t);
            }
            this.H();
        }
        this.r = n;
        this.J();
    }

    @Override
    public float b() {
        return 20.0f;
    }

    private void b(boolean bl) {
        if (bl) {
            if (!this.G()) {
                this.s = this.r;
            }
            this.t = this.n.length();
        } else {
            this.H();
        }
        this.r = this.n.length();
        this.J();
    }

    private boolean b(char c, int n) {
        switch (this.m.ordinal()) {
            case 0: {
                return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == ' ';
            }
            case 1: {
                return c >= '0' && c <= '9';
            }
            case 2: {
                return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_';
            }
            case 3: {
                return c >= '0' && c <= '9' || c == '-' && n == 0 && !this.n.contains("-");
            }
        }
        return true;
    }

    private int x() {
        return this.n.substring(0, this.r).replace(",", "").length();
    }

    private String s() {
        return TextSettingRow.$sf$2(this.r(), this.n);
    }

    public TextInputMode c() {
        return this.m;
    }

    public static TextSettingRow n() {
        for (TextSettingRow wos1w22 : d) {
            if (!wos1w22.q) continue;
            return wos1w22;
        }
        return null;
    }

    public int h() {
        if (this.m == TextInputMode.INT) {
            try {
                return Integer.parseInt(this.n);
            }
            catch (NumberFormatException numberFormatException) {
                return 0;
            }
        }
        if (this.m != TextInputMode.PRICE) {
            return 0;
        }
        try {
            return Integer.parseInt(this.n.replace(",", ""));
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    public String f() {
        switch (this.m.ordinal()) {
            case 0: {
                return TextSettingRow.$sf$0(this.n);
            }
            case 1: {
                return TextSettingRow.$sf$1(this.n);
            }
        }
        return this.n;
    }

    @Override
    public boolean d() {
        return BooleanCoercion.from(this.p == null || this.p.m() ? 1 : 0);
    }

    private int a(int n) {
        String string = this.n.replace(",", "");
        if (n > string.length()) {
            n = string.length();
        }
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        while (n4 < this.n.length() && n2 < n) {
            if (this.n.charAt(n4) != ',') {
                ++n2;
            }
            int n5 = n4++;
            n3 = (n5 ^ 1) + 2 * (n5 & 1);
        }
        return n3;
    }

    private void a(boolean bl, boolean bl2) {
        int n;
        if (bl2) {
            n = this.A();
        } else {
            int n2 = this.r;
            n = Math.max(0, (n2 ^ 1) - 2 * (~n2 & 1));
        }
        if (bl) {
            if (!this.G()) {
                this.s = this.r;
            }
            this.t = n;
        } else {
            if (this.G()) {
                n = Math.min(this.s, this.t);
            }
            this.H();
        }
        this.r = n;
        this.J();
    }

    private void a(boolean bl) {
        if (bl) {
            if (!this.G()) {
                this.s = this.r;
            }
            this.t = 0;
        } else {
            this.H();
        }
        this.r = 0;
        this.J();
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        float f4 = this.F;
        float f5 = f + f3 - 8.0f - f4;
        if (!GuiInput.a(f5, f2 + 10.0f - 7.0f, f4, 14.0f, (double)n, (double)n2)) {
            if (this.q) {
                this.q = false;
                this.u.a(0.0, 0.2, Easings.h);
                this.G = "";
            }
            return false;
        }
        TextSettingRow.a(this);
        if (!this.q) {
            this.q = true;
            this.u.a(1.0, 0.2, Easings.h);
            this.w.d(1.0);
            this.A = false;
            this.w.a(0.3, 0.5, Easings.i);
        }
        FontRenderer v6hnga2 = ClientFonts.a[14];
        this.r = v6hnga2.a(this.n, (float)n - (f5 + 5.0f + v6hnga2.a(this.r())));
        this.H();
        this.u();
        return true;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f6 = 20.0f * f5;
        float f7 = 8.0f * f5;
        this.E = f5;
        this.y.a(GuiInput.a(f, f2, f3, f6, (double)n, (double)n2));
        this.u.a();
        this.v.a();
        this.x.a();
        this.F = (float)this.x.j();
        if (this.q) {
            this.w.a();
            if (this.w.d()) {
                this.A = BooleanCoercion.from(this.A ? 0 : 1);
                this.w.a(!this.A ? 0.3 : 1.0, 0.5, Easings.i);
            }
        } else {
            this.w.d(1.0);
            this.A = true;
        }
        int n3 = (int)(255.0f * f4);
        float f8 = this.F * f5;
        float f9 = 14.0f * f5;
        float f10 = f + f3 - f7 - f8;
        float f11 = f2 + f6 / 2.0f - f9 / 2.0f;
        this.y.a(matrixStack, s7swsm2, v6hnga2, this.l, f + f7, f2 + f6 / 2.0f - v6hnga2.b(this.l) * f5 / 4.0f - 1.0f, f10 - (f + f7) - 4.0f * f5, f5, pryrvd.a, f4);
        this.B = f10;
        this.C = f11;
        this.D = f8;
        boolean bl = GuiInput.a(f10, f11, f8, f9, (double)n, (double)n2);
        if (bl != this.z) {
            this.v.a(!bl ? 0.0 : 1.0, 0.15, Easings.h);
            this.z = bl;
        }
        float f12 = (float)this.u.j();
        float f13 = (float)this.v.j();
        Color color = ColorUtils.a(pryrvd.m, pryrvd.y, f12);
        Color color2 = ColorUtils.a(pryrvd.r, pryrvd.z, f12);
        Color color3 = pryrvd.a(color, n3);
        Color color4 = pryrvd.a(color2, n3);
        s7swsm2.a(f10 - 0.5f * f5, f11 - 0.5f * f5, f8 + f5, f9 + f5, 4.0f * f5, color3, color3, color4, color4, matrixStack);
        int n4 = (int)(f13 * 3.0f);
        Color color5 = pryrvd.a(pryrvd.b(pryrvd.WIDGET_BG_TOP, n4), n3);
        Color color6 = pryrvd.a(pryrvd.b(pryrvd.WIDGET_BG_BOT, n4), n3);
        s7swsm2.a(f10, f11, f8, f9, 4.0f * f5, color5, color5, color6, color6, matrixStack);
        float f14 = 5.0f * f5;
        float f15 = f10 + f14;
        s7swsm2.b().a(f15 - 1.0f, f11, f8 - f14 * 2.0f + 2.0f, f9, matrixStack);
        String string = this.r();
        String string2 = this.s();
        float f16 = f11 + f9 / 2.0f - v6hnga2.b("A") * f5 / 4.0f;
        if (!this.n.isEmpty() || this.q) {
            float f17;
            if (this.G()) {
                int n5 = Math.min(this.s, this.t);
                int n6 = Math.max(this.s, this.t);
                float f18 = v6hnga2.a(string) * f5;
                float f19 = f15 + f18 + v6hnga2.a(this.n, n5) * f5;
                s7swsm2.a(f19, f11 + 2.0f * f5, f15 + f18 + v6hnga2.a(this.n, n6) * f5 - f19, f9 - 4.0f * f5, 2.0f * f5, new Color(pryrvd.A.getRed(), pryrvd.A.getGreen(), pryrvd.A.getBlue(), (int)((float)pryrvd.A.getAlpha() * f4)), matrixStack);
            }
            Color textColor = pryrvd.a(pryrvd.a, n3);
            matrixStack.push();
            matrixStack.translate(f15, f16, 0.0f);
            matrixStack.scale(f5, f5, 1.0f);
            matrixStack.translate(-f15, -f16, 0.0f);
            v6hnga2.a(string2, f15, (double)f16, textColor, matrixStack);
            matrixStack.pop();
            if (this.m == TextInputMode.PLAYER && !this.G.isEmpty() && this.q) {
                float f20 = f15 + v6hnga2.a(string2) * f5;
                Color color7 = pryrvd.a(pryrvd.d, n3);
                matrixStack.push();
                matrixStack.translate(f20, f16, 0.0f);
                matrixStack.scale(f5, f5, 1.0f);
                matrixStack.translate(-f20, -f16, 0.0f);
                v6hnga2.a(this.G, f20, (double)f16, color7, matrixStack);
                matrixStack.pop();
            }
            if (this.q && !this.G() && (f17 = (float)this.w.j()) > 0.3f) {
                s7swsm2.a(f15 + v6hnga2.a(string) * f5 + v6hnga2.a(this.n, this.r) * f5, f11 + 3.0f * f5, 1.0f * f5, f9 - 6.0f * f5, pryrvd.a(pryrvd.a, (int)(255.0f * f4 * f17)), matrixStack);
            }
        } else {
            String placeholder = TextSettingRow.$sf$2(string, this.o);
            if (!placeholder.isEmpty()) {
                Color color8 = pryrvd.a(pryrvd.b, n3);
                matrixStack.push();
                matrixStack.translate(f15, f16, 0.0f);
                matrixStack.scale(f5, f5, 1.0f);
                matrixStack.translate(-f15, -f16, 0.0f);
                v6hnga2.a(placeholder, f15, (double)f16, color8, matrixStack);
                matrixStack.pop();
            }
        }
        if (this.n.isEmpty() && this.q && !this.G()) {
            float f21 = (float)this.w.j();
            float f22 = v6hnga2.a(string) * f5;
            Color color9 = pryrvd.a(pryrvd.a, n3);
            matrixStack.push();
            matrixStack.translate(f15, f16, 0.0f);
            matrixStack.scale(f5, f5, 1.0f);
            matrixStack.translate(-f15, -f16, 0.0f);
            v6hnga2.a(string, f15, (double)f16, color9, matrixStack);
            matrixStack.pop();
            s7swsm2.a(f15 + f22, f11 + 3.0f * f5, 1.0f * f5, f9 - 6.0f * f5, pryrvd.a(pryrvd.a, (int)(255.0f * f4 * f21)), matrixStack);
        }
        s7swsm2.b().a(matrixStack);
        if (bl || this.q) {
            GuiInput.g();
        }
    }

    public void a(String input) {
        if (input == null) {
            input = "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (!this.b(c, stringBuilder.length())) continue;
            stringBuilder.append(c);
        }
        this.n = stringBuilder.toString();
        if (this.m == TextInputMode.PRICE) {
            this.n = this.b(this.n.replace(",", ""));
        }
        this.r = Math.min(this.r, this.n.length());
        this.H();
        this.t();
        this.q();
    }

    private static void a(TextSettingRow wos1w22) {
        for (TextSettingRow wos1w23 : d) {
            if (wos1w23 == wos1w22 || !wos1w23.q) continue;
            wos1w23.q = false;
            wos1w23.u.a(0.0, 0.2, Easings.h);
            wos1w23.G = "";
        }
    }

    @Override
    public boolean a(char c, int n) {
        if (this.q && !Character.isISOControl(c)) {
            if (!this.a(c)) {
                return true;
            }
            if (this.n.length() >= 16 && !this.G()) {
                return true;
            }
            if (this.G()) {
                this.I();
            }
            if (this.n.length() < 16) {
                this.n = TextSettingRow.$sf$3(this.n.substring(0, this.r), c, this.n.substring(this.r));
                int n2 = this.r;
                this.r = (n2 | 1) + (n2 & 1);
                if (this.m == TextInputMode.PRICE) {
                    int n3 = this.x();
                    this.n = this.b(this.n.replace(",", ""));
                    this.r = this.a(n3);
                }
                this.J();
                this.u();
                this.t();
                this.q();
            }
            return true;
        }
        return false;
    }

    @Override
    public String a() {
        return this.l;
    }

    private boolean a(char c) {
        return this.b(c, this.r);
    }

    @Override
    public boolean a(int n, int n2, int n3) {
        int n4;
        if (!this.q) {
            return false;
        }
        int n5 = (~n3 | 2) - ~n3 == 0 ? 0 : 1;
        int n6 = n4 = (~n3 | 1) - ~n3 == 0 ? 0 : 1;
        if (n5 != 0) {
            switch (n) {
                case 65: {
                    this.C();
                    break;
                }
                case 67: {
                    this.D();
                    break;
                }
                case 86: {
                    this.E();
                    break;
                }
                case 88: {
                    this.F();
                }
            }
            return true;
        }
        switch (n) {
            case 256: {
                this.q = false;
                this.u.a(0.0, 0.2, Easings.h);
                this.G = "";
                this.H();
                break;
            }
            case 257: {
                this.q = false;
                this.u.a(0.0, 0.2, Easings.h);
                this.G = "";
                this.H();
                break;
            }
            case 258: {
                if (this.m != TextInputMode.PLAYER) break;
                this.w();
                break;
            }
            case 259: {
                this.y();
                break;
            }
            case 261: {
                this.z();
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
                this.a(BooleanCoercion.from(n4));
                break;
            }
            case 269: {
                this.b(BooleanCoercion.from(n4));
            }
        }
        return true;
    }

    public static boolean m() {
        Iterator<TextSettingRow> iterator = d.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().q) continue;
            return true;
        }
        return false;
    }

    public static void o() {
        for (TextSettingRow wos1w22 : d) {
            if (!wos1w22.q) continue;
            wos1w22.q = false;
            wos1w22.u.d(0.0);
            wos1w22.G = "";
            wos1w22.H();
        }
    }

    public boolean p() {
        if (this.m == TextInputMode.PLAYER) {
            return BooleanCoercion.from(this.n.length() < 3 ? 0 : 1);
        }
        return true;
    }

    private void t() {
        FontRenderer v6hnga2 = ClientFonts.a[14];
        String string = this.s();
        String string2 = string.isEmpty() ? TextSettingRow.$sf$2(this.r(), this.o) : string;
        float f = Math.max(40.0f, Math.min(100.0f, v6hnga2.a(string2) + 10.0f));
        if (Math.abs(f - this.F) > 1.0f) {
            this.x.a((double)f, 0.15, Easings.h);
        }
    }

    public String g() {
        return this.m != TextInputMode.PRICE ? this.n : this.n.replace(",", "");
    }

    private List<String> v() {
        ArrayList<String> arrayList = new ArrayList<String>();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.getNetworkHandler() != null) {
            for (PlayerListEntry playerListEntry : minecraftClient.getNetworkHandler().getPlayerList()) {
                if (playerListEntry.getProfile() == null || playerListEntry.getProfile().getName() == null) continue;
                arrayList.add(playerListEntry.getProfile().getName());
            }
        }
        return arrayList;
    }

    private void q() {
        if (this.p != null) {
            this.p.a(this.n);
        }
    }

    private void z() {
        if (this.G()) {
            this.I();
        } else if (this.r < this.n.length()) {
            this.n = TextSettingRow.$sf$2(this.n.substring(0, this.r), this.n.substring(this.r - -2 - 1));
            if (this.m == TextInputMode.PRICE) {
                int n = this.x();
                this.n = this.b(this.n.replace(",", ""));
                this.r = this.a(n);
            }
        }
        this.J();
        this.u();
        this.t();
        this.q();
    }

    private void w() {
        if (this.G.isEmpty()) {
            return;
        }
        this.n = TextSettingRow.$sf$2(this.n, this.G);
        this.r = this.n.length();
        this.G = "";
        this.H();
        this.t();
    }

    private void u() {
        this.G = "";
        if (this.m != TextInputMode.PLAYER || this.n.isEmpty()) {
            return;
        }
        List<String> list = this.v();
        String string = this.n.toLowerCase(Locale.ROOT);
        for (String string2 : list) {
            if (!string2.toLowerCase(Locale.ROOT).startsWith(string) || string2.equalsIgnoreCase(this.n)) continue;
            this.G = string2.substring(this.n.length());
            return;
        }
    }

    private String r() {
        switch (this.m.ordinal()) {
            case 0: {
                return "/";
            }
            case 1: {
                return "$";
            }
        }
        return "";
    }

    private void E() {
        int n;
        int n2;
        char c;
        String string = MinecraftClient.getInstance().keyboard.getClipboard();
        if (string == null || string.isEmpty()) {
            return;
        }
        String string2 = string.replaceAll("[\\r\\n\\t]", "");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string2.length(); ++i) {
            c = string2.charAt(i);
            if (!this.b(c, ((n2 = this.r) & ~(n = stringBuilder.length())) + (n & ~n2) + 2 * (n2 & n))) continue;
            stringBuilder.append(c);
        }
        String string3 = stringBuilder.toString();
        if (this.G()) {
            this.I();
        }
        int remaining = 16 - this.n.length();
        if (string3.length() > remaining) {
            string3 = string3.substring(0, remaining);
        }
        if (!string3.isEmpty()) {
            this.n = TextSettingRow.$sf$4(this.n.substring(0, this.r), string3, this.n.substring(this.r));
            n2 = this.r;
            n = string3.length();
            this.r = (n2 ^ n) + 2 * (n2 & n);
            if (this.m == TextInputMode.PRICE) {
                int n3 = this.x();
                this.n = this.b(this.n.replace(",", ""));
                this.r = this.a(n3);
            }
        }
        this.J();
        this.u();
        this.t();
        this.q();
    }

    private void y() {
        if (this.G()) {
            this.I();
        } else if (this.r > 0) {
            this.n = TextSettingRow.$sf$2(this.n.substring(0, this.r - 1), this.n.substring(this.r));
            int n = this.r;
            this.r = (n ^ 1) - 2 * (~n & 1);
            if (this.m == TextInputMode.PRICE) {
                int n2 = this.x();
                this.n = this.b(this.n.replace(",", ""));
                this.r = this.a(n2);
            }
        }
        this.J();
        this.u();
        this.t();
        this.q();
    }

    private int A() {
        int n;
        int n2;
        if (this.r == 0) {
            return 0;
        }
        for (n2 = this.r - 1; n2 > 0 && Character.isWhitespace(this.n.charAt(n2)); --n2) {
        }
        while (n2 > 0 && !Character.isWhitespace(this.n.charAt(2 * ((n = n2--) & 0xFFFFFFFE) - (n ^ 1)))) {
        }
        return n2;
    }

    private static /* synthetic */ String $sf$0(String string) {
        return "/" + string;
    }

    private static /* synthetic */ String $sf$4(String string, String string2, String string3) {
        return string + string2 + string3;
    }

    private static /* synthetic */ String $sf$3(String string, char c, String string2) {
        int n = 574;
        return string + c + string2;
    }

    private static /* synthetic */ String $sf$1(String string) {
        return "$" + string;
    }

    private static /* synthetic */ String $sf$2(String string, String string2) {
        return string + string2;
    }
}
