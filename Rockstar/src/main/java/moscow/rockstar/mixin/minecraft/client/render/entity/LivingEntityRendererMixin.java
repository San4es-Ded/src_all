package moscow.rockstar.mixin.minecraft.client.render.entity;





import rockstar.client.ui.*;
import rockstar.client.rotation.*;
import rockstar.client.esp.*;
import rockstar.client.internal.game.*;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import moscow.rockstar.mixin.accessors.BipedEntityModelAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GlowRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.utility.render.ColorRGBA;
import rockstar.modules.player.FreeCameraModule;
import rockstar.modules.visual.AntiInvisibleModule;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.FriendMarkerEspFeature;
import rockstar.client.esp.GlowEspFeature;
import rockstar.client.internal.game.EntityRenderStateAccessor;
import rockstar.client.rotation.RotationManager;

@Mixin({LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
   private Entity currentEntity;
   private S currentRenderState;
   @Shadow
   protected EntityModel<?> model;

   @Shadow
   public abstract Identifier getTexture(LivingEntityRenderState localValue1);

   @ModifyExpressionValue(
      method = {"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;clampBodyYaw(Lnet/minecraft/entity/LivingEntity;FF)F"
      )}
   )
   public float changeYaw(float localValue1, LivingEntity localValue2) {
      if (!(localValue2 instanceof ClientPlayerEntity)) {
         return localValue1;
      } else {
         RotationManager localValue3 = RockstarClient.getInstance().internalMethod02368();
         float localValue4 = localValue3.internalMethod01525() ? localValue1 : localValue3.internalMethod08582().internalMethod00169();
         localValue3.internalMethod08209().internalMethod03239(localValue4);
         return localValue4;
      }
   }

   @ModifyExpressionValue(
      method = {"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"
      )}
   )
   public float changeHeadYaw(float localValue1, LivingEntity localValue2) {
      if (!(localValue2 instanceof ClientPlayerEntity)) {
         return localValue1;
      } else {
         RotationManager localValue3 = RockstarClient.getInstance().internalMethod02368();
         float localValue4 = localValue3.internalMethod01525() ? localValue1 : localValue3.internalMethod08582().internalMethod00169();
         localValue3.internalMethod08209().internalMethod03239(localValue4);
         return localValue4;
      }
   }

   @ModifyExpressionValue(
      method = {"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getLerpedPitch(F)F"
      )}
   )
   public float changePitch(float localValue1, LivingEntity localValue2) {
      if (!(localValue2 instanceof ClientPlayerEntity)) {
         return localValue1;
      } else {
         RotationManager localValue3 = RockstarClient.getInstance().internalMethod02368();
         float localValue4 = localValue3.internalMethod01525() ? localValue1 : localValue3.internalMethod08582().internalMethod00171();
         localValue3.internalMethod08209().internalMethod03289(localValue4);
         return localValue4;
      }
   }

   @ModifyArg(
      method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
      ),
      index = 6
   )
   private int rockstar$changeModelColor(int localValue6) {
      AntiInvisibleModule localValue9 = RockstarClient.getInstance().getModuleManager().getModule(AntiInvisibleModule.class);
      Entity localValue10 = this.currentEntity;
      if (localValue9.isEnabled() && localValue9.internalMethod06737(this.currentRenderState)) {
         localValue6 = localValue10 instanceof ArmorStandEntity
            ? ThemeColors.internalField1312.withAlpha(0.0F).getRGB()
            : ThemeColors.internalField1312.withAlpha(localValue9.internalMethod04278().internalMethod08576() / 100.0F * 255.0F).getRGB();
      }

      BeautifullyModule localValue11 = RockstarClient.getInstance().getModuleManager().getModule(BeautifullyModule.class);
      if (localValue11.isEnabled()
         && localValue11.internalMethod05320().isSelected()
         && !localValue11.internalMethod01947().internalMethod02884()
         && localValue10 == MinecraftClient.getInstance().player) {
         localValue6 = ColorRGBA.applyOpacity(localValue6, localValue11.internalMethod01947().internalMethod02881()).getRGB();
      }

      FreeCameraModule localValue12 = RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class);
      if (localValue12.internalMethod09314() && localValue10 == MinecraftClient.getInstance().player) {
         localValue6 = ColorRGBA.applyOpacity(localValue6, localValue12.internalMethod00300()).getRGB();
      }

      if (GlowEspFeature.internalField0277) {
         GlowEspFeature localValue13 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
         if (localValue13 != null && localValue13.internalMethod06209(localValue10)) {
            ColorRGBA localValue14 = localValue13.internalMethod03008(localValue10);
            if (localValue14 != null) {
               localValue6 = localValue14.getRGB();
            }
         }
      }

      return localValue6;
   }

   @ModifyReturnValue(
      method = {"getRenderLayer"},
      at = {@At("RETURN")}
   )
   private RenderLayer changeRenderLayer(RenderLayer localValue1, S localValue2, boolean localValue3, boolean localValue4, boolean localValue5) {
      AntiInvisibleModule localValue6 = RockstarClient.getInstance().getModuleManager().getModule(AntiInvisibleModule.class);
      if (localValue6.isEnabled() && !localValue3 && !localValue4 && !localValue5) {
         localValue2.invisible = false;
         return net.minecraft.client.render.RenderLayers.itemEntityTranslucentCull(this.getTexture(localValue2));
      } else {
         BeautifullyModule localValue7 = RockstarClient.getInstance().getModuleManager().getModule(BeautifullyModule.class);
         if (localValue7.isEnabled() && localValue7.internalMethod05320().isSelected() && !localValue7.internalMethod01947().internalMethod02884()) {
            Entity localValue8 = ((EntityRenderStateAccessor)localValue2).rockstar$getEntity();
            if (localValue8 == MinecraftClient.getInstance().player) {
               return net.minecraft.client.render.RenderLayers.itemEntityTranslucentCull(this.getTexture(localValue2));
            }
         }

         FreeCameraModule localValue10 = RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class);
         if (localValue10.internalMethod09314()) {
            Entity localValue9 = ((EntityRenderStateAccessor)localValue2).rockstar$getEntity();
            if (localValue9 == MinecraftClient.getInstance().player) {
               return net.minecraft.client.render.RenderLayers.itemEntityTranslucentCull(this.getTexture(localValue2));
            }
         }

         return localValue1;
      }
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"},
      at = {@At("HEAD")}
   )
   private void captureEntity(S localValue1, MatrixStack localValue2, OrderedRenderCommandQueue localValue3, CameraRenderState localValue4, CallbackInfo localValue5) {
      this.currentRenderState = localValue1;
      this.currentEntity = ((EntityRenderStateAccessor)localValue1).rockstar$getEntity();
      RockstarClient.internalField0410 = this.currentEntity;
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"},
      at = {@At("RETURN")}
   )
   private void releaseEntity(S localValue1, MatrixStack localValue2, OrderedRenderCommandQueue localValue3, CameraRenderState localValue4, CallbackInfo localValue5) {
      RockstarClient.internalField0410 = null;
      this.currentRenderState = null;
      this.currentEntity = null;
   }

   @ModifyReturnValue(
      method = {"getRenderLayer"},
      at = {@At("RETURN")}
   )
   private RenderLayer getRenderPlayer(RenderLayer localValue1, S localValue2, boolean localValue3, boolean localValue4, boolean localValue5) {
      GlowEspFeature localValue6 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
      Entity localValue7 = ((EntityRenderStateAccessor)localValue2).rockstar$getEntity();
      if (localValue6 != null && localValue6.internalMethod06209(localValue7) && GlowEspFeature.internalField0277) {
         ColorRGBA localValue8 = localValue6.internalMethod03008(localValue7);
         return localValue8 == null ? localValue1 : GlowRenderLayers.get(this.getTexture(localValue2));
      } else {
         return localValue1;
      }
   }

   @ModifyArg(
      method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
      ),
      index = 4
   )
   private int forceModelFullbright(int localValue1) {
      GlowEspFeature localValue2 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
      return localValue2 != null && localValue2.internalMethod06209(this.currentEntity) && GlowEspFeature.internalField0277 ? 15728880 : localValue1;
   }
}
