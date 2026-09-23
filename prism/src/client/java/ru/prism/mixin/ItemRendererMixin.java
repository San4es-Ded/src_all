package ru.prism.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.module.impl.render.ItemPhysics;
import ru.prism.module.impl.utils.MaceHelper;
import ru.prism.utils.render.MaceHandConsumer;

import java.util.List;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Unique
    private static boolean prism$maceHandFirstPerson;

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V",
            at = @At("HEAD")
    )
    private static void prism$itemPhysicsGround(ItemDisplayContext context, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay, int[] tints, List<BakedQuad> quads, RenderLayer renderLayer, ItemRenderState.Glint glint, CallbackInfo ci) {
        prism$maceHandFirstPerson = context.isFirstPerson();

        if (context != ItemDisplayContext.GROUND) {
            return;
        }

        ItemPhysics itemPhysics = ItemPhysics.getInstance();
        if (itemPhysics == null || !itemPhysics.isEnabled()) {
            return;
        }

        matrices.translate(0.0F, 0.47F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V",
            at = @At("TAIL")
    )
    private static void prism$maceHandEnd(ItemDisplayContext context, MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay, int[] tints, List<BakedQuad> quads, RenderLayer renderLayer, ItemRenderState.Glint glint, CallbackInfo ci) {
        prism$maceHandFirstPerson = false;
    }

    @ModifyVariable(method = "renderBakedItemQuads", at = @At("HEAD"), argsOnly = true, ordinal = 0, index = 1)
    private static VertexConsumer prism$tintMaceHand(VertexConsumer original) {
        MaceHelper maceHelper = MaceHelper.get();
        if (maceHelper == null
                || !maceHelper.isEnabled()
                || !maceHelper.handHighlight.getValue()
                || !prism$maceHandFirstPerson
                || !MaceHelper.isHoldingMace()) {
            return original;
        }

        return new MaceHandConsumer(original, MaceHelper.getChargeColor());
    }
}
