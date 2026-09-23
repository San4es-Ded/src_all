package moscow.rockstar.mixin.minecraft.render.entity.feature;


import rockstar.client.internal.game.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rockstar.modules.player.FreeCameraModule;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.game.EntityRenderStateAccessor;

@Mixin(value={HeldItemFeatureRenderer.class})
public abstract class HeldItemFeatureRendererMixin {
    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideDuringFade(MatrixStack matrixStack, OrderedRenderCommandQueue queue, int n, ArmedEntityRenderState armedEntityRenderState, float f, float f2, CallbackInfo callbackInfo) {
        Entity entity = ((EntityRenderStateAccessor)armedEntityRenderState).rockstar$getEntity();
        if (entity != MinecraftClient.getInstance().player) {
            return;
        }
        FreeCameraModule typedValue262 = RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class);
        if (typedValue262.internalMethod09314()) {
            callbackInfo.cancel();
            return;
        }
        BeautifullyModule typedValue318 = RockstarClient.getInstance().getModuleManager().getModule(BeautifullyModule.class);
        if (typedValue318.isEnabled() && typedValue318.internalMethod05320().isSelected() && !typedValue318.internalMethod01947().internalMethod02884()) {
            callbackInfo.cancel();
        }
    }
}
