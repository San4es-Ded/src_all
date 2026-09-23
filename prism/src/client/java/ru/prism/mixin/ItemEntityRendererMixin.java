package ru.prism.mixin;

import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.prism.module.impl.render.ItemPhysics;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {

    @Redirect(
            require = 0,
            method = "render(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionfc;)V")
    )
    private void prism$itemPhysicsCancelSpin(MatrixStack matrices, Quaternionfc rotation) {
        ItemPhysics itemPhysics = ItemPhysics.getInstance();
        if (itemPhysics == null || !itemPhysics.isEnabled()) {
            matrices.multiply(rotation);
        }
    }
}
