package aethereal.render;

import net.minecraft.class_4588;
import org.joml.Matrix4f;
import platform.client.processors.draw.fonts.FontData;

public class MsdfGlyph {
   private final int a;
   private final float b;
   private final float c;
   private final float d;
   private final float e;
   private final float f;
   private final float g;
   private final float h;
   private final float i;

   public MsdfGlyph(FontData.GlyphData data, float atlasWidth, float atlasHeight) {
      this.a = data.unicode();
      this.f = data.advance();
      FontData.BoundsData atlasBounds = data.atlasBounds();
      if (atlasBounds != null) {
         this.b = atlasBounds.left() / atlasWidth;
         this.c = atlasBounds.right() / atlasWidth;
         this.d = 1.0F - atlasBounds.top() / atlasHeight;
         this.e = 1.0F - atlasBounds.bottom() / atlasHeight;
      } else {
         this.b = 0.0F;
         this.c = 0.0F;
         this.d = 0.0F;
         this.e = 0.0F;
      }

      FontData.BoundsData planeBounds = data.planeBounds();
      if (planeBounds != null) {
         this.h = planeBounds.right() - planeBounds.left();
         this.i = planeBounds.top() - planeBounds.bottom();
         this.g = planeBounds.top();
      } else {
         this.h = 0.0F;
         this.i = 0.0F;
         this.g = 0.0F;
      }
   }

   public float a(Matrix4f matrix, class_4588 consumer, float size, float x, float y, float z, int color) {
      float adjustedY = y - this.g * size;
      float scaledWidth = this.h * size;
      float scaledHeight = this.i * size;
      consumer.method_22918(matrix, x, adjustedY, z).method_22913(this.b, this.d).method_39415(color);
      consumer.method_22918(matrix, x, adjustedY + scaledHeight, z).method_22913(this.b, this.e).method_39415(color);
      consumer.method_22918(matrix, x + scaledWidth, adjustedY + scaledHeight, z).method_22913(this.c, this.e).method_39415(color);
      consumer.method_22918(matrix, x + scaledWidth, adjustedY, z).method_22913(this.c, this.d).method_39415(color);
      return this.f * size;
   }

   public float a(float size) {
      return this.f * size;
   }

   public float b(float size) {
      return this.h * size;
   }

   public float a() {
      return this.g;
   }

   public float b() {
      return this.i;
   }

   public int c() {
      return this.a;
   }

   public static final class a {
      private final char a;
      private final int b;

      public a(char c, int color) {
         this.a = c;
         this.b = color;
      }

      public char a() {
         return this.a;
      }

      public int b() {
         return this.b;
      }
   }
}
