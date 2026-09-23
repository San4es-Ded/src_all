package rockstar.client.ui;


import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.util.Arrays;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.internal.script.ThemeVariant;
import rockstar.client.internal.core.ThemeModeState;
import rockstar.client.internal.script.ThemeStyleModel;
import rockstar.client.RockstarClient;
import rockstar.client.internal.script.AnimatedColor;

public final class ThemeColors {
    public static final ColorRGBA internalField0777 = new ColorRGBA(255.0f, 0.0f, 0.0f);
    public static final ColorRGBA internalField0776 = new ColorRGBA(0.0f, 255.0f, 0.0f);
    public static final ColorRGBA internalField1311 = new ColorRGBA(0.0f, 0.0f, 255.0f);
    public static final ColorRGBA internalField1312 = new ColorRGBA(255.0f, 255.0f, 255.0f);
    public static final ColorRGBA internalField1309 = new ColorRGBA(0.0f, 0.0f, 0.0f);
    public static final int internalField0227 = 2;
    private static final ColorRGBA internalField1615 = new ColorRGBA(144.0f, 107.0f, 255.0f);
    private static final ColorRGBA internalField1609 = new ColorRGBA(255.0f, 255.0f, 255.0f);
    private static final ColorRGBA internalField1813 = new ColorRGBA(16.0f, 14.0f, 20.0f);
    private static final ColorRGBA[] internalField0748 = new ColorRGBA[]{internalField1615, new ColorRGBA(24.0f, 21.0f, 29.0f, 229.5f), new ColorRGBA(24.0f, 21.0f, 29.0f, 102.0f), new ColorRGBA(61.0f, 54.0f, 71.0f, 63.75f), new ColorRGBA(77.0f, 0.0f, 255.0f), new ColorRGBA(255.0f, 255.0f, 255.0f), new ColorRGBA(26.0f, 23.0f, 31.0f), new ColorRGBA(5.0f, 4.0f, 7.0f)};
    public static final ColorRGBA internalField1310 = new InternalType0246(0);
    public static final ColorRGBA internalField1612 = new InternalType0246(1);
    public static final ColorRGBA internalField1614 = new InternalType0246(2);
    public static final ColorRGBA internalField1616 = new InternalType0246(3);
    public static final ColorRGBA internalField1610 = new InternalType0246(4);
    public static final ColorRGBA internalField1613 = new InternalType0246(5);
    public static final ColorRGBA internalField1611 = new InternalType0246(8);
    private static final ThemeStyleModel internalField0397 = new ThemeStyleModel(internalField1615, internalField0748[1], internalField0748[6], internalField0748[5], internalField0748[3], internalField0748[7], internalField0748[5], internalField0748[5], 7.0f, 0.5f, 0.8f, 0.2f, 25.0f, 0.08f, 2.0f, 0.0f, 1.0f, 1.0f);
    private static final AnimatedColor internalField0814 = new AnimatedColor(500L, internalField1615);
    private static final float[] internalField0615 = ThemeColors.internalMethod06180(internalField1615);
    private static final ColorRGBA[] internalField0749 = ThemeColors.internalMethod03171();
    private static final ColorRGBA[] internalField1297 = ThemeColors.internalMethod03171();
    private static ColorRGBA internalField1812 = internalField1615;
    private static long internalField0229;
    private static int internalField1466;

    public static ColorRGBA internalMethod03506() {
        return internalField1812;
    }

    public static void internalMethod01095(ColorRGBA colorRGBA) {
        if (colorRGBA != null) {
            internalField1812 = colorRGBA.withAlpha(255.0f);
        }
    }

    public static void internalMethod00247(ThemeStyleModel typedValue187) {
        if (typedValue187 == null) {
            return;
        }
        if (typedValue187.internalMethod01286() != null && typedValue187.internalMethod01286().getAlpha() > 0.0f) {
            ThemeColors.internalMethod01095(typedValue187.internalMethod01286());
        }
        internalField0814.internalMethod03487(internalField1812);
        internalField1466 = -1;
        internalField0397.internalMethod00996(typedValue187.internalMethod01359()).internalMethod05959(typedValue187.internalMethod01360()).internalMethod08666(typedValue187.internalMethod08704()).internalMethod08066(typedValue187.internalMethod08705()).internalMethod08720(typedValue187.internalMethod08718()).internalMethod08106(typedValue187.internalMethod08719()).internalMethod09478(typedValue187.internalMethod09714()).internalMethod09735(typedValue187.internalMethod09715()).internalMethod09488(typedValue187.internalMethod09721()).internalMethod09157(typedValue187.internalMethod09722());
    }

