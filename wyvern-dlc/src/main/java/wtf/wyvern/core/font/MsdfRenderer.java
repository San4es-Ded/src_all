package wtf.wyvern.core.font;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import wtf.wyvern.render.display.base.Gradient;
import wtf.wyvern.render.display.base.color.ColorRGBA;

public final class MsdfRenderer {
   public static final ShaderProgramKey MSDF_FONT_SHADER_KEY;
   private static ShaderProgram configuredShader;
   private static GlUniform rangeUniform;
   private static GlUniform enableFadeoutUniform;
   private static GlUniform fadeoutStartUniform;
   private static GlUniform fadeoutEndUniform;
   private static GlUniform maxWidthUniform;
   private static GlUniform textPosXUniform;
   private static float configuredRange = Float.NaN;
   private static int configuredFadeout = -1;
   private static float configuredFadeoutStart = Float.NaN;
   private static float configuredFadeoutEnd = Float.NaN;
   private static float configuredMaxWidth = Float.NaN;

   private static void configureShader(MsdfFont font, boolean enableFadeout, float fadeoutStart,
                                       float fadeoutEnd, float maxWidth, float textPosX) {
      ShaderProgram shader = RenderSystem.setShader(MSDF_FONT_SHADER_KEY);
      if (shader != configuredShader) {
         configuredShader = shader;
         rangeUniform = shader.getUniform("Range");
         enableFadeoutUniform = shader.getUniform("EnableFadeout");
         fadeoutStartUniform = shader.getUniform("FadeoutStart");
         fadeoutEndUniform = shader.getUniform("FadeoutEnd");
         maxWidthUniform = shader.getUniform("MaxWidth");
         textPosXUniform = shader.getUniform("TextPosX");
         shader.getUniform("Thickness").set(0.05F);
         shader.getUniform("Smoothness").set(0.57F);
         configuredRange = Float.NaN;
         configuredFadeout = -1;
         configuredFadeoutStart = Float.NaN;
         configuredFadeoutEnd = Float.NaN;
         configuredMaxWidth = Float.NaN;
      }

      float range = font.getAtlas().range();
      if (Float.floatToIntBits(range) != Float.floatToIntBits(configuredRange)) {
         rangeUniform.set(range);
         configuredRange = range;
      }

      int fadeout = enableFadeout ? 1 : 0;
      if (fadeout != configuredFadeout) {
         enableFadeoutUniform.set(fadeout);
         configuredFadeout = fadeout;
      }
      if (Float.floatToIntBits(fadeoutStart) != Float.floatToIntBits(configuredFadeoutStart)) {
         fadeoutStartUniform.set(fadeoutStart);
         configuredFadeoutStart = fadeoutStart;
      }
      if (Float.floatToIntBits(fadeoutEnd) != Float.floatToIntBits(configuredFadeoutEnd)) {
         fadeoutEndUniform.set(fadeoutEnd);
         configuredFadeoutEnd = fadeoutEnd;
      }
      if (Float.floatToIntBits(maxWidth) != Float.floatToIntBits(configuredMaxWidth)) {
         maxWidthUniform.set(maxWidth);
         configuredMaxWidth = maxWidth;
      }
      textPosXUniform.set(textPosX);
   }

   public static void renderText(MsdfFont font, String text, float size, int color, Matrix4f matrix, float x, float y, float z) {
      renderText(font, text, size, color, matrix, x, y, z, false, 0.0F, 1.0F, 0.0F);
   }

   public static void renderText(MsdfFont font, String text, float size, int color, Matrix4f matrix, float x, float y, float z, boolean enableFadeout, float fadeoutStart, float fadeoutEnd, float maxWidth) {
      float thickness = 0.05F;
      float smoothness = 0.5F;
      float spacing = 0.0F;
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, font.getTextureId());
      configureShader(font, enableFadeout, fadeoutStart, fadeoutEnd, maxWidth, x);
      BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      font.applyGlyphs(matrix, builder, text, size, thickness * 0.5F * size, spacing, x - 0.75F, y + size * 0.7F, z, color);
      BuiltBuffer builtBuffer = builder.endNullable();
      if (builtBuffer != null) {
         BufferRenderer.drawWithGlobalProgram(builtBuffer);
      }

      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public static void renderText(MsdfFont font, String text, float size, int color, Matrix4f matrix, float x, float y, float z, boolean enableFadeout, float fadeoutStart, float fadeoutEnd) {
      float maxWidth = font.getWidth(text, size) * 2.0F;
      renderText(font, text, size, color, matrix, x, y, z, enableFadeout, fadeoutStart, fadeoutEnd, maxWidth);
   }

   public static void renderText(MsdfFont font, Text text, float size, Matrix4f matrix, float x, float y, float z) {
      renderText(font, text, size, matrix, x, y, z, false, 0.0F, 1.0F, 0.0F);
   }

