package aethereal.render;

import aethereal.ui.shader.GradientUtil;
import aethereal.util.ChatUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_1044;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_5944;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import platform.client.processors.draw.fonts.FontData;

public class Font {
   private final class_10156 a = new class_10156(class_2960.method_60655("westra", "core/text/text"), class_290.field_1575, class_10149.field_53930);
   private final String b;
   private final class_1044 c;
   private final FontData.AtlasData d;
   private final FontData.MetricsData e;
   private final Map<Integer, MsdfGlyph> f;
   private final Map<Integer, Map<Integer, Float>> g;

   @Generated
   public String b() {
      return this.b;
   }

   @Generated
   public FontData.AtlasData c() {
      return this.d;
   }

   @Generated
   public FontData.MetricsData d() {
      return this.e;
   }

   public Font(
      String name,
      class_1044 texture,
      FontData.AtlasData atlas,
      FontData.MetricsData metrics,
      Map<Integer, MsdfGlyph> glyphs,
      Map<Integer, Map<Integer, Float>> kernings
   ) {
      this.b = name;
      this.c = texture;
      this.d = atlas;
      this.e = metrics;
      this.f = glyphs;
      this.g = kernings;
   }

   private void a(float outlineThickness, float thickness, float smoothness, int outlineColor) {
      this.a(outlineThickness, thickness, smoothness, outlineColor, -1.0F, -1.0F);
   }

   private void a(float outlineThickness, float thickness, float smoothness, int outlineColor, float fadeStart, float fadeEnd) {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.setShaderTexture(0, this.c.method_4624());
      class_5944 shader = RenderSystem.setShader(this.a);
      if (shader != null) {
         shader.method_34582("uRange").method_1251(this.d.range());
         shader.method_34582("uThickness").method_1251(thickness);
         shader.method_34582("uSmoothness").method_1251(smoothness);
         boolean outlineEnabled = outlineThickness > 0.0F;
         shader.method_34582("uOutline").method_35649(outlineEnabled ? 1 : 0);
         if (outlineEnabled) {
            shader.method_34582("uOutlineThickness").method_1251(outlineThickness);
            float[] outlineComponents = ColorUtil.a(outlineColor);
            shader.method_34582("uOutlineColor").method_35657(outlineComponents[0], outlineComponents[1], outlineComponents[2], outlineComponents[3]);
         }

         boolean fadeEnabled = fadeEnd > fadeStart;
         shader.method_34582("uFadeEnabled").method_35649(fadeEnabled ? 1 : 0);
         if (fadeEnabled) {
            shader.method_34582("uFadeStart").method_1251(fadeStart);
            shader.method_34582("uFadeEnd").method_1251(fadeEnd);
         }
      }
   }

