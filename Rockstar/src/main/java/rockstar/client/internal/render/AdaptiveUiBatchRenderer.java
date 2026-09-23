package rockstar.client.internal.render;




import rockstar.client.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rockstar.client.compat.ShaderProgram;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AdaptiveUiBatchRenderer {
   private static final float internalField0205 = 0.0F;
   private static final float internalField0206 = 0.5F;
   private static final float internalField1048 = 0.0F;
   private final Map<AdaptiveUiBatchRenderer.InternalType0434, List<AdaptiveUiBatchRenderer.InternalType0106>> internalField0543 = new LinkedHashMap<>();
   private final Map<FontFamily, Map<AdaptiveUiBatchRenderer.InternalType0434, List<AdaptiveUiBatchRenderer.InternalType0107>>> internalField0544 = new LinkedHashMap<>();
   private final Map<AdaptiveUiBatchRenderer.InternalType0434, List<AdaptiveUiBatchRenderer.InternalType0435>> internalField1197 = new LinkedHashMap<>();
   private AdaptiveUiBatchRenderer.InternalType0434 internalField0223;
   private float internalField1047;
   private float internalField1049 = 3.0F;
   private float internalField1046 = 0.3F;
   private float internalField1456 = 0.42F;
   private int internalField0227 = -1;
   private int internalField0228 = -15856114;

   public AdaptiveUiBatchRenderer internalMethod02287(float localValue1) {
      this.internalField1047 = localValue1;
      return this;
   }

   public AdaptiveUiBatchRenderer internalMethod07251(float localValue1) {
      this.internalField1049 = localValue1;
      return this;
   }

   public AdaptiveUiBatchRenderer internalMethod04668(float localValue1, float localValue2) {
      this.internalField1046 = localValue1;
      this.internalField1456 = localValue2;
      return this;
   }

   public AdaptiveUiBatchRenderer internalMethod00514(int localValue1, int localValue2) {
      this.internalField0227 = localValue1;
      this.internalField0228 = localValue2;
      return this;
   }

   public AdaptiveUiBatchRenderer internalMethod04015(Matrix4f localValue1, float localValue2, float localValue3) {
      this.internalField0223 = internalMethod05982(localValue1, localValue2, localValue3);
      return this;
   }

   public AdaptiveUiBatchRenderer internalMethod00656() {
      this.internalField0223 = null;
      return this;
   }

   public AdaptiveUiBatchRenderer internalMethod00921(FontFamily localValue1, String localValue2, float localValue3, Matrix4f localValue4, float localValue5, float localValue6, float localValue7) {
      if (localValue1 != null && localValue2 != null && !localValue2.isEmpty() && !(localValue7 <= 0.0F)) {
         AdaptiveUiBatchRenderer.InternalType0434 localValue8 = this.internalMethod00713(
            localValue4, localValue5 + localValue1.internalMethod05670(localValue2, localValue3) / 2.0F, localValue6 + localValue1.internalMethod04986(localValue3) / 2.0F
         );
         this.internalField0544
            .computeIfAbsent(localValue1, localValue0 -> new LinkedHashMap<>())
            .computeIfAbsent(localValue8, localValue0 -> new ArrayList<>())
            .add(new AdaptiveUiBatchRenderer.InternalType0107(new Matrix4f(localValue4), localValue2, localValue3, localValue5, localValue6, localValue7));
         return this;
      } else {
         return this;
      }
   }

   public AdaptiveUiBatchRenderer internalMethod02384(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
      if (!(localValue4 <= 0.0F) && !(localValue5 <= 0.0F) && !(localValue6 <= 0.0F)) {
         this.internalField0543
            .computeIfAbsent(this.internalMethod00713(localValue1, localValue2 + localValue4 / 2.0F, localValue3 + localValue5 / 2.0F), localValue0 -> new ArrayList<>())
            .add(new AdaptiveUiBatchRenderer.InternalType0106(new Matrix4f(localValue1), localValue2, localValue3, localValue4, localValue5, localValue6));
         return this;
      } else {
         return this;
      }
   }

   public AdaptiveUiBatchRenderer internalMethod02846(String localValue1, Matrix4f localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
      Integer localValue7 = IconFontRegistry.internalMethod06164(localValue1);
      if (localValue7 != null && !(localValue5 <= 0.0F) && !(localValue6 <= 0.0F) && IconFontRegistry.internalMethod02915()) {
         this.internalField1197
            .computeIfAbsent(this.internalMethod00713(localValue2, localValue3 + localValue5 / 2.0F, localValue4 + localValue5 / 2.0F), localValue0 -> new ArrayList<>())
            .add(new AdaptiveUiBatchRenderer.InternalType0435(new Matrix4f(localValue2), localValue7, localValue3, localValue4, localValue5, localValue6));
         return this;
      } else {
         return this;
      }
   }

   private AdaptiveUiBatchRenderer.InternalType0434 internalMethod00713(Matrix4f localValue1, float localValue2, float localValue3) {
      return this.internalField0223 != null ? this.internalField0223 : internalMethod05982(localValue1, localValue2, localValue3);
   }

   private static AdaptiveUiBatchRenderer.InternalType0434 internalMethod05982(Matrix4f localValue0, float localValue1, float localValue2) {
      Vector3f localValue3 = localValue0.transformPosition(new Vector3f(localValue1, localValue2, 0.0F));
      return new AdaptiveUiBatchRenderer.InternalType0434(localValue3.x, localValue3.y);
   }

   public void internalMethod00733() {
      this.internalField0223 = null;
      if (!this.internalField0543.isEmpty() || !this.internalField0544.isEmpty() || !this.internalField1197.isEmpty()) {
         UiBatchRenderer.internalMethod02576();
         RenderPipeline.internalField0105.internalMethod00769(this.internalField1046, this.internalField1456, this.internalField1049);
         UiBatchRenderer.internalMethod08433();
         RenderSystem.disableCull();
         SlugFontRenderer.internalMethod06418(0, 2, 3);
         RenderSystem.setShaderTexture(1, RenderPipeline.internalField0105.internalMethod02779());
         ShaderProgram localValue1 = RenderPipeline.internalField0993.internalMethod01220();
         internalMethod02943(localValue1, "LightColor", this.internalField0227);
         internalMethod02943(localValue1, "DarkColor", this.internalField0228);
         localValue1.getUniform("Radius").set(this.internalField1047, this.internalField1047, this.internalField1047, this.internalField1047);
         localValue1.getUniform("RectSmoothness").set(0.5F);

         for (Entry localValue3 : this.internalField0543.entrySet()) {
            internalMethod03492(localValue1, (AdaptiveUiBatchRenderer.InternalType0434)localValue3.getKey());
            BufferBuilder localValue4 = internalMethod07065();
            this.internalMethod01441(localValue4, (List<AdaptiveUiBatchRenderer.InternalType0106>)localValue3.getValue());
            internalMethod07198(localValue4);
         }

         for (Entry localValue12 : this.internalField0544.entrySet()) {
            FontFamily localValue14 = (FontFamily)localValue12.getKey();
            localValue1.getUniform("Thickness").set(0.0F);

            for (Entry localValue6 : (Iterable<Entry>)(Iterable<?>)((Map)localValue12.getValue()).entrySet()) {
               internalMethod03492(localValue1, (AdaptiveUiBatchRenderer.InternalType0434)localValue6.getKey());
               BufferBuilder localValue7 = internalMethod07065();

               for (AdaptiveUiBatchRenderer.InternalType0107 localValue9 : (Iterable<AdaptiveUiBatchRenderer.InternalType0107>)(Iterable<?>)(List)localValue6.getValue()) {
                  localValue14.internalMethod02357(
                     localValue9.internalField0788,
                     localValue7,
                     localValue9.internalField0248,
                     localValue9.internalField0205,
                     localValue9.internalField0206,
                     localValue9.internalField1048,
                     0.0F,
                     internalMethod03230(localValue9.internalField1047)
                  );
               }

               internalMethod07198(localValue7);
            }
         }

         if (!this.internalField1197.isEmpty()) {
            FontFamily localValue11 = IconFontRegistry.internalMethod05006();
            if (localValue11 != null) {
               localValue1.getUniform("Thickness").set(0.0F);

               for (Entry localValue15 : this.internalField1197.entrySet()) {
                  internalMethod03492(localValue1, (AdaptiveUiBatchRenderer.InternalType0434)localValue15.getKey());
                  BufferBuilder localValue16 = internalMethod07065();

                  for (AdaptiveUiBatchRenderer.InternalType0435 localValue18 : (Iterable<AdaptiveUiBatchRenderer.InternalType0435>)(Iterable<?>)(List)localValue15.getValue()) {
                     localValue11.internalMethod05316(
                        localValue18.internalField0788,
                        localValue16,
                        localValue18.internalField0227,
                        localValue18.internalField0205,
                        localValue18.internalField0206,
                        localValue18.internalField1048,
                        internalMethod03230(localValue18.internalField1047)
                     );
                  }

                  internalMethod07198(localValue16);
               }
            }
         }

         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.setShaderTexture(1, 0);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         this.internalField0543.clear();
         this.internalField0544.clear();
         this.internalField1197.clear();
      }
   }

   private void internalMethod01441(BufferBuilder localValue1, List<AdaptiveUiBatchRenderer.InternalType0106> localValue2) {
      float localValue3 = 0.5F;
      float localValue4 = -localValue3 / 2.0F + localValue3 * 2.0F;
      float localValue5 = localValue3 / 2.0F + localValue3;

      for (AdaptiveUiBatchRenderer.InternalType0106 localValue7 : localValue2) {
         float localValue8 = localValue7.internalField0205 - localValue4 / 2.0F;
         float localValue9 = localValue7.internalField0206 - localValue5 / 2.0F;
         float localValue10 = localValue7.internalField1048 + localValue4;
         float localValue11 = localValue7.internalField1047 + localValue5;
         int localValue12 = internalMethod03230(localValue7.internalField1049);
         localValue1.vertex(localValue7.internalField0788, localValue8, localValue9, 0.0F).texture(localValue7.internalField1048, localValue7.internalField1047).color(localValue12).light(0);
         localValue1.vertex(localValue7.internalField0788, localValue8, localValue9 + localValue11, 0.0F).texture(localValue7.internalField1048, localValue7.internalField1047).color(localValue12).light(0);
         localValue1.vertex(localValue7.internalField0788, localValue8 + localValue10, localValue9 + localValue11, 0.0F).texture(localValue7.internalField1048, localValue7.internalField1047).color(localValue12).light(0);
         localValue1.vertex(localValue7.internalField0788, localValue8 + localValue10, localValue9, 0.0F).texture(localValue7.internalField1048, localValue7.internalField1047).color(localValue12).light(0);
      }
   }

   private static BufferBuilder internalMethod07065() {
      return Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
   }

   private static void internalMethod07198(BufferBuilder localValue0) {
      BuiltBuffer localValue1 = localValue0.endNullable();
      if (localValue1 != null) {
         BufferRenderer.drawWithGlobalProgram(localValue1);
      }
   }

   private static int internalMethod03230(float localValue0) {
      int localValue1 = Math.round(Math.min(1.0F, Math.max(0.0F, localValue0)) * 255.0F);
      return localValue1 << 24 | 16777215;
   }

   private static void internalMethod03492(ShaderProgram localValue0, AdaptiveUiBatchRenderer.InternalType0434 localValue1) {
      localValue0.getUniform("Anchor").set(localValue1.internalMethod01320(), localValue1.internalMethod01326());
   }

   private static void internalMethod02943(ShaderProgram localValue0, String localValue1, int localValue2) {
      localValue0.getUniform(localValue1).set((localValue2 >> 16 & 0xFF) / 255.0F, (localValue2 >> 8 & 0xFF) / 255.0F, (localValue2 & 0xFF) / 255.0F, (localValue2 >>> 24 & 0xFF) / 255.0F);
   }

   static final class InternalType0106 {
      final Matrix4f internalField0788;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final float internalField1049;

      InternalType0106(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
         this.internalField0788 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField1049 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0106[matrix=" + this.internalField0788 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", width=" + this.internalField1048 + ", height=" + this.internalField1047 + ", alpha=" + this.internalField1049 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AdaptiveUiBatchRenderer.InternalType0106 other = (AdaptiveUiBatchRenderer.InternalType0106) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049);
      }

      public Matrix4f internalMethod02075() {
         return this.internalField0788;
      }

      public float internalMethod05765() {
         return this.internalField0205;
      }

      public float internalMethod05768() {
         return this.internalField0206;
      }

      public float internalMethod08600() {
         return this.internalField1048;
      }

      public float internalMethod08601() {
         return this.internalField1047;
      }

      public float internalMethod08613() {
         return this.internalField1049;
      }
   }

   static final class InternalType0107 {
      final Matrix4f internalField0788;
      final String internalField0248;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;

      InternalType0107(Matrix4f localValue1, String localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
         this.internalField0788 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0205 = localValue3;
         this.internalField0206 = localValue4;
         this.internalField1048 = localValue5;
         this.internalField1047 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0107[matrix=" + this.internalField0788 + ", text=" + this.internalField0248 + ", size=" + this.internalField0205 + ", x=" + this.internalField0206 + ", y=" + this.internalField1048 + ", alpha=" + this.internalField1047 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AdaptiveUiBatchRenderer.InternalType0107 other = (AdaptiveUiBatchRenderer.InternalType0107) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public Matrix4f internalMethod06126() {
         return this.internalField0788;
      }

      public String internalMethod06547() {
         return this.internalField0248;
      }

      public float internalMethod07458() {
         return this.internalField0205;
      }

      public float internalMethod07460() {
         return this.internalField0206;
      }

      public float internalMethod08010() {
         return this.internalField1048;
      }

      public float internalMethod08012() {
         return this.internalField1047;
      }
   }

   static final class InternalType0434 {
      private final float internalField0205;
      private final float internalField0206;

      InternalType0434(float localValue1, float localValue2) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0434[x=" + this.internalField0205 + ", y=" + this.internalField0206 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AdaptiveUiBatchRenderer.InternalType0434 other = (AdaptiveUiBatchRenderer.InternalType0434) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206);
      }

      public float internalMethod01320() {
         return this.internalField0205;
      }

      public float internalMethod01326() {
         return this.internalField0206;
      }
   }

   static final class InternalType0435 {
      final Matrix4f internalField0788;
      final int internalField0227;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;

      InternalType0435(Matrix4f localValue1, int localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
         this.internalField0788 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0205 = localValue3;
         this.internalField0206 = localValue4;
         this.internalField1048 = localValue5;
         this.internalField1047 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0435[matrix=" + this.internalField0788 + ", codepoint=" + this.internalField0227 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", size=" + this.internalField1048 + ", alpha=" + this.internalField1047 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AdaptiveUiBatchRenderer.InternalType0435 other = (AdaptiveUiBatchRenderer.InternalType0435) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047);
      }

      public Matrix4f internalMethod03807() {
         return this.internalField0788;
      }

      public int internalMethod02939() {
         return this.internalField0227;
      }

      public float internalMethod02938() {
         return this.internalField0205;
      }

      public float internalMethod02941() {
         return this.internalField0206;
      }

      public float internalMethod07924() {
         return this.internalField1048;
      }

      public float internalMethod07926() {
         return this.internalField1047;
      }
   }
}
