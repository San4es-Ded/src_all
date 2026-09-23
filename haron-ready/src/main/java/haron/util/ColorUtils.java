package haron.util;

import java.awt.Color;

public final class ColorUtils {
    public static int ensureOpaque(int color) {
        return a(color);
    }

    public static int withAlpha(int color, int alpha) {
        return a(color, alpha);
    }

    public static Color interpolate(Color from, Color to, float progress) {
        return a(from, to, progress);
    }
    public static int a;

    private ColorUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int a(int n, int n2, int n3) {
        int n4 = (~n | 0xFF) - ~n << 16;
        int n5 = (0xFF000000 & ~n4) + n4;
        int n6 = (~n2 | 0xFF) - ~n2 << 8;
        int n7 = (n5 & ~n6) + n6;
        int n8 = (~n3 | 0xFF) - ~n3;
        return (n7 & ~n8) + n8;
    }

    public static Color a(Color color, Color color2, float f) {
        float f2 = color.getRed();
        int n = color2.getRed();
        int n2 = color.getRed();
        int n3 = (int)(f2 + (float)(2 * (n & ~n2) - (n ^ n2)) * f);
        int n4 = (int)((float)color.getGreen() + (float)(color2.getGreen() + ~color.getGreen() + 1) * f);
        float f3 = color.getBlue();
        int n5 = color2.getBlue();
        int n6 = color.getBlue();
        return new Color(Math.max(0, Math.min(255, n3)), Math.max(0, Math.min(255, n4)), Math.max(0, Math.min(255, (int)(f3 + (float)((n5 ^ n6) - 2 * (~n5 & n6)) * f))), Math.max(0, Math.min(255, (int)((float)color.getAlpha() + (float)(color2.getAlpha() - color.getAlpha()) * f))));
    }

    public static int a(int n) {
        int n2 = n >> 24;
        return (~n2 | 0xFF) - ~n2 != 0 ? n : (n & 0xFFFFFF) - 0x1000000;
    }

    public static int a(int n, int n2) {
        int n3 = (~n | 0xFFFFFF) - ~n;
        int n4 = Math.max(0, Math.min(255, n2)) << 24;
        return (n3 & ~n4) + n4;
    }

    public static Color a(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, n)));
    }

    public static int a(int n, int n2, int n3, int n4) {
        int n5 = (~n | 0xFF) - ~n << 16;
        int n6 = ((~n4 | 0xFF) - ~n4 << 24 & ~n5) + n5;
        int n7 = (~n2 | 0xFF) - ~n2 << 8;
        int n8 = (n6 & ~n7) + n7;
        int n9 = (~n3 | 0xFF) - ~n3;
        return (n8 & ~n9) + n9;
    }
}
