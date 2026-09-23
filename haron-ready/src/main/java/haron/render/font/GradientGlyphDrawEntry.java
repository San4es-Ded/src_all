package haron.render.font;

import haron.render.font.FontGlyph;
import java.util.Objects;

public final class GradientGlyphDrawEntry {
    private final float x;
    private final float y;
    private final int topColor;
    private final int bottomColor;
    private final FontGlyph glyph;
    private final float totalHeight;

    public FontGlyph glyph() {
        return this.glyph;
    }

    public int topColor() {
        return this.topColor;
    }

    public int bottomColor() {
        return this.bottomColor;
    }

    public float totalHeight() {
        return this.totalHeight;
    }

    public GradientGlyphDrawEntry(float f, float f2, int n, int n2, FontGlyph wvyq9n2, float f3) {
        this.x = f;
        this.y = f2;
        this.topColor = n;
        this.bottomColor = n2;
        this.glyph = wvyq9n2;
        this.totalHeight = f3;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GradientGlyphDrawEntry)) {
            return false;
        }
        GradientGlyphDrawEntry sb7dbt2 = (GradientGlyphDrawEntry)object;
        return Float.compare(sb7dbt2.x, this.x) == 0 && Float.compare(sb7dbt2.y, this.y) == 0 && this.topColor == sb7dbt2.topColor && this.bottomColor == sb7dbt2.bottomColor && Float.compare(sb7dbt2.totalHeight, this.totalHeight) == 0 && Objects.equals(this.glyph, sb7dbt2.glyph);
    }

    public String toString() {
        return GradientGlyphDrawEntry.$sf$0(this.x, this.y, this.topColor, this.bottomColor, String.valueOf(this.glyph), this.totalHeight);
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y), this.topColor, this.bottomColor, this.glyph, Float.valueOf(this.totalHeight));
    }

    public FontGlyph e() {
        int n = 225;
        return this.glyph;
    }

    public float b() {
        return this.y;
    }

    public float x() {
        int n = 210;
        return this.x;
    }

    public int c() {
        return this.topColor;
    }

    public float f() {
        return this.totalHeight;
    }

    public int d() {
        int n = 681;
        return this.bottomColor;
    }

    public float a() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    private static /* synthetic */ String $sf$0(float f, float f2, int n, int n2, String string, float f3) {
        return "GradientGlyphDrawEntry{x=" + f + ", y=" + f2 + ", topColor=" + n + ", bottomColor=" + n2 + ", glyph=" + string + ", totalHeight=" + f3 + "}";
    }
}

