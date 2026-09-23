package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.compat.ShaderProgram;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import pyrock.utility.render.ColorRGBA;

public class TextBatchRenderer {
   private static final float internalField0205 = 0.0F;
   private static final float internalField0206 = 0.5F;
   private final FontFamily internalField0450;
   private final float internalField1048;
   private final List<TextBatchRenderer.InternalType0003> internalField0416 = new ArrayList<>();
   private final List<TextBatchRenderer.InternalType0002> internalField0417 = new ArrayList<>();
   private final List<TextBatchRenderer.InternalType0497> internalField1145 = new ArrayList<>();
   private final List<TextBatchRenderer.InternalType0497> internalField1146 = new ArrayList<>();
   private boolean internalField0277;
   private final List<TextBatchRenderer.InternalType0003> internalField1148 = new ArrayList<>();
   private int internalField0227 = 0;
   private float internalField1047 = 0.0F;
   private float internalField1049 = 0.0F;

   public TextBatchRenderer(FontFamily localValue1, float localValue2) {
      this.internalField0450 = localValue1;
      this.internalField1048 = localValue2;
   }

   public void internalMethod00327(int localValue1, float localValue2, float localValue3) {
      this.internalField0227 = localValue1;
      this.internalField1047 = localValue2;
      this.internalField1049 = localValue3;
   }

   public void internalMethod04362(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      this.internalMethod02746(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue2, localValue4);
   }

   public void internalMethod02746(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6, float localValue7, float localValue8) {
      this.internalField0416.add(new TextBatchRenderer.InternalType0003(new Matrix4f(localValue1), localValue2, localValue3, localValue4, localValue5, localValue6.getRGB(), localValue7, localValue8));
   }

   public void internalMethod02790(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
      this.internalMethod05922(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue2);
   }

   public void internalMethod06644(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5) {
      this.internalField1148.add(new TextBatchRenderer.InternalType0003(new Matrix4f(localValue1), localValue2, localValue3, localValue4, localValue5, 0, localValue2, localValue4));
   }

   public void internalMethod05922(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7) {
      this.internalField0417.add(new TextBatchRenderer.InternalType0002(new Matrix4f(localValue1), localValue2, localValue3, localValue4, localValue5, localValue6, localValue7));
   }

   public void internalMethod00978(Matrix4f localValue1, String localValue2, float localValue3, float localValue4, float localValue5, float localValue6, int localValue7) {
      Matrix4f localValue8 = new Matrix4f(localValue1);
      String localValue9 = PostProcessRenderer.internalMethod03546(localValue2);
      this.internalField1145.add(new TextBatchRenderer.InternalType0497(localValue8, localValue9, localValue3, localValue4, localValue5, localValue6, localValue7));
      if (PostProcessRenderer.internalMethod08007()) {
         this.internalField1146
            .add(localValue9 == localValue2 ? this.internalField1145.getLast() : new TextBatchRenderer.InternalType0497(localValue8, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7));
         this.internalField0277 |= localValue9 != localValue2;
      }
   }

   public void internalMethod02261(Matrix4f localValue1, Text localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
      this.internalMethod02159(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue4);
   }

   public void internalMethod02159(Matrix4f localValue1, Text localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7) {
      Matrix4f localValue8 = new Matrix4f(localValue1);
      boolean localValue9 = PostProcessRenderer.internalMethod08007();
      float localValue10 = localValue4;
      float localValue11 = localValue7;

      for (FormattedTextProcessor.InternalType0470 localValue13 : FormattedTextProcessor.internalMethod03885(localValue2, ThemeColors.internalField1312.getRGB())) {
         String localValue14 = PostProcessRenderer.internalMethod03546(localValue13.internalField0248);
         this.internalField1145.add(new TextBatchRenderer.InternalType0497(localValue8, localValue14, localValue3, localValue10, localValue5, localValue6, localValue13.internalField0227));
         localValue10 += this.internalField0450.internalMethod05670(localValue14, localValue3);
         if (localValue9) {
            this.internalField1146.add(new TextBatchRenderer.InternalType0497(localValue8, localValue13.internalField0248, localValue3, localValue11, localValue5, localValue6, localValue13.internalField0227));
            localValue11 += this.internalField0450.internalMethod05670(localValue13.internalField0248, localValue3);
            this.internalField0277 = this.internalField0277 | localValue14 != localValue13.internalField0248;
         }
      }
   }

   public float internalMethod02068(Text localValue1, float localValue2) {
      return internalMethod06796(this.internalField0450, localValue1, localValue2);
   }

