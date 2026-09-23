package aethereal.ui.shader;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.class_1068;
import net.minecraft.class_278;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import org.joml.Vector4f;

public class TextureShader extends Shader {
   public class_278 c;
   public class_278 d;
   public class_278 e;

   public TextureShader() {
      super(class_2960.method_60655("westra", "core/rect/texture_rect"), class_290.field_1575);
   }

   @Override
   protected void b() {
      this.c = this.a("uSize");
      this.d = this.a("uRadius");
      this.e = this.a("uSmoothness");
   }

   public void a(float width, float height) {
      if (this.c != null) {
         this.c.method_1255(width, height);
      }
   }

   public void a(Vector4f radius) {
      if (this.d != null) {
         this.d.method_35657(radius.x, radius.z, radius.w, radius.y);
      }
   }

   public void a(float smoothness) {
      if (this.e != null) {
         this.e.method_1251(smoothness);
      }
   }

   public class_2960 b(String nickname) {
      UUID uuid;
      if (nickname != null && !nickname.isEmpty()) {
         uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + nickname).getBytes(StandardCharsets.UTF_8));
      } else {
         uuid = new UUID(0L, 0L);
      }

      return class_1068.method_4648(uuid).comp_1626();
   }
}
