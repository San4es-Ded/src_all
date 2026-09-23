package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

@ModuleRegister(
   a = "Block Overlay",
   b = "Выделяет блок, на который наведён прицел",
   c = Category.Render
)
public class BlockOverlay extends Module {
   private static final int RESOLUTION = 12;
   private final BooleanSetting b = new BooleanSetting("Обводка", true);
   private final SliderSetting c = new SliderSetting("Толщина линий", 2.0F, 1.0F, 5.0F, 0.5F).a(() -> this.b.c());
   private final BooleanSetting d = new BooleanSetting("Заливка", true);
   private final ModeSetting e = new ModeSetting("Тип заливки", "Обычная", "Обычная", "Шейдер").a(() -> this.d.c());
   private final SliderSetting f = new SliderSetting("Прозрачность заливки", 0.3F, 0.05F, 1.0F, 0.05F).a(() -> this.d.c() && this.e.l("Обычная"));
   private final ModeSetting g = new ModeSetting("Эффект", "Туманность", "Туманность", "Звёзды", "Паутина", "Плазма").a(() -> this.d.c() && this.e.l("Шейдер"));
   private final SliderSetting h = new SliderSetting("Скорость эффекта", 1.0F, 0.1F, 3.0F, 0.1F).a(() -> this.d.c() && this.e.l("Шейдер"));
   private final SliderSetting i = new SliderSetting("Прозрачность эффекта", 1.0F, 0.1F, 1.0F, 0.05F).a(() -> this.d.c() && this.e.l("Шейдер"));
   private final ModeSetting j = new ModeSetting("Анимация", "Нет", "Нет", "Пульсация", "Волна");
   private final BooleanSetting k = new BooleanSetting("Скрыть ванильную обводку", true);
   private final BooleanSetting l = new BooleanSetting("Цвет из темы", true);
   private final ColorSetting n = new ColorSetting("Свой цвет", ColorUtil.a(255, 111, 181, 255)).a(() -> !this.l.c());

   @Generated
   public BooleanSetting q() {
      return this.k;
   }

