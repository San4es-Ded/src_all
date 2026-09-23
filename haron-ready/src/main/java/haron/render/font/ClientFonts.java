package haron.render.font;

import haron.render.font.FontRenderer;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class ClientFonts {
    public static final FontRenderer[] REGULAR;
    public static final FontRenderer[] MEDIUM;
    public static final FontRenderer[] ICONS;
    public static final FontRenderer[] PULSE_ICONS;
    public static final FontRenderer[] SMALL_ICONS;
    public static final FontRenderer[] a;
    public static final FontRenderer[] b;
    public static final FontRenderer[] c;
    public static final FontRenderer[] d;
    public static final FontRenderer[] e;
    static final boolean $assertionsDisabled;

    static {
        int n;
        $assertionsDisabled = !ClientFonts.class.desiredAssertionStatus();
        REGULAR = new FontRenderer[65];
        for (n = 6; n < 65; ++n) {
            ClientFonts.REGULAR[n] = ClientFonts.load(n, "sfprodisplayregular.ttf");
        }
        MEDIUM = new FontRenderer[65];
        for (n = 6; n < 65; ++n) {
            ClientFonts.MEDIUM[n] = ClientFonts.load(n, "sfprodisplay-medium-test.ttf");
        }
        ICONS = new FontRenderer[65];
        for (n = 6; n < 65; ++n) {
            ClientFonts.ICONS[n] = ClientFonts.load(n, "icons.ttf");
        }
        PULSE_ICONS = new FontRenderer[265];
        for (n = 6; n < 265; ++n) {
            ClientFonts.PULSE_ICONS[n] = ClientFonts.load(n, "pulseicon.ttf");
        }
        SMALL_ICONS = new FontRenderer[65];
        for (n = 6; n < 65; ++n) {
            ClientFonts.SMALL_ICONS[n] = ClientFonts.load(n, "pulse_icons.ttf");
        }
        a = REGULAR;
        b = MEDIUM;
        c = ICONS;
        d = PULSE_ICONS;
        e = SMALL_ICONS;
    }

    private static FontRenderer load(float f, String string) {
        try {
            InputStream inputStream = ClientFonts.class.getResourceAsStream("/assets/haron/font/".concat(string));
            try {
                if (!$assertionsDisabled && inputStream == null) {
                    throw new AssertionError();
                }
                FontRenderer v6hnga2 = new FontRenderer(Font.createFont(0, inputStream).deriveFont(0, f / 2.0f), f / 2.0f);
                if (inputStream != null) {
                    inputStream.close();
                }
                return v6hnga2;
            }
            catch (Throwable throwable) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                }
                throw throwable;
            }
        }
        catch (FontFormatException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