   public static float internalMethod06796(FontFamily localValue0, Text localValue1, float localValue2) {
      float localValue3 = 0.0F;

      for (FormattedTextProcessor.InternalType0470 localValue5 : FormattedTextProcessor.internalMethod03885(localValue1, ThemeColors.internalField1312.getRGB())) {
         localValue3 += localValue0.internalMethod05670(PostProcessRenderer.internalMethod03546(localValue5.internalField0248), localValue2);
      }

      return localValue3;
   }

   public void internalMethod03841() {
      if (!this.internalField0416.isEmpty() || !this.internalField0417.isEmpty() || !this.internalField1145.isEmpty()) {
         UiBatchRenderer.internalMethod02576();
         if (this.internalField0277) {
            PostProcessRenderer.internalMethod01602(
               this.internalMethod02654(), () -> this.internalMethod01452(this.internalField0416, this.internalField0417, this.internalField1146, true)
            );
         }

         this.internalMethod01452(this.internalField0416, this.internalField0417, this.internalField1145, false);
         this.internalField0416.clear();
         this.internalField0417.clear();
         this.internalField1145.clear();
         this.internalField1146.clear();
         this.internalField1148.clear();
         this.internalField0277 = false;
      }
   }

   private int[] internalMethod02654() {
      if (!this.internalField0277) {
         return null;
      } else {
         int[] localValue1 = null;

         for (TextBatchRenderer.InternalType0003 localValue3 : this.internalField0416) {
            localValue1 = PostProcessRenderer.internalMethod06487(
               localValue1,
               PostProcessRenderer.internalMethod04696(
                  localValue3.internalField0788,
                  localValue3.internalField0205 - 2.0F,
                  localValue3.internalField0206 - 2.0F,
                  localValue3.internalField0205 + localValue3.internalField1048 + 2.0F,
                  localValue3.internalField0206 + localValue3.internalField1047 + 2.0F
               )
            );
            localValue1 = PostProcessRenderer.internalMethod06487(
               localValue1,
               PostProcessRenderer.internalMethod04696(
                  localValue3.internalField0788,
                  localValue3.internalField1049 - 2.0F,
                  localValue3.internalField0206 - 2.0F,
                  localValue3.internalField1049 + localValue3.internalField1046 + 2.0F,
                  localValue3.internalField0206 + localValue3.internalField1047 + 2.0F
               )
            );
         }

         for (TextBatchRenderer.InternalType0002 localValue10 : this.internalField0417) {
            localValue1 = PostProcessRenderer.internalMethod06487(
               localValue1,
               PostProcessRenderer.internalMethod04696(
                  localValue10.internalField0788,
                  localValue10.internalField0205 - 2.0F,
                  localValue10.internalField0206 - 2.0F,
                  localValue10.internalField0205 + this.internalField1047 + 2.0F,
                  localValue10.internalField0206 + this.internalField1047 + 2.0F
               )
            );
            localValue1 = PostProcessRenderer.internalMethod06487(
               localValue1,
               PostProcessRenderer.internalMethod04696(
                  localValue10.internalField0788,
                  localValue10.internalField1046 - 2.0F,
                  localValue10.internalField0206 - 2.0F,
                  localValue10.internalField1046 + this.internalField1047 + 2.0F,
                  localValue10.internalField0206 + this.internalField1047 + 2.0F
               )
            );
         }

         for (TextBatchRenderer.InternalType0497 localValue11 : this.internalField1145) {
            localValue1 = PostProcessRenderer.internalMethod06487(localValue1, this.internalMethod00685(localValue11));
         }

         for (TextBatchRenderer.InternalType0497 localValue12 : this.internalField1146) {
            localValue1 = PostProcessRenderer.internalMethod06487(localValue1, this.internalMethod00685(localValue12));
         }

         for (TextBatchRenderer.InternalType0003 localValue13 : this.internalField1148) {
            localValue1 = PostProcessRenderer.internalMethod06487(
               localValue1,
               PostProcessRenderer.internalMethod04696(
                  localValue13.internalField0788,
                  localValue13.internalField0205 - 2.0F,
                  localValue13.internalField0206 - 2.0F,
                  localValue13.internalField0205 + localValue13.internalField1048 + 2.0F,
                  localValue13.internalField0206 + localValue13.internalField1047 + 2.0F
               )
            );
         }

         return localValue1;
      }
   }

