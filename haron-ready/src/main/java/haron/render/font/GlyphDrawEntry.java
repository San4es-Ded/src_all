package haron.render.font;

import haron.render.font.FontGlyph;
import java.util.Objects;

public final class GlyphDrawEntry {
    private final float x;
    private final float y;
    private final int color;
    private final FontGlyph glyph;

    public FontGlyph glyph() {
        return this.glyph;
    }

    public GlyphDrawEntry(float f, float f2, int n, FontGlyph wvyq9n2) {
        this.x = f;
        this.y = f2;
        this.color = n;
        this.glyph = wvyq9n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof GlyphDrawEntry)) {
            return false;
        }
        GlyphDrawEntry as21iw2 = (GlyphDrawEntry)object;
        return Float.compare(as21iw2.x, this.x) == 0 && Float.compare(as21iw2.y, this.y) == 0 && this.color == as21iw2.color && Objects.equals(this.glyph, as21iw2.glyph);
    }

    public String toString() {
        return GlyphDrawEntry.$sf$0(this.x, this.y, this.color, String.valueOf(this.glyph));
    }

    public int hashCode() {
        return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y), this.color, this.glyph);
    }

    public float e() {
        return this.x;
    }

    public float b() {
        return this.y;
    }

    public float x() {
        return this.x;
    }

    public int c() {
        return this.color;
    }

    public FontGlyph h() {
        return this.glyph;
    }

    public float f() {
        return this.y;
    }

    public FontGlyph d() {
        return this.glyph;
    }

    public float a() {
        return this.x;
    }

    public int g() {
        return this.color;
    }

    public int color() {
        int n = 467;
        return this.color;
    }

    public float y() {
        return this.y;
    }

    private static /* synthetic */ String $sf$0(float f, float f2, int n, String string) {
        return "GlyphDrawEntry{x=" + f + ", y=" + f2 + ", color=" + n + ", glyph=" + string + "}";
    }
}

