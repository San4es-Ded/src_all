package aethereal.module.combat;

import aethereal.config.ThemeInfo;
import aethereal.core.EventTarget;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaseHandler;
import aethereal.handler.Handler_2;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Handler_2
public class AuraHandler extends BaseHandler implements Interface {
   private final Vector3f[] b = new Vector3f[]{
      new Vector3f(0.0F, 1.5F, 0.0F),
      new Vector3f(0.0F, -1.5F, 0.0F),
      new Vector3f(1.0F, 0.0F, 0.0F),
      new Vector3f(-1.0F, 0.0F, 0.0F),
      new Vector3f(0.0F, 0.0F, 1.0F),
      new Vector3f(0.0F, 0.0F, -1.0F)
   };
   private final int[][] c = new int[][]{{0, 4, 2}, {0, 3, 4}, {0, 5, 3}, {0, 2, 5}, {1, 2, 4}, {1, 4, 3}, {1, 3, 5}, {1, 5, 2}};
   private final float[] d = new float[]{1.0F, 0.8F, 0.6F, 0.9F, 0.7F, 0.5F, 0.4F, 0.6F};
   private final AnimationUtil e = new AnimationUtil();
   private class_1309 f;

   @Generated
   public AnimationUtil a() {
      return this.e;
   }

   @EventTarget
   public void a(DrawEvent event) {
      this.e.a(0.0F, 1.0F, 0.2F, EasingList.g, event.g());
      float moving = (float)(System.currentTimeMillis() % 360000L) / 2.5F + this.e.c();
      if (event.c() && this.f != null) {
         float anim = this.e.c();
         if (anim > 0.0F) {
            int themeColor = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();
            class_243 renderPos = this.a(this.f);
            float ringWidth = this.f.method_17681() * 1.5F;
            float ringScale = 1.25F - 0.5F * anim;
            this.b();
            if (Westra.h().d().t().B().r().l("Круг")) {
               this.a(event.h(), renderPos, ColorUtil.a(themeColor, anim));
            } else {
               this.a(event.h(), renderPos, ringWidth, ringScale, moving, ColorUtil.a(themeColor, anim));
               this.a(event.h(), renderPos, ringWidth, ringScale, moving, ColorUtil.a(themeColor, anim * 0.2F), anim);
            }

            this.c();
         }
      }
   }

   @EventTarget
   public void a(GlobalEvent event) {
      if (aM_.field_1724 != null) {
         Westra.h().d().t().B().b++;
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      Aura aura = Westra.h().d().t().B();
      class_1309 current = aura.s() != null ? aura.s() : Westra.h().d().t().X().s();
      boolean changed = current != null && this.f != null && current != this.f;
      boolean visible = current != null && !changed;
      if (visible) {
         this.f = current;
      }

      this.e.a(visible);
      if (!visible && this.e.a() <= 0.0F) {
         this.f = changed ? current : null;
      }
   }

   private void a(class_4587 stack, class_243 renderPos, float ringWidth, float ringScale, float moving, int color) {
      RenderSystem.setShader(class_10142.field_53876);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27379, class_290.field_1576);
      class_243 cam = aM_.method_1561().field_4686.method_19326();
      class_243 targetCenter = this.f.method_19538().method_1031(0.0, this.f.method_17682() / 2.0, 0.0);

      for (int i = 0; i < 360; i += 20) {
         float angle = (float)Math.toRadians(i + moving * 0.3F);
         float offsetX = (float)Math.sin(angle) * ringWidth * ringScale;
         float offsetZ = (float)Math.cos(angle) * ringWidth * ringScale;
         float offsetY = 0.1F + this.f.method_17682() * Math.abs((float)Math.sin(i));
         class_243 crystalPos = renderPos.method_1031(offsetX, offsetY, offsetZ);
         stack.method_22903();
         stack.method_22904(crystalPos.method_10216() - cam.field_1352, crystalPos.method_10214() - cam.field_1351, crystalPos.method_10215() - cam.field_1350);
         stack.method_22907(
            new Quaternionf()
               .rotationTo(
                  new Vector3f(0.0F, 1.0F, 0.0F),
                  new Vector3f(
                        (float)(targetCenter.field_1352 - crystalPos.method_10216()),
                        (float)(targetCenter.field_1351 - crystalPos.method_10214()),
                        (float)(targetCenter.field_1350 - crystalPos.method_10215())
                     )
                     .normalize()
               )
         );
         stack.method_22905(0.1F, 0.1F, 0.1F);
         this.a(stack.method_23760().method_23761(), buffer, color);
         stack.method_22909();
      }

      class_286.method_43433(buffer.method_60800());
   }

   private void a(Matrix4f matrix, class_287 buffer, int color) {
      int[] rgba = ColorUtil.b(color);
      int red = rgba[0];
      int green = rgba[1];
      int blue = rgba[2];
      int alpha = rgba[3];

      for (int i = 0; i < this.c.length; i++) {
         int[] face = this.c[i];
         float brightness = this.d[i];
         int shaded = alpha << 24
            | Math.min(255, (int)(red * brightness)) << 16
            | Math.min(255, (int)(green * brightness)) << 8
            | Math.min(255, (int)(blue * brightness));

         for (int v = 0; v < 3; v++) {
            Vector3f vertex = this.b[face[v]];
            buffer.method_22918(matrix, vertex.x, vertex.y, vertex.z).method_39415(shaded);
         }
      }
   }

