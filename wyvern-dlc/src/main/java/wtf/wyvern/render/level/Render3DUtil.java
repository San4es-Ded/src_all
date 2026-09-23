package wtf.wyvern.render.level;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.floats.Float2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.floats.Float2ObjectMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4i;
import org.lwjgl.opengl.GL11;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.utility.math.ProjectionUtil;
import wtf.wyvern.render.display.base.color.ColorUtil;
import wtf.astroguard.J2C.FastNative;

public final class Render3DUtil implements IMinecraft {
   public static final List<Texture> TEXTURE_DEPTH = new ArrayList();
   public static final List<Texture> TEXTURE = new ArrayList();
   private static final Map<VoxelShape, ShapeOutline> SHAPE_OUTLINES = new HashMap<>();
   private static final Map<VoxelShape, ShapeBoxes> SHAPE_BOXES = new HashMap<>();
   public static final List<Line> LINE_DEPTH = new ArrayList();
   public static final List<Line> LINE = new ArrayList();
   public static final List<Quad> QUAD_DEPTH = new ArrayList();
   public static final List<Quad> QUAD = new ArrayList();
   private static final Float2ObjectMap<List<Line>> LINE_BUCKETS = new Float2ObjectLinkedOpenHashMap<>();
   private static Tessellator tessellator = Tessellator.getInstance();
   private static Matrix4f lastProjMat = new Matrix4f();
   private static Matrix4f lastModMat = new Matrix4f();
   private static Matrix4f lastWorldSpaceMatrix = new Matrix4f();
   private static final Identifier captureId = Identifier.of("textures/capture.png");
   private static final Identifier bloom = Identifier.of("textures/bloom.png");
   private static float espValue = 1.0F;
   private static float espSpeed = 1.0F;
   private static float prevEspValue;
   private static float prevCircleStep;
   private static float circleStep;
   private static boolean flipSpeed;

   public static void onEventRender3D(MatrixStack matrix) {
      Entry entry = matrix.peek();
      final Entry finalEntry = entry;
      Vec3d cameraPos = mc.getEntityRenderDispatcher().camera.getPos();
      BufferBuilder buffer;
      if (!QUAD.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_CONSTANT_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder finalBuffer = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         for (Quad quad : QUAD) {
            vertexQuadOffset(finalEntry, finalBuffer, quad.x, quad.y, quad.w, quad.z, quad.color, cameraPos);
         }
         BufferRenderer.drawWithGlobalProgram(finalBuffer.end());
         RenderSystem.enableDepthTest();
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         QUAD.clear();
      }

      if (!LINE.isEmpty()) {
         GL11.glEnable(GL11.GL_LINE_SMOOTH);
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_CONSTANT_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);

         for (List<Line> bucket : LINE_BUCKETS.values()) {
            bucket.clear();
         }
         for (Line line : LINE) {
            lineBucket(line.width).add(line);
         }

         for (Float2ObjectMap.Entry<List<Line>> widthEntry : LINE_BUCKETS.float2ObjectEntrySet()) {
            if (widthEntry.getValue().isEmpty()) continue;
            float width = widthEntry.getFloatKey();
            RenderSystem.lineWidth(width);
            BufferBuilder buffer1 = tessellator.begin(DrawMode.LINES, VertexFormats.LINES);
            for (Line line : widthEntry.getValue()) {
               vertexLineOffset(matrix, buffer1, line.start, line.end, line.colorStart, line.colorEnd, cameraPos);
            }
            BufferRenderer.drawWithGlobalProgram(buffer1.end());
         }

         RenderSystem.enableDepthTest();
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         LINE.clear();
         GL11.glDisable(GL11.GL_LINE_SMOOTH);
      }

      if (!LINE_DEPTH.isEmpty()) {
         GL11.glEnable(GL11.GL_LINE_SMOOTH);
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_CONSTANT_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);

         for (List<Line> bucket : LINE_BUCKETS.values()) {
            bucket.clear();
         }
         for (Line line : LINE_DEPTH) {
            lineBucket(line.width).add(line);
         }

