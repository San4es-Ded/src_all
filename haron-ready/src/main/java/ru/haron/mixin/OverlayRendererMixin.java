package ru.haron.mixin;

import haron.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InGameOverlayRenderer.class})
public class OverlayRendererMixin {
    @Inject(method={"renderFireOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onRenderFireOverlay(MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, CallbackInfo callbackInfo) {
        if (ModuleManager.RENDER_TWEAKS.p()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderUnderwaterOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private static void onRenderUnderwaterOverlay(MinecraftClient MinecraftClientVar, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, CallbackInfo callbackInfo) {
        if (ModuleManager.NO_FLUID.k()) {
            callbackInfo.cancel();
        }
    }
}

