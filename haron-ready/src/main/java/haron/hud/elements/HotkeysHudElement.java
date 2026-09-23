package haron.hud.elements;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.hud.core.HudElement;
import haron.hud.elements.HotkeyRowState;
import haron.hud.elements.HotkeyTextEntry;
import haron.hud.elements.HotkeySnapshot;
import haron.module.ModuleManager;
import haron.module.HaronModule;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HotkeysHudElement
extends HudElement {
    private static final Color q;
    private static final Color r;
    private static Color DIVIDER_CENTER;
    private static Color DIVIDER_EDGE;
    private static Color ROW_DIVIDER;
    private static Color ROW_DIVIDER_CENTER;
    private static final String HUD_TITLE;
    private static final float z = 8.5f;
    private static final float A = 8.0f;
    private static final float B = 24.0f;
    private static final float C = 16.0f;
    private static final float D = 5.0f;
    private static final float E = 12.0f;
    private static final float F = 2.0f;
    private static final float G = 12.0f;
    private static final float H = 6.0f;
    private static final float I = 6.5f;
    private static final float J = 0.5f;
    private static final float K = 100.0f;
    private static final double L = 0.2;
    private static final long M = 1000L;
    private static final float HEADER_DROP = 5.0f;
    private static final float ROW_TEXT_DROP = 4.0f;
    private static final float HEADER_ICON_Y_OFFSET = -2.0f;
    private static final float HEADER_ICON_SIZE = 10.0f;
    private final Map<HaronModule, HotkeyRowState> N = new LinkedHashMap<HaronModule, HotkeyRowState>();
    private final List<HotkeyTextEntry> O = new ArrayList<HotkeyTextEntry>();
    private final AnimatedValue P = new AnimatedValue();
    private final AnimatedValue Q = new AnimatedValue();
    private final AnimatedValue R = new AnimatedValue();
    private long S = 0L;
    private int T = 0;
    private boolean U = false;
    private boolean V = false;
    private boolean W = false;
    public static boolean n;

    private static int fontIndex(float f) {
        return Math.max(10, Math.min(48, Math.round(f)));
    }

    private static void updateAccentColors() {
        Color color = pryrvd.ACCENT;
        int n = color.getRed();
        int n2 = color.getGreen();
        int n3 = color.getBlue();
        DIVIDER_CENTER = new Color(n, n2, n3, 78);
        DIVIDER_EDGE = new Color(n, n2, n3, 33);
        ROW_DIVIDER = new Color(n, n2, n3, 33);
        ROW_DIVIDER_CENTER = new Color(n, n2, n3, 78);
    }

    private void drawGlowSegment(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5) {
        float f6 = 2.0f * f4;
        float f7 = f6 / 2.0f;
        float f8 = f3 / 2.0f;
        float f9 = f + f8;
        float f10 = f6 + 2.0f * f4;
        Color color = this.a(DIVIDER_EDGE, f5);
        Color color2 = this.a(DIVIDER_CENTER, f5);
        s7swsm2.a(f - 1.0f * f4, f2 - 1.0f * f4, f8 + 1.0f * f4, f10, f10 / 2.0f, color, color2, color, color2, matrixStack);
        s7swsm2.a(f9, f2 - 1.0f * f4, f8 + 1.0f * f4, f10, f10 / 2.0f, color2, color, color2, color, matrixStack);
        Color color3 = this.a(DIVIDER_EDGE, f5);
        Color color4 = this.a(DIVIDER_CENTER, f5);
        s7swsm2.a(f, f2, f8, f6, f7, color3, color4, color3, color4, matrixStack);
        s7swsm2.a(f9, f2, f8, f6, f7, color4, color3, color4, color3, matrixStack);
    }

    private void drawDivider(ShapeRenderer s7swsm2, MatrixStack matrixStack, float f, float f2, float f3, float f4, float f5) {
        float f6 = 0.8f * f5;
        float f7 = f3 / 2.0f;
        float f8 = f + f7;
        float f9 = f6 / 2.0f;
        s7swsm2.a(f, f2, f7, f6, f9, this.a(ROW_DIVIDER, f4), this.a(ROW_DIVIDER_CENTER, f4), this.a(ROW_DIVIDER, f4), this.a(ROW_DIVIDER_CENTER, f4), matrixStack);
        s7swsm2.a(f8, f2, f7, f6, f9, this.a(ROW_DIVIDER_CENTER, f4), this.a(ROW_DIVIDER, f4), this.a(ROW_DIVIDER_CENTER, f4), this.a(ROW_DIVIDER, f4), matrixStack);
    }

    private String trimToWidth(FontRenderer v6hnga2, String string, float f) {
        if (string == null) {
            return "";
        }
        if (f <= 0.0f) {
            return "";
        }
        if (v6hnga2.a(string) <= f) {
            return string;
        }
        String string2 = "...";
        if (v6hnga2.a(string2) > f) {
            return "";
        }
        String string3 = string;
        while (!string3.isEmpty()) {
            if (!(v6hnga2.a(HotkeysHudElement.$sf$0(string3, string2)) > f)) break;
            string3 = string3.substring(0, string3.length() - 1);
        }
        return string3.isEmpty() ? "" : HotkeysHudElement.$sf$0(string3, string2);
    }

    private static float rowWidth(FontRenderer v6hnga2, FontRenderer v6hnga3, String string, String string2, float f) {
        float f2 = 1.6f * f;
        return f2 + 8.0f * f + v6hnga2.a(string) + 8.0f * f + v6hnga3.a(string2);
    }

    private void drawRow(MatrixStack matrixStack, ShapeRenderer s7swsm2, FontRenderer v6hnga2, FontRenderer v6hnga3, float f, float f2, String string, String string2, float f3, float f4) {
        if (f3 < 0.01f) {
            return;
        }
        float f5 = f2 + 12.0f * f4 / 2.0f;
        float f6 = 1.6f * f4;
        float f7 = 8.0f * f4;
        s7swsm2.a(f, f5 - f7 / 2.0f, f6, f7, f6 / 2.0f, this.a(pryrvd.ACCENT, f3), matrixStack);
        float f8 = v6hnga3.a(string2);
        float f9 = this.b + this.d - 8.5f * f4 - f8;
        float f10 = f + f6 + 8.0f * f4;
        float f11 = f9 - f10 - 8.0f * f4;
        String string3 = this.trimToWidth(v6hnga2, string, f11);
        float f12 = 4.0f * f4;
        float f13 = f5 - v6hnga2.b(string3) / 2.0f + f12;
        float f14 = f5 - v6hnga3.b(string2) / 2.0f + f12;
        v6hnga2.a(string3, f10, (double)f13, this.a(r, f3), matrixStack);
        v6hnga3.a(string2, f9, (double)f14, this.a(pryrvd.ACCENT_SOFT, f3), matrixStack);
    }

    public HotkeysHudElement(float f, float f2) {
        super(f, f2);
        this.R.d(0.0);
        this.u();
        this.a();
    }

    static {
        HUD_TITLE = "Hot Keys";
        q = new Color(255, 255, 255, 255);
        r = new Color(243, 230, 215, 255);
    }

    /*
     * Enabled aggressive block sorting
     */
    private String b(int n) {
        String string;
        if (n == 0) {
            return "None";
        }
        if (n < 8) {
            switch (n) {
                case 0: {
                    return "LMB";
                }
                case 1: {
                    return "RMB";
                }
                case 2: {
                    return "MMB";
                }
            }
            return HotkeysHudElement.$sf$1(n);
        }
        switch (n) {
            case 32: {
                return "Space";
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
            case 59: {
                return ";";
            }
            case 61: {
                return "=";
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
                return "Esc";
            }
            case 257: {
                return "Enter";
            }
            case 258: {
                return "Tab";
            }
            case 259: {
                return "Back";
            }
            case 260: {
                return "Ins";
            }
            case 261: {
                return "Del";
            }
            case 262: {
                return "Right";
            }
            case 263: {
                return "Left";
            }
            case 264: {
                return "Down";
            }
            case 265: {
                return "Up";
            }
            case 266: {
                return "PgUp";
            }
            case 267: {
                return "PgDn";
            }
            case 268: {
                return "Home";
            }
            case 269: {
                return "End";
            }
            case 280: {
                return "Caps";
            }
            case 281: {
                return "Scroll";
            }
            case 282: {
                return "Num";
            }
            case 283: {
                return "Print";
            }
            case 284: {
                return "Pause";
            }
            case 340: {
                return "LShift";
            }
            case 341: {
                return "LCtrl";
            }
            case 342: {
                return "LAlt";
            }
            case 343: {
                return "LWin";
            }
            case 344: {
                return "RShift";
            }
            case 345: {
                return "RCtrl";
            }
            case 346: {
                return "RAlt";
            }
            case 347: {
                return "RWin";
            }
            case 348: {
                return "Menu";
            }
        }
        if (n >= 290 && n <= 301) {
            return HotkeysHudElement.$sf$2(n - 290 + 1);
        }
        if (n >= 302 && n <= 314) {
            return HotkeysHudElement.$sf$2(n - 302 + 13);
        }
        if (n >= 320 && n <= 329) {
            return HotkeysHudElement.$sf$3(n - 320);
        }
        switch (n) {
            case 330: {
                return "Num.";
            }
            case 331: {
                return "Num/";
            }
            case 332: {
                return "Num*";
            }
            case 333: {
                return "Num-";
            }
            case 334: {
                return "Num+";
            }
            case 335: {
                return "NumEnter";
            }
            case 336: {
                return "Num=";
            }
        }
        if (n >= 48 && n <= 57) {
            string = String.valueOf((char)n);
            return string;
        }
        if (n >= 65 && n <= 90) {
            string = String.valueOf((char)n);
            return string;
        }
        string = HotkeysHudElement.$sf$4(n);
        return string;
    }

    private List<HotkeySnapshot> x() {
        ArrayList<HotkeySnapshot> arrayList = new ArrayList<HotkeySnapshot>();
        for (HaronModule jxs16t2 : ModuleManager.all()) {
            if (!jxs16t2.k() || jxs16t2.j() == 0) continue;
            arrayList.add(new HotkeySnapshot(jxs16t2, jxs16t2.g(), this.b(jxs16t2.j())));
        }
        arrayList.sort(Comparator.comparing(xan4wx2 -> {
            return xan4wx2.b;
        }));
        return arrayList;
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
        if (this.a.player == null) {
            return;
        }
        HotkeysHudElement.updateAccentColors();
        this.a();
        float f3 = (float)this.R.j();
        if (f3 < 0.01f) {
            return;
        }
        boolean bl = this.v();
        float f4 = this.g();
        FontRenderer v6hnga2 = ClientFonts.b[HotkeysHudElement.fontIndex(15.0f * f4)];
        FontRenderer v6hnga3 = ClientFonts.a[HotkeysHudElement.fontIndex(16.0f * f4)];
        FontRenderer v6hnga4 = ClientFonts.a[HotkeysHudElement.fontIndex(14.0f * f4)];
        float f5 = 8.5f * f4;
        float f6 = 24.0f * f4;
        float f7 = 12.0f * f4;
        float f8 = 2.0f * f4;
        float f9 = 6.5f * f4;
        Color color = this.a(pryrvd.PANEL_BG_TOP, f3);
        Color color2 = this.a(pryrvd.PANEL_BG_BOT, f3);
        s7swsm2.a(this.b, this.c, this.d, this.e, f9, color, color, color2, color2, matrixStack);
        float f10 = this.b + f5 + 2.0f * f4;
        float f11 = v6hnga2.b("Hot Keys");
        float f12 = this.c + f6 / 2.0f - f11 / 2.0f + 5.0f * f4;
        v6hnga2.a("Hot Keys", f10, (double)f12, this.a(q, f3), matrixStack);
        float f13 = v6hnga2.a("Hot Keys");
        float f14 = f12 + f11 / 2.0f;
        Identifier identifier = HaronIcons.get("keyboard");
        float f15 = 10.0f * f4;
        float f16 = this.b + this.d - f5 - f15 - 1.0f * f4;
        float f17 = f14 - f15 / 2.0f + -2.0f * f4;
        s7swsm2.a(identifier, f16, f17, f15, f15, this.a(pryrvd.ACCENT_SOFT, f3), matrixStack);
        float f18 = this.c + f6;
        this.drawGlowSegment(s7swsm2, matrixStack, f10, f18, f13, f4, f3);
        float f19 = f16 - 3.0f * f4;
        float f20 = f15 + 6.0f * f4;
        this.drawGlowSegment(s7swsm2, matrixStack, f19, f18, f20, f4, f3);
        float f21 = this.b + f5;
        float f22 = this.c + f6 + 6.0f * f4;
        if (bl && !this.O.isEmpty()) {
            HotkeyTextEntry w3j3y52 = this.O.get(this.T % this.O.size());
            this.drawRow(matrixStack, s7swsm2, v6hnga3, v6hnga4, f21, f22, w3j3y52.a, this.normalizeKey(w3j3y52.b), f3, f4);
        } else {
            boolean bl2 = true;
            for (HotkeyRowState avar022 : this.N.values()) {
                float f23 = (float)avar022.d.j() * f3;
                if (f23 < 0.01f) continue;
                float f24 = f22 + (float)avar022.e.j() * (f7 + f8);
                if (!bl2) {
                    this.drawDivider(s7swsm2, matrixStack, f21, f24 - f8, this.d - f5 * 2.0f, f23, f4);
                }
                this.drawRow(matrixStack, s7swsm2, v6hnga3, v6hnga4, f21, f24, avar022.b, this.normalizeKey(avar022.c), f23, f4);
                bl2 = false;
            }
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    protected void a() {
        int n;
        int activeCount = 0;
        long l;
        this.w();
        boolean bl = this.v();
        List<HotkeySnapshot> list = this.x();
        float f = this.g();
        FontRenderer v6hnga2 = ClientFonts.b[HotkeysHudElement.fontIndex(15.0f * f)];
        FontRenderer v6hnga3 = ClientFonts.a[HotkeysHudElement.fontIndex(16.0f * f)];
        FontRenderer v6hnga4 = ClientFonts.a[HotkeysHudElement.fontIndex(14.0f * f)];
        if (bl && !this.O.isEmpty() && (l = System.currentTimeMillis()) - this.S >= 1000L) {
            this.T = (this.T + 1) % this.O.size();
            this.S = l;
        }
        HashSet<HaronModule> hashSet = new HashSet<HaronModule>();
        for (HotkeySnapshot iterator : list) {
            hashSet.add(iterator.a);
        }
        for (HotkeyRowState avar022 : this.N.values()) {
            if (hashSet.contains(avar022.a) || avar022.f) continue;
            avar022.f = true;
            avar022.d.a(0.0, 0.2, Easings.h);
        }
        int n2 = 0;
        for (HotkeySnapshot xan4wx2 : list) {
            HotkeyRowState avar023 = this.N.get(xan4wx2.a);
            if (avar023 == null) {
                HotkeyRowState avar024 = new HotkeyRowState(xan4wx2.a, xan4wx2.b, xan4wx2.c);
                avar024.d.d(0.0);
                avar024.d.a(1.0, 0.2, Easings.h);
                avar024.e.d(n2);
                this.N.put(xan4wx2.a, avar024);
            } else {
                avar023.b = xan4wx2.b;
                avar023.c = xan4wx2.c;
                avar023.f = false;
                if (avar023.d.i() < 1.0) {
                    avar023.d.a(1.0, 0.2, Easings.h);
                }
                if (Math.abs(avar023.e.i() - (double)n2) > 0.01) {
                    avar023.e.a((double)n2, 0.2, Easings.h);
                }
            }
            ++n2;
        }
        this.N.entrySet().removeIf(entry -> ((HotkeyRowState)entry.getValue()).f && ((HotkeyRowState)entry.getValue()).d.j() < 0.01);
        for (HotkeyRowState avar025 : this.N.values()) {
            avar025.d.a();
            avar025.e.a();
        }
        boolean bl2 = this.V;
        this.V = false;
        boolean bl3 = false;
        if (bl) {
            this.V = true;
        } else {
            for (HotkeyRowState avar024 : this.N.values()) {
                if (avar024.f) continue;
                this.V = true;
                ++activeCount;
            }
        }
        if (this.V && !bl2) {
            this.R.a(1.0, 0.2, Easings.h);
        } else if (!this.V && bl2) {
            this.R.a(0.0, 0.2, Easings.h);
        } else if (this.V && this.R.i() < 1.0) {
            this.R.a(1.0, 0.2, Easings.h);
        }
        this.R.a();
        float f2 = 8.5f * f;
        float f3 = 24.0f * f;
        float f4 = 12.0f * f;
        float f5 = 2.0f * f;
        float f6 = 100.0f * f;
        float f7 = v6hnga2.a("Hot Keys") + 42.0f * f;
        f6 = Math.max(f6, f7);
        if (bl && !this.O.isEmpty()) {
            HotkeyTextEntry w3j3y52 = this.O.get(this.T % this.O.size());
            f6 = Math.max(f6, HotkeysHudElement.rowWidth(v6hnga3, v6hnga4, w3j3y52.a, this.normalizeKey(w3j3y52.b), f));
        } else {
            for (HotkeyRowState avar026 : this.N.values()) {
                if (avar026.f) continue;
                f6 = Math.max(f6, HotkeysHudElement.rowWidth(v6hnga3, v6hnga4, avar026.b, this.normalizeKey(avar026.c), f));
            }
        }
        int n3 = n = bl ? 1 : activeCount;
        if (bl && this.O.isEmpty()) {
            n = 0;
        }
        float f8 = 0.0f;
        if (n > 0) {
            f8 = 0.0f + (float)n * f4;
            f8 += (float)Math.max(0, n - 1) * f5;
        }
        float f9 = f6 + f2 * 2.0f;
        float f10 = f3 + 6.0f * f + f8 + 10.0f * f;
        if (this.U) {
            if (Math.abs(this.Q.i() - (double)f9) > 0.5) {
                this.Q.a((double)f9, 0.2, Easings.h);
            }
            if (Math.abs(this.P.i() - (double)f10) > 0.5) {
                this.P.a((double)f10, 0.2, Easings.h);
            }
        } else {
            this.Q.d(f9);
            this.P.d(f10);
            this.U = true;
        }
        this.Q.a();
        this.P.a();
        this.d = (float)this.Q.j();
        this.e = (float)this.P.j();
    }

    private Color a(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)((float)color.getAlpha() * f))));
    }

    private boolean v() {
        return BooleanCoercion.from(this.a.currentScreen instanceof ChatScreen && this.x().isEmpty() ? 1 : 0);
    }

    private void w() {
        if (this.W) {
            return;
        }
        this.f().a(ModuleManager.HOTKEYS_HUD);
        this.W = true;
    }

    private void u() {
        this.O.add(new HotkeyTextEntry("Trails", "G"));
        this.O.add(new HotkeyTextEntry("Fast Swap", "O"));
        this.O.add(new HotkeyTextEntry("PVP Safe", "B"));
    }

    private String normalizeKey(String string) {
        return string == null ? "" : string.trim();
    }

    private static /* synthetic */ String $sf$0(String string, String string2) {
        return string + string2;
    }

    private static /* synthetic */ String $sf$4(int n) {
        return "Key" + n;
    }

    private static /* synthetic */ String $sf$3(int n) {
        return "Num" + n;
    }

    private static /* synthetic */ String $sf$1(int n) {
        return "M" + n;
    }

    private static /* synthetic */ String $sf$2(int n) {
        return "F" + n;
    }
}
