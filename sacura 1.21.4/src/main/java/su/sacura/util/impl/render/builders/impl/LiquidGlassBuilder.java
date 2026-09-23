 package su.sacura.util.impl.render.builders.impl;
 
 import su.sacura.util.impl.render.builders.AbstractBuilder;
 import su.sacura.util.impl.render.builders.states.QuadColorState;
 import su.sacura.util.impl.render.builders.states.QuadRadiusState;
 import su.sacura.util.impl.render.builders.states.SizeState;
 import su.sacura.util.impl.render.impl.BuiltLiquidGlass;
 
 public final class LiquidGlassBuilder extends AbstractBuilder<BuiltLiquidGlass> {
   private SizeState size;
   
   private QuadRadiusState radius;
   
   private QuadColorState color;
   
   private float smoothness;
   
   private float fresnelPower;
   
   private float fresnelAlpha;
   
   private float baseAlpha;
   
   private boolean fresnelInvert;
   
   private float fresnelMix;
   
   private float distortStrength;
   
   public LiquidGlassBuilder size(SizeState size) {
     this.size = size;
     return this;
   }
   
   public LiquidGlassBuilder radius(QuadRadiusState radius) {
     this.radius = radius;
     return this;
   }
   
   public LiquidGlassBuilder color(QuadColorState color) {
     this.color = color;
     return this;
   }
   
   public LiquidGlassBuilder smoothness(float smoothness) {
     this.smoothness = smoothness;
     return this;
   }
   
   public LiquidGlassBuilder fresnelPower(float fresnelPower) {
     this.fresnelPower = fresnelPower;
     return this;
   }
   
   public LiquidGlassBuilder fresnelAlpha(float fresnelAlpha) {
     this.fresnelAlpha = fresnelAlpha;
     return this;
   }
   
   public LiquidGlassBuilder baseAlpha(float baseAlpha) {
     this.baseAlpha = baseAlpha;
     return this;
   }
   
   public LiquidGlassBuilder fresnelInvert(boolean fresnelInvert) {
     this.fresnelInvert = fresnelInvert;
     return this;
   }
   
   public LiquidGlassBuilder fresnelMix(float fresnelMix) {
     this.fresnelMix = fresnelMix;
     return this;
   }
   
   public LiquidGlassBuilder distortStrength(float distortStrength) {
     this.distortStrength = distortStrength;
     return this;
   }
   
   protected BuiltLiquidGlass _build() {
     return new BuiltLiquidGlass(this.size, this.radius, this.color, this.smoothness, this.fresnelPower, this.fresnelAlpha, this.baseAlpha, this.fresnelInvert, this.fresnelMix, this.distortStrength);
   }
   
   protected void reset() {
     this.size = SizeState.NONE;
     this.radius = QuadRadiusState.NO_ROUND;
     this.color = QuadColorState.WHITE;
     this.smoothness = 1.0F;
     this.fresnelPower = 0.0F;
     this.fresnelAlpha = 0.0F;
     this.baseAlpha = 0.0F;
     this.fresnelInvert = false;
     this.fresnelMix = 0.0F;
     this.distortStrength = 0.0F;
   }
 }


