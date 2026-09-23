package wtf.wyvern.mixin.client.render;

import wtf.wyvern.core.eventbus.EventManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.wyvern.core.events.impl.entity.EventEntityColor;
import wtf.wyvern.client.modules.impl.render.HitEffect;
import wtf.wyvern.integration.figura.FiguraPreviewContext;
import wtf.wyvern.utility.interfaces.IMinecraft;

@Mixin({LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> implements IMinecraft {
   @Shadow
   @Nullable
   protected abstract RenderLayer method_24302(LivingEntityRenderState var1, boolean var2, boolean var3, boolean var4);

   @Redirect(
      method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;"
)
   )
   private RenderLayer renderHook(LivingEntityRenderer instance, LivingEntityRenderState state, boolean showBody, boolean translucent, boolean showOutline) {
      if (!translucent && state.width == 0.6F) {
         EventEntityColor event = new EventEntityColor(-1);
         EventManager.call(event);
         if (event.isCancelled()) {
            translucent = true;
         }
      }

      return this.method_24302(state, showBody, translucent, showOutline);
   }

   @WrapOperation(
      method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
)
   )
   private void renderModelHook(EntityModel<?> instance, MatrixStack matrixStack, VertexConsumer vertexConsumer,
                                int i, int j, int l,
                                Operation<Void> original,
                                @Local(ordinal = 0, argsOnly = true) LivingEntityRenderState renderState,
                                @Local(argsOnly = true) VertexConsumerProvider vertexConsumers) {
      // Figura's feature renderer runs after this vanilla base-model draw. During
      // Wyvern's catalog preview, suppress only the underlying player skin so the
      // selected Figura avatar remains fully visible without changing world renders.
      if (FiguraPreviewContext.current() != null) {
         return;
      }

      int color = l;
      if (renderState.invisibleToPlayer) {
         EventEntityColor event = new EventEntityColor(l);
         EventManager.call(event);
         color = event.getColor();
      }

      // Chams captures the posed cuboids of player models drawn in the world pass.
      boolean chamsCapture = instance instanceof PlayerEntityModel;
      if (chamsCapture) {
         wtf.wyvern.render.level.ChamsRenderer.beginModelCapture();
      }
      // With "hide model" on, the model still renders (so cuboids get captured)
      // but into a discarding consumer - only the chams boxes stay visible.
      if (wtf.wyvern.render.level.ChamsRenderer.shouldHideModel()) {
         vertexConsumer = wtf.wyvern.render.level.ChamsRenderer.noopConsumer();
      }

      try {
      float armAnimationProgress = HitEffect.getActiveArmAnimationProgress();
      if (armAnimationProgress >= 0.0F && instance instanceof PlayerEntityModel playerModel) {
         // setAngles has already prepared the vanilla pose. Add HitEffect's
         // temporary pose for this draw only, then restore the shared model.
         float rightPitch = playerModel.rightArm.pitch;
         float rightYaw = playerModel.rightArm.yaw;
         float rightRoll = playerModel.rightArm.roll;
         float leftPitch = playerModel.leftArm.pitch;
         float leftYaw = playerModel.leftArm.yaw;
         float leftRoll = playerModel.leftArm.roll;
         HitEffect.applyArmAnimation(playerModel, armAnimationProgress);
         try {
            original.call(instance, matrixStack, vertexConsumer, i, j, color);
         } finally {
            playerModel.rightArm.pitch = rightPitch;
            playerModel.rightArm.yaw = rightYaw;
            playerModel.rightArm.roll = rightRoll;
            playerModel.leftArm.pitch = leftPitch;
            playerModel.leftArm.yaw = leftYaw;
            playerModel.leftArm.roll = leftRoll;
         }
      } else {
         original.call(instance, matrixStack, vertexConsumer, i, j, color);
      }
      } finally {
         if (chamsCapture) {
            wtf.wyvern.render.level.ChamsRenderer.endModelCapture();
         }
      }

   }

   @WrapOperation(
      method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/EntityRenderState;FF)V"
)
   )
   private void wyvern$hideFeaturesForChams(net.minecraft.client.render.entity.feature.FeatureRenderer<?, ?> instance,
                                            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                                            net.minecraft.client.render.entity.state.EntityRenderState state,
                                            float limbAngle, float limbDistance, Operation<Void> original) {
      // Armor, held items and capes would float around an invisible body while
      // chams replaces the model - hide them together with the skin.
      if (wtf.wyvern.render.level.ChamsRenderer.shouldHideFeatures(state)) {
         return;
      }
      original.call(instance, matrices, vertexConsumers, light, state, limbAngle, limbDistance);
   }
}