   private void a(class_287 builder) {
      class_286.method_43433(builder.method_60800());
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   public void a(
      class_4587 matrixStack,
      class_2561 text,
      float x,
      float y,
      float size,
      float alpha,
      float thickness,
      float smoothness,
      float spacing,
      int outlineColor,
      float outlineThickness
   ) {
      try {
         Matrix4f matrix = matrixStack.method_23760().method_23761();
         this.a(outlineThickness, thickness, smoothness, outlineColor);
         class_287 builder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
         float adjustedThickness = (thickness + outlineThickness * 0.5F) * 0.5F * size;
         float baselineY = y + this.e.baselineHeight() * size;
         boolean hasGlyphs = this.a(matrix, builder, this.a(text), size, alpha, adjustedThickness, spacing, x, baselineY, 0.0F);
         if (hasGlyphs) {
            this.a(builder);
         }
      } catch (Exception var17) {
         var17.printStackTrace();
      }
   }

   public void a(
      class_4587 matrixStack,
      String text,
      float x,
      float y,
      float size,
      float thickness,
      int color,
      int colorSecond,
      float offset,
      float smoothness,
      float spacing,
      int outlineColor,
      float outlineThickness
   ) {
      if (text == "11111") {
         ChatUtil.a(color);
      }

      if (text != null && !text.isEmpty()) {
         Matrix4f matrix = matrixStack.method_23760().method_23761();
         this.a(outlineThickness, thickness, smoothness, outlineColor);
         class_287 builder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
         float adjustedThickness = (thickness + outlineThickness * 0.5F) * 0.5F * size;
         float baselineY = y + this.e.baselineHeight() * size;
         boolean hasGlyphs = this.a(matrix, builder, text, size, adjustedThickness, spacing, x, baselineY, 0.0F, color, colorSecond, offset);
         if (hasGlyphs) {
            this.a(builder);
         }
      }
   }

   public List<MsdfGlyph.a> a(class_2561 text) {
      List<MsdfGlyph.a> result = new ArrayList<>();
      boolean[] started = new boolean[]{false};
      text.method_27658((style, string) -> {
         if (string != null && !string.isEmpty()) {
            if (!started[0]) {
               string = string.replaceFirst("^\\s+", "");
               if (string.isEmpty()) {
                  return Optional.empty();
               }

               started[0] = true;
            }

            int color = style.method_10973() != null ? style.method_10973().method_27716() | 0xFF000000 : -1;
            result.addAll(this.a(string, color));
            return Optional.empty();
         } else {
            return Optional.empty();
         }
      }, class_2583.field_24360);
      return result;
   }

   private List<MsdfGlyph.a> a(String raw, int color) {
      String raw2 = raw.replace('⚡', 'ŝ').replace('★', 'Ş');
      List<MsdfGlyph.a> result = new ArrayList<>();

      for (int i = 0; i < raw2.length(); i++) {
         char c = raw2.charAt(i);
         if (i + 1 < raw2.length()) {
            char n = raw2.charAt(i + 1);
            if (c != 3618 && c != 9889 && (c != 167 || "0123456789abcdefklor".indexOf(n) < 0)) {
               if (this.f.containsKey(Integer.valueOf(c))) {
                  result.add(new MsdfGlyph.a(c, color));
               }
            } else {
               i++;
            }
         } else if (this.f.containsKey(Integer.valueOf(c))) {
            result.add(new MsdfGlyph.a(c, color));
         }
      }

      return result;
   }

   public void a(class_4587 matrixStack, class_2561 text, float x, float y, float size) {
      this.a(matrixStack, text, x, y, size, 0.0F, 1.0F);
   }

   public void a(class_4587 matrixStack, class_2561 text, float x, float y, float size, double alpha) {
      this.a(matrixStack, text, x, y, size, 0.0F, (float)alpha);
   }

   public void a(class_4587 matrixStack, class_2561 text, float x, float y, float size, float thickness, float alpha) {
      this.a(matrixStack, text, x, y, size, alpha, thickness, 0.5F, 0.0F, -1, thickness);
   }

   public void a(class_4587 matrixStack, String text, float x, float y, float size, int color) {
      this.a(matrixStack, text, x, y, size, color, 0.0F);
   }

   public void a(class_4587 matrixStack, String text, float x, float y, float size, int color, float thickness) {
      this.a(matrixStack, text, x, y, size, thickness, color, -1, -1.0F, 0.5F, 0.0F, -1, thickness);
   }

   public void a(class_4587 matrixStack, String text, float x, float y, float size, int color, float speed, float offset) {
      this.a(matrixStack, GradientUtil.a(text, color, speed, offset), x, y, size);
   }

   public void b(class_4587 matrixStack, String text, float x, float y, float size, int color) {
      this.b(matrixStack, text, x, y, size, color, 0.0F);
   }

   public void b(class_4587 matrixStack, String text, float x, float y, float size, int color, float thickness) {
      float textWidth = this.b(text, size, thickness);
      this.a(matrixStack, text, x - textWidth / 2.0F, y, size, color, thickness);
   }

   public void b(class_4587 matrixStack, String text, float x, float y, float size, int color, float speed, float offset) {
      this.a(matrixStack, GradientUtil.a(text, color, speed, offset), x - this.a(text, size) / 2.0F, y, size);
   }

   public void c(class_4587 matrixStack, String text, float x, float y, float size, int color, float visibleWidth) {
      this.c(matrixStack, text, x, y, size, color, 0.0F, visibleWidth);
   }

   public void c(class_4587 matrixStack, String text, float x, float y, float size, int color, float thickness, float visibleWidth) {
      if (text != null && !text.isEmpty() && !(visibleWidth <= 0.0F)) {
         float fadeStart = x + Math.max(0.0F, visibleWidth - 5.0F);
         float fadeEnd = x + visibleWidth;
         Matrix4f matrix = matrixStack.method_23760().method_23761();
         this.a(0.0F, thickness, 0.5F, -1, fadeStart, fadeEnd);
         class_287 builder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
         float adjustedThickness = (thickness + thickness * 0.5F) * 0.5F * size;
         float baselineY = y + this.e.baselineHeight() * size;
         boolean hasGlyphs = this.a(matrix, builder, text, size, adjustedThickness, 0.0F, x, baselineY, 0.0F, color, -1, -1.0F);
         if (hasGlyphs) {
            this.a(builder);
         }
      }
   }

   public boolean a(
      Matrix4f matrix,
      class_4588 consumer,
      String text,
      float size,
      float thickness,
      float spacing,
      float x,
      float y,
      float z,
      int color,
      int colorSecond,
      float offset
   ) {
      String text2 = text.replace('⚡', 'ŝ').replace('★', 'Ş');
      int previousChar = -1;
      float totalWidth = this.a(text2, size);
      float time = (float)System.currentTimeMillis() % 3000.0F / 3000.0F;
      boolean hasGlyphs = false;

      for (int i = 0; i < text2.length(); i++) {
         int codePoint = text2.charAt(i);
         MsdfGlyph glyph = this.f.get(codePoint);
         if (glyph != null) {
            hasGlyphs = true;
            float x2 = x + this.a(previousChar, codePoint, size);
            int currentColor = color;
            if (offset > 1.0F) {
               currentColor = ColorUtil.a(color, colorSecond, x2 - x, totalWidth, time, offset);
            }

            x = x2 + glyph.a(matrix, consumer, size, x2, y, z, currentColor) + thickness + spacing;
            previousChar = codePoint;
         }
      }

      return hasGlyphs;
   }

   public boolean a(
      Matrix4f matrix, class_4588 consumer, List<MsdfGlyph.a> coloredGlyphs, float size, float alpha, float thickness, float spacing, float x, float y, float z
   ) {
      int previousChar = -1;
      boolean started = false;
      boolean hasGlyphs = false;

      for (int i = 0; i < coloredGlyphs.size(); i++) {
         MsdfGlyph.a glyphData = coloredGlyphs.get(i);
         int codePoint = glyphData.a();
         if (started || codePoint != 32) {
            started = true;
            int color = glyphData.b();
            MsdfGlyph glyph = this.f.get(codePoint);
            if (glyph != null) {
               hasGlyphs = true;
               float x2 = x + this.a(previousChar, codePoint, size);
               float advance = glyph.a(matrix, consumer, size, x2, y, z, ColorUtil.a(color, alpha));
               if (i < coloredGlyphs.size() - 1) {
                  advance += thickness + spacing;
               }

               x = x2 + advance;
               previousChar = codePoint;
            }
         }
      }

      return hasGlyphs;
   }

   private float a(int previousChar, int currentChar, float size) {
      Map<Integer, Float> kerning = this.g.get(previousChar);
      return kerning == null ? 0.0F : kerning.getOrDefault(currentChar, 0.0F) * size;
   }

   public float a(float size) {
      return size;
   }

   public float a(class_2561 text, float size) {
      return this.a(text, size, 0.0F);
   }

   public float a(class_2561 text, float size, float thickness) {
      if (text == null) {
         return 0.0F;
      } else {
         List<MsdfGlyph.a> coloredGlyphs = this.a(text);
         return this.a(coloredGlyphs, size, thickness);
      }
   }

   public float a(String text, float size) {
      return this.b(text, size, 0.0F);
   }

   public float b(String text, float size) {
      MsdfGlyph glyph;
      return text != null && !text.isEmpty() && (glyph = this.f.get(Integer.valueOf(text.charAt(0)))) != null ? glyph.b(size) : 0.0F;
   }

   public float a(String text, float size, float centerY) {
      if (text != null && !text.isEmpty()) {
         MsdfGlyph glyph = this.f.get(Integer.valueOf(text.charAt(0)));
         if (glyph == null) {
            return centerY - this.a(size) / 2.0F;
         } else {
            float inkCenter = (this.e.baselineHeight() - glyph.a() + glyph.b() / 2.0F) * size;
            return centerY - inkCenter;
         }
      } else {
         return centerY - this.a(size) / 2.0F;
      }
   }

   public float b(String text, float size, float thickness) {
      if (text != null && !text.isEmpty()) {
         String text2 = text.replace('⚡', 'ŝ').replace('★', 'Ş');
         int previousChar = -1;
         float width = 0.0F;
         int renderedGlyphs = 0;

         for (int i = 0; i < text2.length(); i++) {
            int codePoint = text2.charAt(i);
            MsdfGlyph glyph = this.f.get(codePoint);
            if (glyph != null) {
               width = width + this.a(previousChar, codePoint, size) + glyph.a(size);
               renderedGlyphs++;
               previousChar = codePoint;
            }
         }

         return renderedGlyphs > 0 ? width + renderedGlyphs * thickness : width;
      } else {
         return 0.0F;
      }
   }

   private float a(List<MsdfGlyph.a> coloredGlyphs, float size, float thickness) {
      int previousChar = -1;
      float width = 0.0F;
      int renderedGlyphs = 0;

      for (MsdfGlyph.a coloredGlyph : coloredGlyphs) {
         int codePoint = coloredGlyph.a();
         MsdfGlyph glyph = this.f.get(codePoint);
         if (glyph != null) {
            width = width + this.a(previousChar, codePoint, size) + glyph.a(size);
            renderedGlyphs++;
            previousChar = codePoint;
         }
      }

      return renderedGlyphs > 1 ? width + (renderedGlyphs - 1) * thickness : width;
   }

   public float a(class_4587 matrixStack, String text, float x, float y, float size, int color, float maxWidth, boolean isHovered, float offset, float delta) {
      if (text != null && !text.isEmpty() && !(maxWidth <= 0.0F)) {
         float textWidth = this.a(text, size);
         float wrap = textWidth + 12.0F;
         if (isHovered || offset > 0.0F) {
            offset += delta * 1.5F;
            if (offset >= wrap) {
               offset = isHovered ? offset - wrap : 0.0F;
            }
         }

         if (!(textWidth <= maxWidth) && offset != 0.0F) {
            ScissorUtil.a(matrixStack, x - 1.0F, y - size * 0.5F, maxWidth + 2.0F, size * 1.5F + 0.5F);
            this.a(0.0F, 0.0F, 0.5F, -1, x + maxWidth - 5.0F, x + maxWidth);
            class_287 builder = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
            float baselineY = y + this.e.baselineHeight() * size;
            this.a(matrixStack.method_23760().method_23761(), builder, text, size, 0.0F, 0.0F, x - offset, baselineY, 0.0F, color, -1, -1.0F);
            this.a(matrixStack.method_23760().method_23761(), builder, text, size, 0.0F, 0.0F, x - offset + wrap, baselineY, 0.0F, color, -1, -1.0F);
            this.a(builder);
            ScissorUtil.a(matrixStack);
            return offset;
         } else {
            this.c(matrixStack, text, x, y, size, color, 0.0F, maxWidth);
            return offset;
         }
      } else {
         return 0.0F;
      }
   }

   public static FontBuilder a() {
      return new FontBuilder();
   }
}
