package ru.prism.cosmetic.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GpuSampler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import ru.prism.cosmetic.geo.GeoBone;
import ru.prism.cosmetic.geo.GeoCube;
import ru.prism.cosmetic.geo.GeoModel;
import ru.prism.cosmetic.geo.GeoQuad;
import ru.prism.cosmetic.loader.CosmeticLoader;
import ru.prism.cosmetic.model.CosmeticModel;

/**
 * Рисует настоящую 3D-модель косметики внутри карточки GUI: свой pipeline
 * с текстурой и тестом глубины, ортографическая проекция в пикселях карточки.
 * Модель собирается теми же костями и анимациями, что и в мире, но без
 * привязки к игроку. {@link #render} копит карточки, {@link #flush} рисует
 * их одним проходом поверх 2D-фона.
 */
public class CosmeticPreviewRenderer {
   private static final Identifier PIPELINE_ID = Identifier.of("client", "pipeline/cosmetic_preview");
   private static final Identifier SHADER_ID = Identifier.of("client", "core/cosmetic_preview");
   private static final VertexFormat FORMAT = VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL;
   private static final float FIT_FACTOR = 0.8F;
   private static final float MIN_ALPHA = 0.01F;
   private static final float FRONT_ROTATION = 180.0F;
   private static final int INITIAL_BUFFER_SIZE = 4096;
   private static final int UNIFORM_SIZE = 16;

   private static final RenderPipeline PIPELINE = RenderPipelines.register(
      RenderPipeline.builder()
         .withLocation(PIPELINE_ID)
         .withVertexShader(SHADER_ID)
         .withFragmentShader(SHADER_ID)
         .withVertexFormat(FORMAT, VertexFormat.DrawMode.QUADS)
         .withUniform("PreviewData", UniformType.UNIFORM_BUFFER)
         .withSampler("Sampler0")
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(true)
         .withCull(false)
         .build()
   );

   private final CosmeticRenderer renderer = new CosmeticRenderer();
   private final List<Preview> queued = new ArrayList<>();

   private BufferAllocator allocator;
   private BufferBuilder builder;
   private int indexCount;

   private GpuBuffer uniformBuffer;
   private ByteBuffer uniformData;

