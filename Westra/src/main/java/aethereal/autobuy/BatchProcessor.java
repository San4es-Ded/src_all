package aethereal.autobuy;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.Interface;
import aethereal.render.ColorUtil;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.class_10142;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_293.class_5596;
import net.minecraft.class_4587.class_4665;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class BatchProcessor extends BaseProcessor {
   private final List<BatchProcessor.b> b = new ArrayList<>();
   private final List<BatchProcessor.a> c = new ArrayList<>();

   @Compile
   @Override
   public void setup() {
      this.b.clear();
      this.c.clear();
   }

   @Override
   public void unSetup() {
      this.b.clear();
      this.c.clear();
   }

   public void a(BatchProcessor.b task) {
      this.b.add(task);
   }

   public void a(BatchProcessor.a task) {
      this.c.add(task);
   }

   public void a() {
      if (!this.c.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.disableCull();
         RenderSystem.setShader(class_10142.field_53876);
         class_287 class_4588VarMethod_60827 = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);

         for (BatchProcessor.a task : this.c) {
            task.a(class_4588VarMethod_60827);
         }

         class_286.method_43433(class_4588VarMethod_60827.method_60800());
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         this.c.clear();
      }
   }

   public void b() {
      if (!this.b.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA);

         try {
            if (this.b.stream().anyMatch(t -> !t.h)) {
               RenderSystem.setShader(class_10142.field_53876);
               class_287 class_4588VarMethod_60827 = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);

               for (BatchProcessor.b task : this.b) {
                  if (!task.h) {
                     task.a(class_4588VarMethod_60827);
                  }
               }

               class_286.method_43433(class_4588VarMethod_60827.method_60800());
            }

            Map<Float, List<BatchProcessor.b>> byWidth = new TreeMap<>((v0, v1) -> v0.compareTo(v1));

            for (BatchProcessor.b task2 : this.b) {
               byWidth.computeIfAbsent(task2.d, k -> new ArrayList<>()).add(task2);
            }

            GL11.glEnable(2881);
            RenderSystem.setShader(class_10142.field_53864);

            for (Entry<Float, List<BatchProcessor.b>> e : byWidth.entrySet()) {
               RenderSystem.lineWidth(e.getKey());
               class_287 class_4588VarMethod_60828 = class_289.method_1348().method_60827(class_5596.field_27377, class_290.field_29337);

               for (BatchProcessor.b task3 : e.getValue()) {
                  if (task3.h) {
                     task3.c(class_4588VarMethod_60828);
                  } else {
                     task3.b(class_4588VarMethod_60828);
                  }
               }

               class_286.method_43433(class_4588VarMethod_60828.method_60800());
            }

            GL11.glDisable(2881);
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            this.b.clear();
         } catch (Throwable var7) {
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            throw var7;
         }
      }
   }

   public static class a {
      private static final int k = ColorUtil.a(0, 0, 0, 255);
      private static final float l = 0.5F;
      final Matrix4f a;
      final float b;
      final float c;
      final float d;
      final float e;
      final int f;
      final boolean g;
      final boolean h;
      final float i;
      final int j;

      public a(
         Matrix4f matrix, float minX, float minY, float maxX, float maxY, int color, boolean corners, boolean healthBar, float healthPercent, int healthColor
      ) {
         this.a = matrix;
         this.b = minX;
         this.c = minY;
         this.d = maxX;
         this.e = maxY;
         this.f = color;
         this.g = corners;
         this.h = healthBar;
         this.i = healthPercent;
         this.j = healthColor;
      }

      void a(class_4588 buffer) {
         float fMin;
         if (this.g) {
            fMin = Math.min(this.d - this.b, this.e - this.c) * 0.25F;
         } else {
            fMin = Math.min(this.d - this.b, this.e - this.c) * 0.5F;
         }

         float length = fMin;

         for (int pass = 0; pass < 2; pass++) {
            for (int corner = 0; corner < 4; corner++) {
               float cornerX = (corner & 1) == 0 ? this.b : this.d;
               float cornerY = (corner & 2) == 0 ? this.c : this.e;
               float directionX = (corner & 1) == 0 ? 1.0F : -1.0F;
               float directionY = (corner & 2) == 0 ? 1.0F : -1.0F;
               this.a(buffer, cornerX, cornerY, directionX * length, 0.0F, pass == 0);
               this.a(buffer, cornerX, cornerY, 0.0F, directionY * length, pass == 0);
            }
         }

         if (this.h) {
            float height = this.e - this.c;
            float x = this.b - 2.0F - 0.5F;
            this.a(buffer, x - 0.5F, this.c - 0.5F, 1.5F, height + 1.5F, k);
            this.a(buffer, x, this.c + height * (1.0F - this.i), 0.5F, height * this.i + 0.5F, this.j);
         }
      }

      private void a(class_4588 buffer, float x, float y, float lengthX, float lengthY, boolean outline) {
         float left = Math.min(x, x + lengthX) - (lengthX == 0.0F ? 0.25F : 0.0F);
         float top = Math.min(y, y + lengthY) - (lengthY == 0.0F ? 0.25F : 0.0F);
         float width = lengthX == 0.0F ? 0.5F : Math.abs(lengthX);
         float height = lengthY == 0.0F ? 0.5F : Math.abs(lengthY);
         if (outline) {
            this.a(buffer, left - 0.5F, top - 0.5F, width + 1.0F, height + 1.0F, k);
         } else {
            this.a(buffer, left, top, width, height, this.f);
         }
      }

      private void a(class_4588 buffer, float x, float y, float width, float height, int color) {
         buffer.method_22918(this.a, x, y, 0.0F).method_39415(color);
         buffer.method_22918(this.a, x, y + height, 0.0F).method_39415(color);
         buffer.method_22918(this.a, x + width, y + height, 0.0F).method_39415(color);
         buffer.method_22918(this.a, x + width, y, 0.0F).method_39415(color);
      }
   }

   public static class b {
      final class_4665 a;
      final class_238 b;
      final int c;
      final float d;
      final class_243 e;
      final class_243 f;
      final class_243 g;
      final boolean h;

      public b(class_4665 entry, class_238 box, int color, float width) {
         this.a = entry;
         this.b = box;
         this.c = color;
         this.d = width;
         this.e = null;
         this.f = null;
         this.g = null;
         this.h = false;
      }

      public b(class_4665 entry, class_243 start, class_243 end, class_243 control, int color, float width) {
         this.a = entry;
         this.b = null;
         this.c = color;
         this.d = width;
         this.e = start;
         this.f = end;
         this.g = control;
         this.h = true;
      }

      public static BatchProcessor.b a(class_4587 matrices, class_238 box, int color, float width) {
         return new BatchProcessor.b(matrices.method_23760(), box.method_1014(9.999995420800828E-4), color, width);
      }

      public static BatchProcessor.b a(class_4587 matrices, class_243 start, class_243 end, class_243 control, int color, float width) {
         return new BatchProcessor.b(matrices.method_23760(), start, end, control, color, width);
      }

      void a(class_4588 buffer) {
         float[] rgba = ColorUtil.a(ColorUtil.a(this.c, (this.c >> 24 & 0xFF) / 255.0F * 0.12F));
         double[][][] faces = new double[][][]{
            {
                  {this.b.field_1323, this.b.field_1322, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1322, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1322, this.b.field_1324},
                  {this.b.field_1323, this.b.field_1322, this.b.field_1324}
            },
            {
                  {this.b.field_1323, this.b.field_1325, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1325, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1325, this.b.field_1324},
                  {this.b.field_1323, this.b.field_1325, this.b.field_1324}
            },
            {
                  {this.b.field_1323, this.b.field_1322, this.b.field_1321},
                  {this.b.field_1323, this.b.field_1325, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1325, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1322, this.b.field_1321}
            },
            {
                  {this.b.field_1320, this.b.field_1322, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1325, this.b.field_1321},
                  {this.b.field_1320, this.b.field_1325, this.b.field_1324},
                  {this.b.field_1320, this.b.field_1322, this.b.field_1324}
            },
            {
                  {this.b.field_1320, this.b.field_1322, this.b.field_1324},
                  {this.b.field_1320, this.b.field_1325, this.b.field_1324},
                  {this.b.field_1323, this.b.field_1325, this.b.field_1324},
                  {this.b.field_1323, this.b.field_1322, this.b.field_1324}
            },
            {
                  {this.b.field_1323, this.b.field_1322, this.b.field_1324},
                  {this.b.field_1323, this.b.field_1325, this.b.field_1324},
                  {this.b.field_1323, this.b.field_1325, this.b.field_1321},
                  {this.b.field_1323, this.b.field_1322, this.b.field_1321}
            }
         };
         Matrix4f matrix4f = this.a.method_23761();

         for (double[][] face : faces) {
            this.a(
               buffer,
               matrix4f,
               face[0][0],
               face[0][1],
               face[0][2],
               face[1][0],
               face[1][1],
               face[1][2],
               face[2][0],
               face[2][1],
               face[2][2],
               face[3][0],
               face[3][1],
               face[3][2],
               rgba[0],
               rgba[1],
               rgba[2],
               rgba[3]
            );
         }
      }

      void b(class_4588 buffer) {
         float[] rgba = ColorUtil.a(ColorUtil.a(this.c, (this.c >> 24 & 0xFF) / 255.0F));
         Matrix4f matrix = this.a.method_23761();
         double[][] edges = new double[][]{
            {this.b.field_1323, this.b.field_1322, this.b.field_1321, this.b.field_1320, this.b.field_1322, this.b.field_1321},
            {this.b.field_1320, this.b.field_1322, this.b.field_1321, this.b.field_1320, this.b.field_1322, this.b.field_1324},
            {this.b.field_1320, this.b.field_1322, this.b.field_1324, this.b.field_1323, this.b.field_1322, this.b.field_1324},
            {this.b.field_1323, this.b.field_1322, this.b.field_1324, this.b.field_1323, this.b.field_1322, this.b.field_1321},
            {this.b.field_1323, this.b.field_1325, this.b.field_1321, this.b.field_1320, this.b.field_1325, this.b.field_1321},
            {this.b.field_1320, this.b.field_1325, this.b.field_1321, this.b.field_1320, this.b.field_1325, this.b.field_1324},
            {this.b.field_1320, this.b.field_1325, this.b.field_1324, this.b.field_1323, this.b.field_1325, this.b.field_1324},
            {this.b.field_1323, this.b.field_1325, this.b.field_1324, this.b.field_1323, this.b.field_1325, this.b.field_1321},
            {this.b.field_1323, this.b.field_1322, this.b.field_1321, this.b.field_1323, this.b.field_1325, this.b.field_1321},
            {this.b.field_1320, this.b.field_1322, this.b.field_1321, this.b.field_1320, this.b.field_1325, this.b.field_1321},
            {this.b.field_1320, this.b.field_1322, this.b.field_1324, this.b.field_1320, this.b.field_1325, this.b.field_1324},
            {this.b.field_1323, this.b.field_1322, this.b.field_1324, this.b.field_1323, this.b.field_1325, this.b.field_1324}
         };

         for (double[] edge : edges) {
            this.a(matrix, buffer, this.a, edge[0], edge[1], edge[2], edge[3], edge[4], edge[5], rgba);
         }
      }

      private void a(
         class_4588 buffer,
         Matrix4f matrix,
         double x1,
         double y1,
         double z1,
         double x2,
         double y2,
         double z2,
         double x3,
         double y3,
         double z3,
         double x4,
         double y4,
         double z4,
         float r,
         float g,
         float b,
         float a
      ) {
         class_243 cam = Interface.aM_.method_1561().field_4686.method_19326();
         buffer.method_22918(matrix, (float)(x1 - cam.field_1352), (float)(y1 - cam.field_1351), (float)(z1 - cam.field_1350)).method_22915(r, g, b, a);
         buffer.method_22918(matrix, (float)(x2 - cam.field_1352), (float)(y2 - cam.field_1351), (float)(z2 - cam.field_1350)).method_22915(r, g, b, a);
         buffer.method_22918(matrix, (float)(x3 - cam.field_1352), (float)(y3 - cam.field_1351), (float)(z3 - cam.field_1350)).method_22915(r, g, b, a);
         buffer.method_22918(matrix, (float)(x4 - cam.field_1352), (float)(y4 - cam.field_1351), (float)(z4 - cam.field_1350)).method_22915(r, g, b, a);
      }

      private void a(Matrix4f matrix, class_4588 buffer, class_4665 entry, double x1, double y1, double z1, double x2, double y2, double z2, float[] rgba) {
         class_243 cam = Interface.aM_.method_1561().field_4686.method_19326();
         float lenSq = (float)((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
         float len = class_3532.method_15355(lenSq);
         float nx = len > 1.0E-6F ? (float)(x2 - x1) / len : 0.0F;
         float ny = len > 1.0E-6F ? (float)(y2 - y1) / len : 0.0F;
         float nz = len > 1.0E-6F ? (float)(z2 - z1) / len : 0.0F;
         buffer.method_22918(matrix, (float)(x1 - cam.field_1352), (float)(y1 - cam.field_1351), (float)(z1 - cam.field_1350))
            .method_22915(rgba[0], rgba[1], rgba[2], rgba[3])
            .method_60831(entry, nx, ny, nz);
         buffer.method_22918(matrix, (float)(x2 - cam.field_1352), (float)(y2 - cam.field_1351), (float)(z2 - cam.field_1350))
            .method_22915(rgba[0], rgba[1], rgba[2], rgba[3])
            .method_60831(entry, nx, ny, nz);
      }

      void c(class_4588 buffer) {
         float[] rgba = ColorUtil.a(ColorUtil.a(this.c, (this.c >> 24 & 0xFF) / 255.0F));
         Matrix4f matrix = this.a.method_23761();
         if (this.g == null) {
            this.a(
               matrix, buffer, this.a, this.e.field_1352, this.e.field_1351, this.e.field_1350, this.f.field_1352, this.f.field_1351, this.f.field_1350, rgba
            );
         } else {
            class_243 prev = this.e;

            for (int i = 1; i <= 32; i++) {
               float t = i / 32;
               float oneMinusT = 1.0F - t;
               class_243 point = new class_243(
                  oneMinusT * oneMinusT * this.e.field_1352 + 2.0F * oneMinusT * t * this.g.field_1352 + t * t * this.f.field_1352,
                  oneMinusT * oneMinusT * this.e.field_1351 + 2.0F * oneMinusT * t * this.g.field_1351 + t * t * this.f.field_1351,
                  oneMinusT * oneMinusT * this.e.field_1350 + 2.0F * oneMinusT * t * this.g.field_1350 + t * t * this.f.field_1350
               );
               this.a(matrix, buffer, this.a, prev.field_1352, prev.field_1351, prev.field_1350, point.field_1352, point.field_1351, point.field_1350, rgba);
               prev = point;
            }
         }
      }
   }
}