         for (Float2ObjectMap.Entry<List<Line>> widthEntry : LINE_BUCKETS.float2ObjectEntrySet()) {
            if (widthEntry.getValue().isEmpty()) continue;
            float width = widthEntry.getFloatKey();
            RenderSystem.lineWidth(width);
            BufferBuilder buffer2 = tessellator.begin(DrawMode.LINES, VertexFormats.LINES);
            for (Line line : widthEntry.getValue()) {
               vertexLineOffset(matrix, buffer2, line.start, line.end, line.colorStart, line.colorEnd, cameraPos);
            }
            BufferRenderer.drawWithGlobalProgram(buffer2.end());
         }

         RenderSystem.depthMask(true);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         LINE_DEPTH.clear();
         GL11.glDisable(GL11.GL_LINE_SMOOTH);
      }

      if (!QUAD_DEPTH.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_CONSTANT_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder finalBuffer2 = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         for (Quad quad : QUAD_DEPTH) {
            vertexQuadOffset(finalEntry, finalBuffer2, quad.x, quad.y, quad.w, quad.z, quad.color, cameraPos);
         }
         BufferRenderer.drawWithGlobalProgram(finalBuffer2.end());
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         QUAD_DEPTH.clear();
      }

      if (!TEXTURE.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.disableDepthTest();
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         renderTextures(TEXTURE);
         RenderSystem.enableDepthTest();
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         TEXTURE.clear();
      }

      if (!TEXTURE_DEPTH.isEmpty()) {
         RenderSystem.enableBlend();
         RenderSystem.disableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.depthMask(false);
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
         renderTextures(TEXTURE_DEPTH);
         RenderSystem.depthMask(true);
         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         TEXTURE_DEPTH.clear();
      }

   }

   public static void drawBlockBox(MatrixStack matrices, Box box, int color) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      Matrix4f matrix = matrices.peek().getPositionMatrix();

      float x1 = (float)box.minX;
      float y1 = (float)box.minY;
      float z1 = (float)box.minZ;
      float x2 = (float)box.maxX;
      float y2 = (float)box.maxY;
      float z2 = (float)box.maxZ;

      buffer.vertex(matrix, x1, y1, z1).color(color);
      buffer.vertex(matrix, x2, y1, z1).color(color);
      buffer.vertex(matrix, x2, y1, z2).color(color);
      buffer.vertex(matrix, x1, y1, z2).color(color);

      buffer.vertex(matrix, x1, y2, z1).color(color);
      buffer.vertex(matrix, x1, y2, z2).color(color);
      buffer.vertex(matrix, x2, y2, z2).color(color);
      buffer.vertex(matrix, x2, y2, z1).color(color);

      buffer.vertex(matrix, x1, y1, z1).color(color);
      buffer.vertex(matrix, x1, y2, z1).color(color);
      buffer.vertex(matrix, x2, y2, z1).color(color);
      buffer.vertex(matrix, x2, y1, z1).color(color);

      buffer.vertex(matrix, x1, y1, z2).color(color);
      buffer.vertex(matrix, x2, y1, z2).color(color);
      buffer.vertex(matrix, x2, y2, z2).color(color);
      buffer.vertex(matrix, x1, y2, z2).color(color);

      buffer.vertex(matrix, x1, y1, z1).color(color);
      buffer.vertex(matrix, x1, y1, z2).color(color);
      buffer.vertex(matrix, x1, y2, z2).color(color);
      buffer.vertex(matrix, x1, y2, z1).color(color);

      buffer.vertex(matrix, x2, y1, z1).color(color);
      buffer.vertex(matrix, x2, y2, z1).color(color);
      buffer.vertex(matrix, x2, y2, z2).color(color);
      buffer.vertex(matrix, x2, y1, z2).color(color);

      BufferRenderer.drawWithGlobalProgram(buffer.end());
   }

   @FastNative
   public static void drawShape(BlockPos blockPos, VoxelShape voxelShape, int color, float width) {
      drawShape(blockPos, voxelShape, color, width, true, false);
   }

   @FastNative
   public static void drawShape(BlockPos blockPos, VoxelShape voxelShape, int color, float width, boolean fill, boolean depth) {
      if (ProjectionUtil.canSee(voxelShape.getBoundingBox().offset(blockPos))) {
         trimShapeCaches();
         ShapeBoxes shapeBoxes = SHAPE_BOXES.get(voxelShape);
         if (shapeBoxes == null) {
            SHAPE_BOXES.put(voxelShape, new ShapeBoxes(voxelShape, voxelShape.getBoundingBoxes()));
            return;
         }
         for (Box box : shapeBoxes.boxes) {
            drawBox(box.offset(blockPos), color, width, true, fill, depth);
         }
      }

   }

   public static void drawShapeAlternative(BlockPos blockPos, VoxelShape voxelShape, int color, float width, boolean fill, boolean depth) {
      Vec3d vec3d = Vec3d.of(blockPos);
      if (ProjectionUtil.canSee(voxelShape.getBoundingBox().offset(vec3d))) {
         trimShapeCaches();
         List<Box> voxelBoxes = voxelShape.getBoundingBoxes();
         ShapeOutline shapeOutline = SHAPE_OUTLINES.get(voxelShape);
         if (shapeOutline == null) {
            List<Line> lines = new ArrayList();
            voxelShape.forEachEdge((minX, minY, minZ, maxX, maxY, maxZ) -> {
               lines.add(new Line(new Vec3d(minX, minY, minZ), new Vec3d(maxX, maxY, maxZ), 0, 0, 0.0F));
            });
            SHAPE_OUTLINES.put(voxelShape, new ShapeOutline(voxelShape, lines, voxelBoxes));
            return;
         }
         for (Box box : shapeOutline.boxes) {
            drawBox(box.offset(vec3d), color, width, false, fill, depth);
         }
         for (Line line : shapeOutline.lines) {
            drawLine(line.start.add(vec3d), line.end.add(vec3d), color, width, depth);
         }
      }

   }

   @FastNative
   public static void drawBox(Box box, int color, float width) {
      drawBox(box, color, width, true, true, false);
   }

   @FastNative
   public static void drawBox(Box box, int color, float width, boolean line, boolean fill, boolean depth) {
      box = box.expand(0.001D);
      if (ProjectionUtil.canSee(box)) {
         double x1 = box.minX;
         double y1 = box.minY;
         double z1 = box.minZ;
         double x2 = box.maxX;
         double y2 = box.maxY;
         double z2 = box.maxZ;
         if (fill) {
            int fillColor = ColorUtil.multAlpha(color, 0.1F);
            drawQuad(new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z1), new Vec3d(x2, y1, z2), new Vec3d(x1, y1, z2), fillColor, depth);
            drawQuad(new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y1, z1), fillColor, depth);
            drawQuad(new Vec3d(x2, y1, z1), new Vec3d(x2, y2, z1), new Vec3d(x2, y2, z2), new Vec3d(x2, y1, z2), fillColor, depth);
            drawQuad(new Vec3d(x1, y1, z2), new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z2), new Vec3d(x1, y2, z2), fillColor, depth);
            drawQuad(new Vec3d(x1, y1, z1), new Vec3d(x1, y1, z2), new Vec3d(x1, y2, z2), new Vec3d(x1, y2, z1), fillColor, depth);
            drawQuad(new Vec3d(x1, y2, z1), new Vec3d(x1, y2, z2), new Vec3d(x2, y2, z2), new Vec3d(x2, y2, z1), fillColor, depth);
         }

         if (line) {
            drawLine(x1, y1, z1, x2, y1, z1, color, width, depth);
            drawLine(x2, y1, z1, x2, y1, z2, color, width, depth);
            drawLine(x2, y1, z2, x1, y1, z2, color, width, depth);
            drawLine(x1, y1, z2, x1, y1, z1, color, width, depth);
            drawLine(x1, y1, z2, x1, y2, z2, color, width, depth);
            drawLine(x1, y1, z1, x1, y2, z1, color, width, depth);
            drawLine(x2, y1, z2, x2, y2, z2, color, width, depth);
            drawLine(x2, y1, z1, x2, y2, z1, color, width, depth);
            drawLine(x1, y2, z1, x2, y2, z1, color, width, depth);
            drawLine(x2, y2, z1, x2, y2, z2, color, width, depth);
            drawLine(x2, y2, z2, x1, y2, z2, color, width, depth);
            drawLine(x1, y2, z2, x1, y2, z1, color, width, depth);
         }
      }

   }

   public static void vertexLine(@NotNull MatrixStack matrices, @NotNull VertexConsumer buffer, Vec3d start, Vec3d end, int lineColor) {
      vertexLine(matrices, buffer, start.toVector3f(), end.toVector3f(), lineColor, lineColor);
   }

   @FastNative
   public static void vertexLine(@NotNull MatrixStack matrices, @NotNull VertexConsumer buffer, Vec3d start, Vec3d end, int startColor, int endColor) {
      Entry entry = matrices.peek();
      float startX = (float)start.x;
      float startY = (float)start.y;
      float startZ = (float)start.z;
      float endX = (float)end.x;
      float endY = (float)end.y;
      float endZ = (float)end.z;
      float normalX = endX - startX;
      float normalY = endY - startY;
      float normalZ = endZ - startZ;
      float inverseLength = 1.0F / MathHelper.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
      normalX *= inverseLength;
      normalY *= inverseLength;
      normalZ *= inverseLength;
      buffer.vertex(entry, startX, startY, startZ).color(startColor).normal(entry, normalX, normalY, normalZ);
      buffer.vertex(entry, endX, endY, endZ).color(endColor).normal(entry, normalX, normalY, normalZ);
   }

   @FastNative
   private static void vertexLineOffset(@NotNull MatrixStack matrices, @NotNull VertexConsumer buffer,
                                        Vec3d start, Vec3d end, int startColor, int endColor, Vec3d offset) {
      Entry entry = matrices.peek();
      float startX = (float)(start.x - offset.x);
      float startY = (float)(start.y - offset.y);
      float startZ = (float)(start.z - offset.z);
      float endX = (float)(end.x - offset.x);
      float endY = (float)(end.y - offset.y);
      float endZ = (float)(end.z - offset.z);
      float normalX = endX - startX;
      float normalY = endY - startY;
      float normalZ = endZ - startZ;
      float inverseLength = 1.0F / MathHelper.sqrt(normalX * normalX + normalY * normalY + normalZ * normalZ);
      normalX *= inverseLength;
      normalY *= inverseLength;
      normalZ *= inverseLength;
      buffer.vertex(entry, startX, startY, startZ).color(startColor).normal(entry, normalX, normalY, normalZ);
      buffer.vertex(entry, endX, endY, endZ).color(endColor).normal(entry, normalX, normalY, normalZ);
   }

   @FastNative
   private static void vertexQuadOffset(@NotNull Entry entry, @NotNull VertexConsumer buffer,
                                        Vec3d vec1, Vec3d vec2, Vec3d vec3, Vec3d vec4, int color, Vec3d offset) {
      buffer.vertex(entry, (float)(vec1.x - offset.x), (float)(vec1.y - offset.y), (float)(vec1.z - offset.z)).color(color);
      buffer.vertex(entry, (float)(vec2.x - offset.x), (float)(vec2.y - offset.y), (float)(vec2.z - offset.z)).color(color);
      buffer.vertex(entry, (float)(vec3.x - offset.x), (float)(vec3.y - offset.y), (float)(vec3.z - offset.z)).color(color);
      buffer.vertex(entry, (float)(vec4.x - offset.x), (float)(vec4.y - offset.y), (float)(vec4.z - offset.z)).color(color);
   }

   @FastNative
   private static List<Line> lineBucket(float width) {
      List<Line> bucket = LINE_BUCKETS.get(width);
      if (bucket == null) {
         bucket = new ArrayList<>();
         LINE_BUCKETS.put(width, bucket);
      }
      return bucket;
   }

   @FastNative
   private static void trimShapeCaches() {
      if (SHAPE_BOXES.size() + SHAPE_OUTLINES.size() > 1024) {
         SHAPE_BOXES.clear();
         SHAPE_OUTLINES.clear();
      }
   }

   public static void vertexLine(@NotNull MatrixStack matrices, @NotNull VertexConsumer buffer, Vector3f start, Vector3f end, int startColor, int endColor) {
      Entry entry = matrices.peek();
      Vector3f vec = getNormal(start.x, start.y, start.z, end.x, end.y, end.z);
      buffer.vertex(entry, start).color(startColor).normal(entry, vec.x(), vec.y(), vec.z());
      buffer.vertex(entry, end).color(endColor).normal(entry, vec.x(), vec.y(), vec.z());
   }

   @FastNative
   public static void vertexQuad(@NotNull Entry entry, @NotNull VertexConsumer buffer, Vec3d vec1, Vec3d vec2, Vec3d vec3, Vec3d vec4, int color) {
      vertexQuad(entry, buffer, vec1.toVector3f(), vec2.toVector3f(), vec3.toVector3f(), vec4.toVector3f(), color);
   }

   @FastNative
   public static void vertexQuad(@NotNull Entry entry, @NotNull VertexConsumer buffer, Vector3f vec1, Vector3f vec2, Vector3f vec3, Vector3f vec4, int color) {
      buffer.vertex(entry, vec1).color(color);
      buffer.vertex(entry, vec2).color(color);
      buffer.vertex(entry, vec3).color(color);
      buffer.vertex(entry, vec4).color(color);
   }

   @FastNative
   @NotNull
   public static Vector3f getNormal(float x1, float y1, float z1, float x2, float y2, float z2) {
      float xNormal = x2 - x1;
      float yNormal = y2 - y1;
      float zNormal = z2 - z1;
      float normalSqrt = MathHelper.sqrt(xNormal * xNormal + yNormal * yNormal + zNormal * zNormal);
      return new Vector3f(xNormal / normalSqrt, yNormal / normalSqrt, zNormal / normalSqrt);
   }

   @FastNative
   public static void updateTargetEsp() {
      prevEspValue = espValue;
      espValue += espSpeed;
      if (espSpeed > 25.0F) {
         flipSpeed = true;
      }

      if (espSpeed < -25.0F) {
         flipSpeed = false;
      }

      espSpeed = flipSpeed ? espSpeed - 0.5F : espSpeed + 0.5F;
      prevCircleStep = circleStep;
      circleStep += 0.15F;
   }

   @FastNative
   public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int color, float width, boolean depth) {
      drawLine(new Vec3d(minX, minY, minZ), new Vec3d(maxX, maxY, maxZ), color, width, depth);
   }

   @FastNative
   public static void drawLine(Vec3d start, Vec3d end, int color, float width, boolean depth) {
      drawLine(start, end, color, color, width, depth);
   }

   @FastNative
   public static void drawLine(Vec3d start, Vec3d end, int colorStart, int colorEnd, float width, boolean depth) {
      Line line = new Line(start, end, colorStart, colorEnd, width);
      if (depth) {
         LINE_DEPTH.add(line);
      } else {
         LINE.add(line);
      }

   }

   @FastNative
   public static void drawQuad(Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color, boolean depth) {
      Quad quad = new Quad(x, y, w, z, color);
      if (depth) {
         QUAD_DEPTH.add(quad);
      } else {
         QUAD.add(quad);
      }

   }

   @FastNative
   public static void drawTexture(Entry entry, Identifier id, float x, float y, float width, float height, Vector4i color, boolean depth) {
      Texture texture = new Texture(entry, id, x, y, width, height, color);
      if (depth) {
         TEXTURE_DEPTH.add(texture);
      } else {
         TEXTURE.add(texture);
      }

   }

   private static void renderTexture(Texture texture, BufferBuilder buffer) {
      Entry entry = texture.entry;
      float x = texture.x;
      float y = texture.y;
      float w = texture.width;
      float h = texture.height;
      Vector4i colors = texture.color;
      buffer.vertex(entry, x, y, 0.0F).color(colors.x).texture(0.0F, 0.0F);
      buffer.vertex(entry, x, y + h, 0.0F).color(colors.y).texture(0.0F, 1.0F);
      buffer.vertex(entry, x + w, y + h, 0.0F).color(colors.z).texture(1.0F, 1.0F);
      buffer.vertex(entry, x + w, y, 0.0F).color(colors.w).texture(1.0F, 0.0F);
   }

   private static void renderTextures(List<Texture> textures) {
      Identifier activeTexture = null;
      BufferBuilder buffer = null;
      for (Texture texture : textures) {
         if (!texture.id.equals(activeTexture)) {
            if (buffer != null) {
               BufferRenderer.drawWithGlobalProgram(buffer.end());
            }
            activeTexture = texture.id;
            RenderSystem.setShaderTexture(0, activeTexture);
            buffer = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
         }
         renderTexture(texture, buffer);
      }
      if (buffer != null) {
         BufferRenderer.drawWithGlobalProgram(buffer.end());
      }
   }

   @FastNative
   public static float getTickDelta() {
      return mc.getRenderTickCounter().getTickDelta(false);
   }

   @Generated
   private Render3DUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Generated
   public static void setLastProjMat(Matrix4f lastProjMat) {
      Render3DUtil.lastProjMat = lastProjMat;
   }

   @Generated
   public static void setLastModMat(Matrix4f lastModMat) {
      Render3DUtil.lastModMat = lastModMat;
   }

   @Generated
   public static void setLastWorldSpaceMatrix(Matrix4f lastWorldSpaceMatrix) {
      Render3DUtil.lastWorldSpaceMatrix = lastWorldSpaceMatrix;
   }

   @Generated
   public static Matrix4f getLastProjMat() {
      return lastProjMat;
   }

   @Generated
   public static Matrix4f getLastModMat() {
      return lastModMat;
   }

   @Generated
   public static Matrix4f getLastWorldSpaceMatrix() {
      return lastWorldSpaceMatrix;
   }

   public static record Line(Vec3d start, Vec3d end, int colorStart, int colorEnd, float width) {
      public Line(Vec3d start, Vec3d end, int colorStart, int colorEnd, float width) {
         this.start = start;
         this.end = end;
         this.colorStart = colorStart;
         this.colorEnd = colorEnd;
         this.width = width;
      }

      public Vec3d start() {
         return this.start;
      }

      public Vec3d end() {
         return this.end;
      }

      public int colorStart() {
         return this.colorStart;
      }

      public int colorEnd() {
         return this.colorEnd;
      }

      public float width() {
         return this.width;
      }
   }

   public static record Quad(Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color) {
      public Quad(Vec3d x, Vec3d y, Vec3d w, Vec3d z, int color) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.z = z;
         this.color = color;
      }

      public Vec3d x() {
         return this.x;
      }

      public Vec3d y() {
         return this.y;
      }

      public Vec3d w() {
         return this.w;
      }

      public Vec3d z() {
         return this.z;
      }

      public int color() {
         return this.color;
      }
   }

   public static record Texture(Entry entry, Identifier id, float x, float y, float width, float height, Vector4i color) {
      public Texture(Entry entry, Identifier id, float x, float y, float width, float height, Vector4i color) {
         this.entry = entry;
         this.id = id;
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
         this.color = color;
      }

      public Entry entry() {
         return this.entry;
      }

      public Identifier id() {
         return this.id;
      }

      public float x() {
         return this.x;
      }

      public float y() {
         return this.y;
      }

      public float width() {
         return this.width;
      }

      public float height() {
         return this.height;
      }

      public Vector4i color() {
         return this.color;
      }
   }

   public static record ShapeOutline(VoxelShape shape, List<Line> lines, List<Box> boxes) {
      public ShapeOutline(VoxelShape shape, List<Line> lines, List<Box> boxes) {
         this.shape = shape;
         this.lines = lines;
         this.boxes = boxes;
      }

      public VoxelShape shape() {
         return this.shape;
      }

      public List<Line> lines() {
         return this.lines;
      }

      public List<Box> boxes() {
         return this.boxes;
      }
   }

   public static record ShapeBoxes(VoxelShape shape, List<Box> boxes) {
      public ShapeBoxes(VoxelShape shape, List<Box> boxes) {
         this.shape = shape;
         this.boxes = boxes;
      }

      public VoxelShape shape() {
         return this.shape;
      }

      public List<Box> boxes() {
         return this.boxes;
      }
   }
}
