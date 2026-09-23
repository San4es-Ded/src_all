 package su.sacura.util.impl.render.msdf.api;
 
 import com.mojang.blaze3d.systems.RenderSystem;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.stream.Collectors;
 import net.minecraft.client.MinecraftClient;
 import net.minecraft.client.render.VertexConsumer;
 import net.minecraft.client.texture.AbstractTexture;
 import net.minecraft.util.Identifier;
 import org.joml.Matrix4f;
 import su.sacura.util.impl.render.providers.ColorProvider;
 import su.sacura.util.impl.render.providers.ResourceProvider;
 
 public final class MsdfFont {
   private final String name;
   
   private final AbstractTexture texture;
   
   private final FontData.AtlasData atlas;
   
   private final FontData.MetricsData metrics;
   
   private final Map<Integer, MsdfGlyph> glyphs;
   
   private final Map<Integer, Map<Integer, Float>> kernings;
   
   private MsdfFont(String name, AbstractTexture texture, FontData.AtlasData atlas, FontData.MetricsData metrics, Map<Integer, MsdfGlyph> glyphs, Map<Integer, Map<Integer, Float>> kernings) {
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
     int prevChar = -1;
     for (int i = 0; i < text.length(); i++) {
       int _char = text.charAt(i);
       MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
       if (glyph != null) {
         Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
         if (kerning != null)
           x += ((Float)kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0F))).floatValue() * size; 
         x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
         prevChar = _char;
       } 
     } 
   }
   
   public void applyGlyphs(Matrix4f matrix, VertexConsumer consumer, String text, float size, float thickness, float spacing, float x, float y, float z, int color1, int color2) {
     float totalWidth = getWidth(text, size);
     float startX = x;
     int prevChar = -1;
     for (int i = 0; i < text.length(); i++) {
       int _char = text.charAt(i);
       MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
       if (glyph != null) {
         Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
         if (kerning != null)
           x += ((Float)kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0F))).floatValue() * size; 
         float currentOffset = x - startX;
         float factor = (totalWidth > 0.0F) ? (currentOffset / totalWidth) : 0.0F;
         if (factor < 0.0F)
           factor = 0.0F; 
         if (factor > 1.0F)
           factor = 1.0F; 
         int color = ColorProvider.interpolate(color1, color2, factor);
         x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
         prevChar = _char;
       } 
     } 
   }
   
   public void applyWave(Matrix4f matrix, VertexConsumer consumer, String text, float size, float thickness, float spacing, float x, float y, float z, int color1, int color2) {
     float startX = x;
     int prevChar = -1;
     for (int i = 0; i < text.length(); i++) {
       int _char = text.charAt(i);
       MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
       if (glyph != null) {
         Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
         if (kerning != null)
           x += ((Float)kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0F))).floatValue() * size; 
         int color = ColorProvider.wave(color1, color2, ((x - startX) / size) * 0.5D);
         x += glyph.apply(matrix, consumer, size, x, y, z, color) + thickness + spacing;
         prevChar = _char;
       } 
     } 
   }
   
   public float getWidth(String text, float size) {
     int prevChar = -1;
     float width = 0.0F;
     for (int i = 0; i < text.length(); i++) {
       int _char = text.charAt(i);
       MsdfGlyph glyph = this.glyphs.get(Integer.valueOf(_char));
       if (glyph != null) {
         Map<Integer, Float> kerning = this.kernings.get(Integer.valueOf(prevChar));
         if (kerning != null)
           width += ((Float)kerning.getOrDefault(Integer.valueOf(_char), Float.valueOf(0.0F))).floatValue() * size; 
         width += glyph.getWidth(size);
         prevChar = _char;
       } 
     } 
     return width;
   }
   
   public String getName() {
     return this.name;
   }
   
   public FontData.AtlasData getAtlas() {
     return this.atlas;
   }
   
   public FontData.MetricsData getMetrics() {
     return this.metrics;
   }
   
   public static Builder builder() {
     return new Builder();
   }
   
   public static class Builder {
     private String name = "?";
     
     private Identifier dataIdentifer;
     
     private Identifier atlasIdentifier;
     
     public Builder name(String name) {
       this.name = name;
       return this;
     }
     
     public Builder data(String dataFileName) {
       this.dataIdentifer = Identifier.of("mre", "fonts/" + dataFileName + ".json");
       return this;
     }
     
     public Builder atlas(String atlasFileName) {
       this.atlasIdentifier = Identifier.of("mre", "fonts/" + atlasFileName + ".png");
       return this;
     }
     
     public MsdfFont build() {
       FontData data = (FontData)ResourceProvider.fromJsonToInstance(this.dataIdentifer, FontData.class);
       AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(this.atlasIdentifier);
       if (data == null)
         throw new RuntimeException("Failed to read font data file: " + this.dataIdentifer.toString() + "; Are you sure this is json file? Try to check the correctness of its syntax."); 
       RenderSystem.recordRenderCall(() -> texture.setFilter(true, false));
       float aWidth = data.atlas().width();
       float aHeight = data.atlas().height();
       Map<Integer, MsdfGlyph> glyphs = (Map<Integer, MsdfGlyph>)data.glyphs().stream().collect(Collectors.toMap(glyphData -> Integer.valueOf(glyphData.unicode()), glyphData -> new MsdfGlyph(glyphData, aWidth, aHeight)));
       Map<Integer, Map<Integer, Float>> kernings = new HashMap<>();
       if (data.kernings() != null)
         data.kernings().forEach(kerning -> {
               Map<Integer, Float> map = (Map<Integer, Float>)kernings.get(Integer.valueOf(kerning.leftChar()));
               if (map == null) {
                 map = new HashMap<>();
                 kernings.put(Integer.valueOf(kerning.leftChar()), map);
               } 
               map.put(Integer.valueOf(kerning.rightChar()), Float.valueOf(kerning.advance()));
             }); 
       return new MsdfFont(this.name, texture, data.atlas(), data.metrics(), glyphs, kernings);
     }
   }
 }