   private int[] internalMethod00685(TextBatchRenderer.InternalType0497 localValue1) {
      return PostProcessRenderer.internalMethod04696(
         localValue1.internalField0788,
         localValue1.internalField0206 - 1.0F,
         localValue1.internalField1048 - localValue1.internalField0205 * 0.35F,
         localValue1.internalField0206 + this.internalField0450.internalMethod05670(localValue1.internalField0248, localValue1.internalField0205) + 1.0F,
         localValue1.internalField1048 + localValue1.internalField0205 * 1.45F
      );
   }

   private void internalMethod01452(
      List<TextBatchRenderer.InternalType0003> localValue1,
      List<TextBatchRenderer.InternalType0002> localValue2,
      List<TextBatchRenderer.InternalType0497> localValue3,
      boolean localValue4
   ) {
      if (!localValue1.isEmpty() || !localValue2.isEmpty() || !localValue3.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         SlugFontRenderer.internalMethod06418(0, 2, 3);
         RenderSystem.setShaderTexture(1, this.internalField0227);
         ShaderProgram localValue5 = RenderPipeline.internalField0996.internalMethod01220();
         localValue5.getUniform("Radius").set(this.internalField1048, this.internalField1048, this.internalField1048, this.internalField1048);
         localValue5.getUniform("RectSmoothness").set(0.5F);
         localValue5.getUniform("CornerSmoothness").set(2.0F);
         localValue5.getUniform("TextThickness").set(0.0F);
         localValue5.getUniform("HeadSize").set(this.internalField1047, this.internalField1047);
         localValue5.getUniform("HeadRadius").set(this.internalField1049, this.internalField1049, this.internalField1049, this.internalField1049);
         localValue5.getUniform("HeadSmoothness").set(0.5F);
         BufferBuilder localValue6 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
         float localValue7 = 0.5F;
         float localValue8 = -localValue7 / 2.0F + localValue7 * 2.0F;
         float localValue9 = localValue7 / 2.0F + localValue7;

         for (TextBatchRenderer.InternalType0003 localValue11 : localValue1) {
            float localValue12 = localValue4 ? localValue11.internalField1049 : localValue11.internalField0205;
            float localValue13 = localValue4 ? localValue11.internalField1046 : localValue11.internalField1048;
            float localValue14 = localValue12 - localValue8 / 2.0F;
            float localValue15 = localValue11.internalField0206 - localValue9 / 2.0F;
            float localValue16 = localValue13 + localValue8;
            float localValue17 = localValue11.internalField1047 + localValue9;
            localValue6.vertex(localValue11.internalField0788, localValue14, localValue15, 0.0F).texture(localValue13, localValue11.internalField1047).color(localValue11.internalField0227).light(0);
            localValue6.vertex(localValue11.internalField0788, localValue14, localValue15 + localValue17, 0.0F).texture(localValue13, localValue11.internalField1047).color(localValue11.internalField0227).light(0);
            localValue6.vertex(localValue11.internalField0788, localValue14 + localValue16, localValue15 + localValue17, 0.0F).texture(localValue13, localValue11.internalField1047).color(localValue11.internalField0227).light(0);
            localValue6.vertex(localValue11.internalField0788, localValue14 + localValue16, localValue15, 0.0F).texture(localValue13, localValue11.internalField1047).color(localValue11.internalField0227).light(0);
         }

         for (TextBatchRenderer.InternalType0002 localValue23 : localValue2) {
            float localValue25 = (localValue4 ? localValue23.internalField1046 : localValue23.internalField0205) - localValue8 / 2.0F;
            float localValue26 = localValue23.internalField0206 - localValue9 / 2.0F;
            float localValue27 = this.internalField1047 + localValue8;
            float localValue28 = this.internalField1047 + localValue9;
            float localValue29 = localValue23.internalField1048 - 2.0F;
            float localValue30 = localValue23.internalField1047 - 2.0F;
            float localValue18 = localValue23.internalField1048 + localValue23.internalField1049 - 2.0F;
            float localValue19 = localValue23.internalField1047 + localValue23.internalField1049 - 2.0F;
            localValue6.vertex(localValue23.internalField0788, localValue25, localValue26, 0.0F).texture(localValue29, localValue30).color(-1).light(0);
            localValue6.vertex(localValue23.internalField0788, localValue25, localValue26 + localValue28, 0.0F).texture(localValue29, localValue19).color(-1).light(0);
            localValue6.vertex(localValue23.internalField0788, localValue25 + localValue27, localValue26 + localValue28, 0.0F).texture(localValue18, localValue19).color(-1).light(0);
            localValue6.vertex(localValue23.internalField0788, localValue25 + localValue27, localValue26, 0.0F).texture(localValue18, localValue30).color(-1).light(0);
         }

         for (TextBatchRenderer.InternalType0497 localValue24 : localValue3) {
            this.internalField0450
               .internalMethod02357(
                  localValue24.internalField0788,
                  localValue6,
                  localValue24.internalField0248,
                  localValue24.internalField0205,
                  localValue24.internalField0206,
                  localValue24.internalField1048,
                  localValue24.internalField1047,
                  localValue24.internalField0227
               );
         }

         BuiltBuffer localValue22 = localValue6.endNullable();
         if (localValue22 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue22);
         }

         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.setShaderTexture(1, 0);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }
   }

   static final class InternalType0002 {
      final Matrix4f internalField0788;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final float internalField1049;
      final float internalField1046;

      InternalType0002(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7) {
         this.internalField0788 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField1049 = localValue6;
         this.internalField1046 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0002[matrix=" + this.internalField0788 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", u0=" + this.internalField1048 + ", v0=" + this.internalField1047 + ", uv=" + this.internalField1049 + ", realX=" + this.internalField1046 + "]";
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
         result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TextBatchRenderer.InternalType0002 other = (TextBatchRenderer.InternalType0002) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046);
      }

      public Matrix4f internalMethod00367() {
         return this.internalField0788;
      }

      public float internalMethod06113() {
         return this.internalField0205;
      }

      public float internalMethod06115() {
         return this.internalField0206;
      }

      public float internalMethod07669() {
         return this.internalField1048;
      }

      public float internalMethod07670() {
         return this.internalField1047;
      }

      public float internalMethod07677() {
         return this.internalField1049;
      }

      public float internalMethod07679() {
         return this.internalField1046;
      }
   }

   static final class InternalType0003 {
      final Matrix4f internalField0788;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final int internalField0227;
      final float internalField1049;
      final float internalField1046;

      InternalType0003(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, int localValue6, float localValue7, float localValue8) {
         this.internalField0788 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField0227 = localValue6;
         this.internalField1049 = localValue7;
         this.internalField1046 = localValue8;
      }

      @Override
      public final String toString() {
         return "InternalType0003[matrix=" + this.internalField0788 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", width=" + this.internalField1048 + ", height=" + this.internalField1047 + ", rgba=" + this.internalField0227 + ", realX=" + this.internalField1049 + ", realWidth=" + this.internalField1046 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TextBatchRenderer.InternalType0003 other = (TextBatchRenderer.InternalType0003) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046);
      }

      public Matrix4f internalMethod04442() {
         return this.internalField0788;
      }

      public float internalMethod02441() {
         return this.internalField0205;
      }

      public float internalMethod02477() {
         return this.internalField0206;
      }

      public float internalMethod08669() {
         return this.internalField1048;
      }

      public float internalMethod08671() {
         return this.internalField1047;
      }

      public int internalMethod02442() {
         return this.internalField0227;
      }

      public float internalMethod08692() {
         return this.internalField1049;
      }

      public float internalMethod08694() {
         return this.internalField1046;
      }
   }

   static final class InternalType0497 {
      final Matrix4f internalField0788;
      final String internalField0248;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final int internalField0227;

      InternalType0497(Matrix4f localValue1, String localValue2, float localValue3, float localValue4, float localValue5, float localValue6, int localValue7) {
         this.internalField0788 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0205 = localValue3;
         this.internalField0206 = localValue4;
         this.internalField1048 = localValue5;
         this.internalField1047 = localValue6;
         this.internalField0227 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0497[matrix=" + this.internalField0788 + ", text=" + this.internalField0248 + ", size=" + this.internalField0205 + ", x=" + this.internalField0206 + ", y=" + this.internalField1048 + ", z=" + this.internalField1047 + ", color=" + this.internalField0227 + "]";
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
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TextBatchRenderer.InternalType0497 other = (TextBatchRenderer.InternalType0497) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Matrix4f internalMethod04994() {
         return this.internalField0788;
      }

      public String internalMethod05002() {
         return this.internalField0248;
      }

      public float internalMethod01648() {
         return this.internalField0205;
      }

      public float internalMethod01654() {
         return this.internalField0206;
      }

      public float internalMethod08839() {
         return this.internalField1048;
      }

      public float internalMethod08840() {
         return this.internalField1047;
      }

      public int internalMethod01649() {
         return this.internalField0227;
      }
   }
}
