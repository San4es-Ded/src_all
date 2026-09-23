package rockstar.client.render.compat;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.ProjectionType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.MappableRingBuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.ProjectionMatrix2;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

/**
 * Compatibility renderer for the immediate-mode meshes used by the 1.21.4 client.
 * Minecraft 1.21.11 removed BufferRenderer and global shader selection, so every
 * mesh is submitted through an explicit RenderPipeline and RenderPass here.
 */
public final class ImmediateRenderer {
   private static final Map<PipelineKey, RenderPipeline> PIPELINES = new HashMap<>();
   private static final Map<LegacyRenderState, RenderPipeline> CLEAR_PIPELINES = new HashMap<>();
   private static final Map<VertexFormat, MappableRingBuffer> GUI_VERTEX_BUFFERS = new HashMap<>();
   private static GpuTextureView texture;
   private static AbstractTexture managedTexture;
   private static FilterMode textureFilter = FilterMode.LINEAR;
   private static final Map<String, GpuTextureView> namedTextures = new HashMap<>();
   private static final Map<String, FilterMode> namedTextureFilters = new HashMap<>();
   private static Framebuffer target;
   private static LegacyShaderProgram shader;
   private static BlendFunction blend = BlendFunction.TRANSLUCENT;
   private static boolean blendEnabled;
   private static boolean cullEnabled = true;
   private static boolean writeColor = true;
   private static boolean writeAlpha = true;
   private static DepthTestFunction depthFunction = DepthTestFunction.LEQUAL_DEPTH_TEST;
   private static boolean guiPhase;
   private static final Vector4f shaderColor = new Vector4f(1.0F);
   private static boolean depthTest = true;
   private static boolean depthWrite = true;
   private static float lineWidth = 1.0F;
   private static boolean clearTargetBeforeDraw;
   private static boolean deferGuiDraws;
   private static ScreenRect guiScissor;
   private static int[] framebufferScissor;
   private static final List<PendingDraw> deferredGuiDraws = new ArrayList<>();
   private static final List<Runnable> deferredGuiOverlays = new ArrayList<>();
   private static final Matrix4f GUI_MODEL_VIEW = new Matrix4f().translation(0.0F, 0.0F, -11000.0F);
   private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
   private static final Vector3f ZERO_VECTOR = new Vector3f();
   private static final Vector4f WHITE_COLOR = new Vector4f(1.0F);
   private static final Map<Integer, ProjectionMatrix2> GUI_PROJECTIONS = new HashMap<>();

   private ImmediateRenderer() {
   }

   public static void setTexture(Identifier id) {
      managedTexture = MinecraftClient.getInstance().getTextureManager().getTexture(id);
      texture = managedTexture.getGlTextureView();
      textureFilter = FilterMode.LINEAR;
   }

   public static void setTexture(GpuTextureView view) {
      setTexture(view, FilterMode.LINEAR);
   }

   public static void setTexture(GpuTextureView view, FilterMode filter) {
      managedTexture = null;
      texture = view;
      textureFilter = filter;
   }

   public static void setTexture(String sampler, GpuTextureView view, FilterMode filter) {
      namedTextures.put(sampler, view);
      namedTextureFilters.put(sampler, filter);
   }

   public static void clearTexture(String sampler) {
      namedTextures.remove(sampler);
      namedTextureFilters.remove(sampler);
   }

   public static void clearTexture() {
      managedTexture = null;
      texture = null;
      textureFilter = FilterMode.LINEAR;
   }

   public static void setTarget(Framebuffer framebuffer) {
      target = framebuffer;
   }

   public static void clearTarget() {
      target = null;
   }

   public static void clearTargetBeforeDraw() {
      clearTargetBeforeDraw = true;
   }

   static void useShader(LegacyShaderProgram value) {
      shader = value;
   }

   public static void clearShader() {
      shader = null;
   }

   public static void setBlend(BlendFunction value) {
      blend = value;
   }

   public static void defaultBlend() {
      blend = BlendFunction.TRANSLUCENT;
   }

