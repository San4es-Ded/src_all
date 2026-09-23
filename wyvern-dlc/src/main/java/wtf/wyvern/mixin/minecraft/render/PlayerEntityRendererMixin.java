package wtf.wyvern.mixin.minecraft.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.client.modules.impl.render.EntityESP;
import wtf.wyvern.utility.interfaces.ILocalPlayerRenderState;

@Mixin({PlayerEntityRenderer.class})
public class PlayerEntityRendererMixin {
   @Inject(method = "updateRenderState", at = @At("TAIL"))
   private void wyvern$markLocalPlayer(AbstractClientPlayerEntity player, PlayerEntityRenderState state,
                                       float tickDelta, CallbackInfo ci) {
      ((ILocalPlayerRenderState) state).wyvern$setLocalPlayer(player == MinecraftClient.getInstance().player);
   }

   @Inject(
      method = {"renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void render(PlayerEntityRenderState playerEntityRenderState, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
       if (EntityESP.INSTANCE.isEnabled()) {
         ci.cancel();
      }

   }
}
