package pyrock.utility.render;



import rockstar.client.render.*;
import rockstar.client.internal.render.*;
import rockstar.client.compat.RenderSystem;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import jep.python.PyObject;
import rockstar.client.compat.ShaderProgram;
import rockstar.client.compat.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pyrock.events.render.Render3DEvent;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.FontFamily;
import rockstar.client.internal.render.SlugFontRenderer;
import rockstar.client.render.Render3DUtils;
import rockstar.client.render.HudRenderUtils;

public class PyRender3D {
   private static final int BILLBOARD_STRIDE = 8;
   private static final int LINE_STRIDE = 10;
   private static final int TEXT_STRIDE = 11;
   private static final float TEXT_SIZE = 32.0F;
   private static final float TEXT_SOFTNESS = 0.5F;
   private static final float[] OUTLINE_STEPS = new float[]{
      -1.0F, 0.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, -0.7071F, -0.7071F, 0.7071F, -0.7071F, -0.7071F, 0.7071F, 0.7071F, 0.7071F
   };
   private static final double[] EMPTY = new double[0];

   public void line(Render3DEvent localValue1, double localValue2, double localValue4, double localValue6, double localValue8, double localValue10, double localValue12, ColorRGBA localValue14) {
      this.withLines(
         localValue1,
         true,
         localValue15 -> Render3DUtils.internalMethod06311(
            localValue1.getMatrices(), localValue15, new Vec3d(localValue2, localValue4, localValue6), new Vec3d(localValue8, localValue10, localValue12), this.safeColor(localValue14)
         )
      );
   }

   public void marker(Render3DEvent localValue1, double localValue2, double localValue4, double localValue6, double localValue8, ColorRGBA localValue10) {
      double localValue11 = Math.max(0.01, localValue8);
      ColorRGBA localValue13 = this.safeColor(localValue10);
      this.withLines(localValue1, true, localValue10x -> {
         MatrixStack localValue11x = localValue1.getMatrices();
         Vec3d localValue12 = new Vec3d(localValue2, localValue4, localValue6);
         Render3DUtils.internalMethod06311(localValue11x, localValue10x, localValue12.add(-localValue11, 0.0, 0.0), localValue12.add(localValue11, 0.0, 0.0), localValue13);
         Render3DUtils.internalMethod06311(localValue11x, localValue10x, localValue12.add(0.0, -localValue11, 0.0), localValue12.add(0.0, localValue11, 0.0), localValue13);
         Render3DUtils.internalMethod06311(localValue11x, localValue10x, localValue12.add(0.0, 0.0, -localValue11), localValue12.add(0.0, 0.0, localValue11), localValue13);
      });
   }

   public void box(Render3DEvent localValue1, Object localValue2, ColorRGBA localValue3) {
      Box localValue4 = this.boxOf(localValue2);
      if (localValue4 != null) {
         this.withLines(localValue1, true, localValue4x -> Render3DUtils.internalMethod08795(localValue1.getMatrices(), localValue4x, localValue4, this.safeColor(localValue3)));
      }
   }

   public void boxGradient(Render3DEvent localValue1, Object localValue2, ColorRGBA localValue3, ColorRGBA localValue4) {
      Box localValue5 = this.boxOf(localValue2);
      if (localValue5 != null) {
         this.withLines(localValue1, true, localValue5x -> Render3DUtils.internalMethod05921(localValue1.getMatrices(), localValue5x, localValue5, this.safeColor(localValue3), this.safeColor(localValue4)));
      }
   }

   public void filledBox(Render3DEvent localValue1, Object localValue2, ColorRGBA localValue3) {
      Box localValue4 = this.boxOf(localValue2);
      if (localValue4 != null) {
         this.withQuads(localValue1, false, localValue4x -> Render3DUtils.internalMethod02535(localValue1.getMatrices(), localValue4x, localValue4, this.safeColor(localValue3)));
      }
   }

