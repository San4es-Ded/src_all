package moscow.rockstar.mixin.minecraft.render.entity.feature;




import rockstar.client.ui.*;
import rockstar.client.esp.*;
import rockstar.client.internal.game.*;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.GlowRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.RenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import pyrock.utility.render.ColorRGBA;
import rockstar.modules.visual.AntiInvisibleModule;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.GlowEspFeature;
import rockstar.client.internal.game.EntityRenderStateAccessor;

@Mixin({FeatureRenderer.class})
public abstract class FeatureRendererMixin {
   @Unique
   private static final AntiInvisibleModule ANTI_INVISIBLE_MODULE = RockstarClient.getInstance().getModuleManager().getModule(AntiInvisibleModule.class);

   @WrapOperation(
      method = {"renderModel"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
      )}
   )
   private static void changeModelColor(
      RenderCommandQueue queue,
      Model<?> model,
      Object state,
      MatrixStack matrices,
      RenderLayer renderLayer,
      int light,
      int overlay,
      int color,
      Sprite sprite,
      int outlineColor,
      ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay,
      Operation<Void> original
   ) {
      LivingEntityRenderState localValue7 = (LivingEntityRenderState)state;
      if (ANTI_INVISIBLE_MODULE.isEnabled() && ANTI_INVISIBLE_MODULE.internalMethod06737(localValue7)) {
         Entity localValue8 = ((EntityRenderStateAccessor)localValue7).rockstar$getEntity();
         color = localValue8 instanceof ArmorStandEntity
            ? ThemeColors.internalField1312.withAlpha(0.0F).getRGB()
            : ThemeColors.internalField1312.withAlpha(ANTI_INVISIBLE_MODULE.internalMethod04278().internalMethod08576() / 100.0F * 255.0F).getRGB();
      }

      if (GlowEspFeature.internalField0277) {
         GlowEspFeature localValue11 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
         Entity localValue9 = ((EntityRenderStateAccessor)localValue7).rockstar$getEntity();
         if (localValue11 != null && localValue11.internalMethod06209(localValue9)) {
            ColorRGBA localValue10 = localValue11.internalMethod03008(localValue9);
            if (localValue10 != null) {
               color = localValue10.getRGB();
            }
         }
      }

      original.call(new Object[]{queue, model, state, matrices, renderLayer, light, overlay, color, sprite, outlineColor, crumblingOverlay});
   }

   @WrapOperation(
      method = {"renderModel"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/RenderLayers;entityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
      )}
   )
   private static RenderLayer changeModelRenderLayer(Identifier localValue0, Operation<RenderLayer> localValue1, @Local(argsOnly = true) LivingEntityRenderState localValue2) {
      if (ANTI_INVISIBLE_MODULE.isEnabled() && ANTI_INVISIBLE_MODULE.internalMethod06737(localValue2)) {
         return net.minecraft.client.render.RenderLayers.itemEntityTranslucentCull(localValue0);
      } else {
         if (GlowEspFeature.internalField0277) {
            GlowEspFeature localValue3 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
            Entity localValue4 = ((EntityRenderStateAccessor)localValue2).rockstar$getEntity();
            if (localValue3 != null && localValue3.internalMethod06209(localValue4) && localValue3.internalMethod03008(localValue4) != null) {
               return GlowRenderLayers.get(localValue0);
            }
         }

         return (RenderLayer)localValue1.call(new Object[]{localValue0});
      }
   }
}
