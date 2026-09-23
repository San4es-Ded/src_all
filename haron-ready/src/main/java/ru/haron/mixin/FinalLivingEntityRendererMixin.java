package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.visuals.HitColor;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={LivingEntityRenderer.class})
public abstract class FinalLivingEntityRendererMixin<S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    @Shadow
    protected M model;
    @Unique
    private static VertexConsumerProvider currentVertexConsumers;
    @Unique
    private static boolean currentStateHurt;
    @Unique
    private static final Identifier HIT_COLOR_TEXTURE;

    @Inject(method={"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at={@At(value="HEAD")})
    private void captureState(S s, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        currentStateHurt = ((LivingEntityRenderState)s).hurt;
        currentVertexConsumers = VertexConsumerProviderVar;
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void clearState(S s, MatrixStack MatrixStackVar, VertexConsumerProvider VertexConsumerProviderVar, int i, CallbackInfo callbackInfo) {
        currentStateHurt = false;
        currentVertexConsumers = null;
    }

    @ModifyArg(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"), index=1)
    private VertexConsumer modifyVertexConsumer(VertexConsumer VertexConsumerVar) {
        HitColor hitColor = ModuleManager.HIT_COLOR;
        return !hitColor.o() || !currentStateHurt ? VertexConsumerVar : currentVertexConsumers.getBuffer(RenderLayer.getEntityTranslucent((Identifier)HIT_COLOR_TEXTURE));
    }

    @ModifyArg(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"), index=4)
    private int modifyColor(int i) {
        HitColor hitColor = ModuleManager.HIT_COLOR;
        if (!hitColor.o() || !currentStateHurt) {
            return i;
        }
        Color colorN = hitColor.n();
        return ColorUtils.a(colorN.getRed(), colorN.getGreen(), colorN.getBlue(), 255);
    }

    static {
        currentStateHurt = false;
        HIT_COLOR_TEXTURE = Identifier.of((String)"haron", (String)"textures/misc/white.png");
    }
}

