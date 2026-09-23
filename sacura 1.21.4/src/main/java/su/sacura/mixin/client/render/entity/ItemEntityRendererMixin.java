package su.sacura.mixin.client.render.entity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.sacura.Sacura;
import su.sacura.features.modules.impl.render.ItemPhysicModule;
import su.sacura.util.type.MinecraftWrapper;

@Mixin({ItemEntityRenderer.class})
public abstract class ItemEntityRendererMixin implements MinecraftWrapper {
    @Unique private boolean isOnGround = false;
    @Unique private float entityAge = 0.0F;
    @Unique private float uniqueOffset = 0.0F;

    @Inject(method = {"updateRenderState"}, at = {@At("HEAD")})
    private void onUpdateRenderState(ItemEntity entity, ItemEntityRenderState state, float tickDelta, CallbackInfo ci) {
        this.isOnGround = entity.isOnGround();
        // Исправлено: field_53328 -> age
        this.entityAge = state.age;
        this.uniqueOffset = state.uniqueOffset;
    }

    @ModifyVariable(method = {"render"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderStack(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/ItemStackEntityRenderState;Lnet/minecraft/util/math/random/Random;)V"), ordinal = 0)
    private MatrixStack modifyMatrixStack(MatrixStack matrices, ItemEntityRenderState state, MatrixStack original, VertexConsumerProvider vertexConsumers, int light) {
        ItemPhysicModule itemPhysic = (ItemPhysicModule)Sacura.getInstance().getModuleManager().getModule(ItemPhysicModule.class);
        if (itemPhysic.enable) {
            matrices.pop();
            matrices.push();
            float rotation = ItemEntity.getRotation(this.entityAge, this.uniqueOffset);
            if (this.isOnGround) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            } else {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation * 300.0F));
            }
        }
        return matrices;
    }
}