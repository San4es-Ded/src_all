package moscow.rockstar.mixin.minecraft.client.gui.screen;





import rockstar.client.rotation.*;
import rockstar.client.render.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.core.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.events.render.GameRendererEvent;
import rockstar.client.render.PostProcessRenderer;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.DrawCallCounter;
import rockstar.client.internal.rotation.ScreenProjectionUtils;
import rockstar.client.rotation.RotationManager;

@Mixin(value={GameRenderer.class})
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/state/GuiRenderState;clear()V", shift = At.Shift.AFTER))
    private void rockstar$beginLegacyGui(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        rockstar.client.render.compat.ImmediateRenderer.beginGuiOverlayCollection();
        rockstar.client.compat.RenderSystem.beginGuiFrame();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"))
    private void rockstar$endLegacyGui(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        UiBatchRenderer.internalMethod02576();
        rockstar.client.compat.RenderSystem.endGuiFrame();
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
            shift = At.Shift.AFTER
        )
    )
    private void rockstar$renderGuiItems(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        rockstar.client.internal.ui.RockstarGuiItemRenderer.render();
        if (rockstar.client.render.compat.ImmediateRenderer.hasDeferredGuiOverlays()) {
            rockstar.client.compat.RenderSystem.beginGuiFrame();
            try {
                rockstar.client.render.compat.ImmediateRenderer.extractDeferredGuiOverlays();
                UiBatchRenderer.internalMethod02576();
            } finally {
                rockstar.client.compat.RenderSystem.endGuiFrame();
            }
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void rockstar$retireProjectionBuffers(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        rockstar.client.render.compat.RenderAudit.endFrame();
        rockstar.client.compat.RenderSystem.finishFrame();
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;isFinishedLoading()Z", shift=At.Shift.AFTER)})
    public void triggerGameRendererEvent(RenderTickCounter renderTickCounter, boolean bl, CallbackInfo callbackInfo) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new GameRendererEvent());
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void rockstar$resetDrawCalls(RenderTickCounter renderTickCounter, boolean bl, CallbackInfo callbackInfo) {
        rockstar.client.render.compat.RenderAudit.beginFrame();
        DrawCallCounter.internalMethod00124();
    }

    @Inject(method={"renderWorld"}, at={@At(value="RETURN")})
    private void rockstar$applyWorldPatches(RenderTickCounter renderTickCounter, CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod01672(0);
    }

    @Inject(method={"renderWorld"}, at={@At(value="INVOKE_STRING", target="Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args={"ldc=hand"})})
    private void onRenderWorld(RenderTickCounter renderTickCounter, CallbackInfo callbackInfo, @Local(ordinal=0) Matrix4f projectionMatrix, @Local(ordinal=1) Matrix4f viewMatrix) {
        ScreenProjectionUtils.internalMethod00887(viewMatrix, projectionMatrix);
    }

    @Inject(method={"tiltViewWhenHurt"}, at={@At(value="HEAD")}, cancellable=true)
    private void tiltViewWhenHurtHook(MatrixStack matrixStack, float f, CallbackInfo callbackInfo) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod05278().isSelected()) {
            callbackInfo.cancel();
        }
    }

    @Redirect(method={"renderWorld"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
    private float renderWorldHook(float f, float f2, float f3) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod09519().isSelected()) {
            return 0.0f;
        }
        return MathHelper.lerp((float)f, (float)f2, (float)f3);
    }
}
