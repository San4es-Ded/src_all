package rockstar.client.render;


import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import rockstar.client.compat.RenderSystem;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rockstar.client.compat.ShaderProgram;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import org.joml.Matrix4f;
import pyrock.utility.render.ColorRGBA;

public final class UiBatchRenderer implements AutoCloseable {
   private static UiBatchRenderer internalField0200;
   private static boolean internalField0276;
   private static final LegacyFontRenderer internalField0202 = new LegacyFontRenderer();
   public static boolean internalField0277 = false;
   private final UiBatchRenderer internalField0199;
   private final LegacyFontRenderer internalField0201;
   private final List<UiBatchRenderer.InternalType0416> internalField0416 = new ArrayList<>();
   private final Map<UiBatchRenderer.InternalType0414, List<UiBatchRenderer.InternalType0415>> internalField0543 = new LinkedHashMap<>();
   private final Map<UiBatchRenderer.InternalType0414, List<UiBatchRenderer.InternalType0415>> internalField0544 = new LinkedHashMap<>();
   private final Map<FontFamily, List<UiBatchRenderer.InternalType0417>> internalField1197 = new LinkedHashMap<>();
   private final Map<FontFamily, List<UiBatchRenderer.InternalType0432>> internalField1196 = new LinkedHashMap<>();
   private final Map<UiBatchRenderer.InternalType0472, List<UiBatchRenderer.InternalType0473>> internalField1195 = new LinkedHashMap<>();
   private final List<UiBatchRenderer.InternalType0433> internalField0417 = new ArrayList<>();
   private int internalField0227;

   private UiBatchRenderer(boolean localValue1) {
      this.internalField0199 = internalField0200;
      if (localValue1) {
         LegacyFontRenderer localValue2 = internalField0200 != null && internalField0200.internalField0201 == internalField0202
            ? new LegacyFontRenderer()
            : internalField0202;
         this.internalField0201 = localValue2.internalMethod03877() ? localValue2 : null;
         if (this.internalField0201 != null) {
            this.internalField0201.internalMethod03871();
         }
      } else {
         this.internalField0201 = null;
      }

      internalField0200 = this;
   }

   public static UiBatchRenderer internalMethod03756() {
      return new UiBatchRenderer(false);
   }

   public static UiBatchRenderer internalMethod04455() {
      return new UiBatchRenderer(true);
   }

   public static UiBatchRenderer internalMethod08317() {
      return internalField0200;
   }

   public static void internalMethod02576() {
      if (internalField0200 != null) {
         boolean localValue0 = internalField0200.internalField0201 != null;
         internalField0200.internalMethod08442();
         if (localValue0) {
            ScissorStack.internalMethod07646();
         }
      }
   }

   public static void internalMethod02580() {
      if (internalField0200 != null) {
         if (internalField0200.internalField0201 != null) {
            internalField0200.internalMethod09898();
         } else {
            internalField0200.internalMethod08442();
         }
      }
   }

   public static boolean internalMethod02577() {
      return internalField0200 != null && internalField0200.internalField0201 != null;
   }

   public static boolean internalMethod02581() {
      return internalField0200 != null && !internalField0200.internalMethod08443();
   }

   public boolean internalMethod08432() {
      return this.internalField0201 != null;
   }

   public void internalMethod02386(float localValue1, float localValue2, float localValue3, float localValue4) {
      if (this.internalField0201 != null) {
         this.internalField0201.internalMethod00882(localValue1, localValue2, localValue3, localValue4);
      }
   }

   public void internalMethod08431() {
      if (this.internalField0201 != null) {
         this.internalField0201.internalMethod03876();
      }
   }

