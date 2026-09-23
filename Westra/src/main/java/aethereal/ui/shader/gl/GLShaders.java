package aethereal.ui.shader.gl;

import java.util.HashMap;
import java.util.Map;
import lombok.Generated;

public class GLShaders {
   private static final Map<String, GLShader> a = new HashMap<>();
   private static boolean b;

   @Generated
   private GLShaders() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static GLShader a(String name) {
      if (!b) {
         b = true;
         c();
      }

      return a.get(name);
   }

   public static boolean b(String name) {
      GLShader shader = a(name);
      return shader != null && shader.b();
   }

   private static void c() {
      a("block_nebula", "fragment/block_nebula.fsh");
      a("block_starfield", "fragment/block_starfield.fsh");
      a("block_cobweb", "fragment/block_cobweb.fsh");
      a("block_plasma", "fragment/block_plasma.fsh");
      a("hand_shader", "fragment/hand_shader.fsh");
      a("rect_glow", "fragment/rect_glow.fsh");
      a("drop_shadow", "fragment/drop_shadow.fsh");
      a("gradient", "fragment/gradient.fsh");
      a("hue_bar", "fragment/hue_bar.fsh");
      a("color_picker", "fragment/color_picker.fsh");
      a("round_rect", "fragment/round_rect.fsh");
      a("round_rect_outline", "fragment/round_rect_outline.fsh");
      a("round_texture", "fragment/round_texture.fsh");
      a.put(
         "passthrough_color",
         GLShaderBuilder.a()
            .a("vertex/passthrough_color.vsh", GLShaderBuilder.a.VERTEX)
            .a("fragment/passthrough_color.fsh", GLShaderBuilder.a.FRAGMENT)
            .b()
            .c()
      );
   }

   private static void a(String name, String fragment) {
      a.put(name, GLShaderBuilder.a().a("vertex/passthrough.vsh", GLShaderBuilder.a.VERTEX).a(fragment, GLShaderBuilder.a.FRAGMENT).b().c());
   }
}