   private void a(class_4587 stack, class_243 renderPos, float ringWidth, float ringScale, float moving, int color, float anim) {
      int[] rgba = ColorUtil.b(color);
      int red = rgba[0];
      int green = rgba[1];
      int blue = rgba[2];
      int alpha = rgba[3];
      RenderSystem.setShaderTexture(0, class_2960.method_60655("westra", "pictures/bloom.png"));
      RenderSystem.setShader(class_10142.field_53880);
      this.a(stack, renderPos, ringWidth, ringScale, moving, 1.5F * anim, red, green, blue, alpha);
      this.a(stack, renderPos, ringWidth, ringScale, moving, 0.6F * anim, red, green, blue, alpha);
      RenderSystem.setShaderTexture(0, 0);
   }

   private void a(class_4587 stack, class_243 renderPos, float ringWidth, float ringScale, float moving, float size, int red, int green, int blue, int alpha) {
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      Quaternionf cameraRotation = aM_.field_1773.method_19418().method_23767();
      class_243 cam = aM_.method_1561().field_4686.method_19326();
      float half = size / 2.0F;

      for (int i = 0; i < 360; i += 20) {
         float angle = (float)Math.toRadians(i + moving * 0.3F);
         float offsetX = (float)Math.sin(angle) * ringWidth * ringScale;
         float offsetZ = (float)Math.cos(angle) * ringWidth * ringScale;
         float offsetY = 0.1F + this.f.method_17682() * Math.abs((float)Math.sin(i));
         stack.method_22903();
         stack.method_22904(
            renderPos.method_10216() + offsetX - cam.field_1352,
            renderPos.method_10214() + offsetY - cam.field_1351,
            renderPos.method_10215() + offsetZ - cam.field_1350
         );
         stack.method_22907(cameraRotation);
         Matrix4f matrix = stack.method_23760().method_23761();
         buffer.method_22918(matrix, -half, -half, 0.0F).method_22913(0.0F, 0.0F).method_1336(red, green, blue, alpha);
         buffer.method_22918(matrix, -half, half, 0.0F).method_22913(0.0F, 1.0F).method_1336(red, green, blue, alpha);
         buffer.method_22918(matrix, half, half, 0.0F).method_22913(1.0F, 1.0F).method_1336(red, green, blue, alpha);
         buffer.method_22918(matrix, half, -half, 0.0F).method_22913(1.0F, 0.0F).method_1336(red, green, blue, alpha);
         stack.method_22909();
      }

      class_286.method_43433(buffer.method_60800());
   }

   private void a(class_4587 stack, class_243 renderPos, int color) {
      RenderSystem.setShader(class_10142.field_53876);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      Matrix4f matrix = stack.method_23760().method_23761();
      class_243 cam = aM_.method_1561().field_4686.method_19326();
      float height = this.f.method_17682() + 0.15F;
      float radius = this.f.method_17681() * 0.8F;
      double time = System.currentTimeMillis() % 1750.0;
      boolean inverted = time > 875.0;
      double progress = time / 875.0;
      double progress2 = inverted ? progress - 1.0 : 1.0 - progress;
      double ease = progress2 < 0.5 ? 2.0 * progress2 * progress2 : 1.0 - Math.pow(-2.0 * progress2 + 2.0, 2.0) / 2.0;
      float y = (float)(renderPos.method_10214() - cam.field_1351 + height * ease);
      float offset = (float)(height * 0.8000001435473696 * Math.min(ease, 1.0 - ease) * (inverted ? -1.0 : 1.0));
      int[] c = ColorUtil.b(color);
      float r = c[0] / 255.0F;
      float g = c[1] / 255.0F;
      float b = c[2] / 255.0F;
      float a = c[3] / 255.0F;
      double cx = renderPos.method_10216() - cam.field_1352;
      double cz = renderPos.method_10215() - cam.field_1350;
      class_287 skirt = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);

      for (int deg = 0; deg <= 360; deg++) {
         double rad = Math.toRadians(deg);
         float x = (float)(cx + Math.cos(rad) * radius);
         float z = (float)(cz + Math.sin(rad) * radius);
         skirt.method_22918(matrix, x, y, z).method_22915(r, g, b, a * 0.55F);
         skirt.method_22918(matrix, x, y + offset, z).method_22915(r, g, b, 0.0F);
      }

      class_286.method_43433(skirt.method_60800());
      class_287 outline = class_289.method_1348().method_60827(class_5596.field_29344, class_290.field_1576);

      for (int deg2 = 0; deg2 < 360; deg2++) {
         double a0 = Math.toRadians(deg2);
         double a1 = Math.toRadians(deg2 + 1);
         outline.method_22918(matrix, (float)(cx + Math.cos(a0) * radius), y, (float)(cz + Math.sin(a0) * radius)).method_22915(r, g, b, a);
         outline.method_22918(matrix, (float)(cx + Math.cos(a1) * radius), y, (float)(cz + Math.sin(a1) * radius)).method_22915(r, g, b, a);
      }

      class_286.method_43433(outline.method_60800());
   }

   private void b() {
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE);
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
   }

   private void c() {
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private class_243 a(class_1309 target) {
      float tickDelta = class_310.method_1551().method_61966().method_60637(false);
      return new class_243(
         class_3532.method_16436(tickDelta, target.field_6014, target.method_23317()),
         class_3532.method_16436(tickDelta, target.field_6036, target.method_23318()),
         class_3532.method_16436(tickDelta, target.field_5969, target.method_23321())
      );
   }
}
