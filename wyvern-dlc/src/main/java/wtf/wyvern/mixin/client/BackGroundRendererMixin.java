package wtf.wyvern.mixin.client;

import wtf.wyvern.core.eventbus.EventManager;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.wyvern.core.events.impl.render.EventFog;
import wtf.wyvern.client.modules.impl.render.Ambience;
import wtf.wyvern.client.modules.impl.render.CustomFog;
import wtf.wyvern.client.modules.impl.render.NoRender;
import wtf.wyvern.render.display.base.color.ColorUtil;
import wtf.wyvern.render.display.base.color.ColorRGBA;

@Mixin({BackgroundRenderer.class})
public class BackGroundRendererMixin {
   @Inject(
      method = {"getFogModifier(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/client/render/BackgroundRenderer$StatusEffectFogModifier;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onGetFogModifier(Entity entity, float tickDelta, CallbackInfoReturnable<Object> info) {
      NoRender noRender = NoRender.INSTANCE;
      if (noRender.isRemoveBadEffect()) {
         info.setReturnValue((Object)null);
      }

   }

    @Inject(
      method = {"getFogColor"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void getFogColorHook(Camera camera, float tickDelta, ClientWorld world, int clampedViewDistance, float skyDarkness, CallbackInfoReturnable<Vector4f> cir) {
      CustomFog customFog = CustomFog.INSTANCE;
      if (!NoRender.INSTANCE.isRemoveFog() && customFog.shouldModifyFog(camera)) {
         ColorRGBA fogColor = customFog.fogColor.getColor();
         cir.setReturnValue(new Vector4f(
                 fogColor.getRed() / 255.0F,
                 fogColor.getGreen() / 255.0F,
                 fogColor.getBlue() / 255.0F,
                 fogColor.getAlpha() / 255.0F));
         return;
      }

      Ambience ambience = Ambience.INSTANCE;
      if (!NoRender.INSTANCE.isRemoveFog() && ambience.isNormalFog() && ambience.shouldModifyFog(camera)) {
         ColorRGBA fogColor = ambience.getThemeFogColor();
         cir.setReturnValue(new Vector4f(
                 fogColor.getRed() / 255.0F,
                 fogColor.getGreen() / 255.0F,
                 fogColor.getBlue() / 255.0F,
                 1.0F));
         return;
      }

      EventFog event = new EventFog();
      EventManager.call(event);
      if (event.isCancelled()) {
         int color = event.getColor();
         float alpha = ((color >> 24) & 0xFF) / 255.0f;
         if (alpha == 0.0f) alpha = 1.0f;
         cir.setReturnValue(new Vector4f(ColorUtil.redf(color), ColorUtil.greenf(color), ColorUtil.bluef(color), alpha));
      }

   }

   @Inject(
      method = {"applyFog"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void modifyFog(Camera camera, FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
      if (NoRender.INSTANCE.isRemoveFog()) {
         cir.setReturnValue(new Fog(viewDistance, viewDistance, FogShape.CYLINDER,
                 color.x, color.y, color.z, color.w));
         return;
      }
      CustomFog customFog = CustomFog.INSTANCE;
      if (fogType == FogType.FOG_TERRAIN && customFog.shouldModifyFog(camera)) {
         float first = Math.min(customFog.startDistance.getCurrent(), customFog.endDistance.getCurrent());
         float second = Math.max(customFog.startDistance.getCurrent(), customFog.endDistance.getCurrent());
         float start = MathHelper.clamp(first, -8.0F, viewDistance);
         float end = MathHelper.clamp(second, 0.0F, viewDistance);
         ColorRGBA fogColor = customFog.fogColor.getColor();
         cir.setReturnValue(new Fog(start, end, FogShape.SPHERE,
                 fogColor.getRed() / 255.0F,
                 fogColor.getGreen() / 255.0F,
                 fogColor.getBlue() / 255.0F,
                 fogColor.getAlpha() / 255.0F));
         return;
      }

      Ambience ambience = Ambience.INSTANCE;
      if (fogType == FogType.FOG_TERRAIN && ambience.isBlurFog() && ambience.shouldModifyFog(camera)) {
         cir.setReturnValue(new Fog(viewDistance, viewDistance, FogShape.SPHERE,
                 color.x, color.y, color.z, color.w));
         return;
      }
      if (fogType == FogType.FOG_TERRAIN && ambience.isNormalFog() && ambience.shouldModifyFog(camera)) {
         float first = Math.min(ambience.getFogStart(), ambience.getFogEnd());
         float second = Math.max(ambience.getFogStart(), ambience.getFogEnd());
         float start = MathHelper.clamp(first, -8.0F, viewDistance);
         float end = MathHelper.clamp(second, 0.0F, viewDistance);
         ColorRGBA fogColor = ambience.getThemeFogColor();
         cir.setReturnValue(new Fog(start, end, FogShape.SPHERE,
                 fogColor.getRed() / 255.0F,
                 fogColor.getGreen() / 255.0F,
                 fogColor.getBlue() / 255.0F,
                 1.0F));
         return;
      }
      EventFog event = new EventFog();
      EventManager.call(event);
      if (event.isCancelled()) {
         int color1 = event.getColor();
         float alpha = ((color1 >> 24) & 0xFF) / 255.0f;
         if (alpha <= 0.0f) alpha = 1.0f;

         cir.setReturnValue(new Fog(event.getStart(), event.getDistance(), FogShape.SPHERE,
            ((color1 >> 16) & 0xFF) / 255.0f,
            ((color1 >> 8) & 0xFF) / 255.0f,
            (color1 & 0xFF) / 255.0f,
            alpha));
      }

   }
}
