package pulse.render.system;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class ClientPipelines {
   public static final RenderPipeline QUADS_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
         .withLocation("pipeline/pulse_quads")
         .withVertexShader("core/position_color")
         .withFragmentShader("core/position_color")
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.QUADS)
         .build()
   );
   public static final RenderLayer QUAD = renderLayerOf("pulse_quads", RenderSetup.builder(QUADS_PIPELINE).expectedBufferSize(4096).build());
   public static final RenderPipeline QUADS_THROUGH_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
         .withLocation("pipeline/pulse_quads_through")
         .withVertexShader("core/position_color")
         .withFragmentShader("core/position_color")
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.QUADS)
         .build()
   );
   public static final RenderLayer QUAD_THROUGH = renderLayerOf(
      "pulse_quads_through", RenderSetup.builder(QUADS_THROUGH_PIPELINE).expectedBufferSize(4096).build()
   );
   public static final RenderPipeline FILL_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
         .withLocation("pipeline/pulse_fill")
         .withVertexShader("core/position_color")
         .withFragmentShader("core/position_color")
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.QUADS)
         .build()
   );
   public static final RenderLayer FILL = renderLayerOf("pulse_fill", RenderSetup.builder(FILL_PIPELINE).expectedBufferSize(4096).build());
   public static final RenderPipeline OUTLINE_PIPELINE_NO = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
         .withLocation("pipeline/pulse_outline_lines")
         .withVertexShader("core/position_color")
         .withFragmentShader("core/position_color")
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.DEBUG_LINES)
         .build()
   );
   public static final RenderLayer OUTLINE_NO = renderLayerOf(
      "pulse_outline_lines", RenderSetup.builder(OUTLINE_PIPELINE_NO).expectedBufferSize(4096).build()
   );
   public static final RenderPipeline OUTLINE_THROUGH_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
         .withLocation("pipeline/pulse_outline_lines_through")
         .withVertexShader("core/position_color")
         .withFragmentShader("core/position_color")
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.DEBUG_LINES)
         .build()
   );
   public static final RenderLayer OUTLINE_THROUGH = renderLayerOf(
      "pulse_outline_lines_through", RenderSetup.builder(OUTLINE_THROUGH_PIPELINE).expectedBufferSize(4096).build()
   );
   public static final RenderPipeline TEXTURED_QUAD_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
         .withLocation("pipeline/pulse_tex_quad")
         .withVertexShader("core/position_tex_color")
         .withFragmentShader("core/position_tex_color")
         .withSampler("Sampler0")
         .withBlend(BlendFunction.ADDITIVE)
         .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.QUADS)
         .build()
   );
   public static final RenderPipeline TEXTURED_QUAD_THROUGH_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
         .withLocation("pipeline/pulse_tex_quad_through")
         .withVertexShader("core/position_tex_color")
         .withFragmentShader("core/position_tex_color")
         .withSampler("Sampler0")
         .withBlend(BlendFunction.ADDITIVE)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.QUADS)
         .build()
   );
   public static final RenderLayer BLOCK_STARFIELD = renderLayerOf(
      "pulse_block_starfield", RenderSetup.builder(blockShaderPipeline("block_starfield")).expectedBufferSize(4096).build()
   );
   private static final Map<Identifier, RenderLayer> TEXTURED_LAYERS = new ConcurrentHashMap<>();
   private static final Map<Identifier, RenderLayer> TEXTURED_THROUGH_LAYERS = new ConcurrentHashMap<>();
   public static final RenderPipeline SKY_NEBULA_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_AND_PROJECTION_SNIPPET")})
         .withLocation("pipeline/nebula")
         .withUniform("ShaderFogData", UniformType.UNIFORM_BUFFER)
         .withVertexShader(Identifier.of("pulse", "core/shader_fog"))
         .withFragmentShader(Identifier.of("pulse", "core/nebula"))
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .build()
   );
   public static final RenderPipeline SKY_PLASMA_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_AND_PROJECTION_SNIPPET")})
         .withLocation("pipeline/plasma")
         .withUniform("ShaderFogData", UniformType.UNIFORM_BUFFER)
         .withVertexShader(Identifier.of("pulse", "core/shader_fog"))
         .withFragmentShader(Identifier.of("pulse", "core/plasma"))
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .build()
   );
   public static final RenderPipeline SKY_BLOOM_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_AND_PROJECTION_SNIPPET")})
         .withLocation("pipeline/bloom")
         .withUniform("ShaderFogData", UniformType.UNIFORM_BUFFER)
         .withVertexShader(Identifier.of("pulse", "core/shader_fog"))
         .withFragmentShader(Identifier.of("pulse", "core/bloom"))
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .build()
   );
   public static final RenderPipeline SKY_CAUSTIC_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_AND_PROJECTION_SNIPPET")})
         .withLocation("pipeline/caustic")
         .withUniform("ShaderFogData", UniformType.UNIFORM_BUFFER)
         .withVertexShader(Identifier.of("pulse", "core/shader_fog"))
         .withFragmentShader(Identifier.of("pulse", "core/caustic"))
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .build()
   );
   public static final RenderPipeline SKY_DRAIN_PIPELINE = register(
      RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_AND_PROJECTION_SNIPPET")})
         .withLocation("pipeline/drain")
         .withUniform("ShaderFogData", UniformType.UNIFORM_BUFFER)
         .withVertexShader(Identifier.of("pulse", "core/shader_fog"))
         .withFragmentShader(Identifier.of("pulse", "core/drain"))
         .withBlend(BlendFunction.TRANSLUCENT)
         .withDepthTestFunction(DepthTestFunction.EQUAL_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withVertexFormat(VertexFormats.EMPTY, DrawMode.TRIANGLES)
         .build()
   );

   private static RenderPipeline blockShaderPipeline(String name) {
      return register(
         RenderPipeline.builder(new Snippet[]{snippet("TRANSFORMS_PROJECTION_FOG_SNIPPET"), snippet("GLOBALS_SNIPPET")})
            .withLocation("pipeline/pulse_" + name)
            .withVertexShader(Identifier.of("minecraft", "core/pulse_block_overlay"))
            .withFragmentShader(Identifier.of("minecraft", "core/pulse_" + name))
            .withBlend(BlendFunction.TRANSLUCENT)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .withDepthWrite(false)
            .withCull(false)
            .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.QUADS)
            .build()
      );
   }

   public static RenderLayer getTextureLayer(Identifier texture) {
      return TEXTURED_LAYERS.computeIfAbsent(
         texture,
         tex -> renderLayerOf(
            "pulse_tex_" + tex.getPath().replace('/', '_'),
            RenderSetup.builder(TEXTURED_QUAD_PIPELINE).texture("Sampler0", tex).expectedBufferSize(4096).build()
         )
      );
   }

   public static RenderLayer getTextureLayerThrough(Identifier texture) {
      return TEXTURED_THROUGH_LAYERS.computeIfAbsent(
         texture,
         tex -> renderLayerOf(
            "pulse_tex_through_" + tex.getPath().replace('/', '_'),
            RenderSetup.builder(TEXTURED_QUAD_THROUGH_PIPELINE).texture("Sampler0", tex).expectedBufferSize(4096).build()
         )
      );
   }

   private static RenderPipeline register(RenderPipeline pipeline) {
      try {
         Method method = RenderPipelines.class.getDeclaredMethod("register", RenderPipeline.class);
         method.setAccessible(true);
         return (RenderPipeline)method.invoke(null, pipeline);
      } catch (ReflectiveOperationException e) {
         throw new IllegalStateException("Unable to register Pulse render pipeline", e);
      }
   }

   private static Snippet snippet(String fieldName) {
      try {
         Field field = RenderPipelines.class.getDeclaredField(fieldName);
         field.setAccessible(true);
         return (Snippet)field.get(null);
      } catch (ReflectiveOperationException e) {
         throw new IllegalStateException("Unable to read Minecraft render snippet " + fieldName, e);
      }
   }

   private static RenderLayer renderLayerOf(String name, RenderSetup setup) {
      try {
         Method method = RenderLayer.class.getDeclaredMethod("of", String.class, RenderSetup.class);
         method.setAccessible(true);
         return (RenderLayer)method.invoke(null, name, setup);
      } catch (ReflectiveOperationException e) {
         throw new IllegalStateException("Unable to create Pulse render layer " + name, e);
      }
   }
}