   public BlockOverlay() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f, this.g, this.h, this.i, this.j, this.k, this.l, this.n});
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && aM_.field_1687 != null) {
         class_239 target = aM_.field_1765;
         if (target != null && target.method_17783() == class_240.field_1332) {
            class_2338 pos = ((class_3965)target).method_17777();
            class_2680 state = aM_.field_1687.method_8320(pos);
            if (!state.method_26215()) {
               class_238 shape = state.method_26218(aM_.field_1687, pos).method_1107().method_996(pos);
               int color = this.r();
               boolean shaderFill = this.d.c() && this.e.l("Шейдер");
               class_238 primary = this.j.l("Пульсация") ? a(shape, 1.0F + (float)((Math.sin(System.currentTimeMillis() / 260.0) + 1.0) * 0.5) * 0.08F) : shape;
               if (shaderFill) {
                  this.b(event.h().method_23760().method_23761(), primary);
               }

               if (this.d.c() && !shaderFill) {
                  event.e().a(event.h(), primary, ColorUtil.a(color, this.f.c()), 0.0F);
               }

               if (this.b.c() && !shaderFill) {
                  event.e().a(event.h(), primary, color, this.c.c());
               }

               if (this.j.l("Волна")) {
                  float phase = (float)(System.currentTimeMillis() % 1000L) / 1000.0F;
                  event.e()
                     .a(
                        event.h(),
                        a(shape, 1.0F + phase * 0.3F),
                        ColorUtil.a(color, (1.0F - phase) * ((color >>> 24 & 0xFF) / 255.0F)),
                        Math.max(1.0F, this.c.c() - 0.5F)
                     );
               }
            }
         }
      }
   }

   private int r() {
      return this.l.c() ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : this.n.c();
   }

   private static class_238 a(class_238 box, float scale) {
      class_243 center = box.method_1005();
      double halfX = (box.field_1320 - box.field_1323) * 0.5 * scale;
      double halfY = (box.field_1325 - box.field_1322) * 0.5 * scale;
      double halfZ = (box.field_1324 - box.field_1321) * 0.5 * scale;
      return new class_238(
         center.field_1352 - halfX,
         center.field_1351 - halfY,
         center.field_1350 - halfZ,
         center.field_1352 + halfX,
         center.field_1351 + halfY,
         center.field_1350 + halfZ
      );
   }

   private void b(Matrix4f matrix, class_238 box) {
      float x0 = (float)box.field_1323;
      float y0 = (float)box.field_1322;
      float z0 = (float)box.field_1321;
      float x1 = (float)box.field_1320;
      float y1 = (float)box.field_1325;
      float z1 = (float)box.field_1324;
      RenderSystem.enableBlend();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA);
      RenderSystem.setShader(class_10142.field_53876);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      this.a(buffer, matrix, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
      this.a(buffer, matrix, x0, y1, z0, x1, y1, z0, x1, y0, z0, x0, y0, z0);
      this.a(buffer, matrix, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0);
      this.a(buffer, matrix, x1, y1, z0, x1, y1, z1, x1, y0, z1, x1, y0, z0);
      this.a(buffer, matrix, x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0);
      this.a(buffer, matrix, x1, y0, z0, x1, y0, z1, x0, y0, z1, x0, y0, z0);
      class_286.method_43433(buffer.method_60800());
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private void a(
      class_287 buffer, Matrix4f matrix, float ax, float ay, float az, float bx, float by, float bz, float cx, float cy, float cz, float dx, float dy, float dz
   ) {
      class_243 camera = aM_.method_1561().field_4686.method_19326();
      float time = (float)(System.currentTimeMillis() % 100000L) / 1000.0F * this.h.c();
      float alpha = this.i.c();

      for (int u = 0; u < 12; u++) {
         float u0 = u / 12.0F;
         float u1 = (u + 1) / 12.0F;

         for (int v = 0; v < 12; v++) {
            float v0 = v / 12.0F;
            float v1 = (v + 1) / 12.0F;
            this.a(buffer, matrix, camera, ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, u0, v0, time, alpha);
            this.a(buffer, matrix, camera, ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, u1, v0, time, alpha);
            this.a(buffer, matrix, camera, ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, u1, v1, time, alpha);
            this.a(buffer, matrix, camera, ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, u0, v1, time, alpha);
         }
      }
   }

   private void a(
      class_287 buffer,
      Matrix4f matrix,
      class_243 camera,
      float ax,
      float ay,
      float az,
      float bx,
      float by,
      float bz,
      float cx,
      float cy,
      float cz,
      float dx,
      float dy,
      float dz,
      float u,
      float v,
      float time,
      float alpha
   ) {
      float x = a(ax, bx, cx, dx, u, v);
      float y = a(ay, by, cy, dy, u, v);
      float z = a(az, bz, cz, dz, u, v);
      Color color = this.a(x, y, z, time, alpha);
      buffer.method_22918(matrix, (float)(x - camera.field_1352), (float)(y - camera.field_1351), (float)(z - camera.field_1350))
         .method_1336(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   private static float a(float a, float b, float c, float d, float u, float v) {
      return a * (1.0F - u) * (1.0F - v) + b * u * (1.0F - v) + c * u * v + d * (1.0F - u) * v;
   }

   private Color a(float x, float y, float z, float time, float alpha) {
      if (this.g.l("Звёзды")) {
         return c(x, y, z, time, alpha);
      } else if (this.g.l("Паутина")) {
         return d(x, y, z, time, alpha);
      } else {
         return this.g.l("Плазма") ? e(x, y, z, time, alpha) : b(x, y, z, time, alpha);
      }
   }

   private static float a(float x, float y) {
      float n = (float)Math.sin(x * 12.9898F + y * 78.233F) * 43758.547F;
      return n - (float)Math.floor(n);
   }

   private static float b(float x, float y) {
      float ix = (float)Math.floor(x);
      float iy = (float)Math.floor(y);
      float fx = x - ix;
      float fy = y - iy;
      fx = fx * fx * (3.0F - 2.0F * fx);
      fy = fy * fy * (3.0F - 2.0F * fy);
      float a = a(ix, iy);
      float b = a(ix + 1.0F, iy);
      float c = a(ix, iy + 1.0F);
      float d = a(ix + 1.0F, iy + 1.0F);
      return a + (b - a) * fx + (c - a) * fy + (a - b - c + d) * fx * fy;
   }

   private static float a(float x, float y, int octaves) {
      float value = 0.0F;
      float amplitude = 0.5F;

      for (int i = 0; i < octaves; i++) {
         value += amplitude * b(x, y);
         x *= 2.0F;
         y *= 2.0F;
         amplitude *= 0.5F;
      }

      return value;
   }

   private static float a(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   private static float f(float value) {
      float wrapped = value % 1.0F;
      return wrapped < 0.0F ? wrapped + 1.0F : wrapped;
   }

   private static Color a(float r, float g, float b, float alpha) {
      return new Color(
         Math.round(a(r, 0.0F, 1.0F) * 255.0F),
         Math.round(a(g, 0.0F, 1.0F) * 255.0F),
         Math.round(a(b, 0.0F, 1.0F) * 255.0F),
         Math.round(a(alpha, 0.0F, 1.0F) * 255.0F)
      );
   }

   private static Color b(float x, float y, float z, float t, float alpha) {
      float u = x * 6.0F + z * 4.0F;
      float v = y * 5.0F + z * 3.0F;
      float nR = a(u * 0.8F + t * 0.4F, v * 0.9F + t * 0.15F, 4);
      float nG = a(u * 0.6F - t * 0.3F + 10.0F, v * 0.7F + t * 0.25F + 7.0F, 4);
      float nB = a(u * 0.7F + t * 0.2F + 20.0F, v * 0.8F - t * 0.35F + 15.0F, 4);
      float density = a(u * 0.5F + t * 0.18F + 30.0F, v * 0.5F - t * 0.12F + 25.0F, 5);
      density = a(density * 1.8F - 0.3F, 0.0F, 1.0F);
      float r = 0.25F + density * (nR * 0.35F + 0.1F);
      float g = 0.22F + density * (nG * 0.15F + 0.03F);
      float b = 0.28F + density * (nB * 0.4F + 0.15F);
      float bright = a((nR + nB - 0.8F) * 2.0F, 0.0F, 1.0F) * density;
      r += bright * 0.2F;
      g += bright * 0.15F;
      b += bright * 0.12F;
      float star = a(x * 47.3F + z * 31.7F, y * 53.1F);
      if (star > 0.97F) {
         float flicker = (float)(Math.sin(t * 5.0F + star * 150.0F) * 0.3F + 0.7F);
         float brightness = (star - 0.97F) / 0.03F * flicker * 0.4F;
         r = Math.min(1.0F, r + brightness);
         g = Math.min(1.0F, g + brightness * 0.85F);
         b = Math.min(1.0F, b + brightness);
      }

      return a(r, g, b, alpha);
   }

   private static Color c(float x, float y, float z, float t, float alpha) {
      float u = x * 5.0F + z * 4.0F;
      float v = y * 5.0F + z * 2.0F;
      float gradient = a(u * 0.4F + t * 0.08F, v * 0.4F - t * 0.05F, 4);
      float purpleAmount = a(gradient * 1.6F - 0.2F, 0.0F, 1.0F);
      float phase = (float)(Math.sin(t * (Math.PI / 5)) * 0.5 + 0.5);
      float purpleMix = purpleAmount * (0.4F + phase * 0.6F);
      float r = 0.02F + purpleMix * 0.28F;
      float g = 0.02F + purpleMix * 0.03F;
      float b = 0.04F + purpleMix * 0.35F;
      float hash1 = a(x * 31.7F + z * 17.3F, y * 23.1F);
      float hash2 = a(x * 53.1F - z * 41.9F, y * 67.3F);
      if (hash1 > 0.95F) {
         float brightness = (hash1 - 0.95F) / 0.05F;
         float twinkle = (float)(Math.sin(t * (2.0F + hash2 * 4.0F) + hash1 * 60.0F) * 0.4F + 0.6F);
         float star = brightness * twinkle;
         r = Math.min(1.0F, r + star * 0.9F);
         g = Math.min(1.0F, g + star * 0.85F);
         b = Math.min(1.0F, b + star * 0.7F);
      }

      for (int i = 0; i < 5; i++) {
         float seed = a(i * 73.1F, i * 37.9F);
         float shootX = a(i * 127.3F, seed * 99.1F);
         float shootZ = a(i * 83.7F, seed * 61.3F);
         float period = 3.0F + seed * 4.0F;
         float shootPhase = (t + seed * 50.0F) % period / period;
         float starU = shootX * 6.0F - 1.0F;
         float starV = (1.0F - shootPhase) * 6.0F - 1.0F + shootZ * 2.0F;
         float normU = u / 5.0F;
         float normV = v / 5.0F;
         float deltaU = normU - starU;
         float deltaV = normV - starV;
         float distance = (float)Math.sqrt(deltaU * deltaU + deltaV * deltaV);
         float glowSize = 0.3F + seed * 0.2F;
         float fadeOut = a(1.0F - (shootPhase - 0.7F) / 0.3F, 0.0F, 1.0F);
         if (distance < glowSize) {
            float intensity = 1.0F - distance / glowSize;
            intensity = intensity * intensity * (0.7F + shootPhase * 0.3F) * fadeOut;
            r = Math.min(1.0F, r + intensity * 0.95F);
            g = Math.min(1.0F, g + intensity * 0.5F);
            b = Math.min(1.0F, b + intensity * 0.1F);
         }

         float trailV = starV + 0.25F;
         float trailDeltaU = normU - starU;
         float trailDeltaV = normV - trailV;
         float trailDistance = (float)Math.sqrt(trailDeltaU * trailDeltaU + trailDeltaV * trailDeltaV);
         if (trailDistance < glowSize * 0.6F && trailDeltaV > 0.0F && trailDeltaV < 0.5F) {
            float intensity = (1.0F - trailDistance / (glowSize * 0.6F)) * 0.4F * fadeOut;
            r = Math.min(1.0F, r + intensity * 0.8F);
            g = Math.min(1.0F, g + intensity * 0.35F);
            b = Math.min(1.0F, b + intensity * 0.05F);
         }
      }

      return a(r, g, b, alpha);
   }

   private static Color d(float x, float y, float z, float t, float alpha) {
      float u = (x + z) * 3.0F;
      float v = y * 3.0F;
      float line1 = (float)Math.sin(u * 4.0F + t * 0.8F + (float)Math.sin(v * 2.5F + t * 0.3F) * 1.5F);
      float line2 = (float)Math.sin(v * 3.5F - t * 0.6F + (float)Math.sin(u * 2.0F - t * 0.4F) * 1.2F);
      float line3 = (float)Math.sin((u + v) * 2.5F + t * 0.5F + (float)Math.cos((u - v) * 1.8F + t * 0.2F) * 1.0F);
      float line4 = (float)Math.cos(u * 3.0F - v * 2.0F + t * 0.7F);
      float w1 = (float)Math.pow(Math.max(0.0, 1.0 - Math.abs(line1) * 1.8), 3.0);
      float w2 = (float)Math.pow(Math.max(0.0, 1.0 - Math.abs(line2) * 1.8), 3.0);
      float w3 = (float)Math.pow(Math.max(0.0, 1.0 - Math.abs(line3) * 2.0), 3.0);
      float w4 = (float)Math.pow(Math.max(0.0, 1.0 - Math.abs(line4) * 2.2), 3.0);
      float web = Math.min(1.0F, w1 + w2 + w3 + w4);
      float tone = 0.15F + web * 0.7F;
      float tint = (float)(Math.sin(t * 0.4F) * 0.5 + 0.5);
      return a(tone * (0.85F + tint * 0.15F), tone * (0.82F + tint * 0.08F), tone * (0.9F + (1.0F - tint) * 0.1F), alpha);
   }

   private static Color e(float x, float y, float z, float t, float alpha) {
      float u = (x + z) * 2.0F;
      float v = y * 2.0F;
      float p1 = (float)Math.sin(u * 1.5F + t * 0.6F);
      float p2 = (float)Math.sin(v * 1.8F + t * 0.5F);
      float p3 = (float)Math.sin((u + v) * 1.2F + t * 0.4F);
      float p4 = (float)Math.sin((float)Math.sqrt(u * u + v * v) * 2.0F - t * 0.7F);
      float p5 = (float)Math.sin(u * 2.5F - t * 0.3F) * (float)Math.cos(v * 1.5F + t * 0.2F);
      float plasma = (p1 + p2 + p3 + p4 + p5) / 5.0F;
      plasma = plasma * 0.5F + 0.5F;
      float hue = f(0.06F + plasma * 0.08F + (float)(Math.sin(t * 0.25F) * 0.5 + 0.5) * 0.04F);
      Color color = Color.getHSBColor(hue, 0.75F + plasma * 0.2F, 0.6F + plasma * 0.4F);
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(a(alpha, 0.0F, 1.0F) * 255.0F));
   }
}