   public void filledBoxGradient(Render3DEvent localValue1, Object localValue2, ColorRGBA localValue3, ColorRGBA localValue4) {
      Box localValue5 = this.boxOf(localValue2);
      if (localValue5 != null) {
         this.withQuads(localValue1, false, localValue5x -> Render3DUtils.internalMethod05375(localValue1.getMatrices(), localValue5x, localValue5, this.safeColor(localValue3), this.safeColor(localValue4)));
      }
   }

   public void glowingBox(Render3DEvent localValue1, Object localValue2, ColorRGBA localValue3) {
      Box localValue4 = this.boxOf(localValue2);
      if (localValue4 != null) {
         this.withQuads(localValue1, true, localValue4x -> Render3DUtils.internalMethod04460(localValue1.getMatrices(), localValue4x, localValue4, this.safeColor(localValue3)));
      }
   }

   public void boxAt(Render3DEvent localValue1, double localValue2, double localValue4, double localValue6, double localValue8, double localValue10, double localValue12, ColorRGBA localValue14) {
      this.box(localValue1, new Box(localValue2, localValue4, localValue6, localValue2 + localValue8, localValue4 + localValue10, localValue6 + localValue12), localValue14);
   }

   public void filledBoxAt(Render3DEvent localValue1, double localValue2, double localValue4, double localValue6, double localValue8, double localValue10, double localValue12, ColorRGBA localValue14) {
      this.filledBox(localValue1, new Box(localValue2, localValue4, localValue6, localValue2 + localValue8, localValue4 + localValue10, localValue6 + localValue12), localValue14);
   }

   public void ring(Render3DEvent localValue1, Object localValue2, double localValue3, double localValue5, int localValue7, ColorRGBA localValue8) {
      Vec3d localValue9 = this.centerOf(localValue2);
      if (localValue9 != null) {
         int localValue10 = Math.max(8, localValue7);
         double localValue11 = Math.max(0.01, localValue3);
         double localValue13 = localValue9.y + localValue5;
         ColorRGBA localValue15 = this.safeColor(localValue8);
         this.withLines(localValue1, true, localValue8x -> {
            MatrixStack localValue9x = localValue1.getMatrices();

            for (int localValue10x = 0; localValue10x < localValue10; localValue10x++) {
               double localValue11x = (Math.PI * 2) * localValue10x / localValue10;
               double localValue13x = (Math.PI * 2) * (localValue10x + 1) / localValue10;
               Vec3d localValue15x = new Vec3d(localValue9.x + Math.cos(localValue11x) * localValue11, localValue13, localValue9.z + Math.sin(localValue11x) * localValue11);
               Vec3d localValue16 = new Vec3d(localValue9.x + Math.cos(localValue13x) * localValue11, localValue13, localValue9.z + Math.sin(localValue13x) * localValue11);
               Render3DUtils.internalMethod06311(localValue9x, localValue8x, localValue15x, localValue16, localValue15);
            }
         });
      }
   }

   public void target(Render3DEvent localValue1, Object localValue2, ColorRGBA localValue3) {
      Box localValue4 = this.boxOf(localValue2);
      if (localValue4 != null) {
         ColorRGBA localValue5 = this.safeColor(localValue3);
         ColorRGBA localValue6 = localValue5.mulAlpha(0.16F);
         this.filledBox(localValue1, localValue4, localValue6);
         this.box(localValue1, localValue4, localValue5);
         this.marker(localValue1, localValue4.getCenter().x, localValue4.maxY + 0.25, localValue4.getCenter().z, 0.22, localValue5);
         this.ring(localValue1, localValue4, Math.max(localValue4.getLengthX(), localValue4.getLengthZ()) * 0.75, -localValue4.getLengthY() * 0.5 + 0.04, 48, localValue5);
      }
   }