    public static ThemeStyleModel internalMethod02435() {
        ThemeColors.internalMethod06104();
        return internalField0397;
    }

    public static ColorRGBA internalMethod02531() {
        return ThemeColors.internalMethod02560(0);
    }

    public static ColorRGBA internalMethod07738() {
        return ThemeColors.internalMethod02560(1);
    }

    public static ColorRGBA internalMethod08573() {
        return ThemeColors.internalMethod02560(6);
    }

    public static ColorRGBA internalMethod08459() {
        return ThemeColors.internalMethod02560(5);
    }

    public static ColorRGBA internalMethod07681() {
        return ThemeColors.internalMethod02560(7);
    }

    public static ColorRGBA internalMethod09808() {
        return ThemeColors.internalMethod02560(8);
    }

    public static ColorRGBA internalMethod01303(ColorRGBA colorRGBA) {
        return ThemeColors.internalMethod03795(colorRGBA, internalField1609, internalField1813);
    }

    public static ColorRGBA internalMethod03795(ColorRGBA colorRGBA, ColorRGBA colorRGBA2, ColorRGBA colorRGBA3) {
        return colorRGBA2.mix(colorRGBA3, ThemeColors.internalMethod01094(colorRGBA));
    }

    public static float internalMethod01094(ColorRGBA colorRGBA) {
        return ThemeColors.internalMethod03256(colorRGBA, ThemeColors.internalMethod07738());
    }

    private static float internalMethod03256(ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        float f = Math.clamp(colorRGBA.getAlpha() / 255.0f, 0.0f, 1.0f);
        float f2 = colorRGBA.getRed() * f + colorRGBA2.getRed() * (1.0f - f);
        float f3 = colorRGBA.getGreen() * f + colorRGBA2.getGreen() * (1.0f - f);
        float f4 = colorRGBA.getBlue() * f + colorRGBA2.getBlue() * (1.0f - f);
        float f5 = (0.299f * f2 + 0.587f * f3 + 0.114f * f4) / 255.0f;
        return Math.clamp((f5 - 0.58f) / 0.120000005f, 0.0f, 1.0f);
    }

    public static ColorRGBA internalMethod09447() {
        return ColorRGBA.BLACK.withAlpha(255.0f * (ThemeColors.internalMethod02431() == ThemeVariant.internalField0395 ? 0.08f : 0.05f));
    }

    private static ThemeVariant internalMethod02431() {
        ThemeModeState typedValue186 = RockstarClient.getInstance().internalMethod04467();
        return typedValue186 == null ? ThemeVariant.internalField0395 : typedValue186.internalMethod05065();
    }

    static ColorRGBA internalMethod02560(int n) {
        ThemeColors.internalMethod06104();
        return internalField1297[n];
    }