   public static void renderText(MsdfFont font, Text text, float size, Matrix4f matrix, float x, float y, float z, int alpha) {
      renderText(font, text, size, matrix, x, y, z, false, 0.0F, 1.0F, 0.0F, alpha);
   }

   public static void renderText(MsdfFont font, Text text, float size, Matrix4f matrix, float x, float y, float z, boolean enableFadeout, float fadeoutStart, float fadeoutEnd, float maxWidth) {
      float thickness = 0.05F;
      float smoothness = 0.5F;
      float spacing = 0.0F;
      List<FormattedTextProcessor.TextSegment> segments = FormattedTextProcessor.processText(text, ColorRGBA.WHITE.getRGB());
      float currentX = x;
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, font.getTextureId());
      configureShader(font, enableFadeout, fadeoutStart, fadeoutEnd, maxWidth, x);
      BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      FormattedTextProcessor.TextSegment segment;
      for(Iterator var18 = segments.iterator(); var18.hasNext(); currentX += font.getWidth(segment.text(), size)) {
         segment = (FormattedTextProcessor.TextSegment)var18.next();
         font.applyGlyphs(matrix, builder, segment.text(), size, thickness * 0.5F * size, spacing - 0.3F, currentX - 0.75F, y + size * 0.7F, z, segment.color());
      }

      BuiltBuffer builtBuffer = builder.endNullable();
      if (builtBuffer != null) {
         BufferRenderer.drawWithGlobalProgram(builtBuffer);
      }

      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public static void renderText(MsdfFont font, Text text, float size, Matrix4f matrix, float x, float y, float z, boolean enableFadeout, float fadeoutStart, float fadeoutEnd, float maxWidth, int alpha) {
      float thickness = 0.05F;
      float smoothness = 0.5F;
      float spacing = 0.0F;
      List<FormattedTextProcessor.TextSegment> segments = FormattedTextProcessor.processText(text, ColorRGBA.WHITE.getRGB());
      float currentX = x;
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, font.getTextureId());
      configureShader(font, enableFadeout, fadeoutStart, fadeoutEnd, maxWidth, x);
      BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

      FormattedTextProcessor.TextSegment segment;
      for(Iterator var19 = segments.iterator(); var19.hasNext(); currentX += font.getWidth(segment.text(), size)) {
         segment = (FormattedTextProcessor.TextSegment)var19.next();
         int color = segment.color();
         if (alpha != 255) {
            color = color & 16777215 | (alpha & 255) << 24;
         }

         font.applyGlyphs(matrix, builder, segment.text(), size, thickness * 0.5F * size, spacing - 0.3F, currentX - 0.75F, y + size * 0.7F, z, color);
      }

      BuiltBuffer builtBuffer = builder.endNullable();
      if (builtBuffer != null) {
         BufferRenderer.drawWithGlobalProgram(builtBuffer);
      }

      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public static void renderText(MsdfFont font, Text text, float size, Matrix4f matrix, float x, float y, float z, boolean enableFadeout, float fadeoutStart, float fadeoutEnd) {
      float maxWidth = font.getTextWidth(text, size) * 2.0F;
      renderText(font, text, size, matrix, x, y, z, enableFadeout, fadeoutStart, fadeoutEnd, maxWidth);
   }

   public static void renderText(MsdfFont font, String text, float size, Gradient color, Matrix4f matrix, float x, float y, float z) {
      renderText(font, text, size, color, matrix, x, y, z, false, 0.0F, 1.0F, 0.0F);
   }

   public static void renderText(MsdfFont font, String text, float size, Gradient color, Matrix4f matrix, float x, float y, float z, boolean enableFadeout, float fadeoutStart, float fadeoutEnd, float maxWidth) {
      text = text.replace("і", "i").replace("І", "I");
      float thickness = 0.05F;
      float smoothness = 0.5F;
      float spacing = 0.0F;
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, font.getTextureId());
      configureShader(font, enableFadeout, fadeoutStart, fadeoutEnd, maxWidth, x);
      BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      font.applyGlyphs(matrix, builder, text, size, thickness * 0.5F * size, spacing, x - 0.75F, y + size * 0.7F, z, color);
      BuiltBuffer builtBuffer = builder.endNullable();
      if (builtBuffer != null) {
         BufferRenderer.drawWithGlobalProgram(builtBuffer);
      }

      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public static void renderText(MsdfFont font, String text, float size, Gradient color, Matrix4f matrix, float x, float y, float z, boolean enableFadeout, float fadeoutStart, float fadeoutEnd) {
      float maxWidth = font.getWidth(text, size) * 2.0F;
      renderText(font, text, size, color, matrix, x, y, z, enableFadeout, fadeoutStart, fadeoutEnd, maxWidth);
   }

   @Generated
   private MsdfRenderer() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      MSDF_FONT_SHADER_KEY = new ShaderProgramKey(ResourceProvider.getShaderIdentifier("msdf_font/data"), VertexFormats.POSITION_TEXTURE_COLOR, Defines.EMPTY);
   }
}
