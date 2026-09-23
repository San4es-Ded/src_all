package wtf.wyvern.core.font;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.utility.game.other.ReplaceUtil;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.render.display.base.Gradient;

public final class MsdfFont implements IMinecraft {
   private final String name;
   private final AbstractTexture texture;
   private final FontData.AtlasData atlas;
   private final FontData.MetricsData metrics;
   private final Int2ObjectMap<MsdfGlyph> glyphs;
   private final Int2ObjectMap<Int2FloatMap> kernings;
   private final Map<Integer, Font> sizedFonts = new HashMap<>();
   private boolean filterConfigured;

   private MsdfFont(String name, AbstractTexture texture, FontData.AtlasData atlas, FontData.MetricsData metrics, Int2ObjectMap<MsdfGlyph> glyphs, Int2ObjectMap<Int2FloatMap> kernings) {
      this.name = name;
      this.texture = texture;
      this.atlas = atlas;
      this.metrics = metrics;
      this.glyphs = glyphs;
      this.kernings = kernings;
   }

   public int getTextureId() {
      return this.texture.getGlId();
   }

   public void applyGlyphs(Matrix4f matrix, VertexConsumer consumer, String text, float size, float thickness, float spacing, float x, float y, float z, int color) {
      this.ensureTextureFilter();
      text = ReplaceUtil.replaceSymbols(text);
      int prevChar = -1;
      boolean skipNext = false;

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         if (c == 7424) {
            c = 1040;
         }

         if (skipNext) {
            skipNext = false;
         } else if (c == 167) {
            skipNext = true;
         } else {
            MsdfGlyph glyph = this.glyphs.get(c);
            if (glyph != null) {
               Int2FloatMap kerning = this.kernings.get(prevChar);
               if (kerning != null) {
                  x += kerning.getOrDefault(c, 0.0F) * size;
               }

               x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
               prevChar = c;
            }
         }
      }

   }

   public void applyGlyphs(Matrix4f matrix, VertexConsumer consumer, String text, float size, float thickness, float spacing, float x, float y, float z, Gradient color) {
      this.ensureTextureFilter();
      text = ReplaceUtil.replaceSymbols(text);
      int prevChar = -1;
      boolean skipNext = false;

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         if (skipNext) {
            skipNext = false;
         } else if (c == 167) {
            skipNext = true;
         } else {
            MsdfGlyph glyph = this.glyphs.get(c);
            if (glyph != null) {
               Int2FloatMap kerning = this.kernings.get(prevChar);
               if (kerning != null) {
                  x += kerning.getOrDefault(c, 0.0F) * size;
               }

               x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
               prevChar = c;
            }
         }
      }

   }

   public float getWidth(String text, float size) {
      return this.getFont(size).width(text);
   }

   /**
    * Performs the actual glyph walk for {@link Font}'s bounded per-size cache.
    * Keeping the uncached implementation separate prevents the many direct
    * MsdfFont#getWidth calls in HUD code from repeating the same work.
    */
   float measureWidth(String text, float size) {
      text = ReplaceUtil.replaceSymbols(text);
      int prevChar = -1;
      float width = 0.0F;
      boolean skipNext = false;

      for(int i = 0; i < text.length(); ++i) {
         char c = text.charAt(i);
         if (c == 7424) {
            c = 1040;
         }

         if (skipNext) {
            skipNext = false;
         } else if (c == 167) {
            skipNext = true;
         } else {
            MsdfGlyph glyph = this.glyphs.get(c);
            if (glyph != null) {
               Int2FloatMap kerning = this.kernings.get(prevChar);
               if (kerning != null) {
                  width += kerning.getOrDefault(c, 0.0F) * size;
               }

               width += glyph.getWidth(size);
               prevChar = c;
            }
         }
      }

      return width;
   }

   public float getTextWidth(Text text, float size) {
      return this.getWidth(text.getString(), size);
   }

   public Font getFont(float size) {
      int sizeBits = Float.floatToIntBits(size);
      Font font = this.sizedFonts.get(sizeBits);
      if (font == null) {
         font = new Font(this, size);
         this.sizedFonts.put(sizeBits, font);
      }
      return font;
   }

   private void ensureTextureFilter() {
      if (!this.filterConfigured) {
         this.texture.setFilter(true, true);
         this.filterConfigured = true;
      }
   }

   public static Builder builder() {
      return new Builder();
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public FontData.AtlasData getAtlas() {
      return this.atlas;
   }

   @Generated
   public FontData.MetricsData getMetrics() {
      return this.metrics;
   }

   public static class Builder {
      private String name = "?";
      private Identifier dataIdentifer;
      private Identifier atlasIdentifier;

      private Builder() {
      }

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder data(String dataFileName) {
         this.dataIdentifer = Wyvern.id("fonts/msdf/" + dataFileName + ".json");
         return this;
      }

      public Builder atlas(String atlasFileName) {
         this.atlasIdentifier = Wyvern.id("fonts/msdf/" + atlasFileName + ".png");
         return this;
      }

      public MsdfFont build() {
         FontData data = (FontData)ResourceProvider.fromJsonToInstance(this.dataIdentifer, FontData.class);
         AbstractTexture texture = IMinecraft.mc.getTextureManager().getTexture(this.atlasIdentifier);
         if (data == null) {
            throw new RuntimeException("Failed to read font data file: " + this.dataIdentifer.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax.");
         } else {
            RenderSystem.recordRenderCall(() -> {
               texture.setFilter(true, false);
            });
            float aWidth = data.atlas().width();
            float aHeight = data.atlas().height();
            Int2ObjectMap<MsdfGlyph> glyphs = new Int2ObjectOpenHashMap<>(data.glyphs().size());
            data.glyphs().forEach(glyphData ->
                    glyphs.put(glyphData.unicode(), new MsdfGlyph(glyphData, aWidth, aHeight)));
            Int2ObjectMap<Int2FloatMap> kernings = new Int2ObjectOpenHashMap<>();
            data.kernings().forEach((kerning) -> {
               Int2FloatMap map = kernings.computeIfAbsent(kerning.leftChar(), k -> new Int2FloatOpenHashMap());
               map.put(kerning.rightChar(), kerning.advance());
            });
            return new MsdfFont(this.name, texture, data.atlas(), data.metrics(), glyphs, kernings);
         }
      }
   }
}
