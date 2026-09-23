package haron.render.font;

import haron.render.font.FontAtlas;
import java.util.Objects;

public final class FontGlyph {
    private final int u;
    private final int v;
    private final int width;
    private final int height;
    private final char value;
    private final FontAtlas atlas;

    public int height() {
        int n = 6;
        return this.height;
    }

    public FontAtlas atlas() {
        return this.atlas;
    }

    public FontGlyph(int n, int n2, int n3, int n4, char c, FontAtlas ahdbxt2) {
        this.u = n;
        this.v = n2;
        this.width = n3;
        this.height = n4;
        this.value = c;
        this.atlas = ahdbxt2;
    }

    public char value() {
        return this.value;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof FontGlyph)) {
            return false;
        }
        FontGlyph wvyq9n2 = (FontGlyph)object;
        return this.u == wvyq9n2.u && this.v == wvyq9n2.v && this.width == wvyq9n2.width && this.height == wvyq9n2.height && this.value == wvyq9n2.value && Objects.equals(this.atlas, wvyq9n2.atlas);
    }

    public String toString() {
        return FontGlyph.$sf$0(this.u, this.v, this.width, this.height, this.value, String.valueOf(this.atlas));
    }

    public int hashCode() {
        return Objects.hash(this.u, this.v, this.width, this.height, Character.valueOf(this.value), this.atlas);
    }

    public char e() {
        return this.value;
    }

    public int i() {
        return this.width;
    }

    public int b() {
        return this.v;
    }

    public int c() {
        return this.width;
    }

    public int h() {
        return this.v;
    }

    public FontAtlas f() {
        int n = 19;
        return this.atlas;
    }

    public FontAtlas l() {
        return this.atlas;
    }

    public int d() {
        return this.height;
    }

    public int a() {
        return this.u;
    }

    public char k() {
        return this.value;
    }

    public int g() {
        return this.u;
    }

    public int v() {
        return this.v;
    }

    public int j() {
        return this.height;
    }

    public int u() {
        return this.u;
    }

    public int width() {
        return this.width;
    }

    private static /* synthetic */ String $sf$0(int n, int n2, int n3, int n4, char c, String string) {
        return "FontGlyph{u=" + n + ", v=" + n2 + ", width=" + n3 + ", height=" + n4 + ", value=" + c + ", atlas=" + string + "}";
    }
}

