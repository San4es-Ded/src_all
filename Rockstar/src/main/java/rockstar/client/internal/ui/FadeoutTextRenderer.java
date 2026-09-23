package rockstar.client.internal.ui;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import rockstar.client.compat.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.text.Text;
import org.joml.Matrix4f;

public class FadeoutTextRenderer {
   private static final MinecraftClient internalField0149 = MinecraftClient.getInstance();
   private final FontFamily internalField0450;
   private final FontFamily internalField0449;
   private final List<FadeoutTextRenderer.InternalType0455> internalField0416 = new ArrayList<>();
   private final List<FadeoutTextRenderer.InternalType0455> internalField0417 = new ArrayList<>();
   private boolean internalField0277;
   private DrawContext internalField0722;

   public FadeoutTextRenderer(FontFamily localValue1, FontFamily localValue2) {
      this.internalField0450 = localValue1;
      this.internalField0449 = localValue2;
   }

   public void internalMethod01439(DrawContext localValue1) {
      this.internalField0722 = localValue1;
   }

   public void internalMethod06049(Matrix4f localValue1, String localValue2, float localValue3, float localValue4, float localValue5, float localValue6, int localValue7) {
      Matrix4f localValue8 = new Matrix4f(localValue1);
      String localValue9 = PostProcessRenderer.internalMethod03546(localValue2);
      this.internalField0416.add(new FadeoutTextRenderer.InternalType0455(localValue8, localValue9, localValue3, localValue4, localValue5, localValue6, localValue7));
      if (PostProcessRenderer.internalMethod08007()) {
         this.internalField0417
            .add(localValue9 == localValue2 ? this.internalField0416.getLast() : new FadeoutTextRenderer.InternalType0455(localValue8, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7));
         this.internalField0277 |= localValue9 != localValue2;
      }
   }

   public void internalMethod05677(Matrix4f localValue1, Text localValue2, float localValue3, float localValue4, float localValue5, float localValue6) {
      int localValue7 = ThemeColors.internalField1312.getRGB();
      List localValue8 = FormattedTextProcessor.internalMethod03885(localValue2, localValue7);
      boolean localValue9 = PostProcessRenderer.internalMethod08007();
      float localValue10 = localValue4;
      float localValue11 = localValue4;
      Matrix4f localValue12 = new Matrix4f(localValue1);

      for (FormattedTextProcessor.InternalType0470 localValue14 : (Iterable<FormattedTextProcessor.InternalType0470>)(Iterable<?>)localValue8) {
         String localValue15 = PostProcessRenderer.internalMethod03546(localValue14.internalField0248);
         this.internalField0416.add(new FadeoutTextRenderer.InternalType0455(localValue12, localValue15, localValue3, localValue10, localValue5, localValue6, localValue14.internalField0227));
         localValue10 += this.internalMethod03339(localValue15, localValue3);
         if (localValue9) {
            this.internalField0417.add(new FadeoutTextRenderer.InternalType0455(localValue12, localValue14.internalField0248, localValue3, localValue11, localValue5, localValue6, localValue14.internalField0227));
            localValue11 += this.internalMethod03339(localValue14.internalField0248, localValue3);
            this.internalField0277 = this.internalField0277 | localValue15 != localValue14.internalField0248;
         }
      }
   }

   public void internalMethod05430() {
      if (!this.internalField0416.isEmpty()) {
         if (this.internalField0277) {
            PostProcessRenderer.internalMethod01602(this.internalMethod01660(), () -> this.internalMethod07042(this.internalField0417));
         }

         this.internalMethod07042(this.internalField0416);
         this.internalField0416.clear();
         this.internalField0417.clear();
         this.internalField0277 = false;
      }
   }

   private int[] internalMethod01660() {
      int localValue1 = -1;

      for (int localValue2 = 0; localValue2 < this.internalField0417.size() && localValue1 < 0; localValue2++) {
         FadeoutTextRenderer.InternalType0455 localValue3 = localValue2 < this.internalField0416.size() ? this.internalField0416.get(localValue2) : this.internalField0417.get(localValue2);
         if (!this.internalField0417.get(localValue2).internalField0248.equals(localValue3.internalField0248)) {
            localValue1 = localValue2;
         }
      }

      if (localValue1 < 0) {
         return null;
      } else {
         int[] localValue4 = null;

         for (int localValue5 = localValue1; localValue5 < this.internalField0417.size(); localValue5++) {
            localValue4 = PostProcessRenderer.internalMethod06487(localValue4, this.internalMethod02523(this.internalField0417.get(localValue5)));
            if (localValue5 < this.internalField0416.size()) {
               localValue4 = PostProcessRenderer.internalMethod06487(localValue4, this.internalMethod02523(this.internalField0416.get(localValue5)));
            }
         }

         return localValue4;
      }
   }

