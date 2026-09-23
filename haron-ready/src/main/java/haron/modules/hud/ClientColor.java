package haron.modules.hud;

import haron.gui.core.ClickGuiScreen;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.ColorSetting;
import haron.settings.ModeSetting;
import java.awt.Color;

@ModuleInfo(a="Client Color", b="Настройка основного цвета клиента", c=ModuleCategory.HUD)
public class ClientColor
extends HaronModule {
    private static ClientColor INSTANCE;
    private static final int GRADIENT_DURATION_MS = 4000;
    private static final int ASTOLFO_DURATION_MS = 6000;
    private static final int RAINBOW_DURATION_MS = 6000;
    private static final String MODE_STATIC;
    private static final String MODE_GRADIENT;
    private static final String MODE_ASTOLFO;
    private static final String MODE_RAINBOW;
    private static final String[] MODES;
    private static final Color DEFAULT_COLOR;
    private static final Color DEFAULT_GRADIENT_START;
    private static final Color DEFAULT_GRADIENT_END;
    private static final Color[] ASTOLFO_COLORS;
    private static final Color[] PRESET_COLORS;
    public final ModeSetting a = new ModeSetting("Режим", MODES, "Статичный");
    public final ColorSetting b = new ColorSetting("Цвет", DEFAULT_COLOR).a(() -> {
        return this.a.b("Статичный");
    });
    public final ColorSetting e = new ColorSetting("Цвет #1", DEFAULT_GRADIENT_START).a(() -> {
        return this.a.b("Градиент");
    });
    public final ColorSetting f = new ColorSetting("Цвет #2", DEFAULT_GRADIENT_END).a(() -> {
        int n = 688;
        return this.a.b("Градиент");
    });

    private static Color interpolate(Color color, Color color2, float f) {
        if (color == null) {
            color = Color.WHITE;
        }
        if (color2 == null) {
            color2 = Color.WHITE;
        }
        float f2 = Math.max(0.0f, Math.min(1.0f, f));
        return new Color(ClientColor.lerp(color.getRed(), color2.getRed(), f2), ClientColor.lerp(color.getGreen(), color2.getGreen(), f2), ClientColor.lerp(color.getBlue(), color2.getBlue(), f2), ClientColor.lerp(color.getAlpha(), color2.getAlpha(), f2));
    }

    public Color getColor() {
        return this.n();
    }

    private static float sineProgress(int n) {
        return (float)(0.5 * (1.0 + Math.sin((double)(System.currentTimeMillis() % (long)n) * (Math.PI * 2 / (double)n))));
    }

    private static float linearProgress(int n) {
        return (float)(System.currentTimeMillis() % (long)n) / (float)n;
    }

    public static Color defaultColor() {
        return DEFAULT_COLOR;
    }

    private static Color cycleColors(Color[] colorArray, int n) {
        if (colorArray == null || colorArray.length == 0) {
            return Color.WHITE;
        }
        float f = ClientColor.linearProgress(n) * (float)colorArray.length;
        int n2 = (int)f % colorArray.length;
        int n3 = (n2 + 1) % colorArray.length;
        return ClientColor.interpolate(colorArray[n2], colorArray[n3], f - (float)((int)f));
    }

    private Color animatedGradient() {
        return ClientColor.interpolate(this.e.a(), this.f.a(), ClientColor.sineProgress(4000));
    }

    public static void setStaticColor(Color color) {
        ClientColor byzlib2 = ClientColor.getInstance();
        if (byzlib2 == null || color == null) {
            return;
        }
        byzlib2.a.a("Статичный");
        byzlib2.b.setColor(color);
        ClickGuiScreen.updateColors();
    }

    public static Color currentColor() {
        ClientColor byzlib2 = ClientColor.getInstance();
        return byzlib2 != null ? byzlib2.n() : DEFAULT_COLOR;
    }

    public static Color[] presets() {
        return (Color[])PRESET_COLORS.clone();
    }

    private static int lerp(int n, int n2, float f) {
        return (int)((float)n + (float)(n2 - n) * f);
    }

    public int getRgb() {
        return this.o();
    }

    public ClientColor() {
        INSTANCE = this;
    }

    static {
        MODE_STATIC = "Статичный";
        MODE_GRADIENT = "Градиент";
        MODE_ASTOLFO = "Астольфо";
        MODE_RAINBOW = "Радуга";
        MODES = new String[]{"Статичный", "Градиент", "Астольфо", "Радуга"};
        DEFAULT_COLOR = new Color(255, 155, 35);
        DEFAULT_GRADIENT_START = new Color(255, 210, 80);
        DEFAULT_GRADIENT_END = new Color(255, 120, 25);
        ASTOLFO_COLORS = new Color[]{new Color(255, 121, 198), new Color(170, 70, 255), new Color(80, 181, 255)};
        PRESET_COLORS = new Color[]{new Color(150, 180, 255), new Color(255, 145, 190), new Color(190, 130, 255), new Color(255, 230, 105), new Color(255, 175, 75), new Color(255, 125, 55), new Color(100, 220, 255), new Color(100, 235, 190), new Color(120, 255, 145), new Color(255, 105, 115)};
    }

    public Color n() {
        String string = this.a.d();
        if ("Статичный".equals(string)) {
            return this.b.a();
        }
        if ("Градиент".equals(string)) {
            return this.animatedGradient();
        }
        if ("Астольфо".equals(string)) {
            return ClientColor.cycleColors(ASTOLFO_COLORS, 6000);
        }
        if ("Радуга".equals(string)) {
            return Color.getHSBColor(ClientColor.linearProgress(6000), 1.0f, 1.0f);
        }
        return this.b.a();
    }

    public static ClientColor getInstance() {
        return INSTANCE;
    }

    public int o() {
        return this.n().getRGB();
    }
}

