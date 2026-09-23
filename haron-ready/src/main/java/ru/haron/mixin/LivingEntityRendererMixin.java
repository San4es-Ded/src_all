package ru.haron.mixin;

import haron.events.LivingEntityModelRenderEvent;
import haron.events.EventDispatcher;
import haron.module.ModuleManager;
import haron.modules.visuals.SelfNametag;
import haron.render.SelfNametagRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
extends EntityRenderer<T, S> {
    @Shadow
    protected M model;
    private static LivingEntity capturedEntity;

    protected LivingEntityRendererMixin() {
        super((EntityRendererFactory.Context)null);
    }

    @Inject(method={"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"}, at={@At(value="HEAD")})
    private void captureEntity(T t, S s, float f, CallbackInfo callbackInfo) {
        capturedEntity = t;
    }

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/LivingEntityRenderer;shouldRenderFeatures(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;)Z")})
    private void onRenderEntity(S s, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        EventDispatcher.EVENT_BUS.post((Object)new LivingEntityModelRenderEvent(capturedEntity, (LivingEntityRenderState)s, MatrixStackVar, (EntityModel<?>)this.model, VertexConsumerProviderVar));
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void renderSelfNametag(S s, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        PlayerListEntry entry;
        if (!(s instanceof PlayerEntityRenderState)) {
            return;
        }
        PlayerEntityRenderState playerState = (PlayerEntityRenderState)s;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        if (playerState.id != client.player.getId()) {
            return;
        }
        if (!ModuleManager.SELF_NAMETAG.k()) {
            return;
        }
        if (client.options.getPerspective().isFirstPerson()) {
            return;
        }
        if (playerState.squaredDistanceToCamera >= 4096.0) {
            return;
        }
        SelfNametag selfNametag = ModuleManager.SELF_NAMETAG;
        boolean showHP = selfNametag.showHP.a();
        MutableText nameLine = Text.literal((String)playerState.name);
        if (client.getNetworkHandler() != null && (entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid())) != null && entry.getDisplayName() != null) {
            nameLine = entry.getDisplayName().copy();
        }
        float hp = client.player.getHealth();
        int hpInt = Math.round(hp);
        MatrixStackVar.push();
        SelfNametagRenderState.isSelfNametagRender = true;
        float sneakOffset = client.player.isSneaking() ? 0.15f : 0.0f;
        MatrixStackVar.translate(0.0, 0.22 - (double)sneakOffset, 0.0);
        super.renderLabelIfPresent(s, (Text)nameLine, MatrixStackVar, VertexConsumerProviderVar, i);
        if (showHP) {
            MatrixStackVar.translate(0.0, -0.22, 0.0);
            MutableText hpLine = Text.literal((String)(hpInt + " Здоровье")).styled(style -> style.withColor(TextColor.fromFormatting((Formatting)Formatting.RED)));
            super.renderLabelIfPresent(s, (Text)hpLine, MatrixStackVar, VertexConsumerProviderVar, i);
        }
        SelfNametagRenderState.isSelfNametagRender = false;
        MatrixStackVar.pop();
    }
}
