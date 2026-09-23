package wtf.wyvern.render.display.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gl.Defines;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderLoader.LoadException;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus.Internal;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.mixin.accessors.ShaderProgramAccessor;

public class GlProgram implements IMinecraft {
   private static final List<Runnable> REGISTERED_PROGRAMS = new ArrayList();
   private final Map<String, GlUniform> uniformCache = new HashMap<>();
   protected ShaderProgram backingProgram;
   public ShaderProgramKey programKey;

   public GlProgram(Identifier id, VertexFormat vertexFormat) {
      this.programKey = new ShaderProgramKey(id.withPrefixedPath("core/"), vertexFormat, Defines.EMPTY);
      REGISTERED_PROGRAMS.add(() -> {
         try {
            this.backingProgram = mc.getShaderLoader().getProgramToLoad(this.programKey);
            this.uniformCache.clear();
            this.setup();
         } catch (LoadException var2) {
            System.out.println("[Wyvern] FAILED to load shader program " + this.programKey + " : " + var2.getMessage());
         }
      });
   }

   public RenderPhase renderPhaseProgram() {
      return new RenderPhase.ShaderProgram(this.programKey);
   }

   public ShaderProgram use() {
      if (this.backingProgram == null) {

         return null;
      }
      return RenderSystem.setShader(this.programKey);
   }

   public boolean isLoaded() {
      return this.backingProgram != null;
   }

   protected void setup() {
   }

   public GlUniform findUniform(String name) {
      if (this.backingProgram == null) {

         return null;
      }
      GlUniform uniform = this.uniformCache.get(name);
      if (uniform == null) {
         uniform = (GlUniform)((ShaderProgramAccessor)this.backingProgram).getUniformsByName().get(name);
         if (uniform != null) {
            this.uniformCache.put(name, uniform);
         }
      }
      return uniform;
   }

   @Internal
   public static void loadAndSetupPrograms() {
      REGISTERED_PROGRAMS.forEach(Runnable::run);
   }
}
