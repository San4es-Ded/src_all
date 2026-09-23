package haron.theme;

import java.awt.Color;

public final class pryrvd {
    public static Color ACCENT;
    public static Color ACCENT_SOFT;
    public static Color ACCENT_DARK;
    public static Color PANEL_BG_TOP;
    public static Color PANEL_BG_BOT;
    public static Color CELL_BG_TOP;
    public static Color CELL_BG_BOT;
    public static Color PANEL_BORDER_TOP;
    public static Color PANEL_BORDER_BOT;
    public static Color ROW_BG_TOP;
    public static Color ROW_BG_BOT;
    public static Color ROW_HOVER_TOP;
    public static Color ROW_HOVER_BOT;
    public static Color ROW_ACCENT;
    public static Color ROW_TEXT_INACTIVE;
    public static Color WIDGET_BG_TOP;
    public static Color WIDGET_BG_BOT;
    public static Color WIDGET_BORDER_TOP;
    public static Color WIDGET_BORDER_BOT;
    public static Color LOGO_TEXT;
    public static Color SCROLLBAR_THUMB;
    public static Color b;
    public static Color e;
    public static Color f;
    public static Color m;
    public static Color n;
    public static Color q;
    public static Color r;
    public static Color w;
    public static Color C;
    public static Color D;
    public static Color I;
    public static Color K;
    public static Color aa;
    public static int ae;
    public static boolean af;
    public static Color a;
    public static Color c;
    public static Color d;
    public static Color g;
    public static Color h;
    public static Color i;
    public static Color j;
    public static Color k;
    public static Color l;
    public static Color o;
    public static Color p;
    public static Color s;
    public static Color t;
    public static Color u;
    public static Color v;
    public static Color x;
    public static Color y;
    public static Color z;
    public static Color A;
    public static Color B;
    public static Color E;
    public static Color F;
    public static Color G;
    public static Color H;
    public static Color J;
    public static Color L;
    public static Color M;
    public static Color N;
    public static Color O;
    public static Color P;
    public static Color Q;
    public static Color R;
    public static Color S;
    public static Color T;
    public static Color U;
    public static Color V;
    public static Color W;
    public static Color X;
    public static Color Y;
    public static Color Z;
    public static Color FriendCard;
    public static Color FriendsPanel;
    public static Color ad;

    private static Color withAlpha(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
    }

    private pryrvd() {
    }

    static {
        a = new Color(223, 223, 243);
        c = new Color(212, 132, 42);
        d = new Color(110, 85, 50);
        g = new Color(22, 22, 30);
        h = new Color(18, 18, 24);
        i = new Color(30, 25, 20);
        j = new Color(25, 22, 18);
        k = new Color(25, 22, 18);
        l = new Color(20, 18, 14);
        o = new Color(21, 21, 28);
        p = new Color(30, 30, 40);
        s = new Color(50, 40, 28);
        t = new Color(28, 24, 18);
        u = new Color(35, 30, 22);
        v = new Color(22, 20, 16);
        x = new Color(18, 18, 24);
        y = new Color(212, 132, 42);
        z = new Color(174, 100, 20);
        A = new Color(212, 132, 42, 100);
        B = new Color(240, 160, 50);
        E = new Color(87, 55, 15);
        F = new Color(14, 14, 19);
        G = new Color(42, 38, 30);
        H = new Color(0, 0, 0, 55);
        J = new Color(22, 22, 28);
        L = new Color(40, 20, 20);
        M = new Color(30, 15, 15);
        N = new Color(130, 110, 80);
        O = new Color(96, 96, 96);
        P = new Color(85, 70, 45);
        Q = new Color(240, 160, 50);
        R = new Color(180, 110, 30);
        S = new Color(15, 15, 21);
        T = new Color(18, 18, 25);
        U = new Color(170, 150, 120);
        V = new Color(24, 24, 32);
        W = new Color(220, 53, 69);
        X = new Color(40, 167, 69);
        Y = new Color(255, 180, 50);
        Z = new Color(255, 80, 80);
        FriendCard = new Color(0, 0, 0);
        FriendsPanel = new Color(30, 30, 40);
        ad = new Color(0, 0, 0, 125);
        pryrvd.update(new Color(255, 155, 35));
    }

