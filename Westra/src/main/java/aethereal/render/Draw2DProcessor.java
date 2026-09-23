package aethereal.render;

import aethereal.api.Compile;
import aethereal.config.BaseProcessor;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.ui.shader.BlurShader;
import aethereal.ui.shader.GradientShader;
import aethereal.ui.shader.NoiseShader;
import aethereal.ui.shader.RectangleShader;
import aethereal.ui.shader.TextureShader;
import aethereal.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_276;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Draw2DProcessor extends BaseProcessor implements Interface {
   private float b = 1.0F;
   private final RectangleShader c = new RectangleShader();
   private final TextureShader d = new TextureShader();
   private final GradientShader e = new GradientShader();
   private final BlurShader f = new BlurShader();
   private final NoiseShader g = new NoiseShader();

   @Compile
   @Override
   public void setup() {
   }

   @Generated
   public void a(float scale) {
      this.b = scale;
   }

   @Generated
   public float a() {
      return this.b;
   }

   @Generated
   public RectangleShader b() {
      return this.c;
   }

   @Generated
   public TextureShader c() {
      return this.d;
   }

   @Generated
   public GradientShader d() {
      return this.e;
   }

   @Generated
   public BlurShader e() {
      return this.f;
   }

   @Generated
   public NoiseShader f() {
      return this.g;
   }

   @Override
   public void unSetup() {
   }

   public void a(class_4587 matrices, float x, float y, float width, float height, float radius, int color) {
      this.a(matrices, x, y, width, height, this.b(radius), color);
   }

   public void a(class_4587 matrices, float x, float y, float width, float height, Vector4f radius, int color) {
      float[] padding = MathUtil.b(0.8F);
      Matrix4f matrix = matrices.method_23760().method_23761();
      float drawX = x - padding[0] / 2.0F;
      float drawY = y - padding[1] / 2.0F;
      float drawWidth = width + padding[0];
      float drawHeight = height + padding[1];
      this.g();
      this.c.a();
      this.c.a(width, height);
      this.c.a(radius);
      this.c.a(0.8F);
      this.c.b(0.0F);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      this.a(buffer, matrix, drawX, drawY, drawWidth, drawHeight, color);
      class_286.method_43433(buffer.method_60800());
      this.h();
   }

   public void a(class_4587 matrices, float x, float y, float width, float height, float radius, float outlineWidth, int color) {
      this.a(matrices, x, y, width, height, this.b(radius), outlineWidth, color);
   }

   public void a(class_4587 matrices, float x, float y, float width, float height, Vector4f radius, float outlineWidth, int color) {
      float[] padding = MathUtil.b(0.8F);
      float halfOutlineWidth = outlineWidth * 0.5F;
      Matrix4f matrix = matrices.method_23760().method_23761();
      float drawX = x - halfOutlineWidth - padding[0] / 2.0F;
      float drawY = y - halfOutlineWidth - padding[1] / 2.0F;
      float drawWidth = width + outlineWidth + padding[0];
      float drawHeight = height + outlineWidth + padding[1];
      this.g();
      this.c.a();
      this.c.a(width + outlineWidth, height + outlineWidth);
      this.c.a(new Vector4f(radius.x + halfOutlineWidth, radius.y + halfOutlineWidth, radius.z + halfOutlineWidth, radius.w + halfOutlineWidth));
      this.c.a(0.8F);
      this.c.b(outlineWidth);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      this.a(buffer, matrix, drawX, drawY, drawWidth, drawHeight, color);
      class_286.method_43433(buffer.method_60800());
      this.h();
   }

   public void a(class_4587 matrices, class_2960 texture, float x, float y, float width, float height, float radius, int color) {
      this.a(matrices, x, y, width, height, radius, color, 0.0F, 0.0F, 1.0F, 1.0F, aM_.method_1531().method_4619(texture).method_4624());
   }

   public void a(
      class_4587 matrices,
      float x,
      float y,
      float width,
      float height,
      float radius,
      int color,
      float u,
      float v,
      float textureWidth,
      float textureHeight,
      int textureId
   ) {
      this.a(matrices, x, y, width, height, this.b(radius), color, u, v, textureWidth, textureHeight, textureId);
   }

   public void a(
      class_4587 matrices,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      int color,
      float u,
      float v,
      float textureWidth,
      float textureHeight,
      int textureId
   ) {
      float[] padding = MathUtil.b(0.8F);
      Matrix4f matrix = matrices.method_23760().method_23761();
      float drawX = x - padding[0] / 2.0F;
      float drawY = y - padding[1] / 2.0F;
      float drawWidth = width + padding[0];
      float drawHeight = height + padding[1];
      this.g();
      RenderSystem.setShaderTexture(0, textureId);
      this.d.a();
      this.d.a(width, height);
      this.d.a(radius);
      this.d.a(0.8F);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      this.a(buffer, matrix, drawX, drawY, drawWidth, drawHeight, u, v, textureWidth, textureHeight, color);
      class_286.method_43433(buffer.method_60800());
      this.h();
   }

   public void a(class_4587 matrices, class_2960 skin, class_1309 target, float x, float y, float width, float height, float radius, float alpha) {
      if (skin != null) {
         int color = ColorUtil.a(255, 255, 255, (int)(alpha * 255.0F));
         int textureId = aM_.method_1531().method_4619(skin).method_4624();
         this.a(matrices, x, y, width, height, radius, color, 0.125F, 0.125F, 0.125F, 0.125F, textureId);
         this.a(matrices, x, y, width, height, radius, color, 0.625F, 0.125F, 0.125F, 0.125F, textureId);
      }
   }

   public void a(
      class_4587 matrices,
      float x,
      float y,
      float width,
      float height,
      float radius,
      int topLeftColor,
      int topRightColor,
      int bottomLeftColor,
      int bottomRightColor
   ) {
      this.a(matrices, x, y, width, height, this.b(radius), topLeftColor, topRightColor, bottomLeftColor, bottomRightColor);
   }

   public void a(
      class_4587 matrices,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      int topLeftColor,
      int topRightColor,
      int bottomLeftColor,
      int bottomRightColor
   ) {
      float[] padding = MathUtil.b(1.0F);
      Matrix4f matrix = matrices.method_23760().method_23761();
      float[] normalizedTopLeft = ColorUtil.a(topLeftColor);
      float[] normalizedBottomLeft = ColorUtil.a(bottomLeftColor);
      float[] normalizedBottomRight = ColorUtil.a(bottomRightColor);
      float[] normalizedTopRight = ColorUtil.a(topRightColor);
      float drawX = x - padding[0] / 2.0F;
      float drawY = y - padding[1] / 2.0F;
      float drawWidth = width + padding[0];
      float drawHeight = height + padding[1];
      this.g();
      this.e.a();
      this.e.a(width, height);
      this.e.a(radius);
      this.e.a(1.0F);
      this.e.a(normalizedTopLeft[0], normalizedTopLeft[1], normalizedTopLeft[2], normalizedTopLeft[3]);
      this.e.b(normalizedBottomLeft[0], normalizedBottomLeft[1], normalizedBottomLeft[2], normalizedBottomLeft[3]);
      this.e.d(normalizedBottomRight[0], normalizedBottomRight[1], normalizedBottomRight[2], normalizedBottomRight[3]);
      this.e.c(normalizedTopRight[0], normalizedTopRight[1], normalizedTopRight[2], normalizedTopRight[3]);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);
      buffer.method_22918(matrix, drawX, drawY, 0.0F).method_39415(topLeftColor);
      buffer.method_22918(matrix, drawX, drawY + drawHeight, 0.0F).method_39415(bottomLeftColor);
      buffer.method_22918(matrix, drawX + drawWidth, drawY + drawHeight, 0.0F).method_39415(bottomRightColor);
      buffer.method_22918(matrix, drawX + drawWidth, drawY, 0.0F).method_39415(topRightColor);
      class_286.method_43433(buffer.method_60800());
      this.h();
   }

   public void b(class_4587 matrices, float x, float y, float width, float height, float radius, int color) {
      this.a(matrices, x, y, width, height, radius, color, 0.8F);
   }

   public void a(class_4587 matrices, float x, float y, float width, float height, float radius, int color, float mix) {
      this.a(matrices, x, y, width, height, this.b(radius), color, color, color, color, mix);
   }

   public void a(
      class_4587 matrices,
      float x,
      float y,
      float width,
      float height,
      Vector4f radius,
      int topLeftColor,
      int topRightColor,
      int bottomLeftColor,
      int bottomRightColor,
      float mix
   ) {
      if (!this.f.e().isEmpty()) {
         class_276 framebuffer = (class_276)this.f.e().getFirst();
         float uLeft = 0.0F;
         float uRight = 0.0F;
         float vTop = 0.0F;
         float vBottom = 0.0F;
         if (mix != 1.0F) {
            float scale = framebuffer.field_1482 / aM_.method_22683().method_4486();
            uLeft = x * scale / framebuffer.field_1482;
            uRight = (x + width) * scale / framebuffer.field_1482;
            vTop = 1.0F - y * scale / framebuffer.field_1481;
            vBottom = 1.0F - (y + height) * scale / framebuffer.field_1481;
         }

         float[] normalizedTopLeft = ColorUtil.a(topLeftColor);
         float[] normalizedBottomLeft = ColorUtil.a(bottomLeftColor);
         float[] normalizedBottomRight = ColorUtil.a(bottomRightColor);
         float[] normalizedTopRight = ColorUtil.a(topRightColor);
         Matrix4f matrix = matrices.method_23760().method_23761();
         float drawX = x - 0.6F;
         float drawY = y - 0.6F;
         float drawWidth = width + 1.2F;
         float drawHeight = height + 1.2F;
         this.g();
         RenderSystem.setShaderTexture(0, framebuffer.method_30277());
         this.f.a();
         this.f.a(width, height);
         this.f.a(radius);
         this.f.a(0.8F);
         this.f.b(mix);
         this.f.c((normalizedTopLeft[3] + normalizedBottomLeft[3] + normalizedBottomRight[3] + normalizedTopRight[3]) * 0.25F);
         this.f.a(normalizedTopLeft[0], normalizedTopLeft[1], normalizedTopLeft[2], normalizedTopLeft[3]);
         this.f.b(normalizedBottomLeft[0], normalizedBottomLeft[1], normalizedBottomLeft[2], normalizedBottomLeft[3]);
         this.f.d(normalizedBottomRight[0], normalizedBottomRight[1], normalizedBottomRight[2], normalizedBottomRight[3]);
         this.f.c(normalizedTopRight[0], normalizedTopRight[1], normalizedTopRight[2], normalizedTopRight[3]);
         class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
         buffer.method_22918(matrix, drawX, drawY, 0.0F).method_22913(uLeft, vTop).method_39415(topLeftColor);
         buffer.method_22918(matrix, drawX, drawY + drawHeight, 0.0F).method_22913(uLeft, vBottom).method_39415(bottomLeftColor);
         buffer.method_22918(matrix, drawX + drawWidth, drawY + drawHeight, 0.0F).method_22913(uRight, vBottom).method_39415(bottomRightColor);
         buffer.method_22918(matrix, drawX + drawWidth, drawY, 0.0F).method_22913(uRight, vTop).method_39415(topRightColor);
         class_286.method_43433(buffer.method_60800());
         this.h();
      }
   }

   public void a(class_4587 matrices, float x, float y, float width, float height, float radius, int color, float alpha, int glowColor, float glowRadius) {
      this.a(matrices, x, y, width, height, this.b(radius), color, alpha, glowColor, glowRadius);
   }

   public void a(class_4587 matrices, float x, float y, float width, float height, Vector4f radius, int color, float alpha, int glowColor, float glowRadius) {
      if (!this.f.e().isEmpty()) {
         float clampedGlowRadius = Math.max(glowRadius, 0.0F);
         float padding = 1.2F;
         class_276 framebuffer = (class_276)this.f.e().getFirst();
         float scale = framebuffer.field_1482 / aM_.method_22683().method_4486();
         float uLeft = x * scale / framebuffer.field_1482;
         float uRight = (x + width) * scale / framebuffer.field_1482;
         float vTop = 1.0F - y * scale / framebuffer.field_1481;
         float vBottom = 1.0F - (y + height) * scale / framebuffer.field_1481;
         float[] normalizedGlowColor = ColorUtil.a(glowColor);
         Matrix4f matrix = matrices.method_23760().method_23761();
         this.a(
            matrix,
            x - padding * 0.5F,
            y - padding * 0.5F,
            width + padding,
            height + padding,
            width,
            height,
            radius,
            alpha,
            0.8F,
            0.0F,
            null,
            uLeft,
            vTop,
            uRight,
            vBottom,
            framebuffer
         );
         float innerWidth = width - -2.2F;
         float innerHeight = height - -2.2F;
         Vector4f innerRadius = new Vector4f(
            Math.max(0.0F, radius.x - -1.1F), Math.max(0.0F, radius.y - -1.1F), Math.max(0.0F, radius.z - -1.1F), Math.max(0.0F, radius.w - -1.1F)
         );
         this.a(
            matrix,
            x - 1.1F - clampedGlowRadius,
            y - 1.1F - clampedGlowRadius,
            innerWidth + clampedGlowRadius * 2.0F,
            innerHeight + clampedGlowRadius * 2.0F,
            innerWidth,
            innerHeight,
            innerRadius,
            alpha,
            0.8F,
            clampedGlowRadius,
            normalizedGlowColor,
            uLeft,
            vTop,
            uRight,
            vBottom,
            framebuffer
         );
         this.a(matrices, x, y, width, height, radius, color);
      }
   }

   public void b(class_4587 matrices, float x, float y, float width, float height, float radius, int color, float alpha) {
      if (!this.f.e().isEmpty()) {
         float padding = 1.2F;
         class_276 framebuffer = (class_276)this.f.e().getFirst();
         float scale = framebuffer.field_1482 / aM_.method_22683().method_4486();
         float uLeft = x * scale / framebuffer.field_1482;
         float uRight = (x + width) * scale / framebuffer.field_1482;
         float vTop = 1.0F - y * scale / framebuffer.field_1481;
         float vBottom = 1.0F - (y + height) * scale / framebuffer.field_1481;
         this.a(
            matrices.method_23760().method_23761(),
            x - padding * 0.5F,
            y - padding * 0.5F,
            width + padding,
            height + padding,
            width,
            height,
            this.b(radius),
            alpha,
            0.8F,
            0.0F,
            null,
            uLeft,
            vTop,
            uRight,
            vBottom,
            framebuffer
         );
         this.a(matrices, x, y, width, height, radius, color);
      }
   }

   private void a(
      Matrix4f matrix,
      float drawX,
      float drawY,
      float drawWidth,
      float drawHeight,
      float width,
      float height,
      Vector4f radius,
      float alpha,
      float smoothness,
      float glowRadius,
      float[] glowColor,
      float uLeft,
      float vTop,
      float uRight,
      float vBottom,
      class_276 framebuffer
   ) {
      this.g();
      RenderSystem.setShaderTexture(0, framebuffer.method_30277());
      this.f.a();
      this.f.a(width, height);
      this.f.a(radius);
      this.f.c(alpha);
      this.f.d(glowRadius);
      if (glowColor != null) {
         this.f.e(glowColor[0], glowColor[1], glowColor[2], glowColor[3]);
      }

      this.f.a(smoothness);
      this.f.b(0.0F);
      this.f.a(1.0F, 1.0F, 1.0F, 1.0F);
      this.f.b(1.0F, 1.0F, 1.0F, 1.0F);
      this.f.d(1.0F, 1.0F, 1.0F, 1.0F);
      this.f.c(1.0F, 1.0F, 1.0F, 1.0F);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1575);
      buffer.method_22918(matrix, drawX, drawY, 0.0F).method_22913(uLeft, vTop).method_39415(-1);
      buffer.method_22918(matrix, drawX, drawY + drawHeight, 0.0F).method_22913(uLeft, vBottom).method_39415(-1);
      buffer.method_22918(matrix, drawX + drawWidth, drawY + drawHeight, 0.0F).method_22913(uRight, vBottom).method_39415(-1);
      buffer.method_22918(matrix, drawX + drawWidth, drawY, 0.0F).method_22913(uRight, vTop).method_39415(-1);
      class_286.method_43433(buffer.method_60800());
      this.h();
   }

   public void a(class_332 context, float x, float y, float width, float height, int color) {
      context.method_51448().method_22903();
      context.method_51448().method_46416(x, y, 0.0F);
      context.method_51448().method_22905(width, height, 1.0F);
      context.method_25294(0, 0, 1, 1, color);
      context.method_51448().method_22909();
   }

   private void g() {
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
   }

   private void h() {
      RenderSystem.setShaderTexture(0, 0);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private Vector4f b(float radius) {
      return new Vector4f(radius, radius, radius, radius);
   }

   private void a(class_287 buffer, Matrix4f matrix, float x, float y, float width, float height, int color) {
      buffer.method_22918(matrix, x, y, 0.0F).method_39415(color);
      buffer.method_22918(matrix, x, y + height, 0.0F).method_39415(color);
      buffer.method_22918(matrix, x + width, y + height, 0.0F).method_39415(color);
      buffer.method_22918(matrix, x + width, y, 0.0F).method_39415(color);
   }

   private void a(
      class_287 buffer, Matrix4f matrix, float x, float y, float width, float height, float u, float v, float textureWidth, float textureHeight, int color
   ) {
      buffer.method_22918(matrix, x, y, 0.0F).method_22913(u, v).method_39415(color);
      buffer.method_22918(matrix, x, y + height, 0.0F).method_22913(u, v + textureHeight).method_39415(color);
      buffer.method_22918(matrix, x + width, y + height, 0.0F).method_22913(u + textureWidth, v + textureHeight).method_39415(color);
      buffer.method_22918(matrix, x + width, y, 0.0F).method_22913(u + textureWidth, v).method_39415(color);
   }

   static {
      NativeMethodLookup.lookup(Draw2DProcessor.class, 28);
   }
}
