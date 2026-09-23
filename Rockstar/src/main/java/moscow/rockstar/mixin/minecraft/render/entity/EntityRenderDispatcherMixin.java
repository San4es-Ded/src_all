package moscow.rockstar.mixin.minecraft.render.entity;



import rockstar.client.esp.*;
import rockstar.client.internal.game.*;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderManager;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.RockstarClient;
import rockstar.client.esp.GlowEspFeature;
import rockstar.client.internal.game.EntityRenderStateAccessor;
import rockstar.client.internal.game.DynamicLightEngine;

@Environment(value=EnvType.CLIENT)
@Mixin(value={EntityRenderManager.class})
public abstract class EntityRenderDispatcherMixin {
    @WrapWithCondition(method={"render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitShadowPieces(Lnet/minecraft/client/util/math/MatrixStack;FLjava/util/List;)V")})
    private boolean rockstar$skipShadowDuringGlow(OrderedRenderCommandQueue queue, MatrixStack matrices, float shadowRadius, List<EntityRenderState.ShadowPiece> shadowPieces) {
        return !GlowEspFeature.internalField0277 && !GlowEspFeature.internalField0276;
    }

    @Inject(method={"getLight"}, at={@At(value="RETURN")}, cancellable=true)
    private void rockstar$applyDynamicLight(Entity entity, float f, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(DynamicLightEngine.internalMethod07614(BlockPos.ofFloored((Position)entity.getClientCameraPosVec(f)), callbackInfoReturnable.getReturnValueI()));
    }

    @WrapWithCondition(method={"render(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/client/render/state/CameraRenderState;DDDLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitFire(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lorg/joml/Quaternionf;)V")})
    private boolean rockstar$skipFireDuringF5(OrderedRenderCommandQueue queue, MatrixStack matrixStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {
        Entity entity;
        BeautifullyModule typedValue318 = RockstarClient.getInstance().getModuleManager().getModule(BeautifullyModule.class);
        return !typedValue318.isEnabled() || !typedValue318.internalMethod05320().isSelected() || typedValue318.internalMethod01947().internalMethod02884() || (entity = ((EntityRenderStateAccessor)entityRenderState).rockstar$getEntity()) != MinecraftClient.getInstance().player;
    }
}
