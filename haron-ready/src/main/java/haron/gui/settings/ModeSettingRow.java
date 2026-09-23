package haron.gui.settings;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.core.GuiLayerRegistry;
import haron.gui.core.GuiLayer;
import haron.gui.settings.SettingRow;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.ShapeRenderer;
import haron.settings.ModeSetting;
import haron.theme.pryrvd;
import haron.util.cqqvax;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;

public class ModeSettingRow
implements SettingRow {
    public static final float a = 25.0f;
    private static final float e = 8.0f;
    private static final float f = 17.0f;
    private static final float g = 6.0f;
    private static final float h = 20.0f;
    private static final float i = 7.5f;
    private static final float j = 6.0f;
    private static final float k = -2.0f;
    private static final float l = 7.5f;
    private static final float m = 6.0f;
    private static final float n = 1.5f;
    private static final float o = 15.0f;
    private final String q;
    private final String[] r;
    private final boolean s;
    private final ModeSetting t;
    private int u;
    private final Set<Integer> v;
    private boolean w = false;
    private final AnimatedValue x = new AnimatedValue();
    private final AnimatedValue y = new AnimatedValue();
    private final cqqvax z = new cqqvax();
    private final List<AnimatedValue> A = new ArrayList<AnimatedValue>();
    private final List<Boolean> B = new ArrayList<Boolean>();
    private boolean C = false;
    private boolean D = false;
    private boolean E = false;
    private float F;
    private float G;
    private float H;
    private float I = 1.0f;
    private float J;
    private float K;
    private float L;
    private float M;
    private float N;
    public static int b;
    public static boolean c;
    private static final Set<ModeSettingRow> d;
    private static final float DROPDOWN_MAX_HEIGHT = 150.0f;

    public static ModeSettingRow getOpenDropdown() {
        for (ModeSettingRow tn8pw72 : d) {
            if (!tn8pw72.o()) continue;
            return tn8pw72;
        }
        return null;
    }

    public void scrollDropdown(double d) {
        float f;
        float f2;
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f3 = v6hnga2.b(this.r[0]) * this.I;
        float f4 = f3 + (f2 = -2.0f * this.I);
        float f5 = (float)this.r.length * f4;
        float f6 = f5 + 12.0f * this.I;
        if (f6 <= (f = Math.min(150.0f * this.I, ModeSettingRow.getScreenHeight() * 0.7f))) {
            return;
        }
        float f7 = f6 - f;
        this.N = Math.max(0.0f, Math.min(f7, this.N - (float)(d * (double)f4)));
    }

    public static boolean isAnyDropdownOpen() {
        for (ModeSettingRow tn8pw72 : d) {
            if (!tn8pw72.o()) continue;
            return true;
        }
        return false;
    }

    private static float getScreenHeight() {
        try {
            return MinecraftClient.getInstance().getWindow().getScaledHeight();
        }
        catch (Exception exception) {
            return 300.0f;
        }
    }

    public void resetScroll() {
        this.N = 0.0f;
    }

    public static boolean isAnyDropdownOpenAt(int n, int n2) {
        for (ModeSettingRow tn8pw72 : d) {
            if (!tn8pw72.o() || !tn8pw72.c(n, n2)) continue;
            return true;
        }
        return false;
    }

    public ModeSettingRow(ModeSetting s82syr2) {
        this.t = s82syr2;
        this.q = s82syr2.f();
        this.r = s82syr2.a();
        this.s = s82syr2.c();
        this.u = (Integer)s82syr2.k();
        this.v = new HashSet<Integer>(s82syr2.e());
        this.q();
        d.add(this);
    }

    public ModeSettingRow(String string, String[] stringArray, int n) {
        this(string, stringArray, n, false);
    }

    public ModeSettingRow(String string, String[] stringArray, int n, boolean bl) {
        this.t = null;
        this.q = string;
        this.r = stringArray;
        this.s = bl;
        int n2 = stringArray.length;
        this.u = Math.max(0, Math.min(n, (n2 & 0xFFFFFFFE) - (~n2 & 1)));
        this.v = new HashSet<Integer>();
        if (bl && n >= 0 && n < stringArray.length) {
            this.v.add(n);
        }
        this.q();
        d.add(this);
    }

    public ModeSettingRow(String string, String[] stringArray, String string2) {
        this(string, stringArray, string2, false);
    }

    public ModeSettingRow(String string, String[] stringArray, Set<String> set) {
        this.t = null;
        this.q = string;
        this.r = stringArray;
        this.s = true;
        this.u = 0;
        this.v = new HashSet<Integer>();
        for (int i = 0; i < stringArray.length; ++i) {
            if (!set.contains(stringArray[i])) continue;
            this.v.add(i);
            if (this.v.size() != 1) continue;
            this.u = i;
        }
        this.q();
        d.add(this);
    }

    public ModeSettingRow(String string, String[] stringArray, int[] nArray) {
        this.t = null;
        this.q = string;
        this.r = stringArray;
        this.s = true;
        this.u = nArray.length <= 0 ? 0 : nArray[0];
        this.v = new HashSet<Integer>();
        for (int n : nArray) {
            if (n < 0 || n >= stringArray.length) continue;
            this.v.add(n);
        }
        this.q();
        d.add(this);
    }

    public ModeSettingRow(String string, String[] stringArray, String string2, boolean bl) {
        this.t = null;
        this.q = string;
        this.r = stringArray;
        this.s = bl;
        this.u = 0;
        this.v = new HashSet<Integer>();
        int n = 0;
        while (n < stringArray.length) {
            if (stringArray[n].equals(string2)) {
                this.u = n;
                if (!bl) continue;
                this.v.add(n);
                continue;
            }
            ++n;
        }
        this.q();
        d.add(this);
    }

    static {
        d = Collections.newSetFromMap(new WeakHashMap());
    }

    public String e() {
        return this.r[this.u];
    }

    public String[] i() {
        return this.r;
    }

    public boolean b(int n, int n2) {
        boolean bl = GuiInput.a(this.F, this.G, this.H, 17.0f * this.I, (double)n, (double)n2);
        if (this.w || this.x.j() > 0.1) {
            return BooleanCoercion.from(bl || GuiInput.a(this.J, this.K, this.L, this.M, (double)n, (double)n2) ? 1 : 0);
        }
        return bl;
    }

    @Override
    public float b() {
        return 25.0f;
    }

    public void b(Set<String> set) {
        if (this.s) {
            this.v.clear();
            for (int i = 0; i < this.r.length; ++i) {
                if (!set.contains(this.r[i])) continue;
                this.v.add(i);
            }
            if (this.v.isEmpty()) {
                return;
            }
            this.u = this.v.iterator().next();
        }
    }

    public boolean b(int n) {
        if (this.s) {
            return this.v.contains(n);
        }
        return BooleanCoercion.from(n != this.u ? 0 : 1);
    }

    @Override
    public boolean b(float f, float f2, float f3, int n, int n2) {
        return this.d(n, n2);
    }

    private void s() {
        for (ModeSettingRow tn8pw72 : d) {
            if (tn8pw72 == this || !tn8pw72.w || !this.a(tn8pw72)) continue;
            tn8pw72.w = false;
        }
    }

    public boolean c() {
        return this.s;
    }

    public boolean c(int n, int n2) {
        if (!this.w && this.x.j() < 0.1) {
            return false;
        }
        return GuiInput.a(this.J, this.K, this.L, this.M * (float)this.x.j(), (double)n, (double)n2);
    }

    public void c(int n) {
        if (!this.s || n < 0 || n >= this.r.length) {
            return;
        }
        if (this.v.contains(n)) {
            this.v.remove(n);
        } else {
            this.v.add(n);
        }
        if (!this.v.isEmpty()) {
            this.u = this.v.iterator().next();
        }
        if (this.t != null) {
            this.t.b(n);
        }
    }

    public float n() {
        if (!this.w && this.x.j() < 0.01) {
            return 0.0f;
        }
        return this.K + this.M * (float)this.x.j();
    }

    public Set<String> h() {
        return !this.s ? Collections.singleton(this.r[this.u]) : this.v.stream().map(n -> {
            return this.r[n];
        }).collect(Collectors.toSet());
    }

    public int f() {
        return this.u;
    }

    @Override
    public boolean l() {
        return this.w || !(this.x.j() <= 0.1);
    }

    @Override
    public boolean d() {
        int n = this.t == null || this.t.m() ? 1 : 0;
        return BooleanCoercion.from(n);
    }

    private boolean d(int n, int n2) {
        if (GuiInput.a(this.F, this.G, this.H, 17.0f * this.I, (double)n, (double)n2)) {
            this.w = BooleanCoercion.from(this.w ? 0 : 1);
            if (!this.w) {
                this.N = 0.0f;
            }
            return true;
        }
        if (!this.w || this.x.j() <= 0.5) {
            return false;
        }
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f = (float)this.x.j();
        float f2 = 6.0f * this.I;
        float f3 = -2.0f * this.I;
        float f4 = v6hnga2.b(this.r[0]) * this.I;
        float f5 = f4 + f3;
        float f6 = this.N * f;
        float f7 = this.G + 17.0f * this.I + f2 * f - f6;
        int n3 = f5 <= 0.0f ? -1 : (int)(((float)n2 - f7) / f5);
        boolean bl = false;
        if (n3 >= 0 && n3 < this.r.length) {
            float f8 = (float)n2 - (f7 + (float)n3 * f5);
            boolean bl2 = bl = f8 >= 0.0f && f8 < f4;
        }
        if (!((float)n >= this.J && (float)n <= this.J + this.L && bl && n3 >= 0 && n3 < this.r.length)) {
            if (!GuiInput.a(this.J, this.K, this.L, this.M, (double)n, (double)n2)) {
                this.w = false;
                this.N = 0.0f;
            }
            return true;
        }
        if (this.s) {
            this.c(n3);
        } else {
            if (n3 != this.u) {
                this.u = n3;
                if (this.t != null) {
                    this.t.a(Integer.valueOf(n3));
                }
            }
            this.w = false;
            this.N = 0.0f;
        }
        return true;
    }

    @Override
    public String a() {
        return this.q;
    }

    private Color a(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)((float)color.getAlpha() * f))));
    }

    private boolean a(ModeSettingRow tn8pw72) {
        if (tn8pw72 == this || !tn8pw72.w || tn8pw72.x.j() < 0.1) {
            return false;
        }
        float f = this.K;
        float f2 = this.K + this.M;
        float f3 = this.J;
        float f4 = this.J + this.L;
        float f5 = tn8pw72.K;
        float f6 = tn8pw72.K + tn8pw72.M;
        float f7 = tn8pw72.J;
        boolean bl = f3 >= tn8pw72.J + tn8pw72.L ? false : f4 > f7;
        return BooleanCoercion.from(bl && (f > f6 ? 1 : (f == f6 ? 0 : -1)) < 0 && (f2 > f5 ? 1 : (f2 == f5 ? 0 : -1)) > 0 ? 1 : 0);
    }

    private void a(ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5, int n, MatrixStack matrixStack) {
        Color color = pryrvd.a(pryrvd.aa, n);
        float f6 = 3.0f * this.I * (1.0f - f5);
        float f7 = f3 / 2.0f;
        s7swsm2.a(f, f2 - f4 / 2.0f, f7 + 0.5f * this.I, f4, color, matrixStack);
        s7swsm2.a(f + f7 - 0.5f * this.I, f2 - f4 / 2.0f, f7 + 0.5f * this.I, f4, color, matrixStack);
        if (f6 <= 0.1f) {
            return;
        }
        float f8 = f2 - f6 / 2.0f;
        float f9 = f + f7;
        float f10 = f2 + f6 / 2.0f;
        float f11 = f2 - f6 / 2.0f;
        float f12 = f + f3;
        float f13 = (float)Math.atan2(f10 - f8, f9 - f);
        float f14 = (float)Math.atan2(f10 - f11, f9 - f12);
        float f15 = (float)Math.sqrt(Math.pow(f9 - f, 2.0) + Math.pow(f10 - f8, 2.0));
        float f16 = (float)Math.sqrt(Math.pow(f12 - f9, 2.0) + Math.pow(f11 - f10, 2.0));
        matrixStack.push();
        matrixStack.translate(f + f15 / 2.0f, (f8 + f10) / 2.0f, 0.0f);
        matrixStack.multiply(new Quaternionf().rotationZ(f13));
        s7swsm2.a(-f15 / 2.0f, -f4 / 2.0f, f15, f4, color, matrixStack);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(f9 + (f + f3 - f9) / 2.0f, (f10 + f11) / 2.0f, 0.0f);
        matrixStack.multiply(new Quaternionf().rotationZ(f14));
        s7swsm2.a(-f16 / 2.0f, -f4 / 2.0f, f16, f4, color, matrixStack);
        matrixStack.pop();
    }

    public void a(Set<Integer> set) {
        if (this.s) {
            this.v.clear();
            for (int n : set) {
                if (n < 0 || n >= this.r.length) continue;
                this.v.add(n);
            }
            if (!this.v.isEmpty()) {
                this.u = this.v.iterator().next();
            }
            if (this.t != null) {
                this.t.a(set);
            }
        }
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4, float f5) {
        FontRenderer v6hnga2 = ClientFonts.a[14];
        float f6 = 25.0f * f5;
        float f7 = 8.0f * f5;
        this.I = f5;
        this.z.a(GuiInput.a(f, f2, f3, f6, (double)n, (double)n2));
        if (this.w != this.D) {
            double d = this.w ? 1.0 : 0.0;
            this.x.a(d, !this.w ? 0.15 : 0.2, Easings.h);
            if (this.w) {
                this.E = true;
            }
            this.D = this.w;
        }
        this.x.a();
        this.y.a();
        int n3 = (int)(255.0f * f4);
        float f8 = 0.0f;
        for (String string : this.r) {
            f8 = Math.max(f8, v6hnga2.a(string));
        }
        float f9 = 6.0f * f5;
        float f10 = 6.0f * f5;
        float f11 = 17.0f * f5;
        float f12 = 7.5f * f5;
        float f13 = f8 * f5 + 20.0f * f5 + f10 + f9 * 2.0f;
        float f14 = f + f3 - f7 - f13 + 1.0f;
        this.z.a(matrixStack, s7swsm2, v6hnga2, this.q, f + f7, f2 + f6 / 2.0f - v6hnga2.b(this.q) * f5 / 4.0f - 1.0f, f14 - (f + f7) - 4.0f * f5, f5, pryrvd.a, f4);
        String string = this.r();
        float f15 = f2 + f6 / 2.0f - f11 / 2.0f - 1.0f;
        this.F = f14;
        this.G = f15;
        this.H = f13;
        boolean bl = GuiInput.a(f14, f15, f13, f11, (double)n, (double)n2);
        if (bl != this.C) {
            AnimatedValue urhlup2 = this.y;
            double d = bl ? 1.0 : 0.0;
            urhlup2.a(d, 0.15, Easings.h);
            this.C = bl;
        }
        float f16 = (float)this.x.j();
        Color color = this.a(pryrvd.CELL_BG_TOP, f4);
        Color color2 = this.a(pryrvd.CELL_BG_BOT, f4);
        Color color3 = this.a(pryrvd.PANEL_BORDER_TOP, f4);
        float f17 = f12 * (1.0f - f16);
        s7swsm2.a(f14 - 0.5f * f5, f15 - 0.5f * f5, f13 + f5, f11 + f5, f17, f17, f12, f12, color3, color3, color3, color3, matrixStack);
        s7swsm2.a(f14, f15, f13, f11, f17, f17, f12, f12, color, color, color2, color2, matrixStack);
        float f18 = f14 + f9;
        float f19 = f15 + f11 / 2.0f - v6hnga2.b(string) * f5 / 4.0f - 1.0f;
        float f20 = f14 + f13 - f9 - f10;
        float f21 = f20 - f18 - 4.0f * f5;
        float f22 = 15.0f * f5;
        s7swsm2.b().a(f18, f15, f21, f11, matrixStack);
        Color color4 = pryrvd.a(pryrvd.aa, n3);
        matrixStack.push();
        matrixStack.translate(f18, f19, 0.0f);
        matrixStack.scale(f5, f5, 1.0f);
        matrixStack.translate(-f18, -f19, 0.0f);
        v6hnga2.a(string, f18, (double)f19, color4, matrixStack);
        matrixStack.pop();
        if (v6hnga2.a(string) * f5 > f21 - f22) {
            float f23 = f18 + f21 - f22;
            Color color5 = pryrvd.a(pryrvd.CELL_BG_TOP, 0);
            Color color6 = this.a(pryrvd.CELL_BG_TOP, f4);
            s7swsm2.a(f23, f15, f22, f11, 0.0f, color5, color6, color5, color6, matrixStack);
        }
        s7swsm2.b().a(matrixStack);
        this.a(s7swsm2, f20, f15 + f11 / 2.0f, f10, 1.5f * f5, f16, n3, matrixStack);
        if (bl) {
            GuiInput.g();
        }
    }

    public void a(String string) {
        for (int i = 0; i < this.r.length; ++i) {
            if (!this.r[i].equals(string)) continue;
            this.u = i;
            if (this.s) {
                this.v.clear();
                this.v.add(i);
            }
            if (this.t != null) {
                this.t.a(string);
                return;
            }
            return;
        }
    }

    public void a(int n) {
        int n2 = this.r.length;
        this.u = Math.max(0, Math.min(n, (n2 & 0xFFFFFFFE) - (~n2 & 1)));
        if (this.s) {
            this.v.clear();
            this.v.add(this.u);
        }
        if (this.t != null) {
            this.t.a(Integer.valueOf(this.u));
        }
    }

    @Override
    public boolean a(float f, float f2, float f3, int n, int n2) {
        return this.d(n, n2);
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5, float f6) {
        if (this.w || this.x.j() >= 0.01) {
            this.x.a();
            float f7 = (float)this.x.j();
            if (f7 >= 0.01f) {
                FontRenderer v6hnga2 = ClientFonts.a[14];
                float f8 = 6.0f * this.I;
                float f9 = -2.0f * this.I;
                float f10 = 7.5f * this.I;
                float f11 = v6hnga2.b(this.r[0]) * this.I;
                float f12 = f11 + f9;
                float f13 = (float)this.r.length * f12 + f8 * 2.0f;
                float f14 = Math.min(150.0f * this.I, ModeSettingRow.getScreenHeight() * 0.7f);
                float f15 = Math.min(f13, f14);
                float f16 = this.H;
                float f17 = this.F;
                float f18 = this.G + 17.0f * this.I;
                this.J = f17;
                this.K = f18;
                this.L = f16;
                this.M = f15;
                if (this.E) {
                    this.s();
                    this.E = false;
                }
                float f19 = f15 * f7;
                float f20 = Math.max(0.0f, f13 - f14);
                this.N = Math.max(0.0f, Math.min(f20, this.N));
                float f21 = this.N * f7;
                if (this.w && f7 > 0.5f) {
                    GuiLayerRegistry.a().a(GuiLayer.DROPDOWN, f17, f18, f16, f19);
                }
                Color color = this.a(pryrvd.CELL_BG_TOP, f5 * f7);
                Color color2 = this.a(pryrvd.CELL_BG_BOT, f5 * f7);
                Color color3 = this.a(pryrvd.PANEL_BORDER_TOP, f5 * f7);
                s7swsm2.a(f17 - 0.5f * this.I, f18 - 0.5f * this.I, f16 + this.I, f19 + this.I, f10 * f7, f10 * f7, 0.0f, 0.0f, color3, color3, color3, color3, matrixStack);
                s7swsm2.a(f17, f18, f16, f19, f10 * f7, f10 * f7, 0.0f, 0.0f, color, color, color2, color2, matrixStack);
                if (f7 > 0.3f) {
                    int n3 = (int)(255.0f * f5 * Math.min(1.0f, (f7 - 0.3f) / 0.7f));
                    float f22 = f18 + f8 * f7;
                    int n4 = f12 <= 0.0f ? -1 : (int)(((float)n2 - f22 + f21) / f12);
                    boolean bl = false;
                    if (n4 >= 0 && n4 < this.r.length) {
                        float f23 = (float)n2 - f22 + f21 - (float)n4 * f12;
                        bl = f23 >= 0.0f && f23 < f11;
                    }
                    boolean bl2 = (float)n >= f17 && (float)n <= f17 + f16;
                    float f24 = f18 + f8 * f7 - f21;
                    int n5 = (int)(f21 / f12);
                    if (n5 < 0) {
                        n5 = 0;
                    }
                    while (n5 < this.r.length && f24 <= f18 + f19) {
                        if (f24 + f11 >= f18) {
                            int n6;
                            boolean bl3 = this.b(n5);
                            int n7 = n6 = bl2 && bl && n5 == n4 && f7 > 0.8f && this.w ? 1 : 0;
                            if (BooleanCoercion.from(n6) != this.B.get(n5)) {
                                this.A.get(n5).a(n6 == 0 ? 0.0 : 1.0, 0.12, Easings.h);
                                this.B.set(n5, BooleanCoercion.from(n6));
                            }
                            this.A.get(n5).a();
                            float f25 = (float)this.A.get(n5).j();
                            Color color4 = bl3 ? pryrvd.a(pryrvd.aa, n3) : pryrvd.a(ColorUtils.a(pryrvd.b, pryrvd.U, f25), n3);
                            float f26 = f17 + f8;
                            float f27 = f24;
                            matrixStack.push();
                            matrixStack.translate(f26, f27, 0.0f);
                            matrixStack.scale(this.I, this.I, 1.0f);
                            matrixStack.translate(-f26, -f27, 0.0f);
                            v6hnga2.a(this.r[n5], f26, (double)f27, color4, matrixStack);
                            matrixStack.pop();
                            if (n6 != 0 && this.w) {
                                GuiInput.g();
                            }
                        }
                        f24 += f12;
                        ++n5;
                    }
                }
            }
        }
    }

    public boolean m() {
        return this.w;
    }

    public boolean o() {
        return this.w || this.x.j() > 0.01;
    }

    public void p() {
        if (this.w) {
            this.w = false;
            this.N = 0.0f;
        }
    }

    public Set<Integer> g() {
        return !this.s ? Collections.singleton(this.u) : new HashSet<Integer>(this.v);
    }

    @Override
    public boolean j() {
        return this.w || this.x.j() > 0.01;
    }

    private void q() {
        this.N = 0.0f;
        for (int i = 0; i < this.r.length; ++i) {
            this.A.add(new AnimatedValue());
            this.B.add(false);
        }
    }

    private String r() {
        if (!this.s) {
            return this.r[this.u];
        }
        if (this.v.isEmpty()) {
            return "-";
        }
        ArrayList<Integer> arrayList = new ArrayList<Integer>(this.v);
        Collections.sort(arrayList);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayList.size(); ++i) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(this.r[arrayList.get(i)]);
        }
        return stringBuilder.toString();
    }
}