   public static void enableBlend(boolean enabled) { blendEnabled = enabled; }
   public static boolean isBlendEnabled() { return blendEnabled; }
   public static void enableCull(boolean enabled) { cullEnabled = enabled; }
   public static boolean isCullEnabled() { return cullEnabled; }
   public static boolean isDepthTestEnabled() { return depthTest; }
   public static void setDepthFunction(DepthTestFunction value) { depthFunction = value; }
   public static void colorMask(boolean color, boolean alpha) { writeColor = color; writeAlpha = alpha; }
   public static void setGuiPhase(boolean value) { guiPhase = value; }

   public static LegacyRenderState captureState() {
      return captureState(false);
   }

   public static void restoreState(LegacyRenderState state) {
      blendEnabled = state.blend() != null;
      if (state.blend() != null) {
         blend = state.blend();
      }
      depthTest = state.depthTest() != DepthTestFunction.NO_DEPTH_TEST;
      depthFunction = state.depthTest();
      depthWrite = state.depthWrite();
      cullEnabled = state.cull();
      writeColor = state.writeColor();
      writeAlpha = state.writeAlpha();
   }

   private static LegacyRenderState captureState(boolean forceGui) {
      return new LegacyRenderState(blendEnabled ? blend : null,
         !forceGui && depthTest ? depthFunction : DepthTestFunction.NO_DEPTH_TEST,
         !forceGui && depthTest && depthWrite, cullEnabled, writeColor, writeAlpha);
   }

   public static void setShaderColor(float red, float green, float blue, float alpha) {
      shaderColor.set(red, green, blue, alpha);
   }

   public static float[] getShaderColor() {
      return new float[]{shaderColor.x, shaderColor.y, shaderColor.z, shaderColor.w};
   }

   public static void enableDepthTest() {
      depthTest = true;
   }

   public static void disableDepthTest() {
      depthTest = false;
   }

   public static void depthMask(boolean writeDepth) {
      depthWrite = writeDepth;
   }

   public static void setLineWidth(float width) {
      lineWidth = Float.isFinite(width) ? Math.max(0.01F, Math.min(width, 64.0F)) : 1.0F;
   }