   private int[] internalMethod02523(FadeoutTextRenderer.InternalType0455 localValue1) {
      return PostProcessRenderer.internalMethod04696(
         localValue1.internalField0788,
         localValue1.internalField0206 - 1.0F,
         localValue1.internalField1048 - localValue1.internalField0205 * 0.35F,
         localValue1.internalField0206 + this.internalMethod03339(localValue1.internalField0248, localValue1.internalField0205) + 1.0F,
         localValue1.internalField1048 + localValue1.internalField0205 * 1.45F
      );
   }

   private void internalMethod07042(List<FadeoutTextRenderer.InternalType0455> localValue1) {
      float localValue2 = 0.0F;
      float localValue3 = 0.5F;
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      ShaderProgram localValue4 = SlugFontRenderer.internalMethod07504(localValue2);
      localValue4.getUniform("EnableFadeout").set(0);
      BufferBuilder localValue5 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);

      for (FadeoutTextRenderer.InternalType0455 localValue7 : localValue1) {
         this.internalField0450
            .internalMethod02357(
               localValue7.internalField0788, localValue5, localValue7.internalField0248, localValue7.internalField0205, localValue7.internalField0206, localValue7.internalField1048, localValue7.internalField1047, localValue7.internalField0227
            );
      }

      BuiltBuffer localValue8 = localValue5.endNullable();
      if (localValue8 != null) {
         BufferRenderer.drawWithGlobalProgram(localValue8);
      }

      SlugFontRenderer.internalMethod04918();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      if (this.internalField0722 != null) {
         this.internalMethod06718(localValue1);
      }
   }

   private void internalMethod06718(List<FadeoutTextRenderer.InternalType0455> localValue1) {
      for (FadeoutTextRenderer.InternalType0455 localValue3 : localValue1) {
         float localValue4 = localValue3.internalField0205 / 9.0F;
         this.internalField0450
            .internalMethod07006(
               localValue3.internalField0248, localValue3.internalField0205, localValue3.internalField0206, localValue3.internalField1048, this.internalField0449, (localValue3x, localValue4x, localValue5, localValue6, localValue7) -> {
                  if (localValue4x == null) {
                     this.internalField0722.getMatrices().pushMatrix();
                     GuiMatrixCompat.multiply(this.internalField0722.getMatrices(), localValue3.internalField0788);
                     this.internalField0722.getMatrices().scale(localValue4, localValue4);
                     float localValue8 = localValue6 / localValue4;
                     float localValue9 = (localValue3.internalField1048 + localValue3.internalField0205 * 0.15F) / localValue4 - 0.5F;
                     this.internalField0722.drawText(internalField0149.textRenderer, String.valueOf(localValue5), (int)localValue8, (int)localValue9, localValue3.internalField0227, false);
                     this.internalField0722.getMatrices().popMatrix();
                  }
               }
            );
      }
   }

   public float internalMethod01203(String localValue1, float localValue2) {
      return internalMethod01776(this.internalField0450, this.internalField0449, localValue1, localValue2);
   }

   public float internalMethod06953(Text localValue1, float localValue2) {
      return internalMethod01776(this.internalField0450, this.internalField0449, localValue1.getString(), localValue2);
   }

   private float internalMethod03339(String localValue1, float localValue2) {
      return this.internalField0450.internalMethod05616(localValue1, localValue2, this.internalField0449);
   }

   public static float internalMethod01776(FontFamily localValue0, FontFamily localValue1, String localValue2, float localValue3) {
      return localValue0.internalMethod05616(PostProcessRenderer.internalMethod03546(localValue2), localValue3, localValue1);
   }

   static final class InternalType0455 {
      final Matrix4f internalField0788;
      final String internalField0248;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final int internalField0227;

      InternalType0455(Matrix4f localValue1, String localValue2, float localValue3, float localValue4, float localValue5, float localValue6, int localValue7) {
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
         return "InternalType0455[matrix=" + this.internalField0788 + ", text=" + this.internalField0248 + ", size=" + this.internalField0205 + ", x=" + this.internalField0206 + ", y=" + this.internalField1048 + ", z=" + this.internalField1047 + ", color=" + this.internalField0227 + "]";
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
         FadeoutTextRenderer.InternalType0455 other = (FadeoutTextRenderer.InternalType0455) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Matrix4f internalMethod04456() {
         return this.internalField0788;
      }

      public String internalMethod06760() {
         return this.internalField0248;
      }

      public float internalMethod05707() {
         return this.internalField0205;
      }

      public float internalMethod05711() {
         return this.internalField0206;
      }

      public float internalMethod08223() {
         return this.internalField1048;
      }

      public float internalMethod08224() {
         return this.internalField1047;
      }

      public int internalMethod05708() {
         return this.internalField0227;
      }
   }
}