   public void billboard(
      Render3DEvent localValue1, Identifier localValue2, double localValue3, double localValue5, double localValue7, double localValue9, double localValue11, double localValue13, ColorRGBA localValue15, boolean localValue16
   ) {
      if (localValue1 != null && localValue2 != null && !(localValue9 <= 0.0) && !(localValue11 <= 0.0)) {
         MatrixStack localValue17 = localValue1.getMatrices();
         localValue17.push();

         try {
            HudRenderUtils.internalMethod02691(localValue16);
            HudRenderUtils.internalMethod01900(localValue17);
            localValue17.translate(localValue3, localValue5, localValue7);
            localValue17.multiply(localValue1.getCamera().getRotation());
            if (localValue13 != 0.0) {
               localValue17.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)localValue13));
            }

            RenderSystem.setShaderTexture(0, localValue2);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
            BufferBuilder localValue18 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            Matrix4f localValue19 = localValue17.peek().getPositionMatrix();
            float localValue20 = (float)(localValue9 / 2.0);
            float localValue21 = (float)(localValue11 / 2.0);
            int localValue22 = this.safeColor(localValue15).getRGB();
            localValue18.vertex(localValue19, -localValue20, -localValue21, 0.0F).texture(0.0F, 1.0F).color(localValue22);
            localValue18.vertex(localValue19, localValue20, -localValue21, 0.0F).texture(1.0F, 1.0F).color(localValue22);
            localValue18.vertex(localValue19, localValue20, localValue21, 0.0F).texture(1.0F, 0.0F).color(localValue22);
            localValue18.vertex(localValue19, -localValue20, localValue21, 0.0F).texture(0.0F, 0.0F).color(localValue22);
            HudRenderUtils.internalMethod05816(localValue18);
         } finally {
            RenderSystem.setShaderTexture(0, 0);
            HudRenderUtils.internalMethod04670();
            localValue17.pop();
         }
      }
   }

   public void billboards(Render3DEvent localValue1, Identifier localValue2, Object localValue3, boolean localValue4) {
      if (localValue1 != null && localValue2 != null) {
         double[] localValue5 = unpack(localValue3, "billboards");
         if (localValue5.length >= 8) {
            MatrixStack localValue6 = localValue1.getMatrices();
            localValue6.push();

            try {
               HudRenderUtils.internalMethod02691(localValue4);
               HudRenderUtils.internalMethod01900(localValue6);
               RenderSystem.setShaderTexture(0, localValue2);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
               Quaternionf localValue7 = localValue1.getCamera().getRotation();
               Vector3f localValue8 = localValue7.transform(new Vector3f(1.0F, 0.0F, 0.0F));
               Vector3f localValue9 = localValue7.transform(new Vector3f(0.0F, 1.0F, 0.0F));
               Matrix4f localValue10 = localValue6.peek().getPositionMatrix();
               BufferBuilder localValue11 = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

               for (int localValue12 = 0; localValue12 + 8 <= localValue5.length; localValue12 += 8) {
                  float localValue13 = (float)localValue5[localValue12 + 3] * 0.5F;
                  if (!(localValue13 <= 0.0F)) {
                     int localValue14 = packColor(localValue5[localValue12 + 4], localValue5[localValue12 + 5], localValue5[localValue12 + 6], localValue5[localValue12 + 7]);
                     if (localValue14 >>> 24 != 0) {
                        float localValue15 = (float)localValue5[localValue12];
                        float localValue16 = (float)localValue5[localValue12 + 1];
                        float localValue17 = (float)localValue5[localValue12 + 2];
                        float localValue18 = localValue8.x * localValue13;
                        float localValue19 = localValue8.y * localValue13;
                        float localValue20 = localValue8.z * localValue13;
                        float localValue21 = localValue9.x * localValue13;
                        float localValue22 = localValue9.y * localValue13;
                        float localValue23 = localValue9.z * localValue13;
                        localValue11.vertex(localValue10, localValue15 - localValue18 - localValue21, localValue16 - localValue19 - localValue22, localValue17 - localValue20 - localValue23).texture(0.0F, 1.0F).color(localValue14);
                        localValue11.vertex(localValue10, localValue15 + localValue18 - localValue21, localValue16 + localValue19 - localValue22, localValue17 + localValue20 - localValue23).texture(1.0F, 1.0F).color(localValue14);
                        localValue11.vertex(localValue10, localValue15 + localValue18 + localValue21, localValue16 + localValue19 + localValue22, localValue17 + localValue20 + localValue23).texture(1.0F, 0.0F).color(localValue14);
                        localValue11.vertex(localValue10, localValue15 - localValue18 + localValue21, localValue16 - localValue19 + localValue22, localValue17 - localValue20 + localValue23).texture(0.0F, 0.0F).color(localValue14);
                     }
                  }
               }

               HudRenderUtils.internalMethod05816(localValue11);
            } finally {
               RenderSystem.setShaderTexture(0, 0);
               HudRenderUtils.internalMethod04670();
               localValue6.pop();
            }
         }
      }
   }

   public void lines(Render3DEvent localValue1, Object localValue2, boolean localValue3) {
      if (localValue1 != null) {
         double[] localValue4 = unpack(localValue2, "lines");
         if (localValue4.length >= 10) {
            MatrixStack localValue5 = localValue1.getMatrices();
            localValue5.push();

            try {
               HudRenderUtils.internalMethod02691(localValue3);
               HudRenderUtils.internalMethod01900(localValue5);
               RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
               Entry localValue6 = localValue5.peek();
               Matrix4f localValue7 = localValue6.getPositionMatrix();
               BufferBuilder localValue8 = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

               for (int localValue9 = 0; localValue9 + 10 <= localValue4.length; localValue9 += 10) {
                  int localValue10 = packColor(localValue4[localValue9 + 6], localValue4[localValue9 + 7], localValue4[localValue9 + 8], localValue4[localValue9 + 9]);
                  if (localValue10 >>> 24 != 0) {
                     float localValue11 = (float)localValue4[localValue9];
                     float localValue12 = (float)localValue4[localValue9 + 1];
                     float localValue13 = (float)localValue4[localValue9 + 2];
                     float localValue14 = (float)localValue4[localValue9 + 3];
                     float localValue15 = (float)localValue4[localValue9 + 4];
                     float localValue16 = (float)localValue4[localValue9 + 5];
                     float localValue17 = localValue14 - localValue11;
                     float localValue18 = localValue15 - localValue12;
                     float localValue19 = localValue16 - localValue13;
                     float localValue20 = (float)Math.sqrt(localValue17 * localValue17 + localValue18 * localValue18 + localValue19 * localValue19);
                     if (!(localValue20 <= 0.0F)) {
                        localValue17 /= localValue20;
                        localValue18 /= localValue20;
                        localValue19 /= localValue20;
                        localValue8.vertex(localValue7, localValue11, localValue12, localValue13).color(localValue10).normal(localValue6, localValue17, localValue18, localValue19);
                        localValue8.vertex(localValue7, localValue14, localValue15, localValue16).color(localValue10).normal(localValue6, localValue17, localValue18, localValue19);
                     }
                  }
               }

               HudRenderUtils.internalMethod05816(localValue8);
            } finally {
               HudRenderUtils.internalMethod04670();
               localValue5.pop();
            }
         }
      }
   }

   public void texts(
      Render3DEvent localValue1,
      Object localValue2,
      Object localValue3,
      Object localValue4,
      ColorRGBA localValue5,
      double localValue6,
      int localValue8,
      ColorRGBA localValue9,
      double localValue10,
      double localValue12,
      int localValue14,
      int localValue15,
      boolean localValue16,
      boolean localValue17
   ) {
      if (localValue1 != null) {
         List localValue18 = strings(localValue2);
         if (!localValue18.isEmpty()) {
            double[] localValue19 = unpack(localValue3, "texts");
            int localValue20 = Math.min(localValue18.size(), localValue19.length / 11);
            if (localValue20 > 0) {
               FontFamily localValue21 = this.fontOf(localValue4);
               float localValue22 = localValue21.internalMethod04986(32.0F);
               if (!(localValue22 <= 0.0F)) {
                  Vec3d localValue23 = localValue1.getCamera().getCameraPos();
                  Vector3f localValue24 = localValue1.getCamera().getRotation().transform(new Vector3f(0.0F, 0.0F, -1.0F));
                  boolean[] localValue25 = new boolean[localValue20];
                  float[] localValue26 = new float[localValue20];

                  for (int localValue27 = 0; localValue27 < localValue20; localValue27++) {
                     int localValue28 = localValue27 * 11;
                     double localValue29 = localValue19[localValue28 + 3];
                     double localValue31 = localValue19[localValue28] - localValue23.x;
                     double localValue33 = localValue19[localValue28 + 1] - localValue23.y;
                     double localValue35 = localValue19[localValue28 + 2] - localValue23.z;
                     if (!(localValue31 * localValue24.x + localValue33 * localValue24.y + localValue35 * localValue24.z < -localValue29 * 2.0 - 0.5)) {
                        localValue25[localValue27] = true;
                        localValue26[localValue27] = localValue21.internalMethod05670((String)localValue18.get(localValue27), 32.0F);
                     }
                  }

                  MatrixStack localValue55 = localValue1.getMatrices();
                  localValue55.push();

                  try {
                     HudRenderUtils.internalMethod02691(localValue17);
                     if (!localValue16) {
                        RenderSystem.enableDepthTest();
                     }

                     HudRenderUtils.internalMethod01900(localValue55);
                     ShaderProgram localValue56 = SlugFontRenderer.internalMethod00401(0.0F, 0.5F);
                     localValue56.getUniform("EnableFadeout").set(0);
                     localValue56.getUniform("FadeoutStart").set(0.0F);
                     localValue56.getUniform("FadeoutEnd").set(1.0F);
                     localValue56.getUniform("FadeinStart").set(0.0F);
                     localValue56.getUniform("FadeinEnd").set(0.0F);
                     localValue56.getUniform("MaxWidth").set(0.0F);
                     localValue56.getUniform("TextPosX").set(0.0F);
                     BufferBuilder localValue57 = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
                     float localValue30 = (float)(localValue6 * localValue22);
                     int localValue58 = Math.min(OUTLINE_STEPS.length, Math.max(0, localValue8) * 2);
                     boolean localValue32 = localValue5 != null && localValue30 > 0.0F && localValue58 > 0;
                     boolean localValue59 = localValue9 != null;

                     for (int localValue34 = 0; localValue34 < localValue20; localValue34++) {
                        if (localValue25[localValue34]) {
                           int localValue60 = localValue34 * 11;
                           String localValue36 = (String)localValue18.get(localValue34);
                           if (!localValue36.isEmpty()) {
                              float localValue37 = (float)localValue19[localValue60 + 3];
                              int localValue38 = packColor(localValue19[localValue60 + 4], localValue19[localValue60 + 5], localValue19[localValue60 + 6], localValue19[localValue60 + 7]);
                              if (!(localValue37 <= 0.0F) && localValue38 >>> 24 != 0) {
                                 localValue55.push();

                                 try {
                                    localValue55.translate(localValue19[localValue60], localValue19[localValue60 + 1], localValue19[localValue60 + 2]);
                                    this.face(localValue55, localValue1, localValue14, localValue19[localValue60 + 8], localValue19[localValue60 + 9]);
                                    float localValue39 = (float)localValue19[localValue60 + 10];
                                    if (localValue39 != 0.0F) {
                                       localValue55.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(localValue39));
                                    }

                                    float localValue40 = localValue37 / localValue22;
                                    localValue55.scale(localValue40, -localValue40, 1.0F);
                                    Matrix4f localValue41 = localValue55.peek().getPositionMatrix();
                                    float localValue42 = localValue15 == 1 ? 0.0F : (localValue15 == 2 ? -localValue26[localValue34] : -localValue26[localValue34] * 0.5F);
                                    float localValue43 = -localValue22 * 0.5F;
                                    float localValue44 = (localValue38 >>> 24 & 0xFF) / 255.0F;
                                    if (localValue59) {
                                       localValue21.internalMethod05024(
                                          localValue41,
                                          localValue57,
                                          localValue36,
                                          32.0F,
                                          localValue42 + (float)(localValue10 * localValue22),
                                          localValue43 + (float)(localValue12 * localValue22),
                                          0.0F,
                                          faded(localValue9, localValue44),
                                          0.5F
                                       );
                                    }

                                    if (localValue32) {
                                       int localValue45 = faded(localValue5, localValue44);

                                       for (int localValue46 = 0; localValue46 < localValue58; localValue46 += 2) {
                                          localValue21.internalMethod05024(
                                             localValue41,
                                             localValue57,
                                             localValue36,
                                             32.0F,
                                             localValue42 + OUTLINE_STEPS[localValue46] * localValue30,
                                             localValue43 + OUTLINE_STEPS[localValue46 + 1] * localValue30,
                                             0.0F,
                                             localValue45,
                                             0.5F
                                          );
                                       }
                                    }

                                    localValue21.internalMethod05024(localValue41, localValue57, localValue36, 32.0F, localValue42, localValue43, 0.0F, localValue38, 0.5F);
                                 } finally {
                                    localValue55.pop();
                                 }
                              }
                           }
                        }
                     }

                     HudRenderUtils.internalMethod05816(localValue57);
                  } finally {
                     SlugFontRenderer.internalMethod04918();
                     HudRenderUtils.internalMethod04670();
                     localValue55.pop();
                  }
               }
            }
         }
      }
   }

   public double textWidth(String localValue1, double localValue2, Object localValue4) {
      FontFamily localValue5 = this.fontOf(localValue4);
      float localValue6 = localValue5.internalMethod04986(32.0F);
      return !(localValue6 <= 0.0F) && localValue1 != null && !localValue1.isEmpty() ? localValue5.internalMethod05670(localValue1, 32.0F) / localValue6 * localValue2 : 0.0;
   }

   public double lineHeight(double localValue1, Object localValue3) {
      FontFamily localValue4 = this.fontOf(localValue3);
      float localValue5 = localValue4.internalMethod04986(32.0F);
      return localValue5 <= 0.0F ? localValue1 : localValue4.internalMethod08067(32.0F) / localValue5 * localValue1;
   }

   public double[] letters(String localValue1, double localValue2, Object localValue4) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         FontFamily localValue5 = this.fontOf(localValue4);
         float localValue6 = localValue5.internalMethod04986(32.0F);
         if (localValue6 <= 0.0F) {
            return EMPTY;
         } else {
            double localValue7 = localValue2 / localValue6;
            ArrayList localValue9 = new ArrayList();
            localValue5.internalMethod07006(localValue1, 32.0F, 0.0F, 0.0F, null, (localValue3, localValue4x, localValue5x, localValue6x, localValue7x) -> {
               if (localValue4x != null) {
                  localValue9.add(new double[]{localValue5x, localValue6x * localValue7, localValue4x.internalMethod03042() * 32.0F * localValue7});
               }
            });
            double[] localValue10 = new double[localValue9.size() * 3];

            for (int localValue11 = 0; localValue11 < localValue9.size(); localValue11++) {
               double[] localValue12 = (double[])localValue9.get(localValue11);
               localValue10[localValue11 * 3] = localValue12[0];
               localValue10[localValue11 * 3 + 1] = localValue12[1];
               localValue10[localValue11 * 3 + 2] = localValue12[2];
            }

            return localValue10;
         }
      } else {
         return EMPTY;
      }
   }

   private void face(MatrixStack localValue1, Render3DEvent localValue2, int localValue3, double localValue4, double localValue6) {
      switch (localValue3) {
         case 1:
            localValue1.multiply(localValue2.getCamera().getRotation());
            break;
         case 2:
            localValue1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - localValue2.getCamera().getYaw()));
            break;
         default:
            localValue1.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - (float)localValue4));
            if (localValue6 != 0.0) {
               localValue1.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-((float)localValue6)));
            }
      }
   }

   private static int faded(ColorRGBA localValue0, float localValue1) {
      return localValue0.mulAlpha(localValue1).getRGB();
   }

   private FontFamily fontOf(Object localValue1) {
      if (localValue1 instanceof FontFamily localValue4) {
         return localValue4;
      } else if (localValue1 instanceof SizedFont localValue3) {
         return localValue3.internalMethod01335();
      } else {
         return localValue1 instanceof String localValue2 && !localValue2.isBlank() ? PyAssets.slugFont(localValue2) : Fonts.internalField0449;
      }
   }

   private static List<String> strings(Object localValue0) {
      if (localValue0 == null) {
         return List.of();
      } else if (localValue0 instanceof String localValue11) {
         return List.of(localValue11);
      } else if (localValue0 instanceof List localValue10) {
         ArrayList localValue13 = new ArrayList(localValue10.size());

         for (Object localValue17 : localValue10) {
            localValue13.add(localValue17 == null ? "" : localValue17.toString());
         }

         return localValue13;
      } else if (localValue0 instanceof Object[] localValue9) {
         ArrayList localValue12 = new ArrayList(localValue9.length);

         for (Object localValue6 : localValue9) {
            localValue12.add(localValue6 == null ? "" : localValue6.toString());
         }

         return localValue12;
      } else if (localValue0 instanceof Iterable localValue8) {
         ArrayList localValue2 = new ArrayList();

         for (Object localValue4 : localValue8) {
            localValue2.add(localValue4 == null ? "" : localValue4.toString());
         }

         return localValue2;
      } else {
         if (localValue0 instanceof PyObject localValue1) {
            try {
               return strings(localValue1.as(List.class));
            } catch (Exception localValue7) {
            }
         }

         throw new IllegalArgumentException(
            "texts \u0436\u0434\u0451\u0442 \u0441\u043f\u0438\u0441\u043e\u043a \u0441\u0442\u0440\u043e\u043a, \u0430 \u043f\u043e\u043b\u0443\u0447\u0438\u043b "
               + localValue0.getClass().getName()
         );
      }
   }

   private static double[] unpack(Object localValue0, String localValue1) {
      if (localValue0 == null) {
         return EMPTY;
      } else if (localValue0 instanceof double[] localValue13) {
         return localValue13;
      } else if (localValue0 instanceof byte[] localValue12) {
         FloatBuffer localValue19 = ByteBuffer.wrap(localValue12).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
         double[] localValue25 = new double[localValue19.remaining()];

         for (int localValue27 = 0; localValue27 < localValue25.length; localValue27++) {
            localValue25[localValue27] = localValue19.get(localValue27);
         }

         return localValue25;
      } else if (localValue0 instanceof float[] localValue11) {
         double[] localValue18 = new double[localValue11.length];

         for (int localValue24 = 0; localValue24 < localValue11.length; localValue24++) {
            localValue18[localValue24] = localValue11[localValue24];
         }

         return localValue18;
      } else if (localValue0 instanceof int[] localValue10) {
         double[] localValue17 = new double[localValue10.length];

         for (int localValue23 = 0; localValue23 < localValue10.length; localValue23++) {
            localValue17[localValue23] = localValue10[localValue23];
         }

         return localValue17;
      } else if (localValue0 instanceof List localValue9) {
         double[] localValue16 = new double[localValue9.size()];

         for (int localValue22 = 0; localValue22 < localValue16.length; localValue22++) {
            localValue16[localValue22] = number(localValue9.get(localValue22), localValue1);
         }

         return localValue16;
      } else if (localValue0 instanceof Object[] localValue8) {
         double[] localValue15 = new double[localValue8.length];

         for (int localValue21 = 0; localValue21 < localValue8.length; localValue21++) {
            localValue15[localValue21] = number(localValue8[localValue21], localValue1);
         }

         return localValue15;
      } else if (!(localValue0 instanceof Iterable localValue2)) {
         if (localValue0 instanceof PyObject localValue7) {
            List localValue14 = null;

            try {
               localValue14 = (List)localValue7.as(List.class);
            } catch (Exception localValue6) {
            }

            if (localValue14 != null) {
               return unpack(localValue14, localValue1);
            }
         }

         throw new IllegalArgumentException(
            localValue1
               + " \u0436\u0434\u0451\u0442 \u043f\u043b\u043e\u0441\u043a\u0438\u0439 \u0441\u043f\u0438\u0441\u043e\u043a \u0447\u0438\u0441\u0435\u043b, \u0430 \u043f\u043e\u043b\u0443\u0447\u0438\u043b "
               + localValue0.getClass().getName()
         );
      } else {
         ArrayList localValue3 = new ArrayList();

         for (Object localValue5 : localValue2) {
            localValue3.add(localValue5);
         }

         double[] localValue20 = new double[localValue3.size()];

         for (int localValue26 = 0; localValue26 < localValue20.length; localValue26++) {
            localValue20[localValue26] = number(localValue3.get(localValue26), localValue1);
         }

         return localValue20;
      }
   }

   private static double number(Object localValue0, String localValue1) {
      if (localValue0 instanceof Number localValue2) {
         return localValue2.doubleValue();
      } else {
         throw new IllegalArgumentException(
            localValue1
               + " \u0436\u0434\u0451\u0442 \u043f\u043b\u043e\u0441\u043a\u0438\u0439 \u0441\u043f\u0438\u0441\u043e\u043a \u0447\u0438\u0441\u0435\u043b, \u0430 \u0432 \u043d\u0451\u043c \u043b\u0435\u0436\u0438\u0442 "
               + (localValue0 == null ? "None" : localValue0.getClass().getName())
         );
      }
   }

   private static int packColor(double localValue0, double localValue2, double localValue4, double localValue6) {
      return channel(localValue6) << 24 | channel(localValue0) << 16 | channel(localValue2) << 8 | channel(localValue4);
   }

   private static int channel(double localValue0) {
      int localValue2 = (int)Math.round(localValue0);
      return localValue2 < 0 ? 0 : Math.min(localValue2, 255);
   }

   private void withLines(Render3DEvent localValue1, boolean localValue2, Consumer<BufferBuilder> localValue3) {
      this.withBuffer(localValue1, DrawMode.DEBUG_LINES, localValue2, localValue3);
   }

   private void withQuads(Render3DEvent localValue1, boolean localValue2, Consumer<BufferBuilder> localValue3) {
      this.withBuffer(localValue1, DrawMode.QUADS, localValue2, localValue3);
   }

   private void withBuffer(Render3DEvent localValue1, DrawMode localValue2, boolean localValue3, Consumer<BufferBuilder> localValue4) {
      if (localValue1 != null) {
         MatrixStack localValue5 = localValue1.getMatrices();
         localValue5.push();

         try {
            HudRenderUtils.internalMethod02691(localValue3);
            HudRenderUtils.internalMethod01900(localValue5);
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            BufferBuilder localValue6 = RenderSystem.renderThreadTesselator().begin(localValue2, VertexFormats.POSITION_COLOR);
            localValue4.accept(localValue6);
            HudRenderUtils.internalMethod05816(localValue6);
         } finally {
            HudRenderUtils.internalMethod04670();
            localValue5.pop();
         }
      }
   }

   private Box boxOf(Object localValue1) {
      if (localValue1 instanceof Box localValue4) {
         return localValue4;
      } else if (localValue1 instanceof Entity localValue3) {
         return localValue3.getBoundingBox();
      } else {
         return localValue1 instanceof Vec3d localValue2 ? new Box(localValue2, localValue2).expand(0.1) : null;
      }
   }

   private Vec3d centerOf(Object localValue1) {
      if (localValue1 instanceof Vec3d localValue3) {
         return localValue3;
      } else {
         Box localValue2 = this.boxOf(localValue1);
         return localValue2 == null ? null : localValue2.getCenter();
      }
   }

   private ColorRGBA safeColor(ColorRGBA localValue1) {
      return localValue1 == null ? ColorRGBA.WHITE : localValue1;
   }
}
