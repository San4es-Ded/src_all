package ru.haron.mixin;

import haron.cosmetic.CosmeticRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class})
public class CosmeticLivingEntityRendererMixin {
    @Unique
    private boolean haron$dropped;
    @Unique
    private BipedEntityModel<?> haron$trimmed;

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void haron$cosmeticModel(LivingEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        this.haron$dropped = false;
        this.haron$trimmed = null;
        if (state instanceof PlayerEntityRenderState) {
            PlayerEntityRenderState player = (PlayerEntityRenderState)state;
            if (CosmeticRenderer.shouldReplace(player)) {
                CosmeticRenderer.render(player, matrices, vertexConsumers, light);
                ci.cancel();
                return;
            }
            if (CosmeticRenderer.shouldSit(player)) {
                EntityModel entityModel;
                CosmeticRenderer.renderChair(player, matrices, vertexConsumers, light);
                player.hasVehicle = true;
                matrices.translate(0.0f, -CosmeticRenderer.sitDrop(), 0.0f);
                this.haron$dropped = true;
                if (CosmeticRenderer.trimSelf() && (entityModel = ((LivingEntityRenderer)(Object)this).getModel()) instanceof BipedEntityModel) {
                    BipedEntityModel biped = (BipedEntityModel)entityModel;
                    biped.head.visible = false;
                    biped.leftArm.visible = false;
                    biped.rightArm.visible = false;
                    this.haron$trimmed = biped;
                }
            }
        }
    }

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="RETURN")})
    private void haron$restore(LivingEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (this.haron$dropped) {
            matrices.translate(0.0f, CosmeticRenderer.sitDrop(), 0.0f);
            this.haron$dropped = false;
        }
        if (this.haron$trimmed != null) {
            this.haron$trimmed.head.visible = true;
            this.haron$trimmed.leftArm.visible = true;
            this.haron$trimmed.rightArm.visible = true;
            this.haron$trimmed = null;
        }
    }
}

