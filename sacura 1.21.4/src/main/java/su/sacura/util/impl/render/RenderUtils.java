 package su.sacura.util.impl.render;
 
 import org.joml.Matrix4f;
 import su.sacura.util.impl.render.builders.states.QuadColorState;
 import su.sacura.util.impl.render.builders.states.QuadRadiusState;
 import su.sacura.util.impl.render.builders.states.SizeState;
 import su.sacura.util.impl.render.impl.BuiltBlur;
 import su.sacura.util.impl.render.impl.BuiltBorder;
 import su.sacura.util.impl.render.impl.BuiltLiquidGlass;
 import su.sacura.util.impl.render.impl.BuiltRectangle;
 import su.sacura.util.impl.render.impl.BuiltTexture;
 import su.sacura.util.type.MinecraftWrapper;
 
 public class RenderUtils implements MinecraftWrapper {
   public static void rect(Matrix4f matrix, float x, float y, float w, float h, float r, int c) {
     (new BuiltRectangle(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(c, c, c, c), 1.0F)).render(matrix, x, y, 0.0F);
   }
   
   public static void rect(Matrix4f matrix, float x, float y, float w, float h, float r, int c, float smoothness) {
     (new BuiltRectangle(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(c, c, c, c), smoothness)).render(matrix, x, y, 0.0F);
   }
   
   public static void rect(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, int c) {
     (new BuiltRectangle(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(c, c, c, c), 1.0F)).render(matrix, x, y, 0.0F);
   }
   
   public static void rect(Matrix4f matrix, float x, float y, float w, float h, float r, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltRectangle(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), 1.0F)).render(matrix, x, y, 0.0F);
   }
   
   public static void rect(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltRectangle(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), 1.0F)).render(matrix, x, y, 0.0F);
   }
   
   public static void border(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, float thickness, float internalSmoothness, float externalSmoothness, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltBorder(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), thickness, internalSmoothness, externalSmoothness)).render(matrix, x, y, 0.0F);
   }
   
   public static void border(Matrix4f matrix, float x, float y, float w, float h, float r, float thickness, float internalSmoothness, float externalSmoothness, int c) {
     (new BuiltBorder(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(c, c, c, c), thickness, internalSmoothness, externalSmoothness)).render(matrix, x, y, 0.0F);
   }
   
   public static void border(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, float thickness, float internalSmoothness, float externalSmoothness, int c) {
     (new BuiltBorder(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(c, c, c, c), thickness, internalSmoothness, externalSmoothness)).render(matrix, x, y, 0.0F);
   }
   
   public static void border(Matrix4f matrix, float x, float y, float w, float h, float r, float thickness, float internalSmoothness, float externalSmoothness, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltBorder(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), thickness, internalSmoothness, externalSmoothness)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, int textureId, int color) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(0.0F, 0.0F, 0.0F, 0.0F), new QuadColorState(color, color, color, color), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, int textureId, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(0.0F, 0.0F, 0.0F, 0.0F), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, float r, int textureId, int color) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(color, color, color, color), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, float r, int textureId, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, int textureId, int color) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(color, color, color, color), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, int textureId, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, int textureId, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft, float u, float v, float texWidth, float texHeight, float smoothness) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), smoothness, u, v, texWidth, texHeight, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void texture(Matrix4f matrix, float x, float y, float w, float h, float r, int textureId, float u, float v, float texWidth, float texHeight, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltTexture(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), 1.0F, u, v, texWidth, texHeight, textureId)).render(matrix, x, y, 0.0F);
   }
   
   public static void blur(Matrix4f matrix, float x, float y, float w, float h, float r, float blurRadius, int color) {
     (new BuiltBlur(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(color, color, color, color), 1.0F, blurRadius)).render(matrix, x, y, 0.0F);
   }
   
   public static void blur(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, float blurRadius, int color) {
     (new BuiltBlur(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(color, color, color, color), 1.0F, blurRadius)).render(matrix, x, y, 0.0F);
   }
   
   public static void blur(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, float blurRadius, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltBlur(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), 1.0F, blurRadius)).render(matrix, x, y, 0.0F);
   }
   
   public static void blur(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, float smoothness, float blurRadius, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft) {
     (new BuiltBlur(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), smoothness, blurRadius)).render(matrix, x, y, 0.0F);
   }
   
   public static void liquidGlass(Matrix4f matrix, float x, float y, float w, float h, float r, int c, float smoothness, float fresnelPower, float fresnelAlpha, float baseAlpha, boolean fresnelInvert, float fresnelMix, float distortStrength) {
     (new BuiltLiquidGlass(new SizeState(w, h), new QuadRadiusState(r, r, r, r), new QuadColorState(c, c, c, c), smoothness, fresnelPower, fresnelAlpha, baseAlpha, fresnelInvert, fresnelMix, distortStrength)).render(matrix, x, y, 0.0F);
   }
   
   public static void liquidGlass(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, int c, float smoothness, float fresnelPower, float fresnelAlpha, float baseAlpha, boolean fresnelInvert, float fresnelMix, float distortStrength) {
     (new BuiltLiquidGlass(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(c, c, c, c), smoothness, fresnelPower, fresnelAlpha, baseAlpha, fresnelInvert, fresnelMix, distortStrength)).render(matrix, x, y, 0.0F);
   }
   
   public static void liquidGlass(Matrix4f matrix, float x, float y, float w, float h, float rTopLeft, float rTopRight, float rBottomRight, float rBottomLeft, int cTopLeft, int cTopRight, int cBottomRight, int cBottomLeft, float smoothness, float fresnelPower, float fresnelAlpha, float baseAlpha, boolean fresnelInvert, float fresnelMix, float distortStrength) {
     (new BuiltLiquidGlass(new SizeState(w, h), new QuadRadiusState(rTopLeft, rTopRight, rBottomRight, rBottomLeft), new QuadColorState(cTopLeft, cTopRight, cBottomRight, cBottomLeft), smoothness, fresnelPower, fresnelAlpha, baseAlpha, fresnelInvert, fresnelMix, distortStrength)).render(matrix, x, y, 0.0F);
   }
 }