   public static void internalMethod08433() {
      RenderSystem.enableBlend();
      if (internalField0277) {
         RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA);
      } else {
         RenderSystem.defaultBlendFunc();
      }
   }

   public static boolean internalMethod08434() {
      return internalField0276;
   }

   public void internalMethod04843(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, ColorRGBA localValue6) {
      if (!internalMethod05505(localValue4, localValue5) && !internalMethod03602(localValue6)) {
         int localValue7 = this.internalMethod03933(localValue6.getRGB());
         if ((localValue7 >>> 24 & 0xFF) != 0) {
            this.internalMethod09893();
            this.internalField0416.add(new UiBatchRenderer.InternalType0416(new Matrix4f(localValue1), localValue2, localValue3, localValue4, localValue5, localValue7));
         }
      }
   }

   public void internalMethod03494(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, ColorRGBA localValue7) {
      this.internalMethod00451(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, 0.5F, 2.0F, localValue7);
   }

   public void internalMethod00028(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, CornerRadii localValue7, ColorRGBA localValue8) {
      this.internalMethod00451(localValue1, localValue2, localValue3, localValue4, localValue5, internalMethod03310(localValue7, localValue6 / 2.0F), 0.5F, localValue6, localValue8);
   }

   public void internalMethod00020(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, CornerRadii localValue7, ColorRGBA localValue8) {
      this.internalMethod00451(localValue1, localValue2, localValue3, localValue4, localValue5, internalMethod03310(localValue7, 3.0F), localValue6, 2.0F, localValue8);
   }

   public void internalMethod08941(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, CornerRadii localValue7, ColorRGBA localValue8) {
      this.internalMethod03667(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, 2.0F, 1.0F, localValue8);
   }

   public void internalMethod00726(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, CornerRadii localValue8, ColorRGBA localValue9) {
      this.internalMethod03667(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, internalMethod03310(localValue8, localValue7 / 2.0F), localValue7, 0.5F, localValue9);
   }

   public void internalMethod03909(FontFamily localValue1, String localValue2, float localValue3, Matrix4f localValue4, float localValue5, float localValue6, float localValue7, int localValue8) {
      if (localValue1 != null && localValue2 != null && !localValue2.isEmpty()) {
         int localValue9 = this.internalMethod03933(localValue8);
         if ((localValue9 >>> 24 & 0xFF) != 0) {
            this.internalMethod09893();
            this.internalField1197
               .computeIfAbsent(localValue1, localValue0 -> new ArrayList<>())
               .add(new UiBatchRenderer.InternalType0417(new Matrix4f(localValue4), localValue2, localValue3, localValue5, localValue6, localValue7, localValue9));
         }
      }
   }

   public boolean internalMethod05284(
      FontFamily localValue1,
      String localValue2,
      float localValue3,
      Matrix4f localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      int localValue8,
      float localValue9,
      float localValue10,
      boolean localValue11,
      float localValue12,
      float localValue13,
      float localValue14,
      float localValue15,
      float localValue16,
      float localValue17
   ) {
      if (this.internalField0201 == null) {
         return false;
      } else if (localValue1 != null && localValue2 != null && !localValue2.isEmpty()) {
         int localValue18 = this.internalMethod03933(localValue8);
         if ((localValue18 >>> 24 & 0xFF) == 0) {
            return true;
         } else {
            this.internalMethod09898();
            this.internalField0201
               .internalMethod01449(new Matrix4f(localValue4), localValue1, localValue2, localValue3, localValue5, localValue6, localValue7, localValue18, localValue9, localValue10, localValue11, localValue12, localValue13, localValue14, localValue15, localValue16, localValue17);
            return true;
         }
      } else {
         return true;
      }
   }

   public boolean internalMethod03840(
      Matrix4f localValue1,
      float localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      CornerRadii localValue6,
      float localValue7,
      float localValue8,
      ColorRGBA localValue9,
      ColorRGBA localValue10,
      ColorRGBA localValue11,
      ColorRGBA localValue12,
      boolean localValue13
   ) {
      if (this.internalField0201 == null) {
         return false;
      } else {
         this.internalMethod09898();
         this.internalField0201
            .internalMethod07386(
               new Matrix4f(localValue1),
               localValue2,
               localValue3,
               localValue4,
               localValue5,
               localValue6,
               localValue7,
               localValue8,
               this.internalMethod03933(localValue9.getRGB()),
               this.internalMethod03933(localValue10.getRGB()),
               this.internalMethod03933(localValue11.getRGB()),
               this.internalMethod03933(localValue12.getRGB()),
               localValue13
            );
         return true;
      }
   }

   public void internalMethod02423(String localValue1, float localValue2, float localValue3, float localValue4, Matrix4f localValue5, ColorRGBA localValue6) {
      if (localValue1 != null && !(localValue4 <= 0.0F) && !internalMethod03602(localValue6) && IconFontRegistry.internalMethod02915()) {
         Integer localValue7 = IconFontRegistry.internalMethod06164(localValue1);
         if (localValue7 != null) {
            int localValue8 = this.internalMethod03933(localValue6.getRGB());
            if ((localValue8 >>> 24 & 0xFF) != 0) {
               FontFamily localValue9 = IconFontRegistry.internalMethod05006();
               if (localValue9 != null) {
                  this.internalMethod09893();
                  this.internalMethod07488(localValue9, localValue7, localValue2, localValue3, localValue4, localValue5, localValue8);
               }
            }
         }
      }
   }

   public void internalMethod01632(FontFamily localValue1, int localValue2, float localValue3, float localValue4, float localValue5, Matrix4f localValue6, int localValue7) {
      if (localValue1 != null && !(localValue5 <= 0.0F)) {
         int localValue8 = this.internalMethod03933(localValue7);
         if ((localValue8 >>> 24 & 0xFF) != 0) {
            this.internalMethod09893();
            this.internalMethod07488(localValue1, localValue2, localValue3, localValue4, localValue5, localValue6, localValue8);
         }
      }
   }

   public void internalMethod02156(
      int localValue1, Matrix4f localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9, float localValue10, ColorRGBA localValue11
   ) {
      if (localValue1 != 0 && !internalMethod05505(localValue5, localValue6) && !internalMethod03602(localValue11)) {
         int localValue12 = this.internalMethod03933(localValue11.getRGB());
         if ((localValue12 >>> 24 & 0xFF) != 0) {
            this.internalMethod08444();
            this.internalMethod09695();
            if (!this.internalField0417.isEmpty() && this.internalField0227 != localValue1) {
               this.internalMethod09892();
            }

            this.internalField0227 = localValue1;
            this.internalField0417.add(new UiBatchRenderer.InternalType0433(new Matrix4f(localValue2), localValue3, localValue4, localValue5, localValue6, localValue7, localValue8, localValue9, localValue10, localValue12));
         }
      }
   }

   public void internalMethod02893(
      int localValue1,
      Matrix4f localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      float localValue10,
      float localValue11,
      float localValue12,
      float localValue13,
      float localValue14,
      float localValue15,
      float localValue16,
      float localValue17,
      float localValue18,
      ColorRGBA localValue19
   ) {
      this.internalMethod02438(localValue1, true, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue8, localValue9, localValue10, localValue11, localValue12, localValue13, localValue14, localValue15, localValue16, localValue17, localValue18, localValue19);
   }

   public void internalMethod00549(
      int localValue1,
      Matrix4f localValue2,
      float localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      float localValue10,
      float localValue11,
      float localValue12,
      float localValue13,
      float localValue14,
      float localValue15,
      float localValue16,
      float localValue17,
      ColorRGBA localValue18
   ) {
      this.internalMethod02438(localValue1, false, localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue8, localValue9, localValue10, localValue11, localValue12, localValue13, localValue14, localValue15, localValue16, localValue17, 2.0F, localValue18);
   }

   private void internalMethod07488(FontFamily localValue1, int localValue2, float localValue3, float localValue4, float localValue5, Matrix4f localValue6, int localValue7) {
      this.internalField1196
         .computeIfAbsent(localValue1, localValue0 -> new ArrayList<>())
         .add(new UiBatchRenderer.InternalType0432(new Matrix4f(localValue6), localValue2, localValue3, localValue4, localValue5, localValue7));
   }

   public void internalMethod08442() {
      if (this.internalField0201 != null) {
         if (!internalField0276) {
            internalField0276 = true;

            try {
               this.internalMethod09898();
               this.internalField0201.internalMethod08347();
            } finally {
               internalField0276 = false;
            }
         }
      } else if (!internalField0276 && !this.internalMethod08443()) {
         internalField0276 = true;
         float[] localValue1 = (float[])RenderSystem.getShaderColor().clone();

         try {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.internalMethod02920(this.internalField0543, false);
            this.internalMethod09687();
            this.internalMethod02920(this.internalField0544, true);
            this.internalMethod09688();
            this.internalMethod09694();
            this.internalMethod09695();
            this.internalMethod09892();
         } finally {
            RenderSystem.setShaderColor(localValue1[0], localValue1[1], localValue1[2], localValue1[3]);
            this.internalMethod10074();
            internalField0276 = false;
         }
      }
   }

   @Override
   public void close() {
      try {
         this.internalMethod08442();
      } finally {
         if (internalField0200 == this) {
            internalField0200 = this.internalField0199;
         }
      }
   }

   private void internalMethod00451(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, CornerRadii localValue6, float localValue7, float localValue8, ColorRGBA localValue9) {
      if (!internalMethod05505(localValue4, localValue5) && !internalMethod03602(localValue9)) {
         int localValue10 = this.internalMethod03933(localValue9.getRGB());
         if ((localValue10 >>> 24 & 0xFF) != 0) {
            this.internalMethod09893();
            UiBatchRenderer.InternalType0414 localValue11 = UiBatchRenderer.InternalType0414.internalMethod00533(localValue6, localValue7, localValue8);
            this.internalField0543
               .computeIfAbsent(localValue11, localValue0 -> new ArrayList<>())
               .add(new UiBatchRenderer.InternalType0415(new Matrix4f(localValue1), localValue2, localValue3, localValue4, localValue5, localValue10));
         }
      }
   }

   private void internalMethod03667(
      Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, CornerRadii localValue7, float localValue8, float localValue9, ColorRGBA localValue10
   ) {
      if (!internalMethod05505(localValue4, localValue5) && !(localValue6 <= 0.0F) && !internalMethod03602(localValue10)) {
         int localValue11 = this.internalMethod03933(localValue10.getRGB());
         if ((localValue11 >>> 24 & 0xFF) != 0) {
            this.internalMethod09893();
            UiBatchRenderer.InternalType0414 localValue12 = UiBatchRenderer.InternalType0414.internalMethod07514(localValue7, localValue6, 0.5F, localValue9, localValue8);
            this.internalField0544
               .computeIfAbsent(localValue12, localValue0 -> new ArrayList<>())
               .add(new UiBatchRenderer.InternalType0415(new Matrix4f(localValue1), localValue2, localValue3, localValue4, localValue5, localValue11));
         }
      }
   }

   private void internalMethod02438(
      int localValue1,
      boolean localValue2,
      Matrix4f localValue3,
      float localValue4,
      float localValue5,
      float localValue6,
      float localValue7,
      float localValue8,
      float localValue9,
      float localValue10,
      float localValue11,
      float localValue12,
      float localValue13,
      float localValue14,
      float localValue15,
      float localValue16,
      float localValue17,
      float localValue18,
      float localValue19,
      ColorRGBA localValue20
   ) {
      if (localValue1 != 0 && !internalMethod05505(localValue6, localValue7) && !internalMethod05505(localValue12, localValue13) && !internalMethod03602(localValue20)) {
         int localValue21 = this.internalMethod03933(localValue20.getRGB());
         if ((localValue21 >>> 24 & 0xFF) != 0) {
            this.internalMethod08444();
            this.internalMethod09892();
            UiBatchRenderer.InternalType0472 localValue22 = new UiBatchRenderer.InternalType0472(
               localValue1, localValue2, localValue12, localValue13, localValue14, localValue15, localValue16, localValue17, localValue18, localValue19
            );
            this.internalField1195
               .computeIfAbsent(localValue22, localValue0 -> new ArrayList<>())
               .add(new UiBatchRenderer.InternalType0473(new Matrix4f(localValue3), localValue4, localValue5, localValue6, localValue7, localValue8, localValue9, localValue10, localValue11, localValue21));
         }
      }
   }

   private void internalMethod08444() {
      if (!this.internalMethod08445() && !internalField0276) {
         if (this.internalField0201 != null) {
            this.internalMethod09899();
         } else {
            internalField0276 = true;
            float[] localValue1 = (float[])RenderSystem.getShaderColor().clone();

            try {
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               this.internalMethod02920(this.internalField0543, false);
               this.internalMethod09687();
               this.internalMethod02920(this.internalField0544, true);
               this.internalMethod09688();
               this.internalMethod09694();
               this.internalMethod10075();
            } finally {
               RenderSystem.setShaderColor(localValue1[0], localValue1[1], localValue1[2], localValue1[3]);
               internalField0276 = false;
            }
         }
      }
   }

   private void internalMethod09687() {
      if (!this.internalField0416.isEmpty()) {
         internalMethod08433();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder localValue1 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

         for (UiBatchRenderer.InternalType0416 localValue3 : this.internalField0416) {
            localValue1.vertex(localValue3.internalField0788, localValue3.internalField0205, localValue3.internalField0206 + localValue3.internalField1047, 0.0F).color(localValue3.internalField0227);
            localValue1.vertex(localValue3.internalField0788, localValue3.internalField0205 + localValue3.internalField1048, localValue3.internalField0206 + localValue3.internalField1047, 0.0F).color(localValue3.internalField0227);
            localValue1.vertex(localValue3.internalField0788, localValue3.internalField0205 + localValue3.internalField1048, localValue3.internalField0206, 0.0F).color(localValue3.internalField0227);
            localValue1.vertex(localValue3.internalField0788, localValue3.internalField0205, localValue3.internalField0206, 0.0F).color(localValue3.internalField0227);
         }

         BuiltBuffer localValue4 = localValue1.endNullable();
         if (localValue4 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue4);
         }

         RenderSystem.disableBlend();
      }
   }

   private void internalMethod02920(Map<UiBatchRenderer.InternalType0414, List<UiBatchRenderer.InternalType0415>> localValue1, boolean localValue2) {
      if (!localValue1.isEmpty()) {
         internalMethod08433();
         RenderSystem.disableCull();
         SlugFontRenderer.internalMethod06418(0, 2, 3);
         RenderSystem.setShaderTexture(1, 0);

         for (Entry localValue4 : localValue1.entrySet()) {
            UiBatchRenderer.InternalType0414 localValue5 = (UiBatchRenderer.InternalType0414)localValue4.getKey();
            ShaderProgram localValue6 = RenderPipeline.internalField0996.internalMethod01220();
            localValue6.getUniform("Radius").set(localValue5.internalField0205, localValue5.internalField0206, localValue5.internalField1048, localValue5.internalField1047);
            localValue6.getUniform("RectSmoothness").set(localValue5.internalField1456);
            localValue6.getUniform("CornerSmoothness").set(localValue5.internalField1457);
            localValue6.getUniform("BorderThickness").set(localValue5.internalField1049);
            localValue6.getUniform("BorderSmoothness").set(localValue5.internalField1046, localValue5.internalField1456);
            localValue6.getUniform("TextThickness").set(0.0F);
            localValue6.getUniform("HeadSize").set(0.0F, 0.0F);
            localValue6.getUniform("HeadRadius").set(0.0F, 0.0F, 0.0F, 0.0F);
            localValue6.getUniform("HeadSmoothness").set(0.5F);
            BufferBuilder localValue7 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);

            for (UiBatchRenderer.InternalType0415 localValue9 : (Iterable<UiBatchRenderer.InternalType0415>)(Iterable<?>)(List)localValue4.getValue()) {
               this.internalMethod06879(localValue7, localValue9, localValue5.internalField1456, localValue2);
            }

            BuiltBuffer localValue10 = localValue7.endNullable();
            if (localValue10 != null) {
               BufferRenderer.drawWithGlobalProgram(localValue10);
            }
         }

         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.setShaderTexture(1, 0);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }
   }

   private void internalMethod09688() {
      if (!this.internalField1197.isEmpty()) {
         internalMethod08433();
         RenderSystem.disableCull();
         ShaderProgram localValue1 = SlugFontRenderer.internalMethod07504(0.0F);
         localValue1.getUniform("EnableFadeout").set(0);
         localValue1.getUniform("FadeoutStart").set(0.0F);
         localValue1.getUniform("FadeoutEnd").set(1.0F);
         localValue1.getUniform("FadeinStart").set(0.0F);
         localValue1.getUniform("FadeinEnd").set(0.0F);
         localValue1.getUniform("MaxWidth").set(0.0F);
         localValue1.getUniform("TextPosX").set(0.0F);
         BufferBuilder localValue2 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);

         for (Entry localValue4 : this.internalField1197.entrySet()) {
            FontFamily localValue5 = (FontFamily)localValue4.getKey();

            for (UiBatchRenderer.InternalType0417 localValue7 : (Iterable<UiBatchRenderer.InternalType0417>)(Iterable<?>)(List)localValue4.getValue()) {
               localValue5.internalMethod02357(
                  localValue7.internalField0788, localValue2, localValue7.internalField0248, localValue7.internalField0205, localValue7.internalField0206, localValue7.internalField1048, localValue7.internalField1047, localValue7.internalField0227
               );
            }
         }

         BuiltBuffer localValue8 = localValue2.endNullable();
         if (localValue8 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue8);
         }

         SlugFontRenderer.internalMethod04918();
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }
   }

   private void internalMethod09694() {
      for (Entry localValue2 : this.internalField1196.entrySet()) {
         FontFamily localValue3 = (FontFamily)localValue2.getKey();
         internalMethod08433();
         RenderSystem.disableCull();
         ShaderProgram localValue4 = SlugFontRenderer.internalMethod07504(0.0F);
         localValue4.getUniform("EnableFadeout").set(0);
         localValue4.getUniform("FadeoutStart").set(0.0F);
         localValue4.getUniform("FadeoutEnd").set(1.0F);
         localValue4.getUniform("FadeinStart").set(0.0F);
         localValue4.getUniform("FadeinEnd").set(0.0F);
         localValue4.getUniform("MaxWidth").set(0.0F);
         localValue4.getUniform("TextPosX").set(0.0F);
         BufferBuilder localValue5 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);

         for (UiBatchRenderer.InternalType0432 localValue7 : (Iterable<UiBatchRenderer.InternalType0432>)(Iterable<?>)(List)localValue2.getValue()) {
            localValue3.internalMethod05316(localValue7.internalField0788, localValue5, localValue7.internalField0227, localValue7.internalField0205, localValue7.internalField0206, localValue7.internalField1048, localValue7.internalField0228);
         }

         BuiltBuffer localValue8 = localValue5.endNullable();
         if (localValue8 != null) {
            BufferRenderer.drawWithGlobalProgram(localValue8);
         }

         RenderSystem.setShaderTexture(0, 0);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
      }
   }

   private void internalMethod09695() {
      if (!this.internalField1195.isEmpty()) {
         if (this.internalField0201 != null) {
            this.internalMethod10072();
         } else {
            boolean localValue1 = internalField0276;
            internalField0276 = true;
            float[] localValue2 = (float[])RenderSystem.getShaderColor().clone();

            try {
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

               for (Entry localValue4 : this.internalField1195.entrySet()) {
                  RenderPipeline.internalMethod01296(
                     (UiBatchRenderer.InternalType0472)localValue4.getKey(), (List<UiBatchRenderer.InternalType0473>)localValue4.getValue()
                  );
               }

               this.internalField1195.clear();
            } finally {
               RenderSystem.setShaderColor(localValue2[0], localValue2[1], localValue2[2], localValue2[3]);
               internalField0276 = localValue1;
            }
         }
      }
   }

   private void internalMethod09892() {
      if (!this.internalField0417.isEmpty()) {
         if (this.internalField0201 != null) {
            this.internalMethod10073();
         } else {
            boolean localValue1 = internalField0276;
            internalField0276 = true;
            float[] localValue2 = (float[])RenderSystem.getShaderColor().clone();

            try {
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               internalMethod08433();
               RenderSystem.disableCull();
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               RenderSystem.setShaderTexture(0, this.internalField0227);
               BufferBuilder localValue3 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

               for (UiBatchRenderer.InternalType0433 localValue5 : this.internalField0417) {
                  localValue3.vertex(localValue5.internalField0788, localValue5.internalField0205, localValue5.internalField0206, 0.0F).texture(localValue5.internalField1049, localValue5.internalField1046).color(localValue5.internalField0227);
                  localValue3.vertex(localValue5.internalField0788, localValue5.internalField0205, localValue5.internalField0206 + localValue5.internalField1047, 0.0F)
                     .texture(localValue5.internalField1049, localValue5.internalField1457)
                     .color(localValue5.internalField0227);
                  localValue3.vertex(localValue5.internalField0788, localValue5.internalField0205 + localValue5.internalField1048, localValue5.internalField0206 + localValue5.internalField1047, 0.0F)
                     .texture(localValue5.internalField1456, localValue5.internalField1457)
                     .color(localValue5.internalField0227);
                  localValue3.vertex(localValue5.internalField0788, localValue5.internalField0205 + localValue5.internalField1048, localValue5.internalField0206, 0.0F)
                     .texture(localValue5.internalField1456, localValue5.internalField1046)
                     .color(localValue5.internalField0227);
               }

               BuiltBuffer localValue9 = localValue3.endNullable();
               if (localValue9 != null) {
                  BufferRenderer.drawWithGlobalProgram(localValue9);
               }

               RenderSystem.setShaderTexture(0, 0);
               RenderSystem.enableCull();
               RenderSystem.disableBlend();
               this.internalField0417.clear();
               this.internalField0227 = 0;
            } finally {
               RenderSystem.setShaderColor(localValue2[0], localValue2[1], localValue2[2], localValue2[3]);
               internalField0276 = localValue1;
            }
         }
      }
   }

   private void internalMethod09893() {
      this.internalMethod09695();
      this.internalMethod09892();
   }

   private void internalMethod06879(BufferBuilder localValue1, UiBatchRenderer.InternalType0415 localValue2, float localValue3, boolean localValue4) {
      float localValue5 = -localValue3 / 2.0F + localValue3 * 2.0F;
      float localValue6 = localValue3 / 2.0F + localValue3;
      float localValue7 = localValue2.internalField0205 - localValue5 / 2.0F;
      float localValue8 = localValue2.internalField0206 - localValue6 / 2.0F;
      float localValue9 = localValue2.internalField1048 + localValue5;
      float localValue10 = localValue2.internalField1047 + localValue6;
      float localValue11 = localValue4 ? -localValue2.internalField1048 - 4.0F : localValue2.internalField1048;
      float localValue12 = localValue2.internalField1047;
      localValue1.vertex(localValue2.internalField0788, localValue7, localValue8, 0.0F).texture(localValue11, localValue12).color(localValue2.internalField0227).light(0);
      localValue1.vertex(localValue2.internalField0788, localValue7, localValue8 + localValue10, 0.0F).texture(localValue11, localValue12).color(localValue2.internalField0227).light(0);
      localValue1.vertex(localValue2.internalField0788, localValue7 + localValue9, localValue8 + localValue10, 0.0F).texture(localValue11, localValue12).color(localValue2.internalField0227).light(0);
      localValue1.vertex(localValue2.internalField0788, localValue7 + localValue9, localValue8, 0.0F).texture(localValue11, localValue12).color(localValue2.internalField0227).light(0);
   }

   private boolean internalMethod08443() {
      return this.internalMethod08445()
         && this.internalField1195.isEmpty()
         && this.internalField0417.isEmpty()
         && (this.internalField0201 == null || !this.internalField0201.internalMethod03872());
   }

   private void internalMethod09898() {
      if (this.internalField0201 != null) {
         this.internalMethod09899();
         this.internalMethod10072();
         this.internalMethod10073();
      }
   }

   private void internalMethod09899() {
      if (this.internalField0201 != null && !this.internalMethod08445()) {
         for (Entry localValue2 : this.internalField0543.entrySet()) {
            UiBatchRenderer.InternalType0414 localValue3 = (UiBatchRenderer.InternalType0414)localValue2.getKey();
            CornerRadii localValue4 = internalMethod01992(localValue3);

            for (UiBatchRenderer.InternalType0415 localValue6 : (Iterable<UiBatchRenderer.InternalType0415>)(Iterable<?>)(List)localValue2.getValue()) {
               this.internalField0201
                  .internalMethod05080(
                     localValue6.internalField0788,
                     localValue6.internalField0205,
                     localValue6.internalField0206,
                     localValue6.internalField1048,
                     localValue6.internalField1047,
                     localValue4,
                     localValue3.internalField1456,
                     localValue3.internalField1457,
                     localValue6.internalField0227,
                     localValue6.internalField0227,
                     localValue6.internalField0227,
                     localValue6.internalField0227
                  );
            }
         }

         for (UiBatchRenderer.InternalType0416 localValue11 : this.internalField0416) {
            this.internalField0201
               .internalMethod02918(
                  localValue11.internalField0788,
                  localValue11.internalField0205,
                  localValue11.internalField0206,
                  localValue11.internalField1048,
                  localValue11.internalField1047,
                  localValue11.internalField0227,
                  localValue11.internalField0227,
                  localValue11.internalField0227,
                  localValue11.internalField0227
               );
         }

         for (Entry localValue12 : this.internalField0544.entrySet()) {
            UiBatchRenderer.InternalType0414 localValue15 = (UiBatchRenderer.InternalType0414)localValue12.getKey();
            CornerRadii localValue18 = internalMethod01992(localValue15);

            for (UiBatchRenderer.InternalType0415 localValue24 : (Iterable<UiBatchRenderer.InternalType0415>)(Iterable<?>)(List)localValue12.getValue()) {
               this.internalField0201
                  .internalMethod05986(
                     localValue24.internalField0788,
                     localValue24.internalField0205,
                     localValue24.internalField0206,
                     localValue24.internalField1048,
                     localValue24.internalField1047,
                     localValue18,
                     localValue15.internalField1049,
                     localValue15.internalField1046,
                     localValue15.internalField1456,
                     localValue15.internalField1457,
                     localValue24.internalField0227
                  );
            }
         }

         for (Entry localValue13 : this.internalField1197.entrySet()) {
            FontFamily localValue16 = (FontFamily)localValue13.getKey();

            for (UiBatchRenderer.InternalType0417 localValue22 : (Iterable<UiBatchRenderer.InternalType0417>)(Iterable<?>)(List)localValue13.getValue()) {
               this.internalField0201
                  .internalMethod01449(
                     localValue22.internalField0788,
                     localValue16,
                     localValue22.internalField0248,
                     localValue22.internalField0205,
                     localValue22.internalField0206,
                     localValue22.internalField1048,
                     localValue22.internalField1047,
                     localValue22.internalField0227,
                     0.0F,
                     0.5F,
                     false,
                     0.0F,
                     0.0F,
                     0.0F,
                     1.0F,
                     0.0F,
                     0.0F
                  );
            }
         }

         for (Entry localValue14 : this.internalField1196.entrySet()) {
            FontFamily localValue17 = (FontFamily)localValue14.getKey();

            for (UiBatchRenderer.InternalType0432 localValue23 : (Iterable<UiBatchRenderer.InternalType0432>)(Iterable<?>)(List)localValue14.getValue()) {
               this.internalField0201
                  .internalMethod06400(localValue23.internalField0788, localValue17, localValue23.internalField0227, localValue23.internalField0205, localValue23.internalField0206, localValue23.internalField1048, localValue23.internalField0228);
            }
         }

         this.internalMethod10075();
      }
   }

   private void internalMethod10072() {
      if (this.internalField0201 != null && !this.internalField1195.isEmpty()) {
         for (Entry localValue2 : this.internalField1195.entrySet()) {
            UiBatchRenderer.InternalType0472 localValue3 = (UiBatchRenderer.InternalType0472)localValue2.getKey();
            CornerRadii localValue4 = new CornerRadii(localValue3.internalField1048, localValue3.internalField1049, localValue3.internalField1046, localValue3.internalField1047);

            for (UiBatchRenderer.InternalType0473 localValue6 : (Iterable<UiBatchRenderer.InternalType0473>)(Iterable<?>)(List)localValue2.getValue()) {
               this.internalField0201
                  .internalMethod06943(
                     localValue6.internalField0788,
                     localValue3.internalField0227,
                     localValue6.internalField0205,
                     localValue6.internalField0206,
                     localValue6.internalField1048,
                     localValue6.internalField1047,
                     localValue3.internalField0205,
                     localValue3.internalField0206,
                     localValue4,
                     localValue3.internalField1456,
                     localValue3.internalField1457,
                     localValue6.internalField1049,
                     localValue6.internalField1046,
                     localValue6.internalField1456,
                     localValue6.internalField1457,
                     localValue6.internalField0227
                  );
            }
         }

         this.internalField1195.clear();
      }
   }

   private void internalMethod10073() {
      if (this.internalField0201 != null && !this.internalField0417.isEmpty()) {
         for (UiBatchRenderer.InternalType0433 localValue2 : this.internalField0417) {
            this.internalField0201
               .internalMethod04147(
                  localValue2.internalField0788,
                  this.internalField0227,
                  localValue2.internalField0205,
                  localValue2.internalField0206,
                  localValue2.internalField1048,
                  localValue2.internalField1047,
                  localValue2.internalField1049,
                  localValue2.internalField1046,
                  localValue2.internalField1456,
                  localValue2.internalField1457,
                  localValue2.internalField0227
               );
         }

         this.internalField0417.clear();
         this.internalField0227 = 0;
      }
   }

   private static CornerRadii internalMethod01992(UiBatchRenderer.InternalType0414 localValue0) {
      return new CornerRadii(localValue0.internalField0205, localValue0.internalField1048, localValue0.internalField1047, localValue0.internalField0206);
   }

   private boolean internalMethod08445() {
      return this.internalField0416.isEmpty()
         && this.internalField0543.isEmpty()
         && this.internalField0544.isEmpty()
         && this.internalField1197.isEmpty()
         && this.internalField1196.isEmpty();
   }

   private void internalMethod10074() {
      this.internalMethod10075();
      this.internalField1195.clear();
      this.internalField0417.clear();
      this.internalField0227 = 0;
   }

   private void internalMethod10075() {
      this.internalField0416.clear();
      this.internalField0543.clear();
      this.internalField0544.clear();
      this.internalField1197.clear();
      this.internalField1196.clear();
   }

   private int internalMethod03933(int localValue1) {
      float[] localValue2 = RenderSystem.getShaderColor();
      int localValue3 = localValue1 >>> 24 & 0xFF;
      int localValue4 = localValue1 >>> 16 & 0xFF;
      int localValue5 = localValue1 >>> 8 & 0xFF;
      int localValue6 = localValue1 & 0xFF;
      int localValue7 = this.internalMethod03932(localValue3 * localValue2[3]);
      int localValue8 = this.internalMethod03932(localValue4 * localValue2[0]);
      int localValue9 = this.internalMethod03932(localValue5 * localValue2[1]);
      int localValue10 = this.internalMethod03932(localValue6 * localValue2[2]);
      return localValue7 << 24 | localValue8 << 16 | localValue9 << 8 | localValue10;
   }

   private int internalMethod03932(float localValue1) {
      return Math.max(0, Math.min(255, Math.round(localValue1)));
   }

   private static boolean internalMethod05505(float localValue0, float localValue1) {
      return localValue0 <= 0.0F || localValue1 <= 0.0F;
   }

   private static boolean internalMethod03602(ColorRGBA localValue0) {
      return localValue0 == null || localValue0.getAlpha() <= 0.5F;
   }

   private static CornerRadii internalMethod03310(CornerRadii localValue0, float localValue1) {
      return new CornerRadii(
         localValue0.internalMethod05337() * localValue1, localValue0.internalMethod05340() * localValue1, localValue0.internalMethod08939() * localValue1, localValue0.internalMethod08942() * localValue1
      );
   }

   static final class InternalType0414 {
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final float internalField1049;
      final float internalField1046;
      final float internalField1456;
      final float internalField1457;

      private InternalType0414(float localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8) {
         this.internalField0205 = localValue1;
         this.internalField0206 = localValue2;
         this.internalField1048 = localValue3;
         this.internalField1047 = localValue4;
         this.internalField1049 = localValue5;
         this.internalField1046 = localValue6;
         this.internalField1456 = localValue7;
         this.internalField1457 = localValue8;
      }

      static UiBatchRenderer.InternalType0414 internalMethod00533(CornerRadii localValue0, float localValue1, float localValue2) {
         return new UiBatchRenderer.InternalType0414(
            localValue0.internalMethod05337(), localValue0.internalMethod08942(), localValue0.internalMethod05340(), localValue0.internalMethod08939(), 0.0F, localValue1, localValue1, localValue2
         );
      }

      static UiBatchRenderer.InternalType0414 internalMethod07514(CornerRadii localValue0, float localValue1, float localValue2, float localValue3, float localValue4) {
         return new UiBatchRenderer.InternalType0414(
            localValue0.internalMethod05337(), localValue0.internalMethod08942(), localValue0.internalMethod05340(), localValue0.internalMethod08939(), localValue1, localValue2, localValue3, localValue4
         );
      }

      @Override
      public final String toString() {
         return "InternalType0414[topLeft=" + this.internalField0205 + ", bottomLeft=" + this.internalField0206 + ", topRight=" + this.internalField1048 + ", bottomRight=" + this.internalField1047 + ", thickness=" + this.internalField1049 + ", internalSmoothness=" + this.internalField1046 + ", externalSmoothness=" + this.internalField1456 + ", cornerSmoothness=" + this.internalField1457 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         UiBatchRenderer.InternalType0414 other = (UiBatchRenderer.InternalType0414) localValue1;
         return java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046)
            && java.util.Objects.equals(this.internalField1456, other.internalField1456)
            && java.util.Objects.equals(this.internalField1457, other.internalField1457);
      }

      public float internalMethod05156() {
         return this.internalField0205;
      }

      public float internalMethod05158() {
         return this.internalField0206;
      }

      public float internalMethod07948() {
         return this.internalField1048;
      }

      public float internalMethod07949() {
         return this.internalField1047;
      }

      public float internalMethod07958() {
         return this.internalField1049;
      }

      public float internalMethod07959() {
         return this.internalField1046;
      }

      public float internalMethod09676() {
         return this.internalField1456;
      }

      public float internalMethod09677() {
         return this.internalField1457;
      }
   }

   static final class InternalType0415 {
      final Matrix4f internalField0788;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final int internalField0227;

      InternalType0415(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, int localValue6) {
         this.internalField0788 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField0227 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0415[matrix=" + this.internalField0788 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", width=" + this.internalField1048 + ", height=" + this.internalField1047 + ", rgba=" + this.internalField0227 + "]";
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
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         UiBatchRenderer.InternalType0415 other = (UiBatchRenderer.InternalType0415) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Matrix4f internalMethod06136() {
         return this.internalField0788;
      }

      public float internalMethod06830() {
         return this.internalField0205;
      }

      public float internalMethod06833() {
         return this.internalField0206;
      }

      public float internalMethod08887() {
         return this.internalField1048;
      }

      public float internalMethod08888() {
         return this.internalField1047;
      }

      public int internalMethod06831() {
         return this.internalField0227;
      }
   }

   static final class InternalType0416 {
      final Matrix4f internalField0788;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final int internalField0227;

      InternalType0416(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, int localValue6) {
         this.internalField0788 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField0227 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0416[matrix=" + this.internalField0788 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", width=" + this.internalField1048 + ", height=" + this.internalField1047 + ", rgba=" + this.internalField0227 + "]";
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
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         UiBatchRenderer.InternalType0416 other = (UiBatchRenderer.InternalType0416) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Matrix4f internalMethod06294() {
         return this.internalField0788;
      }

      public float internalMethod01971() {
         return this.internalField0205;
      }

      public float internalMethod01974() {
         return this.internalField0206;
      }

      public float internalMethod07927() {
         return this.internalField1048;
      }

      public float internalMethod07928() {
         return this.internalField1047;
      }

      public int internalMethod01972() {
         return this.internalField0227;
      }
   }

   static final class InternalType0417 {
      final Matrix4f internalField0788;
      final String internalField0248;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final int internalField0227;

      InternalType0417(Matrix4f localValue1, String localValue2, float localValue3, float localValue4, float localValue5, float localValue6, int localValue7) {
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
         return "InternalType0417[matrix=" + this.internalField0788 + ", text=" + this.internalField0248 + ", size=" + this.internalField0205 + ", x=" + this.internalField0206 + ", y=" + this.internalField1048 + ", z=" + this.internalField1047 + ", rgba=" + this.internalField0227 + "]";
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
         UiBatchRenderer.InternalType0417 other = (UiBatchRenderer.InternalType0417) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Matrix4f internalMethod02268() {
         return this.internalField0788;
      }

      public String internalMethod03502() {
         return this.internalField0248;
      }

      public float internalMethod03676() {
         return this.internalField0205;
      }

      public float internalMethod03679() {
         return this.internalField0206;
      }

      public float internalMethod08865() {
         return this.internalField1048;
      }

      public float internalMethod08866() {
         return this.internalField1047;
      }

      public int internalMethod03677() {
         return this.internalField0227;
      }
   }

   static final class InternalType0432 {
      final Matrix4f internalField0788;
      final int internalField0227;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final int internalField0228;

      InternalType0432(Matrix4f localValue1, int localValue2, float localValue3, float localValue4, float localValue5, int localValue6) {
         this.internalField0788 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0205 = localValue3;
         this.internalField0206 = localValue4;
         this.internalField1048 = localValue5;
         this.internalField0228 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0432[matrix=" + this.internalField0788 + ", codepoint=" + this.internalField0227 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", size=" + this.internalField1048 + ", rgba=" + this.internalField0228 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0788);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         UiBatchRenderer.InternalType0432 other = (UiBatchRenderer.InternalType0432) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228);
      }

      public Matrix4f internalMethod00801() {
         return this.internalField0788;
      }

      public int internalMethod04988() {
         return this.internalField0227;
      }

      public float internalMethod04987() {
         return this.internalField0205;
      }

      public float internalMethod04990() {
         return this.internalField0206;
      }

      public float internalMethod08398() {
         return this.internalField1048;
      }

      public int internalMethod04991() {
         return this.internalField0228;
      }
   }

   static final class InternalType0433 {
      final Matrix4f internalField0788;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final float internalField1049;
      final float internalField1046;
      final float internalField1456;
      final float internalField1457;
      final int internalField0227;

      InternalType0433(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9, int localValue10) {
         this.internalField0788 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField1049 = localValue6;
         this.internalField1046 = localValue7;
         this.internalField1456 = localValue8;
         this.internalField1457 = localValue9;
         this.internalField0227 = localValue10;
      }

      @Override
      public final String toString() {
         return "InternalType0433[matrix=" + this.internalField0788 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", width=" + this.internalField1048 + ", height=" + this.internalField1047 + ", u1=" + this.internalField1049 + ", v1=" + this.internalField1046 + ", u2=" + this.internalField1456 + ", v2=" + this.internalField1457 + ", rgba=" + this.internalField0227 + "]";
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
         result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         UiBatchRenderer.InternalType0433 other = (UiBatchRenderer.InternalType0433) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046)
            && java.util.Objects.equals(this.internalField1456, other.internalField1456)
            && java.util.Objects.equals(this.internalField1457, other.internalField1457)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Matrix4f internalMethod04937() {
         return this.internalField0788;
      }

      public float internalMethod06669() {
         return this.internalField0205;
      }

      public float internalMethod06673() {
         return this.internalField0206;
      }

      public float internalMethod07799() {
         return this.internalField1048;
      }

      public float internalMethod07800() {
         return this.internalField1047;
      }

      public float internalMethod07821() {
         return this.internalField1049;
      }

      public float internalMethod07823() {
         return this.internalField1046;
      }

      public float internalMethod09748() {
         return this.internalField1456;
      }

      public float internalMethod09749() {
         return this.internalField1457;
      }

      public int internalMethod06670() {
         return this.internalField0227;
      }
   }

   public static final class InternalType0472 {
      final int internalField0227;
      private final boolean internalField0277;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final float internalField1049;
      final float internalField1046;
      final float internalField1456;
      final float internalField1457;

      public InternalType0472(int localValue1, boolean localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9, float localValue10) {
         this.internalField0227 = localValue1;
         this.internalField0277 = localValue2;
         this.internalField0205 = localValue3;
         this.internalField0206 = localValue4;
         this.internalField1048 = localValue5;
         this.internalField1047 = localValue6;
         this.internalField1049 = localValue7;
         this.internalField1046 = localValue8;
         this.internalField1456 = localValue9;
         this.internalField1457 = localValue10;
      }

      @Override
      public final String toString() {
         return "InternalType0472[textureId=" + this.internalField0227 + ", squircle=" + this.internalField0277 + ", width=" + this.internalField0205 + ", height=" + this.internalField0206 + ", topLeft=" + this.internalField1048 + ", bottomLeft=" + this.internalField1047 + ", topRight=" + this.internalField1049 + ", bottomRight=" + this.internalField1046 + ", smoothness=" + this.internalField1456 + ", cornerSmoothness=" + this.internalField1457 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0205);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0206);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1048);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1047);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1049);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1046);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         UiBatchRenderer.InternalType0472 other = (UiBatchRenderer.InternalType0472) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046)
            && java.util.Objects.equals(this.internalField1456, other.internalField1456)
            && java.util.Objects.equals(this.internalField1457, other.internalField1457);
      }

      public int internalMethod02686() {
         return this.internalField0227;
      }

      public boolean internalMethod02687() {
         return this.internalField0277;
      }

      public float internalMethod02685() {
         return this.internalField0205;
      }

      public float internalMethod02689() {
         return this.internalField0206;
      }

      public float internalMethod07774() {
         return this.internalField1048;
      }

      public float internalMethod07775() {
         return this.internalField1047;
      }

      public float internalMethod07788() {
         return this.internalField1049;
      }

      public float internalMethod07790() {
         return this.internalField1046;
      }

      public float internalMethod09561() {
         return this.internalField1456;
      }

      public float internalMethod09562() {
         return this.internalField1457;
      }
   }

   public static final class InternalType0473 {
      final Matrix4f internalField0788;
      final float internalField0205;
      final float internalField0206;
      final float internalField1048;
      final float internalField1047;
      final float internalField1049;
      final float internalField1046;
      final float internalField1456;
      final float internalField1457;
      final int internalField0227;

      public InternalType0473(Matrix4f localValue1, float localValue2, float localValue3, float localValue4, float localValue5, float localValue6, float localValue7, float localValue8, float localValue9, int localValue10) {
         this.internalField0788 = localValue1;
         this.internalField0205 = localValue2;
         this.internalField0206 = localValue3;
         this.internalField1048 = localValue4;
         this.internalField1047 = localValue5;
         this.internalField1049 = localValue6;
         this.internalField1046 = localValue7;
         this.internalField1456 = localValue8;
         this.internalField1457 = localValue9;
         this.internalField0227 = localValue10;
      }

      @Override
      public final String toString() {
         return "InternalType0473[matrix=" + this.internalField0788 + ", x=" + this.internalField0205 + ", y=" + this.internalField0206 + ", width=" + this.internalField1048 + ", height=" + this.internalField1047 + ", u1=" + this.internalField1049 + ", v1=" + this.internalField1046 + ", u2=" + this.internalField1456 + ", v2=" + this.internalField1457 + ", rgba=" + this.internalField0227 + "]";
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
         result = 31 * result + java.util.Objects.hashCode(this.internalField1456);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1457);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         UiBatchRenderer.InternalType0473 other = (UiBatchRenderer.InternalType0473) localValue1;
         return java.util.Objects.equals(this.internalField0788, other.internalField0788)
            && java.util.Objects.equals(this.internalField0205, other.internalField0205)
            && java.util.Objects.equals(this.internalField0206, other.internalField0206)
            && java.util.Objects.equals(this.internalField1048, other.internalField1048)
            && java.util.Objects.equals(this.internalField1047, other.internalField1047)
            && java.util.Objects.equals(this.internalField1049, other.internalField1049)
            && java.util.Objects.equals(this.internalField1046, other.internalField1046)
            && java.util.Objects.equals(this.internalField1456, other.internalField1456)
            && java.util.Objects.equals(this.internalField1457, other.internalField1457)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public Matrix4f internalMethod05108() {
         return this.internalField0788;
      }

      public float internalMethod04475() {
         return this.internalField0205;
      }

      public float internalMethod04478() {
         return this.internalField0206;
      }

      public float internalMethod08746() {
         return this.internalField1048;
      }

      public float internalMethod08747() {
         return this.internalField1047;
      }

      public float internalMethod08758() {
         return this.internalField1049;
      }

      public float internalMethod08759() {
         return this.internalField1046;
      }

      public float internalMethod09540() {
         return this.internalField1456;
      }

      public float internalMethod09541() {
         return this.internalField1457;
      }

      public int internalMethod04476() {
         return this.internalField0227;
      }
   }
}
