package aethereal.ui.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.minecraft.class_10149;
import net.minecraft.class_10156;
import net.minecraft.class_278;
import net.minecraft.class_293;
import net.minecraft.class_2960;
import net.minecraft.class_5944;

public abstract class Shader {
   protected final class_10156 a;
   protected class_5944 b;

   protected abstract void b();

   @Generated
   public class_10156 c() {
      return this.a;
   }

   @Generated
   public class_5944 d() {
      return this.b;
   }

   public Shader(class_2960 identifier, class_293 vertexFormat) {
      this.a = new class_10156(identifier, vertexFormat, class_10149.field_53930);
   }

   public void a() {
      this.b = RenderSystem.setShader(this.a);
      this.b();
   }

   protected class_278 a(String name) {
      return this.b != null ? this.b.method_34582(name) : null;
   }
}
