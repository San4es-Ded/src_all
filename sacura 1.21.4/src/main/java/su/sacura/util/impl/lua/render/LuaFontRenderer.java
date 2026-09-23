package su.sacura.util.impl.lua.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class LuaFontRenderer {
    private final Font font;
    private final Map<String, TextureData> stringCache = new HashMap<>();

    public LuaFontRenderer(Font font) {
        this.font = font;
    }

    public int getWidth(String text, float scale) {
        if (text == null)
            return 0;
        BufferedImage temp = new BufferedImage(1, 1, 2);
        Graphics2D g2 = temp.createGraphics();
        g2.setFont(this.font);
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(text);
        g2.dispose();
        return (int)(w / 2.0F * scale);
    }

    public int getWidth(String text) {
        return getWidth(text, 1.0F);
    }

    public void drawString(Matrix4f matrix, String text, float x, float y, int color, float scale) {
        if (text == null || text.isEmpty())
            return;
        TextureData data = this.stringCache.get(text);
        if (data == null) {
            data = renderString(text);
            this.stringCache.put(text, data);
        }
        if (data == null)
            return;
        float renderScale = 0.5F * scale;
        float renderWidth = data.width * renderScale;
        float renderHeight = data.height * renderScale;
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        RenderSystem.setShaderTexture(0, data.id);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // Исправлено: field_53880 -> ShaderProgramKeys.POSITION_TEX_COLOR
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);

        Tessellator tessellator = Tessellator.getInstance();
        // Исправлено: field_27382 -> VertexFormat.DrawMode.QUADS
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        float zLevel = 0.01F;

        buffer.vertex(matrix, x, y + renderHeight, zLevel).texture(0.0F, 1.0F).color(r, g, b, a);
        buffer.vertex(matrix, x + renderWidth, y + renderHeight, zLevel).texture(1.0F, 1.0F).color(r, g, b, a);
        buffer.vertex(matrix, x + renderWidth, y, zLevel).texture(1.0F, 0.0F).color(r, g, b, a);
        buffer.vertex(matrix, x, y, zLevel).texture(0.0F, 0.0F).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    private TextureData renderString(String text) {
        BufferedImage temp = new BufferedImage(1, 1, 2);
        Graphics2D g2 = temp.createGraphics();
        g2.setFont(this.font);
        FontMetrics fm = g2.getFontMetrics();
        int width = fm.stringWidth(text) + 4;
        int height = fm.getHeight();
        g2.dispose();

        if (width <= 4 || height <= 0)
            return null;

        BufferedImage image = new BufferedImage(width, height, 2);
        g2 = image.createGraphics();
        g2.setFont(this.font);

        g2.setColor(new Color(255, 255, 255, 0));
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.drawString(text, 0, fm.getAscent());
        g2.dispose();

        NativeImage nativeImage = new NativeImage(width, height, true);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                nativeImage.setColorArgb(x, y, image.getRGB(x, y));
        }

        NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);

        String idStr = "lua_font/" + UUID.randomUUID().toString().replace("-", "") + "_" + Math.abs(text.hashCode());
        Identifier id = Identifier.of("sacura", idStr.toLowerCase());

        MinecraftClient.getInstance().getTextureManager().registerTexture(id, (AbstractTexture)texture);
        // Исправлено: setMipmapLevels(boolean, boolean) удалён — в 1.21.4 этот метод отсутствует,
        // upload() самостоятельно загружает текстуру
        texture.upload();

        return new TextureData(id, width, height);
    }

    private static class TextureData {
        Identifier id;
        int width;
        int height;

        public TextureData(Identifier id, int width, int height) {
            this.id = id;
            this.width = width;
            this.height = height;
        }
    }
}