package haron.render.font;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.render.font.FontAtlas;
import haron.render.font.GlyphDrawEntry;
import haron.render.font.GlyphFilter;
import haron.render.font.GradientGlyphDrawEntry;
import haron.render.font.FontGlyph;
import haron.render.FramebufferCapture;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class FontRenderer {
    private static final FontRenderContext a = new FontRenderContext(new AffineTransform(), true, true);
    private final Object2ObjectMap<Identifier, ObjectList<GlyphDrawEntry>> b = new Object2ObjectOpenHashMap();
    private final Object2ObjectMap<Identifier, ObjectList<GradientGlyphDrawEntry>> c = new Object2ObjectOpenHashMap();
    private final ObjectList<FontAtlas> d = new ObjectArrayList();
    private Font e;
    private float f;

    public FontRenderer(Font font, float f) {
        this.a(font, f);
    }

    private FontGlyph b(char c) {
        for (FontAtlas ahdbxt2 : this.d) {
            if (!ahdbxt2.b(c)) continue;
            return ahdbxt2.a(c);
        }
        char c2 = (char)FontRenderer.a((int)c);
        return this.a(c2, (char)(c2 + 256)).a(c);
    }

    public float b(String string) {
        float f = 0.0f;
        float f2 = 0.0f;
        for (char c : (string.isEmpty() ? " " : string).toCharArray()) {
            float f3;
            if (c == '\n') {
                f2 += f == 0.0f ? (float)this.b(' ').d() : f;
                f3 = 0.0f;
            } else {
                FontGlyph wvyq9n2 = this.b(c);
                f3 = Math.max(wvyq9n2 == null ? 0.0f : (float)wvyq9n2.d(), f);
            }
            f = f3;
        }
        return f + f2;
    }

    public String b(String string, int n) {
        return string.substring(0, this.d(string, n));
    }

    public void b(String string, double d, double d2, Color color, MatrixStack matrixStack) {
        this.a(string, d - (double)this.a(string) / 2.0, d2, color, matrixStack);
    }

    public void b(MatrixStack matrixStack, String string, double d, double d2, int n) {
        this.a(matrixStack, string, (double)((float)(d - (double)(this.a(string) / 2.0f))), (float)d2, n);
    }

    private void b(Matrix4f matrix4f) {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        for (Identifier identifier : this.c.keySet()) {
            RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            for (GradientGlyphDrawEntry sb7dbt2 : this.c.get(identifier)) {
                float f = sb7dbt2.a();
                float f2 = sb7dbt2.b();
                FontGlyph wvyq9n2 = sb7dbt2.e();
                FontAtlas ahdbxt2 = wvyq9n2.f();
                float f3 = wvyq9n2.c();
                float f4 = wvyq9n2.d();
                float f5 = (float)wvyq9n2.a() / (float)ahdbxt2.b;
                float f6 = (float)wvyq9n2.b() / (float)ahdbxt2.c;
                float f7 = (float)(wvyq9n2.a() + wvyq9n2.c()) / (float)ahdbxt2.b;
                float f8 = (float)(wvyq9n2.b() + wvyq9n2.d()) / (float)ahdbxt2.c;
                int n = sb7dbt2.c();
                int n2 = sb7dbt2.d();
                bufferBuilder.vertex(matrix4f, f, f2 + f4, 0.0f).texture(f5, f8).color(n2);
                bufferBuilder.vertex(matrix4f, f + f3, f2 + f4, 0.0f).texture(f7, f8).color(n2);
                bufferBuilder.vertex(matrix4f, f + f3, f2, 0.0f).texture(f7, f6).color(n);
                bufferBuilder.vertex(matrix4f, f, f2, 0.0f).texture(f5, f6).color(n);
            }
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        }
    }

    public Font b() {
        return this.e;
    }

    public void c(String string, double d, double d2, Color color, MatrixStack matrixStack) {
        this.a(string, d, d2 - (double)this.b(string) / 2.0, color, matrixStack);
    }

    public static Identifier c() {
        int n = 796;
        return Identifier.of((String)"haron", (String)FontRenderer.$sf$0(FontRenderer.d()));
    }

    private float[] c(String string) {
        if (string.isEmpty()) {
            return new float[0];
        }
        String string2 = string.split("\n")[0];
        float[] fArray = new float[string2.length()];
        GlyphVector glyphVector = this.e.createGlyphVector(a, string2);
        fArray[0] = 0.0f;
        float f = 0.0f;
        for (int i = 1; i < string2.length(); ++i) {
            fArray[i] = f += (float)Math.round((float)glyphVector.getGlyphPosition(i).getX() - (float)glyphVector.getGlyphPosition(i - 1).getX());
        }
        return fArray;
    }

    public String c(String string, int n) {
        if (string.isEmpty()) {
            return string;
        }
        GlyphVector glyphVector = this.e.createGlyphVector(a, string);
        float f = (float)n * 2.0f;
        float[] fArray = new float[string.length() + 1];
        fArray[0] = 0.0f;
        for (int i = 1; i <= string.length(); ++i) {
            float f2 = (float)glyphVector.getGlyphPosition(i - 1).getX();
            fArray[i] = fArray[i - 1] + (float)Math.round((float)glyphVector.getGlyphPosition(i).getX() - f2);
        }
        float f3 = fArray[string.length()];
        if (f3 <= f) {
            return string;
        }
        for (int i = 1; i < string.length(); ++i) {
            if (!(f3 - fArray[i] <= f)) continue;
            return string.substring(i);
        }
        return "";
    }

    private static String d() {
        Random random = new Random();
        return IntStream.range(0, 32).mapToObj(n -> {
            return String.valueOf((char)(97 + random.nextInt(26)));
        }).collect(Collectors.joining());
    }

    private int d(String string, int n) {
        if (string.isEmpty()) {
            return 0;
        }
        GlyphVector glyphVector = this.e.createGlyphVector(a, string);
        float f = (float)n * 2.0f;
        float f2 = 0.0f;
        for (int i = 1; i <= string.length(); ++i) {
            float f3 = (float)glyphVector.getGlyphPosition(i - 1).getX();
            if (!((f2 += (float)Math.round((float)glyphVector.getGlyphPosition(i).getX() - f3)) > f)) continue;
            return i - 1;
        }
        return string.length();
    }

    public float a(String string) {
        if (string.isEmpty()) {
            return 0.0f;
        }
        float f = 0.0f;
        for (String string2 : string.split("\n")) {
            if (string2.isEmpty()) continue;
            GlyphVector glyphVector = this.e.createGlyphVector(a, string2);
            float f2 = 0.0f;
            for (int i = 1; i <= string2.length(); ++i) {
                float f3 = (float)glyphVector.getGlyphPosition(i - 1).getX();
                f2 += (float)Math.round((float)glyphVector.getGlyphPosition(i).getX() - f3);
            }
            f = Math.max(f, f2);
        }
        return (float)((double)Math.round((double)(f / 2.0f) * 2.0) / 2.0);
    }

    public String a(String string, int n, boolean bl) {
        return bl ? this.c(string, n) : this.b(string, n);
    }

    public float a(char c) {
        return (float)((double)Math.round((double)((float)Math.round((float)this.e.createGlyphVector(a, String.valueOf(c)).getGlyphPosition(1).getX()) / 2.0f) * 2.0) / 2.0);
    }

    public float a(String string, int n) {
        if (string.isEmpty() || n <= 0) {
            return 0.0f;
        }
        if (n > string.length()) {
            n = string.length();
        }
        GlyphVector glyphVector = this.e.createGlyphVector(a, string);
        float f = 0.0f;
        for (int i = 1; i <= n; ++i) {
            float f2 = (float)glyphVector.getGlyphPosition(i - 1).getX();
            f += (float)Math.round((float)glyphVector.getGlyphPosition(i).getX() - f2);
        }
        return (float)((double)Math.round((double)(f / 2.0f) * 2.0) / 2.0);
    }

    public int a(String string, float f) {
        int n;
        if (string.isEmpty() || f <= 0.0f) {
            return 0;
        }
        GlyphVector glyphVector = this.e.createGlyphVector(a, string);
        float f2 = f * 2.0f;
        float[] fArray = new float[string.length() + 1];
        fArray[0] = 0.0f;
        for (n = 1; n <= string.length(); ++n) {
            float f3 = (float)glyphVector.getGlyphPosition(n - 1).getX();
            fArray[n] = fArray[n - 1] + (float)Math.round((float)glyphVector.getGlyphPosition(n).getX() - f3);
        }
        for (n = 0; n < string.length(); ++n) {
            if (!(f2 < (fArray[n] + fArray[n + 1]) / 2.0f)) continue;
            return n;
        }
        return string.length();
    }

    private void a(Font font, float f) {
        int n = 988;
        this.f = f;
        this.e = font.deriveFont(f * 2.0f);
    }

    private FontAtlas a(char c, char c2) {
        FontAtlas ahdbxt2 = new FontAtlas(c, c2, this.e, FontRenderer.c(), 5);
        this.d.add(ahdbxt2);
        return ahdbxt2;
    }

    private static int a(int n) {
        return 256 * (int)Math.floor((double)n / 256.0);
    }

    public void a(String string, double d, double d2, MatrixStack matrixStack) {
        this.a(string, d, d2, Color.WHITE, matrixStack);
    }

    private static void a(String string, GlyphFilter qr595b2) {
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            if (qr595b2.a(i, string.charAt(i))) continue;
            return;
        }
    }

    public float a() {
        int n = 230;
        return this.f;
    }

    public void a(float f) {
        int n = 993;
        this.f = f;
    }

    public void a(MatrixStack matrixStack, String string, double d, double d2, int n, int n2, float f) {
        matrixStack.push();
        matrixStack.translate((double)Math.round(d * 2.0) / 2.0, (double)Math.round(d2 * 2.0) / 2.0, 0.0);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        RenderSystem.enableBlend();
        FramebufferCapture.applyBlendState();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        char[] cArray = string.toCharArray();
        float[] fArray = this.c(string);
        float f2 = 0.0f;
        int n3 = 0;
        for (int i = 0; i < cArray.length; ++i) {
            char c = cArray[i];
            if (c == '\n') {
                f2 += this.b(string.substring(n3, i)) - 2.0f;
                n3 = i + 1;
                if (n3 >= cArray.length) continue;
                fArray = this.c(string.substring(n3));
                continue;
            }
            FontGlyph wvyq9n2 = this.b(c);
            if (wvyq9n2 == null || wvyq9n2.e() == ' ') continue;
            this.c.computeIfAbsent(wvyq9n2.f().a, key -> new ObjectArrayList<>())
                    .add(new GradientGlyphDrawEntry(fArray[i - n3], f2, n, n2, wvyq9n2, f));
        }
        this.b(matrix4f);
        RenderSystem.disableBlend();
        this.c.clear();
        matrixStack.pop();
    }

    private Color a(Color color, Color color2, float f) {
        return new Color((int)((float)color.getRed() + (float)(color2.getRed() - color.getRed()) * f), (int)((float)color.getGreen() + (float)(color2.getGreen() - color.getGreen()) * f), (int)((float)color.getBlue() + (float)(color2.getBlue() - color.getBlue()) * f), (int)((float)color.getAlpha() + (float)(color2.getAlpha() - color.getAlpha()) * f));
    }

    public void a(Font font) {
        this.e = font;
    }

    private void a(Matrix4f matrix4f) {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_TEX_COLOR);
        for (Identifier identifier : this.b.keySet()) {
            RenderSystem.setShaderTexture((int)0, (Identifier)identifier);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
            BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            for (GlyphDrawEntry as21iw2 : this.b.get(identifier)) {
                float f = as21iw2.a();
                float f2 = as21iw2.b();
                FontGlyph wvyq9n2 = as21iw2.d();
                FontAtlas ahdbxt2 = wvyq9n2.f();
                float f3 = wvyq9n2.c();
                float f4 = wvyq9n2.d();
                float f5 = (float)wvyq9n2.a() / (float)ahdbxt2.b;
                float f6 = (float)wvyq9n2.b() / (float)ahdbxt2.c;
                float f7 = (float)(wvyq9n2.a() + wvyq9n2.c()) / (float)ahdbxt2.b;
                float f8 = (float)(wvyq9n2.b() + wvyq9n2.d()) / (float)ahdbxt2.c;
                int n = as21iw2.c();
                bufferBuilder.vertex(matrix4f, f, f2 + f4, 0.0f).texture(f5, f8).color(n);
                bufferBuilder.vertex(matrix4f, f + f3, f2 + f4, 0.0f).texture(f7, f8).color(n);
                bufferBuilder.vertex(matrix4f, f + f3, f2, 0.0f).texture(f7, f6).color(n);
                bufferBuilder.vertex(matrix4f, f, f2, 0.0f).texture(f5, f6).color(n);
            }
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        }
    }

    public void a(MatrixStack matrixStack, String string, double d, double d2, int n) {
        matrixStack.push();
        matrixStack.translate((double)Math.round(d * 2.0) / 2.0, (double)Math.round(d2 * 2.0) / 2.0, 0.0);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        RenderSystem.enableBlend();
        FramebufferCapture.applyBlendState();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        char[] cArray = string.toCharArray();
        float[] fArray = this.c(string);
        float f = 0.0f;
        int n2 = 0;
        for (int i = 0; i < cArray.length; ++i) {
            char c = cArray[i];
            if (c == '\n') {
                f += this.b(string.substring(n2, i)) - 2.0f;
                n2 = i + 1;
                if (n2 >= cArray.length) continue;
                fArray = this.c(string.substring(n2));
                continue;
            }
            FontGlyph wvyq9n2 = this.b(c);
            if (wvyq9n2 == null || wvyq9n2.e() == ' ') continue;
            this.b.computeIfAbsent(wvyq9n2.f().a, key -> new ObjectArrayList<>())
                    .add(new GlyphDrawEntry(fArray[i - n2], f, n, wvyq9n2));
        }
        this.a(matrix4f);
        RenderSystem.disableBlend();
        this.b.clear();
        matrixStack.pop();
    }

    public void a(String string, double d, double d2, Color color, Color color2, MatrixStack matrixStack) {
        this.a(matrixStack, string, d, d2, color.getRGB(), color2.getRGB(), this.b(string));
    }

    public void a(String string, double d, double d2, Color color, MatrixStack matrixStack) {
        this.a(matrixStack, string, d, d2, color.getRGB());
    }

    private static /* synthetic */ String $sf$0(String string) {
        int n = 259;
        return "temp/" + string;
    }
}
