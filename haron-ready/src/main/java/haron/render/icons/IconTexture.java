package haron.render.icons;

import net.minecraft.util.Identifier;

public final class IconTexture {
    private final Identifier identifier;
    private final int width;
    private final int height;

    public IconTexture(Identifier identifier, int n, int n2) {
        this.identifier = identifier;
        this.width = n;
        this.height = n2;
    }

    public int b() {
        return this.width;
    }

    public int c() {
        return this.height;
    }

    public Identifier a() {
        return this.identifier;
    }
}