    private static void internalMethod06104() {
        long l = System.currentTimeMillis();
        if (l == internalField0229) {
            return;
        }
        internalField0229 = l;
        internalField0814.internalMethod03893(internalField1812);
        ColorRGBA colorRGBA = internalField0814.internalMethod04159();
        ThemeVariant typedValue185 = ThemeColors.internalMethod02431();
        int n = 31 * colorRGBA.getRGB() + typedValue185.ordinal();
        if (n == internalField1466) {
            return;
        }
        internalField1466 = n;
        float[] fArray = ThemeColors.internalMethod06180(colorRGBA);
        float f = fArray[0] - internalField0615[0];
        float f2 = internalField0615[1] == 0.0f ? 1.0f : fArray[1] / internalField0615[1];
        ThemeColors.internalField0749[0] = colorRGBA;
        for (int i = 1; i < internalField0748.length; ++i) {
            ColorRGBA colorRGBA2 = internalField0748[i];
            float[] fArray2 = ThemeColors.internalMethod06180(colorRGBA2);
            if (fArray2[1] == 0.0f) {
                ThemeColors.internalField0749[i] = colorRGBA2;
                continue;
            }
            float f3 = fArray2[0] + f;
            ThemeColors.internalField0749[i] = ColorRGBA.fromHSB(f3 - (float)Math.floor(f3), Math.clamp(fArray2[1] * f2, 0.0f, 1.0f), fArray2[2]).withAlpha(colorRGBA2.getAlpha());
        }
        System.arraycopy(internalField0749, 0, internalField1297, 0, internalField0749.length);
        if (typedValue185 == ThemeVariant.internalField0394) {
            ThemeColors.internalField1297[1] = ThemeVariant.internalField0394.internalMethod05867().withAlpha(internalField0749[1].getAlpha());
            ThemeColors.internalField1297[2] = ThemeVariant.internalField0394.internalMethod05867().withAlpha(internalField0749[2].getAlpha());
            ThemeColors.internalField1297[3] = ThemeVariant.internalField0394.internalMethod07702().withAlpha(internalField0749[3].getAlpha());
            ThemeColors.internalField1297[5] = ThemeVariant.internalField0394.internalMethod01634().withAlpha(internalField0749[5].getAlpha());
            ThemeColors.internalField1297[6] = ThemeVariant.internalField0394.internalMethod08464().withAlpha(internalField0749[6].getAlpha());
            ThemeColors.internalField1297[7] = ThemeVariant.internalField0394.internalMethod08057().withAlpha(internalField0749[7].getAlpha());
        }
        ThemeColors.internalField1297[8] = internalField1609.mix(internalField1813, ThemeColors.internalMethod03256(internalField1297[0], internalField1297[1]));
        internalField0397.internalMethod03207(internalField1812).internalMethod06852(internalField1297[1]).internalMethod09079(internalField1297[6]).internalMethod08210(internalField1297[5]).internalMethod08771(internalField1297[3]).internalMethod07906(internalField1297[7]).internalMethod09885(internalField1297[5]).internalMethod09433(internalField1297[5]);
    }

    private static ColorRGBA[] internalMethod03171() {
        ColorRGBA[] colorRGBAArray = Arrays.copyOf(internalField0748, 9);
        colorRGBAArray[8] = internalField1609;
        return colorRGBAArray;
    }

    private static float[] internalMethod06180(ColorRGBA colorRGBA) {
        return Color.RGBtoHSB(Math.round(colorRGBA.getRed()), Math.round(colorRGBA.getGreen()), Math.round(colorRGBA.getBlue()), null);
    }

    @Generated
    private ThemeColors() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        internalField1466 = -1;
    }

    static final class InternalType0246
    extends ColorRGBA {
        private final int internalField0227;

        InternalType0246(int n) {
            super(0.0f, 0.0f, 0.0f);
            this.internalField0227 = n;
        }

        private ColorRGBA internalMethod07262() {
            return ThemeColors.internalMethod02560(this.internalField0227);
        }

        @Override
        public float getRed() {
            return this.internalMethod07262().getRed();
        }

        @Override
        public float getGreen() {
            return this.internalMethod07262().getGreen();
        }

        @Override
        public float getBlue() {
            return this.internalMethod07262().getBlue();
        }

        @Override
        public float getAlpha() {
            return this.internalMethod07262().getAlpha();
        }

        @Override
        public int getRGB() {
            return this.internalMethod07262().getRGB();
        }

        @Override
        public String toHex() {
            return this.internalMethod07262().toHex();
        }

        @Override
        public ColorRGBA withAlpha(float f) {
            return this.internalMethod07262().withAlpha(f);
        }

        @Override
        public ColorRGBA mulAlpha(float f) {
            return this.internalMethod07262().mulAlpha(f);
        }

        @Override
        public ColorRGBA mix(ColorRGBA colorRGBA, float f) {
            return this.internalMethod07262().mix(colorRGBA, f);
        }

        @Override
        public float getHue() {
            return this.internalMethod07262().getHue();
        }

        @Override
        public float getSaturation() {
            return this.internalMethod07262().getSaturation();
        }

        @Override
        public float getBrightness() {
            return this.internalMethod07262().getBrightness();
        }

        @Override
        public JsonObject toJson() {
            return this.internalMethod07262().toJson();
        }

        @Override
        public boolean equals(Object object) {
            return this.internalMethod07262().equals(object);
        }

        @Override
        public int hashCode() {
            return this.internalMethod07262().hashCode();
        }

        @Override
        public String toString() {
            return this.internalMethod07262().toString();
        }
    }
}