    public static void update(Color color) {
        if (color == null) {
            color = new Color(255, 155, 35);
        }
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float f = fArray[0];
        float f2 = fArray[1];
        float f3 = Math.min(1.0f, f2 * 0.67f);
        ACCENT = color;
        ACCENT_SOFT = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.82f), 0.75f);
        ACCENT_DARK = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.84f), 0.59f);
        PANEL_BG_TOP = Color.getHSBColor(f, f3, 0.15f);
        PANEL_BG_BOT = Color.getHSBColor(f, f3, 0.19f);
        PANEL_BORDER_TOP = Color.getHSBColor(f, f3, 0.28f);
        PANEL_BORDER_BOT = PANEL_BG_BOT;
        CELL_BG_TOP = Color.getHSBColor(f, f3, 0.2f);
        CELL_BG_BOT = Color.getHSBColor(f, f3, 0.24f);
        ROW_BG_TOP = CELL_BG_TOP;
        ROW_BG_BOT = CELL_BG_BOT;
        ROW_HOVER_TOP = new Color(color.getRed(), color.getGreen(), color.getBlue(), 30);
        ROW_HOVER_BOT = new Color(color.getRed(), color.getGreen(), color.getBlue(), 45);
        ROW_ACCENT = color;
        ROW_TEXT_INACTIVE = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.12f), 0.86f);
        WIDGET_BG_TOP = Color.getHSBColor(f, f3, 0.16f);
        WIDGET_BG_BOT = Color.getHSBColor(f, f3, 0.2f);
        WIDGET_BORDER_TOP = Color.getHSBColor(f, f3, 0.24f);
        WIDGET_BORDER_BOT = Color.getHSBColor(f, f3, 0.15f);
        LOGO_TEXT = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.3f), 1.0f);
        SCROLLBAR_THUMB = Color.getHSBColor(f, f3 * 0.5f, 0.3f);
        y = C = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.93f), 0.83f);
        A = pryrvd.withAlpha(C, 100);
        D = Color.getHSBColor(f, Math.min(1.0f, f2), 0.71f);
        c = C;
        z = Color.getHSBColor(f, Math.min(1.0f, f2 * 1.1f), 0.68f);
        Q = B = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.85f), 0.94f);
        Y = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.85f), 1.0f);
        R = Color.getHSBColor(f, Math.min(1.0f, f2), 0.71f);
        E = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.8f), 0.34f);
        N = Color.getHSBColor(f, f3 * 0.5f, 0.51f);
        P = Color.getHSBColor(f, f3 * 0.5f, 0.33f);
        b = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.5f), 0.35f);
        d = Color.getHSBColor(f, Math.min(1.0f, f2 * 0.5f), 0.43f);
        float f4 = f3 * 0.5f;
        pryrvd.f = Color.getHSBColor(f, f4, 0.067f);
        e = g = Color.getHSBColor(f, f4, 0.086f);
        x = h = Color.getHSBColor(f, f4, 0.071f);
        i = Color.getHSBColor(f, f4, 0.118f);
        k = j = Color.getHSBColor(f, f4, 0.098f);
        l = Color.getHSBColor(f, f4, 0.078f);
        o = Color.getHSBColor(f, f4, 0.082f);
        p = Color.getHSBColor(f, f4, 0.118f);
        s = Color.getHSBColor(f, f3, 0.2f);
        t = Color.getHSBColor(f, f4, 0.11f);
        u = Color.getHSBColor(f, f4, 0.137f);
        v = Color.getHSBColor(f, f4, 0.086f);
        m = Color.getHSBColor(f, f4, 0.122f);
        n = Color.getHSBColor(f, f4, 0.098f);
        q = Color.getHSBColor(f, f4, 0.106f);
        r = Color.getHSBColor(f, f3, 0.157f);
        I = n;
        K = Color.getHSBColor(f, f4, 0.078f);
        J = Color.getHSBColor(f, f4, 0.086f);
        G = Color.getHSBColor(f, f3 * 0.4f, 0.165f);
        V = Color.getHSBColor(f, f4, 0.094f);
        F = Color.getHSBColor(f, f4, 0.055f);
        S = Color.getHSBColor(f, f4, 0.059f);
        T = Color.getHSBColor(f, f4, 0.071f);
        FriendsPanel = p;
        FriendCard = Color.BLACK;
        ad = new Color(0, 0, 0, 125);
        a = new Color(223, 223, 243);
        aa = Color.WHITE;
        w = new Color(255, 255, 255, 13);
        H = new Color(0, 0, 0, 55);
    }

    public static Color b(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int)((float)color.getAlpha() * f))));
    }

    public static Color b(Color color, int n) {
        int n2 = color.getRed();
        int n3 = Math.min(255, (n2 ^ n) + 2 * (n2 & n));
        int n4 = color.getGreen();
        return new Color(n3, Math.min(255, 2 * (n4 | n) - (n4 ^ n)), Math.min(255, color.getBlue() - ~n - 1), color.getAlpha());
    }

    public static Color c(Color color, int n) {
        int n2 = Math.max(0, color.getRed() - n);
        int n3 = color.getGreen();
        int n4 = Math.max(0, (n3 ^ n) - 2 * (~n3 & n));
        int n5 = color.getBlue();
        return new Color(n2, n4, Math.max(0, (n5 ^ n) - 2 * (~n5 & n)), color.getAlpha());
    }

    public static Color a(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, n)));
    }

    public static Color a(Color color, float f) {
        return pryrvd.a(color, (int)(255.0f * f));
    }

}

