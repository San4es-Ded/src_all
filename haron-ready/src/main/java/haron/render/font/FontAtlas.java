package haron.render.font;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.render.font.FontGlyph;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class FontAtlas {
    public final Identifier a;
    public int b;
    public int c;
    private final Char2ObjectArrayMap<FontGlyph> glyphs = new Char2ObjectArrayMap();
    private final char firstChar;
    private final char lastCharExclusive;
    private final Font baseFont;
    private final int padding;
    private boolean built;

    public FontGlyph glyph(char c) {
        if (!this.built) {
            this.build();
        }
        return (FontGlyph)this.glyphs.get(c);
    }

    private static Object createRasterDataBuffer(WritableRaster writableRaster) {
        int n = writableRaster.getDataBuffer().getDataType();
        int n2 = writableRaster.getNumDataElements();
        switch (n) {
            case 0: {
                return new byte[n2];
            }
            case 1: {
                return new short[n2];
            }
            default: {
                throw new IllegalArgumentException(FontAtlas.$sf$0(n));
            }
            case 3: 
        }
        return new int[n2];
    }

    private Font fontFor(char c) {
        return this.baseFont.canDisplay(c) ? this.baseFont : new Font("SansSerif", 0, this.baseFont.getSize());
    }

    public static void uploadTexture(Identifier identifier, BufferedImage bufferedImage) {
        int n = bufferedImage.getWidth();
        int n2 = bufferedImage.getHeight();
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, n, n2, false);
        WritableRaster writableRaster = bufferedImage.getRaster();
        ColorModel colorModel = bufferedImage.getColorModel();
        Object object = FontAtlas.createRasterDataBuffer(writableRaster);
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < n; ++j) {
                writableRaster.getDataElements(j, i, object);
                nativeImage.setColorArgb(j, i, ColorHelper.getArgb((int)colorModel.getAlpha(object), (int)colorModel.getBlue(object), (int)colorModel.getGreen(object), (int)colorModel.getRed(object)));
            }
        }
        NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
        nativeImageBackedTexture.upload();
        if (RenderSystem.isOnRenderThread()) {
            MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, (AbstractTexture)nativeImageBackedTexture);
        } else {
            RenderSystem.recordRenderCall(() -> {
                MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, (AbstractTexture)nativeImageBackedTexture);
            });
        }
    }

    public FontAtlas(char c, char c2, Font font, Identifier identifier, int n) {
        this.firstChar = c;
        this.lastCharExclusive = c2;
        this.baseFont = font;
        this.a = identifier;
        this.padding = n;
    }

    public boolean b(char c) {
        return this.contains(c);
    }

    public boolean contains(char c) {
        return c >= this.firstChar && c < this.lastCharExclusive;
    }

    public FontGlyph a(char c) {
        return this.glyph(c);
    }

    public static void a(Identifier identifier, BufferedImage bufferedImage) {
        FontAtlas.uploadTexture(identifier, bufferedImage);
    }

    public void a() {
        this.build();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void build() {
        if (this.built) {
            return;
        }
        int n = Math.max(this.lastCharExclusive - this.firstChar, 1);
        int n2 = Math.max((int)(Math.ceil(Math.sqrt(n)) * 1.5), 1);
        this.glyphs.clear();
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        ArrayList<FontGlyph> arrayList = new ArrayList<FontGlyph>();
        FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, false);
        for (int i = 0; i < n; ++i) {
            char c = (char)(this.firstChar + i);
            Rectangle2D rectangle2D = this.fontFor(c).getStringBounds(String.valueOf(c), fontRenderContext);
            int n9 = (int)Math.ceil(rectangle2D.getWidth());
            int n10 = (int)Math.ceil(rectangle2D.getHeight());
            n3 = Math.max(n3, n5 + n9);
            n4 = Math.max(n4, n6 + n10);
            if (n8 >= n2) {
                n5 = 0;
                n6 += n7 + this.padding;
                n8 = 0;
                n7 = 0;
            }
            n7 = Math.max(n7, n10);
            arrayList.add(new FontGlyph(n5, n6, n9, n10, c, this));
            n5 += n9 + this.padding;
            ++n8;
        }
        BufferedImage bufferedImage = new BufferedImage(Math.max(n3 + this.padding, 1), Math.max(n4 + this.padding, 1), 2);
        this.b = bufferedImage.getWidth();
        this.c = bufferedImage.getHeight();
        Graphics2D graphics2D = bufferedImage.createGraphics();
        try {
            graphics2D.setColor(new Color(255, 255, 255, 0));
            graphics2D.fillRect(0, 0, this.b, this.c);
            graphics2D.setColor(Color.WHITE);
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            for (FontGlyph wvyq9n2 : arrayList) {
                graphics2D.setFont(this.fontFor(wvyq9n2.value()));
                graphics2D.drawString(String.valueOf(wvyq9n2.value()), wvyq9n2.u(), wvyq9n2.v() + graphics2D.getFontMetrics().getAscent());
                this.glyphs.put(wvyq9n2.value(), wvyq9n2);
            }
            FontAtlas.uploadTexture(this.a, bufferedImage);
            this.built = true;
        }
        finally {
            graphics2D.dispose();
        }
    }

    private static /* synthetic */ String $sf$0(int n) {
        return "Unsupported data buffer type: " + n;
    }
}
