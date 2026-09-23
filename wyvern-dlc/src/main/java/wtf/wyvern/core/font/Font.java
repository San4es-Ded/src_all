package wtf.wyvern.core.font;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import net.minecraft.text.Text;

public class Font {
   private static final int MAX_CACHED_WIDTHS = 512;
   private MsdfFont font;
   private float size;
   private final Map<String, Float> widthCache = new HashMap<>(128);

   public float height() {
      return this.size * 0.7F;
   }

   public float width(String text) {
      Float cached = this.widthCache.get(text);
      if (cached != null) {
         return cached;
      }

      float width = this.font.measureWidth(text, this.size);
      if (this.widthCache.size() >= MAX_CACHED_WIDTHS) {
         this.widthCache.clear();
      }
      this.widthCache.put(text, width);
      return width;
   }

   public float width(Text text) {
      return this.width(text.getString());
   }

   @Generated
   public MsdfFont getFont() {
      return this.font;
   }

   @Generated
   public float getSize() {
      return this.size;
   }

   @Generated
   public Font(MsdfFont font, float size) {
      this.font = font;
      this.size = size;
   }
}
