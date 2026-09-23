package ru.haron.mixin;

import haron.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerEntityRenderer.class})
public abstract class PlayerEntityRendererMixin
extends EntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState> {
    protected PlayerEntityRendererMixin() {
        super((EntityRendererFactory.Context)null);
    }

    @Inject(method={"renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="HEAD")})
    private void onRenderLabel(PlayerEntityRenderState PlayerEntityRenderStateVar, Text TextVar, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        PlayerListEntry PlayerListEntryVarGetPlayerListEntry;
        MinecraftClient MinecraftClientVarGetInstance = MinecraftClient.getInstance();
        if (MinecraftClientVarGetInstance.player == null || PlayerEntityRenderStateVar.id != MinecraftClientVarGetInstance.player.getId() || !ModuleManager.SELF_NAMETAG.k() || MinecraftClientVarGetInstance.options.getPerspective().isFirstPerson() || PlayerEntityRenderStateVar.squaredDistanceToCamera >= 4096.0) {
            return;
        }
        MatrixStackVar.push();
        if (PlayerEntityRenderStateVar.playerName != null) {
            super.renderLabelIfPresent(PlayerEntityRenderStateVar, PlayerEntityRenderStateVar.playerName, MatrixStackVar, VertexConsumerProviderVar, i);
            MatrixStackVar.translate(0.0, (double)0.25875f, 0.0);
        }
        MutableText TextVarLiteral = MinecraftClientVarGetInstance.getNetworkHandler() == null || (PlayerListEntryVarGetPlayerListEntry = MinecraftClientVarGetInstance.getNetworkHandler().getPlayerListEntry(MinecraftClientVarGetInstance.player.getUuid())) == null || PlayerListEntryVarGetPlayerListEntry.getDisplayName() == null ? Text.literal((String)PlayerEntityRenderStateVar.name) : PlayerListEntryVarGetPlayerListEntry.getDisplayName().copy();
        super.renderLabelIfPresent(PlayerEntityRenderStateVar, TextVarLiteral, MatrixStackVar, VertexConsumerProviderVar, i);
        MatrixStackVar.pop();
    }
}