   public static void clearDepth() {
      Framebuffer framebuffer = target != null ? target : MinecraftClient.getInstance().getFramebuffer();
      if (!framebuffer.useDepthAttachment) {
         return;
      }

      try (RenderPass ignored = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
         () -> "Rockstar clear depth",
         framebuffer.getColorAttachmentView(),
         OptionalInt.empty(),
         framebuffer.getDepthAttachmentView(),
         OptionalDouble.of(1.0)
      )) {
      }
   }

   /** Clear now, preserving the legacy channel mask and scissor rectangle. */
   public static void clearColor(int color) {
      Framebuffer framebuffer = target != null ? target : MinecraftClient.getInstance().getFramebuffer();
      if (writeColor && writeAlpha && framebufferScissor == null && guiScissor == null) {
         RenderSystem.getDevice().createCommandEncoder().clearColorTexture(framebuffer.getColorAttachment(), color);
         return;
      }
      LegacyRenderState state = new LegacyRenderState(null, DepthTestFunction.NO_DEPTH_TEST, false, false, writeColor, writeAlpha);
      RenderPipeline clearPipeline = CLEAR_PIPELINES.computeIfAbsent(state, key -> RenderPipelines.register(
         key.apply(RenderPipeline.builder()).withLocation(Identifier.of("rockstar", "pipeline/clear_" + CLEAR_PIPELINES.size()))
            .withVertexShader(Identifier.of("rockstar", "core/immediate/clear"))
            .withFragmentShader(Identifier.of("rockstar", "core/immediate/clear"))
            .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS).build()));
      var builder = net.minecraft.client.render.Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
      builder.vertex(-1, -1, 0).color(color);
      builder.vertex(1, -1, 0).color(color);
      builder.vertex(1, 1, 0).color(color);
      builder.vertex(-1, 1, 0).color(color);
      BuiltBuffer mesh = builder.end();
      executeSingleDraw(new PendingDraw(mesh, mesh.getDrawParameters(), VertexFormats.POSITION_COLOR, clearPipeline,
         framebuffer, IDENTITY_MATRIX, 0, WHITE_COLOR, null, null, null, null, FilterMode.NEAREST,
         Map.of(), Map.of(), guiScissor, framebufferScissor == null ? null : framebufferScissor.clone(), false), false);
   }

   public static void beginGuiDeferral() {
      deferGuiDraws = true;
      guiScissor = null;
      deferredGuiDraws.clear();
      deferredGuiOverlays.clear();
   }

   public static void setGuiScissor(ScreenRect scissor) {
      guiScissor = scissor;
      framebufferScissor = null;
   }

   public static void setFramebufferScissor(int x, int y, int width, int height) {
      framebufferScissor = new int[]{x, y, Math.max(0, width), Math.max(0, height)};
      guiScissor = null;
   }

   public static void clearScissor() {
      guiScissor = null;
      framebufferScissor = null;
   }

   public static void deferGuiOverlay(Runnable overlay) {
      deferredGuiOverlays.add(overlay);
   }

   public static void beginGuiOverlayCollection() {
      deferredGuiOverlays.clear();
   }

   public static boolean hasDeferredGuiOverlays() {
      return !deferredGuiOverlays.isEmpty();
   }

   public static boolean isDeferringGui() {
      return deferGuiDraws;
   }

   public static void extractDeferredGuiOverlays() {
      if (deferredGuiOverlays.isEmpty()) {
         return;
      }

      List<Runnable> overlays = List.copyOf(deferredGuiOverlays);
      deferredGuiOverlays.clear();
      for (Runnable overlay : overlays) {
         overlay.run();
      }
   }

   public static void flushGuiDeferral() {
      deferGuiDraws = false;
      if (deferredGuiDraws.isEmpty()) {
         LegacyShaderProgram.rotateUniformBuffers();
         return;
      }

      RenderSystem.backupProjectionMatrix();
      try {
         executeDraws(deferredGuiDraws);
      } finally {
         RenderSystem.restoreProjectionMatrix();
         deferredGuiDraws.clear();
         LegacyShaderProgram.rotateUniformBuffers();
      }
   }

   public static void draw(BuiltBuffer buffer) {
      draw(buffer, false);
   }

   /** Draws an off-screen world pass even while the surrounding screen batches GUI geometry. */
   public static void drawWorldImmediate(BuiltBuffer buffer) {
      drawWorldImmediate(buffer, null);
   }

   /**
    * Draws world geometry through a vanilla pipeline.  This is used by the
    * headless view for real block models (atlas texture, lightmap and alpha
    * cutout) instead of Rockstar's legacy position/color shader.
    */
   public static void drawWorldImmediate(BuiltBuffer buffer, RenderPipeline pipeline) {
      boolean wasDeferring = deferGuiDraws;
      ScreenRect previousScissor = guiScissor;
      int[] previousFramebufferScissor = framebufferScissor;
      deferGuiDraws = false;
      clearScissor();
      try {
         draw(buffer, false, pipeline);
      } finally {
         deferGuiDraws = wasDeferring;
         guiScissor = previousScissor;
         framebufferScissor = previousFramebufferScissor;
      }
   }

   public static void drawGui(BuiltBuffer buffer) {
      draw(buffer, true);
   }

   private static void draw(BuiltBuffer buffer, boolean forceGui) {
      draw(buffer, forceGui, null);
   }

   private static void draw(BuiltBuffer buffer, boolean forceGui, RenderPipeline pipelineOverride) {
      BuiltBuffer.DrawParameters parameters = buffer.getDrawParameters();
      VertexFormat format = parameters.format();
      LegacyShaderProgram selectedShader = shader;
      if (selectedShader == null
         && format.equals(VertexFormats.POSITION_COLOR)
         && lineWidth > 1.0F
         && (parameters.mode() == VertexFormat.DrawMode.DEBUG_LINES
            || parameters.mode() == VertexFormat.DrawMode.DEBUG_LINE_STRIP)) {
         buffer = convertLegacyLines(buffer, lineWidth);
         parameters = buffer.getDrawParameters();
         format = parameters.format();
      }
      // 1.21.11 has several textured formats (fonts, lightmapped UI, item and
      // entity vertices).  Treating only POSITION_TEXTURE_COLOR as textured
      // silently selected the color-only shader for every other UV0 format.
      boolean textured = format.contains(VertexFormatElement.UV0);
      BlendFunction selectedBlend = blendEnabled ? blend : null;
      // Shader identity does not determine coordinate space: the same rounded,
      // textured and font shaders are used by both world effects and the HUD.
      boolean guiDraw = forceGui || deferGuiDraws;
      LegacyRenderState selectedState = captureState(guiDraw);
      RenderPipeline pipeline;
      if (pipelineOverride != null) {
         pipeline = pipelineOverride;
      } else if (selectedShader != null) {
         pipeline = selectedShader.pipeline(selectedState, parameters.mode());
      } else if (!MinecraftClient.getInstance().isFinishedLoading()) {
         pipeline = bootstrapPipeline(format, parameters.mode(), selectedBlend, guiDraw ? false : depthTest, guiDraw ? false : depthWrite);
         if (pipeline == null) {
            buffer.close();
            return;
         }
      } else {
         pipeline = pipeline(format, parameters.mode(), textured, selectedState);
      }
      Framebuffer framebuffer = target != null ? target : MinecraftClient.getInstance().getFramebuffer();

      // 1.21.11's GUI projection spans z=-1000..-11000 and vanilla submits
      // every 2D layer with this translation.  Legacy Rockstar vertices use z=0,
      // so without matching it the draw succeeds but is clipped completely.
      Matrix4f modelView = guiDraw ? GUI_MODEL_VIEW : rockstar.client.compat.RenderSystem.getModelViewMatrix();
      int guiScale = guiDraw ? Math.max(1, MinecraftClient.getInstance().getWindow().getScaleFactor()) : 0;
      Vector4f color = shaderColor.equals(WHITE_COLOR) ? WHITE_COLOR : new Vector4f(shaderColor);
      RenderAudit.record(buffer, pipeline, framebuffer, modelView, guiPhase || guiDraw, color);
      boolean dedicatedCustomUniforms = selectedShader != null
         && (!(guiDraw || guiPhase) || selectedShader.usesDedicatedUniformBuffer());
      GpuBuffer ownedCustomUniforms = dedicatedCustomUniforms ? selectedShader.createUniformBuffer() : null;
      GpuBufferSlice customUniforms = selectedShader == null
         ? null
         : dedicatedCustomUniforms ? ownedCustomUniforms.slice() : selectedShader.createUniformSlice();
      GpuTextureView primaryTexture = texture;
      AbstractTexture primaryManagedTexture = managedTexture;
      FilterMode primaryTextureFilter = textureFilter;
      Map<String, GpuTextureView> textures = namedTextures.isEmpty() ? Map.of() : Map.copyOf(namedTextures);
      Map<String, FilterMode> filters = namedTextureFilters.isEmpty() ? Map.of() : Map.copyOf(namedTextureFilters);
      ScreenRect selectedScissor = guiScissor;
      int[] selectedFramebufferScissor = framebufferScissor == null ? null : framebufferScissor.clone();
      boolean selectedClearTarget = clearTargetBeforeDraw;
      clearTargetBeforeDraw = false;

      PendingDraw draw = new PendingDraw(
         buffer,
         parameters,
         format,
         pipeline,
         framebuffer,
         modelView,
         guiScale,
         color,
         customUniforms,
         ownedCustomUniforms,
         primaryTexture,
         primaryManagedTexture,
         primaryTextureFilter,
         textures,
         filters,
         selectedScissor,
         selectedFramebufferScissor,
         selectedClearTarget
      );
      if (deferGuiDraws) {
         deferredGuiDraws.add(draw);
      } else {
         executeSingleDraw(draw, true);
      }
   }

   /**
    * The forward-compatible 1.21.11 OpenGL context rejects glLineWidth values
    * above one. Vanilla now expands wide lines in its rendertype_lines shader,
    * so adapt old POSITION_COLOR line buffers to that format at submission.
    */
   private static BuiltBuffer convertLegacyLines(BuiltBuffer source, float width) {
      BuiltBuffer.DrawParameters sourceParameters = source.getDrawParameters();
      ByteBuffer bytes = source.getBuffer().duplicate().order(ByteOrder.nativeOrder());
      int stride = sourceParameters.format().getVertexSize();
      int vertexCount = sourceParameters.vertexCount();
      BufferBuilder builder = Tessellator.getInstance().begin(
         VertexFormat.DrawMode.LINES,
         VertexFormats.POSITION_COLOR_NORMAL_LINE_WIDTH
      );

      if (sourceParameters.mode() == VertexFormat.DrawMode.DEBUG_LINE_STRIP) {
         for (int vertex = 0; vertex + 1 < vertexCount; vertex++) {
            appendWideLine(builder, bytes, stride, vertex, vertex + 1, width);
         }
      } else {
         for (int vertex = 0; vertex + 1 < vertexCount; vertex += 2) {
            appendWideLine(builder, bytes, stride, vertex, vertex + 1, width);
         }
      }

      BuiltBuffer converted = builder.endNullable();
      source.close();
      if (converted == null) {
         throw new IllegalStateException("Legacy line conversion produced no vertices");
      }
      return converted;
   }

   private static void appendWideLine(
      BufferBuilder builder,
      ByteBuffer bytes,
      int stride,
      int first,
      int second,
      float width
   ) {
      int firstOffset = first * stride;
      int secondOffset = second * stride;
      float x1 = bytes.getFloat(firstOffset);
      float y1 = bytes.getFloat(firstOffset + 4);
      float z1 = bytes.getFloat(firstOffset + 8);
      float x2 = bytes.getFloat(secondOffset);
      float y2 = bytes.getFloat(secondOffset + 4);
      float z2 = bytes.getFloat(secondOffset + 8);
      float dx = x2 - x1;
      float dy = y2 - y1;
      float dz = z2 - z1;
      float length = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
      if (length > 1.0E-6F) {
         dx /= length;
         dy /= length;
         dz /= length;
      } else {
         dx = 1.0F;
         dy = 0.0F;
         dz = 0.0F;
      }

      appendWideLineVertex(builder, bytes, firstOffset, x1, y1, z1, dx, dy, dz, width);
      appendWideLineVertex(builder, bytes, secondOffset, x2, y2, z2, dx, dy, dz, width);
   }

   private static void appendWideLineVertex(
      BufferBuilder builder,
      ByteBuffer bytes,
      int offset,
      float x,
      float y,
      float z,
      float normalX,
      float normalY,
      float normalZ,
      float width
   ) {
      int red = Byte.toUnsignedInt(bytes.get(offset + 12));
      int green = Byte.toUnsignedInt(bytes.get(offset + 13));
      int blue = Byte.toUnsignedInt(bytes.get(offset + 14));
      int alpha = Byte.toUnsignedInt(bytes.get(offset + 15));
      builder.vertex(x, y, z)
         .color(red, green, blue, alpha)
         .normal(normalX, normalY, normalZ)
         .lineWidth(width);
   }

   private static void executeDraws(List<PendingDraw> draws) {
      // Keep the control path deliberately simple while the 1.21.11 GUI port
      // is validated. Each draw keeps the GUI scale that was active when its
      // vertices were authored (ScaleUtil temporarily forces scale 2).
      for (PendingDraw pending : draws) {
         executeSingleDraw(pending, false);
      }
   }

   private static void executeSingleDraw(PendingDraw pending, boolean manageProjection) {
      PreparedDraw prepared = prepareDraw(pending);
      Framebuffer framebuffer = pending.framebuffer();
      if (manageProjection && pending.guiScale() > 0) {
         RenderSystem.backupProjectionMatrix();
      }
      try {
         if (pending.guiScale() > 0) {
            applyGuiProjection(pending.guiScale());
         }
         try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
            () -> "Rockstar immediate draw",
            framebuffer.getColorAttachmentView(),
            pending.clearTarget() ? OptionalInt.of(0) : OptionalInt.empty(),
            framebuffer.useDepthAttachment ? framebuffer.getDepthAttachmentView() : null,
            OptionalDouble.empty()
         )) {
            executeDraw(pass, prepared);
         }
      } finally {
         if (manageProjection && pending.guiScale() > 0) {
            RenderSystem.restoreProjectionMatrix();
         }
         if (pending.ownedCustomUniforms() != null) {
            RenderSystem.queueFencedTask(pending.ownedCustomUniforms()::close);
         }
      }
   }

   private static PreparedDraw prepareDraw(PendingDraw draw) {
      try (BuiltBuffer buffer = draw.buffer()) {
         // DynamicTransforms is backed by Minecraft's per-frame ring buffer.  It
         // must be allocated immediately before this pass: the vanilla GUI pass
         // runs between extraction and our deferred draw and may reuse the slice.
         GpuBufferSlice transforms = RenderSystem.getDynamicUniforms().write(draw.modelView(), draw.color(), ZERO_VECTOR, IDENTITY_MATRIX);
         GpuBuffer vertexBuffer = draw.format().uploadImmediateVertexBuffer(buffer.getBuffer());
         GpuBuffer indexBuffer;
         VertexFormat.IndexType indexType;
         if (buffer.getSortedBuffer() == null) {
            RenderSystem.ShapeIndexBuffer sequential = RenderSystem.getSequentialBuffer(draw.parameters().mode());
            indexBuffer = sequential.getIndexBuffer(draw.parameters().indexCount());
            indexType = sequential.getIndexType();
         } else {
            indexBuffer = draw.format().uploadImmediateIndexBuffer(buffer.getSortedBuffer());
            indexType = draw.parameters().indexType();
         }
         return new PreparedDraw(draw, transforms, vertexBuffer, indexBuffer, indexType, 0);
      }
   }

   private static void executeDraw(RenderPass pass, PreparedDraw prepared) {
         PendingDraw draw = prepared.pending();
            pass.setPipeline(draw.pipeline());
            if (draw.framebufferScissor() != null) {
               int[] scissor = draw.framebufferScissor();
               pass.enableScissor(scissor[0], scissor[1], scissor[2], scissor[3]);
            } else if (draw.scissor() != null) {
               int scissorScale = draw.guiScale() > 0
                  ? draw.guiScale()
                  : Math.max(1, MinecraftClient.getInstance().getWindow().getScaleFactor());
               int scissorX = draw.scissor().getLeft() * scissorScale;
               int scissorY = draw.framebuffer().textureHeight - draw.scissor().getBottom() * scissorScale;
               pass.enableScissor(
                  scissorX,
                  scissorY,
                  Math.max(0, draw.scissor().width() * scissorScale),
                  Math.max(0, draw.scissor().height() * scissorScale)
               );
            } else {
               // Render-pass state is not guaranteed to start with scissoring
               // disabled. Priority popups follow clipped module draws, so an
               // explicit reset is required to keep dropdowns stable.
               pass.disableScissor();
            }
            RenderSystem.bindDefaultUniforms(pass);
            pass.setUniform("DynamicTransforms", prepared.transforms());
            if (draw.customUniforms() != null) {
               pass.setUniform("RockstarData", draw.customUniforms());
            }
            pass.setVertexBuffer(0, prepared.vertexBuffer());
            for (String sampler : draw.pipeline().getSamplers()) {
               GpuTextureView samplerTexture = draw.textures().get(sampler);
               if (samplerTexture != null) {
                  pass.bindTexture(
                     sampler,
                     samplerTexture,
                     RenderSystem.getSamplerCache().get(draw.filters().getOrDefault(sampler, FilterMode.LINEAR))
                  );
               } else if (("Sampler0".equals(sampler) || draw.pipeline().getSamplers().size() == 1)
                  && draw.primaryTexture() != null) {
                  pass.bindTexture(
                     sampler,
                     draw.primaryTexture(),
                     draw.primaryManagedTexture() != null
                        ? draw.primaryManagedTexture().getSampler()
                        : RenderSystem.getSamplerCache().get(draw.primaryTextureFilter())
                  );
               }
            }
            pass.setIndexBuffer(prepared.indexBuffer(), prepared.indexType());
            pass.drawIndexed(prepared.baseVertex(), 0, draw.parameters().indexCount(), 1);
   }

   private static void applyGuiProjection(int scale) {
      MinecraftClient client = MinecraftClient.getInstance();
      ProjectionMatrix2 projection = GUI_PROJECTIONS.computeIfAbsent(
         scale,
         key -> new ProjectionMatrix2("Rockstar gui scale " + key, 1000.0F, 11000.0F, true)
      );
      RenderSystem.setProjectionMatrix(
         projection.set(
            (float) client.getWindow().getFramebufferWidth() / scale,
            (float) client.getWindow().getFramebufferHeight() / scale
         ),
         ProjectionType.ORTHOGRAPHIC
      );
   }

   private static RenderPipeline pipeline(
      VertexFormat format,
      VertexFormat.DrawMode mode,
      boolean textured,
      LegacyRenderState state
   ) {
      PipelineKey stateKey = new PipelineKey(format, mode, textured, state);
      return PIPELINES.computeIfAbsent(stateKey, ignored -> {
         boolean lineFormat = format.equals(VertexFormats.POSITION_COLOR_NORMAL_LINE_WIDTH);
         RenderPipeline.Builder builder = state.apply(RenderPipeline.builder(
               lineFormat ? RenderPipelines.RENDERTYPE_LINES_SNIPPET : RenderPipelines.TRANSFORMS_AND_PROJECTION_SNIPPET
            ))
            .withLocation(Identifier.of("rockstar", "pipeline/immediate_" + PIPELINES.size()))
            .withVertexFormat(format, mode);
         if (!lineFormat) {
            Identifier shaderId = Identifier.of("rockstar", textured ? "core/immediate/position_tex_color" : "core/immediate/position_color");
            builder.withVertexShader(shaderId).withFragmentShader(shaderId);
         }
         if (textured) {
            builder.withSampler("Sampler0");
         }
         return RenderPipelines.register(builder.build());
      });
   }

   /**
    * Resource-backed pipelines cannot be registered while the initial reload
    * screen is already drawing: ShaderLoader has not published its source
    * cache yet. Reuse an equivalent vanilla bootstrap pipeline for those few
    * frames, then switch to Rockstar's exact-state pipelines after loading.
    */
   private static RenderPipeline bootstrapPipeline(
      VertexFormat format,
      VertexFormat.DrawMode mode,
      BlendFunction blendFunction,
      boolean useDepthTest,
      boolean writeDepth
   ) {
      if (mode != VertexFormat.DrawMode.QUADS || !BlendFunction.TRANSLUCENT.equals(blendFunction) || useDepthTest || writeDepth) {
         return null;
      }
      if (format.equals(VertexFormats.POSITION_TEXTURE_COLOR)) {
         return RenderPipelines.FIRE_SCREEN_EFFECT;
      }
      if (format.equals(VertexFormats.POSITION_COLOR)) {
         return RenderPipelines.GUI;
      }
      return null;
   }

   private record PipelineKey(VertexFormat format, VertexFormat.DrawMode mode, boolean textured, LegacyRenderState state) {
   }

   private record PendingDraw(
      BuiltBuffer buffer,
      BuiltBuffer.DrawParameters parameters,
      VertexFormat format,
      RenderPipeline pipeline,
      Framebuffer framebuffer,
      Matrix4f modelView,
      int guiScale,
      Vector4f color,
      GpuBufferSlice customUniforms,
      GpuBuffer ownedCustomUniforms,
      GpuTextureView primaryTexture,
      AbstractTexture primaryManagedTexture,
      FilterMode primaryTextureFilter,
      Map<String, GpuTextureView> textures,
      Map<String, FilterMode> filters,
      ScreenRect scissor,
      int[] framebufferScissor,
      boolean clearTarget
   ) {
   }

   private record PreparedDraw(
      PendingDraw pending,
      GpuBufferSlice transforms,
      GpuBuffer vertexBuffer,
      GpuBuffer indexBuffer,
      VertexFormat.IndexType indexType,
      int baseVertex
   ) {
   }
}
