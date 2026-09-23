package rockstar.client.render;


import rockstar.client.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public final class FontFamily {
   public static final float internalField0205 = 0.05F;
   public static final float internalField0206 = 0.0F;
   private static final FontRenderContext internalField0099 = new FontRenderContext(null, false, true);
   private static volatile FontFamily internalField0450;
   private final String internalField0248;
   private final Identifier internalField0354;
   private final Map<Integer, FontGlyphGeometry> internalField0543 = new ConcurrentHashMap<>();
   private Font internalField0081;
   private float internalField1048 = 0.7F;
   private float internalField1047 = 0.75F;
   private float internalField1049 = -0.25F;
   private float internalField1046 = 1.0F;
   private final ConcurrentHashMap<FontFamily.InternalType0233, Float> internalField0155 = new ConcurrentHashMap<>();
   private final ConcurrentHashMap<Integer, SizedFont> internalField0154 = new ConcurrentHashMap<>();

   public static float internalMethod04944(float localValue0) {
      return 0.025F * localValue0;
   }

   private FontFamily(String localValue1, Identifier localValue2) {
      this.internalField0248 = localValue1;
      this.internalField0354 = localValue2;
   }

   public static FontFamily internalMethod06934() {
      FontFamily localValue0 = internalField0450;
      if (localValue0 != null) {
         return localValue0;
      } else {
         synchronized (FontFamily.class) {
            if (internalField0450 == null) {
               FontFamily localValue2 = new FontFamily("fallback", null);
               localValue2.internalField0081 = internalMethod06236("Segoe UI", "Helvetica Neue", "DejaVu Sans", "Arial", "SansSerif");
               localValue2.internalMethod08622();
               internalField0450 = localValue2;
            }

            return internalField0450;
         }
      }
   }

   private static Font internalMethod06236(String... localValue0) {
      for (String localValue4 : localValue0) {
         Font localValue5 = new Font(localValue4, 0, 1).deriveFont(2048.0F);
         if (localValue5.canDisplay('\u1d00') || localValue4.equals("SansSerif")) {
            return localValue5;
         }
      }

      return new Font("SansSerif", 0, 1).deriveFont(2048.0F);
   }

   public synchronized void internalMethod01612() {
      this.internalField0543.clear();
      this.internalField0155.clear();
      if (this.internalField0354 != null) {
         try (InputStream localValue1 = MinecraftClient.getInstance().getResourceManager().open(this.internalField0354)) {
            this.internalField0081 = Font.createFont(0, localValue1).deriveFont(2048.0F);
         } catch (Exception localValue6) {
            RockstarClient.internalField0572
               .error(
                  "[slug] \u043d\u0435 \u0447\u0438\u0442\u0430\u0435\u0442\u0441\u044f \u0448\u0440\u0438\u0444\u0442 {}: {}",
                  this.internalField0354,
                  localValue6.toString()
               );
            this.internalField0081 = internalMethod06934().internalField0081;
         }

         this.internalMethod08622();
      }
   }

   private void internalMethod08620() {
      if (this.internalField0081 == null && this.internalField0354 != null) {
         this.internalMethod01612();
      }
   }

   private void internalMethod08622() {
      LineMetrics localValue1 = this.internalField0081.getLineMetrics("Hg", internalField0099);
      this.internalField1047 = localValue1.getAscent() / 2048.0F;
      this.internalField1049 = -localValue1.getDescent() / 2048.0F;
      this.internalField1046 = localValue1.getHeight() / 2048.0F;
      Rectangle2D localValue2 = this.internalField0081.createGlyphVector(internalField0099, "H").getOutline().getBounds2D();
      this.internalField1048 = localValue2.getHeight() > 0.0 ? (float)(-localValue2.getMinY() / 2048.0) : Math.max(0.1F, this.internalField1047 * 0.72F);
   }

   public FontGlyphGeometry internalMethod01567(int localValue1) {
      FontGlyphGeometry localValue2 = this.internalField0543.get(localValue1);
      if (localValue2 != null) {
         return localValue2;
      } else {
         this.internalMethod08620();
         if (this.internalField0081 != null && this.internalField0081.canDisplay(localValue1)) {
            GlyphMesh localValue3 = GlyphMesh.internalMethod00927(this.internalField0081, internalField0099, localValue1);
            int localValue4 = localValue3.internalMethod01752() ? -1 : FontGlyphAtlas.internalMethod05614().internalMethod02482(GlyphSpanIndex.internalMethod00809(localValue3));
            FontGlyphGeometry localValue5 = new FontGlyphGeometry(localValue1, localValue4, localValue3.internalField1046, localValue3.internalField0206, localValue3.internalField1048, localValue3.internalField1047, localValue3.internalField1049);
            FontGlyphGeometry localValue6 = this.internalField0543.putIfAbsent(localValue1, localValue5);
            return localValue6 != null ? localValue6 : localValue5;
         } else {
            return null;
         }
      }
   }

   public FontGlyphGeometry internalMethod02359(int localValue1) {
      FontGlyphGeometry localValue2 = this.internalMethod01567(localValue1);
      if (localValue2 != null) {
         return localValue2;
      } else {
         FontFamily localValue3 = internalMethod06934();
         return localValue3 == this ? null : localValue3.internalMethod01567(localValue1);
      }
   }

   public boolean internalMethod04943(char localValue1) {
      return this.internalMethod02359(localValue1) != null;
   }

   public boolean internalMethod05316(Matrix4f localValue1, VertexConsumer localValue2, int localValue3, float localValue4, float localValue5, float localValue6, int localValue7) {
      FontGlyphGeometry localValue8 = this.internalMethod02359(localValue3);
      if (localValue8 == null) {
         return false;
      } else {
         localValue8.internalMethod01939(localValue1, localValue2, localValue4, localValue5, localValue6, localValue7);
         return true;
      }
   }

   public void internalMethod07043(String localValue1) {
      for (int localValue2 = 0; localValue2 < localValue1.length(); localValue2++) {
         this.internalMethod02359(localValue1.charAt(localValue2));
      }
   }

   public float internalMethod01611() {
      this.internalMethod08620();
      return this.internalField1048;
   }

   public float internalMethod01616() {
      this.internalMethod08620();
      return this.internalField1047;
   }

   public float internalMethod08619() {
      this.internalMethod08620();
      return this.internalField1049;
   }

   public float internalMethod08621() {
      this.internalMethod08620();
      return this.internalField1046;
   }

   public float internalMethod04986(float localValue1) {
      return this.internalMethod01611() * localValue1;
   }

   public float internalMethod08067(float localValue1) {
      return this.internalMethod08621() * localValue1;
   }

   public float internalMethod01958(float localValue1, float localValue2) {
      return localValue1 + this.internalMethod01611() * localValue2;
   }

   public static String internalMethod07308(String localValue0) {
      return localValue0;
   }

   private float internalMethod06854(String localValue1, float localValue2, float localValue3, FontFamily.InternalType0478 localValue4) {
      float localValue5 = internalMethod04944(localValue2);
      float localValue6 = localValue3;
      boolean localValue7 = false;

      for (int localValue8 = 0; localValue8 < localValue1.length(); localValue8++) {
         char localValue9 = localValue1.charAt(localValue8);
         if (localValue7) {
            localValue7 = false;
         } else if (localValue9 == 167) {
            localValue7 = true;
         } else {
            FontGlyphGeometry localValue10 = this.internalMethod02359(localValue9);
            if (localValue10 != null) {
               if (localValue4 != null) {
                  localValue4.place(localValue10, localValue6);
               }

               localValue6 += localValue10.internalMethod03042() * localValue2 + localValue5;
            }
         }
      }

      return localValue6;
   }

   public void internalMethod02357(Matrix4f localValue1, VertexConsumer localValue2, String localValue3, float localValue4, float localValue5, float localValue6, float localValue7, int localValue8) {
      this.internalMethod05024(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue8, 1.0F);
   }

   public void internalMethod05024(Matrix4f localValue1, VertexConsumer localValue2, String localValue3, float localValue4, float localValue5, float localValue6, float localValue7, int localValue8, float localValue9) {
      float localValue10 = this.internalMethod01958(localValue6, localValue4);
      this.internalMethod06854(localValue3, localValue4, localValue5, (localValue7x, localValue8x) -> localValue7x.internalMethod03374(localValue1, localValue2, localValue4, localValue8x, localValue10, localValue7, localValue8, localValue9));
   }

   public void internalMethod03437(String localValue1, float localValue2, float localValue3, float localValue4, float localValue5, int localValue6, FontFamily.InternalType0232 localValue7) {
      float localValue8 = this.internalMethod01958(localValue4, localValue2);
      this.internalMethod06854(localValue1, localValue2, localValue3, (localValue5x, localValue6x) -> localValue7.internalMethod02648(localValue5x, localValue2, localValue6x, localValue8, localValue5, localValue6));
   }

   public void internalMethod07006(String localValue1, float localValue2, float localValue3, float localValue4, FontFamily localValue5, FontFamily.InternalType0477 localValue6) {
      float localValue7 = this.internalMethod01958(localValue4, localValue2);
      float localValue8 = internalMethod04944(localValue2);
      float localValue9 = localValue3;
      boolean localValue10 = false;

      for (int localValue11 = 0; localValue11 < localValue1.length(); localValue11++) {
         char localValue12 = localValue1.charAt(localValue11);
         if (localValue10) {
            localValue10 = false;
         } else if (localValue12 == 167) {
            localValue10 = true;
         } else {
            FontGlyphGeometry localValue13 = this.internalMethod01567(localValue12);
            FontFamily localValue14 = this;
            if (localValue13 == null) {
               localValue14 = internalMethod06934();
               localValue13 = localValue14.internalMethod01567(localValue12);
            }

            localValue6.visit(localValue14, localValue13, localValue12, localValue9, localValue7);
            if (localValue13 != null) {
               localValue9 += localValue13.internalMethod03042() * localValue2 + localValue8;
            }
         }
      }
   }

   public float internalMethod05616(String localValue1, float localValue2, FontFamily localValue3) {
      return this.internalMethod05670(localValue1, localValue2);
   }

   public void internalMethod02416(
      Matrix4f localValue1, VertexConsumer localValue2, String localValue3, float localValue4, float localValue5, float localValue6, float localValue7, int localValue8, FontFamily localValue9, boolean localValue10
   ) {
      if (!localValue10) {
         this.internalMethod02357(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue8);
      }
   }

   public float internalMethod05670(@Nullable String localValue1, float localValue2) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         FontFamily.InternalType0233 localValue3 = new FontFamily.InternalType0233(localValue1, localValue2);
         Float localValue4 = this.internalField0155.get(localValue3);
         if (localValue4 != null) {
            return localValue4;
         } else {
            float localValue5 = this.internalMethod06854(localValue1, localValue2, 0.0F, null);
            if (this.internalField0155.size() >= 4096) {
               this.internalField0155.clear();
            }

            this.internalField0155.put(localValue3, localValue5);
            return localValue5;
         }
      } else {
         return 0.0F;
      }
   }

   public void internalMethod01617() {
      this.internalField0155.clear();
   }

   public float internalMethod04675(Text localValue1, float localValue2) {
      return this.internalMethod05670(localValue1.getString(), localValue2);
   }

   public float internalMethod01957(char localValue1, float localValue2) {
      FontGlyphGeometry localValue3 = this.internalMethod02359(localValue1);
      return localValue3 == null ? 0.0F : localValue3.internalMethod03042() * localValue2 + internalMethod04944(localValue2);
   }

   public SizedFont internalMethod01432(float localValue1) {
      return this.internalField0154.computeIfAbsent(Float.floatToIntBits(localValue1), localValue2 -> new SizedFont(this, localValue1));
   }

   public static FontFamily internalMethod02606(String localValue0, String localValue1) {
      return new FontFamily(localValue0, Identifier.of(RockstarClient.internalField1077, "fonts/" + localValue1 + ".otf"));
   }

   public static FontFamily internalMethod05676(String localValue0) {
      return new FontFamily(localValue0, null);
   }

   public FontGlyphGeometry internalMethod01906(int localValue1, GlyphMesh localValue2) {
      int localValue3 = localValue2.internalMethod01752() ? -1 : FontGlyphAtlas.internalMethod05614().internalMethod02482(GlyphSpanIndex.internalMethod00809(localValue2));
      FontGlyphGeometry localValue4 = new FontGlyphGeometry(localValue1, localValue3, localValue2.internalField1046, localValue2.internalField0206, localValue2.internalField1048, localValue2.internalField1047, localValue2.internalField1049);
      this.internalField0543.put(localValue1, localValue4);
      return localValue4;
   }

   public static FontFamily internalMethod00176(String localValue0) {
      FontFamily localValue1 = new FontFamily(localValue0, null);
      localValue1.internalField0081 = internalMethod06934().internalField0081;
      localValue1.internalMethod08622();
      return localValue1;
   }

   public synchronized void internalMethod00602(Font localValue1) {
      this.internalField0543.clear();
      this.internalField0155.clear();
      this.internalField0081 = localValue1.getSize2D() == 2048.0F ? localValue1 : localValue1.deriveFont(2048.0F);
      this.internalMethod08622();
   }

   public static FontFamily internalMethod04605(String localValue0, Font localValue1) {
      FontFamily localValue2 = new FontFamily(localValue0, null);
      localValue2.internalField0081 = localValue1.getSize2D() == 2048.0F ? localValue1 : localValue1.deriveFont(2048.0F);
      localValue2.internalMethod08622();
      return localValue2;
   }

   @Generated
   public String internalMethod05894() {
      return this.internalField0248;
   }

   @FunctionalInterface
   public interface InternalType0232 {
      void internalMethod02648(FontGlyphGeometry localValue1, float localValue2, float localValue3, float localValue4, float localValue5, int localValue6);
   }

   static final class InternalType0233 {
      private final String internalField0248;
      private final float internalField0205;

      InternalType0233(String localValue1, float localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0205 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0233[text=" + this.internalField0248 + ", size=" + this.internalField0205 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         FontFamily.InternalType0233 other = (FontFamily.InternalType0233) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205);
      }

      public String internalMethod03276() {
         return this.internalField0248;
      }

      public float internalMethod03709() {
         return this.internalField0205;
      }
   }

   @FunctionalInterface
   public interface InternalType0477 {
      void visit(FontFamily localValue1, FontGlyphGeometry localValue2, char localValue3, float localValue4, float localValue5);
   }

   @FunctionalInterface
   interface InternalType0478 {
      void place(FontGlyphGeometry localValue1, float localValue2);
   }
}
