package moscow.rockstar.mixin.minecraft.render.entity;


import rockstar.client.internal.core.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.internal.core.SkinOverrideStore;

@Mixin(value={PlayerEntityRenderer.class})
public class PlayerEntityRendererMixin {
    @Inject(method={"updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V"}, at={@At(value="TAIL")})
    private void rockstar$forceCape(PlayerLikeEntity playerLikeEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo callbackInfo) {
        if (SkinOverrideStore.internalMethod00189()) {
            return;
        }
        if (playerLikeEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity
            && SkinOverrideStore.internalMethod05342(abstractClientPlayerEntity.getGameProfile().name())) {
            playerEntityRenderState.capeVisible = true;
        }
    }
}
