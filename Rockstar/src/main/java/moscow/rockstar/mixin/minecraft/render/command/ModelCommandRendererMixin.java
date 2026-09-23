package moscow.rockstar.mixin.minecraft.render.command;

import moscow.rockstar.mixin.accessors.BipedEntityModelAccessor;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.FriendMarkerEspFeature;
import rockstar.client.internal.game.EntityRenderStateAccessor;

/** Applies model-part changes after the deferred renderer has restored this command's pose. */
@Mixin(ModelCommandRenderer.class)
public abstract class ModelCommandRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/client/render/command/OrderedRenderCommandQueueImpl$ModelCommand;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/OutlineVertexConsumerProvider;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/model/Model;setAngles(Ljava/lang/Object;)V",
            shift = At.Shift.AFTER
        )
    )
    private <S> void rockstar$scaleFriendHead(
        OrderedRenderCommandQueueImpl.ModelCommand<S> command,
        RenderLayer renderLayer,
        VertexConsumer vertexConsumer,
        OutlineVertexConsumerProvider outlineVertexConsumers,
        VertexConsumerProvider.Immediate crumblingOverlayVertexConsumers,
        CallbackInfo callbackInfo
    ) {
        if (!(command.state() instanceof LivingEntityRenderState state)
            || !(command.model() instanceof BipedEntityModel<?> bipedModel)) {
            return;
        }

        Entity entity = ((EntityRenderStateAccessor)state).rockstar$getEntity();
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        FriendMarkerEspFeature feature = EspManager.internalMethod06726().internalMethod05464(FriendMarkerEspFeature.class);
        if (feature == null || !feature.internalMethod01517()) {
            return;
        }

        boolean friend = RockstarClient.getInstance().internalMethod03375().internalMethod00380(player.getName().getString());
        if (friend || FriendMarkerEspFeature.internalMethod08144()) {
            ((BipedEntityModelAccessor)(Object)bipedModel).rockstar$getHead().scale(new Vector3f(1.05F, 1.05F, 1.05F));
        }
    }
}
