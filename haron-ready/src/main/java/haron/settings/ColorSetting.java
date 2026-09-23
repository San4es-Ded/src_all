package haron.settings;

import haron.settings.Setting;
import java.awt.Color;
import java.util.function.Supplier;

public class ColorSetting
extends Setting<Color> {
    private float hue;
    private float saturation;
    private float brightness;

    public float saturation() {
        return this.saturation;
    }

    public Color getColor() {
        return Color.getHSBColor(this.hue, this.saturation, this.brightness);
    }

    public float hue() {
        int n = 533;
        return this.hue;
    }

    public float brightness() {
        return this.brightness;
    }

    private float clamp01(float f) {
        return Math.max(0.0f, Math.min(1.0f, f));
    }

    public int rgb() {
        return this.getColor().getRGB();
    }

    public void setHue(float f) {
        this.setHsb(f, this.saturation, this.brightness);
    }

    private void updateHsb(Color color) {
        if (color == null) {
            color = Color.WHITE;
        }
        float[] fArray = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        this.hue = this.clamp01(fArray[0]);
        this.saturation = this.clamp01(fArray[1]);
        this.brightness = this.clamp01(fArray[2]);
    }

    @Override
    public Setting<Color> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<Color> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public void setBrightness(float f) {
        this.setHsb(this.hue, this.saturation, f);
    }

    public void setSaturation(float f) {
        this.setHsb(this.hue, f, this.brightness);
    }

    public void setHsb(float f, float f2, float f3) {
        this.hue = this.clamp01(f);
        this.saturation = this.clamp01(f2);
        this.brightness = this.clamp01(f3);
        super.a(this.getColor());
    }

    public ColorSetting(String string, String string2, Color color) {
        super(string, string2, color == null ? Color.WHITE : color);
        this.updateHsb(color == null ? Color.WHITE : color);
    }

    public ColorSetting(String string, Color color) {
        this(string, "", color);
    }

    public ColorSetting(String string, int n, int n2, int n3) {
        this(string, "", new Color(n, n2, n3));
    }

    public float e() {
        return this.brightness();
    }

    public int b() {
        return this.rgb();
    }

    public void b(float f) {
        this.setSaturation(f);
    }

    public void c(float f) {
        int n = 616;
        this.setBrightness(f);
    }

    public float c() {
        int n = 218;
        return this.hue();
    }

    public float d() {
        return this.saturation();
    }

    public void a(float f, float f2, float f3) {
        this.setHsb(f, f2, f3);
    }

    public ColorSetting a(Supplier<Boolean> supplier) {
        return (ColorSetting)this.visibleWhen(supplier);
    }

    public Color a() {
        return this.getColor();
    }

    public void a(Color color) {
        this.setColor(color);
    }

    public void a(float f) {
        this.setHue(f);
    }

    public void setColor(Color color) {
        if (color == null) {
            color = Color.WHITE;
        }
        this.updateHsb(color);
        super.a(color);
    }
}
