package su.sacura.util.impl.render.providers;

import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.math.MathHelper;
import su.sacura.display.csgui.helper.ThemeStorage;

public final class ColorProvider {
    private static final ConcurrentHashMap<ColorKey, CacheEntry> colorCache = new ConcurrentHashMap();
    private static final long CACHE_EXPIRATION_TIME = 60000L;
    private static final DelayQueue<CacheEntry> cleanupQueue = new DelayQueue();

    public static int pack(int red, int green, int blue, int alpha) {
        return (alpha & 0xFF) << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF) << 0;
    }

    public static int fade(int index) {
        return ColorProvider.fade(8, index, ColorProvider.getColorStyle(0.0f), -1);
    }

    public static float redf(int c) {
        return (float)ColorProvider.red(c) / 255.0f;
    }

    public static float greenf(int c) {
        return (float)ColorProvider.green(c) / 255.0f;
    }

    public static float bluef(int c) {
        return (float)ColorProvider.blue(c) / 255.0f;
    }

    public static float alphaf(int c) {
        return (float)ColorProvider.alpha(c) / 255.0f;
    }

    public static int setAlpha(int color, int alpha) {
        return color & 0xFFFFFF | alpha << 24;
    }

    public static int fade(int speed, int index, int first, int second) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = angle >= 180 ? 360 - angle : angle;
        return ColorProvider.overCol(first, second, (float)angle / 180.0f);
    }

    public static int overCol(int color1, int color2, float percent01) {
        float percent = MathHelper.clamp((float)percent01, (float)0.0f, (float)1.0f);
        return ColorProvider.getColor(MathHelper.lerp((float)percent, (int)ColorProvider.red(color1), (int)ColorProvider.red(color2)), MathHelper.lerp((float)percent, (int)ColorProvider.green(color1), (int)ColorProvider.green(color2)), MathHelper.lerp((float)percent, (int)ColorProvider.blue(color1), (int)ColorProvider.blue(color2)), MathHelper.lerp((float)percent, (int)ColorProvider.alpha(color1), (int)ColorProvider.alpha(color2)));
    }

    public static int blendColors(int color1, int color2, float ratio) {
        int r1 = color1 >> 16 & 0xFF;
        int g1 = color1 >> 8 & 0xFF;
        int b1 = color1 & 0xFF;
        int r2 = color2 >> 16 & 0xFF;
        int g2 = color2 >> 8 & 0xFF;
        int b2 = color2 & 0xFF;
        int r = (int)((float)r1 * (1.0f - ratio) + (float)r2 * ratio);
        int g = (int)((float)g1 * (1.0f - ratio) + (float)g2 * ratio);
        int b = (int)((float)b1 * (1.0f - ratio) + (float)b2 * ratio);
        return r << 16 | g << 8 | b;
    }

    public static int red(int c) {
        return c >> 16 & 0xFF;
    }

    public static int green(int c) {
        return c >> 8 & 0xFF;
    }

    public static int blue(int c) {
        return c & 0xFF;
    }

    public static int alpha(int c) {
        return c >> 24 & 0xFF;
    }

    private static int computeColor(int red, int green, int blue, int alpha) {
        return MathHelper.clamp((int)alpha, (int)0, (int)255) << 24 | MathHelper.clamp((int)red, (int)0, (int)255) << 16 | MathHelper.clamp((int)green, (int)0, (int)255) << 8 | MathHelper.clamp((int)blue, (int)0, (int)255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        ColorKey key = new ColorKey(red, green, blue, alpha);
        CacheEntry cacheEntry = colorCache.computeIfAbsent(key, k -> {
            CacheEntry newEntry = new CacheEntry((ColorKey)k, ColorProvider.computeColor(red, green, blue, alpha), 60000L);
            cleanupQueue.offer(newEntry);
            return newEntry;
        });
        return cacheEntry.getColor();
    }

    public static int multAlpha(int color, float percent01) {
        return ColorProvider.getColor(ColorProvider.red(color), ColorProvider.green(color), ColorProvider.blue(color), Math.round((float)ColorProvider.alpha(color) * percent01));
    }

    public static int injectAlpha(int color, int alpha) {
        alpha = MathHelper.clamp((int)alpha, (int)0, (int)255);
        return alpha << 24 | color & 0xFFFFFF;
    }

    public static int[] unpack(int color) {
        return new int[]{color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF};
    }

    public static float[] normalize(Color color) {
        return new float[]{(float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f};
    }

    public static float[] normalize(int color) {
        int[] components = ColorProvider.unpack(color);
        return new float[]{(float)components[0] / 255.0f, (float)components[1] / 255.0f, (float)components[2] / 255.0f, (float)components[3] / 255.0f};
    }

    public static int rgb(int r, int g, int b) {
        return ColorProvider.argb(255, r, g, b);
    }

    public static int rgba(int r, int g, int b, int a) {
        return ColorProvider.argb(a, r, g, b);
    }

    private static int argb(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int interpolate(int color1, int color2, float factor) {
        int[] c1 = ColorProvider.unpack(color1);
        int[] c2 = ColorProvider.unpack(color2);
        return ColorProvider.pack((int)((float)c1[0] + (float)(c2[0] - c1[0]) * factor), (int)((float)c1[1] + (float)(c2[1] - c1[1]) * factor), (int)((float)c1[2] + (float)(c2[2] - c1[2]) * factor), (int)((float)c1[3] + (float)(c2[3] - c1[3]) * factor));
    }

    public static int wave(int color1, int color2, double offset) {
        double time = (double)System.currentTimeMillis() / 1000.0 * 3.0;
        double waveValue = Math.sin(time - offset);
        float factor = (float)((waveValue + 1.0) / 2.0);
        return ColorProvider.interpolate(color1, color2, factor);
    }

    public static int applyOpacity(int color, float opacity) {
        opacity = MathHelper.clamp((float)opacity, (float)0.0f, (float)1.0f);
        int alpha = (int)((float)(color >>> 24 & 0xFF) * opacity);
        return alpha << 24 | color & 0xFFFFFF;
    }

    public static int getColorStyle(float index) {
        return ThemeStorage.accentColor.getRGB();
    }

    private static class ColorKey {
        final int red;
        final int green;
        final int blue;
        final int alpha;

        public int getRed() {
            return this.red;
        }

        public int getGreen() {
            return this.green;
        }

        public int getBlue() {
            return this.blue;
        }

        public int getAlpha() {
            return this.alpha;
        }

        public ColorKey(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ColorKey)) {
                return false;
            }
            ColorKey other = (ColorKey)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (this.getRed() != other.getRed()) {
                return false;
            }
            if (this.getGreen() != other.getGreen()) {
                return false;
            }
            if (this.getBlue() != other.getBlue()) {
                return false;
            }
            return this.getAlpha() == other.getAlpha();
        }

        protected boolean canEqual(Object other) {
            return other instanceof ColorKey;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getRed();
            result = result * 59 + this.getGreen();
            result = result * 59 + this.getBlue();
            result = result * 59 + this.getAlpha();
            return result;
        }
    }

    private static class CacheEntry
    implements Delayed {
        private final ColorKey key;
        private final int color;
        private final long expirationTime;

        CacheEntry(ColorKey key, int color, long ttl) {
            this.key = key;
            this.color = color;
            this.expirationTime = System.currentTimeMillis() + ttl;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long delay = this.expirationTime - System.currentTimeMillis();
            return unit.convert(delay, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            if (other instanceof CacheEntry) {
                return Long.compare(this.expirationTime, ((CacheEntry)other).expirationTime);
            }
            return 0;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > this.expirationTime;
        }

        public ColorKey getKey() {
            return this.key;
        }

        public int getColor() {
            return this.color;
        }

        public long getExpirationTime() {
            return this.expirationTime;
        }
    }
}
