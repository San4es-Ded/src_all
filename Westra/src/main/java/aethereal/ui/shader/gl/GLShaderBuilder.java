package aethereal.ui.shader.gl;

import aethereal.lib.log4j.LoggerFactory;
import aethereal.lib.log4j.Logger_2;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.Generated;

public class GLShaderBuilder {
   @Generated
   private static final Logger_2 a = LoggerFactory.a(GLShaderBuilder.class);
   private static final String b = "/assets/westra/shaders/fx/";
   private final GLShader c = new GLShader();

   private GLShaderBuilder() {
   }

   public static GLShaderBuilder a() {
      return new GLShaderBuilder();
   }

   public GLShaderBuilder a(String path, GLShaderBuilder.a stage) {
      String source = b(path);
      if (source != null) {
         this.c.a(source, stage.a());
      }

      return this;
   }

   public GLShaderBuilder b() {
      this.c.c();
      return this;
   }

   public GLShader c() {
      return this.c;
   }

   private static String b(String path) {
      try {
         String var2;
         try (InputStream stream = GLShaderBuilder.class.getResourceAsStream("/assets/westra/shaders/fx/" + path)) {
            if (stream == null) {
               a.f("Файл шейдера не найден: {}{}", new Object[]{"/assets/westra/shaders/fx/", path});
               return null;
            }

            var2 = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
         }

         return var2;
      } catch (IOException var6) {
         a.f("Не удалось прочитать шейдер {}: {}", new Object[]{path, var6.getMessage()});
         return null;
      }
   }

   public static enum a {
      VERTEX(35633),
      FRAGMENT(35632);

      private final int c;

      private a(int stage) {
         this.c = stage;
      }

      public int a() {
         return this.c;
      }
   }
}
