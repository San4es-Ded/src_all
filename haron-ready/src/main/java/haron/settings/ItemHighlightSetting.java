package haron.settings;

import haron.settings.Setting;
import java.awt.Color;
import net.minecraft.item.Item;

public class ItemHighlightSetting
extends Setting<Boolean> {
    private final Item item;
    private Color color;

    public int rgb() {
        return this.color.getRGB();
    }

    public ItemHighlightSetting(String string, String string2, Item item, boolean bl, Color color) {
        super(string, string2, bl);
        this.item = item;
        this.color = color;
    }

    public ItemHighlightSetting(String string, Item item, Color color) {
        this(string, "", item, false, color);
    }

    public ItemHighlightSetting(String string, Item item, boolean bl, Color color) {
        this(string, "", item, bl, color);
    }

    public boolean get() {
        return (Boolean)this.k();
    }

    public int e() {
        return this.rgb();
    }

    public void b() {
        this.toggle();
    }

    public Item c() {
        return this.item();
    }

    public Color d() {
        return this.color();
    }

    public void a(Color color) {
        this.setColor(color);
    }

    public void a(boolean bl) {
        int n = 125;
        this.set(bl);
    }

    public boolean a() {
        return this.get();
    }

    public void set(boolean bl) {
        super.a(bl);
    }

    public Color color() {
        return this.color;
    }

    public Item item() {
        return this.item;
    }

    public void setColor(Color color) {
        int n = 176;
        this.color = color;
    }

    public void toggle() {
        this.set(!this.get());
    }
}