   public boolean render(CosmeticModel cosmetic, MatrixStack matrices, float centerX, float centerY,
                         float boxSize, float rotationDeg, float alpha) {
      if (cosmetic == null || cosmetic.getTextureId() == null || matrices == null
            || boxSize <= 0.0F || alpha <= MIN_ALPHA) {
         return false;
      }

      float[] bounds = this.renderer.getPreviewBounds(cosmetic);
      if (bounds == null) {
         return false;
      }

      float maxSize = Math.max(bounds[3] - bounds[0], Math.max(bounds[4] - bounds[1], bounds[5] - bounds[2]));
      if (maxSize <= 0.0001F) {
         return false;
      }

      GeoModel model = this.renderer.getPreviewModel(cosmetic);
      if (model == null) {
         return false;
      }

      int quads = countQuads(model);
      if (quads <= 0) {
         return false;
      }

      float pixelScale = boxSize * FIT_FACTOR / maxSize;
      float boundCenterX = (bounds[0] + bounds[3]) / 2.0F;
      float boundCenterY = (bounds[1] + bounds[4]) / 2.0F;
      float boundCenterZ = (bounds[2] + bounds[5]) / 2.0F;

      matrices.push();
      matrices.translate(centerX, centerY, 0.0F);
      matrices.scale(pixelScale, pixelScale, pixelScale);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationDeg + FRONT_ROTATION));
      matrices.translate(-boundCenterX, -boundCenterY, -boundCenterZ);
      this.renderer.renderPreview(cosmetic, matrices, this.consumer(), LightmapTextureManager.MAX_LIGHT_COORDINATE, alpha);
      matrices.pop();

      int cardIndexCount = quads * 6;
      this.queued.add(new Preview(cosmetic.getTextureId(), this.indexCount, cardIndexCount));
      this.indexCount += cardIndexCount;
      return true;
   }

   public boolean renderCape(int index, MatrixStack matrices, float centerX, float centerY,
                             float boxSize, float rotationDeg, float alpha) {
      Identifier capeTexture = CosmeticLoader.getInstance().getCapeTexture(index);
      if (capeTexture == null || matrices == null || boxSize <= 0.0F || alpha <= MIN_ALPHA) {
         return false;
      }

      boolean skinLayout = CosmeticLoader.getInstance().isCapeSkinLayout(index);
      float frontU0 = skinLayout ? 1.0F / 64.0F : 0.0F;
      float frontU1 = skinLayout ? 11.0F / 64.0F : 1.0F;
      float backU0 = skinLayout ? 12.0F / 64.0F : 0.0F;
      float backU1 = skinLayout ? 22.0F / 64.0F : 1.0F;
      float v0 = skinLayout ? 1.0F / 32.0F : 0.0F;
      float v1 = skinLayout ? 17.0F / 32.0F : 1.0F;
      float topV1 = skinLayout ? 1.0F / 32.0F : 1.0F;
      float bottomU0 = skinLayout ? 11.0F / 64.0F : 0.0F;
      float bottomU1 = skinLayout ? 21.0F / 64.0F : 1.0F;

      float pixelScale = boxSize * FIT_FACTOR;
      matrices.push();
      matrices.translate(centerX, centerY, 0.0F);
      matrices.scale(pixelScale, pixelScale, pixelScale);
      matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationDeg + FRONT_ROTATION));
      matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
      matrices.scale(1.0F, -1.0F, 1.0F);
      matrices.translate(0.0F, -0.5F, 0.03125F);

      Matrix4f matrix = matrices.peek().getPositionMatrix();
      VertexConsumer consumer = this.consumer();
      int light = LightmapTextureManager.MAX_LIGHT_COORDINATE;
      float hw = 0.3125F;
      float yTop = 0.0F;
      float yBottom = 1.0F;
      float zFront = -0.0625F;
      float zBack = 0.0F;

      quad(consumer, matrix, light, -hw, yTop, zFront, -hw, yBottom, zFront, hw, yBottom, zFront, hw, yTop, zFront, frontU0, v0, frontU1, v1, 0.0F, 0.0F, -1.0F, alpha);
      quad(consumer, matrix, light, hw, yTop, zBack, hw, yBottom, zBack, -hw, yBottom, zBack, -hw, yTop, zBack, backU0, v0, backU1, v1, 0.0F, 0.0F, 1.0F, alpha);
      quad(consumer, matrix, light, -hw, yTop, zBack, -hw, yBottom, zBack, -hw, yBottom, zFront, -hw, yTop, zFront, frontU0, v0, frontU1, v1, -1.0F, 0.0F, 0.0F, alpha);
      quad(consumer, matrix, light, hw, yTop, zFront, hw, yBottom, zFront, hw, yBottom, zBack, hw, yTop, zBack, frontU0, v0, frontU1, v1, 1.0F, 0.0F, 0.0F, alpha);
      quad(consumer, matrix, light, -hw, yBottom, zFront, -hw, yBottom, zBack, hw, yBottom, zBack, hw, yBottom, zFront, bottomU0, 0.0F, bottomU1, topV1, 0.0F, 1.0F, 0.0F, alpha);
      quad(consumer, matrix, light, -hw, yTop, zBack, -hw, yTop, zFront, hw, yTop, zFront, hw, yTop, zBack, frontU0, 0.0F, frontU1, topV1, 0.0F, -1.0F, 0.0F, alpha);

      matrices.pop();

      this.queued.add(new Preview(capeTexture, this.indexCount, 36));
      this.indexCount += 36;
      return true;
   }

   private static void quad(VertexConsumer consumer, Matrix4f matrix, int light,
                            float x0, float y0, float z0, float x1, float y1, float z1,
                            float x2, float y2, float z2, float x3, float y3, float z3,
                            float u0, float vv0, float u1, float vv1,
                            float nx, float ny, float nz, float alpha) {
      consumer.vertex(matrix, x0, y0, z0).color(1.0F, 1.0F, 1.0F, alpha).texture(u0, vv0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
      consumer.vertex(matrix, x1, y1, z1).color(1.0F, 1.0F, 1.0F, alpha).texture(u0, vv1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
      consumer.vertex(matrix, x2, y2, z2).color(1.0F, 1.0F, 1.0F, alpha).texture(u1, vv1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
      consumer.vertex(matrix, x3, y3, z3).color(1.0F, 1.0F, 1.0F, alpha).texture(u1, vv0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(nx, ny, nz);
   }

   public void flush() {
      BufferBuilder builder = this.builder;
      BufferAllocator allocator = this.allocator;
      this.builder = null;
      this.allocator = null;
      if (builder == null) {
         this.reset();
         return;
      }

      BuiltBuffer built = null;
      try {
         built = builder.endNullable();
         if (built != null) {
            this.draw(built);
         }
      } finally {
         if (built != null) {
            built.close();
         }

         if (allocator != null) {
            allocator.close();
         }

         this.reset();
      }
   }

   public void close() {
      this.builder = null;
      if (this.allocator != null) {
         this.allocator.close();
         this.allocator = null;
      }

      if (this.uniformBuffer != null) {
         this.uniformBuffer.close();
         this.uniformBuffer = null;
      }

      if (this.uniformData != null) {
         MemoryUtil.memFree(this.uniformData);
         this.uniformData = null;
      }

      this.reset();
   }

   private void draw(BuiltBuffer built) {
      MinecraftClient client = MinecraftClient.getInstance();
      if (client.getFramebuffer() == null || this.queued.isEmpty()) {
         return;
      }

      GpuBuffer vertexBuffer = FORMAT.uploadImmediateVertexBuffer(built.getBuffer());
      RenderSystem.ShapeIndexBuffer sequential = RenderSystem.getSequentialBuffer(built.getDrawParameters().mode());
      GpuBuffer indexBuffer = sequential.getIndexBuffer(this.indexCount);
      VertexFormat.IndexType indexType = sequential.getIndexType();

      this.uploadUniforms(client);

      boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
      boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDepthMask(true);

      GpuTextureView depthView = client.getFramebuffer().getDepthAttachmentView();
      OptionalDouble clearDepth = depthView == null ? OptionalDouble.empty() : OptionalDouble.of(1.0D);

      try {
         CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();
         try (RenderPass pass = encoder.createRenderPass(
               () -> "client:cosmetic_preview",
               client.getFramebuffer().getColorAttachmentView(),
               OptionalInt.empty(),
               depthView,
               clearDepth)) {
            pass.setPipeline(PIPELINE);
            pass.setVertexBuffer(0, vertexBuffer);
            pass.setIndexBuffer(indexBuffer, indexType);
            pass.setUniform("PreviewData", this.uniformBuffer.slice());
            GpuSampler sampler = RenderSystem.getSamplerCache().get(FilterMode.NEAREST);

            for (Preview preview : this.queued) {
               GpuTextureView view = this.textureView(preview.texture());
               if (view == null) {
                  continue;
               }

               pass.bindTexture("Sampler0", view, sampler);
               pass.drawIndexed(0, preview.firstIndex(), preview.indexCount(), 1);
            }
         }
      } finally {
         if (depthTest) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
         } else {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
         }

         GL11.glDepthMask(depthMask);
      }
   }

   private void uploadUniforms(MinecraftClient client) {
      if (this.uniformBuffer == null) {
         this.uniformBuffer = RenderSystem.getDevice().createBuffer(
            () -> "client:cosmetic_preview_uniform",
            GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_COPY_DST,
            UNIFORM_SIZE
         );
      }

      if (this.uniformData == null) {
         this.uniformData = MemoryUtil.memAlloc(UNIFORM_SIZE);
      }

      this.uniformData.clear();
      this.uniformData.putFloat(client.getWindow().getFramebufferWidth() / 2.0F);
      this.uniformData.putFloat(client.getWindow().getFramebufferHeight() / 2.0F);
      this.uniformData.putFloat(0.0F);
      this.uniformData.putFloat(0.0F);
      this.uniformData.flip();
      RenderSystem.getDevice().createCommandEncoder()
            .writeToBuffer(this.uniformBuffer.slice(), this.uniformData);
   }

   private GpuTextureView textureView(Identifier textureId) {
      AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(textureId);
      if (texture == null) {
         return null;
      }

      try {
         return texture.getGlTextureView();
      } catch (IllegalStateException exception) {
         return null;
      }
   }

   private BufferBuilder consumer() {
      if (this.builder == null) {
         this.allocator = new BufferAllocator(INITIAL_BUFFER_SIZE);
         this.builder = new BufferBuilder(this.allocator, VertexFormat.DrawMode.QUADS, FORMAT);
      }

      return this.builder;
   }

   private void reset() {
      this.queued.clear();
      this.indexCount = 0;
   }

   private static int countQuads(GeoModel model) {
      int quads = 0;
      for (GeoBone bone : model.topLevelBones) {
         quads += countBoneQuads(bone);
      }

      return quads;
   }

   private static int countBoneQuads(GeoBone bone) {
      if (bone.isHidden) {
         return 0;
      }

      int quads = 0;
      for (GeoCube cube : bone.childCubes) {
         for (GeoQuad quad : cube.quads) {
            if (quad != null) {
               quads++;
            }
         }
      }

      for (GeoBone child : bone.childBones) {
         quads += countBoneQuads(child);
      }

      return quads;
   }

   private record Preview(Identifier texture, int firstIndex, int indexCount) {
   }
}
